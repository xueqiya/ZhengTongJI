package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.utils.CashierInputFilter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.utils.ZhifuHelpUtils;
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

/**
 * Created by Administrator on 2016-11-02.
 */

public class ZHuZhActivity extends BaseActivity implements IUAZView ,IPriceView{
    private static final int SDK_PAY_FLAG = 1;
    private String tell,name="",pass,biaoshi,zhuanZhangId;
    private TextView tv_name;
    private TextView tv_num;
    private Button btn_commit;
    private EditText et_jine;
    private int errorTime=3;
    private IPricePresenter pricePresenter;
    private IUAZPresenter presenter=null;
    private double yuE;
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
                        Toast.makeText(ZHuZhActivity.this, "支付成功,转账成功", Toast.LENGTH_SHORT).show();
                        sendPushMessage();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ZHuZhActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
        if (zhifuZhuangtai!=null&&"转账成功".equals(zhifuZhuangtai)){
            Toast.makeText(ZHuZhActivity.this, "支付成功,转账成功", Toast.LENGTH_SHORT).show();
            sendPushMessage();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_zhuanzhang);
        WeakReference<ZHuZhActivity> reference =  new WeakReference<ZHuZhActivity>(ZHuZhActivity.this);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_num = (TextView) findViewById(R.id.tv_num);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        et_jine = (EditText) findViewById(R.id.et_jine);
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        presenter = new UAZPresenter(this,reference.get());
        pricePresenter = new PricePresenter(this,reference.get());
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        tell = this.getIntent().getStringExtra(FXConstant.JSON_KEY_HXID);
        pass = this.getIntent().getStringExtra("payPass");
        yuE = DemoApplication.getInstance().getCurrenPrice();
        zhuanZhangId = DemoHelper.getInstance().getCurrentUsernName();
        pricePresenter.updatePriceData(zhuanZhangId);
        et_jine.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters={new CashierInputFilter()};
        et_jine.setFilters(filters);
        et_jine.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable edt)
            {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });
        presenter.loadThisDetail(tell);
        String number = "";
        if (tell.length()>10) {
            number = tell.substring(0, 3) + "****" + tell.substring(7, 11);
        }
        tv_num.setText(number);
        setListener();
    }

    private void setListener() {
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = DemoApplication.getInstance().getCurrentPayPass();
                yuE = DemoApplication.getInstance().getCurrenPrice();
                zhuanZhangId = DemoHelper.getInstance().getCurrentUsernName();
                if (!"".equals(tell)) {
                    final String sum = et_jine.getText().toString().trim();
                    if (!sum.equals("")) {
                        LayoutInflater inflaterDl = LayoutInflater.from(ZHuZhActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                        final Dialog dialog = new AlertDialog.Builder(ZHuZhActivity.this,R.style.Dialog).create();
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
                                zhifu(sum);
                            }
                        });
                        re_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                rechargefromWx(sum,tell);
                            }
                        });
                        re_item5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                rechargefromZhFb(sum,tell);
                            }
                        });
                        re_item3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(ZHuZhActivity.this, "请输入金额！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ZHuZhActivity.this, "转账账户为空号！", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            LayoutInflater inflaterDl = LayoutInflater.from(ZHuZhActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(ZHuZhActivity.this,R.style.Dialog).create();
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
                    final ProgressDialog pd = new ProgressDialog(ZHuZhActivity.this);
                    pd.setMessage("正在支付...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    if (pwdView.getStrPassword().equals(pass)) {
                        dialog.dismiss();
                        String url = FXConstant.URL_ZHUANZHANG;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.d("chen", "转账" + s);
                                //这里我偷个懒直接判断有没有包含fail字符串
                                if (!s.contains("fail")) {
                                    Toast.makeText(ZHuZhActivity.this, "转账成功", Toast.LENGTH_SHORT).show();
                                    sendPushMessage();
                                } else {
                                    Toast.makeText(ZHuZhActivity.this, "转账失败", Toast.LENGTH_SHORT).show();
                                }
                                if (pd!=null&&pd.isShowing()){
                                    pd.dismiss();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(ZHuZhActivity.this, "转账失败", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<String, String>();
                                param.put("merId", tell);
                                param.put("balance", et_jine.getText().toString().trim());
                                param.put("userId", zhuanZhangId);
                                param.put("qrCode", "1");
                                return param;
                            }
                        };
                        MySingleton.getInstance(ZHuZhActivity.this).addToRequestQueue(request);
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
                            ZhifuHelpUtils.showErrorLing(ZHuZhActivity.this);
                        }else {
                            ZhifuHelpUtils.showErrorTishi(ZHuZhActivity.this,times + "",null,"000");
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
                    bs = "000";
                    startActivity(new Intent(ZHuZhActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(ZHuZhActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(ZHuZhActivity.this,R.style.Dialog).create();
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
                    rechargefromWx(sum,tell);
                }
            });
            re_item5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    rechargefromZhFb(sum,tell);
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
        MySingleton.getInstance(ZHuZhActivity.this).addToRequestQueue(request3);
    }
    private void sendPushMessage() {
        String zongjia = et_jine.getText().toString().trim();
        final double orderSu = Double.valueOf(zongjia);
        yuE = yuE - orderSu;
        DemoApplication.getApp().saveCurrentPrice(yuE);
        setResult(RESULT_OK, new Intent().putExtra("value", yuE));
        finish();
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        if (name==null||"".equals(name)){
            name = DemoHelper.getInstance().getCurrentUsernName();
        }
        final String finalName = name;
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("zhuazhaac,s",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",tell);
                param.put("body","转账消息");
                param.put("type","08");
                param.put("userId",myId);
                param.put("companyId", "0");
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                Log.e("zhuazhaac,p",param.toString());
                return param;
            }
        };
        MySingleton.getInstance(ZHuZhActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void updateThisUser(Userful user2) {
        name = user2.getName();
        tv_name.setText(name);
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

    private void rechargefromWx(final String balance1,final String uId) {
        final String mubiaoId = zhuanZhangId+"_"+"1"+"_"+uId;
        String balance = (int)(Double.parseDouble(balance1)*100)+"";
        Toast.makeText(ZHuZhActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = balance;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity","ZHuZhActivity");
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
                Toast.makeText(ZHuZhActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("body","正事多-转账");
                param.put("detail","正事多-转账");
                param.put("out_trade_no",getNowTime());
                param.put("total_fee", finalBalance);
                param.put("spbill_create_ip",getHostIP());
                param.put("attach",mubiaoId);
                return param;
            }
        };
        MySingleton.getInstance(ZHuZhActivity.this).addToRequestQueue(request);
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
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX,balance,chongzhiId,"1",uId,null,"正事多-转账");
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
                                PayTask alipay = new PayTask(ZHuZhActivity.this);
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
                    Toast.makeText(ZHuZhActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(ZHuZhActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(ZHuZhActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateCurrentPrice(Object success) {
        yuE = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }
}
