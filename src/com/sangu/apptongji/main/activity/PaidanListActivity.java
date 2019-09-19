package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fanxin.easeui.EaseConstant;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.PaidanAdapter;
import com.sangu.apptongji.main.alluser.entity.MySendPaidanInfo;
import com.sangu.apptongji.main.moments.MomentsPublishActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by chen on 2018-1-16.
 */

public class PaidanListActivity extends BaseActivity {
    private TextView tvtitle = null, tv_create = null, tv_trade_track = null;
    private RadioButton rb_baojia_to = null, rb_baojia_from = null;
    private XRecyclerView mRecyclerView;
    private CustomProgressDialog mProgress = null;
    public MyLocationListenner myListener = new MyLocationListenner();
    private PaidanAdapter adapter;
    private List<JSONObject> articles = new ArrayList<>();
    private int type = 0;
    private int pushType = 0;
    private DynamicReceiver dynamicReceiver;
    private String lng;
    private String lat;
    private LocationClient mLocClient;
    private LocationClientOption option;
    private String companyName;
    private boolean isShiTing = false;
    private AlertDialog dialog2;
    private int page_num = 1;
    private String task_label;
    private Integer countResponse=1;
    private Double sumResponse;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_paidan_list);
        //companyName = getIntent().getStringExtra("companyName");
        type = getIntent().getIntExtra("type", 0);
        task_label = getIntent().getStringExtra("task_label");
        pushType = getIntent().getIntExtra("pushType", 0);
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
        deletePush(pushType);
        //mMediaPlayer = new MediaPlayer();

        /*if (pushType != 19) {
            updateTotalNumber(companyName, true);
        } else {
            showDialogPrepare();
        }*/
        //updateTotalNumber(companyName, true);
        //动态注册广播
        /*dynamicReceiver = new DynamicReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.broadcast.reflash");
        registerReceiver(dynamicReceiver, intentFilter);*/

        //判断是否评论过 没有的话提示一下 点击一次的话就不管了
        final SharedPreferences mSharedPreferences = getSharedPreferences("score", Context.MODE_PRIVATE);

        String isScore = mSharedPreferences.getString("isScore","0");

        if (isScore.equals("yes")){

        }else {


            LayoutInflater inflaterD5 = LayoutInflater.from(PaidanListActivity.this);
            LinearLayout layout5 = (LinearLayout) inflaterD5.inflate(R.layout.dialog_pushappstore_score, null);
            final Dialog dialog5 = new AlertDialog.Builder(PaidanListActivity.this, R.style.Dialog).create();
            dialog5.show();
            dialog5.getWindow().setContentView(layout5);
            WindowManager.LayoutParams params = dialog5.getWindow().getAttributes() ;
            Display display = PaidanListActivity.this.getWindowManager().getDefaultDisplay();
            params.width =(int) (display.getWidth()*0.75); //使用这种方式更改了dialog的框宽
            dialog5.getWindow().setAttributes(params);
            dialog5.setCancelable(true);
            dialog5.setCanceledOnTouchOutside(true);

            TextView tv_mid = (TextView)layout5.findViewById(R.id.tv_midBtn);

            tv_mid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog5.dismiss();

                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("isScore","yes");
                    editor.commit();

                    try {
                        Uri uri = Uri.parse("market://details?id="
                                + getPackageName());//需要评分的APP包名
                        Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                        intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent5,1);

                    } catch (Exception e) {

                        Toast.makeText(PaidanListActivity.this, "跳转失败", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

    }

    private void deletePush(final int pushType) {
        if (pushType == 0) {
            return;
        }
        String url = FXConstant.URL_DELETE_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                param.put("type", pushType + "");
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void updateTotalNumber(final String companyName, boolean isAddTotalNum) {
        if (!TextUtils.isEmpty(companyName) && isAddTotalNum) {
            RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
            String url = FXConstant.URL_UPDATE_TOTALNUM;
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    android.util.Log.d("chen", "updateTotalNumber" + s);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    android.util.Log.d("chen", "updateTotalNumber volleyError" + volleyError.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    //sid=&uid=
                    params.put("uid", DemoHelper.getInstance().getCurrentUsernName());
                    params.put("sid", companyName);
                    return params;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(request);
        }
        if (pushType != 0 && pushType != 19) {
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
                    params.put("type", pushType + "");
                    return params;
                }
            };
            MySingleton.getInstance(this).addToRequestQueue(request2);
        }

    }

    private List<JSONObject> articles2 = new ArrayList<>();

    public void updateMySendPaidan() {
        if (page_num >= 2) {
            return;
        }
        //Log.d("chen", "开始刷新我发送的派单数据");
        String url = FXConstant.URL_QUERY_PUBLIC_PAIDAN;
        //http://192.168.1.120/dynamic/selectDynamicReceipt?uId=10000001055&type=01&currentPage=1
        List<Param> params = new ArrayList<>();
        params.add(new Param("currentPage", page_num + ""));
        params.add(new Param("type", "00"));
        params.add(new Param("uId", DemoHelper.getInstance().getCurrentUsernName()));
        OkHttpManager.getInstance().post(params, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (type == 0) {
                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("dynamicInfoList");
                    if (jsonArray != null) {
                        articles2.clear();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            articles2.add(jsonArray.getJSONObject(i));
                        }
                        adapter.setNewData(articles2);
                    }

                }


            }

            @Override
            public void onFailure(String errorMsg) {
                Log.d("chen", "onFailure" + errorMsg);
                ToastUtils.showNOrmalToast(PaidanListActivity.this.getApplicationContext(), "获取数据失败");

            }
        });

    }

    private void setListener() {
        rb_baojia_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_baojia_to.setChecked(true);
                rb_baojia_from.setChecked(false);
                type = 0;
                mRecyclerView.refresh(false);
            }
        });
        rb_baojia_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_baojia_from.setChecked(true);
                rb_baojia_to.setChecked(false);
                type = 1;
                mRecyclerView.refresh(false);
            }
        });
        tv_trade_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaidanListActivity.this, TradeTrackActivity.class));
            }
        });
    }

    private void initView() {
        rb_baojia_to = (RadioButton) findViewById(R.id.rb_baojia_to);
        rb_baojia_from = (RadioButton) findViewById(R.id.rb_baojia_from);
        tvtitle = (TextView) findViewById(R.id.tvtitle);
        tv_trade_track = (TextView) findViewById(R.id.tv_trade_track);
        tv_create = (TextView) findViewById(R.id.tv_create);
        mRecyclerView = (XRecyclerView) findViewById(R.id.refresh_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        if (type == 1) {
            rb_baojia_from.setChecked(true);
            rb_baojia_to.setChecked(false);
        }
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page_num = 1;
                getBaojiaList();
            }

            @Override
            public void onLoadMore() {
                page_num++;
                getBaojiaList();
            }
        });
        mRecyclerView.refresh(false);
    }

    public void getBaojiaList() {
        /*if (adapter != null) {
            adapter.updateAllPaidanPush();
        }*/
        if (mProgress != null) {
            mProgress.show();
        }
        String url = FXConstant.URL_QUERY_PUBLIC_PAIDAN;

        //http://192.168.1.120/dynamic/selectDynamicReceipt?uId=10000001055&type=01&currentPage=1
        List<Param> params = new ArrayList<>();
        params.add(new Param("currentPage", page_num + ""));
        if (type == 0) {
            params.add(new Param("type", "01"));
        } else {
            params.add(new Param("type", "00"));
        }

        params.add(new Param("uId", DemoHelper.getInstance().getCurrentUsernName()));
        OkHttpManager.getInstance().post(params, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("chen", "onResponse" + jsonObject);
                if (type == 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("dynamicInfoList");
                    try {
                        if (jsonObject.getInteger("countResponse") != null)
                        {
                            countResponse = jsonObject.getInteger("countResponse");
                        }
                        sumResponse = jsonObject.getDouble("sumResponse");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (page_num == 1) {
                        mRecyclerView.refreshComplete();
                    } else {
                        mRecyclerView.loadMoreComplete();
                    }
                    if (jsonArray != null && jsonArray.size() < 20) {
                        if (page_num > 1) {
                            mRecyclerView.setNoMore(true);
                        }
                    }
                    if (jsonArray != null) {
                        if (page_num == 1) {
                            if (articles != null) {
                                articles.clear();
                            }
                        }

                        for (int i = 0; i < jsonArray.size(); i++) {
                            articles.add(jsonArray.getJSONObject(i));
                        }
                    }

                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("dynamicInfoList");
                    if (page_num == 1) {
                        mRecyclerView.refreshComplete();
                    } else {
                        mRecyclerView.loadMoreComplete();
                    }
                    if (jsonArray != null && jsonArray.size() < 20) {
                        if (page_num > 1) {
                            mRecyclerView.setNoMore(true);
                        }
                    }
                    if (jsonArray != null) {
                        if (page_num == 1) {
                            if (articles != null) {
                                articles.clear();
                            }
                        }

                        for (int i = 0; i < jsonArray.size(); i++) {
                            articles.add(jsonArray.getJSONObject(i));
                        }

                    }

                }
                Log.d("chen", "传入" + lat + " -- " + lng);
                adapter = new PaidanAdapter(PaidanListActivity.this, articles, type, lat, lng);
                if (type == 0) {
                    adapter.setReactTime(countResponse, sumResponse);
                }
                mRecyclerView.setAdapter(adapter);
                if (mProgress != null && mProgress.isShowing()) {
                    mProgress.dismiss();
                }

            }

            @Override
            public void onFailure(String errorMsg) {
                Log.d("chen", "onFailure" + errorMsg);
                if (mProgress != null && mProgress.isShowing()) {
                    mProgress.dismiss();
                }
                ToastUtils.showNOrmalToast(PaidanListActivity.this.getApplicationContext(), "获取数据失败");

            }
        });


        /*StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = JSON.parseObject(s);
                JSONArray users_temp = jsonObject.getJSONArray("list");
                mRecyclerView.refreshComplete();
                if (users_temp == null || users_temp.size() != 20) {
                    mRecyclerView.setNoMore(true);
                }
                //暂时不考虑分页，所以这里先都默认设置没有更多
                mRecyclerView.setNoMore(true);
                if (users_temp == null || users_temp.size() == 0 || users_temp.toString().equals("[]")) {
                    articles = new ArrayList<>();
                    adapter = new PaidanAdapter(PaidanListActivity.this, articles, type, lat, lng);
                    mRecyclerView.setAdapter(adapter);
                } else {
                    if (articles == null) {
                        articles = new ArrayList<JSONObject>();
                    } else {
                        if (currentPage == 1) {
                            articles.clear();
                        }
                    }
                    for (int i = 0; i < users_temp.size(); i++) {
                        JSONObject json = users_temp.getJSONObject(i);
                        if (articles != null && json != null) {
                            articles.add(json);
                        }
                    }
                    adapter = new PaidanAdapter(PaidanListActivity.this, articles, type, lat, lng);
                    mRecyclerView.setAdapter(adapter);
                }


                if (mProgress != null && mProgress.isShowing()) {
                    mProgress.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgress != null && mProgress.isShowing()) {
                    mProgress.dismiss();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*Log.d("chen","收到了,"+requestCode + "www" + resultCode);
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case 1:
                Log.d("chen","我收到了要刷新的指示,");
                updateAllPaidanWithNoCreateNewAdapter();
                break;
            default:
                break;
        }*/

    }

    public void updateAllPaidanWithNoCreateNewAdapter() {
        String url = null;
        //不同的tab使用不同的url
        if (type == 0) {
            url = FXConstant.URL_QUERY_PUSH_DINGDAN;
        } else {
            url = FXConstant.URL_QUERY_MY_SEND_DINGDAN;
        }
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = JSON.parseObject(s);
                JSONArray users_temp = jsonObject.getJSONArray("list");
                if (users_temp == null || users_temp.size() == 0 || users_temp.toString().equals("[]")) {
                    return;
                } else {
                    if (articles == null) {
                        articles = new ArrayList<JSONObject>();
                    } else {
                        if (page_num == 1) {
                            articles.clear();
                        }
                    }
                    for (int i = 0; i < users_temp.size(); i++) {
                        JSONObject json = users_temp.getJSONObject(i);
                        if (articles != null && json != null) {
                            articles.add(json);
                        }
                    }

                }
                Log.d("chen", "onResponse");
                adapter.setNewData(articles);
                if (mProgress != null && mProgress.isShowing()) {
                    mProgress.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    @Override
    protected void onDestroy() {
        if (adapter != null) {

            //adapter.updateAllPaidanPush();
            adapter.stopThread();
        }
        //解除广播
        //unregisterReceiver(dynamicReceiver);
        if (myListener != null) {
            mLocClient.unRegisterLocationListener(myListener);
            myListener = null;
        }
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
        if (option != null) {
            option.setOpenGps(false);
            option = null;
        }
        super.onDestroy();
    }

    class DynamicReceiver extends BroadcastReceiver {
        public DynamicReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("type") != null) {
                rb_baojia_from.setChecked(false);
                rb_baojia_to.setChecked(true);
                type = 0;
                companyName = intent.getStringExtra("companyName");
                pushType = Integer.parseInt(intent.getStringExtra("pushType"));
                if (pushType == 19) {
                    showDialogPrepare();
                } else {
                    updateTotalNumber(companyName, true);
                }
                getBaojiaList();
                if (dialog2 != null && dialog2.isShowing()) {
                    dialog2.dismiss();
                }
            } else {
                if (adapter != null) {
                    updateAllPaidanWithNoCreateNewAdapter();
                }
            }


        }
    }

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.d("chen", "百度定位" + location.getLatitude());
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            lng = "" + location.getLongitude();

            lat = "" + location.getLatitude();
            if (TextUtils.isEmpty(lat)) {
                lat = DemoApplication.getInstance().getCurrentLat();

            }
            if (TextUtils.isEmpty(lng)) {
                lng = DemoApplication.getInstance().getCurrentLng();
            }
            if (lng.equalsIgnoreCase("4.9E-324")) {
                ToastUtils.showNOrmalToast(PaidanListActivity.this.getApplicationContext(), "定位出错了发送位置会出现错误，请开启GPS，再发送一次");
                lng = "116.407170";
                lat = "39.904690";
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    private void showDialogPrepare() {
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_QUERY_PAIDAN_BY_ID;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "查询我的派单" + s);
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    org.json.JSONObject object2 = object.getJSONObject("speedList");
                    String voiceFilePath = object2.isNull("file") ? null : object2.getString("file");
                    String content = object2.isNull("content") ? null : object2.getString("content");
                    if (!TextUtils.isEmpty(voiceFilePath)) {
                        showPaidanStateDialog(voiceFilePath, true);
                    } else {
                        showPaidanStateDialog(content, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "showPaidanStateDialog volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("s_id", companyName);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    private void showPaidanStateDialog(final String msg, final boolean isVoice) {
        if (dialog2 != null && dialog2.isShowing()) {
            dialog2.dismiss();
        }
        RelativeLayout view = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.dialog_paidan_state, null);
        dialog2 = new AlertDialog.Builder(this, R.style.Dialog).create();
        dialog2.show();
        dialog2.getWindow().setContentView(view);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        Button btn_yes = (Button) view.findViewById(R.id.btn_yes);
        Button btn_contact_kefu = (Button) view.findViewById(R.id.btn_contact_kefu);
        final LinearLayout ll_cb_1 = (LinearLayout) view.findViewById(R.id.ll_cb_1);
        final LinearLayout ll_cb_2 = (LinearLayout) view.findViewById(R.id.ll_cb_2);
        final LinearLayout ll_cb_3 = (LinearLayout) view.findViewById(R.id.ll_cb_3);
        final LinearLayout ll_cb_4 = (LinearLayout) view.findViewById(R.id.ll_cb_4);
        final LinearLayout ll_cb_5 = (LinearLayout) view.findViewById(R.id.ll_cb_5);
        final LinearLayout ll_cb_6 = (LinearLayout) view.findViewById(R.id.ll_cb_6);

        final CheckBox cb_1 = (CheckBox) view.findViewById(R.id.cb_1);
        final CheckBox cb_2 = (CheckBox) view.findViewById(R.id.cb_2);
        final CheckBox cb_3 = (CheckBox) view.findViewById(R.id.cb_3);
        final CheckBox cb_4 = (CheckBox) view.findViewById(R.id.cb_4);
        final CheckBox cb_5 = (CheckBox) view.findViewById(R.id.cb_5);
        final CheckBox cb_6 = (CheckBox) view.findViewById(R.id.cb_6);
        final List<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(cb_1);
        checkBoxes.add(cb_2);
        checkBoxes.add(cb_3);
        checkBoxes.add(cb_4);
        checkBoxes.add(cb_5);
        checkBoxes.add(cb_6);

        ll_cb_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChooseCheckBox(checkBoxes, 0);
            }
        });
        ll_cb_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChooseCheckBox(checkBoxes, 1);
            }
        });
        ll_cb_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChooseCheckBox(checkBoxes, 2);
            }
        });
        ll_cb_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChooseCheckBox(checkBoxes, 3);
            }
        });
        ll_cb_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChooseCheckBox(checkBoxes, 4);
            }
        });
        ll_cb_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleChooseCheckBox(checkBoxes, 5);
            }
        });

        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_voice_contain);
        final ImageView iv_voice = (ImageView) view.findViewById(R.id.iv_voice);

        if (isVoice) {
            relativeLayout.setVisibility(View.VISIBLE);
            tv_content.setVisibility(View.GONE);
        } else {
            tv_content.setText(msg);
            relativeLayout.setVisibility(View.GONE);
            tv_content.setVisibility(View.VISIBLE);
        }
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShiTing) {
                    ToastUtils.showNOrmalToast(getApplicationContext(), "正在播放中");
                } else {
                    iv_voice.setImageResource(R.drawable.voice_play);
                    AnimationDrawable animationDrawable = (AnimationDrawable) iv_voice.getDrawable();
                    animationDrawable.start();
                    //playVoice(msg, iv_voice);
                }
            }
        });
        btn_contact_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent11 = new Intent(PaidanListActivity.this, ChatActivity.class);
                intent11.putExtra("userId", "18337101357");
                intent11.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                intent11.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                intent11.putExtra(EaseConstant.EXTRA_USER_IMG, "zhengshiduo.png");
                intent11.putExtra(EaseConstant.EXTRA_USER_NAME, "正事多客服");
                intent11.putExtra(EaseConstant.EXTRA_USER_SHARERED, "无");
                startActivity(intent11);
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_1.isChecked()) {
                    MySendPaidanInfo info = new MySendPaidanInfo();
                    if (isVoice) {
                        info.setContent("");
                    } else {
                        info.setContent(msg);
                    }
                    info.setuId(DemoHelper.getInstance().getCurrentUsernName());
                    info.setsId(companyName);
                    resendPaidan(info);
                    insertReason(companyName, "1");
                    deletePushType19(companyName);
                } else if (cb_2.isChecked()) {
                    MySendPaidanInfo info = new MySendPaidanInfo();
                    if (isVoice) {
                        info.setContent("");
                    } else {
                        info.setContent(msg);
                    }
                    info.setuId(DemoHelper.getInstance().getCurrentUsernName());
                    info.setsId(companyName);
                    resendPaidan(info);
                    insertReason(companyName, "2");
                    deletePushType19(companyName);
                } else if (cb_3.isChecked()) {
                    if (dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                    RelativeLayout view = (RelativeLayout) LayoutInflater.from(PaidanListActivity.this).inflate(R.layout.dialog_paidan_choose_tip, null);
                    final Dialog dialog4 = new AlertDialog.Builder(PaidanListActivity.this, R.style.Dialog).create();
                    dialog4.show();
                    dialog4.getWindow().setContentView(view);
                    dialog4.setCanceledOnTouchOutside(false);
                    dialog4.setCancelable(false);
                    Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                    Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletePushType19(companyName);
                            dialog4.dismiss();
                        }
                    });
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isVoice) {
                                PaidanListActivity.this.startActivityForResult(new Intent(PaidanListActivity.this, FinishPaidanActivity.class).
                                        putExtra("sId", companyName).putExtra("content", ""), 1);
                            } else {
                                PaidanListActivity.this.startActivityForResult(new Intent(PaidanListActivity.this, FinishPaidanActivity.class).
                                        putExtra("sId", companyName).putExtra("content", msg), 1);
                            }
                            dialog4.dismiss();
                            insertReason(companyName, "3");
                            deletePushType19(companyName);
                        }
                    });
                } else if (cb_4.isChecked()) {
                    if (dialog2.isShowing()) {
                        dialog2.dismiss();
                    }
                    RelativeLayout view = (RelativeLayout) LayoutInflater.from(PaidanListActivity.this).inflate(R.layout.dialog_paidan_choose_tip, null);
                    final Dialog dialog4 = new AlertDialog.Builder(PaidanListActivity.this, R.style.Dialog).create();
                    dialog4.show();
                    dialog4.getWindow().setContentView(view);
                    dialog4.setCanceledOnTouchOutside(false);
                    dialog4.setCancelable(false);
                    Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                    Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deletePushType19(companyName);
                            dialog4.dismiss();
                        }
                    });
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isVoice) {
                                PaidanListActivity.this.startActivityForResult(new Intent(PaidanListActivity.this, FinishPaidanActivity.class).
                                        putExtra("sId", companyName).putExtra("content", ""), 1);
                            } else {
                                PaidanListActivity.this.startActivityForResult(new Intent(PaidanListActivity.this, FinishPaidanActivity.class).
                                        putExtra("sId", companyName).putExtra("content", msg), 1);
                            }
                            dialog4.dismiss();
                            insertReason(companyName, "4");
                            deletePushType19(companyName);
                        }
                    });
                } else if (cb_5.isChecked()) {
                    deletePaidan(companyName);
                    insertReason(companyName, "5");
                    deletePushType19(companyName);
                } else if (cb_6.isChecked()) {
                    RelativeLayout view = (RelativeLayout) LayoutInflater.from(PaidanListActivity.this).inflate(R.layout.dialog_paidan6, null);
                    final Dialog dialog3 = new AlertDialog.Builder(PaidanListActivity.this, R.style.Dialog).create();
                    dialog3.show();
                    dialog3.getWindow().setContentView(view);
                    dialog3.setCanceledOnTouchOutside(false);
                    dialog3.setCancelable(false);
                    Button btn_delete = (Button) view.findViewById(R.id.btn_delete);
                    Button btn_send_detail_paidan = (Button) view.findViewById(R.id.btn_send_detail_paidan);
                    Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog3.dismiss();
                            if (dialog2.isShowing()) {
                                dialog2.dismiss();

                            }
                            insertReason(companyName, "6");
                            deletePushType19(companyName);
                        }
                    });
                    btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog3.dismiss();
                            insertReason(companyName, "7");
                            deletePushType19(companyName);
                            deletePaidan(companyName);
                        }
                    });
                    btn_send_detail_paidan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog3.dismiss();
                            Intent intent6 = new Intent(PaidanListActivity.this, MomentsPublishActivity.class);
                            intent6.putExtra("biaoshi", "xuqiu");
                            startActivityForResult(intent6, 0);
                            insertReason(companyName, "8");
                            deletePushType19(companyName);
                        }
                    });

                } else {
                    //表示什么都没选
                    ToastUtils.showNOrmalToast(PaidanListActivity.this.getApplicationContext(), "您必须选择一个");
                }
            }
        });


    }

    private void singleChooseCheckBox(List<CheckBox> checkBoxes, int positon) {
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (positon == i) {
                checkBoxes.get(positon).setChecked(!checkBoxes.get(positon).isChecked());
            } else {
                checkBoxes.get(i).setChecked(false);
            }
        }

    }

    //private MediaPlayer mMediaPlayer;

   /* private void playVoice(String file, final ImageView imageView) {
        isShiTing = true;
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(FXConstant.URL_UPLOAD_SPEED + "/" + file); // 设置数据源
            mMediaPlayer.prepare(); // prepare自动播放
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //android.graphics.drawable.BitmapDrawable cannot be cast to android.graphics.drawable.AnimationDrawable
                    AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                    animationDrawable.stop();
                    imageView.setImageResource(R.drawable.ease_chatfrom_voice_playing_f3);
                    isShiTing = false;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/

    private void resendPaidan(final MySendPaidanInfo info) {
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_UPDATE_KUAISUDINGDAN;
        //http://192.168.1.120/speedList/updateList?sId=62&uId=15513994458&content=123&label=&log=1&lat=1&state=00&display=01&distance=200&totalNumber=300
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "resendPaidan" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "resendPaidan volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sId", info.getsId());
                params.put("uId", info.getuId());
                params.put("content", info.getContent());
                params.put("display", "00");
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);

    }

    private void deletePaidan(final String s_id) {
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_DELETE_PAIDAN_BY_ID;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                Log.d("chen", "删除派单" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "deletePaidan volleyError" + volleyError.getMessage());
                ToastUtils.showNOrmalToast(getApplicationContext(), "删除订单失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("s_id", s_id);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void insertReason(final String sId, final String type) {
        Log.d("chen", "开始插入type" + type + "sId" + sId + "uid" + DemoHelper.getInstance().getCurrentUsernName());
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_INSERTREASON;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (dialog2.isShowing()) {
                    dialog2.dismiss();
                }
                Log.d("chen", "插入原因" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "插入原因失败" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //sId=&uId=&type=
                params.put("sId", sId);
                params.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("type", type);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void deletePushType19(final String s_id) {
        Log.d("chen", "deletePushType19" + s_id);
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_DELETE_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "删除我发出派单的推送" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "deletePushType19 volleyError" + volleyError.getMessage());
                ToastUtils.showNOrmalToast(getApplicationContext(), "删除我发出派单的推送失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                params.put("type", "19");
                //这里传companyId  我为了便于管理用的是companyName   只是名字不一样，获取的数据其实还是这里传companyId
                params.put("companyId", s_id);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

}
