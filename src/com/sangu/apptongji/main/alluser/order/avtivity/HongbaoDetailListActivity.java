package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.components.Legend;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.SettingSelfActivity;
import com.sangu.apptongji.main.adapter.HbDetailListAdapter;
import com.sangu.apptongji.main.alluser.entity.MerDetail;
import com.sangu.apptongji.main.alluser.presenter.IMerDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.MerDetailPresenter;
import com.sangu.apptongji.main.alluser.view.IMerDetailView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.youzan.zancharts.ChartItem;
import com.youzan.zancharts.Line;
import com.youzan.zancharts.ZanLineChart;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-04-27.
 */

public class HongbaoDetailListActivity extends BaseActivity implements IMerDetailView,View.OnClickListener{
    private IMerDetailPresenter presenter=null;
    private XRecyclerView mRecyclerView=null;
    private List<MerDetail> details=null;
    private RadioButton radioShouru=null;
    private RadioButton radioZhichu=null;
    HbDetailListAdapter adapter = null;
    private RelativeLayout ll_shxtu;
    private ZanLineChart mLineChart;
    private TextView tv_shezhi = null;
    private TextView tv_title = null;
    private TextView tv_back = null;
    private TextView tv1,tv2,tv_tj_zhufa_count,tv_tj_liulan_count;
    private CustomProgressDialog mProgress=null;
    private boolean isNext = false;
    private String type,shareRed,friendsNumber,biaoshi,managerId;
    private int currentPage = 1;
    int i=0;
    private boolean newsFinish = false;
    private boolean escaFinish = false;
    private boolean isshuaxin = true;
    private Line newFanLine;
    private Line escapedFanLine;
    private ArrayList<String> timestep=new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (newsFinish&&escaFinish) {
                mLineChart.clear();
                List<Integer> color = new ArrayList<>();
                int color1 = Color.rgb(196,196,196);
                color.add(color1);
                final Legend legend = mLineChart.getLegend();
                legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
                legend.setTextSize(10f);
                legend.setComputedColors(color);
                legend.setTextColor(Color.rgb(196,196,196));
                legend.setFormSize(16f);
                mLineChart.setDescription(null);
                mLineChart.setDescriptionTextSize(0f);
                mLineChart.setHighlightEnabled(true);
                mLineChart.getXAxis().setLabelCount(7, true);
                mLineChart.setExtraOffsets(0, 15, 0, 0);
                mLineChart.addLines(new ArrayList<Line>() {
                    {
                        add(newFanLine);
                        add(escapedFanLine);
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_hongbaodetail_list);
        int j = i+7;
        for (i=0;i<j;i++){
            String t1 = getStatetime(i);
            timestep.add(t1);
        }
        WeakReference<HongbaoDetailListActivity> reference =  new WeakReference<HongbaoDetailListActivity>(HongbaoDetailListActivity.this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        presenter = new MerDetailPresenter(this,reference.get());
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        managerId = this.getIntent().getStringExtra("managerId");
        shareRed = this.getIntent().getStringExtra("shareRed");
        friendsNumber = this.getIntent().getStringExtra("friendsNumber");
        ll_shxtu = (RelativeLayout) findViewById(R.id.ll_shxtu);
        mLineChart = (ZanLineChart) findViewById(R.id.line_chart);
        assert mLineChart != null;
        tv_shezhi = (TextView) findViewById(R.id.tv_shezhi);
        radioZhichu = (RadioButton) findViewById(R.id.radioZhichu);
        radioShouru = (RadioButton) findViewById(R.id.radioShouru);
        mRecyclerView = (XRecyclerView) findViewById(R.id.list);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv_tj_zhufa_count = (TextView) findViewById(R.id.tv_tj_zhufa_count);
        tv_tj_liulan_count = (TextView) findViewById(R.id.tv_tj_liulan_count);
        radioZhichu.setOnClickListener(this);
        radioShouru.setOnClickListener(this);
        tv_shezhi.setOnClickListener(this);
        type = "收入";
        ll_shxtu.setVisibility(View.GONE);
        initlayout();
        tv_back.setText("个人详情");
        tv_title.setText("转发红包");
        details = new ArrayList<>();
        String id;
        if ("00".equals(biaoshi)){
            id = DemoHelper.getInstance().getCurrentUsernName();
        }else {
            id = DemoApplication.getInstance().getCurrentQiYeId();
        }
        presenter.loadMerDetail("1",id,"红包",type,timestep.get(6),timestep.get(0));
        mRecyclerView.refresh(false);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isshuaxin = false;
                String id;
                if ("00".equals(biaoshi)){
                    id = DemoHelper.getInstance().getCurrentUsernName();
                }else {
                    id = DemoApplication.getInstance().getCurrentQiYeId();
                }
                timestep.clear();
                int j = i+7;
                for (int a=i;a<j;a++){
                    String t1 = getStatetime(a);
                    timestep.add(t1);
                }
                i=i+7;
                getLiulancishu();
                presenter.loadMerDetail(currentPage+"",id,"红包",type,timestep.get(6),timestep.get(0));
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Long.parseLong(timestep.get(0))<Long.parseLong(getStatetime(0))) {
                    isshuaxin = false;
                    String id;
                    if ("00".equals(biaoshi)){
                        id = DemoHelper.getInstance().getCurrentUsernName();
                    }else {
                        id = DemoApplication.getInstance().getCurrentQiYeId();
                    }
                    timestep.clear();
                    int j = i - 14;
                    int j2 = i - 7;
                    for (int a = j; a < j2; a++) {
                        String t1 = getStatetime(a);
                        timestep.add(t1);
                    }
                    i = i - 7;
                    getLiulancishu();
                    presenter.loadMerDetail(currentPage+"",id,"红包",type,timestep.get(6),timestep.get(0));
                }
            }
        });
    }

    public String getStatetime(int i){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, - i);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initlayout() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isshuaxin = true;
                currentPage = 1;
                isNext = false;
                String id;
                if ("00".equals(biaoshi)){
                    id = DemoHelper.getInstance().getCurrentUsernName();
                }else {
                    id = DemoApplication.getInstance().getCurrentQiYeId();
                }
                presenter.loadMerDetail(currentPage+"",id,"红包",type,timestep.get(6),timestep.get(0));
            }

            @Override
            public void onLoadMore() {
                isshuaxin = true;
                currentPage = currentPage + 1;
                isNext = true;
                String id;
                if ("00".equals(biaoshi)){
                    id = DemoHelper.getInstance().getCurrentUsernName();
                }else {
                    id = DemoApplication.getInstance().getCurrentQiYeId();
                }
                presenter.loadMerDetail(currentPage+"",id,"红包",type,timestep.get(6),timestep.get(0));
            }
        });
        mRecyclerView.refresh(false);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void updateUserList(final List<MerDetail> merDetails,final org.json.JSONArray array,final String size,String income,String expenditure, boolean hasMore) {
        if ("支出".equals(type)) {
            escapedFanLine = new Line();
            fanLines2(array);
            tv_tj_zhufa_count.setText("转发"+size+"次");
        }
        if (isshuaxin){
            if (expenditure==null||"".equals(expenditure)){
                expenditure="0.00";
            }
            if (income==null||"".equals(income)){
                income="0.00";
            }
            double jine = Double.parseDouble(income);
            income = String.format("%.2f", jine);
            radioShouru.setText("收入  ("+income+")");
            double jine2 = Double.parseDouble(expenditure);
            expenditure = String.format("%.2f", jine2);
            radioZhichu.setText("支出  ("+expenditure+")");
            if (isNext) {
                details.addAll(merDetails);
                mRecyclerView.loadMoreComplete();
                adapter.notifyDataSetChanged();
                if (!hasMore){
                    mRecyclerView.setNoMore(true);
                }
            } else {
                this.details = merDetails;
                adapter = new HbDetailListAdapter(details, HongbaoDetailListActivity.this);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.refreshComplete();
                if (!hasMore) {
                    mRecyclerView.setNoMore(true);
                }
                adapter.setOnItemClickListener(new HbDetailListAdapter.MyItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MerDetail merDetail = details.get(position - 1);
                        startActivity(new Intent(HongbaoDetailListActivity.this, MerDetailActivity.class).putExtra("merDetail", merDetail));
                    }
                });
            }
        }
    }

    @Override
    public void showLoading() {
        if (mProgress!=null&&!mProgress.isShowing()) {
            mProgress.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mProgress!=null&&mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void showError() {
        if (mProgress!=null&&mProgress.isShowing()) {
            mProgress.dismiss();
        }
        Toast.makeText(HongbaoDetailListActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioShouru:
                if (mProgress!=null){
                    if (!mProgress.isShowing()){
                        mProgress.show();
                    }
                }else {
                    mProgress = CustomProgressDialog.createDialog(this);
                    mProgress.setMessage("正在加载请求列表...");
                    mProgress.show();
                    mProgress.setCancelable(true);
                    mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mProgress.dismiss();
                        }
                    });
                }
                ll_shxtu.setVisibility(View.GONE);
                radioShouru.setChecked(true);
                radioZhichu.setChecked(false);
                type="收入";
                isNext = false;
                isshuaxin = true;
                String id;
                if ("00".equals(biaoshi)){
                    id = DemoHelper.getInstance().getCurrentUsernName();
                }else {
                    id = DemoApplication.getInstance().getCurrentQiYeId();
                }
                presenter.loadMerDetail("1",id,"红包",type,null,null);
                break;
            case R.id.radioZhichu:
                if (mProgress!=null){
                    if (!mProgress.isShowing()){
                        mProgress.show();
                    }
                }else {
                    mProgress = CustomProgressDialog.createDialog(this);
                    mProgress.setMessage("正在加载请求列表...");
                    mProgress.show();
                    mProgress.setCancelable(true);
                    mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mProgress.dismiss();
                        }
                    });
                }
                ll_shxtu.setVisibility(View.VISIBLE);
                getLiulancishu();
                radioShouru.setChecked(false);
                radioZhichu.setChecked(true);
                type="支出";
                isNext = false;
                String id2;
                if ("00".equals(biaoshi)){
                    id2 = DemoHelper.getInstance().getCurrentUsernName();
                }else {
                    id2 = DemoApplication.getInstance().getCurrentQiYeId();
                }
                presenter.loadMerDetail("1",id2,"红包",type,timestep.get(6),timestep.get(0));
                break;
            case R.id.tv_shezhi:
                startActivity(new Intent(HongbaoDetailListActivity.this, SettingSelfActivity.class).putExtra("friendsNumber",friendsNumber)
                        .putExtra("shareRed",shareRed).putExtra("managerId",managerId).putExtra("biaoshi",biaoshi));
                break;
        }
    }

    private void getLiulancishu() {
        String url = FXConstant.URL_SearchZheXian_LiuLanList;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                int size = object.getIntValue("size");
                JSONArray array = object.getJSONArray("list");
                tv_tj_liulan_count.setText("浏览"+size+"次");
                newFanLine = new Line();
                fanLines1(array);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                param.put("beginDate",timestep.get(6));
                param.put("endDate",timestep.get(0));
                return param;
            }
        };
        MySingleton.getInstance(HongbaoDetailListActivity.this).addToRequestQueue(request);
    }

    public void fanLines1(JSONArray array) {
        int t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0, t6 = 0, t7 = 0;
        for (int j = 0; j < array.size(); j++) {
            JSONObject object = array.getJSONObject(j);
            String date = object.getString("date");
            int times = object.getIntValue("times");
            if (date.equals(timestep.get(0))) {
                t1 = times;
            } else if (date.equals(timestep.get(1))) {
                t2 = times;
            } else if (date.equals(timestep.get(2))) {
                t3 = times;
            } else if (date.equals(timestep.get(3))) {
                t4 = times;
            } else if (date.equals(timestep.get(4))) {
                t5 = times;
            } else if (date.equals(timestep.get(5))) {
                t6 = times;
            } else if (date.equals(timestep.get(6))) {
                t7 = times;
            }
        }
        final int finalT1 = t1;
        final int finalT2 = t2;
        final int finalT3 = t3;
        final int finalT4 = t4;
        final int finalT5 = t5;
        final int finalT6 = t6;
        final int finalT7 = t7;
        newFanLine.color = Color.rgb(62,197,255);
        newFanLine.label = "浏览";
        newFanLine.items = new ArrayList<ChartItem>() {
            {
                add(new ChartItem(timestep.get(6), timestep.get(6).substring(6, 8), finalT7 + ""));
                add(new ChartItem(timestep.get(5), timestep.get(5).substring(6, 8), finalT6 + ""));
                add(new ChartItem(timestep.get(4), timestep.get(4).substring(6, 8), finalT5 + ""));
                add(new ChartItem(timestep.get(3), timestep.get(3).substring(6, 8), finalT4 + ""));
                add(new ChartItem(timestep.get(2), timestep.get(2).substring(6, 8), finalT3 + ""));
                add(new ChartItem(timestep.get(1), timestep.get(1).substring(6, 8), finalT2 + ""));
                add(new ChartItem(timestep.get(0), timestep.get(0).substring(6, 8), finalT1 + ""));
            }
        };
        newsFinish = true;
        handler.sendEmptyMessage(0);
    }
    public void fanLines2(org.json.JSONArray array) {
        int t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0, t6 = 0, t7 = 0;
        if (array!=null&&!"".equals(array)&&!array.equals("null")) {
            for (int j = 0; j < array.length(); j++) {
                org.json.JSONObject object = array.optJSONObject(j);
                String date = object.isNull("date")?"":object.optString("date");
                int times = object.isNull("times")?0:object.optInt("times");
                if (date.equals(timestep.get(0))) {
                    t1 = times;
                } else if (date.equals(timestep.get(1))) {
                    t2 = times;
                } else if (date.equals(timestep.get(2))) {
                    t3 = times;
                } else if (date.equals(timestep.get(3))) {
                    t4 = times;
                } else if (date.equals(timestep.get(4))) {
                    t5 = times;
                } else if (date.equals(timestep.get(5))) {
                    t6 = times;
                } else if (date.equals(timestep.get(6))) {
                    t7 = times;
                }
            }
        }else {
            t1 = 0; t2 = 0; t3 = 0; t4 = 0; t5 = 0; t6 = 0; t7 = 0;
        }
        final int finalT1 = t1;
        final int finalT2 = t2;
        final int finalT3 = t3;
        final int finalT4 = t4;
        final int finalT5 = t5;
        final int finalT6 = t6;
        final int finalT7 = t7;
        escapedFanLine.color = Color.RED;
        escapedFanLine.label = "转发";
        escapedFanLine.items = new ArrayList<ChartItem>() {
            {
                add(new ChartItem(timestep.get(6), timestep.get(6).substring(6, 8), finalT7 + ""));
                add(new ChartItem(timestep.get(5), timestep.get(5).substring(6, 8), finalT6 + ""));
                add(new ChartItem(timestep.get(4), timestep.get(4).substring(6, 8), finalT5 + ""));
                add(new ChartItem(timestep.get(3), timestep.get(3).substring(6, 8), finalT4 + ""));
                add(new ChartItem(timestep.get(2), timestep.get(2).substring(6, 8), finalT3 + ""));
                add(new ChartItem(timestep.get(1), timestep.get(1).substring(6, 8), finalT2 + ""));
                add(new ChartItem(timestep.get(0), timestep.get(0).substring(6, 8), finalT1 + ""));
            }
        };
        escaFinish = true;
        handler.sendEmptyMessage(1);
    }

}
