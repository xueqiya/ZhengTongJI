package com.sangu.apptongji.main.moments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanxin.easeui.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.DynamicLinkDetailActivity;
import com.sangu.apptongji.main.activity.DynamicRecommendActivity;
import com.sangu.apptongji.main.activity.HbHuoQuActivity;
import com.sangu.apptongji.main.activity.LoginActivity;
import com.sangu.apptongji.main.activity.MessageOrderIntroduceActivity;
import com.sangu.apptongji.main.activity.SoftAgreementActivity;
import com.sangu.apptongji.main.activity.SoftUserAgreementActivity;
import com.sangu.apptongji.main.activity.TopVipActivity;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.adapter.DynamicCommentAdapter;
import com.sangu.apptongji.main.adapter.JiLuListAdapter;
import com.sangu.apptongji.main.adapter.PingLunAdapter;
import com.sangu.apptongji.main.adapter.QdBaojiaAdapter;
import com.sangu.apptongji.main.address.AddressListActivity;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuBzLocationActivity;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuFLocationActivity;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuYuanGongLocationActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.BZJJNActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.BZJZJActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.NewsOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFiveActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFourActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailThreeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailTwoActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.XuqiuListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.ZhFaActivity;
import com.sangu.apptongji.main.model.DynamicRecommend;
import com.sangu.apptongji.main.qiye.QiYeDetailsActivity;
import com.sangu.apptongji.main.utils.BitmapUtils;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.utils.SoundPlayUtils;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.main.widget.ExpandableTextView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.sangu.apptongji.widget.CircleImageView;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2016-11-21.
 */

public class DynaDetaActivity extends BaseActivity {
    private String dynamicViews="0",pushType=null,dType=null,type=null,sID=null,data,profession,firstImage=null,dynamicSeq=null,createTime=null,loginId=null,sum=null,fromId,currentFloorPrice="0",currentPostion;
    private RelativeLayout rl_title=null,rl_zhuanfa=null,rl_pinglun=null,rl_huikui1=null,rl_huikui2=null,
            re_edittext=null,rl_neirong=null,rl_geshu,rl_liulancishu;
    private TextView tv_nick=null,tv_time=null,tvTitleA=null,tv_count_llc=null,tv_content2,tv_first_nick=null,
            tvfirst_TitleA=null,tv_delete=null,tvDistance=null;
    private ExpandableTextView tv_zhf_content=null,tv_content=null;
    private TextView tv_marginLabel,tv_vipLabel,tv_messageLabel;
    private TextView tv_content5=null;
    private ImageView image_1=null,image_2=null,image_3=null,image_4=null,image_5=null,image_6=null,image_7=null,image_8=null;
    private CircleImageView iv_first_avatar=null,iv_avatar=null;
    private TextView tvNianLing,tv_geshu,tv_chujia,tv_demandType;
    private TextView tvCompany;
    EditText et_comment = null;
    private Button btn_xiadan=null,btn_send=null;
    private TextureMapView mMapView=null;
    private ImageView iv_ditu,iv_bz_type,iv_pre_click,image_shareRed;
    private RelativeLayout rl_video;
    private JCVideoPlayerStandard videoPlayer;
    private ScrollView scr_all;

    private int loadType=1,currentPage=1;
    ListView lv_pinglun=null;
    List<JSONObject> datas=new ArrayList<>();
    PingLunAdapter adapter=null;
    QdBaojiaAdapter adapter2=null;
    JiLuListAdapter adapter3=null;
    DynamicCommentAdapter adapter4 = null;
    private CustomProgressDialog mProgress=null;
    private LinearLayout ll_huikui;
    private TextView tv_shpjg=null,tv_shpyj=null,tv_huikui_zonge=null,tv_huikui_yue=null,tv_huikui_zhaunfa=null,tv_liulan_cishu=null,
            tv_count_zhf=null,tv_count_pl=null,tv_xiaoliang=null,tv_location=null,tv_title_pl,tv_title_llcsh,tv_title_zhf;
    private String type2,myuserID,myNick,countPinglun,redImage,firstid;
    private boolean isNext = false;
    View v2;
    private String responseTime;
    private String resv1;
    private String resv2;
    private String firstDistance;
    private String isHaveMargin="0";
    private int isVip = 0;
    private String vipLevel;
    private TextView tv_commentTitle,tv_commentCount;
    private LinearLayout re_edittext2,rl_type;
    private RelativeLayout rl_send;
    private TextView tv_commentType1,tv_commentType2,tv_commentType3,tv_commentType4;
    private String tagId="",commentType="no",isReply="no",currentContent="";
    private String isAdvert="0",advertImageUrl="";
    private TextView weizhiDemandType,weizhiContent;
    private String orderBodyTask;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        GetNoticeInfo();
        WeakReference<DynaDetaActivity> reference =  new WeakReference<DynaDetaActivity>(DynaDetaActivity.this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        mProgress.show();
        createTime = this.getIntent().getStringExtra("createTime");
        dynamicSeq = this.getIntent().getStringExtra("dynamicSeq");
        sID = this.getIntent().getStringExtra("sID");
        dType = this.getIntent().getStringExtra("dType");
        type = this.getIntent().getStringExtra("type");
        type2 = this.getIntent().getStringExtra("type2");
        pushType = this.getIntent().getStringExtra("pushType");
        profession = this.getIntent().getStringExtra("profession");
        tagId = sID;
        Log.d("chen", "动态详情" + createTime + "\\n" + dynamicSeq
                + "\\n" + sID
                + "\\n" + dType
                + "\\n" + type
                + "\\n" + type2
                + "\\n" + profession);
        //动态详情20180307151313\n2018030715131310000001044\n10000001044\n05\n01\n00\n乌拉啦啦啦
        myuserID = DemoHelper.getInstance().getCurrentUsernName();
        myNick = DemoApplication.getInstance().getCurrentUser().getName();
        if (createTime!=null){
            Log.e("dynadeac,cr",createTime);
        }
        if (dynamicSeq!=null){
            Log.e("dynadeac,dy",dynamicSeq);
        }
        getDynaDetail();
        if (DemoHelper.getInstance().isLoggedIn(this)) {
            deletePush();
        }

    }

    private void deletePush() {
        String url = FXConstant.URL_DELETE_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());

                if (pushType != null){

                    param.put("type", pushType);

                }else {

                    if ("05".equals(dType)){
                        param.put("type", "10");
                    }else {
                        param.put("type", "09");
                    }

                }

                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void getDynaDetail() {
        String url = FXConstant.URL_PUBLISHDETAIL_QUERY;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                JSONArray array = object.getJSONArray("clist");
                if (array==null||array.size()==0){
                    ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接错误...");
                    return;
                }
                JSONObject json = array.getJSONObject(0);

                data = json.toString();
                if (json.getString("fromUId")!=null){
                    type = "02";
                    firstid = "01";
                }else {
                    type = "01";
                    firstid = "00";
                }
                dType = getIntent().getStringExtra("dType");
                dynamicSeq = json.getString("dynamicSeq");
                responseTime = json.getString("responseTime");
                firstDistance = json.getString("firstDistance");

                Log.d("chen", "responseTime" + responseTime + "firstDistance" + firstDistance);

                if (dType.equals("01")) {
                    if (type.equals("01")) {
                        if (!isNext) {
                            setContentView(R.layout.activity_item_social_main);
                        }
                        setdata(json);
                        initView1(json);
                    } else {
                        if (!isNext) {
                            setContentView(R.layout.activity_item_zhfsocial_main);
                        }
                        setdata(json);
                        initView2(json);
                    }
                } else if (dType.equals("02")) {
                    if (type.equals("01")) {
                        if (!isNext) {
                            setContentView(R.layout.activity_item_social_weizhi);
                        }
                        setdata(json);
                        initView5(json);
                    } else {
                        if (!isNext) {
                            setContentView(R.layout.activity_item_zhfsocial_main);
                        }
                        setdata(json);
                        initView6(json);
                    }
                } else if (dType.equals("03") || dType.equals("04")) {
                    if (type.equals("01")) {
                        if (!isNext) {
                            setContentView(R.layout.activity_item2_social_main);
                        }
                        setdata(json);
                        initView3(json);
                    } else {
                        if (!isNext) {
                            setContentView(R.layout.activity_item2_zhfsocial_main);
                        }
                        setdata(json);
                        initView4(json);
                    }
                } else if (dType.equals("05")) {

                    isHaveMargin = object.getString("margin");
                    isVip = object.getIntValue("vip");
                    vipLevel = object.getString("vipLevel");

                    if (type.equals("01")) {
                        if (!isNext) {
                            setContentView(R.layout.activity_item5_social_main);
                        }
                        setdata(json);
                        initView7(json);

                    } else {
                        if (!isNext) {
                            setContentView(R.layout.activity_item5_zhfsocial_main);
                        }
                        setdata(json);
                        initView8(json);
                    }
                    updateTJLiulancishu("浏览");
                }
                rl_title.setFocusable(true);
                rl_title.setFocusableInTouchMode(true);
                rl_title.requestFocus();
                if (!isNext) {
                    initColor(1);
                    if ("05".equals(dType)) {
                        initColor(4);
                       // getdataQd();
                        getdata();
                        showCommentEditText(myuserID, myNick, dynamicSeq);
                    } else {
                        initColor(1);
                        getdata();
                        showCommentEditText(myuserID, myNick, dynamicSeq);
                    }
                }
                setlistener(json);
                updateLiulancishu();
                updateDeLiulancishu();
                if (!isNext) {
                    updateTJLiulancishu("浏览");
                }
                isNext = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "网络连接错误！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("dynamicSeq",dynamicSeq);
                param.put("createTime",createTime);
                //myuserID.equals(sID)
                if (DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)) {
                    param.put("uLoginId", myuserID);
                }
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private Boolean ComparePriceAndVipLevel(String price, String task_position){

        if (isVip > 0){

            String[] strloca=null;

            if (task_position!=null&&!"".equals(task_position)){
                strloca = task_position.split("\\|");
            }
            String resv2="",resv1="";
            if (strloca!=null&&strloca.length>0) {
                resv2 = strloca[0];
            }
            if (strloca!=null&&strloca.length>1) {
                resv1 = strloca[1];
            }
            double latitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLat()) ? "34.762711" : DemoApplication.getApp().getCurrentLat());
            double longitude1 = Double.valueOf(TextUtils.isEmpty(DemoApplication.getApp().getCurrentLng()) ? "113.744531" : DemoApplication.getApp().getCurrentLng());

            String str = "";
            if (!(String.valueOf(latitude1).equals("") || String.valueOf(longitude1).equals("") || resv1.equals("") || resv2.equals(""))) {
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1,longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                str = String.format("%.2f",dou);//format 返回的是字符串

            } else {
                str = "空";
            }

            if (str.equals("空")){

                return false;

            }else {

                if (vipLevel.equals("1")){

                    if (Double.valueOf(str) < 50 && Double.valueOf(price) < 101){

                        return true;

                    }else {

                        return false;

                    }

                }else if (vipLevel.equals("2")){

                    if (Double.valueOf(str) < 100 && Double.valueOf(price) < 1001){

                        return true;

                    }else {

                        return false;

                    }

                }else {

                    return true;

                }

            }

        }else {

            return false;

        }

    }

    private void updateTJLiulancishu(final String type) {
        String url = FXConstant.URL_JILU_LIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("chen","成功,updateTJLiulancishu="+s+type);
                if (type.equals("转发")){



                }else if (type.equals("浏览")) {

                    int a = Integer.valueOf(dynamicViews);

                    if (a > 0 && a%20 ==0){
                        sendPushMessage(sID,type);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("chen","失败,updateTJLiulancishu="+volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("create_time",createTime);
                param.put("dynamic_seq",dynamicSeq);
                param.put("timestamp",getNowTime());
                if (TextUtils.isEmpty(responseTime) || responseTime.equalsIgnoreCase("0")) {
                    String time = getMin(createTime);
                    Log.d("chen", "增加浏览time"+ time);
                    if (!TextUtils.isEmpty(time)  ) {
                        param.put("responseTime",time);
                    }
                }
                if (TextUtils.isEmpty(firstDistance)|| firstDistance.equalsIgnoreCase("0")) {
                    String distance = getDistance(resv2, resv1);
                    Log.d("chen", "增加浏览distance"+ distance);
                    if (!TextUtils.isEmpty(distance)) {
                        param.put("distance",distance);
                    }
                }

                param.put("type",type);
                param.put("v_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        if (!TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat())) {
            MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
        }
    }

    private String getMin(String s) {
        if (s == null || TextUtils.isEmpty(s)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        long spaceTime = 0;
        try {
            Date date = format.parse(s);
            spaceTime = System.currentTimeMillis() - date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        double dou = (spaceTime / 1000.00) / 60;
        String str = String.format("%.1f", dou);//format 返回的是字符串
        return str;

    }


    private void getRecommendList(){

        JSONObject object = JSONObject.parseObject(data);
        JSONArray array2 = object.getJSONArray("memodynamic");
        datas.clear();
        if (array2==null||"".equals(array2)||array2.size()==0){

            if (datas!=null&&datas.size()==0) {
                datas = new ArrayList<>();
                int width = 1080;
                if (DynaDetaActivity.this != null){

                    WindowManager wm = DynaDetaActivity.this.getWindowManager();
                    DisplayMetrics dm = new DisplayMetrics();
                    wm.getDefaultDisplay().getMetrics(dm);
                    width = dm.widthPixels;

                }
                adapter3 = new JiLuListAdapter(DynaDetaActivity.this, datas,"1",isAdvert,advertImageUrl,width);
                setHeight3();
                lv_pinglun.setAdapter(adapter3);
            }

        }else {

            if (datas==null){
                datas = new ArrayList<>();
            }

            List<DynamicRecommend> datas2 = new ArrayList<>();

            for (int i=0;i<array2.size();i++){

                JSONObject data = array2.getJSONObject(i);

                JSONArray userPro = data.getJSONArray("userProfession");

                Double all = 0.0;
                for (Object object1:userPro){

                    JSONObject jsonObject = (JSONObject)object1;

                    String proMargin = jsonObject.getString("margin");

                    if (proMargin != null && Double.valueOf(proMargin) > 0){

                        all = all + Double.valueOf(proMargin);

                    }

                }

                DynamicRecommend dynamicRecommend = new DynamicRecommend();
                dynamicRecommend.setMargin(all);
                dynamicRecommend.setObject(data);

                datas2.add(dynamicRecommend);

              //  datas.add(data);
            }

            Collections.sort(datas2, new Comparator() {
                @Override
                public int compare(Object o, Object t1) {

                    DynamicRecommend dynamicRecommend1 = (DynamicRecommend)o;

                    DynamicRecommend dynamicRecommend2 = (DynamicRecommend)t1;

                    return dynamicRecommend2.getMargin().compareTo(dynamicRecommend1.getMargin());

                }

            });


            for (DynamicRecommend dynamicRecommend: datas2){

                datas.add(dynamicRecommend.getObject());

            }


            int width = 1080;
            if (DynaDetaActivity.this != null){

                WindowManager wm = DynaDetaActivity.this.getWindowManager();
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                width = dm.widthPixels;

            }
            adapter3 = new JiLuListAdapter(DynaDetaActivity.this,datas,"1",isAdvert,advertImageUrl,width);
            setHeight3();
            lv_pinglun.setAdapter(adapter3);

        }

        if (mProgress!=null&&mProgress.isShowing()){
            mProgress.dismiss();
        }

    }



    private void getdataQd() {
        JSONObject object = JSONObject.parseObject(data);
        String firstId = object.getString("firstUId");
        String uLoginId = object.getString("uLoginId");
        String currentId;
        if (firstId==null||firstId.equalsIgnoreCase("null")){
            currentId = uLoginId;
        }else {
            currentId = firstId;
        }
        JSONArray array = object.getJSONArray("dynamicOrders");
        if (array==null||"".equals(array)||array.size()==0){
            if (mProgress!=null&&mProgress.isShowing()){
                mProgress.dismiss();
            }
            datas = new ArrayList<>();
            adapter2 = new QdBaojiaAdapter(DynaDetaActivity.this,datas);
            setHeight2();
            lv_pinglun.setAdapter(adapter2);
        }else {
            if (currentId.equals(myuserID)) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    datas.add(data);
                }
            }else {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject data = array.getJSONObject(i);
                    String u_id = data.getString("u_id");
                    if (u_id.equals(myuserID)) {
                        datas.add(data);
                        break;
                    }
                }
            }
            adapter2 = new QdBaojiaAdapter(DynaDetaActivity.this,datas);
            setHeight2();
            lv_pinglun.setAdapter(adapter2);
            lv_pinglun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                    final String orderId = datas.get(position).getString("order_id");
                    final String dynamic_seq = datas.get(position).getString("dynamic_seq");
                    final String timestamp = datas.get(position).getString("timestamp");
                    final String type = datas.get(position).getString("type");
                    final String u_id = datas.get(position).getString("u_id");
                    final String quote = datas.get(position).getString("quote");
                    final String state = datas.get(position).getString("state");
                    final String d_id = datas.get(position).getString("d_id");

                    if (state.equals("1")){

                        LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        TextView tv_title = (TextView) dialog.findViewById(R.id.title_tv);
                        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                        tv_title.setText("该订单已进行过验资，请在入账单/出账单中查看订单");
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        return;
                    }
                    if (!datas.get(position).getString("u_id").equals(myuserID)){


                        if ("06".equals(type)) {

                            // 06 代表新的订单模式
                            Intent intent = new Intent(DynaDetaActivity.this, NewsOrderDetailActivity.class);
                            intent.putExtra("orderId",orderId);
                            intent.putExtra("dynamicSeq",dynamic_seq);
                            intent.putExtra("timestamp",timestamp);
                            intent.putExtra("task_label",orderBodyTask);
                            intent.putExtra("u_id",u_id);
                            intent.putExtra("quote",quote);
                            intent.putExtra("biaoshi","1");
                            intent.putExtra("conId",d_id);
                            intent.putExtra("merId",u_id);
                            intent.putExtra("createTime",createTime);
                            intent.putExtra("orderState","02");

                            startActivity(intent);

                        }else {

                            String url = FXConstant.URL_Order_Detail;
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    if ("02".equals(type)) {
                                        Intent intent = new Intent(DynaDetaActivity.this, UOrderDetailTwoActivity.class);
                                        intent.putExtra("orderId",orderId);
                                        intent.putExtra("dynamic_seq",dynamic_seq);
                                        intent.putExtra("timestamp",timestamp);
                                        intent.putExtra("task_label",orderBodyTask);
                                        intent.putExtra("u_id",u_id);
                                        intent.putExtra("quote",quote);
                                        intent.putExtra("shuju",s);
                                        intent.putExtra("biaoshi","04");
                                        startActivity(intent);
                                    }else if ("04".equals(type)){
                                        Intent intent = new Intent(DynaDetaActivity.this, UOrderDetailFourActivity.class);
                                        intent.putExtra("orderId",orderId);
                                        intent.putExtra("dynamic_seq",dynamic_seq);
                                        intent.putExtra("timestamp",timestamp);
                                        intent.putExtra("task_label",orderBodyTask);
                                        intent.putExtra("u_id",u_id);
                                        intent.putExtra("quote",quote);
                                        intent.putExtra("shuju",s);
                                        intent.putExtra("biaoshi","04");
                                        startActivity(intent);
                                    }else if ("05".equals(type)){
                                        Intent intent = new Intent(DynaDetaActivity.this, UOrderDetailFiveActivity.class);
                                        intent.putExtra("orderId",orderId);
                                        intent.putExtra("dynamic_seq",dynamic_seq);
                                        intent.putExtra("timestamp",timestamp);
                                        intent.putExtra("task_label",orderBodyTask);
                                        intent.putExtra("u_id",u_id);
                                        intent.putExtra("quote",quote);
                                        intent.putExtra("shuju",s);
                                        intent.putExtra("biaoshi","04");
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(DynaDetaActivity.this, UOrderDetailActivity.class);
                                        intent.putExtra("orderId",orderId);
                                        intent.putExtra("dynamic_seq",dynamic_seq);
                                        intent.putExtra("timestamp",timestamp);
                                        intent.putExtra("task_label",orderBodyTask);
                                        intent.putExtra("u_id",u_id);
                                        intent.putExtra("quote",quote);
                                        intent.putExtra("shuju",s);
                                        intent.putExtra("biaoshi","04");
                                        startActivity(intent);
                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(getApplicationContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<>();
                                    params.put("orderId",orderId);
                                    return params;
                                }
                            };
                            MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);

                        }

                    }else {
                        LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                        re_item2.setVisibility(View.GONE);
                        tv_item1.setText("删除报价");
                        tv_item2.setText("修改报价");
                        re_item1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                deleteBaoJia(dynamic_seq,timestamp,orderId,position);
                            }
                        });
                        re_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                                if ("06".equals(type)) {

                                    // 06 代表新的订单模式
                                    Intent intent = new Intent(DynaDetaActivity.this, NewsOrderDetailActivity.class);
                                    intent.putExtra("orderId",orderId);
                                    intent.putExtra("dynamicSeq",dynamic_seq);
                                    intent.putExtra("timestamp",timestamp);
                                    intent.putExtra("task_label",orderBodyTask);
                                    intent.putExtra("u_id",u_id);
                                    intent.putExtra("quote",quote);
                                    intent.putExtra("biaoshi","1");
                                    intent.putExtra("conId",d_id);
                                    intent.putExtra("merId",u_id);
                                    intent.putExtra("createTime",createTime);

                                    startActivity(intent);

                                }else {

                                    String url = FXConstant.URL_Order_Detail;
                                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                            if ("02".equals(type)) {
                                                Intent intent = new Intent(DynaDetaActivity.this, UOrderDetailTwoActivity.class);
                                                intent.putExtra("orderId",orderId);
                                                intent.putExtra("dynamic_seq",dynamic_seq);
                                                intent.putExtra("timestamp",timestamp);
                                                intent.putExtra("u_id",u_id);
                                                intent.putExtra("quote",quote);
                                                intent.putExtra("shuju",s);
                                                intent.putExtra("biaoshi","07");
                                                startActivity(intent);
                                            }else if ("04".equals(type)){
                                                Intent intent = new Intent(DynaDetaActivity.this, UOrderDetailFourActivity.class);
                                                intent.putExtra("orderId",orderId);
                                                intent.putExtra("dynamic_seq",dynamic_seq);
                                                intent.putExtra("timestamp",timestamp);
                                                intent.putExtra("u_id",u_id);
                                                intent.putExtra("quote",quote);
                                                intent.putExtra("shuju",s);
                                                intent.putExtra("biaoshi","07");
                                                startActivity(intent);
                                            }else if ("05".equals(type)){
                                                Intent intent = new Intent(DynaDetaActivity.this, UOrderDetailFiveActivity.class);
                                                intent.putExtra("orderId",orderId);
                                                intent.putExtra("dynamic_seq",dynamic_seq);
                                                intent.putExtra("timestamp",timestamp);
                                                intent.putExtra("u_id",u_id);
                                                intent.putExtra("quote",quote);
                                                intent.putExtra("shuju",s);
                                                intent.putExtra("biaoshi","07");
                                                startActivity(intent);
                                            }else {
                                                Intent intent = new Intent(DynaDetaActivity.this, UOrderDetailActivity.class);
                                                intent.putExtra("orderId",orderId);
                                                intent.putExtra("dynamic_seq",dynamic_seq);
                                                intent.putExtra("timestamp",timestamp);
                                                intent.putExtra("u_id",u_id);
                                                intent.putExtra("quote",quote);
                                                intent.putExtra("shuju",s);
                                                intent.putExtra("biaoshi","07");
                                                startActivity(intent);
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            Toast.makeText(getApplicationContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
                                        }
                                    }){
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String,String> params = new HashMap<>();
                                            params.put("orderId",orderId);
                                            return params;
                                        }
                                    };
                                    MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);

                                }

                            }
                        });
                        re_item3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
            });
            if (mProgress!=null&&mProgress.isShowing()){
                mProgress.dismiss();
            }

        }
    }


    private void deleteBaoJia(final String dynamic_seq, final String timestamp, final String orderId, final int position) {
        String url = FXConstant.URL_DELETE_BAOJIA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("dynamicac,dels",s);
                reducePinglinCount();
                Toast.makeText(DynaDetaActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
                datas.remove(position);
                adapter2.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null){
                    Log.e("dynamicac,dele",volleyError.toString());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("dynamic_seq",dynamic_seq);
                param.put("timestamp",timestamp);
                param.put("order_id",orderId);
                param.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void updateTJzhuanfa(final String loginId,final int type) {
        String url = FXConstant.URL_TONGJI_ZHUANFA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add","增加浏览次数成功"+s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null) {
                    Log.e("Userdetailac,add", "增加浏览次数错误");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uLoginId",loginId);
                if ("01".equals(dType) || "05".equals(dType)) {
                    param.put("lifeDynamics", "1");
                    if (type==0) {
                        param.put("type", "qqLifeDynamic");
                    }else if (type==1){
                        param.put("type", "weixinLifeDynamic");
                    }else if (type==2){
                        param.put("type", "weiboLifeDynamic");
                    }
                }else if ("02".equals(dType)){
                    param.put("locationDynamics", "1");
                    if (type==0) {
                        param.put("type", "qqLocationDynamic");
                    }else if (type==1){
                        param.put("type", "weixinLocationDynamic");
                    }else if (type==2){
                        param.put("type", "weiboLocationDynamic");
                    }
                }else if ("03".equals(dType)){
                    param.put("businessDynamics", "1");
                    if (type==0) {
                        param.put("type", "qqBusinessDynamic");
                    }else if (type==1){
                        param.put("type", "weixinBusinessDynamic");
                    }else if (type==2){
                        param.put("type", "weiboLocationDynamic");
                    }
                }
                param.put("f_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void updateDeLiulancishu() {
        String url = FXConstant.URL_ADD_USERCISHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add","增加浏览次数成功"+s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null) {
                    Log.e("Userdetailac,add", "增加浏览次数错误");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uLoginId",loginId);
                param.put("timestamp",createTime);
                if (dType.equals("01") || dType.equals("05")) {
                    param.put("lifeDynamics", "1");
                }else if (dType.equals("02")){
                    param.put("locationDynamics", "1");
                }else if (dType.equals("03")){
                    param.put("businessDynamics", "1");
                }
                param.put("v_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void updateLiulancishu() {
        String url =FXConstant.URL_TONGJI_LIULANCISHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("dynamicActivity","成功,s="+s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dynamicActivity","失败,e="+volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("createTime",createTime);
                param.put("dynamicSeq",dynamicSeq);
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void getdata() {
        String url = FXConstant.URL_QUERY_DYNACOMMENT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSONObject.parseObject(s);
                JSONArray array = object.getJSONArray("list");
                if (array==null||"".equals(array)||array.size()==0){
                    if (mProgress!=null&&mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                    datas = new ArrayList<>();
                  //  adapter = new PingLunAdapter(DynaDetaActivity.this,datas);
                    int width = 1080;
                    if (DynaDetaActivity.this != null){

                        WindowManager wm = DynaDetaActivity.this.getWindowManager();
                        DisplayMetrics dm = new DisplayMetrics();
                        wm.getDefaultDisplay().getMetrics(dm);
                        width = dm.widthPixels;

                    }
                    adapter4 = new DynamicCommentAdapter(DynaDetaActivity.this,datas,dType,isAdvert,advertImageUrl,width);
                    setHeight();
                    lv_pinglun.setAdapter(adapter4);
                }else {
                    if (datas==null){
                        datas = new ArrayList<>();
                    }
                    for (int i=0;i<array.size();i++){
                        JSONObject data = array.getJSONObject(i);
                        datas.add(data);
                    }
                   // adapter = new PingLunAdapter(DynaDetaActivity.this,datas);
                    int width = 1080;
                    if (DynaDetaActivity.this != null){

                        WindowManager wm = DynaDetaActivity.this.getWindowManager();
                        DisplayMetrics dm = new DisplayMetrics();
                        wm.getDefaultDisplay().getMetrics(dm);
                        width = dm.widthPixels;

                    }
                    adapter4 = new DynamicCommentAdapter(DynaDetaActivity.this,datas,dType,isAdvert,advertImageUrl,width);
                    setHeight();
                    lv_pinglun.setAdapter(adapter4);

                    adapter4.setOnItemClickListener(new DynamicCommentAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            JSONObject jsonObject = datas.get(position);
                            String clickId = datas.get(position).getString("userId");
                            String objId = datas.get(position).getString("timeStamp");


                            showDeleteDialog(clickId,objId,position);

                            /*
                            if (myuserID.equals(clickId)){
                                showDeleteDialog(clickId,objId,position);
                            }else {
                                et_comment.setFocusable(true);
                                et_comment.setFocusableInTouchMode(true);
                                et_comment.requestFocus();
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                                isReply = "yes";
                                tagId = datas.get(position).getString("userId");
                                String userName = datas.get(position).getString("userName");
                                String content = datas.get(position).getString("content");
                                String[] contentArr = content.split("\\|");
                                if (contentArr.length>1){
                                    //回复的别人回复的

                                    currentContent = "//|@"+userName+":|"+contentArr[0].substring(0,contentArr[0].length()-2);
                                }else {
                                    //回复的
                                    currentContent = "//|@"+userName+":|"+content;
                                }

                            }
                            */

                        }
                    });

//                    adapter.setOnItemClickListener(new PingLunAdapter.MyItemClickListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            String clickId = datas.get(position).getString("userId");
//                            String objId = datas.get(position).getString("timeStamp");
//                            if (myuserID.equals(clickId)){
//                                showDeleteDialog(clickId,objId,position);
//                            }else {
//                                et_comment.setFocusable(true);
//                                et_comment.setFocusableInTouchMode(true);
//                                et_comment.requestFocus();
//                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                            }
//                        }
//                    });
                    if (mProgress!=null&&mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgress!=null&&mProgress.isShowing()){
                    mProgress.dismiss();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("dynamicId",dynamicSeq);
                param.put("createTime",createTime);
                param.put("uLoginId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    public void setHeight(){
        int height = 240;
        int count = adapter4.getCount();
        for(int i=0;i<count;i++){
            View temp = adapter4.getView(i,null,lv_pinglun);
            temp.measure(0,0);
            height += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = this.lv_pinglun.getLayoutParams();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        params.height = height;
        lv_pinglun.setLayoutParams(params);
    }
    public void setHeight2(){
        int height = 220;
        int count = adapter2.getCount();
        for(int i=0;i<count;i++){
            if (adapter2!=null&&lv_pinglun!=null) {
                View temp = adapter2.getView(i, null, lv_pinglun);
                temp.measure(0, 0);
                height += temp.getMeasuredHeight();
            }
        }
        ViewGroup.LayoutParams params = this.lv_pinglun.getLayoutParams();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        params.height = height;
        lv_pinglun.setLayoutParams(params);
    }
    public void setHeight3(){
        int height = 220;
        int count = adapter3.getCount();
        for(int i=0;i<count;i++){
            if (adapter3!=null&&lv_pinglun!=null) {
                View temp = adapter3.getView(i, null, lv_pinglun);
                temp.measure(0, 0);
                height += temp.getMeasuredHeight();
            }
        }
        ViewGroup.LayoutParams params = this.lv_pinglun.getLayoutParams();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        params.height = height+1000;
        lv_pinglun.setLayoutParams(params);
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }
    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }

    private void setlistener(final JSONObject json) {
        rl_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadType = 1;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                isReply = "no";
                currentContent = "";
                tagId = sID;
                initColor(1);
                if ("05".equals(dType)){
                   // getdataQd();
                    getRecommendList();
                }else {
                    getdata();
                    showCommentEditText(myuserID, myNick, dynamicSeq);
                }
            }
        });
        rl_liulancishu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage=1;
                loadType = 2;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                initColor(2);
                getJiLuList("浏览");
            }
        });
        scr_all.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 判断scrollview 滑动到底部
                // scrollY 的值和子view的高度一样，这人物滑动到了底部
                if (scr_all.getChildAt(0).getHeight() - scr_all.getHeight() == scr_all.getScrollY()){
                    if (datas!=null&&datas.size()>0&&datas.size()==20*currentPage) {
                        if (loadType == 2) {
                            if (mProgress != null) {
                                mProgress.setMessage("正在加载数据...");
                                mProgress.show();
                            }
                            currentPage++;
                            getJiLuList("浏览");
                        } else if (loadType==3){
                            if (mProgress != null) {
                                mProgress.setMessage("正在加载数据...");
                                mProgress.show();
                            }
                            currentPage++;
                            getJiLuList("转发");
                        }
                    }
                }
                return false;
            }
        });
        if (loginId.equals(myuserID)){
            if ("05".equals(dType)){
                tv_delete.setText("编辑");
            }
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("05".equals(dType)){
                        LayoutInflater inflater5 = LayoutInflater.from(DynaDetaActivity.this);
                        RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.layout_buttom2_dialog, null);
                        final Dialog dialog5 = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
                        dialog5.show();
                        dialog5.getWindow().setContentView(layout5);
                        dialog5.setCanceledOnTouchOutside(true);
                        dialog5.setCancelable(true);
                        TextView tv_title = (TextView) layout5.findViewById(R.id.tv_title);
                        TextView tv_item1 = (TextView) layout5.findViewById(R.id.tv_item1);
                        TextView tv_item2 = (TextView) layout5.findViewById(R.id.tv_item2);
                        RelativeLayout re_item1 = (RelativeLayout) layout5.findViewById(R.id.re_item1);
                        RelativeLayout re_item2 = (RelativeLayout) layout5.findViewById(R.id.re_item2);
                        RelativeLayout re_item3 = (RelativeLayout) layout5.findViewById(R.id.re_item3);
                        RelativeLayout re_item4 = (RelativeLayout) layout5.findViewById(R.id.re_item4);
                        RelativeLayout re_item5 = (RelativeLayout) layout5.findViewById(R.id.re_item5);
                        re_item3.setVisibility(View.GONE);
                        re_item4.setVisibility(View.GONE);
                        tv_item1.setText("取消派单");
                        tv_item2.setText("修改派单");
                        tv_title.setText("请选择");
                        re_item1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog5.dismiss();
                                ShowOrderDelete();
                            }
                        });
                        re_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog5.dismiss();
                                String data = json.toString();
                                startActivity(new Intent(DynaDetaActivity.this,ReviseXuQiuActivity.class).putExtra("data",data));
                            }
                        });
                        re_item5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog5.dismiss();
                            }
                        });
                    }else {
                        showDelete();
                    }
                }
            });
        }else {
            tv_delete.setText("分享");
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveCurrentImage();
                }
            });
        }
    }

    private void getJiLuList(final String str) {
        String url = FXConstant.URL_SELECT_JILULIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject obj = JSON.parseObject(s);
                JSONArray array = obj.getJSONArray("list");
                if (array==null||"".equals(array)||array.size()==0){
                    if (mProgress!=null&&mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                    if (datas!=null&&datas.size()==0) {
                        datas = new ArrayList<>();

                        int width = 1080;
                        if (DynaDetaActivity.this != null){

                            WindowManager wm = DynaDetaActivity.this.getWindowManager();
                            DisplayMetrics dm = new DisplayMetrics();
                            wm.getDefaultDisplay().getMetrics(dm);
                            width = dm.widthPixels;

                        }

                        adapter3 = new JiLuListAdapter(DynaDetaActivity.this, datas,"0",isAdvert,advertImageUrl,width);
                        setHeight3();
                        lv_pinglun.setAdapter(adapter3);
                    }
                }else {
                    int pos = 0;
                    if (datas==null){
                        datas = new ArrayList<>();
                    }else {
                        pos = datas.size();
                    }
                    for (int i=0;i<array.size();i++){
                        JSONObject data = array.getJSONObject(i);
                        datas.add(data);
                    }
                    int width = 1080;
                    if (DynaDetaActivity.this != null){

                        WindowManager wm = DynaDetaActivity.this.getWindowManager();
                        DisplayMetrics dm = new DisplayMetrics();
                        wm.getDefaultDisplay().getMetrics(dm);
                        width = dm.widthPixels;

                    }
                    adapter3 = new JiLuListAdapter(DynaDetaActivity.this,datas,"0",isAdvert,advertImageUrl,width);
                    setHeight3();
                    lv_pinglun.setAdapter(adapter3);
                    if (mProgress!=null&&mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("dynamic_seq",dynamicSeq);
                params.put("create_time",createTime);
                params.put("currentPage",currentPage+"");
                params.put("type",str);
                return params;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void initColor(int i) {
        tv_title_zhf.setTextColor(getResources().getColor(R.color.gray));
        tv_count_zhf.setTextColor(getResources().getColor(R.color.gray));
        tv_title_llcsh.setTextColor(getResources().getColor(R.color.gray));
        tv_count_llc.setTextColor(getResources().getColor(R.color.gray));
        tv_title_pl.setTextColor(getResources().getColor(R.color.gray));
        tv_count_pl.setTextColor(getResources().getColor(R.color.gray));

        if (dType.equals("05")){
            tv_commentTitle.setTextColor(getResources().getColor(R.color.gray));
            tv_commentCount.setTextColor(getResources().getColor(R.color.gray));
        }

        if (i==4){
            tv_commentTitle.setTextColor(getResources().getColor(R.color.accent_red));
            tv_commentCount.setTextColor(getResources().getColor(R.color.accent_red));
        }else if (i==3){
            tv_title_zhf.setTextColor(getResources().getColor(R.color.accent_red));
            tv_count_zhf.setTextColor(getResources().getColor(R.color.accent_red));
        }else if (i==2){
            tv_title_llcsh.setTextColor(getResources().getColor(R.color.accent_red));
            tv_count_llc.setTextColor(getResources().getColor(R.color.accent_red));
        }else {
            tv_title_pl.setTextColor(getResources().getColor(R.color.accent_red));
            tv_count_pl.setTextColor(getResources().getColor(R.color.accent_red));
        }
    }

    private void ShowOrderDelete(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(DynaDetaActivity.this);
        builder.setMessage("是否确认取消派单?");
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
                        dialog.dismiss();
                        if (mProgress != null && !mProgress.isShowing()) {
                            mProgress.setMessage("请稍后...");
                            mProgress.show();
                        } else {
                            mProgress = CustomProgressDialog.createDialog(DynaDetaActivity.this);
                            mProgress.setMessage("请稍后...");
                            mProgress.setCancelable(true);
                            mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    mProgress.dismiss();
                                }
                            });
                            mProgress.show();
                        }

                        String url = FXConstant.URL_DELETEDYNAMICPUSH;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (mProgress != null && mProgress.isShowing()) {
                                    mProgress.dismiss();
                                }

                                Log.e("Dynamicac,s", "删除结果s=" + s);
                                JSONObject object = JSON.parseObject(s);
                                String code = object.getString("code");

                                if (code.equals("SUCCESS")) {

                                    Toast.makeText(DynaDetaActivity.this, "已取消派单", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(DynaDetaActivity.this, "网络不稳定，请稍后再试", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                if (mProgress != null && mProgress.isShowing()) {
                                    mProgress.dismiss();
                                }

                                Toast.makeText(DynaDetaActivity.this, "网络不稳定,取消失败", Toast.LENGTH_SHORT).show();
                            }

                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();

                                params.put("dynamicSeq", dynamicSeq);
                                params.put("createTime", createTime);
                                params.put("orderState","03");
                                return params;

                            }

                        };

                        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
                    }
                }
        );

        builder.show();

    }

    private void showDelete() {
        if (sum!=null&&fromId!=null) {
            String nowTime = getNowTime();
            long time = Long.valueOf(nowTime) - Long.valueOf(createTime);
            if (time<1000000){
                Toast.makeText(DynaDetaActivity.this,"转发有反馈的动态24小时之后才能删除！",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        final boolean[] canDelete = {true};
        final boolean[] hasOrder = {false};
        final boolean[] finishTime = {true};
        final boolean[] finishOrder = {true};
        final AlertDialog.Builder builder = new AlertDialog.Builder(DynaDetaActivity.this);
        builder.setMessage("确认删除吗?");
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
                dialog.dismiss();
                if (mProgress!=null&&!mProgress.isShowing()){
                    mProgress.setMessage("正在删除,请稍后...");
                    mProgress.show();
                }else {
                    mProgress = CustomProgressDialog.createDialog(DynaDetaActivity.this);
                    mProgress.setMessage("正在删除,请稍后...");
                    mProgress.setCancelable(true);
                    mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            mProgress.dismiss();
                        }
                    });
                    mProgress.show();
                }
                if (dType.equals("05")){
                    String nowTime = getNowTime();
                    long time = Long.valueOf(nowTime) - Long.valueOf(createTime);
                    if (time<10000){
                        LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                        final Dialog dialog2 = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
                        dialog2.show();
                        dialog2.getWindow().setContentView(layout);
                        dialog2.setCanceledOnTouchOutside(false);
                        dialog2.setCancelable(false);
                        TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                        Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                        tv_title.setText("发布的需求动态一小时之后才可以删除");
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                                if (mProgress!=null&&mProgress.isShowing()){
                                    mProgress.dismiss();
                                }
                            }
                        });
                    }else if (datas==null||datas.size()==0){
                        String url = FXConstant.URL_DELETE_PUBLISH;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (mProgress!=null&&mProgress.isShowing()){
                                    mProgress.dismiss();
                                }
                                Log.e("Dynamicac,s","删除结果s="+s);
                                JSONObject object = JSON.parseObject(s);
                                String code = object.getString("code");
                                if (code.equals("SUCCESS")){
                                    Toast.makeText(DynaDetaActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(DynaDetaActivity.this,"网络繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                if (mProgress!=null&&mProgress.isShowing()){
                                    mProgress.dismiss();
                                }
                                Toast.makeText(DynaDetaActivity.this,"网络连接错误,删除失败",Toast.LENGTH_SHORT).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("dynamicSeq",dynamicSeq);
                                params.put("createTime",createTime);
                                return params;
                            }
                        };
                        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
                    }else {
                        for (int i = 0; i < datas.size(); i++) {
                            final String state = datas.get(i).getString("state");
                            Log.e("Dynamicac,state",state);
                            if (state.equals("1")) {
                                canDelete[0] = false;
                                finishTime[0] = false;
                                finishOrder[0] = false;
                                hasOrder[0] = true;
                                final String orderId = datas.get(i).getString("order_id");
                                String url = FXConstant.URL_Order_Detail;
                                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        JSONObject object1 = JSONObject.parseObject(s);
                                        JSONObject object = object1.getJSONObject("odi");
                                        String resv4 = object.getString("resv4");
                                        if (resv4 != null && !"".equals(resv4) && !resv4.equalsIgnoreCase("null")) {
                                            finishOrder[0] = true;
                                            String time1 = resv4.substring(0, 4);
                                            String time2 = resv4.substring(5, 7);
                                            String time3 = resv4.substring(8, 10);
                                            String nowTime = getNowTime2();
                                            String nowTime1 = nowTime.substring(0, 4);
                                            String nowTime2 = nowTime.substring(4, 6);
                                            String nowTime3 = nowTime.substring(6, 8);
                                            int day1 = (Integer.parseInt(nowTime1) - Integer.parseInt(time1)) * 365;
                                            int day2 = (Integer.parseInt(nowTime2) - Integer.parseInt(time2)) * 30;
                                            int day3 = (Integer.parseInt(nowTime3) - Integer.parseInt(time3));
                                            if (day1 + day2 + day3 >= 15) {
                                                Log.e("Dynamicac,s","完成时间不足，无法删除");
                                                canDelete[0] = true;
                                                finishTime[0] = true;
                                            } else {
                                                canDelete[0] = false;
                                                finishTime[0] = false;
                                            }
                                        } else {
                                            Log.e("Dynamicac,s","resv4不为空无法删除");
                                            canDelete[0] = false;
                                            finishOrder[0] = false;
                                        }
                                        if (!canDelete[0]){
                                            LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
                                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                            final Dialog dialog2 = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
                                            dialog2.show();
                                            dialog2.getWindow().setContentView(layout);
                                            dialog2.setCanceledOnTouchOutside(false);
                                            dialog2.setCancelable(false);
                                            TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                                            Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                                            if (hasOrder[0]&&!finishOrder[0]) {
                                                tv_title.setText("该动态有相关交易未完成,暂时无法删除");
                                            }else if (hasOrder[0]&&finishOrder[0]&&!finishTime[0]){
                                                tv_title.setText("该需求相关交易完成后两周才允许删除动态");
                                            }else {
                                                tv_title.setText("发布的需求动态一小时之后才可以删除");
                                            }
                                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog2.dismiss();
                                                    if (mProgress!=null&&mProgress.isShowing()){
                                                        mProgress.dismiss();
                                                    }
                                                }
                                            });
                                        }else {
                                            Log.e("Dynamicac,s","开始删除");
                                            String url = FXConstant.URL_DELETE_PUBLISH;
                                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    if (mProgress!=null&&mProgress.isShowing()){
                                                        mProgress.dismiss();
                                                    }
                                                    Log.e("Dynamicac,s","删除结果s="+s);
                                                    JSONObject object = JSON.parseObject(s);
                                                    String code = object.getString("code");
                                                    if (code.equals("SUCCESS")){
                                                        Toast.makeText(DynaDetaActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }else {
                                                        Toast.makeText(DynaDetaActivity.this,"网络繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    if (mProgress!=null&&mProgress.isShowing()){
                                                        mProgress.dismiss();
                                                    }
                                                    Toast.makeText(DynaDetaActivity.this,"网络连接错误,删除失败",Toast.LENGTH_SHORT).show();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String,String> params = new HashMap<String, String>();
                                                    params.put("dynamicSeq",dynamicSeq);
                                                    params.put("createTime",createTime);
                                                    return params;
                                                }
                                            };
                                            MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("orderId", orderId);
                                        return params;
                                    }
                                };
                                MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
                                break;
                            }
                        }
                    }
                }else {
                    String url = FXConstant.URL_DELETE_PUBLISH;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (mProgress!=null&&mProgress.isShowing()){
                                mProgress.dismiss();
                            }
                            JSONObject object = JSON.parseObject(s);
                            String code = object.getString("code");
                            if (code.equals("SUCCESS")){
                                Toast.makeText(DynaDetaActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(DynaDetaActivity.this,"网络繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (mProgress!=null&&mProgress.isShowing()){
                                mProgress.dismiss();
                            }
                            Toast.makeText(DynaDetaActivity.this,"网络连接错误,删除失败",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("dynamicSeq",dynamicSeq);
                            params.put("createTime",createTime);
                            return params;
                        }
                    };
                    MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
                }
            }
        });
        builder.show();
    }

    private void showDeleteDialog(final String clickId, final String objId, final int position) {
        LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        tv_item1.setText("回复");
        tv_item2.setText("删除");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                et_comment.setFocusable(true);
                et_comment.setFocusableInTouchMode(true);
                et_comment.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                isReply = "yes";
                tagId = datas.get(position).getString("userId");
                String userName = datas.get(position).getString("userName");
                String content = datas.get(position).getString("content");
                String[] contentArr = content.split("\\|");
                if (contentArr.length>1){
                    //回复的别人回复的
                    currentContent = "//|@"+userName+":|"+contentArr[0].substring(0,contentArr[0].length()-2);
                }else {
                    //回复的
                    currentContent = "//|@"+userName+":|"+content;
                }

            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                new AlertDialog.Builder(DynaDetaActivity.this)
                        .setTitle("确认")
                        .setMessage("确认删除评论么？")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                String url = FXConstant.URL_DELETE_DYNACOMMENT;
                                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        Log.e("dynamicac,deleone",s);
                                        if (mProgress!=null&&mProgress.isShowing()){
                                            mProgress.dismiss();
                                        }
                                        JSONObject object = JSONObject.parseObject(s);
                                        String code = object.getString("code");
                                        if ("success".equals(code)){
                                            reducePinglinCount();
                                            Toast.makeText(DynaDetaActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                            datas.remove(position);
                                            adapter4.notifyDataSetChanged();
                                        }else {
                                            Toast.makeText(DynaDetaActivity.this,"网络繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        if (mProgress!=null&&mProgress.isShowing()){
                                            mProgress.dismiss();
                                        }
                                        Toast.makeText(DynaDetaActivity.this,"网络连接错误,删除失败",Toast.LENGTH_SHORT).show();
                                    }
                                }){
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String,String> param = new HashMap<>();
                                        param.put("dynamicId",dynamicSeq);
                                        param.put("createTime",createTime);
                                        param.put("userId",clickId);
                                        param.put("timeStamp",objId);
                                        return param;
                                    }
                                };
                                MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
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
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void saveCurrentImage() {
        int jietuType;
        if ("01".equals(dType)){
            jietuType = 1;//生活
        }else if ("02".equals(dType)){
            if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
                jietuType = 3;//宝藏
            }else {
                jietuType = 2;//坐标
            }
        }else if (dType.equals("03") || dType.equals("04")){
            jietuType = 4;
        }else {
            jietuType = 5;
        }
        //获取当前屏幕的大小
        ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, rl_neirong, "分享动态红包",null,jietuType,false,0,0);

        if (dType.equals("05")){

            SharedPreferences sp = getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);

            String dynamicType = sp.getString("dynamicType","0");

            if (dynamicType.equals("01")){
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, rl_neirong, "分享动态红包", null,51,false,0,0);
            }else if (dynamicType.equals("02")){
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, rl_neirong, "分享动态红包", null,52,false,0,0);
            }else if (dynamicType.equals("03")){
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, rl_neirong, "分享动态红包", null,53,false,0,0);
            }

        }

        fenxiang();
    }

    private void fenxiang() {


        if (dType.equals("05")){

            LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
            final LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.dialog_dynamicorder_share, null);
            final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this, R.style.Dialog).create();

            TextView tv_first = (TextView)layout.findViewById(R.id.tv_firstTitle);
            TextView tv_second = (TextView)layout.findViewById(R.id.tv_secondTitle);
            String str1 = "<font color='#FF2600'><big>最佳</big></font>接单方案";
            String str2 = "<font color='#5BB252'><big>免费</big></font>接单方案";
            tv_first.setText(Html.fromHtml(str1));
            tv_second.setText(Html.fromHtml(str2));

            dialog.show();
            dialog.getWindow().setContentView(layout);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes() ;
            Display display = DynaDetaActivity.this.getWindowManager().getDefaultDisplay();
            params.width =(int) (display.getWidth()*0.75);                     //使用这种方式更改了dialog的框宽
            dialog.getWindow().setAttributes(params);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            RelativeLayout r1 = (RelativeLayout)layout.findViewById(R.id.rl_wxcricle);
            r1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    fxtowxm();
                }
            });

            RelativeLayout r2 = (RelativeLayout)layout.findViewById(R.id.r2_qzone);
            r2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    fxtoqqf();
                }
            });

            RelativeLayout r3 = (RelativeLayout)layout.findViewById(R.id.r3_wxchat);
            r3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    fxtowxf();
                }
            });
            Button bt_vip = (Button)layout.findViewById(R.id.bt_vip);
            bt_vip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(DynaDetaActivity.this, TopVipActivity.class);

                    startActivityForResult(intent,0);
                }
            });

            Button bt_margin = (Button)layout.findViewById(R.id.bt_margin);
            bt_margin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //直接跳转质保页面
                    dialog.dismiss();
                    SelectMarginInfo();

                }
            });

            Button bt_message = (Button)layout.findViewById(R.id.bt_message);
            bt_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(DynaDetaActivity.this, MessageOrderIntroduceActivity.class);

                    startActivityForResult(intent, 0);

                }
            });

        }else {
            ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, findViewById(R.id.rl_neirong), "分享名片红包", null,6,false,0,0);
            LayoutInflater inflater5 = LayoutInflater.from(DynaDetaActivity.this);
            final RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_fenxiang, null);
            final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
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
                    fxtoqqz();
                }
            });
            rl2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    fxtowxm();
                }
            });
            rl3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    fxtowb();
                }
            });
            rl4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    fxtoqqf();
                }
            });
            rl5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    fxtowxf();
                }
            });
        }


    }

    //查询专业信息 跳转对应的质保界面
    private void SelectMarginInfo(){

        if (DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)) {

            String url = FXConstant.URL_SELECTPROSINGLE;
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    JSONObject object = JSON.parseObject(s);
                    JSONObject proMap = object.getJSONObject("list");
                    if (proMap==null){
                        ToastUtils.showNOrmalToast(getApplicationContext(),"请您优先编辑第一个专业");
                        return;
                    }

                    String upName = proMap.getString("upName");
                    String margin = proMap.getString("margin");
                    String createTime = proMap.getString("creatTime");
                    String marginTime = proMap.getString("margin_time");

                    if (margin==null||"".equals(margin)||Double.parseDouble(margin)<=0){
                        startActivityForResult(new Intent(DynaDetaActivity.this, BZJJNActivity.class).putExtra("upId",DemoHelper.getInstance().getCurrentUsernName()+"1").putExtra("maj",upName).putExtra("biaoshi","00"),0);
                    }else {
                        startActivityForResult(new Intent(DynaDetaActivity.this, BZJZJActivity.class).putExtra("JINE",margin).putExtra("upId",DemoHelper.getInstance().getCurrentUsernName()+"1").putExtra("maj",upName)
                                .putExtra("createTime",createTime).putExtra("biaoshi","00").putExtra("margin_time",marginTime),0);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(), "网络不稳定！", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("up_id", myuserID+"1");
                    return param;
                }
            };
            MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);

        }

    }



    private void fxtoqqf() {
        String fenxiangType=null;
        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "qqLifeDynamic";
            } else if (dType.equals("02")){
                fenxiangType = "qqLocationDynamic";
            }else {
                fenxiangType = "qqBusinessDynamic";
            }
        }
        final QQ.ShareParams sp = new QQ.ShareParams();
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setTitle(null);
                sp.setText(null);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qq.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if ("05".equals(dType)) {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                }else {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                }
                sp.setSite("分享链接");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                sp.setText("发布接单坐标,名片主页分享奖励红包");
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qq.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fxtoqqz() {
        String fenxiangType=null;
        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "qqLifeDynamic";
            } else if (dType.equals("02")){
                fenxiangType = "qqLocationDynamic";
            }else {
                fenxiangType = "qqBusinessDynamic";
            }
        }
        final QZone.ShareParams sp = new QZone.ShareParams();
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setTitle(null);
                sp.setText(null);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qzone.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if ("05".equals(dType)) {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                }else {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                }
                sp.setSite("分享链接");
                sp.setTitle("正事多-接单派单工具");
                sp.setText("【正事多】里面有接单派单名片红包");
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qzone.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private String paidanZhuanfa;
    private void updatePaidanZhuanfa() {
        Log.d("chen", "开始保存记录发送过的派单" + paidanZhuanfa);
        if (TextUtils.isEmpty(paidanZhuanfa) && !dType.equalsIgnoreCase("05")) {
            return;
        }
        SharedPreferences mSharedPreferences = getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(paidanZhuanfa,"321");
        editor.commit();
        if (iv_pre_click != null) {
            iv_pre_click.setVisibility(View.GONE);
        }
    }
    private void fxtowxm() {
        String fenxiangType=null;
        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "weixinLifeDynamic";
            } else if (dType.equals("02")){
                fenxiangType = "weixinLocationDynamic";
            }else {
                fenxiangType = "weixinBusinessDynamic";
            }
        }


        final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setTitle("【正事多】里面有接单派单名片红包");
                if ("05".equals(dType)) {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime);
                }else {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime+"&type="+ finalFenxiangType);
                }
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fxtowxf() {
        String fenxiangType=null;
        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "weixinLifeDynamic";
            } else if (dType.equals("02")){
                fenxiangType = "weixinLocationDynamic";
            }else {
                fenxiangType = "weixinBusinessDynamic";
            }
        }
        final Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setTitle("【正事多】里面有接单派单名片红包");
                sp.setText("发布接单坐标,名片主页分享奖励红包");
                if ("05".equals(dType)) {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime);
                }else {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime+"&type="+ finalFenxiangType);
                }
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fxtowb() {
        String fenxiangType=null;
        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "weiboLifeDynamic";
            } else if (dType.equals("02")){
                fenxiangType = "weiboLocationDynamic";
            }else {
                fenxiangType = "weiboBusinessDynamic";
            }
        }
        final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wb.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String url;
                if ("05".equals(dType)) {
                    url = "http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime;
                }else {
                    url = "http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime+"&type="+ finalFenxiangType;
                }
                sp.setText("【正事多】上发现的,感觉不错,推荐!"+url);
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wb.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    private void setdata(JSONObject json) {

        String video = json.getString("video");
        String videoPictures = json.getString("videoPictures");
        sum = json.getString("sum");
        loginId = json.getString("uLoginId");
        createTime = json.getString("createTime");
        fromId = json.getString("fromUId");
        scr_all = (ScrollView) findViewById(R.id.scr_all);
        tv_count_pl = (TextView) this.findViewById(R.id.tv_count_pl);
        tv_title_pl = (TextView) this.findViewById(R.id.tv_title_pl);
        tv_title_zhf = (TextView) this.findViewById(R.id.tv_title_zhf);
        tv_title_llcsh = (TextView) this.findViewById(R.id.tv_title_llcsh);
        countPinglun = json.getString("resv7");
        if (countPinglun==null||"".equals(countPinglun)){
            countPinglun = "0";
        }
        if (countPinglun!=null&&Integer.valueOf(countPinglun)>9999){
            Double a = Double.valueOf(countPinglun)/10000;

            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(a);

            countPinglun = str+"万";
        }
        String views = json.getString("views");
        if (views==null||"".equals(views)){
            views = "0";
        }
        if (views!=null&&Integer.valueOf(views)>9999){

            Double a = Double.valueOf(views)/10000;

            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(a);

            views = str+"万";

        }
        dynamicViews = json.getString("views");

        tv_count_pl.setText(countPinglun);
        rl_video = (RelativeLayout) findViewById(R.id.rl_video);
        videoPlayer = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        tvNianLing = (TextView) findViewById(R.id.tv_nianling);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tvDistance = (TextView) this.findViewById(R.id.tv_distance);
        tv_count_llc = (TextView) this.findViewById(R.id.tv_count_llc);
        lv_pinglun = (ListView) findViewById(R.id.id_stickynavlayout);
        rl_title = (RelativeLayout) this.findViewById(R.id.rl_title);
        et_comment = (EditText)findViewById(R.id.et_comment);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_commentType1 = (TextView) findViewById(R.id.tv_commentType1);
        tv_commentType2 = (TextView) findViewById(R.id.tv_commentType2);
        tv_commentType3 = (TextView) findViewById(R.id.tv_commentType3);
        tv_commentType4 = (TextView) findViewById(R.id.tv_commentType4);


        //默认评论留言标签
        commentType = "2";
        tv_commentType1.setBackgroundColor(Color.parseColor("#ffbebebe"));
        tv_commentType2.setBackgroundColor(Color.parseColor("#ffbebebe"));
        tv_commentType3.setBackgroundColor(Color.parseColor("#46c01b"));
        tv_commentType4.setBackgroundColor(Color.parseColor("#ffbebebe"));


        rl_title.setVisibility(View.VISIBLE);
        tv_delete = (TextView) this.findViewById(R.id.tv_delete_dynamic);
        tv_nick = (TextView) this.findViewById(R.id.tv_nick);
        tv_time = (TextView) this.findViewById(R.id.tv_from);
        tvTitleA = (TextView) this.findViewById(R.id.tv_titl);
        iv_avatar = (CircleImageView) this.findViewById(R.id.sdv_image);
        rl_neirong = (RelativeLayout) this.findViewById(R.id.rl_neirong);
        rl_zhuanfa = (RelativeLayout) this.findViewById(R.id.rl_zhuanfa);
        rl_liulancishu = (RelativeLayout) this.findViewById(R.id.rl_liulancishu);
        image_1 = (ImageView) this.findViewById(R.id.image_1);
        image_2 = (ImageView) this.findViewById(R.id.image_2);
        image_3 = (ImageView) this.findViewById(R.id.image_3);
        image_4 = (ImageView) this.findViewById(R.id.image_4);
        image_5 = (ImageView) this.findViewById(R.id.image_5);
        image_6 = (ImageView) this.findViewById(R.id.image_6);
        image_7 = (ImageView) this.findViewById(R.id.image_7);
        image_8 = (ImageView) this.findViewById(R.id.image_8);
        tv_count_llc.setText(views);
         resv1 = TextUtils.isEmpty(json.getString("resv1")) ? "" : json.getString("resv1");
         resv2 = TextUtils.isEmpty(json.getString("resv2")) ? "" : json.getString("resv2");
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
        final String shareUserId = json.getString("shareUserId");
        String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
        if (video!=null&&videoPictures!=null){
            rl_video.setVisibility(View.VISIBLE);
            boolean setUp = videoPlayer.setUp(FXConstant.URL_VIDEO+video, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    "");
            v2 = rl_video;
            if (setUp) {
                Glide.with(getApplicationContext()).load(FXConstant.URL_VIDEO+videoPictures)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .error(R.drawable.default_error)
                        .crossFade().into(videoPlayer.thumbImageView);
            }else {
                Toast.makeText(getApplicationContext(),"视频播放失败",Toast.LENGTH_SHORT).show();
            }
        }else {
            rl_video.setVisibility(View.GONE);
            if (!imageStr.equals("")) {
                String[] images = imageStr.split("\\|");
                int imNumb = images.length;
                image_1.setVisibility(View.VISIBLE);
                v2 = image_1;
                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[0],image_1,DemoApplication.mOptions2);
                if (shareUserId!=null){
                    image_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (shareUserId.length()>11){
                                startActivity(new Intent(DynaDetaActivity.this, QiYeDetailsActivity.class).putExtra("qiyeId", shareUserId));
                            }else {
                                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, shareUserId));
                            }
                        }
                    });
                }else {
                    image_1.setOnClickListener(new ImageListener(images, 0));
                }
                if (imNumb > 1) {
                    image_2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[1],image_2,DemoApplication.mOptions2);
                    image_2.setOnClickListener(new ImageListener(images, 1));
                    if (imNumb > 2) {
                        image_3.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[2], image_3, DemoApplication.mOptions2);
                        image_3.setOnClickListener(new ImageListener(images, 2));
                        if (imNumb > 3) {
                            image_4.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[3], image_4, DemoApplication.mOptions2);
                            image_4.setOnClickListener(new ImageListener(images, 3));
                            if (imNumb > 4) {
                                image_5.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[4], image_5, DemoApplication.mOptions2);
                                image_5.setOnClickListener(new ImageListener(images, 4));
                                if (imNumb > 5) {
                                    image_6.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[5], image_6, DemoApplication.mOptions2);
                                    image_6.setOnClickListener(new ImageListener(images, 5));
                                    if (imNumb > 6) {
                                        image_7.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[6], image_7, DemoApplication.mOptions2);
                                        image_7.setOnClickListener(new ImageListener(images, 6));
                                        if (imNumb > 7) {
                                            image_8.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO + images[7], image_8, DemoApplication.mOptions2);
                                            image_8.setOnClickListener(new ImageListener(images, 7));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void showZhfDialog(final View v1, final String jinE, final String dynamicSeq, final String sID, final String fromUId, final String finalFirstImage, final String userID, final String content,final boolean pinjie) {


        if (dType.equals("05")){

            LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
            final LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.dialog_dynamicorder_share, null);
            final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this, R.style.Dialog).create();

            TextView tv_first = (TextView)layout.findViewById(R.id.tv_firstTitle);
            TextView tv_second = (TextView)layout.findViewById(R.id.tv_secondTitle);
            String str1 = "<font color='#FF2600'><big>最佳</big></font>接单方案";
            String str2 = "<font color='#5BB252'><big>免费</big></font>接单方案";
            tv_first.setText(Html.fromHtml(str1));
            tv_second.setText(Html.fromHtml(str2));

            dialog.show();
            dialog.getWindow().setContentView(layout);
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes() ;
            Display display = DynaDetaActivity.this.getWindowManager().getDefaultDisplay();
            params.width =(int) (display.getWidth()*0.75);                     //使用这种方式更改了dialog的框宽
            dialog.getWindow().setAttributes(params);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            RelativeLayout r1 = (RelativeLayout)layout.findViewById(R.id.rl_wxcricle);
            r1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    queryfxhbCount(v1,jinE,dynamicSeq,sID,fromUId,userID,pinjie,1,content);
                }
            });

            RelativeLayout r2 = (RelativeLayout)layout.findViewById(R.id.r2_qzone);
            r2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    queryfxhbCount(v1,jinE,dynamicSeq,sID,fromUId,userID,pinjie,3,content);
                }
            });

            RelativeLayout r3 = (RelativeLayout)layout.findViewById(R.id.r3_wxchat);
            r3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    fenxiangtowxf(v1,jinE,dynamicSeq,createTime,sID,fromUId,userID,pinjie,5,content);
                }
            });
            Button bt_vip = (Button)layout.findViewById(R.id.bt_vip);
            bt_vip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(DynaDetaActivity.this, TopVipActivity.class);

                    startActivityForResult(intent,0);
                }
            });
            Button bt_margin = (Button)layout.findViewById(R.id.bt_margin);
            bt_margin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //直接跳转质保页面
                    dialog.dismiss();
                    SelectMarginInfo();

                }
            });

            Button bt_message = (Button)layout.findViewById(R.id.bt_message);
            bt_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(DynaDetaActivity.this, MessageOrderIntroduceActivity.class);

                    startActivityForResult(intent, 0);

                }
            });

        }else {

            LayoutInflater inflater5 = LayoutInflater.from(DynaDetaActivity.this);
            final RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_fenxiang, null);
            final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
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
            TextView tv_title = (TextView) layout5.findViewById(R.id.tv_title);
            LinearLayout ll2 = (LinearLayout) layout5.findViewById(R.id.ll2);
            RelativeLayout rl1 = (RelativeLayout) layout5.findViewById(R.id.rl1);
            RelativeLayout rl2 = (RelativeLayout) layout5.findViewById(R.id.rl2);
            RelativeLayout rl3 = (RelativeLayout) layout5.findViewById(R.id.rl3);
            RelativeLayout rl4 = (RelativeLayout) layout5.findViewById(R.id.rl4);
            RelativeLayout rl5 = (RelativeLayout) layout5.findViewById(R.id.rl5);
            RelativeLayout rl6 = (RelativeLayout) layout5.findViewById(R.id.rl6);
            rl6.setVisibility(View.INVISIBLE);

//        rl6.setVisibility(View.INVISIBLE);
//        rl5.setVisibility(View.INVISIBLE);
//        tv4.setText("动    态");
//        Bitmap b1 = BitmapUtils.readBitMap(DynaDetaActivity.this,R.drawable.app_logo);
//        iv4.setImageBitmap(b1);

            if ("05".equals(dType)){

                SharedPreferences mSharedPreferences = getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);

//                if (!sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(dynamicSeq, "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(currentFloorPrice,currentPostion) && (Double.parseDouble(isHaveMargin) < 100 || Double.parseDouble(isHaveMargin)<Double.parseDouble(currentFloorPrice))) {
//                    Log.d("chen", content + "没有转发过");
//                    tv_title.setText("分享后  可接单");
//                    rl5.setVisibility(View.INVISIBLE);
//                    rl4.setVisibility(View.INVISIBLE);
//                    rl3.setVisibility(View.INVISIBLE);
//
//                } else {
                    Log.d("chen", content + "转发过");
                    tv_title.setText("分享至");
          //      }

                rl6.setVisibility(View.INVISIBLE);

            }
            rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    queryfxhbCount(v1,jinE,dynamicSeq,sID,fromUId,userID,pinjie,0,content);
                }
            });
            rl2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    queryfxhbCount(v1,jinE,dynamicSeq,sID,fromUId,userID,pinjie,1,content);
                }
            });
            rl3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    queryfxhbCount(v1,jinE,dynamicSeq,sID,fromUId,userID,pinjie,2,content);
                }
            });

            rl4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final int jietuType;
                    if ("01".equals(dType)){
                        jietuType = 1;//生活
                    }else if ("02".equals(dType)){
                        if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
                            jietuType = 3;//宝藏
                        }else {
                            jietuType = 2;//坐标
                        }
                    }else if (dType.equals("03") || dType.equals("04")){
                        jietuType = 4;
                    }else {
                        jietuType = 5;
                    }
                    fenxiangtoqqf(v1,jinE,dynamicSeq,createTime,sID,fromUId,userID,false,jietuType,content);
                }
            });

            rl5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    final int jietuType;
                    if ("01".equals(dType)){
                        jietuType = 1;//生活
                    }else if ("02".equals(dType)){
                        if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
                            jietuType = 3;//宝藏
                        }else {
                            jietuType = 2;//坐标
                        }
                    }else if (dType.equals("03") || dType.equals("04")){
                        jietuType = 4;
                    }else {
                        jietuType = 5;
                    }
                    fenxiangtowxf(v1,jinE,dynamicSeq,createTime,sID,fromUId,userID,false,jietuType,content);

                }
            });

            rl6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(DynaDetaActivity.this,ZhFaActivity.class);
                    intent.putExtra("dType",dType);
                    intent.putExtra("dynamicSeq",dynamicSeq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("jinE",jinE);
                    intent.putExtra("firstUId",sID);
                    intent.putExtra("fromUId",fromUId);
                    intent.putExtra("firstImage", finalFirstImage);
                    intent.putExtra("firstName",userID);
                    intent.putExtra("firstContent",content);
                    intent.putExtra("responseTime",responseTime);
                    intent.putExtra("firstDistance",firstDistance);
                    intent.putExtra("resv1",resv1);
                    intent.putExtra("resv2",resv2);
                    startActivityForResult(intent,0);
                }
            });

        }

    }

    private void queryfxhbCount(final View v1, final String fenxiangJine, final String dynamicSeq, final String sID, final String fromUId, final String userID, final boolean pinjie,final int type, final String content) {
        final int jietuType;
        if ("01".equals(dType)){
            jietuType = 1;//生活
        }else if ("02".equals(dType)){
            if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
                jietuType = 3;//宝藏
            }else {
                jietuType = 2;//坐标
            }
        }else if (dType.equals("03") || dType.equals("04")){
            jietuType = 4;
        }else {
            jietuType = 5;
        }
        String url = FXConstant.URL_QUERY_HBCOUNT+DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                int sum = object.getIntValue("sum");
                sum = sum+1;
                if (type==0) {
                    fenxiangtoqq(v1,fenxiangJine,dynamicSeq,sID,fromUId,userID,pinjie,jietuType,sum,content);
                }else if (type==1){
                    fenxiangtowx(v1,fenxiangJine,dynamicSeq,sID,fromUId,userID,pinjie,jietuType,sum,content);
                }else if (type==2){
                    fenxiangtowb(v1,fenxiangJine,dynamicSeq,sID,fromUId,userID,pinjie,jietuType,sum,content);
                }else if (type==3){
                    fenxiangtoqqf(v1,fenxiangJine,dynamicSeq,createTime,sID,fromUId,userID,pinjie,jietuType,content);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type==0) {
                    fenxiangtoqq(v1,fenxiangJine,dynamicSeq,sID,fromUId,userID,pinjie,jietuType,-2,content);
                }else if (type==1){
                    fenxiangtowx(v1,fenxiangJine,dynamicSeq,sID,fromUId,userID,pinjie,jietuType,-2,content);
                }else if (type==2){
                    fenxiangtowb(v1,fenxiangJine,dynamicSeq,sID,fromUId,userID,pinjie,jietuType,-2,content);
                }else if (type==3){
                    fenxiangtoqqf(v1,fenxiangJine,dynamicSeq,createTime,sID,fromUId,userID,false,jietuType,content);
                }
            }
        });
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void fenxiangtowb(final View v1, final String fenxiangJine, final String dynamicSeq, final String sID, final String fromUId, final String userID, final boolean pinjie, final int jietuType, final int sum, final String content) {
        String fenxiangType=null;
        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "weiboLifeDynamic";
            } else if (dType.equals("02")){
                fenxiangType = "weiboLocationDynamic";
            }else {
                fenxiangType = "weiboBusinessDynamic";
            }
        }
        final String text;
        if (!pinjie){
            text = "不限行业接派单，全民分享赚红包"+content;
        }else {
            text = "我转发他的动态，获得了一个红包"+content;
        }
        if (!pinjie){
            ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,jietuType,false,0,0);
        }else {
            ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", fenxiangJine,sum,true,0,0);
        }
        final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        sp.setTitle("正事多-接单派单工具");
        Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wb.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                if (!dType.equals("05")) {
                    updateTJzhuanfa(sID,2);
                }
                updatePaidanZhuanfa();
                addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wb.share(sp);

        /*LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!pinjie){
                    ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,jietuType,false);
                }else {
                    ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", fenxiangJine,sum,true);
                }
                final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,2);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wb.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByV2(DynaDetaActivity.this, v2);
                final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                String url;
                if ("05".equals(dType)) {
                    url = "http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime;
                }else {
                    url = "http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime+"&type="+ finalFenxiangType;
                }
                sp.setText(text+url);
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,2);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wb.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    private void fenxiangtowx(final View v1, final String fenxiangJine, final String dynamicSeq, final String sID, final String fromUId, final String userID, final boolean pinjie, final int jietuType, final int sum, final String content) {
        String fenxiangType=null;
        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "weixinLifeDynamic";
            } else if (dType.equals("02")){
                fenxiangType = "weixinLocationDynamic";
            }else {
                fenxiangType = "weixinBusinessDynamic";
            }
        }
        final String text;
        if (!pinjie){
            text = "不限行业接派单，全民分享赚红包"+content;
        }else {
            text = "我转发他的动态，获得了一个红包"+content;
        }
        if (!pinjie){
            ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,jietuType,false,0,0);

            if (dType.equals("05")){

                SharedPreferences sp = getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);

                String dynamicType = sp.getString("dynamicType","0");

                if (dynamicType.equals("01")){
                    ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,51,false,0,0);
                }else if (dynamicType.equals("02")){
                    ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,52,false,0,0);
                }else if (dynamicType.equals("03")){
                    ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,53,false,0,0);
                }

            }

        }else {
            ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", fenxiangJine,sum,true,0,0);
        }
        final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        sp.setTitle("【正事多】里面有接单派单名片红包");
        Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                if (!dType.equals("05")) {
                    updateTJzhuanfa(sID,1);
                }
                updatePaidanZhuanfa();
                addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);

        /*LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!pinjie){
                    ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,jietuType,false);
                }else {
                    ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", fenxiangJine,sum,true);
                }
                final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
                sp.setShareType(Platform.SHARE_IMAGE);
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,1);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByV2(DynaDetaActivity.this, v2);
                final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setTitle(text);
                if ("05".equals(dType)) {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime);
                }else {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime+"&type="+ finalFenxiangType);
                }
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,1);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    private void fenxiangtowxf(final View v1, final String fenxiangJine, final String dynamicSeq, final String createTime, final String sID, final String fromUId, final String userID, boolean pinjie, final int jietuType, final String content) {
        String fenxiangType=null;
        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "weixinLifeDynamic";
            } else if (dType.equals("02")){
                fenxiangType = "weixinLocationDynamic";
            }else {
                fenxiangType = "weixinBusinessDynamic";
            }
        }
        ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,jietuType,false,0,0);

        if (dType.equals("05")){

            SharedPreferences sp = getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);

            String dynamicType = sp.getString("dynamicType","0");

            if (dynamicType.equals("01")){
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,51,false,0,0);
            }else if (dynamicType.equals("02")){
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,52,false,0,0);
            }else if (dynamicType.equals("03")){
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,53,false,0,0);
            }

        }

        final Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        sp.setTitle("【正事多】里面有接单派单名片红包");
        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(DynaDetaActivity.this, "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(DynaDetaActivity.this, "成功分享到好友！", Toast.LENGTH_SHORT).show();
                if (!dType.equals("05")) {
                    updateTJzhuanfa(sID,1);
                }
                updatePaidanZhuanfa();
                addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID);
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(DynaDetaActivity.this, "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);
        /*LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,jietuType,false);
                final Wechat.ShareParams sp = new Wechat.ShareParams();
                sp.setShareType(Platform.SHARE_IMAGE);
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(DynaDetaActivity.this, "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(DynaDetaActivity.this, "成功分享到好友！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,1);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(DynaDetaActivity.this, "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByV2(DynaDetaActivity.this, v2);
                final Wechat.ShareParams sp = new Wechat.ShareParams();
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("不限行业接派单，全民分享赚红包"+content);
                if ("05".equals(dType)) {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime);
                }else {
                    sp.setUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq="+dynamicSeq+"&createTime="+createTime+"&type="+ finalFenxiangType);
                }
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(DynaDetaActivity.this, "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(DynaDetaActivity.this, "成功分享到微信好友！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,1);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID);
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(DynaDetaActivity.this, "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    private void fenxiangtoqqf(final View v1, final String fenxiangJine, final String dynamicSeq, final String createTime, final String sID, final String fromUId, final String userID, final boolean pinjie, final int jietuType, final String content) {
        String fenxiangType=null;
        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "qqLifeDynamic";
            } else if (dType.equals("02")){
                fenxiangType = "qqLocationDynamic";
            }else {
                fenxiangType = "qqBusinessDynamic";
            }
        }

        ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,jietuType,false,0,0);

        if (dType.equals("05")){

            SharedPreferences sp = getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);

            String dynamicType = sp.getString("dynamicType","0");

            if (dynamicType.equals("01")){
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,51,false,0,0);
            }else if (dynamicType.equals("02")){
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,52,false,0,0);
            }else if (dynamicType.equals("03")){
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,53,false,0,0);
            }

        }

        final QQ.ShareParams sp = new QQ.ShareParams();
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        sp.setTitle(null);
        sp.setText(null);
        Platform qqf = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qqf.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                if (!dType.equals("05")) {
                    updateTJzhuanfa(sID,0);
                }
                updatePaidanZhuanfa();
                if (dType.equals("05")){
                    addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID);
                }else {
                    addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID);
                }

            }
            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qqf.share(sp);

        /*LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,jietuType,false);
                final QQ.ShareParams sp = new QQ.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qqf = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qqf.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,0);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID);
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qqf.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByV2(DynaDetaActivity.this,v2);
                final QQ.ShareParams sp = new QQ.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                if ("05".equals(dType)) {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                }else {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                }
                sp.setSite("分享链接");
                sp.setTitle("正事多-接单派单工具");
                sp.setText("不限行业接派单，全民分享赚红包"+content);
                Platform qqf = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qqf.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(DynaDetaActivity.this, "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(DynaDetaActivity.this, "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,0);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,null,sID,fromUId,userID);
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(DynaDetaActivity.this, "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qqf.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }

    private void fenxiangtoqq(final View v1, final String fenxiangJine, final String dynamicSeq, final String sID, final String fromUId, final String userID, final boolean pinjie, final int jietuType, final int sum, final String content) {
        String fenxiangType=null;
        if (!dType.equals("05")) {
            if (dType.equals("01")) {
                fenxiangType = "qqLifeDynamic";
            } else if (dType.equals("02")){
                fenxiangType = "qqLocationDynamic";
            }else {
                fenxiangType = "qqBusinessDynamic";
            }
        }
        final String text;
        if (!pinjie){
            text = "不限行业接派单，全民分享赚红包"+content;
        }else {
            text = "我转发他的动态，获得了一个红包"+content;
        }
        if (!pinjie){
            ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,jietuType,false,0,0);
        }else {
            ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", fenxiangJine,sum,true,0,0);
        }
        final QZone.ShareParams sp = new QZone.ShareParams();
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        sp.setTitle(null);
        sp.setText(null);
        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qzone.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                if (!dType.equals("05")) {
                    updateTJzhuanfa(sID,0);
                }
                updatePaidanZhuanfa();
                addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID);
            }
            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qzone.share(sp);


       /* LayoutInflater inflaterDl = LayoutInflater.from(DynaDetaActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (!pinjie){
                    ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", null,jietuType,false);
                }else {
                    ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, v1, "分享动态红包", fenxiangJine,sum,true);
                }
                final QZone.ShareParams sp = new QZone.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,0);
                        }

                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID);
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qzone.share(sp);
            }
        });
        final String finalFenxiangType = fenxiangType;
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ScreenshotUtil.getBitmapByV2(DynaDetaActivity.this, v2);
                final QZone.ShareParams sp = new QZone.ShareParams();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                if ("05".equals(dType)) {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime);
                }else {
                    sp.setTitleUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                    sp.setSiteUrl("http://www.fulu86.com/Details_dt.html?dynamicSeq=" + dynamicSeq + "&createTime=" + createTime+"&type="+ finalFenxiangType);
                }
                sp.setSite("分享链接");
                sp.setTitle("正事多-接单派单工具");
                sp.setText(text);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                        if (!dType.equals("05")) {
                            updateTJzhuanfa(sID,0);
                        }
                        addzhuanfaCount(dynamicSeq,myuserID,fenxiangJine,sID,fromUId,userID);
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qzone.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
    }


    private void addzhuanfaCount(final String dynamicSeq, final String myuserID, final String jinE, final String sID, final String fromUId, final String userID) {
        String url = FXConstant.URL_ADD_COUNT_ZHUFA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateTJLiulancishu("转发");
                if ("05".equals(dType)){

                    if (jinE!=null&&Double.parseDouble(jinE) > 0 && isVip>0 && Double.parseDouble(isHaveMargin)>99) {
                        addhongbao(jinE,dynamicSeq,myuserID,userID);
                    }

                }else {
                    if (jinE!=null&&Double.parseDouble(jinE) > 0) {

                        dongtai(sID, fromUId, dynamicSeq, jinE, userID);

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                param.put("u_id", myuserID);
                param.put("dynamicSeq",dynamicSeq);
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void addhongbao(final String jinE, final String dynamicSeq, final String myuserID,final String userId) {
        String url = FXConstant.URL_ADD_RED_XUQIU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("socailmain,s",s);
                JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("已经转发过次动态".equals(code)){
                    LayoutInflater inflater1 = LayoutInflater.from(getApplicationContext());
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
                    dialog1.show();
                    dialog1.getWindow().setContentView(layout1);
                    dialog1.setCanceledOnTouchOutside(true);
                    dialog1.setCancelable(true);
                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                    title.setText("温馨提示");
                    btnOK1.setText("确定");
                    btnCancel1.setText("取消");
                    title_tv1.setText("您已经分享过此条动态,本次分享无红包奖励");
                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                    btnOK1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                }else if (code.equals("success")){
                    SoundPlayUtils.play(2);
                    LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                    final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    TextView tv_title1 = (TextView) layout.findViewById(R.id.tv_title1);
                    TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
                    TextView tv_yue = (TextView) layout.findViewById(R.id.tv_yue);
                    TextPaint tp = tv_title.getPaint();
                    tp.setFakeBoldText(true);
                    tv_title.setText(jinE + "元");
                    tv_title1.setText(userId);
                    tv_yue.setText("正事多 动态红包");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                param.put("u_id", myuserID);
                param.put("dynamicSeq",dynamicSeq);
                param.put("amount",jinE);
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void dongtai(final String firstUId, final String fromUId, final String dynamicSeq,final String jinE,final String name) {
        final String content = "我获得了一个分享动态红包!";
        String url = FXConstant.URL_PUBLISH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = JSONObject.parseObject(s);
                String code = jsonObject.getString("code");
                String message = jsonObject.getString("message");
                if (code.equals("SUCCESS")){
                    String timel1 = getNowTime();
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    if (dType.equals("01")) {
                        editor.putString("time1", timel1);
                        editor.putString("time5", timel1);
                    }else if (dType.equals("02")){
                        editor.putString("time2", timel1);
                        editor.putString("time6", timel1);
                    }else if (dType.equals("03")){
                        editor.putString("time3", timel1);
                        editor.putString("time7", timel1);
                    }else {
                        editor.putString("time4", timel1);
                        editor.putString("time8", timel1);
                    }
                    editor.commit();
                    updateBmob();
                }
                if ("已经转发过次动态".equals(message)){
                    LayoutInflater inflater1 = LayoutInflater.from(DynaDetaActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
                    dialog1.show();
                    dialog1.getWindow().setContentView(layout1);
                    dialog1.setCanceledOnTouchOutside(true);
                    dialog1.setCancelable(true);
                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                    title.setText("温馨提示");
                    btnOK1.setText("确定");
                    btnCancel1.setText("取消");
                    title_tv1.setText("您已经分享过此条动态,本次分享无红包奖励");
                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                    btnOK1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });
                }else {
                    SoundPlayUtils.play(2);
                    LayoutInflater inflaterDl = LayoutInflater.from(getApplicationContext());
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                    final Dialog dialog = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    TextView tv_title1 = (TextView) layout.findViewById(R.id.tv_title1);
                    TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
                    TextView tv_yue = (TextView) layout.findViewById(R.id.tv_yue);
                    TextPaint tp = tv_title.getPaint();
                    tp.setFakeBoldText(true);
                    tv_title.setText(jinE + "元");
                    tv_title1.setText(name);
                    tv_yue.setText("正事多 动态红包");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("dType",dType);
                param.put("firstUId",firstUId);
                param.put("fromUId",fromUId);
                param.put("dynamicSeq",dynamicSeq);
                param.put("content",content);
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void updateBmob() {
        String url = FXConstant.URL_UPDATE_DYNATIME;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                if (dType.equals("01")){
                    param.put("type5",getNowTime());
                }else if (dType.equals("02")){
                    param.put("type6",getNowTime());
                }else if (dType.equals("03")){
                    param.put("type7",getNowTime());
                }else if (dType.equals("05")){
                    param.put("type8",getNowTime());
                }
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void initView1(JSONObject json) {
        rl_pinglun = (RelativeLayout) this.findViewById(R.id.rl_pinglun);
        tv_content = (ExpandableTextView) findViewById(R.id.tv_content);
        tv_count_zhf = (TextView) this.findViewById(R.id.tv_count_zhf);
        final String userID = json.getString("uName");
        final String content = json.getString("content");
        final String sID = json.getString("uLoginId");
        // String token = json.getString("token");
        String rel_time = json.getString("createTime");
        String forwardTimes = json.getString("forwardTimes");
        String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
        if (!avatar.equals("")) {
            firstImage = avatar.split("\\|")[0];
        }
        if (forwardTimes==null||"".equals(forwardTimes)){
            forwardTimes = "0";
        }
        if (forwardTimes!=null&&Integer.valueOf(forwardTimes)>9999){
            Double a = Double.valueOf(forwardTimes)/10000;

            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(a);

            forwardTimes = str+"万";

        }
        final String dynamicSeq = json.getString("dynamicSeq");
        final String fromUId = sID;
        rl_zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage=1;
                loadType = 3;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                initColor(3);
                getJiLuList("转发");
                if (DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                    showZhfDialog(rl_neirong,"0",dynamicSeq,sID,fromUId,firstImage,userID,content,false);
                }
            }
        });
        if (!avatar.equals("")) {
            String[] orderProjectArray = avatar.split("\\|");
            avatar = orderProjectArray[0];
        }
        if (!(avatar.equals(""))) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar,iv_avatar,DemoApplication.mOptions);
        } else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
        }
        tv_count_zhf.setText(forwardTimes);
        tv_nick.setText(userID);
        final String shareRed = json.getString("shareRed");
        final String friendsNumber = json.getString("friendsNumber");
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            tv_nick.setTextColor(Color.RED);
        }
        tv_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,sID));
            }
        });
        tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,sID));
            }
        });
        // 设置头像.....
        // 显示文章内容
        // .setText(content);
        tv_content.setText(content, 0);
        tv_content.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
        rel_time = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
        tv_time.setText(rel_time);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
    }

    private void initView2(JSONObject json) {
        rl_pinglun = (RelativeLayout) this.findViewById(R.id.rl_pinglun);
        tv_content2 = (TextView) findViewById(R.id.tv_content);
        tv_zhf_content = (ExpandableTextView) this.findViewById(R.id.tv_zhf_content);
        tv_count_zhf = (TextView) this.findViewById(R.id.tv_count_zhf);
        tv_first_nick = (TextView) this.findViewById(R.id.tv_first_nick);
        tvfirst_TitleA = (TextView) this.findViewById(R.id.tv_zhf_titl);
        iv_first_avatar = (CircleImageView) this.findViewById(R.id.sdv_first_image);
        final String userID = json.getString("uName");
        final String content = json.getString("content");
        final String sID = json.getString("uLoginId");
        String rel_time = json.getString("createTime");
        String forwardTimes = json.getString("forwardTimes");
        String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
        String newContent = TextUtils.isEmpty(json.getString("newcontent"))?"":json.getString("newcontent");
        final String firstName = json.getJSONObject("userInfo").getString("uName");
        String firstImage = json.getJSONObject("userInfo").getString("uImage");
        final String firstID = json.getJSONObject("userInfo").getString("uLoginId");
        final String dynamicSeq = json.getString("dynamicSeq");
        final String firstId = json.getJSONObject("userInfo").getString("uLoginId");
        final String fromUId = sID;
        if (forwardTimes==null||"".equals(forwardTimes)){
            forwardTimes = "0";
        }
        if (forwardTimes!=null&&Integer.valueOf(forwardTimes)>9999){

            Double a = Double.valueOf(forwardTimes)/10000;

            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(a);

            forwardTimes = str+"万";

        }
        if (avatar.length() > 40) {
            String[] orderProjectArray = avatar.split("\\|");
            avatar = orderProjectArray[0];
        }
        if (!(avatar == "")) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar,iv_avatar,DemoApplication.mOptions);
        } else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
        }
        tv_count_zhf.setText(forwardTimes);
        tv_nick.setText(userID);
        final String shareRed = json.getString("shareRed");
        final String friendsNumber = json.getString("friendsNumber");
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            tv_nick.setTextColor(Color.RED);
        }
        tv_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,sID));
            }
        });
        tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,sID));
            }
        });
        tv_zhf_content.setText(newContent, 0);
        tv_zhf_content.setVisibility(TextUtils.isEmpty(newContent) ? View.GONE : View.VISIBLE);
        tv_first_nick.setText(firstName+":");
        if (firstImage!=null) {
            String[] orderProjectArray = firstImage.split("\\|");
            firstImage = orderProjectArray[0];
        }
        final String finalFirstImage = firstImage;
        rl_zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage=1;
                loadType = 3;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                initColor(3);
                getJiLuList("转发");
                if (DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                    showZhfDialog(rl_neirong,"0",dynamicSeq,firstId,fromUId,finalFirstImage,firstName,content,false);
                }
            }
        });
        String firstSex = json.getJSONObject("userInfo").getString("uSex");
        if ("00".equals(firstSex)){
            tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
        }else {
            tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }
        if (firstImage!=null) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + firstImage,iv_first_avatar,DemoApplication.mOptions);
        } else {
            iv_first_avatar.setVisibility(View.INVISIBLE);
            tvfirst_TitleA.setVisibility(View.VISIBLE);
            tvfirst_TitleA.setText(TextUtils.isEmpty(firstName) ? firstID : firstName);
        }
        iv_first_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID));
            }
        });
        tv_first_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID));
            }
        });
        tvfirst_TitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID));
            }
        });
        tv_content2.setText(content);
        rel_time = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
        tv_time.setText(rel_time);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
    }
    private void initMapView(String lat,String lng){
        mMapView.setVisibility(View.VISIBLE);
        BaiduMap mBaiduMap;
        UiSettings mUiSettings;
        mBaiduMap = mMapView.getMap();
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setAllGesturesEnabled(false);
        //设置是否显示比例尺控件
        mMapView.showScaleControl(false);
        //设置是否显示缩放控件
        mMapView.showZoomControls(false);
        mBaiduMap.clear();
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
        LatLng p = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        mMapView = new TextureMapView(this,new BaiduMapOptions().mapStatus(new MapStatus.Builder()
                .target(p).build()));
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(p);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();
        MarkerOptions option = new MarkerOptions().position(convertLatLng).icon(mCurrentMarker);
        mBaiduMap.addOverlay(option);
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 16.0f);
        mBaiduMap.animateMapStatus(u);
        final String finalLat = lat;
        final String finalLng = lng;
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                    Toast.makeText(getApplicationContext(),"登陆后才可以操作哦！",Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(DynaDetaActivity.this, BaiDuYuanGongLocationActivity.class).putExtra("biaoshi", "01").putExtra("dynaLat", finalLat).putExtra("dynaLng", finalLng));
                }
            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }
    private void initView5(final JSONObject json) {
        rl_pinglun = (RelativeLayout) this.findViewById(R.id.rl_pinglun);
        tv_count_zhf = (TextView) this.findViewById(R.id.tv_count_zhf);
        rl_geshu = (RelativeLayout) findViewById(R.id.rl_geshu);
        tv_geshu = (TextView) findViewById(R.id.tv_geshu);
        weizhiContent = (TextView) findViewById(R.id.tv_content);
        weizhiDemandType = (TextView) findViewById(R.id.tv_demandType);
        mMapView = (TextureMapView) this.findViewById(R.id.bmapView1);
        iv_ditu = (ImageView) findViewById(R.id.iv_ditu);
        iv_bz_type = (ImageView) findViewById(R.id.iv_bz_type);
        final String userID = json.getString("uName");
        final String content = json.getString("content");
        final String sID = json.getString("uLoginId");
        // String token = json.getString("token");
        String rel_time = json.getString("createTime");
        String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
        redImage = json.getString("redImage");
        String redTime = json.getString("redTime");
        final String gameRed = json.getString("gameRed");
        String forwardTimes = json.getString("forwardTimes");
        String redSum = json.getString("redSum");
        if (redSum==null||"".equals(redSum)||redSum.equalsIgnoreCase("null")){
            redSum = "0";
        }
        if (!avatar.equals("")) {
            firstImage = avatar.split("\\|")[0];
        }
        if (forwardTimes==null||"".equals(forwardTimes)){
            forwardTimes = "0";
        }
        if (forwardTimes!=null&&Integer.valueOf(forwardTimes)>9999){

            Double a = Double.valueOf(forwardTimes)/10000;

            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(a);

            forwardTimes = str+"万";
        }
        String location1 = TextUtils.isEmpty(json.getString("location")) ? "" : json.getString("location");
        String lat = "",lng="";
        if (!"".equals(location1)){
            lat = location1.split("\\|")[0];
            lng = location1.split("\\|")[1];
        }
        if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
            iv_ditu.setVisibility(View.VISIBLE);
            Bitmap b1 = BitmapUtils.readBitMap(DynaDetaActivity.this,R.drawable.image_baozang);
            iv_ditu.setImageBitmap(b1);
            mMapView.setVisibility(View.GONE);
            final String biaoshi;
            if ("0".equals(redSum)) {
                rl_geshu.setVisibility(View.GONE);
                iv_bz_type.setVisibility(View.VISIBLE);
                Bitmap b2 = BitmapUtils.readBitMap(DynaDetaActivity.this,R.drawable.baoxiang_open);
                iv_bz_type.setImageBitmap(b2);
                biaoshi = "open";
            } else {
                if ("no".equals(redTime)) {
                    rl_geshu.setVisibility(View.VISIBLE);
                    iv_bz_type.setVisibility(View.GONE);
                    tv_geshu.setText(redSum);
                    biaoshi = "close";
                } else {
                    String nowTime = getNowTime();
                    if (Long.parseLong(nowTime) < Long.parseLong(redTime)) {
                        rl_geshu.setVisibility(View.VISIBLE);
                        iv_bz_type.setVisibility(View.GONE);
                        tv_geshu.setText(redSum);
                        biaoshi = "close";
                    } else {
                        rl_geshu.setVisibility(View.GONE);
                        iv_bz_type.setVisibility(View.GONE);
                        biaoshi = "none";
                    }
                }
            }
            final String finalLat = lat;
            final String finalLng = lng;
            final String finalRedSum = redSum;
            iv_ditu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                        Toast.makeText(getApplicationContext(),"登陆后才可以操作哦！",Toast.LENGTH_SHORT).show();
                    }else {
                        SharedPreferences sp = getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
                        String location = sp.getString("location", null);
                        ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, findViewById(R.id.rl_neirong), "分享名片红包", location, 3, false,0,0);
                        startActivity(new Intent(DynaDetaActivity.this, BaiDuBzLocationActivity.class).putExtra("biaoshi", biaoshi).putExtra("bzlat", finalLat)
                                .putExtra("bzlng", finalLng).putExtra("pintuUrl", redImage).putExtra("createTime", createTime).putExtra("user_id", sID)
                                .putExtra("dynamicSeq", dynamicSeq).putExtra("gameRed", gameRed).putExtra("name",userID).putExtra("geshu", finalRedSum));
                    }
                }
            });
        } else {
            rl_geshu.setVisibility(View.GONE);
            iv_bz_type.setVisibility(View.GONE);
            iv_ditu.setVisibility(View.GONE);
            if (!"".equals(lat) && !"".equals(lng)) {
                initMapView(lat, lng);
            }
        }
        final String dynamicSeq = json.getString("dynamicSeq");
        final String fromUId = sID;
        rl_zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage=1;
                loadType = 3;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                initColor(3);
                getJiLuList("转发");
                if (DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                    showZhfDialog(rl_neirong,"0",dynamicSeq,sID,fromUId,firstImage,userID,content,false);
                }
            }
        });
        if (!avatar.equals("")) {
            String[] orderProjectArray = avatar.split("\\|");
            avatar = orderProjectArray[0];
        }
        if (!(avatar.equals(""))) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar,iv_avatar,DemoApplication.mOptions);
        } else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
        }
        tv_count_zhf.setText(forwardTimes);
        tv_nick.setText(userID);
        final String shareRed = json.getString("shareRed");
        final String friendsNumber = json.getString("friendsNumber");
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            tv_nick.setTextColor(Color.RED);
        }
        tv_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,sID));
            }
        });
        tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,sID));
            }
        });
        // 设置头像.....
        // 显示文章内容
        // .setText(content);

        if (json.getString("demandType").equals("店")){

            weizhiDemandType.setText("店");
            weizhiDemandType.setVisibility(View.VISIBLE);
            weizhiContent.setText("      " + content);
            weizhiDemandType.setBackgroundResource(R.drawable.btn_corner_weizhired);

        }else if (json.getString("demandType").equals("玩")){

            weizhiDemandType.setText("玩");
            weizhiDemandType.setVisibility(View.VISIBLE);
            weizhiContent.setText("      " + content);
            weizhiDemandType.setBackgroundResource(R.drawable.btn_corner_weizhigreen);

        }else {

            weizhiDemandType.setVisibility(View.GONE);
            weizhiContent.setText(content);

        }

        rel_time = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
        tv_time.setText(rel_time);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
    }

    private void initView6(JSONObject json) {
        rl_pinglun = (RelativeLayout) this.findViewById(R.id.rl_pinglun);
        tv_count_zhf = (TextView) this.findViewById(R.id.tv_count_zhf);
        rl_geshu = (RelativeLayout) findViewById(R.id.rl_geshu);
        tv_geshu = (TextView) findViewById(R.id.tv_geshu);
        tv_content2 = (TextView) findViewById(R.id.tv_content);
        mMapView = (TextureMapView) this.findViewById(R.id.bmapView1);
        iv_ditu = (ImageView) findViewById(R.id.iv_ditu);
        iv_bz_type = (ImageView) findViewById(R.id.iv_bz_type);
        tv_zhf_content = (ExpandableTextView) this.findViewById(R.id.tv_zhf_content);
        tv_first_nick = (TextView) this.findViewById(R.id.tv_first_nick);
        tvfirst_TitleA = (TextView) this.findViewById(R.id.tv_zhf_titl);
        iv_first_avatar = (CircleImageView) this.findViewById(R.id.sdv_first_image);
        final String userID = json.getString("uName");
        final String content = json.getString("content");
        final String sID = json.getString("uLoginId");
        String rel_time = json.getString("createTime");
        String forwardTimes = json.getString("forwardTimes");
        String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
        String newContent = TextUtils.isEmpty(json.getString("newcontent"))?"":json.getString("newcontent");
        final String firstName = json.getJSONObject("userInfo").getString("uName");
        String firstImage = json.getJSONObject("userInfo").getString("uImage");
        final String firstID = json.getJSONObject("userInfo").getString("uLoginId");
        final String dynamicSeq = json.getString("dynamicSeq");
        final String firstId = json.getJSONObject("userInfo").getString("uLoginId");
        final String redImage = json.getString("redImage");
        String redTime = json.getString("redTime");
        final String gameRed = json.getString("gameRed");
        String redSum = json.getString("redSum");
        if (forwardTimes==null||"".equals(forwardTimes)){
            forwardTimes = "0";
        }
        if (forwardTimes!=null&&Integer.valueOf(forwardTimes)>9999){
            Double a = Double.valueOf(forwardTimes)/10000;

            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(a);

            forwardTimes = str+"万";
        }
        if (redSum==null||"".equals(redSum)||redSum.equalsIgnoreCase("null")){
            redSum = "0";
        }
        final String fromUId = sID;
        if (avatar.length() > 40) {
            String[] orderProjectArray = avatar.split("\\|");
            avatar = orderProjectArray[0];
        }
        String location1 = TextUtils.isEmpty(json.getString("location")) ? "" : json.getString("location");
        String lat = "",lng="";
        if (!"".equals(location1)){
            lat = location1.split("\\|")[0];
            lng = location1.split("\\|")[1];
        }
        if (redImage != null && !"".equals(redImage) && !redImage.equalsIgnoreCase("null")) {
            iv_ditu.setVisibility(View.VISIBLE);
            Bitmap b2 = BitmapUtils.readBitMap(DynaDetaActivity.this,R.drawable.image_baozang);
            iv_ditu.setImageBitmap(b2);
            mMapView.setVisibility(View.GONE);
            final String biaoshi;
            if ("0".equals(redSum)) {
                rl_geshu.setVisibility(View.GONE);
                iv_bz_type.setVisibility(View.VISIBLE);
                Bitmap b3 = BitmapUtils.readBitMap(DynaDetaActivity.this,R.drawable.baoxiang_open);
                iv_bz_type.setImageBitmap(b3);
                biaoshi = "open";
            } else {
                if ("no".equals(redTime)) {
                    rl_geshu.setVisibility(View.VISIBLE);
                    iv_bz_type.setVisibility(View.GONE);
                    tv_geshu.setText(redSum);
                    biaoshi = "close";
                } else {
                    String nowTime = getNowTime();
                    if (Long.parseLong(nowTime) < Long.parseLong(redTime)) {
                        rl_geshu.setVisibility(View.VISIBLE);
                        iv_bz_type.setVisibility(View.GONE);
                        tv_geshu.setText(redSum);
                        biaoshi = "close";
                    } else {
                        rl_geshu.setVisibility(View.GONE);
                        iv_bz_type.setVisibility(View.GONE);
                        biaoshi = "none";
                    }
                }
            }
            final String finalLat = lat;
            final String finalLng = lng;
            final String finalRedSum = redSum;
            iv_ditu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                        Toast.makeText(getApplicationContext(),"登陆后才可以操作哦！",Toast.LENGTH_SHORT).show();
                    }else {
                        SharedPreferences sp = getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
                        String location = sp.getString("location", null);
                        ScreenshotUtil.getBitmapByView(DynaDetaActivity.this, findViewById(R.id.rl_neirong), "分享名片红包", location, 3, false,0,0);
                        startActivity(new Intent(DynaDetaActivity.this, BaiDuBzLocationActivity.class).putExtra("biaoshi", biaoshi).putExtra("bzlat", finalLat)
                                .putExtra("bzlng", finalLng).putExtra("pintuUrl", redImage).putExtra("createTime", createTime).putExtra("user_id", firstId)
                                .putExtra("dynamicSeq", dynamicSeq).putExtra("gameRed", gameRed).putExtra("name",userID).putExtra("geshu",finalRedSum));
                    }
                }
            });
        } else {
            rl_geshu.setVisibility(View.GONE);
            iv_bz_type.setVisibility(View.GONE);
            iv_ditu.setVisibility(View.GONE);
            if (!"".equals(lat) && !"".equals(lng)) {
                initMapView(lat, lng);
            }
        }
        if (!(avatar == "")) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar,iv_avatar,DemoApplication.mOptions);
        } else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
        }
        tv_count_zhf.setText(forwardTimes);
        tv_nick.setText(userID);
        tv_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,sID));
            }
        });
        tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,sID));
            }
        });
        tv_zhf_content.setText(newContent, 0);
        tv_zhf_content.setVisibility(TextUtils.isEmpty(newContent) ? View.GONE : View.VISIBLE);
        tv_first_nick.setText(firstName+":");
        if (firstImage!=null) {
            String[] orderProjectArray = firstImage.split("\\|");
            firstImage = orderProjectArray[0];
        }
        final String finalFirstImage = firstImage;
        rl_zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage=1;
                loadType = 3;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                initColor(3);
                getJiLuList("转发");
                if (DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                    showZhfDialog(rl_neirong,"0",dynamicSeq,firstId,fromUId,finalFirstImage,firstName,content,false);
                }
            }
        });
        String firstSex = json.getJSONObject("userInfo").getString("uSex");
        if ("00".equals(firstSex)){
            tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
        }else {
            tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }
        if (firstImage!=null) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + firstImage,iv_first_avatar,DemoApplication.mOptions);
        } else {
            iv_first_avatar.setVisibility(View.INVISIBLE);
            tvfirst_TitleA.setVisibility(View.VISIBLE);
            tvfirst_TitleA.setText(TextUtils.isEmpty(firstName) ? firstID : firstName);
        }
        iv_first_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID));
            }
        });
        tv_first_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID));
            }
        });
        tvfirst_TitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID));
            }
        });
        tv_nick.setText(userID);
        final String shareRed = json.getString("shareRed");
        final String friendsNumber = json.getString("friendsNumber");
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            tv_nick.setTextColor(Color.RED);
        }
        tv_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,sID));
            }
        });
        tv_content2.setText(content);
        rel_time = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
        tv_time.setText(rel_time);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
    }

    private void initView7(final JSONObject json) {
        tv_content5 = (TextView) findViewById(R.id.tv_content);
        tv_chujia = (TextView) findViewById(R.id.tv_chujia);
        ll_huikui = (LinearLayout) this.findViewById(R.id.ll_huikui);
        tv_huikui_zonge = (TextView) this.findViewById(R.id.tv_huikui_zonge);
        tv_huikui_yue = (TextView) this.findViewById(R.id.tv_huikui_yue);
        tv_huikui_zhaunfa = (TextView) this.findViewById(R.id.tv_huikui_zhaunfa);
        tv_liulan_cishu = (TextView) this.findViewById(R.id.tv_liulan_cishu);
        tv_count_zhf = (TextView) this.findViewById(R.id.tv_count_zhf);
        tv_location = (TextView) this.findViewById(R.id.tv_location);
        rl_pinglun = (RelativeLayout) this.findViewById(R.id.rl_pinglun);
        btn_xiadan = (Button) this.findViewById(R.id.btn_xiadan);
        iv_pre_click = (ImageView) this.findViewById(R.id.iv_pre_click);
        image_shareRed = (ImageView) this.findViewById(R.id.image_shareRed);
        TextView tv_recommend =(TextView)this.findViewById(R.id.tv_recommend);
        tv_commentCount = (TextView)this.findViewById(R.id.tv_comcount);
        tv_commentTitle = (TextView) this.findViewById(R.id.tv_comment_pl);
        tv_demandType = (TextView) this.findViewById(R.id.tv_demandType);
        Button btn_daohang = (Button) this.findViewById(R.id.btn_daohang);


        RelativeLayout rl_comment = (RelativeLayout) this.findViewById(R.id.rl_comment);
        rl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadType = 1;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                isReply = "no";
                currentContent = "";
                tagId = sID;
                initColor(4);
                getdata();
                showCommentEditText(myuserID, myNick, dynamicSeq);
            }
        });


        String exShareRed="无";
        final String userID = json.getString("uName");
        final String content = json.getString("content");
        final String sex1 = json.getString("uSex");
        final String uloginId = json.getString("uLoginId");
        String location = json.getString("location");
        final String floorPrice = json.getString("floorPrice");
        currentFloorPrice = json.getString("floorPrice");
        String forwardTimes = json.getString("forwardTimes");
        String task_position = json.getString("task_position");
        currentPostion = task_position;
        String task_locaName = json.getString("task_locaName");
        String task_jurisdiction = json.getString("task_jurisdiction");
        String recomend = json.getString("recommendCount");//推荐人数
        String orderComment = json.getString("orderCommentCount");//评论人数
        String demandType = json.getString("demandType");

        if (demandType.equals("1")){

            tv_demandType.setText("需要产品");
            tv_demandType.setBackgroundColor(Color.parseColor("#FF8D00"));

        }else if (demandType.equals("2")){

            tv_demandType.setText("需要方案");
            tv_demandType.setBackgroundColor(Color.parseColor("#46c01b"));

        }else if (demandType.equals("3")){

            tv_demandType.setText("工程招标");
            tv_demandType.setBackgroundColor(Color.parseColor("#FF0000"));

        }else {

            tv_demandType.setText("需要服务");
            tv_demandType.setBackgroundColor(Color.parseColor("#3EC5FF"));

        }

        //标注
        if (task_jurisdiction.equals("01") || task_jurisdiction.equals("02")){
        }else {
            tvCompany.setText("("+task_jurisdiction+")");
            tvCompany.setTextColor(Color.parseColor("#FFA500"));
        }



        if (Integer.valueOf(orderComment) > 0){

            if (Integer.valueOf(orderComment) > 9999){

                Double a = Double.valueOf(orderComment)/10000;

                DecimalFormat myformat=new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);

                orderComment = str+"万";
                tv_commentCount.setText(orderComment);

            }else {

                tv_commentCount.setText(""+Integer.valueOf(orderComment));

            }

        }


        if (countPinglun.equals("0")){

            tv_recommend.setVisibility(View.INVISIBLE);

        }else {

            tv_recommend.setVisibility(View.VISIBLE);

            if (Integer.valueOf(recomend) > 99){
                tv_recommend.setText("报价 99+");
            }else {
                tv_recommend.setText("报价 " + Integer.valueOf(countPinglun));
            }

            tv_recommend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (sID.equals(DemoHelper.getInstance().getCurrentUsernName())){

                        startActivity(new Intent(DynaDetaActivity.this,DynamicRecommendActivity.class).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));

                    }

                 //   startActivity(new Intent(DynaDetaActivity.this,DynamicRecommendActivity.class).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));

                }
            });

        }

        tv_count_pl.setText(recomend);

        if (Integer.valueOf(recomend) > 0){

            tv_title_pl.setText("匹配:");

        }else {
            tv_count_pl.setText("");
            tv_title_pl.setText("匹配中");
        }

        if (floorPrice==null||Double.parseDouble(floorPrice)<=0){
            tv_chujia.setVisibility(View.INVISIBLE);
        }else {

            if (Double.valueOf(floorPrice) > 9999){

                Double a = Double.valueOf(floorPrice)/10000;

                DecimalFormat myformat=new java.text.DecimalFormat("0.0");
                String str = myformat.format(a);
                tv_chujia.setText("意向价： " + str+"万元");

            }else {

                tv_chujia.setText("意向价： " + floorPrice+"元");
            }

        }

        if (task_position != null){
            final String resv2 = task_position.split("\\|")[0];
            final String resv1 = task_position.split("\\|")[1];
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
                tvDistance.setText("定位失败");
            }
        }else {
            tvDistance.setText("定位失败");
        }

        SharedPreferences mSharedPreferences = getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
//        if (!json.getString("uLoginId").equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(dynamicSeq, "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(currentFloorPrice,currentPostion) && (Double.parseDouble(isHaveMargin) < 100 || Double.parseDouble(isHaveMargin) < Double.parseDouble(currentFloorPrice))) {
//
//            Log.d("chen", content + "没有转发过");
//            if ( Double.parseDouble(isHaveMargin) < Double.parseDouble(json.getString("floorPrice")) && Double.parseDouble(isHaveMargin) > 0)
//            {
//                tv_location.setText("质保低于出价 需分享后查看");
//
//            }else
//            {
//                tv_location.setText("无质保 需分享后 才能查看");
//            }
//
//        } else {

            if (task_locaName==null||"".equals(task_locaName)||task_locaName.equalsIgnoreCase("null")){
                tv_location.setText("地点错误，建议主动联系用户沟通");
            }else {
                task_locaName = subString(task_locaName);

                final String lat = task_position.split("\\|")[0];
                final String lng = task_position.split("\\|")[1];
                tvDistance.setVisibility(View.INVISIBLE);
                tv_location.setText(task_locaName+" 距离"+tvDistance.getText().toString().trim());
                tv_location.setTextSize(10);

                tv_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] strLat = new String[]{lat};
                        final String[] strLong = new String[]{lng};
                        final String[] strLoginId = new String[]{uloginId};
                        final String[] strName = new String[]{userID};
                        final String[] strSex = new String[]{sex1};
                        Intent intent = new Intent(DynaDetaActivity.this, BaiDuFLocationActivity.class);
                        intent.putExtra("lat", strLat);
                        intent.putExtra("lng", strLong);
                        intent.putExtra("loginId", strLoginId);
                        intent.putExtra("name", strName);
                        intent.putExtra("sex", strSex);
                        intent.putExtra("biaoshi","导航");
                        startActivity(intent);
                    }
                });
                btn_daohang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String[] strLat = new String[]{lat};
                        final String[] strLong = new String[]{lng};
                        final String[] strLoginId = new String[]{uloginId};
                        final String[] strName = new String[]{userID};
                        final String[] strSex = new String[]{sex1};
                        Intent intent = new Intent(DynaDetaActivity.this, BaiDuFLocationActivity.class);
                        intent.putExtra("lat", strLat);
                        intent.putExtra("lng", strLong);
                        intent.putExtra("loginId", strLoginId);
                        intent.putExtra("name", strName);
                        intent.putExtra("sex", strSex);
                        intent.putExtra("biaoshi","导航");
                        startActivity(intent);
                    }
                });
            }
        //}

        final String sum = json.getString("sum");
        if (sum != null) {
           // ll_huikui.setVisibility(View.VISIBLE);
            double redBalance = Double.valueOf(json.getString("redBalance"));

            if (redBalance > 0){
                image_shareRed.setVisibility(View.VISIBLE);
            }else {
                image_shareRed.setVisibility(View.INVISIBLE);
            }

//            double oncePrice = Double.valueOf(json.getString("oncePrice"));
//            int cishu = (int) ((redBalance*100)/(oncePrice*100));
//            tv_huikui_zonge.setText("￥" + sum);
//            tv_huikui_yue.setText("￥" + redBalance);
//            tv_huikui_zhaunfa.setText("￥" + oncePrice);
//            tv_liulan_cishu.setText(cishu + "次");
        } else {
           // ll_huikui.setVisibility(View.GONE);
            image_shareRed.setVisibility(View.INVISIBLE);
        }

        if (forwardTimes==null||"".equals(forwardTimes)){
            forwardTimes = "0";
        }
        if (forwardTimes!=null&&Integer.valueOf(forwardTimes)>9999){
            Double a = Double.valueOf(forwardTimes)/10000;

            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(a);

            forwardTimes = str+"万";
        }
        final String sID = json.getString("uLoginId");
        final String resv4 = json.getString("resv4");
        // String token = json.getString("token");
        String rel_time = json.getString("createTime");
        String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
        final String task_label = json.getString("task_label");
        orderBodyTask = task_label;
        final String dynamicSeq = json.getString("dynamicSeq");
        final String xiaoliang = TextUtils.isEmpty(json.getString("orderState")) ? "00" : json.getString("orderState");
        final String fromUId = sID;
        tv_nick.setText(userID);
        final String shareRed = json.getString("shareRed");
        final String friendsNumber = json.getString("friendsNumber");
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            tv_nick.setTextColor(Color.RED);
            exShareRed = "有";
        }
        if (avatar!=null&&!avatar.equals("")){
            firstImage = avatar.split("\\|")[0];
        }
        if (!DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
            btn_xiadan.setEnabled(true);
            btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);
            btn_xiadan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(DynaDetaActivity.this, LoginActivity.class));
                }
            });
        }else {

            if (!sID.equals(DemoHelper.getInstance().getCurrentUsernName())) {

                if ("01".equals(xiaoliang) || "02".equals(xiaoliang)) {

                    btn_xiadan.setText("交易完成");
                    btn_xiadan.setEnabled(true);
                    btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_oriange);

                }else if ("03".equals(xiaoliang)){

                    btn_xiadan.setText("派单结束");
                    btn_xiadan.setEnabled(true);
                    btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green_1);

                }else {


                    SharedPreferences sp2 = getSharedPreferences("sangu_dynamicClick_info", Context.MODE_PRIVATE);

                    String dynamicType = sp2.getString("dynamicType","0");

                    String redImage = json.getString("redImage");

                    if ((redImage != null &&  redImage.length() > 7) || dynamicType.equals("02")) {

                        btn_xiadan.setText("投标报价");
                        btn_daohang.setText("订单坐标");

                    }else {

                        btn_xiadan.setText("接单报价");

                    }

                  //  btn_xiadan.setText("接单报价");
                    btn_xiadan.setEnabled(true);
                    btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);

                }
                final String finalRel_time1 = rel_time;
                final String finalRel_time = rel_time;
                final String finalRel_time2 = rel_time;
                final String finalRel_time3 = rel_time;
                final String finalExShareRed = exShareRed;
                btn_xiadan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        UserPermissionUtil.getUserPermission(DynaDetaActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "7", new UserPermissionUtil.UserPermissionListener() {
                            @Override
                            public void onAllow() {

                                if ("01".equals(xiaoliang) || "02".equals(xiaoliang) ||"03".equals(xiaoliang)) {

                                    passivereceiptAlert();

                                }else {

                                    //判断是招标信息还是普通动态
                                    String redImage = json.getString("redImage");

                                    if (redImage != null &&  redImage.length() > 7){

                                        Intent intent = new Intent(DynaDetaActivity.this, DynamicLinkDetailActivity.class);

                                        intent.putExtra("redImage", redImage);

                                        startActivityForResult(intent, 0);

                                    }else {

                                        //先弹出新加的联系方式提示框  然后点击报价在弹出的原来的

                                        LayoutInflater inflaterD6 = LayoutInflater.from(DynaDetaActivity.this);
                                        LinearLayout layout6 = (LinearLayout) inflaterD6.inflate(R.layout.dialog_communication_alert, null);
                                        final Dialog dialog6 = new AlertDialog.Builder(DynaDetaActivity.this, R.style.Dialog).create();
                                        dialog6.show();
                                        dialog6.getWindow().setContentView(layout6);
                                        WindowManager.LayoutParams params2 = dialog6.getWindow().getAttributes() ;
                                        Display display2 = DynaDetaActivity.this.getWindowManager().getDefaultDisplay();
                                        params2.width =(int) (display2.getWidth()*0.7); //使用这种方式更改了dialog的框宽
                                        dialog6.getWindow().setAttributes(params2);
                                        dialog6.setCancelable(true);
                                        dialog6.setCanceledOnTouchOutside(true);

                                        TextView tv_chat1 = (TextView) layout6.findViewById(R.id.tv_chatmode1);
                                        TextView tv_chat2 = (TextView) layout6.findViewById(R.id.tv_chatmode2);
                                        TextView tv_chat3 = (TextView) layout6.findViewById(R.id.tv_chatmode3);
                                        TextView tv_chat4 = (TextView) layout6.findViewById(R.id.tv_chatmode4);

                                        tv_chat1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                dialog6.dismiss();

                                                String type;
                                                if (json.getString("fromUId") != null&&!json.getString("fromUId").equalsIgnoreCase("null")) {
                                                    type = "02";
                                                } else {
                                                    type = "01";
                                                }

                                                Intent intent = new Intent(DynaDetaActivity.this, DynaDetaActivity.class);
                                                intent.putExtra("sID", sID);
                                                intent.putExtra("dynamicSeq",dynamicSeq);
                                                intent.putExtra("createTime",createTime);
                                                intent.putExtra("dType", dType);
                                                intent.putExtra("type", type);
                                                intent.putExtra("type2", "00");
                                                startActivityForResult(intent,0);

                                            }
                                        });

                                        tv_chat2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                dialog6.dismiss();

                                                Intent intent = new Intent(DynaDetaActivity.this, ChatActivity.class);
                                                intent.putExtra(EaseConstant.EXTRA_USER_ID,sID);
                                                intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                                                intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                                            //    intent.putExtra(EaseConstant.EXTRA_USER_IMG, finalFirstImage);
                                                intent.putExtra(EaseConstant.EXTRA_USER_NAME,userID);
                                                intent.putExtra(EaseConstant.EXTRA_USER_SHARERED, finalExShareRed);
                                                startActivity(intent);
                                                //dynamic/insertDynamicDealContact  dynamicSeq=&createTime=&uId=&contactId=&type=
                                               // insertDynamicContact(dynamicSeq, createTime, DemoHelper.getInstance().getCurrentUsernName(), sID, "00");

                                            }
                                        });

                                        tv_chat3.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                dialog6.dismiss();

                                                PermissionUtil permissionUtil = new PermissionUtil((FragmentActivity)DynaDetaActivity.this);
                                                permissionUtil.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                                        new PermissionListener() {
                                                            @Override
                                                            public void onGranted() {
                                                                UserPermissionUtil.getUserPermission(DynaDetaActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "2", new UserPermissionUtil.UserPermissionListener() {
                                                                    @Override
                                                                    public void onAllow() {

                                                                        //所有权限都已经授权
                                                                        if (!"1833710135".equals(sID) && !"1000000".equals(sID) && !"2000000".equals(sID) && !"3000000".equals(sID)) {
                                                                            Intent intent = new Intent();
                                                                            intent.setAction(Intent.ACTION_CALL);
                                                                            //url:统一资源定位符
                                                                            //uri:统一资源标示符（更广）
                                                                            intent.setData(Uri.parse("tel:" + sID));
                                                                            //开启系统拨号器
                                                                            startActivity(intent);
                                                                        } else {
                                                                            Intent intent = new Intent();
                                                                            intent.setAction(Intent.ACTION_CALL);
                                                                            //url:统一资源定位符
                                                                            //uri:统一资源标示符（更广）
                                                                            intent.setData(Uri.parse("tel:" + "13513895563"));
                                                                            //开启系统拨号器
                                                                            startActivity(intent);
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onBan() {
                                                                        ToastUtils.showNOrmalToast(DynaDetaActivity.this, "您的账户已被禁止打电话");

                                                                    }
                                                                });

                                                            }

                                                            @Override
                                                            public void onDenied(List<String> deniedPermission) {
                                                                //Toast第一个被拒绝的权限
                                                                Toast.makeText(DynaDetaActivity.this, "您拒绝了拨打电话的权限！", Toast.LENGTH_SHORT).show();
                                                            }

                                                            @Override
                                                            public void onShouldShowRationale(List<String> deniedPermission) {
                                                                //Toast第一个勾选不在提示的权限
                                                                Toast.makeText(DynaDetaActivity.this, "您拒绝了拨打电话的权限,请前往设置手动打开！", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            }
                                        });

                                        tv_chat4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                dialog6.dismiss();


                                                //跳转新的报价界面 判断是否阅读过规则

                                                final SharedPreferences mSharedPreferences5 = DynaDetaActivity.this.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
                                                String userPromte = mSharedPreferences5.getString("userPromte1","0");

                                                if (userPromte.equals("1")){

                                                    String url = FXConstant.URL_SELECTAllORDERBYID;

                                                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String s) {
                                                            JSONObject object = JSON.parseObject(s);

                                                            JSONArray array = object.getJSONArray("list");

                                                            if (array.size() > 0){

                                                                Toast.makeText(DynaDetaActivity.this,"入账单有未完成订单,禁止报价",Toast.LENGTH_SHORT).show();

                                                            }else {

                                                                //阅读过
                                                                Intent intent = new Intent(DynaDetaActivity.this, NewsOrderDetailActivity.class);

                                                                intent.putExtra("createTime", createTime);
                                                                intent.putExtra("dynamicSeq", dynamicSeq);
                                                                intent.putExtra("task_label", task_label);
                                                                intent.putExtra("task_locaName", json.getString("task_locaName"));
                                                                intent.putExtra("floorPrice", json.getString("floorPrice"));
                                                                intent.putExtra("content", json.getString("content"));
                                                                intent.putExtra("contentImage", json.getString("image1"));
                                                                intent.putExtra("task_position", json.getString("task_position"));
                                                                intent.putExtra("uName", json.getString("uName"));
                                                                intent.putExtra("thridInfo", json.getString("thridInfo"));
                                                                intent.putExtra("typeDetail", "01");
                                                                intent.putExtra("hxid", sID);
                                                                intent.putExtra("biaoshi", "03");
                                                                startActivityForResult(intent, 0);

                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError volleyError) {
                                                            Toast.makeText(DynaDetaActivity.this,"网络不稳定，请稍后重试",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }){
                                                        @Override
                                                        protected Map<String, String> getParams() throws AuthFailureError {
                                                            Map<String, String> param = new HashMap<>();

                                                            param.put("userId",DemoHelper.getInstance().getCurrentUsernName());

                                                            return param;
                                                        }
                                                    };

                                                    MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);


                                                }else {
                                                    //没有阅读过

                                                    LayoutInflater inflaterD7 = LayoutInflater.from(DynaDetaActivity.this);
                                                    LinearLayout layout7 = (LinearLayout) inflaterD7.inflate(R.layout.dialog_orderprocess, null);
                                                    final Dialog dialog7 = new AlertDialog.Builder(DynaDetaActivity.this, R.style.Dialog).create();
                                                    dialog7.show();
                                                    dialog7.getWindow().setContentView(layout7);
                                                    WindowManager.LayoutParams params3 = dialog7.getWindow().getAttributes() ;
                                                    Display display3 = DynaDetaActivity.this.getWindowManager().getDefaultDisplay();
                                                    params3.width =(int) (display3.getWidth()*0.85); //使用这种方式更改了dialog的框宽
                                                    dialog7.getWindow().setAttributes(params3);
                                                    dialog7.setCancelable(true);
                                                    dialog7.setCanceledOnTouchOutside(true);

                                                    TextView tv_midBtn = (TextView) layout7.findViewById(R.id.tv_midBtn);
                                                    TextView tv_soft = (TextView) layout7.findViewById(R.id.tv_softAgreement);
                                                    TextView tv_user = (TextView) layout7.findViewById(R.id.tv_userAgreement);


                                                    tv_soft.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {

                                                            Intent intent = new Intent(DynaDetaActivity.this,SoftAgreementActivity.class);

                                                            startActivity(intent);

                                                        }
                                                    });
                                                    tv_user.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            Intent intent = new Intent(DynaDetaActivity.this,SoftUserAgreementActivity.class);

                                                            startActivity(intent);
                                                        }
                                                    });


                                                    tv_midBtn.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {

                                                            dialog7.dismiss();

                                                            SharedPreferences.Editor editor = mSharedPreferences5.edit();
                                                            if (editor!=null) {
                                                                editor.putString("userPromte1","1");
                                                                editor.commit();
                                                            }

                                                            String url = FXConstant.URL_SELECTAllORDERBYID;

                                                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String s) {
                                                                    JSONObject object = JSON.parseObject(s);

                                                                    JSONArray array = object.getJSONArray("list");

                                                                    if (array.size() > 0){

                                                                        Toast.makeText(DynaDetaActivity.this,"入账单有未完成订单,禁止报价",Toast.LENGTH_SHORT).show();

                                                                    }else {


                                                                        Intent intent = new Intent(DynaDetaActivity.this, NewsOrderDetailActivity.class);

                                                                        intent.putExtra("createTime", createTime);
                                                                        intent.putExtra("dynamicSeq", dynamicSeq);
                                                                        intent.putExtra("task_label", task_label);
                                                                        intent.putExtra("task_locaName", json.getString("task_locaName"));
                                                                        intent.putExtra("floorPrice", json.getString("floorPrice"));
                                                                        intent.putExtra("content", json.getString("content"));
                                                                        intent.putExtra("contentImage", json.getString("image1"));
                                                                        intent.putExtra("task_position", json.getString("task_position"));
                                                                        intent.putExtra("uName", json.getString("uName"));
                                                                        intent.putExtra("thridInfo", json.getString("thridInfo"));
                                                                        intent.putExtra("typeDetail", "01");
                                                                        intent.putExtra("hxid", sID);
                                                                        intent.putExtra("biaoshi", "03");
                                                                        startActivityForResult(intent, 0);

                                                                    }
                                                                }
                                                            }, new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError volleyError) {
                                                                    Toast.makeText(DynaDetaActivity.this,"网络不稳定，请稍后重试",Toast.LENGTH_SHORT).show();
                                                                }
                                                            }){
                                                                @Override
                                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                                    Map<String, String> param = new HashMap<>();

                                                                    param.put("userId",DemoHelper.getInstance().getCurrentUsernName());

                                                                    return param;
                                                                }
                                                            };

                                                            MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);

                                                        }
                                                    });

                                                }

                                            }

                                        });


                                    }

                                }

                            }

                            @Override
                            public void onBan() {
                                ToastUtils.showNOrmalToast(DynaDetaActivity.this, "您的账户已被禁止接单报价,有问题联系客服");
                            }
                        });

                    }
                });

            } else if (sID.equals(DemoHelper.getInstance().getCurrentUsernName()) && DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)) {
                btn_xiadan.setText("查看");
                btn_xiadan.setEnabled(true);
                btn_xiadan.setBackgroundResource(R.drawable.fx_bg_btn_green);
            }
        }
        rl_zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage=1;
                loadType = 3;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                initColor(3);
                getJiLuList("转发");
                if (DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                    paidanZhuanfa = dynamicSeq;
                    showZhfDialog(rl_neirong,"0",dynamicSeq,sID,fromUId,firstImage,userID,content,false);
                }
            }
        });
        firstImage = "";
        if (avatar.length() > 40) {
            String[] orderProjectArray = avatar.split("\\|");
            avatar = orderProjectArray[0];
        }
        if (!(avatar == "")) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar,iv_avatar,DemoApplication.mOptions);
        } else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
        }
        tv_count_zhf.setText(forwardTimes);
        tv_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (("01".equals(xiaoliang) || "02".equals(xiaoliang) ||"03".equals(xiaoliang)) && !sID.equals(DemoHelper.getInstance().getCurrentUsernName())) {

                    passivereceiptAlert();

                }else {
                    //判断是招标信息还是普通动态
                    String redImage = json.getString("redImage");

                    if (redImage != null &&  redImage.length() > 7){

                        Intent intent = new Intent(DynaDetaActivity.this, DynamicLinkDetailActivity.class);

                        intent.putExtra("redImage", redImage);

                        startActivityForResult(intent, 0);

                    }else {
                        startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));
                    }

                }

             }
        });

        tv_content5.setText("              【"+json.getString("task_label")+"】："+content);

        tv_content5.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
       // rel_time = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
       //         + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
        tv_time.setText(rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12));
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (("01".equals(xiaoliang) || "02".equals(xiaoliang) ||"03".equals(xiaoliang)) && !sID.equals(DemoHelper.getInstance().getCurrentUsernName())) {

                    passivereceiptAlert();

                }else {

                    //判断是招标信息还是普通动态
                    String redImage = json.getString("redImage");

                    if (redImage != null &&  redImage.length() > 7){

                        Intent intent = new Intent(DynaDetaActivity.this, DynamicLinkDetailActivity.class);

                        intent.putExtra("redImage", redImage);

                        startActivityForResult(intent, 0);

                    }else {

                        startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));

                    }

                }
            }
        });
        tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (("01".equals(xiaoliang) || "02".equals(xiaoliang) ||"03".equals(xiaoliang)) && !sID.equals(DemoHelper.getInstance().getCurrentUsernName())) {

                    passivereceiptAlert();

                }else {

                    //判断是招标信息还是普通动态
                    String redImage = json.getString("redImage");

                    if (redImage != null &&  redImage.length() > 7){

                        Intent intent = new Intent(DynaDetaActivity.this, DynamicLinkDetailActivity.class);

                        intent.putExtra("redImage", redImage);

                        startActivityForResult(intent, 0);

                    }else {

                        startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));

                    }

                }

            }
        });



//        if (!json.getString("uLoginId").equals(DemoHelper.getInstance().getCurrentUsernName()) && mSharedPreferences.getString(dynamicSeq, "123").equalsIgnoreCase("123") && !ComparePriceAndVipLevel(currentFloorPrice,currentPostion) && (Double.parseDouble(isHaveMargin) < 100 || Double.parseDouble(isHaveMargin) < Double.parseDouble(currentFloorPrice))) {
//
//            Log.d("chen", content + "没有转发过");
//            iv_pre_click.setVisibility(View.VISIBLE);
//            iv_pre_click.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    rl_zhuanfa.performClick();
//                    //String oncePrice = json.getString("oncePrice");
//                    //showZhfDialog(rel_time,null,((SocialMainAdapter.ViewHolderFive) holder).card, finalV,oncePrice,dynamicSeq,sID,fromUId,finalFirstImage,userID,content,false);
//                }
//            });
//
//        } else {
            Log.d("chen", content + "转发过");
            iv_pre_click.setVisibility(View.GONE);
    //    }
    }

    private void passivereceiptAlert(){

        LayoutInflater inflaterD5 = LayoutInflater.from(DynaDetaActivity.this);
        LinearLayout layout5 = (LinearLayout) inflaterD5.inflate(R.layout.dialog_passivereceipt_alert, null);
        final Dialog dialog5 = new AlertDialog.Builder(DynaDetaActivity.this, R.style.Dialog).create();
        dialog5.show();
        dialog5.getWindow().setContentView(layout5);
        WindowManager.LayoutParams params = dialog5.getWindow().getAttributes() ;
        Display display = DynaDetaActivity.this.getWindowManager().getDefaultDisplay();
        params.width =(int) (display.getWidth()*0.75); //使用这种方式更改了dialog的框宽
        dialog5.getWindow().setAttributes(params);
        dialog5.setCancelable(true);
        dialog5.setCanceledOnTouchOutside(true);

        TextView tv_mid = (TextView)layout5.findViewById(R.id.tv_mid);

        tv_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //跳转短信派单
                dialog5.dismiss();
                Intent intent = new Intent(DynaDetaActivity.this, MessageOrderIntroduceActivity.class);

                startActivityForResult(intent, 0);

            }

        });

    }

    private String subString(String task_locaName) {
        String str = task_locaName;
        if (str.contains("区")||str.contains("县")){
            int i2;
            if (str.contains("区")) {
                i2 = task_locaName.indexOf("区");
            } else {
                i2 = task_locaName.indexOf("县");
            }
            if (str.contains("市")){
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    str = task_locaName.substring(i1 + 1, i2 + 1);
                }else {
                    str = task_locaName.substring(0, i2 + 1);
                }
            }else {
                str = task_locaName.substring(0, i2 + 1);
            }
        }else {
            if (str.contains("市")){
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    int i2 = task_locaName.indexOf("市");
                    str = task_locaName.substring(i1 + 1,i2 + 1);
                }else {
                    int i2 = task_locaName.indexOf("市");
                    str = task_locaName.substring(0, i2 + 1);
                }
            }else {
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    str = task_locaName.substring(i1+1,str.length());
                }else {
                    str= task_locaName;
                }
            }
        }
        return str;
    }
    private void initView3(final JSONObject json) {
        tv_content = (ExpandableTextView) findViewById(R.id.tv_content);
        tv_shpjg = (TextView) this.findViewById(R.id.tv_price1);
        tv_shpyj = (TextView) this.findViewById(R.id.tv_price2);
        tv_xiaoliang = (TextView) this.findViewById(R.id.tv_xiaoliang);
        tv_huikui_zonge = (TextView) this.findViewById(R.id.tv_huikui_zonge);
        tv_huikui_yue = (TextView) this.findViewById(R.id.tv_huikui_yue);
        tv_huikui_zhaunfa = (TextView) this.findViewById(R.id.tv_huikui_zhaunfa);
        tv_liulan_cishu = (TextView) this.findViewById(R.id.tv_redcount);
        tv_count_zhf = (TextView) this.findViewById(R.id.tv_count_zhf);
        rl_pinglun = (RelativeLayout) this.findViewById(R.id.rl_pinglun);
        rl_huikui1 = (RelativeLayout) this.findViewById(R.id.rl_huikui1);
        rl_huikui2 = (RelativeLayout) this.findViewById(R.id.rl_huikui2);
        btn_xiadan = (Button) this.findViewById(R.id.bt_left);

        tv_marginLabel = (TextView) this.findViewById(R.id.tv_marginLabel);
        tv_vipLabel = (TextView) this.findViewById(R.id.tv_vipLabel);
        tv_messageLabel = (TextView) this.findViewById(R.id.tv_messageLabel);

        String vip = json.getString("vip");
        String vipLevel = json.getString("vipLevel");
        String messageOrderAll = json.getString("messageOrderAll");

        com.alibaba.fastjson.JSONArray userProfession = json.getJSONArray("userProfessions");

        String proString = "";
        double marginString = 0;
        if (userProfession!=null&&userProfession.size() > 0) {
            String pl1 = userProfession.getJSONObject(0).getString("upName");
            String margin1 = userProfession.getJSONObject(0).getString("margin");
            if (margin1 != null && margin1.length() != 0 && Double.valueOf(margin1) > 99 ){
                marginString = Double.valueOf(margin1);
            }
            if (pl1.equalsIgnoreCase("null")) {
                pl1 = "";
            }
            if (pl1.equals("")){

            }else {
                proString = pl1;
            }
        }
        if (userProfession.size() > 1) {
            String pl2 = userProfession.getJSONObject(1).getString("upName");
            String margin2 = userProfession.getJSONObject(1).getString("margin");
            if (margin2 != null && margin2.length() != 0 &&  Double.valueOf(margin2) > 99 ){
                marginString = marginString + Double.valueOf(margin2);
            }
            if (pl2.equalsIgnoreCase("null")) {
                pl2 = "";
            }
            if (pl2.equals("")){

            }else {
                proString = proString+" "+pl2;
            }
        }
        if (userProfession.size() > 2) {
            String pl3 = userProfession.getJSONObject(2).getString("upName");
            String margin3 = userProfession.getJSONObject(2).getString("margin");
            if (margin3 != null && margin3.length() != 0 && Double.valueOf(margin3) > 99 ){

                marginString = marginString + Double.valueOf(margin3);
            }
            if (pl3.equalsIgnoreCase("null")) {
                pl3 = "";
            }
            if (pl3.equals("")){

            }else {
                proString = proString+" "+pl3;
            }
        }
        if (userProfession.size() > 3) {
            String pl4 = userProfession.getJSONObject(3).getString("upName");
            String margin4 = userProfession.getJSONObject(3).getString("margin");
            if (margin4 != null && margin4.length() != 0 && Double.valueOf(margin4) > 99 ){
                marginString = marginString + Double.valueOf(margin4);
            }
            if (pl4.equalsIgnoreCase("null")) {
                pl4 = "";
            }
            if (pl4.equals("")){

            }else {
                proString = proString+" "+pl4;
            }
        }

        if (marginString>99){

            tv_marginLabel.setVisibility(View.VISIBLE);

            if (marginString > 9999){

                tv_marginLabel.setText("万元级保障商户");

            }else {

                DecimalFormat df = new DecimalFormat("######0");
                String numberStr = df.format(marginString);

                tv_marginLabel.setText("承诺保障"+numberStr);
            }

        }else {

            tv_marginLabel.setVisibility(View.GONE);
        }

        if (Double.valueOf(messageOrderAll) > 0){

            tv_messageLabel.setVisibility(View.VISIBLE);

        }else {
            tv_messageLabel.setVisibility(View.GONE);
        }

        if (Double.valueOf(vip) > 0){

            tv_vipLabel.setText("VIP"+vipLevel);
            tv_vipLabel.setVisibility(View.VISIBLE);

        }else {

            tv_vipLabel.setVisibility(View.GONE);

        }

        ImageView image_redpage = (ImageView) this.findViewById(R.id.image_redpage);
        image_redpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_zhuanfa.performClick();
            }
        });

        Button offLine = (Button) this.findViewById(R.id.bt_right);
        offLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userID = json.getString("uName");
                final String loginId = json.getString("uLoginId");
                final String sex1 = json.getString("uSex");
                final String lat = json.getString("lat");
                final String lng = json.getString("lng");

                if (lat.equals("0") || lng.equals("0")){

                    if (json.getString("resv1") != null && json.getString("resv2") != null){

                        String[] strLat = new String[]{json.getString("resv2")};
                        final String[] strLong = new String[]{json.getString("resv1")};
                        final String[] strLoginId = new String[]{loginId};
                        final String[] strName = new String[]{userID};
                        final String[] strSex = new String[]{sex1};
                        Intent intent = new Intent(DynaDetaActivity.this, BaiDuFLocationActivity.class);
                        intent.putExtra("lat", strLat);
                        intent.putExtra("lng", strLong);
                        intent.putExtra("loginId", strLoginId);
                        intent.putExtra("name", strName);
                        intent.putExtra("sex", strSex);
                        intent.putExtra("biaoshi","导航");
                        DynaDetaActivity.this.startActivity(intent);

                    }else {

                        Toast.makeText(DynaDetaActivity.this,"位置获取失败,建议直接与用户沟通",Toast.LENGTH_SHORT).show();

                    }

                }else {

                    String[] strLat = new String[]{lat};
                    final String[] strLong = new String[]{lng};
                    final String[] strLoginId = new String[]{loginId};
                    final String[] strName = new String[]{userID};
                    final String[] strSex = new String[]{sex1};
                    Intent intent = new Intent(DynaDetaActivity.this, BaiDuFLocationActivity.class);
                    intent.putExtra("lat", strLat);
                    intent.putExtra("lng", strLong);
                    intent.putExtra("loginId", strLoginId);
                    intent.putExtra("name", strName);
                    intent.putExtra("sex", strSex);
                    intent.putExtra("biaoshi","导航");
                    DynaDetaActivity.this.startActivity(intent);

                }
            }
        });

        final String userID = json.getString("uName");
        final String content = json.getString("content");
        String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
        String location = json.getString("location");
        String price = json.getString("price");
        String salePrice = json.getString("salePrice");
        String forwardTimes = json.getString("forwardTimes");
        if (forwardTimes==null||"".equals(forwardTimes)){
            forwardTimes = "0";
        }
        if (forwardTimes!=null&&Integer.valueOf(forwardTimes)>9999){

            Double a = Double.valueOf(forwardTimes)/10000;

            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(a);

            forwardTimes = str+"万";

        }
        if (dType.equals("04")) {
            tv_shpjg.setText("￥"+salePrice);
            tv_shpyj.setText("￥"+price);
        } else if (dType.equals("03")||dType.equals("05")) {
            tv_shpjg.setText(""+new DecimalFormat("0.00").format(Double.parseDouble(price)));

            if (salePrice==null||"".equals(salePrice)){

                tv_shpyj.setText(""+new DecimalFormat("0.00").format(Double.parseDouble(price)));

            }else {

                tv_shpyj.setText(""+new DecimalFormat("0.00").format(Double.parseDouble(salePrice)));
            }

        }
        final String sum = json.getString("sum");
        if (sum != null) {
            double redBalance = Double.valueOf(json.getString("redBalance"));
            double oncePrice = Double.valueOf(json.getString("oncePrice"));
            int cishu = (int) ((redBalance*100)/(oncePrice*100));
            tv_huikui_zonge.setText("￥" + sum);
            tv_huikui_yue.setText("￥" + redBalance);
            tv_huikui_zhaunfa.setText("￥" + oncePrice);
            tv_liulan_cishu.setText(cishu + "");
        } else {
        //    rl_huikui1.setVisibility(View.GONE);
          //  rl_huikui2.setVisibility(View.GONE);
            tv_liulan_cishu.setText("0");
        }
        final String sID = json.getString("uLoginId");
        final String resv4 = TextUtils.isEmpty(json.getString("orderType"))?"01":json.getString("orderType");
        // String token = json.getString("token");
        String rel_time = json.getString("createTime");
        String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
        final String dynamicSeq = json.getString("dynamicSeq");
        final String fromUId = sID;
        String xiaoliang = TextUtils.isEmpty(json.getString("transTimes")) ? "0" : json.getString("transTimes");
        tv_xiaoliang.setText(xiaoliang);
        if (!avatar.equals("")){
            firstImage = avatar.split("\\|")[0];
        }
        rl_zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage=1;
                loadType = 3;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                initColor(3);
                getJiLuList("转发");
                if (DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                    if (sum != null) {
                        String balance = TextUtils.isEmpty(json.getString("redBalance"))?"0":json.getString("redBalance");
                        double redBalance = Double.valueOf(balance);
                        final String oncePrice = json.getString("oncePrice");
                        if (redBalance>0){


                            //查询一下是否被设置了禁止转发红包，必须邀请好友才能解开
                            String url = FXConstant.URL_SEARCH_REDAUTH;
                            StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    Log.d("chen", "onResponse" + s);

                                    try {
//                            org.json.JSONObject object = new org.json.JSONObject(s);
//
//                            String code = object.getString("code");

                                        org.json.JSONObject object = new org.json.JSONObject(s);
                                        String code = object.getString("code");

                                        if (code==null||"".equals(code)||code.equalsIgnoreCase("null")){
                                            //处理null

                                            UserPermissionUtil.getUserPermission(DynaDetaActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "8", new UserPermissionUtil.UserPermissionListener() {
                                                @Override
                                                public void onAllow() {

                                                    PermissionUtil permissionUtil = new PermissionUtil(DynaDetaActivity.this);
                                                    permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                                                                    ,Manifest.permission.READ_PHONE_STATE
                                                                    ,Manifest.permission.ACCESS_WIFI_STATE},
                                                            new PermissionListener() {
                                                                @Override
                                                                public void onGranted() {
                                                                    queryhbzgCount(rl_neirong,oncePrice,dynamicSeq,sID,fromUId,firstImage,userID,content);
                                                                }

                                                                @Override
                                                                public void onDenied(List<String> deniedPermission) {
                                                                    Toast.makeText(DynaDetaActivity.this.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请开启权限", Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onShouldShowRationale(List<String> deniedPermission) {
                                                                    Toast.makeText(DynaDetaActivity.this.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
                                                                    Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                                                                    startActivity(intent);
                                                                }
                                                            });

                                                }

                                                @Override
                                                public void onBan() {
                                                    ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止转发红包");

                                                }
                                            });

                                        }else {
                                            //禁止转了  要求发短信才可以

                                            final LayoutInflater inflater1 = LayoutInflater.from(DynaDetaActivity.this);
                                            RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                                            final Dialog dialog1 = new AlertDialog.Builder(DynaDetaActivity.this, R.style.Dialog).create();
                                            dialog1.show();
                                            dialog1.getWindow().setContentView(layout1);
                                            dialog1.setCanceledOnTouchOutside(true);
                                            dialog1.setCancelable(true);
                                            TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                                            Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                                            final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                                            TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                                            title.setText("温馨提示");
                                            btnOK1.setText("确定");
                                            btnCancel1.setText("取消");
                                            title_tv1.setText("您需要邀请20个用户注册才可以继续赚取红包");
                                            btnCancel1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog1.dismiss();
                                                }
                                            });
                                            btnOK1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog1.dismiss();

                                                    //跳转到通讯录界面邀请好友注册  标识是红包

                                                    if (ContextCompat.checkSelfPermission(DynaDetaActivity.this, Manifest.permission.READ_CONTACTS)
                                                            != PackageManager.PERMISSION_GRANTED) {
                                                        ActivityCompat.requestPermissions(DynaDetaActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                                                    }else {
                                                        Intent intent = new Intent(DynaDetaActivity.this,AddressListActivity.class);

                                                        intent.putExtra("redAuth","yes");

                                                        startActivityForResult(intent,0);
                                                    }
                                                }
                                            });
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    if (volleyError!=null) {
                                        Log.e("hongbao", volleyError.getMessage());
                                        Log.d("chen", "hongbao" + volleyError.getMessage());
                                    }
                                    Toast.makeText(DynaDetaActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> param = new HashMap<>();

                                    param.put("rid",DemoHelper.getInstance().getCurrentUsernName());

                                    return param;
                                }
                            };
                            MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);

                        }else {
                            showZhfDialog(rl_neirong,"0",dynamicSeq,sID,fromUId,firstImage,userID,content,false);
                        }
                    }else {
                        showZhfDialog(rl_neirong,"0",dynamicSeq,sID,fromUId,firstImage,userID,content,false);
                    }
                }
            }
        });
        firstImage = "";
        if (avatar.length() > 40) {
            String[] orderProjectArray = avatar.split("\\|");
            avatar = orderProjectArray[0];
        }
        if (!(avatar == "")) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar,iv_avatar,DemoApplication.mOptions);
        } else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
        }
        tv_count_zhf.setText(forwardTimes);
        tv_nick.setText(userID);
        final String shareRed = json.getString("shareRed");
        final String friendsNumber = json.getString("friendsNumber");
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            tv_nick.setTextColor(Color.RED);
        }
        tv_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
        tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
        String balance = "";
        if (dType.equals("04")) {
            balance = salePrice;
        } else if (dType.equals("03")||dType.equals("05")) {
            balance = price;
        }
        final String finalBalance = balance;
        if (dType.equals("05")){
            if ("00".equals(type2)) {
                if ("0".equals(xiaoliang) && DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)) {
                    btn_xiadan.setText("接单");
                    btn_xiadan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String wodezhanghao = DemoHelper.getInstance().getCurrentUsernName();
                            if (wodezhanghao.equals(sID)){
                                Toast.makeText(DynaDetaActivity.this, "不能接自己的订单!", Toast.LENGTH_LONG).show();
                                return;
                            }
                            new AlertDialog.Builder(DynaDetaActivity.this)
                                    .setTitle("确认")
                                    .setMessage("是否申请接单？")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            final ProgressDialog mProgress = new ProgressDialog(DynaDetaActivity.this);
                                            mProgress.setMessage("正在发送申请...");
                                            mProgress.show();
                                            mProgress.setCancelable(true);
                                            mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    mProgress.dismiss();
                                                }
                                            });
                                            String url = FXConstant.URL_INSERT_APPLYORDER;
                                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    if (mProgress != null && mProgress.isShowing()) {
                                                        mProgress.dismiss();
                                                    }
                                                    JSONObject object = JSONObject.parseObject(s);
                                                    String code = object.getString("code");
                                                    if ("success".equals(code)){
                                                        Toast.makeText(getApplicationContext(), "成功提交申请，等待对方选择", Toast.LENGTH_LONG).show();
                                                    }else {
                                                        Toast.makeText(getApplicationContext(), "您已提交过接单申请，不能重复提交！", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    Toast.makeText(getApplicationContext(),"网络连接错误！",Toast.LENGTH_SHORT).show();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String,String> param = new HashMap<String, String>();
                                                    param.put("applyId",wodezhanghao);
                                                    param.put("dynamicId",dynamicSeq);
                                                    param.put("userId",sID);
                                                    return param;
                                                }
                                            };
                                            MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
                                        }
                                    })
                                    .show();
                        }
                    });
                } else {
                    btn_xiadan.setText("已接单");
                    btn_xiadan.setEnabled(false);
                    btn_xiadan.setBackgroundColor(Color.GRAY);
                }
            }else if ("01".equals(type2)){
                if ("0".equals(xiaoliang) && DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)) {
                    btn_xiadan.setText("查看");
                    final String finalRel_time = rel_time;
                    btn_xiadan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(DynaDetaActivity.this, XuqiuListActivity.class).putExtra("dynamicId", dynamicSeq).putExtra("createTime", finalRel_time));
                        }
                    });
                }else {
                    btn_xiadan.setText("已接单");
                    btn_xiadan.setEnabled(false);
                    btn_xiadan.setBackgroundColor(Color.GRAY);
                }
            }
        }else {
            btn_xiadan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String distance = tvDistance.getText().toString();
                    showDialog1(sID, resv4, content, distance, finalBalance);
                }
            });
        }
        tv_content.setText(content, 0);
        tv_content.setVisibility(TextUtils.isEmpty(content) ? View.GONE : View.VISIBLE);
        rel_time = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
        tv_time.setText(rel_time);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过
                Intent intent = new Intent(DynaDetaActivity.this,AddressListActivity.class);
                intent.putExtra("redAuth","yes");
                startActivityForResult(intent,0);

            } else {  //权限拒绝

                Toast.makeText(DynaDetaActivity.this,"您拒绝了访问通讯录权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void initView8(final JSONObject json) {
        tv_content2 = (TextView) findViewById(R.id.tv_content);
        tv_location = (TextView) this.findViewById(R.id.tv_location);
        tv_first_nick = (TextView) this.findViewById(R.id.tv_first_nick);
        tvfirst_TitleA = (TextView) this.findViewById(R.id.tv_zhf_titl);
        iv_first_avatar = (CircleImageView) this.findViewById(R.id.sdv_first_image);
        tv_zhf_content = (ExpandableTextView) this.findViewById(R.id.tv_zhf_content);
        tv_count_zhf = (TextView) this.findViewById(R.id.tv_count_zhf);
        rl_pinglun = (RelativeLayout) this.findViewById(R.id.rl_pinglun);
        btn_xiadan = (Button) this.findViewById(R.id.btn_xiadan);
        final String userID = json.getString("uName");
        final String content = json.getString("content");
        String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
        String newContent = TextUtils.isEmpty(json.getString("newcontent"))?"":json.getString("newcontent");
        final String firstName = json.getJSONObject("userInfo").getString("uName");
        String firstImage = json.getJSONObject("userInfo").getString("uImage");
        final String firstID = json.getJSONObject("userInfo").getString("uLoginId");
        String location = json.getString("location");
        String forwardTimes = json.getString("forwardTimes");
        String xiaoliang = TextUtils.isEmpty(json.getString("transTimes")) ? "0" : json.getString("transTimes");
        String task_position = json.getString("task_position");
        String task_locaName = json.getString("task_locaName");
        String task_jurisdiction = json.getString("task_jurisdiction");
        tv_location.setText(task_locaName);
        if (forwardTimes==null||"".equals(forwardTimes)){
            forwardTimes = "0";
        }
        if (forwardTimes!=null&&Integer.valueOf(forwardTimes)>9999){
            Double a = Double.valueOf(forwardTimes)/10000;

            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(a);

            forwardTimes = str+"万";

        }
        tv_zhf_content.setText(newContent, 0);
        tv_zhf_content.setVisibility(TextUtils.isEmpty(newContent) ? View.GONE : View.VISIBLE);
        tv_first_nick.setText(firstName+":");
        if (firstImage!=null) {
            String[] orderProjectArray = firstImage.split("\\|");
            firstImage = orderProjectArray[0];
        }
        if (firstImage!=null) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + firstImage,iv_first_avatar,DemoApplication.mOptions);
        } else {
            iv_first_avatar.setVisibility(View.INVISIBLE);
            tvfirst_TitleA.setVisibility(View.VISIBLE);
            tvfirst_TitleA.setText(TextUtils.isEmpty(firstName) ? firstID : firstName);
        }
        iv_first_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));
            }
        });
        tv_first_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));
            }
        });
        tvfirst_TitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));
            }
        });
        final String sID = json.getString("uLoginId");
        final String resv4 = json.getString("resv4");
        // String token = json.getString("token");
        String rel_time = json.getString("createTime");
        String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
        final String dynamicSeq = json.getString("dynamicSeq");
        final String fromUId = sID;
        final String finalFirstImage = firstImage;
        rl_zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage=1;
                loadType = 3;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                initColor(3);
                getJiLuList("转发");
                if (DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                    showZhfDialog(rl_neirong,"0",dynamicSeq,firstID,fromUId,finalFirstImage,firstName,content,false);
                }
            }
        });
        if (avatar.length() > 40) {
            String[] orderProjectArray = avatar.split("\\|");
            avatar = orderProjectArray[0];
        }
        if (!(avatar == "")) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar,iv_avatar,DemoApplication.mOptions);
        } else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
        }
        tv_count_zhf.setText(forwardTimes);
        tv_nick.setText(userID);
        final String shareRed = json.getString("shareRed");
        final String friendsNumber = json.getString("friendsNumber");
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            tv_nick.setTextColor(Color.RED);
        }
        tv_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));
            }
        });
        tv_content2.setText(content);
        rel_time = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
        tv_time.setText(rel_time);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));
            }
        });
        tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID).putExtra("dynamicSeq",dynamicSeq).putExtra("createTime",createTime));
            }
        });
    }
    private void initView4(final JSONObject json) {
        tv_content2 = (TextView) findViewById(R.id.tv_content2);
        tv_first_nick = (TextView) this.findViewById(R.id.tv_first_nick);
        tvfirst_TitleA = (TextView) this.findViewById(R.id.tv_zhf_titl);
        iv_first_avatar = (CircleImageView) this.findViewById(R.id.sdv_first_image);
        tv_zhf_content = (ExpandableTextView) this.findViewById(R.id.tv_zhf_content);
        tv_xiaoliang = (TextView) this.findViewById(R.id.tv_xiaoliang);
        tv_shpjg = (TextView) this.findViewById(R.id.tv_shpjg);
        tv_shpyj = (TextView) this.findViewById(R.id.tv_shpyj);
        tv_huikui_zonge = (TextView) this.findViewById(R.id.tv_huikui_zonge);
        tv_huikui_yue = (TextView) this.findViewById(R.id.tv_huikui_yue);
        tv_huikui_zhaunfa = (TextView) this.findViewById(R.id.tv_huikui_zhaunfa);
        tv_liulan_cishu = (TextView) this.findViewById(R.id.tv_liulan_cishu);
        tv_count_zhf = (TextView) this.findViewById(R.id.tv_count_zhf);
        rl_pinglun = (RelativeLayout) this.findViewById(R.id.rl_pinglun);
        rl_huikui1 = (RelativeLayout) this.findViewById(R.id.rl_huikui1);
        rl_huikui2 = (RelativeLayout) this.findViewById(R.id.rl_huikui2);
        btn_xiadan = (Button) this.findViewById(R.id.btn_xiadan);
        final String userID = json.getString("uName");
        final String content = json.getString("content");
        String imageStr = TextUtils.isEmpty(json.getString("image1")) ? "" : json.getString("image1");
        String newContent = TextUtils.isEmpty(json.getString("newcontent"))?"":json.getString("newcontent");
        final String firstName = json.getJSONObject("userInfo").getString("uName");
        String firstImage = json.getJSONObject("userInfo").getString("uImage");
        final String firstID = json.getJSONObject("userInfo").getString("uLoginId");
        String location = json.getString("location");
        String price = json.getString("price");
        String salePrice = json.getString("salePrice");
        String forwardTimes = json.getString("forwardTimes");
        String xiaoliang = TextUtils.isEmpty(json.getString("transTimes")) ? "0" : json.getString("transTimes");
        tv_xiaoliang.setText(xiaoliang);
        if (forwardTimes==null||"".equals(forwardTimes)){
            forwardTimes = "0";
        }
        if (forwardTimes!=null&&Integer.valueOf(forwardTimes)>9999){
            Double a = Double.valueOf(forwardTimes)/10000;

            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
            String str = myformat.format(a);

            forwardTimes = str+"万";
        }
        if (dType.equals("04")) {
            tv_shpjg.setText("￥"+salePrice);
            tv_shpyj.setText("￥"+price);
        } else if (dType.equals("03")||dType.equals("05")) {
            tv_shpjg.setText("￥"+price);
        }
        tv_zhf_content.setText(newContent, 0);
        tv_zhf_content.setVisibility(TextUtils.isEmpty(newContent) ? View.GONE : View.VISIBLE);
        tv_first_nick.setText(firstName+":");
        if (firstImage!=null) {
            String[] orderProjectArray = firstImage.split("\\|");
            firstImage = orderProjectArray[0];
        }
        String firstSex = json.getJSONObject("userInfo").getString("uSex");
        if ("00".equals(firstSex)){
            tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
        }else {
            tvfirst_TitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }
        if (firstImage!=null) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + firstImage,iv_first_avatar,DemoApplication.mOptions);
        } else {
            iv_first_avatar.setVisibility(View.INVISIBLE);
            tvfirst_TitleA.setVisibility(View.VISIBLE);
            tvfirst_TitleA.setText(TextUtils.isEmpty(firstName) ? firstID : firstName);
        }
        iv_first_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID));
            }
        });
        tv_first_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID));
            }
        });
        tvfirst_TitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,firstID));
            }
        });
        final String sum = json.getString("sum");
        if (sum != null) {
            String balance = TextUtils.isEmpty(json.getString("redBalance"))?"0":json.getString("redBalance");
            double redBalance = Double.valueOf(balance);
            double oncePrice = Double.valueOf(json.getString("oncePrice"));
            int cishu = (int) ((redBalance*100)/(oncePrice*100));
            tv_huikui_zonge.setText("￥" + sum);
            tv_huikui_yue.setText("￥" + redBalance);
            tv_huikui_zhaunfa.setText("￥" + oncePrice);
            tv_liulan_cishu.setText(cishu + "次");
        } else {
            rl_huikui1.setVisibility(View.GONE);
            rl_huikui2.setVisibility(View.GONE);
        }
        final String sID = json.getString("uLoginId");
        final String resv4 = json.getString("resv4");
        // String token = json.getString("token");
        String rel_time = json.getString("createTime");
        String avatar = TextUtils.isEmpty(json.getString("uImage")) ? "" : json.getString("uImage");
        final String dynamicSeq = json.getString("dynamicSeq");
        final String fromUId = sID;
        final String finalFirstImage = firstImage;
        rl_zhuanfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage=1;
                loadType = 3;
                if (datas!=null&&datas.size()>0){
                    datas.clear();
                }
                if (mProgress!=null){
                    mProgress.setMessage("正在加载数据...");
                    mProgress.show();
                }
                initColor(3);
                getJiLuList("转发");
                if (DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                    if (sum != null) {
                        String balance = TextUtils.isEmpty(json.getString("redBalance"))?"0":json.getString("redBalance");
                        double redBalance = Double.valueOf(balance);
                        final String oncePrice = json.getString("oncePrice");
                        if (redBalance>0){


                            //查询一下是否被设置了禁止转发红包，必须邀请好友才能解开
                            String url = FXConstant.URL_SEARCH_REDAUTH;
                            StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    Log.d("chen", "onResponse" + s);

                                    try {
//                            org.json.JSONObject object = new org.json.JSONObject(s);
//
//                            String code = object.getString("code");

                                        org.json.JSONObject object = new org.json.JSONObject(s);
                                        String code = object.getString("code");

                                        if (code==null||"".equals(code)||code.equalsIgnoreCase("null")){
                                            //处理null

                                            UserPermissionUtil.getUserPermission(DynaDetaActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "8", new UserPermissionUtil.UserPermissionListener() {
                                                @Override
                                                public void onAllow() {


                                                }

                                                @Override
                                                public void onBan() {
                                                    ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止转发红包");

                                                }
                                            });

                                            PermissionUtil permissionUtil = new PermissionUtil(DynaDetaActivity.this);
                                            permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION
                                                            ,Manifest.permission.READ_PHONE_STATE
                                                            ,Manifest.permission.ACCESS_WIFI_STATE},
                                                    new PermissionListener() {
                                                        @Override
                                                        public void onGranted() {
                                                            queryhbzgCount(rl_neirong,oncePrice,dynamicSeq,firstID,fromUId,finalFirstImage,firstName,content);
                                                        }

                                                        @Override
                                                        public void onDenied(List<String> deniedPermission) {
                                                            Toast.makeText(DynaDetaActivity.this.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onShouldShowRationale(List<String> deniedPermission) {
                                                            Toast.makeText(DynaDetaActivity.this.getApplicationContext(), "您拒绝了获取定位的权限！无法赚取红包 请手动开启权限", Toast.LENGTH_SHORT).show();
                                                            Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                                                            startActivity(intent);
                                                        }
                                                    });

                                        }else {
                                            //禁止转了  要求发短信才可以

                                            final LayoutInflater inflater1 = LayoutInflater.from(DynaDetaActivity.this);
                                            RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                                            final Dialog dialog1 = new AlertDialog.Builder(DynaDetaActivity.this, R.style.Dialog).create();
                                            dialog1.show();
                                            dialog1.getWindow().setContentView(layout1);
                                            dialog1.setCanceledOnTouchOutside(true);
                                            dialog1.setCancelable(true);
                                            TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                                            Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                                            final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                                            TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                                            title.setText("温馨提示");
                                            btnOK1.setText("确定");
                                            btnCancel1.setText("取消");
                                            title_tv1.setText("您需要邀请20个用户注册才可以继续赚取红包");
                                            btnCancel1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog1.dismiss();
                                                }
                                            });
                                            btnOK1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog1.dismiss();

                                                    //跳转到通讯录界面邀请好友注册  标识是红包

                                                    if (ContextCompat.checkSelfPermission(DynaDetaActivity.this, Manifest.permission.READ_CONTACTS)
                                                            != PackageManager.PERMISSION_GRANTED) {
                                                        ActivityCompat.requestPermissions(DynaDetaActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                                                    }else {
                                                        Intent intent = new Intent(DynaDetaActivity.this,AddressListActivity.class);

                                                        intent.putExtra("redAuth","yes");

                                                        startActivityForResult(intent,0);
                                                    }
                                                }
                                            });
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                    if (volleyError!=null) {
                                        Log.e("hongbao", volleyError.getMessage());
                                        Log.d("chen", "hongbao" + volleyError.getMessage());
                                    }
                                    Toast.makeText(DynaDetaActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> param = new HashMap<>();

                                    param.put("rid",DemoHelper.getInstance().getCurrentUsernName());

                                    return param;
                                }
                            };
                            MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);

                        }else {
                            showZhfDialog(rl_neirong,"0",dynamicSeq,firstID,fromUId,finalFirstImage,firstName,content,false);
                        }

                    }else {
                        showZhfDialog(rl_neirong,"0",dynamicSeq,firstID,fromUId,finalFirstImage,firstName,content,false);
                    }
                }
            }
        });
        if (avatar.length() > 40) {
            String[] orderProjectArray = avatar.split("\\|");
            avatar = orderProjectArray[0];
        }
        if (!(avatar == "")) {
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + avatar,iv_avatar,DemoApplication.mOptions);
        } else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(TextUtils.isEmpty(userID) ? sID : userID);
        }
        tv_count_zhf.setText(forwardTimes);
        tv_nick.setText(userID);
        final String shareRed = json.getString("shareRed");
        final String friendsNumber = json.getString("friendsNumber");
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            tv_nick.setTextColor(Color.RED);
        }
        tv_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
        String balance = "";
        if (dType.equals("04")) {
            balance = salePrice;
        } else if (dType.equals("03")||dType.equals("05")) {
            balance = price;
        }
        final String finalBalance = balance;
        if (dType.equals("05")){
            if ("00".equals(type2)) {
                if ("0".equals(xiaoliang) && DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)) {
                    btn_xiadan.setText("接单");
                    btn_xiadan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String wodezhanghao = DemoHelper.getInstance().getCurrentUsernName();
                            if (wodezhanghao.equals(firstID)){
                                Toast.makeText(DynaDetaActivity.this, "不能接自己的订单!", Toast.LENGTH_LONG).show();
                                return;
                            }
                            new AlertDialog.Builder(DynaDetaActivity.this)
                                    .setTitle("确认")
                                    .setMessage("是否申请接单？")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            final ProgressDialog mProgress = new ProgressDialog(DynaDetaActivity.this);
                                            mProgress.setMessage("正在发送申请...");
                                            mProgress.show();
                                            mProgress.setCancelable(true);
                                            mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    mProgress.dismiss();
                                                }
                                            });
                                            String url = FXConstant.URL_INSERT_APPLYORDER;
                                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    if (mProgress != null && mProgress.isShowing()) {
                                                        mProgress.dismiss();
                                                    }
                                                    JSONObject object = JSONObject.parseObject(s);
                                                    String code = object.getString("code");
                                                    if ("success".equals(code)){
                                                        Toast.makeText(getApplicationContext(), "成功提交申请，等待对方选择", Toast.LENGTH_LONG).show();
                                                    }else {
                                                        Toast.makeText(getApplicationContext(), "您已提交过接单申请，不能重复提交！", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {

                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String,String> param = new HashMap<String, String>();
                                                    param.put("applyId",wodezhanghao);
                                                    param.put("dynamicId",dynamicSeq);
                                                    param.put("userId",firstID);
                                                    return param;
                                                }
                                            };
                                            MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
                                        }
                                    })
                                    .show();
                        }
                    });
                } else {
                    btn_xiadan.setText("已接单");
                    btn_xiadan.setEnabled(false);
                    btn_xiadan.setBackgroundColor(Color.GRAY);
                }
            }else if ("01".equals(type2)){
                if ("0".equals(xiaoliang) && DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)) {
                    btn_xiadan.setText("查看");
                    final String finalRel_time = rel_time;
                    btn_xiadan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(DynaDetaActivity.this, XuqiuListActivity.class).putExtra("dynamicId", dynamicSeq).putExtra("createTime", finalRel_time));
                        }
                    });
                }else {
                    btn_xiadan.setText("已接单");
                    btn_xiadan.setEnabled(false);
                    btn_xiadan.setBackgroundColor(Color.GRAY);
                }
            }
        }else {
            btn_xiadan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String distance = tvDistance.getText().toString();
                    showDialog1(sID, resv4, content, distance, finalBalance);
                }
            });
        }
        tv_content2.setText(content);
        rel_time = rel_time.substring(4, 6) + "-" + rel_time.substring(6, 8) + " "
                + rel_time.substring(8, 10) + ":" + rel_time.substring(10, 12);
        tv_time.setText(rel_time);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
        tvTitleA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DynaDetaActivity.this, UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, sID));
            }
        });
    }

    private void queryhbzgCount(final RelativeLayout rl_neirong, final String oncePrice, final String dynamicSeq, final String firstID, final String fromUId, final String finalFirstImage, final String firstName, final String content) {

        String url = FXConstant.URL_Get_UserInfo+DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                JSONObject userInfo = object.getJSONObject("userInfo");
                String dynamicTimes = userInfo.getString("dynamicTimes");
                final String score = userInfo.getString("score");
                final String withdrawals = DemoApplication.getInstance().getCurrentWithdrawals();
                if (dynamicTimes==null||"".equals(dynamicTimes)||Double.parseDouble(dynamicTimes)==0){
                    LayoutInflater inflater2 = LayoutInflater.from(DynaDetaActivity.this);
                    RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog2 = new AlertDialog.Builder(DynaDetaActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout2);
                    dialog2.setCanceledOnTouchOutside(true);
                    dialog2.setCancelable(true);
                    TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                    Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                    final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                    TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                    if ("提现".equals(withdrawals)&&"否".equals(score)) {
                        title2.setText("温馨提示");
                        btnOK2.setText("前去评分");
                        btnCancel2.setText("下次再说");
                        title_tv2.setText("您的红包次数已用完,前去应用市场为软件评分,即可永久获得一次分享次数！");
                    }else {
                        title2.setText("温馨提示");
                        btnOK2.setText("增加次数");
                        btnCancel2.setText("取消操作");
                        title_tv2.setText("您的红包次数已用完！");
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
                            if ("提现".equals(withdrawals)&&"否".equals(score)) {
                                try {
                                    Uri uri = Uri.parse("market://details?id="
                                            + getPackageName());//需要评分的APP包名
                                    Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                                    intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivityForResult(intent5,1);
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "跳转失败,请下载应用宝之后评分", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                startActivity(new Intent(DynaDetaActivity.this, HbHuoQuActivity.class));
                            }

                        }
                    });
                }else {
                    showZhfDialog(rl_neirong,oncePrice, dynamicSeq,firstID,fromUId,finalFirstImage,firstName,content,true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                updateHbTimes();
                updateScore();
                break;
        }
    }

    private void updateScore() {
        String url = FXConstant.URL_UPDATE_TIME;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                Map<String,String> params = new HashMap<String, String>();
                params.put("uLoginId",DemoHelper.getInstance().getCurrentUsernName());
                params.put("score","是");
                return params;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void updateHbTimes() {
        String url = FXConstant.URL_XIUGAI_HBTIMES;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getApplicationContext(),"红包次数增加成功！",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("shareTimes", "1");
                param.put("homePageTimes", "1");
                param.put("dynamicTimes", "1");
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void showDialog1(final String sID,final String resv4,final String zy1,final String distance,final String balance) {
        final String[] typeDetail = new String[1];
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        View v2 = dialog.findViewById(R.id.v2);
        v2.setVisibility(View.GONE);
        re_item2.setVisibility(View.GONE);
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDetail[0] = "01";
                addToDingdan(DemoHelper.getInstance().getCurrentUsernName(),sID,"动态订单", typeDetail[0],resv4,distance,balance);
                dialog.dismiss();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDetail[0] = "02";
                addToDingdan(DemoHelper.getInstance().getCurrentUsernName(),sID,"动态订单", typeDetail[0],resv4,distance,balance);
                dialog.dismiss();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

//    private void setviews(boolean isExpand, final TextView tv_content, TextView expandView) {
//        tv_content.clearAnimation();
//        final int deltaValue;
//        final int startValue = tv_content.getHeight();
//        int durationMillis = 350;
//        if (isExpand) {
//            deltaValue = tv_content.getLineHeight() * tv_content.getLineCount() - startValue;
//            RotateAnimation animation = new RotateAnimation(0, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            animation.setDuration(durationMillis);
//            animation.setFillAfter(true);
//            expandView.startAnimation(animation);
//            expandView.setText("收起");
//        } else {
//            deltaValue = tv_content.getLineHeight() * maxDescripLine - startValue;
//            RotateAnimation animation = new RotateAnimation(0, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            animation.setDuration(durationMillis);
//            animation.setFillAfter(true);
//            expandView.startAnimation(animation);
//            expandView.setText("全文");
//        }
//        Animation animation = new Animation() {
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                tv_content.setHeight((int) (startValue + deltaValue * interpolatedTime));
//            }
//
//        };
//        animation.setDuration(durationMillis);
//        tv_content.startAnimation(animation);
//    }

    private void addToDingdan(final String wodezhanghao, final String hxid, final String zy1 , final String typeDetail, String zyType, final String distance,final String balance) {
        String pass = DemoApplication.getApp().getCurrentPayPass();
        zyType = TextUtils.isEmpty(zyType)?"":zyType;
        if (wodezhanghao.equals(hxid)) {
            Toast.makeText(this, "不能给自己下单！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (zyType.equals("01")) {
            Intent intent = new Intent(this, UOrderDetailActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "01");
            intent.putExtra("balance",balance);
            startActivity(intent);
        }else if (zyType.equals("02")){
            Intent intent = new Intent(this, UOrderDetailTwoActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("distance", distance);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "01");
            intent.putExtra("balance",balance);
            startActivity(intent);
        }else if (zyType.equals("03")){
            Intent intent = new Intent(this, UOrderDetailThreeActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "01");
            intent.putExtra("balance",balance);
            startActivity(intent);
        }else if (zyType.equals("04")){
            Intent intent = new Intent(this, UOrderDetailFourActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "01");
            intent.putExtra("balance",balance);
            startActivity(intent);
        }else if (zyType.equals("05")){
            Intent intent = new Intent(this, UOrderDetailFiveActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", zy1);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", "01");
            intent.putExtra("balance",balance);
            startActivity(intent);
        }
    }

    /**
     * 显示发表评论的输入框
     */
    public void showCommentEditText(final String sID, final String sName,final String dynamicId) {

        re_edittext2 = (LinearLayout) findViewById(R.id.re_edittext2);
        rl_type = (LinearLayout) findViewById(R.id.rl_type);
        rl_send = (RelativeLayout) findViewById(R.id.rl_send);

        re_edittext2.setVisibility(View.VISIBLE);
        rl_type.setVisibility(View.VISIBLE);
        rl_send.setVisibility(View.VISIBLE);

       // re_edittext = (RelativeLayout)findViewById(R.id.re_edittext);
      //  re_edittext.setVisibility(View.VISIBLE);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DemoHelper.getInstance().isLoggedIn(DynaDetaActivity.this)){
                    Toast.makeText(DynaDetaActivity.this,"登陆后才可以评论哦！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String comment = et_comment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    Toast.makeText(DynaDetaActivity.this, "请输入评论", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (commentType.equals("no") && !isReply.equals("yes")){
                    Toast.makeText(DynaDetaActivity.this, "请选择一个标签", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (mProgress!=null&&!mProgress.isShowing()){
                    mProgress.setMessage("正在提交...");
                    mProgress.show();
                }
                submitComment(sID, comment, sName, dynamicId,createTime);
                et_comment.setText("");
            }
        });

        tv_commentType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                commentType = "0";
                tv_commentType1.setBackgroundColor(Color.parseColor("#00acff"));
                tv_commentType2.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType3.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType4.setBackgroundColor(Color.parseColor("#ffbebebe"));

            }
        });

        tv_commentType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentType = "1";
                tv_commentType1.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType2.setBackgroundColor(Color.parseColor("#FF3E4A"));
                tv_commentType3.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType4.setBackgroundColor(Color.parseColor("#ffbebebe"));
            }
        });

        tv_commentType3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentType = "2";
                tv_commentType1.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType2.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType3.setBackgroundColor(Color.parseColor("#46c01b"));
                tv_commentType4.setBackgroundColor(Color.parseColor("#ffbebebe"));
            }
        });

        tv_commentType4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentType = "4";
                tv_commentType1.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType2.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType3.setBackgroundColor(Color.parseColor("#ffbebebe"));
                tv_commentType4.setBackgroundColor(Color.parseColor("#FF00FF"));
            }
        });

    }

    private void addPinglinCount(){


        String url = FXConstant.URL_ADD_PINGLUN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("DynadateActivity","评论增加成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("DynadateActivity","评论增加失败"+volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("createTime",createTime);
                param.put("dynamicSeq",dynamicSeq);
                if (dType.equals("05")){
                    param.put("type","1");
                }
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }
    private void reducePinglinCount(){
        String url = FXConstant.URL_REDUCE_PINGLUN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                countPinglun = Integer.parseInt(countPinglun)-1+"";
                if (countPinglun==null||"".equals(countPinglun)){
                    countPinglun = "0";
                }
                if (countPinglun!=null&&Integer.valueOf(countPinglun)>9999){
                    Double a = Double.valueOf(countPinglun)/10000;

                    DecimalFormat myformat=new java.text.DecimalFormat("0.0");
                    String str = myformat.format(a);

                    countPinglun = str+"万";
                }
                tv_count_pl.setText(countPinglun);
                Log.e("DynadateActivity","评论减少成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("DynadateActivity","评论减少失败"+volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("createTime",createTime);
                param.put("dynamicSeq",dynamicSeq);
                if (dType.equals("05")){
                    param.put("type","1");
                }
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    /**
     * 提交评论
     */
    private void submitComment(final String sID, final String comment, String sName, final String dynamicId, final String creatTime) {
        // 更新后台
        String url = FXConstant.URL_INSERT_DYNACOMMENT;
        final String finalSName = sName;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSONObject.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){

                    if (!tagId.equals(DemoHelper.getInstance().getCurrentUsernName())){
                        sendPushMessage(DynaDetaActivity.this.sID,"评论");
                    }

                    addPinglinCount();
                    Toast.makeText(DynaDetaActivity.this,"评论成功！",Toast.LENGTH_SHORT).show();

                    if (dType.equals("05")){
                        countPinglun = Integer.parseInt(countPinglun)+1+"";
                        if (countPinglun==null||"".equals(countPinglun)){
                            countPinglun = "0";
                        }
                        if (countPinglun!=null&&Integer.valueOf(countPinglun)>9999){
                            Double a = Double.valueOf(countPinglun)/10000;

                            DecimalFormat myformat=new java.text.DecimalFormat("0.0");
                            String str = myformat.format(a);

                            countPinglun = str+"万";
                        }
                        tv_count_pl.setText(countPinglun);
                    }

                    if (datas!=null&&datas.size()>0){
                        datas.clear();
                    }

                    isReply = "no";
                    currentContent = "";
                    tagId = sID;
                    getdata();

                }else {
                    Toast.makeText(DynaDetaActivity.this,"网络繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
                    Log.e("socialMainAdapter",s);
                }
                if (mProgress!=null&&mProgress.isShowing()){
                    mProgress.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgress!=null&&mProgress.isShowing()){
                    mProgress.dismiss();
                }
                Toast.makeText(DynaDetaActivity.this,"网络繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("userId",sID);
                param.put("dynamicId",dynamicId);
                param.put("createTime",createTime);

                if (isReply.equals("yes")){
                    param.put("content",comment+currentContent);
                }else {
                    param.put("content",comment);
                }

                param.put("userName", finalSName);
                param.put("tagId", tagId);
                if (!isReply.equals("yes")){
                    param.put("type", commentType);
                }else {
                    param.put("type", "3");
                }

                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    private void sendPushMessage(final String hxid1,final String type) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
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

                if (type.equals("浏览")){

                    param.put("body","您的动态被浏览了"+dynamicViews+"次");
                    param.put("type","20");

                }else  if (type.equals("转发")){

                    param.put("body","有用户分享了您的动态");
                    param.put("type","21");

                }else {
                    param.put("body","评论消息");
                    param.put("type","09");
                }

                if (tagId.length()>1){
                    param.put("u_id",tagId);
                }else {
                    param.put("u_id",hxid1);
                }

                param.put("userId",myId);
                param.put("companyId", "0");
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq",dynamicSeq);
                param.put("createTime",createTime);
                param.put("fristId",firstid);
                param.put("dType",dType);
                return param;
            }
        };
        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);
    }

    class ImageListener implements View.OnClickListener {
        String[] images;
        int page;

        public ImageListener(String[] images, int page) {
            this.images = images;
            this.page = page;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), BigImageActivity.class);
            intent.putExtra("images", images);
            intent.putExtra("page", page);
            intent.putExtra("biaoshi","13");
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView!=null){
            mMapView.onResume();
        }
        if (isNext) {
            getDynaDetail();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView!=null){
            mMapView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView!=null){
            mMapView.onDestroy();
        }
        if (datas!=null){
            datas.clear();
            datas=null;
        }
        if (mProgress!=null){
            if (mProgress.isShowing())
                mProgress.dismiss();
            mProgress=null;
        }
    }

    private String getDistance(String latS, String lngS) {
        String paidanLat = latS;
        String paidanLng = lngS;
        //4.9E-324  考虑到百度定位出错时 就不显示
        if (!(TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) || TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) || TextUtils.isEmpty(paidanLat) || TextUtils.isEmpty(paidanLng) ||
                paidanLat.equalsIgnoreCase("4.9E-324") || paidanLng.equalsIgnoreCase("4.9E-324"))) {
            double latitude1 = Double.valueOf(DemoApplication.getInstance().getCurrentLat());
            double longitude1 = Double.valueOf(DemoApplication.getInstance().getCurrentLng());
            final LatLng ll1 = new LatLng(Double.parseDouble(paidanLat), Double.parseDouble(paidanLng));
            LatLng ll = new LatLng(latitude1, longitude1);
            double distance = DistanceUtil.getDistance(ll, ll1);
            double dou = distance / 1000;
            String str = String.format("%.1f", dou);//format 返回的是字符串
            return str;
        } else {
            return null;
        }
    }

    private void GetNoticeInfo(){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");

                String type = object1.getString("type");

                if (type.equals("1")){

                    isAdvert = "1";
                    String image1 = object1.getString("image2");
                    String image2 = object1.getString("image3");
                    String image3 = object1.getString("image4");

                    advertImageUrl = image1+"|"+image2+"|"+image3;

                }else {
                    isAdvert = "0";
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
                param.put("deviceType","android13");
                return param;
            }

        };

        MySingleton.getInstance(DynaDetaActivity.this).addToRequestQueue(request);

    }

}
