package com.sangu.apptongji.main.alluser.order.avtivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.CertificationActivity;
import com.sangu.apptongji.main.address.KyqInfo;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.callback.IError;
import com.sangu.apptongji.main.callback.ISuccess;
import com.sangu.apptongji.main.chatheadimage.StringUtils;
import com.sangu.apptongji.main.utils.CashierInputFilter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.SyAddressBookUtil;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by user on 2016/9/8.
 */

public class TiXianActivity extends BaseActivity implements IPriceView{
    private IPricePresenter presenter=null;
    private ImageView ivBack=null;
    private EditText et_zhanghao=null;
    private EditText et_jine=null;
    private TextView tv_jine=null;
    private TextView tv_tixian_all=null;
    private Button btnNext=null;
    private String pass=null,baozhengjin=null,biaoshi2=null,biaoshi=null,totalWithdrawals,totalCashWithdrawal;
    private LinearLayout ll1=null,ll2=null;
    private Double price=null;
    private IWXAPI api=null;
    private String openId=null,managerId;
    private int errorTime=3,userCount=0,redCount=0;
    private ProgressDialog mProgress;
    private List<String> kyqIds = new ArrayList<>();
    private String orderIdDyna,orderIdComp,orderIdHome;
    private double amountAll;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        openId = null;
        setResult(RESULT_OK);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mProgress!=null&&mProgress.isShowing()) {
            mProgress.dismiss();
        }
        openId = null;
        setIntent(intent);
        openId = intent.getStringExtra("openId");
        if (openId!=null){
            if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {

                // 判断完操作权限之后 先看本地是否已经有存储的必须实名的条件 realAuth
                //有的话就查询一下是否认证过了 没有的话就弹出对应提示框

                SharedPreferences sp = getSharedPreferences("sangu_RealNameAuth_info", Context.MODE_PRIVATE);
                String auth = sp.getString("realAuth","0");

                if (auth.equals("no"))
                {
                    //本地有记录  检测实名
                    IsRealNameAuth();

                }else
                {
                    //本地没记录 正常操作
                    showDialog(openId,et_jine.getText().toString().trim());
                }

          //  showDialog(openId,et_jine.getText().toString().trim());

            } else {
                ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian);

        presenter = new PricePresenter(this,this);
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("正在调起微信授权...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        Intent intent = this.getIntent();
        biaoshi2 = intent.hasExtra("biaoshi2")?intent.getStringExtra("biaoshi2"):"";
        managerId = intent.hasExtra("managerId")?intent.getStringExtra("managerId"):"";
        biaoshi = intent.hasExtra("biaoshi")?intent.getStringExtra("biaoshi"):"";
        baozhengjin = intent.getStringExtra("baozhengjin");
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        et_zhanghao = (EditText) findViewById(R.id.et_zhanghao);
        et_zhanghao.setEnabled(false);
        et_jine = (EditText) findViewById(R.id.et_jine);
        tv_jine = (TextView) findViewById(R.id.tv_jine);
        tv_tixian_all = (TextView) findViewById(R.id.tv_tixian_all);
        btnNext = (Button) findViewById(R.id.btn_tx_next);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if ("01".equals(biaoshi)){
            presenter.updatePriceData(DemoApplication.getInstance().getCurrentQiYeId());
        }else {
            presenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        }
        if ("01".equals(biaoshi2)){
            et_zhanghao.setText("提现到账户余额");
            initView2();
        }else {
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {

                        String balance = et_jine.getText().toString().trim();

                        if (!TextUtils.isEmpty(balance)&&Double.parseDouble(balance)>=1)
                        {

                            if (Double.parseDouble(balance)>price){

                                Toast.makeText(TiXianActivity.this,"转出金额超限！",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // 判断完操作权限之后 先看本地是否已经有存储的必须实名的条件 realAuth
                            //有的话就查询一下是否认证过了 没有的话就弹出对应提示框
                            SharedPreferences sp4 = getSharedPreferences("sangu_RealNameAuth_info", Context.MODE_PRIVATE);
                            String auth4 = sp4.getString("realAuth","0");
                            if (auth4.equals("no")) {

                                IsRealNameAuth();

                            }else {

                                if (openId==null||"".equals(openId)) {
                                    mProgress.show();

                                    final SendAuth.Req req = new SendAuth.Req();
                                    req.scope = "snsapi_userinfo";
                                    req.state = "sangutixian";
                                    api.sendReq(req);

                                }else {

                                    showDialog(openId,et_jine.getText().toString().trim());

                                }

                            }

                        }else {

                            Toast.makeText(TiXianActivity.this,"请输入提现金额，且金额必须大于等于1元",Toast.LENGTH_SHORT).show();

                        }

                    } else {
                        ToastUtils.showNOrmalToast(TiXianActivity.this.getApplicationContext(), "您的账户已被冻结");
                    }
                }
            });
        }


    }

    private void showDialog(final String openId, final String balance){
        LayoutInflater inflater2 = LayoutInflater.from(TiXianActivity.this);
        RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
        final Dialog dialog2 = new AlertDialog.Builder(TiXianActivity.this,R.style.Dialog).create();
        dialog2.show();
        dialog2.getWindow().setContentView(layout2);
        dialog2.setCanceledOnTouchOutside(true);
        dialog2.setCancelable(true);
        TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
        Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
        final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
        btnOK2.setText("确定");
        btnCancel2.setText("取消");
        title_tv2.setText("微信授权成功,确认继续提现？");
        btnCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                TiXianActivity.this.openId = null;
                finish();
            }
        });
        btnOK2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                final String pass = DemoApplication.getInstance().getCurrentPayPass();
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
                if (Double.parseDouble(totalWithdrawals)==0){
                    new AlertDialog.Builder(TiXianActivity.this)
                            .setMessage("\n您当天操作次数已达到上限,为保护您账户安全,请明天再进行提现操作或联系客服\n")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return;
                }
                LayoutInflater inflaterDl = LayoutInflater.from(TiXianActivity.this);
                final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
                final Dialog dialog = new AlertDialog.Builder(TiXianActivity.this,R.style.Dialog).create();
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
                        final ProgressDialog pd = new ProgressDialog(TiXianActivity.this);
                        pd.setMessage("正在请求提现...");
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();
                        if (pwdView.getStrPassword().equals(pass)) {
                            dialog.dismiss();
                            withdrawCash(openId,balance,pd);
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
                        startActivity(new Intent(TiXianActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
                    }
                });
            }
        });
    }

    private void IsRealNameAuth()
    {

        String url = FXConstant.URL_CHAXUN_RENZHENG;

        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
                Log.d("chen", "onResponse" + s);

                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("SUCCESS")){
                        org.json.JSONObject object1 = object.getJSONObject("list");
                        if (object1!=null&&!"".equals(object1)){

                            String examine = object1.getString("examine");

                            if ("审核通过".equals(examine)) {

                                //审核通过允许提现 删除本地标识 恢复原来的提现流程
                                SharedPreferences sp3 = getSharedPreferences("sangu_RealNameAuth_info", Context.MODE_PRIVATE);
                                if (sp3!=null) {
                                    SharedPreferences.Editor editor1 = sp3.edit();
                                    editor1.clear();
                                    editor1.commit();
                                }

                                showDialog(openId,et_jine.getText().toString().trim());

                            }else if ("正在审核".equals(examine))
                            {
                                //正在审核提示等待
                                Toast.makeText(TiXianActivity.this, "请等待实名认证审核通过后再提现", Toast.LENGTH_SHORT).show();


                            }else {
                                //未实名 直接跳转实名提示框

                                LayoutInflater inflater2 = LayoutInflater.from(TiXianActivity.this);
                                RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                                final Dialog dialog2 = new AlertDialog.Builder(TiXianActivity.this).create();
                                dialog2.show();
                                dialog2.getWindow().setContentView(layout2);
                                dialog2.setCanceledOnTouchOutside(true);
                                dialog2.setCancelable(true);
                                TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                                Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                                final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                                btnOK2.setText("确定");
                                btnCancel2.setText("取消");
                                title_tv2.setText("当前账号与微信实名不一致，请您实名认证后再提现");
                                btnCancel2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                    }
                                });
                                btnOK2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(TiXianActivity.this, CertificationActivity.class));
                                        dialog2.dismiss();
                                    }
                                });

                            }
                        }else {

                            LayoutInflater inflater2 = LayoutInflater.from(TiXianActivity.this);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog2 = new AlertDialog.Builder(TiXianActivity.this).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout2);
                            dialog2.setCanceledOnTouchOutside(true);
                            dialog2.setCancelable(true);
                            TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                            Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                            final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                            btnOK2.setText("确定");
                            btnCancel2.setText("取消");
                            title_tv2.setText("当前账号与微信实名不一致，请您实名认证后再提现");
                            btnCancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });
                            btnOK2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(TiXianActivity.this, CertificationActivity.class));
                                    dialog2.dismiss();
                                }
                            });

                        }

                    }else {

                        LayoutInflater inflater2 = LayoutInflater.from(TiXianActivity.this);
                        RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog2 = new AlertDialog.Builder(TiXianActivity.this).create();
                        dialog2.show();
                        dialog2.getWindow().setContentView(layout2);
                        dialog2.setCanceledOnTouchOutside(true);
                        dialog2.setCancelable(true);
                        TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                        Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                        final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                        btnOK2.setText("确定");
                        btnCancel2.setText("取消");
                        title_tv2.setText("当前账号与微信实名不一致，请您实名认证后再提现");
                        btnCancel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
                        btnOK2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(TiXianActivity.this, CertificationActivity.class));
                                dialog2.dismiss();
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (volleyError!=null) {
                    Log.e("tixian", volleyError.getMessage());
                    Log.d("chen", "tixian" + volleyError.getMessage());
                }
                Toast.makeText(TiXianActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();

                param.put("uId",DemoHelper.getInstance().getCurrentUsernName());

                return param;
            }
        };
        MySingleton.getInstance(TiXianActivity.this).addToRequestQueue(request);
    }

    private void withdrawCash(final String openId, String balance, final ProgressDialog dialog) {
        balance = (int)(Double.parseDouble(balance)*100)+"";
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        final String id = DemoHelper.getInstance().getCurrentUsernName();
        name = TextUtils.isEmpty(name)?id:name;
        String url = FXConstant.URL_WX_TIXIAN;
        final String finalName = name;
        final String finalBalance = balance;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (dialog!=null&&dialog.isShowing()) {
                    dialog.dismiss();
                }
                //提现返回结果onResponse{"state":"禁止提现"}
                Log.d("chen", "提现返回结果onResponse" + s);
                if (s==null||"".equals(s)||"null".equals(s)||"NULL".equals(s)){
                    Toast.makeText(TiXianActivity.this, "微信客户端繁忙", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONObject object = new JSONObject(s);
                    if (object==null||"".equals(object)||"null".equals(object)||"NULL".equals(object)){
                        Toast.makeText(TiXianActivity.this, "微信客户端繁忙", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String state = object.getString("state");
                    String err_code_des = null;
                    if (s.contains("err_code_des")) {
                         err_code_des = object.getString("err_code_des");
                    }
                    if (state.equals("SUCCESS")) {
                        Toast.makeText(TiXianActivity.this, "提现成功，请注意查收" , Toast.LENGTH_SHORT).show();
                        String nowTime = getNowTime2();
                        SharedPreferences mSharedPreference = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
                        String tiXianTime = mSharedPreference.getString("tiXianTime",null);
                        int size = StringUtils.getBetweenDays(nowTime,tiXianTime).size();
                        if (tiXianTime==null||size>7){
                            LayoutInflater inflater2 = LayoutInflater.from(TiXianActivity.this);
                            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                            final Dialog dialog2 = new AlertDialog.Builder(TiXianActivity.this,R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout2);
                            dialog2.setCanceledOnTouchOutside(false);
                            dialog2.setCancelable(false);
                            TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                            Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                            final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                            TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                            title2.setText("分享赚红包");
                            btnOK2.setText("一键分享");
                            btnCancel2.setText("不分享悄悄赚");
                            title_tv2.setText("把这个赚红包的消息\n分享给亲朋好友吧");
                            btnCancel2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                    finish();
                                }
                            });
                            btnOK2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();

                                    UserPermissionUtil.getUserPermission(TiXianActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "8", new UserPermissionUtil.UserPermissionListener() {
                                        @Override
                                        public void onAllow() {
                                            PermissionUtil permissionUtil = new PermissionUtil(TiXianActivity.this);
                                            permissionUtil.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                                    new PermissionListener() {
                                                        @Override
                                                        public void onGranted() {
                                                            //所有权限都已经授权
                                                            if (mProgress!=null) {
                                                                mProgress.setMessage("正在分享，请稍后...");
                                                                mProgress.setCancelable(false);
                                                                mProgress.show();
                                                            }
                                                            getRedCount();
                                                        }
                                                        @Override
                                                        public void onDenied(List<String> deniedPermission) {
                                                            //Toast第一个被拒绝的权限
                                                            Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限！",Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                        @Override
                                                        public void onShouldShowRationale(List<String> deniedPermission) {
                                                            //Toast第一个勾选不在提示的权限
                                                            Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onBan() {
                                            ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止转发红包");

                                        }
                                    });


                                }
                            });
                        }
                    }else if (state.equals("操作频繁,请于60秒后重试!")){
                        Toast.makeText(TiXianActivity.this, "您的操作过于频繁,请于60秒后重试!", Toast.LENGTH_LONG).show();
                    }else if (state.equalsIgnoreCase("禁止提现")){
                        Log.d("chen", "禁止提现" + state);
                        LayoutInflater inflater = LayoutInflater.from(TiXianActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_regist_error, null);
                        final AlertDialog dialog3 = new AlertDialog.Builder(TiXianActivity.this,R.style.Dialog).create();
                        TextView tv_phone = (TextView) layout.findViewById(R.id.tv_phone);
                        TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                        Button btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
                        Button btn_kefu = (Button) layout.findViewById(R.id.btn_kefu);
                        dialog3.show();
                        dialog3.getWindow().setContentView(layout);
                        dialog3.setCanceledOnTouchOutside(false);
                        dialog3.setCancelable(false);
                        tv_phone.setVisibility(View.INVISIBLE);
                        title_tv.setText("该账号存在安全风险,禁止提现操作,请联系客户经理解决");
                        btn_kefu.setText("联系客户经理");
                        btn_kefu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callPhone("18337101357");
                                dialog3.dismiss();
                            }
                        });
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog3.dismiss();
                            }
                        });
                    }else {
                        if ("非实名用户账号不可发放".equals(err_code_des)){
                            Toast.makeText(TiXianActivity.this, "微信账号没有实名认证,请先实名认证微信账号后提现！", Toast.LENGTH_LONG).show();
                        }else if ("该用户今日付款次数超过限制,如有需要请登录微信支付商户平台更改API安全配置.".equals(err_code_des)) {
                            Toast.makeText(TiXianActivity.this, "提现失败，有问题可联系客服", Toast.LENGTH_LONG).show();
                        }else if ("真实姓名不一致.".equalsIgnoreCase(err_code_des)) {

                            //存本地一个标识记录用户没有实名，没有实名下次点击提现直接弹出实名框，
                            SharedPreferences sp = getSharedPreferences("sangu_RealNameAuth_info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sp.edit();
                            editor1.putString("realAuth", "no");
                            editor1.commit();

                            Toast.makeText(TiXianActivity.this, "当前帐号与微信实名认证不一致", Toast.LENGTH_LONG).show();

                        }else if ("余额不足".equals(err_code_des)) {

                            Date date = new Date(System.currentTimeMillis());
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                            String currentTime = dateFormat.format(date);

                            SharedPreferences sp = getSharedPreferences("sangu_RealNameAuth_info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = sp.edit();
                            editor1.putString("withdrawTime", currentTime);
                            editor1.commit();

                            Toast.makeText(TiXianActivity.this, "已提交申请，工作日24小时后提取", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(TiXianActivity.this, "微信服务器繁忙,请稍后重试!.", Toast.LENGTH_LONG).show();
                        }

                    }
                    if (state==null){
                        Toast.makeText(TiXianActivity.this, "微信客户端繁忙", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (dialog!=null&&dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(TiXianActivity.this,"网络连接错误",Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("partner_trade_no",getNowTime());
                param.put("spbill_create_ip",getHostIP());
                param.put("openid",openId);
                param.put("re_user_name", finalName);
                param.put("merid", id);
                param.put("amount",finalBalance);
                param.put("desc", "正事多平台提现");
                return param;
            }
        };
        MySingleton.getInstance(TiXianActivity.this).addToRequestQueue(request);
    }

    private void getRedCount() {
        String id = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_QUERY_HBCOUNT + id+"&type=01";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                double amount1=0,amount2=0,amount3=0;
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject dynamic = object.getJSONObject("dynamic");
                com.alibaba.fastjson.JSONObject company = object.getJSONObject("company");
                com.alibaba.fastjson.JSONObject homePage = object.getJSONObject("homePage");
                orderIdDyna = dynamic.getString("order_id");
                orderIdComp = company.getString("order_id");
                orderIdHome = homePage.getString("order_id");
                String transactionAmountDy = dynamic.getString("transaction_amount");
                String transactionAmountCo = company.getString("transaction_amount");
                String transactionAmountHo = homePage.getString("transaction_amount");
                amount1 = StringUtils.toDouble(transactionAmountDy,0.0);
                amount2 = StringUtils.toDouble(transactionAmountCo,0.0);
                amount3 = StringUtils.toDouble(transactionAmountHo,0.0);
                amountAll = amount1+amount2+amount3;
                getRemainRed();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgress!=null&&mProgress.isShowing()){
                    mProgress.dismiss();
                }
                ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接中断，同步失败");
                finish();
            }
        });
        MySingleton.getInstance(TiXianActivity.this).addToRequestQueue(request);
    }

    private void getRemainRed() {
        String url = FXConstant.URL_QUERY_SHARERED;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                JSONArray array = object.getJSONArray("userInfo");
                userCount = array.size();
                for (int i=0;i<userCount;i++){
                    com.alibaba.fastjson.JSONObject info = array.getJSONObject(i);
                    String shareRed = info.getString("shareRed");
                    double red = StringUtils.toDouble(shareRed,0.0);
                    String friendsNumber = info.getString("friendsNumber");
                    if (friendsNumber!=null){
                        String singleRedStr = friendsNumber.split("\\|")[0];
                        double singleRed = StringUtils.toDouble(singleRedStr,0.0);
                        if (singleRed>0){
                            redCount += (int)(red/singleRed);
                        }
                    }
                }
                SyAddressBookUtil.builder()
                        .loader(getApplicationContext())
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(List<KyqInfo> listsKyq, List<String> list_selected, org.json.JSONArray arrayKyq,double per) {
                                kyqIds = list_selected;
                                inserttoSer();
                            }
                        })
                        .error(new IError() {
                            @Override
                            public void onError(String msg) {
                                if (mProgress!=null&&mProgress.isShowing()){
                                    mProgress.dismiss();
                                }
                                ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接中断，同步失败");
                                finish();
                            }
                        })
                        .build()
                        .getKyqList();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgress!=null&&mProgress.isShowing()){
                    mProgress.dismiss();
                }
                ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接中断，同步失败");
                finish();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void inserttoSer() {
        int size = StringUtils.getBetweenDays("20171129","20171202").size();
        Log.e("tixianac,kyid","进入同步"+size);
        if (kyqIds==null||kyqIds.size()==0){
            if (mProgress!=null&&mProgress.isShowing()){
                mProgress.dismiss();
            }
            Log.e("tixianac,kyid","没有可同步id");
            ToastUtils.showNOrmalToast(getApplicationContext(),"没有可同步的通讯录");
            finish();
        }else {
            Log.e("tixianac,kyid","进入同步id"+kyqIds.size());
            String list = null;
            for (int i = 0; i < kyqIds.size(); i++) {
                String num = kyqIds.get(i);
                if (num.startsWith("+86")) {
                    num = num.substring(3, num.length());
                }
                if (num.startsWith("[+86")) {
                    num = num.substring(4, num.length());
                }
                if (num.startsWith("[")) {
                    num = num.substring(1, num.length());
                }
                if (num.endsWith("]")) {
                    num = num.substring(0, num.length() - 1);
                }
                if (num.length() == 11) {
                    if (i == 0) {
                        list = num;
                    } else {
                        list = list + "," + num;
                    }
                }
            }
            if (list == null || list.length() == 0) {
                Toast.makeText(getApplicationContext(), "请同意访问通讯录权限!", Toast.LENGTH_SHORT).show();
                return;
            }
            final String id = DemoHelper.getInstance().getCurrentUsernName();
            String url = FXConstant.URL_INSERT_INVITE;

            String aaArray[] = list.split(",");
            HashSet<String> hs = new HashSet<String>();
            for(String s : aaArray){
                hs.add(s);
            }
            Iterator<String> it = hs.iterator();
            String aa_ = "";
            if(it.hasNext()){
                try {
                    aa_ = hs.toString().replace("[", "").replace("]", "");//去除相同项的字符串
                    aa_ = aa_.replace(", ", ",");
                } catch (Exception e) {
                    e.printStackTrace();
                    aa_ = hs.toString().replace("[", "").replace("]", "");//去除相同项的字符串
                }
            }
            final String finalList = aa_;
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String code = object.getString("code");
                        Log.e("addreliac,s", s);
                        if ("success".equals(code)) {
                            SharedPreferences mSharedPreference = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
                            SharedPreferences.Editor meditor = mSharedPreference.edit();
                            meditor.putString("tiXianTime", getNowTime2());
                            meditor.commit();
                            duanxin(600);
                        } else {
                            if (mProgress!=null&&mProgress.isShowing()){
                                mProgress.dismiss();
                            }
                            ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接中断，同步失败");
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (mProgress!=null&&mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                    ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接中断，同步失败");
                    finish();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.e("addreliac,l", finalList);
                    Map<String, String> param = new HashMap<>();
                    param.put("u_id", id);
                    param.put("inviteId", finalList);//"13513895563,17729794711"
                    return param;
                }
            };
            MySingleton.getInstance(TiXianActivity.this).addToRequestQueue(request);
        }
    }

    private void duanxin(final int size) {
        if (kyqIds==null||kyqIds.size()==0){
            if (mProgress!=null&&mProgress.isShowing()){
                mProgress.dismiss();
            }
            ToastUtils.showNOrmalToast(getApplicationContext(),"没有可同步的通讯录");
            finish();
        }else {
            String list = null;
            int inviteSize = size;
            if (kyqIds.size() <= size) {
                inviteSize = kyqIds.size();
            }
            for (int i = size - 600; i < inviteSize; i++) {
                String num = kyqIds.get(i);
                if (num.startsWith("+86")) {
                    num = num.substring(3, num.length());
                }
                if (num.startsWith("[+86")) {
                    num = num.substring(4, num.length());
                }
                if (num.startsWith("[")) {
                    num = num.substring(1, num.length());
                }
                if (num.endsWith("]")) {
                    num = num.substring(0, num.length() - 1);
                }
                if (num.length() == 11) {
                    if (i == size - 600) {
                        list = num;
                    } else {
                        list = list + "," + num;
                    }
                }
            }
            if (list == null || list.length() == 0) {
                ToastUtils.showNOrmalToast(getApplicationContext(),"没有可同步的通讯录");
                finish();
            }
            final String id = DemoHelper.getInstance().getCurrentUsernName();
            final String name = DemoApplication.getInstance().getCurrentUser().getName();
            final String url = FXConstant.URL_DUANXIN_TONGZHI;
            final String finalList = list;
            final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (kyqIds.size() <= size) {
                        if (mProgress != null && mProgress.isShowing()) {
                            mProgress.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "同步成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        int s2 = size + 600;
                        duanxin(s2);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (mProgress != null && mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                    ToastUtils.showNOrmalToast(getApplicationContext(),"没有可同步的通讯录");
                    finish();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    if (name == null || "".equals(name)) {
                        param.put("message", "【正事多】我（"+id+"）在正事多分享名片红包"+orderIdHome+"次企业"+orderIdComp+"次动态红包"+orderIdDyna+"次，共赚"+amountAll+"元，没套路真能提现，还有"+userCount+"个人"+redCount+"个红包可转");
                    } else {
                        param.put("message", "【正事多】我（"+name+"）在正事多分享名片红包"+orderIdHome+"次企业"+orderIdComp+"次动态红包"+orderIdDyna+"次，共赚"+amountAll+"元，没套路真能提现，还有"+userCount+"个人"+redCount+"个红包可转");
                    }
                    param.put("telNum", finalList);
                    Log.e("tixianac,p", param.toString());
                    return param;
                }
            };
            UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
                @Override
                public void onAllow() {
                    MySingleton.getInstance(TiXianActivity.this).addToRequestQueue(request);

                }

                @Override
                public void onBan() {
                    if (mProgress != null && mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                    ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

                }
            });
        }
    }

    private String getNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }
    private String getNowTime2() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
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
//    private void updatebmob(final String balance) {
//        WeakReference<TiXianActivity> reference = new WeakReference<TiXianActivity>(TiXianActivity.this);
//        final String jinE = Double.parseDouble(balance)/100+"";
//        String id = DemoHelper.getInstance().getCurrentUsernName();
//        String name = DemoApplication.getInstance().getCurrentUser().getName();
//        WithdrawalRecord recharge = new WithdrawalRecord();
//        recharge.setUserId(id);
//        recharge.setUserName(TextUtils.isEmpty(name)?id:name);
//        recharge.setAmount(jinE);
//        recharge.save(reference.get(),new SaveListener() {
//            @Override
//            public void onSuccess() {
//                finish();
//            }
//
//            @Override
//            public void onFailure(int i, String s) {
//                finish();
//                if (s!=null) {
//                    Log.e("tixian,e", s);
//                }
//            }
//        });
//    }

    private void initView2() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                SharedPreferences sp = getSharedPreferences("sangu_RealNameAuth_info", Context.MODE_PRIVATE);
                String withdrawTime = sp.getString("withdrawTime","0");
                String isWithDraw = "";
                try {
                    Date date1 = dateFormat.parse(withdrawTime);

                    if (!withdrawTime.equals("0") && BigInteger.valueOf((date.getTime() - date1.getTime())/1000).compareTo(BigInteger.valueOf(3600*24))<0){

                        //小于24小时  不能提取
                        isWithDraw = "no";

                    }else {

                        isWithDraw = "yes";
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                    isWithDraw = "yes";
                }

                if (isWithDraw.equals("yes")){

                    double price1 = DemoApplication.getInstance().getCurrenQiyePrice();
                    String sum = et_jine.getText().toString().trim();
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
                        if (sum!=null&&Double.parseDouble(sum)>0) {
                            final double orderSu = Double.valueOf(sum);
                            final Double d = price1 - orderSu;
                            if (d >= 0) {
                                LayoutInflater inflaterDl = LayoutInflater.from(TiXianActivity.this);
                                final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
                                final Dialog dialog = new AlertDialog.Builder(TiXianActivity.this,R.style.Dialog).create();
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
                                        final ProgressDialog pd = new ProgressDialog(TiXianActivity.this);
                                        pd.setMessage("正在请求提现...");
                                        pd.setCanceledOnTouchOutside(false);
                                        pd.show();
                                        if (pwdView.getStrPassword().equals(pass)) {
                                            dialog.dismiss();
                                            String url = FXConstant.URL_ZHUANZHANG;
                                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String s) {
                                                    pd.dismiss();
                                                    Toast.makeText(TiXianActivity.this,"提现成功",Toast.LENGTH_SHORT).show();
                                                    DemoApplication.getInstance().saveCurrentPrice(d);
                                                    setResult(RESULT_OK);
                                                    finish();
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    dialog.dismiss();
                                                    Toast.makeText(TiXianActivity.this,"提现失败",Toast.LENGTH_SHORT).show();
                                                }
                                            }){
                                                @Override
                                                protected Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String,String> param = new HashMap<>();
                                                    param.put("merId",DemoHelper.getInstance().getCurrentUsernName());
                                                    param.put("balance",et_jine.getText().toString().trim());
                                                    param.put("userId", DemoApplication.getInstance().getCurrentQiYeId());
                                                    param.put("qrCode","1");
                                                    return param;
                                                }
                                            };
                                            MySingleton.getInstance(TiXianActivity.this).addToRequestQueue(request);
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
                                        startActivity(new Intent(TiXianActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
                                    }
                                });
                            } else {
                                Toast.makeText(TiXianActivity.this, "账户余额不足，请充值！", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(TiXianActivity.this, "请输入金额！", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        ToastUtils.showNOrmalToast(TiXianActivity.this.getApplicationContext(), "您的账户已被冻结");
                    }

                }else {

                    LayoutInflater inflaterDl = LayoutInflater.from(TiXianActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(TiXianActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    tv_title.setText("您已经提交过申请,请勿重复提交,上次提交申请工作日24小时后再次操作即可提现");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });

                }

            }
        });
    }

    private void showErrorLing() {
        LayoutInflater inflaterDl = LayoutInflater.from(TiXianActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
        final Dialog dialog2 = new AlertDialog.Builder(TiXianActivity.this,R.style.Dialog).create();
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
        LayoutInflater inflater1 = LayoutInflater.from(TiXianActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(TiXianActivity.this,R.style.Dialog).create();
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
                startActivity(new Intent(TiXianActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
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
        LayoutInflater inflater1 = LayoutInflater.from(TiXianActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(TiXianActivity.this,R.style.Dialog).create();
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
                startActivity(new Intent(TiXianActivity.this, XiuGaiZFActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",pass));
            }
        });
    }

    private void showErrorMiMaSHZH() {
        LayoutInflater inflater1 = LayoutInflater.from(TiXianActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(TiXianActivity.this,R.style.Dialog).create();
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
                startActivity(new Intent(TiXianActivity.this,ZhiFuSettingActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",pass));
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
                if ("01".equals(biaoshi)){
                    params1.put("merId", DemoApplication.getInstance().getCurrentQiYeId());
                }else {
                    params1.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                }
                params1.put("enterErrorTimes", times+"");
                return params1;
            }
        };
        MySingleton.getInstance(TiXianActivity.this).addToRequestQueue(request3);
    }

    @Override
    public void updateCurrentPrice(Object success) {
        try {
            JSONObject object = (JSONObject) success;
            errorTime = object.getInt("enterErrorTimes");
            totalWithdrawals = object.getString("totalWithdrawals");
            totalCashWithdrawal = object.getString("totalCashWithdrawal");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("01".equals(biaoshi)){
            price = DemoApplication.getInstance().getCurrenQiyePrice();
            pass = DemoApplication.getInstance().getCurrentQiyePayPass();
        }else {
            price = DemoApplication.getInstance().getCurrenPrice();
            pass = DemoApplication.getInstance().getCurrentPayPass();
        }
        if (totalCashWithdrawal!=null&&price>Double.parseDouble(totalCashWithdrawal)){
            price = Double.parseDouble(totalCashWithdrawal);
        }
        final String str = String.format("%.2f", price);
        tv_jine.setText("可提现余额"+str+"元");
        tv_jine.setTextColor(Color.GRAY);
        tv_tixian_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_jine.setText(price+"");
            }
        });
        et_jine.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters={new CashierInputFilter()};
        et_jine.setFilters(filters);
        et_jine.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable edt){
                String temp = edt.toString();
                if (temp!=null&&!"".equals(temp)){
                    if (Double.parseDouble(temp)>price){
                        tv_jine.setText("金额已超过可提现余额");
                        tv_jine.setTextColor(Color.RED);
                    }else {
                        tv_jine.setText("可提现余额"+str+"元");
                        tv_jine.setTextColor(Color.GRAY);
                    }
                }
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
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("01")) {
            et_jine.setEnabled(false);
            btnNext.setEnabled(false);
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    private void callPhone(final String info) {
        PermissionUtil permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                new PermissionListener() {
                    @Override
                    public void onGranted() {

                        UserPermissionUtil.getUserPermission(TiXianActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "2", new UserPermissionUtil.UserPermissionListener() {
                            @Override
                            public void onAllow() {
                                //改变派单状态
                                //所有权限都已经授权
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                //url:统一资源定位符
                                //uri:统一资源标示符（更广）
                                intent.setData(Uri.parse("tel:" + info));
                                //开启系统拨号器
                                TiXianActivity.this.startActivity(intent);
                            }

                            @Override
                            public void onBan() {
                                ToastUtils.showNOrmalToast(TiXianActivity.this.getApplicationContext(), "您的账户已被禁止打聊天");

                            }
                        });


                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        //Toast第一个被拒绝的权限
                        Toast.makeText(TiXianActivity.this.getApplicationContext(), "您拒绝了拨打电话的权限！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        //Toast第一个勾选不在提示的权限
                        Toast.makeText(TiXianActivity.this.getApplicationContext(), "您拒绝了拨打电话的权限,请前往设置手动打开！", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
