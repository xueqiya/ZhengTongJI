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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.utils.CashierInputFilter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.utils.ZhifuHelpUtils;
import com.sangu.apptongji.main.widget.FXPopWindow;
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
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by user on 2016/9/8.
 */

public class ChongZhiActivity extends BaseActivity implements IPriceView{
    private static final int SDK_PAY_FLAG = 1;
    private String pass=null,biaoshi=null,biaoshi2=null;
    private double prices= 0.00;
    private int errorTime=3;
    private IPricePresenter pricePresenter;
    private ImageView ivBack=null;
    private Button btnNext=null;
    private LinearLayout ll_yue=null;
    private EditText etYue=null;
    private EditText etJine=null;
    private IWXAPI api=null;
    private boolean hasChange=false;
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
                        Toast.makeText(ChongZhiActivity.this,"充值成功",Toast.LENGTH_SHORT).show();
                        String chongzhi = etJine.getText().toString().trim();
                        double yue = prices + Double.parseDouble(chongzhi);
                        DemoApplication.getApp().saveCurrentPrice(yue);
                        Toast.makeText(ChongZhiActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ChongZhiActivity.this, "支付失败"+resultStatus, Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

//    private void updatebmob() {
//        WeakReference<ChongZhiActivity> reference = new WeakReference<ChongZhiActivity>(ChongZhiActivity.this);
//        String id = DemoHelper.getInstance().getCurrentUsernName();
//        String name = DemoApplication.getInstance().getCurrentUser().getName();
//        RechargeRecord recharge = new RechargeRecord();
//        recharge.setUserId(id);
//        recharge.setUserName(TextUtils.isEmpty(name)?id:name);
//        recharge.setAmount(etJine.getText().toString().trim());
//        recharge.save(reference.get(),new SaveListener() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(ChongZhiActivity.this,"充值成功",Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                if (s!=null)
//                    Log.e("chongzhi,s",s);
//            }
//        });
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String zhifuZhuangtai = intent.getStringExtra("zhifu");
        if (zhifuZhuangtai!=null){
            Toast.makeText(ChongZhiActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            String chongzhi = etJine.getText().toString().trim();
            double yue = prices + Double.parseDouble(chongzhi);
            DemoApplication.getApp().saveCurrentPrice(yue);
//            updatebmob();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chongzhi);
        pricePresenter = new PricePresenter(this,this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ll_yue = (LinearLayout) findViewById(R.id.ll_yue);
        btnNext = (Button) findViewById(R.id.btn_xiayibu);
        etJine = (EditText) findViewById(R.id.et_jine);
        etYue = (EditText) findViewById(R.id.et_yue);
        etJine.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters={new CashierInputFilter()};
        etJine.setFilters(filters);
        etJine.addTextChangedListener(new TextWatcher(){
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
        Intent intent = this.getIntent();
        biaoshi = intent.getStringExtra("biaoshi");
        biaoshi2 = intent.getStringExtra("biaoshi2");
        if ("01".equals(biaoshi)){
            pass = DemoApplication.getInstance().getCurrentQiyePayPass();
        }else {
            pass = DemoApplication.getInstance().getCurrentPayPass();
        }
        prices = DemoApplication.getInstance().getCurrenPrice();
//        Log.d("充值传过来的数据","price="+prices+"pass="+pass+"biaoshi="+biaoshi);
        initViews();
        if ("01".equals(biaoshi2)){
            ll_yue.setVisibility(View.VISIBLE);
            etYue.setEnabled(false);
            etYue.setText(prices+"元");
            initView2();
        }else {
            initView();
        }
    }

    private void initView2() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserPermissionUtil.getUserPermission(ChongZhiActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "6", new UserPermissionUtil.UserPermissionListener() {
                    @Override
                    public void onAllow() {

                        final String payPass = DemoApplication.getInstance().getCurrentPayPass();
                        if (payPass==null||"".equals(payPass)){
                            ZhifuHelpUtils.showErrorMiMaSHZH(ChongZhiActivity.this,payPass,"000");
                            return;
                        }
                        if (payPass.length()!=6||!ZhifuHelpUtils.isNumeric(payPass)){
                            ZhifuHelpUtils.showErrorMiMaXG(ChongZhiActivity.this,payPass,"000");
                            return;
                        }
                        if (errorTime<=0){
                            ZhifuHelpUtils.showErrorLing(ChongZhiActivity.this);
                            return;
                        }
                        String sum = etJine.getText().toString().trim();
                        if (!sum.equals("")) {
                            if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
                                final double orderSu = Double.valueOf(sum);
                                final Double d = prices - orderSu;
                                if (d >= 0) {
                                    LayoutInflater inflaterDl = LayoutInflater.from(ChongZhiActivity.this);
                                    final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
                                    final Dialog dialog = new AlertDialog.Builder(ChongZhiActivity.this).create();
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
                                            final ProgressDialog pd = new ProgressDialog(ChongZhiActivity.this);
                                            pd.setMessage("正在充值...");
                                            pd.setCanceledOnTouchOutside(false);
                                            pd.show();
                                            if (pwdView.getStrPassword().equals(payPass)) {
                                                dialog.dismiss();
                                                String url = FXConstant.URL_ZHUANZHANG;
                                                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String s) {
                                                        Toast.makeText(ChongZhiActivity.this,"充值成功",Toast.LENGTH_SHORT).show();
                                                        DemoApplication.getInstance().saveCurrentPrice(d);
                                                        setResult(RESULT_OK);
                                                        finish();
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError volleyError) {
                                                        dialog.dismiss();
                                                        Toast.makeText(ChongZhiActivity.this,"充值失败",Toast.LENGTH_SHORT).show();
                                                    }
                                                }){
                                                    @Override
                                                    protected Map<String, String> getParams() throws AuthFailureError {
                                                        Map<String,String> param = new HashMap<>();
                                                        param.put("merId",DemoApplication.getInstance().getCurrentQiYeId());
                                                        param.put("balance",etJine.getText().toString().trim());
                                                        param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                                        param.put("qrCode","1");
                                                        return param;
                                                    }
                                                };
                                                MySingleton.getInstance(ChongZhiActivity.this).addToRequestQueue(request);
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
                                                    ZhifuHelpUtils.showErrorLing(ChongZhiActivity.this);
                                                }else {
                                                    ZhifuHelpUtils.showErrorTishi(ChongZhiActivity.this,times + "",null,"000");
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
                                            startActivity(new Intent(ChongZhiActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
                                        }
                                    });
                                } else {
                                    Toast.makeText(ChongZhiActivity.this, "账户余额不足，请充值！", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                ToastUtils.showNOrmalToast(ChongZhiActivity.this.getApplicationContext(), "您的账户已被冻结");
                            }
                        }else {
                            Toast.makeText(ChongZhiActivity.this, "请输入金额！", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onBan() {
                        Toast.makeText(ChongZhiActivity.this, "您的账户已被禁止充值,有问题联系客服", Toast.LENGTH_SHORT).show();
                    }
                });

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
        MySingleton.getInstance(ChongZhiActivity.this).addToRequestQueue(request3);
    }

    private void initViews() {
        final TextView ivAdd = (TextView) findViewById(R.id.iv_add);
        ivAdd.setVisibility(View.INVISIBLE);
        final FXPopWindow fxPopWindow=new FXPopWindow(this,R.layout.popupwindow_more,new FXPopWindow.OnItemClickListener(){
            @Override
            public void onClick(int position) {
                switch (position){
                    //设置支付密码
                    case 0:
                        Intent intent3 = new Intent(ChongZhiActivity.this,ZhiFuSettingActivity.class);
                        intent3.putExtra("payPass",pass);
                        intent3.putExtra("biaoshi",biaoshi);
                        startActivity(intent3);
                        break;
                    //修改支付密码
                    case 1:
                        Intent intent = new Intent(ChongZhiActivity.this,XiuGaiZFActivity.class);
                        intent.putExtra("zhifupass",pass);
                        intent.putExtra("biaoshi",biaoshi);
                        startActivity(intent);
                        break;
                    //忘记密码
                    case 2:
                        startActivity(new Intent(ChongZhiActivity.this, WJPaActivity.class).putExtra("biaoshi",biaoshi));
                        break;
                    //帮助及反馈
                    case 3:
                        Toast.makeText(ChongZhiActivity.this,"有疑问请致电客服,客服电话：400-0010084",Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fxPopWindow.showPopupWindow(ivAdd);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ckeckChange();
            }
        });
    }

    private void duanxintongzhi(final String price) {
        final String id = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("chongzhiActivity","短信通知成功");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("chongzhiActivity","服务器繁忙");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message","【正事多】通知:用户"+id+"在正事多平台充值"+price+"元!");
                param.put("telNum","13513895563");
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(ChongZhiActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void initView() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String balance = etJine.getText().toString().trim();
                if (TextUtils.isEmpty(balance)||Double.parseDouble(balance)==0){
                    Toast.makeText(ChongZhiActivity.this, "请输入充值金额！", Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflaterDl = LayoutInflater.from(ChongZhiActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                final Dialog dialog = new AlertDialog.Builder(ChongZhiActivity.this).create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.setCanceledOnTouchOutside(true);
                RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                tv_item1.setText("从支付宝充值");
                tv_item2.setText("从微信充值");
                re_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        rechargefromZhFb();
                    }
                });
                re_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        rechargefromWx();
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
    private void rechargefromWx() {
        String balance = etJine.getText().toString().trim();
        balance = (int)(Double.parseDouble(balance)*100)+"";
        Toast.makeText(ChongZhiActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = balance;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity","ChongZhiActivity");
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
                Toast.makeText(ChongZhiActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
                if (volleyError != null) {
                    Log.e("ChongZhiActivity",volleyError.getMessage());
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("body","正事多-钱包充值");
                param.put("detail","正事多-钱包充值");
                param.put("out_trade_no",getNowTime());
                param.put("total_fee", finalBalance);
                param.put("spbill_create_ip",getHostIP());
                param.put("attach",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(ChongZhiActivity.this).addToRequestQueue(request);
    }

    private String getNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

    private void rechargefromZhFb(){
        String chongzhiId=null;
        if ("00".equals(biaoshi)) {
            chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        }else {
            chongzhiId = DemoApplication.getInstance().getCurrentQiYeId();
        }
        try {
            chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String balance = etJine.getText().toString().trim();
        if (!balance.equals("")) {
            String url = FXConstant.URL_ZhiFu;
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX, balance,chongzhiId,null,null,null,"正事多-钱包充值");
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
                                PayTask alipay = new PayTask(ChongZhiActivity.this);
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
                    Toast.makeText(ChongZhiActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(ChongZhiActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(ChongZhiActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void back(View view){
        ckeckChange();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            ckeckChange();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void ckeckChange(){
        if( hasChange){
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void updateCurrentPrice(Object success) {
        prices = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }
}
