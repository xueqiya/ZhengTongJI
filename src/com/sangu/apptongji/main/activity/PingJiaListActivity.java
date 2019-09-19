package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.PingjiaAdapter;
import com.sangu.apptongji.main.db.UserEvaluationList;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-30.
 */

public class PingJiaListActivity extends BaseActivity implements View.OnClickListener{
    TextView tv_title=null;
    TextView tv_pingjia=null;
    Button btn_tianjia = null;
    XRecyclerView mRecyclerView=null;
    PingjiaAdapter adapter=null;
    String userId=null,type,biaoshi;
    RadioButton radiohp=null;
    RadioButton radiozhp=null;
    RadioButton radiochp=null;
    List<UserEvaluationList> datas = new ArrayList<>();
    private boolean isNext = false;
    private int currentPage=0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.pingjialist_activity);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        userId = this.getIntent().getStringExtra("userId");
        type = getIntent().hasExtra("type")?getIntent().getStringExtra("type"):"2";
        initView();
        queryPingjiaList(type);
        tv_pingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflaterDl = LayoutInflater.from(PingJiaListActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                final Dialog dialog = new AlertDialog.Builder(PingJiaListActivity.this,R.style.Dialog).create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                btnOK.setText("确定");
                btnCancel.setText("取消");
                title_tv.setText("对该用户进行过下单并且完成交易才能对他进行评价，每次交易对应一次评价机会，满足条件请点击确认进行评价");
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        quertPingjiaZige();
                    }
                });
            }
        });
        adapter = new PingjiaAdapter(PingJiaListActivity.this,datas);
        mRecyclerView.setAdapter(adapter);
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
                isNext = false;
                currentPage=1;
                queryPingjiaList(type);
            }

            @Override
            public void onLoadMore() {
                isNext = true;
                currentPage++;
                queryPingjiaList(type);
            }
        });
        mRecyclerView.refresh(false);
        if ("0".equals(type)){
            radiohp.setChecked(false);
            radiochp.setChecked(true);
            radiozhp.setChecked(false);
        }else if ("1".equals(type)){
            radiohp.setChecked(false);
            radiochp.setChecked(false);
            radiozhp.setChecked(true);
        }else {
            radiohp.setChecked(true);
            radiochp.setChecked(false);
            radiozhp.setChecked(false);
        }
        if ("00".equals(biaoshi)){
            tv_pingjia.setVisibility(View.INVISIBLE);
        }
    }

    private void queryPingjiaList(final String type) {
        String url = FXConstant.URL_QUERY_PINJIALIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                JSONArray array = object.getJSONArray("code");
                String praiseTime = object.getString("praiseTime");//好
                String pertinentTimes = object.getString("PertinentTimes");//中
                String badTime = object.getString("BadTime");//差
                List<UserEvaluationList> lists = new ArrayList<>();
                if (array!=null&&array.size()>0) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        UserEvaluationList evaluationList = new UserEvaluationList();
                        String commentId = object1.getString("commentId");
                        String commentName = object1.getString("commentName");
                        String commentType = object1.getString("commentType");
                        String content = object1.getString("content");
                        String starCount = object1.getString("starCount");
                        String userId = object1.getString("userId");
                        String timestamp = object1.getString("timestamp");
                        evaluationList.setCommentId(commentId);
                        evaluationList.setCommentName(commentName);
                        evaluationList.setCommentType(commentType);
                        evaluationList.setContent(content);
                        evaluationList.setStarCount(starCount);
                        evaluationList.setUserId(userId);
                        evaluationList.setTimestamp(timestamp);
                        lists.add(evaluationList);
                    }
                }
                if ("00".equals(biaoshi)) {
                    SharedPreferences sp = getSharedPreferences("sangu_pingjia_count", Context.MODE_PRIVATE);
                    if ("0".equals(type)) {
                        SharedPreferences.Editor editor4 = sp.edit();
                        editor4.putString("badTime", badTime);
                        editor4.commit();
                    } else if ("1".equals(type)) {
                        SharedPreferences.Editor editor4 = sp.edit();
                        editor4.putString("pertinentTimes", pertinentTimes);
                        editor4.commit();
                    } else {
                        SharedPreferences.Editor editor4 = sp.edit();
                        editor4.putString("praiseTime", praiseTime);
                        editor4.commit();
                    }
                }
                if (isNext){
                    datas.addAll(lists);
                    mRecyclerView.loadMoreComplete();
                    adapter.notifyDataSetChanged();
                    if (lists.size()!=20){
                        mRecyclerView.setNoMore(true);
                    }
                }else {
                    datas = lists;
                    adapter = new PingjiaAdapter(PingJiaListActivity.this, datas);
                    mRecyclerView.setAdapter(adapter);
                    mRecyclerView.refreshComplete();
                    if (lists.size()!=20){
                        mRecyclerView.setNoMore(true);
                    }
                    if (DemoHelper.getInstance().isLoggedIn(PingJiaListActivity.this)) {
                        adapter.setOnItemClickListener(new PingjiaAdapter.MyItemClickListener() {
                            @Override
                            public void onItemClick(View view, final int position) {
                                final UserEvaluationList evaluationList = datas.get(position-1);
                                final String commentId = evaluationList.getCommentId();
                                final String userId = evaluationList.getUserId();
                                final String timestamp = evaluationList.getTimestamp();
                                if (commentId.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                                    LayoutInflater inflaterDl = LayoutInflater.from(PingJiaListActivity.this);
                                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                                    final Dialog dialog = new AlertDialog.Builder(PingJiaListActivity.this,R.style.Dialog).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    dialog.setCanceledOnTouchOutside(true);
                                    dialog.setCancelable(true);
                                    TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                                    Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                                    final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                                    btnOK.setText("确定");
                                    btnCancel.setText("取消");
                                    title_tv.setText("确认删除吗？");
                                    btnCancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    btnOK.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            String url = FXConstant.URL_DELETE_PINJIA;
                                            StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    Toast.makeText(PingJiaListActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                                                    datas.remove(position-1);
                                                    if (adapter!=null) {
                                                        adapter.notifyDataSetChanged();
                                                    }else {
                                                        adapter = new PingjiaAdapter(PingJiaListActivity.this,datas);
                                                        mRecyclerView.setAdapter(adapter);
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    Toast.makeText(PingJiaListActivity.this,"网络连接中断！",Toast.LENGTH_SHORT).show();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String,String> param = new HashMap<>();
                                                    param.put("commentId",commentId);
                                                    param.put("timestamp",timestamp);
                                                    param.put("userId", userId);
                                                    return param;
                                                }
                                            };
                                            MySingleton.getInstance(PingJiaListActivity.this).addToRequestQueue(request1);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("userId",userId);
                param.put("currentPage",currentPage+"");
                param.put("commentType", type);
                return param;
            }
        };
        MySingleton.getInstance(PingJiaListActivity.this).addToRequestQueue(request);
    }

    private void quertPingjiaZige() {
        String url = FXConstant.URL_QUERY_PINJIAZIGE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                int code = object.getInteger("code");
                if (code==0){
                    LayoutInflater inflaterDl = LayoutInflater.from(PingJiaListActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(PingJiaListActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    tv_title.setText("需对该用户进行下单并完成交易才可有权限评价");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                }else {
                    startActivity(new Intent(PingJiaListActivity.this,PingjiaActivity.class).putExtra("userId",userId));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("userId",userId);
                param.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(PingJiaListActivity.this).addToRequestQueue(request);
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_pingjia = (TextView) findViewById(R.id.tv_pingjia);
        btn_tianjia = (Button) findViewById(R.id.btn_tianjia);
        mRecyclerView = (XRecyclerView) findViewById(R.id.lv_fuwufankui);
        radiohp = (RadioButton) findViewById(R.id.radiohp);
        radiochp = (RadioButton) findViewById(R.id.radiochp);
        radiozhp = (RadioButton) findViewById(R.id.radiozhP);
        radiohp.setOnClickListener(this);
        radiozhp.setOnClickListener(this);
        radiochp.setOnClickListener(this);
        tv_title.setText("他的评价");
        tv_pingjia.setVisibility(View.VISIBLE);
        btn_tianjia.setVisibility(View.INVISIBLE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radiohp:
                radiohp.setChecked(true);
                radiochp.setChecked(false);
                radiozhp.setChecked(false);
                isNext = false;
                currentPage=1;
                type = "2";
                queryPingjiaList(type);
                break;
            case R.id.radiozhP:
                radiohp.setChecked(false);
                radiochp.setChecked(false);
                radiozhp.setChecked(true);
                isNext = false;
                currentPage=1;
                type = "1";
                queryPingjiaList(type);
                break;
            case R.id.radiochp:
                radiohp.setChecked(false);
                radiochp.setChecked(true);
                radiozhp.setChecked(false);
                isNext = false;
                currentPage=1;
                type = "0";
                queryPingjiaList(type);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

}
