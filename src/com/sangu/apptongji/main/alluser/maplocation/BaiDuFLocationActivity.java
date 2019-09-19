package com.sangu.apptongji.main.alluser.maplocation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2016-10-13.
 */

public class BaiDuFLocationActivity extends BaseActivity implements
        OnMapLongClickListener,OnMapClickListener,OnGetRoutePlanResultListener {
    private TextView tvTitle = null;
    private ImageView iv_mylocation,ivSex;
    LocationClient mLocClient = null;
    private float mCurrentX;
    private MyOrientationListener myOrientationListener;
    private MyLocationListenner myListener;
    private MapView mMapView = null;
    private RoutePlanSearch mSearch = null;
    private BaiduMap mBaiduMap = null;
    private LinearLayout ll_search = null;
    private InfoWindow mInfoWindow = null;
    private double mLat1, mLon1;
    private String[] strLat = null;
    private String[] strLong = null;
    private String[] strLoginId = null;
    private String[] strName = null;
    private String[] strSex = null;
    private RelativeLayout rl_sex;
    private TextView tvmaj1 = null, tvmaj2 = null, tvmaj3 = null, tvmaj4 = null, tv_titl = null, tv_zy1_bao = null, tv_zy2_bao = null, tv_zy3_bao = null, tv_distance,
            tv_zy4_bao = null, tvName = null, iv_zy1_tupian = null, iv_zy2_tupian = null, iv_zy3_tupian = null, iv_zy4_tupian = null, tvsign = null, tvAge = null;
    private TextView tv_company = null, tv_company_count = null;
    private Button btnXq = null, btnDh = null, btnXq2 = null, btnDh2 = null;
    private CircleImageView ivAvatar = null;
    private LocationInfo[] loactions = null;
    View vi2 = null, vi3 = null;
    Marker[] oa = null;
    MarkerOptions[] ooA = null;
    private LocationMode mCurrentMode = null;
    private boolean isMylocaclicked = false;
    private boolean isFirst = true;
    String endLat, endLng;
    String biaoshi;
    LatLng latLngend;

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getApplicationContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
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
                    .direction(mCurrentX).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (mSearch != null && isFirst) {
                LatLng latLngstar = new LatLng(location.getLatitude(), location.getLongitude());
                PlanNode stNode = PlanNode.withLocation(latLngstar);
                PlanNode enNode = PlanNode.withLocation(latLngend);
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode).to(enNode));
                isFirst = false;
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    public void onMapLongClick(final LatLng latLng) {
        mInfoWindow = new InfoWindow(vi3, latLng, -47);
        mBaiduMap.showInfoWindow(mInfoWindow);
        btnXq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent().putExtra("lat", String.valueOf
                        (latLng.latitude)).putExtra("lng", String.valueOf(latLng.longitude)));
                finish();
            }
        });
        btnDh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNavi(latLng.latitude, latLng.longitude);
            }
        });
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
            showDialog();
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(BaiDuFLocationActivity.this);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_baidumap);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        iv_mylocation = (ImageView) findViewById(R.id.iv_mylocation);
        ll_search.setVisibility(View.GONE);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        vi3 = LayoutInflater.from(this).inflate(R.layout.item_find_fragments2, null);
        btnXq2 = (Button) vi3.findViewById(R.id.btn_xiangqing);
        btnDh2 = (Button) vi3.findViewById(R.id.btn_daohang);
        vi2 = LayoutInflater.from(this).inflate(R.layout.item_find_fragments, null);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.removeViewAt(1);
        mBaiduMap = mMapView.getMap();
        mCurrentMode = LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        myListener = new MyLocationListenner();
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mBaiduMap.setMyLocationEnabled(true);
        myOrientationListener = new MyOrientationListener(BaiDuFLocationActivity.this);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMapLongClickListener(this);
        initMapView();
        initV2();
        String lat = getIntent().getStringExtra("mylat");
        String lng = getIntent().getStringExtra("mylng");
        strLat = this.getIntent().getStringArrayExtra("lat");
        strLong = this.getIntent().getStringArrayExtra("lng");
        strLoginId = this.getIntent().getStringArrayExtra("loginId");
        strName = this.getIntent().getStringArrayExtra("name");
        strSex = this.getIntent().getStringArrayExtra("sex");
        loactions = new LocationInfo[strName.length];
        ooA = new MarkerOptions[strName.length];
        oa = new Marker[strName.length];
        if ("导航".equals(biaoshi)) {
            double mlat = 0.0, mlng = 0.0;
            if (lat != null && !"".equals(lat)) {
                mlat = Double.parseDouble(lat);
            }
            if (lng != null && !"".equals(lng)) {
                mlng = Double.parseDouble(lng);
            }
            showMarker(mlat,mlng);
            endLat = strLat[0];
            endLng = strLong[0];
            LatLng p = new LatLng(Double.parseDouble(endLat), Double.parseDouble(endLng));
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(p);
            converter.from(CoordinateConverter.CoordType.COMMON);
            latLngend = converter.convert();
            mSearch = RoutePlanSearch.newInstance();
            mSearch.setOnGetRoutePlanResultListener(this);
        }

        initMap();

        setListener(lat, lng);

    }

    private void initMap() {
        for (int i = 0; i < strLat.length; i++) {
            loactions[i] = new LocationInfo(strLat[i], strLong[i], strName
                    [i], strLoginId[i], strSex[i]);
        }
        for (int i = 0; i < loactions.length; i++) {
            if ("导航".equals(biaoshi)){
                double lat = Double.valueOf(loactions[i].latitude);
                double lng = Double.valueOf(loactions[i].longtitude);
                LatLng p = new LatLng(lat, lng);
                CoordinateConverter converter = new CoordinateConverter();
                converter.coord(p);
                converter.from(CoordinateConverter.CoordType.COMMON);
                LatLng convertLatLng = converter.convert();
                if (ooA.length > 0) {
                    ooA[i] = new MarkerOptions()
                            .position(convertLatLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_geo))
                            .zIndex(9)
                            .draggable(false);
                    oa[i] = (Marker) mBaiduMap.addOverlay(ooA[i]);
                }
            }else {
                View v = null;
                if ("00".equals(strSex[i])) {
                    v = LayoutInflater.from(BaiDuFLocationActivity.this).inflate
                            (R.layout.item_map_user1, null);
                } else {
                    v = LayoutInflater.from(BaiDuFLocationActivity.this).inflate
                            (R.layout.item_map_user, null);
                }
                v.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tvTitle = (TextView) v.findViewById(R.id.tv_name);
                tvTitle.setText(loactions[i].title1);
                double lat = Double.valueOf(loactions[i].latitude);
                double lng = Double.valueOf(loactions[i].longtitude);
                LatLng p = new LatLng(lat, lng);
                CoordinateConverter converter = new CoordinateConverter();
                converter.coord(p);
                converter.from(CoordinateConverter.CoordType.COMMON);
                LatLng convertLatLng = converter.convert();
                if (ooA.length > 0) {
                    ooA[i] = new MarkerOptions()
                            .position(convertLatLng)
                            .icon(BitmapDescriptorFactory
                                    .fromView(v))
                            .zIndex(9)
                            .draggable(false);
                    oa[i] = (Marker) mBaiduMap.addOverlay(ooA[i]);
                }
            }
        }
    }

    private void initV2() {
        tvAge = (TextView) vi2.findViewById(R.id.tv_nianling);
        tv_distance = (TextView) vi2.findViewById(R.id.tv_distance);
        tv_company = (TextView) vi2.findViewById(R.id.tv_company);
        tv_company_count = (TextView) vi2.findViewById(R.id.tv_company_count);
        rl_sex = (RelativeLayout) vi2.findViewById(R.id.rl_sex);
        tvmaj1 = (TextView) vi2.findViewById(R.id.tv_project_one);
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

    private void showMarker(double lat, double lng) {
        if (lat != 0.0) {
            LatLng p = new LatLng(lat, lng);
            if (!"导航".equals(biaoshi)) {
                CoordinateConverter converter = new CoordinateConverter();
                converter.coord(p);
                converter.from(CoordinateConverter.CoordType.COMMON);
                p = converter.convert();
            }
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(p, 17.0f);
            mBaiduMap.animateMapStatus(u);
        }
    }

    private void setListener(String mylat, String mylng) {
        if (!"导航".equals(biaoshi)) {
            double mlat = 0.0, mlng = 0.0;
            if (mylat != null && !"".equals(mylat)) {
                mlat = Double.parseDouble(mylat);
            }
            if (mylng != null && !"".equals(mylng)) {
                mlng = Double.parseDouble(mylng);
            }
            LatLng p = new LatLng(mlat, mlng);
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(p);
            converter.from(CoordinateConverter.CoordType.COMMON);
            p = converter.convert();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(p, 17.0f);
            mBaiduMap.animateMapStatus(u);
        }
        iv_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMylocaclicked = true;
                String lat = DemoApplication.getInstance().getCurrentLat();
                String lng = DemoApplication.getInstance().getCurrentLng();
                double mlat = 0.0, mlng = 0.0;
                if (lat != null && !"".equals(lat)) {
                    mlat = Double.parseDouble(lat);
                }
                if (lng != null && !"".equals(lng)) {
                    mlng = Double.parseDouble(lng);
                }
                if ("导航".equals(biaoshi)) {
                    showMarker(mLat1, mLon1);
                } else {
                    showMarker(mlat, mlng);
                }
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if ("导航".equals(biaoshi)){
                    btnXq2.setText("取消操作");
                    final LatLng ll1 = marker.getPosition();
                    MapStatus mMapStatus = new MapStatus.Builder()
                            .target(ll1)
                            .build();
                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    //改变地图状态
                    mBaiduMap.setMapStatus(mMapStatusUpdate);
                    mInfoWindow = new InfoWindow(vi3, ll1, -100);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                    btnXq2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBaiduMap.hideInfoWindow();
                        }
                    });
                    btnDh2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startNavi(ll1.latitude, ll1.longitude);
                        }
                    });
                }else {
                    int j = 3;
                    for (int i = 0; i < oa.length; i++) {
                        if (marker.equals(oa[i])) {
                            j = i;
                        }
                    }
                    if (j > oa.length) {
                        return false;
                    }
                    String id = strLoginId[j];
                    String url = FXConstant.URL_Get_UserInfo + id;
                    final int finalI = j;
                    StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            setView(s, finalI);
                            LatLng ll1 = marker.getPosition();
                            double lat = ll1.latitude + 0.001;
                            double lng = ll1.longitude;
                            LatLng ll2 = new LatLng(lat, lng);
                            MapStatus mMapStatus = new MapStatus.Builder()
                                    .target(ll2)
                                    .zoom(18)
                                    .build();
                            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                            //改变地图状态
                            mBaiduMap.setMapStatus(mMapStatusUpdate);
                            mInfoWindow = new InfoWindow(vi2, ll1, -100);
                            mBaiduMap.showInfoWindow(mInfoWindow);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //callback.onError("获取数据失败");
                        }
                    });
                    MySingleton.getInstance(BaiDuFLocationActivity.this).addToRequestQueue(request);
                }
                return true;
            }
        });
    }

    private void setView(String s, final int i) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONObject object = jsonObject.getJSONObject("userInfo");
            Userful user2 = JSONParser.parseUser(object);
            String imageStr = TextUtils.isEmpty(user2.getImage()) ? "" : user2.getImage();
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
                iv_zy1_tupian.setVisibility(View.VISIBLE);
            } else {
                iv_zy1_tupian.setVisibility(View.INVISIBLE);
            }
            if (margan1 != null) {
                if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                    tv_zy1_bao.setVisibility(View.VISIBLE);
                } else {
                    iv_zy1_tupian.setVisibility(View.INVISIBLE);
                }
            }
            if (!"".equals(image2) && image2 != null) {
                iv_zy2_tupian.setVisibility(View.VISIBLE);
            } else {
                iv_zy2_tupian.setVisibility(View.INVISIBLE);
            }
            if (margan2 != null) {
                if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                    tv_zy2_bao.setVisibility(View.VISIBLE);
                } else {
                    tv_zy2_bao.setVisibility(View.INVISIBLE);
                }
            }
            if (!"".equals(image3) && image3 != null) {
                iv_zy3_tupian.setVisibility(View.VISIBLE);
            } else {
                iv_zy3_tupian.setVisibility(View.INVISIBLE);
            }
            if (margan3 != null) {
                if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                    tv_zy3_bao.setVisibility(View.VISIBLE);
                } else {
                    tv_zy3_bao.setVisibility(View.INVISIBLE);
                }
            }
            if (!"".equals(image4) && image4 != null) {
                iv_zy4_tupian.setVisibility(View.VISIBLE);
            } else {
                iv_zy4_tupian.setVisibility(View.INVISIBLE);
            }
            if (margan4 != null) {
                if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                    tv_zy4_bao.setVisibility(View.VISIBLE);
                } else {
                    tv_zy4_bao.setVisibility(View.INVISIBLE);
                }
            }
            if (("00").equals(sex)) {
                ivSex.setImageResource(R.drawable.nv);
                tvAge.setBackgroundColor(Color.rgb(234, 121, 219));
                tv_titl.setBackgroundResource(R.drawable.fx_bg_text_red);
            } else {
                ivSex.setImageResource(R.drawable.nan);
                tv_titl.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }
            if (!imageStr.equals("")) {
                String[] images = imageStr.split("\\|");
                ivAvatar.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[0], ivAvatar, DemoApplication.mOptions);
            } else {
                ivAvatar.setVisibility(View.INVISIBLE);
                tv_titl.setVisibility(View.VISIBLE);
                tv_titl.setText(strName[i]);
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
                tv_company_count.setVisibility(View.VISIBLE);
            }
            if (endLat != null && String.valueOf(mLat1) != null) {
                if (!("".equals(endLat) || "".equals(endLng) || mLat1 == 0.0)) {
                    double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
                    double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
                    final LatLng ll1 = new LatLng(Double.parseDouble(endLat), Double.parseDouble(endLng));
                    LatLng ll = new LatLng(latitude1,longitude1);
                    double distance = DistanceUtil.getDistance(ll, ll1);
                    double dou = distance / 1000;
                    String str = String.format("%.2f",dou);//format 返回的是字符串
                    if (str!=null&&dou>=10000){
                        tv_distance.setText("隐藏");
                    }else {
                        tv_distance.setText(str + "km");
                    }
                } else {
                    tv_distance.setText("3km以外");
                }
            } else {
                tv_distance.setText("3km以外");
            }
            tv_company_count.setText("(" + member + ")");
            tv_company.setText(company);
            tvAge.setText(age);
            tvmaj1.setText(ZY1);
            tvmaj2.setText(ZY2);
            tvmaj3.setText(ZY3);
            tvmaj4.setText(ZY4);
            tvName.setText(strName[i]);
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
            btnDh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNavi(Double.valueOf(strLat[i]), Double.valueOf(strLong[i]));
                    //finish();
                }
            });
            btnXq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaiDuFLocationActivity.this,
                            UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID, strLoginId[i]);
                    startActivity(intent);
                    finish();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mInfoWindow != null) {
            mBaiduMap.hideInfoWindow();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    private static class LocationInfo implements Serializable {
        private final String latitude;
        private final String longtitude;
        private String title1;
        private String id;
        private String sex;

        public LocationInfo(String latitude, String longtitude, String title1, String
                id, String sex) {
            this.latitude = latitude;
            this.longtitude = longtitude;
            this.title1 = title1;
            this.id = id;
            this.sex = sex;
        }
    }

    private void initMapView() {
        mMapView.setLongClickable(true);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        if ("导航".equals(biaoshi) && mLocClient != null) {
            mLocClient.start();
        }
        if ("导航".equals(biaoshi) && myOrientationListener != null) {
            myOrientationListener.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        if (mLocClient != null) {
            mLocClient.stop();
        }
        if (myOrientationListener != null) {
            myOrientationListener.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myListener != null) {
            mLocClient.unRegisterLocationListener(myListener);
            myListener = null;
        }
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
        if (myOrientationListener != null) {
            myOrientationListener.stop();
            myOrientationListener = null;
        }
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView.onDestroy();
        mMapView = null;
    }

    public void back(View v) {
        if (isMylocaclicked) {
            setResult(RESULT_OK, new Intent().putExtra("lat", String.valueOf(mLat1)).putExtra("lng", String.valueOf(mLon1)));
        }
        finish();
    }
}