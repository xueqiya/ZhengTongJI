package com.sangu.apptongji.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
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
import com.fanxin.easeui.EaseConstant;
import com.fanxin.easeui.adapter.EaseMessageAdapter;
import com.fanxin.easeui.controller.EaseUI;
import com.fanxin.easeui.ui.EaseChatFragment;
import com.fanxin.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.fanxin.easeui.widget.EaseChatInputMenu;
import com.fanxin.easeui.widget.EaseChatMessageList;
import com.fanxin.easeui.widget.chatrow.EaseChatRow;
import com.fanxin.easeui.widget.chatrow.EaseChatRowVoice;
import com.fanxin.easeui.widget.chatrow.EaseChatRowVoicePlayClickListener;
import com.fanxin.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.fanxin.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.PathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.DemoModel;
import com.sangu.apptongji.R;
import com.sangu.apptongji.domain.EmojiconExampleGroupData;
import com.sangu.apptongji.domain.RobotUser;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.AddFriendsPreActivity;
import com.sangu.apptongji.main.activity.ChatSettingSingleActivity;
import com.sangu.apptongji.main.activity.MessageOrderIntroduceActivity;
import com.sangu.apptongji.main.activity.ScanCaptureActivity;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuQiyeLocationActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFiveActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFourActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailTwoActivity;
import com.sangu.apptongji.main.chatheadimage.StringUtils;
import com.sangu.apptongji.main.moments.MomentsPublishActivity;
import com.sangu.apptongji.main.qiye.QiYeDetailsActivity;
import com.sangu.apptongji.main.qiye.QiYeYuGoActivity;
import com.sangu.apptongji.main.utils.AnimationUtil;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.RecordPlayer;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.utils.SendVoiceUtils;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.utils.PreferenceManager;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.sangu.apptongji.widget.ChatRowVoiceCall;
import com.sangu.apptongji.widget.CircleImageView;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper,OnGetRoutePlanResultListener{
    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;
    //private static final int ITEM_RED_PACKET = 16;
    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;

//    private static final int MESSAGE_TYPE_RECV_MONEY = 5;
//    private static final int MESSAGE_TYPE_SEND_MONEY = 6;
//    private static final int MESSAGE_TYPE_SEND_LUCKY = 7;
//    private static final int MESSAGE_TYPE_RECV_LUCKY = 8;

    private String lng="",lat="",locationState = "01",latMyself,lngMyself,toChatImg,toChatName,shareRed;
    private MapView mMapView= null;
    public MyLocationListenner myListener = new MyLocationListenner();
    LocationClient mLocClient = null;
    LocationClientOption option = null;
    BitmapDescriptor mCurrentMarker= null;
    Marker marOther;
    private BaiduMap mBaiduMap= null;
    private UiSettings mUiSettings= null;
    private RoutePlanSearch mSearch=null;
    boolean isFirstLoc = true; // 是否首次定位
    private boolean isMylocaclicked = false;
    public boolean isChange = false;
    private ImageButton btn_fabu= null;
    private DemoModel settingsModel= null;
    private BroadcastReceiver intentReceiver;
    private List<Marker> list;
    private List<String> strLoginId;
    View v,otherView,vi2;
    private TextView tvmaj1 = null, tvmaj2 = null, tvmaj3 = null, tvmaj4 = null, tv_titl = null, tv_zy1_bao = null, tv_zy2_bao = null, tv_zy3_bao = null, tv_distance,
            tv_zy4_bao = null, tvName_v2 = null, iv_zy1_tupian = null, iv_zy2_tupian = null, iv_zy3_tupian = null, iv_zy4_tupian = null, tvsign = null, tvAge = null;
    private TextView tv_company = null, tv_company_count = null;
    private Button btnXq = null, btnDh = null;
    private CircleImageView ivAvatar = null;
    private ImageView iv_mylocation,ivSex;

    /**
     * if it is chatBot
     */
    private boolean isRobot;
    private TextView tvName= null;
    EaseChatInputMenu input_menu;
    private RelativeLayout rl_list,rl_voice,rl_luyin,rl_head,rl_shouqi;
    private RelativeLayout rl_bottom,rl_butvoice,rl_receive;
    CircleImageView iv_avatar1=null;
    CircleImageView iv_avatar2=null;
    CircleImageView iv_avatar3=null;
    CircleImageView iv_avatar4=null;
    CircleImageView iv_avatar5=null;
    EaseChatMessageList message_list;
    private CircleImageView iv_receive,iv_send;
    private TextView tv_title_receive,tv_title_send,tv_send_name,tv_receive_name;
    private TextView tv_luyin,tv1,tv2,tv3,tv4,tv5,tv_quxiao,tv_shiting;
    private ImageView iv_shouqi;
    private boolean isLuYin = false,isShiTing = false,isShowMap = false;
    SendVoiceUtils utils;
    private RecordPlayer player;
    String filePath=null;
    boolean isfirst = true;
    boolean isFirst = true;
    int length;
    int mtype;

    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i!=0) {
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) tv1.getLayoutParams();
                params1.height = ScreenshotUtil.dip2px(getActivity(), 40 + i);
                tv1.setLayoutParams(params1);
                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) tv2.getLayoutParams();
                params2.height = ScreenshotUtil.dip2px(getActivity(), 25 + i);
                tv2.setLayoutParams(params2);
                RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) tv3.getLayoutParams();
                params3.height = ScreenshotUtil.dip2px(getActivity(), 15 + i);
                tv3.setLayoutParams(params3);
                RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) tv4.getLayoutParams();
                params4.height = ScreenshotUtil.dip2px(getActivity(), 25 + i);
                tv4.setLayoutParams(params4);
                RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) tv5.getLayoutParams();
                params5.height = ScreenshotUtil.dip2px(getActivity(), 40 + i);
                tv5.setLayoutParams(params5);
                isfirst = true;
            }else if (i==0&&isfirst){
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) tv1.getLayoutParams();
                params1.height = ScreenshotUtil.dip2px(getActivity(), 40);
                tv1.setLayoutParams(params1);
                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) tv2.getLayoutParams();
                params2.height = ScreenshotUtil.dip2px(getActivity(), 25);
                tv2.setLayoutParams(params2);
                RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) tv3.getLayoutParams();
                params3.height = ScreenshotUtil.dip2px(getActivity(), 15);
                tv3.setLayoutParams(params3);
                RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) tv4.getLayoutParams();
                params4.height = ScreenshotUtil.dip2px(getActivity(), 25);
                tv4.setLayoutParams(params4);
                RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) tv5.getLayoutParams();
                params5.height = ScreenshotUtil.dip2px(getActivity(), 40);
                tv5.setLayoutParams(params5);
                isfirst = false;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate( R.layout.fx_fragment_chat, container, false);
        vi2 = LayoutInflater.from(getActivity()).inflate(R.layout.item_find_fragments, null);
        initViews();
        UserPermissionUtil.getUserPermission(getActivity(), DemoHelper.getInstance().getCurrentUsernName(), "1", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {

            }

            @Override
            public void onBan() {
                input_menu.setVisibility(View.INVISIBLE);
              //  rl_bottom.setVisibility(View.INVISIBLE);
                ToastUtils.showNOrmalToast(getActivity().getApplicationContext(), "您的账户已被禁止聊天");
            }

        });
        RequestQueue queue = MySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        settingsModel = DemoHelper.getInstance().getModel();
        return v;
    }

    private void initViews() {
        iv_mylocation = (ImageView) v.findViewById(R.id.iv_mylocation);
        tv_title_receive = (TextView) v.findViewById(R.id.tv_title_receive);
        tv_title_send = (TextView) v.findViewById(R.id.tv_title_send);
        tv_send_name = (TextView) v.findViewById(R.id.tv_send_name);
        tv_receive_name = (TextView) v.findViewById(R.id.tv_receive_name);
        tv_luyin = (TextView) v.findViewById(R.id.tv_luyin);
        tv_shiting = (TextView) v.findViewById(R.id.tv_shiting);
        tv_quxiao = (TextView) v.findViewById(R.id.tv_quxiao);
        tv1 = (TextView) v.findViewById(R.id.tv1);
        tv2 = (TextView) v.findViewById(R.id.tv2);
        tv3 = (TextView) v.findViewById(R.id.tv3);
        tv4 = (TextView) v.findViewById(R.id.tv4);
        tv5 = (TextView) v.findViewById(R.id.tv5);
        iv_receive = (CircleImageView) v.findViewById(R.id.iv_receive);
        iv_send = (CircleImageView) v.findViewById(R.id.iv_send);
        input_menu = (EaseChatInputMenu) v.findViewById(R.id.input_menu);
        message_list = (EaseChatMessageList) v.findViewById(R.id.message_list);
        rl_list = (RelativeLayout) v.findViewById(R.id.rl_list);
        rl_voice = (RelativeLayout) v.findViewById(R.id.rl_voice);
        rl_head = (RelativeLayout) v.findViewById(R.id.rl_head);
        rl_shouqi = (RelativeLayout) v.findViewById(R.id.rl_shouqi);
        rl_luyin = (RelativeLayout) v.findViewById(R.id.rl_luyin);
        rl_bottom = (RelativeLayout) v.findViewById(R.id.rl_bottom);
        rl_butvoice = (RelativeLayout) v.findViewById(R.id.rl_butvoice);
        rl_receive = (RelativeLayout) v.findViewById(R.id.rl_receive);
        iv_shouqi = (ImageView) v.findViewById(R.id.iv_shouqi);

        tvAge = (TextView) vi2.findViewById(R.id.tv_nianling);
        tv_distance = (TextView) vi2.findViewById(R.id.tv_distance);
        tv_company = (TextView) vi2.findViewById(R.id.tv_company);
        tv_company_count = (TextView) vi2.findViewById(R.id.tv_company_count);
        ivSex = (ImageView) vi2.findViewById(R.id.iv_sex);
        tvmaj1 = (TextView) vi2.findViewById(R.id.tv_project_one);
        tvmaj2 = (TextView) vi2.findViewById(R.id.tv_project_two);
        tvmaj3 = (TextView) vi2.findViewById(R.id.tv_project_three);
        tvmaj4 = (TextView) vi2.findViewById(R.id.tv_project_four);
        tv_zy1_bao = (TextView) vi2.findViewById(R.id.tv_zy1_bao);
        tv_zy2_bao = (TextView) vi2.findViewById(R.id.tv_zy2_bao);
        tv_zy3_bao = (TextView) vi2.findViewById(R.id.tv_zy3_bao);
        tv_zy4_bao = (TextView) vi2.findViewById(R.id.tv_zy4_bao);
        tvName_v2 = (TextView) vi2.findViewById(R.id.tv_name);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list = new ArrayList<>();
        strLoginId = new ArrayList<>();
        locationState = DemoApplication.getInstance().getCurrentlocationState();
        latMyself = DemoApplication.getInstance().getCurrentLat();
        lngMyself = DemoApplication.getInstance().getCurrentLng();
        UserPermissionUtil.getUserPermission(getActivity(), DemoHelper.getInstance().getCurrentUsernName(), "1", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                myhandler.postDelayed(mapRunnable, 100);
            }

            @Override
            public void onBan() {
                //ToastUtils.showNOrmalToast(getActivity().getApplicationContext(), "您的账户已被禁止打聊天");

            }
        });
        initView1();
    }

    private void registerIntentReceiver() {
        intentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String type = intent.getStringExtra("types");
                String flg = intent.getStringExtra("flg");
                String mingPianId = intent.getStringExtra("mingPianId");
                String userId = intent.getStringExtra("userId");
                String merId = intent.getStringExtra("merId");
                if ("名片".equals(type)) {
                    if (mingPianId != null && mingPianId.length() > 12) {
                        startActivity(new Intent(getActivity(), QiYeDetailsActivity.class).putExtra("qiyeId", mingPianId));
                    } else if (mingPianId != null) {
                        startActivity(new Intent(getActivity(), UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID, mingPianId));
                    }
                }else if ("编辑订单".equals(type)){
                    if ("01".equals(flg)) {
                        startActivity(new Intent(getActivity(), UOrderDetailActivity.class).putExtra("biaoshi","05").putExtra("userId",userId)
                                .putExtra("merId",merId).putExtra("orderId",mingPianId));
                    }else if ("02".equals(flg)){
                        startActivity(new Intent(getActivity(), UOrderDetailTwoActivity.class).putExtra("biaoshi","05").putExtra("userId",userId)
                                .putExtra("merId",merId).putExtra("orderId",mingPianId));
                    }else if ("04".equals(flg)){
                        startActivity(new Intent(getActivity(), UOrderDetailFourActivity.class).putExtra("biaoshi","05").putExtra("userId",userId)
                                .putExtra("merId",merId).putExtra("orderId",mingPianId));
                    }else {
                        startActivity(new Intent(getActivity(), UOrderDetailFiveActivity.class).putExtra("biaoshi","05").putExtra("userId",userId)
                                .putExtra("merId",merId).putExtra("orderId",mingPianId));
                    }
                }else if ("已编辑".equals(type)){
                    if ("01".equals(flg)) {
                        startActivity(new Intent(getActivity(), UOrderDetailActivity.class).putExtra("biaoshi","06").putExtra("userId",userId)
                                .putExtra("merId",merId).putExtra("orderId",mingPianId));
                    }else if ("02".equals(flg)){
                        startActivity(new Intent(getActivity(), UOrderDetailTwoActivity.class).putExtra("biaoshi","06").putExtra("userId",userId)
                                .putExtra("merId",merId).putExtra("orderId",mingPianId));
                    }else if ("04".equals(flg)){
                        startActivity(new Intent(getActivity(), UOrderDetailFourActivity.class).putExtra("biaoshi","06").putExtra("userId",userId)
                                .putExtra("merId",merId).putExtra("orderId",mingPianId));
                    }else {
                        startActivity(new Intent(getActivity(), UOrderDetailFiveActivity.class).putExtra("biaoshi","06").putExtra("userId",userId)
                                .putExtra("merId",merId).putExtra("orderId",mingPianId));
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter("com.tiaozhuan");
        getActivity().registerReceiver(intentReceiver, filter);
    }

    private void initView1() {
        iv_mylocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_mylocation.setImageResource(R.drawable.gps_mylocation);
                isMylocaclicked = true;
                showPianyi(false);
            }
        });
        rl_shouqi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float y1 = 0,y2;
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    //当手指按下的时候
                    y1 = event.getY();
                }
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    //当手指离开的时候
                    y2 = event.getY();
                    if(y1 - y2 > 10) {
                        filePath = null;
                        length = 0;
                        if (mLocClient!=null) {
                            mLocClient.unRegisterLocationListener(myListener);
                            mLocClient.stop();
                        }
                        if (player!=null&&player.isPlaying()) {
                            player.stopPalyer();
                        }
                        if (utils!=null) {
                            utils.discardRecording();
                        }
                        iv_mylocation.setVisibility(View.INVISIBLE);
                        rl_luyin.setVisibility(View.INVISIBLE);
                        rl_head.setVisibility(View.VISIBLE);
                        rl_voice.setVisibility(View.INVISIBLE);
                        tv_luyin.setVisibility(View.VISIBLE);
                        isfirst = true;
                        isLuYin = false;
                        isShiTing = false;
                        isShowMap = false;
                        showPianyi(true);
                        rl_list.setVisibility(View.VISIBLE);
                     //   rl_bottom.setVisibility(View.INVISIBLE);
                        rl_shouqi.setVisibility(View.INVISIBLE);
                        input_menu.setVisibility(View.VISIBLE);
                        message_list.setVisibility(View.VISIBLE);
                        rl_list.setAnimation(AnimationUtil.moveToViewLocation());
                        message_list.setAnimation(AnimationUtil.moveToViewLocation());
                        rl_bottom.setAnimation(AnimationUtil.moveToViewBottom());
                        rl_shouqi.setAnimation(AnimationUtil.moveToViewBottom());
                        if (myhandler!=null&&receivelocaRunnable!=null){
                            myhandler.removeCallbacks(receivelocaRunnable);
                            myhandler.removeCallbacks(sendlocaRunnable);
                        }
                        if (myhandler!=null&&receiveAllLocaRunnable!=null){
                            leaveCarTeam();
                            myhandler.removeCallbacks(receiveAllLocaRunnable);
                            myhandler.removeCallbacks(sendlocaRunnable);
                        }
                    }
                }
                return true;
            }
        });
        iv_shouqi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                filePath = null;
                length = 0;
                if (mLocClient!=null) {
                    mLocClient.unRegisterLocationListener(myListener);
                    mLocClient.stop();
                }
                if (player!=null&&player.isPlaying()) {
                    player.stopPalyer();
                }
                if (utils!=null) {
                    utils.discardRecording();
                }
                iv_mylocation.setVisibility(View.INVISIBLE);
                rl_luyin.setVisibility(View.INVISIBLE);
                rl_head.setVisibility(View.VISIBLE);
                rl_voice.setVisibility(View.INVISIBLE);
                tv_luyin.setVisibility(View.VISIBLE);
                isfirst = true;
                isLuYin = false;
                isShiTing = false;
                isShowMap = false;
                showPianyi(true);
                rl_list.setVisibility(View.VISIBLE);
             //   rl_bottom.setVisibility(View.INVISIBLE);
                rl_shouqi.setVisibility(View.INVISIBLE);
                input_menu.setVisibility(View.VISIBLE);
                message_list.setVisibility(View.VISIBLE);
                rl_list.setAnimation(AnimationUtil.moveToViewLocation());
                message_list.setAnimation(AnimationUtil.moveToViewLocation());
                rl_bottom.setAnimation(AnimationUtil.moveToViewBottom());
                rl_shouqi.setAnimation(AnimationUtil.moveToViewBottom());
                if (myhandler!=null&&receivelocaRunnable!=null){
                    myhandler.removeCallbacks(receivelocaRunnable);
                    myhandler.removeCallbacks(sendlocaRunnable);
                }
                if (myhandler!=null&&receiveAllLocaRunnable!=null){
                    leaveCarTeam();
                    myhandler.removeCallbacks(receiveAllLocaRunnable);
                    myhandler.removeCallbacks(sendlocaRunnable);
                }
            }
        });
        rl_butvoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLuYin){
                    //在录音，点击发送录音，并改变按钮状态和隐藏试听
                    if (isShiTing){
                        if (player!=null&&player.isPlaying()) {
                            player.stopPalyer();
                        }
                        if (filePath != null) {
                            EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
                            sendMessage(message);
                            filePath = null;
                            isShiTing = false;
                            isLuYin = false;
                        }
                    }else {
                        try {
                            length = utils.stopRecoding();
                            if (length > 0) {
                                filePath = utils.getVoiceFilePath();
                                if (filePath != null) {
                                    EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
                                    sendMessage(message);
                                    filePath = null;
                                }
                            } else if (length == EMError.FILE_INVALID) {
                                Toast.makeText(getActivity(), com.hyphenate.easeui.R.string.Recording_without_permission, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), com.hyphenate.easeui.R.string.The_recording_time_is_too_short, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), com.hyphenate.easeui.R.string.send_failure_please, Toast.LENGTH_SHORT).show();
                        }
                    }
                    rl_luyin.setVisibility(View.INVISIBLE);
                    rl_head.setVisibility(View.VISIBLE);
                    rl_voice.setVisibility(View.INVISIBLE);
                    tv_luyin.setVisibility(View.VISIBLE);
                    isLuYin = false;
                }else {
                    //没在录音,点击关闭播放，开始录音，显示录音动画和试听
                    filePath = null;
                    length = 0;
                    if (EaseChatRowVoicePlayClickListener.isPlaying)
                        EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
                    utils.startRecording();
                    rl_luyin.setVisibility(View.VISIBLE);
                    rl_head.setVisibility(View.INVISIBLE);
                    rl_voice.setVisibility(View.VISIBLE);
                    tv_luyin.setVisibility(View.INVISIBLE);
                    isLuYin = true;
                    isShiTing = false;
                }
            }
        });
        tv_quxiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击取消，停止录音，删除录音文件，改变录音状态，隐藏试听
                isfirst = true;
                filePath = null;
                length = 0;
                if (player.isPlaying()) {
                    player.stopPalyer();
                }
                utils.discardRecording();
                rl_luyin.setVisibility(View.INVISIBLE);
                rl_head.setVisibility(View.VISIBLE);
                rl_voice.setVisibility(View.INVISIBLE);
                tv_luyin.setVisibility(View.VISIBLE);
                isLuYin = false;
                isShiTing = false;
            }
        });
        tv_shiting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isShiTing = true;
                isfirst = true;
                //点击试听，停止录音，播放本地录音文件
                if (filePath==null) {
                    try {
                        length = utils.stopRecoding();
                        if (length > 0) {
                            filePath = utils.getVoiceFilePath();
                            if (filePath != null && new File(filePath).exists()) {
                                player.playRecordFile(new File(filePath));
                            }
                        } else if (length == EMError.FILE_INVALID) {
                            Toast.makeText(getActivity(), com.hyphenate.easeui.R.string.Recording_without_permission, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), com.hyphenate.easeui.R.string.The_recording_time_is_too_short, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    if (filePath != null && new File(filePath).exists()) {
                        player.playRecordFile(new File(filePath));
                    }
                }
            }
        });

        btn_fabu = (ImageButton) getView().findViewById(R.id.btn_fabu);
        btn_fabu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final String payPass = DemoApplication.getInstance().getCurrentPayPass();
                LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.fx_popupwindow_robit, null);
                final Dialog dialog = new AlertDialog.Builder(getActivity(),R.style.Dialog).create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.setCanceledOnTouchOutside(true);
                RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
                RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                RelativeLayout re_item6 = (RelativeLayout) dialog.findViewById(R.id.re_item6);

                TextView tv1 = (TextView) dialog.findViewById(R.id.tv1);
                TextView tv2 = (TextView) dialog.findViewById(R.id.tv2);
                final TextView tv3 = (TextView) dialog.findViewById(R.id.tv3);
                TextView tv4 = (TextView) dialog.findViewById(R.id.tv4);
                TextView tv5 = (TextView) dialog.findViewById(R.id.tv5);
                tv1.setText("扫   一   扫");
                tv4.setText("发   动   态");
                tv5.setText("搜 索 好 友");
                if ("00".equals(locationState)) {
                    tv3.setText("位 置 开 启");
                } else {
                    tv3.setText("位 置 关 闭");
                }
                if (settingsModel.getSettingMsgSound()) {
                    tv2.setText("声 音 关 闭");
                } else {
                    tv2.setText("声 音 开 启");
                }
                re_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(), ScanCaptureActivity.class).putExtra("payPass", payPass));
                    }
                });
                re_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        showDialog();
                    }
                });
                re_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(),AddFriendsPreActivity.class));
                    }
                });
                re_item4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (settingsModel.getSettingMsgSound()) {
                            settingsModel.setSettingMsgSound(false);
                        } else {
                            settingsModel.setSettingMsgSound(true);
                        }
                    }
                });
                re_item5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if ("01".equals(locationState)) {
                            weizhi("下班", "", "00");
                            locationState = "00";
                            tv3.setText("位置开启");
                        } else {
                            weizhi("上班", "", "00");
                            locationState = "01";
                            tv3.setText("位置关闭");
                        }
                    }
                });

                re_item6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                        startActivity(new Intent(getActivity(), MessageOrderIntroduceActivity.class));

                    }
                });
            }
        });
    }
    private void weizhi(final String shangbanzt,final String text,final String biaoshi) {
        String url = FXConstant.URL_UPDATE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getActivity(), "操作成功",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                if ("上班".equals(shangbanzt)) {
                    param.put("locationState", "01");
                }else {
                    param.put("locationState", "00");
                }
                return param;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    private void showDialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(getActivity());
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_buttom2_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(getActivity(),R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
        RelativeLayout re_item6 = (RelativeLayout) dialog.findViewById(R.id.re_item6);
        re_item4.setVisibility(View.GONE);
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(),MomentsPublishActivity.class);
                intent1.putExtra("biaoshi","shenghuo");
                startActivityForResult(intent1,0);
                dialog.dismiss();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(),MomentsPublishActivity.class);
                intent2.putExtra("biaoshi","xinwen");
                startActivityForResult(intent2,0);
                dialog.dismiss();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getActivity(),MomentsPublishActivity.class);
                intent3.putExtra("biaoshi","shangye");
                startActivityForResult(intent3,0);
                dialog.dismiss();
            }
        });
        re_item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(getActivity(),MomentsPublishActivity.class);
                intent4.putExtra("biaoshi","youhui");
                startActivityForResult(intent4,0);
                dialog.dismiss();
            }
        });
        re_item6.setVisibility(View.VISIBLE);
        re_item6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(getActivity(),MomentsPublishActivity.class);
                intent6.putExtra("biaoshi","xuqiu");
                startActivityForResult(intent6,0);
                dialog.dismiss();
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(getActivity().getApplicationContext(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR && lat != null && lng != null && ChatActivity.activityInstance.isHideLocation() == false) {
            mBaiduMap.clear();
            DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            showMarker(latMyself,lngMyself,0);
            showMarker(lat,lng,mtype);
            if (isFirstLoc){
                overlay.zoomToSpan();
                showPianyi(true);
                isFirstLoc = false;
            }
        }
    }

    private void showPianyi(boolean b) {
        if (latMyself==null||lngMyself==null){
            return;
        }
        double pianyi=0;
        if (b){
            pianyi = jisuanPianyi();
            Log.e("chatfrag,py",pianyi+"");
        }
        double cenLat = Double.parseDouble(latMyself)-pianyi;
        double cenLng = Double.parseDouble(lngMyself);
        LatLng latLng = new LatLng(cenLat,cenLng);
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(latLng);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();
        float zoom = mBaiduMap.getMapStatus().zoom;
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng,zoom);
        mBaiduMap.animateMapStatus(u);
    }

    Runnable sendlocaRunnable = new Runnable() {
        @Override
        public void run() {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());
            final String date = sDateFormat.format(curDate);
            String url = FXConstant.URL_UPDATE_TIME;
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    myhandler.postDelayed(sendlocaRunnable, 3000);
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
                    params.put("loginTime",date);
                    if (lngMyself!=null) {
                        params.put("lng", lngMyself);
                    }
                    if (latMyself!=null) {
                        params.put("lat", latMyself);
                    }
                    return params;
                }
            };
            MySingleton.getInstance(getActivity()).addToRequestQueue(request);
        }
    };

    Runnable receivelocaRunnable = new Runnable() {
        @Override
        public void run() {
            String url = FXConstant.URL_RECEIVE_LOCAL+toChatUsername;
            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    JSONObject jsonObject = JSON.parseObject(s);
                    JSONObject object = jsonObject.getJSONObject("list");
                    String receLat = object.getString("resv2");
                    String receLng = object.getString("resv1");
                    if (receLat!=null&&!"".equals(receLat)){
                        lat = receLat;
                    }
                    if (receLng!=null&&!"".equals(receLng)){
                        lng = receLng;
                    }
                    myhandler.postDelayed(receivelocaRunnable, 3000);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            MySingleton.getInstance(getActivity()).addToRequestQueue(request);
        }
    };

    private static void Remove(List<Marker> list){
        if (list != null) {
            for (Marker marker : list) {
                marker.remove();
            }
        }
    }

    public void allLatLng(JSONArray jsonArray){
        try{
            if (list.size() != 0){
                Remove(list);
                list.clear();
                strLoginId.clear();
            }
            for (int i = 0;i<jsonArray.size();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String resv1 = jsonObject.getString("resv1");
                String resv2 = jsonObject.getString("resv2");
                String u_name = jsonObject.getString("u_name");
                String u_id = jsonObject.getString("u_id");
                if (resv1!=null&&!"".equals(resv1)) {
                    lat = resv2;
                    lng = resv1;
                    LatLng p = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    CoordinateConverter converter = new CoordinateConverter();
                    converter.coord(p);
                    converter.from(CoordinateConverter.CoordType.COMMON);
                    LatLng latLng = converter.convert();
                    if (u_id.equals(DemoHelper.getInstance().getCurrentUsernName())){
                        Log.e("chatfrag,","添加自己");
                        marOther = (Marker) mBaiduMap.addOverlay(help_add_icon(latLng, null, R.drawable.icon_geo));
                    }else {
                        Log.e("chatfrag,","添加他人");
                        View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_map_user, null);
                        v.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        TextView tvTitle = (TextView) v.findViewById(R.id.tv_name);
                        tvTitle.setText(u_name);
                        marOther = (Marker) mBaiduMap.addOverlay(help_add_icon(latLng, v, -1));
                    }
                    list.add(marOther);
                    strLoginId.add(u_id);
                }
            }
        }catch (Exception e){

        }
    }

    private static MarkerOptions help_add_icon(LatLng latLng,View view,int id){
        MarkerOptions markOptiopns ;
        if (id==-1) {
            markOptiopns = new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromView(view));
        }else {
            markOptiopns = new MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(id));
        }
        return markOptiopns;
    }

    Runnable receiveAllLocaRunnable = new Runnable() {
        @Override
        public void run() {
//            String url = FXConstant.URL_SELECT_MEMBERLIST;
//            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String s) {
//                    JSONObject jsonObject = JSON.parseObject(s);
//                    JSONArray array = jsonObject.getJSONArray("list");
//                    allLatLng(array);
//                    myhandler.postDelayed(receiveAllLocaRunnable, 3000);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError volleyError) {
//
//                }
//            }){
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String,String> params = new HashMap<>();
//                    params.put("groupId",toChatUsername);
//                    return params;
//                }
//            };
//            MySingleton.getInstance(getActivity()).addToRequestQueue(request);
            String url = FXConstant.URL_RECEIVE_ALLLOCAL+toChatUsername;
            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    JSONObject jsonObject = JSON.parseObject(s);
                    JSONArray array = jsonObject.getJSONArray("list");
                    allLatLng(array);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            MySingleton.getInstance(getActivity()).addToRequestQueue(request);
            myhandler.postDelayed(this, 3000);
        }
    };

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            latMyself = String.valueOf(location.getLatitude());
            lngMyself = String.valueOf(location.getLongitude());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if ("单聊".equals(type)) {
                if (latMyself != null && !"".equals(latMyself) && lat != null && !"".equals(lat) && ChatActivity.activityInstance.isHideLocation() == false) {
                    LatLng p1 = new LatLng(Double.valueOf(latMyself), Double.valueOf(lngMyself));
                    LatLng p2 = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                    CoordinateConverter converter1 = new CoordinateConverter();
                    converter1.coord(p1);
                    converter1.from(CoordinateConverter.CoordType.COMMON);
                    LatLng convertLatLng1 = converter1.convert();
                    CoordinateConverter converter2 = new CoordinateConverter();
                    converter2.coord(p2);
                    converter2.from(CoordinateConverter.CoordType.COMMON);
                    LatLng convertLatLng2 = converter2.convert();
                    PlanNode stNode = PlanNode.withLocation(convertLatLng1);
                    PlanNode enNode = PlanNode.withLocation(convertLatLng2);
                    mSearch.drivingSearch((new DrivingRoutePlanOption())
                            .from(stNode).to(enNode));
                }
            }
        }
    }

    Runnable mapRunnable = new Runnable() {
        @Override
        public void run() {

            if (getView() == null) {
                return;
            }
            WeakReference<ChatFragment> reference =  new WeakReference<ChatFragment>(ChatFragment.this);
            mMapView = (MapView) getView().findViewById(R.id.bmapView1);
            mSearch = RoutePlanSearch.newInstance();
            mSearch.setOnGetRoutePlanResultListener(reference.get());
            mBaiduMap = mMapView.getMap();
            mUiSettings = mBaiduMap.getUiSettings();
            mUiSettings.setAllGesturesEnabled(true);
            mLocClient = new LocationClient(reference.get().getActivity());
            option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            option.setCoorType("gcj02"); // 设置坐标类型
            option.setScanSpan(3000);
            mLocClient.setLocOption(option);
            mBaiduMap.clear();
            mMapView.clearFocus();
            //设置是否显示比例尺控件
            mMapView.showScaleControl(false);
            //设置是否显示缩放控件
            mMapView.showZoomControls(false);
            View child = mMapView.getChildAt(1);
            if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
                child.setVisibility(View.INVISIBLE);
            }
            if (ChatActivity.activityInstance!=null) {
                lng = ChatActivity.activityInstance.getResv1();
                lat = ChatActivity.activityInstance.getResv2();
            }
            if (type==null||"".equals(type)||"0".equals(type)){
                initmapClick1(false);
                if (ChatActivity.activityInstance!=null) {
                    initmapClick2();
                }
            }else {
                if ("单聊".equals(type)){
                    if (ChatActivity.activityInstance!=null) {
                        initmapClick2();
                    }
                }else if ("企业".equals(type)){
                    initmapClick1(true);
                }else if ("群组".equals(type)){
                    Log.e("chatfrag,","群组");
                    if (ChatActivity.activityInstance!=null) {
                        initmapClick2();
                    }
                }
            }
            Userful user = DemoApplication.getInstance().getCurrentUser();
            String image2 = user.getImage();
            String name2 = user.getName();
            String myId = user.getLoginId();
            if (image2!=null&&!"".equals(image2)){
                image2 = image2.split("\\|")[0];
                iv_send.setVisibility(View.VISIBLE);
                tv_title_send.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+image2,iv_send, DemoApplication.mOptions);
            }else {
                iv_send.setVisibility(View.INVISIBLE);
                tv_title_send.setVisibility(View.VISIBLE);
                tv_title_send.setText(TextUtils.isEmpty(name2)?myId:name2);
            }
            tv_send_name.setText(TextUtils.isEmpty(name2)?myId:name2);
            showMarker(latMyself, lngMyself,0);
            if (!"群组".equals(type)){
                LatLng p1 = null,p2=null;
                if (latMyself!=null&&!"".equals(latMyself)){
                    p1 = new LatLng(Double.valueOf(latMyself), Double.valueOf(lngMyself));
                }
                if (lat!=null&&!"".equals(lat)){
                    p2 = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                }
                CoordinateConverter converter1 = new CoordinateConverter();
                converter1.coord(p1);
                converter1.from(CoordinateConverter.CoordType.COMMON);
                LatLng convertLatLng1 = converter1.convert();
                CoordinateConverter converter2 = new CoordinateConverter();
                converter2.coord(p2);
                converter2.from(CoordinateConverter.CoordType.COMMON);
                LatLng convertLatLng2 = converter2.convert();
                PlanNode stNode = PlanNode.withLocation(convertLatLng1);
                PlanNode enNode = PlanNode.withLocation(convertLatLng2);
                mSearch.drivingSearch((new DrivingRoutePlanOption())
                        .from(stNode).to(enNode));
            }
            myhandler.removeCallbacks(mapRunnable);
        }
    };

    private void initmapClick2() {
        String name = ChatActivity.activityInstance.getTitleName();
        String sex1 = ChatActivity.activityInstance.getSex();
        String image = ChatActivity.activityInstance.getImage();
        String location = ChatActivity.activityInstance.getLocationState();
        if ("01".equals(location)||"群组".equals(type)) {
            if ("00".equals(sex1)){
                mtype = 2;
            }else {
                mtype = 1;
            }
            if (!"群组".equals(type)) {
                showMarker(lat, lng, mtype);
            }
            mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Log.e("chatfrag,","OnMapClick");
                    if (!isShowMap) {
                        WeakReference<ChatFragment> references =  new WeakReference<ChatFragment>(ChatFragment.this);
                        player = new RecordPlayer(references.get().getActivity());
                        utils = new SendVoiceUtils(references.get().getActivity(),references.get().myhandler);
                        showPianyi(false);
                        mLocClient.registerLocationListener(myListener);
                        mLocClient.start();
                        rl_list.setVisibility(View.GONE);
                        message_list.setVisibility(View.GONE);
                        iv_mylocation.setVisibility(View.VISIBLE);
                     //   rl_bottom.setVisibility(View.VISIBLE);
                        rl_shouqi.setVisibility(View.VISIBLE);
                        input_menu.setVisibility(View.INVISIBLE);
                        rl_list.setAnimation(AnimationUtil.moveToViewBottom());
                        message_list.setAnimation(AnimationUtil.moveToViewBottom());
                        rl_bottom.setAnimation(AnimationUtil.moveToViewLocation());
                        rl_shouqi.setAnimation(AnimationUtil.moveToViewLocation());
                        isShowMap = true;
                        if ("群组".equals(type)) {
                            joinInCarTeam();
//                            myhandler.postDelayed(receiveAllLocaRunnable, 3000);
                        }else {
                            myhandler.postDelayed(receivelocaRunnable, 3000);
                        }
                        myhandler.postDelayed(sendlocaRunnable,3000);
                    }else {
                        mBaiduMap.hideInfoWindow();
                    }
                }
                @Override
                public boolean onMapPoiClick(MapPoi mapPoi) {
                    return false;
                }
            });
            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    if ("群组".equals(type)){
                        for (int i = 0; i < list.size(); i++) {
                            if (marker.equals(list.get(i))) {
                                list.get(i).setToTop();
                                String id = strLoginId.get(i);
                                String url = FXConstant.URL_Get_UserInfo + id;
                                StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        setView(s);
                                        LatLng ll1 = marker.getPosition();
                                        double lat = ll1.latitude + 0.001;
                                        double lng = ll1.longitude;
                                        LatLng ll2 = new LatLng(lat, lng);
                                        MapStatus mMapStatus = new MapStatus.Builder()
                                                .target(ll2)
                                                .build();
                                        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                        //改变地图状态
                                        mBaiduMap.setMapStatus(mMapStatusUpdate);
                                        InfoWindow mInfoWindow = new InfoWindow(vi2, ll1, -47);
                                        mBaiduMap.showInfoWindow(mInfoWindow);
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {

                                    }
                                });
                                MySingleton.getInstance(getActivity()).addToRequestQueue(request);
                                return true;
                            }
                        }
                    }else {
                        if (marker.equals(marOther)) {
                            String id = toChatUsername;
                            String url = FXConstant.URL_Get_UserInfo + id;
                            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    setView(s);
                                    LatLng ll1 = marker.getPosition();
                                    double lat = ll1.latitude + 0.001;
                                    double lng = ll1.longitude;
                                    LatLng ll2 = new LatLng(lat, lng);
                                    MapStatus mMapStatus = new MapStatus.Builder()
                                            .target(ll2)
                                            .build();
                                    //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
                                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
                                    //改变地图状态
                                    mBaiduMap.setMapStatus(mMapStatusUpdate);
                                    InfoWindow mInfoWindow = new InfoWindow(vi2, ll1, -47);
                                    mBaiduMap.showInfoWindow(mInfoWindow);
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    //callback.onError("获取数据失败");
                                }
                            });
                            MySingleton.getInstance(getActivity()).addToRequestQueue(request);
                            return true;
                        }
                    }
                    return false;
                }
            });
            mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
                @Override
                public void onMapStatusChangeStart(MapStatus mapStatus) {
                }
                @Override
                public void onMapStatusChange(MapStatus mapStatus) {
                }
                @Override
                public void onMapStatusChangeFinish(MapStatus mapStatus) {
                    if (isMylocaclicked) {
                        iv_mylocation.setImageResource(R.drawable.gps_mylocation);
                    } else {
                        iv_mylocation.setImageResource(R.drawable.gps_mylocationt);
                    }
                    isMylocaclicked = false;
                }
            });
        }
        if (image!=null&&!"".equals(image)){
            image = image.split("\\|")[0];
            iv_receive.setVisibility(View.VISIBLE);
            tv_title_receive.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+image,iv_receive, DemoApplication.mOptions);
        }else {
            iv_receive.setVisibility(View.INVISIBLE);
            tv_title_receive.setVisibility(View.VISIBLE);
            tv_title_receive.setText(TextUtils.isEmpty(name)?toChatUsername:name);
        }
        tv_receive_name.setText(TextUtils.isEmpty(name)?toChatUsername:name);
        if ("群组".equals(type)){
            iv_receive.setVisibility(View.INVISIBLE);
            tv_title_receive.setVisibility(View.INVISIBLE);
            rl_receive.setVisibility(View.VISIBLE);
            String myUserId = DemoHelper.getInstance().getCurrentUsernName();
            String[] str = null;
            List<String> urlList = new ArrayList<>();
            if (toChatImg!=null&&!"".equals(toChatImg)) {
                str=toChatImg.split("\\|");
            }
            if (str!=null) {
                if (str.length > 0) {
                    urlList.add(str[0]);
                }
                if (str.length > 1) {
                    urlList.add(str[1]);
                }
                if (str.length > 2) {
                    urlList.add(str[2]);
                }
                if (str.length > 3) {
                    urlList.add(str[3]);
                }
                if (str.length > 4) {
                    urlList.add(str[4]);
                }
                if (str.length > 5) {
                    urlList.add(str[5]);
                }
                for (int j = 0; j < urlList.size(); j++) {
                    if (urlList.get(j).indexOf(myUserId) != -1) {
                        urlList.remove(j);
                        break;
                    }
                }
            }
            if (urlList.size()>0) {
                rl_receive.addView(creatAvatarView(urlList.size()));
                showImg(toChatImg, urlList);
            }else {
                rl_receive.setVisibility(View.INVISIBLE);
                iv_receive.setVisibility(View.INVISIBLE);
                tv_title_receive.setVisibility(View.VISIBLE);
                tv_title_receive.setText("群");
            }
        }
    }
    
    private void showImg(String image,List<String> urlList){
        if (image!=null){
            switch (urlList.size()) {
                case 0:
                    rl_receive.setVisibility(View.INVISIBLE);
                    iv_receive.setVisibility(View.INVISIBLE);
                    tv_title_receive.setVisibility(View.VISIBLE);
                    tv_title_receive.setText("群");
                    break;
                case 1:
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(0), iv_avatar1, DemoApplication.mOptions);
                    break;
                case 2:
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(0), iv_avatar1, DemoApplication.mOptions);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(1), iv_avatar2, DemoApplication.mOptions);
                    break;
                case 3:
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(0), iv_avatar1, DemoApplication.mOptions);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(1), iv_avatar2, DemoApplication.mOptions);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(2), iv_avatar3, DemoApplication.mOptions);
                    break;
                case 4:
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(0), iv_avatar1, DemoApplication.mOptions);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(1), iv_avatar2, DemoApplication.mOptions);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(2), iv_avatar3, DemoApplication.mOptions);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(3), iv_avatar4, DemoApplication.mOptions);
                    break;
                case 5:
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(0), iv_avatar1, DemoApplication.mOptions);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(1), iv_avatar2, DemoApplication.mOptions);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(2), iv_avatar3, DemoApplication.mOptions);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(3), iv_avatar4, DemoApplication.mOptions);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR + urlList.get(4), iv_avatar5, DemoApplication.mOptions);
                    break;
            }
        }else {
            rl_receive.setVisibility(View.INVISIBLE);
            iv_receive.setVisibility(View.INVISIBLE);
            tv_title_receive.setVisibility(View.VISIBLE);
            tv_title_receive.setText("群");
        }
    }

    private View creatAvatarView(int type) {
        switch (type) {
            case 0:
                return null;
            case 1:
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.fx_group_avatar1, null, false);
                iv_avatar1 = (CircleImageView) v.findViewById(R.id.iv_avatar1);
                return v;
            case 2:
                View v1 = LayoutInflater.from(getActivity()).inflate(R.layout.fx_group_avatar2, null, false);
                iv_avatar1 = (CircleImageView) v1.findViewById(R.id.iv_avatar1);
                iv_avatar2 = (CircleImageView) v1.findViewById(R.id.iv_avatar2);
                return v1;
            case 3:
                View v2 = LayoutInflater.from(getActivity()).inflate(R.layout.fx_group_avatar3, null, false);
                iv_avatar1 = (CircleImageView) v2.findViewById(R.id.iv_avatar1);
                iv_avatar2 = (CircleImageView) v2.findViewById(R.id.iv_avatar2);
                iv_avatar3 = (CircleImageView) v2.findViewById(R.id.iv_avatar3);
                return v2;
            case 4:
                View v3 = LayoutInflater.from(getActivity()).inflate(R.layout.fx_group_avatar4, null, false);
                iv_avatar1 = (CircleImageView) v3.findViewById(R.id.iv_avatar1);
                iv_avatar2 = (CircleImageView) v3.findViewById(R.id.iv_avatar2);
                iv_avatar3 = (CircleImageView) v3.findViewById(R.id.iv_avatar3);
                iv_avatar4 = (CircleImageView) v3.findViewById(R.id.iv_avatar4);
                return v3;
            case 5:
                View v4 = LayoutInflater.from(getActivity()).inflate(R.layout.fx_group_avatar5, null, false);
                iv_avatar1 = (CircleImageView) v4.findViewById(R.id.iv_avatar1);
                iv_avatar2 = (CircleImageView) v4.findViewById(R.id.iv_avatar2);
                iv_avatar3 = (CircleImageView) v4.findViewById(R.id.iv_avatar3);
                iv_avatar4 = (CircleImageView) v4.findViewById(R.id.iv_avatar4);
                iv_avatar5 = (CircleImageView) v4.findViewById(R.id.iv_avatar5);
                return v4;
            default:
                View v6 = LayoutInflater.from(getActivity()).inflate(R.layout.fx_group_avatar1, null, false);
                iv_avatar1 = (CircleImageView) v6.findViewById(R.id.iv_avatar1);
                return v6;
        }
    }

    private void joinInCarTeam() {
        String url = FXConstant.URL_INSERT_SHARETEAM;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                myhandler.postDelayed(receiveAllLocaRunnable, 3000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(),"加入失败,请确保网络连接,并开启定位",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                params.put("teamId",toChatUsername);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    private void leaveCarTeam() {
        String url = FXConstant.URL_DELETE_SHARETEAM;
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
                params.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                params.put("teamId",toChatUsername);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    private void initmapClick1(boolean useType) {
        if (chatType == EaseConstant.CHATTYPE_GROUP||useType){
            mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(getActivity(), BaiDuQiyeLocationActivity.class);
                    startActivityForResult(intent, 21);
                }
                @Override
                public boolean onMapPoiClick(MapPoi mapPoi) {
                    return false;
                }
            });
        }
    }

    private void setView(String s) {
        try {
            org.json.JSONObject jsonObject = new org.json.JSONObject(s);
            org.json.JSONObject object = jsonObject.getJSONObject("userInfo");
            Userful user2 = JSONParser.parseUser(object);
            final String uId = user2.getLoginId();
            String imageStr = TextUtils.isEmpty(user2.getImage()) ? "" : user2.getImage();
            String sex = TextUtils.isEmpty(user2.getSex()) ? "" : user2.getSex();
            String ZY1 = TextUtils.isEmpty(user2.getUpName1()) ? "" : user2.getUpName1();
            String ZY2 = TextUtils.isEmpty(user2.getUpName2()) ? "" : user2.getUpName2();
            String ZY3 = TextUtils.isEmpty(user2.getUpName3()) ? "" : user2.getUpName3();
            String ZY4 = TextUtils.isEmpty(user2.getUpName4()) ? "" : user2.getUpName4();
            String sign = TextUtils.isEmpty(user2.getSignaTure()) ? "" : user2.getSignaTure();
            String age = TextUtils.isEmpty(user2.getuAge()) ? "27" : user2.getuAge();
            String name = TextUtils.isEmpty(user2.getName()) ? "" : user2.getName();
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
                tv_titl.setText(TextUtils.isEmpty(name)?toChatUsername:name);
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
            if (latMyself != null && lat != null) {
                if (!("".equals(latMyself) || "".equals(lat))) {
                    double latitude1 = Double.valueOf(latMyself);
                    double longitude1 = Double.valueOf(lngMyself);
                    final LatLng ll1 = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
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
            tvName_v2.setText(TextUtils.isEmpty(name)?toChatUsername:name);
            tvsign.setText(sign);
            String shareRed = user2.getShareRed();
            final String friendsNumber = user2.getFriendsNumber();
            String onceJine = null;
            if (friendsNumber != null && !"".equals(friendsNumber)) {
                onceJine = friendsNumber.split("\\|")[0];
            }
            if (shareRed != null && !"".equals(shareRed) && Double.parseDouble(shareRed) > 0 && onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                tvName_v2.setTextColor(Color.RED);
            } else {
                tvName_v2.setTextColor(Color.BLACK);
            }
            btnDh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNavi(Double.valueOf(lat), Double.valueOf(lng));
                    //finish();
                }
            });
            btnXq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),
                            UserDetailsActivity.class);
                    intent.putExtra(FXConstant.JSON_KEY_HXID, uId);
                    startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startNavi(double lat, double Lng) {
        if (latMyself==null||"".equals(latMyself)){
            Toast.makeText(getActivity(),"定位失败，请查看权限是否开启",Toast.LENGTH_SHORT).show();
            return;
        }
        LatLng pt1 = new LatLng(Double.parseDouble(latMyself), Double.parseDouble(lngMyself));
        LatLng pt2 = new LatLng(lat, Lng);
        // 构建 导航参数
        RouteParaOption para = new RouteParaOption()
                .startPoint(pt1).endPoint(pt2);
        try {
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para,getActivity());
        } catch (Exception e) {
            e.printStackTrace();
            showDialog2();
        }
    }

    public void showDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(getActivity());
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

    private void showMarker(final String lat, final String lng,int type) {
        if (lat==null||"".equals(lat)){
            return;
        }
        LatLng p = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        CoordinateConverter converter = new CoordinateConverter();
        converter.coord(p);
        converter.from(CoordinateConverter.CoordType.COMMON);
        LatLng convertLatLng = converter.convert();
        if (type==0) {
            mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
            MarkerOptions option = new MarkerOptions().position(convertLatLng).icon(mCurrentMarker);
            mBaiduMap.addOverlay(option);
            if (isFirstLoc) {
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 16.0f);
                mBaiduMap.animateMapStatus(u);
            }
        }else {
            otherView = null;
            if (type==2) {
                otherView = LayoutInflater.from(getActivity()).inflate(R.layout.item_map_user1, null);
            }else if (type==1){
                otherView = LayoutInflater.from(getActivity()).inflate(R.layout.item_map_user, null);
            }
            otherView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tvTitle = (TextView) otherView.findViewById(R.id.tv_name);
            if (ChatActivity.activityInstance!=null) {
                if (ChatActivity.activityInstance.getTitleName()!=null&&!"".equals(ChatActivity.activityInstance.getTitleName())) {
                    tvTitle.setText(ChatActivity.activityInstance.getTitleName());
                }else {
                    tvTitle.setText(toChatUsername);
                }
            }else {
                tvTitle.setText(toChatUsername);
            }
            MarkerOptions ooA = new MarkerOptions()
                    .position(convertLatLng)
                    .icon(BitmapDescriptorFactory
                            .fromView(otherView))
                    .zIndex(9)
                    .draggable(false);
            marOther = (Marker) mBaiduMap.addOverlay(ooA);
        }
    }

    private double jisuanPianyi() {
        double pianyi = 0;
        double zoomLevel = mBaiduMap.getMapStatus().zoom;
        pianyi = 0.1 * Math.pow(2,(13-zoomLevel));
        return pianyi;
    }

    private int getIndex(String[] imgUrl,String uId){
        if (imgUrl!=null&&uId!=null){
            for (int i=0;i<imgUrl.length;i++){
                if (imgUrl[i].indexOf(uId) != -1){
                    return i;
                }
            }
        }
        return -1;
    }
    private void updateGroupInServer(final String memImg) {
        String url = FXConstant.URL_UPDATE_GROUPINFO;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("groupdeac,supdate",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("groupdeac,supdate","错误");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("groupId",toChatUsername);
                if (memImg!=null){
                    param.put("groupImage", memImg);
                }
                return param;
            }
        };
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    @Override
    protected void setUpView() {
        if (fragmentArgs!=null) {
            toChatImg = fragmentArgs.getString(EaseConstant.EXTRA_USER_IMG);
            toChatName = fragmentArgs.getString(EaseConstant.EXTRA_USER_NAME);
            shareRed = fragmentArgs.getString(EaseConstant.EXTRA_USER_SHARERED);
        }
        if (type!=null){
            Log.e("chatfrag,type",type);
        }
        if (toChatName!=null){
            Log.e("chatfrag,toChatName",toChatName);
        }
        if (toChatImg!=null){
            Log.e("chatfrag,toChatImg",toChatImg);
        }
        String userId = DemoHelper.getInstance().getCurrentUsernName();
        if (!StringUtils.isBlank(toChatImg)&&!StringUtils.isBlank(userId)&&toChatImg.indexOf(userId) != -1) {
            String curImg = DemoApplication.getInstance().getCurrentUser().getImage();
            if (curImg!=null&&!"".equals(curImg)){
                curImg = curImg.split("\\|")[0];
            }
            String[] img = toChatImg.split("\\|");
            int index = getIndex(img, userId);
            if (index!=-1){
                if (!curImg.equals(img[index])){
                    toChatImg = toChatImg.replace(img[index],curImg);
                    updateGroupInServer(toChatImg);
                }
            }
        }else {
            Log.e("chatfr,cur","无头像");
        }
        setChatFragmentListener(this);
        tvName= (TextView) getView().findViewById(R.id.name);
        if (type==null||"".equals(type)||"0".equals(type)) {
            if (chatType == Constant.CHATTYPE_SINGLE) {
                Map<String, RobotUser> robotMap = DemoHelper.getInstance().getRobotList();
                if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                    isRobot = true;
                }
                getView().findViewById(R.id.iv_setting_single).setVisibility(View.GONE);
                getView().findViewById(R.id.iv_setting_single).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), ChatSettingSingleActivity.class).putExtra("userId", toChatUsername));
                    }
                });
                getView().findViewById(R.id.iv_setting_group).setVisibility(View.GONE);
                if (ChatActivity.activityInstance != null) {
                    if (ChatActivity.activityInstance.getTitleName() != null && !"".equals(ChatActivity.activityInstance.getTitleName())) {
                        tvName.setText(ChatActivity.activityInstance.getTitleName());
                    } else {
                        tvName.setText(toChatUsername);
                    }
                } else {
                    tvName.setText(toChatUsername);
                }
            } else if (chatType == EaseConstant.CHATTYPE_GROUP) {
                getView().findViewById(R.id.iv_setting_group).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.iv_setting_group).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), QiYeYuGoActivity.class).putExtra("companyId", toChatUsername).putExtra("orderId", "操作员工"));
                    }
                });
                getView().findViewById(R.id.iv_setting_single).setVisibility(View.GONE);
                inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
                            startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).
                                    putExtra("groupId", toChatUsername), REQUEST_CODE_SELECT_AT_USER);
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group == null || group.getAffiliationsCount() <= 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().getGroupFromServer(toChatUsername);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                if (ChatActivity.activityInstance != null) {
                    if (ChatActivity.activityInstance.getTitleName() != null && !"".equals(ChatActivity.activityInstance.getTitleName())) {
                        tvName.setText(ChatActivity.activityInstance.getTitleName());
                    } else {
                        tvName.setText(toChatUsername);
                    }
                } else {
                    tvName.setText(toChatUsername);
                }
            }
        }else {
            if (type.equals("单聊")){
                getView().findViewById(R.id.iv_setting_single).setVisibility(View.GONE);
                getView().findViewById(R.id.iv_setting_group).setVisibility(View.GONE);
                tvName.setText(toChatName);
            }else if (type.equals("企业")){
                getView().findViewById(R.id.iv_setting_group).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.iv_setting_group).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), QiYeYuGoActivity.class).putExtra("companyId", toChatUsername).putExtra("orderId", "操作员工"));
                    }
                });
                getView().findViewById(R.id.iv_setting_single).setVisibility(View.GONE);
                inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
                            startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).
                                    putExtra("groupId", toChatUsername), REQUEST_CODE_SELECT_AT_USER);
                        }
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group == null || group.getAffiliationsCount() <= 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().getGroupFromServer(toChatUsername);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                tvName.setText(toChatName);
            }else if (type.equals("群组")){
                TextView tv = (TextView)getView().findViewById(R.id.iv_setting_group);
                tv.setVisibility(View.VISIBLE);
                tv.setText("设置");
                tv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(
                                (new Intent(getActivity(), GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
                                REQUEST_CODE_GROUP_DETAIL);
                    }
                });
                getView().findViewById(R.id.iv_setting_single).setVisibility(View.GONE);
                tvName.setText(toChatName);
                tvName.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            }
        }
        super.setUpView();
        hideTitleBar();

        ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        inputMenu.init(null);
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
        //inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
        }
        //no red packet in chatroom
//        if (chatType != Constant.CHATTYPE_CHATROOM) {
//            inputMenu.registerExtendMenuItem(R.string.attach_red_packet, R.drawable.em_chat_red_packet_selector, ITEM_RED_PACKET, extendMenuItemClickListener);
//        }
    }

    private void updateGroupFromServer() {
        String url = FXConstant.URL_SELECT_GROUPINFO+toChatUsername;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                JSONObject groupInfo = object.getJSONObject("groupInfo");
                if (groupInfo!=null) {
                    String groupMember = groupInfo.getString("groupMember");
                    toChatName = groupInfo.getString("groupName")+"("+groupMember+")";
                    toChatImg = groupInfo.getString("groupImage");
                    tvName.setText(toChatName);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }else if (requestCode == REQUEST_CODE_GROUP_DETAIL){
            if (resultCode == Activity.RESULT_OK){
                isChange = true;
                updateGroupFromServer();
            }
        }else {
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_CODE_SELECT_VIDEO: //send the video
                        if (data != null) {
                            int duration = data.getIntExtra("dur", 0);
                            String videoPath = data.getStringExtra("path");
                            File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                                ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                                fos.close();
                                sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case REQUEST_CODE_SELECT_FILE: //send the file
                        if (data != null) {
                            Uri uri = data.getData();
                            if (uri != null) {
                                sendFileByUri(uri);
                            }
                        }
                        break;
                    case REQUEST_CODE_SELECT_AT_USER:
                        if (data != null) {
                            String username = data.getStringExtra("username");
                            inputAtUsername(username, false);
                        }
                        break;

//            case REQUEST_CODE_SEND_MONEY:
//                if (data != null){
//                    sendMessage(RedPacketUtil.createRPMessage(getActivity(), data, toChatUsername));
//                }
//                break;
                    default:
                        break;
                }
            }
        }

    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if(isRobot){
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }
        String userPic = PreferenceManager.getInstance().getCurrentUserAvatar();
        if (!TextUtils.isEmpty(userPic)){
            message.setAttribute("userPic",userPic);
        }
        String userName = PreferenceManager.getInstance().getCurrentUserNick();
        if (userName==null||"".equals(userName)){
            userName = DemoHelper.getInstance().getCurrentUsernName();
        }
        if (!TextUtils.isEmpty(userName)){
            message.setAttribute("userName",userName);
        }
        String msgType = type;
        if (msgType==null||"名片".equals(msgType)||"编辑订单".equals(msgType)||"已编辑".equals(msgType)){
            msgType = "单聊";
        }
        String chatImg = toChatImg;
        if (chatImg==null){
            chatImg = "0";
        }
        String chatName = toChatName;
        if (chatName==null){
            chatName = toChatUsername;
        }
        if (shareRed==null||"".equals(shareRed)){
            shareRed = "无";
        }
        if (msgType.equals("单聊")) {
            message.setAttribute(DemoHelper.getInstance().getCurrentUsernName(), userPic+"|"+userName);
            message.setAttribute(toChatUsername, chatImg+"|"+chatName);
        }else {
            message.setAttribute(DemoHelper.getInstance().getCurrentUsernName(), userPic);
            message.setAttribute(toChatUsername, chatImg);
        }
        message.setFrom(DemoHelper.getInstance().getCurrentUsernName());
        message.setAttribute("name",chatName);
        message.setAttribute("type",msgType);
        message.setAttribute("shareRed",shareRed);

    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    @Override
    public void onEnterToChatDetails() {
        if (chatType == Constant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found,Toast.LENGTH_SHORT).show();
                return;
            }
            startActivityForResult(
                    (new Intent(getActivity(), GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
                    REQUEST_CODE_GROUP_DETAIL);
        }else if(chatType == Constant.CHATTYPE_CHATROOM){
            startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
        }
    }

    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
        Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
        intent.putExtra(FXConstant.JSON_KEY_HXID, username);
        startActivity(intent);
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView!=null) {
            mMapView.onResume();
        }
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        registerIntentReceiver();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView!=null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        if (isChange&&getActivity()!=null){
            getActivity().setResult(Activity.RESULT_OK);
        }
        super.onDestroy();
        if (btn_fabu!=null){
            btn_fabu = null;
        }
        if (mMapView!=null) {
            // 关闭定位图层
            mMapView.onDestroy();
            mMapView = null;
        }
        if (mUiSettings!=null){
            mUiSettings = null;
        }
        if (mCurrentMarker!=null){
            mCurrentMarker.recycle();
            mCurrentMarker = null;
        }
        if (mSearch!=null){
            mSearch.destroy();
            mSearch = null;
        }
        if (mLocClient!=null) {
            mLocClient.unRegisterLocationListener(myListener);
            mLocClient.stop();
        }
        if (player!=null&&player.isPlaying()) {
            player.stopPalyer();
        }
        if (utils!=null) {
            utils.discardRecording();
        }
        if (myhandler!=null&&receivelocaRunnable!=null){
            myhandler.removeCallbacks(receivelocaRunnable);
            myhandler.removeCallbacks(sendlocaRunnable);
        }
        if (myhandler!=null&&receiveAllLocaRunnable!=null){
            leaveCarTeam();
            myhandler.removeCallbacks(receiveAllLocaRunnable);
            myhandler.removeCallbacks(sendlocaRunnable);
        }
    }

    @Override
    public boolean onMessageBubbleClick(EMMessage message,EaseMessageAdapter eAdapter) {
        //open red packet if the message is red packet
//        if (message.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)){
//            RedPacketUtil.openRedPacket(getActivity(), chatType, message, toChatUsername, messageList);
//            return true;
//        }
        if(message.getType() == EMMessage.Type.VOICE && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()){// 是语音  并且是接受到的未读
            if ("群组".equals(type)){
                String uId = message.getFrom();
                String image = message.getStringAttribute(uId,"");
                String name = message.getStringAttribute("userName","");
                if (image!=null&&!"".equals(image)){
                    image = image.split("\\|")[0];
                    rl_receive.setVisibility(View.INVISIBLE);
                    iv_receive.setVisibility(View.VISIBLE);
                    tv_title_receive.setVisibility(View.INVISIBLE);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+image,iv_receive, DemoApplication.mOptions);
                }else {
                    rl_receive.setVisibility(View.INVISIBLE);
                    iv_receive.setVisibility(View.INVISIBLE);
                    tv_title_receive.setVisibility(View.VISIBLE);
                    tv_title_receive.setText(TextUtils.isEmpty(name)?uId:name);
                }
                Log.e("chatfrag,na",name+"");
                tv_receive_name.setText(TextUtils.isEmpty(name)?uId:name);
            }
            int position=-1;
            if(message != null && message.getMsgId() != null && !message.getMsgId().isEmpty()) {
                String var2 = message.getMsgId();
                List var3 = conversation.getAllMessages();
                for(int var4 = 0; var4 < var3.size(); ++var4) {
                    if(((EMMessage)var3.get(var4)).getMsgId().equals(var2)) {
                        position = var4;
                    }
                }
            }
            EaseChatRowVoice easeChatRowVoice = new EaseChatRowVoice(getActivity(), message, position, eAdapter, conversation);  // 在EaseChatRowVoice添加参数conversation
            easeChatRowVoice.onBubbleClick();
            return true;  //返回true  自行处理事件
        }
        return false;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
            String action = cmdMsgBody.action();//get user defined action
//            if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION) && message.getChatType() == EMMessage.ChatType.GroupChat){
//                RedPacketUtil.receiveRedPacketAckMessage(message);
//                messageList.refresh();
//            }
        }
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        // no message forward when in chat room
        try {
            startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message",message)
                            .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
                    REQUEST_CODE_CONTEXT_MENU);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO:
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;
//        case ITEM_RED_PACKET:
//            RedPacketUtil.startRedPacketActivityForResult(this, chatType, toChatUsername, REQUEST_CODE_SEND_MONEY);
//            break;
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }
    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // videoCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
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
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
    }
    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
    }

    /**
     * chat row provider
     *
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 8;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if(message.getType() == EMMessage.Type.TXT){
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                }else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
//                else if (message.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {
//                    //sent redpacket message
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_MONEY : MESSAGE_TYPE_SEND_MONEY;
//                } else if (message.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    //received redpacket message
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LUCKY : MESSAGE_TYPE_SEND_LUCKY;
//                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if(message.getType() == EMMessage.Type.TXT){
                // voice call or video call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                        message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
                }
//                else if (message.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {//send redpacket
//                    return new ChatRowRedPacket(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {//open redpacket message
//                    return new ChatRowRedPacketAck(getActivity(), message, position, adapter);
//                }
            }
            return null;
        }

    }

    public EMMessageListener messageListener = new EMMessageListener() {

        // implement methods in EMMessageListener
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for (EMMessage message : messages) {
                String username = null;
                // group message
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    // single chat message
                    username = message.getFrom();
                }

                // if the message is for current conversation
                if (username.equals(toChatUsername)) {
                    messageList.refreshSelectLast();
                    EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
                } else {
                    EaseUI.getInstance().getNotifier().onNewMsg(message);
                }
            }
            for (final EMMessage message : messages) {
                boolean isPlayIng = EaseChatRowVoicePlayClickListener.isPlaying;
                if (message.getType() == EMMessage.Type.VOICE && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked() && !isPlayIng) {// 是语音  并且是接受到的未读
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(500);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!isLuYin) {
                                            onMessageBubbleClick(message, messageList.getAdapter());
                                        }
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                String action = cmdMsgBody.action();//get user defined action
//                if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    Log.d("ack_redpacket-->","222222");
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                    messageList.refresh();
//                }
            }
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {
            if(isMessageListInited) {
                messageList.refresh();
            }
        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {
            if(isMessageListInited) {
                messageList.refresh();
            }
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
            if(isMessageListInited) {
                messageList.refresh();
            }
        }


        @Override
        public void onMessageChanged(EMMessage emMessage, Object change) {
            if(isMessageListInited) {
                messageList.refresh();
            }
        }

    };
    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        getActivity().unregisterReceiver(intentReceiver);
        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(getActivity());
    }

}
