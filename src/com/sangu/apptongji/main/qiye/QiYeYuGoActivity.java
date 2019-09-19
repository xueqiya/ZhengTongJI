package com.sangu.apptongji.main.qiye;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuYuanGongLocationActivity;
import com.sangu.apptongji.main.qiye.adapter.MemberTwoAdapter;
import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.main.qiye.presenter.IMemberPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.MemberPresenter;
import com.sangu.apptongji.main.qiye.view.IMemberView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-30.
 */

public class QiYeYuGoActivity extends BaseActivity implements IMemberView{
    private XRecyclerView mRecyclerView;
    private MemberTwoAdapter adapter;
    private List<MemberInfo> memberInfos;
    private IMemberPresenter memberPresenter;
    private String companyId,orderId,totalAmt,filepath,orderDesc,biaoshi,cusName,cusPhone,cusAdress,infoPrice,paidanName,paidanId,companyName,companyAdress;
    private String lng, lat;
    private MapView mMapView=null;
    BitmapDescriptor mCurrentMarker;
    private ArrayList<String> imagePaths1=null;
    private ArrayList<String> imagePaths2=null;
    private BaiduMap mBaiduMap;
    private UiSettings mUiSettings;
    private CustomProgressDialog mProgress=null;
    private int currentPage=1;
    private boolean isNext = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_qiye_chengyuan);
        WeakReference<QiYeYuGoActivity> reference =  new WeakReference<QiYeYuGoActivity>(QiYeYuGoActivity.this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        memberPresenter = new MemberPresenter(this,reference.get());
        mProgress = CustomProgressDialog.createDialog(reference.get());
        imagePaths1 = new ArrayList<>();
        imagePaths2 = new ArrayList<>();
        mProgress.setMessage("正在派单...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        companyName = this.getIntent().hasExtra("companyName")?this.getIntent().getStringExtra("companyName"):"";
        orderId = this.getIntent().hasExtra("orderId")?this.getIntent().getStringExtra("orderId"):"";
        totalAmt = this.getIntent().hasExtra("totalAmt")?this.getIntent().getStringExtra("totalAmt"):"";
        biaoshi = this.getIntent().hasExtra("biaoshi")?getIntent().getStringExtra("biaoshi"):"";
        filepath = this.getIntent().hasExtra("filepath")?getIntent().getStringExtra("filepath"):"";
        orderDesc = this.getIntent().hasExtra("orderDesc")?getIntent().getStringExtra("orderDesc"):"0";
        companyId = this.getIntent().hasExtra("companyId")?getIntent().getStringExtra("companyId"):"";
        cusName = this.getIntent().hasExtra("cusName")?getIntent().getStringExtra("cusName"):"";
        cusPhone = this.getIntent().hasExtra("cusPhone")?getIntent().getStringExtra("cusPhone"):"";
        cusAdress = this.getIntent().hasExtra("cusAdress")?getIntent().getStringExtra("cusAdress"):"";
        infoPrice = this.getIntent().hasExtra("infoPrice")?getIntent().getStringExtra("infoPrice"):"0";
        imagePaths1 = this.getIntent().hasExtra("imagePaths1")?getIntent().getStringArrayListExtra("imagePaths1"):null;
        imagePaths2 = this.getIntent().hasExtra("imagePaths2")?getIntent().getStringArrayListExtra("imagePaths2"):null;
        lng = TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng();
        lat = TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.763711" : DemoApplication.getApp().getCurrentLat();
        mRecyclerView = (XRecyclerView) this.findViewById(R.id.refresh_list);
        mMapView = (MapView) findViewById(R.id.bmapView1);
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
                currentPage = 1;
                isNext = false;
                if (memberPresenter!=null) {
                    memberPresenter.loadMemberList(currentPage);
                }
            }

            @Override
            public void onLoadMore() {
                currentPage++;
                isNext = true;
                if (memberPresenter!=null) {
                    memberPresenter.loadMemberList(currentPage);
                }
            }
        });
        mRecyclerView.refresh(false);
        if (!"操作员工".equals(orderId)) {
            mMapView.setVisibility(View.VISIBLE);
            showHeadView();
            showMarker(lat, lng);
        }
        memberPresenter.loadMemberList(1);
    }

    private void showHeadView() {
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setAllGesturesEnabled(false);
        //设置是否显示比例尺控件
        mMapView.showScaleControl(false);
        //设置是否显示缩放控件
        mMapView.showZoomControls(false);
    }
    private void showMarker(String lat, String lng) {
        mBaiduMap.clear();
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        LatLng p = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        mMapView = new MapView(QiYeYuGoActivity.this,
                new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                        .target(p).build()));
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(p);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();
        MarkerOptions option = new MarkerOptions().position(convertLatLng).icon(mCurrentMarker);
        mBaiduMap.addOverlay(option);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
        mBaiduMap.animateMapStatus(u);
    }
    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void updateUserList(final List<MemberInfo> users,boolean hasMore) {
        String resv6 ="";
        String id = DemoHelper.getInstance().getCurrentUsernName();
        for (int i =0;i<users.size();i++){
            String uId= users.get(i).getuLoginId();
            if (uId.equals(id)){
                resv6 = users.get(i).getResv6();
            }
        }
        if (memberInfos==null){
            memberInfos = new ArrayList<>();
        }
        if (isNext) {
            memberInfos.addAll(users);
            mRecyclerView.loadMoreComplete();
            adapter.notifyDataSetChanged();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        }else {
            this.memberInfos = users;
            adapter = new MemberTwoAdapter(memberInfos, this);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.refreshComplete();
            if (!hasMore) {
                mRecyclerView.setNoMore(true);
            }
            if (!"操作员工".equals(orderId)) {
                if (users.size() > 0) {
                    int j = users.size();
                    if (users.size() > 20) {
                        j = 20;
                    }
                    final String[] strLat = new String[j + 1];
                    final String[] strLong = new String[j + 1];
                    final String[] strLoginId = new String[j + 1];
                    final String[] strName = new String[j + 1];
                    final String[] strSex = new String[j + 1];
                    for (int i = 0; i < j; i++) {
                        strLong[i] = users.get(i).getResv1();
                        strLat[i] = users.get(i).getResv2();
                        strLoginId[i] = users.get(i).getuLoginId();
                        strName[i] = users.get(i).getuName();
                        strSex[i] = TextUtils.isEmpty(users.get(i).getuSex()) ? "01" : users.get(i).getuSex();
                    }
                    for (int i = 0; i < users.size(); i++) {
                        String id1 = users.get(i).getuLoginId();
                        if (id1.equals(id)) {
                            strLong[j] = users.get(i).getResv1();
                            strLat[j] = users.get(i).getResv2();
                            strLoginId[j] = users.get(i).getuLoginId();
                            strName[j] = users.get(i).getuName();
                            strSex[j] = TextUtils.isEmpty(users.get(i).getuSex()) ? "01" : users.get(i).getuSex();
                        }
                    }
                    mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            Intent intent = new Intent(QiYeYuGoActivity.this, BaiDuYuanGongLocationActivity.class);
                            intent.putExtra("lat", strLat);
                            intent.putExtra("lng", strLong);
                            intent.putExtra("loginId", strLoginId);
                            intent.putExtra("name", strName);
                            intent.putExtra("sex", strSex);
                            intent.putExtra("companyId", companyId);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("totalAmt", totalAmt);
                            startActivityForResult(intent, 1);
                        }

                        @Override
                        public boolean onMapPoiClick(MapPoi mapPoi) {
                            return false;
                        }
                    });
                }
            }
            if ("操作员工".equals(orderId)) {
                if ("01".equals(biaoshi)) {
                    adapter.setOnItemClickListener(new MemberTwoAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            MemberInfo users = memberInfos.get(position - 1);
                            final String userId = users.getuLoginId();
                            final String name = TextUtils.isEmpty(users.getuName()) ? userId : users.getuName();
                            new AlertDialog.Builder(QiYeYuGoActivity.this)
                                    .setTitle("确认")
                                    .setMessage("确认派单给该用户？")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            insertDataWithMany(name, userId);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    });
                } else {
                    final String finalResv = resv6;
                    adapter.setOnItemClickListener(new MemberTwoAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            MemberInfo users = memberInfos.get(position - 1);
                            String userId = users.getuLoginId();
                            String name = users.getuName();
                            String sex = users.getuSex();
                            String resv6 = users.getResv6();
                            if ("00".equals(finalResv)) {
                                startActivityForResult(new Intent(QiYeYuGoActivity.this, YuanGongDetailActivity.class).putExtra("companyId", companyId)
                                        .putExtra("userId", userId).putExtra("userName", name).putExtra("sex", sex).putExtra("resv6", resv6), 0);
                            } else {
                                startActivity(new Intent(QiYeYuGoActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, userId));
                            }
                        }
                    });
                }
            } else {
                adapter.setOnItemClickListener(new MemberTwoAdapter.MyItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MemberInfo users = memberInfos.get(position - 1);
                        String hxid = users.getuLoginId();
                        String name = users.getuName();
                        Intent intent = new Intent(QiYeYuGoActivity.this, QitePaidanActivity.class);
                        intent.putExtra("companyId", companyId);
                        intent.putExtra("companyName", companyName);
                        intent.putExtra("companyAdress", companyAdress);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("userId", hxid);
                        intent.putExtra("name", name);
                        intent.putExtra("totalAmt", totalAmt);
                        startActivity(intent);
                    }
                });
            }
        }
    }
    private void insertDataWithMany(final String userName, final String userId) {
        if (mProgress!=null){
            if (!mProgress.isShowing()){
                mProgress.setMessage("正在派单，请稍等...");
                mProgress.show();
            }
        }else {
            WeakReference<QiYeYuGoActivity> reference =  new WeakReference<QiYeYuGoActivity>(QiYeYuGoActivity.this);
            mProgress = CustomProgressDialog.createDialog(reference.get());
            mProgress.setMessage("正在派单，请稍等...");
            mProgress.show();
            mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mProgress.dismiss();
                }
            });
        }
        String orderInfo = null,cusInfo=null;
        orderInfo = orderDesc+"|"+infoPrice+"|"+"0";
        cusInfo = cusName+"|"+cusPhone+"|"+cusAdress;
        final List<File> files = new ArrayList<File>();
        if (filepath!=null&&!"".equals(filepath)) {
            File file = null;
            file = new File(filepath);
            if (file.exists()) {
                files.add(file);
            }
        }
        final List<File> files1 = new ArrayList<File>();
        if (imagePaths1.size()>0){
            File file = null;
            file = new File(imagePaths1.get(0));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>1){
            File file = null;
            file = new File(imagePaths1.get(1));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>2){
            File file = null;
            file = new File(imagePaths1.get(2));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>3){
            File file = null;
            file = new File(imagePaths1.get(3));
            if (file.exists()) {
                files1.add(file);
            }
        }
        final List<File> files2 = new ArrayList<File>();
        if (imagePaths2.size() > 0) {
            File file = null;
            file = new File(imagePaths2.get(0));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 1) {
            File file = null;
            file = new File(imagePaths2.get(1));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 2) {
            File file = null;
            file = new File(imagePaths2.get(2));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 3) {
            File file = null;
            file = new File(imagePaths2.get(3));
            if (file.exists()) {
                files2.add(file);
            }
        }
        List<Param> param=new ArrayList<>();
        param.add(new Param("companyId",companyId));
        param.add(new Param("ordName",userName));
        param.add(new Param("ordId", userId));
        param.add(new Param("sendIdentify","1"));
        param.add(new Param("cusInfo", cusInfo));
        param.add(new Param("orderInfo", orderInfo));
        param.add(new Param("Identification", "1"));
        String url = FXConstant.URL_INSERT_OFFSEND;
        String str = "signature1";
        String str1 = "beforeService";
        String str2 = "afterService";
        OkHttpManager.getInstance().postThfile(str, files, param, str1, files1, str2, files2, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                String code = jsonObject.getString("code");
                if ("success".equals(code)){
                    sendPushMessage(userId);
                    updateBmob(userId);
                    Toast.makeText(QiYeYuGoActivity.this,"派单成功！",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                        mProgress=null;
                    }
                }
                Toast.makeText(QiYeYuGoActivity.this,"网络连接错误,派单失败",Toast.LENGTH_SHORT).show();
                if (errorMsg!=null) {
                    Log.e("offlineorder,e", errorMsg);
                }
            }
        });
    }

    private void sendPushMessage(final String hxid1) {
        String comId = "0";
        if (companyId!=null&&!"".equals(companyId)){
            comId = companyId;
        }
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        final String finalComId = comId;
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                param.put("u_id",hxid1);
                param.put("body","派单消息");
                param.put("type","05");
                param.put("userId",myId);
                param.put("companyId", finalComId);
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(QiYeYuGoActivity.this).addToRequestQueue(request);
    }

    private void updateBmob(final String userId) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                duanxintongzhi(userId,"【正事多】 通知:您有一条新的派单消息");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
                duanxintongzhi(userId,"【正事多】 通知:您有一条新的派单消息");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("offSendOrderCount","1");
                param.put("userId",userId);
                return param;
            }
        };
        MySingleton.getInstance(QiYeYuGoActivity.this).addToRequestQueue(request);
    }

    private void duanxintongzhi(final String id, final String message) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message",message);
                param.put("telNum",id);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(QiYeYuGoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currentPage=1;
        isNext = false;
        memberPresenter.loadMemberList(currentPage);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgress!=null){
            if (mProgress.isShowing()){
                mProgress.dismiss();
            }
            mProgress=null;
        }
        if (memberInfos!=null){
            memberInfos.clear();
            memberInfos=null;
        }
        if (mMapView!=null){
            mUiSettings=null;
            mMapView.onDestroy();
            mMapView=null;
        }
        if (mCurrentMarker!=null){
            mCurrentMarker.recycle();
        }
        if (imagePaths1!=null){
            imagePaths1.clear();
            imagePaths1=null;
        }
        if (imagePaths2!=null){
            imagePaths2.clear();
            imagePaths2=null;
        }
    }
}
