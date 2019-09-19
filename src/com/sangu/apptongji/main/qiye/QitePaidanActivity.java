package com.sangu.apptongji.main.qiye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.avtivity.WJPaActivity;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
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
import java.math.BigDecimal;
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
 * Created by Administrator on 2017-01-14.
 */

public class QitePaidanActivity extends BaseActivity implements IPriceView {
    private static final int SDK_PAY_FLAG = 1;
    private String companyId=null,orderId=null,userId=null,name=null,totalAmt=null;
    private double geren;
    private IPricePresenter pricePresenter;
    private TextView tv_paidan_name;
    private EditText et_qiye_baochou;
    private EditText et_qiye_beizhu;
    private Button btn_commit;
    private int errorTime =3;
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
                        Toast.makeText(QitePaidanActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                        paidan("0",geren+"",et_qiye_beizhu.getText().toString().trim());
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(QitePaidanActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String zhifuZhuangtai = intent.getStringExtra("zhifu");
        if (zhifuZhuangtai!=null&&"支付成功".equals(zhifuZhuangtai)){
            Toast.makeText(QitePaidanActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
            paidan("0",geren+"",et_qiye_beizhu.getText().toString().trim());
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_qiye_paidan);
        pricePresenter = new PricePresenter(this,this);
        initView();
        pricePresenter.updatePriceData(DemoApplication.getInstance().getCurrentQiYeId());
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        companyId = this.getIntent().getStringExtra("companyId");
        orderId = this.getIntent().getStringExtra("orderId");
        userId = this.getIntent().getStringExtra("userId");
        name = this.getIntent().getStringExtra("name");
        totalAmt = this.getIntent().getStringExtra("totalAmt");
        tv_paidan_name.setText(name);
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pricePresenter.updatePriceData(DemoApplication.getInstance().getCurrentQiYeId());
                if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("01")) {
                    ToastUtils.showNOrmalToast(QitePaidanActivity.this.getApplicationContext(), "您的账户已被冻结");
                    return;

                }
                final String baochou = et_qiye_baochou.getText().toString().trim();
                final String beizhu = et_qiye_beizhu.getText().toString().trim();
                if (TextUtils.isEmpty(baochou)){
                    Toast.makeText(QitePaidanActivity.this,"请先设置公司报酬，可设置为0%,或者大于100%",Toast.LENGTH_SHORT).show();
                }else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QitePaidanActivity.this);
                    builder.setMessage("确认按"+baochou+"%报酬派单么?");
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
                            if (Double.valueOf(baochou)<=100) {
                                double qiye = (100 - Double.valueOf(baochou)) / 100 * Double.valueOf(totalAmt);
                                geren = Double.valueOf(baochou) / 100 * Double.valueOf(totalAmt);
                                BigDecimal bd = new BigDecimal(geren);
                                geren = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                BigDecimal bd2 = new BigDecimal(qiye);
                                qiye = bd2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                paidan(qiye + "", geren + "", beizhu);
                            }else {
                                final String pass = DemoApplication.getInstance().getCurrentQiyePayPass();
                                final double comYue = DemoApplication.getInstance().getCurrenQiyePrice();
                                final double qiye = (Double.valueOf(baochou) - 100)/100 * Double.valueOf(totalAmt);
                                geren = Double.valueOf(totalAmt) + qiye;
                                if (pass==null||"".equals(pass)){
                                    Toast.makeText(QitePaidanActivity.this,"您还没有设置企业支付密码，请先设置支付密码！",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                LayoutInflater inflaterDl = LayoutInflater.from(QitePaidanActivity.this);
                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                                final Dialog dialog2 = new AlertDialog.Builder(QitePaidanActivity.this,R.style.Dialog).create();
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
                                        zhifu(baochou,beizhu);
                                    }
                                });
                                re_item2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                        rechargefromWx(String.valueOf(qiye),orderId);
                                    }
                                });
                                re_item5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                        rechargefromZhFb(String.valueOf(qiye),orderId);
                                    }
                                });
                                re_item3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                    }
                                });
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    private void zhifu(final String baochou, final String beizhu) {
        final String pass = DemoApplication.getInstance().getCurrentQiyePayPass();
        final double comYue = DemoApplication.getInstance().getCurrenQiyePrice();
        final double qiye = (Double.valueOf(baochou) - 100)/100 * Double.valueOf(totalAmt);
        if (pass==null||"".equals(pass)){
            ZhifuHelpUtils.showErrorMiMaSHZH(this,pass,"001");
            return;
        }
        if (pass.length()!=6||!ZhifuHelpUtils.isNumeric(pass)){
            ZhifuHelpUtils.showErrorMiMaXG(this,pass,"001");
            return;
        }
        if (errorTime<=0){
            ZhifuHelpUtils.showErrorLing(this);
            return;
        }
        geren = Double.valueOf(totalAmt) + qiye;
        LayoutInflater inflaterDl = LayoutInflater.from(QitePaidanActivity.this);
        final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
        final Dialog dialog = new AlertDialog.Builder(QitePaidanActivity.this,R.style.Dialog).create();
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
                final ProgressDialog pd = new ProgressDialog(QitePaidanActivity.this);
                pd.setMessage("正在支付...");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                if (pwdView.getStrPassword().equals(pass)) {
                    dialog.dismiss();
                    if (comYue>=qiye) {
                        fukuan(qiye, geren, beizhu);
                    }else {
                        LayoutInflater inflaterDl = LayoutInflater.from(QitePaidanActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                        final Dialog dialog1 = new AlertDialog.Builder(QitePaidanActivity.this,R.style.Dialog).create();
                        dialog1.show();
                        dialog1.getWindow().setContentView(layout);
                        dialog1.setCanceledOnTouchOutside(true);
                        RelativeLayout re_item1 = (RelativeLayout) dialog1.findViewById(R.id.re_item1);
                        RelativeLayout re_item2 = (RelativeLayout) dialog1.findViewById(R.id.re_item2);
                        RelativeLayout re_item3 = (RelativeLayout) dialog1.findViewById(R.id.re_item3);
                        RelativeLayout re_item5 = (RelativeLayout) dialog1.findViewById(R.id.re_item5);
                        re_item1.setVisibility(View.GONE);
                        TextView tv_title = (TextView) dialog1.findViewById(R.id.tv_title);
                        TextView tv_item1 = (TextView) dialog1.findViewById(R.id.tv_item1);
                        TextView tv_item2 = (TextView) dialog1.findViewById(R.id.tv_item2);
                        TextView tv_item5 = (TextView) dialog1.findViewById(R.id.tv_item5);
                        tv_title.setText("企业钱包余额不足(剩余"+comYue+"元),请您先充值或者选择其他支付方式");
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
                                dialog1.dismiss();
                                rechargefromWx(String.valueOf(qiye),orderId);
                            }
                        });
                        re_item5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                                rechargefromZhFb(String.valueOf(qiye),orderId);
                            }
                        });
                        re_item3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                    }
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
                        ZhifuHelpUtils.showErrorLing(QitePaidanActivity.this);
                    }else {
                        ZhifuHelpUtils.showErrorTishi(QitePaidanActivity.this,times + "",null,"001");
                    }
                    dialog.dismiss();
                }
            }
        });
        /**
         *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供响应
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
                bs = "001";
                startActivity(new Intent(QitePaidanActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
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
        MySingleton.getInstance(QitePaidanActivity.this).addToRequestQueue(request3);
    }

    private void initView() {
        tv_paidan_name = (TextView) findViewById(R.id.tv_paidan_name);
        et_qiye_baochou = (EditText) findViewById(R.id.et_qiye_baochou);
        et_qiye_beizhu = (EditText) findViewById(R.id.et_qiye_beizhu);
        btn_commit = (Button) findViewById(R.id.btn_commit);
    }

    private void rechargefromWx(String zongjia, String uId) {
        String chongzhiId=null;
        chongzhiId = DemoApplication.getInstance().getCurrentQiYeId();
        try {
            chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        zongjia = (int)(Double.parseDouble(zongjia)*100)+"";
        final String mubiaoId = chongzhiId+"_"+"2"+"_"+uId;
        Toast.makeText(QitePaidanActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = zongjia;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity","QitePaidanActivity");
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
                Toast.makeText(QitePaidanActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(QitePaidanActivity.this).addToRequestQueue(request);
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
        chongzhiId = DemoApplication.getInstance().getCurrentQiYeId();
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
                                PayTask alipay = new PayTask(QitePaidanActivity.this);
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
                    Toast.makeText(QitePaidanActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(QitePaidanActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(QitePaidanActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private void fukuan(final double qiye, final double geren, final String beizhu) {
        String url = FXConstant.URL_DingDan_Pay;
        StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("success")) {
                        paidan("0",geren+"",beizhu);
                    } else {
                        Toast.makeText(QitePaidanActivity.this, "付款失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(QitePaidanActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", orderId);
                params.put("userId", companyId);
                params.put("balance", qiye+"");
                return params;
            }
        };
        MySingleton.getInstance(QitePaidanActivity.this).addToRequestQueue(request1);
    }
    @Override
    public void back(View view) {
        super.back(view);
    }

    private void reduceQjcount() {
        if (companyId==null||"".equals(companyId)){
            companyId = DemoApplication.getInstance().getCurrentQiYeId();
        }
        String url = FXConstant.URL_UPDATE_UNREADQIYE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderCount","-1");
                param.put("companyId",companyId);
                return param;
            }
        };
        MySingleton.getInstance(QitePaidanActivity.this).addToRequestQueue(request);
    }

    private void tongji(final String remark, final String qiyeId, final String userId) {
        String url = FXConstant.URL_QIYE_YUANGONGTONGJI;
        StringRequest qDrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(QitePaidanActivity.this,"派单成功！",Toast.LENGTH_SHORT).show();
                reduceQjcount();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("网络连接错误",volleyError+"");
                Toast.makeText(QitePaidanActivity.this, "网络连接错误...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("companyId", qiyeId);
                params.put("userId", userId);
                params.put("orderTimes","1");
                return params;
            }
        };
        MySingleton.getInstance(QitePaidanActivity.this).addToRequestQueue(qDrequest);
    }

    private void paidan(final String qiye, final String geren, final String beizhu) {
        final String ticheng = et_qiye_baochou.getText().toString().trim();
        String url = FXConstant.URL_QIYE_PAIDAN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                sendPushMessage(userId);
                duanxintongzhi(userId,"【正事多】 通知:您有一条新的派单消息");
                updateBmob();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(QitePaidanActivity.this,"网络连接错误！",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("companyId",companyId);
                param.put("userId",userId);
                param.put("orderId",orderId);
                param.put("dispatchDesc",beizhu);
                param.put("user_amt",geren);
                param.put("company_amt",qiye);
                param.put("percent",ticheng+"%");
                Log.e("uorder,qite,re5",param.toString());
                return param;
            }
        };
        MySingleton.getInstance(QitePaidanActivity.this).addToRequestQueue(request);
    }

    private void sendPushMessage(final String hxid1) {
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
                param.put("u_id",hxid1);
                param.put("body","派单消息");
                param.put("type","04");
                param.put("userId",myId);
                param.put("companyId",companyId);
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(QitePaidanActivity.this).addToRequestQueue(request);
    }

    private void duanxintongzhi(final String id, final String message) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("短信通知","成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
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
                MySingleton.getInstance(QitePaidanActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void updateBmob() {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                tongji("1",companyId,userId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
                tongji("1",companyId,userId);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderUnReadCount","1");
                param.put("userId",userId);
                return param;
            }
        };
        MySingleton.getInstance(QitePaidanActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateCurrentPrice(Object success) {
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }
}
