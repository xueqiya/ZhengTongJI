package com.sangu.apptongji.main.activity;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.TradeTrackAdapter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018-01-23.
 */

public class TradeTrackActivity extends BaseActivity {
    private XRecyclerView mRecyclerView;
    private CustomProgressDialog mProgress=null;
    private RadioButton rb_wait=null,rb_accept=null,rb_no=null;
    private List<JSONObject> articles = new ArrayList<>();
    private int currentPage = 1;
    private int type = 0;
    private TradeTrackAdapter adapter;
    private String companyName = "123";

    public MyLocationListenner myListener = new MyLocationListenner();
    private String lng;
    private String lat;
    private LocationClient mLocClient;
    private LocationClientOption option;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_trade_track);
        companyName = getIntent().getStringExtra("companyName");
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        mProgress = CustomProgressDialog.createDialog(this);
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("gcj02");
        option.setScanSpan(30000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        initView();
        setListener();
        deletePush();
    }

    private void deletePush() {
        if (TextUtils.isEmpty(companyName)) {
            return;
        }
        String url2 = FXConstant.URL_DELETE_PUSH;
        StringRequest request2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                android.util.Log.d("chen", "URL_DELETE_PUSH" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                android.util.Log.d("chen", "URL_DELETE_PUSH volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                params.put("type", "17");
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request2);
    }

    private void setListener() {
        rb_wait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_wait.setChecked(true);
                rb_accept.setChecked(false);
                rb_no.setChecked(false);
                type = 0;
                mRecyclerView.refresh(false);
            }
        });
        rb_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_accept.setChecked(true);
                rb_wait.setChecked(false);
                rb_no.setChecked(false);
                type = 1;
                mRecyclerView.refresh(false);
            }
        });
        rb_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_no.setChecked(true);
                rb_accept.setChecked(false);
                rb_wait.setChecked(false);
                type = 2;
                mRecyclerView.refresh(false);
            }
        });

    }

    private void initView() {
        rb_wait = (RadioButton) findViewById(R.id.rb_wait);
        rb_accept = (RadioButton) findViewById(R.id.rb_accept);
        rb_no = (RadioButton) findViewById(R.id.rb_no);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (XRecyclerView) findViewById(R.id.refresh_list);
        mRecyclerView.setLayoutManager(layoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                getTradeList();
            }

            @Override
            public void onLoadMore() {
                currentPage++;
                getTradeList();
            }
        });
        mRecyclerView.refresh(false);
    }

    private void getTradeList() {
        if (mProgress!=null) {
            mProgress.show();
        }
        String url = FXConstant.URL_QUERY_DEAL_CONTACT;
        String state = "";
        //不同的tab使用不同的url
        if (type==0) {
            state = "01";
        }else if (type==1){
            state = "02";
        } else  {
            state = "03";
        }

        final String finalState = state;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "getTradeList" + s);
                JSONObject jsonObject = JSON.parseObject(s);
                JSONArray users_temp = jsonObject.getJSONArray("code");
                mRecyclerView.refreshComplete();
                //暂时不考虑分页，所以这里先都默认设置没有更多
                mRecyclerView.setNoMore(true);

                if (users_temp==null||users_temp.size()==0||users_temp.toString().equals("[]")){
                    articles = new ArrayList<>();
                    adapter = new TradeTrackAdapter(TradeTrackActivity.this,articles,type,lat,lng);
                    mRecyclerView.setAdapter(adapter);
                }else {
                    if (articles==null) {
                        articles = new ArrayList<JSONObject>();
                    }else {
                        if (currentPage==1) {
                            articles.clear();
                        }
                    }
                    for (int i = 0; i < users_temp.size(); i++) {
                        JSONObject json = users_temp.getJSONObject(i);
                        if (articles!=null&&json!=null) {
                            articles.add(json);
                        }
                    }
                    adapter = new TradeTrackAdapter(TradeTrackActivity.this,articles,type,lat,lng);
                    mRecyclerView.setAdapter(adapter);
                }
                if (mProgress!=null&&mProgress.isShowing()){
                    mProgress.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgress!=null&&mProgress.isShowing()){
                    mProgress.dismiss();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("type", finalState);
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            lng = "" + location.getLongitude();

            lat = "" + location.getLatitude();
            if (lng.equalsIgnoreCase("4.9E-324")) {
                ToastUtils.showNOrmalToast(TradeTrackActivity.this,"定位出错了发送位置会出现错误，请开启GPS，再发送一次");
                lng = "116.407170";
                lat = "39.904690";
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    @Override
    protected void onDestroy() {
        if (myListener!=null){
            mLocClient.unRegisterLocationListener(myListener);
            myListener = null;
        }
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
        if (option !=null ){
            option.setOpenGps(false);
            option = null;
        }
        super.onDestroy();
    }
}
