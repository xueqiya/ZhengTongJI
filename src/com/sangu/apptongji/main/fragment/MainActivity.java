/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sangu.apptongji.main.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
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
import com.fanxin.easeui.utils.EaseCommonUtils;
import com.fanxin.easeui.utils.UserManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatService;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EMLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.db.InviteMessgeDao;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.NewFriendsActivity;
import com.sangu.apptongji.main.activity.PaidanListActivity;
import com.sangu.apptongji.main.activity.TouMingActivity;
import com.sangu.apptongji.main.activity.TradeTrackActivity;
import com.sangu.apptongji.main.address.KyqInfo;
import com.sangu.apptongji.main.alluser.entity.PushDetail;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.avtivity.ConsumeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.DanjuListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.DingdanActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.SelfYuEActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.SouSuoActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.SouSuoThreeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.SouSuoTwoActivity;
import com.sangu.apptongji.main.alluser.presenter.IProfilePresenter;
import com.sangu.apptongji.main.alluser.presenter.IPushPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ProfilePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PushPresenter;
import com.sangu.apptongji.main.alluser.view.IProfileView;
import com.sangu.apptongji.main.alluser.view.IPushView;
import com.sangu.apptongji.main.callback.IError;
import com.sangu.apptongji.main.callback.ISuccess;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.qiye.BaobiaoListActivity;
import com.sangu.apptongji.main.qiye.NewMajorActivity;
import com.sangu.apptongji.main.qiye.QingjiaListActivity;
import com.sangu.apptongji.main.qiye.QiyePaidanListActivity;
import com.sangu.apptongji.main.service.AlermService;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.SoundPlayUtils;
import com.sangu.apptongji.main.utils.SyAddressBookUtil;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.GroupsActivity;
import com.sangu.apptongji.update.ApkInfo;
import com.sangu.apptongji.update.IUpdatePresenter;
import com.sangu.apptongji.update.IUpdateView;
import com.sangu.apptongji.update.UpdatePresenter;
import com.sangu.apptongji.update.VersionManager;
import com.sangu.apptongji.update.VersionManager.AppVersion;
import com.sangu.apptongji.update.VersionManager.OnUpdateListener;
import com.xiaomi.mimc.MIMCUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class MainActivity extends BaseActivity implements IUpdateView,IProfileView,IPushView{
    public static MainActivity instance = null;
    private IProfilePresenter profilePresenter;
    private IUpdatePresenter updatePresenter;
    protected static final String TAG = "MainActivity";
    // textview for unread message count
    private TextView unreadLabel;
    private TextView unreadAll;
    private TextView tv_sousuo;
    private TextView tv1;
    // textview for unread event message
    private TextView unreadAddressLable;
    private Button[] mTabs;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();
    private ContactListFragment contactListFragment;
    private FragmentFind fragmentFind;
    private FragmentProfile fragmentProfile;
    private ConversationListFragment conversationListFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private int childIndex;
    private int contactUnread;
    // user logged into another device
    public boolean isConflict = false;
    private boolean showTishi = true;
    private boolean isFirst = true;
    private int allUnread;
    private int billCount;
    private int thirdPartyCount;
    private int allUnreadchu;
    private int allUnreadru;
    private int allUnreadqy;
    private int tongjiAll;
    Bundle bundle;
    Bundle bundle2;
    String lat,lng,uNation,resv5,resv6,city;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;
    boolean isNext = false;
    private String location1;
    private IPushPresenter pushPresenter;

    private int all = 0;

    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }
    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Log.e("mainac,tongjia",tongjiAll+"");
                    allUnread = billCount+thirdPartyCount+allUnreadchu+allUnreadru+allUnreadqy+tongjiAll;
                    Log.e("mainac,allun",allUnread+"");
                    if (allUnread>0) {
                        unreadAll.setVisibility(View.VISIBLE);
                        if (allUnread>99){
                            unreadAll.setText("99");
                        }else {
                            unreadAll.setText(String.valueOf(allUnread));
                        }
                    }else {
                        unreadAll.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };

    @Override
    public void updateUserInfo(Userful user) {
        uNation = user.getuNation();
        resv5 = user.getResv5();
        resv6 = user.getResv6();
        if ("1".equals(uNation)&&"00".equals(resv6)) {
            queryUnreadCount();
        }
        isNext = true;
    }
    @Override
    public void showproLoading() {}
    @Override
    public void hideproLoading() {}
    @Override
    public void showproError() {}
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            lat = location.getLatitude()+"";
            lng = location.getLongitude()+"";
            city = location.getCity();
            String district = location.getDistrict();
            String street = location.getStreet();
            location1 = "";
            if (city!=null&&!"null".equals(city)){
                location1 = city;
            }
            if (district!=null&&!"null".equals(district)){
                location1 = location1 + district;
            }
            if (street!=null&&!"null".equals(street)){
                location1 = location1 + street;
            }
            DemoApplication.getInstance().saveCurrentLat(lat);
            DemoApplication.getInstance().saveCurrentLng(lng);
            SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("city",city);
            editor.putString("location",location1);
            editor.commit();
            Log.e("mainac","保存城市"+city);
            if (mLocationClient!=null) {
                mLocationClient.stop();
                mLocationClient = null;
            }
            insertLoginLog(lat,lng,location1);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            String packageName = getPackageName();
//            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//                Intent intent = new Intent();
//                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                intent.setData(Uri.parse("package:" + packageName));
//                startActivity(intent);
//            }
//        }
        //make sure activity will not in background if user is logged into another device or removed
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            SharedPreferences sp2 = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
            SharedPreferences sp = getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
            if (sp2!=null) {
                SharedPreferences.Editor editor1 = sp2.edit();
                editor1.clear();
                editor1.commit();
            }
            if (sp!=null) {
                SharedPreferences.Editor editor1 = sp.edit();
                editor1.clear();
                editor1.commit();
            }
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, MainTwoActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            finish();
            startActivity(new Intent(this, MainTwoActivity.class));
            return;
        }

        setContentView(R.layout.fx_activity_main);

       WeakReference<MainActivity> reference =  new WeakReference<MainActivity>(MainActivity.this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        instance = this;
        lat = DemoApplication.getInstance().getCurrentLat();
        lng = DemoApplication.getInstance().getCurrentLng();
        mLocationClient = new LocationClient(reference.get());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        SoundPlayUtils.init(reference.get());
        initLocation();//初始化
        mLocationClient.start();
        profilePresenter = new ProfilePresenter(this,reference.get());
        updatePresenter = new UpdatePresenter(this,reference.get());
        profilePresenter.updateData();
        updatePresenter.checkUpdate();
        currentTabIndex = 0;
        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
        getManifestPermissions(this);
        bundle = new Bundle();
        bundle2 = new Bundle();
        inviteMessgeDao = new InviteMessgeDao(reference.get());
        fragmentFind = new FragmentFind();
        conversationListFragment=new ConversationListFragment();
        contactListFragment = new ContactListFragment();
        fragmentProfile = new FragmentProfile();
        // 将bundle绑定到fragmentFind的对象上
        fragmentFind.setArguments(bundle);
        fragmentProfile.setArguments(bundle2);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentFind).add(R.id.fragment_container,fragmentProfile).hide(fragmentProfile).show(fragmentFind).commit();
        fragments = new Fragment[]{fragmentFind,conversationListFragment, contactListFragment, fragmentProfile};
        initView();

//        bindUid();
       // Log.d("chen", "开始获取发送设备号");
            PermissionUtil permissionUtil = new PermissionUtil(MainActivity.this);
        permissionUtil.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_SETTINGS,Manifest.permission.BROADCAST_PACKAGE_REMOVED},
                new PermissionListener() {
                    @Override
                    public void onGranted() {
                        //所有权限都已经授权
                        upDateInstall(0);
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        //Toast第一个被拒绝的权限
                        upDateInstall(1);
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        //Toast第一个勾选不在提示的权限
                        upDateInstall(0);
                    }
                });
        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();
//        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        //debug purpose only
        registerInternalDebugReceiver();
        Bundle bundle = getIntent().getBundleExtra("launchBundle");
        if(bundle != null){
            try {
                startPushIntent(bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SharedPreferences sp = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        String userId = sp.getString("userId","");
        String passWord = sp.getString("passWord","");
        Log.e("main,conver", "userId="+userId);
        Log.e("main,conver", "passWord="+passWord);
        if (!"".equals(userId)&&!"".equals(passWord)) {
            loginHuanXin(userId, passWord);
            loginMimcInfo(userId,passWord);
        }
        queryIsFreeze(DemoHelper.getInstance().getCurrentUsernName());
        startAlermService();


        //临时加的查一下android0的sharetype 根据这个值判断用户派单分享的内容
        getAndroidShareTypeNotice();

    }

    private void startAlermService() {
        Intent startServiceIntent=new Intent(this, EMChatService.class);
        startServiceIntent.putExtra("reason", "boot");
        startService(startServiceIntent);
        Intent startAlarmServiceIntent=new Intent(this, AlermService.class);
       startService(startAlarmServiceIntent);
    }


    private void loginMimcInfo(String usertel,String password){

        MIMCUser user = UserManager.getInstance().newUser(usertel,MainActivity.this);
        if (user != null) {

            user.login();

        }
    }

    private void loginHuanXin(String usertel,String password){
        // 调用sdk登陆方法登陆聊天服务器
        Log.e("main,conver", "EMClient.getInstance().login");
        EMClient.getInstance().login(usertel, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("main,conver", "login: onSuccess");
                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
            }
            @Override
            public void onProgress(int progress, String status) {
                Log.e("main,conver", "login: onProgress");
            }
            @Override
            public void onError(final int code, final String message) {
                Log.e("main,conver", "login: onError: " + code);
            }
        });
    }

    private void startPushIntent(Bundle bundle) {
        String extraMap = bundle.getString("extraMap");
        Log.d("chen", "startPushIntent" + extraMap);
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(extraMap);
        String type = object.getString("type");
        //加上一个默认的地址
        Intent intent2 = new Intent(MainActivity.this, DingdanActivity.class).putExtra("biaoshi", "01");
        if ("000".equals(type)) {
            intent2 = new Intent(MainActivity.this, DingdanActivity.class).putExtra("biaoshi", "01");
        } else if ("001".equals(type)) {
            intent2 = new Intent(MainActivity.this, ConsumeActivity.class).putExtra("biaoshi", "01");
        } else if ("002".equals(type)) {
            intent2 = new Intent(MainActivity.this, DingdanActivity.class).putExtra("biaoshi", "00");
        } else if ("02".equals(type)) {
            intent2 = new Intent(MainActivity.this, NewFriendsActivity.class);
        } else if ("03".equals(type)) {
            String companyName = object.getString("companyName");
            String comAddress = object.getString("companyAdress");
            intent2 = new Intent(MainActivity.this, NewMajorActivity.class).putExtra("companyName", companyName).putExtra("comAddress", comAddress);
        } else if ("04".equals(type)) {
            intent2 = new Intent(MainActivity.this, DingdanActivity.class).putExtra("biaoshi", "01");
        } else if ("05".equals(type)) {
            intent2 = new Intent(MainActivity.this, QiyePaidanListActivity.class).putExtra("biaoshi", "00").putExtra("isQunzhu", "00");
        } else if ("06".equals(type)) {
            intent2 = new Intent(MainActivity.this, QingjiaListActivity.class).putExtra("biaoshi", "01");
        } else if ("07".equals(type)) {
            intent2 = new Intent(MainActivity.this, BaobiaoListActivity.class).putExtra("biaoshi", "01");
        } else if ("08".equals(type)) {
            intent2 = new Intent(MainActivity.this, SelfYuEActivity.class).putExtra("biaoshi", "000");
        } else if ("09".equals(type) || "21".equals(type)) {
            String dynamicSeq = object.getString("dynamicSeq");
            String createTime = object.getString("createTime");
            String dType = object.getString("dType");
            intent2 = new Intent(MainActivity.this, DynaDetaActivity.class).putExtra("pushType", type).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime).putExtra("dType", dType);
        } else if ("10".equals(type)) {
            String dynamicSeq = object.getString("dynamicSeq");
            String createTime = object.getString("createTime");
            intent2 = new Intent(MainActivity.this, DynaDetaActivity.class).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime)
                    .putExtra("dType", "05").putExtra("sID", DemoHelper.getInstance().getCurrentUsernName());
        } else if ("11".equals(type)) {
            intent2 = new Intent(MainActivity.this, DanjuListActivity.class).putExtra("biaoshi", "单据");
        } else if ("12".equals(type)) {
            intent2 = new Intent(MainActivity.this, DanjuListActivity.class).putExtra("biaoshi", "订单");
        }else if ("13".equals(type)){
            /*String task_label = object.getString("dynamicSeq");
            intent2 = new Intent(MainActivity.this, PushDynaActivity.class).putExtra("task_label",task_label);*/
            String task_label = object.getString("dynamicSeq");
            intent2 = new Intent(MainActivity.this, PaidanListActivity.class).putExtra("task_label",task_label).putExtra("type", "0").putExtra("pushType", 13);
        }else if ("14".equals(type)){
            String companyId = object.getString("companyId");
            String companyName = object.getString("companyName");
            String shangbanTime = companyName.split(".")[0];
            String xiabanTime = companyName.split(".")[1];
            SharedPreferences sp = getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(companyId+"_qiandao_shb",shangbanTime);
            editor.putString(companyId+"_qiandao_xb",xiabanTime);
            editor.commit();
            startService(new Intent(MainActivity.this, AlermService.class));
        }else if ("15".equals(type)){
            SharedPreferences mSharedPreference = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
            SharedPreferences.Editor meditor = mSharedPreference.edit();
            meditor.putString("fenXiangTime", getNowTime3());
            meditor.commit();
            String companyName = object.getString("companyName");
            intent2 = new Intent(MainActivity.this, TouMingActivity.class).putExtra("companyName",companyName);
        }else if ("16".equals(type)){
            //进入派单界面
            intent2 = new Intent(MainActivity.this, PaidanListActivity.class);
            String companyName = object.getString("companyName");
            intent2.putExtra("companyName", companyName);
            intent2.putExtra("pushType", 16);
            intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }else if ("17".equals(type)){
            //进入派单界面
            intent2 = new Intent(MainActivity.this, TradeTrackActivity.class);
            String companyName = object.getString("companyName");
            intent2.putExtra("companyName", "123");
            intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }else if ("18".equals(type)){
            //进入派单界面
            intent2 = new Intent(MainActivity.this, PaidanListActivity.class);
            String companyName = object.getString("companyName");
            intent2.putExtra("pushType", 18);
            intent2.putExtra("type", 1);
            intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }else if ("19".equals(type)) {
            intent2 = new Intent(MainActivity.this, PaidanListActivity.class);
            String companyName = object.getString("companyId");
            intent2.putExtra("pushType", 19);
            intent2.putExtra("companyName", companyName);
        }else if ("20".equals(type)) {
            String dynamicSeq = object.getString("dynamicSeq");
            String createTime = object.getString("createTime");
            intent2 = new Intent(MainActivity.this, DynaDetaActivity.class).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime)
                    .putExtra("dType", "05").putExtra("pushType", "20").putExtra("sID", DemoHelper.getInstance().getCurrentUsernName());
        }

        /*else if ("16".equals(type)){
            //进入派单界面
            intent2 = new Intent(MainActivity.this, PaidanListActivity.class);
            String companyName = object.getString("companyName");
            intent2.putExtra("companyName", companyName);
            intent2.putExtra("pushType", 16);
            intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }else if ("17".equals(type)){
            //进入派单界面
            intent2 = new Intent(MainActivity.this, PaidanListActivity.class);
            String companyName = object.getString("companyName");
            intent2.putExtra("companyName", companyName);
            intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }else if ("18".equals(type)){
            //进入派单界面
            intent2 = new Intent(MainActivity.this, PaidanListActivity.class);
            String companyName = object.getString("companyName");
            intent2.putExtra("pushType", 18);
            intent2.putExtra("type", 1);
            intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } / else if ("19".equals(type)) {
            intent2 = new Intent(MainActivity.this, PaidanListActivity.class);
            String companyName = object.getString("companyId");
            intent2.putExtra("pushType", 19);
            intent2.putExtra("companyName", companyName);
        }*/

        if (type.equals("99")){

        }else {

            try {
                startActivity(intent2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private String getNowTime3() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }
    private void showTongzhi() {
        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");
                SharedPreferences sp = getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);
                boolean isShow = true;
                if (sp!=null) {
                    isShow = sp.getBoolean("isShow",true);
                }
                String type = object1.getString("type");
                String content = object1.getString("content");
                content = content.replace("|", "\n\n");
                if (!"0".equals(type)) {
                    LayoutInflater inflaterDl = LayoutInflater.from(MainActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_gonggao, null);
                    TextView tv_content = (TextView) layout.findViewById(R.id.tv_content);
                    final Dialog dialog = new AlertDialog.Builder(MainActivity.this,R.style.Dialog).create();
                    if ("2".equals(type)) {
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        tv_content.setText(content);
                    } else if ("1".equals(type) && isShow) {
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        tv_content.setText(content);
                    }else if ("3".equals(type)){
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCancelable(true);
                        dialog.setCanceledOnTouchOutside(true);
                        tv_content.setText(content);
                    }
                    if (sp != null) {
                        SharedPreferences.Editor editor1 = sp.edit();
                        editor1.putBoolean("isShow", false);
                        editor1.putString("shareType",object1.getString("shareType"));
                        editor1.commit();

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
                Map<String,String> param = new HashMap<>();
                param.put("deviceType","android0");
                return param;
            }
        };
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }

    private void getManifestPermissions(final Activity activity) {
        PackageInfo packageInfo = null;
        List<String> list = new ArrayList<String>(1);
        try {
            Log.d(TAG, activity.getPackageName());
            packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "A problem occurred when retrieving permissions", e);
        }
        if (packageInfo != null) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null) {
                for (String perm : permissions) {
                    Log.d(TAG, "Manifest contained permission: " + perm);
                    list.add(perm);
                }
            }
        }
    }


    private void queryIsFreeze(final String uId) {
        String url = FXConstant.URL_QUERY_FREEZE_LOGIN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("chen","queryIsFreeze"+s);
                if (!s.contains("code")) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("code");
                    JSONObject item = jsonArray.getJSONObject(0);
                    if (item.getString("freezeType").equalsIgnoreCase("01")) {
                        //表明被冻结 让他强制下线
                        SharedPreferences sp = getSharedPreferences("sangu_shxb_zhuangtai", Context.MODE_PRIVATE);
                        SharedPreferences sp1 = getSharedPreferences("sangu_bddh_time", Context.MODE_PRIVATE);
                        SharedPreferences sp2 = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
                        SharedPreferences sp3 = getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
                        if (sp3!=null) {
                            SharedPreferences.Editor editor1 = sp3.edit();
                            editor1.clear();
                            editor1.commit();
                        }
                        if (sp2!=null) {
                            SharedPreferences.Editor editor1 = sp2.edit();
                            editor1.clear();
                            editor1.commit();
                        }
                        if (sp!=null) {
                            SharedPreferences.Editor editor = sp.edit();
                            if (editor!=null) {
                                editor.clear();
                                editor.commit();
                            }
                        }
                        if (sp1!=null) {
                            SharedPreferences.Editor editor = sp1.edit();
                            if (editor!=null) {
                                editor.clear();
                                editor.commit();
                            }
                        }
                        deleteInstall();
                        DemoHelper.getInstance().logout(true,new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        // show login screen
                                        DemoApplication.getInstance().clear();
                                        DemoHelper.getInstance().setCurrentUserName(null);
                                        if (MainActivity.instance!=null) {
                                            MainActivity.instance.finish();
                                        }
                                    }
                                });
                            }
                            @Override
                            public void onProgress(int progress, String status) {

                            }
                            @Override
                            public void onError(int code, String message) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(MainActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                Log.e("chen","queryIsFreeze  onErrorResponse"+ volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("u_id",uId);
                return param;
            }
        };
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }

    private void deleteInstall() {
        String url = FXConstant.URL_DELETE_INSTALL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "deleteInstall" + s);
                startActivity(new Intent(MainActivity.this, MainTwoActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "deleteInstall  onErrorResponse" + volleyError.getMessage());
                startActivity(new Intent(MainActivity.this, MainTwoActivity.class));
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uId",DemoHelper.getInstance().getCurrentUsernName());
                Log.d("chen", "删除uid" + DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }


    private void upDateInstall(int type) {
        final String uId = DemoHelper.getInstance().getCurrentUsernName();
        final String deviceStr;
        if (null == PushServiceFactory.getCloudPushService() || null == PushServiceFactory.getCloudPushService().getDeviceId()) {
            deviceStr = "android";
        } else {
            deviceStr =  PushServiceFactory.getCloudPushService().getDeviceId();
            Log.e("DeviceId:",deviceStr);
        }
        //Log.d("chen","我开始上传DeviceId" + deviceStr);
        String url = FXConstant.URL_UPDATE_INSTALL;
        final String finalDeviceStr = deviceStr;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override

            public void onResponse(String s) {
                Log.e("mainac,ins","更新设备"+s+deviceStr);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("uId",uId);
                param.put("deviceStr", finalDeviceStr);
                param.put("deviceType","android");
                param.put("version","3.4");
                return param;
            }
        };
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");
        option.setScanSpan(5000);
        option.setAddrType("all");
//        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
//        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(false);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void updateApk(ApkInfo info) {
        int i = getVerCode();
        String versionnum = info.getVersionnum();
        final String updateflg = info.getUpdateflg();
        String apkUrl = info.getApkurl();
        String versiondesc = info.getVersiondesc();
        final AppVersion version = new AppVersion();
        version.setApkUrl(FXConstant.URL_DOWNLOAD_APK + apkUrl);
        // 设置文件名
        version.setFileName("Zhengshiduo.apk");
        // 设置文件在sd卡的目录
        version.setFilePath("zhengshier/update");
        // 设置app当前版本号
        version.setVersionCode(versionnum);
        if (Integer.valueOf(versionnum) > i) {
            LayoutInflater inflaterDl = LayoutInflater.from(MainActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
            final Dialog dialog = new AlertDialog.Builder(MainActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            if (updateflg.equals("1")||updateflg.equals("2")) {
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
            }
            Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
            final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (updateflg.equals("0")) {
                        dialog.dismiss();
                    } else {
                        return;
                    }
                }
            });
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (updateflg.equals("1")||updateflg.equals("0")) {
                        dialog.dismiss();
                    }
                    PermissionUtil permissionUtil = new PermissionUtil(MainActivity.this);
                    permissionUtil.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                            new PermissionListener() {
                                @Override
                                public void onGranted() {
                                    //所有权限都已经授权
                                    final VersionManager manager = VersionManager
                                            .getInstance(MainActivity.this, version);
                                    manager.setOnUpdateListener(new OnUpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(MainActivity.this, "下载成功等待安装", Toast.LENGTH_LONG).show();
                                        }
                                        @Override
                                        public void onError(String msg) {
                                            Toast.makeText(MainActivity.this, "更新失败" + msg,Toast.LENGTH_LONG).show();
                                        }
                                        @Override
                                        public void onDownloading() {
                                            Toast.makeText(MainActivity.this, "正在下载...",Toast.LENGTH_LONG).show();
                                        }
                                        @Override
                                        public void hasNewVersion(boolean has) {
                                            if (has) {
                                                Toast.makeText(MainActivity.this, "正在后台下载",Toast.LENGTH_LONG).show();
                                                manager.downLoad();
                                            }
                                        }
                                    });
                                    manager.checkUpdateInfo();
                                }

                                @Override
                                public void onDenied(List<String> deniedPermission) {
                                    //Toast第一个被拒绝的权限
                                    ToastUtils.showNOrmalToast(getApplicationContext(),"您拒绝了访问内存卡权限,更新失败！");
                                }

                                @Override
                                public void onShouldShowRationale(List<String> deniedPermission) {
                                    //Toast第一个勾选不在提示的权限
                                    ToastUtils.showNOrmalToast(getApplicationContext(),"您拒绝了访问内存卡权限,更新失败！");
                                }
                            });
                }
            });
        }else {
            showTongzhi();
        }
    }

    private void getAndroidShareTypeNotice(){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");
                SharedPreferences sp = getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);

                String shareType = object1.getString("shareType");

                if (sp != null) {
                    SharedPreferences.Editor editor1 = sp.edit();
                    editor1.putString("shareType", shareType);
                    editor1.commit();
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
                param.put("deviceType","android0");
                return param;
            }
        };

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);

    }

    public int getVerCode() {
        int verCode = 0;
        try {
            verCode = getPackageManager().getPackageInfo("com.sangu.app", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verCode;
    }

//    @TargetApi(23)
//    private void requestPermissions() {
//        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
//            @Override
//            public void onGranted() {
////				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onDenied(String permission) {
//                Log.e("mainac,permission",permission);
////                Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void setRobot(int type){
        fragmentFind.setRobot(type);
    }

    public void setSousuoColor(int index){
        this.childIndex = index;
        if (tv_sousuo!=null) {
            if (index == 0) {
                tv_sousuo.setBackgroundResource(R.drawable.fx_bg_text_gra);
            } else if (index == 1) {
                tv_sousuo.setBackgroundResource(R.drawable.fx_bg_text_gray);
            } else if (index == 2) {
                tv_sousuo.setBackgroundResource(R.drawable.fx_bg_text_orange);
            } else if (index == 3) {
                tv_sousuo.setBackgroundResource(R.drawable.fx_bg_text_red);
            }
        }
    }

    /**
     * init views
     */
    private void initView() {
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv_sousuo = (TextView) findViewById(R.id.tv_sousuo);
        unreadAll = (TextView) findViewById(R.id.unread_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.btn_find);
        mTabs[1] = (Button) findViewById(R.id.btn_conversation);
        mTabs[2] = (Button) findViewById(R.id.btn_address_list);
        mTabs[3] = (Button) findViewById(R.id.btn_profile);
        // select first tab
        mTabs[0].setSelected(true);
        tv_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = fragmentFind.getcurrentIndex();

                if (currentTabIndex==0) {
                    if (index==0) {
                        startActivityForResult(new Intent(MainActivity.this, SouSuoActivity.class), 1);
                    }else if (index==1){

                        startActivityForResult(new Intent(MainActivity.this, SouSuoTwoActivity.class),2);

                    }else if (index==2){

                        startActivityForResult(new Intent(MainActivity.this, SouSuoThreeActivity.class),3);

                    }else {
                        Toast.makeText(MainActivity.this,"暂未处理操作",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"暂未处理操作",Toast.LENGTH_SHORT).show();
                }
            }
        });

        pushPresenter = new PushPresenter(MainActivity.this,this);
        pushPresenter.loadPushList(DemoHelper.getInstance().getCurrentUsernName());

    }


    @Override
    public void updatePushList(List<PushDetail> pushLists) {

        if (pushLists.size() != 0) {
            addConersationListPush(pushLists.size());
        }

    }


    /**
     * on tab clicked
     *
     * @param view
     */
    public void onTabClicked(View view) {
        JCVideoPlayer.releaseAllVideos();
        switch (view.getId()) {
            case R.id.btn_find:
                index = 0;
                setSousuoColor(0);
                break;
            case R.id.btn_conversation:
                index = 1;
                conversationListFragment.refreshPushList();
                setSousuoColor(1);
                unreadLabel.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_address_list:
                index = 2;
                contactListFragment.refresh();
                setSousuoColor(1);
                tv1.setVisibility(View.INVISIBLE);

                unreadAddressLable.setVisibility(View.INVISIBLE);

//                if (contactUnread>0){
//                    unreadAddressLable.setVisibility(View.VISIBLE);
//                    unreadAddressLable.setText(contactUnread+"");
//                }else {
//                    unreadAddressLable.setVisibility(View.INVISIBLE);
//                }
                break;
            case R.id.btn_profile:
                bundle2.putInt("billCount",billCount);
                bundle2.putInt("thirdPartyCount",thirdPartyCount);
                fragmentProfile.loadDanju();
                index = 3;
                setSousuoColor(1);
                unreadAll.setVisibility(View.INVISIBLE);
                PermissionUtil permissionUtil = new PermissionUtil(MainActivity.this);
                permissionUtil.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        new PermissionListener() {
                            @Override
                            public void onGranted() {
                                //所有权限都已经授权
                            }
                            @Override
                            public void onDenied(List<String> deniedPermission) {
                                //Toast第一个被拒绝的权限
                                Toast.makeText(getApplicationContext(),"您拒绝了内存卡访问权限,可能导致无法显示头像,无法签名",Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onShouldShowRationale(List<String> deniedPermission) {
                                //Toast第一个勾选不在提示的权限
                                Toast.makeText(getApplicationContext(),"您拒绝了内存卡访问权限,可能导致无法显示头像,无法签名",Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab selected
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    public void hidUnread(){
        fragmentFind.hidUnread();
    }

    public void setCurrentIndex(){
        index = 3;
        setSousuoColor(1);
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab selected
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                if(message.getChatType()!= EMMessage.ChatType.ChatRoom){
                    DemoHelper.getInstance().getNotifier().onNewMsg(message);
                }
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//get the action user defined in command message
//                if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION) && message.getChatType() == EMMessage.ChatType.GroupChat) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
            }
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> list) {
            for (EMMessage message : list) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//get the action user defined in command message
//                if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION) && message.getChatType() == EMMessage.ChatType.GroupChat) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
            }
            refreshUIWithMessage();
        }

        @Override
        public void onMessageDelivered(List<EMMessage> list) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentTabIndex == 1) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                }
            }
        });
    }

    public void refreshDyna(){
        fragmentFind.onRefreshDyna();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==0){
            fragmentFind.onRefreshDyna();
        }
        if (resultCode==RESULT_OK&&data!=null){
            switch (requestCode){
                case 1:
                    String zhY = data.hasExtra("zhuanye")?data.getStringExtra("zhuanye"):"";
                    String name = data.hasExtra("name")?data.getStringExtra("name"):"";
                    String comName = data.hasExtra("comName")?data.getStringExtra("comName"):"";
                    String isclear = data.hasExtra("isclear")?data.getStringExtra("isclear"):"";
                    try {
                        comName = URLEncoder.encode(comName,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    bundle.putString("zhY",zhY);
                    bundle.putString("name",name);
                    bundle.putString("comName",comName);
                    bundle.putString("isclear",isclear);
                    Log.e("MainAc,1","刷新person");
                    fragmentFind.onRefreshPer();
                    break;
                case 2:
                    String content = data.getStringExtra("data");
                    String type = data.getStringExtra("type");

                    bundle.putString("content",content);
                    bundle.putString("type",type);

                    Log.e("MainAc,1","刷新dyna");
                    fragmentFind.onRefreshDyna();
                    break;
                case 3:
                    String baozhjin = data.getStringExtra("baozhjin");
                    String qiye_mch = data.getStringExtra("qiye_mch");
                    String qiye_zhy = data.getStringExtra("qiye_zhy");
                    String isclear2 = data.getStringExtra("isclear");
                    bundle.putString("baozhjin",baozhjin);
                    bundle.putString("qiye_mch",qiye_mch);
                    bundle.putString("qiye_zhy",qiye_zhy);
                    bundle.putString("isclear",isclear2);
                    Log.e("MainAc,1","刷新qiye");
                    fragmentFind.onRefreshQiye();
                    break;
            }
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
//        intentFilter.addAction(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
//                updateUnreadAddressLable();
                if (currentTabIndex == 1) {
                    // refresh conversation list
                    if (conversationListFragment != null) {
                        conversationListFragment.refresh();
                    }
                } else if (currentTabIndex == 2) {
                    if (contactListFragment != null) {
//                        contactListFragment.refresh();
                    }
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
//                if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

//    public class MyContactListener implements EMContactListener {
//        @Override
//        public void onContactAdded(String username) {
//        }
//
//        @Override
//        public void onContactDeleted(final String username) {
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
//                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
//                        String st10 = getResources().getString(R.string.have_you_removed);
//                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
//                                .show();
//                        ChatActivity.activityInstance.finish();
//                    }
//                }
//            });
//        }
//
//        @Override
//        public void onContactInvited(String username, String reason) {
//        }
//
//        @Override
//        public void onContactAgreed(String username) {
//        }
//
//        @Override
//        public void onContactRefused(String username) {
//        }
//    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        insertLoginOffLog();
        super.onDestroy();
        instance=null;
        if (mLocationClient!=null){
            mLocationClient.stop();
            mLocationClient=null;
        }
        unregisterBroadcastReceiver();
        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }

    }

    private void insertLoginOffLog() {
        if (DemoHelper.getInstance().getCurrentUsernName() == null || TextUtils.isEmpty(DemoHelper.getInstance().getCurrentUsernName())) {
            return;
        }
        //u_id   exitTime（当前时间戳）
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        final String time = format.format(new Date(System.currentTimeMillis()));
        StringRequest request = new StringRequest(Request.Method.POST,FXConstant.URL_UPDATE_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "登出插入log" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "登出插入logonErrorResponse" + volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //u_id   exitTime（当前时间戳）
                Map<String,String> param = new HashMap<>();
                param.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                param.put("exitTime",time);
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {

 //       GetMessageListInfo();

//        int count = getUnreadMsgCountTotal();
//        if (count > 0) {
//            unreadLabel.setText(String.valueOf(count));
//            unreadLabel.setVisibility(View.VISIBLE);
//        } else {
//            unreadLabel.setVisibility(View.INVISIBLE);
//        }
    }

    /**
     * update the total unread count
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
                //    unreadAddressLable.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLable.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * get unread event notification count, including application, accepted, etc
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    private InviteMessgeDao inviteMessgeDao;

    @Override
    protected void onResume() {
        super.onResume();
        allUnread = 0;
        allUnreadchu = 0;
        billCount = 0;
        thirdPartyCount = 0;
        allUnreadru = 0;
        queryLiuZhuUnread();
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
//            updateUnreadAddressLable();
        }
        if ("1".equals(uNation)&&"00".equals(resv6)&&isNext) {
            queryUnreadCount();
        }
        queryAllUnread();
        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    private void queryLiuZhuUnread() {
        SharedPreferences sp2 = getSharedPreferences("sangu_zhufa_count", Context.MODE_PRIVATE);
        final String llzhufAll = sp2.getString("zhufaliulanAll", "0");
        String id = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_SEARCH_ALL_UNREAD+id;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject amount = object.getJSONObject("amount");
                com.alibaba.fastjson.JSONObject forwardInfoList = object.getJSONObject("forwardInfoList");
                int praiseTime = object.getIntValue("praiseTime");
                int PertinentTimes = object.getIntValue("PertinentTimes");
                int BadTime = object.getIntValue("BadTime");
                String homePagell=null;
                String lifeDynamicsll = null;
                String locationDynamicsll = null;
                String businessDynamicsll = null;
                if (amount!=null){
                    homePagell = amount.getString("homePage");
                    lifeDynamicsll = amount.getString("lifeDynamics");
                    locationDynamicsll = amount.getString("locationDynamics");
                    businessDynamicsll = amount.getString("businessDynamics");
                }
                if (homePagell==null||"".equals(homePagell)){
                    homePagell = "0";
                }
                if (lifeDynamicsll==null||"".equals(lifeDynamicsll)){
                    lifeDynamicsll = "0";
                }
                if (locationDynamicsll==null||"".equals(locationDynamicsll)){
                    locationDynamicsll = "0";
                }
                if (businessDynamicsll==null||"".equals(businessDynamicsll)){
                    businessDynamicsll = "0";
                }
                int liulanAll = Integer.valueOf(homePagell)+Integer.valueOf(lifeDynamicsll)+Integer.valueOf(locationDynamicsll)+Integer.valueOf(businessDynamicsll);
                String homePagezf=null;
                String lifeDynamicszf = null;
                String locationDynamicszf = null;
                String businessDynamicszf = null;
                if (forwardInfoList!=null){
                    homePagezf = forwardInfoList.getString("homePage");
                    lifeDynamicszf = forwardInfoList.getString("lifeDynamics");
                    locationDynamicszf = forwardInfoList.getString("locationDynamics");
                    businessDynamicszf = forwardInfoList.getString("businessDynamics");
                }
                if (homePagezf==null||"".equals(homePagezf)){
                    homePagezf = "0";
                }
                if (lifeDynamicszf==null||"".equals(lifeDynamicszf)){
                    lifeDynamicszf = "0";
                }
                if (locationDynamicszf==null||"".equals(locationDynamicszf)){
                    locationDynamicszf = "0";
                }
                if (businessDynamicszf==null||"".equals(businessDynamicszf)){
                    businessDynamicszf = "0";
                }
                int zhufaAll = Integer.valueOf(homePagezf)+Integer.valueOf(lifeDynamicszf)+Integer.valueOf(locationDynamicszf)+Integer.valueOf(businessDynamicszf);
                tongjiAll = liulanAll+zhufaAll+praiseTime+PertinentTimes+BadTime-Integer.valueOf(llzhufAll);
                bundle2.putInt("tongjiAll",tongjiAll);
                fragmentProfile.onResume();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dingdanac,e",volleyError.toString());
            }
        });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }



    private void GetMessageListInfo(final int size){

        all = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {

                final UserManager userManager = UserManager.getInstance();

                String url = "https://mimc.chat.xiaomi.net/api/contact/";

                String token = userManager.getUser().getAppAccount();

                OkHttpClient client = new OkHttpClient();
                okhttp3.Request request = new okhttp3.Request
                        .Builder()
                        .url(url)
                        .addHeader("token",userManager.getUser().getToken())
                        .addHeader("Content-Type","application/json;charset=UTF-8")
                        .build();

                Call call = client.newCall(request);
                JSONObject data = null;
                try {
                    okhttp3.Response response = call.execute();
                    data = new JSONObject(response.body().string());
                    int code = data.getInt("code");
                    if (code != 200) {
                        //logger.warn("Error, code = " + code);

                    }else {

                        JSONArray array = data.getJSONArray("data");

                        List<JSONObject> datas = new ArrayList<JSONObject>();

                        for (int i=0 ; i<array.length() ; i ++){

                            JSONObject object = (JSONObject)array.get(i);

                            String extra = object.getString("extra");
                            String timestamp = object.getString("timestamp");

                            if (extra != null && !extra.equals("") && !extra.equals("null")){

                                if (Long.valueOf(extra)<Long.valueOf(timestamp)){

                                    String username = object.getString("name");

                                    SharedPreferences sp = getSharedPreferences("sangu_message_info", Context.MODE_PRIVATE);

                                    String count = sp.getString(username + "message","0");

                                    if (count.equals("0")){
                                    }else {

                                        all = all + Integer.valueOf(count);

                                    }

                                }

                            }else {

                            }

                        }

                    }

                } catch (Exception e) {
                    //logger.warn("Get token exception: " + e);
                    e.printStackTrace();
                }



                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (all+size > 0) {
                            unreadLabel.setVisibility(View.VISIBLE);
                            unreadLabel.setText(String.valueOf(all+size));
                        } else {
                            unreadLabel.setVisibility(View.INVISIBLE);
                        }

                    }
                });

            }

        }).start();

    }



    private void queryAllUnread() {
        String url = FXConstant.URL_QUERY_UNREADUSER+DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    if (s==null||"".equals(s)){
                        Log.e("dingdanac","offSendOrderCount为空");
                        if (showTishi){
                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                                    != PackageManager.PERMISSION_GRANTED) {
                                tv1.setVisibility(View.INVISIBLE);
                            }else{
                                searchFriend();
                            }
                        }
                    }else {
                        JSONObject object = new JSONObject(s);
                        int addfriend = object.getInt("addFriendCount");
                        int joincompany = object.getInt("joinCompanyCount");
                        billCount = object.getInt("billCount");
                        thirdPartyCount = object.getInt("thirdPartyCount");
                        allUnreadchu = object.getInt("cusmUnReadCount");
                        allUnreadru = object.getInt("orderUnReadCount");
                        contactUnread = joincompany+addfriend;
                        bundle2.putInt("billCount",billCount);
                        bundle2.putInt("thirdPartyCount",thirdPartyCount);
                        fragmentProfile.loadDanju();
                        if (contactUnread>0){
                         //   unreadAddressLable.setVisibility(View.VISIBLE);
                         //   unreadAddressLable.setText(contactUnread+"");
                            if (showTishi){
                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    tv1.setVisibility(View.INVISIBLE);
                                }else{
                                    searchFriend();
                                }
                            }
                        }else {
                            unreadAddressLable.setVisibility(View.INVISIBLE);
                            if (showTishi){
                                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    tv1.setVisibility(View.VISIBLE);
                                }else{
                                    searchFriend();
                                }
                            }
                        }
                        myhandler.sendEmptyMessage(0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dingdanac,e",volleyError.toString());
            }
        });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }

    private void searchFriend() {
        MyRunnable runnable = new MyRunnable(this);
        new Thread(runnable).start();
    }

    private static class MyRunnable implements Runnable{

        WeakReference<MainActivity> mactivity;

        public MyRunnable(MainActivity activity){
            mactivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void run() {
            SyAddressBookUtil.builder()
                    .loader(mactivity.get())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(List<KyqInfo> listsKyq, List<String> list_selected, JSONArray arrayKyq,double per) {
                            if (arrayKyq!=null&&arrayKyq.length()>0) {
                                int size = arrayKyq.length() + mactivity.get().contactUnread;
                                if (size>99){
                                    size = 99;
                                }
                                mactivity.get().tv1.setVisibility(View.GONE);
                              //  mactivity.get().unreadAddressLable.setVisibility(View.VISIBLE);
                               // mactivity.get().unreadAddressLable.setText(size+"");
                            }else {
                                mactivity.get().tv1.setVisibility(View.GONE);
                            }
                            mactivity.get().showTishi = false ;
                        }
                    })
                    .error(new IError() {
                        @Override
                        public void onError(String msg) {
                        }
                    })
                    .build()
                    .getKyqList();
        }
    }

    private void queryUnreadCount() {
        String url = FXConstant.URL_QUERY_UNREADQIYE+ resv5;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    if (s==null||"".equals(s)){
                        Log.e("dingdanac","offSendOrderCount为空");
                    }else {
                        JSONObject object = new JSONObject(s);
                        int qjcount = object.getInt("leaveCount");
                        int bbcount = object.getInt("reportCount");
                        int ddcount = object.getInt("orderCount");
                        allUnreadqy = qjcount+bbcount+ddcount;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dingdanac,e",volleyError.toString());
            }
        });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
        super.onStop();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level==TRIM_MEMORY_COMPLETE){
            EMClient.getInstance().chatManager().removeMessageListener(messageListener);
            DemoHelper sdkHelper = DemoHelper.getInstance();
            sdkHelper.popActivity(this);
            ImageLoader.getInstance().clearMemoryCache();
            if (mLocationClient!=null){
                mLocationClient.stop();
            }
            if (fragmentFind!=null){
                fragmentFind.onLowMemory();
            }
            if (contactListFragment!=null){
                contactListFragment.onLowMemory();
            }
        }else if (level==TRIM_MEMORY_BACKGROUND){
            if (mLocationClient!=null){
                mLocationClient.stop();
            }
            if (contactListFragment!=null){
                contactListFragment.onLowMemory();
            }
        }else if (level==TRIM_MEMORY_RUNNING_CRITICAL){
            ImageLoader.getInstance().clearMemoryCache();
            if (mLocationClient!=null){
                mLocationClient.stop();
            }
            if (fragmentFind!=null){
                fragmentFind.onLowMemory();
            }
            if (contactListFragment!=null){
                contactListFragment.onLowMemory();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private BroadcastReceiver internalDebugReceiver;

    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    /**
     * show the dialog when user logged into another device
     */
    private void showConflictDialog() {
        SharedPreferences sp2 = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
        if (sp2!=null) {
            SharedPreferences.Editor editor1 = sp2.edit();
            editor1.clear();
            editor1.commit();
        }
        if (sp!=null) {
            SharedPreferences.Editor editor1 = sp.edit();
            editor1.clear();
            editor1.commit();
        }
        isConflictDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        Intent intent = new Intent(MainActivity.this, MainTwoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    /**
     * show the dialog if user account is removed
     */
    private void showAccountRemovedDialog() {
        SharedPreferences sp2 = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
        if (sp2!=null) {
            SharedPreferences.Editor editor1 = sp2.edit();
            editor1.clear();
            editor1.commit();
        }
        if (sp!=null) {
            SharedPreferences.Editor editor1 = sp.edit();
            editor1.clear();
            editor1.commit();
        }
        isAccountRemovedDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        finish();
                        startActivity(new Intent(MainActivity.this, MainTwoActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationClient!=null){
            mLocationClient.stop();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    /**
     * debug purpose only, you can ignore this
     */
    private void registerInternalDebugReceiver() {
        internalDebugReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                DemoHelper.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                SharedPreferences sp2 = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
                                SharedPreferences sp = getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
                                if (sp2!=null) {
                                    SharedPreferences.Editor editor1 = sp2.edit();
                                    editor1.clear();
                                    editor1.commit();
                                }
                                if (sp!=null) {
                                    SharedPreferences.Editor editor1 = sp.edit();
                                    editor1.clear();
                                    editor1.commit();
                                }
                                finish();
                                startActivity(new Intent(MainActivity.this, MainTwoActivity.class));
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

    public void addConersationListPush(int size) {

        if (index != 1){

            GetMessageListInfo(size);

        }

//        int count = getUnreadMsgCountTotal();
//        if (count + size > 0) {
//            unreadLabel.setText(String.valueOf(count + size));
//            unreadLabel.setVisibility(View.VISIBLE);
//        } else {
//            unreadLabel.setVisibility(View.INVISIBLE);
//        }
    }

    private void insertLoginLog(final String lat, final String lng, final String location) {
        if (DemoHelper.getInstance().getCurrentUsernName() == null || TextUtils.isEmpty(DemoHelper.getInstance().getCurrentUsernName())) {
            return;
        }
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        final String time = format.format(new Date(System.currentTimeMillis()));

        StringRequest request = new StringRequest(Request.Method.POST,FXConstant.URL_INSERT_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "登陆插入log" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "登陆插入logonErrorResponse" + volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //u_id loginTime（时间戳） deviceType  resv1（经度） resv2（纬度） uLocation地址
                Map<String,String> param = new HashMap<>();
                param.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                param.put("loginTime",time);
                param.put("deviceType","android4.5正事多");
                param.put("resv1",lng);
                param.put("resv2",lat);
                if ( location != null) {
                    param.put("uLocation",location);
                }
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,  String[] permissions,
//                                           int[] grantResults) {
////        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
//        if (requestCode==1){
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                upDateInstall(0);
//                // permission was granted, yay! Do the
//                // contacts-related task you need to do.
//
//            } else {
//                upDateInstall(1);
//                Toast.makeText(getApplicationContext(),"拒绝权限可能会造成应用崩溃，请手动打开！",Toast.LENGTH_LONG).show();
//                // permission denied, boo! Disable the
//                // functionality that depends on this permission.
//            }
//            return;
//        }
//    }


}
