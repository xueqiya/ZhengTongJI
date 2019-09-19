/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sangu.apptongji.main.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.utils.UserManager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.db.DemoDBManager;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.fragment.MainActivity;
import com.sangu.apptongji.main.fragment.MainTwoActivity;
import com.sangu.apptongji.main.service.ContactsService;
import com.sangu.apptongji.main.utils.CountDownTextViewHelper;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.IdentifyingCodeView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.CodeUtil;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.xiaomi.mimc.MIMCUser;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sangu.apptongji.main.utils.OkHttpManager.context;


/**
 * 注册页
 */
public class RegisterActivity extends BaseActivity {
    private RelativeLayout rl_button,rl_zhuce;
    private TextView tv_tishi;
    private TextView tv_phone;
    private Button btn_wancheng;
    private Button btn_congfa;
    private Button btn_kefu;
    private IdentifyingCodeView icv;
    String str=null;
    private int count = 0;
    private String name,zhuaYe,birth,sex,filePath,phone,mima,age;
    private boolean isNoZY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_register);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        rl_zhuce = (RelativeLayout) findViewById(R.id.rl_zhuce);
        rl_button = (RelativeLayout) findViewById(R.id.rl_button);
        tv_tishi = (TextView) findViewById(R.id.tv_tishi);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        btn_wancheng = (Button) findViewById(R.id.btn_wancheng);
        btn_congfa = (Button) findViewById(R.id.btn_congfa);
        btn_kefu = (Button) findViewById(R.id.btn_kefu);
        str = getIntent().getStringExtra("str");
        age = getIntent().getStringExtra("age");
        name = getIntent().getStringExtra("name");
        zhuaYe = getIntent().getStringExtra("zhuaYe");
        birth = getIntent().getStringExtra("birth");
        sex = getIntent().getStringExtra("sex");
        filePath = getIntent().getStringExtra("filePath");
        phone = getIntent().getStringExtra("phone");
        mima = getIntent().getStringExtra("mima");
        isNoZY = getIntent().getBooleanExtra("isNoZY",true);
        tv_phone.setText("验证码已经发送到: "+phone);
        icv = (IdentifyingCodeView) findViewById(R.id.icv);
        icv.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                count++;
                if (count>0){
                    rl_button.setVisibility(View.GONE);
                    rl_zhuce.setVisibility(View.VISIBLE);
                    btn_wancheng.setVisibility(View.VISIBLE);
                    if (count==6){
                        btn_wancheng.setBackgroundResource(R.drawable.fx_bg_btn_green);
                        btn_wancheng.setEnabled(true);
                        if (icv.getTextContent().equals(str)) {
                            zhuce();
                        }else {
                            Toast.makeText(getApplicationContext(),"验证码错误",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        btn_wancheng.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                        btn_wancheng.setEnabled(false);
                    }
                }else {
                    rl_button.setVisibility(View.VISIBLE);
                    rl_zhuce.setVisibility(View.GONE);
                }
            }
            @Override
            public void deleteContent() {
                count--;
                if (count==6){
                    btn_wancheng.setBackgroundResource(R.drawable.fx_bg_btn_green);
                    btn_wancheng.setEnabled(true);
                }else {
                    btn_wancheng.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                    btn_wancheng.setEnabled(false);
                }
                if (count==0){
                    rl_button.setVisibility(View.VISIBLE);
                    rl_zhuce.setVisibility(View.GONE);
                }else {
                    rl_button.setVisibility(View.GONE);
                    rl_zhuce.setVisibility(View.VISIBLE);
                }
            }
        });
        SharedPreferences sp = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        if (!sp.contains("isLogedIn")) {
            bundAndroidId();
        }
        setListener();
        btn_kefu.setVisibility(View.INVISIBLE);
        btn_congfa.setVisibility(View.INVISIBLE);
        CountDownTextViewHelper helper = new CountDownTextViewHelper(tv_tishi, 60, 1);
        helper.setOnFinishListener(new CountDownTextViewHelper.OnFinishListener() {
            @Override
            public void finish() {
                btn_kefu.setVisibility(View.VISIBLE);
                btn_congfa.setVisibility(View.VISIBLE);
            }
        });
        helper.start();

    }

    private void bundAndroidId() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date curDate = new Date(System.currentTimeMillis());
        final String date = sDateFormat.format(curDate);
        final String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        String url = FXConstant.URL_INSERT_READHIS;
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
                Map<String,String> param = new HashMap<>();
                param.put("deviceStr","android"+ANDROID_ID);
                param.put("resv4",date);
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void setListener() {
        btn_congfa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chongfa();
            }
        });
        btn_kefu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater2 = LayoutInflater.from(RegisterActivity.this);
                RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                final Dialog dialog2 = new AlertDialog.Builder(RegisterActivity.this,R.style.Dialog).create();
                dialog2.show();
                dialog2.getWindow().setContentView(layout2);
                dialog2.setCanceledOnTouchOutside(true);
                dialog2.setCancelable(true);
                TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                title2.setText("温馨提示");
                btnOK2.setText("确定");
                btnCancel2.setText("取消");
                title_tv2.setText("确定短信联系客服吗？");
                btnCancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                btnOK2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                         duanxintongzhi("【正事多】"+"用户"+name+"("+phone+")注册失败,联系客服");
                    }
                });
            }
        });
        btn_wancheng.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (icv.getTextContent().equals(str)) {
                    zhuce();
                }else {
                    Toast.makeText(getApplicationContext(),"验证码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void zhuce() {
        final String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        final ProgressDialog dialog = new ProgressDialog(
                RegisterActivity.this);
        dialog.setMessage("正在注册...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();
        ArrayList<String> imagePaths1 = new ArrayList<>();
        imagePaths1.add(filePath);
        List<Param> param=new ArrayList<>();
        param.add(new Param("uLoginId",phone));
        param.add(new Param("uPassword",mima));
        param.add(new Param("uBirthday",birth));
        param.add(new Param("upName",zhuaYe));
        param.add(new Param("name",name));
        param.add(new Param("sex",sex));
        param.add(new Param("uAge",age));
        param.add(new Param("deviceStr","android"+ANDROID_ID));  //传一个唯一标识符
        if (isNoZY) {
            param.add(new Param("userType","0"));
        }
        //192.168.1.120/user/register?uLoginId=15513994458&uPassword=123456&uBirthday=123456678&upName=123&name=123&sex=1&uAge=21&deviceStr=aa826da0ef035886
        String url = FXConstant.URL_REGISTER;
        OkHttpManager.getInstance().postMoments("uImage",param,imagePaths1,url,new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                Log.d("chen", jsonObject.toString());
                if (dialog!=null&&dialog.isShowing()) {
                    dialog.dismiss();
                }
                String code = jsonObject.getString("code");
                String u_id = jsonObject.getString("u_id");
                if (code.equals("SUCCESS")) {
                    dialog.dismiss();
                    updateDeviceType(phone,mima);

                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    if (RegisterOneActivity.instance!=null) {
                        RegisterOneActivity.instance.finish();
                    }
                    if (RegisterTwoActivity.instance!=null) {
                        RegisterTwoActivity.instance.finish();
                    }

                } else if (code.equals("1008")) {
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "手机号已被注册", Toast.LENGTH_SHORT).show();
                } else if (code.equalsIgnoreCase("禁止使用同一设备注册")) {
                    dialog.dismiss();
                    LayoutInflater inflater = LayoutInflater.from(RegisterActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_regist_error, null);
                    final AlertDialog dialog2 = new AlertDialog.Builder(RegisterActivity.this,R.style.Dialog).create();
                    TextView tv_phone = (TextView) layout.findViewById(R.id.tv_phone);
                    Button btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
                    Button btn_kefu = (Button) layout.findViewById(R.id.btn_kefu);
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(true);
                    dialog2.setCancelable(true);
                    tv_phone.setText(u_id);
                    btn_kefu.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            callPhone("4000010084");
                            dialog2.dismiss();
                        }
                    });
                    btn_cancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });

                    //Toast.makeText(RegisterActivity.this, "此设备已注册，无法使用同一设备注册", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(String errorMsg) {
                Log.e("服务器繁忙",errorMsg);
                if (dialog!=null&&dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDeviceType(final String phone, final String mima) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        final String date = sDateFormat.format(curDate);
        final String brand = android.os.Build.BRAND;
        final String model = android.os.Build.MODEL;
        String url = FXConstant.URL_UPDATE_TIME;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loadFriendlist(phone, mima);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadFriendlist(phone, mima);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("uLoginId",phone);
                params.put("loginTime",date);
                params.put("deviceType","android4.5正事多"+brand+model);
                return params;
            }
        };
        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);
    }

    private void loadFriendlist(final String loginId,final String pass) {
        String url = FXConstant.URL_Get_UserInfo+loginId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                    String code = jsonObject.getString("code");
                    if ("用户名为空".equals(code)){
                        Toast.makeText(getApplicationContext(),"该账号不存在!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    org.json.JSONObject object = jsonObject.getJSONObject("userInfo");
                    DemoApplication app = DemoApplication.getApp();
                    Userful user = JSONParser.parseUser(object);
                    app.saveCurrentUser(user);
                    String qiyeId = user.getResv5();
                    String remark = user.getuNation();
                    String resv6 = user.getResv6();
                    String comAddress = user.getCompanyAdress();
                    String companyName = user.getCompany();
                    String locationState = user.getLocationState();
                    app.setCurrentQiYeRemark(remark);//公司的审核状态
                    app.saveCurrentlocationState(locationState);
                    app.setCurrentCompanyName(companyName);
                    app.setCurrentcomAddress(comAddress);
                    app.setCurrentQiYeId(qiyeId);
                    app.setCurrentResv6(resv6);
                    loginInSever(loginId, pass,qiyeId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("-----失败----",""+volleyError.getMessage());
                ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接中断...");
            }
        });
        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);
    }

    /**
     * 显示地理位置经度和纬度信息
     * @param
     */
    private void loginInSever(final String tel, final String password, final String qiyeId) {
        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();
        String url = FXConstant.URL_LOGIN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    String code = object.getString("code");
                    pd.dismiss();
                    if(code.equals("SUCCESS")){
                        SharedPreferences sp = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sp.edit();
                        editor1.putBoolean("isLogedIn",true);
                        editor1.putString("userId",tel);
                        editor1.putString("passWord",password);
                        editor1.putString("qiyeId",qiyeId);
                        editor1.commit();
                        //finish();
                        loginHuanXin(tel, password);
                        loginMimcInfo(tel,password);
                    }else if (code.equals("1010")||code.equals("1011")) {
                        //pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "帐号或密码错误", Toast.LENGTH_SHORT).show();
                    }else if (code.equals("1009")) {
                        //pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "该手机号未被注册", Toast.LENGTH_SHORT).show();
                    }else if (code.equals("该用户已被禁止登录")){
                        Toast.makeText(RegisterActivity.this, "您已被禁止登陆,请联系客服", Toast.LENGTH_SHORT).show();
                    }else {
                        //pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("uLoginId",tel);
                params.put("uPassword",password);
                return params;
            }
        };
        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);
    }

    private void loginMimcInfo(String usertel,String password){

        MIMCUser user = UserManager.getInstance().newUser(usertel,RegisterActivity.this);
        if (user != null) {

            user.login();

        }
    }

    private void loginHuanXin(String usertel,String password){
        final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("registerac", "EMClient.getInstance().onCancel");
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        if (pd!=null&&!pd.isShowing()) {
            pd.show();
        }
        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();
        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(usertel);
        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        Log.d("registerac", "EMClient.getInstance().login");
        EMClient.getInstance().login(usertel, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("registerac", "login: onSuccess");
                if (!RegisterActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                // ** manually load all local groups and
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
//                if (!updatenick) {
//                    Log.e("LoginActivity", "update current user nick fail");
//                }
                startService(new Intent(RegisterActivity.this, ContactsService.class));
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        if (pd!=null&&pd.isShowing()){
                            pd.dismiss();
                        }
                        if (MainTwoActivity.instance!=null) {
                            MainTwoActivity.instance.finish();
                        }
                        // 进入主页面
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d("registerac", "login: onProgress");
            }
            @Override
            public void onError(final int code, final String message) {
                Log.d("registerac", "login: onError: " + code);
                startService(new Intent(RegisterActivity.this, ContactsService.class));
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        if (pd!=null&&pd.isShowing()){
                            pd.dismiss();
                        }
                        if (MainTwoActivity.instance!=null) {
                            MainTwoActivity.instance.finish();
                        }
                        // 进入主页面
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void chongfa() {
        //int numcode = (int) ((Math.random() * 9 + 1) * 100000);
        //str = String.valueOf(numcode);
        btn_congfa.setVisibility(View.INVISIBLE);
        btn_kefu.setVisibility(View.INVISIBLE);
        CountDownTextViewHelper helper = new CountDownTextViewHelper(tv_tishi, 60, 1);



        String url = FXConstant.URL_AUTHCODE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    String code1 = object.getString("code1");
                    String code2 = object.getString("code2");
                    String code3 = object.getString("code3");
                    String code4 = object.getString("code4");
                    String code5 = object.getString("code5");
                    str = code1;

                    /*
                    * {"code4":"944298","code3":"410647","code2":"710604","code1":"722045","code5":"706133"}
                    * */
                    if (code3.equalsIgnoreCase("操作频繁")) {
                        Toast.makeText(RegisterActivity.this, "操作频繁！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        final String code = CodeUtil.getAuthCode(code1, code2);
                        if (TextUtils.isEmpty(code)) {
                            Toast.makeText(RegisterActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String url = FXConstant.URL_SEND_AUTHCODE;
                        StringRequest request2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.d("chen", "发送验证码成功" + s);
                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<>();
                                param.put("uId", phone.trim());
                                param.put("deviceStr", code);
                                return param;
                            }
                        };

                        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

                /*JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if (code.equals("SUCCESS")){
                    Toast.makeText(RegisterActivity.this,"获取短信验证码成功！",Toast.LENGTH_SHORT).show();
                }else{
                    btn_congfa.setVisibility(View.VISIBLE);
                    btn_kefu.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this,"获取短信验证码失败！",Toast.LENGTH_SHORT).show();
                }*/

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络连接中断",Toast.LENGTH_SHORT).show();
                btn_congfa.setVisibility(View.VISIBLE);
                btn_kefu.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uId", phone.trim());
                return param;
            }
        };
        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);
        helper.setOnFinishListener(new CountDownTextViewHelper.OnFinishListener() {
            @Override
            public void finish() {
                btn_kefu.setVisibility(View.VISIBLE);
                btn_congfa.setVisibility(View.VISIBLE);
            }
        });
        helper.start();
    }

    private void duanxintongzhi(final String message) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(context, "通知成功,请等待客服回复！", Toast.LENGTH_SHORT).show();
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
                param.put("telNum","18337101357");
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            showdiaLog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showdiaLog() {
        LayoutInflater inflaterDl = LayoutInflater.from(RegisterActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.activity_back_tishi, null);
        final Dialog dialog2 = new AlertDialog.Builder(RegisterActivity.this,R.style.Dialog).create();
        dialog2.show();
        dialog2.getWindow().setContentView(layout);
        dialog2.setCanceledOnTouchOutside(true);
        dialog2.setCancelable(true);
        RelativeLayout rl_item1 = (RelativeLayout) dialog2.findViewById(R.id.rl_item1);
        RelativeLayout rl_item2 = (RelativeLayout) dialog2.findViewById(R.id.rl_item2);
        RelativeLayout rl_item3 = (RelativeLayout) dialog2.findViewById(R.id.rl_item3);
        RelativeLayout rl_item4 = (RelativeLayout) dialog2.findViewById(R.id.rl_item4);
        TextView tv_title = (TextView) dialog2.findViewById(R.id.tv_title);
        tv_title.setText("手机"+phone+"收不到验证码？你可以尝试以下方式");
        rl_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                chongfa();
            }
        });
        rl_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                LayoutInflater inflater2 = LayoutInflater.from(RegisterActivity.this);
                RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                final Dialog dialog3 = new AlertDialog.Builder(RegisterActivity.this,R.style.Dialog).create();
                dialog3.show();
                dialog3.getWindow().setContentView(layout2);
                dialog3.setCanceledOnTouchOutside(true);
                dialog3.setCancelable(true);
                TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                title2.setText("温馨提示");
                btnOK2.setText("确定");
                btnCancel2.setText("取消");
                title_tv2.setText("确定短信联系客服吗？");
                btnCancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                    }
                });
                btnOK2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                        duanxintongzhi("【正事多】"+"用户"+name+"("+phone+")注册失败,联系客服");
                    }
                });
            }
        });
        rl_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
                finish();
            }
        });
        rl_item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
    }
    private void callPhone(final String info) {
        PermissionUtil permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                new PermissionListener() {
                    @Override
                    public void onGranted() {
                        UserPermissionUtil.getUserPermission(RegisterActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "2", new UserPermissionUtil.UserPermissionListener() {
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
                                RegisterActivity.this.startActivity(intent);
                            }

                            @Override
                            public void onBan() {
                                ToastUtils.showNOrmalToast(RegisterActivity.this.getApplicationContext(), "您的账户已被禁止打聊天");

                            }
                        });

                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        //Toast第一个被拒绝的权限
                        Toast.makeText(RegisterActivity.this.getApplicationContext(), "您拒绝了拨打电话的权限！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        //Toast第一个勾选不在提示的权限
                        Toast.makeText(RegisterActivity.this.getApplicationContext(), "您拒绝了拨打电话的权限,请前往设置手动打开！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void back(View view) {
        showdiaLog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
