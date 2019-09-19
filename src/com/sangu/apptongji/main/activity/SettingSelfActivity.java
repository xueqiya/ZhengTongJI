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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.sangu.apptongji.main.alluser.order.avtivity.ChongZhiActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.WJPaActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.XiuGaiZFActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.ZhiFuSettingActivity;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.IProfilePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ProfilePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.alluser.view.IProfileView;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.CashierInputFilter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lishaokang on 2016/7/4.\
 * QQ:84543217
 */
public class SettingSelfActivity extends BaseActivity implements IPriceView, IProfileView, IQiYeDetailView {
    private IProfilePresenter presenterpro;
    private IQiYeInfoPresenter presenterqiye;
    private static final int SDK_PAY_FLAG = 1;
    private IPricePresenter pricePresenter = null;
    String id;
    private CheckBox cb1;
    private CheckBox cb2;
    //    private Button btn_tiqu;
    private Button btn_commit;
    private EditText et_yaoqiu;
    private EditText et_jine;
    private EditText et_once_jine;
    private EditText et_time;
    private TextView tv_hongbao_yue;
    private TextView tv_count;
    private int errorTime = 3;
    private String hbJinE = null, chzhiJinE = null, pass = null, biaoshi, friendsNumber, redInterval, singleShare, managerId, shareType;
    private RelativeLayout rl;
    private double yuE;
    private CheckBox cb_bx, cb_qzone, cb_wx, cb_wb, cb_lx_bx, cb_lx_tw, cb_lx_lj;
    String one_down = null;

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
                        Toast.makeText(SettingSelfActivity.this, "支付成功,设置成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        if (cb1.isChecked() || cb2.isChecked() || !TextUtils.isEmpty(et_time.getText().toString().trim()) || !TextUtils.isEmpty(et_yaoqiu.getText().toString().trim())) {
                            xiugai(1);
                        }
                        pricePresenter.updatePriceData(id);
                        if ("00".equals(biaoshi)) {
                            presenterpro.updateData();
                        } else {
                            presenterqiye.loadQiYeInfo(DemoApplication.getInstance().getCurrentQiYeId());
                        }
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(SettingSelfActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(SettingSelfActivity.this, "支付成功,设置成功", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            pricePresenter.updatePriceData(id);
            if (cb1.isChecked() || cb2.isChecked() || !TextUtils.isEmpty(et_time.getText().toString().trim()) || !TextUtils.isEmpty(et_yaoqiu.getText().toString().trim())) {
                xiugai(1);
            }
            if ("00".equals(biaoshi)) {
                presenterpro.updateData();
            } else {
                presenterqiye.loadQiYeInfo(DemoApplication.getInstance().getCurrentQiYeId());
            }
        }
    }

    @Override
    public void back(View view) {
        super.back(view);

    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_setting_self);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constant.APP_ID);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        pricePresenter = new PricePresenter(this, this);
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        managerId = this.getIntent().getStringExtra("managerId");
        initViews();
        rl.setFocusable(true);
        rl.setFocusableInTouchMode(true);
        rl.requestFocus();
        if ("00".equals(biaoshi)) {
            pass = DemoApplication.getInstance().getCurrentPayPass();
            yuE = DemoApplication.getInstance().getCurrenPrice();
            presenterpro = new ProfilePresenter(this, this);
            presenterpro.updateData();
        } else {
            pass = DemoApplication.getInstance().getCurrentQiyePayPass();
            yuE = DemoApplication.getInstance().getCurrenQiyePrice();
            presenterqiye = new QiYeInfoPresenter(this, this);
            presenterqiye.loadQiYeInfo(DemoApplication.getInstance().getCurrentQiYeId());
        }
        setlistener();
    }

    private void initViews() {
        cb_bx = (CheckBox) findViewById(R.id.cb_bx);
        cb_qzone = (CheckBox) findViewById(R.id.cb_qzone);
        cb_wx = (CheckBox) findViewById(R.id.cb_wx);
        cb_wb = (CheckBox) findViewById(R.id.cb_wb);
        cb_lx_bx = (CheckBox) findViewById(R.id.cb_lx_bx);
        cb_lx_tw = (CheckBox) findViewById(R.id.cb_lx_tw);
        cb_lx_lj = (CheckBox) findViewById(R.id.cb_lx_lj);
        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);
        et_time = (EditText) findViewById(R.id.et_time);
        rl = (RelativeLayout) findViewById(R.id.rl);
        et_yaoqiu = (EditText) findViewById(R.id.et_yaoqiu);
        et_jine = (EditText) findViewById(R.id.et_jine);
        et_once_jine = (EditText) findViewById(R.id.et_once_jine);
//        btn_tiqu = (Button) findViewById(R.id.btn_tiqu);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        tv_hongbao_yue = (TextView) findViewById(R.id.tv2);
        tv_count = (TextView) findViewById(R.id.tv_count);
    }

    private void initView() {
        String[] num;
        String fxPingTai = null, fxLeiXing = null;
        if (friendsNumber != null && !"".equals(friendsNumber) && !"0".equals(friendsNumber)) {
            num = friendsNumber.split("\\|");
            String onedown;
            if (num.length > 0) {
                onedown = num[0];
            } else {
                onedown = "0";
            }
            double once = Double.parseDouble(onedown);
            if (once > 0) {
                String prices = String.format("%.2f", once);
                et_once_jine.setText(prices);
            } else {
                et_once_jine.setText(null);
            }
            if (hbJinE != null && !"".equals(hbJinE) && Double.parseDouble(hbJinE) > 0 && Double.parseDouble(onedown) > 0) {
                int i1;
                if ((Double.parseDouble(hbJinE) * 100) % (Double.parseDouble(onedown) * 100) != 0) {
                    i1 = (int) ((Double.parseDouble(hbJinE) * 100) / (Double.parseDouble(onedown) * 100)) + 1;
                } else {
                    i1 = (int) ((Double.parseDouble(hbJinE) * 100) / (Double.parseDouble(onedown) * 100));
                }
                tv_count.setText("共" + String.valueOf(i1) + "个");
            }
        }
        if (hbJinE == null || "".equals(hbJinE)) {
            hbJinE = "0";
        }
        double str = Double.parseDouble(hbJinE);
        String prices1 = String.format("%.2f", str);
        tv_hongbao_yue.setText("￥" + prices1 + "元");
        if ("no".equals(redInterval)) {
            cb1.setChecked(true);
            cb2.setChecked(false);
        } else {
            if ("".equals(redInterval) || "0".equals(redInterval)) {
                cb1.setChecked(false);
                cb2.setChecked(false);
            } else if (redInterval != null && Integer.valueOf(redInterval) > 0) {
                cb2.setChecked(true);
                et_time.setText(redInterval);
            }
        }
        if (singleShare != null && !"".equals(singleShare) && !singleShare.equalsIgnoreCase("null") && Double.parseDouble(singleShare) > 0) {
            et_yaoqiu.setText(singleShare);
        }
        if (shareType == null || "".equals(shareType)) {
            cb_bx.setChecked(true);
            cb_lx_bx.setChecked(true);
        } else {
            String[] count;
            count = shareType.split("\\|");
            if (count.length > 0) {
                fxPingTai = count[0];
            }
            if (count.length > 1) {
                fxLeiXing = count[1];
            }
            if (fxPingTai != null && fxPingTai.contains("空间")) {
                cb_bx.setChecked(false);
                cb_qzone.setChecked(true);
            }
            if (fxPingTai != null && fxPingTai.contains("微信")) {
                cb_bx.setChecked(false);
                cb_wx.setChecked(true);
            }
            if (fxPingTai != null && fxPingTai.contains("微博")) {
                cb_bx.setChecked(false);
                cb_wb.setChecked(true);
            }
            if (fxLeiXing != null && fxLeiXing.contains("图文")) {
                cb_lx_bx.setChecked(false);
                cb_lx_tw.setChecked(true);
            } else if (fxLeiXing != null && fxLeiXing.contains("链接")) {
                cb_lx_bx.setChecked(false);
                cb_lx_lj.setChecked(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ("00".equals(biaoshi)) {
            id = DemoHelper.getInstance().getCurrentUsernName();
        } else {
            id = DemoApplication.getInstance().getCurrentQiYeId();
        }
        pricePresenter.updatePriceData(id);
    }

    private void setlistener() {
        cb_bx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_qzone.setChecked(false);
                cb_wx.setChecked(false);
                cb_wb.setChecked(false);
            }
        });
        cb_qzone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_qzone.isChecked() && cb_wx.isChecked() && cb_wb.isChecked()) {
                    cb_bx.setChecked(true);
                    cb_qzone.setChecked(false);
                    cb_wx.setChecked(false);
                    cb_wb.setChecked(false);
                } else if (!cb_qzone.isChecked() && !cb_wx.isChecked() && !cb_wb.isChecked()) {
                    cb_bx.setChecked(true);
                } else {
                    cb_bx.setChecked(false);
                }
            }
        });
        cb_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_qzone.isChecked() && cb_wx.isChecked() && cb_wb.isChecked()) {
                    cb_bx.setChecked(true);
                    cb_qzone.setChecked(false);
                    cb_wx.setChecked(false);
                    cb_wb.setChecked(false);
                } else if (!cb_qzone.isChecked() && !cb_wx.isChecked() && !cb_wb.isChecked()) {
                    cb_bx.setChecked(true);
                } else {
                    cb_bx.setChecked(false);
                }
            }
        });
        cb_wb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_qzone.isChecked() && cb_wx.isChecked() && cb_wb.isChecked()) {
                    cb_bx.setChecked(true);
                    cb_qzone.setChecked(false);
                    cb_wx.setChecked(false);
                    cb_wb.setChecked(false);
                } else if (!cb_qzone.isChecked() && !cb_wx.isChecked() && !cb_wb.isChecked()) {
                    cb_bx.setChecked(true);
                } else {
                    cb_bx.setChecked(false);
                }
            }
        });
        cb_lx_bx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_lx_tw.setChecked(false);
                cb_lx_lj.setChecked(false);
            }
        });
        cb_lx_tw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_lx_tw.isChecked() && cb_lx_lj.isChecked()) {
                    cb_lx_bx.setChecked(true);
                    cb_lx_tw.setChecked(false);
                    cb_lx_lj.setChecked(false);
                } else if (!cb_lx_tw.isChecked() && !cb_lx_lj.isChecked()) {
                    cb_lx_bx.setChecked(true);
                } else {
                    cb_lx_bx.setChecked(false);
                }
            }
        });
        cb_lx_lj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_lx_tw.isChecked() && cb_lx_lj.isChecked()) {
                    cb_lx_bx.setChecked(true);
                    cb_lx_tw.setChecked(false);
                    cb_lx_lj.setChecked(false);
                } else if (!cb_lx_tw.isChecked() && !cb_lx_lj.isChecked()) {
                    cb_lx_bx.setChecked(true);
                } else {
                    cb_lx_bx.setChecked(false);
                }
            }
        });

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rl_chongzhi.setVisibility(View.GONE);
//                rl_chongzhi.setAnimation(AnimationUtil.moveToViewBottom());
            }
        });
        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb2.isChecked()) {
                    cb2.setChecked(false);
                    et_time.setText(null);
                }
            }
        });
        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb1.isChecked()) {
                    cb1.setChecked(false);
                }
            }
        });
        et_yaoqiu.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_time.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                if (temp.length() > 2) {
                    edt.delete(2, 3);
                }
            }
        });
        et_jine.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters = {new CashierInputFilter()};
        et_jine.setFilters(filters);
        et_jine.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String onceJine = et_once_jine.getText().toString().trim();
                String temp = edt.toString();
                if (temp == null || "".equals(temp) || TextUtils.isEmpty(temp)) {
                    temp = "0";
                }
                if (hbJinE == null || "".equals(hbJinE) || hbJinE.equalsIgnoreCase("null")) {
                    hbJinE = "0";
                }
                double zongE = Double.parseDouble(hbJinE) + Double.parseDouble(temp);
                if (onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                    int i1;
                    if ((zongE * 100) % (Double.parseDouble(onceJine) * 100) != 0) {
                        i1 = (int) ((zongE * 100) / (Double.parseDouble(onceJine) * 100)) + 1;
                    } else {
                        i1 = (int) ((zongE * 100) / (Double.parseDouble(onceJine) * 100));
                    }
                    tv_count.setText("共" + String.valueOf(i1) + "个");
                } else {
                    tv_count.setText(null);
                }
                if (TextUtils.isEmpty(onceJine)) {
                    tv_count.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_count.setText(null);
                        }
                    });
                }
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
        et_once_jine.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_once_jine.setFilters(filters);
        et_once_jine.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String jine = et_jine.getText().toString().trim();
                String onceJine = edt.toString();
                if (jine == null || "".equals(jine) || TextUtils.isEmpty(jine)) {
                    jine = "0";
                }
                if (hbJinE == null || "".equals(hbJinE) || hbJinE.equalsIgnoreCase("null")) {
                    hbJinE = "0";
                }
                double zongE = Double.parseDouble(hbJinE) + Double.parseDouble(jine);
                if (onceJine != null && !"".equals(onceJine) && Double.parseDouble(onceJine) > 0) {
                    int i1;
                    if ((zongE * 100) % (Double.parseDouble(onceJine) * 100) != 0) {
                        i1 = (int) ((zongE * 100) / (Double.parseDouble(onceJine) * 100)) + 1;
                    } else {
                        i1 = (int) ((zongE * 100) / (Double.parseDouble(onceJine) * 100));
                    }
                    tv_count.setText("共" + String.valueOf(i1) + "个");
                } else {
                    tv_count.setText(null);
                }
                if (TextUtils.isEmpty(onceJine)) {
                    tv_count.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_count.setText(null);
                        }
                    });
                }
                int posDot = onceJine.indexOf(".");
                if (posDot <= 0) return;
                if (onceJine.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
//        btn_tiqu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (hbJinE==null||"".equals(hbJinE)||Double.parseDouble(hbJinE)==0){
//                    Toast.makeText(SettingSelfActivity.this, "您还没有设置分享红包！", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                LayoutInflater inflater1 = LayoutInflater.from(SettingSelfActivity.this);
//                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
//                final Dialog dialog1 = new AlertDialog.Builder(SettingSelfActivity.this,R.style.Dialog).create();
//                dialog1.show();
//                dialog1.getWindow().setContentView(layout1);
//                dialog1.setCanceledOnTouchOutside(true);
//                dialog1.setCancelable(true);
//                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
//                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
//                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
//                btnOK1.setText("确定");
//                btnCancel1.setText("取消");
//                title_tv1.setText("确定提取分享红包吗");
//                btnCancel1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog1.dismiss();
//                    }
//                });
//                btnOK1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog1.dismiss();
//                        tiquhongbao();
//                    }
//                });
//            }
//        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
                if (isOpen) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SettingSelfActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                String onceJine = null;
                if (friendsNumber != null && !"".equals(friendsNumber)) {
                    onceJine = friendsNumber.split("\\|")[0];
                }
                one_down = et_once_jine.getText().toString().trim();
                if (onceJine == null || "".equals(onceJine) || Double.parseDouble(onceJine) == 0) {
                    if (one_down == null || "".equals(one_down) || TextUtils.isEmpty(one_down) || Double.parseDouble(one_down) <= 0) {
                        Toast.makeText(SettingSelfActivity.this, "请输入单次分享金额！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (one_down == null || "".equals(one_down) || TextUtils.isEmpty(one_down) || Double.parseDouble(one_down) <= 0) {
                        one_down = onceJine;
                    }
                }
                if (!TextUtils.isEmpty(one_down) && !one_down.equalsIgnoreCase("null") && Double.parseDouble(one_down) >= 0) {
                    chzhiJinE = et_jine.getText().toString().trim();
                    if ((chzhiJinE == null || "".equals(chzhiJinE)) && (hbJinE == null || "".equals(hbJinE) || hbJinE.equalsIgnoreCase("null"))) {
                        Toast.makeText(SettingSelfActivity.this, "请输入金额！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (chzhiJinE == null || "".equals(chzhiJinE) || Double.parseDouble(chzhiJinE) == 0) {
                        chzhiJinE = "0";
                    }
                    if (hbJinE == null || "".equals(hbJinE) || hbJinE.equalsIgnoreCase("null")) {
                        hbJinE = "0";
                    }
                    double zonge = Double.parseDouble(chzhiJinE) + Double.parseDouble(hbJinE);
                    if (one_down != null && !TextUtils.isEmpty(one_down) && zonge < Double.parseDouble(one_down)) {
                        Toast.makeText(SettingSelfActivity.this, "单次分享金额不能大于红包金额！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (one_down == null || "".equals(one_down)) {
                        one_down = "0";
                    }
                    friendsNumber = one_down + "|" + "0" + "|" + "0";
                    if (!"0".equals(chzhiJinE)) {
                        LayoutInflater inflater1 = LayoutInflater.from(SettingSelfActivity.this);
                        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog1 = new AlertDialog.Builder(SettingSelfActivity.this, R.style.Dialog).create();
                        dialog1.show();
                        dialog1.getWindow().setContentView(layout1);
                        dialog1.setCanceledOnTouchOutside(true);
                        dialog1.setCancelable(true);
                        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                        title.setText("温馨提示");
                        btnOK1.setText("确定充值");
                        btnCancel1.setText("下次再说");
                        title_tv1.setText("本次充值金额为" + chzhiJinE + "元");
                        btnCancel1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                        btnOK1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pricePresenter.updatePriceData(id);
                                dialog1.dismiss();
                                LayoutInflater inflaterDl = LayoutInflater.from(SettingSelfActivity.this);
                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                                final Dialog dialog = new AlertDialog.Builder(SettingSelfActivity.this, R.style.Dialog).create();
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
                                tv_item1.setText("余 额 充 值");
                                tv_item2.setText("微 信 充 值");
                                tv_item5.setText("支付宝充值");
                                re_item1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        pricePresenter.updatePriceData(id);
                                        chongzhi(Double.parseDouble(chzhiJinE));
                                    }
                                });
                                re_item2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        rechargefromWx(chzhiJinE, friendsNumber);
                                    }
                                });
                                re_item5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        rechargefromZhFb(chzhiJinE, friendsNumber);
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
                    } else {
                        xiugai(0);
                    }
                } else {
                    Toast.makeText(SettingSelfActivity.this, "必须至少设置一个单次分享金额！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void xiugai(final int type) {

        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        one_down = et_once_jine.getText().toString().trim();
        String fxPingTai, fxLeiXing;
        final String shareType;
        if (cb_bx.isChecked()) {
            fxPingTai = "不限";
        } else if (cb_qzone.isChecked()) {
            fxPingTai = "空间," + "" + ",";
            if (cb_wx.isChecked()) {
                fxPingTai = "空间,微信," + "";
            } else if (cb_wb.isChecked()) {
                fxPingTai = "空间," + "" + ",微博";
            }
        } else if (cb_wx.isChecked()) {
            fxPingTai = "" + ",微信," + "";
            if (cb_wb.isChecked()) {
                fxPingTai = "" + ",微信,微博";
            }
        } else {
            fxPingTai = "," + "" + ",微博";
        }
        if (cb_lx_bx.isChecked()) {
            fxLeiXing = "不限";
        } else if (cb_lx_tw.isChecked()) {
            fxLeiXing = "图文";
        } else {
            fxLeiXing = "链接";
        }
        shareType = fxPingTai + "|" + fxLeiXing;
        final String time = TextUtils.isEmpty(et_time.getText().toString().trim()) ? "0" : et_time.getText().toString().trim();
        final String renshu = et_yaoqiu.getText().toString().trim();
        final String url, uId;
        if ("00".equals(biaoshi)) {
            url = FXConstant.URL_SHEZHI_FXHONGBAO;
            uId = DemoHelper.getInstance().getCurrentUsernName();
        } else {
            url = FXConstant.URL_SHEZHI_QIYEFXHONGBAO;
            uId = DemoApplication.getInstance().getCurrentQiYeId();
        }
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (type == 0) {
                    pricePresenter.updatePriceData(id);
                    Toast.makeText(SettingSelfActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type == 0) {
                    Toast.makeText(SettingSelfActivity.this, "网络连接中断,修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("userId", uId);
                param.put("Amount", "0");
                param.put("friendsNumber", friendsNumber);
                if ("00".equals(biaoshi)) {
                    param.put("shareRedSort", one_down);
                }
                param.put("shareType", shareType);
                if (cb1.isChecked()) {
                    param.put("redInterval", "no");
                } else {
                    param.put("redInterval", time);
                }
                if (!TextUtils.isEmpty(renshu)) {
                    param.put("singleShare", renshu);
                }
                Log.e("settingselfac,par", param.toString());
                return param;
            }
        };
        MySingleton.getInstance(SettingSelfActivity.this).addToRequestQueue(request);

        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    private void chongzhi(final double orderSu) {
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        String fxPingTai, fxLeiXing;
        final String shareType;
        if (cb_bx.isChecked()) {
            fxPingTai = "不限";
        } else if (cb_qzone.isChecked()) {
            fxPingTai = "空间," + "" + ",";
            if (cb_wx.isChecked()) {
                fxPingTai = "空间,微信," + "";
            } else if (cb_wb.isChecked()) {
                fxPingTai = "空间," + "" + ",微博";
            }
        } else if (cb_wx.isChecked()) {
            fxPingTai = "" + ",微信," + "";
            if (cb_wb.isChecked()) {
                fxPingTai = "" + ",微信,微博";
            }
        } else {
            fxPingTai = "," + "" + ",微博";
        }
        if (cb_lx_bx.isChecked()) {
            fxLeiXing = "不限";
        } else if (cb_lx_tw.isChecked()) {
            fxLeiXing = "图文";
        } else {
            fxLeiXing = "链接";
        }
        shareType = fxPingTai + "|" + fxLeiXing;
        one_down = et_once_jine.getText().toString().trim();
        if (one_down == null || "".equals(one_down)) {
            one_down = "0";
        }
        if ("00".equals(biaoshi)) {
            pass = DemoApplication.getInstance().getCurrentPayPass();
            yuE = DemoApplication.getInstance().getCurrenPrice();
        } else {
            pass = DemoApplication.getInstance().getCurrentQiyePayPass();
            yuE = DemoApplication.getInstance().getCurrenQiyePrice();
        }
        if (pass == null || "".equals(pass)) {
            showErrorMiMaSHZH();
            return;
        }
        if (pass.length() != 6 || !isNumeric(pass)) {
            showErrorMiMaXG();
            return;
        }
        if (errorTime <= 0) {
            showErrorLing();
            return;
        }
        friendsNumber = one_down + "|" + "0" + "|" + "0";
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
            Double d = yuE - orderSu;
            if (d >= 0) {
                LayoutInflater inflaterDl = LayoutInflater.from(SettingSelfActivity.this);
                final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
                final Dialog dialog = new AlertDialog.Builder(SettingSelfActivity.this, R.style.Dialog).create();
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
                        final ProgressDialog pd = new ProgressDialog(SettingSelfActivity.this);
                        pd.setMessage("正在支付...");
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();
                        final String time = TextUtils.isEmpty(et_time.getText().toString().trim()) ? "0" : et_time.getText().toString().trim();
                        String renshu = et_yaoqiu.getText().toString().trim();
                        if (TextUtils.isEmpty(renshu)) {
                            renshu = "0";
                        }
                        if (pwdView.getStrPassword().equals(pass)) {
                            final String url, uId;
                            if ("00".equals(biaoshi)) {
                                url = FXConstant.URL_SHEZHI_FXHONGBAO;
                                uId = DemoHelper.getInstance().getCurrentUsernName();
                            } else {
                                url = FXConstant.URL_SHEZHI_QIYEFXHONGBAO;
                                uId = DemoApplication.getInstance().getCurrentQiYeId();
                            }
                            final String finalRenshu = renshu;
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    dialog.dismiss();
                                    pd.dismiss();
                                    pricePresenter.updatePriceData(id);
                                    Toast.makeText(SettingSelfActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                                    double zonge = Double.parseDouble(chzhiJinE) + Double.parseDouble(hbJinE);
                                    if ("00".equals(biaoshi)) {
                                        presenterpro.updateData();
                                    } else {
                                        presenterqiye.loadQiYeInfo(DemoApplication.getInstance().getCurrentQiYeId());
                                    }
                                    setResult(RESULT_OK, new Intent().putExtra("hbJinE", zonge).putExtra("friendsNumber", friendsNumber));
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    dialog.dismiss();
                                    pd.dismiss();
                                    Toast.makeText(SettingSelfActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> param = new HashMap<String, String>();
                                    param.put("userId", uId);
                                    param.put("Amount", chzhiJinE);
                                    param.put("friendsNumber", friendsNumber);
                                    if ("00".equals(biaoshi)) {
                                        param.put("shareRedSort", one_down);
                                    }
                                    param.put("shareType", shareType);
                                    if (cb1.isChecked()) {
                                        param.put("redInterval", "no");
                                    } else if (cb2.isChecked()) {
                                        param.put("redInterval", time);
                                    }
                                    param.put("singleShare", finalRenshu);
                                    return param;
                                }
                            };
                            MySingleton.getInstance(SettingSelfActivity.this).addToRequestQueue(request);
                        } else {
                            pd.dismiss();
                            int times;
                            if (errorTime > 0) {
                                times = errorTime - 1;
                            } else {
                                times = 0;
                            }
                            reduceShRZFCount(times + "");
                            if (times == 0) {
                                showErrorLing();
                            } else {
                                showErrorTishi(times + "");
                            }
                            dialog.dismiss();
                        }
                    }
                });
                /**
                 *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
                 *  如果写了，会覆盖我们在自定义控件中提供的响应
                 *  可以看到这里toast显示 ""而不是"Cancel"*/
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
                        if ("01".equals(biaoshi)) {
                            bs = "001";
                        } else {
                            bs = "000";
                        }
                        startActivity(new Intent(SettingSelfActivity.this, WJPaActivity.class).putExtra("biaoshi", bs).putExtra("managerId", managerId));
                    }
                });
            } else {
                LayoutInflater inflaterDl = LayoutInflater.from(SettingSelfActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                final Dialog dialog = new AlertDialog.Builder(SettingSelfActivity.this, R.style.Dialog).create();
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
                tv_title.setText("钱包余额不足(剩余" + yuE + "元),请选择其他支付方式");
                tv_item1.setText("余 额 充 值");
                tv_item2.setText("微 信 充 值");
                tv_item5.setText("支付宝充值");
                re_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(SettingSelfActivity.this, ChongZhiActivity.class);
                        intent.putExtra("papass", pass);
                        intent.putExtra("biaoshi", "00");
                        startActivity(intent);
                    }
                });
                re_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        rechargefromWx(chzhiJinE, friendsNumber);
                    }
                });
                re_item5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        rechargefromZhFb(chzhiJinE, friendsNumber);
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

        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    private void rechargefromWx(final String balance1, final String eachJinE) {
        final String mubiaoId = DemoHelper.getInstance().getCurrentUsernName() + "_" + "5" + "_" + eachJinE;
        String balance = (int) (Double.parseDouble(balance1) * 100) + "";
        Toast.makeText(SettingSelfActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = balance;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity", "SettingSelfActivity");
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
                Toast.makeText(SettingSelfActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(SettingSelfActivity.this).addToRequestQueue(request);
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
        chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
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
                                PayTask alipay = new PayTask(SettingSelfActivity.this);
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
                    Toast.makeText(SettingSelfActivity.this, "网络错误，请重试！", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("orderInfo", orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(SettingSelfActivity.this).addToRequestQueue(request);
        } else {
            Toast.makeText(SettingSelfActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private void tiquhongbao() {

        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        final String url, uId;
        if ("00".equals(biaoshi)) {
            url = FXConstant.URL_TIQU_FXHONGBAO;
            uId = DemoHelper.getInstance().getCurrentUsernName();
        } else {
            url = FXConstant.URL_TIQU_QIYEFXHONGBAO;
            uId = DemoApplication.getInstance().getCurrentQiYeId();
        }
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Settingself,s", s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if ("success".equals(code)) {
                        Toast.makeText(SettingSelfActivity.this, "提取成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(SettingSelfActivity.this, "提取失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SettingSelfActivity.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("userId", uId);
                param.put("Amount", hbJinE);
                return param;
            }
        };
        MySingleton.getInstance(SettingSelfActivity.this).addToRequestQueue(request);
        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    @Override
    public void updateCurrentPrice(Object success) {
        if ("00".equals(biaoshi)) {
            pass = DemoApplication.getInstance().getCurrentPayPass();
            yuE = DemoApplication.getInstance().getCurrenPrice();
        } else {
            pass = DemoApplication.getInstance().getCurrentQiyePayPass();
            yuE = DemoApplication.getInstance().getCurrenQiyePrice();
        }
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }

    @Override
    public void updateUserInfo(Userful user) {
        hbJinE = user.getShareRed();
        friendsNumber = user.getFriendsNumber();
        singleShare = user.getSingleShare();
        redInterval = user.getRedInterval();
        shareType = user.getShareType();
        initView();
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
    public void updateQiyeInfo(QiYeInfo qiYeInfo) throws UnsupportedEncodingException {
        hbJinE = qiYeInfo.getShareRed();
        friendsNumber = qiYeInfo.getFriendsNumber();
        singleShare = qiYeInfo.getSingleShare();
        redInterval = qiYeInfo.getRedInterval();
        shareType = qiYeInfo.getShareType();
        initView();
    }

    private void showErrorLing() {
        LayoutInflater inflaterDl = LayoutInflater.from(SettingSelfActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
        final Dialog dialog2 = new AlertDialog.Builder(SettingSelfActivity.this, R.style.Dialog).create();
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
        LayoutInflater inflater1 = LayoutInflater.from(SettingSelfActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(SettingSelfActivity.this, R.style.Dialog).create();
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
        title_tv1.setText("支付密码错误，您还可以输入" + s + "次");
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
                if ("00".equals(biaoshi)) {
                    bs = "000";
                } else {
                    bs = "001";
                }
                startActivity(new Intent(SettingSelfActivity.this, WJPaActivity.class).putExtra("biaoshi", bs).putExtra("managerId", managerId));
            }
        });
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private void showErrorMiMaXG() {
        LayoutInflater inflater1 = LayoutInflater.from(SettingSelfActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(SettingSelfActivity.this, R.style.Dialog).create();
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
                startActivity(new Intent(SettingSelfActivity.this, XiuGaiZFActivity.class).putExtra("biaoshi", bs).putExtra("zhifupass", pass));
            }
        });
    }

    private void showErrorMiMaSHZH() {
        LayoutInflater inflater1 = LayoutInflater.from(SettingSelfActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(SettingSelfActivity.this, R.style.Dialog).create();
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
        if ("00".equals(biaoshi)) {
            title_tv1.setText("您还没有设置支付密码!");
        } else {
            title_tv1.setText("您还没有设置企业的支付密码!");
        }
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
                if ("00".equals(biaoshi)) {
                    bs = "000";
                } else {
                    bs = "001";
                }
                startActivity(new Intent(SettingSelfActivity.this, ZhiFuSettingActivity.class).putExtra("biaoshi", bs).putExtra("zhifupass", pass));
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
                if (code.equals("SUCCESS")) {
                    if (errorTime > 0) {
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
                params1.put("enterErrorTimes", times + "");
                return params1;
            }
        };
        MySingleton.getInstance(SettingSelfActivity.this).addToRequestQueue(request3);
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
}
