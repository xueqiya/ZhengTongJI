package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.DianziDanju;
import com.sangu.apptongji.main.alluser.order.adapter.DZdanjuListAdapter;
import com.sangu.apptongji.main.alluser.order.adapter.DanjuAdapter;
import com.sangu.apptongji.main.alluser.order.entity.OrderInfo;
import com.sangu.apptongji.main.alluser.presenter.IDanjuListPresenter;
import com.sangu.apptongji.main.alluser.presenter.IDzdanjuListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.DanjuListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.DzdanjuListPresenter;
import com.sangu.apptongji.main.alluser.view.IDanjuListView;
import com.sangu.apptongji.main.alluser.view.IDzdanjuView;
import com.sangu.apptongji.main.utils.BitmapUtils;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.CenterShowHorizontalScrollView;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-19.
 */

public class DanjuListActivity extends BaseActivity implements View.OnClickListener,IDanjuListView,IDzdanjuView{
    private TextView tvtitle=null,tv_create=null;
    private TextView tvback=null,tv_none,unread_danju_number,unread_dingdan_number;
    private IDanjuListPresenter presenter;
    private IDzdanjuListPresenter dzdanjuListPresenter;
    private View v1=null;
    private View v2=null;
    private ListView lv_paidan_list=null;
    private List<OrderInfo> list=new ArrayList<>();
    private List<DianziDanju> list2=new ArrayList<>();
    private DanjuAdapter adapter=null;
    private DZdanjuListAdapter dzAdapter=null;
    private LinearLayout nav=null;
    private RadioButton radioPaidan=null,radioOffline=null;
    private CustomProgressDialog mProgress;
    private boolean isNext = false;
    private boolean hasMore = false;
    private boolean isDDFirst = true;
    private boolean isDjFirst = true;
    private int currentPage;
    private int lastVisibleIndex;
    private int billCount=0;// 数据总条数
    private int thirdPartyCount=0;// 数据总条数
    private String biaoshi;

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_new_major);
        billCount = getIntent().getIntExtra("billCount",0);
        thirdPartyCount = getIntent().getIntExtra("thirdPartyCount",0);
        biaoshi = getIntent().hasExtra("biaoshi")?getIntent().getStringExtra("biaoshi"):"单据";
        presenter = new DanjuListPresenter(this,this);
        dzdanjuListPresenter = new DzdanjuListPresenter(this,this);
        WeakReference<DanjuListActivity> reference =  new WeakReference<DanjuListActivity>(DanjuListActivity.this);
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载请求列表...");
        mProgress.show();
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        v1 = findViewById(R.id.v1);
        v2 = findViewById(R.id.v2);
        nav = (LinearLayout) findViewById(R.id.nav);
        radioPaidan = (RadioButton) findViewById(R.id.radioPaidan);
        radioOffline = (RadioButton) findViewById(R.id.radioOffline);
        tvback = (TextView) findViewById(R.id.tvback);
        tv_none = (TextView) findViewById(R.id.tv_none);
        unread_danju_number = (TextView) findViewById(R.id.unread_danju_number);
        unread_dingdan_number = (TextView) findViewById(R.id.unread_dingdan_number);
        tvtitle = (TextView) findViewById(R.id.tvtitle);
        tv_create = (TextView) findViewById(R.id.tv_create);
        lv_paidan_list = (ListView) this.findViewById(R.id.lv_new_yuangong);
        radioPaidan.setOnClickListener(this);
        radioOffline.setOnClickListener(this);
        tv_create.setOnClickListener(this);
        nav.setVisibility(View.VISIBLE);
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.VISIBLE);
        tv_create.setVisibility(View.VISIBLE);
        tv_create.setText("开单据");
        tvback.setText("个人中心");
        tvtitle.setText("我的单据");
        radioPaidan.setText("单据");
        radioOffline.setText("订单");
        currentPage = 1;
        String type;
        if ("订单".equals(biaoshi)){
            type = "12";
            isDDFirst = false;
            radioOffline.setChecked(true);
            radioPaidan.setChecked(false);
            if (presenter!=null) {
                presenter.loadDanjuList(DemoHelper.getInstance().getCurrentUsernName());
            }
        }else {
            type = "11";
            isDjFirst = false;
            radioPaidan.setChecked(true);
            radioOffline.setChecked(false);
            if (dzdanjuListPresenter!=null) {
                dzdanjuListPresenter.loadDZdanjuList(DemoHelper.getInstance().getCurrentUsernName(), currentPage + "");
            }
        }
        if (billCount>0){
            unread_danju_number.setVisibility(View.VISIBLE);
            unread_danju_number.setText(billCount+"");
        }else {
            unread_danju_number.setVisibility(View.INVISIBLE);
        }
        if (thirdPartyCount>0){
            unread_dingdan_number.setVisibility(View.VISIBLE);
            unread_dingdan_number.setText(thirdPartyCount+"");
        }else {
            unread_dingdan_number.setVisibility(View.INVISIBLE);
        }
        deletePush(type);
    }

    private void deletePush(final String type) {
        String url = FXConstant.URL_DELETE_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                param.put("type", type);
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_create:
                final int[] clickPos = {0};
                LayoutInflater inflaterDl = LayoutInflater.from(DanjuListActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_horscrollview, null);
                final Dialog dialog = new AlertDialog.Builder(DanjuListActivity.this,R.style.Dialog).create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                final CenterShowHorizontalScrollView ct_scrollView = (CenterShowHorizontalScrollView) dialog.findViewById(R.id.ct_scrollView);
                Button btn_contact = (Button) dialog.findViewById(R.id.btn_contact);
                Button btn_commit = (Button) dialog.findViewById(R.id.btn_commit);
                btn_contact.setText("取  消");
                btn_commit.setText("确  定");
                final View titleItem1 = View.inflate(DanjuListActivity.this, R.layout.item_select_img, null);
                ImageView iv1 = (ImageView) titleItem1.findViewById(R.id.iv_moshi);
                Bitmap bm1 = BitmapUtils.readBitMap(DanjuListActivity.this,R.drawable.dianzidanju_one);
                iv1.setImageBitmap(bm1);
                ct_scrollView.addItemView(titleItem1, 0);
                titleItem1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickPos[0] = 0;
                        ct_scrollView.onClicked(v,0);
                    }
                });
                final View titleItem2 = View.inflate(DanjuListActivity.this, R.layout.item_select_img, null);
                ImageView iv2 = (ImageView) titleItem2.findViewById(R.id.iv_moshi);
                Bitmap bm2 = BitmapUtils.readBitMap(DanjuListActivity.this,R.drawable.dianzidanju_two);
                iv2.setImageBitmap(bm2);
                ct_scrollView.addItemView(titleItem2, 1);
                titleItem2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickPos[0] = 1;
                        ct_scrollView.onClicked(v,1);
                    }
                });
                final View titleItem3 = View.inflate(DanjuListActivity.this, R.layout.item_select_img, null);
                ImageView iv3 = (ImageView) titleItem3.findViewById(R.id.iv_moshi);
                Bitmap bm3 = BitmapUtils.readBitMap(DanjuListActivity.this,R.drawable.newfour);
                iv3.setImageBitmap(bm3);
                ct_scrollView.addItemView(titleItem3, 2);
                titleItem3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickPos[0] = 2;
                        ct_scrollView.onClicked(v,2);
                    }
                });
                final View titleItem4 = View.inflate(DanjuListActivity.this, R.layout.item_select_img, null);
                ImageView iv4 = (ImageView) titleItem4.findViewById(R.id.iv_moshi);
                Bitmap bm4 = BitmapUtils.readBitMap(DanjuListActivity.this,R.drawable.dianzidanju_five);
                iv4.setImageBitmap(bm4);
                ct_scrollView.addItemView(titleItem4, 3);
                titleItem4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickPos[0] = 3;
                        ct_scrollView.onClicked(v,3);
                    }
                });
                btn_contact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (clickPos[0]==0){
                            startActivity(new Intent(DanjuListActivity.this,DianziDanJuOneActivity.class).putExtra("biaoshi","00"));
                        }else if (clickPos[0]==1){
                            startActivity(new Intent(DanjuListActivity.this,DianziDanJuTwoActivity.class).putExtra("biaoshi","00"));
                        }else if (clickPos[0]==2){
                         //   startActivity(new Intent(DanjuListActivity.this,DianziDanJuFourActivity.class).putExtra("biaoshi","00"));
                            startActivity(new Intent(DanjuListActivity.this,DianziDanJuNewsFourActivity.class).putExtra("biaoshi","00"));
                        }else if (clickPos[0]==3){
                            startActivity(new Intent(DanjuListActivity.this,DianziDanJuActivity.class).putExtra("biaoshi","00"));
                        }
                    }
                });
                break;
            case R.id.radioPaidan:
                if (mProgress != null) {
                    if (!mProgress.isShowing()) {
                        mProgress.show();
                    }
                } else {
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
                if (isDjFirst){
                    deletePush("11");
                    isDjFirst = false;
                }
                unread_danju_number.setVisibility(View.INVISIBLE);
                radioPaidan.setChecked(true);
                radioOffline.setChecked(false);
                isNext = false;
                currentPage = 1;
                dzdanjuListPresenter.loadDZdanjuList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");
                break;
            case R.id.radioOffline:
                if (mProgress != null) {
                    if (!mProgress.isShowing()) {
                        mProgress.show();
                    }
                } else {
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
                if (isDDFirst){
                    deletePush("12");
                    isDDFirst = false;
                }
                unread_dingdan_number.setVisibility(View.INVISIBLE);
                radioPaidan.setChecked(false);
                radioOffline.setChecked(true);
                presenter.loadDanjuList(DemoHelper.getInstance().getCurrentUsernName());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (radioOffline.isChecked()){
            if (presenter!=null) {
                presenter.loadDanjuList(DemoHelper.getInstance().getCurrentUsernName());
            }
        }else {
            if (dzdanjuListPresenter!=null) {
                dzdanjuListPresenter.loadDZdanjuList(DemoHelper.getInstance().getCurrentUsernName(), currentPage + "");
            }
        }
    }

    @Override
    public void updateQDanjuList(List<OrderInfo> orderInfos) {
        if (mProgress != null&&mProgress.isShowing()) {
            mProgress.dismiss();
        }
        if (orderInfos==null||orderInfos.size()==0){
            tv_none.setVisibility(View.VISIBLE);
        }else {
            tv_none.setVisibility(View.GONE);
        }
        if (orderInfos!=null) {
            this.list = orderInfos;
        }
        adapter = new DanjuAdapter(list, this);
        lv_paidan_list.setAdapter(adapter);
        lv_paidan_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                OrderInfo info = list.get(position);
                String biaoshi;
                String flg = TextUtils.isEmpty(info.getResv2())?"01":info.getResv2();
                String remark = TextUtils.isEmpty(info.getRemark())?"":info.getRemark();
                String orderBody = info.getOrderBody();
                String orderId = info.getOrderId();
                String merId = info.getMerId();
                String orderState = info.getOrderState();
                String send_id1 = info.getSend_id1();
                String send_id2 = info.getSend_id2();
                String orderTime = TextUtils.isEmpty(info.getOrderTime()) ? "" : info.getOrderTime();
                orderTime = orderTime.substring(0, 4) + "-" + orderTime.substring(4, 6) + "-" + orderTime.substring(6, 8) + " "
                        + orderTime.substring(8, 10) + ":" + orderTime.substring(10, 12);
                if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase("null")){
                    biaoshi = "02";
                }else if (send_id1!=null&&!"".equals(send_id1)&&!send_id1.equalsIgnoreCase("null")){
                    biaoshi = "01";
                }else {
                    biaoshi = "00";
                }
                if (flg.equals("01")) {
                    Intent intent = new Intent(DanjuListActivity.this, UTOrderDetailActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("biaoshi", biaoshi);
                    intent.putExtra("biaoshi2", "01");
                    intent.putExtra("merId", merId);
                    intent.putExtra("orderBody", orderBody);
                    intent.putExtra("orderTime", orderTime);
                    intent.putExtra("companyId", remark);
                    intent.putExtra("orderState", orderState);
                    startActivity(intent);
                }else if (flg.equals("02")){
                    Intent intent = new Intent(DanjuListActivity.this, UTOrderDetailTwoActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("merId", merId);
                    intent.putExtra("orderBody", orderBody);
                    intent.putExtra("biaoshi", biaoshi);
                    intent.putExtra("biaoshi2", "01");
                    intent.putExtra("orderTime", orderTime);
                    intent.putExtra("companyId",remark);
                    intent.putExtra("orderState", orderState);
                    startActivity(intent);
                }else if (flg.equals("04")){
                    Intent intent = new Intent(DanjuListActivity.this, UTOrderDetailFourActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("merId", merId);
                    intent.putExtra("orderBody", orderBody);
                    intent.putExtra("biaoshi", biaoshi);
                    intent.putExtra("biaoshi2", "01");
                    intent.putExtra("orderTime", orderTime);
                    intent.putExtra("companyId",remark);
                    intent.putExtra("orderState", orderState);
                    startActivity(intent);
                }else if (flg.equals("05")){
                    Intent intent = new Intent(DanjuListActivity.this, UTOrderDetailFiveActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("merId", merId);
                    intent.putExtra("orderBody", orderBody);
                    intent.putExtra("biaoshi", biaoshi);
                    intent.putExtra("biaoshi2", "01");
                    intent.putExtra("orderTime", orderTime);
                    intent.putExtra("companyId",remark);
                    intent.putExtra("orderState", orderState);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void updateDzdanjuList(List<DianziDanju> lists, final boolean hasMore) {
        if (mProgress != null&&mProgress.isShowing()) {
            mProgress.dismiss();
        }
        this.hasMore = hasMore;
        if (isNext&&lists!=null){
            list2.addAll(lists);
            dzAdapter.appendData(lists,this);
            dzAdapter.notifyDataSetChanged();
        }else {
            if (lists!=null) {
                this.list2 = lists;
            }
            dzAdapter = new DZdanjuListAdapter(list2,this);
            lv_paidan_list.setAdapter(dzAdapter);
        }
        if (list2==null||list2.size()==0){
            tv_none.setVisibility(View.VISIBLE);
        }else {
            tv_none.setVisibility(View.GONE);
        }
        // listView设置滑动简监听
        lv_paidan_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                Log.e("TAG", "lastVisibleIndex = " + lastVisibleIndex);
                Log.e("TAG", "adapter.getCount() = " + dzAdapter.getCount());
                // 滑到底部后自动加载，判断listView已经停止滚动并且最后可视的条目等于adapter的条目
                // 注意这里在listView设置好adpter后，加了一个底部加载布局。
                // 所以判断条件为：lastVisibleIndex == adapter.getCount()
                if (scrollState == SCROLL_STATE_IDLE
                        && lastVisibleIndex == dzAdapter.getCount()-1&&hasMore) {
                    /**
                     * 这里也要设置为可见，是因为当你真正从网络获取数据且获取失败的时候。
                     * 我在失败的方法里面，隐藏了底部的加载布局并提示用户加载失败。所以再次监听的时候需要
                     * 继续显示隐藏的控件。因为我模拟的获取数据，失败的情况这里不给出。实际中简单的加上几句代码就行了。
                     */
                    isNext=true;
                    currentPage++;
                    dzdanjuListPresenter.loadDZdanjuList(DemoHelper.getInstance().getCurrentUsernName(),currentPage+"");// 加载更多数据
                }
            }

            @Override
            public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount) {
                // 计算最后可见条目的索引
                lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
                // 当adapter中的所有条目数已经和要加载的数据总条数相等时，则移除底部的View
            }
        });
        lv_paidan_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DianziDanju dianziDanju = list2.get(position);
                String timestamp = dianziDanju.getTimestamp();
                String flag = dianziDanju.getFlag();
                if ("01".equals(flag)){
                    Intent intent = new Intent(DanjuListActivity.this,DianziDanJuOneActivity.class);
                    intent.putExtra("biaoshi","01");
                    intent.putExtra("timestamp",timestamp);
                    startActivity(intent);
                }else if ("02".equals(flag)){
                    Intent intent = new Intent(DanjuListActivity.this,DianziDanJuTwoActivity.class);
                    intent.putExtra("biaoshi","01");
                    intent.putExtra("timestamp",timestamp);
                    startActivity(intent);
                }else if ("04".equals(flag)){
                    Intent intent = new Intent(DanjuListActivity.this,DianziDanJuFourActivity.class);
                    intent.putExtra("biaoshi","01");
                    intent.putExtra("timestamp",timestamp);
                    startActivity(intent);
                }else if ("06".equals(flag)){
                    Intent intent = new Intent(DanjuListActivity.this,DianziDanJuNewsFourActivity.class);
                    intent.putExtra("biaoshi","01");
                    intent.putExtra("timestamp",timestamp);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(DanjuListActivity.this, DianziDanJuActivity.class);
                    intent.putExtra("biaoshi", "01");
                    intent.putExtra("timestamp", timestamp);
                    startActivity(intent);
                }
            }
        });
    }
}
