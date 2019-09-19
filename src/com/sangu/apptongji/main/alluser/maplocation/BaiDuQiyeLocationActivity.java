package com.sangu.apptongji.main.alluser.maplocation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.qiye.QingjiaActivity;
import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IMemberPresenter;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.MemberPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IMemberView;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.JSONUtil;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;
import com.sangu.apptongji.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2016-10-13.
 */

public class BaiDuQiyeLocationActivity extends BaseActivity implements OnMapClickListener,IQiYeDetailView,IMemberView{
    private MapView mMapView = null;
    private TextView tvTitle = null;
    LocationClient mLocClient = null;
    LocationClientOption option = null;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位
    private BaiduMap mBaiduMap = null;
    private InfoWindow mInfoWindow = null;
    private LinearLayout ll_search = null;
    private double mLat1, mLon1;
    private IMemberPresenter memberPresenter=null;
    String strId=null;
    String currentId = DemoHelper.getInstance().getCurrentUsernName();
    String signState = "";
    String signTime = "";
    String nowTime = getcurrentTime().substring(0,8);
    private Dialog mWeiboDialog;
    private String shBzhutai = null;
    List<String> strLat = new ArrayList<>(), strLong = new ArrayList<>(), strLoginId = new ArrayList<>(), strName = new ArrayList<>(), strSex = new ArrayList<>();
    String signRemark = null;
    private TextView tvmaj1 = null, tvmaj2 = null, tvmaj3 = null, tvmaj4 = null, tv_titl = null, tv_zy1_bao = null, tv_zy2_bao = null, tv_zy3_bao = null,
            tv_zy4_bao = null, tvName = null, tvsign = null, tvAge = null, tv_bottom = null, tv_distance = null, iv_zy1_tupian = null,
            iv_zy2_tupian = null, iv_zy3_tupian = null, iv_zy4_tupian = null, tv_title;
    private TextView tvmaj13 = null, tvmaj23 = null, tvmaj33 = null, tvmaj43 = null, tv_titl3 = null, tv_zy1_bao3 = null, tv_zy2_bao3 = null, tv_zy3_bao3 = null,
            tv_zy4_bao3 = null, tvName3 = null, tvsign3 = null, tvAge3 = null, tv_bottom3 = null, tv_distance3 = null, iv_zy1_tupian3 = null,
            iv_zy2_tupian3 = null, iv_zy3_tupian3 = null, iv_zy4_tupian3 = null, tv_company_count3, tv_company3;
    private Button btnXq = null, btnDh = null;
    private Button btnXq3 = null, btnDh3 = null;
    private CircleImageView ivAvatar = null,ivAvatar3;
    private ImageView ivSex3 = null,ivSex;
    private IQiYeInfoPresenter qiYeInfoPresenter = null;
    View vi2 = null, vi3 = null;
    List<Marker> oa = new ArrayList<>();
    Marker oaQiye;
    private boolean isWifiConnected = false;
    private boolean isFinished = false;
    private boolean isLFinished = false;
    private boolean isdistance = false;
    private boolean isFirst = true;
    private LocationMode mCurrentMode;
    private boolean isFenxiang = false;

    private QiYeInfo companyInfo;
    private MemberInfo memberInfo;
    private String comClockInfo;

    private String shangbanTime, xiabanTime, qiyeLat = "", qiyeLon = "", fenxiangPic, managerId;
    private Handler myMaphandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    com.alibaba.fastjson.JSONObject object = JSON.parseObject(comClockInfo);

                    String latitude = object.getString("latitude");
                    String longitude = object.getString("longitude");

                    //qiyeLat == null || qiyeLon == null
                    if (latitude == null || longitude == null || "".equals(latitude) || "".equals(longitude)) {
                        Toast.makeText(BaiDuQiyeLocationActivity.this, "企业未设置签到地址！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    double lat = Double.parseDouble(latitude);
                    double lon = Double.parseDouble(longitude);
                    final LatLng ll = JSONUtil.convertGPSToBaidu(new LatLng(lat, lon));
                    final MarkerOptions mop = new MarkerOptions()
                            .position(ll)
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.icon_geo))
                            .zIndex(9)
                            .draggable(false);
                    oaQiye = (Marker) mBaiduMap.addOverlay(mop);
                    double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));
                    if (distance <= 1000) {
                        String str = String.format("%.2f", distance);
                        tv_distance.setText(str + "m");
                    } else if (distance<10000000){
                        String str = String.format("%.2f", distance / 1000);
                        if (str!=null&&Double.parseDouble(str)/1000>=10000){
                            tv_distance.setText("隐藏");
                        }else {
                            tv_distance.setText(str + "km");
                        }
                    }else {
                        tv_distance.setText("隐藏");
                    }
                    if (isFinished && isFirst) {
                        isFirst = false;
                        MapStatus mMapStatus = new MapStatus.Builder()
                                .target(ll)
                                .zoom(15.0f)
                                .build();
                        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                        //改变地图状态
                        mBaiduMap.setMapStatus(mMapStatusUpdate);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mInfoWindow = new InfoWindow(vi2, ll, -47);
                                            mBaiduMap.showInfoWindow(mInfoWindow);
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                    break;
            }
        }
    };

    @Override
    public void onMapClick(LatLng latLng) {
        if (mInfoWindow != null) {
            mBaiduMap.hideInfoWindow();
        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_baidumap);
        qiYeInfoPresenter = new QiYeInfoPresenter(this,this);
        memberPresenter = new MemberPresenter(this,this);
        SharedPreferences sp2 = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        String qiyeId = sp2.getString("qiyeId","");
        mCurrentMode = LocationMode.NORMAL;
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        tv_bottom = (TextView) findViewById(R.id.tv_bottom);
        tv_title = (TextView) findViewById(R.id.tv_title);
        vi2 = LayoutInflater.from(this).inflate(R.layout.item_find_fragments1, null);
        vi3 = LayoutInflater.from(this).inflate(R.layout.item_find_fragments, null);
        tv_title.setText("签到详情");
        mMapView = (MapView) findViewById(R.id.bmapView);
        mCurrentMode = LocationMode.NORMAL;
        mMapView.removeViewAt(1);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setOnMapClickListener(this);
        initMapView();
        initV2();
        initV3();
        qiYeInfoPresenter.loadQiYeInfo(qiyeId);
        memberPresenter.loadMemberList(0);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        tv_bottom.setText("正事多—考勤管理系统");
        ll_search.setVisibility(View.GONE);
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                for (int i = 0; i < oa.size(); i++) {
                    if (marker.equals(oaQiye)) {
                        if (qiyeLat == null || qiyeLon == null || "".equals(qiyeLat) || "".equals(qiyeLon)) {
                            Toast.makeText(BaiDuQiyeLocationActivity.this, "企业未设置签到地址！", Toast.LENGTH_SHORT).show();
                        } else {
                            double lat = Double.parseDouble(qiyeLat);
                            double lon = Double.parseDouble(qiyeLon);
                            final LatLng ll = JSONUtil.convertGPSToBaidu(new LatLng(lat, lon));
                            MapStatus mMapStatus = new MapStatus.Builder()
                                    .target(ll)
                                    .build();
                            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                            //改变地图状态
                            mBaiduMap.setMapStatus(mMapStatusUpdate);
                            mInfoWindow = new InfoWindow(vi2, ll, -47);
                            mBaiduMap.showInfoWindow(mInfoWindow);
                        }
                    } else if (marker.equals(oa.get(i))) {
                        String id = strLoginId.get(i);
                        String url = FXConstant.URL_Get_UserInfo + id;
                        final int finalI = i;
                        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                marker.setToTop();
                                LatLng ll1 = marker.getPosition();
                                double lat = ll1.latitude + 0.0001;
                                double lng = ll1.longitude;
                                LatLng ll2 = new LatLng(lat, lng);
                                MapStatus mMapStatus = new MapStatus.Builder()
                                        .target(ll2)
                                        .build();
                                //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                //改变地图状态
                                mBaiduMap.setMapStatus(mMapStatusUpdate);
                                mInfoWindow = new InfoWindow(vi3, ll2, -100);
                                mBaiduMap.showInfoWindow(mInfoWindow);
                                setView(s, strLat.get(finalI), strLong.get(finalI));
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //callback.onError("获取数据失败");
                            }
                        });
                        MySingleton.getInstance(BaiDuQiyeLocationActivity.this).addToRequestQueue(request);
                    }
                }
                return true;
            }
        });
    }


    private void GetCompanyListInfo(){


        String url = FXConstant.URL_QUERY_QIYEMAJAR;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("网络连接错误", volleyError + "");
                Toast.makeText(BaiDuQiyeLocationActivity.this, "网络连接错误...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                return params;
            }
        };
        MySingleton.getInstance(BaiDuQiyeLocationActivity.this).addToRequestQueue(request);


    }


    private void setView(String s, final String strLat, final String strLon) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject object = jsonObject.getJSONObject("userInfo");
            Userful user2 = JSONParser.parseUser(object);
            final String id = user2.getLoginId();
            String imageStr = TextUtils.isEmpty(user2.getImage()) ? "" : user2.getImage();
            String name = TextUtils.isEmpty(user2.getName()) ? user2.getLoginId() : user2.getName();
            String sex = TextUtils.isEmpty(user2.getSex()) ? "" : user2.getSex();
            String ZY1 = TextUtils.isEmpty(user2.getUpName1()) ? "" : user2.getUpName1();
            String ZY2 = TextUtils.isEmpty(user2.getUpName2()) ? "" : user2.getUpName2();
            String ZY3 = TextUtils.isEmpty(user2.getUpName3()) ? "" : user2.getUpName3();
            String ZY4 = TextUtils.isEmpty(user2.getUpName4()) ? "" : user2.getUpName4();
            String sign = TextUtils.isEmpty(user2.getSignaTure()) ? "" : user2.getSignaTure();
            String age = TextUtils.isEmpty(user2.getuAge()) ? "27" : user2.getuAge();
            String image1 = TextUtils.isEmpty(user2.getZyImage1()) ? "" : user2.getZyImage1();
            String image2 = TextUtils.isEmpty(user2.getZyImage2()) ? "" : user2.getZyImage2();
            String image3 = TextUtils.isEmpty(user2.getZyImage3()) ? "" : user2.getZyImage3();
            String image4 = TextUtils.isEmpty(user2.getZyImage4()) ? "" : user2.getZyImage4();
            String margan1 = TextUtils.isEmpty(user2.getMargin1()) ? "" : user2.getMargin1();
            String margan2 = TextUtils.isEmpty(user2.getMargin2()) ? "" : user2.getMargin2();
            String margan3 = TextUtils.isEmpty(user2.getMargin3()) ? "" : user2.getMargin3();
            String margan4 = TextUtils.isEmpty(user2.getMargin3()) ? "" : user2.getMargin3();
            if (!"".equals(image1) && image1 != null) {
                iv_zy1_tupian3.setVisibility(View.VISIBLE);
            } else {
                iv_zy1_tupian3.setVisibility(View.INVISIBLE);
            }
            if (margan1 != null) {
                if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                    tv_zy1_bao3.setVisibility(View.VISIBLE);
                } else {
                    iv_zy1_tupian3.setVisibility(View.INVISIBLE);
                }
            }
            if (!"".equals(image2) && image2 != null) {
                iv_zy2_tupian3.setVisibility(View.VISIBLE);
            } else {
                iv_zy2_tupian3.setVisibility(View.INVISIBLE);
            }
            if (margan2 != null) {
                if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                    tv_zy2_bao3.setVisibility(View.VISIBLE);
                } else {
                    tv_zy2_bao3.setVisibility(View.INVISIBLE);
                }
            }
            if (!"".equals(image3) && image3 != null) {
                iv_zy3_tupian3.setVisibility(View.VISIBLE);
            } else {
                iv_zy3_tupian3.setVisibility(View.INVISIBLE);
            }
            if (margan3 != null) {
                if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                    tv_zy3_bao3.setVisibility(View.VISIBLE);
                } else {
                    tv_zy3_bao3.setVisibility(View.INVISIBLE);
                }
            }
            if (!"".equals(image4) && image4 != null) {
                iv_zy4_tupian3.setVisibility(View.VISIBLE);
            } else {
                iv_zy4_tupian3.setVisibility(View.INVISIBLE);
            }
            if (margan4 != null) {
                if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                    tv_zy4_bao3.setVisibility(View.VISIBLE);
                } else {
                    tv_zy4_bao3.setVisibility(View.INVISIBLE);
                }
            }
            if (("00").equals(sex)) {
                ivSex3.setImageResource(R.drawable.nv);
                tvAge3.setBackgroundColor(Color.rgb(234, 121, 219));
                tv_titl3.setBackgroundResource(R.drawable.fx_bg_text_red);
            } else {
                ivSex3.setImageResource(R.drawable.nan);
                tvAge3.setBackgroundColor(Color.rgb(0, 172, 255));
                tv_titl3.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }
            if (!imageStr.equals("")) {
                String[] images = imageStr.split("\\|");
                ivAvatar3.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[0], ivAvatar3, DemoApplication.mOptions);
            } else {
                ivAvatar3.setVisibility(View.INVISIBLE);
                tv_titl3.setVisibility(View.VISIBLE);
                tv_titl3.setText(name);
            }
            String company = TextUtils.isEmpty(user2.getCompany()) ? "暂未加入企业" : user2.getCompany();
            String uNation = user2.getuNation();
            String resv5 = user2.getResv5();
            String resv6 = user2.getResv6();
            if (company == null || company.equals("")) {
                company = "暂未加入企业";
            }
            if ("00".equals(resv6)&&!"1".equals(uNation)){
                company = "暂未加入企业";
            }
            if (resv5==null||"".equals(resv5)){
                company = "暂未加入企业";
            }
            try {
                company = URLDecoder.decode(company, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String member = user2.getMenberNum();
            if (member == null || "".equals(member)) {
                member = "0";
            }
            if (!company.equals("暂未加入企业")) {
                tv_company_count3.setVisibility(View.VISIBLE);
            }
            if (strLat != null && String.valueOf(mLat1) != null) {
                if (!("".equals(strLat) || "".equals(strLon) || mLat1 == 0.0)) {
                    double latitude1 = Double.valueOf(strLat);
                    double longitude1 = Double.valueOf(strLon);
                    final LatLng ll1 = new LatLng(mLat1, mLon1);
                    LatLng ll = new LatLng(latitude1, longitude1);
                    double distance = DistanceUtil.getDistance(ll, ll1);
                    double dou = distance / 1000;
                    if (dou<10000) {
                        String str = String.format("%.2f", dou);//format 返回的是字符串
                        if (str!=null&&dou>=10000){
                            tv_distance.setText("隐藏");
                        }else {
                            tv_distance.setText(str + "km");
                        }
                    }else {
                        tv_distance3.setText("隐藏");
                    }
                } else {
                    tv_distance3.setText("3km以外");
                }
            } else {
                tv_distance3.setText("3km以外");
            }
            tv_company_count3.setText("(" + member + ")");
            tv_company3.setText(company);
            tvAge3.setText(age);
            tvmaj13.setText(ZY1);
            tvmaj23.setText(ZY2);
            tvmaj33.setText(ZY3);
            tvmaj43.setText(ZY4);
            tvName3.setText(name);
            tvsign3.setText(sign);
            String shareRed = user2.getShareRed();
            if (shareRed != null && !"".equals(shareRed) && !shareRed.equalsIgnoreCase("null") && Double.parseDouble(shareRed) > 0) {
                tvName3.setTextColor(Color.RED);
            } else {
                tvName3.setTextColor(Color.BLACK);
            }
            btnDh3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNavi(Double.valueOf(strLat), Double.valueOf(strLon));
                    //finish();
                }
            });
            btnXq3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaiDuQiyeLocationActivity.this, UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID, id);
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startNavi(double lat, double Lng) {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(lat, Lng);
        // 构建 导航参数
        RouteParaOption para = new RouteParaOption()
                .startPoint(pt1).endPoint(pt2);
        try {
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQiyeInfo(QiYeInfo user2){

        companyInfo = user2;

        String companyShortName = TextUtils.isEmpty(user2.getCompanyShortName()) ? "0" : user2.getCompanyShortName();
        if ("1".equals(companyShortName)) {
            isFenxiang = true;
            fenxiangPic = user2.getResv6();
        }
        qiyeLat = user2.getComLatitude();
        qiyeLon = user2.getComLongitude();
        String loginTime = TextUtils.isEmpty(user2.getLoginTime()) ? null : user2.getLoginTime();
        Log.d("chen", "loginTime" + loginTime);
        if (loginTime!=null&&!"".equals(loginTime)&&loginTime.length() > 0) {
            shangbanTime = loginTime.split("\\|")[0];
            xiabanTime = loginTime.split("\\|")[1];
        }
        final String name = TextUtils.isEmpty(user2.getCompanyName()) ? "" : user2.getCompanyName();
        String imageStr = TextUtils.isEmpty(user2.getComImage()) ? "" : user2.getComImage();
        String ZY1 = TextUtils.isEmpty(user2.getUpName1()) ? "" : user2.getUpName1();
        String ZY2 = TextUtils.isEmpty(user2.getUpName2()) ? "" : user2.getUpName2();
        String ZY3 = TextUtils.isEmpty(user2.getUpName3()) ? "" : user2.getUpName3();
        String ZY4 = TextUtils.isEmpty(user2.getUpName4()) ? "" : user2.getUpName4();
        String sign = TextUtils.isEmpty(user2.getComSignature()) ? "" : user2.getComSignature();
        String image1 = TextUtils.isEmpty(user2.getZyImage1()) ? "" : user2.getZyImage1();
        String image2 = TextUtils.isEmpty(user2.getZyImage2()) ? "" : user2.getZyImage2();
        String image3 = TextUtils.isEmpty(user2.getZyImage3()) ? "" : user2.getZyImage3();
        String image4 = TextUtils.isEmpty(user2.getZyImage4()) ? "" : user2.getZyImage4();
        String margan1 = TextUtils.isEmpty(user2.getMargin1()) ? "" : user2.getMargin1();
        String margan2 = TextUtils.isEmpty(user2.getMargin2()) ? "" : user2.getMargin2();
        String margan3 = TextUtils.isEmpty(user2.getMargin3()) ? "" : user2.getMargin3();
        String margan4 = TextUtils.isEmpty(user2.getMargin3()) ? "" : user2.getMargin3();
        String name1 = null;
        try {
            name1 = URLDecoder.decode(name, "UTF-8");
            sign = URLDecoder.decode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!"".equals(image1) && image1 != null) {
            iv_zy1_tupian.setVisibility(View.VISIBLE);
        }
        if (margan1 != null) {
            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                tv_zy1_bao.setVisibility(View.VISIBLE);
            }
        }
        if (!"".equals(image2) && image2 != null) {
            iv_zy2_tupian.setVisibility(View.VISIBLE);
        }
        if (margan2 != null) {
            if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                tv_zy2_bao.setVisibility(View.VISIBLE);
            }
        }
        if (!"".equals(image3) && image3 != null) {
            iv_zy3_tupian.setVisibility(View.VISIBLE);
        }
        if (margan3 != null) {
            if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                tv_zy3_bao.setVisibility(View.VISIBLE);
            }
        }
        if (!"".equals(image4) && image4 != null) {
            iv_zy4_tupian.setVisibility(View.VISIBLE);
        }
        if (margan4 != null) {
            if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                tv_zy4_bao.setVisibility(View.VISIBLE);
            }
        }
        if (!imageStr.equals("")) {
            String[] images = imageStr.split("\\|");
            ivAvatar.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG + images[0], ivAvatar, DemoApplication.mOptions);
        } else {
            ivAvatar.setVisibility(View.INVISIBLE);
            tv_titl.setVisibility(View.VISIBLE);
            tv_titl.setText(name1);
        }
        tvmaj1.setText(ZY1);
        tvmaj2.setText(ZY2);
        tvmaj3.setText(ZY3);
        tvmaj4.setText(ZY4);
        tvName.setText(name1);
        tvsign.setText(sign);
        String shareRed = user2.getShareRed();
        final String friendsNumber = user2.getFriendsNumber();
        String onceJine = null;
        if (friendsNumber != null && !"".equals(friendsNumber)) {
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0 && onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
            tvName.setTextColor(Color.RED);
        } else {
            tvName.setTextColor(Color.BLACK);
        }
        btnXq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaiDuQiyeLocationActivity.this, QingjiaActivity.class).putExtra("name", name)
                        .putExtra("biaoshi", "").putExtra("managerId", managerId));
            }
        });
        isFinished = true;
    }

    private void showdshShbTishi() {
        String remark = "04";
        qiandao(remark);
    }

    private void showdwShbTishi(final String str) {
        final String remark = jisuanRemark();
        if (remark.equals("05")) {
            LayoutInflater inflater2 = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
            final Dialog dialog2 = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this).create();
            dialog2.show();
            dialog2.getWindow().setContentView(layout2);
            dialog2.setCanceledOnTouchOutside(true);
            dialog2.setCancelable(true);
            TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
            Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
            final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
            btnOK2.setText("确定");
            btnCancel2.setText("取消");
            title_tv2.setText("本次签到属于提前下班离岗,确认签到下班么?");
            btnCancel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                }
            });
            btnOK2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                    qiandao("05");
                }
            });
        } else {
            if (!isWifiConnected(getApplicationContext()) && str.equals("签到")) {
                Toast.makeText(BaiDuQiyeLocationActivity.this, "连接wifi后才可以上班签到哦！", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if ("02".equals(remark)) {
                    LayoutInflater inflater2 = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                    RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog2 = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout2);
                    dialog2.setCanceledOnTouchOutside(true);
                    dialog2.setCancelable(true);
                    TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                    Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                    final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                    btnOK2.setText("确定");
                    btnCancel2.setText("取消");
                    if (isdistance) {
                        title_tv2.setText("定位的距离与公司签到地址太远,签到会被认定为迟到，确认签到么?");
                    } else {
                        title_tv2.setText("已超出上班时间,本次签到会被认定为迟到，确认签到么?");
                    }
                    btnCancel2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    btnOK2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                            qiandao(remark);
                        }
                    });
                } else {
                    qiandao(remark);
                }
            }
        }
    }

    private void showXbTishi() {
        final String remark = jisuanRemark();
        if (remark.equals("05")) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this);
            builder.setMessage("本次签到属于提前下班离岗,确认签到下班么?");
            builder.setTitle("温馨提示");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    qiandao("05");
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this);
            builder.setMessage("确认签到下班么?");
            builder.setTitle("确认");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    qiandao(remark);
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    private void showTishi(final String zhuangtai) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this);
        builder.setMessage("您的企业设置的上下班分享图片,分享后才可以签到");
        builder.setTitle("确认分享");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                dialog.dismiss();
                fenxiang(zhuangtai);
            }
        });
        builder.show();
    }


    //点击签到的时候完全走新的判断

    private void NewsSignJudge(){

        if (companyInfo.getSignShareType().equals("00")){

            //不等于00 代表设置了先分享再签到

            //判断是必须分享还是选择性分享

            if (companyInfo.getSignShareNeed().equals("01")){

                //必须分享


            }else {

                //选择性分享
                final LayoutInflater inflater1 = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog collectionDialog = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                collectionDialog.show();
                collectionDialog.getWindow().setContentView(layout1);
                collectionDialog.setCanceledOnTouchOutside(true);
                collectionDialog.setCancelable(true);
                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                title.setText("提示");
                btnOK1.setText("确定");
                btnCancel1.setText("取消");

                title_tv1.setText("是否分享考勤情况？");

                btnCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectionDialog.dismiss();

                        //不分享 直接签到打卡
                        //直接走新的定位签到逻辑
                        NewSignByTimeAndLocation();

                    }
                });

                btnOK1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectionDialog.dismiss();

                        //分享


                    }
                });

            }

        }else {

            //等于00 代表没设置分享后再签到
            //直接继续签到步骤
            final LayoutInflater inflater1 = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
            RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
            final Dialog collectionDialog = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
            collectionDialog.show();
            collectionDialog.getWindow().setContentView(layout1);
            collectionDialog.setCanceledOnTouchOutside(true);
            collectionDialog.setCancelable(true);
            TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
            Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
            final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
            TextView title = (TextView) layout1.findViewById(R.id.tv_title);
            title.setText("提示");
            btnOK1.setText("确定");
            btnCancel1.setText("取消");

            title_tv1.setText("是否现在进行打卡？");

            btnCancel1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collectionDialog.dismiss();
                }
            });

            btnOK1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collectionDialog.dismiss();

                    //直接走新的定位签到逻辑
                    NewSignByTimeAndLocation();

                }
            });

        }

    }

    //新考勤的定位签到判断
    private void NewSignByTimeAndLocation(){

        //加一个判断是四个签到还是早晚两个签到


        //先判断是早上签到还是下午签到
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(comClockInfo);

        String comLatitude = object.getString("latitude");//纬度
        String comLongitude = object.getString("longitude");//经度

        final LatLng ll = JSONUtil.convertGPSToBaidu(new LatLng(Double.parseDouble(comLatitude), Double.parseDouble(comLongitude)));

        String setSignaTime = object.getString("setSignaTime");//该有的签到时间

        //四个签到时间
        String mornTime = object.getString("mornTime");//早上上班
        String noonTime = object.getString("noonTime");//中午下班
        String afternoonTime = object.getString("afternoonTime");//下午上班
        String nightTime = object.getString("nightTime");//晚上下班

        //早上班|晚下班|早下班|晚上班
        String[] signTimes = setSignaTime.split("\\|");

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String current =  dateFormat.format(date);
        String currentTime1 = dateFormat.format(date).substring(8,10);
        String currentTime2 = dateFormat.format(date).substring(10,12);

        String[] fristTimes = signTimes[0].split("\\:");//早上上班
        String[] secondTimes = signTimes[2].split("\\:");//中午下班
        String[] thridTimes = signTimes[3].split("\\:");//下午上班

        if (companyInfo.getSignPattern().equals("00")){

            //四个签到时间的逻辑

            //定位签到问题  用comClockInfo来查看当前员工应该有的考勤情况
            //定时是每天更新考勤

            //下午签到分为早上没签到直接按下午签到或者早上签到了 到了正常下午签到时间
            if ((mornTime != null && mornTime.length()>0) || (Double.parseDouble(currentTime1)>Double.parseDouble(secondTimes[0]) ||
                    (Double.parseDouble(currentTime1)==Double.parseDouble(secondTimes[0]) && Double.parseDouble(currentTime2)>Double.parseDouble(secondTimes[1])))){

                //中午打卡  一种是正常打卡 一种是早上已经过了下班时间
                //按下午打卡的时候需要顺便更新状态

                //小时相等  根据分钟判断是否迟到
                if (currentTime1.equals(thridTimes[0])){

                    //当前分钟小于签到分钟或者等于签到分钟
                    //例如上班时间是8点30 现在是8点29或者8点半

                    if (Double.parseDouble(currentTime2) == Double.parseDouble(thridTimes[1]) || Double.parseDouble(currentTime2) < Double.parseDouble(thridTimes[1])){

                        //判断位置是否正确

                        double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));

                        //误差大于500米就算是迟到了
                        if (distance>500){

                            LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                            final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                            dialoga.show();
                            dialoga.getWindow().setContentView(layout);
                            dialoga.setCanceledOnTouchOutside(true);
                            RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
                            RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
                            RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);

                            TextView tv_title = (TextView) dialoga.findViewById(R.id.tv_title);
                            TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
                            TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
                            TextView tv_item3 = (TextView) dialoga.findViewById(R.id.tv_item3);

                            tv_title.setText("检测到当前位置与签到地点误差较大");
                            tv_item1.setText("直接签到");
                            tv_item2.setText("提交异常");
                            tv_item3.setText("取消");

                            re_item1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();

                                    //按迟到算
                            //        NewUpdateWorkState("0202");
                            //        tongji("02", companyInfo.getCompanyId(), DemoHelper.getInstance().getCurrentUsernName());

                                }
                            });

                            re_item2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();

                                    //提交异常
                                    UploadAbnormalInfoWithType("下午上班");
                                }
                            });

                            re_item3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();
                                }
                            });


                        }else {

                            //正常下午打卡
                            NewUpdateWorkState("0102");
                        }


                    }else {

                        //迟到了
                        NewUpdateWorkState("0202");
                        tongji("02", companyInfo.getCompanyId(), DemoHelper.getInstance().getCurrentUsernName());
                    }

                }else if (Double.parseDouble(currentTime1) < Double.parseDouble(thridTimes[0])){
                    //大概就是八点多上班 当前是七点的情况
                    //判断位置直接

                    double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));

                    //误差大于500米就算是迟到了
                    if (distance>500){

                        LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                        final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                        dialoga.show();
                        dialoga.getWindow().setContentView(layout);
                        dialoga.setCanceledOnTouchOutside(true);
                        RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
                        RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
                        RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);

                        TextView tv_title = (TextView) dialoga.findViewById(R.id.tv_title);
                        TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
                        TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
                        TextView tv_item3 = (TextView) dialoga.findViewById(R.id.tv_item3);

                        tv_title.setText("检测到当前位置与签到地点误差较大");
                        tv_item1.setText("直接签到");
                        tv_item2.setText("提交异常");
                        tv_item3.setText("取消");

                        re_item1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialoga.dismiss();

                                //按迟到算


                            }
                        });

                        re_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialoga.dismiss();

                                //提交异常
                                UploadAbnormalInfoWithType("下午上班");
                            }
                        });

                        re_item3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialoga.dismiss();
                            }
                        });

                    }else {

                        //正常下午打卡
                        NewUpdateWorkState("0102");
                    }

                }else {

                    //直接按迟到处理  下午上班的迟到
                    NewUpdateWorkState("0202");
                    tongji("02", companyInfo.getCompanyId(), DemoHelper.getInstance().getCurrentUsernName());
                }

            }else {

                //早上签到


                //小时相等  根据分钟判断是否迟到
                if (currentTime1.equals(fristTimes[0])) {

                    //先判断分钟 再判断位置

                    if (Double.parseDouble(currentTime2) == Double.parseDouble(fristTimes[1]) || Double.parseDouble(currentTime2) < Double.parseDouble(fristTimes[1])){

                        //判断位置是否正确

                        double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));

                        //误差大于500米就算是迟到了
                        if (distance>500){

                            LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                            final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                            dialoga.show();
                            dialoga.getWindow().setContentView(layout);
                            dialoga.setCanceledOnTouchOutside(true);
                            RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
                            RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
                            RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);

                            TextView tv_title = (TextView) dialoga.findViewById(R.id.tv_title);
                            TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
                            TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
                            TextView tv_item3 = (TextView) dialoga.findViewById(R.id.tv_item3);

                            tv_title.setText("检测到当前位置与签到地点误差较大");
                            tv_item1.setText("直接签到");
                            tv_item2.setText("提交异常");
                            tv_item3.setText("取消");

                            re_item1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();

                                    //按迟到算


                                }
                            });

                            re_item2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();

                                    //提交异常
                                    UploadAbnormalInfoWithType("早上上班");
                                }
                            });

                            re_item3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();
                                }
                            });


                        }else {

                            //正常早上打卡
                            NewUpdateWorkState("01");
                        }


                    }else {

                        //迟到了
                        NewUpdateWorkState("02");
                        tongji("02", companyInfo.getCompanyId(), DemoHelper.getInstance().getCurrentUsernName());
                    }

                }else if (Double.parseDouble(currentTime1) < Double.parseDouble(fristTimes[0])){

                    //判断位置

                    double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));

                    //误差大于500米就算是迟到了
                    if (distance>500){

                        LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                        final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                        dialoga.show();
                        dialoga.getWindow().setContentView(layout);
                        dialoga.setCanceledOnTouchOutside(true);
                        RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
                        RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
                        RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);

                        TextView tv_title = (TextView) dialoga.findViewById(R.id.tv_title);
                        TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
                        TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
                        TextView tv_item3 = (TextView) dialoga.findViewById(R.id.tv_item3);

                        tv_title.setText("检测到当前位置与签到地点误差较大");
                        tv_item1.setText("直接签到");
                        tv_item2.setText("提交异常");
                        tv_item3.setText("取消");

                        re_item1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialoga.dismiss();

                                //按迟到算


                            }
                        });

                        re_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialoga.dismiss();

                                //提交异常
                                UploadAbnormalInfoWithType("早上上班");
                            }
                        });

                        re_item3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialoga.dismiss();
                            }
                        });


                    }else {

                        //正常早上打卡
                        NewUpdateWorkState("01");
                    }

                }else {

                    //早上迟到
                    NewUpdateWorkState("02");
                    tongji("02", companyInfo.getCompanyId(), DemoHelper.getInstance().getCurrentUsernName());

                }

            }

        }else {

            //两个签到时间的情况


            //小时相等  根据分钟判断是否迟到
            if (currentTime1.equals(fristTimes[0])) {

                //先判断分钟 再判断位置

                if (Double.parseDouble(currentTime2) == Double.parseDouble(fristTimes[1]) || Double.parseDouble(currentTime2) < Double.parseDouble(fristTimes[1])){

                    //判断位置是否正确

                    double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));

                    //误差大于500米就算是迟到了
                    if (distance>500){

                        LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                        final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                        dialoga.show();
                        dialoga.getWindow().setContentView(layout);
                        dialoga.setCanceledOnTouchOutside(true);
                        RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
                        RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
                        RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);

                        TextView tv_title = (TextView) dialoga.findViewById(R.id.tv_title);
                        TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
                        TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
                        TextView tv_item3 = (TextView) dialoga.findViewById(R.id.tv_item3);

                        tv_title.setText("检测到当前位置与签到地点误差较大");
                        tv_item1.setText("直接签到");
                        tv_item2.setText("提交异常");
                        tv_item3.setText("取消");

                        re_item1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialoga.dismiss();

                                //按迟到算


                            }
                        });

                        re_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialoga.dismiss();

                                //提交异常
                                UploadAbnormalInfoWithType("早上上班");
                            }
                        });

                        re_item3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialoga.dismiss();
                            }
                        });


                    }else {

                        //正常早上打卡
                        NewUpdateWorkState("01");
                    }


                }else {

                    //迟到了
                    NewUpdateWorkState("02");
                    tongji("02", companyInfo.getCompanyId(), DemoHelper.getInstance().getCurrentUsernName());
                }

            }else if (Double.parseDouble(currentTime1) < Double.parseDouble(fristTimes[0])){

                //判断位置

                double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));

                //误差大于500米就算是迟到了
                if (distance>500){

                    LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                    final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                    dialoga.show();
                    dialoga.getWindow().setContentView(layout);
                    dialoga.setCanceledOnTouchOutside(true);
                    RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
                    RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
                    RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);

                    TextView tv_title = (TextView) dialoga.findViewById(R.id.tv_title);
                    TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
                    TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
                    TextView tv_item3 = (TextView) dialoga.findViewById(R.id.tv_item3);

                    tv_title.setText("检测到当前位置与签到地点误差较大");
                    tv_item1.setText("直接签到");
                    tv_item2.setText("提交异常");
                    tv_item3.setText("取消");

                    re_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialoga.dismiss();

                            //按迟到算


                        }
                    });

                    re_item2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialoga.dismiss();

                            //提交异常
                            UploadAbnormalInfoWithType("早上上班");
                        }
                    });

                    re_item3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialoga.dismiss();
                        }
                    });


                }else {

                    //正常早上打卡
                    NewUpdateWorkState("01");
                }

            }else {

                //早上迟到
                NewUpdateWorkState("02");
                tongji("02", companyInfo.getCompanyId(), DemoHelper.getInstance().getCurrentUsernName());

            }

        }

    }


    //异常的时候提交审批
    private void UploadAbnormalInfoWithType(final String type){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(BaiDuQiyeLocationActivity.this, "请稍后...");

        String url = FXConstant.URL_INSERTABNORMAL;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);

                if (object.getString("code").equals("SUCCESS")){

                    //提交审批成功
                    Toast.makeText(BaiDuQiyeLocationActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

            }


        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String currentTime = dateFormat.format(date);
                String timestamp = dateFormat.format(date).substring(0,8);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(comClockInfo);
                SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
                String location = mSharedPreferences.getString("location","0");

                params.put("companyid",companyInfo.getCompanyId());
                params.put("uid",DemoHelper.getInstance().getCurrentUsernName());
                params.put("timestamp",timestamp);
                params.put("site",location);
                params.put("reason","定位问题");
                params.put("address",mLat1+"|"+mLon1);
                params.put("signtime",currentTime);

                params.put("timetype",type);

                return params;

            }
        };

        MySingleton.getInstance(BaiDuQiyeLocationActivity.this).addToRequestQueue(request);

    }




    //新的签到的时候传的参数
    //mornTime      上午上班
    //noonTime      上午下班
    //afternoonTime 下午上班
    //nightTime     下午下班
    //add1 add2 add3 add4  分别代表4个签到时间
    //  01 正常签到  02迟到  03外出（传请假字段=02）04下班  05早退
    //0102代表下午正常签到   0202代表下午上班迟到   0402代表上午下班   0502代表上午早退

    private void NewUpdateWorkState(final String state){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(BaiDuQiyeLocationActivity.this, "加载中...");

        String url = FXConstant.URL_UPDATEComClock;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);

                if (object.getString("code").equals("SUCCESS")){

                    Toast.makeText(BaiDuQiyeLocationActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    finish();

                }else {


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String currentTime = dateFormat.format(date);
                String timestamp = dateFormat.format(date).substring(0,8);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(comClockInfo);


                params.put("companyId",companyInfo.getCompanyId());
                params.put("uId",DemoHelper.getInstance().getCurrentUsernName());
                params.put("timestamp",timestamp);

                //根据不同判断传不同的参数
                if (state.equals("01")){

                    //早上上班签到
                    if (object.getString("leaveState") != null && object.getString("leaveState").equals("01")) {
                        //请假状态不计作迟到时间
                    }else {
                        //修改工作状态为工作
                        params.put("workState","01");
                    }

                    params.put("mornTime",currentTime);
                    params.put("add1",mLat1+"|"+mLon1);

                }else if (state.equals("0102")){
                    //下午上班签到

                    params.put("afternoonTime",currentTime);
                    params.put("add3",mLat1+"|"+mLon1);

                    //下午上班签到可能会出现早上没签的情况
                    //直接把早上签到中午下班签到签到位置都传过去
                    //状态也要更新

                    if (!(object.getString("mornTime") != null && object.getString("mornTime").length()>0)){


                        if (object.getString("leaveState") != null && object.getString("leaveState").equals("01")) {
                            //请假状态不计作迟到时间
                        }else {
                            //修改工作状态为工作
                            params.put("workState","01");
                        }

                        params.put("mornTime",currentTime);
                        params.put("add1",mLat1+"|"+mLon1);

                        params.put("noonTime",currentTime);
                        params.put("add2",mLat1+"|"+mLon1);

                    }

                }else if (state.equals("02")){
                    //迟到 计算迟到时间传过去做记录

                    params.put("mornTime",currentTime);
                    params.put("add1",mLat1+"|"+mLon1);

                    if (object.getString("leaveState") != null && object.getString("leaveState").equals("01")) {
                        //请假状态不计作迟到时间
                    }else {
                        //修改工作状态为工作
                        params.put("workState","01");

                        //记录迟到状态
                        params.put("lateState","01");

                        //计算迟到的时间
                        params.put("lateTime",NewCalculateWithType("早上迟到")+"");

                    }


                }else if (state.equals("0202")){

                    params.put("afternoonTime",currentTime);
                    params.put("add3",mLat1+"|"+mLon1);

                    if (object.getString("leaveState") != null && object.getString("leaveState").equals("01")) {
                        //请假状态不计作迟到时间
                    }else {
                        //修改工作状态为工作
                        params.put("workState","01");

                        //记录迟到状态
                        params.put("lateState","01");


                        if (object.getString("lateTime") != null && object.getString("lateTime").length()>0){

                            //计算迟到的时间
                            params.put("lateTime",Double.parseDouble(object.getString("lateTime"))+NewCalculateWithType("下午迟到")+"");

                        }else {

                            //计算迟到的时间
                            params.put("lateTime",NewCalculateWithType("下午迟到")+"");

                        }

                    }

                    if (!(object.getString("mornTime") != null && object.getString("mornTime").length()>0)){


                        if (object.getString("leaveState") != null && object.getString("leaveState").equals("01")) {
                            //请假状态不计作迟到时间
                        }else {
                            //修改工作状态为工作
                            params.put("workState","01");
                        }

                        params.put("mornTime",currentTime);
                        params.put("add1",mLat1+"|"+mLon1);

                        params.put("noonTime",currentTime);
                        params.put("add2",mLat1+"|"+mLon1);

                    }

                }else if (state.equals("03")){



                }else if (state.equals("04")){

                    //下午下班更新下班时间 计算一共上了多少小时班 传过去

                    //时间统计原则换：
                    //下午上班签到的时候或者上午下班的时候
                    //1、优先计算上午的时间 直接截止到十二点不管几点签到 0402得时候计算
                    //2、04是下午下班 计算下午的工作时间 然后累加上原有的时间再次更新
                    //3、下班时间到签到时间间隔就是加班时间 单独统计到加班时间里边

                    params.put("nightTime",currentTime);
                    params.put("add4",mLat1+"|"+mLon1);

                    if (object.getString("leaveState") != null && object.getString("leaveState").equals("01")) {
                        //请假状态不计作迟到时间
                    }else {

                        //改成下班 00未上班 01上班  02下班
                        params.put("workState","02");

                        if (object.getString("workTime") != null && object.getString("workTime").length()>0){

                            params.put("workTime",Double.parseDouble(object.getString("workTime"))+NewCalculateWithType("下午下班")+"");

                        }else {

                            params.put("workTime",NewCalculateWithType("下午下班")+"");

                        }

                    }

                }else if (state.equals("0402")) {

                    //上午下班只计算时间  不更新状态
                    //计算上午签到到下班的工作时间做更新  位置地址做更新
                    params.put("noonTime",currentTime);
                    params.put("add2",mLat1+"|"+mLon1);

                    if (object.getString("leaveState") != null && object.getString("leaveState").equals("01")) {
                        //请假状态不计作迟到时间
                    }else {

                        params.put("workTime",NewCalculateWithType("中午下班")+"");

                    }

                }else if (state.equals("05")) {

                    //下午下班早退 计算上班时间  计算早退时间
                    //早退时间分两次  上午早退跟下午早退

                    params.put("nightTime",currentTime);
                    params.put("add4",mLat1+"|"+mLon1);

                    if (object.getString("leaveState") != null && object.getString("leaveState").equals("01")) {
                        //请假状态不计作迟到时间
                    }else {

                        params.put("leaveEarly","01");
                        params.put("workState","02");

                        if (object.getString("leaveEarlyTime") != null && object.getString("leaveEarlyTime").length()>0){

                            params.put("leaveEarlyTime",Double.parseDouble(object.getString("leaveEarlyTime"))+NewCalculateWithType("下午早退")+"");

                        }else {

                            params.put("leaveEarlyTime",NewCalculateWithType("下午早退")+"");
                        }

                    }


                    if (object.getString("workTime") != null && object.getString("workTime").length()>0) {

                        params.put("workTime",Double.parseDouble(object.getString("workTime"))+NewCalculateWithType("下午下班")+"");

                    }else {

                        params.put("workTime",NewCalculateWithType("下午下班")+"");

                    }

                }else if (state.equals("0502")) {

                    //上午下班早退 计算上班时间  计算早退时间
                    //修改为早退状态 不修改工作状态  计算上午早退时间

                    //中午下班签到的时间
                    params.put("noonTime",currentTime);
                    params.put("add2",mLat1+"|"+mLon1);

                    if (object.getString("leaveState") != null && object.getString("leaveState").equals("01")) {
                        //请假状态不计作迟到时间
                    }else {

                        params.put("leaveEarlyTime",NewCalculateWithType("中午早退")+"");
                        params.put("workTime",NewCalculateWithType("中午下班")+"");

                    }

                }


                return params;
            }
        };

        MySingleton.getInstance(BaiDuQiyeLocationActivity.this).addToRequestQueue(request);

    }


    //计算时间相关
    private double NewCalculateWithType(String type){

        double allTime = 0;

        String currentTime = getcurrentTime();

        String current1 = currentTime.substring(8,10);
        String current2 = currentTime.substring(10,12);

        com.alibaba.fastjson.JSONObject object = JSON.parseObject(comClockInfo);

        String mornTime = object.getString("mornTime");
        String afternoonTime = object.getString("afternoonTime");
        String setSignaTime = object.getString("setSignaTime");//该有的签到时间
        //早上班|晚下班|早下班|晚上班
        String[] signTimes = setSignaTime.split("\\|");


        if (type.equals("早上迟到") || type.equals("下午迟到")){

            //计算早上迟到时间
            //分割上午签到时间arr[0]是早上签到时间  arr1[0]是小时 arr1[1]是分钟

            String[] arr1;

            if (type.equals("早上迟到")){

                arr1 = signTimes[0].split("\\:");

            }else {

                arr1 = signTimes[3].split("\\:");
            }


            //arr1[0] 时   arr1[1]分  8:10

            if (current1.equals(arr1[0])){

                //小时相同  直接计算分钟
                allTime = Double.parseDouble(current2) - Double.parseDouble(arr1[1]);

            }else{

                //当前大于签到时间 判断分大小 顺便计算分钟
                if (Double.parseDouble(current2) > Double.parseDouble(arr1[1])){

                    //时大于时  分大于分  例如8点10分上班  9点20签到
                    //当前时分都比签到时间大  直接减 算分钟

                    double hour = Double.parseDouble(current1) - Double.parseDouble(arr1[0]);

                    double minutes = Double.parseDouble(current2) - Double.parseDouble(arr1[1]);

                    allTime = hour * 60 + minutes;


                }else {

                    double hour = Double.parseDouble(current1) - Double.parseDouble(arr1[0]) - 1;

                    double minutes = Double.parseDouble(current2) + 60 - Double.parseDouble(arr1[1]);

                    allTime = hour * 60 + minutes;

                }

            }

        }else if (type.equals("中午下班")){


            //计算上班到中午下班时间
            //拆分签到时间 早上班|晚下班|早下班|晚上班  arr0123

            //下班签到时间减去上午签到时间  （暂时直接固定用下班时间减，暂不考虑中午下班签到晚的情况）

            String mornHour = mornTime.substring(8,10);
            String mornMin = mornTime.substring(10,12);

            //分割中午下班时间arr[2]  arr2[0]是小时 arr2[1]是分钟
            String[] arr2 = signTimes[2].split("\\:");

            //正常下班还是早退早退结束时间已当前为主正常下班以下班时间为主

            if (Double.parseDouble(current1) < Double.parseDouble(arr2[0]) || (Double.parseDouble(current1) == Double.parseDouble(arr2[0]) && Double.parseDouble(current2) < Double.parseDouble(arr2[1]))){

                //早退了 按当前时间计算
                //当前时间减去上班签到时间就是上班时间

                // (下班时肯定大于上班签到的时，直接判断分)
                if (Double.parseDouble(current2) > Double.parseDouble(mornMin)){

                    double hour = Double.parseDouble(current1) - Double.parseDouble(mornHour);

                    double minutes = Double.parseDouble(current2) - Double.parseDouble(mornMin);

                    allTime = hour * 60 + minutes;

                }else {

                    double hour = Double.parseDouble(current1) - Double.parseDouble(mornHour) - 1;

                    double minutes = Double.parseDouble(current2) + 60 - Double.parseDouble(mornMin);

                    allTime = hour * 60 + minutes;

                }


            }else {

                // (下班时肯定大于上班签到的时，直接判断分)
                if (Double.parseDouble(arr2[1]) > Double.parseDouble(mornMin)){

                    double hour = Double.parseDouble(arr2[0]) - Double.parseDouble(mornHour);

                    double minutes = Double.parseDouble(arr2[1]) - Double.parseDouble(mornMin);

                    allTime = hour * 60 + minutes;

                }else {

                    double hour = Double.parseDouble(arr2[0]) - Double.parseDouble(mornHour) - 1;

                    double minutes = Double.parseDouble(arr2[1]) + 60 - Double.parseDouble(mornMin);

                    allTime = hour * 60 + minutes;

                }

            }

        }else if (type.equals("中午早退") || type.equals("下午早退")){

            //中午早退  计算现在的时间距离中午下班还有多久就是早退时间
            //分割中午下班时间arr[2]  arr2[0]是小时 arr2[1]是分钟
            String[] arr2;

            if (type.equals("中午早退")) {

                arr2 = signTimes[2].split("\\:");

            }else
            {    //下午下班时间
                arr2 = signTimes[1].split("\\:");
            }

            //早退情况下  现在时间肯定小于中午下班时间

            //直接判断分钟

            if (Double.parseDouble(arr2[1]) > Double.parseDouble(current2)){

                double hour = Double.parseDouble(arr2[0]) - Double.parseDouble(current1);

                double minutes = Double.parseDouble(arr2[1]) - Double.parseDouble(current2);

                allTime = hour * 60 + minutes;

            }else {

                double hour = Double.parseDouble(arr2[0]) - Double.parseDouble(current1) - 1;

                double minutes = Double.parseDouble(arr2[1]) + 60 - Double.parseDouble(current2);

                allTime = hour * 60 + minutes;
            }

        }else if (type.equals("下午下班")){

            //分两种情况，单双签到时间区分

            //下午上班签到小时点
            String mornHour ;
            //下午上班分钟点
            String mornMin ;

            if (companyInfo.getSignPattern().equals("00")){

                //四个签到时间的情况
                 mornHour = afternoonTime.substring(8,10);
                 mornMin = afternoonTime.substring(10,12);

            }else {

                 mornHour = mornTime.substring(8,10);
                 mornMin = mornTime.substring(10,12);

            }

            //分割下午下班时间arr[1]  arr2[0]是小时 arr2[1]是分钟
            String[] arr2 = signTimes[1].split("\\:");

            //正常下班还是早退早退结束时间已当前为主正常下班以下班时间为主

            if (Double.parseDouble(current1) < Double.parseDouble(arr2[0]) || (Double.parseDouble(current1) == Double.parseDouble(arr2[0]) && Double.parseDouble(current2) < Double.parseDouble(arr2[1]))){

                //早退了 按当前时间计算
                //当前时间减去上班签到时间就是上班时间

                // (下班时肯定大于上班签到的时，直接判断分)
                if (Double.parseDouble(current2) > Double.parseDouble(mornMin)){

                    double hour = Double.parseDouble(current1) - Double.parseDouble(mornHour);

                    double minutes = Double.parseDouble(current2) - Double.parseDouble(mornMin);

                    allTime = hour * 60 + minutes;

                }else {

                    double hour = Double.parseDouble(current1) - Double.parseDouble(mornHour) - 1;

                    double minutes = Double.parseDouble(current2) + 60 - Double.parseDouble(mornMin);

                    allTime = hour * 60 + minutes;

                }


            }else {

                // (下班时肯定大于上班签到的时，直接判断分)
                if (Double.parseDouble(arr2[1]) > Double.parseDouble(mornMin)){

                    double hour = Double.parseDouble(arr2[0]) - Double.parseDouble(mornHour);

                    double minutes = Double.parseDouble(arr2[1]) - Double.parseDouble(mornMin);

                    allTime = hour * 60 + minutes;

                }else {

                    double hour = Double.parseDouble(arr2[0]) - Double.parseDouble(mornHour) - 1;

                    double minutes = Double.parseDouble(arr2[1]) + 60 - Double.parseDouble(mornMin);

                    allTime = hour * 60 + minutes;

                }

            }

        }

        return allTime;

    }


    @Override
    public void updateUserList(List<MemberInfo> users,boolean hasMore) {
        boolean isFinishma = false;
        boolean isFinishse = false;
        for (int m = 0;m<users.size();m++){
            strId = users.get(m).getResv6();
            String id = users.get(m).getuLoginId();
            if ("00".equals(strId)){
                managerId = users.get(m).getuLoginId();
                isFinishma = true;
            }
            if (id.equals(currentId)){
                signState = users.get(m).getSignInfo_signState();
                signRemark = users.get(m).getSignInfo_remark();
                signTime = users.get(m).getSignInfo_signTime();
                memberInfo = users.get(m);
                comClockInfo = users.get(m).getComClockInfo();
                isFinishse = true;
            }
            if (isFinishma&&isFinishse){
                break;
            }
        }
        if (users.size() > 0) {
            for (int i = 0;i<users.size();i++){
                String lng = users.get(i).getResv1();
                String lat = users.get(i).getResv2();
                String loginId = users.get(i).getuLoginId();
                String name = users.get(i).getuName();
                String sex = TextUtils.isEmpty(users.get(i).getuSex())?"01":users.get(i).getuSex();
                strLat.add(lat);
                strLong.add(lng);
                strLoginId.add(loginId);
                strName.add(name);
                strSex.add(sex);
            }

            //判断是签到还是没签到
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(comClockInfo);

            String type = object.getString("type");
            String mornTime = object.getString("mornTime");//早上上班
            String noonTime = object.getString("noonTime");//中午下班
            String afternoonTime = object.getString("afternoonTime");//下午上班
            String nightTime = object.getString("nightTime");//晚上下班
            String signPattern = companyInfo.getSignPattern();//判断是单还是双签到时间

            btnDh.setText("签到");
            shBzhutai = "签到";


            if (type.equals("01")){

                //01代表没什么问题  00代表正在等待被审批

                //四个签到时间

                //先判断早上有没有签到  没有的话显示签到

                if (mornTime != null && mornTime.length()>0){

                    btnDh.setText("下班签到");
                    shBzhutai = "下班签到";

                }

                if (signPattern.equals("00")){

                    //只有四个签到时间的时候才判断中午下班跟下午上班

                    //然后上午下班判断

                    if (noonTime != null && noonTime.length()>0){

                        btnDh.setText("签到");
                        shBzhutai = "签到";

                    }

                    if (afternoonTime != null && afternoonTime.length()>0){

                        btnDh.setText("下班签到");
                        shBzhutai = "下班签到";

                    }

                }

                if (nightTime != null && nightTime.length()>0){

                    btnDh.setText("已签到");
                    shBzhutai = "已签到";
                    btnDh.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                    btnDh.setEnabled(false);

                }

            }else {

                btnDh.setText("待审批");
                shBzhutai = "待审批";
                btnDh.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                btnDh.setEnabled(false);

            }


//            if (signTime.length()>0){
//                signTime = signTime.substring(0,8);
//            }
//            if (nowTime.equals(signTime)){
//                if (signState.equals("01")){
//                    shBzhutai = "下班签到";
//                }else if (signState.equals("00")){
//                    shBzhutai = "已签到";
//                }
//            }else {
//                shBzhutai = "签到";
//            }
        }
      //  btnDh.setText(shBzhutai);
        btnXq.setText("请假");
//        if (shBzhutai.equals("已签到")) {
//            btnDh.setEnabled(false);
//            btnDh.setBackgroundResource(R.drawable.fx_bg_btn_gray);
//        }
        btnDh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shBzhutai.equals("签到")) {
                   // showDialog1(shBzhutai);

                    NewsSignJudge();

                } else if (shBzhutai.equals("下班签到")) {
//                    if (isFenxiang) {
////                        showTishi("下班签到");
////                    } else {
////                        showXbTishi();
////                    }

                    AfterWorkClick();

                }
            }
        });
        isLFinished = true;
        myMaphandler.postDelayed(mapThread, 100);
    }


    private void AfterWorkClick(){


        //  先判断是否需要分享
        if (companyInfo.getSignShareType().equals("00")){

            //不等于00 代表设置了先分享再签到

            //判断是必须分享还是选择性分享

            if (companyInfo.getSignShareNeed().equals("01")){

                //必须分享


            }else {

                //选择性分享
                final LayoutInflater inflater1 = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog collectionDialog = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                collectionDialog.show();
                collectionDialog.getWindow().setContentView(layout1);
                collectionDialog.setCanceledOnTouchOutside(true);
                collectionDialog.setCancelable(true);
                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                title.setText("提示");
                btnOK1.setText("确定");
                btnCancel1.setText("取消");

                title_tv1.setText("是否分享考勤情况？");

                btnCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectionDialog.dismiss();

                        //不分享 直接打卡
                        WorkEndAboutInfo();

                    }
                });

                btnOK1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectionDialog.dismiss();

                        //分享


                    }
                });

            }

        }else {

            //等于00 代表没设置分享后再签到

            //直接打卡
            WorkEndAboutInfo();

        }

    }


    //下班签到的逻辑判断
    private void WorkEndAboutInfo(){

        final LayoutInflater inflater1 = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog collectionDialog = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
        collectionDialog.show();
        collectionDialog.getWindow().setContentView(layout1);
        collectionDialog.setCanceledOnTouchOutside(true);
        collectionDialog.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("提示");
        btnOK1.setText("确定");
        btnCancel1.setText("取消");

        title_tv1.setText("是否确认下班打卡？");

        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionDialog.dismiss();
            }
        });

        btnOK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionDialog.dismiss();

                com.alibaba.fastjson.JSONObject object = JSON.parseObject(comClockInfo);

                String comLatitude = object.getString("latitude");//纬度
                String comLongitude = object.getString("longitude");//经度

                final LatLng ll = JSONUtil.convertGPSToBaidu(new LatLng(Double.parseDouble(comLatitude), Double.parseDouble(comLongitude)));

                String setSignaTime = object.getString("setSignaTime");//该有的签到时间

                //四个签到时间
                String mornTime = object.getString("mornTime");//早上上班
                String noonTime = object.getString("noonTime");//中午下班
                String afternoonTime = object.getString("afternoonTime");//下午上班
                String nightTime = object.getString("nightTime");//晚上下班

                //早上班|晚下班|早下班|晚上班
                String[] signTimes = setSignaTime.split("\\|");

                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String current =  dateFormat.format(date);
                String currentTime1 = dateFormat.format(date).substring(8,10);
                String currentTime2 = dateFormat.format(date).substring(10,12);

                String[] fristTimes = signTimes[0].split("\\:");//早上上班
                String[] secondTimes = signTimes[2].split("\\:");//中午下班
                String[] thridTimes = signTimes[3].split("\\:");//下午上班
                String[] fourTimes = signTimes[1].split("\\:");//下午下班


                //下班签到的时候判断是中午下班签到还是晚上下班签到

                if ((object.getString("noonTime") != null && object.getString("noonTime").length() > 0 ) ||
                        companyInfo.getSignPattern().equals("01")){

                    //下午下班签到

                    //先判断当前时是大于还是小于下班规定时
                    if (Double.parseDouble(currentTime1) > Double.parseDouble(fourTimes[0])){

                        //直接判断位置

                        double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));

                        //误差大于500米就是早退了
                        if (distance>500){

                            LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                            final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                            dialoga.show();
                            dialoga.getWindow().setContentView(layout);
                            dialoga.setCanceledOnTouchOutside(true);
                            RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
                            RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
                            RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);

                            TextView tv_title = (TextView) dialoga.findViewById(R.id.tv_title);
                            TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
                            TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
                            TextView tv_item3 = (TextView) dialoga.findViewById(R.id.tv_item3);

                            tv_title.setText("检测到当前位置与签到地点误差较大");
                            tv_item1.setText("直接打卡");
                            tv_item2.setText("提交异常");
                            tv_item3.setText("取消");

                            re_item1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();


                                }
                            });

                            re_item2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();

                                    //提交异常
                                    UploadAbnormalInfoWithType("晚上下班");
                                }
                            });

                            re_item3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();
                                }
                            });


                        }else {

                            //正常晚上下班签到  04
                            NewUpdateWorkState("04");

                        }


                    }else if (Double.parseDouble(currentTime1) == Double.parseDouble(fourTimes[0])){

                        //时相同 判断分

                        if (Double.parseDouble(currentTime2) > Double.parseDouble(fourTimes[1]) || Double.parseDouble(currentTime2) == Double.parseDouble(fourTimes[1])){

                            //判断位置

                            double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));

                            //误差大于500米就是早退了
                            if (distance>500){

                                LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                                final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                                dialoga.show();
                                dialoga.getWindow().setContentView(layout);
                                dialoga.setCanceledOnTouchOutside(true);
                                RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
                                RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
                                RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);

                                TextView tv_title = (TextView) dialoga.findViewById(R.id.tv_title);
                                TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
                                TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
                                TextView tv_item3 = (TextView) dialoga.findViewById(R.id.tv_item3);

                                tv_title.setText("检测到当前位置与签到地点误差较大");
                                tv_item1.setText("直接打卡");
                                tv_item2.setText("提交异常");
                                tv_item3.setText("取消");

                                re_item1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialoga.dismiss();


                                    }
                                });

                                re_item2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialoga.dismiss();

                                        //提交异常
                                        UploadAbnormalInfoWithType("晚上下班");
                                    }
                                });

                                re_item3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialoga.dismiss();
                                    }
                                });


                            }else {

                                //正常晚上下班签到  04
                                NewUpdateWorkState("04");

                            }

                        }else {

                            //早退
                            NewUpdateWorkState("05");
                        }


                    }else {

                        //直接按早退处理
                        NewUpdateWorkState("05");

                    }


                }else {

                    //中午下班相关处理

                    //先判断当前时是大于还是小于下班规定时
                    if (Double.parseDouble(currentTime1) > Double.parseDouble(secondTimes[0])){

                        //直接判断位置

                        double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));

                        //误差大于500米就是早退了
                        if (distance>500){

                            LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                            final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                            dialoga.show();
                            dialoga.getWindow().setContentView(layout);
                            dialoga.setCanceledOnTouchOutside(true);
                            RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
                            RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
                            RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);

                            TextView tv_title = (TextView) dialoga.findViewById(R.id.tv_title);
                            TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
                            TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
                            TextView tv_item3 = (TextView) dialoga.findViewById(R.id.tv_item3);

                            tv_title.setText("检测到当前位置与签到地点误差较大");
                            tv_item1.setText("直接打卡");
                            tv_item2.setText("提交异常");
                            tv_item3.setText("取消");

                            re_item1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();


                                }
                            });

                            re_item2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();

                                    //提交异常
                                    UploadAbnormalInfoWithType("中午下班");
                                }
                            });

                            re_item3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialoga.dismiss();
                                }
                            });


                        }else {

                            //正常中午下班签到  0402
                            NewUpdateWorkState("0402");

                        }


                    }else if (Double.parseDouble(currentTime1) == Double.parseDouble(secondTimes[0])){

                        //时相同 判断分

                        if (Double.parseDouble(currentTime2) > Double.parseDouble(secondTimes[1]) || Double.parseDouble(currentTime2) == Double.parseDouble(secondTimes[1])){

                            //判断位置

                            double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));

                            //误差大于500米就是早退了
                            if (distance>500){

                                LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                                final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this, R.style.Dialog).create();
                                dialoga.show();
                                dialoga.getWindow().setContentView(layout);
                                dialoga.setCanceledOnTouchOutside(true);
                                RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
                                RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
                                RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);

                                TextView tv_title = (TextView) dialoga.findViewById(R.id.tv_title);
                                TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
                                TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
                                TextView tv_item3 = (TextView) dialoga.findViewById(R.id.tv_item3);

                                tv_title.setText("检测到当前位置与签到地点误差较大");
                                tv_item1.setText("直接打卡");
                                tv_item2.setText("提交异常");
                                tv_item3.setText("取消");

                                re_item1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialoga.dismiss();


                                    }
                                });

                                re_item2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialoga.dismiss();

                                        //提交异常
                                        UploadAbnormalInfoWithType("中午下班");
                                    }
                                });

                                re_item3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialoga.dismiss();
                                    }
                                });


                            }else {

                                //正常中午下班签到  0402
                                NewUpdateWorkState("0402");

                            }

                        }else {

                            //早退
                            NewUpdateWorkState("0502");
                        }


                    }else {

                        //直接按早退处理
                        NewUpdateWorkState("0502");

                    }

                }

            }
        });



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

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mLat1 = location.getLatitude();
            mLon1 = location.getLongitude();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(15.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            if (isFinished&&isLFinished) {
                myMaphandler.sendEmptyMessage(0);
            }
        }

    }

    Runnable mapThread = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < strLat.size(); i++) {
                View v = null;
                if (strSex.get(i).equals("01")) {
                    v = LayoutInflater.from(BaiDuQiyeLocationActivity.this).inflate(R.layout.item_map_user, null);
                } else {
                    v = LayoutInflater.from(BaiDuQiyeLocationActivity.this).inflate(R.layout.item_map_user1, null);
                }
                v.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tvTitle = (TextView) v.findViewById(R.id.tv_name);
                tvTitle.setText(strName.get(i));
                double lat = 0, lng = 0;
                if (strLat.get(i) != null && !"".equals(strLat.get(i))) {
                    lat = Double.valueOf(strLat.get(i));
                }
                if (strLong.get(i) != null && !"".equals(strLong.get(i))) {
                    lng = Double.valueOf(strLong.get(i));
                }
                LatLng p = new LatLng(lat, lng);
                mMapView = new MapView(BaiDuQiyeLocationActivity.this,
                        new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                                .target(p).build()));
                CoordinateConverter converter = new CoordinateConverter();
                converter.coord(p);
                converter.from(CoordinateConverter.CoordType.COMMON);
                LatLng convertLatLng = converter.convert();
                MarkerOptions mao = new MarkerOptions()
                        .position(convertLatLng)
                        .icon(BitmapDescriptorFactory
                                .fromView(v))
                        .zIndex(9)
                        .draggable(false);
                Marker mar = (Marker) mBaiduMap.addOverlay(mao);
                oa.add(mar);
//                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 16.0f);
//                mBaiduMap.animateMapStatus(u);
            }
            myMaphandler.removeCallbacks(mapThread);
        }
    };

    private void initV2() {
        tvAge = (TextView) vi2.findViewById(R.id.tv_nianling);
        tvmaj1 = (TextView) vi2.findViewById(R.id.tv_project_one);
        tv_distance = (TextView) vi2.findViewById(R.id.tv_distance);
        tvmaj2 = (TextView) vi2.findViewById(R.id.tv_project_two);
        tvmaj3 = (TextView) vi2.findViewById(R.id.tv_project_three);
        tvmaj4 = (TextView) vi2.findViewById(R.id.tv_project_four);
        tv_zy1_bao = (TextView) vi2.findViewById(R.id.tv_zy1_bao);
        tv_zy2_bao = (TextView) vi2.findViewById(R.id.tv_zy2_bao);
        tv_zy3_bao = (TextView) vi2.findViewById(R.id.tv_zy3_bao);
        tv_zy4_bao = (TextView) vi2.findViewById(R.id.tv_zy4_bao);
        tvName = (TextView) vi2.findViewById(R.id.tv_name);
        tvsign = (TextView) vi2.findViewById(R.id.tv_qianming);
        tv_titl = (TextView) vi2.findViewById(R.id.tv_titl);
        btnDh = (Button) vi2.findViewById(R.id.btn_daohang);
        btnXq = (Button) vi2.findViewById(R.id.btn_xiangqing);
        ivAvatar = (CircleImageView) vi2.findViewById(R.id.iv_head);
        ivSex = (ImageView) vi2.findViewById(R.id.iv_sex);
        iv_zy1_tupian = (TextView) vi2.findViewById(R.id.iv_zy1_tupian);
        iv_zy2_tupian = (TextView) vi2.findViewById(R.id.iv_zy2_tupian);
        iv_zy3_tupian = (TextView) vi2.findViewById(R.id.iv_zy3_tupian);
        iv_zy4_tupian = (TextView) vi2.findViewById(R.id.iv_zy4_tupian);
    }

    private void initV3() {
        tvAge3 = (TextView) vi3.findViewById(R.id.tv_nianling);
        tvmaj13 = (TextView) vi3.findViewById(R.id.tv_project_one);
        tv_distance3 = (TextView) vi3.findViewById(R.id.tv_distance);
        tvmaj23 = (TextView) vi3.findViewById(R.id.tv_project_two);
        tvmaj33 = (TextView) vi3.findViewById(R.id.tv_project_three);
        tvmaj43 = (TextView) vi3.findViewById(R.id.tv_project_four);
        tv_zy1_bao3 = (TextView) vi3.findViewById(R.id.tv_zy1_bao);
        tv_zy2_bao3 = (TextView) vi3.findViewById(R.id.tv_zy2_bao);
        tv_zy3_bao3 = (TextView) vi3.findViewById(R.id.tv_zy3_bao);
        tv_zy4_bao3 = (TextView) vi3.findViewById(R.id.tv_zy4_bao);
        tvName3 = (TextView) vi3.findViewById(R.id.tv_name);
        tvsign3 = (TextView) vi3.findViewById(R.id.tv_qianming);
        tv_titl3 = (TextView) vi3.findViewById(R.id.tv_titl);
        btnDh3 = (Button) vi3.findViewById(R.id.btn_daohang);
        btnXq3 = (Button) vi3.findViewById(R.id.btn_xiangqing);
        ivAvatar3 = (CircleImageView) vi3.findViewById(R.id.iv_head);
        ivSex3 = (ImageView) vi3.findViewById(R.id.iv_sex);
        iv_zy1_tupian3 = (TextView) vi3.findViewById(R.id.iv_zy1_tupian);
        iv_zy2_tupian3 = (TextView) vi3.findViewById(R.id.iv_zy2_tupian);
        iv_zy3_tupian3 = (TextView) vi3.findViewById(R.id.iv_zy3_tupian);
        iv_zy4_tupian3 = (TextView) vi3.findViewById(R.id.iv_zy4_tupian);
        tv_company_count3 = (TextView) vi3.findViewById(R.id.tv_company_count);
        tv_company3 = (TextView) vi3.findViewById(R.id.tv_company);
    }

    private boolean isWifiConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                isWifiConnected = true;
            } else {
                isWifiConnected = false;
            }
        }
        return isWifiConnected;
    }

    private String jisuanRemark() {
        String remark = "01";
        double lat = Double.parseDouble(qiyeLat);
        double lon = Double.parseDouble(qiyeLon);
        LatLng ll = JSONUtil.convertGPSToBaidu(new LatLng(lat, lon));
        double distance = DistanceUtil.getDistance(ll, new LatLng(mLat1, mLon1));
        String nowTime = getcurrentTime();
        nowTime = nowTime.substring(8, 10) + ":" + nowTime.substring(10, 12);
        double nowtime1 = Double.parseDouble(nowTime.substring(0, 2));
        double nowtime2 = Double.parseDouble(nowTime.substring(3, 5));
        if (btnDh.getText().toString().trim().equals("签到")) {
            if (shangbanTime != null) {
                double shbTime1 = Double.parseDouble(shangbanTime.substring(0, 2));
                double shbTime2 = Double.parseDouble(shangbanTime.substring(3, 5));
                if ((shbTime1 - nowtime1) * 60 + (shbTime2 - nowtime2) > 0) {
                    if (distance <= 500) {
                        remark = "01";
                    } else {
                        isdistance = true;
                        remark = "02";
                    }
                } else {
                    remark = "02";
                }
            } else {
                ToastUtils.showNOrmalToast(BaiDuQiyeLocationActivity.this.getApplicationContext(),"获取时间失败！");

            }

        } else {
            double xbTime1 = Double.parseDouble(xiabanTime.substring(0, 2));
            double xbTime2 = Double.parseDouble(xiabanTime.substring(3, 5));
            if ((xbTime1 - nowtime1) * 60 + (xbTime2 - nowtime2) < 0) {
                remark = "01";
            } else {
                remark = "05";
            }
        }
        return remark;
    }

    private void showDialog1(final String str) {
        LayoutInflater inflaterDl = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialoga = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this).create();
        dialoga.show();
        dialoga.getWindow().setContentView(layout);
        dialoga.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialoga.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialoga.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialoga.findViewById(R.id.re_item1);
        final RelativeLayout re_item2 = (RelativeLayout) dialoga.findViewById(R.id.re_item2);
        final RelativeLayout re_item3 = (RelativeLayout) dialoga.findViewById(R.id.re_item3);
        tv_item1.setText("定位签到");
        tv_item2.setText("定时签到");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFenxiang) {
                    dialoga.dismiss();
                    showTishi("定位签到");
                } else {
                    dialoga.dismiss();
                    showdwShbTishi(str);
                }
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFenxiang) {
                    dialoga.dismiss();
                    showTishi("定时签到");
                } else {
                    dialoga.dismiss();
                    showdshShbTishi();
                }
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoga.dismiss();
            }
        });
    }

    private void fenxiang(final String zhuangtai) {
        ScreenshotUtil.getBitmapByView(BaiDuQiyeLocationActivity.this, findViewById(R.id.ll1), "分享名片红包", null,6,false,0,0);
        LayoutInflater inflater5 = LayoutInflater.from(BaiDuQiyeLocationActivity.this);
        final RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_fenxiang, null);
        final Dialog dialog = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this).create();
        dialog.show();
        Window window = dialog.getWindow();
        dialog.show();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        //这句就是设置dialog横向满屏了。
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.getWindow().setContentView(layout5);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        TextView tv4 = (TextView) layout5.findViewById(R.id.tv4);
        TextView tv5 = (TextView) layout5.findViewById(R.id.tv5);
        RelativeLayout rl1 = (RelativeLayout) layout5.findViewById(R.id.rl1);
        RelativeLayout rl2 = (RelativeLayout) layout5.findViewById(R.id.rl2);
        RelativeLayout rl3 = (RelativeLayout) layout5.findViewById(R.id.rl3);
        RelativeLayout rl4 = (RelativeLayout) layout5.findViewById(R.id.rl4);
        RelativeLayout rl5 = (RelativeLayout) layout5.findViewById(R.id.rl5);
        RelativeLayout rl6 = (RelativeLayout) layout5.findViewById(R.id.rl6);
        rl6.setVisibility(View.INVISIBLE);
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtoqqz(zhuangtai);
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtowxm(zhuangtai);
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtowb(zhuangtai);
            }
        });
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtoqqf(zhuangtai);
            }
        });
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtowxf(zhuangtai);
            }
        });
    }

    private void fxtoqqf(final String zhuangtai) {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setText(null);
        sp.setTitle(null);
        sp.setTitleUrl(null);
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qq.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                showQdDialog(zhuangtai);
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                showQdDialog(zhuangtai);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(BaiDuQiyeLocationActivity.this, "取消分享后无法进行签到！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qq.share(sp);
    }

    private void fxtoqqz(final String zhuangtai) {
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setText(null);
        sp.setTitle(null);
        sp.setTitleUrl(null);
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform qqz = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qqz.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                showQdDialog(zhuangtai);
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                showQdDialog(zhuangtai);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(BaiDuQiyeLocationActivity.this, "取消分享后无法进行签到！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qqz.share(sp);
    }

    private void fxtowxm(final String zhuangtai) {
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("正事多app");
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                showQdDialog(zhuangtai);
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                showQdDialog(zhuangtai);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(BaiDuQiyeLocationActivity.this, "取消分享后无法进行签到！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);
    }

    private void fxtowxf(final String zhuangtai) {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("正事多app");
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                showQdDialog(zhuangtai);
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                showQdDialog(zhuangtai);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(BaiDuQiyeLocationActivity.this, "取消分享后无法进行签到！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);
    }

    private void fxtowb(final String zhuangtai) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("正事多app");
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wb.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                showQdDialog(zhuangtai);
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                showQdDialog(zhuangtai);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(BaiDuQiyeLocationActivity.this, "取消分享后无法进行签到！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wb.share(sp);
    }

    private void showQdDialog(final String zhuangtai) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(BaiDuQiyeLocationActivity.this);
        builder.setMessage("点击确定进行签到");
        builder.setTitle("确认签到");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                dialog.dismiss();
                if ("定位签到".equals(zhuangtai)) {
                    showdwShbTishi("签到");
                } else if ("下班签到".equals(zhuangtai)) {
                    showXbTishi();
                } else if ("定时签到".equals(zhuangtai)) {
                    showdshShbTishi();
                }
            }
        });
        builder.show();
    }

    private void qiandao(final String remark) {
        final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        final String userId = DemoHelper.getInstance().getCurrentUsernName();
        String clockType = "01";//上班或者下班
        if (btnDh.getText().toString().trim().equals("签到")) {
            clockType = "01";
        } else {
            clockType = "00";
        }
        String url = FXConstant.URL_YUGONG_QIANDAO;
        final String finalClockType = clockType;
        StringRequest qDrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "签到" + s);

                Toast.makeText(BaiDuQiyeLocationActivity.this, "签到成功！", Toast.LENGTH_SHORT).show();
                if ("02".equals(remark)) {
                    tongji(remark, qiyeId, userId);
                } else if (shBzhutai.equals("下班签到")&&"01".equals(signRemark)) {
                    tongji("06", qiyeId, userId);
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("网络连接错误", volleyError + "");
                Toast.makeText(BaiDuQiyeLocationActivity.this, "网络连接错误...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("companyId", qiyeId);
                params.put("userId", userId);
                params.put("remark", remark);
                params.put("clockType", finalClockType);
                params.put("latitude", mLat1 + "");
                params.put("longitude", mLon1 + "");
                return params;
            }
        };
        MySingleton.getInstance(BaiDuQiyeLocationActivity.this).addToRequestQueue(qDrequest);
    }

    private void tongji(final String remark, final String qiyeId, final String userId) {
        String url = FXConstant.URL_QIYE_YUANGONGTONGJI;
        StringRequest qDrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                setResult(RESULT_OK);
              //  finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("qiandao,volleyError", volleyError.toString());
                Toast.makeText(BaiDuQiyeLocationActivity.this, "网络连接错误...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("companyId", qiyeId);
                params.put("userId", userId);
                if ("02".equals(remark)) {
                    params.put("lateTimes", "1");
                } else if ("06".equals(remark)) {
                    params.put("resv1", "1");
                }
                return params;
            }
        };
        MySingleton.getInstance(BaiDuQiyeLocationActivity.this).addToRequestQueue(qDrequest);
    }

    public String getcurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    private void initMapView() {
        mMapView.setLongClickable(true);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        if (mLocClient != null) {
            mLocClient.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 关闭定位图层
        super.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        if (option.isOpenGps()) {
            option.setOpenGps(false);
        }
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
        if (oa != null) {
            oa.clear();
            oa = null;
        }
        if (strLat != null) {
            strLat.clear();
            strLat = null;
        }
        if (strName != null) {
            strName.clear();
            strName = null;
        }
        if (strLoginId != null) {
            strLoginId.clear();
            strLoginId = null;
        }
        if (strLong != null) {
            strLong.clear();
            strLong = null;
        }
        if (strSex != null) {
            strSex.clear();
            strSex = null;
        }
    }

    public void back(View v) {
        setResult(RESULT_OK);
        finish();
    }
}