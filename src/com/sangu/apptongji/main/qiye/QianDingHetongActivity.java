package com.sangu.apptongji.main.qiye;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.avtivity.QianMingActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.WJPaActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.XiuGaiZFActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.ZhiFuSettingActivity;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.NoScrollGridView;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.FileUtils;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.sangu.apptongji.main.utils.OkHttpManager.context;

/**
 * Created by Administrator on 2017-02-15.
 */

public class QianDingHetongActivity extends BaseActivity implements IPriceView,View.OnClickListener{
    private static final int SDK_PAY_FLAG = 3;
    private TextView tv_yonghu,tv_jine;
    private TextView tv_gongsi;
    private TextView tv_time_yonghu;
    private TextView tv_time_gongsi;
    private RelativeLayout rl_jine;
    private LinearLayout ll_shijian_yonghu;
    private LinearLayout ll_shijian_gongsi;
    private ImageView iv_yonghu;
    private ImageView iv_gongsi;
    private Button btn_commit;
    private ArrayList<String> imagePaths1=null;
    private IPricePresenter pricePresenter;

    NoScrollGridView gridView1;
    TextView tv_content;
    private GridAdapter gridAdapter1=null;
    private int columnWidth;
    private Handler fileHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
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
                        Toast.makeText(QianDingHetongActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        addFriendInDetail(qiyeId);
                        insertHetong(biaoshi2,false);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(QianDingHetongActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    private double yuE;
    private String biaoshi,biaoshi2,image,feiyong,qiyeId,path1="",path2="",pass="",managerId,fufei,url,body;
    private String userId,companyName,comAddress,createTime,userSignature,comSignature,signatureTime;
    private int errorTime=3;
    private IWXAPI api=null;

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("sangu_chongzhi_zhuangtai", Context.MODE_PRIVATE);
        String zhuangtai = sp.getString("zhuangtai","失败");
        if ("成功".equals(zhuangtai)){
            Toast.makeText(QianDingHetongActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            if (sp!=null) {
                SharedPreferences.Editor editor = sp.edit();
                if (editor!=null) {
                    editor.clear();
                    editor.commit();
                }
            }
            insertHetong(biaoshi2,false);
//            addFriendInDetail(qiyeId);
        }
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String zhifuZhuangtai = intent.getStringExtra("zhifu");
        if (zhifuZhuangtai!=null&&"支付成功".equals(zhifuZhuangtai)){
            Toast.makeText(QianDingHetongActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//            addFriendInDetail(qiyeId);
            insertHetong(biaoshi2,false);
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.qianding_hetong_activity);
        pricePresenter = new PricePresenter(this,this);
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        tv_yonghu = (TextView) findViewById(R.id.tv_yonghu);
        tv_gongsi = (TextView) findViewById(R.id.tv_gongsi);
        tv_time_yonghu = (TextView) findViewById(R.id.tv_time_yonghu);
        tv_time_gongsi = (TextView) findViewById(R.id.tv_time_gongsi);
        tv_jine = (TextView) findViewById(R.id.tv_jine);
        rl_jine = (RelativeLayout) findViewById(R.id.rl_jine);
        ll_shijian_yonghu = (LinearLayout) findViewById(R.id.ll_shijian_yonghu);
        ll_shijian_gongsi = (LinearLayout) findViewById(R.id.ll_shijian_gongsi);
        iv_yonghu = (ImageView) findViewById(R.id.iv_yonghu);
        iv_gongsi = (ImageView) findViewById(R.id.iv_gongsi);
        btn_commit = (Button) findViewById(R.id.btn_commit);

        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        imagePaths1 = new ArrayList<>();
        gridView1 = (NoScrollGridView) findViewById(R.id.noScrollgridview);
        tv_content = (TextView) findViewById(R.id.tv_content);
        yuE = DemoApplication.getInstance().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        Intent intent = this.getIntent();
        body = intent.hasExtra("body") ? intent.getStringExtra("body"):"";
        fufei = intent.hasExtra("fufei") ? intent.getStringExtra("fufei") : "";
        biaoshi = intent.hasExtra("biaoshi") ? intent.getStringExtra("biaoshi") : "";
        biaoshi2 = intent.hasExtra("biaoshi2") ? intent.getStringExtra("biaoshi2") : "";
        image = intent.hasExtra("image") ? intent.getStringExtra("image"):"";
        feiyong = intent.hasExtra("feiyong") ? intent.getStringExtra("feiyong") : "";
        qiyeId = intent.hasExtra("qiyeId") ? intent.getStringExtra("qiyeId") : "";
        managerId = intent.hasExtra("managerId") ? intent.getStringExtra("managerId") : "";
        userId = intent.hasExtra("userId") ? intent.getStringExtra("userId") : "";
        companyName = intent.hasExtra("companyName") ? intent.getStringExtra("companyName") : "";
        comAddress = intent.hasExtra("comAddress") ? intent.getStringExtra("comAddress") : "";
        createTime = intent.hasExtra("createTime") ? intent.getStringExtra("createTime") : "";
        signatureTime = intent.hasExtra("signatureTime") ? intent.getStringExtra("signatureTime") : "";
        userSignature = intent.hasExtra("userSignature") ? intent.getStringExtra("userSignature") : "";
        comSignature = intent.hasExtra("comSignature") ? intent.getStringExtra("comSignature") : "";
        if ("yingpin".equals(biaoshi)) {
            url = FXConstant.URL_QIYE_ZHAOPIN;
        } else {
            url = FXConstant.URL_QIYE_JIAMENG;
        }
        initView();
        if ("yonghu".equals(biaoshi2)) {
            tv_yonghu.setOnClickListener(this);
            iv_yonghu.setOnClickListener(this);
            btn_commit.setOnClickListener(this);
        }else if ("gongsi".equals(biaoshi2)) {
            tv_gongsi.setOnClickListener(this);
            iv_gongsi.setOnClickListener(this);
            btn_commit.setOnClickListener(this);
            initgongsi();
        }else if ("quanbu".equals(biaoshi2)){
            initall();
        }
    }

    private void initView() {
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 4 ? 4 : cols;
        gridView1.setNumColumns(cols);
        // Item Width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        columnWidth = (screenWidth - columnSpace * (cols-1)) / cols;
        gridView1.setSelector(new ColorDrawable(Color.TRANSPARENT));
        String [] str;
        if (image!=null&&!"".equals(image)) {
            str = image.split("\\|");
            if (str.length>0) {
                String url1 = url+str[0];
                imagePaths1.add(url1);
            }
            if (str.length>1){
                String url1 = url+str[1];
                imagePaths1.add(url1);
            }
            if (str.length>2){
                String url1 = url+str[2];
                imagePaths1.add(url1);
            }
            if (str.length>3){
                String url1 = url+str[3];
                imagePaths1.add(url1);
            }
            if (str.length>4){
                String url1 = url+str[4];
                imagePaths1.add(url1);
            }
            if (str.length>5){
                String url1 = url+str[5];
                imagePaths1.add(url1);
            }
            if (str.length>6){
                String url1 = url+str[6];
                imagePaths1.add(url1);
            }
            if (str.length>7){
                String url1 = url+str[7];
                imagePaths1.add(url1);
            }
        }
        gridAdapter1 = new GridAdapter(imagePaths1);
        gridView1.setAdapter(gridAdapter1);
        tv_content.setText(body);
        if ("付费".equals(fufei)){
            rl_jine.setVisibility(View.VISIBLE);
            if (feiyong!=null&&!"".equals(feiyong)){
                double jine = Double.parseDouble(feiyong);
                String prices = String.format("%.2f", jine);
                tv_jine.setText(prices);
            }
        }else {
            rl_jine.setVisibility(View.VISIBLE);
            tv_jine.setText("免费");
        }
    }

    private void initall() {
        initgongsi();
        tv_gongsi.setVisibility(View.INVISIBLE);
        ll_shijian_gongsi.setVisibility(View.VISIBLE);
        if (comSignature.length()>0){
            iv_gongsi.setVisibility(View.VISIBLE);
            String qiandingTime = signatureTime.substring(0, 4) + "-" + signatureTime.substring(4, 6) + "-" + signatureTime.substring(6, 8) + " "
                    + signatureTime.substring(8, 10) + ":" + signatureTime.substring(10, 12);
            tv_time_gongsi.setVisibility(View.VISIBLE);
            tv_time_gongsi.setText(qiandingTime);
            ImageLoader.getInstance().displayImage(FXConstant.URL_HETONG_QIANMING+comSignature,iv_gongsi);
        }
    }

    private void initgongsi() {
        tv_yonghu.setVisibility(View.INVISIBLE);
        ll_shijian_yonghu.setVisibility(View.VISIBLE);
        if (userSignature.length()>0){
            iv_yonghu.setVisibility(View.VISIBLE);
            String qiandingTime = createTime.substring(0, 4) + "-" + createTime.substring(4, 6) + "-" + createTime.substring(6, 8) + " "
                    + createTime.substring(8, 10) + ":" + createTime.substring(10, 12);
            tv_time_yonghu.setVisibility(View.VISIBLE);
            tv_time_yonghu.setText(qiandingTime);
            ImageLoader.getInstance().displayImage(FXConstant.URL_HETONG_QIANMING+userSignature,iv_yonghu);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_yonghu:
                startActivityForResult(new Intent(QianDingHetongActivity.this, QianMingActivity.class).putExtra("biaoshi", "21"),0);
                break;
            case R.id.tv_gongsi:
                startActivityForResult(new Intent(QianDingHetongActivity.this, QianMingActivity.class).putExtra("biaoshi", "22"),1);
                break;
            case R.id.iv_yonghu:
                startActivityForResult(new Intent(QianDingHetongActivity.this, QianMingActivity.class).putExtra("biaoshi", "21"),0);
                break;
            case R.id.iv_gongsi:
                startActivityForResult(new Intent(QianDingHetongActivity.this, QianMingActivity.class).putExtra("biaoshi", "22"),1);
                break;
            case R.id.btn_commit:
                pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
                String path="";
                if ("yonghu".equals(biaoshi2)) {
                    path = path1;
                }else if ("gongsi".equals(biaoshi2)){
                    path = path2;
                }
                if (!"".equals(path)) {
                    if ("付费".equals(fufei)&&"yonghu".equals(biaoshi2)){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(QianDingHetongActivity.this);
                        builder.setMessage("该企业为付费加入,付费金额为"+feiyong+"元,是否确定加入");
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
                                LayoutInflater inflaterDl = LayoutInflater.from(QianDingHetongActivity.this);
                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                                final Dialog dialog2 = new AlertDialog.Builder(QianDingHetongActivity.this,R.style.Dialog).create();
                                dialog2.show();
                                dialog2.getWindow().setContentView(layout);
                                dialog2.setCanceledOnTouchOutside(true);
                                RelativeLayout re_item1 = (RelativeLayout) dialog2.findViewById(R.id.re_item1);
                                RelativeLayout re_item2 = (RelativeLayout) dialog2.findViewById(R.id.re_item2);
                                RelativeLayout re_item3 = (RelativeLayout) dialog2.findViewById(R.id.re_item3);
                                RelativeLayout re_item5 = (RelativeLayout) dialog2.findViewById(R.id.re_item5);
                                re_item5.setVisibility(View.VISIBLE);
                                TextView tv_title = (TextView) dialog2.findViewById(R.id.tv_title);
                                TextView tv_item1 = (TextView) dialog2.findViewById(R.id.tv_item1);
                                TextView tv_item2 = (TextView) dialog2.findViewById(R.id.tv_item2);
                                TextView tv_item5 = (TextView) dialog2.findViewById(R.id.tv_item5);
                                tv_title.setText("请选择支付方式");
                                tv_item1.setText("余 额 支 付");
                                tv_item2.setText("微 信 支 付");
                                tv_item5.setText("支付宝支付");
                                re_item1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                        showdialog();
                                    }
                                });
                                re_item2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                        rechargefromWx(feiyong);
                                    }
                                });
                                re_item5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                        rechargefromZhFb(feiyong);
                                    }
                                });
                                re_item3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                    }
                                });
                            }
                        });
                        builder.show();
                    }else {
                        insertHetong(biaoshi2,true);
                    }
                }else {
                    Toast.makeText(QianDingHetongActivity.this,"签名之后才能确认哦！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void showdialog() {
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
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
            final double orderSu = Double.valueOf(feiyong);
            Double d = yuE - orderSu;
            if (d >= 0) {
                LayoutInflater inflaterDl = LayoutInflater.from(QianDingHetongActivity.this);
                final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
                final Dialog dialog = new AlertDialog.Builder(QianDingHetongActivity.this,R.style.Dialog).create();
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
                        if (pwdView.getStrPassword().equals(pass)) {
                            dialog.dismiss();
                            insertHetong(biaoshi2,true);
                        } else {
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
                        startActivity(new Intent(QianDingHetongActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                    }
                });
            } else {
                LayoutInflater inflaterDl = LayoutInflater.from(QianDingHetongActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                final Dialog dialog = new AlertDialog.Builder(QianDingHetongActivity.this,R.style.Dialog).create();
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
                        rechargefromWx(feiyong);
                    }
                });
                re_item5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        rechargefromZhFb(feiyong);
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
        LayoutInflater inflaterDl = LayoutInflater.from(QianDingHetongActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
        final Dialog dialog2 = new AlertDialog.Builder(QianDingHetongActivity.this,R.style.Dialog).create();
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
        LayoutInflater inflater1 = LayoutInflater.from(QianDingHetongActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(QianDingHetongActivity.this,R.style.Dialog).create();
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
                startActivity(new Intent(QianDingHetongActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
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
        LayoutInflater inflater1 = LayoutInflater.from(QianDingHetongActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(QianDingHetongActivity.this,R.style.Dialog).create();
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
                startActivity(new Intent(QianDingHetongActivity.this, XiuGaiZFActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",pass));
            }
        });
    }

    private void showErrorMiMaSHZH() {
        LayoutInflater inflater1 = LayoutInflater.from(QianDingHetongActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(QianDingHetongActivity.this,R.style.Dialog).create();
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
                startActivity(new Intent(QianDingHetongActivity.this,ZhiFuSettingActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",pass));
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
        MySingleton.getInstance(QianDingHetongActivity.this).addToRequestQueue(request3);
    }

    private void rechargefromWx(String balance1) {
        String resv1;
        if ("yingpin".equals(biaoshi)) {
            resv1 = "0";
        } else {
            resv1 = "1";
        }
        String chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        final String mubiaoId = chongzhiId+"_"+"4"+"_"+chongzhiId+qiyeId+resv1;
        String balance = (int)(Double.parseDouble(balance1)*100)+"";
        Toast.makeText(QianDingHetongActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = balance;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity","QianDingHetongActivity");
                    editor.commit();
                    org.json.JSONObject object = new org.json.JSONObject(s);
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
                Toast.makeText(QianDingHetongActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("body","正事多-加入企业");
                param.put("detail","正事多-加入企业");
                param.put("out_trade_no",getNowTime3());
                param.put("total_fee", finalBalance);
                param.put("spbill_create_ip",getHostIP());
                param.put("attach",mubiaoId);
                return param;
            }
        };
        MySingleton.getInstance(QianDingHetongActivity.this).addToRequestQueue(request);
    }

    private String getNowTime3() {
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

    private void rechargefromZhFb(final String balance){
        String resv1;
        if ("yingpin".equals(biaoshi)) {
            resv1 = "0";
        } else {
            resv1 = "1";
        }
        String mubiaoId = DemoHelper.getInstance().getCurrentUsernName();
        String chongzhiId=null;
        try {
            chongzhiId = URLEncoder.encode(qiyeId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (balance!=null&&Double.parseDouble(balance)>0) {
            String url = FXConstant.URL_ZhiFu;
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX,balance,chongzhiId,"4",mubiaoId,resv1,"正事多-加入企业");
            final String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            final String orderinfo = OrderInfoUtil2_0.getSign(params);
            StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        org.json.JSONObject object = new org.json.JSONObject(s);
                        String sign = object.getString("sign");
                        sign = URLEncoder.encode(sign, "UTF-8");
                        final String orderInfo = orderParam + "&" + "sign=" + sign;
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(QianDingHetongActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                fileHandler.sendMessage(msg);
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
                    Toast.makeText(QianDingHetongActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(QianDingHetongActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(QianDingHetongActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }
    private void insertHetong(final String biaoshi2,boolean hasjine) {
        String url="";
        String str="",resv1 = "";
        List<Param> param=new ArrayList<>();
        param.add(new Param("companyId", qiyeId));
        if ("yingpin".equals(biaoshi)) {
            param.add(new Param("transaction_type", "应聘企业"));
        } else {
            param.add(new Param("transaction_type", "加盟企业"));
        }
        if ("付费".equals(fufei)&&hasjine) {
            param.add(new Param("margin", feiyong));
        }else {
            param.add(new Param("margin", "0"));
        }
        final List<File> files = new ArrayList<File>();
        if ("yonghu".equals(biaoshi2)) {
            param.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
            url = FXConstant.URL_QIYE_CHUANGJIANHETONG;
            str = "userSignatureFile";
            if ("yingpin".equals(biaoshi)) {
                resv1 = "0";
            } else {
                resv1 = "1";
            }
            if (!"".equals(path1)) {
                File file1 = null;
                file1 = new File(path1);
                if (file1.exists()) {
                    files.add(file1);
                }else {
                    try {
                        file1.createNewFile();
                        files.add(file1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            param.add(new Param("resv1",resv1));
        }else if ("gongsi".equals(biaoshi2)) {
            param.add(new Param("userId", userId));
            url = FXConstant.URL_QIYE_XIUGAIANHETONG;
            str = "comSignatureFile";
            if (!"".equals(path2)) {
                File file1 = null;
                file1 = new File(path2);
                if (file1.exists()) {
                    files.add(file1);
                }else {
                    try {
                        file1.createNewFile();
                        files.add(file1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        OkHttpManager.getInstance().post(str, param, files, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("qdhtac,json",biaoshi2+",,,,,,"+jsonObject.toString());
                String code = jsonObject.getString("code");
                if ("SUCCESS".equals(code)) {
                    if ("yonghu".equals(biaoshi2)) {
                        EMClient.getInstance().groupManager().asyncApplyJoinToGroup(qiyeId, "请求加入企业", new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                sendPushMessage(managerId,null,0);
                                updateBmob(managerId);
                            }

                            @Override
                            public void onError(int i, String s) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(QianDingHetongActivity.this, "发送请求失败...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onProgress(int i, String s) {
                            }
                        });
                    } else if ("gongsi".equals(biaoshi2)) {
//                    acceptInvitation(userId,feiyong);
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    EMClient.getInstance().groupManager().asyncAddUsersToGroup(qiyeId, new String[]{userId}, new EMCallBack() {
                                        @Override
                                        public void onSuccess() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        String comName = URLDecoder.decode(companyName, "UTF-8");
//                                                        sendPushMessage(userId,comName,1);
                                                        updateHbTimes(userId);
                                                        reduceQjcount();
                                                        duanxintongzhi(userId, "【正事多】 通知:您已被同意加入" + comName + "企业");
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError(int i, String s) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    try {
                                                        String comName = URLDecoder.decode(companyName, "UTF-8");
//                                                        sendPushMessage(userId,comName,1);
                                                        updateHbTimes(userId);
                                                        reduceQjcount();
                                                        duanxintongzhi(userId, "【正事多】 通知:您已被同意加入" + comName + "企业");
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }

                                        @Override
                                        public void onProgress(int i, String s) {
                                        }
                                    });
                                } catch (final Exception e) {
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }else if ("FAIL1".equals(code)){
                    Toast.makeText(QianDingHetongActivity.this, "您已发送过请求,请勿重复发送", Toast.LENGTH_SHORT).show();
                }else if ("FAIL".equals(code)){
                    Toast.makeText(getApplicationContext(),"网络连接中断!",Toast.LENGTH_SHORT).show();
                }
                FileUtils.deleteAllFiles(files.get(0));
            }
            @Override
            public void onFailure(String errorMsg) {
                Log.e("fanhuishuju,failure",errorMsg);
                Toast.makeText(getApplicationContext(),"网络连接中断!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateHbTimes(final String uId) {
        String url = FXConstant.URL_XIUGAI_HBTIMES;
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
                Map<String, String> param = new HashMap<String, String>();
                param.put("uLoginId", uId);
                param.put("shareTimes", "1");
                param.put("homePageTimes", "1");
                param.put("dynamicTimes", "1");
                return param;
            }
        };
        MySingleton.getInstance(QianDingHetongActivity.this).addToRequestQueue(request);
    }

    private void sendPushMessage(final String hxid1, final String comName, final int type) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        if (name==null||"".equals(name)){
            name = DemoHelper.getInstance().getCurrentUsernName();
        }
        String url = FXConstant.URL_SENDPUSHMSG;
        final String finalName = name;
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
                param.put("u_id",hxid1);
                param.put("body","企业消息");
//                if (type==0) {
//                    param.put("body", finalName + "申请加入您的企业");
//                }else {
//                    param.put("body", "您已被同意加入"+comName+"企业");
//                }
                param.put("type","03");
                param.put("userId",myId);
                param.put("companyId",qiyeId);
                param.put("companyName",companyName);
                param.put("companyAdress",comAddress);
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(QianDingHetongActivity.this).addToRequestQueue(request);
    }

    private void duanxintongzhi(final String id, final String message) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(context, "同意成功！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
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
                MySingleton.getInstance(QianDingHetongActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 0:
                    if (data!=null){
                        path1 = data.getStringExtra("xdimageName");
                        tv_yonghu.setVisibility(View.INVISIBLE);
                        iv_yonghu.setVisibility(View.VISIBLE);
                        iv_yonghu.setImageURI(Uri.fromFile(new File(path1)));
                    }
                    break;
                case 1:
                    if (data!=null){
                        path2 = data.getStringExtra("jdimageName");
                        tv_gongsi.setVisibility(View.INVISIBLE);
                        iv_gongsi.setVisibility(View.VISIBLE);
                        iv_gongsi.setImageURI(Uri.fromFile(new File(path2)));
                    }
                    break;
            }
        }
    }
    private void reduceQjcount() {
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
                param.put("joinCompanyCount","-1");
                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(QianDingHetongActivity.this).addToRequestQueue(request);
    }

    private void updateBmob(final String hxid1) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                duanxintongzhi();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null) {
                    Log.e("addfriend,e", volleyError.toString());
                }
                duanxintongzhi();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("joinCompanyCount","1");
                param.put("userId",hxid1);
                return param;
            }
        };
        MySingleton.getInstance(QianDingHetongActivity.this).addToRequestQueue(request);
    }

    private void duanxintongzhi() {
        final String id = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(QianDingHetongActivity.this, "发送成功！请等待企业审核通过!", Toast.LENGTH_LONG).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(QianDingHetongActivity.this, "发送成功！请等待企业审核通过!", Toast.LENGTH_LONG).show();
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message","【正事多】 通知:用户"+id+"在正事多平台申请加入您的企业,希望您及时处理!");
                param.put("telNum",managerId);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(QianDingHetongActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    @Override
    public void updateCurrentPrice(Object success) {
        yuE = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }

    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
        }

        @Override
        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item_image, null);
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(imageView);
                // 重置ImageView宽高
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth-15, columnWidth-15);
                imageView.setLayoutParams(params);
            }else {
                imageView = (ImageView) convertView.getTag();
            }
            Glide.with(QianDingHetongActivity.this)
                    .load(getItem(position))
                    .placeholder(R.drawable.default_error)
                    .error(R.drawable.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
            return convertView;
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
