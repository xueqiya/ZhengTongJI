package com.sangu.apptongji.main.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
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
import com.sangu.apptongji.main.alluser.entity.UserAdapter;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuMLocationActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.ShaiXuanActivity;
import com.sangu.apptongji.main.alluser.presenter.IFindPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FindPresenter;
import com.sangu.apptongji.main.alluser.view.IFindView;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/6.
 */

public class PersonFragment extends Fragment implements IFindView ,View.OnClickListener{
    private IFindPresenter findPresenter=null;
    private XRecyclerView mRecyclerView=null;
    private List<UserAll> list=new ArrayList<>();
    private UserAdapter adapter=null;
    private RadioButton rbGs=null;
    private RadioButton rbJy=null;
    private RadioButton rbBt=null;
    private RadioButton rbSs=null;
    View v;
    private String bZJ=null,zhY=null;
    private String lng="116.407170",lat="39.904690",name=null,comName=null;
    private TextureMapView mMapView=null;
    BitmapDescriptor mCurrentMarker=null;
    RelativeLayout stickynavlayout_topview=null,id_stickynavlayout_viewpager=null;
    TextView id_stickynavlayout_indicator=null;

    private BaiduMap mBaiduMap=null;
    private UiSettings mUiSettings=null;
    private int currentPage;
    private boolean isNext;
    private boolean isShaixuan;
    private String hasZhy,hasCom,sex,ageStart,hasBao,hasJy,hasJdl,hasHb,ageEnd,myUserId;
    boolean hasjy = false;
    boolean hasjdl = false;
    boolean hasMore = true;
    private boolean isGsChecked;
    private boolean isJyChecked;
    private boolean isBtChecked;
    private boolean isHbChecked;
    private ImageView iv_new_hongbao;
    private CustomProgressDialog mProgress=null;
    private boolean hasNewRed = false;
    private boolean hasOldRed = false;
    private boolean isVisible = true;
    private boolean isfirst = true;
    private boolean isTwo = false;
    private Button btn_clear;
    private View headView;
    private Dialog mWeiboDialog;

    private String selectType = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_person, container, false);
        headView = inflater.inflate(R.layout.head_recycle, null);
        RequestQueue queue = MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        WeakReference<PersonFragment> reference =  new WeakReference<PersonFragment>(PersonFragment.this);
        mProgress = CustomProgressDialog.createDialog(reference.get().getActivity());
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
     //   mProgress.show();
        isNext = false;
        isShaixuan = false;
        isGsChecked = false;
        isJyChecked = false;
        isBtChecked = false;
        isHbChecked = false;
        btn_clear = (Button) v.findViewById(R.id.btn_clear);
        iv_new_hongbao = (ImageView) v.findViewById(R.id.iv_new_hongbao);
        mRecyclerView = (XRecyclerView) v.findViewById(R.id.id_stickynavlayout_innerscrollview);
        rbGs = (RadioButton) v.findViewById(R.id.radioGS);
        rbJy = (RadioButton) v.findViewById(R.id.radioEP);
        rbBt = (RadioButton) v.findViewById(R.id.radioBT);
        rbSs = (RadioButton) v.findViewById(R.id.radioSS);
        stickynavlayout_topview = (RelativeLayout) v.findViewById(R.id.id_stickynavlayout_topview);
        id_stickynavlayout_viewpager = (RelativeLayout) v.findViewById(R.id.id_stickynavlayout_viewpager);
        id_stickynavlayout_indicator = (TextView) v.findViewById(R.id.id_stickynavlayout_indicator);
        rbGs.setEnabled(false);
        rbJy.setEnabled(false);
        rbSs.setEnabled(false);
        rbBt.setEnabled(false);
        if (DemoHelper.getInstance().getCurrentUsernName()!=null){
            myUserId = DemoHelper.getInstance().getCurrentUsernName();
        }else {
            myUserId = "";
        }
        return v;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (getActivity()!=null&&!isVisible) {
            if (list != null) {
                list.clear();
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WeakReference<PersonFragment> reference =  new WeakReference<PersonFragment>(PersonFragment.this);
        findPresenter = new FindPresenter(getActivity(),reference.get());
        currentPage = 1;
        rbGs.setOnClickListener(reference.get());
        rbJy.setOnClickListener(reference.get());
        rbBt.setOnClickListener(reference.get());
        rbSs.setOnClickListener(reference.get());
        lng = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "116.407170" : DemoApplication.getInstance().getCurrentLng();
        lat = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "39.904690" : DemoApplication.getInstance().getCurrentLat();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity(), R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.addHeaderView(headView);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                isNext = false;

                if (selectType.equals("0")){

                    if (isShaixuan) {
                        findPresenter.loadUserList(myUserId,"1","2",lng,lat,hasZhy,hasBao,sex,ageStart,ageEnd,name,hasCom,hasjdl,hasjy,isHbChecked);
                    }else {
                        findPresenter.loadUserList(myUserId,"1","1",lng,lat,zhY,bZJ,sex,ageStart,ageEnd,name,comName,isGsChecked,isJyChecked,isHbChecked);
                    }

                }else {

                    SelectProhibitList("5");

                }


            }

            @Override
            public void onLoadMore() {
                currentPage = currentPage + 1;
                isNext = true;

                if (selectType.equals("0")) {

                    if (isShaixuan) {
                        findPresenter.loadUserList(myUserId,currentPage+"","2",lng,lat,hasZhy,hasBao,sex,ageStart,ageEnd,name,hasCom,hasjdl,hasjy,isHbChecked);
                    }else {
                        findPresenter.loadUserList(myUserId,currentPage+"","1",lng,lat,zhY,bZJ,sex,ageStart,ageEnd,name,comName,isGsChecked,isJyChecked,isHbChecked);
                    }

                }else {

                    if (list.size() % 20 == 0){

                        if (selectType.equals("3")){

                            SelectProhibitList("5");

                        }else if (selectType.equals("4")){

                            SelectProhibitList("4");

                        }


                    }else {
                        mRecyclerView.loadMoreComplete();
                    }

                }

            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (btn_clear.getVisibility()==View.VISIBLE) {
                    btn_clear.setVisibility(View.INVISIBLE);
                }
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && firstItemPosition == 1) {
                        if (mMapView!=null) {
                            mMapView.onResume();
                        }
                    }
                    if (!canScrollVertically(recyclerView,1)){
                        if (!hasMore) {
                            Log.e("personfrag,", "到底了");
                            btn_clear.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
        showHeadView();
        showMarker(lat, lng);
        initView();
    }

    /**
     * direction的值为  1 表示是否能向上滚动，false表示已经滚动到底部
     *            值为 -1 表示是否能向下滚动，false表示已经滚动到顶部
     */
    private boolean canScrollVertically(RecyclerView recyclerView, int direction){
        final int offset = recyclerView.computeVerticalScrollOffset();
        final int range = recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent();
        if (range==0)
            return false;
        if (direction < 0){
            return offset > 0;
        }else {
            return offset < range-1;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radioGS:
                currentPage = 1;

                rbGs.setChecked(true);
                rbJy.setChecked(false);
                rbBt.setChecked(false);
                rbSs.setChecked(false);

                clearShaixuan();
             //   isNext = false;

              //  mRecyclerView.refresh(false);

                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");

                selectType = "1"; //禁止登录的

                SelectProhibitList("5");

                break;
            case R.id.radioEP:
                currentPage = 1;

                rbGs.setChecked(false);
                rbJy.setChecked(true);
                rbBt.setChecked(false);
                rbSs.setChecked(false);

//                isJyChecked = !isJyChecked;
//                rbJy.setChecked(isJyChecked);
                clearShaixuan();
                isNext = false;
              //  mRecyclerView.refresh(false);
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");

                selectType = "2"; //禁止提现的

                SelectProhibitList("5");

                break;
            case R.id.radioBT:
                currentPage = 1;

                rbGs.setChecked(false);
                rbJy.setChecked(false);
                rbBt.setChecked(true);
                rbSs.setChecked(false);


//                isBtChecked = !isBtChecked;
//                rbBt.setChecked(isBtChecked);
//                isShaixuan = isBtChecked;
                isNext = false;
//                if (isBtChecked) {
//                    hasBao = "1";
//                }else {
//                    hasBao = null;
//                }
            //    mRecyclerView.refresh(false);
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");

                selectType = "3"; //禁止派单的
                SelectProhibitList("5");

                break;
            case R.id.radioSS:

                currentPage = 1;

                rbGs.setChecked(false);
                rbJy.setChecked(false);
                rbBt.setChecked(false);
                rbSs.setChecked(true);

                isNext = false;

                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");

                selectType = "4"; //禁止发动态

                SelectProhibitList("4");

//                SharedPreferences mSharedPreferences = getActivity().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = mSharedPreferences.edit();
//                editor.putString("time9", getNowTime());
//                editor.commit();
//                if (hasNewRed){
//                    hasNewRed = false;
//                    hasOldRed = true;
//                    iv_new_hongbao.setVisibility(View.INVISIBLE);
//                    findPresenter.loadUserList(myUserId,"1","2",lng,lat,hasZhy,hasBao,sex,ageStart,ageEnd,name,hasCom,hasjdl,hasjy,true);
//                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(getActivity(), "加载中...");
//                }else {
//                    rbSs.setChecked(false);
//                    Intent intent = new Intent(getActivity(), ShaiXuanActivity.class);
//                    intent.putExtra("hasZhy", hasZhy);
//                    intent.putExtra("hasBao", hasBao);
//                    intent.putExtra("sex", sex);
//                    intent.putExtra("ageStart", ageStart);
//                    intent.putExtra("ageEnd", ageEnd);
//                    intent.putExtra("hasJy", hasJy);
//                    intent.putExtra("hasJdl", hasJdl);
//                    intent.putExtra("hasCom", hasCom);
//                    intent.putExtra("hasHb", hasHb);
//                    startActivityForResult(intent, 0);
//                }
                break;
        }
    }

    private void clearShaixuan() {
        isShaixuan = false;
        rbSs.setChecked(false);
        hasZhy = null;
        hasBao = null;
        sex = null;
        ageStart = null;
        ageEnd = null;
        hasJy = null;
        hasJdl = null;
        hasCom = null;
        hasHb = null;
        hasjy = false;
        hasjdl = false;
        isHbChecked = false;
    }

    private void showHeadView() {
        mMapView = (TextureMapView) headView.findViewById(R.id.bmapView1);
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
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(p);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();
        MarkerOptions option = new MarkerOptions().position(convertLatLng).icon(mCurrentMarker);
        mBaiduMap.addOverlay(option);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
        mBaiduMap.animateMapStatus(u);
    }

    private void initView() {
        rbGs.setEnabled(true);
        rbJy.setEnabled(true);
        rbBt.setEnabled(true);
        rbSs.setEnabled(true);
        mRecyclerView.refresh(false);
//        findPresenter.loadUserList(myUserId,currentPage+"","1",lng,lat,zhY,bZJ,sex,ageStart,ageEnd,name,comName,isGsChecked,isJyChecked,isHbChecked);
        adapter = new UserAdapter(list,getActivity(),lat,lng);
        mRecyclerView.setAdapter(adapter);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_clear.setVisibility(View.INVISIBLE);
                clearShaixuan();
                isNext = false;
                mRecyclerView.refresh(false);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView!=null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView!=null) {
            mMapView.onResume();
        }
        if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
            queryUpdatedyna();
        }
        if (isfirst){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mBaiduMap!=null) {
                        mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                            @Override
                            public void onSnapshotReady(Bitmap snapshot) {
                                if (snapshot!=null) {
                                    saveBitmapToSharedPreferences(snapshot);
                                }
                            }
                        });
                    }
                }
            },4000);
            isTwo = true;
        }
    }

    private void queryUpdatedyna() {
        String time;
        if (DemoHelper.getInstance().isLoggedIn(getActivity())){
            time = DemoApplication.getInstance().getCurrentUser().getResv3();
            time = dataOne(time);
        }else {
            time = getNowTime();
        }
        SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
        final String timec9 = sp.getString("time9",time);
        String url = FXConstant.URL_QUERY_DYNATIME;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                String time = object.getString("userShareRed");
                long t1;
                if (time != null) {
                    t1 = Long.parseLong(time);
                    if (timec9!=null&&t1 > Long.parseLong(timec9)) {
                        iv_new_hongbao.setVisibility(View.VISIBLE);
                        hasNewRed = true;
                    } else {
                        iv_new_hongbao.setVisibility(View.INVISIBLE);
                        hasNewRed = false;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }
    private String dataOne(String time) {
        if (time==null||"".equals(time)){
            time = getNowTime2();
        }
        String times = null;
        try {
            times = time.substring(0,4)+time.substring(5,7)+time.substring(8,10)+time.substring(11,13)+time.substring(14,16)+time.substring(17,19);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("sangu_ditucut", getActivity().getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();
        isfirst = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView!=null) {
            mUiSettings=null;
            mMapView.onDestroy();
            mMapView = null;
        }
        if (list!=null){
            list.clear();
            list=null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == 1) {
                lat = data.getStringExtra("lat");
                lng = data.getStringExtra("lng");
                zhY = data.getStringExtra("zhuanye");
                comName = data.getStringExtra("comName");
                isNext = false;
//                showHeadView();
                if (lng!=null&&lng.length()>10){
                    lng = lng.substring(0,10);
                }
                if (lat!=null&&lat.length()>9){
                    lat = lat.substring(0,9);
                }
                showMarker(lat, lng);
                currentPage = 1;
//                findPresenter.loadUserList(myUserId,"1","1",lng,lat,zhY,bZJ,sex,ageStart,ageEnd,name,comName,isGsChecked,isJyChecked,isHbChecked);
                mRecyclerView.refresh(false);
            }
            if (requestCode == 0){
                isShaixuan = true;
                hasZhy = data.hasExtra("hasZhy")?data.getStringExtra("hasZhy"):null;
                hasCom = data.hasExtra("hasCom")?data.getStringExtra("hasCom"):null;
                sex = data.hasExtra("sex")?data.getStringExtra("sex"):null;
                ageStart = data.hasExtra("ageStart")?data.getStringExtra("ageStart"):null;
                ageEnd = data.hasExtra("ageEnd")?data.getStringExtra("ageEnd"):null;
                hasBao = data.hasExtra("hasBao")?data.getStringExtra("hasBao"):null;
                hasJy = data.hasExtra("hasJy")?data.getStringExtra("hasJy"):null;
                hasJdl = data.hasExtra("hasJdl")?data.getStringExtra("hasJdl"):null;
                hasHb = data.hasExtra("hasHb")?data.getStringExtra("hasHb"):null;
                if (hasZhy==null&&hasCom==null&&sex==null&&(ageStart==null||"".equals(ageStart))&&(ageEnd==null||"".equals(ageEnd))&&hasBao==null&&hasJy==null&&hasJdl==null&&hasHb==null){
                    isShaixuan = false;
                    rbSs.setChecked(false);
                }else {
                    rbSs.setChecked(true);
                }
                if (!"1".equals(hasBao)){
                    rbBt.setChecked(false);
                    isBtChecked = false;
                }
                if ("1".equals(hasJy)){
                    hasjy = true;
                }else {
                    hasjy = false;
                }
                if ("1".equals(hasJdl)){
                    hasjdl = true;
                }else {
                    hasjdl = false;
                }
                if ("1".equals(hasHb)){
                    isHbChecked = true;
                }else {
                    isHbChecked = false;
                }
                isNext = false;
                currentPage = 1;
//                if (isShaixuan) {
//                    findPresenter.loadUserList(myUserId,"1","2",lng,lat,hasZhy,hasBao,sex,ageStart,ageEnd,name,hasCom,hasjdl,hasjy,isHbChecked);
//                }else {
//                    findPresenter.loadUserList(myUserId,"1","1",lng,lat,zhY,bZJ,sex,ageStart,ageEnd,name,comName,isGsChecked,isJyChecked,isHbChecked);
//                }
                mRecyclerView.refresh(false);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (isTwo) {
                if (list == null || list.size() == 0) {
                    if (findPresenter != null) {
                        clearShaixuan();
                        currentPage = 1;
                        isNext = false;
                        mRecyclerView.refresh(false);
                    }
                }else if (hasOldRed){
                    hasOldRed = false;
                    currentPage = 1;
                    mRecyclerView.refresh(false);
                }
                if (DemoHelper.getInstance().isLoggedIn(getActivity())) {
                    queryUpdatedyna();
                }
                isVisible = true;
            }
        }else {
            isVisible = false;
        }
    }



    private void SelectProhibitList(final String type){

        String url = "";

        if (selectType.equals("1")){

            url = FXConstant.URL_SELECTFREEZELOGIN;

        }else if (selectType.equals("2")){

            url = FXConstant.URL_SELECTMERACCOUNTPROHIBIT;

        }else {

            url = FXConstant.URL_SELECTPROHIBIT;

        }


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

                try {

                    org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("list");

                    List<UserAll> users = JSONParser.parseUserList(array);

                    if (list==null){
                        list = new ArrayList<>();
                    }

                    if (currentPage == 1){

                        list.clear();
                        mRecyclerView.refreshComplete();

                    }else {

                        mRecyclerView.loadMoreComplete();
                    }

                    list.addAll(users);


                    if (currentPage == 1){

                        adapter = new UserAdapter(list,getActivity(),lat,lng);
                        mRecyclerView.setAdapter(adapter);


                        adapter.setOnItemClickListener(new UserAdapter.MyItemClickListener() {
                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onItemClick(View view, int position,ImageView iv) {
                                UserAll users = list.get(position-2);
                                String hxid = users.getuLoginId();
                                Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                                intent.putExtra("hxid", hxid);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), iv, "share").toBundle());
                                startActivity(intent);
                            }
                        });

                    }else {

                        adapter.notifyDataSetChanged();
                    }


                }catch (JSONException e) {

                    e.printStackTrace();

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

                Map<String, String> param = new  HashMap<String, String>();

                if (selectType.equals("3") || selectType.equals("4")){
                    param.put("type",type);
                }

                param.put("currentPage",currentPage+"");

                return param;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(request);

    }



    public void onRefresh(){
        if (mRecyclerView!=null) {
            clearShaixuan();
            Bundle bundle = getArguments();
            zhY = bundle.getString("zhY");
            name = bundle.getString("name");
            comName = bundle.getString("comName");
            String isclear = bundle.getString("isclear");
            if ("1".equals(isclear)) {
                bZJ = null;
                zhY = null;
                sex = null;
                ageStart = null;
                ageEnd = null;
                name = null;
                comName = null;
            }
            currentPage = 1;
            isNext = false;
            Log.i("per,onrefresh", "刷新");
//        findPresenter.loadUserList(myUserId,"1","1",lng,lat,zhY,bZJ,sex,ageStart,ageEnd,name,comName,isGsChecked,isJyChecked,isHbChecked);
            mRecyclerView.refresh(false);
        }
    }

    @Override
    public void updateUserList(final List<UserAll> users,boolean hasMore) {
        this.hasMore = hasMore;
        if (list==null){
            list = new ArrayList<>();
        }
        if (isNext) {
            list.addAll(users);
            mRecyclerView.loadMoreComplete();
            adapter.notifyDataSetChanged();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        } else {
            this.list = users;
            adapter = new UserAdapter(list,getActivity(),lat,lng);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.refreshComplete();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
            adapter.setOnItemClickListener(new UserAdapter.MyItemClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onItemClick(View view, int position,ImageView iv) {
                    UserAll users = list.get(position-2);
                    String hxid = users.getuLoginId();
                    Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                    intent.putExtra("hxid", hxid);
//                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), iv, "share").toBundle());
                    startActivity(intent);
                }
            });
            if (users.size() > 0) {
                final List<String> strLat = new ArrayList<>();
                final List<String> strLong = new ArrayList<>();
                final List<String> strLoginId = new ArrayList<>();
                final List<String> strName = new ArrayList<>();
                final List<String> strSex = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) {
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
                mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        clearShaixuan();
                        Intent intent = new Intent(getActivity(), BaiDuMLocationActivity.class);
                        intent.putExtra("lat", (Serializable) strLat);
                        intent.putExtra("lng", (Serializable) strLong);
                        intent.putExtra("loginId", (Serializable) strLoginId);
                        intent.putExtra("name", (Serializable) strName);
                        intent.putExtra("sex", (Serializable) strSex);
                        intent.putExtra("mlat", lat);
                        intent.putExtra("mlon", lng);
                        startActivityForResult(intent, 1);
                    }
                    @Override
                    public boolean onMapPoiClick(MapPoi mapPoi) {
                        return false;
                    }
                });
            }
            if (mMapView!=null) {
                mMapView.onResume();
            }
        }
        if (mProgress!=null&&mProgress.isShowing()&&getActivity()!=null&&!getActivity().isFinishing()){
            mProgress.dismiss();
        }

        WeiboDialogUtils.closeDialog(mWeiboDialog);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
        if (getActivity()!=null&&mProgress!=null){
            if (mProgress.isShowing()){
                mProgress.dismiss();
            }
        }
    }

    @Override
    public void showError(String msg) {
        if (mRecyclerView!=null){
            if (currentPage==1){
                mRecyclerView.refreshComplete();
            }else {
                mRecyclerView.loadMoreComplete();
            }
        }
        if (mProgress!=null){
            if (mProgress.isShowing()){
                mProgress.dismiss();
            }
        }
        if (getActivity()!=null) {
            Toast.makeText(getActivity(), "网络连接中断", Toast.LENGTH_SHORT).show();
        }
        if (msg!=null) {
            Log.e("findperson", msg);
        }
    }
}
