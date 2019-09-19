package com.sangu.apptongji.main.alluser.maplocation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanxin.easeui.EaseConstant;
import com.fanxin.easeui.utils.FileStorage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.CollectionDynamicActivity;
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
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.utils.AnimationUtil;
import com.sangu.apptongji.main.utils.BitmapUtils;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.JSONUtil;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;
import com.sangu.apptongji.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2016-10-13.
 */

public class BaiDuMLocationActivity extends BaseActivity implements OnMapLongClickListener,OnMapClickListener,
        OnGetGeoCoderResultListener,BaiduMap.OnMapStatusChangeListener,IFindView,IbzView {
    private MapView mMapView = null;
    private TextView tvTitle = null, tv_sousuo = null;
    private Button btn_previous = null, btn_next = null;
    private ImageView iv1,iv2,iv3,iv4,iv5;
    private TextView tv_v1,tv_v2,tv_v3,tv_v4;
    private GeoCoder mSearch = null;
    private BaiduMap mBaiduMap = null;
    BitmapDescriptor mCurrentMarker = null;
    private LinearLayout ll_search = null,ll_gongxiang;
    private EditText et_search = null;
    private InfoWindow mInfoWindow = null;
    private String zhuanye, comName, name, myUserId;
    private double mLat1, mLon1;
    List<UserAll> users = new ArrayList<>();
    private List<com.alibaba.fastjson.JSONObject> locationDatas=new ArrayList<>();

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
    View vi2 = null, vi3 = null, vi4 = null;
    List<Marker> oa = new ArrayList<>();
    List<Marker> oa2 = new ArrayList<>();
    private IFindPresenter findPresenter = null;
    private IbzPresenter bzPresenter = null;
    private boolean isBaoZclicked = false;
    private boolean isBaoZfirst = true;
    private boolean isMylocaclicked = false;
    private boolean issousuo = false;
    private String isFristLoadLocation="no";
    private String sID;
    private ImageView iv_mysearch;
    private LinearLayout ll_chatline;
    private String isClickLeftType = "0";


    CircleImageView iv_avatar;
    // 昵称
    TextView tvDistance;
    TextView tv_count_llc;
    TextView tv_nick;
    // 时间
    TextView tv_time;
    TextView tv_video;
    TextView tvTitleA;
    TextView tvNianLing;
    TextView tvCompany;
    // 三行图片
    LinearLayout ll_one;
    LinearLayout ll_two;
    ImageView image_1;
    ImageView image_2;
    ImageView image_3;
    ImageView image_4;
    ImageView image_5;
    ImageView image_6;
    ImageView image_8;
    ImageView image_7;
    ImageView iv_ditu;
    ImageView iv_bz_type;
    // 动态内容
    TextView tv_count_pl;
    TextView tv_count_zhf;
    TextView tv_geshu;
    TextView tv_content;
    // 位置
    TextView tv_location;
    LinearLayout card;
    RelativeLayout rl_geshu;
    RelativeLayout rl_neirong;
    RelativeLayout rl_zhuanfa;
    RelativeLayout rl_pinglun;
    RelativeLayout rl_video;
    RelativeLayout rl_collection;
    TextView tv_collectioncount;
    JCVideoPlayerStandard videoPlayer;
    TextView locationMapView;
    TextView tv_collectiontitle;
    TextView tv_demandType;

    private RelativeLayout rl_mapLayout;

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
                OpenClientUtil.getLatestBaiduMapApp(BaiDuMLocationActivity.this);
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
        setContentView(R.layout.activity_baidumap_m);
        WeakReference<BaiDuMLocationActivity> reference = new WeakReference<BaiDuMLocationActivity>(BaiDuMLocationActivity.this);
        findPresenter = new FindPresenter(this,reference.get());
        if (DemoHelper.getInstance().getCurrentUsernName() != null) {
            myUserId = DemoHelper.getInstance().getCurrentUsernName();
        } else {
            myUserId = "";
        }
        bzPresenter = new BzPresenter(this,reference.get());
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);
        iv5 = (ImageView) findViewById(R.id.iv5);
        tv_v1 = (TextView) findViewById(R.id.tv_v1);
        tv_v2 = (TextView) findViewById(R.id.tv_v2);
        tv_v3 = (TextView) findViewById(R.id.tv_v3);
        tv_v4 = (TextView) findViewById(R.id.tv_v4);
        ll_chatline = (LinearLayout) findViewById(R.id.ll_chatline);

        rl_mapLayout = (RelativeLayout) findViewById(R.id.rl_mapLayout);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_previous = (Button) findViewById(R.id.btn_previous);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ll_gongxiang = (LinearLayout) findViewById(R.id.ll_gongxiang);
        tv_sousuo = (TextView) findViewById(R.id.tv_sousuo);
        iv_baoxiang = (ImageView) findViewById(R.id.iv_baoxiang);
        iv_mylocation = (ImageView) findViewById(R.id.iv_mylocation);
        iv_mysearch = (ImageView) findViewById(R.id.iv_mysearch);

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
        SharedPreferences sp = getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
        final String city = sp.getString("city", "郑州市");
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(reference.get());
        et_search = (EditText) findViewById(R.id.et_search);
        et_search.setFocusable(false);
        et_search.setFocusableInTouchMode(false);
        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_chatline.setVisibility(View.GONE);
                startActivityForResult(new Intent(BaiDuMLocationActivity.this, SuggestActivity.class).putExtra("city", city), 0);
            }
        });
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        vi3 = LayoutInflater.from(reference.get()).inflate(R.layout.item_find_fragments2, null);
        btnXq2 = (Button) vi3.findViewById(R.id.btn_xiangqing);
        btnDh2 = (Button) vi3.findViewById(R.id.btn_daohang);
        vi2 = LayoutInflater.from(reference.get()).inflate(R.layout.item_find_fragments, null);
        vi4 = LayoutInflater.from(reference.get()).inflate(R.layout.item_find_locationdynamic, null);

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
        initLocationView();
        strLat = this.getIntent().getStringArrayListExtra("lat");
        strLong = this.getIntent().getStringArrayListExtra("lng");
        strLoginId = this.getIntent().getStringArrayListExtra("loginId");
        strName = this.getIntent().getStringArrayListExtra("name");
        strSex = this.getIntent().getStringArrayListExtra("sex");
        loactions = new LocationInfo[strName.size()];
        setListener();
        showMarker(mLat1, mLon1);
        loadIcon();

        String isSousuoPush = this.getIntent().getStringExtra("sousuopush");

        if (isSousuoPush != null ){

            if (isSousuoPush.equals("yes")){

                isBaoZclicked = true;
                oa2.clear();
                baoZInfos.clear();
                bzPresenter.loadbaozList();

            }

        }

        String isLocationDynamic = this.getIntent().getStringExtra("locationDynamic");


        if (isLocationDynamic != null && isLocationDynamic.equals("yes")){

            isFristLoadLocation = "yes";
            sID = getIntent().getStringExtra("sID");

            String lat = getIntent().getStringExtra("mlat");
            String lng = getIntent().getStringExtra("mlon");

            findPresenter.loadUserList(myUserId, "1", "1", lng + "", lat + "", "", null, null, null, null, "", "", false, false, false);

        }

        iv_mysearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_chatline.setVisibility(View.GONE);
                startActivityForResult(new Intent(BaiDuMLocationActivity.this, SouSuoActivity.class), 1);
            }
        });

        tv_v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_chatline.setVisibility(View.VISIBLE);
            }
        });
        tv_v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.clear();
                strLoginId.clear();
                strName.clear();
                strLong.clear();
                strLat.clear();
                strSex.clear();
                oa.clear();
                ll_chatline.setVisibility(View.GONE);
                isClickLeftType = "店";
                selectLocationDynamic();
            }
        });
        tv_v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.clear();
                strLoginId.clear();
                strName.clear();
                strLong.clear();
                strLat.clear();
                strSex.clear();
                oa.clear();
                ll_chatline.setVisibility(View.GONE);
                isClickLeftType = "玩";
                selectLocationDynamic();
            }
        });

        tv_v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_chatline.setVisibility(View.GONE);
                mBaiduMap.clear();
                strLoginId.clear();
                strName.clear();
                strLong.clear();
                strLat.clear();
                strSex.clear();
                oa.clear();
                comName = "低碳居";
                findPresenter.loadUserList(myUserId, "1", "1", mLon1 + "", mLat1 + "", null, null, null, null, null, null, "低碳居", false, false, false);

            }

        });

        ll_chatline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ll_chatline.setVisibility(View.GONE);
                Intent intent2 = new Intent(BaiDuMLocationActivity.this, ChatActivity.class);
                intent2.putExtra("userId", "18337101357");
                intent2.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                intent2.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                intent2.putExtra(EaseConstant.EXTRA_USER_SHARERED,"无");
                BaiDuMLocationActivity.this.startActivity(intent2);

            }
        });


        for (int j = 0; j < strLoginId.size(); j++) {

            if (strLoginId.get(j).equals(DemoHelper.getInstance().getCurrentUsernName())) {

                LoadMineView(j);

            }

        }

        String redPromote = getIntent().getStringExtra("redPromote");
        if (redPromote != null){
            LoadMineView(0);
        }

    }

    private void LoadMineView(final int j){

        ll_chatline.setVisibility(View.GONE);
        ll_gongxiang.setVisibility(View.GONE);

        String id = DemoHelper.getInstance().getCurrentUsernName();

        String url = FXConstant.URL_Get_UserInfo + id;

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                String mlon = strLong.get(j)+"";
                String mlat = strLat.get(j)+"";

                setView(s, mlat, mlon);

                Marker marker;

                if (j+1 < strLoginId.size()){
                    marker = oa.get(j+1);
                }else {
                    marker = oa.get(j);
                }

                LatLng ll1 = marker.getPosition();
               // LatLng ll1 = new LatLng(Double.valueOf(mlat),Double.valueOf(mlon));
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

        MySingleton.getInstance(BaiDuMLocationActivity.this).addToRequestQueue(request);

    }



    private void ShareQzone(){

        mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v1;
                v1 = inflater.inflate(R.layout.mapshare_topalert,null);

                TextView tv_adress = (TextView) v1.findViewById(R.id.tv_adress);

                SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
                String location = mSharedPreferences.getString("location","0");

                if (location.equals("0")){
                    tv_adress.setText("我现在的接单位置：");
                }else {
                    tv_adress.setText("我现在的接单位置："+location);
                }


                v1.destroyDrawingCache();
                v1.setLayoutParams(new LinearLayout.LayoutParams(bitmap.getWidth(), 120));
                v1.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                v1.layout(0, 0, v1.getMeasuredWidth(), v1.getMeasuredHeight());
                v1.setDrawingCacheEnabled(true);
                Bitmap head = v1.getDrawingCache(true);

                int headWidth = head.getWidth();
                int kebianwidth = bitmap.getWidth();

                int headHeight = head.getHeight();
                int kebiaoheight = bitmap.getHeight();
                //生成三个图片合并大小的Bitmap  为防止出现oom采用RGB_565 不适用ARGB_8888
                Bitmap newbmp = Bitmap.createBitmap(kebianwidth, headHeight + kebiaoheight, Bitmap.Config.RGB_565);
                Canvas cv = new Canvas(newbmp);
                cv.drawBitmap(head, (kebianwidth - headWidth)/2, 0, null);// 在 0，0坐标开始画入headBitmap

                //因为手机不同图片的大小的可能小了 就绘制白色的界面填充剩下的界面
                if (headWidth < kebianwidth) {
                    System.out.println("绘制头");
                    Bitmap ne = Bitmap.createBitmap((kebianwidth - headWidth)/2, headHeight, Bitmap.Config.RGB_565);
                    Canvas canvas = new Canvas(ne);
                    canvas.drawColor(Color.rgb(33,150,243));
                    cv.drawBitmap(ne, 0, 0, null);
                    cv.drawBitmap(ne, headWidth+(kebianwidth - headWidth)/2, 0, null);
                }
                cv.drawBitmap(bitmap, 0, headHeight, null);// 在 0，headHeight坐标开始填充课表的Bitmap
                cv.save(Canvas.ALL_SAVE_FLAG);// 保存
                cv.restore();// 存储
                //回收
                head.recycle();
                bitmap.recycle();

                File file = new FileStorage("fenxiang").createCropFile("mingpCut.png",null);

                Uri imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(mContext, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
                    mContext.grantUriPermission(mContext.getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    imageUri = Uri.fromFile(file);
                }
                File f = new File(file.getPath());
                try {
                    //如果文件不存在，则创建文件
                    if(!f.exists()){
                        f.createNewFile();
                    }
                    //输出流
                    FileOutputStream out = new FileOutputStream(f);
                    /** mBitmap.compress 压缩图片
                     *
                     *  Bitmap.CompressFormat.PNG   图片的格式
                     *   100  图片的质量（0-100）
                     *   out  文件输出流
                     */
                    newbmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();


                    final QQ.ShareParams sp = new QQ.ShareParams();

                  //  ScreenshotUtil.getBitmapByView(BaiDuMLocationActivity.this,rl_mapLayout, "分享动态红包", null,1,false,0,0);
                    sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");

                    sp.setTitle(null);
                    sp.setText(null);
                    Platform qqf = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                    qqf.setPlatformActionListener(new PlatformActionListener() {
                        public void onError(Platform arg0, int arg1, Throwable arg2) {
                            Toast.makeText(BaiDuMLocationActivity.this, "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                        }

                        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {

                            Toast.makeText(BaiDuMLocationActivity.this, "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();

                        }
                        public void onCancel(Platform arg0, int arg1) {
                            Toast.makeText(BaiDuMLocationActivity.this, "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                        }
                    });
// 执行图文分享
                    qqf.share(sp);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                    Toast.makeText(BaiDuMLocationActivity.this, "分享失败", Toast.LENGTH_SHORT).show();

                }
                catch (IOException e) {
                    e.printStackTrace();

                    Toast.makeText(BaiDuMLocationActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void loadIcon() {

        for (int i = 0; i < strLat.size(); i++) {
            loactions[i] = new LocationInfo(strLat.get(i), strLong.get(i), strName.get(i), strLoginId.get(i), strSex.get(i));
        }

        for (int i = 0; i < loactions.length; i++) {

            View v = null;

            if ("00".equals(strSex.get(i))) {
                v = LayoutInflater.from(BaiDuMLocationActivity.this).inflate(R.layout.item_map_user1, null);
            } else {
                v = LayoutInflater.from(BaiDuMLocationActivity.this).inflate(R.layout.item_map_user, null);
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
                mMapView = new MapView(BaiDuMLocationActivity.this,
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

    private void initLocationView(){

        tvNianLing = (TextView) vi4.findViewById(R.id.tv_nianling);
        tvCompany = (TextView) vi4.findViewById(R.id.tv_company);
        tvDistance = (TextView) vi4.findViewById(R.id.tv_distance);
        tv_count_llc = (TextView) vi4.findViewById(R.id.tv_count_llc);
        locationMapView = (TextView) vi4.findViewById(R.id.bmapView2);
        tv_nick = (TextView) vi4.findViewById(R.id.tv_nick);
        tv_time = (TextView) vi4.findViewById(R.id.tv_from);
        tv_video = (TextView) vi4.findViewById(R.id.tv_video);
        tvTitleA = (TextView) vi4.findViewById(R.id.tv_titl);
        iv_avatar = (CircleImageView) vi4.findViewById(R.id.sdv_image);
        card = (LinearLayout) vi4.findViewById(R.id.card);
        rl_geshu = (RelativeLayout) vi4.findViewById(R.id.rl_geshu);
        rl_neirong = (RelativeLayout) vi4.findViewById(R.id.rl_neirong);
        rl_zhuanfa = (RelativeLayout) vi4.findViewById(R.id.rl_zhuanfa);
        rl_pinglun = (RelativeLayout) vi4.findViewById(R.id.rl_pinglun);
        rl_video = (RelativeLayout) vi4.findViewById(R.id.rl_video);
        videoPlayer = (JCVideoPlayerStandard) vi4.findViewById(R.id.videoplayer);
        tv_count_pl = (TextView) vi4.findViewById(R.id.tv_count_pl);
        image_1 = (ImageView) vi4.findViewById(R.id.image_1);
        image_2 = (ImageView) vi4.findViewById(R.id.image_2);
        image_3 = (ImageView) vi4.findViewById(R.id.image_3);
        image_4 = (ImageView) vi4.findViewById(R.id.image_4);
        image_5 = (ImageView) vi4.findViewById(R.id.image_5);
        image_6 = (ImageView) vi4.findViewById(R.id.image_6);
        image_7 = (ImageView) vi4.findViewById(R.id.image_7);
        image_8 = (ImageView) vi4.findViewById(R.id.image_8);
        iv_ditu = (ImageView) vi4.findViewById(R.id.iv_ditu);
        iv_bz_type = (ImageView) vi4.findViewById(R.id.iv_bz_type);
        ll_one = (LinearLayout) vi4.findViewById(R.id.ll_one);
        ll_two = (LinearLayout) vi4.findViewById(R.id.ll_two);
        tv_content = (TextView) vi4.findViewById(R.id.tv_content);
        tv_count_zhf = (TextView) vi4.findViewById(R.id.tv_count_zhf);
        tv_geshu = (TextView) vi4.findViewById(R.id.tv_geshu);
        tv_location = (TextView) vi4.findViewById(R.id.tv_location);

        tv_demandType = (TextView) vi4.findViewById(R.id.tv_demandType);

        rl_collection = (RelativeLayout) vi4.findViewById(R.id.rl_collection);
        tv_collectioncount = (TextView) vi4.findViewById(R.id.tv_collectioncount);
        tv_collectiontitle = (TextView) vi4.findViewById(R.id.tv_collectiontitle);

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
        Bitmap bitmap = BitmapUtils.readBitMap(this,R.drawable.map_icon_24);
        iv1.setImageBitmap(bitmap);
        Bitmap bitmap2 = BitmapUtils.readBitMap(this,R.drawable.map_icon_dayin);
        iv2.setImageBitmap(bitmap2);
        Bitmap bitmap3 = BitmapUtils.readBitMap(this,R.drawable.map_icon_danche);
        iv3.setImageBitmap(bitmap3);
        Bitmap bitmap4 = BitmapUtils.readBitMap(this,R.drawable.map_icon_dianche);
        iv4.setImageBitmap(bitmap4);
        Bitmap bitmap5 = BitmapUtils.readBitMap(this,R.drawable.map_icon_hbxiang);
        iv5.setImageBitmap(bitmap4);
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sousuo("紧急");
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sousuo("打印");
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sousuo("单车");
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sousuo("电动车");
            }
        });
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_chatline.setVisibility(View.GONE);
                sousuo("电动车");
            }
        });
        tv_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!DemoHelper.getInstance().isLoggedIn(BaiDuMLocationActivity.this)){

                    Toast.makeText(BaiDuMLocationActivity.this,"请您先登录！",Toast.LENGTH_SHORT).show();

                }else {

                    ll_chatline.setVisibility(View.GONE);
                    //现在改为我去过的  跳转到跟收藏一个界面好了  用identify等于1 标示一下是我去过的

                    startActivityForResult(new Intent(BaiDuMLocationActivity.this, CollectionDynamicActivity.class).putExtra("identify","1"),0);

                }

                //startActivityForResult(new Intent(BaiDuMLocationActivity.this, SouSuoActivity.class), 1);
            }
        });

        iv_baoxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_chatline.setVisibility(View.GONE);
                isBaoZclicked = true;
                oa2.clear();
                baoZInfos.clear();
                bzPresenter.loadbaozList();
            }
        });

        iv_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_chatline.setVisibility(View.GONE);
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

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                ll_chatline.setVisibility(View.GONE);
                if (isBaoZclicked && DemoHelper.getInstance().isLoggedIn(BaiDuMLocationActivity.this)) {

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
                                    startActivityForResult(new Intent(BaiDuMLocationActivity.this, DynaDetaActivity.class).putExtra("dynamicSeq", dynamicSeq)
                                            .putExtra("createTime", createTime).putExtra("biaoshi", "01").putExtra("dType", "02").putExtra("sID", DemoHelper.getInstance().getCurrentUsernName()), 2);
                                }
                            });
                            btnXq2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mBaiduMap.hideInfoWindow();
                                    if (ll_gongxiang!=null&&(ll_gongxiang.getVisibility()==View.GONE||ll_gongxiang.getVisibility()==View.INVISIBLE)){
                                        ll_gongxiang.setVisibility(View.VISIBLE);
                                        ll_gongxiang.setAnimation(AnimationUtil.moveToViewLeftLocation());
                                    }
                                }
                            });
                        }
                    }
                } else {
                    if (!isClickLeftType.equals("0")) {

                    }else {
                        btn_previous.setVisibility(View.VISIBLE);
                        btn_next.setVisibility(View.VISIBLE);
                    }

                    for (int i = 0; i < oa.size(); i++) {
                        if (marker.equals(oa.get(i))) {

                            oa.get(i).setToTop();

                            if (!isClickLeftType.equals("0")){
                                //店或者玩的弹出

                                setView2(locationDatas.get(i),strLat.get(i), strLong.get(i));

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
                                mInfoWindow = new InfoWindow(vi4, ll1, 155);
                                mBaiduMap.showInfoWindow(mInfoWindow);

                                final String dynamicSeq = locationDatas.get(i).getString("dynamicSeq");
                                final String createTime = locationDatas.get(i).getString("createTime");
                                final String uLoginId = locationDatas.get(i).getString("uLoginId");

                                rl_neirong.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        startActivityForResult(new Intent(BaiDuMLocationActivity.this, DynaDetaActivity.class).putExtra("dynamicSeq", dynamicSeq)
                                                .putExtra("createTime", createTime).putExtra("biaoshi", "01").putExtra("dType", "02").putExtra("sID", uLoginId), 90);

                                    }
                                });

                            }else {

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

                                MySingleton.getInstance(BaiDuMLocationActivity.this).addToRequestQueue(request);
                                final int[] j = {finalI};
                                btn_previous.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (j[0] >= 1) {
                                            j[0] = j[0] - 1;
                                            if (strLoginId.size() == 0) {
                                                ToastUtils.showNOrmalToast(BaiDuMLocationActivity.this.getApplicationContext(),"获取数据失败");
                                                return;
                                            }
                                            String id = strLoginId.get(j[0]);
                                            String url = FXConstant.URL_Get_UserInfo + id;
                                            final int finalI1 = j[0];
                                            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    setView(s, strLat.get(finalI1), strLong.get(finalI1));
                                                    oa.get(j[0]).setToTop();
                                                    LatLng ll = new LatLng(Double.parseDouble(strLat.get(finalI1)), Double.parseDouble(strLong.get(finalI1)));
                                                    CoordinateConverter converter = new CoordinateConverter();
                                                    converter.coord(ll);
                                                    converter.from(CoordinateConverter.CoordType.COMMON);
                                                    LatLng convertLatLng = converter.convert();
                                                    double lat = convertLatLng.latitude + 0.001;
                                                    double lng = convertLatLng.longitude;
                                                    LatLng ll2 = new LatLng(lat, lng);
                                                    MapStatus mMapStatus = new MapStatus.Builder()
                                                            .target(ll2)
                                                            .zoom(18)
                                                            .build();
                                                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                                                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                                    //改变地图状态
                                                    mBaiduMap.setMapStatus(mMapStatusUpdate);
                                                    mInfoWindow = new InfoWindow(vi2, convertLatLng, -100);
                                                    mBaiduMap.showInfoWindow(mInfoWindow);
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    //callback.onError("获取数据失败");
                                                }
                                            });
                                            MySingleton.getInstance(BaiDuMLocationActivity.this).addToRequestQueue(request);
                                        } else {
                                            Toast.makeText(BaiDuMLocationActivity.this, "已经是第一个了！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                btn_next.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (j[0] < oa.size() - 1) {
                                            j[0] = j[0] + 1;
                                            String id = strLoginId.get(j[0]);
                                            String url = FXConstant.URL_Get_UserInfo + id;
                                            final int finalI1 = j[0];
                                            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    oa.get(j[0]).setToTop();
                                                    setView(s, strLat.get(finalI1), strLong.get(finalI1));
                                                    LatLng ll = new LatLng(Double.parseDouble(strLat.get(finalI1)), Double.parseDouble(strLong.get(finalI1)));
                                                    CoordinateConverter converter = new CoordinateConverter();
                                                    converter.coord(ll);
                                                    converter.from(CoordinateConverter.CoordType.COMMON);
                                                    LatLng convertLatLng = converter.convert();
                                                    double lat = convertLatLng.latitude + 0.001;
                                                    double lng = convertLatLng.longitude;
                                                    LatLng ll2 = new LatLng(lat, lng);
                                                    MapStatus mMapStatus = new MapStatus.Builder()
                                                            .target(ll2)
                                                            .zoom(18)
                                                            .build();
                                                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                                                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                                    //改变地图状态
                                                    mBaiduMap.setMapStatus(mMapStatusUpdate);
                                                    mInfoWindow = new InfoWindow(vi2, convertLatLng, -100);
                                                    mBaiduMap.showInfoWindow(mInfoWindow);
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    //callback.onError("获取数据失败");
                                                }
                                            });
                                            MySingleton.getInstance(BaiDuMLocationActivity.this).addToRequestQueue(request);
                                        } else {
                                            Toast.makeText(BaiDuMLocationActivity.this, "已经是最后一个了！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }

                        }
                    }
                }
                if (ll_gongxiang!=null&&ll_gongxiang.getVisibility()==View.VISIBLE) {
                    ll_gongxiang.setVisibility(View.GONE);
                    ll_gongxiang.setAnimation(AnimationUtil.moveToViewLeft());
                }
                return true;
            }
        });
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


    private void ShowDynamicUserInfo(final Marker marker){

        btn_previous.setVisibility(View.VISIBLE);
        btn_next.setVisibility(View.VISIBLE);
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

                MySingleton.getInstance(BaiDuMLocationActivity.this).addToRequestQueue(request);
                final int[] j = {finalI};
                btn_previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (j[0] >= 1) {
                            j[0] = j[0] - 1;
                            if (strLoginId.size() == 0) {
                                ToastUtils.showNOrmalToast(BaiDuMLocationActivity.this.getApplicationContext(),"获取数据失败");
                                return;
                            }
                            String id = strLoginId.get(j[0]);
                            String url = FXConstant.URL_Get_UserInfo + id;
                            final int finalI1 = j[0];
                            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    setView(s, strLat.get(finalI1), strLong.get(finalI1));
                                    oa.get(j[0]).setToTop();
                                    LatLng ll = new LatLng(Double.parseDouble(strLat.get(finalI1)), Double.parseDouble(strLong.get(finalI1)));
                                    CoordinateConverter converter = new CoordinateConverter();
                                    converter.coord(ll);
                                    converter.from(CoordinateConverter.CoordType.COMMON);
                                    LatLng convertLatLng = converter.convert();
                                    double lat = convertLatLng.latitude + 0.001;
                                    double lng = convertLatLng.longitude;
                                    LatLng ll2 = new LatLng(lat, lng);
                                    MapStatus mMapStatus = new MapStatus.Builder()
                                            .target(ll2)
                                            .zoom(18)
                                            .build();
                                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                    //改变地图状态
                                    mBaiduMap.setMapStatus(mMapStatusUpdate);
                                    mInfoWindow = new InfoWindow(vi2, convertLatLng, -100);
                                    mBaiduMap.showInfoWindow(mInfoWindow);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    //callback.onError("获取数据失败");
                                }
                            });
                            MySingleton.getInstance(BaiDuMLocationActivity.this).addToRequestQueue(request);
                        } else {
                            Toast.makeText(BaiDuMLocationActivity.this, "已经是第一个了！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (j[0] < oa.size() - 1) {
                            j[0] = j[0] + 1;
                            String id = strLoginId.get(j[0]);
                            String url = FXConstant.URL_Get_UserInfo + id;
                            final int finalI1 = j[0];
                            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    oa.get(j[0]).setToTop();
                                    setView(s, strLat.get(finalI1), strLong.get(finalI1));
                                    LatLng ll = new LatLng(Double.parseDouble(strLat.get(finalI1)), Double.parseDouble(strLong.get(finalI1)));
                                    CoordinateConverter converter = new CoordinateConverter();
                                    converter.coord(ll);
                                    converter.from(CoordinateConverter.CoordType.COMMON);
                                    LatLng convertLatLng = converter.convert();
                                    double lat = convertLatLng.latitude + 0.001;
                                    double lng = convertLatLng.longitude;
                                    LatLng ll2 = new LatLng(lat, lng);
                                    MapStatus mMapStatus = new MapStatus.Builder()
                                            .target(ll2)
                                            .zoom(18)
                                            .build();
                                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                    //改变地图状态
                                    mBaiduMap.setMapStatus(mMapStatusUpdate);
                                    mInfoWindow = new InfoWindow(vi2, convertLatLng, -100);
                                    mBaiduMap.showInfoWindow(mInfoWindow);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    //callback.onError("获取数据失败");
                                }
                            });
                            MySingleton.getInstance(BaiDuMLocationActivity.this).addToRequestQueue(request);
                        } else {
                            Toast.makeText(BaiDuMLocationActivity.this, "已经是最后一个了！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }


    }

    //查询店或者玩的数据
    private void selectLocationDynamic(){

        String url = FXConstant.URL_SELECTDYNAMICBYIDENTIFY;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);

                JSONArray users_temp = object.getJSONArray("clist");

                if (users_temp != null && users_temp.size()>0) {

                    for (int i = 0; i < users_temp.size(); i++) {

                        com.alibaba.fastjson.JSONObject json = users_temp.getJSONObject(i);

                        locationDatas.add(json);

                    }

                }


                if (locationDatas.size() > 0) {

                    if (mBaiduMap==null && mMapView != null){
                        mBaiduMap = mMapView.getMap();
                    }

                    MyRunnable runnable = new MyRunnable(BaiDuMLocationActivity.this);

                    new Thread(runnable).start();

                } else {
                   // Toast.makeText(getApplicationContext(), "没有更多的数据了...", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(BaiDuMLocationActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("lat",mLat1+"");
                param.put("lng",mLon1+"");
                param.put("demandType",isClickLeftType);

                return param;
            }
        };

        MySingleton.getInstance(BaiDuMLocationActivity.this).addToRequestQueue(request);

    }


    private void setView2(final com.alibaba.fastjson.JSONObject json, final String strLat, final String strLon) {

        String views = json.getString("views");
        if (views==null||"".equals(views)){
            views = "0";
        }
        if (views!=null&&Integer.valueOf(views)>999){
            views = "999+";
        }
        tv_count_llc.setText(views);
        final String redImage = json.getString("redImage");
        String redTime = json.getString("redTime");
        final String gameRed = json.getString("gameRed");
        String redSum = json.getString("redSum");
        final String userID = json.getString("uName");
        final String content = json.getString("content");
        String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
        final String sID = json.getString("uLoginId");
        final String dynamicSeq = json.getString("dynamicSeq");
        String location1 = TextUtils.isEmpty(json.getString("location")) ? "" : json.getString("location");
        final String video = json.getString("video");
        final String videoPictures = json.getString("videoPictures");

        String collection = json.getString("collectionCount");

        if (Double.valueOf(collection)>999){

            tv_collectioncount.setText("999+");

        }else {

            tv_collectioncount.setText(collection);

        }

        String lat = "",lng="",address="";
        if (redSum==null||"".equals(redSum)||redSum.equalsIgnoreCase("null")){
            redSum = "0";
        }
        if (!"".equals(location1)){
            lat = location1.split("\\|")[0];
            lng = location1.split("\\|")[1];
        }
        address = json.getString("price");
        // String token = json.getString("token");
        final String rel_time = json.getString("createTime");
        String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
        String countPinglun = json.getString("resv7");
        if (countPinglun==null||"".equals(countPinglun)){
            countPinglun = "0";
        }
        if (countPinglun!=null&&Integer.valueOf(countPinglun)>999){
            countPinglun = "999+";
        }
        tv_count_pl.setText(countPinglun);
        String forwardTimes = json.getString("forwardTimes");
        if (forwardTimes==null||"".equals(forwardTimes)){
            forwardTimes = "0";
        }
        if (forwardTimes!=null&&Integer.valueOf(forwardTimes)>999){
            forwardTimes = "999+";
        }
        tv_count_zhf.setText(forwardTimes);
        String firstImage = "";
        if (!avatar.equals("")) {
            firstImage = avatar.split("\\|")[0];
        }
        final String createTime = json.getString("createTime");
        String locaImage = json.getString("locaImage");
        if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
            iv_ditu.setVisibility(View.VISIBLE);
            iv_ditu.setImageResource(R.drawable.image_baozang);
            locationMapView.setVisibility(View.GONE);
            final String biaoshi;
            if ("0".equals(redSum)) {
                rl_geshu.setVisibility(View.GONE);
                iv_bz_type.setVisibility(View.VISIBLE);
                iv_bz_type.setImageResource(R.drawable.baoxiang_open);
                biaoshi = "open";
            } else {
                if ("no".equals(redTime)) {
                    iv_bz_type.setVisibility(View.GONE);
                    rl_geshu.setVisibility(View.VISIBLE);
                    tv_geshu.setText(redSum);
                    biaoshi = "close";
                } else {
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    String nowTime = dateFormat.format(date);
                    if (Long.parseLong(nowTime) < Long.parseLong(redTime)) {
                        iv_bz_type.setVisibility(View.GONE);
                        rl_geshu.setVisibility(View.VISIBLE);
                        tv_geshu.setText(redSum);
                        biaoshi = "close";
                    } else {
                        iv_bz_type.setVisibility(View.GONE);
                        rl_geshu.setVisibility(View.GONE);
                        biaoshi = "none";
                    }
                }
            }
            final String finalLat = lat;
            final String finalLng = lng;
            final String finalRedSum = redSum;

        } else {
            iv_bz_type.setVisibility(View.GONE);
            rl_geshu.setVisibility(View.GONE);
            if (locaImage != null && !"".equals(locaImage) && !locaImage.equalsIgnoreCase("null")) {
                iv_ditu.setVisibility(View.VISIBLE);
                locationMapView.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + locaImage, iv_ditu);
            } else {
                locationMapView.setText(address);
                locationMapView.setVisibility(View.VISIBLE);
                iv_ditu.setVisibility(View.GONE);
            }
            if (!"".equals(lat) && !"".equals(lng)) {
                final String finalLat = lat;
                final String finalLng = lng;

            }
        }
        if (!avatar.equals("")) {
            String[] orderProjectArray = avatar.split("\\|");
            avatar = orderProjectArray[0];
        }
        String sex = TextUtils.isEmpty(json.getString("uSex")) ? "01" : json.getString("uSex");
        String nianLing = TextUtils.isEmpty(json.getString("uAge")) ? "27" : json.getString("uAge");
        String company = TextUtils.isEmpty(json.getString("uCompany")) ? "暂未加入企业" : json.getString("uCompany");
        String uNation = json.getString("uNation");
        String resv5 = json.getString("resv5");
        String resv6 = json.getString("resv6");
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
        tvCompany.setText("("+company+")");
        tvNianLing.setText(nianLing);
        if ("00".equals(sex)){
            tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
            tvNianLing.setBackgroundColor(Color.rgb(234,121,219));
        }else {
            tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
            tvNianLing.setBackgroundResource(R.color.accent_blue);
        }
        if (!("".equals(avatar))) {
            iv_avatar.setVisibility(View.VISIBLE);
            tvTitleA.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+avatar,iv_avatar, DemoApplication.mOptions);
        } else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
        }
        tv_nick.setText(userID);
        final String shareRed = json.getString("shareRed");
        final String friendsNumber = json.getString("friendsNumber");
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            tv_nick.setTextColor(Color.RED);
        }else {
            tv_nick.setTextColor(Color.rgb(87,107,149));
        }

        String resv1 = TextUtils.isEmpty(json.getString("resv1")) ? "" : json.getString("resv1");
        String resv2 = TextUtils.isEmpty(json.getString("resv2")) ? "" : json.getString("resv2");
        double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
        double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());
        if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
            final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
            LatLng ll = new LatLng(latitude1,longitude1);
            double distance = DistanceUtil.getDistance(ll, ll1);
            double dou = distance / 1000;
            String str = String.format("%.2f",dou);//format 返回的是字符串
            if (str!=null&&dou>=10000){
                tvDistance.setText("隐藏");
            }else {
                tvDistance.setText(str + "km");
            }
        } else {
            tvDistance.setText("3km之内");
        }
        // 设置文章中的图片
        image_1.setVisibility(View.GONE);
        image_2.setVisibility(View.GONE);
        image_3.setVisibility(View.GONE);
        image_4.setVisibility(View.GONE);
        image_5.setVisibility(View.GONE);
        image_6.setVisibility(View.GONE);
        image_7.setVisibility(View.GONE);
        image_8.setVisibility(View.GONE);
        View v2 = null;
        if (video!=null&&videoPictures!=null){
            rl_video.setVisibility(View.VISIBLE);
            boolean setUp = videoPlayer.setUp(FXConstant.URL_VIDEO+video, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    "");
            v2 = rl_video;
            if (setUp) {
                Glide.with(BaiDuMLocationActivity.this).load(FXConstant.URL_VIDEO+videoPictures)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .error(R.drawable.default_error)
                        .crossFade().into(videoPlayer.thumbImageView);
            }else {
                Toast.makeText(BaiDuMLocationActivity.this,"视频播放失败",Toast.LENGTH_SHORT).show();
            }
            tv_video.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        Log.e("socialmain,", "touch");
                       // updateLiulancishu(rel_time, dynamicSeq);
                        //updateDeLiulancishu(rel_time, sID);
                    }
                    return false;
                }
            });

        }else {
            rl_video.setVisibility(View.GONE);
            if (!imageStr.equals("")) {
                String[] images = imageStr.split("\\|");
                int imNumb = images.length;
                image_1.setVisibility(View.VISIBLE);
                v2 = image_1;
                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[0],image_1, DemoApplication.mOptions2);
               // ((ViewHolderTwo) holder).image_1.setOnClickListener(new ImageListener(images, 0,rel_time, dynamicSeq,sID));
                if (imNumb > 1) {
                    image_2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[1],image_2, DemoApplication.mOptions2);
                   // ((ViewHolderTwo) holder).image_2.setOnClickListener(new ImageListener(images, 1,rel_time, dynamicSeq,sID));
                    if (imNumb > 2) {
                        image_3.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[2], image_3, DemoApplication.mOptions2);
                       // ((ViewHolderTwo) holder).image_3.setOnClickListener(new ImageListener(images, 2, rel_time, dynamicSeq,sID));
                        if (imNumb > 3) {
                            image_4.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[3], image_4, DemoApplication.mOptions2);
                            //((ViewHolderTwo) holder).image_4.setOnClickListener(new ImageListener(images, 3, rel_time, dynamicSeq,sID));
                            if (imNumb > 4) {
                                image_5.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[4], image_5, DemoApplication.mOptions2);
                                //((ViewHolderTwo) holder).image_5.setOnClickListener(new ImageListener(images, 4, rel_time, dynamicSeq,sID));
                                if (imNumb > 5) {
                                    image_6.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], image_6, DemoApplication.mOptions2);
                                   // ((ViewHolderTwo) holder).image_6.setOnClickListener(new ImageListener(images, 5, rel_time, dynamicSeq,sID));
                                    if (imNumb > 6) {
                                        image_7.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], image_7, DemoApplication.mOptions2);
                                       // ((ViewHolderTwo) holder).image_7.setOnClickListener(new ImageListener(images, 6, rel_time, dynamicSeq,sID));
                                        if (imNumb > 7) {
                                            image_8.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], image_8, DemoApplication.mOptions2);
                                           // image_8.setOnClickListener(new ImageListener(images, 7, rel_time, dynamicSeq,sID));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 显示文章内容
        // .setText(content);
        final String fromUId = sID;
        final String finalFirstImage = firstImage;
        final View finalV = v2;

        if (json.getString("demandType").equals("店"))
        {
            tv_demandType.setText("店");
            tv_demandType.setVisibility(View.VISIBLE);
            tv_content.setText("      " + content);
            tv_demandType.setBackgroundResource(R.drawable.btn_corner_weizhired);

        }else if (json.getString("demandType").equals("玩"))
        {
            tv_demandType.setText("玩");
            tv_demandType.setVisibility(View.VISIBLE);
            tv_content.setText("      " + content);
            tv_demandType.setBackgroundResource(R.drawable.btn_corner_weizhigreen);

        }else {

            tv_demandType.setVisibility(View.GONE);
            tv_content.setText(content);
        }

        tv_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    String type;
                    if (json.getString("fromUId") != null && !json.getString("fromUId").equalsIgnoreCase("null")) {
                        type = "02";
                    } else {
                        type = "01";
                    }

                    Intent intent = new Intent(BaiDuMLocationActivity.this, DynaDetaActivity.class);
                    intent.putExtra("sID", sID);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("createTime", rel_time);
                    intent.putExtra("dType", "02");
                    intent.putExtra("type", type);
                    intent.putExtra("type2", "00");
                    startActivityForResult(intent, 0);

                };
                return true;
            }
        });
        String rel_time2 = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
        tv_time.setText(rel_time2);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BaiDuMLocationActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });

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


            if (id.equals(DemoHelper.getInstance().getCurrentUsernName())){

                btnDh.setText("坐标分享");
                btnXq.setText("我的详情");


            }else {

                btnDh.setText("导航到这");
                btnXq.setText("看他详情");
            }

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

                    if (btnDh.getText().toString().trim().equals("坐标分享")){

                        ShareQzone();

                        //分享当前的截图

                       // Toast.makeText(BaiDuMLocationActivity.this,"处理分享逻辑",Toast.LENGTH_SHORT).show();


                    }else {

                        startNavi(Double.valueOf(strLat), Double.valueOf(strLon));

                    }


                    //finish();
                }
            });
            btnXq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaiDuMLocationActivity.this, UserDetailsActivity.class);
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
        if (ll_gongxiang!=null&&(ll_gongxiang.getVisibility()==View.GONE||ll_gongxiang.getVisibility()==View.INVISIBLE)){
            ll_gongxiang.setVisibility(View.VISIBLE);
            ll_gongxiang.setAnimation(AnimationUtil.moveToViewLeftLocation());
        }
        btn_previous.setVisibility(View.INVISIBLE);
        btn_next.setVisibility(View.INVISIBLE);
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
            Toast.makeText(BaiDuMLocationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
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
                if (ll_gongxiang!=null&&(ll_gongxiang.getVisibility()==View.GONE||ll_gongxiang.getVisibility()==View.INVISIBLE)){
                    ll_gongxiang.setVisibility(View.VISIBLE);
                    ll_gongxiang.setAnimation(AnimationUtil.moveToViewLeftLocation());
                }
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

                if (!isClickLeftType.equals("0")){

                    selectLocationDynamic();

                }else {
                    findPresenter.loadUserList(myUserId, "1", "1", nowLng + "", nowLat + "", zhuanye, null, null, null, null, name, comName, false, false, false);
                }

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
                    mSearch.geocode(new GeoCodeOption().city(city).address(addr));
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
            BzRunnable runnable = new BzRunnable(BaiDuMLocationActivity.this);
            new Thread(runnable).start();
        } else {
            Toast.makeText(getApplicationContext(), "没有更多的数据了...", Toast.LENGTH_SHORT).show();
        }
    }

    private static class BzRunnable implements Runnable {
        WeakReference<BaiDuMLocationActivity> mactivity;

        public BzRunnable(BaiDuMLocationActivity activity) {
            mactivity = new WeakReference<BaiDuMLocationActivity>(activity);
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

        WeakReference<BaiDuMLocationActivity> mactivity;

        public MyRunnable(BaiDuMLocationActivity activity) {
            mactivity = new WeakReference<BaiDuMLocationActivity>(activity);
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

            if (!mactivity.get().isClickLeftType.equals("0")){
                //搜素店或者玩的动态

                if (mactivity.get().locationDatas!=null) {
                    for (int i = 0; i < mactivity.get().locationDatas.size(); i++) {
                        com.alibaba.fastjson.JSONObject object = mactivity.get().locationDatas.get(i);
                        id = object.getString("uLoginId");
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
                            name = object.getString("uName");
                            sex = object.getString("uSex");
                            if (sex == null){
                                sex = "01";
                            }
                            lng = object.getString("lng");
                            lat = object.getString("lat");
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
                                if (mactivity.get().mBaiduMap == null) {
                                    return;
                                }

                                Marker mark = (Marker) mactivity.get().mBaiduMap.addOverlay(mop);

                                mactivity.get().oa.add(mark);

                                if (i == mactivity.get().locationDatas.size() / 2 && mactivity.get().issousuo) {

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


            }else {

                //原来的加载普通用户

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
                                if (mactivity.get().mBaiduMap == null) {
                                    return;
                                }

                                Marker mark = (Marker) mactivity.get().mBaiduMap.addOverlay(mop);

                                mactivity.get().oa.add(mark);

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

            MyRunnable runnable = new MyRunnable(BaiDuMLocationActivity.this);

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
        if (mSearch != null) {
            mSearch.destroy();
            mSearch = null;
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
