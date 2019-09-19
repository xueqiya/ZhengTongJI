package com.sangu.apptongji.main.alluser.maplocation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
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
import com.pintu.deutils.activity.PuzzleMain;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.fragment.MainActivity;
import com.sangu.apptongji.main.utils.BitmapUtils;
import com.sangu.apptongji.main.utils.JSONUtil;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.CompassView;
import com.sangu.apptongji.main.widget.MarqueeTextView;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-10-13.
 */

public class BaiDuBzLocationActivity extends BaseActivity implements
        OnMapLongClickListener,OnMapClickListener,BaiduMap.OnMarkerClickListener,OnGetRoutePlanResultListener{
    private ImageView iv_mylocation;
    private MarqueeTextView text;
    private CompassView mPointer;
    private final float MAX_ROATE_DEGREE = 1.0f;// 最多旋转一周，即360°
    private SensorManager mSensorManager;// 传感器管理对象
    private Sensor mOrientationSensor;// 传感器对象
    private float mDirection;// 当前浮点方向
    private float mTargetDirection;// 目标浮点方向
    private AccelerateInterpolator mInterpolator;// 动画从开始到结束，变化率是一个加速的过程,就是一个动画速率
    protected final Handler mHandler = new Handler();
    private boolean mStopDrawing;// 是否停止指南针旋转的标志位

    LocationClient mLocClient=null;
    private float mCurrentX;
    private MyOrientationListener myOrientationListener;
    private MyLocationListenner myListener;
    private MapView mMapView = null;
    private RoutePlanSearch mSearch=null;
    private BaiduMap mBaiduMap = null;
    private Marker oA;
    private Marker oA2;
    private Marker oA3;
    private TextView tv_geshu;
    private ImageView iv_baoxiang;
    private InfoWindow mInfoWindow = null;
    private double mLat1,mLon1;
    private Button btn_daohang;
    private Button btn_xiangqing;
    View vi3=null;
    private String pintuUrl,createTime,user_id,dynamicSeq,gameRed,name,geshu;
    private LocationMode mCurrentMode=null;
    private boolean isMylocaclicked = false;
    private boolean isFirst = true;
    private boolean isJmFirst = true;
    LinearLayout line;
    String endLat,endLng,biaoshi;
    LatLng latLngend;
    double distance;

    // 这个是更新指南针旋转的线程，handler的灵活使用，每20毫秒检测方向变化值，对应更新指南针旋转
    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
            if (mPointer != null && !mStopDrawing) {
                if (mDirection != mTargetDirection) {
                    // calculate the short routine
                    float to = mTargetDirection;
                    if (to - mDirection > 180) {
                        to -= 360;
                    } else if (to - mDirection < -180) {
                        to += 360;
                    }
                    // limit the max speed to MAX_ROTATE_DEGREE
                    float distance = to - mDirection;
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROATE_DEGREE
                                : (-1.0f * MAX_ROATE_DEGREE);
                    }
                    // need to slow down if the distance is short
                    mDirection = normalizeDegree(mDirection
                            + ((to - mDirection) * mInterpolator
                            .getInterpolation(Math.abs(distance) > MAX_ROATE_DEGREE ? 0.4f
                                    : 0.3f)));// 用了一个加速动画去旋转图片，很细致
                    mPointer.updateDirection(mDirection);// 更新指南针旋转
                }
                mHandler.postDelayed(mCompassViewUpdater, 20);// 20毫米后重新执行自己，比定时器好
            }
        }
    };

    // 调整方向传感器获取的值
    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }

    // 方向传感器变化监听
    private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float direction = event.values[0] * -1.0f;
            mTargetDirection = normalizeDegree(direction);// 赋值给全局变量，让指南针旋转
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

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
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            MainActivity.instance.refreshDyna();
            finish();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.equals(oA2)) {
            double mlat1 = 0.0, mlng1 = 0.0;
            if (endLat != null && !"".equals(endLat)) {
                mlat1 = Double.parseDouble(endLat);
            }
            if (endLng != null && !"".equals(endLng)) {
                mlng1 = Double.parseDouble(endLng);
            }
            LatLng p = new LatLng(mlat1, mlng1);
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(p);
            converter.from(CoordinateConverter.CoordType.COMMON);
            p = converter.convert();
            if (btn_daohang.getText().toString().equals("起航探宝")) {
                mInfoWindow = new InfoWindow(vi3, p, -30);
                mBaiduMap.showInfoWindow(mInfoWindow);
            } else {
                mInfoWindow = new InfoWindow(vi3, p, -150);
                mBaiduMap.showInfoWindow(mInfoWindow);
            }
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(p, 16.0f);
            mBaiduMap.animateMapStatus(u);
            return true;
        }else {
            return false;
        }
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
            double lat = Double.parseDouble(endLat);
            double lon = Double.parseDouble(endLng);
            LatLng ll = JSONUtil.convertGPSToBaidu(new LatLng(lat,lon));
            distance = DistanceUtil.getDistance(ll,new LatLng(mLat1,mLon1));
            if (distance <= 30) {
                if (isJmFirst) {
                    line.setVisibility(View.GONE);
                    setClick();
                }
            }else {
                if ("close".equals(biaoshi)) {
                    line.setVisibility(View.VISIBLE);
                }else {
                    mBaiduMap.removeMarkerClickListener(BaiDuBzLocationActivity.this);
                }
            }
            if (mSearch!=null&&isFirst){
                LatLng latLngstar=new LatLng(location.getLatitude(),location.getLongitude());
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
    }

    public void startNavi(double lat, double Lng) {
        LatLng pt1 = new LatLng(mLat1, mLon1);
        LatLng pt2 = new LatLng(lat,Lng);
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
                OpenClientUtil.getLatestBaiduMapApp(BaiDuBzLocationActivity.this);
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
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        text = (MarqueeTextView) findViewById(R.id.test);
        text.startScroll();
        line = (LinearLayout) findViewById(R.id.conver);
        iv_mylocation = (ImageView) findViewById(R.id.iv_mylocation);
        iv_mylocation.setImageResource(R.drawable.luopan);
        vi3 = LayoutInflater.from(this).inflate(R.layout.item_find_fragments2,null);
        btn_daohang = (Button) vi3.findViewById(R.id.btn_daohang);
        btn_xiangqing = (Button) vi3.findViewById(R.id.btn_xiangqing);
        mMapView = (MapView) findViewById(R.id.bmapView);
        initResources();// 初始化view
        mMapView.removeViewAt(1);
        mBaiduMap = mMapView.getMap();
        mCurrentMode = LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, null));
        myListener = new MyLocationListenner();
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        mLocClient.setLocOption(option);
        mBaiduMap.setMyLocationEnabled(true);
        myOrientationListener=new MyOrientationListener(BaiDuBzLocationActivity.this);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX=x;
            }
        });
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMapLongClickListener(this);
        String url = getIntent().getStringExtra("pintuUrl");
        gameRed = getIntent().getStringExtra("gameRed");
        geshu = getIntent().getStringExtra("geshu");
        name = getIntent().getStringExtra("name");
        user_id = getIntent().getStringExtra("user_id");
        dynamicSeq = getIntent().getStringExtra("dynamicSeq");
        createTime = getIntent().getStringExtra("createTime");
        biaoshi = getIntent().getStringExtra("biaoshi");
        endLat = getIntent().getStringExtra("bzlat");
        endLng = getIntent().getStringExtra("bzlng");
        pintuUrl = FXConstant.URL_SOCIAL_PHOTO + url;
        LatLng p = new LatLng(Double.parseDouble(endLat),Double.parseDouble(endLng));
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(p);
        converter.from(CoordinateConverter.CoordType.COMMON);
        latLngend = converter.convert();
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        setListener(endLat,endLng);
//        showRollNews();
    }

    // 初始化view
    private void initResources() {
        mDirection = 0.0f;// 初始化起始方向
        mTargetDirection = 0.0f;// 初始化目标方向
        mInterpolator = new AccelerateInterpolator();// 实例化加速动画对象
        mStopDrawing = true;
        mPointer = (CompassView) findViewById(R.id.iv_mylocation_big);// 自定义的指南针view
        mPointer.setImageResource(R.drawable.luopan);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientationSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return null;
        }
    }

    private void setClick(){
        isJmFirst = false;
        btn_daohang.setText("解密宝箱");
        btn_xiangqing.setText("取消操作");
        double mlat =0.0,mlng=0.0;
        if (endLat!=null&&!"".equals(endLat)){
            mlat = Double.parseDouble(endLat);
        }
        if (endLng!=null&&!"".equals(endLng)){
            mlng = Double.parseDouble(endLng);
        }
        LatLng p = new LatLng(mlat, mlng);
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(p);
        converter.from(CoordinateConverter.CoordType.COMMON);
        p = converter.convert();
        if ("close".equals(biaoshi)){
            oA.setVisible(false);
            View v = LayoutInflater.from(BaiDuBzLocationActivity.this).inflate(R.layout.item_map_user3, null);
            iv_baoxiang = (ImageView) v.findViewById(R.id.iv_baoxiang);
            Bitmap bm1 = BitmapUtils.readBitMap(BaiDuBzLocationActivity.this,R.drawable.baoxiang_close);
            tv_geshu = (TextView) v.findViewById(R.id.tv_geshu);
            iv_baoxiang.setImageBitmap(bm1);
            v.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_geshu.setText(geshu);
            MarkerOptions ooA = new MarkerOptions()
                    .position(p)
                    .icon(BitmapDescriptorFactory.fromView(v))
                    .zIndex(9)
                    .draggable(false);
            oA3 = (Marker) mBaiduMap.addOverlay(ooA);
        }else {
            MarkerOptions ooA = new MarkerOptions()
                    .position(p)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.baoxiang_sm_open))
                    .zIndex(9)
                    .draggable(false);
            oA2 = (Marker) mBaiduMap.addOverlay(ooA);
        }
        btn_xiangqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.hideInfoWindow();
            }
        });
        btn_daohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queryQuanXian();
            }
        });
    }

    private void queryQuanXian() {
        String url = FXConstant.URL_QUERY_BZQUANXIAN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if (code.equals("0")){
                    startToJieMi();
                }else {
                    LayoutInflater inflater2 = LayoutInflater.from(BaiDuBzLocationActivity.this);
                    RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog2 = new AlertDialog.Builder(BaiDuBzLocationActivity.this).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout2);
                    dialog2.setCanceledOnTouchOutside(true);
                    dialog2.setCancelable(true);
                    TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                    Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                    final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                    TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                    title2.setText("温馨提示");
                    btnOK2.setText("确定");
                    btnCancel2.setText("取消");
                    title_tv2.setText("您已经开启过该宝藏,无法重复获得！");
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
                        }
                    });
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
                param.put("order_id",createTime);
                param.put("mer_id", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(BaiDuBzLocationActivity.this).addToRequestQueue(request);
    }

    private void startToJieMi() {
        final ProgressDialog mProgress = new ProgressDialog(BaiDuBzLocationActivity.this);
        mProgress.setMessage("正在加载解密图片...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        mProgress.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL fileUrl = null;
                Bitmap bitmap = null;
                try {
                    fileUrl = new URL(pintuUrl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) fileUrl
                            .openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    saveBitmapToSharedPreferences(bitmap);
                    final Bitmap finalBitmap = bitmap;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mProgress!=null&&mProgress.isShowing()){
                                mProgress.dismiss();
                            }
                            if (finalBitmap ==null){
                                Toast.makeText(getApplicationContext(),"获取拼图资源失败，请检查网络设置!",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent intent = new Intent(
                                    BaiDuBzLocationActivity.this,
                                    PuzzleMain.class);
                            intent.putExtra("dynamicSeq",dynamicSeq);
                            intent.putExtra("createTime",createTime);
                            intent.putExtra("user_id",user_id);
                            intent.putExtra("gameRed",gameRed);
                            intent.putExtra("name",name);
                            intent.putExtra("pintuUrl",pintuUrl);
                            startActivityForResult(intent,1);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setListener(String bzlat, String bzlng) {
        btn_daohang.setText("起航探宝");
        btn_xiangqing.setText("取消操作");
        double mlat =0.0,mlng=0.0;
        if (bzlat!=null&&!"".equals(bzlat)){
            mlat = Double.parseDouble(bzlat);
        }
        if (bzlng!=null&&!"".equals(bzlng)){
            mlng = Double.parseDouble(bzlng);
        }
        LatLng p = new LatLng(mlat, mlng);
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(p);
        converter.from(CoordinateConverter.CoordType.COMMON);
        p = converter.convert();
        MarkerOptions ooA = null;
        if ("close".equals(biaoshi)){
            View v = LayoutInflater.from(BaiDuBzLocationActivity.this).inflate(R.layout.item_map_user2, null);
            v.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_geshu = (TextView) v.findViewById(R.id.tv_geshu);
            tv_geshu.setText(geshu);
            ooA = new MarkerOptions()
                    .position(p)
                    .icon(BitmapDescriptorFactory.fromView(v))
                    .zIndex(9)
                    .draggable(false);
            oA = (Marker) mBaiduMap.addOverlay(ooA);
        }else {
            ooA = new MarkerOptions()
                    .position(p)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.baoxiang_sm_open))
                    .zIndex(9)
                    .draggable(false);
            oA2 = (Marker) mBaiduMap.addOverlay(ooA);
        }
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(p, 14.0f);
        mBaiduMap.animateMapStatus(u);
        iv_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMylocaclicked = true;
                mPointer.setVisibility(View.VISIBLE);
                iv_mylocation.setVisibility(View.GONE);
            }
        });
        mPointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPointer.setVisibility(View.GONE);
                iv_mylocation.setVisibility(View.VISIBLE);
            }
        });
        mBaiduMap.setOnMarkerClickListener(this);
        btn_xiangqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.hideInfoWindow();
            }
        });
        btn_daohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double lat = Double.parseDouble(endLat);
                double lon = Double.parseDouble(endLng);
                startNavi(lat,lon);
            }
        });
    }
    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("sangu_pintu", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mInfoWindow!=null) {
            mBaiduMap.hideInfoWindow();
        }
        mPointer.setVisibility(View.GONE);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        mStopDrawing = true;
        if (mOrientationSensor != null) {
            mSensorManager.unregisterListener(mOrientationSensorEventListener);
        }
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        mLocClient.start();
        myOrientationListener.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        if (mLocClient!=null) {
            mLocClient.stop();
        }
        if (myOrientationListener!=null) {
            myOrientationListener.stop();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        if (mOrientationSensor != null) {
            mSensorManager.registerListener(mOrientationSensorEventListener,
                    mOrientationSensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            Toast.makeText(this, "未获取到方向传感器", Toast.LENGTH_SHORT)
                    .show();
        }
        mStopDrawing = false;
        mHandler.postDelayed(mCompassViewUpdater, 20);// 20毫秒执行一次更新指南针图片旋转
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSensorManager!=null){
            mSensorManager=null;
        }
        if (myListener!=null){
            mLocClient.unRegisterLocationListener(myListener);
            myListener = null;
        }
        if (mLocClient!=null){
            mLocClient.stop();
            mLocClient=null;
        }
        if (myOrientationListener!=null) {
            myOrientationListener.stop();
            myOrientationListener = null;
        }
        if (mSearch!=null) {
            mSearch.destroy();
        }
        mMapView.onDestroy();
        mMapView=null;
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}