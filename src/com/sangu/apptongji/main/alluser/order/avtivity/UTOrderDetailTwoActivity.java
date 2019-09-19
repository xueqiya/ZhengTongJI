package com.sangu.apptongji.main.alluser.order.avtivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.address.AddressListTwoActivity;
import com.sangu.apptongji.main.alluser.entity.FWFKInfo;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.alluser.presenter.IFWFKPresenter;
import com.sangu.apptongji.main.alluser.presenter.IOrderDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FWFKPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.OrderDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IFWFKView;
import com.sangu.apptongji.main.alluser.view.IOrderDetailView;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.qiye.QiYeYuGoActivity;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.utils.ZhifuHelpUtils;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by user on 2016/9/1.
 */
public class UTOrderDetailTwoActivity extends BaseActivity implements IPriceView,IUAZView,IOrderDetailView,IQiYeDetailView,IFWFKView{
    private static final int SDK_PAY_FLAG = 1;
    private IOrderDetailPresenter orderDetailPresenter=null;
    private IUAZPresenter uazPresenter=null;
    private IFWFKPresenter presenter=null;
    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_titl;
    private TextView tv_nianling;
    private TextView tv_company;
    private TextView tv_company_count;
    private TextView tv_distance;
    private TextView tv_project_one;
    private TextView tv_project_two;
    private TextView tv_project_three;
    private TextView tv_project_four;
    private TextView tv_zy1_bao;
    private TextView tv_zy2_bao;
    private TextView tv_zy3_bao;
    private TextView tv_zy4_bao;
    private TextView tv_qianming;
    private TextView iv_zy1_tupian;
    private TextView iv_zy2_tupian;
    private TextView iv_zy3_tupian;
    private TextView iv_zy4_tupian;
    private ImageView iv_sex;
    private LinearLayout ll3_paidan,ll2,ll2_paidan;
    private RelativeLayout rl_sex;
    private RelativeLayout rl_qiye;
    private ImageView iv_paidan_id1,iv_paidan_caiwu,iv_paidan_kehu,iv_jiantou;
    private TextView tv_paidan_id1,tev_paidan_id1,tv_paidan_id1_time,tv_paidan_caiwu,tev_paidan_caiwu,tv_paidan_caiwu_time
            ,tev_paidan_kehu,tv_paidan_kehu_time,tv_paidan_kehu;
    private RelativeLayout rl_paidan_id1;

    private IWXAPI api=null;
    private IPricePresenter pricePresenter;
    private String pass;
    private double yuE;
    private int errorTime=3;
    LinearLayout ll_one,ll_two,ll_three,ll_four,ll;
    private String orderState,biaoshi,biaoshi1,totalAmt,biaoshi2;
    private EditText et_your_project=null,et_qianshou_dizhi=null,et_dingdan_bianhao=null,et_zhifu_jine=null,etUBeizhu=null;
    private Button btnCommit=null,btnCansul=null;
    private String orderId,merId,orderProject,orderNumber,orderAmt,orderSum,oImage,mImage,beizhu,companyId,userId,send_id1,send_id2
            ,image1,image2,image3,image4,time1,time2,time3,resv4,oSignature,mSignature,orderTime,dynamicSeq,create_time;
    private Button btnCommit2=null;
    private TextView tv_count_fankui=null;
    private RelativeLayout rl_btn1=null;
    private LinearLayout ll_btn=null;
    private ImageView ivkehu=null,ivshangjia=null;
    private RelativeLayout llM_beizhu=null;
    private RelativeLayout rl_ticheng=null;
    private CheckBox cb_fapiao=null;
    private IQiYeInfoPresenter qiYeInfoPresenter=null;
    private String resv5="",resv5Geren="",resv5Qiye="",orderBody;
    private TextView tv_qiye_time_shanghu=null,tv_qiye_time_xiaofei=null,tv_qiye_xiaofei=null,tv_qiye_shanghu=null,tv_qiye_ticheng=null;
    private TextView tv_saomiao=null,tv_zhifuxianshi=null,tv_fenxiang=null,tev_qiye_xiaofei=null,tev_qiye_shanghu=null,tv_fasong;
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
                        sendPushMessage(merId,"000");
                        Toast.makeText(UTOrderDetailTwoActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(UTOrderDetailTwoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
            sendPushMessage(merId,"000");
            Toast.makeText(UTOrderDetailTwoActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morder_moshier);
        initView();
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        pricePresenter = new PricePresenter(this,this);
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        rl_qiye = (RelativeLayout) findViewById(R.id.rl_qiye);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_company_count = (TextView) findViewById(R.id.tv_company_count);
        tv_nianling = (TextView) findViewById(R.id.tv_nianling);
        tv_titl = (TextView) findViewById(R.id.tv_titl);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_fasong = (TextView) findViewById(R.id.tv_fasong);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        tv_project_one = (TextView) findViewById(R.id.tv_project_one);
        tv_project_two = (TextView) findViewById(R.id.tv_project_two);
        tv_project_three = (TextView) findViewById(R.id.tv_project_three);
        tv_project_four = (TextView) findViewById(R.id.tv_project_four);
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
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        qiYeInfoPresenter = new QiYeInfoPresenter(this,this);
        uazPresenter = new UAZPresenter(this,this);
        presenter = new FWFKPresenter(this,this);
        orderDetailPresenter = new OrderDetailPresenter(this,this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        llM_beizhu = (RelativeLayout) findViewById(R.id.llM_beizhu);
        ivkehu = (ImageView) findViewById(R.id.iv_paidan_qiye);
        ivshangjia = (ImageView) findViewById(R.id.iv_paidan_qiyeren);
        rl_ticheng = (RelativeLayout) findViewById(R.id.rl_ticheng);
        tv_qiye_ticheng = (TextView) findViewById(R.id.tv_qiye_ticheng);
        tv_qiye_time_shanghu = (TextView) findViewById(R.id.tv_paidan_qiyeren_time);
        tev_qiye_xiaofei = (TextView) findViewById(R.id.tev_paidan_qiye);
        tev_qiye_shanghu = (TextView) findViewById(R.id.tev_paidan_qiyeren);
        tv_qiye_time_xiaofei = (TextView) findViewById(R.id.tv_paidan_qiye_time);
        tv_qiye_xiaofei = (TextView) findViewById(R.id.tv_paidan_qiye);
        tv_qiye_shanghu = (TextView) findViewById(R.id.tv_paidan_qiyeren);
        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        ll_two = (LinearLayout) findViewById(R.id.ll_two);
        ll_three = (LinearLayout) findViewById(R.id.ll_three);
        ll_four = (LinearLayout) findViewById(R.id.ll_four);
        ll = (LinearLayout) findViewById(R.id.ll);
        ll.setFocusable(true);
        ll.setFocusableInTouchMode(true);
        ll.requestFocus();
//        yuE = DemoApplication.getApp().getCurrenPrice();
        Intent intent = this.getIntent();
        orderTime = intent.getStringExtra("orderTime");
        orderBody = intent.getStringExtra("orderBody");
        companyId = intent.getStringExtra("companyId");
        userId = intent.getStringExtra("userId");
        orderId = intent.getStringExtra("orderId");
        orderState = intent.getStringExtra("orderState");
        merId = intent.getStringExtra("merId");
        totalAmt = intent.hasExtra("totalAmt")?intent.getStringExtra("totalAmt"):"";
        biaoshi = intent.hasExtra("biaoshi")?intent.getStringExtra("biaoshi"):"";
        biaoshi2 = intent.hasExtra("biaoshi2")?intent.getStringExtra("biaoshi2"):"";
        biaoshi1 = intent.hasExtra("biaoshi1")?intent.getStringExtra("biaoshi1"):"";
        if ("".equals(companyId)&&merId.length()<12){
            uazPresenter.loadThisDetail(merId);
        }else {
            rl_sex.setVisibility(View.INVISIBLE);
            rl_qiye.setVisibility(View.INVISIBLE);
            final RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) tv_name.getLayoutParams();
            lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            lp.height= RelativeLayout.LayoutParams.WRAP_CONTENT;
            tv_name.setLayoutParams(lp);
            if (companyId.length()>0){
                qiYeInfoPresenter.loadQiYeInfo(companyId);
            }else {
                qiYeInfoPresenter.loadQiYeInfo(merId);
            }
        }
        if ("01".equals(biaoshi)){
            iv_jiantou.setVisibility(View.VISIBLE);
            ll2_paidan.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll3_paidan.setVisibility(View.VISIBLE);
        }
        if ("02".equals(biaoshi)){
            iv_jiantou.setVisibility(View.VISIBLE);
            ll2_paidan.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll3_paidan.setVisibility(View.GONE);
        }
        if (!"01".equals(biaoshi2)){
            if (orderState.equals("12")){
                initView0();
            }else if (orderState.equals("03")) {
                initView2();
            } else if (orderState.equals("05") || orderState.equals("07")) {
                initView1();
            } else if (orderState.equals("04")) {
                initView3();
            } else if (orderState.equals("06")) {
                initView4();
            } else if (orderState.equals("08") || orderState.equals("09")) {
                initView5();
            } else if (orderState.equals("10") || orderState.equals("11")) {
                presenter.loadFWFKList(orderId);
                initView6();
            }
        }
        if ("不可操作".equals(biaoshi)){
            initView7();
        }
        if ("01".equals(biaoshi1)){
            tv_fenxiang.setVisibility(View.VISIBLE);
            tv_fenxiang.setEnabled(true);
            tv_fenxiang.setText("派单");
            tv_fenxiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UTOrderDetailTwoActivity.this, QiYeYuGoActivity.class).putExtra("companyId",companyId)
                            .putExtra("orderId",orderId).putExtra("totalAmt",totalAmt));
                }
            });
        }
        orderDetailPresenter.loadOrderDetail(orderId);
    }

    private void initView0() {
        llM_beizhu.setVisibility(View.GONE);
        etUBeizhu.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setVisibility(View.VISIBLE);
        btnCommit.setText("验  资");
        btnCommit.setEnabled(true);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflaterDl = LayoutInflater.from(UTOrderDetailTwoActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                final Dialog dialog = new AlertDialog.Builder(UTOrderDetailTwoActivity.this,R.style.Dialog).create();
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

                        zhifu3(orderSum);
                    }
                });
                re_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        rechargefromWx(orderSum,orderId);
                    }
                });
                re_item5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        rechargefromZhFb(orderSum,orderId);
                    }
                });
                re_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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

        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        final double orderSu = Double.valueOf(sum);
        Double d = yuE - orderSu;
        if (d >= 0) {
            LayoutInflater inflaterDl = LayoutInflater.from(UTOrderDetailTwoActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(UTOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                    final ProgressDialog pd = new ProgressDialog(UTOrderDetailTwoActivity.this);
                    pd.setMessage("正在支付...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    if (pwdView.getStrPassword().equals(pass)) {
                        dialog.dismiss();
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
                            ZhifuHelpUtils.showErrorLing(UTOrderDetailTwoActivity.this);
                        }else {
                            ZhifuHelpUtils.showErrorTishi(UTOrderDetailTwoActivity.this,times + "",null,"000");
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
                    startActivity(new Intent(UTOrderDetailTwoActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UTOrderDetailTwoActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UTOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                    rechargefromWx(orderSum,orderId);
                }
            });
            re_item5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    rechargefromZhFb(orderSum,orderId);
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
        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request3);
    }

    private void fukuan() {
        String url = FXConstant.URL_DingDan_Pay;
        StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("success")) {
                        sendPushMessage(merId,"000");
                        Toast.makeText(UTOrderDetailTwoActivity.this, "付款成功,您已完成验资！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UTOrderDetailTwoActivity.this, "付款失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UTOrderDetailTwoActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", orderId);
                params.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("balance", orderSum);
                return params;
            }
        };
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
            MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request1);

        } else {
            ToastUtils.showNOrmalToast(UTOrderDetailTwoActivity.this.getApplicationContext(), "您的账户已被冻结");
        }
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
                if (type==0) {
                    param.put("type","000");
                }else {
                    param.put("type","12");
                }
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
        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
    }
    private void sendPushMessage(final String hxid1,final String type) {
        String comId = "0";
        if (companyId!=null&&!"".equals(companyId)){
            comId = companyId;
        }
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        final String finalComId = comId;
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateUscount(hxid1);
                reduceUscount(DemoHelper.getInstance().getCurrentUsernName());
                duanxintongzhi(hxid1, "【正事多】 通知:您有一条新的订单消息", type);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                updateUscount(hxid1);
                reduceUscount(DemoHelper.getInstance().getCurrentUsernName());
                duanxintongzhi(hxid1, "【正事多】 通知:您有一条新的订单消息", type);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",hxid1);
                param.put("body","订单消息");
                param.put("type",type);
                param.put("userId",myId);
                param.put("companyId", finalComId);
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
    }

    private void reduceUscount(final String currentUsernName) {
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
                param.put("orderUnReadCount","-1");
                param.put("userId",currentUsernName);
                return param;
            }
        };
        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
    }

    private void duanxintongzhi(final String id, final String message,final String type) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setResult(RESULT_OK);
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
                MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void rechargefromWx(String zongjia, String uId) {
        String chongzhiId=null;
        chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        zongjia = (int)(Double.parseDouble(zongjia)*100)+"";
        if (Double.parseDouble(zongjia)>0) {
            final String mubiaoId = chongzhiId + "_" + "2" + "_" + uId;
            Toast.makeText(UTOrderDetailTwoActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
            String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
            final String finalBalance = zongjia;
            StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString("activity", "UTOrderDetailTwoActivity");
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
                    Toast.makeText(UTOrderDetailTwoActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
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
            MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(UTOrderDetailTwoActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
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
        chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
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
                                PayTask alipay = new PayTask(UTOrderDetailTwoActivity.this);
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
                    Toast.makeText(UTOrderDetailTwoActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(UTOrderDetailTwoActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void updataFuWuFanKuiList(List<FWFKInfo> fuWuFKinfo) {
        int count = fuWuFKinfo.size();
        tv_count_fankui.setText("["+count+"]");
        btnCommit2.setText("售后反馈");
        btnCommit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderState.equals("11")) {
                    new AlertDialog.Builder(UTOrderDetailTwoActivity.this)
                            .setTitle("确认")
                            .setMessage("确认申请售后吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    shenqingshouhou();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }else if (orderState.equals("10")){
                    startActivity(new Intent(UTOrderDetailTwoActivity.this, FWFKListActivity.class)
                            .putExtra("biaoshi", "00").putExtra("orderId", orderId).putExtra("resv5",resv5).putExtra("companyId",companyId));
                }
            }
        });
        tv_count_fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UTOrderDetailTwoActivity.this, FWFKListActivity.class)
                        .putExtra("biaoshi", "02").putExtra("orderId", orderId).putExtra("resv5",resv5).putExtra("companyId",companyId));
            }
        });
    }
    private void initView6() {
        llM_beizhu.setVisibility(View.GONE);
        etUBeizhu.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        ll_btn.setVisibility(View.INVISIBLE);
        rl_btn1.setVisibility(View.VISIBLE);
        btnCommit.setVisibility(View.VISIBLE);
        tv_fenxiang.setVisibility(View.INVISIBLE);
        tv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentImage();
            }
        });
    }
    private void initView7() {
        rl_ticheng.setVisibility(View.VISIBLE);
        tv_fenxiang.setEnabled(false);
        etUBeizhu.setEnabled(false);
        btnCommit.setEnabled(false);
        btnCansul.setEnabled(false);
        llM_beizhu.setEnabled(false);
        ivkehu.setEnabled(false);
        tv_qiye_time_shanghu.setEnabled(false);
        tv_qiye_time_xiaofei.setEnabled(false);
        tv_qiye_xiaofei.setEnabled(false);
        tv_qiye_shanghu.setEnabled(false);
        ivshangjia.setEnabled(false);
    }

    @Override
    public void updateThisUser(Userful allUser) {
        tv_name.setText(TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName());
        String nianLing = TextUtils.isEmpty(allUser.getuAge()) ? "27" : allUser.getuAge();
        tv_nianling.setText(nianLing);
        String company = TextUtils.isEmpty(allUser.getCompany()) ? "暂未加入企业" : allUser.getCompany();
        if (company == null || company.equals("")) {
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

    private void initView2() {
        btnCansul.setVisibility(View.VISIBLE);
        btnCommit.setVisibility(View.VISIBLE);
        btnCansul.setEnabled(false);
        btnCommit.setEnabled(true);
        btnCansul.setText("等待对方提交");
        btnCansul.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        btnCommit.setText("申请退款");
        etUBeizhu.setEnabled(true);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUBeizhu.setFocusable(true);
                etUBeizhu.setFocusableInTouchMode(true);
                etUBeizhu.requestFocus();
                final String beizhu = etUBeizhu.getText().toString().trim();
                if (!TextUtils.isEmpty(beizhu)) {
                    Intent intent = new Intent(UTOrderDetailTwoActivity.this,QianMingActivity.class);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("remark", beizhu);
                    intent.putExtra("biaoshi","13");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UTOrderDetailTwoActivity.this, "请写明退款原因", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        tv_fasong.setVisibility(View.VISIBLE);
        tv_fasong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase(null)){
                    LayoutInflater inflaterDl = LayoutInflater.from(UTOrderDetailTwoActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(UTOrderDetailTwoActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    tv_title.setText("单个订单最多发送给两个用户，该订单已达到上限！");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    return;
                }
                LayoutInflater inflaterDl = LayoutInflater.from(UTOrderDetailTwoActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_order_send, null);
                final Dialog dialog = new AlertDialog.Builder(UTOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                        PermissionUtil permissionUtil = new PermissionUtil(UTOrderDetailTwoActivity.this);
                        permissionUtil.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                new PermissionListener() {
                                    @Override
                                    public void onGranted() {
                                        //所有权限都已经授权
                                        Intent intent = new Intent(UTOrderDetailTwoActivity.this,AddressListTwoActivity.class);
                                        intent.putExtra("biaoshi","03");
                                        intent.putExtra("orderId",orderId);
                                        intent.putExtra("orderBody",orderBody);
                                        intent.putExtra("hasId1",send_id1);
                                        intent.putExtra("hasId2",send_id2);
                                        intent.putExtra("hasId3",merId);
                                        intent.putExtra("hasId4",DemoHelper.getInstance().getCurrentUsernName());
                                        startActivityForResult(intent,0);
                                    }
                                    @Override
                                    public void onDenied(List<String> deniedPermission) {
                                        //Toast第一个被拒绝的权限
                                        Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限！",Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onShouldShowRationale(List<String> deniedPermission) {
                                        //Toast第一个勾选不在提示的权限
                                        Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                re_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent3 = new Intent(UTOrderDetailTwoActivity.this,FriendActivity.class);
                        intent3.putExtra("biaoshi","03");
                        intent3.putExtra("orderId",orderId);
                        intent3.putExtra("hasId1",send_id1);
                        intent3.putExtra("hasId2",send_id2);
                        intent3.putExtra("hasId3",merId);
                        intent3.putExtra("orderBody",orderBody);
                        intent3.putExtra("hasId4",DemoHelper.getInstance().getCurrentUsernName());
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
                        if (userId.equals(DemoHelper.getInstance().getCurrentUsernName())||userId.equals(merId)||userId.equals(send_id1)||userId.equals(send_id2)){
                            LayoutInflater inflaterDl = LayoutInflater.from(UTOrderDetailTwoActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                            final Dialog dialog2 = new AlertDialog.Builder(UTOrderDetailTwoActivity.this,R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout);
                            dialog2.setCanceledOnTouchOutside(false);
                            dialog2.setCancelable(false);
                            TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                            Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                            tv_title.setText("该账号已在本个订单中,无需再次发送！");
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });
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
        });
    }

    private void queryUserInfo(final String userId) {
        String url = FXConstant.URL_Get_UserInfo+userId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                sendPushMessage2(userId,1);
                if ("用户名为空".equals(code)){
                    SendMessage(userId,0,"");
                    SendMessage1(userId);
                }else {
                    com.alibaba.fastjson.JSONObject userInfo = object.getJSONObject("userInfo");
                    String name = userInfo.getString("uName");
                    if (name == null||"".equals(name)){
                        name="";
                    }else {
                        name = name+":";
                    }
                    SendMessage(userId,1,name);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
    }

    private void SendMessage1(final String userId) {
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        final String loginId = DemoHelper.getInstance().getCurrentUsernName();
        if (name==null||"".equals(name)){
            name = loginId;
        }
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final String finalName = name;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                sendToUser(userId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "发送失败，网络错误！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】订单("+orderBody+"),需要您验收签字,请注册手机端“正事多”在电子单据中查看");
                param.put("telNum", userId);
                Log.e("utorderdeac,sm1",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });

    }

    private void SendMessage(final String userId, final int type,final String name) {
        String lists;
        if (type==1){
            lists = userId+","+merId;
        }else {
            lists = merId;
        }
        if (send_id1!=null&&!"".equals(send_id1)){
            lists = lists+","+send_id1;
        }
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final String finalLists = lists;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (type==1) {
                    sendToUser(userId);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type==1) {
                    sendToUser(userId);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】订单(" + orderBody + "),需要("+name+userId+")的用户签字验收");
                param.put("telNum", finalLists);
                Log.e("utorderdeac,sm",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void sendToUser(final String username) {
        String url = FXConstant.URL_FASONG_DZDANJU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateaddUscount(username);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){
                    Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络连接中断,发送失败！",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("order_id",orderId);
                param.put("send_id",DemoHelper.getInstance().getCurrentUsernName());
                param.put("u_id",username);
                return param;
            }
        };
        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
    }
    private void updateaddUscount(final String username) {
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
                param.put("thirdPartyCount","1");
                param.put("userId",username);
                return param;
            }
        };
        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
    }

    private void initView1() {
        tv_zhifuxianshi.setText("实付：");
        llM_beizhu.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setVisibility(View.VISIBLE);
        tv_fenxiang.setVisibility(View.INVISIBLE);
        tv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentImage();
            }
        });
        if ("05".equals(orderState)) {
            llM_beizhu.setVisibility(View.GONE);
            etUBeizhu.setVisibility(View.GONE);
            btnCommit.setText("申请售后");
            btnCommit.setEnabled(true);
            btnCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(UTOrderDetailTwoActivity.this)
                            .setTitle("确认")
                            .setMessage("确认申请售后吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    shenqingshouhou();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            });
        }else {
            llM_beizhu.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setText("退款成功");
            btnCommit.setEnabled(false);
            btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }
    }
    private void shenqingshouhou() {
        String url = FXConstant.URL_SHOUHOU_UPDATE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(UTOrderDetailTwoActivity.this,"申请成功,等待商家回复！",Toast.LENGTH_SHORT).show();
                if (resv5.length()>0||companyId.length()>0){
                    updateQjcount();
                }else {
                    updateUscount(merId);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UTOrderDetailTwoActivity.this,"网络连接错误！",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderId",orderId);
                param.put("state","10");
                return param;
            }
        };
        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
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
        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
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
                param.put("companyId",companyId);
                return param;
            }
        };
        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request);
    }
    private void saveCurrentImage()
    {
        //获取当前屏幕的大小
//        int width = getWindow().getDecorView().getRootView().getWidth();
//        int height = getWindow().getDecorView().getRootView().getHeight();
//        //生成相同大小的图片
//        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888 );
//        //找到当前页面的跟布局
//        View view =  getWindow().getDecorView().getRootView();
//        //设置缓存
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
//        //从缓存中获取当前屏幕的图片
//        temBitmap = view.getDrawingCache();
//        //输出到sd卡
//        saveImg(temBitmap);
//        ScreenshotUtil.getBitmapByView(UTOrderDetailTwoActivity.this, ll, "订单", null,false);
        fenxiang();
    }

    private void fenxiang() {
        OnekeyShare oks = new OnekeyShare();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode(true);
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                } else if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                    paramsToShare.setShareType(Platform.SHARE_IMAGE);
                    paramsToShare.setTitle("正事多app");
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                } else if (platform.getName().equalsIgnoreCase(QZone.NAME)) {
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                }else if (platform.getName().equalsIgnoreCase(WechatMoments.NAME)){
                    paramsToShare.setShareType(Platform.SHARE_IMAGE);
                    paramsToShare.setTitle("正事多app");
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                }
            }
        });
        oks.show(this);
    }

    private void initView() {
        tv_paidan_id1 = (TextView) findViewById(R.id.tv_paidan_id1);
        tev_paidan_id1 = (TextView) findViewById(R.id.tev_paidan_id1);
        tv_paidan_id1_time = (TextView) findViewById(R.id.tv_paidan_id1_time);
        tv_paidan_caiwu = (TextView) findViewById(R.id.tv_paidan_caiwu);
        tev_paidan_caiwu = (TextView) findViewById(R.id.tev_paidan_caiwu);
        tv_paidan_caiwu_time = (TextView) findViewById(R.id.tv_paidan_caiwu_time);
        tev_paidan_kehu = (TextView) findViewById(R.id.tev_paidan_kehu);
        tv_paidan_kehu_time = (TextView) findViewById(R.id.tv_paidan_kehu_time);
        tv_paidan_kehu = (TextView) findViewById(R.id.tv_paidan_kehu);
        iv_paidan_id1 = (ImageView) findViewById(R.id.iv_paidan_id1);
        iv_paidan_caiwu = (ImageView) findViewById(R.id.iv_paidan_caiwu);
        iv_paidan_kehu = (ImageView) findViewById(R.id.iv_paidan_kehu);
        iv_jiantou = (ImageView) findViewById(R.id.iv_jiantou);
        ll3_paidan = (LinearLayout) findViewById(R.id.ll3_paidan);
        ll2_paidan = (LinearLayout) findViewById(R.id.ll2_paidan);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        rl_paidan_id1 = (RelativeLayout) findViewById(R.id.rl_paidan_id1);

        et_your_project = (EditText) findViewById(R.id.et_your_project);
        et_qianshou_dizhi = (EditText) findViewById(R.id.et_qianshou_dizhi);
        et_dingdan_bianhao = (EditText) findViewById(R.id.et_dingdan_bianhao);
        et_zhifu_jine = (EditText) findViewById(R.id.et_zhifu_jine);
        cb_fapiao = (CheckBox) findViewById(R.id.cb_fapiao);
        etUBeizhu = (EditText) findViewById(R.id.et_Ubeizhu);
        tv_count_fankui = (TextView) findViewById(R.id.tv_count_fankui);
        btnCommit2 = (Button) findViewById(R.id.btn_comit2);
        ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
        rl_btn1 = (RelativeLayout) findViewById(R.id.rl_btn1);
        tv_zhifuxianshi = (TextView) findViewById(R.id.tv_zhifuxianshi);
        tv_fenxiang = (TextView) findViewById(R.id.tv_fenxiang);
        tv_saomiao = (TextView) findViewById(R.id.tv_saomiao);
        btnCommit = (Button) findViewById(R.id.btn_comit);
        btnCansul = (Button) findViewById(R.id.btn_cansul);
        btnCansul.setEnabled(false);
        btnCommit.setEnabled(false);
    }

    @Override
    public void updateCurrentOrderDetail(OrderDetail orderDetail) {
        dynamicSeq = orderDetail.getDynamicSeq();
        create_time = orderDetail.getCreate_time();
        image1 = TextUtils.isEmpty(orderDetail.getImage1())?"":orderDetail.getImage1();
        image2 = TextUtils.isEmpty(orderDetail.getImage2())?"":orderDetail.getImage2();
        image3 = TextUtils.isEmpty(orderDetail.getImage3())?"":orderDetail.getImage3();
        image4 = TextUtils.isEmpty(orderDetail.getImage2())?"":orderDetail.getImage4();
        time1 = TextUtils.isEmpty(orderDetail.getTime1())?"":orderDetail.getTime1();
        time2 = TextUtils.isEmpty(orderDetail.getTime2())?"":orderDetail.getTime2();
        time3 = TextUtils.isEmpty(orderDetail.getTime3())?"":orderDetail.getTime3();
        resv4 = TextUtils.isEmpty(orderDetail.getResv4())?"":orderDetail.getResv4();
        oSignature = TextUtils.isEmpty(orderDetail.getoSignature())?"":orderDetail.getoSignature();
        mSignature = TextUtils.isEmpty(orderDetail.getmSignature())?"":orderDetail.getmSignature();
        send_id1 = orderDetail.getSend_id1();
        send_id2 = orderDetail.getSend_id2();
        orderProject = TextUtils.isEmpty(orderDetail.getOrderProject())?"":orderDetail.getOrderProject();
        orderNumber = TextUtils.isEmpty(orderDetail.getOrderNumber())?"":orderDetail.getOrderNumber();
        orderAmt = TextUtils.isEmpty(orderDetail.getOrderAmt())?"":orderDetail.getOrderAmt();
        orderSum = TextUtils.isEmpty(orderDetail.getOrderSum())?"":orderDetail.getOrderSum();
        oImage = TextUtils.isEmpty(orderDetail.getoImage())?"":orderDetail.getoImage();
        mImage = TextUtils.isEmpty(orderDetail.getmImage())?"":orderDetail.getmImage();
        beizhu = TextUtils.isEmpty(orderDetail.getRemark())?"":orderDetail.getRemark();
        resv5 = TextUtils.isEmpty(orderDetail.getResv5())?"":orderDetail.getResv5();
//        yueding = TextUtils.isEmpty(orderDetail.getResv1())?"":orderDetail.getResv1();
//        resv2 = TextUtils.isEmpty(orderDetail.getResv2())?"":orderDetail.getResv2();
        String time4 = TextUtils.isEmpty(orderDetail.getTime4())?"00":orderDetail.getTime4();
        String oSigntrue = TextUtils.isEmpty(orderDetail.getoSignature())?"":orderDetail.getoSignature();
        String mSigntrue = TextUtils.isEmpty(orderDetail.getmSignature())?"":orderDetail.getmSignature();
        if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase("null")){
            iv_jiantou.setVisibility(View.VISIBLE);
            ll2_paidan.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll3_paidan.setVisibility(View.GONE);
            biaoshi = "02";
            if (time2==null||"".equals(time2)){
                tev_paidan_kehu.setVisibility(View.INVISIBLE);
                tv_paidan_kehu_time.setVisibility(View.INVISIBLE);
            }else {
                tev_paidan_kehu.setVisibility(View.VISIBLE);
                tv_paidan_kehu_time.setVisibility(View.VISIBLE);
                tv_paidan_kehu_time.setText(time2);
            }
            if (time3==null||"".equals(time3)){
                tev_paidan_caiwu.setVisibility(View.INVISIBLE);
                tv_paidan_caiwu_time.setVisibility(View.INVISIBLE);
            }else {
                tev_paidan_caiwu.setVisibility(View.VISIBLE);
                tv_paidan_caiwu_time.setVisibility(View.VISIBLE);
                tv_paidan_caiwu_time.setText(time3);
            }
            if (!image3.equals("")){
                tv_paidan_kehu.setText("客户已确认");
            }
            if (!image1.equals("")){
                tv_paidan_kehu.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_PAIDANQIANZI + image1,iv_paidan_kehu);
            }
            if (!image4.equals("")){
                tv_paidan_caiwu.setText("客户已确认");
            }
            if (!image2.equals("")){
                tv_paidan_caiwu.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_PAIDANQIANZI + image2,iv_paidan_caiwu);
            }
        }else if (send_id1!=null&&!"".equals(send_id1)&&!send_id1.equalsIgnoreCase("null")){
            iv_jiantou.setVisibility(View.VISIBLE);
            ll2_paidan.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll3_paidan.setVisibility(View.VISIBLE);
            biaoshi = "01";
            if (time2==null||"".equals(time2)){
                rl_paidan_id1.setVisibility(View.GONE);
                tev_paidan_id1.setVisibility(View.INVISIBLE);
            }else {
                rl_paidan_id1.setVisibility(View.VISIBLE);
                tev_paidan_id1.setVisibility(View.VISIBLE);
                tv_paidan_id1_time.setText(time2);
            }
            if (!image3.equals("")){
                tv_paidan_id1.setText("客户已确认");
            }
            if (!image1.equals("")){
                tv_paidan_id1.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_PAIDANQIANZI + image1,iv_paidan_id1);
            }
        }else {
            iv_jiantou.setVisibility(View.GONE);
            ll2_paidan.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll3_paidan.setVisibility(View.GONE);
            biaoshi = "00";
        }
        String ticheng = "0%";
        if (resv5.length()>0){
            resv5Geren = resv5.split("\\|")[0];
            resv5Qiye = resv5.split("\\|")[1];
            if (resv5.split("\\|").length>2) {
                ticheng = resv5.split("\\|")[2];
            }
        }
        if ((mImage==null&&mSignature==null)||(mImage.equals("")&&mSignature.equals(""))||(mImage.equalsIgnoreCase("null")&&mSignature.equalsIgnoreCase("null"))){
            iv_jiantou.setImageResource(R.drawable.fuzejiantouone);
        }else if ((image1==null&&image3==null)||(image1.equals("")&&image3.equals(""))||(image1.equalsIgnoreCase("null")&&image3.equalsIgnoreCase("null"))){
            if (send_id2==null||"".equals(send_id2)||send_id2.equalsIgnoreCase(null)) {
                iv_jiantou.setImageResource(R.drawable.fuzejiantousix);
            }else {
                iv_jiantou.setImageResource(R.drawable.fuzejiantoutwo);
            }
        }else if ((image2==null&&image4==null)||(image2.equals("")&&image4.equals(""))||(image2.equalsIgnoreCase("null")&&image4.equalsIgnoreCase("null"))){
            if (send_id2==null||"".equals(send_id2)||send_id2.equalsIgnoreCase(null)) {
                iv_jiantou.setImageResource(R.drawable.fuzejiantoufour);
            }else {
                iv_jiantou.setImageResource(R.drawable.fuzejiantouthree);
            }
        }else if ((oImage==null&&oSignature==null)||(oImage.equals("")&&oSignature.equals(""))||(oImage.equalsIgnoreCase("null")&&oSignature.equals("null"))){
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufour);
        }else {
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufive);
        }
        if (oImage!=null&&resv4!=null&&!"".equals(oImage)&&!"".equals(resv4)){
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufive);
        }
        tv_qiye_ticheng.setText(ticheng);
        if (time1==null||"".equals(time1)){
            tev_qiye_shanghu.setVisibility(View.INVISIBLE);
            tv_qiye_time_shanghu.setVisibility(View.INVISIBLE);
        }else {
            tev_qiye_shanghu.setVisibility(View.VISIBLE);
            tv_qiye_time_shanghu.setVisibility(View.VISIBLE);
        }
        if (resv4==null||"".equals(resv4)){
            tev_qiye_xiaofei.setVisibility(View.VISIBLE);
            tv_qiye_time_xiaofei.setVisibility(View.VISIBLE);
            tv_qiye_time_xiaofei.setText(orderTime);
        }else {
            tev_qiye_xiaofei.setVisibility(View.VISIBLE);
            tev_qiye_xiaofei.setText("完成:");
            tv_qiye_time_xiaofei.setVisibility(View.VISIBLE);
            tv_qiye_time_xiaofei.setText(resv4);
        }
        tv_qiye_time_shanghu.setText(time1);
        if (!oSigntrue.equals("")){
            tv_qiye_xiaofei.setText("客户已确认");
        }
        if (!mSigntrue.equals("")){
            tv_qiye_shanghu.setText("商家已确认");
        }
        if (!oImage.equals("")){
            tv_qiye_xiaofei.setVisibility(View.INVISIBLE);
            String[] avatar = oImage.split("\\|");
            oImage = avatar[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN + oImage,ivkehu);
        }
        if (!mImage.equals("")){
            tv_qiye_shanghu.setVisibility(View.INVISIBLE);
            String[] avatar = mImage.split("\\|");
            mImage = avatar[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN + mImage,ivshangjia);
        }
        if (orderSum!=null&&!"".equals(orderSum)) {
            double jine = Double.parseDouble(orderSum);
            final String str = String.format("%.2f", jine);
            et_zhifu_jine.setText(str+"元");
        }else {
            et_zhifu_jine.setText("0.00元");
        }
        et_your_project.setText(orderProject);
        et_qianshou_dizhi.setText(orderNumber);
//        et_dingdan_bianhao.setText(orderAmt);
        et_zhifu_jine.setEnabled(false);
        et_zhifu_jine.setTextColor(Color.BLACK);
        et_your_project.setTextColor(Color.BLACK);
        et_qianshou_dizhi.setTextColor(Color.BLACK);
        et_dingdan_bianhao.setTextColor(Color.BLACK);
        et_your_project.setEnabled(false);
        et_qianshou_dizhi.setEnabled(false);
        et_dingdan_bianhao.setEnabled(false);
        if (beizhu.equals("")&&mImage.equals("")){
            llM_beizhu.setVisibility(View.VISIBLE);
        }else {
            llM_beizhu.setVisibility(View.VISIBLE);
            etUBeizhu.setText(beizhu);
            etUBeizhu.setTextColor(Color.BLACK);
        }
        cb_fapiao.setEnabled(false);
        if (time4.equals("01")){
            cb_fapiao.setChecked(true);
        }else {
            cb_fapiao.setChecked(false);
        }
        etUBeizhu.setText(beizhu);
        if ("01".equals(biaoshi2)){
            llM_beizhu.setVisibility(View.GONE);
            etUBeizhu.setVisibility(View.GONE);
            btnCansul.setVisibility(View.GONE);
            ll_btn.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.VISIBLE);
            if ("05".equals(orderState)||"07".equals(orderState)){
                btnCommit.setText("交易完成");
                btnCommit.setEnabled(false);
                btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            }else {
                final String myUserId = DemoHelper.getInstance().getCurrentUsernName();
                if (myUserId.equals(send_id1)){
                    if (!"".equals(image1)){
                        btnCommit.setText("交 易 中");
                        btnCommit.setEnabled(false);
                        btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                    }else {
                        btnCommit.setText("签    字");
                        btnCommit.setEnabled(true);
                        btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_green);
                        btnCommit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(UTOrderDetailTwoActivity.this, QianMingActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("biaoshi", "1201");
                                startActivityForResult(intent,0);
                            }
                        });
                    }
                }
                if (myUserId.equals(send_id2)){
                    if (!"".equals(image2)){
                        btnCommit.setText("交 易 中");
                        btnCommit.setEnabled(false);
                        btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                    }else {
                        btnCommit.setText("签    字");
                        btnCommit.setEnabled(true);
                        btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_green);
                        btnCommit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(UTOrderDetailTwoActivity.this, QianMingActivity.class);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("balance", orderSum);
                                intent.putExtra("biaoshi", "1101");
                                startActivityForResult(intent,0);
                            }
                        });
                    }
                }
            }
        }
    }

    private Bitmap decodeResource(Resources resources, int id) {
        int densityDpi = resources.getDisplayMetrics().densityDpi;
        Bitmap bitmap;
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
        if (densityDpi > DisplayMetrics.DENSITY_HIGH) {
            opts.inTargetDensity = value.density;
            bitmap = BitmapFactory.decodeResource(resources, id, opts);
        }else{
            bitmap = BitmapFactory.decodeResource(resources, id);
        }
        return bitmap;
    }

    private Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        return resizedBitmap;
    }

    private void initView3() {
        btnCommit.setText("确认签收");
        btnCommit.setEnabled(true);
        btnCansul.setEnabled(true);
        etUBeizhu.setEnabled(true);
        btnCansul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUBeizhu.setFocusable(true);
                etUBeizhu.setFocusableInTouchMode(true);
                etUBeizhu.requestFocus();
                final String beizhu = etUBeizhu.getText().toString().trim();
                if (!TextUtils.isEmpty(beizhu)) {
                    Intent intent = new Intent(UTOrderDetailTwoActivity.this,QianMingActivity.class);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("remark", beizhu);
                    intent.putExtra("biaoshi","13");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UTOrderDetailTwoActivity.this, "请写明退款原因", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        tv_fasong.setVisibility(View.VISIBLE);
        tv_fasong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase(null)){
                    LayoutInflater inflaterDl = LayoutInflater.from(UTOrderDetailTwoActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(UTOrderDetailTwoActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    tv_title.setText("单个订单最多发送给两个用户，该订单已达到上限！");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    return;
                }
                LayoutInflater inflaterDl = LayoutInflater.from(UTOrderDetailTwoActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_order_send, null);
                final Dialog dialog = new AlertDialog.Builder(UTOrderDetailTwoActivity.this,R.style.Dialog).create();
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
                        PermissionUtil permissionUtil = new PermissionUtil(UTOrderDetailTwoActivity.this);
                        permissionUtil.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                new PermissionListener() {
                                    @Override
                                    public void onGranted() {
                                        //所有权限都已经授权
                                        Intent intent = new Intent(UTOrderDetailTwoActivity.this,AddressListTwoActivity.class);
                                        intent.putExtra("biaoshi","03");
                                        intent.putExtra("orderId",orderId);
                                        intent.putExtra("orderBody",orderBody);
                                        intent.putExtra("hasId1",send_id1);
                                        intent.putExtra("hasId2",send_id2);
                                        intent.putExtra("hasId3",merId);
                                        intent.putExtra("hasId4",DemoHelper.getInstance().getCurrentUsernName());
                                        startActivityForResult(intent,0);
                                    }
                                    @Override
                                    public void onDenied(List<String> deniedPermission) {
                                        //Toast第一个被拒绝的权限
                                        Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限！",Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onShouldShowRationale(List<String> deniedPermission) {
                                        //Toast第一个勾选不在提示的权限
                                        Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });
                re_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent3 = new Intent(UTOrderDetailTwoActivity.this,FriendActivity.class);
                        intent3.putExtra("biaoshi","03");
                        intent3.putExtra("orderId",orderId);
                        intent3.putExtra("orderBody",orderBody);
                        intent3.putExtra("hasId1",send_id1);
                        intent3.putExtra("hasId2",send_id2);
                        intent3.putExtra("hasId3",merId);
                        intent3.putExtra("hasId4",DemoHelper.getInstance().getCurrentUsernName());
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
                        if (userId.equals(DemoHelper.getInstance().getCurrentUsernName())||userId.equals(merId)||userId.equals(send_id1)||userId.equals(send_id2)){
                            LayoutInflater inflaterDl = LayoutInflater.from(UTOrderDetailTwoActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                            final Dialog dialog2 = new AlertDialog.Builder(UTOrderDetailTwoActivity.this,R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout);
                            dialog2.setCanceledOnTouchOutside(false);
                            dialog2.setCancelable(false);
                            TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                            Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                            tv_title.setText("该账号已在本个订单中,无需再次发送！");
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });
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
        });

        if ((resv5!=null&&resv5.length()>0)||(companyId!=null&&companyId.length()>0)){
            tv_qiye_xiaofei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UTOrderDetailTwoActivity.this, QianMingActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("merId", merId);
                    intent.putExtra("balance", orderSum);
                    intent.putExtra("companyId", companyId);
                    intent.putExtra("companyAmt",resv5Qiye);
                    intent.putExtra("balance1", resv5Geren);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("create_time", create_time);
                    intent.putExtra("biaoshi", "11");
                    startActivity(intent);
                    finish();
                }
            });
            btnCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UTOrderDetailTwoActivity.this, QianMingActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("merId", merId);
                    intent.putExtra("balance", orderSum);
                    intent.putExtra("companyId", companyId);
                    intent.putExtra("companyAmt",resv5Qiye);
                    intent.putExtra("balance1", resv5Geren);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("create_time", create_time);
                    intent.putExtra("biaoshi", "11");
                    startActivity(intent);
                    finish();
                }
            });
        }else {
            tv_qiye_xiaofei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UTOrderDetailTwoActivity.this, QianMingActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("merId", merId);
                    intent.putExtra("balance", orderSum);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("create_time", create_time);
                    intent.putExtra("biaoshi", "11");
                    startActivity(intent);
                    finish();
                }
            });
            btnCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UTOrderDetailTwoActivity.this, QianMingActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("merId", merId);
                    intent.putExtra("balance", orderSum);
                    intent.putExtra("dynamicSeq", dynamicSeq);
                    intent.putExtra("create_time", create_time);
                    intent.putExtra("biaoshi", "11");
                    startActivity(intent);
                    finish();
                }
            });
        }
//        tv_qiye_xiaofei.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UTOrderDetailTwoActivity.this,QianMingActivity.class);
//                intent.putExtra("orderId",orderId);
//                intent.putExtra("merId",merId);
//                intent.putExtra("balance", orderSum);
//                intent.putExtra("biaoshi","11");
//                startActivity(intent);
//                finish();
//            }
//        });
//        btnCommit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UTOrderDetailTwoActivity.this,QianMingActivity.class);
//                intent.putExtra("orderId",orderId);
//                intent.putExtra("merId",merId);
//                intent.putExtra("balance", orderSum);
//                intent.putExtra("biaoshi","11");
//                startActivity(intent);
//                finish();
//            }
//        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            orderDetailPresenter.loadOrderDetail(orderId);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView4() {
        etUBeizhu.setEnabled(false);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
    }

    private void initView5() {
        etUBeizhu.setEnabled(false);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setText("申请平台介入");
        btnCommit.setEnabled(true);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UTOrderDetailTwoActivity.this);
                builder.setMessage("确认申请平台介入吗?");
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
                        String url = FXConstant.URL_Update_OrderState;
                        StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject object = new JSONObject(s);
                                    String code = object.getString("code");
                                    if (code.equals("success")) {
                                        dialog.dismiss();
                                        Toast.makeText(UTOrderDetailTwoActivity.this, "申请平台介入成功！", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(UTOrderDetailTwoActivity.this, "申请失败！", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                dialog.dismiss();
                                Toast.makeText(UTOrderDetailTwoActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params3 = new HashMap<String, String>();
                                params3.put("orderId", orderId);
                                params3.put("orderState", "09");
                                params3.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                return params3;
                            }
                        };
                        MySingleton.getInstance(UTOrderDetailTwoActivity.this).addToRequestQueue(request1);
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
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
        yuE = DemoApplication.getInstance().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }
}
