package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.avtivity.WJPaActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.XiuGaiZFActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.ZhiFuSettingActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.SoundPlayUtils;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018-09-26.
 */

public class TopVipActivity extends BaseActivity{
    private static final int SDK_PAY_FLAG = 1;

    private TextView tv_top,tv_top2,tv_top3,tv_viptime;
    private int vip=0;
    private String vipLevel = "1";
    private String currentLevel;
    private String sumString="0";
    private int errorTime=3;
    private CustomProgressDialog mProgress=null;
    private IWXAPI api=null;
    private String pass;
    private Double yuE;
    private ProgressDialog pd=null;
    private ImageView image_advert;
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
//                        updatebmob();

                       UpdateMeraccountSubsidy();
                        //finish();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(TopVipActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };



    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_topvip);

        tv_top = (TextView) findViewById(R.id.tv_top);

        tv_top2 = (TextView) findViewById(R.id.tv_top2);
        tv_top3 = (TextView) findViewById(R.id.tv_top3);

        image_advert = (ImageView) findViewById(R.id.image_advert);

        tv_viptime = (TextView) findViewById(R.id.tv_viptime);
        tv_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sumString = String.valueOf(15);
                vipLevel = "1";
                if (vip == 0){

                    TopUPWithSum();

                }else {
                    Toast.makeText(TopVipActivity.this, "请在会员到期后续费", Toast.LENGTH_SHORT).show();
                }

            }
        });


        tv_top2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sumString = String.valueOf(36);
                vipLevel = "2";
                if (vip == 0 || currentLevel.equals("1")){

                    TopUPWithSum();

                }else {
                    Toast.makeText(TopVipActivity.this, "请在会员到期后续费", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tv_top3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sumString = String.valueOf(66);
                vipLevel = "3";
                if (vip == 0 || currentLevel.equals("1") || currentLevel.equals("2")){

                    TopUPWithSum();

                }else {
                    Toast.makeText(TopVipActivity.this, "请在会员到期后续费", Toast.LENGTH_SHORT).show();
                }

            }
        });

        WeakReference<TopVipActivity> reference =  new WeakReference<TopVipActivity>(TopVipActivity.this);
        mProgress = CustomProgressDialog.createDialog(reference.get());

        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);

        QueryUserVipInfo();

        GetNotice();




    }


    private void UpdateMeraccountSubsidy(){

        QueryUserVipInfo();

        String url = FXConstant.URL_UPDATEZHHU;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);

                if (object.getString("code").equals("SUCCESS")){

                    SoundPlayUtils.play(2);
                    LayoutInflater inflaterDl = LayoutInflater.from(TopVipActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                    final Dialog dialog = new AlertDialog.Builder(TopVipActivity.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    TextView tv_title1 = (TextView) layout.findViewById(R.id.tv_title1);
                    TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
                    TextView tv_yue = (TextView) layout.findViewById(R.id.tv_yue);
                    TextView tv_pushclick = (TextView) layout.findViewById(R.id.tv_pushclick);
                    tv_pushclick.setVisibility(View.VISIBLE);
                    TextPaint tp = tv_title.getPaint();
                    tp.setFakeBoldText(true);
                    tv_title.setText("1元");
                    tv_title1.setText("充值成功");
                    tv_yue.setText("正事多 补贴红包");

                    tv_pushclick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                            Intent intent = new Intent(TopVipActivity.this,RedPromoteActivity.class);

                            startActivityForResult(intent,0);

                        }
                    });

                }else {

                    Toast.makeText(TopVipActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                    finish();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(TopVipActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                finish();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("merId",DemoHelper.getInstance().getCurrentUsernName());
                param.put("redPromoteBalance","1");
                param.put("redPromoteCount","0");
                param.put("redPromoteType","0");

                return param;
            }

        };

        MySingleton.getInstance(TopVipActivity.this).addToRequestQueue(request);

    }


    private void QueryUserVipInfo(){

        String url = FXConstant.URL_Get_UserInfo+DemoHelper.getInstance().getCurrentUsernName();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                com.alibaba.fastjson.JSONObject userInfo = object.getJSONObject("userInfo");

                vip = userInfo.getIntValue("vip");

                currentLevel = userInfo.getString("vipLevel");

                if (vip != 0){

                    tv_viptime.setText("剩余"+vip+"天");

                }else {

                    tv_viptime.setText("暂未开通");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        MySingleton.getInstance(TopVipActivity.this).addToRequestQueue(request);
    }


    //查询广告
    private void GetNotice(){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");
                SharedPreferences sp = getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);

                String type = object1.getString("type2");

                if (type.equals("1")){

                    String url = FXConstant.URL_ADVERTURL+object1.getString("image1");

                    ImageLoader.getInstance().displayImage(url,image_advert);

                    image_advert.setVisibility(View.VISIBLE);

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
                param.put("deviceType","payadvert");
                return param;
            }
        };

        MySingleton.getInstance(TopVipActivity.this).addToRequestQueue(request);

    }

    private void TopUPWithSum (){

        LayoutInflater inflaterDl = LayoutInflater.from(TopVipActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(TopVipActivity.this,R.style.Dialog).create();
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
                zhifu();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                rechargefromWx();
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                rechargefromZhFb();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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

    private void showErrorMiMaSHZH() {
        LayoutInflater inflater1 = LayoutInflater.from(TopVipActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(TopVipActivity.this,R.style.Dialog).create();
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
                startActivity(new Intent(TopVipActivity.this,ZhiFuSettingActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",pass));
            }
        });
    }

    private void showErrorMiMaXG() {
        LayoutInflater inflater1 = LayoutInflater.from(TopVipActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(TopVipActivity.this,R.style.Dialog).create();
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
                startActivity(new Intent(TopVipActivity.this, XiuGaiZFActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",pass));
            }
        });
    }

    private void showErrorLing() {
        LayoutInflater inflaterDl = LayoutInflater.from(TopVipActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
        final Dialog dialog2 = new AlertDialog.Builder(TopVipActivity.this,R.style.Dialog).create();
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

    private void zhifu() {

        yuE = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();

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

        final double orderSu = Double.valueOf(sumString);

        Double d = yuE - orderSu;

        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {

            if (d >= 0) {

                LayoutInflater inflaterDl = LayoutInflater.from(TopVipActivity.this);
                final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
                final Dialog dialog = new AlertDialog.Builder(TopVipActivity.this, R.style.Dialog).create();
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
                        pd = new ProgressDialog(TopVipActivity.this);
                        pd.setMessage("正在支付...");
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();
                        if (pwdView.getStrPassword().equals(pass)) {
                            dialog.dismiss();

                            String url = FXConstant.URL_PAYVIP;
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    if (mProgress!=null&&mProgress.isShowing()){
                                        mProgress.dismiss();
                                    }

                                    pd.dismiss();
                                    UpdateMeraccountSubsidy();
//                                    Toast.makeText(TopVipActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
//                                     finish();
                                    //  Log.e("OfflineOrderActivity", "转账成功");

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(TopVipActivity.this, "网络不稳定", Toast.LENGTH_SHORT).show();
                                    if (mProgress!=null&&mProgress.isShowing()){
                                        mProgress.dismiss();
                                    }
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> param = new HashMap<String, String>();
                                    param.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                                    param.put("vip", sumString);
                                    param.put("vipLevel", vipLevel);
                                    return param;
                                }
                            };
                            MySingleton.getInstance(TopVipActivity.this).addToRequestQueue(request);

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
                        startActivity(new Intent(TopVipActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",DemoHelper.getInstance().getCurrentUsernName()));
                    }
                });

            }else {

                LayoutInflater inflaterDl = LayoutInflater.from(TopVipActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                final Dialog dialog = new AlertDialog.Builder(TopVipActivity.this,R.style.Dialog).create();
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
                        rechargefromWx();

                    }
                });
                re_item5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        rechargefromZhFb();

                    }
                });
                re_item3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
            }


        }else {

            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");

        }

    }


    private void showErrorTishi(String s) {
        LayoutInflater inflater1 = LayoutInflater.from(TopVipActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(TopVipActivity.this,R.style.Dialog).create();
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
                startActivity(new Intent(TopVipActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",DemoHelper.getInstance().getCurrentUsernName()));
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

        MySingleton.getInstance(TopVipActivity.this).addToRequestQueue(request3);

    }


    private void rechargefromZhFb(){
        String chongzhiId=null;

        chongzhiId = DemoHelper.getInstance().getCurrentUsernName();

        try {
            chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String balance = sumString;
        if (!balance.equals("")) {
            String url = FXConstant.URL_ZhiFu;
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX, balance,chongzhiId,"8",vipLevel,null,"正事多-充值VIP");
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
                                PayTask alipay = new PayTask(TopVipActivity.this);
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
                    Toast.makeText(TopVipActivity.this,"网络不稳定，请稍后重试",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(TopVipActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(TopVipActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        String zhifuZhuangtai = intent.getStringExtra("zhifu");
        if (zhifuZhuangtai!=null&&"支付成功".equals(zhifuZhuangtai)){

            UpdateMeraccountSubsidy();

        }

    }

    private void rechargefromWx() {
        String balance = sumString;
        balance = (int)(Double.parseDouble(balance)*100)+"";
        Toast.makeText(TopVipActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = balance;
        final String mubiaoId = DemoHelper.getInstance().getCurrentUsernName() + "_" + "8" + "_" + vipLevel;

        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity","TopVipActivity");
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
                Toast.makeText(TopVipActivity.this,"网络不稳定",Toast.LENGTH_SHORT).show();
                if (volleyError != null) {
                    Log.e("ChongZhiActivity",volleyError.getMessage());
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("body","正事多-充值VIP");
                param.put("detail","正事多-充值VIP");
                param.put("out_trade_no",getNowTime());
                param.put("total_fee", finalBalance);
                param.put("spbill_create_ip",getHostIP());
                param.put("attach",mubiaoId);
                return param;
            }
        };
        MySingleton.getInstance(TopVipActivity.this).addToRequestQueue(request);
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

}
