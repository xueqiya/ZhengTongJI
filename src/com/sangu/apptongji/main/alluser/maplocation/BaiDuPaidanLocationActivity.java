package com.sangu.apptongji.main.alluser.maplocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.entity.BaoZInfo;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.avtivity.SouSuoActivity;
import com.sangu.apptongji.main.alluser.presenter.IFindPresenter;
import com.sangu.apptongji.main.alluser.presenter.IbzPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.BzPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FindPresenter;
import com.sangu.apptongji.main.alluser.view.IFindView;
import com.sangu.apptongji.main.alluser.view.IbzView;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.JSONUtil;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-10-13.
 */

public class BaiDuPaidanLocationActivity extends BaseActivity implements OnMapLongClickListener,OnMapClickListener,
        OnGetGeoCoderResultListener,BaiduMap.OnMapStatusChangeListener,IFindView,IbzView {
    private MapView mMapView = null;
    private TextView tvTitle = null, tv_sousuo = null;
    private BaiduMap mBaiduMap = null;
    BitmapDescriptor mCurrentMarker = null;
    private EditText et_search = null;
    private InfoWindow mInfoWindow = null;
    private String zhuanye, comName, name, myUserId;
    private double mLat1, mLon1;
    List<UserAll> users = new ArrayList<>();
    List<BaoZInfo> baoZInfos = new ArrayList<>();
    List<String> strLat = new ArrayList<>(), strLong = new ArrayList<>(), strLoginId = new ArrayList<>(), strName = new ArrayList<>(), strSex = new ArrayList<>();
    private TextView tvmaj1 = null, tvmaj2 = null, tvmaj3 = null, tvmaj4 = null, tv_titl = null, tv_zy1_bao = null, tv_zy2_bao = null, tv_zy3_bao = null,
            tv_zy4_bao = null, tvName = null, tvsign = null, tvAge = null, iv_zy1_tupian = null, iv_zy2_tupian = null, iv_zy3_tupian = null, iv_zy4_tupian = null;
    private TextView tv_company = null, tv_company_count = null, tv_distance = null;
    private Button btnXq = null, btnDh = null, btnXq2 = null, btnDh2 = null;
    private ImageView ivSex = null, iv_mylocation = null, iv_baoxiang = null;
    private CircleImageView ivAvatar = null;
    private LocationInfo[] loactions = null;
    double oldLat, oldLng, nowLat, nowLng;
    View vi2 = null, vi3 = null;
    List<Marker> oa = new ArrayList<>();
    List<Marker> oa2 = new ArrayList<>();
    private IFindPresenter findPresenter = null;
    private IbzPresenter bzPresenter = null;
    private boolean isBaoZclicked = false;
    private boolean isBaoZfirst = true;
    private boolean isMylocaclicked = false;
    private boolean issousuo = false;

    @Override
    public void onMapLongClick(final LatLng latLng) {
        mInfoWindow = new InfoWindow(vi3, latLng, -47);
        mBaiduMap.showInfoWindow(mInfoWindow);
        btnXq2.setText("附近的人");
        btnDh2.setText("导航到这");
        btnXq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowLat = latLng.latitude;
                nowLng = latLng.longitude;
                setResult(RESULT_OK, new Intent().putExtra("lat", String.valueOf(nowLat)).putExtra("lng", String.valueOf(nowLng))
                        .putExtra("comName", "").putExtra("zhuanye", ""));
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
        builder.setTitle("温馨提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(BaiDuPaidanLocationActivity.this);
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
        setContentView(R.layout.activity_baidumap_paidan);
        WeakReference<BaiDuPaidanLocationActivity> reference = new WeakReference<BaiDuPaidanLocationActivity>(BaiDuPaidanLocationActivity.this);
        findPresenter = new FindPresenter(this,reference.get());
        if (DemoHelper.getInstance().getCurrentUsernName() != null) {
            myUserId = DemoHelper.getInstance().getCurrentUsernName();
        } else {
            myUserId = "";
        }
        bzPresenter = new BzPresenter(this,reference.get());

        tv_sousuo = (TextView) findViewById(R.id.tv_sousuo);
        iv_mylocation = (ImageView) findViewById(R.id.iv_mylocation);
        zhuanye = "";
        comName = "";
        String mlat = this.getIntent().getStringExtra("mlat");
        String mlon = this.getIntent().getStringExtra("mlon");
        if (mlat != null && !"".equals(mlat)) {
            mLat1 = Double.parseDouble(mlat);
        }
        if (mlon != null && !"".equals(mlon)) {
            mLon1 = Double.parseDouble(mlon);
        }
        oldLat = mLat1;
        oldLng = mLon1;
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        vi3 = LayoutInflater.from(reference.get()).inflate(R.layout.item_find_fragments2, null);
        btnXq2 = (Button) vi3.findViewById(R.id.btn_xiangqing);
        btnDh2 = (Button) vi3.findViewById(R.id.btn_daohang);
        vi2 = LayoutInflater.from(reference.get()).inflate(R.layout.item_find_fragments, null);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mMapView.removeViewAt(1);
        //设置是否显示比例尺控件
        mMapView.showScaleControl(false);
        //设置是否显示缩放控件
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setOnMapClickListener(reference.get());
        mBaiduMap.setOnMapLongClickListener(reference.get());
        mBaiduMap.setOnMapStatusChangeListener(reference.get());
        initMapView();
        initV2();
        strLat = this.getIntent().getStringArrayListExtra("lat");
        strLong = this.getIntent().getStringArrayListExtra("lng");
        strLoginId = this.getIntent().getStringArrayListExtra("loginId");
        strName = this.getIntent().getStringArrayListExtra("name");
        strSex = this.getIntent().getStringArrayListExtra("sex");
        loactions = new LocationInfo[strName.size()];
        setListener();
        showMarker(mLat1, mLon1);
        loadIcon();
    }

    private void loadIcon() {
        for (int i = 0; i < strLat.size(); i++) {
            loactions[i] = new LocationInfo(strLat.get(i), strLong.get(i), strName.get(i), strLoginId.get(i), strSex.get(i));
        }
        for (int i = 0; i < loactions.length; i++) {
            View v = null;
            if ("00".equals(strSex.get(i))) {
                v = LayoutInflater.from(BaiDuPaidanLocationActivity.this).inflate(R.layout.item_map_user1, null);
            } else {
                v = LayoutInflater.from(BaiDuPaidanLocationActivity.this).inflate(R.layout.item_map_user, null);
            }
            v.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tvTitle = (TextView) v.findViewById(R.id.tv_name);
            tvTitle.setText(loactions[i].title1);
            if (loactions[i].latitude != null && !"".equals(loactions[i].latitude) && loactions[i].longtitude != null && !"".equals(loactions[i].longtitude)) {
                double lat = Double.valueOf(loactions[i].latitude);
                double lng = Double.valueOf(loactions[i].longtitude);
                LatLng p = new LatLng(lat, lng);
                mMapView = new MapView(BaiDuPaidanLocationActivity.this,
                        new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                                .target(p).build()));
                CoordinateConverter converter = new CoordinateConverter();
                converter.coord(p);
                converter.from(CoordinateConverter.CoordType.COMMON);
                LatLng convertLatLng = converter.convert();
                MarkerOptions aaA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory.fromView(v))
                        .zIndex(10)
                        .draggable(false);
                Marker ooa = (Marker) mBaiduMap.addOverlay(aaA);
                oa.add(ooa);
            }
        }
    }

    private void initV2() {
        tvAge = (TextView) vi2.findViewById(R.id.tv_nianling);
        tv_company = (TextView) vi2.findViewById(R.id.tv_company);
        tv_distance = (TextView) vi2.findViewById(R.id.tv_distance);
        tv_company_count = (TextView) vi2.findViewById(R.id.tv_company_count);
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

        tv_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(BaiDuPaidanLocationActivity.this, SouSuoActivity.class), 1);
            }
        });

        iv_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBaoZclicked) {
                    mBaiduMap.clear();
                    strLoginId.clear();
                    strName.clear();
                    strLong.clear();
                    strLat.clear();
                    strSex.clear();
                    oa.clear();
                }
                isBaoZfirst = false;
                isBaoZclicked = false;
                iv_mylocation.setImageResource(R.drawable.gps_mylocation);
                zhuanye = null;
                comName = null;
                isMylocaclicked = true;
                String mlat = DemoApplication.getInstance().getCurrentLat();
                String mlon = DemoApplication.getInstance().getCurrentLng();
                if (mlat != null && !"".equals(mlat)) {
                    nowLat = mLat1;
                    mLat1 = Double.parseDouble(mlat);
                }
                if (mlon != null && !"".equals(mlon)) {
                    nowLng = mLon1;
                    mLon1 = Double.parseDouble(mlon);
                }
                findPresenter.loadUserList(myUserId, "1", "1", nowLng + "", nowLat + "", zhuanye, null, null, null, null, name, comName, false, false, false);
                showMarker(mLat1, mLon1);
            }
        });

        /*mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (isBaoZclicked && DemoHelper.getInstance().isLoggedIn(BaiDuPaidanLocationActivity.this)) {
                    String lat = DemoApplication.getInstance().getCurrentLat();
                    String lng = DemoApplication.getInstance().getCurrentLng();
                    double Lat1 = 0, Lon1 = 0;
                    if (lat != null && !"".equals(lat)) {
                        Lat1 = Double.parseDouble(lat);
                    }
                    if (lng != null && !"".equals(lng)) {
                        Lon1 = Double.parseDouble(lng);
                    }
                    BaoZInfo baoZInfo;
                    LatLng ll1 = marker.getPosition();
                    final LatLng ll2 = new LatLng(Lat1, Lon1);
                    CoordinateConverter converter = new CoordinateConverter();
                    converter.coord(ll2);
                    converter.from(CoordinateConverter.CoordType.COMMON);
                    LatLng convertLatLng = converter.convert();
                    double distance = DistanceUtil.getDistance(convertLatLng, ll1);
                    btnXq2.setText("取消操作");
                    if (distance <= 30) {
                        btnDh2.setText("解密宝藏");
                    } else {
                        btnDh2.setText("路径规划");
                    }
                    MapStatus mMapStatus = new MapStatus.Builder()
                            .target(ll1)
                            .zoom(18)
                            .build();
                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                    //改变地图状态
                    mBaiduMap.setMapStatus(mMapStatusUpdate);
                    mInfoWindow = new InfoWindow(vi3, ll1, -47);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                    for (int i = 0; i < oa2.size(); i++) {
                        if (marker.equals(oa2.get(i))) {
                            baoZInfo = baoZInfos.get(i);
                            oa2.get(i).setToTop();
                            final String dynamicSeq = baoZInfo.getDynamicSeq();
                            final String createTime = baoZInfo.getCreateTime();
                            btnDh2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivityForResult(new Intent(BaiDuPaidanLocationActivity.this, DynaDetaActivity.class).putExtra("dynamicSeq", dynamicSeq)
                                            .putExtra("createTime", createTime).putExtra("biaoshi", "01").putExtra("dType", "02").putExtra("sID", DemoHelper.getInstance().getCurrentUsernName()), 2);
                                }
                            });
                            btnXq2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mBaiduMap.hideInfoWindow();

                                }
                            });
                        }
                    }
                } else {
                    for (int i = 0; i < oa.size(); i++) {
                        if (marker.equals(oa.get(i))) {
                            oa.get(i).setToTop();
                            String id = strLoginId.get(i);
                            String url = FXConstant.URL_Get_UserInfo + id;
                            final int finalI = i;
                            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    setView(s, strLat.get(finalI), strLong.get(finalI));
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
                            MySingleton.getInstance(BaiDuPaidanLocationActivity.this).addToRequestQueue(request);
                            final int[] j = {finalI};
                        }
                    }
                }

                return true;
            }
        });*/
    }

    private void sousuo(String str) {
        isBaoZfirst = true;
        isBaoZclicked = false;
        if (str != null) {
            issousuo = true;
            mBaiduMap.clear();
            strLoginId.clear();
            strName.clear();
            strLong.clear();
            strLat.clear();
            strSex.clear();
            oa.clear();
            findPresenter.loadUserList(myUserId, "1", "1", nowLng + "", nowLat + "", str, null, null, null, null, null, null, false, false, false);
        }
    }

    /**
     * 隐藏输入框
     */
    public void hideCommentEditText(Activity context) {
            if(context==null){
                return;
            }
            final View v = ((Activity) context).getWindow().peekDecorView();
            if (v != null && v.getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
//        if (getCurrentFocus()!=null)
//            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
                tvAge.setBackgroundColor(Color.rgb(0, 172, 255));
                tv_titl.setBackgroundResource(R.drawable.fx_bg_text_gra);
            }
            if (!imageStr.equals("")) {
                String[] images = imageStr.split("\\|");
                ivAvatar.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + images[0], ivAvatar, DemoApplication.mOptions);
            } else {
                ivAvatar.setVisibility(View.INVISIBLE);
                tv_titl.setVisibility(View.VISIBLE);
                tv_titl.setText(name);
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
            if (strLat != null && String.valueOf(mLat1) != null) {
                if (!("".equals(strLat) || "".equals(strLon) || mLat1 == 0.0)) {
                    double latitude1 = Double.valueOf(strLat);
                    double longitude1 = Double.valueOf(strLon);
                    final LatLng ll1 = new LatLng(mLat1, mLon1);
                    LatLng ll = new LatLng(latitude1, longitude1);
                    double distance = DistanceUtil.getDistance(ll, ll1);
                    double dou = distance / 1000;
                    String str = String.format("%.2f", dou);//format 返回的是字符串
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
            tvName.setText(name);
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
                    startNavi(Double.valueOf(strLat), Double.valueOf(strLon));
                    //finish();
                }
            });
            btnXq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaiDuPaidanLocationActivity.this, UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID, id);
                    startActivity(intent);
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
        if (isSoftShowing()) {
            hideCommentEditText(this);
        }
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
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
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiDuPaidanLocationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        btnXq2.setText("附近的人");
        btnDh2.setText("导航到这");
        mBaiduMap.clear();
        strLoginId.clear();
        strName.clear();
        strLong.clear();
        strLat.clear();
        strSex.clear();
        oa.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_150)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        double[] d = JSONUtil.bd09togcj02(result.getLocation().longitude, result.getLocation().latitude);
        final String strlat = String.format("%.6f", d[1]);
        final String strlng = String.format("%.6f", d[0]);
        nowLat = Double.parseDouble(strlat);
        nowLng = Double.parseDouble(strlng);
        final LatLng latLng1 = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
        mInfoWindow = new InfoWindow(vi3, latLng1, -47);
        mBaiduMap.showInfoWindow(mInfoWindow);
        btnXq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, new Intent().putExtra("lat", String.valueOf(nowLat)).putExtra("lng", String.valueOf(nowLng))
                        .putExtra("comName", "").putExtra("zhuanye", ""));
                finish();
            }
        });
        btnDh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaiduMap.hideInfoWindow();

                startNavi(latLng1.latitude, latLng1.longitude);
            }
        });
        findPresenter.loadUserList(myUserId, "1", "1", strlng, strlat, zhuanye, null, null, null, null, name, comName, false, false, false);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {
        if (!isBaoZclicked) {
            if (isMylocaclicked) {
                iv_mylocation.setImageResource(R.drawable.gps_mylocation);
            } else {
                iv_mylocation.setImageResource(R.drawable.gps_mylocationt);
            }
            isMylocaclicked = false;
            LatLng _latLng = mapStatus.target;
            double nowLat1 = _latLng.latitude;
            double nowLng1 = _latLng.longitude;
            double[] dou = JSONUtil.bd09togcj02(nowLng1, nowLat1);
            nowLat = dou[1];
            nowLng = dou[0];
            LatLng p1LL = new LatLng(nowLat, nowLng);
            LatLng p2LL = new LatLng(oldLat, oldLng);
            double distance = DistanceUtil.getDistance(p1LL, p2LL);
            if (distance > 50) {
                findPresenter.loadUserList(myUserId, "1", "1", nowLng + "", nowLat + "", zhuanye, null, null, null, null, name, comName, false, false, false);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (isBaoZclicked) {
                    mBaiduMap.clear();
                }
                isBaoZfirst = true;
                isBaoZclicked = false;
                if (data != null) {
                    String city = data.getStringExtra("city");
                    String addr = data.getStringExtra("key");
                    et_search.setText(addr);
                }
                break;
            case 1:
                isBaoZfirst = true;
                isBaoZclicked = false;
                if (data != null) {
                    name = data.hasExtra("name") ? data.getStringExtra("name") : null;
                    comName = data.hasExtra("comName") ? data.getStringExtra("comName") : null;
                    zhuanye = data.hasExtra("zhuanye") ? data.getStringExtra("zhuanye") : null;
                    String isclear = data.hasExtra("isclear") ? data.getStringExtra("isclear") : "0";
                    if (comName != null) {
                        try {
                            comName = URLEncoder.encode(comName, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    if ("1".equals(isclear)) {
                        comName = null;
                        name = null;
                        zhuanye = null;
                    }
                    issousuo = true;
                    mBaiduMap.clear();
                    strLoginId.clear();
                    strName.clear();
                    strLong.clear();
                    strLat.clear();
                    strSex.clear();
                    oa.clear();
                    findPresenter.loadUserList(myUserId, "1", "1", nowLng + "", nowLat + "", zhuanye, null, null, null, null, name, comName, false, false, false);
                }
                break;
            case 2:
                oa2.clear();
                baoZInfos.clear();
                bzPresenter.loadbaozList();
                break;
        }
    }

    @Override
    public void updateBzList(List<BaoZInfo> baozInfos) {
        mBaiduMap.clear();
        Log.e("baidumac,bz", baozInfos.size() + "");
        this.baoZInfos = baozInfos;
        if (this.baoZInfos != null && this.baoZInfos.size() > 0) {
            BzRunnable runnable = new BzRunnable(BaiDuPaidanLocationActivity.this);
            new Thread(runnable).start();
        } else {
            Toast.makeText(getApplicationContext(), "没有更多的数据了...", Toast.LENGTH_SHORT).show();
        }
    }

    private static class BzRunnable implements Runnable {
        WeakReference<BaiDuPaidanLocationActivity> mactivity;

        public BzRunnable(BaiDuPaidanLocationActivity activity) {
            mactivity = new WeakReference<BaiDuPaidanLocationActivity>(activity);
        }

        @Override
        public void run() {
            BaoZInfo baoZInfo = null;
            String lng;
            String lat,geshu;
            for (int i = 0; i < mactivity.get().baoZInfos.size(); i++) {
                baoZInfo = mactivity.get().baoZInfos.get(i);
                lng = baoZInfo.getLng();
                lat = baoZInfo.getLat();
                geshu = baoZInfo.getRedSum();
                if (lng != null && !"".equals(lng)) {
                    Log.e("baidumac,bzoa", i + "开始添加");
                    View v = LayoutInflater.from(mactivity.get()).inflate(R.layout.item_map_user2, null);
                    v.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    TextView tv_geshu = (TextView) v.findViewById(R.id.tv_geshu);
                    tv_geshu.setText(geshu);
                    LatLng p = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    CoordinateConverter converter = new CoordinateConverter();
                    converter.coord(p);
                    converter.from(CoordinateConverter.CoordType.COMMON);
                    LatLng convertLatLng = converter.convert();
                    MarkerOptions mop = new MarkerOptions()
                            .position(convertLatLng)
                            .icon(BitmapDescriptorFactory
                                    .fromView(v))
                            .zIndex(9)
                            .draggable(false);
                    Marker mark = (Marker) mactivity.get().mBaiduMap.addOverlay(mop);
                    mactivity.get().oa2.add(mark);
                    if (i == mactivity.get().baoZInfos.size() / 2 && mactivity.get().isBaoZfirst) {
                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
                        mactivity.get().mBaiduMap.animateMapStatus(u);
                        mactivity.get().isBaoZfirst = false;
                    }
                }
            }
        }
    }

    private static class MyRunnable implements Runnable {

        WeakReference<BaiDuPaidanLocationActivity> mactivity;

        public MyRunnable(BaiDuPaidanLocationActivity activity) {
            mactivity = new WeakReference<BaiDuPaidanLocationActivity>(activity);
        }

        @Override
        public void run() {
            boolean isrepeated = false;
            String id = null;
            UserAll userAll = null;
            String name;
            String sex;
            String lng;
            String lat;
            View v = null;
            if (mactivity.get().users!=null) {
                for (int i = 0; i < mactivity.get().users.size(); i++) {
                    userAll = mactivity.get().users.get(i);
                    id = userAll.getuLoginId();
                    if (mactivity.get().strLoginId!=null) {
                        for (int j = 0; j < mactivity.get().strLoginId.size(); j++) {
                            if (mactivity.get().strLoginId.get(j).equals(id)) {
                                isrepeated = true;
                                break;
                            }
                        }
                    }else {
                        mactivity.get().strLoginId = new ArrayList<>();
                        mactivity.get().strName = new ArrayList<>();
                        mactivity.get().strSex = new ArrayList<>();
                        mactivity.get().strLat = new ArrayList<>();
                        mactivity.get().strLong = new ArrayList<>();
                    }
                    if (!isrepeated&&mactivity.get().strLoginId!=null) {
                        name = userAll.getuName();
                        sex = userAll.getuSex();
                        lng = userAll.getResv1();
                        lat = userAll.getResv2();
                        if (lng != null && !"".equals(lng)) {
                            mactivity.get().strLoginId.add(id);
                            mactivity.get().strName.add(name);
                            mactivity.get().strSex.add(sex);
                            mactivity.get().strLat.add(lat);
                            mactivity.get().strLong.add(lng);
                            if (sex.equals("00")) {
                                v = LayoutInflater.from(mactivity.get()).inflate(R.layout.item_map_user1, null);
                            } else {
                                v = LayoutInflater.from(mactivity.get()).inflate(R.layout.item_map_user, null);
                            }
                            v.setLayoutParams(new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                            mactivity.get().tvTitle = (TextView) v.findViewById(R.id.tv_name);
                            mactivity.get().tvTitle.setText(name);
                            LatLng p = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                            CoordinateConverter converter = new CoordinateConverter();
                            converter.coord(p);
                            converter.from(CoordinateConverter.CoordType.COMMON);
                            LatLng convertLatLng = converter.convert();
                            MarkerOptions mop = new MarkerOptions()
                                    .position(convertLatLng)
                                    .icon(BitmapDescriptorFactory
                                            .fromView(v))
                                    .zIndex(9)
                                    .draggable(false);
                            if (mactivity.get() != null) {
                                Marker mark = (Marker) mactivity.get().mBaiduMap.addOverlay(mop);
                                mactivity.get().oa.add(mark);
                            }
                            if (i == mactivity.get().users.size() / 2 && mactivity.get().issousuo) {
                                final MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
                                if (Looper.myLooper() != Looper.getMainLooper()) {
                                    Handler mainHandler = new Handler(Looper.getMainLooper());
                                    mainHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //已在主线程中，可以更新UI
                                            mactivity.get().mBaiduMap.animateMapStatus(u);
                                        }
                                    });
                                } else {
                                    mactivity.get().mBaiduMap.animateMapStatus(u);
                                }

                                mactivity.get().issousuo = false;
                            }
                        }
                        isrepeated = false;
                    }
                }
            }
        }
    }

    @Override
    public void updateUserList(List<UserAll> users, boolean hasMore) {
        oldLng = nowLng;
        oldLat = nowLat;
        this.users = users;
        if (this.users != null && this.users.size() > 0) {
            if (mBaiduMap==null && mMapView != null){
                mBaiduMap = mMapView.getMap();
            }
            MyRunnable runnable = new MyRunnable(BaiDuPaidanLocationActivity.this);
            new Thread(runnable).start();
        } else {
            Toast.makeText(getApplicationContext(), "没有更多的数据了...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void showError(String msg) {
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
        if (mBaiduMap != null) {
            mBaiduMap.clear();
            mBaiduMap = null;
        }
        if (mMapView != null) {
            mMapView.onDestroy();
            mMapView = null;
        }

        if (mCurrentMarker != null) {
            mCurrentMarker.recycle();
            mCurrentMarker = null;
        }
        if (mInfoWindow != null) {
            mInfoWindow = null;
        }
        if (oa != null) {
            oa.clear();
            oa = null;
        }
        if (strName != null) {
            strName.clear();
            strName = null;
        }
        if (strSex != null) {
            strSex.clear();
            strSex = null;
        }
        if (strLoginId != null) {
            strLoginId.clear();
            strLoginId = null;
        }
        if (strLat != null) {
            strLat.clear();
            strLat = null;
        }
        if (strLong != null) {
            strLong.clear();
            strLong = null;
        }
    }

    public void back(View v) {
        if (isMylocaclicked) {
            setResult(RESULT_OK, new Intent().putExtra("lat", String.valueOf(mLat1)).putExtra("lng", String.valueOf(mLon1))
                    .putExtra("comName", "").putExtra("zhuanye", ""));
        } else if (nowLat != 0.0 && !"".equals(nowLat)) {
            setResult(RESULT_OK, new Intent().putExtra("lat", String.valueOf(nowLat)).putExtra("lng", String.valueOf(nowLng))
                    .putExtra("comName", comName).putExtra("zhuanye", zhuanye));
        }
        finish();
    }
}
