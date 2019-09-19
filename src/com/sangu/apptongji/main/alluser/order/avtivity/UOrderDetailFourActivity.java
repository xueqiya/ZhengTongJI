package com.sangu.apptongji.main.alluser.order.avtivity;

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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.utils.FileStorage;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.GridImageAdapter;
import com.sangu.apptongji.main.address.AddressListTwoActivity;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.fragment.MainActivity;
import com.sangu.apptongji.main.utils.CashierInputFilter;
import com.sangu.apptongji.main.utils.FullyGridLayoutManager;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.utils.ZhifuHelpUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.PreferenceManager;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yalantis.ucrop.entity.LocalMedia;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017-08-18.
 */

public class UOrderDetailFourActivity extends BaseActivity implements IPriceView,OnClickListener{
    private static final int SDK_PAY_FLAG = 12;
    private ArrayList<String> imagePaths1=new ArrayList<>();
    private ArrayList<String> imagePaths2=new ArrayList<>();
    private String biaoshi=null,orderId,pass,task_label,createTime="",dynamicSeq="",orderBody;
    private String hxid,companyName=null,companyAdress=null,managerId=null,wodezhanghao,zongjia,typeDetail,zy1,
            orderProject,orderNumber,merId;
    private List<LocalMedia> selectMedia1 = new ArrayList<>();
    private List<LocalMedia> selectMedia2 = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private RecyclerView recyclerView2;
    private GridImageAdapter adapter2;
    private int errorTime=3;
    private IPricePresenter pricePresenter;

    private RelativeLayout countDown=null,rlt1=null;
    private TextView daysTv,hoursTv=null, minutesTv=null,secondsTv=null,tv2=null,tv_weishezhi;
    String hours = "00",mins="00",second="00",time="";
    private long mDay;
    private long mHour;
    private long mMin;
    private long mSecond=00;
    private boolean isRun = true;
    private boolean isJian = true;
    private Handler timeHandler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                tv2.setText("距离约定时间");
                computeTime();
                if ((mDay==0&&mHour==0&&mMin==0&&mSecond==0)||(mDay==0&&mHour==00&&mMin==00&&mSecond==00)) {
                    isJian = false;
                }
            }else if (msg.what==2){
                tv2.setText("超出约定时间");
                addTime();
            }
            if (mDay==0){
                daysTv.setVisibility(View.GONE);
            }else {
                daysTv.setVisibility(View.VISIBLE);
            }
            if (mHour<10){
                hours = "0"+mHour;
            }else {
                hours = String.valueOf(mHour);
            }
            if (mMin<10){
                mins = "0"+mMin;
            }else {
                mins = String.valueOf(mMin);
            }
            if (mSecond<10){
                second = "0"+mSecond ;
            }else {
                second = String.valueOf(mSecond);
            }
            daysTv.setText(mDay+"天");
            hoursTv.setText(hours);
            minutesTv.setText(mins);
            secondsTv.setText(second);
        }
    };
    private RelativeLayout layout;

    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        if (isJian){
                            message.what = 1;
                        }else {
                            message.what = 2;
                        }
                        timeHandler1.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void addTime() {
        mSecond++;
        if (mSecond>59){
            mMin++;
            mSecond = 0;
            if (mMin>59){
                mMin = 0;
                mHour++;
                if (mHour>23){
                    mHour = 0;
                    mDay++;
                }
            }
        }
    }
    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour<0){
                    mHour = 24;
                    mDay--;
                }
            }
        }
    }

    private ProgressDialog pd;
    private CustomProgressDialog mProgress=null;
    int allSize=0,downloadSize=0,allSize1=0,allSize2=0;
    String filePath1=null,filePath2=null,filePath3=null,filePath4=null,filePath5=null,filePath6=null,filePath7=null,filePath8=null;
    private EditText et_miaoshu=null,et_name=null,et_phone=null,et_dizhi=null,et_feiyong=null,et_Ubeizhu;
    private TextView tv_kehu=null,tv_kehu_time=null,tv_jiedan=null,tv_jiedan_time=null,tv_paidan_id1,tv_paidan_id1_time,
            tv_paidan_caiwu,tv_paidan_caiwu_time,tv_paidan_kehu,tv_paidan_kehu_time,tv_yanzi,
            tev_kehu=null,tev_jiedan=null,tv_fasong=null,tev_paidan_id1,tev_paidan_caiwu,tev_paidan_kehu,tv_count_fankui;
    private LinearLayout ll3_paidan,ll2,ll2_paidan,ll_btn;
    private RelativeLayout rl_xinxi2=null,rl_miaoshu=null,rl_btn1,rlM_beizhu,rl_paidan_id1;
    private ImageView iv_kehu=null,iv_jiedan=null,iv_paidan_id1,iv_paidan_caiwu,iv_paidan_kehu;
    private Button btn_comit=null,btn_cansul,btn_comit2;
    private IWXAPI api=null;
    private Double yuE;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (allSize==downloadSize){
                        imagePaths1.clear();
                        imagePaths2.clear();
                        int type = FunctionConfig.TYPE_IMAGE;
                        if (allSize1>0) {
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths1.add(filePath1);
                            media.setNum(imagePaths1.size());
                            media.setPath(filePath1);
                            selectMedia1.add(media);
                        }
                        if (allSize1>1){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths1.add(filePath2);
                            media.setNum(imagePaths1.size());
                            media.setPath(filePath2);
                            selectMedia1.add(media);
                        }
                        if (allSize1>2){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths1.add(filePath3);
                            media.setNum(imagePaths1.size());
                            media.setPath(filePath3);
                            selectMedia1.add(media);
                        }
                        if (allSize1>3){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths1.add(filePath4);
                            media.setNum(imagePaths1.size());
                            media.setPath(filePath4);
                            selectMedia1.add(media);
                        }
                        if (allSize1>0) {
                            Log.e("uorderdef,s1",selectMedia1.size()+"");
                            adapter.notifyDataSetChanged();
                        }
                        if (allSize2>0) {
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths2.add(filePath5);
                            media.setNum(imagePaths2.size());
                            media.setPath(filePath5);
                            selectMedia2.add(media);
                        }
                        if (allSize2>1){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths2.add(filePath6);
                            media.setNum(imagePaths2.size());
                            media.setPath(filePath6);
                            selectMedia2.add(media);
                        }
                        if (allSize2>2){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths2.add(filePath7);
                            media.setNum(imagePaths2.size());
                            media.setPath(filePath7);
                            selectMedia2.add(media);
                        }
                        if (allSize2>3){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths2.add(filePath8);
                            media.setNum(imagePaths2.size());
                            media.setPath(filePath8);
                            selectMedia2.add(media);
                        }
                        if (allSize2>0) {
                            Log.e("uorderdef,s2",selectMedia2.size()+"");
                            adapter2.notifyDataSetChanged();
                        }
                        if (mProgress!=null&&mProgress.isShowing()) {
                            mProgress.dismiss();
                        }
                    }
                    break;
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if ("04".equals(biaoshi)){
                            ChangeState();
                            updateUscount(hxid);
                        }
                        if (hxid.length()>12){
                            sendPushMessage(managerId,"002");
                        }else {
                            sendPushMessage(hxid,"000");
                        }
                        Toast.makeText(UOrderDetailFourActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
                        zongjia = et_feiyong.getText().toString().trim();
                        final double orderSu = Double.valueOf(zongjia);
                        yuE = yuE - orderSu;
                        DemoApplication.getApp().saveCurrentPrice(yuE);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(UOrderDetailFourActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String zhifuZhuangtai = intent.getStringExtra("zhifu");
        if (zhifuZhuangtai!=null&&"支付成功".equals(zhifuZhuangtai)){
            if ("04".equals(biaoshi)){
                ChangeState();
                updateUscount(hxid);
            }
            if (hxid.length()>12){
                sendPushMessage(managerId,"002");
            }else {
                sendPushMessage(hxid,"000");
            }
            Toast.makeText(UOrderDetailFourActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
            zongjia = et_feiyong.getText().toString().trim();
            final double orderSu = Double.valueOf(zongjia);
            yuE = yuE - orderSu;
            DemoApplication.getApp().saveCurrentPrice(yuE);
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_order_moshisi);
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        WeakReference<UOrderDetailFourActivity> reference =  new WeakReference<UOrderDetailFourActivity>(UOrderDetailFourActivity.this);
        pricePresenter = new PricePresenter(this,this);
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        imagePaths1 = new ArrayList<>();
        imagePaths2 = new ArrayList<>();
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        companyName = this.getIntent().hasExtra("companyName")?this.getIntent().getStringExtra("companyName"):"0";
        companyAdress = this.getIntent().hasExtra("companyAdress")?this.getIntent().getStringExtra("companyAdress"):"0";
        managerId = this.getIntent().getStringExtra("managerId");
        orderBody = this.getIntent().getStringExtra("orderBody");
        zy1 = this.getIntent().getStringExtra("zy1");
        hxid = this.getIntent().getStringExtra("hxid");
        wodezhanghao = DemoHelper.getInstance().getCurrentUsernName();
        typeDetail = "01";
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        task_label = this.getIntent().hasExtra("task_label")?this.getIntent().getStringExtra("task_label"):"";
        createTime = this.getIntent().hasExtra("createTime")?this.getIntent().getStringExtra("createTime"):"";
        dynamicSeq = this.getIntent().hasExtra("dynamicSeq")?this.getIntent().getStringExtra("dynamicSeq"):"";
        initView();
        initViews();
        tv_weishezhi.setVisibility(View.VISIBLE);
        rlM_beizhu.setVisibility(View.GONE);
        btn_comit.setText("验    资");
        rl_miaoshu.setFocusable(true);
        rl_miaoshu.setFocusableInTouchMode(true);
        rl_miaoshu.requestFocus();
        if (biaoshi.equals("01")){
            String balance = this.getIntent().getStringExtra("balance");
            et_feiyong.setText(balance);
            et_feiyong.setEnabled(false);
        }
        if ("00".equals(biaoshi)){
            tv_fasong.setVisibility(View.VISIBLE);
            tv_fasong.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = FXConstant.URL_Get_UserInfo+hxid;
                    StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            com.alibaba.fastjson.JSONObject objects = JSON.parseObject(s);
                            com.alibaba.fastjson.JSONObject object = objects.getJSONObject("userInfo");
                            String uName = object.getString("uName");
                            String uImage = object.getString("uImage");
                            String share = object.getString("shareRed");
                            String friendsNumber = object.getString("friendsNumber");
                            final String msgType = "单聊";
                            String chatImg = uImage;
                            String shareRed = null;
                            if (chatImg==null||"".equals(chatImg)){
                                chatImg = "0";
                            }else {
                                chatImg = chatImg.split("\\|")[0];
                            }
                            String chatName = uName;
                            if (chatName==null||"".equals(chatName)){
                                chatName = hxid;
                            }
                            String onceJine = null;
                            if (friendsNumber!=null&&!"".equals(friendsNumber)){
                                onceJine = friendsNumber.split("\\|")[0];
                            }
                            if (share!=null&&!"".equals(share)&& Double.parseDouble(share)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
                                shareRed = "有";
                            }else {
                                shareRed = "无";
                            }

                            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
                            dialog.show();
                            dialog.getWindow().setContentView(layout);
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setCancelable(true);
                            TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                            Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                            final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                            btnOK.setText("确定");
                            btnCancel.setText("取消");
                            title_tv.setText("是否发送给对方由对方编辑详情？");
                            btnCancel.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            final String finalChatImg = chatImg;
                            final String finalChatName = chatName;
                            final String finalShareRed = shareRed;
                            btnOK.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    ScreenshotUtil.getBitmapByView2(UOrderDetailFourActivity.this, findViewById(R.id.ll_all),false);
                                    String url = FXConstant.URL_INSERT_ORDER;
                                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String s) {
                                            try {
                                                JSONObject obj = new JSONObject(s);
                                                String code = obj.getString("code");
                                                JSONObject object = obj.getJSONObject("orderInfo");
                                                orderId = object.getString("orderId");
                                                if (code.equals("数据更新成功")) {
                                                    String merId = hxid;
                                                    String userId = wodezhanghao;
                                                    String filePath = Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png";
                                                    EMMessage message = EMMessage.createImageSendMessage(filePath,false,merId);
                                                    message.setAttribute("flg","04");
                                                    message.setAttribute("merId",merId);
                                                    message.setAttribute("shareId",orderId);
                                                    message.setAttribute("userId",userId);
                                                    message.setAttribute("types","编辑订单");
                                                    String userPic = PreferenceManager.getInstance().getCurrentUserAvatar();
                                                    if (!TextUtils.isEmpty(userPic)){
                                                        message.setAttribute("userPic",userPic);
                                                    }
                                                    String userName = PreferenceManager.getInstance().getCurrentUserNick();
                                                    if (!TextUtils.isEmpty(userName)){
                                                        message.setAttribute("userName",userName);
                                                    }
                                                    message.setAttribute(DemoHelper.getInstance().getCurrentUsernName(), userPic+"|"+userName);
                                                    message.setAttribute(merId, finalChatImg +"|"+ finalChatName);
                                                    message.setAttribute("name", finalChatName);
                                                    message.setAttribute("type",msgType);
                                                    message.setAttribute("shareRed", finalShareRed);
                                                    EMClient.getInstance().chatManager().sendMessage(message);
                                                    Toast.makeText(UOrderDetailFourActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(UOrderDetailFourActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            Log.e("onResponse,s",volleyError.getMessage());
                                        }
                                    }) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("userId", wodezhanghao);
                                            params.put("merId", hxid);
                                            params.put("orderBody",orderBody);
                                            params.put("flg", "04");
                                            params.put("type", typeDetail);
                                            params.put("upId", zy1);
                                            return params;
                                        }
                                    };
                                    MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
                                }
                            });
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    });
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                }
            });
        }
        if ("03".equals(biaoshi)){
            tv_yanzi.setText("报  价");
            initView3();
        }else if ("04".equals(biaoshi)) {
            tv_yanzi.setText("报  价");
            initView4();
        }else if ("05".equals(biaoshi)){
            tv_fasong.setVisibility(View.VISIBLE);
            initView5();
        }else if ("06".equals(biaoshi)){
            initView6();
        }else if ("07".equals(biaoshi)){
            tv_yanzi.setText("报  价");
            initView7();
        }else if ("08".equals(biaoshi)){
            initView8();
        }else {
            btn_comit.setOnClickListener(this);
        }
    }

    private void initView8() {
        et_feiyong.setEnabled(true);
        et_miaoshu.setEnabled(true);
        et_name.setEnabled(true);
        et_dizhi.setEnabled(true);
        et_phone.setEnabled(true);
        et_Ubeizhu.setEnabled(true);
        btn_cansul.setVisibility(View.GONE);
        btn_comit.setEnabled(true);
        btn_comit.setText("发  送");
        btn_comit.setWidth(200);
        tv_fasong.setVisibility(View.VISIBLE);
        tv_fasong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtoself();
            }
        });
        btn_comit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtoself();
            }
        });
    }

    private void sendtoself() {
        zongjia = et_feiyong.getText().toString().trim();
        orderProject = et_miaoshu.getText().toString().trim();
        String name = TextUtils.isEmpty(et_name.getText().toString().trim())?"0":et_name.getText().toString().trim();
        String phone = TextUtils.isEmpty(et_phone.getText().toString().trim())?"0":et_phone.getText().toString().trim();
        String dizhi = TextUtils.isEmpty(et_dizhi.getText().toString().trim())?"0":et_dizhi.getText().toString().trim();
        orderNumber = name+"|"+phone+"|"+dizhi;
        zongjia = et_feiyong.getText().toString().trim();
        if (zongjia==null||"".equals(zongjia)||Double.parseDouble(zongjia)<=0){
            Toast.makeText(getApplicationContext(),"请输入总价！",Toast.LENGTH_SHORT).show();
            return;
        }
        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_order_send, null);
        final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
        final EditText et_phone3 = (EditText) dialog.findViewById(R.id.et_phone3);
        Button btn_phone3 = (Button) dialog.findViewById(R.id.btn_phone3);
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (ContextCompat.checkSelfPermission(UOrderDetailFourActivity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UOrderDetailFourActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                }else {
                    Intent intent = new Intent(UOrderDetailFourActivity.this,AddressListTwoActivity.class);
                    intent.putExtra("biaoshi","05");
                    intent.putExtra("orderBody",orderBody);
                    intent.putExtra("flg","04");
                    intent.putExtra("upId",zy1);
                    intent.putStringArrayListExtra("imagePaths1",imagePaths1);
                    intent.putStringArrayListExtra("imagePaths2",imagePaths2);
                    intent.putExtra("orderProject",orderProject);
                    intent.putExtra("orderNumber",orderNumber);
                    intent.putExtra("orderSum",zongjia);
                    intent.putExtra("conventionTime",time);
                    startActivityForResult(intent,0);
                }
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent3 = new Intent(UOrderDetailFourActivity.this,FriendActivity.class);
                intent3.putExtra("biaoshi","05");
                intent3.putExtra("orderBody",orderBody);
                intent3.putExtra("flg","04");
                intent3.putExtra("upId",zy1);
                intent3.putStringArrayListExtra("imagePaths1",imagePaths1);
                intent3.putStringArrayListExtra("imagePaths2",imagePaths2);
                intent3.putExtra("orderProject",orderProject);
                intent3.putExtra("orderNumber",orderNumber);
                intent3.putExtra("orderSum",zongjia);
                intent3.putExtra("conventionTime",time);
                startActivityForResult(intent3,0);
            }
        });
        btn_phone3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String userId = et_phone3.getText().toString().trim();
                if (TextUtils.isEmpty(userId)||userId.length()!=11) {
                    Toast.makeText(getApplicationContext(), "请输入正确格式的电话号码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                queryUserInfo(userId);
            }
        });
        re_item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void queryUserInfo(final String userId) {
        String url = FXConstant.URL_Get_UserInfo+userId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("用户名为空".equals(code)){
                    insertOrder2(userId,"00");
                }else {
                    insertOrder2(userId,"01");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private void insertOrder2(final String userId,final String biaoshi){
        String url = FXConstant.URL_INSERT_ORDER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    String code = obj.getString("code");
                    JSONObject object = obj.getJSONObject("orderInfo");
                    orderId = object.getString("orderId");
                    merId = object.getString("merId");
                    if (code.equals("数据更新成功")) {
                        bianji2(userId,biaoshi);
                    } else {
                        Toast.makeText(UOrderDetailFourActivity.this, "下单失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("onResponse,s",volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("orderBody",orderBody);
                params.put("flg", "04");
                params.put("type", typeDetail);
                params.put("upId", zy1);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private void bianji2(final String userId, final String biaoshi){
        String path1 = null,path2 = null,path3 = null,path4 = null,path5 = null,path6 = null,path7 = null,path8 = null;
        if (imagePaths1.size()>0){
            path1 = imagePaths1.get(0);
        }
        if (imagePaths1.size()>1){
            path2 = imagePaths1.get(1);
        }
        if (imagePaths1.size()>2){
            path3 = imagePaths1.get(2);
        }
        if (imagePaths1.size()>3){
            path4 = imagePaths1.get(3);
        }
        if (imagePaths2.size() > 0) {
            path5 = imagePaths2.get(0);
        }
        if (imagePaths2.size() > 1) {
            path6 = imagePaths2.get(1);
        }
        if (imagePaths2.size() > 2) {
            path7 = imagePaths2.get(2);
        }
        if (imagePaths2.size() > 3) {
            path8 = imagePaths2.get(3);
        }
        zongjia = et_feiyong.getText().toString().trim();
        orderProject = et_miaoshu.getText().toString().trim();
        String name = TextUtils.isEmpty(et_name.getText().toString().trim())?"0":et_name.getText().toString().trim();
        String phone = TextUtils.isEmpty(et_phone.getText().toString().trim())?"0":et_phone.getText().toString().trim();
        String dizhi = TextUtils.isEmpty(et_dizhi.getText().toString().trim())?"0":et_dizhi.getText().toString().trim();
        orderNumber = name+"|"+phone+"|"+dizhi;
        List<Param> param=new ArrayList<>();
        if (!"".equals(time)) {
            param.add(new Param("conventionTime",time));
        }
        param.add(new Param("state", "02"));
        param.add(new Param("orderId",orderId));
        param.add(new Param("userId", userId));
        param.add(new Param("orderProject", orderProject));
        param.add(new Param("orderNumber", orderNumber));
        param.add(new Param("orderSum", zongjia));
        String url = FXConstant.URL_INSERT_OrderDetail;
        OkHttpManager.getInstance().posts2(param, null, new ArrayList<String>(), null, new ArrayList<String>(),"image01", path1, "image02", path2, "image03", path3, "image04", path4
                , "image05", path5, "image06", path6, "image07", path7, "image08", path8, url, new OkHttpManager.HttpCallBack() {
                    @Override
                    public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                        String code = jsonObject.getString("code");
                        if (code.equals("数据更新成功")) {
                            sendPushMessage2(userId,0);
                            updateUscount(userId);
                            if ("00".equals(biaoshi)){
                                SendMessage1(userId);
                            }else {
                                SendMessage(userId);
                            }
                        } else {
                            Toast.makeText(UOrderDetailFourActivity.this, "编辑错误！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        if (mProgress!=null){
                            if (mProgress.isShowing()){
                                mProgress.dismiss();
                                mProgress=null;
                            }
                        }
                        Toast.makeText(UOrderDetailFourActivity.this,"网络连接错误,派单失败",Toast.LENGTH_SHORT).show();
                        if (errorMsg!=null) {
                            Log.e("offlineorder,e", errorMsg);
                        }
                    }
                });
    }

    private void SendMessage1(final String userId) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】您有一个（"+orderBody+")的订单，需要验资，请在“正事多”手机端操作完成后，即可提供相应服务");
                param.put("telNum", userId);
                Log.e("utorderdeac,sm1",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void SendMessage(final String userId) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】您有一个（"+orderBody+")的订单，需要验资，请在“正事多”手机端操作完成后，即可提供相应服务");
                param.put("telNum", userId);
                Log.e("utorderdeac,sm",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }
    
    private void initView7() {
        et_feiyong.setEnabled(true);
        et_miaoshu.setEnabled(true);
        et_name.setEnabled(true);
        et_dizhi.setEnabled(true);
        et_phone.setEnabled(true);
        et_Ubeizhu.setEnabled(true);
        String shuju = getIntent().getStringExtra("shuju");
        orderId = getIntent().getStringExtra("orderId");
        dynamicSeq = getIntent().getStringExtra("dynamic_seq");
        createTime = getIntent().getStringExtra("timestamp");
        hxid = getIntent().getStringExtra("u_id");
        zongjia = getIntent().getStringExtra("quote");
        OrderDetail orderDetail = new OrderDetail();
        try {
            JSONObject object1 = new JSONObject(shuju);
            JSONObject object = object1.getJSONObject("odi");
            orderDetail = JSONParser.parseOrderDetail(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String yueding = TextUtils.isEmpty(orderDetail.getResv1())?"":orderDetail.getResv1();
        String resv2 = TextUtils.isEmpty(orderDetail.getResv2())?"":orderDetail.getResv2();
        runTime(yueding,resv2);
        orderProject = TextUtils.isEmpty(orderDetail.getOrderProject())?"":orderDetail.getOrderProject();
        orderNumber = TextUtils.isEmpty(orderDetail.getOrderNumber())?"":orderDetail.getOrderNumber();
        et_feiyong.setText(zongjia);
        et_miaoshu.setText(orderProject);
        String sImage1 = orderDetail.getSimage1();
        String sImage2 = orderDetail.getSimage2();
        String sImage3 = orderDetail.getSimage3();
        String sImage4 = orderDetail.getSimage4();
        String sImage5 = orderDetail.getSimage5();
        String sImage6 = orderDetail.getSimage6();
        String sImage7 = orderDetail.getSimage7();
        String sImage8 = orderDetail.getSimage8();
        if (sImage1 != null&&!"".equals(sImage1)&&!"null".equals(sImage1)) {
            allSize++;
            allSize1++;
        }
        if (sImage2 != null&&!"".equals(sImage2)&&!"null".equals(sImage2)) {
            allSize++;
            allSize1++;
        }
        if (sImage3 != null&&!"".equals(sImage3)&&!"null".equals(sImage3)) {
            allSize++;
            allSize1++;
        }
        if (sImage4 != null&&!"".equals(sImage4)&&!"null".equals(sImage4)) {
            allSize++;
            allSize1++;
        }
        if (sImage5 != null&&!"".equals(sImage5)&&!"null".equals(sImage5)) {
            allSize++;
            allSize2++;
        }
        if (sImage6 != null&&!"".equals(sImage6)&&!"null".equals(sImage6)) {
            allSize++;
            allSize2++;
        }
        if (sImage7 != null&&!"".equals(sImage7)&&!"null".equals(sImage7)) {
            allSize++;
            allSize2++;
        }
        if (sImage8 != null&&!"".equals(sImage8)&&!"null".equals(sImage8)) {
            allSize++;
            allSize2++;
        }
        if (sImage1!=null&&!"".equals(sImage1)&&!sImage1.equalsIgnoreCase("null")){
            if (mProgress!=null&&!mProgress.isShowing()){
                mProgress.show();
            }
            downloadFile(FXConstant.URL_SIGN_FOUR+sImage1,sImage1,1);
        }
        if (sImage2!=null&&!"".equals(sImage2)&&!sImage2.equalsIgnoreCase("null")){
            if (mProgress!=null&&!mProgress.isShowing()){
                mProgress.show();
            }
            downloadFile(FXConstant.URL_SIGN_FOUR+sImage2,sImage2,2);
        }
        if (sImage3!=null&&!"".equals(sImage3)&&!sImage3.equalsIgnoreCase("null")){
            if (mProgress!=null&&!mProgress.isShowing()){
                mProgress.show();
            }
            downloadFile(FXConstant.URL_SIGN_FOUR+sImage3,sImage3,3);
        }
        if (sImage4!=null&&!"".equals(sImage4)&&!sImage4.equalsIgnoreCase("null")){
            if (mProgress!=null&&!mProgress.isShowing()){
                mProgress.show();
            }
            downloadFile(FXConstant.URL_SIGN_FOUR+sImage4,sImage4,4);
        }
        if (sImage5!=null&&!"".equals(sImage5)&&!sImage5.equalsIgnoreCase("null")){
            if (mProgress!=null&&!mProgress.isShowing()){
                mProgress.show();
            }
            downloadFile(FXConstant.URL_SIGN_FOUR+sImage5,sImage5,5);
        }
        if (sImage6!=null&&!"".equals(sImage6)&&!sImage6.equalsIgnoreCase("null")){
            if (mProgress!=null&&!mProgress.isShowing()){
                mProgress.show();
            }
            downloadFile(FXConstant.URL_SIGN_FOUR+sImage6,sImage6,6);
        }
        if (sImage7!=null&&!"".equals(sImage7)&&!sImage7.equalsIgnoreCase("null")){
            if (mProgress!=null&&!mProgress.isShowing()){
                mProgress.show();
            }
            downloadFile(FXConstant.URL_SIGN_FOUR+sImage7,sImage7,7);
        }
        if (sImage8!=null&&!"".equals(sImage8)&&!sImage8.equalsIgnoreCase("null")){
            if (mProgress!=null&&!mProgress.isShowing()){
                mProgress.show();
            }
            downloadFile(FXConstant.URL_SIGN_FOUR+sImage8,sImage8,8);
        }

        btn_comit.setText("修改报价");
        btn_comit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path1 = null,path2 = null,path3 = null,path4 = null,path5 = null,path6 = null,path7 = null,path8 = null;
                if (imagePaths1.size()>0){
                    path1 = imagePaths1.get(0);
                }
                if (imagePaths1.size()>1){
                    path2 = imagePaths1.get(1);
                }
                if (imagePaths1.size()>2){
                    path3 = imagePaths1.get(2);
                }
                if (imagePaths1.size()>3){
                    path4 = imagePaths1.get(3);
                }
                if (imagePaths2.size() > 0) {
                    path5 = imagePaths2.get(0);
                }
                if (imagePaths2.size() > 1) {
                    path6 = imagePaths2.get(1);
                }
                if (imagePaths2.size() > 2) {
                    path7 = imagePaths2.get(2);
                }
                if (imagePaths2.size() > 3) {
                    path8 = imagePaths2.get(3);
                }
                zongjia = et_feiyong.getText().toString().trim();
                orderProject = et_miaoshu.getText().toString().trim();
                String name = TextUtils.isEmpty(et_name.getText().toString().trim())?"0":et_name.getText().toString().trim();
                String phone = TextUtils.isEmpty(et_phone.getText().toString().trim())?"0":et_phone.getText().toString().trim();
                String dizhi = TextUtils.isEmpty(et_dizhi.getText().toString().trim())?"0":et_dizhi.getText().toString().trim();
                orderNumber = name+"|"+phone+"|"+dizhi;

                if (zongjia==null||"".equals(zongjia)||Double.parseDouble(zongjia)<=0){
                    Toast.makeText(getApplicationContext(),"请输入报价！",Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailFourActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
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
                title_tv1.setText("确定修改吗?");
                btnCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                final String finalPath1 = path1;
                final String finalPath2 = path2;
                final String finalPath3 = path3;
                final String finalPath4 = path4;
                final String finalPath5 = path5;
                final String finalPath6 = path6;
                final String finalPath7 = path7;
                final String finalPath8 = path8;
                btnOK1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                        List<Param> param=new ArrayList<>();
                        if (!"".equals(time)) {
                            param.add(new Param("conventionTime",time));
                        }
                        param.add(new Param("order_id",orderId));
                        param.add(new Param("timestamp",createTime));
                        param.add(new Param("dynamic_seq",dynamicSeq));
                        param.add(new Param("u_id", DemoHelper.getInstance().getCurrentUsernName()));
                        param.add(new Param("orderProject", orderProject));
                        param.add(new Param("orderNumber", orderNumber));
                        param.add(new Param("quote", zongjia));
                        String url = FXConstant.URL_UPDATE_BAOJIA;
                        OkHttpManager.getInstance().posts2(param, null, new ArrayList<String>(),null, new ArrayList<String>(), "image01", finalPath1, "image02", finalPath2, "image03", finalPath3, "image04", finalPath4
                                , "image05", finalPath5, "image06", finalPath6, "image07", finalPath7, "image08", finalPath8, url, new OkHttpManager.HttpCallBack() {
                                    @Override
                                    public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                        String code = jsonObject.getString("code");
                                        if ("success".equals(code)){
                                            Toast.makeText(getApplicationContext(),"修改成功！",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(String errorMsg) {
                                        if (mProgress!=null){
                                            if (mProgress.isShowing()){
                                                mProgress.dismiss();
                                                mProgress=null;
                                            }
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_comit:
                final String sum = et_feiyong.getText().toString().trim();
                if (!TextUtils.isEmpty(sum)&&Double.parseDouble(sum)>0) {
                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                    final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                    RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                    RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                    RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                    re_item5.setVisibility(View.VISIBLE);
                    TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                    TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                    TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                    TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
                    tv_title.setText("请选择支付方式");
                    tv_item1.setText("余 额 支 付");
                    tv_item2.setText("微 信 支 付");
                    tv_item5.setText("支付宝支付");
                    re_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            zhifu3(sum);
                        }
                    });
                    re_item2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            insertOrder("02");
                        }
                    });
                    re_item5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            insertOrder("01");
                        }
                    });
                    re_item3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(UOrderDetailFourActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initView6() {
        merId = getIntent().getStringExtra("merId");
        orderId = getIntent().getStringExtra("orderId");
        final String userId = getIntent().getStringExtra("userId");
        hxid = userId;
        et_feiyong.setEnabled(false);
        et_miaoshu.setEnabled(false);
        et_name.setEnabled(false);
        et_dizhi.setEnabled(false);
        et_phone.setEnabled(false);
        et_Ubeizhu.setEnabled(false);
        recyclerView.setEnabled(false);
        recyclerView2.setEnabled(false);
        queryOrder();
    }

    private void reduceShRZFCount(final String times) {
        String url = FXConstant.URL_UPDATEZHHU;
        StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if (code.equals("SUCCESS")){
                    if (errorTime>0) {
                        errorTime--;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                params1.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                params1.put("enterErrorTimes", times+"");
                return params1;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request3);
    }
    
    private void zhifu(final String sum) {
        if (pass==null||"".equals(pass)){
            ZhifuHelpUtils.showErrorMiMaSHZH(this,pass,"000");
            return;
        }
        if (pass.length()!=6||!ZhifuHelpUtils.isNumeric(pass)){
            ZhifuHelpUtils.showErrorMiMaXG(this,pass,"000");
            return;
        }
        if (errorTime<=0){
            ZhifuHelpUtils.showErrorLing(this);
            return;
        }
        final double orderSu = Double.valueOf(sum);
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        Double d = yuE - orderSu;
        if (d >= 0) {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            final PasswordView pwdView = (PasswordView)layout.findViewById(R.id.pwd_view);
            pwdView.setOnFinishInput(new OnPasswordInputFinish() {
                @Override
                public void inputFinish() {
                    pd = new ProgressDialog(UOrderDetailFourActivity.this);
                    pd.setMessage("正在支付...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    if (pwdView.getStrPassword().equals(pass)) {
                        dialog.dismiss();
                        updateUscount(hxid);
                        fukuan();
                    } else {
                        pd.dismiss();
                        int times;
                        if (errorTime>0){
                            times = errorTime-1;
                        }else {
                            times = 0;
                        }
                        reduceShRZFCount(times+"");
                        if (times==0) {
                            ZhifuHelpUtils.showErrorLing(UOrderDetailFourActivity.this);
                        }else {
                            ZhifuHelpUtils.showErrorTishi(UOrderDetailFourActivity.this,times + "",managerId,"000");
                        }
                        dialog.dismiss();
                    }
                }
            });
            /**
             *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
             *  如果写了，会覆盖我们在自定义控件中提供的响应
             *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
            pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            pwdView.getForgetTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bs;
                    bs = "000";
                    startActivity(new Intent(UOrderDetailFourActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
            RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
            RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
            RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
            re_item1.setVisibility(View.GONE);
            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
            TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
            TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
            tv_title.setText("钱包余额不足(剩余"+yuE+"元),请您先充值或者选择其他支付方式");
            tv_item1.setText("");
            tv_item2.setText("微信支付");
            tv_item5.setText("支付宝支付");
            re_item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    rechargefromWx(zongjia,orderId);
                }
            });
            re_item5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    rechargefromZhFb(zongjia,orderId);
                }
            });
            re_item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }


        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    private void zhifu2(final String sum) {
        if (pass==null||"".equals(pass)){
            ZhifuHelpUtils.showErrorMiMaSHZH(this,pass,"000");
            return;
        }
        if (pass.length()!=6||!ZhifuHelpUtils.isNumeric(pass)){
            ZhifuHelpUtils.showErrorMiMaXG(this,pass,"000");
            return;
        }
        if (errorTime<=0){
            ZhifuHelpUtils.showErrorLing(this);
            return;
        }
        final double orderSu = Double.valueOf(sum);
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        Double d = yuE - orderSu;
        if (d >= 0) {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
            final RelativeLayout layout =   (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            final PasswordView pwdView = (PasswordView) layout.findViewById(R.id.pwd_view);
            pwdView.setOnFinishInput(new OnPasswordInputFinish() {
                @Override
                public void inputFinish() {
                    pd = new ProgressDialog(UOrderDetailFourActivity.this);
                    pd.setMessage("正在支付...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    if (pwdView.getStrPassword().equals(pass)) {
                        dialog.dismiss();
                        ChangeState();
                        updateUscount(hxid);
                        fukuan();
                    } else {
                        pd.dismiss();
                        int times;
                        if (errorTime>0){
                            times = errorTime-1;
                        }else {
                            times = 0;
                        }
                        reduceShRZFCount(times+"");
                        if (times==0) {
                            ZhifuHelpUtils.showErrorLing(UOrderDetailFourActivity.this);
                        }else {
                            ZhifuHelpUtils.showErrorTishi(UOrderDetailFourActivity.this,times + "",managerId,"000");
                        }
                        dialog.dismiss();
                    }
                }
            });
            /**
             *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
             *  如果写了，会覆盖我们在自定义控件中提供的响应
             *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
            pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            pwdView.getForgetTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bs;
                    bs = "000";
                    startActivity(new Intent(UOrderDetailFourActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
            RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
            RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
            RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
            re_item1.setVisibility(View.GONE);
            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
            TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
            TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
            tv_title.setText("钱包余额不足(剩余"+yuE+"元),请您先充值或者选择其他支付方式");
            tv_item1.setText("");
            tv_item2.setText("微信支付");
            tv_item5.setText("支付宝支付");
            re_item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    rechargefromWx(zongjia,orderId);
                }
            });
            re_item5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    rechargefromZhFb(zongjia,orderId);
                }
            });
            re_item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    private void zhifu3(String sum) {
        if (pass==null||"".equals(pass)){
            ZhifuHelpUtils.showErrorMiMaSHZH(this,pass,"000");
            return;
        }
        if (pass.length()!=6||!ZhifuHelpUtils.isNumeric(pass)){
            ZhifuHelpUtils.showErrorMiMaXG(this,pass,"000");
            return;
        }
        if (errorTime<=0){
            ZhifuHelpUtils.showErrorLing(this);
            return;
        }
        final double orderSu = Double.valueOf(sum);
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        Double d = yuE - orderSu;
        if (d >= 0) {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            final PasswordView pwdView = (PasswordView)layout.findViewById(R.id.pwd_view);
            pwdView.setOnFinishInput(new OnPasswordInputFinish() {
                @Override
                public void inputFinish() {
                    pd = new ProgressDialog(UOrderDetailFourActivity.this);
                    pd.setMessage("正在支付...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    if (pwdView.getStrPassword().equals(pass)) {
                        dialog.dismiss();
                        insertOrder("00");
                    } else {
                        pd.dismiss();
                        int times;
                        if (errorTime>0){
                            times = errorTime-1;
                        }else {
                            times = 0;
                        }
                        reduceShRZFCount(times+"");
                        if (times==0) {

                            ZhifuHelpUtils.showErrorLing(UOrderDetailFourActivity.this);
                        }else {
                            ZhifuHelpUtils.showErrorTishi(UOrderDetailFourActivity.this,times + "",managerId,"000");
                        }
                        dialog.dismiss();
                    }
                }
            });
            /**
             *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
             *  如果写了，会覆盖我们在自定义控件中提供的响应
             *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
            pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            pwdView.getForgetTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bs;
                    bs = "000";
                    startActivity(new Intent(UOrderDetailFourActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
            RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
            RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
            RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
            re_item1.setVisibility(View.GONE);
            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
            TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
            TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
            tv_title.setText("钱包余额不足(剩余"+yuE+"元),请您先充值或者选择其他支付方式");
            tv_item1.setText("");
            tv_item2.setText("微信支付");
            tv_item5.setText("支付宝支付");
            re_item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    insertOrder("02");
                }
            });
            re_item5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    insertOrder("01");
                }
            });
            re_item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }
    
    private void queryOrder() {
        final String userId = getIntent().getStringExtra("userId");
        String url = FXConstant.URL_Order_Detail;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object1 = new JSONObject(s);
                    JSONObject object = object1.getJSONObject("odi");
                    OrderDetail orderDetail = JSONParser.parseOrderDetail(object);
                    String sImage1 = orderDetail.getSimage1();
                    String sImage2 = orderDetail.getSimage2();
                    String sImage3 = orderDetail.getSimage3();
                    String sImage4 = orderDetail.getSimage4();
                    String sImage5 = orderDetail.getSimage5();
                    String sImage6 = orderDetail.getSimage6();
                    String sImage7 = orderDetail.getSimage7();
                    String sImage8 = orderDetail.getSimage8();
                    String yueding = TextUtils.isEmpty(orderDetail.getResv1())?"":orderDetail.getResv1();
                    String resv2 = TextUtils.isEmpty(orderDetail.getResv2())?"":orderDetail.getResv2();
                    runTime(yueding,resv2);
                    int type = FunctionConfig.TYPE_IMAGE;
                    if (sImage1!=null&&!"".equals(sImage1)&&!sImage1.equalsIgnoreCase("null")){
                        LocalMedia media = new LocalMedia();
                        media.setType(type);
                        media.setCompressed(false);
                        media.setCut(false);
                        media.setIsChecked(true);
                        imagePaths1.add(FXConstant.URL_SIGN_FOUR+sImage1);
                        media.setNum(imagePaths1.size());
                        media.setPath(FXConstant.URL_SIGN_FOUR+sImage1);
                        selectMedia1.add(media);
                    }
                    if (sImage2!=null&&!"".equals(sImage2)&&!sImage2.equalsIgnoreCase("null")){
                        LocalMedia media = new LocalMedia();
                        media.setType(type);
                        media.setCompressed(false);
                        media.setCut(false);
                        media.setIsChecked(true);
                        imagePaths1.add(FXConstant.URL_SIGN_FOUR+sImage2);
                        media.setPath(FXConstant.URL_SIGN_FOUR+sImage2);
                        media.setNum(imagePaths1.size());
                        selectMedia1.add(media);
                    }
                    if (sImage3!=null&&!"".equals(sImage3)&&!sImage3.equalsIgnoreCase("null")){
                        LocalMedia media = new LocalMedia();
                        media.setType(type);
                        media.setCompressed(false);
                        media.setCut(false);
                        media.setIsChecked(true);
                        imagePaths1.add(FXConstant.URL_SIGN_FOUR+sImage3);
                        media.setPath(FXConstant.URL_SIGN_FOUR+sImage3);
                        media.setNum(imagePaths1.size());
                        selectMedia1.add(media);
                    }
                    if (sImage4!=null&&!"".equals(sImage4)&&!sImage4.equalsIgnoreCase("null")){
                        LocalMedia media = new LocalMedia();
                        media.setType(type);
                        media.setCompressed(false);
                        media.setCut(false);
                        media.setIsChecked(true);
                        imagePaths1.add(FXConstant.URL_SIGN_FOUR+sImage4);
                        media.setPath(FXConstant.URL_SIGN_FOUR+sImage4);
                        media.setNum(imagePaths1.size());
                        selectMedia1.add(media);
                    }
                    if (imagePaths1.size()>0){
                        adapter.notifyDataSetChanged();
                    }
                    if (sImage5!=null&&!"".equals(sImage5)&&!sImage5.equalsIgnoreCase("null")){
                        LocalMedia media = new LocalMedia();
                        media.setType(type);
                        media.setCompressed(false);
                        media.setCut(false);
                        media.setIsChecked(true);
                        imagePaths2.add(FXConstant.URL_SIGN_FOUR+sImage5);
                        media.setPath(FXConstant.URL_SIGN_FOUR+sImage5);
                        media.setNum(imagePaths2.size());
                        selectMedia2.add(media);
                    }
                    if (sImage6!=null&&!"".equals(sImage6)&&!sImage6.equalsIgnoreCase("null")){
                        LocalMedia media = new LocalMedia();
                        media.setType(type);
                        media.setCompressed(false);
                        media.setCut(false);
                        media.setIsChecked(true);
                        imagePaths2.add(FXConstant.URL_SIGN_FOUR+sImage6);
                        media.setPath(FXConstant.URL_SIGN_FOUR+sImage6);
                        media.setNum(imagePaths2.size());
                        selectMedia2.add(media);
                    }
                    if (sImage7!=null&&!"".equals(sImage7)&&!sImage7.equalsIgnoreCase("null")){
                        LocalMedia media = new LocalMedia();
                        media.setType(type);
                        media.setCompressed(false);
                        media.setCut(false);
                        media.setIsChecked(true);
                        imagePaths2.add(FXConstant.URL_SIGN_FOUR+sImage7);
                        media.setPath(FXConstant.URL_SIGN_FOUR+sImage7);
                        media.setNum(imagePaths2.size());
                        selectMedia2.add(media);
                    }
                    if (sImage8!=null&&!"".equals(sImage8)&&!sImage8.equalsIgnoreCase("null")){
                        LocalMedia media = new LocalMedia();
                        media.setType(type);
                        media.setCompressed(false);
                        media.setCut(false);
                        media.setIsChecked(true);
                        imagePaths2.add(FXConstant.URL_SIGN_FOUR+sImage8);
                        media.setPath(FXConstant.URL_SIGN_FOUR+sImage8);
                        media.setNum(imagePaths2.size());
                        selectMedia2.add(media);
                    }
                    if (imagePaths2.size()>0){
                        adapter2.notifyDataSetChanged();
                    }
                    String name = null,phone= null,dizhi= null;
                    String [] str = null;
                    orderProject = TextUtils.isEmpty(orderDetail.getOrderProject())?"":orderDetail.getOrderProject();
                    orderNumber = TextUtils.isEmpty(orderDetail.getOrderNumber())?"":orderDetail.getOrderNumber();
                    zongjia = orderDetail.getOrderSum();
                    et_feiyong.setText(zongjia);
                    et_miaoshu.setText(orderProject);
                    if (orderNumber!=null&&!"".equals(orderNumber)){
                        str = orderNumber.split("\\|");
                    }
                    if (str.length>0){
                        name = str[0];
                    }
                    if (str.length>1){
                        phone = str[1];
                    }
                    if (str.length>2){
                        dizhi = str[2];
                    }
                    if (name!=null&&!"0".equals(name)){
                        et_name.setText(name);
                    }
                    if (phone!=null&&!"0".equals(phone)){
                        et_phone.setText(phone);
                    }
                    if (dizhi!=null&&!"0".equals(dizhi)){
                        et_dizhi.setText(dizhi);
                    }
                    et_feiyong.setEnabled(false);
                    if (wodezhanghao.equals(userId)) {
                        btn_comit.setEnabled(true);
                        btn_comit.setText("验  资");
                        btn_comit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String sum = et_feiyong.getText().toString().trim();
                                if (!TextUtils.isEmpty(sum)&&Double.parseDouble(sum)>0) {
                                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
                                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                                    final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
                                    dialog.show();
                                    dialog.getWindow().setContentView(layout);
                                    dialog.setCanceledOnTouchOutside(true);
                                    RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                                    RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                                    RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                                    RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                                    re_item5.setVisibility(View.VISIBLE);
                                    TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                                    TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                                    TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                                    TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
                                    tv_title.setText("请选择支付方式");
                                    tv_item1.setText("余 额 支 付");
                                    tv_item2.setText("微 信 支 付");
                                    tv_item5.setText("支付宝支付");
                                    re_item1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
                                            zhifu(sum);
                                        }
                                    });
                                    re_item2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            rechargefromWx(zongjia,orderId);
                                        }
                                    });
                                    re_item5.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            rechargefromZhFb(zongjia,orderId);
                                        }
                                    });
                                    re_item3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }else {
                                    Toast.makeText(UOrderDetailFourActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        et_feiyong.setEnabled(false);
                        btn_comit.setEnabled(false);
                        btn_comit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                        btn_comit.setText("等待对方验资");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                Log.e("uorderdetail,fp",params.toString());
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private void initView5() {
        final String userId = getIntent().getStringExtra("userId");
        merId = getIntent().getStringExtra("merId");
        orderId = getIntent().getStringExtra("orderId");
        hxid = merId;
        if (wodezhanghao.equals(merId)) {
            et_feiyong.setEnabled(true);
            et_miaoshu.setEnabled(true);
            et_name.setEnabled(true);
            et_dizhi.setEnabled(true);
            et_phone.setEnabled(true);
            btn_comit.setEnabled(true);
            btn_comit.setText("发  送");
        }else {
            et_feiyong.setEnabled(false);
            et_miaoshu.setEnabled(false);
            et_name.setEnabled(false);
            et_dizhi.setEnabled(false);
            et_phone.setEnabled(false);
            et_Ubeizhu.setEnabled(false);
            tv_fasong.setVisibility(View.INVISIBLE);
            btn_comit.setEnabled(false);
            btn_comit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            btn_comit.setText("等待对方编辑");
        }
        tv_fasong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = FXConstant.URL_Get_UserInfo+userId;
                StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        com.alibaba.fastjson.JSONObject objects = JSON.parseObject(s);
                        com.alibaba.fastjson.JSONObject object = objects.getJSONObject("userInfo");
                        String uName = object.getString("uName");
                        String uImage = object.getString("uImage");
                        String share = object.getString("shareRed");
                        String friendsNumber = object.getString("friendsNumber");
                        final String msgType = "单聊";
                        String chatImg = uImage;
                        String shareRed = null;
                        if (chatImg==null||"".equals(chatImg)){
                            chatImg = "0";
                        }else {
                            chatImg = chatImg.split("\\|")[0];
                        }
                        String chatName = uName;
                        if (chatName==null||"".equals(chatName)){
                            chatName = userId;
                        }
                        String onceJine = null;
                        if (friendsNumber!=null&&!"".equals(friendsNumber)){
                            onceJine = friendsNumber.split("\\|")[0];
                        }
                        if (share!=null&&!"".equals(share)&& Double.parseDouble(share)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
                            shareRed = "有";
                        }else {
                            shareRed = "无";
                        }

                        zongjia = et_feiyong.getText().toString().trim();
                        if (TextUtils.isEmpty(zongjia)||Double.parseDouble(zongjia)==0){
                            Toast.makeText(UOrderDetailFourActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                        final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                        btnOK.setText("确定");
                        btnCancel.setText("取消");
                        title_tv.setText("是否确定完成编辑并发送回给对方？");
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        final String finalChatImg = chatImg;
                        final String finalChatName = chatName;
                        final String finalShareRed = shareRed;
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                ScreenshotUtil.getBitmapByView2(UOrderDetailFourActivity.this, findViewById(R.id.ll_all),false);
                                String path1 = null,path2 = null,path3 = null,path4 = null,path5 = null,path6 = null,path7 = null,path8 = null;
                                if (imagePaths1.size()>0){
                                    path1 = imagePaths1.get(0);
                                }
                                if (imagePaths1.size()>1){
                                    path2 = imagePaths1.get(1);
                                }
                                if (imagePaths1.size()>2){
                                    path3 = imagePaths1.get(2);
                                }
                                if (imagePaths1.size()>3){
                                    path4 = imagePaths1.get(3);
                                }
                                if (imagePaths2.size() > 0) {
                                    path5 = imagePaths2.get(0);
                                }
                                if (imagePaths2.size() > 1) {
                                    path6 = imagePaths2.get(1);
                                }
                                if (imagePaths2.size() > 2) {
                                    path7 = imagePaths2.get(2);
                                }
                                if (imagePaths2.size() > 3) {
                                    path8 = imagePaths2.get(3);
                                }
                                orderProject = et_miaoshu.getText().toString().trim();
                                String name = TextUtils.isEmpty(et_name.getText().toString().trim())?"0":et_name.getText().toString().trim();
                                String phone = TextUtils.isEmpty(et_phone.getText().toString().trim())?"0":et_phone.getText().toString().trim();
                                String dizhi = TextUtils.isEmpty(et_dizhi.getText().toString().trim())?"0":et_dizhi.getText().toString().trim();
                                orderNumber = name+"|"+phone+"|"+dizhi;

                                List<Param> param=new ArrayList<>();
                                if (!"".equals(time)) {
                                    param.add(new Param("conventionTime",time));
                                }
                                param.add(new Param("orderId",orderId));
                                param.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
                                param.add(new Param("orderProject", orderProject));
                                param.add(new Param("orderNumber", orderNumber));
                                param.add(new Param("orderSum", zongjia));
                                String url = FXConstant.URL_INSERT_OrderDetail;
                                OkHttpManager.getInstance().posts2(param, null, new ArrayList<String>(),null, new ArrayList<String>(), "image01", path1, "image02", path2, "image03", path3, "image04", path4
                                        , "image05", path5, "image06", path6, "image07", path7, "image08", path8, url, new OkHttpManager.HttpCallBack() {
                                            @Override
                                            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                                String code = jsonObject.getString("code");
                                                if (code.equals("数据更新成功")) {
                                                    String filePath = Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png";
                                                    EMMessage message = EMMessage.createImageSendMessage(filePath,false,userId);
                                                    message.setAttribute("flg","04");
                                                    message.setAttribute("merId",merId);
                                                    message.setAttribute("shareId",orderId);
                                                    message.setAttribute("userId",userId);
                                                    message.setAttribute("types","已编辑");
                                                    String userPic = PreferenceManager.getInstance().getCurrentUserAvatar();
                                                    if (!TextUtils.isEmpty(userPic)){
                                                        message.setAttribute("userPic",userPic);
                                                    }
                                                    String userName = PreferenceManager.getInstance().getCurrentUserNick();
                                                    if (!TextUtils.isEmpty(userName)){
                                                        message.setAttribute("userName",userName);
                                                    }
                                                    message.setAttribute(DemoHelper.getInstance().getCurrentUsernName(), userPic+"|"+userName);
                                                    message.setAttribute(userId, finalChatImg +"|"+ finalChatName);
                                                    message.setAttribute("name", finalChatName);
                                                    message.setAttribute("type",msgType);
                                                    message.setAttribute("shareRed", finalShareRed);
                                                    EMClient.getInstance().chatManager().sendMessage(message);
                                                    Toast.makeText(UOrderDetailFourActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(UOrderDetailFourActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(String errorMsg) {
                                                if (mProgress!=null){
                                                    if (mProgress.isShowing()){
                                                        mProgress.dismiss();
                                                        mProgress=null;
                                                    }
                                                }
                                                Toast.makeText(UOrderDetailFourActivity.this,"网络连接错误,派单失败",Toast.LENGTH_SHORT).show();
                                                if (errorMsg!=null) {
                                                    Log.e("offlineorder,e", errorMsg);
                                                }
                                            }
                                        });
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

            }
        });
        btn_comit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = FXConstant.URL_Get_UserInfo+userId;
                StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        com.alibaba.fastjson.JSONObject objects = JSON.parseObject(s);
                        com.alibaba.fastjson.JSONObject object = objects.getJSONObject("userInfo");
                        String uName = object.getString("uName");
                        String uImage = object.getString("uImage");
                        String share = object.getString("shareRed");
                        String friendsNumber = object.getString("friendsNumber");
                        final String msgType = "单聊";
                        String chatImg = uImage;
                        String shareRed = null;
                        if (chatImg==null||"".equals(chatImg)){
                            chatImg = "0";
                        }else {
                            chatImg = chatImg.split("\\|")[0];
                        }
                        String chatName = uName;
                        if (chatName==null||"".equals(chatName)){
                            chatName = userId;
                        }
                        String onceJine = null;
                        if (friendsNumber!=null&&!"".equals(friendsNumber)){
                            onceJine = friendsNumber.split("\\|")[0];
                        }
                        if (share!=null&&!"".equals(share)&& Double.parseDouble(share)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
                            shareRed = "有";
                        }else {
                            shareRed = "无";
                        }

                        zongjia = et_feiyong.getText().toString().trim();
                        if (TextUtils.isEmpty(zongjia)||Double.parseDouble(zongjia)==0){
                            Toast.makeText(UOrderDetailFourActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                        final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                        btnOK.setText("确定");
                        btnCancel.setText("取消");
                        title_tv.setText("是否确定完成编辑并发送回给对方？");
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        final String finalChatImg = chatImg;
                        final String finalChatName = chatName;
                        final String finalShareRed = shareRed;
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                ScreenshotUtil.getBitmapByView2(UOrderDetailFourActivity.this, findViewById(R.id.ll_all),false);
                                String path1 = null,path2 = null,path3 = null,path4 = null,path5 = null,path6 = null,path7 = null,path8 = null;
                                if (imagePaths1.size()>0){
                                    path1 = imagePaths1.get(0);
                                }
                                if (imagePaths1.size()>1){
                                    path2 = imagePaths1.get(1);
                                }
                                if (imagePaths1.size()>2){
                                    path3 = imagePaths1.get(2);
                                }
                                if (imagePaths1.size()>3){
                                    path4 = imagePaths1.get(3);
                                }
                                if (imagePaths2.size() > 0) {
                                    path5 = imagePaths2.get(0);
                                }
                                if (imagePaths2.size() > 1) {
                                    path6 = imagePaths2.get(1);
                                }
                                if (imagePaths2.size() > 2) {
                                    path7 = imagePaths2.get(2);
                                }
                                if (imagePaths2.size() > 3) {
                                    path8 = imagePaths2.get(3);
                                }
                                orderProject = et_miaoshu.getText().toString().trim();
                                String name = TextUtils.isEmpty(et_name.getText().toString().trim())?"0":et_name.getText().toString().trim();
                                String phone = TextUtils.isEmpty(et_phone.getText().toString().trim())?"0":et_phone.getText().toString().trim();
                                String dizhi = TextUtils.isEmpty(et_dizhi.getText().toString().trim())?"0":et_dizhi.getText().toString().trim();
                                orderNumber = name+"|"+phone+"|"+dizhi;

                                List<Param> param=new ArrayList<>();
                                if (!"".equals(time)) {
                                    param.add(new Param("conventionTime",time));
                                }
                                param.add(new Param("orderId",orderId));
                                param.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
                                param.add(new Param("orderProject", orderProject));
                                param.add(new Param("orderNumber", orderNumber));
                                param.add(new Param("orderSum", zongjia));
                                String url = FXConstant.URL_INSERT_OrderDetail;
                                OkHttpManager.getInstance().posts2(param, null, new ArrayList<String>(),null, new ArrayList<String>(), "image01", path1, "image02", path2, "image03", path3, "image04", path4
                                        , "image05", path5, "image06", path6, "image07", path7, "image08", path8, url, new OkHttpManager.HttpCallBack() {
                                            @Override
                                            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                                String code = jsonObject.getString("code");
                                                if (code.equals("数据更新成功")) {
                                                    String filePath = Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png";
                                                    EMMessage message = EMMessage.createImageSendMessage(filePath,false,userId);
                                                    message.setAttribute("flg","04");
                                                    message.setAttribute("merId",merId);
                                                    message.setAttribute("shareId",orderId);
                                                    message.setAttribute("userId",userId);
                                                    message.setAttribute("types","已编辑");
                                                    String userPic = PreferenceManager.getInstance().getCurrentUserAvatar();
                                                    if (!TextUtils.isEmpty(userPic)){
                                                        message.setAttribute("userPic",userPic);
                                                    }
                                                    String userName = PreferenceManager.getInstance().getCurrentUserNick();
                                                    if (!TextUtils.isEmpty(userName)){
                                                        message.setAttribute("userName",userName);
                                                    }
                                                    message.setAttribute(DemoHelper.getInstance().getCurrentUsernName(), userPic+"|"+userName);
                                                    message.setAttribute(userId, finalChatImg +"|"+ finalChatName);
                                                    message.setAttribute("name", finalChatName);
                                                    message.setAttribute("type",msgType);
                                                    message.setAttribute("shareRed", finalShareRed);
                                                    EMClient.getInstance().chatManager().sendMessage(message);
                                                    Toast.makeText(UOrderDetailFourActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(UOrderDetailFourActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(String errorMsg) {
                                                if (mProgress!=null){
                                                    if (mProgress.isShowing()){
                                                        mProgress.dismiss();
                                                        mProgress=null;
                                                    }
                                                }
                                                Toast.makeText(UOrderDetailFourActivity.this,"网络连接错误,派单失败",Toast.LENGTH_SHORT).show();
                                                if (errorMsg!=null) {
                                                    Log.e("offlineorder,e", errorMsg);
                                                }
                                            }
                                        });
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            }
        });
    }

    private void initView4() {
        et_feiyong.setEnabled(false);
        et_miaoshu.setEnabled(false);
        et_name.setEnabled(false);
        et_dizhi.setEnabled(false);
        et_phone.setEnabled(false);
        et_Ubeizhu.setEnabled(false);
        String shuju = getIntent().getStringExtra("shuju");
        orderId = getIntent().getStringExtra("orderId");
        dynamicSeq = getIntent().getStringExtra("dynamic_seq");
        createTime = getIntent().getStringExtra("timestamp");
        task_label = getIntent().getStringExtra("task_label");
        hxid = getIntent().getStringExtra("u_id");
        zongjia = getIntent().getStringExtra("quote");
        OrderDetail orderDetail = new OrderDetail();
        try {
            JSONObject object1 = new JSONObject(shuju);
            JSONObject object = object1.getJSONObject("odi");
            orderDetail = JSONParser.parseOrderDetail(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String yueding = TextUtils.isEmpty(orderDetail.getResv1())?"":orderDetail.getResv1();
        String resv2 = TextUtils.isEmpty(orderDetail.getResv2())?"":orderDetail.getResv2();
        runTime(yueding,resv2);
        orderProject = TextUtils.isEmpty(orderDetail.getOrderProject())?"":orderDetail.getOrderProject();
        orderNumber = TextUtils.isEmpty(orderDetail.getOrderNumber())?"":orderDetail.getOrderNumber();
        et_feiyong.setText(zongjia);
        et_miaoshu.setText(orderProject);
        String sImage1 = orderDetail.getSimage1();
        String sImage2 = orderDetail.getSimage2();
        String sImage3 = orderDetail.getSimage3();
        String sImage4 = orderDetail.getSimage4();
        String sImage5 = orderDetail.getSimage5();
        String sImage6 = orderDetail.getSimage6();
        String sImage7 = orderDetail.getSimage7();
        String sImage8 = orderDetail.getSimage8();
        int type = FunctionConfig.TYPE_IMAGE;
        if (sImage1!=null&&!"".equals(sImage1)&&!sImage1.equalsIgnoreCase("null")){
            LocalMedia media = new LocalMedia();
            media.setType(type);
            media.setCompressed(false);
            media.setCut(false);
            media.setIsChecked(true);
            imagePaths1.add(FXConstant.URL_SIGN_FOUR+sImage1);
            media.setNum(imagePaths1.size());
            media.setPath(FXConstant.URL_SIGN_FOUR+sImage1);
            selectMedia1.add(media);
        }
        if (sImage2!=null&&!"".equals(sImage2)&&!sImage2.equalsIgnoreCase("null")){
            LocalMedia media = new LocalMedia();
            media.setType(type);
            media.setCompressed(false);
            media.setCut(false);
            media.setIsChecked(true);
            imagePaths1.add(FXConstant.URL_SIGN_FOUR+sImage2);
            media.setPath(FXConstant.URL_SIGN_FOUR+sImage2);
            media.setNum(imagePaths1.size());
            selectMedia1.add(media);
        }
        if (sImage3!=null&&!"".equals(sImage3)&&!sImage3.equalsIgnoreCase("null")){
            LocalMedia media = new LocalMedia();
            media.setType(type);
            media.setCompressed(false);
            media.setCut(false);
            media.setIsChecked(true);
            imagePaths1.add(FXConstant.URL_SIGN_FOUR+sImage3);
            media.setPath(FXConstant.URL_SIGN_FOUR+sImage3);
            media.setNum(imagePaths1.size());
            selectMedia1.add(media);
        }
        if (sImage4!=null&&!"".equals(sImage4)&&!sImage4.equalsIgnoreCase("null")){
            LocalMedia media = new LocalMedia();
            media.setType(type);
            media.setCompressed(false);
            media.setCut(false);
            media.setIsChecked(true);
            imagePaths1.add(FXConstant.URL_SIGN_FOUR+sImage4);
            media.setPath(FXConstant.URL_SIGN_FOUR+sImage4);
            media.setNum(imagePaths1.size());
            selectMedia1.add(media);
        }
        if (imagePaths1.size()>0){
            adapter.notifyDataSetChanged();
        }
        if (sImage5!=null&&!"".equals(sImage5)&&!sImage5.equalsIgnoreCase("null")){
            LocalMedia media = new LocalMedia();
            media.setType(type);
            media.setCompressed(false);
            media.setCut(false);
            media.setIsChecked(true);
            imagePaths2.add(FXConstant.URL_SIGN_FOUR+sImage5);
            media.setPath(FXConstant.URL_SIGN_FOUR+sImage5);
            media.setNum(imagePaths2.size());
            selectMedia2.add(media);
        }
        if (sImage6!=null&&!"".equals(sImage6)&&!sImage6.equalsIgnoreCase("null")){
            LocalMedia media = new LocalMedia();
            media.setType(type);
            media.setCompressed(false);
            media.setCut(false);
            media.setIsChecked(true);
            imagePaths2.add(FXConstant.URL_SIGN_FOUR+sImage6);
            media.setPath(FXConstant.URL_SIGN_FOUR+sImage6);
            media.setNum(imagePaths2.size());
            selectMedia2.add(media);
        }
        if (sImage7!=null&&!"".equals(sImage7)&&!sImage7.equalsIgnoreCase("null")){
            LocalMedia media = new LocalMedia();
            media.setType(type);
            media.setCompressed(false);
            media.setCut(false);
            media.setIsChecked(true);
            imagePaths2.add(FXConstant.URL_SIGN_FOUR+sImage7);
            media.setPath(FXConstant.URL_SIGN_FOUR+sImage7);
            media.setNum(imagePaths2.size());
            selectMedia2.add(media);
        }
        if (sImage8!=null&&!"".equals(sImage8)&&!sImage8.equalsIgnoreCase("null")){
            LocalMedia media = new LocalMedia();
            media.setType(type);
            media.setCompressed(false);
            media.setCut(false);
            media.setIsChecked(true);
            imagePaths2.add(FXConstant.URL_SIGN_FOUR+sImage8);
            media.setPath(FXConstant.URL_SIGN_FOUR+sImage8);
            media.setNum(imagePaths2.size());
            selectMedia2.add(media);
        }
        if (imagePaths2.size()>0){
            adapter2.notifyDataSetChanged();
        }
        btn_comit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sum = et_feiyong.getText().toString().trim();
                if (!TextUtils.isEmpty(sum)&&Double.parseDouble(sum)>0) {
                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                    final Dialog dialog = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                    RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                    RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                    RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                    re_item5.setVisibility(View.VISIBLE);
                    TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                    TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                    TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                    TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
                    tv_title.setText("请选择支付方式");
                    tv_item1.setText("余 额 支 付");
                    tv_item2.setText("微 信 支 付");
                    tv_item5.setText("支付宝支付");
                    re_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());

                            zhifu2(sum);
                        }
                    });
                    re_item2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            rechargefromWx(zongjia,orderId);
                        }
                    });
                    re_item5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            rechargefromZhFb(zongjia,orderId);
                        }
                    });
                    re_item3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(UOrderDetailFourActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView3() {

        btn_comit.setEnabled(true);
        btn_comit.setText("接单报价");
        btn_comit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String path1 = null,path2 = null,path3 = null,path4 = null,path5 = null,path6 = null,path7 = null,path8 = null;
                if (imagePaths1.size()>0){
                    path1 = imagePaths1.get(0);
                }
                if (imagePaths1.size()>1){
                    path2 = imagePaths1.get(1);
                }
                if (imagePaths1.size()>2){
                    path3 = imagePaths1.get(2);
                }
                if (imagePaths1.size()>3){
                    path4 = imagePaths1.get(3);
                }
                if (imagePaths2.size() > 0) {
                    path5 = imagePaths2.get(0);
                }
                if (imagePaths2.size() > 1) {
                    path6 = imagePaths2.get(1);
                }
                if (imagePaths2.size() > 2) {
                    path7 = imagePaths2.get(2);
                }
                if (imagePaths2.size() > 3) {
                    path8 = imagePaths2.get(3);
                }
                zongjia = et_feiyong.getText().toString().trim();
                orderProject = et_miaoshu.getText().toString().trim();
                String name = TextUtils.isEmpty(et_name.getText().toString().trim())?"0":et_name.getText().toString().trim();
                String phone = TextUtils.isEmpty(et_phone.getText().toString().trim())?"0":et_phone.getText().toString().trim();
                String dizhi = TextUtils.isEmpty(et_dizhi.getText().toString().trim())?"0":et_dizhi.getText().toString().trim();
                orderNumber = name+"|"+phone+"|"+dizhi;

                if (zongjia==null||"".equals(zongjia)||Double.parseDouble(zongjia)<=0){
                    Toast.makeText(getApplicationContext(),"请输入报价！",Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailFourActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
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
                title_tv1.setText("报价前请沟通确认距离、价格、时间");
                btnCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                final String finalPath1 = path1;
                final String finalPath2 = path2;
                final String finalPath3 = path3;
                final String finalPath4 = path4;
                final String finalPath5 = path5;
                final String finalPath6 = path6;
                final String finalPath7 = path7;
                final String finalPath8 = path8;
                btnOK1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                        List<Param> param=new ArrayList<>();
                        if (!"".equals(time)) {
                            param.add(new Param("conventionTime",time));
                        }
                        param.add(new Param("type","01"));
                        param.add(new Param("flg","04"));
                        param.add(new Param("orderBody",task_label));
                        param.add(new Param("task_label",task_label));
                        param.add(new Param("createTime",createTime));
                        param.add(new Param("dynamicSeq",dynamicSeq));
                        param.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
                        param.add(new Param("orderProject", orderProject));
                        param.add(new Param("orderNumber", orderNumber));
                        param.add(new Param("orderSum", zongjia));
                        String url = FXConstant.URL_INSERT_DYNAMIC_ORDER;
                        OkHttpManager.getInstance().posts2(param, null, new ArrayList<String>(), null, new ArrayList<String>(),"image01", finalPath1, "image02", finalPath2, "image03", finalPath3, "image04", finalPath4
                                , "image05", finalPath5, "image06", finalPath6, "image07", finalPath7, "image08", finalPath8, url, new OkHttpManager.HttpCallBack() {
                                    @Override
                                    public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                        String code = jsonObject.getString("code");
                                        if ("success".equals(code)){
                                            sendPushMessage(hxid,"003");
                                            addPinglinCount(createTime,dynamicSeq);
                                            insertDynamicContact(dynamicSeq,createTime,DemoHelper.getInstance().getCurrentUsernName(),hxid,"00");

                                            //接单报价给用户发短信
                                            duanxintongzhi(hxid,"【正事多】("+task_label+")派单，已接收到新的报价，请及时沟通，无异议可下单验资进入服务流程（提示：线下交易与平台无关不提供售后保障以及投诉）","111");

                                        }else {
                                            if (code!=null&&"FAIL1".equals(code)) {
                                                LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
                                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                                final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
                                                dialog2.show();
                                                dialog2.getWindow().setContentView(layout);
                                                dialog2.setCanceledOnTouchOutside(false);
                                                dialog2.setCancelable(false);
                                                TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                                                Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                                                tv_title.setText("您已进行过报价,请勿重复报价");
                                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog2.dismiss();
                                                    }
                                                });
                                            }else {
                                                Toast.makeText(getApplicationContext(), "报价失败！", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(String errorMsg) {
                                        if (mProgress!=null){
                                            if (mProgress.isShowing()){
                                                mProgress.dismiss();
                                                mProgress=null;
                                            }
                                        }
                                        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
                                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                        final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
                                        dialog2.show();
                                        dialog2.getWindow().setContentView(layout);
                                        dialog2.setCanceledOnTouchOutside(false);
                                        dialog2.setCancelable(false);
                                        TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                                        Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                                        tv_title.setText("您已进行过报价,请勿重复报价");
                                        btn_ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog2.dismiss();
                                            }
                                        });
                                    }
                                });
                    }
                });

            }
        });
    }
    private void insertOrder(final String biaoshi) {
        String url = FXConstant.URL_INSERT_ORDER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    String code = obj.getString("code");
                    JSONObject object = obj.getJSONObject("orderInfo");
                    orderId = object.getString("orderId");
                    merId = object.getString("merId");
                    if (code.equals("数据更新成功")) {
                        if (hxid.length()>12){
                            updateQjcount();
                        }else {
                            updateUscount(hxid);
                        }
                        bianji(biaoshi);
                    } else {
                        Toast.makeText(UOrderDetailFourActivity.this, "下单失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("onResponse,s",volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", wodezhanghao);
                params.put("merId", hxid);
                params.put("orderBody",orderBody);
                params.put("flg", "04");
                params.put("type", typeDetail);
                params.put("upId", zy1);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }
    private void bianji(final String biaoshi){
        String path1 = null,path2 = null,path3 = null,path4 = null,path5 = null,path6 = null,path7 = null,path8 = null;
        if (imagePaths1.size()>0){
            path1 = imagePaths1.get(0);
        }
        if (imagePaths1.size()>1){
            path2 = imagePaths1.get(1);
        }
        if (imagePaths1.size()>2){
            path3 = imagePaths1.get(2);
        }
        if (imagePaths1.size()>3){
            path4 = imagePaths1.get(3);
        }
        if (imagePaths2.size() > 0) {
            path5 = imagePaths2.get(0);
        }
        if (imagePaths2.size() > 1) {
            path6 = imagePaths2.get(1);
        }
        if (imagePaths2.size() > 2) {
            path7 = imagePaths2.get(2);
        }
        if (imagePaths2.size() > 3) {
            path8 = imagePaths2.get(3);
        }
        zongjia = et_feiyong.getText().toString().trim();
        orderProject = et_miaoshu.getText().toString().trim();
        String name = TextUtils.isEmpty(et_name.getText().toString().trim())?"0":et_name.getText().toString().trim();
        String phone = TextUtils.isEmpty(et_phone.getText().toString().trim())?"0":et_phone.getText().toString().trim();
        String dizhi = TextUtils.isEmpty(et_dizhi.getText().toString().trim())?"0":et_dizhi.getText().toString().trim();
        orderNumber = name+"|"+phone+"|"+dizhi;

        List<Param> param=new ArrayList<>();
        if (!"".equals(time)) {
            param.add(new Param("conventionTime",time));
        }
        param.add(new Param("orderId",orderId));
        param.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
        param.add(new Param("orderProject", orderProject));
        param.add(new Param("orderNumber", orderNumber));
        param.add(new Param("orderSum", zongjia));
        String url = FXConstant.URL_INSERT_OrderDetail;
        OkHttpManager.getInstance().posts2(param, null, new ArrayList<String>(), null, new ArrayList<String>(),"image01", path1, "image02", path2, "image03", path3, "image04", path4
                , "image05", path5, "image06", path6, "image07", path7, "image08", path8, url, new OkHttpManager.HttpCallBack() {
                    @Override
                    public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                        String code = jsonObject.getString("code");
                        if (code.equals("数据更新成功")) {
                            if ("01".equals(biaoshi)) {
                                rechargefromZhFb(zongjia,orderId);
                            }else if ("02".equals(biaoshi)){
                                rechargefromWx(zongjia,orderId);
                            }else {
                                fukuan();
                            }
                        } else {
                            Toast.makeText(UOrderDetailFourActivity.this, "编辑错误！", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        if (mProgress!=null){
                            if (mProgress.isShowing()){
                                mProgress.dismiss();
                                mProgress=null;
                            }
                        }
                        Toast.makeText(UOrderDetailFourActivity.this,"网络连接错误,派单失败",Toast.LENGTH_SHORT).show();
                        if (errorMsg!=null) {
                            Log.e("offlineorder,e", errorMsg);
                        }
                    }
                });
    }

    private void rechargefromZhFb(final String balance,final String uId){
        String chongzhiId=null;
        if (hxid.length()>12) {
            chongzhiId = DemoApplication.getInstance().getCurrentQiYeId();
        }else {
            chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        }
        try {
            chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (balance!=null&&Double.parseDouble(balance)>0) {
            String url = FXConstant.URL_ZhiFu;
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX,balance,chongzhiId,"2",uId,null,"正事多-订单支付");
            final String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            final String orderinfo = OrderInfoUtil2_0.getSign(params);
            StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String sign = object.getString("sign");
                        sign = URLEncoder.encode(sign, "UTF-8");
                        final String orderInfo = orderParam + "&" + "sign=" + sign;
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(UOrderDetailFourActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                handler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(UOrderDetailFourActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(UOrderDetailFourActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }


    private void rechargefromWx(String zongjia, String uId) {
        String chongzhiId=null;
        if (hxid.length()>12) {
            chongzhiId = DemoApplication.getInstance().getCurrentQiYeId();
        }else {
            chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        }
        try {
            chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        zongjia = (int)(Double.parseDouble(zongjia)*100)+"";
        final String mubiaoId = chongzhiId+"_"+"2"+"_"+uId;
        Toast.makeText(UOrderDetailFourActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = zongjia;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity","UOrderDetailFourActivity");
                    editor.commit();
                    JSONObject object = new JSONObject(s);
                    Log.e("chongzhiac,s", s);
                    String appid="",mch_id="",nonce_str="",sign="",prepayId="",timestamp="";
                    appid = object.getString("appid");
                    nonce_str = object.getString("noncestr");
                    mch_id = object.getString("partnerid");
                    prepayId = object.getString("prepayid");
                    timestamp = object.getString("timestamp");
                    sign = object.getString("sign");
                    PayReq req = new PayReq();
                    req.appId = appid;
                    req.partnerId = mch_id;
                    req.prepayId = prepayId;
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = nonce_str;
                    //这是得到一个时间戳(除以1000转化成秒数)
                    req.timeStamp = timestamp;
                    //调用获得签名的方法,这里直接把服务器返回来的sign给覆盖了,所以我不是很明白服务器为什么返回这个sign值,然后调起支付,基本上就可以了(我的反正是可以了....)
//                sign = OrderInfoUtil2_0.createSign(parameters);
                    Log.e("TAG", "timestamp=====" + req.timeStamp);
                    req.sign = sign;
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UOrderDetailFourActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("body","正事多-订单支付");
                param.put("detail","正事多-订单支付");
                param.put("out_trade_no",getNowTime2());
                param.put("total_fee", finalBalance);
                param.put("spbill_create_ip",getHostIP());
                param.put("attach",mubiaoId);
                return param;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private String getNowTime2() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

    private void fukuan() {
        zongjia = et_feiyong.getText().toString().trim();
        String url = FXConstant.URL_DingDan_Pay;
        StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("success")) {
                        if (mProgress!=null&&mProgress.isShowing()) {
                            mProgress.dismiss();
                        }
                        if (pd!=null&&pd.isShowing()) {
                            pd.dismiss();
                        }
                        if (hxid.length()>12){
                            sendPushMessage(managerId,"002");
                        }else {
                            sendPushMessage(hxid,"000");
                        }
                        Toast.makeText(UOrderDetailFourActivity.this, "付款成功,您已完成验资！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mProgress!=null&&mProgress.isShowing()) {
                            mProgress.dismiss();
                        }
                        if (pd!=null&&pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(UOrderDetailFourActivity.this, "付款失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgress!=null&&mProgress.isShowing()) {
                    mProgress.dismiss();
                }
                Toast.makeText(UOrderDetailFourActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", orderId);
                params.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("balance", zongjia);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request1);
    }

    private void addPinglinCount(final String createTime, final String dynamicSeq){
        String url = FXConstant.URL_ADD_PINGLUN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("uorderdetac","评论增加成功");
                MainActivity.instance.refreshDyna();
                LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
                dialog2.show();
                dialog2.getWindow().setContentView(layout);
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.setCancelable(false);
                TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                tv_title.setText("报价成功，等待对方验资之后即可开始进行订单处理");
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                        finish();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("uorderdetac","评论增加失败"+volleyError.getMessage());
                MainActivity.instance.refreshDyna();
                LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailFourActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailFourActivity.this,R.style.Dialog).create();
                dialog2.show();
                dialog2.getWindow().setContentView(layout);
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.setCancelable(false);
                TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                tv_title.setText("报价成功，等待对方验资之后即可开始进行订单处理");
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                        finish();
                    }
                });
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
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private void ChangeState() {
        String url = FXConstant.URL_DYNAMIC_CHANGE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("uorderdac,chasta",s);
                chandyOrderState();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("dynamic_seq",dynamicSeq);
                param.put("timestamp",createTime);
                param.put("u_id",hxid);
                return param;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private void chandyOrderState() {
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
                param.put("orderState","01");
                return param;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private void updateUscount(final String hxid1) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderUnReadCount","1");
                param.put("userId",hxid1);
                return param;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private void initView() {
        hoursTv = (TextView) findViewById(R.id.tv_hours);
        tv2 = (TextView) findViewById(R.id.tv2);
        daysTv = (TextView) findViewById(R.id.tv_days);
        minutesTv = (TextView) findViewById(R.id.tv_mins);
        secondsTv = (TextView) findViewById(R.id.tv_mills);
        tv_weishezhi = (TextView) findViewById(R.id.tv_all);
        countDown = (RelativeLayout) findViewById(R.id.rlt1);
        rlt1 = (RelativeLayout) findViewById(R.id.countDown);
        rlt1.setVisibility(View.INVISIBLE);

        ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
        ll2_paidan = (LinearLayout) findViewById(R.id.ll2_paidan);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3_paidan = (LinearLayout) findViewById(R.id.ll3_paidan);
        et_miaoshu = (EditText) findViewById(R.id.et_miaoshu);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_dizhi = (EditText) findViewById(R.id.et_dizhi);
        et_feiyong = (EditText) findViewById(R.id.et_feiyong);
        et_Ubeizhu = (EditText) findViewById(R.id.et_Ubeizhu);
        tev_paidan_caiwu = (TextView) findViewById(R.id.tev_paidan_caiwu);
        tev_paidan_kehu = (TextView) findViewById(R.id.tev_paidan_kehu);
        tev_kehu = (TextView) findViewById(R.id.tev_kehu);
        tev_jiedan = (TextView) findViewById(R.id.tev_jiedan);
        tev_paidan_id1 = (TextView) findViewById(R.id.tev_paidan_id1);
        tv_fasong = (TextView) findViewById(R.id.tv_fasong);
        tv_kehu = (TextView) findViewById(R.id.tv_paidan_caiwu);
        tv_kehu_time = (TextView) findViewById(R.id.tv_kehu_time);
        tv_jiedan = (TextView) findViewById(R.id.tv_jiedan);
        tv_jiedan_time = (TextView) findViewById(R.id.tv_jiedan_time);
        tv_paidan_id1 = (TextView) findViewById(R.id.tv_paidan_id1);
        tv_paidan_id1_time = (TextView) findViewById(R.id.tv_paidan_id1_time);
        tv_paidan_caiwu = (TextView) findViewById(R.id.tv_paidan_caiwu);
        tv_paidan_caiwu_time = (TextView) findViewById(R.id.tv_paidan_caiwu_time);
        tv_paidan_kehu = (TextView) findViewById(R.id.tv_paidan_kehu);
        tv_paidan_kehu_time = (TextView) findViewById(R.id.tv_paidan_kehu_time);
        tv_yanzi = (TextView) findViewById(R.id.tv_zhifuxianshi);
        tv_count_fankui = (TextView) findViewById(R.id.tv_count_fankui);
        rl_xinxi2 = (RelativeLayout) findViewById(R.id.rl_xinxi2);
        rl_miaoshu = (RelativeLayout) findViewById(R.id.rl_miaoshu);
        rl_btn1 = (RelativeLayout) findViewById(R.id.rl_btn1);
        rlM_beizhu = (RelativeLayout) findViewById(R.id.rlM_beizhu);
        rl_paidan_id1 = (RelativeLayout) findViewById(R.id.rl_paidan_id1);
        iv_kehu = (ImageView) findViewById(R.id.iv_kehu);
        iv_jiedan = (ImageView) findViewById(R.id.iv_jiedan);
        iv_paidan_id1 = (ImageView) findViewById(R.id.iv_paidan_id1);
        iv_paidan_caiwu = (ImageView) findViewById(R.id.iv_paidan_caiwu);
        iv_paidan_kehu = (ImageView) findViewById(R.id.iv_paidan_kehu);
        btn_comit = (Button) findViewById(R.id.btn_comit);
        btn_cansul = (Button) findViewById(R.id.btn_cansul);
        btn_comit2 = (Button) findViewById(R.id.btn_comit2);
        countDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(UOrderDetailFourActivity.this,YueDingTimeActivity.class),1);
            }
        });
    }

    private void downloadFile(final String url, String fileName, final int size){
        Log.e("uorderdef,下载",size+"");
        File saveFile = new FileStorage("offline").createCropFile(fileName,null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(UOrderDetailFourActivity.this, "com.sangu.app.fileprovider", saveFile);//通过FileProvider创建一个content类型的Uri
            grantUriPermission(getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(saveFile);
        }
        File f = new File(saveFile.getPath());
        if (size==1) {
            filePath1 = f.getAbsolutePath();
        }else if (size==2){
            filePath2 = f.getAbsolutePath();
        }else if (size==3){
            filePath3 = f.getAbsolutePath();
        }else if (size==4){
            filePath4 = f.getAbsolutePath();
        }else if (size==5){
            filePath5 = f.getAbsolutePath();
        }else if (size==6){
            filePath6 = f.getAbsolutePath();
        }else if (size==7){
            filePath7 = f.getAbsolutePath();
        }else if (size==8){
            filePath8 = f.getAbsolutePath();
        }
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (f.exists()) {
            final File finalSaveFile = f;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    OkHttpClient client = new OkHttpClient();
                    try {
                        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
                        okhttp3.Response response = client.newCall(request).execute();
                        InputStream is = response.body().byteStream();
                        final Bitmap bm = BitmapFactory.decodeStream(is);
                        if (bm != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    saveToSD(bm, finalSaveFile);
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    private void saveToSD(Bitmap mBitmap,File f) {
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
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            downloadSize++;
            if (downloadSize==allSize) {
                handler.sendEmptyMessage(1);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

    private String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }
    private void sendPushMessage2(final String userId,final int type) {
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
                param.put("u_id",userId);
                param.put("body","订单消息");
                param.put("type","001");
                param.put("userId",myId);
                param.put("companyId","0");
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }
    private void sendPushMessage(final String hxid1,final String type) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if ("002".equals(type)) {
                    if (createTime!=null&&!"".equals(createTime)) {
                        tongji();
                    }
                   //
                    if ("04".equals(biaoshi)) {
                        duanxintongzhi(managerId, "【正事多】您对（"+task_label+"）接单报价客户已下单验资，请及时服务，完成后进入（入账单）签字提交，等待客户签收（客户签收10天内，无异议可提现100%，平台不抽成）", "00");
                    }else if (!"03".equals(biaoshi)){
                        duanxintongzhi(managerId, "【正事多】 通知:您有一条新的订单消息", type);
                    }
                }else if ("000".equals(type)){
                    if (createTime!=null&&!"".equals(createTime)) {
                        tongji();
                    }
                    if ("04".equals(biaoshi)) {
                        duanxintongzhi(hxid1, "【正事多】您对（"+task_label+"）接单报价客户已下单验资，请及时服务，完成后进入（入账单）签字提交，等待客户签收（客户签收10天内，无异议可提现100%，平台不抽成）", "00");
                    }else if (!"03".equals(biaoshi)){
                        duanxintongzhi(hxid1, "【正事多】 通知:您有一条新的订单消息", type);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if ("002".equals(type)) {
                    if (createTime!=null&&!"".equals(createTime)) {
                        tongji();
                    }
                    if ("04".equals(biaoshi)) {
                        duanxintongzhi(managerId, "【正事多】您对（"+task_label+"）接单报价客户已下单验资，请及时服务，完成后进入（入账单）签字提交，等待客户签收（客户签收10天内，无异议可提现100%，平台不抽成）", "00");
                    }else if (!"03".equals(biaoshi)){
                        duanxintongzhi(managerId, "【正事多】 通知:您有一条新的订单消息", type);
                    }
                }else if ("000".equals(type)) {
                    if (createTime!=null&&!"".equals(createTime)) {
                        tongji();
                    }
                    if ("04".equals(biaoshi)) {
                        duanxintongzhi(hxid1, "【正事多】您对（"+task_label+"）接单报价客户已下单验资，请及时服务，完成后进入（入账单）签字提交，等待客户签收（客户签收10天内，无异议可提现100%，平台不抽成）", "00");
                    }else if (!"03".equals(biaoshi)){
                        duanxintongzhi(hxid1, "【正事多】 通知:您有一条新的订单消息", type);
                    }
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",hxid1);
                if ("03".equals(biaoshi)) {
                    param.put("body", "报价消息");
                    param.put("type","10");
                }else if ("04".equals(biaoshi)){
                    param.put("body", "订单消息");
                    param.put("type","000");
                }else {
                    if ("000".equals(type)) {
                        param.put("body", "订单消息");
                    }else {
                        param.put("body", "企业订单消息");
                    }
                    param.put("type",type);
                }
                param.put("userId",myId);
                param.put("companyId",hxid);
                param.put("companyName",companyName);
                param.put("companyAdress",companyAdress);
                if ("03".equals(biaoshi)){
                    param.put("dynamicSeq",dynamicSeq);
                    param.put("createTime",createTime);
                }else {
                    param.put("dynamicSeq", "0");
                    param.put("createTime", "0");
                }
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private void tongji() {
        String url = FXConstant.URL_TONGJI_XIAOLIANG;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                MainActivity.instance.refreshDyna();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("uorderDetailActivity","统计连接错误");
                MainActivity.instance.refreshDyna();
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("createTime", createTime);
                params.put("dynamicSeq", dynamicSeq);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private void duanxintongzhi(final String id, final String message,final String type) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                yuE = yuE - Double.valueOf(zongjia);
                DemoApplication.getApp().saveCurrentPrice(yuE);
                setResult(RESULT_OK, new Intent().putExtra("value", yuE));
//                if ("002".equals(type)) {
//                    startActivity(new Intent(UOrderDetailFourActivity.this, ConsumeActivity.class).putExtra("biaoshi", "00"));
//                }else {
//                    startActivity(new Intent(UOrderDetailFourActivity.this, ConsumeActivity.class).putExtra("biaoshi", "01"));
//                }
                if (type.equals("111")){

                }else {
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                yuE = yuE - Double.valueOf(zongjia);
                DemoApplication.getApp().saveCurrentPrice(yuE);
                setResult(RESULT_OK, new Intent().putExtra("value", yuE));
                if ("002".equals(type)) {
                    startActivity(new Intent(UOrderDetailFourActivity.this, ConsumeActivity.class).putExtra("biaoshi", "00"));
                }else {
                    startActivity(new Intent(UOrderDetailFourActivity.this, ConsumeActivity.class).putExtra("biaoshi", "01"));
                }
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message",message);
                param.put("telNum",id);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void updateQjcount() {
        String url = FXConstant.URL_UPDATE_UNREADQIYE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderCount","1");
                param.put("companyId",hxid);
                return param;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);
    }

    private void initViews(){
        et_feiyong.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters={new CashierInputFilter()};
        et_feiyong.setFilters(filters);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(UOrderDetailFourActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(UOrderDetailFourActivity.this, onAddPicClickListener1);
        adapter.setList(selectMedia1);
        adapter.setSelectMax(4);
        adapter.setType(1);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (selectMedia1.get(position).getType()) {
                    case FunctionConfig.TYPE_IMAGE:
                        // 预览图片 可长按保存 也可自定义保存路径
//                        PictureConfig.getInstance().externalPicturePreview(MainActivity.this, "/custom_file", position, selectMedia);
                        PictureConfig.getInstance().externalPicturePreview(UOrderDetailFourActivity.this, position, selectMedia1);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia1.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(UOrderDetailFourActivity.this, selectMedia1.get(position).getPath());
                        }
                        break;
                }
            }
        });
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler2);
        FullyGridLayoutManager manager2 = new FullyGridLayoutManager(UOrderDetailFourActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(manager2);
        adapter2 = new GridImageAdapter(UOrderDetailFourActivity.this, onAddPicClickListener2);
        adapter2.setList(selectMedia2);
        adapter2.setSelectMax(4);
        adapter2.setType(2);
        recyclerView2.setAdapter(adapter2);
        adapter2.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (selectMedia2.get(position).getType()) {
                    case FunctionConfig.TYPE_IMAGE:
                        // 预览图片 可长按保存 也可自定义保存路径
//                        PictureConfig.getInstance().externalPicturePreview(MainActivity.this, "/custom_file", position, selectMedia);
                        PictureConfig.getInstance().externalPicturePreview(UOrderDetailFourActivity.this, position, selectMedia2);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia2.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(UOrderDetailFourActivity.this, selectMedia2.get(position).getPath());
                        }
                        break;
                }
            }
        });
    }

    /**
     * 删除图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener1 = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    goCamera(4,"01");
                    break;
                case 1:
                    // 删除图片
                    imagePaths1.remove(position);
                    selectMedia1.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };
    /**
     * 删除图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener2 = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    goCamera(4,"02");
                    break;
                case 1:
                    // 删除图片
                    imagePaths2.remove(position);
                    selectMedia2.remove(position);
                    adapter2.notifyItemRemoved(position);
                    break;
            }
        }
    };

    private void goCamera(int count,String type){
        List<LocalMedia> medias = new ArrayList<>();
        if ("01".equals(type)) {
            medias = selectMedia1;
        } else {
            medias = selectMedia2;
        }
        WeakReference<UOrderDetailFourActivity> reference = new WeakReference<UOrderDetailFourActivity>(UOrderDetailFourActivity.this);
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
//                            .setOffsetX() // 自定义裁剪比例
//                            .setOffsetY() // 自定义裁剪比例
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(count) // 可选择图片的数量
                .setMinSelectNum(0)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(FunctionConfig.MODE_MULTIPLE) // 单选 or 多选
                .setShowCamera(false) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(false) // 是否打开剪切选项
                .setCircularCut(false)// 是否采用圆形裁剪
                .setPreviewVideo(true) // 是否预览视频(播放) mode or 多选有效
                .setCheckedBoxDrawable(0)
                .setRecordVideoDefinition(FunctionConfig.HIGH) // 视频清晰度
                .setRecordVideoSecond(60) // 视频秒数
                .setCustomQQ_theme(0)// 可自定义QQ数字风格，不传就默认是蓝色风格
                .setGif(false)// 是否显示gif图片，默认不显示
                .setCropW(0) // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
                .setCropH(0) // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
                .setMaxB(102400) // 压缩最大值 例如:200kb  就设置202400，202400 / 1024 = 200kb
                .setPreviewColor(ContextCompat.getColor(reference.get(), R.color.blue)) //预览字体颜色
                .setCompleteColor(ContextCompat.getColor(reference.get(), R.color.blue)) //已完成字体颜色
                .setPreviewBottomBgColor(0) //预览图片底部背景色
                .setPreviewTopBgColor(0)//预览图片标题背景色
                .setBottomBgColor(0) //图片列表底部背景色
                .setGrade(Luban.THIRD_GEAR) // 压缩档次 默认三档
                .setCheckNumMode(false)
                .setCompressQuality(100) // 图片裁剪质量,默认无损
                .setImageSpanCount(4) // 每行个数
                .setVideoS(0)// 查询多少秒内的视频 单位:秒
                .setSelectMedia(medias) // 已选图片，传入在次进去可选中，不能传入网络图片
                .setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
                .setCompressW(0) // 压缩宽 如果值大于图片原始宽高无效
                .setCompressH(0) // 压缩高 如果值大于图片原始宽高无效
                .setThemeStyle(ContextCompat.getColor(reference.get(), R.color.blue)) // 设置主题样式
                .setNumComplete(false) // 0/9 完成  样式
                .setClickVideo(false)// 点击声音
                .setFreeStyleCrop(false) // 裁剪是移动矩形框或是图片
                .create();
        if ("01".equals(type)) {
            // 只拍照
            PictureConfig.getInstance().init(options).openPhoto(reference.get(), resultCallback1);
        } else {
            // 先初始化参数配置，在启动相册
            PictureConfig.getInstance().init(options).openPhoto(reference.get(), resultCallback2);
        }
    }

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback1 = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            if (imagePaths1 != null) {
                imagePaths1.clear();
            }

            // 多选回调
            selectMedia1 = resultList;
            Log.i("callBack_result", selectMedia1.size() + "");
            LocalMedia media = resultList.get(0);
            String path;
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图地址
                path = media.getPath();
            }
            if (selectMedia1 != null) {
                adapter.setList(selectMedia1);
                adapter.notifyDataSetChanged();
                for (int i=0;i<selectMedia1.size();i++){
                    imagePaths1.add(selectMedia1.get(i).getCompressPath());
                }
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            imagePaths1.clear();
            // 单选回调
            selectMedia1.add(media);
            if (selectMedia1 != null) {
                adapter.setList(selectMedia1);
                adapter.notifyDataSetChanged();
                for (int i=0;i<selectMedia1.size();i++){
                    imagePaths1.add(selectMedia1.get(i).getCompressPath());
                }
            }
        }
    };
    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback2 = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            imagePaths2.clear();
            // 多选回调
            selectMedia2 = resultList;
            Log.i("callBack_result", selectMedia2.size() + "");
            LocalMedia media = resultList.get(0);
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                String path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                String path = media.getCompressPath();
            } else {
                // 原图地址
                String path = media.getPath();
            }
            if (selectMedia2 != null) {
                adapter2.setList(selectMedia2);
                adapter2.notifyDataSetChanged();
                for (int i=0;i<selectMedia2.size();i++){
                    imagePaths2.add(selectMedia2.get(i).getCompressPath());
                }
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            imagePaths2.clear();
            // 单选回调
            selectMedia2.add(media);
            if (selectMedia2 != null) {
                adapter2.setList(selectMedia2);
                adapter2.notifyDataSetChanged();
                for (int i=0;i<selectMedia2.size();i++){
                    imagePaths2.add(selectMedia2.get(i).getCompressPath());
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 0:
                    finish();
                    break;
                case 1:
                    if (data!=null){// 2016-11-01 22:17
                        tv_weishezhi.setVisibility(View.INVISIBLE);
                        rlt1.setVisibility(View.VISIBLE);
                        tv2.setText("距离约定时间");
                        time = data.getStringExtra("time");
                        time = time + "00";
                        String time1 = getNowTime();
                        long year = Long.parseLong(time.substring(0,4));
                        long year1 = Long.parseLong(time1.substring(0,4));
                        long mou = Long.parseLong(time.substring(4,6));
                        long mou1 = Long.parseLong(time1.substring(4,6));
                        long day = Long.parseLong(time.substring(6,8));
                        long day1 = Long.parseLong(time1.substring(6,8));
                        long hour = Long.parseLong(time.substring(8,10));
                        long hour1 = Long.parseLong(time1.substring(8,10));
                        long min = Long.parseLong(time.substring(10,12));
                        long min1 = Long.parseLong(time1.substring(10,12));
                        long minAll = (year-year1)*365*24*60 + (mou-mou1)*30*24*60 + (day-day1)*24*60 + (hour-hour1)*60 + (min-min1);
                        mDay = minAll / 60 /24;
                        if (mDay==0) {
                            daysTv.setVisibility(View.GONE);
                            mHour = minAll / 60;
                            mMin = minAll - mHour * 60;
                        }else {
                            daysTv.setVisibility(View.VISIBLE);
                            mHour = (minAll - mDay*60*24)/60;
                            mMin = minAll - mDay*60*24 - mHour*60;
                        }
                        if (mHour<10){
                            hours = "0"+mHour;
                        }else {
                            hours = String.valueOf(mHour);
                        }
                        if (mMin<10){
                            mins = "0"+mMin;
                        }else {
                            mins = String.valueOf(mMin);
                        }
                        if (mSecond<10){
                            second = "0"+mSecond ;
                        }else {
                            second = String.valueOf(mSecond);
                        }
                        daysTv.setText(mDay+"天");
                        hoursTv.setText(hours);
                        minutesTv.setText(mins);
                        secondsTv.setText(second);
//                startRun();
                    }else {
                        tv2.setText("选择约定时间");
                    }
                    break;
            }
        }
    }

    private String getnowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(date);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        yuE = DemoApplication.getInstance().getCurrenPrice();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imagePaths1!=null){
            imagePaths1.clear();
            imagePaths1=null;
        }
        if (selectMedia1!=null){
            selectMedia1.clear();
            selectMedia1=null;
        }
        if (selectMedia2!=null){
            selectMedia2.clear();
            selectMedia2=null;
        }
        if (imagePaths2!=null){
            imagePaths2.clear();
            imagePaths2=null;
        }
    }

    @Override
    public void updateCurrentPrice(Object success) {
        yuE = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }

    private void runTime(String yueding,String resv2){
        if (!"07".equals(biaoshi)) {
            countDown.setEnabled(false);
        }
        if (!"".equals(resv2)){
            String[] res = resv2.split(",");
            isRun = false;
            rlt1.setVisibility(View.GONE);
            tv_weishezhi.setVisibility(View.VISIBLE);
            tv_weishezhi.setText(res[1]);
            if (res[0].equals("0")){
                tv2.setText("距离约定时间");
            }else if (res[0].equals("1")){
                tv2.setText("超出约定时间");
            }
        }else {
            if (!"".equals(yueding)) {
                rlt1.setVisibility(View.VISIBLE);
                tv_weishezhi.setVisibility(View.INVISIBLE);
                String time1 = getNowTime();
                long year = Long.parseLong(yueding.substring(0, 4));
                long year1 = Long.parseLong(time1.substring(0, 4));
                long mou = Long.parseLong(yueding.substring(4, 6));
                long mou1 = Long.parseLong(time1.substring(4, 6));
                long day = Long.parseLong(yueding.substring(6, 8));
                long day1 = Long.parseLong(time1.substring(6, 8));
                long hour = Long.parseLong(yueding.substring(8, 10));
                long hour1 = Long.parseLong(time1.substring(8, 10));
                long min = Long.parseLong(yueding.substring(10, 12));
                long min1 = Long.parseLong(time1.substring(10, 12));
                mSecond = Long.parseLong(time1.substring(12, 14));
                long minAll = (year - year1) * 365 * 24 * 60 + (mou - mou1) * 30 * 24 * 60 + (day - day1) * 24 * 60 + (hour - hour1) * 60 + (min - min1);
                if (minAll <= 0) {
                    isJian = false;
                    minAll = -minAll;
                    tv2.setText("超出约定时间");
                } else {
                    isJian = true;
                    tv2.setText("距离约定时间");
                }
                mDay = minAll / 60 /24;
                if (mDay==0) {
                    daysTv.setVisibility(View.GONE);
                    mHour = minAll / 60;
                    mMin = minAll - mHour * 60;
                }else {
                    daysTv.setVisibility(View.VISIBLE);
                    mHour = (minAll - mDay*60*24)/60;
                    mMin = minAll - mDay*60*24 - mHour*60;
                }
                if (mHour < 10) {
                    hours = "0" + mHour;
                } else {
                    hours = String.valueOf(mHour);
                }
                if (mMin < 10) {
                    mins = "0" + mMin;
                } else {
                    mins = String.valueOf(mMin);
                }
                if (mSecond < 10) {
                    second = "0" + mSecond;
                } else {
                    second = String.valueOf(mSecond);
                }
                daysTv.setText(mDay+"天");
                hoursTv.setText(hours);
                minutesTv.setText(mins);
                secondsTv.setText(second);
                startRun();
            } else {
                isRun = false;
                rlt1 .setVisibility(View.GONE);
                tv_weishezhi.setVisibility(View.VISIBLE);
                tv_weishezhi.setText("未设置");
            }
        }
    }
    private void insertDynamicContact(final String dynamicSeq, final String createTime, final String uId, final String contactId, final String type) {
        String url = FXConstant.URL_UPDATE_INSERT_DYNAMIC_CONTACT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "insertDynamicContact onResponse" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "insertDynamicContact onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //uId=&dynamicSeq=&createTime=&dis=&responsetime=&ordernum=&status=
                Map<String, String> param = new HashMap<>();
                param.put("dynamicSeq", dynamicSeq);
                param.put("createTime", createTime);
                param.put("uId", uId);
                param.put("contactId", contactId);
                param.put("type", type);
                return param;
            }
        };
        MySingleton.getInstance(UOrderDetailFourActivity.this).addToRequestQueue(request);

    }
}
