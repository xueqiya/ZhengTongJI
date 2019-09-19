package com.sangu.apptongji.main.alluser.order.avtivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.address.AddressListTwoActivity;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.fragment.MainActivity;
import com.sangu.apptongji.main.utils.CashierInputFilter;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.utils.ZhifuHelpUtils;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.PreferenceManager;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by user on 2016/9/1.
 */
public class UOrderDetailActivity extends BaseActivity implements View.OnClickListener,TextWatcher,IPriceView{
    private static final int SDK_PAY_FLAG = 1;
    private ImageView ivBack=null,iv_jiantou=null;
    private String pass=null,typeDetail=null;
    private EditText etBody1=null,etBody2=null,etBody3=null,etBody4=null,etBody5=null,etCount1=null,etCount2=null,etCount3=null,etCount4=null,etCount5=null,
            etDanjia1=null,etDanjia2=null,etDanjia3=null,etDanjia4=null,etDanjia5=null,etAllPrice=null,etUBeizhu=null;
    private Button btnCommit=null,btnCansul=null;
    private TextView tvJiedan=null,tvXiadan=null,tv_weishezhi=null,tv_send;
    private String orderId=null,merId=null,orderProject=null,orderNumber=null,orderAmt=null,zongjia=null
            ,createTime="",dynamicSeq="",task_label;
    private LinearLayout llM_beizhu=null,ll_xiadan=null,ll1_paidan=null,ll2_paidan=null,ll2=null,ll_all;
    private RelativeLayout countDown=null,rlt1=null;
    private Double yuE;
    private String oP1=null,oP2=null,oP3=null,oP4=null,oP5=null,oN1=null,oN2=null,oN3=null,oN4=null,oN5=null,oA1=null,oA2=null,
            oA3=null,oA4=null,oA5=null,companyName=null,companyAdress=null,managerId=null;
    private String wodezhanghao=null,hxid=null,zy1=null,time="",biaoshi=null,orderBody;
    private ProgressDialog pd=null;
    private IPricePresenter pricePresenter;
    private int errorTime=3;
    // 倒计时
    String hours = "00",mins="00",second="00";
    private TextView daysTv,hoursTv=null, minutesTv=null, secondsTv=null,tv2=null;
    private TextView tv_paidan_qiye_time=null,tev_paidan_qiye=null;
    private TextView tv_paidan_qiyeren_time=null,tev_paidan_qiyeren=null;
    private TextView tv_paidan_caiwu_time=null,tev_paidan_caiwu=null;
    private TextView tv_paidan_kehu_time=null,tev_paidan_kehu=null;
    private long mDay;
    private long mHour;
    private long mMin;
    private long mSecond=00;
    private IWXAPI api=null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
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
                        if ("04".equals(biaoshi)||"06".equals(biaoshi)) {
                            if ("04".equals(biaoshi)){
                                ChangeState();
                            }
                            updateUscount(hxid);
                        }
                        if (hxid.length()>12){
                            sendPushMessage(managerId,"002");
                        }else {
                            sendPushMessage(hxid,"000");
                        }
                        Toast.makeText(UOrderDetailActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
                        zongjia = etAllPrice.getText().toString().trim();
                        final double orderSu = Double.valueOf(zongjia);
                        yuE = yuE - orderSu;
                        DemoApplication.getApp().saveCurrentPrice(yuE);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(UOrderDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String zhifuZhuangtai = intent.getStringExtra("zhifu");
        if (zhifuZhuangtai!=null&&"支付成功".equals(zhifuZhuangtai)){
            if ("04".equals(biaoshi)||"06".equals(biaoshi)) {
                if ("04".equals(biaoshi)){
                    ChangeState();
                }
                updateUscount(hxid);
            }
            if (hxid.length()>12){
                sendPushMessage(managerId,"002");
            }else {
                sendPushMessage(hxid,"000");
            }
            Toast.makeText(UOrderDetailActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
            zongjia = etAllPrice.getText().toString().trim();
            final double orderSu = Double.valueOf(zongjia);
            yuE = yuE - orderSu;
            DemoApplication.getApp().saveCurrentPrice(yuE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detial);
        pricePresenter = new PricePresenter(this,this);
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("正在发送请求...");
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        ll_xiadan = (LinearLayout) findViewById(R.id.ll_xiadan);
        ll1_paidan = (LinearLayout) findViewById(R.id.ll1_paidan);
        ll2_paidan = (LinearLayout) findViewById(R.id.ll2_paidan);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        iv_jiantou = (ImageView) findViewById(R.id.iv_jiantou);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tev_paidan_qiye = (TextView) findViewById(R.id.tev_paidan_qiye);
        tev_paidan_qiyeren = (TextView) findViewById(R.id.tev_paidan_qiyeren);
        tev_paidan_caiwu = (TextView) findViewById(R.id.tev_paidan_caiwu);
        tev_paidan_kehu = (TextView) findViewById(R.id.tev_paidan_kehu);
        tv_paidan_qiye_time = (TextView) findViewById(R.id.tv_paidan_qiye_time);
        tv_paidan_qiyeren_time = (TextView) findViewById(R.id.tv_paidan_qiyeren_time);
        tv_paidan_caiwu_time = (TextView) findViewById(R.id.tv_paidan_caiwu_time);
        tv_paidan_kehu_time = (TextView) findViewById(R.id.tv_paidan_kehu_time);
        tev_paidan_qiye.setVisibility(View.INVISIBLE);
        tev_paidan_qiyeren.setVisibility(View.INVISIBLE);
        tev_paidan_caiwu.setVisibility(View.INVISIBLE);
        tev_paidan_kehu.setVisibility(View.INVISIBLE);
        tv_paidan_qiye_time.setVisibility(View.INVISIBLE);
        tv_paidan_qiyeren_time.setVisibility(View.INVISIBLE);
        tv_paidan_caiwu_time.setVisibility(View.INVISIBLE);
        tv_paidan_kehu_time.setVisibility(View.INVISIBLE);
        iv_jiantou.setVisibility(View.INVISIBLE);
        hoursTv = (TextView) findViewById(R.id.tv_hours);
        tv2 = (TextView) findViewById(R.id.tv2);
        daysTv = (TextView) findViewById(R.id.tv_days);
        minutesTv = (TextView) findViewById(R.id.tv_mins);
        secondsTv = (TextView) findViewById(R.id.tv_mills);
        tv_weishezhi = (TextView) findViewById(R.id.tv_weishezhi);
        tv_send = (TextView) findViewById(R.id.tv_send);
        llM_beizhu = (LinearLayout) findViewById(R.id.llM_beizhu);
        ll_all = (LinearLayout) findViewById(R.id.ll_all);
        countDown = (RelativeLayout) findViewById(R.id.countDown);
        rlt1 = (RelativeLayout) findViewById(R.id.rlt1);
        rlt1.setVisibility(View.INVISIBLE);
        ll_all.setFocusable(true);
        ll_all.setFocusableInTouchMode(true);
        ll_all.requestFocus();
        tv_weishezhi.setVisibility(View.VISIBLE);
        countDown.setOnClickListener(this);
        Intent intent = this.getIntent();
        wodezhanghao = DemoHelper.getInstance().getCurrentUsernName();
        companyName = intent.hasExtra("companyName")?intent.getStringExtra("companyName"):"0";
        companyAdress = intent.hasExtra("companyAdress")?intent.getStringExtra("companyAdress"):"0";
        managerId = intent.getStringExtra("managerId");
        typeDetail = intent.getStringExtra("typeDetail");
        orderBody = intent.getStringExtra("orderBody");
        createTime = this.getIntent().hasExtra("createTime")?this.getIntent().getStringExtra("createTime"):"";
        dynamicSeq = this.getIntent().hasExtra("dynamicSeq")?this.getIntent().getStringExtra("dynamicSeq"):"";
        task_label = this.getIntent().hasExtra("task_label")?this.getIntent().getStringExtra("task_label"):"";
        hxid = intent.getStringExtra("hxid");
        zy1 = intent.getStringExtra("zy1");
        biaoshi = intent.getStringExtra("biaoshi");
        if ("01".equals(typeDetail)){
            ll_xiadan.setVisibility(View.VISIBLE);
            ll1_paidan.setVisibility(View.GONE);
            ll2_paidan.setVisibility(View.GONE);
            iv_jiantou.setVisibility(View.GONE);
        }else if ("02".equals(typeDetail)){
            ll_xiadan.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);
            ll1_paidan.setVisibility(View.VISIBLE);
            ll2_paidan.setVisibility(View.VISIBLE);
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
        if ("00".equals(biaoshi)){
            tv_send.setVisibility(View.VISIBLE);
        }
        Log.e("uorderdeac",biaoshi);
        if (!"03".equals(biaoshi)&&!"04".equals(biaoshi)&&!"05".equals(biaoshi)&&!"06".equals(biaoshi)&&!"07".equals(biaoshi)&&!"08".equals(biaoshi)) {
            initView2();
        }else if ("03".equals(biaoshi)){
            initView3();
        }else if ("04".equals(biaoshi)){
            initView4();
        }else if ("05".equals(biaoshi)){
            tv_send.setVisibility(View.VISIBLE);
            initView5();
        }else if ("06".equals(biaoshi)){
            initView6();
        }else if ("07".equals(biaoshi)){
            initView7();
        }else if ("08".equals(biaoshi)){
            initView8();
        }
        if ("01".equals(biaoshi)){
            String balance = this.getIntent().getStringExtra("balance");
            etAllPrice.setText(balance);
            etAllPrice.setEnabled(false);
            etAllPrice.setTextColor(Color.BLUE);
        }

    }

    private void initView8() {
        etBody1.setEnabled(true);
        etBody2.setEnabled(true);
        etBody3.setEnabled(true);
        etBody4.setEnabled(true);
        etBody5.setEnabled(true);
        etCount1.setEnabled(true);
        etCount2.setEnabled(true);
        etCount3.setEnabled(true);
        etCount4.setEnabled(true);
        etCount5.setEnabled(true);
        etDanjia1.setEnabled(true);
        etDanjia2.setEnabled(true);
        etDanjia3.setEnabled(true);
        etDanjia4.setEnabled(true);
        etDanjia5.setEnabled(true);
        etAllPrice.setEnabled(true);
        btnCommit.setEnabled(true);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setText("发  送");
        btnCommit.setWidth(200);
        tv_send.setVisibility(View.VISIBLE);
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtoself();
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtoself();
            }
        });
    }

    private void sendtoself() {
        oP1 = TextUtils.isEmpty(etBody1.getText().toString().trim()) ? "" : etBody1.getText().toString().trim();
        oP2 = TextUtils.isEmpty(etBody2.getText().toString().trim()) ? "" : etBody2.getText().toString().trim();
        oP3 = TextUtils.isEmpty(etBody3.getText().toString().trim()) ? "" : etBody3.getText().toString().trim();
        oP4 = TextUtils.isEmpty(etBody4.getText().toString().trim()) ? "" : etBody4.getText().toString().trim();
        oP5 = TextUtils.isEmpty(etBody5.getText().toString().trim()) ? "" : etBody5.getText().toString().trim();
        oN1 = TextUtils.isEmpty(etCount1.getText().toString().trim()) ? "" : etCount1.getText().toString().trim();
        oN2 = TextUtils.isEmpty(etCount2.getText().toString().trim()) ? "" : etCount2.getText().toString().trim();
        oN3 = TextUtils.isEmpty(etCount3.getText().toString().trim()) ? "" : etCount3.getText().toString().trim();
        oN4 = TextUtils.isEmpty(etCount4.getText().toString().trim()) ? "" : etCount4.getText().toString().trim();
        oN5 = TextUtils.isEmpty(etCount5.getText().toString().trim()) ? "" : etCount5.getText().toString().trim();
        oA1 = TextUtils.isEmpty(etDanjia1.getText().toString().trim()) ? "" : etDanjia1.getText().toString().trim();
        oA2 = TextUtils.isEmpty(etDanjia2.getText().toString().trim()) ? "" : etDanjia2.getText().toString().trim();
        oA3 = TextUtils.isEmpty(etDanjia3.getText().toString().trim()) ? "" : etDanjia3.getText().toString().trim();
        oA4 = TextUtils.isEmpty(etDanjia4.getText().toString().trim()) ? "" : etDanjia4.getText().toString().trim();
        oA5 = TextUtils.isEmpty(etDanjia5.getText().toString().trim()) ? "" : etDanjia5.getText().toString().trim();
        orderProject = oP1 + "," + oP2 + "," + oP3 + "," + oP4 + "," + oP5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
        orderNumber = oN1 + "," + oN2 + "," + oN3 + "," + oN4 + "," + oN5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
        orderAmt = oA1 + "," + oA2 + "," + oA3 + "," + oA4 + "," + oA5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
        zongjia = etAllPrice.getText().toString().trim();
        if (zongjia==null||"".equals(zongjia)||Double.parseDouble(zongjia)<=0){
            Toast.makeText(getApplicationContext(),"请输入总价！",Toast.LENGTH_SHORT).show();
            return;
        }
        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_order_send, null);
        final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                if (ContextCompat.checkSelfPermission(UOrderDetailActivity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UOrderDetailActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                }else {
                    Intent intent = new Intent(UOrderDetailActivity.this,AddressListTwoActivity.class);
                    intent.putExtra("biaoshi","05");
                    intent.putExtra("orderBody",orderBody);
                    intent.putExtra("flg","01");
                    intent.putExtra("upId",zy1);
                    intent.putExtra("orderProject",orderProject);
                    intent.putExtra("orderNumber",orderNumber);
                    intent.putExtra("orderAmt",orderAmt);
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
                Intent intent3 = new Intent(UOrderDetailActivity.this,FriendActivity.class);
                intent3.putExtra("biaoshi","05");
                intent3.putExtra("orderBody",orderBody);
                intent3.putExtra("flg","01");
                intent3.putExtra("upId",zy1);
                intent3.putExtra("orderProject",orderProject);
                intent3.putExtra("orderNumber",orderNumber);
                intent3.putExtra("orderAmt",orderAmt);
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
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
                        Toast.makeText(UOrderDetailActivity.this, "下单失败！", Toast.LENGTH_SHORT).show();
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
                params.put("merId", wodezhanghao);
                params.put("orderBody", orderBody);
                params.put("flg", "01");
                params.put("type", "01");
                params.put("upId", zy1);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
    }

    private void bianji2(final String userId, final String biaoshi){
        String url = FXConstant.URL_INSERT_OrderDetail;
        StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("数据更新成功")) {
                        sendPushMessage2(userId,0);
                        updateUscount(userId);
                        if ("00".equals(biaoshi)){
                            SendMessage1(userId);
                        }else {
                            SendMessage(userId);
                        }
                    } else {
                        Toast.makeText(UOrderDetailActivity.this, "网络错误，请重新编辑！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UOrderDetailActivity.this, "网络错误，请重新编辑！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                params1.put("orderId", orderId);
                params1.put("userId", userId);
                params1.put("state", "02");
                params1.put("orderProject", orderProject);
                params1.put("orderNumber", orderNumber);
                params1.put("orderAmt", orderAmt);
                params1.put("orderSum", zongjia);
                if (!"".equals(time)) {
                    params1.put("conventionTime", time);
                }
                return params1;
            }
        };
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request3);
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
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
                MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);

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
                MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {
                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void initView7() {
        etBody1.setEnabled(true);
        etBody2.setEnabled(true);
        etBody3.setEnabled(true);
        etBody4.setEnabled(true);
        etBody5.setEnabled(true);
        etCount1.setEnabled(true);
        etCount2.setEnabled(true);
        etCount3.setEnabled(true);
        etCount4.setEnabled(true);
        etCount5.setEnabled(true);
        etDanjia1.setEnabled(true);
        etDanjia2.setEnabled(true);
        etDanjia3.setEnabled(true);
        etDanjia4.setEnabled(true);
        etDanjia5.setEnabled(true);
        btnCansul.setVisibility(View.GONE);
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
        orderAmt = TextUtils.isEmpty(orderDetail.getOrderAmt())?"":orderDetail.getOrderAmt();
        int maxSplit = 6;
        String[] orderProjectArray = orderProject.split(",", maxSplit);
        if (orderProjectArray.length>0) {
            oP1 = orderProjectArray[0];
        }
        if (orderProjectArray.length>1) {
            oP2 = orderProjectArray[1];
        }
        if (orderProjectArray.length>2) {
            oP3 = orderProjectArray[2];
        }
        if (orderProjectArray.length>3) {
            oP4 = orderProjectArray[3];
        }
        if (orderProjectArray.length>4) {
            oP5 = orderProjectArray[4];
        }
        String[] orderNumberArray = orderNumber.split(",",maxSplit);
        if (orderNumberArray.length>0) {
            oN1 = orderNumberArray[0];
        }
        if (orderNumberArray.length>1) {
            oN2 = orderNumberArray[1];
        }
        if (orderNumberArray.length>2) {
            oN3 = orderNumberArray[2];
        }
        if (orderNumberArray.length>3) {
            oN4 = orderNumberArray[3];
        }
        if (orderNumberArray.length>4) {
            oN5 = orderNumberArray[4];
        }
        String[] orderAmtArray = orderAmt.split(",",maxSplit);
        if (orderAmtArray.length>0) {
            oA1 = orderAmtArray[0];
        }
        if (orderAmtArray.length>1) {
            oA2 = orderAmtArray[1];
        }
        if (orderAmtArray.length>2) {
            oA3 = orderAmtArray[2];
        }
        if (orderAmtArray.length>3) {
            oA4 = orderAmtArray[3];
        }
        if (orderAmtArray.length>4) {
            oA5 = orderAmtArray[4];
        }
        etBody1.setText(oP1);
        etBody2.setText(oP2);
        etBody3.setText(oP3);
        etBody4.setText(oP4);
        etBody5.setText(oP5);
        etCount1.setText(oN1);
        etCount2.setText(oN2);
        etCount3.setText(oN3);
        etCount4.setText(oN4);
        etCount5.setText(oN5);
        etDanjia1.setText(oA1);
        etDanjia2.setText(oA2);
        etDanjia3.setText(oA3);
        etDanjia4.setText(oA4);
        etDanjia5.setText(oA5);
        etAllPrice.setText(zongjia);
        etAllPrice.setEnabled(true);
        btnCommit.setEnabled(true);
        btnCommit.setWidth(200);
        btnCommit.setText("修改报价");
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zongjia = etAllPrice.getText().toString().trim();
                if (zongjia==null||"".equals(zongjia)||Double.parseDouble(zongjia)<=0){
                    Toast.makeText(getApplicationContext(),"请输入报价！",Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                btnOK1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                        oP1 = TextUtils.isEmpty(etBody1.getText().toString().trim()) ? "" : etBody1.getText().toString().trim();
                        oP2 = TextUtils.isEmpty(etBody2.getText().toString().trim()) ? "" : etBody2.getText().toString().trim();
                        oP3 = TextUtils.isEmpty(etBody3.getText().toString().trim()) ? "" : etBody3.getText().toString().trim();
                        oP4 = TextUtils.isEmpty(etBody4.getText().toString().trim()) ? "" : etBody4.getText().toString().trim();
                        oP5 = TextUtils.isEmpty(etBody5.getText().toString().trim()) ? "" : etBody5.getText().toString().trim();
                        oN1 = TextUtils.isEmpty(etCount1.getText().toString().trim()) ? "" : etCount1.getText().toString().trim();
                        oN2 = TextUtils.isEmpty(etCount2.getText().toString().trim()) ? "" : etCount2.getText().toString().trim();
                        oN3 = TextUtils.isEmpty(etCount3.getText().toString().trim()) ? "" : etCount3.getText().toString().trim();
                        oN4 = TextUtils.isEmpty(etCount4.getText().toString().trim()) ? "" : etCount4.getText().toString().trim();
                        oN5 = TextUtils.isEmpty(etCount5.getText().toString().trim()) ? "" : etCount5.getText().toString().trim();
                        oA1 = TextUtils.isEmpty(etDanjia1.getText().toString().trim()) ? "" : etDanjia1.getText().toString().trim();
                        oA2 = TextUtils.isEmpty(etDanjia2.getText().toString().trim()) ? "" : etDanjia2.getText().toString().trim();
                        oA3 = TextUtils.isEmpty(etDanjia3.getText().toString().trim()) ? "" : etDanjia3.getText().toString().trim();
                        oA4 = TextUtils.isEmpty(etDanjia4.getText().toString().trim()) ? "" : etDanjia4.getText().toString().trim();
                        oA5 = TextUtils.isEmpty(etDanjia5.getText().toString().trim()) ? "" : etDanjia5.getText().toString().trim();
                        orderProject = oP1 + "," + oP2 + "," + oP3 + "," + oP4 + "," + oP5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                        orderNumber = oN1 + "," + oN2 + "," + oN3 + "," + oN4 + "," + oN5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                        orderAmt = oA1 + "," + oA2 + "," + oA3 + "," + oA4 + "," + oA5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                        String url = FXConstant.URL_UPDATE_BAOJIA;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                                String code = object.getString("code");
                                if ("success".equals(code)){
                                    Toast.makeText(getApplicationContext(),"修改成功！",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                if (volleyError!=null&&volleyError.getMessage()!=null) {
                                    Log.e("uorderac,volleyError", volleyError.getMessage());
                                    Toast.makeText(getApplicationContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> param = new HashMap<String, String>();
                                param.put("order_id;",orderId);
                                param.put("timestamp",createTime);
                                param.put("dynamic_seq",dynamicSeq);
                                param.put("orderProject", orderProject);
                                param.put("orderNumber", orderNumber);
                                param.put("orderAmt", orderAmt);
                                param.put("quote", zongjia);
                                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                                if (!"".equals(time)) {
                                    param.put("conventionTime", time);
                                }
                                return param;
                            }
                        };
                        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
                    }
                });
            }
        });
    }

    private void initView6() {
        merId = getIntent().getStringExtra("merId");
        orderId = getIntent().getStringExtra("orderId");
        final String userId = getIntent().getStringExtra("userId");
        hxid = userId;
        etBody1.setEnabled(false);
        etBody2.setEnabled(false);
        etBody3.setEnabled(false);
        etBody4.setEnabled(false);
        etBody5.setEnabled(false);
        etCount1.setEnabled(false);
        etCount2.setEnabled(false);
        etCount3.setEnabled(false);
        etCount4.setEnabled(false);
        etCount5.setEnabled(false);
        etDanjia1.setEnabled(false);
        etDanjia2.setEnabled(false);
        etDanjia3.setEnabled(false);
        etDanjia4.setEnabled(false);
        etDanjia5.setEnabled(false);
        btnCansul.setVisibility(View.GONE);
        queryOrder();
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
                    orderProject = TextUtils.isEmpty(orderDetail.getOrderProject())?"":orderDetail.getOrderProject();
                    orderNumber = TextUtils.isEmpty(orderDetail.getOrderNumber())?"":orderDetail.getOrderNumber();
                    orderAmt = TextUtils.isEmpty(orderDetail.getOrderAmt())?"":orderDetail.getOrderAmt();
                    zongjia = orderDetail.getOrderSum();
                    int maxSplit = 6;
                    String[] orderProjectArray = orderProject.split(",", maxSplit);
                    if (orderProjectArray.length>0) {
                        oP1 = orderProjectArray[0];
                    }
                    if (orderProjectArray.length>1) {
                        oP2 = orderProjectArray[1];
                    }
                    if (orderProjectArray.length>2) {
                        oP3 = orderProjectArray[2];
                    }
                    if (orderProjectArray.length>3) {
                        oP4 = orderProjectArray[3];
                    }
                    if (orderProjectArray.length>4) {
                        oP5 = orderProjectArray[4];
                    }
                    String[] orderNumberArray = orderNumber.split(",",maxSplit);
                    if (orderNumberArray.length>0) {
                        oN1 = orderNumberArray[0];
                    }
                    if (orderNumberArray.length>1) {
                        oN2 = orderNumberArray[1];
                    }
                    if (orderNumberArray.length>2) {
                        oN3 = orderNumberArray[2];
                    }
                    if (orderNumberArray.length>3) {
                        oN4 = orderNumberArray[3];
                    }
                    if (orderNumberArray.length>4) {
                        oN5 = orderNumberArray[4];
                    }
                    String[] orderAmtArray = orderAmt.split(",",maxSplit);
                    if (orderAmtArray.length>0) {
                        oA1 = orderAmtArray[0];
                    }
                    if (orderAmtArray.length>1) {
                        oA2 = orderAmtArray[1];
                    }
                    if (orderAmtArray.length>2) {
                        oA3 = orderAmtArray[2];
                    }
                    if (orderAmtArray.length>3) {
                        oA4 = orderAmtArray[3];
                    }
                    if (orderAmtArray.length>4) {
                        oA5 = orderAmtArray[4];
                    }
                    etBody1.setText(oP1);
                    etBody2.setText(oP2);
                    etBody3.setText(oP3);
                    etBody4.setText(oP4);
                    etBody5.setText(oP5);
                    etCount1.setText(oN1);
                    etCount2.setText(oN2);
                    etCount3.setText(oN3);
                    etCount4.setText(oN4);
                    etCount5.setText(oN5);
                    etDanjia1.setText(oA1);
                    etDanjia2.setText(oA2);
                    etDanjia3.setText(oA3);
                    etDanjia4.setText(oA4);
                    etDanjia5.setText(oA5);
                    etAllPrice.setText(zongjia);
                    etAllPrice.setEnabled(false);
                    countDown.setEnabled(false);
                    String yueding = TextUtils.isEmpty(orderDetail.getResv1())?"":orderDetail.getResv1();
                    String resv2 = TextUtils.isEmpty(orderDetail.getResv2())?"":orderDetail.getResv2();
                    runTime(yueding,resv2);
                    if (wodezhanghao.equals(userId)) {
                        btnCommit.setEnabled(true);
                        btnCommit.setText("验  资");
                        btnCommit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String sum = etAllPrice.getText().toString().trim();
                                if (!TextUtils.isEmpty(sum)&&Double.parseDouble(sum)>0) {
                                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                                    final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                                    Toast.makeText(UOrderDetailActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        etAllPrice.setEnabled(false);
                        btnCommit.setEnabled(false);
                        btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                        btnCommit.setText("等待对方验资");
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
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
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
        Double d = yuE - orderSu;
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
            if (d >= 0) {
                LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
                final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                        pd = new ProgressDialog(UOrderDetailActivity.this);
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
                                ZhifuHelpUtils.showErrorLing(UOrderDetailActivity.this);
                            }else {
                                ZhifuHelpUtils.showErrorTishi(UOrderDetailActivity.this,times + "",null,"000");
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
                        startActivity(new Intent(UOrderDetailActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                    }
                });
            } else {
                LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request3);
    }

    private void initView5() {
        final String userId = getIntent().getStringExtra("userId");
        merId = getIntent().getStringExtra("merId");
        orderId = getIntent().getStringExtra("orderId");
        hxid = merId;
        if (wodezhanghao.equals(merId)) {
            etBody1.setEnabled(true);
            etBody2.setEnabled(true);
            etBody3.setEnabled(true);
            etBody4.setEnabled(true);
            etBody5.setEnabled(true);
            etCount1.setEnabled(true);
            etCount2.setEnabled(true);
            etCount3.setEnabled(true);
            etCount4.setEnabled(true);
            etCount5.setEnabled(true);
            etDanjia1.setEnabled(true);
            etDanjia2.setEnabled(true);
            etDanjia3.setEnabled(true);
            etDanjia4.setEnabled(true);
            etDanjia5.setEnabled(true);
            btnCansul.setVisibility(View.GONE);
            btnCommit.setEnabled(true);
            btnCommit.setText("发  送");
        }else {
            etBody1.setEnabled(false);
            etBody2.setEnabled(false);
            etBody3.setEnabled(false);
            etBody4.setEnabled(false);
            etBody5.setEnabled(false);
            etCount1.setEnabled(false);
            etCount2.setEnabled(false);
            etCount3.setEnabled(false);
            etCount4.setEnabled(false);
            etCount5.setEnabled(false);
            etDanjia1.setEnabled(false);
            etDanjia2.setEnabled(false);
            etDanjia3.setEnabled(false);
            etDanjia4.setEnabled(false);
            etDanjia5.setEnabled(false);
            etAllPrice.setEnabled(false);
            tv_send.setVisibility(View.INVISIBLE);
            btnCansul.setVisibility(View.GONE);
            btnCommit.setEnabled(false);
            btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            btnCommit.setText("等待对方编辑");
        }
        etDanjia1.addTextChangedListener(this);
        etDanjia2.addTextChangedListener(this);
        etDanjia3.addTextChangedListener(this);
        etDanjia4.addTextChangedListener(this);
        etDanjia5.addTextChangedListener(this);
        tv_send.setOnClickListener(new View.OnClickListener() {
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

                        String jine = etAllPrice.getText().toString().trim();
                        if (TextUtils.isEmpty(jine)||Double.parseDouble(jine)==0){
                            Toast.makeText(UOrderDetailActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                                ScreenshotUtil.getBitmapByView2(UOrderDetailActivity.this, findViewById(R.id.ll_all),false);
                                zongjia = etAllPrice.getText().toString().trim();
                                oP1 = TextUtils.isEmpty(etBody1.getText().toString().trim()) ? "" : etBody1.getText().toString().trim();
                                oP2 = TextUtils.isEmpty(etBody2.getText().toString().trim()) ? "" : etBody2.getText().toString().trim();
                                oP3 = TextUtils.isEmpty(etBody3.getText().toString().trim()) ? "" : etBody3.getText().toString().trim();
                                oP4 = TextUtils.isEmpty(etBody4.getText().toString().trim()) ? "" : etBody4.getText().toString().trim();
                                oP5 = TextUtils.isEmpty(etBody5.getText().toString().trim()) ? "" : etBody5.getText().toString().trim();
                                oN1 = TextUtils.isEmpty(etCount1.getText().toString().trim()) ? "" : etCount1.getText().toString().trim();
                                oN2 = TextUtils.isEmpty(etCount2.getText().toString().trim()) ? "" : etCount2.getText().toString().trim();
                                oN3 = TextUtils.isEmpty(etCount3.getText().toString().trim()) ? "" : etCount3.getText().toString().trim();
                                oN4 = TextUtils.isEmpty(etCount4.getText().toString().trim()) ? "" : etCount4.getText().toString().trim();
                                oN5 = TextUtils.isEmpty(etCount5.getText().toString().trim()) ? "" : etCount5.getText().toString().trim();
                                oA1 = TextUtils.isEmpty(etDanjia1.getText().toString().trim()) ? "" : etDanjia1.getText().toString().trim();
                                oA2 = TextUtils.isEmpty(etDanjia2.getText().toString().trim()) ? "" : etDanjia2.getText().toString().trim();
                                oA3 = TextUtils.isEmpty(etDanjia3.getText().toString().trim()) ? "" : etDanjia3.getText().toString().trim();
                                oA4 = TextUtils.isEmpty(etDanjia4.getText().toString().trim()) ? "" : etDanjia4.getText().toString().trim();
                                oA5 = TextUtils.isEmpty(etDanjia5.getText().toString().trim()) ? "" : etDanjia5.getText().toString().trim();
                                orderProject = oP1 + "," + oP2 + "," + oP3 + "," + oP4 + "," + oP5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                                orderNumber = oN1 + "," + oN2 + "," + oN3 + "," + oN4 + "," + oN5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                                orderAmt = oA1 + "," + oA2 + "," + oA3 + "," + oA4 + "," + oA5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                                String url = FXConstant.URL_INSERT_OrderDetail;
                                StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        try {
                                            JSONObject object = new JSONObject(s);
                                            String code = object.getString("code");
                                            if (code.equals("数据更新成功")) {
                                                String filePath = Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png";
                                                EMMessage message = EMMessage.createImageSendMessage(filePath,false,userId);
                                                message.setAttribute("flg","01");
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
                                                Toast.makeText(UOrderDetailActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(UOrderDetailActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(UOrderDetailActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params1 = new HashMap<>();
                                        params1.put("orderId", orderId);
                                        params1.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                        params1.put("orderProject", orderProject);
                                        params1.put("orderNumber", orderNumber);
                                        params1.put("orderAmt", orderAmt);
                                        params1.put("orderSum", zongjia);
                                        if (!"".equals(time)) {
                                            params1.put("conventionTime", time);
                                        }
                                        return params1;
                                    }
                                };
                                MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request3);
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
        btnCommit.setOnClickListener(new View.OnClickListener() {
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

                        String jine = etAllPrice.getText().toString().trim();
                        if (TextUtils.isEmpty(jine)||Double.parseDouble(jine)==0){
                            Toast.makeText(UOrderDetailActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                                ScreenshotUtil.getBitmapByView2(UOrderDetailActivity.this, findViewById(R.id.ll_all),false);
                                zongjia = etAllPrice.getText().toString().trim();
                                oP1 = TextUtils.isEmpty(etBody1.getText().toString().trim()) ? "" : etBody1.getText().toString().trim();
                                oP2 = TextUtils.isEmpty(etBody2.getText().toString().trim()) ? "" : etBody2.getText().toString().trim();
                                oP3 = TextUtils.isEmpty(etBody3.getText().toString().trim()) ? "" : etBody3.getText().toString().trim();
                                oP4 = TextUtils.isEmpty(etBody4.getText().toString().trim()) ? "" : etBody4.getText().toString().trim();
                                oP5 = TextUtils.isEmpty(etBody5.getText().toString().trim()) ? "" : etBody5.getText().toString().trim();
                                oN1 = TextUtils.isEmpty(etCount1.getText().toString().trim()) ? "" : etCount1.getText().toString().trim();
                                oN2 = TextUtils.isEmpty(etCount2.getText().toString().trim()) ? "" : etCount2.getText().toString().trim();
                                oN3 = TextUtils.isEmpty(etCount3.getText().toString().trim()) ? "" : etCount3.getText().toString().trim();
                                oN4 = TextUtils.isEmpty(etCount4.getText().toString().trim()) ? "" : etCount4.getText().toString().trim();
                                oN5 = TextUtils.isEmpty(etCount5.getText().toString().trim()) ? "" : etCount5.getText().toString().trim();
                                oA1 = TextUtils.isEmpty(etDanjia1.getText().toString().trim()) ? "" : etDanjia1.getText().toString().trim();
                                oA2 = TextUtils.isEmpty(etDanjia2.getText().toString().trim()) ? "" : etDanjia2.getText().toString().trim();
                                oA3 = TextUtils.isEmpty(etDanjia3.getText().toString().trim()) ? "" : etDanjia3.getText().toString().trim();
                                oA4 = TextUtils.isEmpty(etDanjia4.getText().toString().trim()) ? "" : etDanjia4.getText().toString().trim();
                                oA5 = TextUtils.isEmpty(etDanjia5.getText().toString().trim()) ? "" : etDanjia5.getText().toString().trim();
                                orderProject = oP1 + "," + oP2 + "," + oP3 + "," + oP4 + "," + oP5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                                orderNumber = oN1 + "," + oN2 + "," + oN3 + "," + oN4 + "," + oN5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                                orderAmt = oA1 + "," + oA2 + "," + oA3 + "," + oA4 + "," + oA5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                                String url = FXConstant.URL_INSERT_OrderDetail;
                                StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                        try {
                                            JSONObject object = new JSONObject(s);
                                            String code = object.getString("code");
                                            if (code.equals("数据更新成功")) {
                                                String filePath = Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png";
                                                EMMessage message = EMMessage.createImageSendMessage(filePath,false,userId);
                                                message.setAttribute("flg","01");
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
                                                Toast.makeText(UOrderDetailActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(UOrderDetailActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(UOrderDetailActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params1 = new HashMap<>();
                                        params1.put("orderId", orderId);
                                        params1.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                        params1.put("orderProject", orderProject);
                                        params1.put("orderNumber", orderNumber);
                                        params1.put("orderAmt", orderAmt);
                                        params1.put("orderSum", zongjia);
                                        if (!"".equals(time)) {
                                            params1.put("conventionTime", time);
                                        }
                                        return params1;
                                    }
                                };
                                MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request3);
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
        etBody1.setEnabled(false);
        etBody2.setEnabled(false);
        etBody3.setEnabled(false);
        etBody4.setEnabled(false);
        etBody5.setEnabled(false);
        etCount1.setEnabled(false);
        etCount2.setEnabled(false);
        etCount3.setEnabled(false);
        etCount4.setEnabled(false);
        etCount5.setEnabled(false);
        etDanjia1.setEnabled(false);
        etDanjia2.setEnabled(false);
        etDanjia3.setEnabled(false);
        etDanjia4.setEnabled(false);
        etDanjia5.setEnabled(false);
        btnCansul.setVisibility(View.GONE);
        String shuju = getIntent().getStringExtra("shuju");
        orderId = getIntent().getStringExtra("orderId");
        dynamicSeq = getIntent().getStringExtra("dynamic_seq");
        createTime = getIntent().getStringExtra("timestamp");
        task_label = getIntent().getStringExtra("task_label");
        hxid = getIntent().getStringExtra("u_id");
        zongjia = getIntent().getStringExtra("quote");
        Log.e("uorderdetail,or",orderId);
        Log.e("uorderdetail,shuju",shuju);
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
        orderAmt = TextUtils.isEmpty(orderDetail.getOrderAmt())?"":orderDetail.getOrderAmt();
        int maxSplit = 6;
        String[] orderProjectArray = orderProject.split(",", maxSplit);
        if (orderProjectArray.length>0) {
            oP1 = orderProjectArray[0];
        }
        if (orderProjectArray.length>1) {
            oP2 = orderProjectArray[1];
        }
        if (orderProjectArray.length>2) {
            oP3 = orderProjectArray[2];
        }
        if (orderProjectArray.length>3) {
            oP4 = orderProjectArray[3];
        }
        if (orderProjectArray.length>4) {
            oP5 = orderProjectArray[4];
        }
        String[] orderNumberArray = orderNumber.split(",",maxSplit);
        if (orderNumberArray.length>0) {
            oN1 = orderNumberArray[0];
        }
        if (orderNumberArray.length>1) {
            oN2 = orderNumberArray[1];
        }
        if (orderNumberArray.length>2) {
            oN3 = orderNumberArray[2];
        }
        if (orderNumberArray.length>3) {
            oN4 = orderNumberArray[3];
        }
        if (orderNumberArray.length>4) {
            oN5 = orderNumberArray[4];
        }
        String[] orderAmtArray = orderAmt.split(",",maxSplit);
        if (orderAmtArray.length>0) {
            oA1 = orderAmtArray[0];
        }
        if (orderAmtArray.length>1) {
            oA2 = orderAmtArray[1];
        }
        if (orderAmtArray.length>2) {
            oA3 = orderAmtArray[2];
        }
        if (orderAmtArray.length>3) {
            oA4 = orderAmtArray[3];
        }
        if (orderAmtArray.length>4) {
            oA5 = orderAmtArray[4];
        }
        etBody1.setText(oP1);
        etBody2.setText(oP2);
        etBody3.setText(oP3);
        etBody4.setText(oP4);
        etBody5.setText(oP5);
        etCount1.setText(oN1);
        etCount2.setText(oN2);
        etCount3.setText(oN3);
        etCount4.setText(oN4);
        etCount5.setText(oN5);
        etDanjia1.setText(oA1);
        etDanjia2.setText(oA2);
        etDanjia3.setText(oA3);
        etDanjia4.setText(oA4);
        etDanjia5.setText(oA5);
        etAllPrice.setText(zongjia);
        etAllPrice.setEnabled(false);
        btnCommit.setEnabled(true);
        btnCommit.setWidth(200);
        btnCommit.setText("验  资");
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sum = etAllPrice.getText().toString().trim();
                if (!TextUtils.isEmpty(sum)&&Double.parseDouble(sum)>0) {
                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                    final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                    Toast.makeText(UOrderDetailActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        Double d = yuE - orderSu;
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        if (d >= 0) {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                    pd = new ProgressDialog(UOrderDetailActivity.this);
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
                            ZhifuHelpUtils.showErrorLing(UOrderDetailActivity.this);
                        }else {
                            ZhifuHelpUtils.showErrorTishi(UOrderDetailActivity.this,times + "",null,"000");
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
                    startActivity(new Intent(UOrderDetailActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (hxid.length()>12){
//            yuE = DemoApplication.getApp().getCurrenQiyePrice();
//            pass = DemoApplication.getInstance().getCurrentQiyePayPass();
//        }else {
        yuE = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

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

    private void initView() {
        etBody1 = (EditText) findViewById(R.id.et_body1);
        etBody2 = (EditText) findViewById(R.id.et_body2);
        etBody3 = (EditText) findViewById(R.id.et_body3);
        etBody4 = (EditText) findViewById(R.id.et_body4);
        etBody5 = (EditText) findViewById(R.id.et_body5);
        etCount1 = (EditText) findViewById(R.id.et_count1);
        etCount2 = (EditText) findViewById(R.id.et_count2);
        etCount3 = (EditText) findViewById(R.id.et_count3);
        etCount4 = (EditText) findViewById(R.id.et_count4);
        etCount5 = (EditText) findViewById(R.id.et_count5);
        etDanjia1 = (EditText) findViewById(R.id.et_danjia1);
        etDanjia2 = (EditText) findViewById(R.id.et_danjia2);
        etDanjia3 = (EditText) findViewById(R.id.et_danjia3);
        etDanjia4 = (EditText) findViewById(R.id.et_danjia4);
        etDanjia5 = (EditText) findViewById(R.id.et_danjia5);
        etAllPrice = (EditText) findViewById(R.id.et_all);
        etUBeizhu = (EditText) findViewById(R.id.et_Ubeizhu);
        btnCommit = (Button) findViewById(R.id.btn_comit);
        btnCansul = (Button) findViewById(R.id.btn_cansul);
        tvJiedan = (TextView) findViewById(R.id.tv_jiedan);
        tvXiadan = (TextView) findViewById(R.id.tv_xiadan);
        etDanjia1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etDanjia2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etDanjia3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etDanjia4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etDanjia5.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etAllPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters={new CashierInputFilter()};
        etDanjia1.setFilters(filters);
        etDanjia2.setFilters(filters);
        etDanjia3.setFilters(filters);
        etDanjia4.setFilters(filters);
        etDanjia5.setFilters(filters);
        etAllPrice.setFilters(filters);
        llM_beizhu.setVisibility(View.GONE);
        tvJiedan.setVisibility(View.GONE);
        tvXiadan.setVisibility(View.GONE);
        etUBeizhu.setEnabled(false);
        btnCansul.setEnabled(false);
        btnCommit.setEnabled(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str1 = TextUtils.isEmpty(etDanjia1.getText().toString().trim())?"0":etDanjia1.getText().toString().trim();
        String str2 = TextUtils.isEmpty(etDanjia2.getText().toString().trim())?"0":etDanjia2.getText().toString().trim();
        String str3 = TextUtils.isEmpty(etDanjia3.getText().toString().trim())?"0":etDanjia3.getText().toString().trim();
        String str4 = TextUtils.isEmpty(etDanjia4.getText().toString().trim())?"0":etDanjia4.getText().toString().trim();
        String str5 = TextUtils.isEmpty(etDanjia5.getText().toString().trim())?"0":etDanjia5.getText().toString().trim();
        double d1 = Double.valueOf(str1);
        double d2 = Double.valueOf(str2);
        double d3 = Double.valueOf(str3);
        double d4 = Double.valueOf(str4);
        double d5 = Double.valueOf(str5);
        double alld = d1+d2+d3+d4+d5;
        etAllPrice.setText(String.valueOf(alld));
    }
    private void initView3() {
        etBody1.setEnabled(true);
        etBody2.setEnabled(true);
        etBody3.setEnabled(true);
        etBody4.setEnabled(true);
        etBody5.setEnabled(true);
        etCount1.setEnabled(true);
        etCount2.setEnabled(true);
        etCount3.setEnabled(true);
        etCount4.setEnabled(true);
        etCount5.setEnabled(true);
        etDanjia1.setEnabled(true);
        etDanjia2.setEnabled(true);
        etDanjia3.setEnabled(true);
        etDanjia4.setEnabled(true);
        etDanjia5.setEnabled(true);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setEnabled(true);
        btnCommit.setText("接单报价");
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zongjia = etAllPrice.getText().toString().trim();
                if (zongjia==null||"".equals(zongjia)||Double.parseDouble(zongjia)<=0){
                    Toast.makeText(getApplicationContext(),"请输入报价！",Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                btnOK1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                        oP1 = TextUtils.isEmpty(etBody1.getText().toString().trim()) ? "" : etBody1.getText().toString().trim();
                        oP2 = TextUtils.isEmpty(etBody2.getText().toString().trim()) ? "" : etBody2.getText().toString().trim();
                        oP3 = TextUtils.isEmpty(etBody3.getText().toString().trim()) ? "" : etBody3.getText().toString().trim();
                        oP4 = TextUtils.isEmpty(etBody4.getText().toString().trim()) ? "" : etBody4.getText().toString().trim();
                        oP5 = TextUtils.isEmpty(etBody5.getText().toString().trim()) ? "" : etBody5.getText().toString().trim();
                        oN1 = TextUtils.isEmpty(etCount1.getText().toString().trim()) ? "" : etCount1.getText().toString().trim();
                        oN2 = TextUtils.isEmpty(etCount2.getText().toString().trim()) ? "" : etCount2.getText().toString().trim();
                        oN3 = TextUtils.isEmpty(etCount3.getText().toString().trim()) ? "" : etCount3.getText().toString().trim();
                        oN4 = TextUtils.isEmpty(etCount4.getText().toString().trim()) ? "" : etCount4.getText().toString().trim();
                        oN5 = TextUtils.isEmpty(etCount5.getText().toString().trim()) ? "" : etCount5.getText().toString().trim();
                        oA1 = TextUtils.isEmpty(etDanjia1.getText().toString().trim()) ? "" : etDanjia1.getText().toString().trim();
                        oA2 = TextUtils.isEmpty(etDanjia2.getText().toString().trim()) ? "" : etDanjia2.getText().toString().trim();
                        oA3 = TextUtils.isEmpty(etDanjia3.getText().toString().trim()) ? "" : etDanjia3.getText().toString().trim();
                        oA4 = TextUtils.isEmpty(etDanjia4.getText().toString().trim()) ? "" : etDanjia4.getText().toString().trim();
                        oA5 = TextUtils.isEmpty(etDanjia5.getText().toString().trim()) ? "" : etDanjia5.getText().toString().trim();
                        orderProject = oP1 + "," + oP2 + "," + oP3 + "," + oP4 + "," + oP5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                        orderNumber = oN1 + "," + oN2 + "," + oN3 + "," + oN4 + "," + oN5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                        orderAmt = oA1 + "," + oA2 + "," + oA3 + "," + oA4 + "," + oA5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
                        String url = FXConstant.URL_INSERT_DYNAMIC_ORDER;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject object = new JSONObject(s);
                                    String code = object.getString("code");
                                    if ("success".equals(code)){
                                        sendPushMessage(hxid,"003");
                                        addPinglinCount(createTime,dynamicSeq);
                                        insertDynamicContact(dynamicSeq,createTime,DemoHelper.getInstance().getCurrentUsernName(),hxid,"00");

                                        //接单报价给用户发短信
                                        duanxintongzhi(hxid,"【正事多】您发出的("+task_label+")派单，已接收到新的报价，请及时沟通，无异议可下单验资进入服务流程（提示：线下交易与平台无关不提供售后保障以及投诉）","111");

                                    }else {
                                        if (code!=null&&"FAIL1".equals(code)) {
                                            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                            final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                if (volleyError!=null&&volleyError.getMessage()!=null) {
                                    Log.e("uorderac,volleyError", volleyError.getMessage());
                                    Toast.makeText(getApplicationContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
                                }else {
                                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                    final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> param = new HashMap<String, String>();
                                param.put("type","01");
                                param.put("flg","01");
                                param.put("orderBody",task_label);
                                param.put("task_label",task_label);
                                param.put("createTime",createTime);
                                param.put("dynamicSeq",dynamicSeq);
                                param.put("orderProject", orderProject);
                                param.put("orderNumber", orderNumber);
                                param.put("orderAmt", orderAmt);
                                param.put("orderSum", zongjia);
                                param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                if (!"".equals(time)) {
                                    param.put("conventionTime", time);
                                }
                                Log.e("uorderdeac,pabj",param.toString());
                                return param;
                            }
                        };

                        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
                    }
                });

            }
        });
    }
    private void addPinglinCount(final String createTime, final String dynamicSeq){
        String url = FXConstant.URL_ADD_PINGLUN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("uorderdeac","评论增加成功");
                MainActivity.instance.refreshDyna();
                LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                Log.e("uorderdeac","评论增加失败"+volleyError.getMessage());
                MainActivity.instance.refreshDyna();
                LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
    }

    private void initView2() {
        etBody1.setEnabled(true);
        etBody2.setEnabled(true);
        etBody3.setEnabled(true);
        etBody4.setEnabled(true);
        etBody5.setEnabled(true);
        etCount1.setEnabled(true);
        etCount2.setEnabled(true);
        etCount3.setEnabled(true);
        etCount4.setEnabled(true);
        etCount5.setEnabled(true);
        etDanjia1.setEnabled(true);
        etDanjia2.setEnabled(true);
        etDanjia3.setEnabled(true);
        etDanjia4.setEnabled(true);
        etDanjia5.setEnabled(true);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setEnabled(true);
        btnCommit.setText("验资");
        etDanjia1.addTextChangedListener(this);
        etDanjia2.addTextChangedListener(this);
        etDanjia3.addTextChangedListener(this);
        etDanjia4.addTextChangedListener(this);
        etDanjia5.addTextChangedListener(this);
        tv_send.setOnClickListener(new View.OnClickListener() {
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

                        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                                ScreenshotUtil.getBitmapByView2(UOrderDetailActivity.this, findViewById(R.id.ll_all),false);
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
                                                message.setAttribute("flg","01");
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
                                                Toast.makeText(UOrderDetailActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(UOrderDetailActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
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
                                        params.put("flg", "01");
                                        params.put("type", typeDetail);
                                        params.put("upId", zy1);
                                        return params;
                                    }
                                };
                                MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
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
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sum = etAllPrice.getText().toString().trim();
                if (!TextUtils.isEmpty(sum)&&Double.parseDouble(sum)>0) {
                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                    final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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
                    Toast.makeText(UOrderDetailActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        Double d = yuE - orderSu;
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        if (d >= 0) {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            Display display = this.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            //设置dialog的宽高为屏幕的宽高
            ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width, height);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout,layoutParams);
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
                    pd = new ProgressDialog(UOrderDetailActivity.this);
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
                            ZhifuHelpUtils.showErrorLing(UOrderDetailActivity.this);
                        }else {
                            ZhifuHelpUtils.showErrorTishi(UOrderDetailActivity.this,times + "",null,"000");
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
                    startActivity(new Intent(UOrderDetailActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailActivity.this,R.style.Dialog).create();
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

    private void rechargefromWx(String zongjia, String uId) {
        String chongzhiId=null;
        if (hxid.length()>12) {
            chongzhiId = DemoApplication.getInstance().getCurrentQiYeId();
        }else {
            chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        }
        zongjia = (int)(Double.parseDouble(zongjia)*100)+"";
        if (Double.parseDouble(zongjia)>0) {
            final String mubiaoId = chongzhiId + "_" + "2" + "_" + uId;
            Toast.makeText(UOrderDetailActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
            String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
            final String finalBalance = zongjia;
            StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString("activity", "UOrderDetailActivity");
                        editor.commit();
                        JSONObject object = new JSONObject(s);
                        Log.e("chongzhiac,s", s);
                        String appid = "", mch_id = "", nonce_str = "", sign = "", prepayId = "", timestamp = "";
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
                    Toast.makeText(UOrderDetailActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("body", "正事多-订单支付");
                    param.put("detail", "正事多-订单支付");
                    param.put("out_trade_no", getNowTime2());
                    param.put("total_fee", finalBalance);
                    param.put("spbill_create_ip", getHostIP());
                    param.put("attach", mubiaoId);
                    return param;
                }
            };
            MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(UOrderDetailActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private String getNowTime2() {
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
                                PayTask alipay = new PayTask(UOrderDetailActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
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
                    Toast.makeText(UOrderDetailActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(UOrderDetailActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
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
                        Toast.makeText(UOrderDetailActivity.this, "下单失败！", Toast.LENGTH_SHORT).show();
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
                params.put("orderBody", orderBody);
                params.put("flg", "01");
                params.put("type", typeDetail);
                params.put("upId", zy1);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
    }
    private void bianji(final String biaoshi){
        zongjia = etAllPrice.getText().toString().trim();
        oP1 = TextUtils.isEmpty(etBody1.getText().toString().trim()) ? "" : etBody1.getText().toString().trim();
        oP2 = TextUtils.isEmpty(etBody2.getText().toString().trim()) ? "" : etBody2.getText().toString().trim();
        oP3 = TextUtils.isEmpty(etBody3.getText().toString().trim()) ? "" : etBody3.getText().toString().trim();
        oP4 = TextUtils.isEmpty(etBody4.getText().toString().trim()) ? "" : etBody4.getText().toString().trim();
        oP5 = TextUtils.isEmpty(etBody5.getText().toString().trim()) ? "" : etBody5.getText().toString().trim();
        oN1 = TextUtils.isEmpty(etCount1.getText().toString().trim()) ? "" : etCount1.getText().toString().trim();
        oN2 = TextUtils.isEmpty(etCount2.getText().toString().trim()) ? "" : etCount2.getText().toString().trim();
        oN3 = TextUtils.isEmpty(etCount3.getText().toString().trim()) ? "" : etCount3.getText().toString().trim();
        oN4 = TextUtils.isEmpty(etCount4.getText().toString().trim()) ? "" : etCount4.getText().toString().trim();
        oN5 = TextUtils.isEmpty(etCount5.getText().toString().trim()) ? "" : etCount5.getText().toString().trim();
        oA1 = TextUtils.isEmpty(etDanjia1.getText().toString().trim()) ? "" : etDanjia1.getText().toString().trim();
        oA2 = TextUtils.isEmpty(etDanjia2.getText().toString().trim()) ? "" : etDanjia2.getText().toString().trim();
        oA3 = TextUtils.isEmpty(etDanjia3.getText().toString().trim()) ? "" : etDanjia3.getText().toString().trim();
        oA4 = TextUtils.isEmpty(etDanjia4.getText().toString().trim()) ? "" : etDanjia4.getText().toString().trim();
        oA5 = TextUtils.isEmpty(etDanjia5.getText().toString().trim()) ? "" : etDanjia5.getText().toString().trim();
        orderProject = oP1 + "," + oP2 + "," + oP3 + "," + oP4 + "," + oP5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
        orderNumber = oN1 + "," + oN2 + "," + oN3 + "," + oN4 + "," + oN5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
        orderAmt = oA1 + "," + oA2 + "," + oA3 + "," + oA4 + "," + oA5 + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + "," + "" + ",";
        String url = FXConstant.URL_INSERT_OrderDetail;
        StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("数据更新成功")) {
                        if ("01".equals(biaoshi)) {
                            rechargefromZhFb(zongjia,orderId);
                        }else if ("02".equals(biaoshi)){
                            rechargefromWx(zongjia,orderId);
                        }else {

                            fukuan();
                        }
                    } else {
                        Toast.makeText(UOrderDetailActivity.this, "编辑错误！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UOrderDetailActivity.this, "验资失败！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                params1.put("orderId", orderId);
                params1.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                params1.put("orderProject", orderProject);
                params1.put("orderNumber", orderNumber);
                params1.put("orderAmt", orderAmt);
                params1.put("orderSum", zongjia);
                if (!"".equals(time)) {
                    params1.put("conventionTime", time);
                }
                return params1;
            }
        };
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request3);
    }

    private void fukuan() {
        zongjia = etAllPrice.getText().toString().trim();
        String url = FXConstant.URL_DingDan_Pay;
        StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("success")) {
                        if (pd!=null&&pd.isShowing()) {
                            pd.dismiss();
                        }
                        if (hxid.length()>12){
                            sendPushMessage(managerId,"002");
                        }else {
                            sendPushMessage(hxid,"000");
                        }
                        Toast.makeText(UOrderDetailActivity.this, "付款成功,您已完成验资！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (pd!=null&&pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(UOrderDetailActivity.this, "付款失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (pd!=null&&pd.isShowing()) {
                    pd.dismiss();
                }
                Toast.makeText(UOrderDetailActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request1);
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);
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
//                    startActivity(new Intent(UOrderDetailActivity.this, ConsumeActivity.class).putExtra("biaoshi", "00"));
//                }else {
//                    startActivity(new Intent(UOrderDetailActivity.this, ConsumeActivity.class).putExtra("biaoshi", "01"));
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
//                if ("002".equals(type)) {
//                    startActivity(new Intent(UOrderDetailActivity.this, ConsumeActivity.class).putExtra("biaoshi", "00"));
//                }else {
//                    startActivity(new Intent(UOrderDetailActivity.this, ConsumeActivity.class).putExtra("biaoshi", "01"));
//                }
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
                MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.countDown:
                startActivityForResult(new Intent(UOrderDetailActivity.this,YueDingTimeActivity.class),1);
                break;
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
        MySingleton.getInstance(UOrderDetailActivity.this).addToRequestQueue(request);

    }

}
