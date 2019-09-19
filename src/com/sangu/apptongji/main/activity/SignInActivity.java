package com.sangu.apptongji.main.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.main.qiye.presenter.IMemberPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.MemberPresenter;
import com.sangu.apptongji.main.qiye.view.IMemberView;
import com.sangu.apptongji.main.utils.JSONUtil;
import com.sangu.apptongji.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends CoreActivity implements IMemberView,BaiduMap.OnMapClickListener {
    @BindView(R.id.bmapView)
    MapView mMapView = null;
    LocationClient mLocClient = null;
    LocationClientOption option = null;
    public MyLocationListenner myListener = new MyLocationListenner();
    private BaiduMap mBaiduMap = null;
    private double mLat1, mLon1;
    boolean isFirstLoc = true; // 是否首次定位
    //    private boolean isFinished = false;
//    private boolean isLFinished = false;
    private IMemberPresenter memberPresenter = null;
    private InfoWindow mInfoWindow = null;
    private View vi2 = null;
    private TextView tvmaj1 = null, tvmaj2 = null, tvmaj3 = null, tvmaj4 = null, tv_titl = null, tv_zy1_bao = null, tv_zy2_bao = null, tv_zy3_bao = null,
            tv_zy4_bao = null, tvName = null, tvsign = null, tvAge = null, tv_bottom = null, tv_distance = null, iv_zy1_tupian = null,
            iv_zy2_tupian = null, iv_zy3_tupian = null, iv_zy4_tupian = null, tv_title;
    private Button btnXq = null, btnDh = null;
    private Button btnXq3 = null, btnDh3 = null;
    private CircleImageView ivAvatar = null,ivAvatar3;
    private ImageView ivSex3 = null,ivSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        mBaiduMap = mMapView.getMap();
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        memberPresenter = new MemberPresenter(this, this);
        memberPresenter.loadMemberList(0);
        vi2 = LayoutInflater.from(this).inflate(R.layout.item_find_fragments1, null);
        mBaiduMap.setOnMapClickListener(this);
        initV2();
    }

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

    @Override
    public void updateUserList(List<MemberInfo> users, boolean hasMore) {
        setUserMark(users);
    }

    private void setUserMark(List<MemberInfo> users) {
        for (int i = 0; i < users.size(); i++) {
            MemberInfo user = users.get(i);
            View v = LayoutInflater.from(SignInActivity.this).inflate(R.layout.item_map_user, null);
//            v.setLayoutParams(new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tvTitle = (TextView) v.findViewById(R.id.tv_name);
            double lat = Double.valueOf(user.getResv2());
            double lng = Double.valueOf(user.getResv1());
            LatLng p = new LatLng(lat, lng);
            if (i == 0) {
                tvTitle.setText("企");
                mInfoWindow = new InfoWindow(vi2,JSONUtil.convertGPSToBaidu(p) , -dip2px(50));
                mBaiduMap.showInfoWindow(mInfoWindow);
            } else {
                tvTitle.setText(user.getuName());
            }
            Log.e("name==", user.getuName() + "=" + lat + "-" + lng);
            mMapView = new MapView(SignInActivity.this,
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
            //oa.add(mar);
        }
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
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mInfoWindow != null) {
            mBaiduMap.hideInfoWindow();
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
            Log.e("location==", mLat1 + "-" + mLon1);
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
//            if (isFinished&&isLFinished) {
//                myMaphandler.sendEmptyMessage(0);
//            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时必须调用mMapView. onPause ()
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }
    public int dip2px(int dp)
    {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp*density+0.5);
    }


}
