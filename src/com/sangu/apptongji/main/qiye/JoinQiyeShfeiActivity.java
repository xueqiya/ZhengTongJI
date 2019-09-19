package com.sangu.apptongji.main.qiye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.widget.EaseSwitchButton;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.avtivity.ChongZhiActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.WJPaActivity;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
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
 * Created by Administrator on 2017-01-17.
 */

public class JoinQiyeShfeiActivity extends BaseActivity implements IQiYeDetailView, IPriceView {
    private TextView tv_yincang = null;
    private TextView tv_zhaopin = null;
    private TextView tv_jiameng = null;
    private RelativeLayout re_qiye_yincang = null;
    private RelativeLayout re_qiye_zhaopin = null;
    private RelativeLayout re_qiye_jiameng = null;
    private IQiYeInfoPresenter qiYeInfoPresenter = null;
    private IPricePresenter pricePresenter = null;
    private String companyName = null, companId = null, recruitImages = null, recruitBody = null, recruitMoney = null, joinImages = null, joinBody = null, joinMoney = null, resv1 = null;
    private static final int SDK_PAY_FLAG = 1;
    private Button btn_tiqu = null;
    private RelativeLayout rl_switch_hongbao = null;
    private RelativeLayout rl_hongbao_yue = null;
    private EaseSwitchButton switch_hongbao = null;
    private EditText et_hongbao_jg = null, et_each_jg = null;
    private ImageView iv12, iv13;
    private TextView tv_hongbao_yue = null;
    private TextView tv_hongbao_set = null;
    private TextView tv_hongbao_title = null;
    private TextView tv_each_jine = null;
    private TextView tv_wancheng = null;
    private String hbJinE = null, eachJinE = null, pass = null, biaoshi;
    private double yuE;
    private IWXAPI api = null;
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
                        Toast.makeText(JoinQiyeShfeiActivity.this, "支付成功,设置成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(JoinQiyeShfeiActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
        if (zhifuZhuangtai != null && "支付成功".equals(zhifuZhuangtai)) {
            Toast.makeText(JoinQiyeShfeiActivity.this, "支付成功,设置成功", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_shoufei_join);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constant.APP_ID);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        qiYeInfoPresenter = new QiYeInfoPresenter(this, this);
        pricePresenter = new PricePresenter(this, this);
        companyName = this.getIntent().getStringExtra("companyName");
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        companId = DemoApplication.getInstance().getCurrentQiYeId();
        iv12 = (ImageView) findViewById(R.id.iv12);
        iv13 = (ImageView) findViewById(R.id.iv13);
        tv_yincang = (TextView) findViewById(R.id.tv_yincang);
        tv_zhaopin = (TextView) findViewById(R.id.tv_zhaopin);
        tv_jiameng = (TextView) findViewById(R.id.tv_jiameng);
        tv_wancheng = (TextView) findViewById(R.id.tv_wancheng);
        re_qiye_yincang = (RelativeLayout) findViewById(R.id.re_qiye_yincang);
        re_qiye_zhaopin = (RelativeLayout) findViewById(R.id.re_qiye_zhaopin);
        re_qiye_jiameng = (RelativeLayout) findViewById(R.id.re_qiye_jiameng);
        tv_wancheng.setVisibility(View.GONE);
        et_hongbao_jg = (EditText) findViewById(R.id.et_hongbao_jg);
        et_each_jg = (EditText) findViewById(R.id.et_each_jg);
        rl_hongbao_yue = (RelativeLayout) findViewById(R.id.rl_hongbao_yue);
        rl_switch_hongbao = (RelativeLayout) findViewById(R.id.rl_switch_hongbao);
        switch_hongbao = (EaseSwitchButton) findViewById(R.id.switch_hongbao);
        btn_tiqu = (Button) findViewById(R.id.btn_tiqu);
        tv_each_jine = (TextView) findViewById(R.id.tv_each_jine);
        tv_hongbao_yue = (TextView) findViewById(R.id.tv_hongbao_yue);
        tv_hongbao_set = (TextView) findViewById(R.id.tv_hongbao_set);
        tv_hongbao_title = (TextView) findViewById(R.id.tv_hongbao_title);
        pass = DemoApplication.getInstance().getCurrentQiyePayPass();
        yuE = DemoApplication.getInstance().getCurrenQiyePrice();
        setlistener();
        if ("00".equals(biaoshi)) {
            re_qiye_zhaopin.setVisibility(View.GONE);
            re_qiye_jiameng.setVisibility(View.GONE);
            re_qiye_yincang.setVisibility(View.VISIBLE);
        } else {
            re_qiye_zhaopin.setVisibility(View.VISIBLE);
            re_qiye_jiameng.setVisibility(View.VISIBLE);
            re_qiye_yincang.setVisibility(View.GONE);
            iv12.setImageResource(R.drawable.bianhao_one);
            iv13.setImageResource(R.drawable.bianhao_two);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        qiYeInfoPresenter.loadQiYeInfo(companId);
        pricePresenter.updatePriceData(companId);
    }

    private void setlistener() {
        re_qiye_yincang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(resv1)) {
                    //隐藏
                    LayoutInflater inflater4 = LayoutInflater.from(JoinQiyeShfeiActivity.this);
                    RelativeLayout layout4 = (RelativeLayout) inflater4.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog4 = new AlertDialog.Builder(JoinQiyeShfeiActivity.this, R.style.Dialog).create();
                    dialog4.show();
                    dialog4.getWindow().setContentView(layout4);
                    dialog4.setCanceledOnTouchOutside(true);
                    dialog4.setCancelable(true);
                    TextView title_tv4 = (TextView) layout4.findViewById(R.id.title_tv);
                    Button btnCancel4 = (Button) layout4.findViewById(R.id.btn_cancel);
                    final Button btnOK4 = (Button) layout4.findViewById(R.id.btn_ok);
                    btnOK4.setText("确定");
                    btnCancel4.setText("取消");
                    title_tv4.setText("确定不隐藏企业人数吗？");
                    btnCancel4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog4.dismiss();
                        }
                    });
                    btnOK4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog4.dismiss();
                            setYOrNrenshu("1");
                        }
                    });
                } else {
                    LayoutInflater inflater4 = LayoutInflater.from(JoinQiyeShfeiActivity.this);
                    RelativeLayout layout4 = (RelativeLayout) inflater4.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog4 = new AlertDialog.Builder(JoinQiyeShfeiActivity.this, R.style.Dialog).create();
                    dialog4.show();
                    dialog4.getWindow().setContentView(layout4);
                    dialog4.setCanceledOnTouchOutside(true);
                    dialog4.setCancelable(true);
                    TextView title_tv4 = (TextView) layout4.findViewById(R.id.title_tv);
                    Button btnCancel4 = (Button) layout4.findViewById(R.id.btn_cancel);
                    final Button btnOK4 = (Button) layout4.findViewById(R.id.btn_ok);
                    btnOK4.setText("确定");
                    btnCancel4.setText("取消");
                    title_tv4.setText("确定隐藏企业人数吗？");
                    btnCancel4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog4.dismiss();
                        }
                    });
                    btnOK4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog4.dismiss();
                            setYOrNrenshu("0");
                        }
                    });
                }
            }
        });
        re_qiye_zhaopin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JoinQiyeShfeiActivity.this, SetJoinQiyeActivity.class).putExtra("companId", companId).putExtra("companyName", companyName)
                        .putExtra("biaoshi", "0").putExtra("images", recruitImages).putExtra("body", recruitBody).putExtra("feiyong", recruitMoney));
            }
        });
        re_qiye_jiameng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JoinQiyeShfeiActivity.this, SetJoinQiyeActivity.class).putExtra("companId", companId).putExtra("companyName", companyName)
                        .putExtra("biaoshi", "1").putExtra("images", joinImages).putExtra("body", joinBody).putExtra("feiyong", joinMoney));
            }
        });
        et_hongbao_jg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_hongbao_jg.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        et_each_jg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_each_jg.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
        btn_tiqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hbJinE == null || "".equals(hbJinE) || Double.parseDouble(hbJinE) == 0) {
                    Toast.makeText(JoinQiyeShfeiActivity.this, "您还没有设置分享红包！", Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater1 = LayoutInflater.from(JoinQiyeShfeiActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog dialog1 = new AlertDialog.Builder(JoinQiyeShfeiActivity.this, R.style.Dialog).create();
                dialog1.show();
                dialog1.getWindow().setContentView(layout1);
                dialog1.setCanceledOnTouchOutside(true);
                dialog1.setCancelable(true);
                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                btnOK1.setText("确定");
                btnCancel1.setText("取消");
                title_tv1.setText("确定提取分享红包吗");
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
                        tiquhongbao();
                    }
                });
            }
        });
        tv_wancheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pricePresenter.updatePriceData(companId);
                if (switch_hongbao.isSwitchOpen()) {
                    pass = DemoApplication.getInstance().getCurrentQiyePayPass();
                    yuE = DemoApplication.getInstance().getCurrenQiyePrice();
                    if (pass == null || "".equals(pass)) {
                        Toast.makeText(JoinQiyeShfeiActivity.this, "您还没有设置支付密码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LayoutInflater inflater1 = LayoutInflater.from(JoinQiyeShfeiActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(JoinQiyeShfeiActivity.this, R.style.Dialog).create();
                    dialog1.show();
                    dialog1.getWindow().setContentView(layout1);
                    dialog1.setCanceledOnTouchOutside(true);
                    dialog1.setCancelable(true);
                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                    btnOK1.setText("确定");
                    btnCancel1.setText("取消");
                    title_tv1.setText("确定提交吗");
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
                            final String hb_jine = et_hongbao_jg.getText().toString().trim();
                            String each_jine = et_each_jg.getText().toString().trim();
                            if (!TextUtils.isEmpty(each_jine) && each_jine.startsWith("￥")) {
                                each_jine = each_jine.substring(1, each_jine.length());
                            }
                            if (!TextUtils.isEmpty(hb_jine) && !TextUtils.isEmpty(each_jine)) {
                                if (Double.parseDouble(hb_jine) < Double.parseDouble(each_jine)) {
                                    Toast.makeText(JoinQiyeShfeiActivity.this, "单次分享金额不能大于红包金额！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if ((Double.parseDouble(hb_jine) * 100) % (Double.parseDouble(each_jine) * 100) != 0) {
                                    Toast.makeText(JoinQiyeShfeiActivity.this, "单次分享金额必须能整除红包金额！", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                final String finalEach_jine = each_jine;
                                if (!TextUtils.isEmpty(hb_jine)) {
                                    if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
                                        final double orderSu = Double.valueOf(hb_jine);
                                        Double d = yuE - orderSu;
                                        if (d >= 0) {
                                            LayoutInflater inflaterDl = LayoutInflater.from(JoinQiyeShfeiActivity.this);
                                            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
                                            final Dialog dialog = new AlertDialog.Builder(JoinQiyeShfeiActivity.this, R.style.Dialog).create();
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
                                                    final ProgressDialog pd = new ProgressDialog(JoinQiyeShfeiActivity.this);
                                                    pd.setMessage("正在支付...");
                                                    pd.setCanceledOnTouchOutside(false);
                                                    pd.show();
                                                    if (pwdView.getStrPassword().equals(pass)) {
                                                        dialog.dismiss();
                                                        String url = FXConstant.URL_SHEZHI_QIYEFXHONGBAO;
                                                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                Log.e("joinqiye,s", s);
                                                                if (pd != null && pd.isShowing()) {
                                                                    pd.dismiss();
                                                                }
                                                                Toast.makeText(JoinQiyeShfeiActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                                                                setResult(RESULT_OK);
                                                                finish();
                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError volleyError) {
                                                                dialog.dismiss();
                                                                Toast.makeText(JoinQiyeShfeiActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }) {
                                                            @Override
                                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                                Map<String, String> param = new HashMap<String, String>();
                                                                param.put("userId", companId);
                                                                param.put("Amount", hb_jine);
                                                                param.put("singleShare", finalEach_jine);
                                                                return param;
                                                            }
                                                        };
                                                        MySingleton.getInstance(JoinQiyeShfeiActivity.this).addToRequestQueue(request);
                                                    } else {
                                                        pd.dismiss();
                                                        dialog.dismiss();
                                                        Toast.makeText(JoinQiyeShfeiActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();
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
                                                    startActivity(new Intent(JoinQiyeShfeiActivity.this, WJPaActivity.class).putExtra("biaoshi", bs));
                                                }
                                            });
                                        } else {
                                            LayoutInflater inflaterDl = LayoutInflater.from(JoinQiyeShfeiActivity.this);
                                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
                                            final Dialog dialog = new AlertDialog.Builder(JoinQiyeShfeiActivity.this, R.style.Dialog).create();
                                            dialog.show();
                                            dialog.getWindow().setContentView(layout);
                                            dialog.setCanceledOnTouchOutside(true);
                                            RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                                            RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                                            RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                                            RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                                            RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
                                            re_item4.setVisibility(View.VISIBLE);
                                            re_item5.setVisibility(View.VISIBLE);
                                            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                                            TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                                            TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                                            TextView tv_item4 = (TextView) dialog.findViewById(R.id.tv_item4);
                                            TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
                                            tv_title.setText("企业账户余额不足(剩余" + yuE + "元),请您先充值或者选择其他支付方式");
                                            tv_item1.setText("支付宝支付");
                                            tv_item2.setText("微 信 支 付");
                                            tv_item4.setText("钱包余额充值");
                                            tv_item5.setText("直 接 充 值");
                                            re_item1.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    rechargefromZhFb(hb_jine, finalEach_jine);
                                                }
                                            });
                                            re_item2.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    rechargefromWx(hb_jine, finalEach_jine);
                                                }
                                            });
                                            re_item5.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    Intent intent2 = new Intent(JoinQiyeShfeiActivity.this, ChongZhiActivity.class);
                                                    intent2.putExtra("papass", pass);
                                                    intent2.putExtra("price", yuE);
                                                    intent2.putExtra("biaoshi", "01");
                                                    intent2.putExtra("biaoshi2", "00");
                                                    startActivity(intent2);
                                                }
                                            });
                                            re_item4.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    dialog.dismiss();
                                                    Intent intent2 = new Intent(JoinQiyeShfeiActivity.this, ChongZhiActivity.class);
                                                    intent2.putExtra("papass", pass);
                                                    intent2.putExtra("price", yuE);
                                                    intent2.putExtra("biaoshi", "01");
                                                    intent2.putExtra("biaoshi2", "01");
                                                    startActivity(intent2);
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
                                        ToastUtils.showNOrmalToast(JoinQiyeShfeiActivity.this.getApplicationContext(), "您的账户已被冻结");
                                    }
                                } else {
                                    Toast.makeText(JoinQiyeShfeiActivity.this, "请输入金额！", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(JoinQiyeShfeiActivity.this, "金额不能为空！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        rl_switch_hongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switch_hongbao.isSwitchOpen()) {
                    if (hbJinE != null && !"".equals(hbJinE) && Double.parseDouble(hbJinE) > 0) {
                        LayoutInflater inflater1 = LayoutInflater.from(JoinQiyeShfeiActivity.this);
                        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog1 = new AlertDialog.Builder(JoinQiyeShfeiActivity.this, R.style.Dialog).create();
                        dialog1.show();
                        dialog1.getWindow().setContentView(layout1);
                        dialog1.setCanceledOnTouchOutside(true);
                        dialog1.setCancelable(true);
                        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                        btnOK1.setText("确定");
                        btnCancel1.setText("取消");
                        title_tv1.setText("提取出红包余额后才能关闭分享红包,确定提取吗");
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
                                tiquhongbao();
                            }
                        });
                    } else {
                        switch_hongbao.closeSwitch();
                        rl_hongbao_yue.setVisibility(View.GONE);
                        tv_hongbao_yue.setTextColor(Color.rgb(170, 170, 170));
                        tv_hongbao_set.setTextColor(Color.rgb(170, 170, 170));
                        tv_hongbao_title.setTextColor(Color.rgb(170, 170, 170));
                        tv_each_jine.setTextColor(Color.rgb(170, 170, 170));
                    }
                } else {
                    switch_hongbao.openSwitch();
                    et_hongbao_jg.setEnabled(true);
                    et_each_jg.setEnabled(true);
                    rl_hongbao_yue.setVisibility(View.VISIBLE);
                    tv_hongbao_yue.setTextColor(Color.BLACK);
                    tv_hongbao_set.setTextColor(Color.BLACK);
                    tv_hongbao_title.setTextColor(Color.BLACK);
                    tv_each_jine.setTextColor(Color.BLACK);
                }
            }
        });
    }

    private void setYOrNrenshu(final String str) {
        String url = FXConstant.URL_UPDATE_QIYE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("SUCCESS")) {
                        Toast.makeText(JoinQiyeShfeiActivity.this, "设置成功！", Toast.LENGTH_SHORT).show();
                        if ("0".equals(str)) {
                            tv_yincang.setText("隐藏");
                        } else {
                            tv_yincang.setText("不隐藏");
                        }
                    } else {
                        Toast.makeText(JoinQiyeShfeiActivity.this, "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(JoinQiyeShfeiActivity.this, "网络连接错误！请稍后再试...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("resv1", str);
                param.put("companyName", companyName);
                param.put("companyId", companId);
                return param;
            }
        };
        MySingleton.getInstance(JoinQiyeShfeiActivity.this).addToRequestQueue(request);
    }

    private void rechargefromWx(final String balance1, final String eachJinE) {
        final String mubiaoId = companId + "_" + "5" + "_" + eachJinE;
        String balance = (int) (Double.parseDouble(balance1) * 100) + "";
        Toast.makeText(JoinQiyeShfeiActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = balance;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity", "JoinQiyeShfeiActivity");
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
                Toast.makeText(JoinQiyeShfeiActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("body", "正事多-转账");
                param.put("detail", "正事多-转账");
                param.put("out_trade_no", getNowTime());
                param.put("total_fee", finalBalance);
                param.put("spbill_create_ip", getHostIP());
                param.put("attach", mubiaoId);
                return param;
            }
        };
        MySingleton.getInstance(JoinQiyeShfeiActivity.this).addToRequestQueue(request);
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

    private void rechargefromZhFb(final String balance, final String eachJinE) {
        String chongzhiId = null;
        chongzhiId = companId;
        try {
            chongzhiId = URLEncoder.encode(chongzhiId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (balance != null && Double.parseDouble(balance) > 0) {
            String url = FXConstant.URL_ZhiFu;
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX, balance, chongzhiId, "5", eachJinE, null, "正事多-红包充值");
            final String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            final String orderinfo = OrderInfoUtil2_0.getSign(params);
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                                PayTask alipay = new PayTask(JoinQiyeShfeiActivity.this);
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
                    Toast.makeText(JoinQiyeShfeiActivity.this, "网络错误，请重试！", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("orderInfo", orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(JoinQiyeShfeiActivity.this).addToRequestQueue(request);
        } else {
            Toast.makeText(JoinQiyeShfeiActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private void tiquhongbao() {
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {

        String url = FXConstant.URL_TIQU_QIYEFXHONGBAO;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("joinqiye,stiqu", s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if ("success".equals(code)) {
                        Toast.makeText(JoinQiyeShfeiActivity.this, "提取成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(JoinQiyeShfeiActivity.this, "提取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(JoinQiyeShfeiActivity.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("userId", companId);
                param.put("Amount", hbJinE);
                return param;
            }
        };
        MySingleton.getInstance(JoinQiyeShfeiActivity.this).addToRequestQueue(request);
        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    @Override
    public void updateQiyeInfo(QiYeInfo qiYeInfo) throws UnsupportedEncodingException {
        resv1 = qiYeInfo.getResv1();
        recruitImages = TextUtils.isEmpty(qiYeInfo.getRecruitImages()) ? "" : qiYeInfo.getRecruitImages();
        recruitBody = TextUtils.isEmpty(qiYeInfo.getRecruitBody()) ? "" : qiYeInfo.getRecruitBody();
        recruitMoney = TextUtils.isEmpty(qiYeInfo.getRecruitMoney()) ? "0" : qiYeInfo.getRecruitMoney();
        joinImages = TextUtils.isEmpty(qiYeInfo.getJoinImages()) ? "" : qiYeInfo.getJoinImages();
        joinBody = TextUtils.isEmpty(qiYeInfo.getJoinBody()) ? "" : qiYeInfo.getJoinBody();
        joinMoney = TextUtils.isEmpty(qiYeInfo.getJoinMoney()) ? "0" : qiYeInfo.getJoinMoney();
        if (joinImages != null && !"".equals(joinImages)) {
            tv_jiameng.setText("已设置");
        } else {
            tv_jiameng.setText("未设置");
        }
        if (recruitImages != null && !"".equals(recruitImages)) {
            tv_zhaopin.setText("已设置");
        } else {
            tv_zhaopin.setText("未设置");
        }
        if ("0".equals(resv1)) {
            tv_yincang.setText("隐藏");
        } else {
            tv_yincang.setText("不隐藏");
        }
        hbJinE = qiYeInfo.getShareRed();
        eachJinE = qiYeInfo.getFriendsNumber();
        String yue;
        if (hbJinE == null || "".equals(hbJinE)) {
            yue = "0";
        } else {
            yue = hbJinE;
        }
        tv_hongbao_yue.setText("￥" + yue);
        et_each_jg.setText("￥" + eachJinE);
        et_each_jg.setTextColor(Color.BLACK);
        et_hongbao_jg.setEnabled(false);
        et_each_jg.setEnabled(false);
        rl_hongbao_yue.setVisibility(View.GONE);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateCurrentPrice(Object success) {
        pass = DemoApplication.getInstance().getCurrentQiyePayPass();
        yuE = DemoApplication.getInstance().getCurrenQiyePrice();
    }
}
