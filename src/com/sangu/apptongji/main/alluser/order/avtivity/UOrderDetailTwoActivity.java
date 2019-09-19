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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.address.AddressListTwoActivity;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.fragment.MainActivity;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.CashierInputFilter;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.utils.ToastUtils;
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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-11-23.
 */

public class UOrderDetailTwoActivity extends BaseActivity implements IUAZView,IQiYeDetailView,IPriceView,View.OnClickListener{
    private static final int SDK_PAY_FLAG = 1;
    private TextView tv_paidan_qiye_time;
    private TextView tv_paidan_qiyeren_time;
    private TextView tv_paidan_caiwu_time;
    private TextView tv_paidan_kehu_time;
    private ImageView iv_jiantou;
    private LinearLayout ll2;
    private LinearLayout ll_xiadan,ll_all;
    private LinearLayout ll1_paidan;
    private LinearLayout ll2_paidan;
    private RelativeLayout rl_sex;
    private RelativeLayout rl_qiye;
    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_titl;
    private TextView tv_nianling;
    private TextView tv_company_count;
    private TextView tv_company;
    private TextView tv_distance;
    private TextView tv_project_one;
    private TextView tv_project_two;
    private TextView tv_project_three;
    private TextView tv_project_four;
    private TextView tv_zy1_bao,tv_yanzi,tv_send;
    private TextView tv_zy2_bao;
    private TextView tv_zy3_bao;
    private TextView tv_zy4_bao;
    private TextView tv_qianming;
    private TextView tv_saomiao;
    private EditText et_your_project;
    private EditText et_qianshou_dizhi;
    private EditText et_dingdan_bianhao;
    private EditText et_zhifu_jine;
    private CheckBox cb_fapiao;
    private TextView iv_zy1_tupian;
    private TextView iv_zy2_tupian;
    private TextView iv_zy3_tupian;
    private TextView iv_zy4_tupian;
    private ImageView iv_sex;
    private ImageView iv_xiaofei;
    private ImageView iv_shanghu;
    private Button btn_yanzi;
    private TextView tev_paidan_qiye=null;
    private TextView tev_paidan_qiyeren=null;
    private TextView tev_paidan_caiwu=null;
    private TextView tev_paidan_kehu=null;
    LinearLayout ll_one,ll_two,ll_three,ll_four;
    private IPricePresenter pricePresenter;
    private IUAZPresenter presenter=null;
    private IQiYeInfoPresenter iQiYeInfoPresenter=null;
    private String hxid,companyName=null,companyAdress=null,managerId=null,wodezhanghao,orderBody;
    private String pass,zy1,orderId,merId,zongjia,orderProject,orderNumber,orderAmt,time="",time4,typeDetail,biaoshi,task_label,createTime="",dynamicSeq="";
    private Double yuE;
    private int errorTime=3;
    private ProgressDialog pd=null;
    private IWXAPI api=null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
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
                        Toast.makeText(UOrderDetailTwoActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
                        zongjia = et_zhifu_jine.getText().toString().trim();
                        final double orderSu = Double.valueOf(zongjia);
                        yuE = yuE - orderSu;
                        DemoApplication.getApp().saveCurrentPrice(yuE);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(UOrderDetailTwoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

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
            if ("04".equals(biaoshi)){
                ChangeState();
                updateUscount(hxid);
            }
            if (hxid.length()>12){
                sendPushMessage(managerId,"002");
            }else {
                sendPushMessage(hxid,"000");
            }
            Toast.makeText(UOrderDetailTwoActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
            zongjia = et_zhifu_jine.getText().toString().trim();
            final double orderSu = Double.valueOf(zongjia);
            yuE = yuE - orderSu;
            DemoApplication.getApp().saveCurrentPrice(yuE);
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_xiadan_moshier);
        pricePresenter = new PricePresenter(this,this);
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_qiye = (RelativeLayout) findViewById(R.id.rl_qiye);
        tv_project_one = (TextView) findViewById(R.id.tv_project_one);
        tv_project_two = (TextView) findViewById(R.id.tv_project_two);
        tv_project_three = (TextView) findViewById(R.id.tv_project_three);
        tv_project_four = (TextView) findViewById(R.id.tv_project_four);
        tv_yanzi = (TextView) findViewById(R.id.tv_yanzi);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_zy1_bao = (TextView) findViewById(R.id.tv_zy1_bao);
        tv_zy2_bao = (TextView) findViewById(R.id.tv_zy2_bao);
        tv_zy3_bao = (TextView) findViewById(R.id.tv_zy3_bao);
        tv_zy4_bao = (TextView) findViewById(R.id.tv_zy4_bao);
        tv_qianming = (TextView) findViewById(R.id.tv_qianming);
        tv_saomiao = (TextView) findViewById(R.id.tv_saomiao);
        iv_zy1_tupian = (TextView) findViewById(R.id.iv_zy1_tupian);
        iv_zy2_tupian = (TextView) findViewById(R.id.iv_zy2_tupian);
        iv_zy3_tupian = (TextView) findViewById(R.id.iv_zy3_tupian);
        iv_zy4_tupian = (TextView) findViewById(R.id.iv_zy4_tupian);
        et_your_project = (EditText) findViewById(R.id.et_your_project);
        et_qianshou_dizhi = (EditText) findViewById(R.id.et_qianshou_dizhi);
        et_dingdan_bianhao = (EditText) findViewById(R.id.et_dingdan_bianhao);
        cb_fapiao = (CheckBox) findViewById(R.id.cb_fapiao);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_company_count = (TextView) findViewById(R.id.tv_company_count);
        tv_nianling = (TextView) findViewById(R.id.tv_nianling);
        tv_titl = (TextView) findViewById(R.id.tv_titl);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_paidan_qiye_time = (TextView) findViewById(R.id.tv_paidan_qiye_time);
        tv_paidan_qiyeren_time = (TextView) findViewById(R.id.tv_paidan_qiyeren_time);
        tv_paidan_caiwu_time = (TextView) findViewById(R.id.tv_paidan_caiwu_time);
        tv_paidan_kehu_time = (TextView) findViewById(R.id.tv_paidan_kehu_time);
        btn_yanzi = (Button) findViewById(R.id.btn_yanzi);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        iv_jiantou = (ImageView) findViewById(R.id.iv_jiantou);
        iv_xiaofei = (ImageView) findViewById(R.id.iv_xiaofei);
        iv_shanghu = (ImageView) findViewById(R.id.iv_shanghu);
        cb_fapiao = (CheckBox) findViewById(R.id.cb_fapiao);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll_all = (LinearLayout) findViewById(R.id.ll_all);
        ll_xiadan = (LinearLayout) findViewById(R.id.ll_xiadan);
        ll1_paidan = (LinearLayout) findViewById(R.id.ll1_paidan);
        ll2_paidan = (LinearLayout) findViewById(R.id.ll2_paidan);
        et_zhifu_jine = (EditText) findViewById(R.id.et_zhifu_jine);
        ll_all.setFocusable(true);
        ll_all.setFocusableInTouchMode(true);
        ll_all.requestFocus();
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        tev_paidan_qiye = (TextView) findViewById(R.id.tev_paidan_qiye);
        tev_paidan_qiyeren = (TextView) findViewById(R.id.tev_paidan_qiyeren);
        tev_paidan_caiwu = (TextView) findViewById(R.id.tev_paidan_caiwu);
        tev_paidan_kehu = (TextView) findViewById(R.id.tev_paidan_kehu);
        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        ll_two = (LinearLayout) findViewById(R.id.ll_two);
        ll_three = (LinearLayout) findViewById(R.id.ll_three);
        ll_four = (LinearLayout) findViewById(R.id.ll_four);
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("正在发送请求...");
        presenter = new UAZPresenter(this,this);
        iQiYeInfoPresenter = new QiYeInfoPresenter(this,this);
        tev_paidan_qiye.setVisibility(View.INVISIBLE);
        tev_paidan_qiyeren.setVisibility(View.INVISIBLE);
        tev_paidan_caiwu.setVisibility(View.INVISIBLE);
        tev_paidan_kehu.setVisibility(View.INVISIBLE);
        tv_paidan_qiye_time.setVisibility(View.INVISIBLE);
        tv_paidan_qiyeren_time.setVisibility(View.INVISIBLE);
        tv_paidan_caiwu_time.setVisibility(View.INVISIBLE);
        tv_paidan_kehu_time.setVisibility(View.INVISIBLE);
        iv_jiantou.setVisibility(View.INVISIBLE);
        wodezhanghao = DemoHelper.getInstance().getCurrentUsernName();
        companyName = this.getIntent().hasExtra("companyName")?this.getIntent().getStringExtra("companyName"):"0";
        companyAdress = this.getIntent().hasExtra("companyAdress")?this.getIntent().getStringExtra("companyAdress"):"0";
        managerId = this.getIntent().getStringExtra("managerId");
        zy1 = this.getIntent().getStringExtra("zy1");
        hxid = this.getIntent().getStringExtra("hxid");
        typeDetail = this.getIntent().getStringExtra("typeDetail");
        orderBody = this.getIntent().getStringExtra("orderBody");
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        task_label = this.getIntent().hasExtra("task_label")?this.getIntent().getStringExtra("task_label"):"";
        createTime = this.getIntent().hasExtra("createTime")?this.getIntent().getStringExtra("createTime"):"";
        dynamicSeq = this.getIntent().hasExtra("dynamicSeq")?this.getIntent().getStringExtra("dynamicSeq"):"";
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
        if (biaoshi.equals("01")){
            String balance = this.getIntent().getStringExtra("balance");
            et_zhifu_jine.setText(balance);
            et_zhifu_jine.setEnabled(false);
        }
        if ("00".equals(biaoshi)){
            tv_send.setVisibility(View.VISIBLE);
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

                            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                                    ScreenshotUtil.getBitmapByView2(UOrderDetailTwoActivity.this, findViewById(R.id.ll_all),false);
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
                                                    message.setAttribute("flg","02");
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
                                                    Toast.makeText(UOrderDetailTwoActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(UOrderDetailTwoActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
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
                                            params.put("flg", "02");
                                            params.put("type", typeDetail);
                                            params.put("upId", zy1);
                                            return params;
                                        }
                                    };
                                    MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
            tv_yanzi.setText("报价");
            initView3();
        }else if ("04".equals(biaoshi)) {
            tv_yanzi.setText("报价");
            initView4();
        }else if ("05".equals(biaoshi)){
            tv_send.setVisibility(View.VISIBLE);
            initView5();
        }else if ("06".equals(biaoshi)){
            initView6();
        }else if ("07".equals(biaoshi)){
            tv_yanzi.setText("报价");
            initView7();
        }else if ("08".equals(biaoshi)){
            initView8();
        }else{
            tv_saomiao.setOnClickListener(this);
            btn_yanzi.setOnClickListener(this);
        }
        et_zhifu_jine.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters={new CashierInputFilter()};
        et_zhifu_jine.setFilters(filters);
        if (hxid.length()>12){
            rl_sex.setVisibility(View.INVISIBLE);
            rl_qiye.setVisibility(View.INVISIBLE);
            final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv_name.getLayoutParams();
            lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            lp.height= RelativeLayout.LayoutParams.WRAP_CONTENT;
            tv_name.setLayoutParams(lp);
            iQiYeInfoPresenter.loadQiYeInfo(hxid);
        }else {
            presenter.loadThisDetail(hxid);
        }
    }

    private void initView8() {
        et_zhifu_jine.setEnabled(true);
        et_dingdan_bianhao.setEnabled(true);
        et_qianshou_dizhi.setEnabled(true);
        et_your_project.setEnabled(true);
        cb_fapiao.setEnabled(true);
        btn_yanzi.setText("发  送");
        btn_yanzi.setWidth(200);
        tv_send.setVisibility(View.VISIBLE);
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtoself();
            }
        });
        btn_yanzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendtoself();
            }
        });
    }

    private void sendtoself() {
        if (cb_fapiao.isChecked()){
            time4 = "01";
        }else {
            time4 = "00";
        }
        zongjia = et_zhifu_jine.getText().toString().trim();
        orderProject = et_your_project.getText().toString().trim();
        orderNumber = et_qianshou_dizhi.getText().toString().trim();
        orderAmt = et_dingdan_bianhao.getText().toString().trim();
        if (zongjia==null||"".equals(zongjia)||Double.parseDouble(zongjia)<=0){
            Toast.makeText(getApplicationContext(),"请输入总价！",Toast.LENGTH_SHORT).show();
            return;
        }
        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_order_send, null);
        final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                if (ContextCompat.checkSelfPermission(UOrderDetailTwoActivity.this, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UOrderDetailTwoActivity.this,new String[]{Manifest.permission.READ_CONTACTS},1);
                }else {
                    Intent intent = new Intent(UOrderDetailTwoActivity.this,AddressListTwoActivity.class);
                    intent.putExtra("biaoshi","05");
                    intent.putExtra("orderBody",orderBody);
                    intent.putExtra("flg","02");
                    intent.putExtra("upId",zy1);
                    intent.putExtra("time4",time4);
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
                Intent intent3 = new Intent(UOrderDetailTwoActivity.this,FriendActivity.class);
                intent3.putExtra("biaoshi","05");
                intent3.putExtra("orderBody",orderBody);
                intent3.putExtra("flg","02");
                intent3.putExtra("upId",zy1);
                intent3.putExtra("time4",time4);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case 0:
                    finish();
                    break;
            }
        }
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
                    if (code.equals("数据更新成功")) {
                        bianji2(userId,biaoshi);
                    } else {
                        Toast.makeText(UOrderDetailTwoActivity.this, "下单失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UOrderDetailTwoActivity.this, "网络连接错误，下单失败！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId",userId);
                params.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("orderBody",orderBody);
                params.put("flg", "02");
                params.put("type", "01");
                params.put("upId", zy1);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
                        Toast.makeText(UOrderDetailTwoActivity.this, "编辑错误！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UOrderDetailTwoActivity.this, "验资失败！", Toast.LENGTH_SHORT).show();
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
                params1.put("time4", time4);
                params1.put("orderAmt", orderAmt);
                params1.put("orderSum", zongjia);
                if (!time.equals("")) {
                    params1.put("conventionTime", time);
                }
                return params1;
            }
        };
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request3);
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
                MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);

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
                MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }
    private void initView7() {
        et_zhifu_jine.setEnabled(true);
        et_dingdan_bianhao.setEnabled(true);
        et_qianshou_dizhi.setEnabled(true);
        et_your_project.setEnabled(true);
        cb_fapiao.setEnabled(true);
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
        String timec = TextUtils.isEmpty(orderDetail.getTime4())?"00":orderDetail.getTime4();
        orderProject = TextUtils.isEmpty(orderDetail.getOrderProject())?"":orderDetail.getOrderProject();
        orderNumber = TextUtils.isEmpty(orderDetail.getOrderNumber())?"":orderDetail.getOrderNumber();
        orderAmt = TextUtils.isEmpty(orderDetail.getOrderAmt())?"":orderDetail.getOrderAmt();
        cb_fapiao.setEnabled(false);
        if (timec.equals("01")){
            cb_fapiao.setChecked(true);
        }else {
            cb_fapiao.setChecked(false);
        }
        et_zhifu_jine.setText(zongjia);
        et_your_project.setText(orderProject);
        et_qianshou_dizhi.setText(orderNumber);
//        et_dingdan_bianhao.setText(orderAmt);
        btn_yanzi.setText("修改报价");
        btn_yanzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_fapiao.isChecked()){
                    time4 = "01";
                }else {
                    time4 = "00";
                }
                zongjia = et_zhifu_jine.getText().toString().trim();
                orderProject = et_your_project.getText().toString().trim();
                orderNumber = et_qianshou_dizhi.getText().toString().trim();
                if (TextUtils.isEmpty(zongjia)||Double.parseDouble(zongjia)==0){
                    Toast.makeText(getApplicationContext(),"请输入报价！",Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailTwoActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                                Log.e("uorderdeact,xiugai",s);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.e("uorderdeact,xiugaie",volleyError.toString());
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
                                param.put("quote", zongjia);
                                param.put("time4", time4);
                                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                                return param;
                            }
                        };
                        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
        et_your_project.setEnabled(false);
        et_zhifu_jine.setEnabled(false);
        et_qianshou_dizhi.setEnabled(false);
        et_dingdan_bianhao.setEnabled(false);
        queryOrder();
    }

    private void zhifu(final String sum) {
        if (pass==null||"".equals(pass)){
            showErrorMiMaSHZH();
            return;
        }
        if (pass.length()!=6||!isNumeric(pass)){
            showErrorMiMaXG();
            return;
        }
        if (errorTime<=0){
            showErrorLing();
            return;
        }
        final double orderSu = Double.valueOf(sum);
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")){
            Double d = yuE - orderSu;
        if (d >= 0) {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                    pd = new ProgressDialog(UOrderDetailTwoActivity.this);
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
                            showErrorLing();
                        }else {
                            showErrorTishi(times + "");
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
                    startActivity(new Intent(UOrderDetailTwoActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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

    private void showErrorLing() {
        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
        final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
        dialog2.show();
        dialog2.getWindow().setContentView(layout);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
        Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
        tv_title.setText("支付密码错误次数达到上限,请次日重试或联系客服");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
    }

    private void showErrorTishi(String s) {
        LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailTwoActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("温馨提示");
        btnOK1.setText("忘记密码");
        btnCancel1.setText("重新输入");
        title_tv1.setText("支付密码错误，您还可以输入"+s+"次");
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
                String bs;
                bs = "000";
                startActivity(new Intent(UOrderDetailTwoActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
            }
        });
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if(!isNum.matches() ){
            return false;
        }
        return true;
    }

    private void showErrorMiMaXG() {
        LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailTwoActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("温馨提示");
        btnOK1.setText("前去设置");
        btnCancel1.setText("以后再说");
        title_tv1.setText("支付系统更新,请先修改您的支付密码为6为纯数字组合");
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
                String bs;
                bs = "000";
                startActivity(new Intent(UOrderDetailTwoActivity.this, XiuGaiZFActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",pass));
            }
        });
    }

    private void showErrorMiMaSHZH() {
        LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailTwoActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("温馨提示");
        btnOK1.setText("前去设置");
        btnCancel1.setText("以后再说");
        title_tv1.setText("您还没有设置支付密码!");
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
                String bs;
                bs = "000";
                startActivity(new Intent(UOrderDetailTwoActivity.this,ZhiFuSettingActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",pass));
            }
        });
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request3);
    }

    private void zhifu2(final String sum) {
        if (pass==null||"".equals(pass)){
            showErrorMiMaSHZH();
            return;
        }
        if (pass.length()!=6||!isNumeric(pass)){
            showErrorMiMaXG();
            return;
        }
        if (errorTime<=0){
            showErrorLing();
            return;
        }
        final double orderSu = Double.valueOf(sum);
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        Double d = yuE - orderSu;
        if (d >= 0) {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                    pd = new ProgressDialog(UOrderDetailTwoActivity.this);
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
                            showErrorLing();
                        }else {
                            showErrorTishi(times + "");
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
                    startActivity(new Intent(UOrderDetailTwoActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
            showErrorMiMaSHZH();
            return;
        }
        if (pass.length()!=6||!isNumeric(pass)){
            showErrorMiMaXG();
            return;
        }
        if (errorTime<=0){
            showErrorLing();
            return;
        }
        final double orderSu = Double.valueOf(sum);
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        Double d = yuE - orderSu;
        if (d >= 0) {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                    pd = new ProgressDialog(UOrderDetailTwoActivity.this);
                    pd.setMessage("正在支付...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    if (pwdView.getStrPassword().equals(pass)) {
                        dialog.dismiss();
                        pd.dismiss();
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
                            showErrorLing();
                        }else {
                            showErrorTishi(times + "");
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
                    startActivity(new Intent(UOrderDetailTwoActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                    orderProject = TextUtils.isEmpty(orderDetail.getOrderProject())?"":orderDetail.getOrderProject();
                    orderNumber = TextUtils.isEmpty(orderDetail.getOrderNumber())?"":orderDetail.getOrderNumber();
                    orderAmt = TextUtils.isEmpty(orderDetail.getOrderAmt())?"":orderDetail.getOrderAmt();
                    zongjia = orderDetail.getOrderSum();
                    String timec = TextUtils.isEmpty(orderDetail.getTime4())?"00":orderDetail.getTime4();
                    orderProject = TextUtils.isEmpty(orderDetail.getOrderProject())?"":orderDetail.getOrderProject();
                    orderNumber = TextUtils.isEmpty(orderDetail.getOrderNumber())?"":orderDetail.getOrderNumber();
                    orderAmt = TextUtils.isEmpty(orderDetail.getOrderAmt())?"":orderDetail.getOrderAmt();
                    cb_fapiao.setEnabled(false);
                    if (timec.equals("01")){
                        cb_fapiao.setChecked(true);
                    }else {
                        cb_fapiao.setChecked(false);
                    }
                    et_zhifu_jine.setText(zongjia);
                    et_your_project.setText(orderProject);
                    et_qianshou_dizhi.setText(orderNumber);
                    et_zhifu_jine.setEnabled(false);
                    if (wodezhanghao.equals(userId)) {
                        btn_yanzi.setEnabled(true);
                        btn_yanzi.setText("验  资");
                        btn_yanzi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String sum = et_zhifu_jine.getText().toString().trim();
                                if (!TextUtils.isEmpty(sum)&&Double.parseDouble(sum)>0) {
                                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
                                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                                    final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                                    Toast.makeText(UOrderDetailTwoActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else {
                        et_zhifu_jine.setEnabled(false);
                        btn_yanzi.setEnabled(false);
                        btn_yanzi.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                        btn_yanzi.setText("等待对方验资");
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
    }

    private void initView5() {
        final String userId = getIntent().getStringExtra("userId");
        merId = getIntent().getStringExtra("merId");
        orderId = getIntent().getStringExtra("orderId");
        hxid = merId;
        if (wodezhanghao.equals(merId)) {
            et_dingdan_bianhao.setEnabled(true);
            et_zhifu_jine.setEnabled(true);
            et_qianshou_dizhi.setEnabled(true);
            et_your_project.setEnabled(true);
            btn_yanzi.setEnabled(true);
            btn_yanzi.setText("发  送");
        }else {
            et_dingdan_bianhao.setEnabled(false);
            et_zhifu_jine.setEnabled(false);
            et_qianshou_dizhi.setEnabled(false);
            et_your_project.setEnabled(false);
            tv_send.setVisibility(View.INVISIBLE);
            btn_yanzi.setEnabled(false);
            btn_yanzi.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            btn_yanzi.setText("等待对方编辑");
        }
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

                        zongjia = et_zhifu_jine.getText().toString().trim();
                        if (TextUtils.isEmpty(zongjia)||Double.parseDouble(zongjia)==0){
                            Toast.makeText(UOrderDetailTwoActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                                ScreenshotUtil.getBitmapByView2(UOrderDetailTwoActivity.this, findViewById(R.id.ll_all),false);
                                if (cb_fapiao.isChecked()){
                                    time4 = "01";
                                }else {
                                    time4 = "00";
                                }
                                orderProject = et_your_project.getText().toString().trim();
                                orderNumber = et_qianshou_dizhi.getText().toString().trim();
                                orderAmt = et_dingdan_bianhao.getText().toString().trim();
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
                                                message.setAttribute("flg","02");
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
                                                Toast.makeText(UOrderDetailTwoActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(UOrderDetailTwoActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(UOrderDetailTwoActivity.this, "验资失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params1 = new HashMap<>();
                                        params1.put("orderId", orderId);
                                        params1.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                        params1.put("orderProject", orderProject);
                                        params1.put("orderNumber", orderNumber);
                                        params1.put("time4", time4);
                                        params1.put("orderAmt", orderAmt);
                                        params1.put("orderSum", zongjia);
                                        if (!time.equals("")) {
                                            params1.put("conventionTime", time);
                                        }
                                        return params1;
                                    }
                                };
                                MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request3);
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
        btn_yanzi.setOnClickListener(new View.OnClickListener() {
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

                        zongjia = et_zhifu_jine.getText().toString().trim();
                        if (TextUtils.isEmpty(zongjia)||Double.parseDouble(zongjia)==0){
                            Toast.makeText(UOrderDetailTwoActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                                ScreenshotUtil.getBitmapByView2(UOrderDetailTwoActivity.this, findViewById(R.id.ll_all),false);
                                if (cb_fapiao.isChecked()){
                                    time4 = "01";
                                }else {
                                    time4 = "00";
                                }
                                orderProject = et_your_project.getText().toString().trim();
                                orderNumber = et_qianshou_dizhi.getText().toString().trim();
                                orderAmt = et_dingdan_bianhao.getText().toString().trim();
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
                                                message.setAttribute("flg","02");
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
                                                Toast.makeText(UOrderDetailTwoActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(UOrderDetailTwoActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        Toast.makeText(UOrderDetailTwoActivity.this, "验资失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params1 = new HashMap<>();
                                        params1.put("orderId", orderId);
                                        params1.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                        params1.put("orderProject", orderProject);
                                        params1.put("orderNumber", orderNumber);
                                        params1.put("time4", time4);
                                        params1.put("orderAmt", orderAmt);
                                        params1.put("orderSum", zongjia);
                                        if (!time.equals("")) {
                                            params1.put("conventionTime", time);
                                        }
                                        return params1;
                                    }
                                };
                                MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request3);
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
        et_zhifu_jine.setEnabled(false);
        et_dingdan_bianhao.setEnabled(false);
        et_qianshou_dizhi.setEnabled(false);
        et_your_project.setEnabled(false);
        cb_fapiao.setEnabled(false);
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
        String timec = TextUtils.isEmpty(orderDetail.getTime4())?"00":orderDetail.getTime4();
        orderProject = TextUtils.isEmpty(orderDetail.getOrderProject())?"":orderDetail.getOrderProject();
        orderNumber = TextUtils.isEmpty(orderDetail.getOrderNumber())?"":orderDetail.getOrderNumber();
        orderAmt = TextUtils.isEmpty(orderDetail.getOrderAmt())?"":orderDetail.getOrderAmt();
        cb_fapiao.setEnabled(false);
        if (timec.equals("01")){
            cb_fapiao.setChecked(true);
        }else {
            cb_fapiao.setChecked(false);
        }
        et_zhifu_jine.setText(zongjia);
        et_your_project.setText(orderProject);
        et_qianshou_dizhi.setText(orderNumber);
//        et_dingdan_bianhao.setText(orderAmt);
        btn_yanzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sum = et_zhifu_jine.getText().toString().trim();
                if (!TextUtils.isEmpty(sum)&&Double.parseDouble(sum)>0) {
                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                    final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                    Toast.makeText(UOrderDetailTwoActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
    }

    private void initView3() {
        btn_yanzi.setEnabled(true);
        btn_yanzi.setText("接单报价");
        btn_yanzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cb_fapiao.isChecked()){
                    time4 = "01";
                }else {
                    time4 = "00";
                }
                zongjia = et_zhifu_jine.getText().toString().trim();
                orderProject = et_your_project.getText().toString().trim();
                orderNumber = et_qianshou_dizhi.getText().toString().trim();
                orderAmt = et_dingdan_bianhao.getText().toString().trim();
                if (zongjia==null||"".equals(zongjia)||Double.parseDouble(zongjia)<=0){
                    Toast.makeText(getApplicationContext(),"请输入报价！",Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailTwoActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                                            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
                                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                            final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
                                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                    final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                                param.put("flg","02");
                                param.put("orderBody",task_label);
                                param.put("task_label",task_label);
                                param.put("createTime",createTime);
                                param.put("dynamicSeq",dynamicSeq);
                                param.put("orderProject", orderProject);
                                param.put("orderNumber", orderNumber);
                                param.put("orderSum", zongjia);
                                param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                param.put("time4", time4);
                                if (time!=null&&!"".equals(time)) {
                                    param.put("conventionTime", time);
                                }
                                return param;
                            }
                        };
                        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
                Log.e("uorderdetac","评论增加成功");
                MainActivity.instance.refreshDyna();
                LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        yuE = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_yanzi:
                final String sum = et_zhifu_jine.getText().toString().trim();
                if (!TextUtils.isEmpty(sum)&&Double.parseDouble(sum)>0) {
                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailTwoActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                    final Dialog dialog = new AlertDialog.Builder(UOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                    Toast.makeText(UOrderDetailTwoActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
                    if (code.equals("数据更新成功")) {
                        if (hxid.length()>12){
                            updateQjcount();
                        }else {
                            updateUscount(hxid);
                        }
                        bianji(biaoshi);
                    } else {
                        Toast.makeText(UOrderDetailTwoActivity.this, "下单失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UOrderDetailTwoActivity.this, "网络连接错误，下单失败！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("merId", hxid);
                params.put("orderBody",orderBody);
                params.put("flg", "02");
                params.put("type", typeDetail);
                params.put("upId", zy1);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
    }
    private void bianji(final String biaoshi){
        if (cb_fapiao.isChecked()){
            time4 = "01";
        }else {
            time4 = "00";
        }
        zongjia = et_zhifu_jine.getText().toString().trim();
        orderProject = et_your_project.getText().toString().trim();
        orderNumber = et_qianshou_dizhi.getText().toString().trim();
        orderAmt = et_dingdan_bianhao.getText().toString().trim();
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
                        Toast.makeText(UOrderDetailTwoActivity.this, "编辑错误！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UOrderDetailTwoActivity.this, "验资失败！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                params1.put("orderId", orderId);
                params1.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                params1.put("orderProject", orderProject);
                params1.put("orderNumber", orderNumber);
                params1.put("time4", time4);
                params1.put("orderAmt", orderAmt);
                params1.put("orderSum", zongjia);
                if (!time.equals("")) {
                    params1.put("conventionTime", time);
                }
                return params1;
            }
        };
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request3);
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
        Toast.makeText(UOrderDetailTwoActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = zongjia;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity","UOrderDetailTwoActivity");
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
                Toast.makeText(UOrderDetailTwoActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
                                PayTask alipay = new PayTask(UOrderDetailTwoActivity.this);
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
                    Toast.makeText(UOrderDetailTwoActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(UOrderDetailTwoActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private void fukuan() {
        zongjia = et_zhifu_jine.getText().toString().trim();
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
                        Toast.makeText(UOrderDetailTwoActivity.this, "付款成功,您已完成验资！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (pd!=null&&pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(UOrderDetailTwoActivity.this, "付款失败", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UOrderDetailTwoActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request1);
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);
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
//                    startActivity(new Intent(UOrderDetailTwoActivity.this, ConsumeActivity.class).putExtra("biaoshi", "00"));
//                }else {
//                    startActivity(new Intent(UOrderDetailTwoActivity.this, ConsumeActivity.class).putExtra("biaoshi", "01"));
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
                    startActivity(new Intent(UOrderDetailTwoActivity.this, ConsumeActivity.class).putExtra("biaoshi", "00"));
                }else {
                    startActivity(new Intent(UOrderDetailTwoActivity.this, ConsumeActivity.class).putExtra("biaoshi", "01"));
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
                MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    @Override
    public void updateQiyeInfo(QiYeInfo allUser) throws UnsupportedEncodingException {
        String conName = allUser.getCompanyName();
        String sign = allUser.getComSignature();
        if (sign==null||"".equals(sign)){
            sign = "未设置简介";
        }
        try {
            sign = URLDecoder.decode(sign, "UTF-8");
            conName = URLDecoder.decode(conName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        tv_name.setText(conName);
        tv_nianling.setVisibility(View.INVISIBLE);
        tv_project_one.setText(allUser.getUpName1());
        tv_project_two.setText(allUser.getUpName2());
        tv_project_three.setText(allUser.getUpName3());
        tv_project_four.setText(allUser.getUpName4());
        String image1 = allUser.getZyImage1();
        String image2 = allUser.getZyImage2();
        String image3 = allUser.getZyImage3();
        String image4 = allUser.getZyImage4();
        String margan1 = allUser.getMargin1();
        String margan2 = allUser.getMargin2();
        String margan3 = allUser.getMargin3();
        String margan4 = allUser.getMargin4();
        if (allUser.getUpName1()==null||allUser.getUpName1().equals("")){
            ll_one.setVisibility(View.GONE);
        }else {
            ll_one.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName2()==null||allUser.getUpName2().equals("")){
            ll_two.setVisibility(View.GONE);
        }else {
            ll_two.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName3()==null||allUser.getUpName3().equals("")){
            ll_three.setVisibility(View.GONE);
        }else {
            ll_three.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName4()==null||allUser.getUpName4().equals("")){
            ll_four.setVisibility(View.GONE);
        }else {
            ll_four.setVisibility(View.VISIBLE);
        }
        if (image1!=null&&!"".equals(image1)) {
            iv_zy1_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy1_tupian.setVisibility(View.GONE);
        }
        if (margan1!=null) {
            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                tv_zy1_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy1_bao.setVisibility(View.GONE);
            }
        }
        if (image2!=null&&!"".equals(image2)) {
            iv_zy2_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy2_tupian.setVisibility(View.GONE);
        }
        if (margan2!=null) {
            if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                tv_zy2_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy2_bao.setVisibility(View.GONE);
            }
        }
        if (image3!=null&&!"".equals(image3)) {
            iv_zy3_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy3_tupian.setVisibility(View.GONE);
        }
        if (margan3!=null) {
            if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                tv_zy3_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy3_bao.setVisibility(View.GONE);
            }
        }
        if (image4!=null&&!"".equals(image4)) {
            iv_zy4_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy4_tupian.setVisibility(View.GONE);
        }
        if (margan4!=null) {
            if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                tv_zy4_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy4_bao.setVisibility(View.GONE);
            }
        }
        String head = TextUtils.isEmpty(allUser.getComImage()) ? "" : allUser.getComImage();
        if (head.length() > 40) {
            tv_titl.setVisibility(View.INVISIBLE);
            iv_head.setVisibility(View.VISIBLE);
            String[] orderProjectArray = head.split("\\|");
            head = orderProjectArray[0];
        }
        if (!(head.equals("") || head.equals(null))) {
            tv_titl.setVisibility(View.INVISIBLE);
            iv_head.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG+head,iv_head, DemoApplication.mOptions);
        } else {
            iv_head.setVisibility(View.INVISIBLE);
            tv_titl.setVisibility(View.VISIBLE);
            tv_titl.setText("企");
        }
        String resv3 = TextUtils.isEmpty(allUser.getResv3()) ? "" : allUser.getResv3();
        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
        String resv2 = TextUtils.isEmpty(allUser.getResv2()) ? "" : allUser.getResv2();
        String lat = DemoApplication.getInstance().getCurrentLat();
        String lng = DemoApplication.getInstance().getCurrentLng();
        if (lat!=null&&lng!=null) {
            if (!("".equals(lat) || "".equals(lng) || resv1.equals("") || resv2.equals(""))) {
                double latitude1 = Double.valueOf(lat);
                double longitude1 = Double.valueOf(lng);
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
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
        }else {
            tv_distance.setText("3km以外");
        }
        tv_qianming.setText(sign);
        iv_sex.setVisibility(View.INVISIBLE);
    }
    @Override
    public void updateThisUser(Userful allUser) {
        tv_name.setText(TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName());
        String nianLing = TextUtils.isEmpty(allUser.getuAge()) ? "27" : allUser.getuAge();
        tv_nianling.setText(nianLing);
        String company = TextUtils.isEmpty(allUser.getCompany()) ? "暂未加入企业" : allUser.getCompany();
        String uNation = allUser.getuNation();
        String resv5 = allUser.getResv5();
        String resv6 = allUser.getResv6();
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
        String member = allUser.getMenberNum();
        if (member==null||"".equals(member)){
            member = "0";
        }
        if (!company.equals("暂未加入企业")){
            tv_company_count.setVisibility(View.VISIBLE);
        }else {
            tv_company_count.setVisibility(View.INVISIBLE);
        }
        tv_company_count.setText("("+member+"人"+")");
        tv_company.setText(company);
        tv_project_one.setText(allUser.getUpName1());
        tv_project_two.setText(allUser.getUpName2());
        tv_project_three.setText(allUser.getUpName3());
        tv_project_four.setText(allUser.getUpName4());
        String image1 = allUser.getZyImage1();
        String image2 = allUser.getZyImage2();
        String image3 = allUser.getZyImage3();
        String image4 = allUser.getZyImage4();
        String margan1 = allUser.getMargin1();
        String margan2 = allUser.getMargin2();
        String margan3 = allUser.getMargin3();
        String margan4 = allUser.getMargin4();
        if (allUser.getUpName1()==null||allUser.getUpName1().equals("")){
            ll_one.setVisibility(View.GONE);
        }else {
            ll_one.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName2()==null||allUser.getUpName2().equals("")){
            ll_two.setVisibility(View.GONE);
        }else {
            ll_two.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName3()==null||allUser.getUpName3().equals("")){
            ll_three.setVisibility(View.GONE);
        }else {
            ll_three.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName4()==null||allUser.getUpName4().equals("")){
            ll_four.setVisibility(View.GONE);
        }else {
            ll_four.setVisibility(View.VISIBLE);
        }
        if (image1!=null&&!"".equals(image1)) {
            iv_zy1_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy1_tupian.setVisibility(View.GONE);
        }
        if (margan1!=null) {
            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                tv_zy1_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy1_bao.setVisibility(View.GONE);
            }
        }
        if (image2!=null&&!"".equals(image2)) {
            iv_zy2_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy2_tupian.setVisibility(View.GONE);
        }
        if (margan2!=null) {
            if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                tv_zy2_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy2_bao.setVisibility(View.GONE);
            }
        }
        if (image3!=null&&!"".equals(image3)) {
            iv_zy3_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy3_tupian.setVisibility(View.GONE);
        }
        if (margan3!=null) {
            if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                tv_zy3_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy3_bao.setVisibility(View.GONE);
            }
        }
        if (image4!=null&&!"".equals(image4)) {
            iv_zy4_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy4_tupian.setVisibility(View.GONE);
        }
        if (margan4!=null) {
            if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                tv_zy4_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy4_bao.setVisibility(View.GONE);
            }
        }
        String head = TextUtils.isEmpty(allUser.getImage()) ? "" : allUser.getImage();
        if (head.length() > 40) {
            tv_titl.setVisibility(View.INVISIBLE);
            iv_head.setVisibility(View.VISIBLE);
            String[] orderProjectArray = head.split("\\|");
            head = orderProjectArray[0];
        }
        if (!(head.equals("") || head.equals(null))) {
            tv_titl.setVisibility(View.INVISIBLE);
            iv_head.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+head,iv_head, DemoApplication.mOptions);
        } else {
            iv_head.setVisibility(View.INVISIBLE);
            tv_titl.setVisibility(View.VISIBLE);
            tv_titl.setText(TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName());
        }
        if (("00").equals(allUser.getSex())) {
            iv_sex.setImageResource(R.drawable.nv);
            //保 255 62 74  图 255 170 76
            iv_sex.setBackgroundColor(Color.rgb(234,121,219));
            tv_titl.setBackgroundResource(R.drawable.fx_bg_text_red);
        } else {
            iv_sex.setImageResource(R.drawable.nan);
            tv_titl.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }
        String resv3 = TextUtils.isEmpty(allUser.getResv3()) ? "" : allUser.getResv3();
        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
        String resv2 = TextUtils.isEmpty(allUser.getResv2()) ? "" : allUser.getResv2();
        String lat = DemoApplication.getInstance().getCurrentLat();
        String lng = DemoApplication.getInstance().getCurrentLng();
        if (lat!=null&&lng!=null) {
            if (!("".equals(lat) || "".equals(lng) || resv1.equals("") || resv2.equals(""))) {
                double latitude1 = Double.valueOf(lat);
                double longitude1 = Double.valueOf(lng);
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
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
        }else {
            tv_distance.setText("3km以外");
        }
        String sign = allUser.getSignaTure();
        if (sign==null||"".equals(sign)){
            sign = "未设置简介";
        }
        tv_qianming.setText(sign);
    }

    @Override
    public void showproLoading() {

    }

    @Override
    public void hideproLoading() {

    }

    @Override
    public void showproError() {

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
    public void updateCurrentPrice(Object success) {
        yuE = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
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
        MySingleton.getInstance(UOrderDetailTwoActivity.this).addToRequestQueue(request);

    }
}
