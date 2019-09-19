package com.sangu.apptongji.main.alluser.maplocation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
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
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.qiye.QitePaidanActivity;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-10-13.
 */

public class BaiDuYuanGongLocationActivity extends BaseActivity implements OnMapLongClickListener,OnMapClickListener,OnGetRoutePlanResultListener {
    private final static String TAG = "map";
    private MapView mMapView = null;
    private TextView tvTitle = null;
    private BaiduMap mBaiduMap = null;
    private RoutePlanSearch mSearch = null;
    private ImageView iv_mylocation = null;
    BitmapDescriptor mCurrentMarker = null;
    private LinearLayout ll_search = null;
    private InfoWindow mInfoWindow = null;
    private double mLat1, mLon1;
    private String[] strLat = null;
    private String[] strLong = null;
    private String[] strLoginId = null;
    private String[] strName = null;
    private String[] strSex = null;
    private Button btnXq2 = null, btnDh2 = null;
    private LocationInfo[] loactions = null;
    View vi3 = null;
    Marker[] oa = null;
    MarkerOptions[] ooA = null;
    private LocationMode mCurrentMode = null;
    private String companyId = null, orderId = null, totalAmt = null, biaoshi = null, dynaLat = null, dynaLng = null;
    private boolean isMylocaclicked = false;
    private Handler myMaphandler = new Handler() {
    };

    @Override
    public void onMapLongClick(final LatLng latLng) {
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
                OpenClientUtil.getLatestBaiduMapApp(BaiDuYuanGongLocationActivity.this);
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
        ll_search.setVisibility(View.GONE);
        String mlat = DemoApplication.getInstance().getCurrentLat();
        String mlon = DemoApplication.getInstance().getCurrentLng();
        if (mlat != null && !"".equals(mlat)) {
            mLat1 = Double.parseDouble(mlat);
        }
        if (mlon != null && !"".equals(mlon)) {
            mLon1 = Double.parseDouble(mlon);
        }

        mCurrentMode = LocationMode.NORMAL;
        iv_mylocation = (ImageView) findViewById(R.id.iv_mylocation);
        vi3 = LayoutInflater.from(this).inflate(R.layout.item_find_fragments3, null);
        btnXq2 = (Button) vi3.findViewById(R.id.btn_xiangqing);
        btnDh2 = (Button) vi3.findViewById(R.id.btn_daohang);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mCurrentMode = LocationMode.NORMAL;
        mMapView.removeViewAt(1);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setOnMapClickListener(this);
        mBaiduMap.setOnMapLongClickListener(this);
        initMapView();
        strLat = this.getIntent().getStringArrayExtra("lat");
        strLong = this.getIntent().getStringArrayExtra("lng");
        strLoginId = this.getIntent().getStringArrayExtra("loginId");
        strName = this.getIntent().getStringArrayExtra("name");
        strSex = this.getIntent().getStringArrayExtra("sex");
        companyId = this.getIntent().getStringExtra("companyId");
        orderId = this.getIntent().getStringExtra("orderId");
        totalAmt = this.getIntent().getStringExtra("totalAmt");
        biaoshi = this.getIntent().hasExtra("biaoshi") ? this.getIntent().getStringExtra("biaoshi") : "";
        dynaLat = this.getIntent().hasExtra("dynaLat") ? this.getIntent().getStringExtra("dynaLat") : "";
        dynaLng = this.getIntent().hasExtra("dynaLng") ? this.getIntent().getStringExtra("dynaLng") : "";
        if ("".equals(biaoshi)) {
            loactions = new LocationInfo[strName.length];
            ooA = new MarkerOptions[strName.length];
            oa = new Marker[strName.length];
            myMaphandler.postDelayed(mapThread, 100);
            setListener();
        } else {
            mSearch = RoutePlanSearch.newInstance();
            mSearch.setOnGetRoutePlanResultListener(this);
            btnXq2.setText("取消操作");
            btnDh2.setText("导航到这");
            final LatLng p = new LatLng(Double.parseDouble(dynaLat), Double.parseDouble(dynaLng));
            mMapView = new MapView(BaiDuYuanGongLocationActivity.this,
                    new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                            .target(p).build()));
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(p);
            converter.from(CoordinateConverter.CoordType.COMMON);
            final LatLng convertLatLng = converter.convert();
            MarkerOptions ooA1 = new MarkerOptions()
                    .position(convertLatLng)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.icon_gcoding))
                    .zIndex(9)
                    .draggable(false);
            mBaiduMap.addOverlay(ooA1);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 16.0f);
            mBaiduMap.animateMapStatus(u);
            mInfoWindow = new InfoWindow(vi3, convertLatLng, -100);
            mBaiduMap.showInfoWindow(mInfoWindow);
            LatLng latLngstar = new LatLng(mLat1, mLon1);
            CoordinateConverter converter2 = new CoordinateConverter();
            converter2.coord(latLngstar);
            converter2.from(CoordinateConverter.CoordType.COMMON);
            final LatLng convertstar = converter2.convert();
            mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
            MarkerOptions option = new MarkerOptions().position(convertstar).icon(mCurrentMarker);
            mBaiduMap.addOverlay(option);
            PlanNode stNode = PlanNode.withLocation(convertstar);
            PlanNode enNode = PlanNode.withLocation(convertLatLng);
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).to(enNode));
            btnDh2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNavi(convertLatLng.latitude, convertLatLng.longitude);
                }
            });
            btnXq2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBaiduMap.hideInfoWindow();
                }
            });
            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    mInfoWindow = new InfoWindow(vi3, convertLatLng, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                    return false;
                }
            });
        }
    }

    Runnable mapThread = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < strLat.length; i++) {
                loactions[i] = new LocationInfo(strLat[i], strLong[i], strName[i], strLoginId[i], strSex[i]);
            }
            for (int i = 0; i < loactions.length; i++) {
                View v = null;
                if ("01".equals(strSex[i])) {
                    v = LayoutInflater.from(BaiDuYuanGongLocationActivity.this).inflate(R.layout.item_map_user, null);
                } else {
                    v = LayoutInflater.from(BaiDuYuanGongLocationActivity.this).inflate(R.layout.item_map_user1, null);
                }
                v.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                tvTitle = (TextView) v.findViewById(R.id.tv_name);
                tvTitle.setText(loactions[i].title1);
                double lat = Double.valueOf(loactions[i].latitude);
                double lng = Double.valueOf(loactions[i].longtitude);
                LatLng p = new LatLng(lat, lng);
                mMapView = new MapView(BaiDuYuanGongLocationActivity.this,
                        new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                                .target(p).build()));
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
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 16.0f);
                mBaiduMap.animateMapStatus(u);
            }
            myMaphandler.removeCallbacks(mapThread);
        }
    };

    private void showMarker(double lat, double lng) {
        mBaiduMap.clear();
        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        LatLng p = new LatLng(lat, lng);
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(p);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();
        MarkerOptions option = new MarkerOptions().position(convertLatLng).icon(mCurrentMarker);
        mBaiduMap.addOverlay(option);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
        mBaiduMap.animateMapStatus(u);
    }

    private void setListener() {
        iv_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMylocaclicked = true;
                showMarker(mLat1, mLon1);
            }
        });
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(vi3, ll, -47);
                mBaiduMap.showInfoWindow(mInfoWindow);
                int j = 1;
                for (int i = 0; i < oa.length; i++) {
                    if (marker.equals(oa[i])) {
                        j = i;
                    }
                }
                final String id = strLoginId[j];
                final String name = strName[j];
                btnXq2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BaiDuYuanGongLocationActivity.this, QitePaidanActivity.class);
                        intent.putExtra("companyId", companyId);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("userId", id);
                        intent.putExtra("name", name);
                        intent.putExtra("totalAmt", totalAmt);
                        startActivity(intent);
                        finish();
                    }
                });
                btnDh2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBaiduMap.hideInfoWindow();
                    }
                });
                return true;
            }
        });
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
            Toast.makeText(getApplicationContext(), "距离太近或未找到结果", Toast.LENGTH_SHORT).show();
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

    private static class LocationInfo implements Serializable {
        private final String latitude;
        private final String longtitude;
        private String title1;
        private String id;
        private String sex;

        public LocationInfo(String latitude, String longtitude, String title1, String id, String sex) {
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

    private LatLng pianyi(double lon, double lat) {
        double x = lon;
        double y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * Math.PI);
        double temp = Math.atan2(y, x) + 0.000003 * Math.cos(x * Math.PI);
        double bdLon = z * Math.cos(temp) + 0.0065;
        double bdLat = z * Math.sin(temp) + 0.006;
        LatLng newcenpt = new LatLng(bdLat, bdLon);
        return newcenpt;
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
        if (mSearch != null) {
            mSearch.destroy();
        }
        if (mCurrentMarker != null) {
            mCurrentMarker.recycle();
            mCurrentMarker = null;
        }
    }

    public void back(View v) {
        if (isMylocaclicked) {
            setResult(RESULT_OK, new Intent().putExtra("lat", String.valueOf(mLat1)).putExtra("lng", String.valueOf(mLon1)));
        }
        finish();
    }
}