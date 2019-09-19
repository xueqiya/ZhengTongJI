package com.sangu.apptongji.main.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.db.DemoDBManager;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.fragment.MainActivity;
import com.sangu.apptongji.main.fragment.MainTwoActivity;
import com.sangu.apptongji.main.service.ContactsService;
import com.sangu.apptongji.main.utils.CountDownButtonHelper;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.CodeUtil;
import com.xiaomi.mimc.MIMCUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-11-22.
 */

public class LoginWithDXActivity extends BaseActivity {
    private EditText et_phone;
    private EditText et_yanzheng;
    private Button btn_yanzheng;
    private Button btn_next;
    private String number, str;
    private String lng = null, lat = null, uLocation = null, city = null;
    CountDownButtonHelper helper = null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.login_with_duanxin);
        uLocation = getIntent().getStringExtra("uLocation");
        lng = getIntent().getStringExtra("lng");
        lat = getIntent().getStringExtra("lat");
        city = getIntent().getStringExtra("city");
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_yanzheng = (EditText) findViewById(R.id.et_yanzheng);
        btn_yanzheng = (Button) findViewById(R.id.btn_yanzheng);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_yanzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(LoginWithDXActivity.this, "输入电话之后才能获取验证码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number.length() != 11) {
                    Toast.makeText(LoginWithDXActivity.this, "请输入正确格式的电话号码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                loadUserInfo(number, 0);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginId = et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(loginId)) {
                    Toast.makeText(LoginWithDXActivity.this, "输入电话之后才能获取验证码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (loginId.length() != 11) {
                    Toast.makeText(LoginWithDXActivity.this, "请输入正确格式的电话号码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number != null && !number.equals(loginId)) {
                    Toast.makeText(LoginWithDXActivity.this, "手机号与获取验证码的不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String yanzheng = et_yanzheng.getText().toString().trim();
                if (TextUtils.isEmpty(yanzheng)) {
                    Toast.makeText(LoginWithDXActivity.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (str != null && str.equals(yanzheng)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    loadUserInfo(loginId, 1);
                } else {
                    Toast.makeText(LoginWithDXActivity.this, "验证码错误！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadUserInfo(final String loginId, final int type) {
        String url = FXConstant.URL_Get_UserInfo + loginId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                    String code = jsonObject.getString("code");
                    if ("用户名为空".equals(code)) {
                        Toast.makeText(getApplicationContext(), "该账号不存在!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (type == 1) {
                        org.json.JSONObject object = jsonObject.getJSONObject("userInfo");
                        String passWord = object.getString("uPassword");
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
                        updateTime(loginId);
                        loginInSever(loginId, passWord, qiyeId);
                    } else {
                        receiveDx();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("-----失败----", "" + volleyError.getMessage());
                Toast.makeText(getApplicationContext(), "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(LoginWithDXActivity.this).addToRequestQueue(request);
    }

    private void updateTime(final String loginId) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        final String date = sDateFormat.format(curDate);
        final String brand = android.os.Build.BRAND;
        final String model = android.os.Build.MODEL;
        String url = FXConstant.URL_UPDATE_TIME;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uLoginId", loginId);
                params.put("loginTime", date);
                if (uLocation != null) {
                    params.put("uLocation", uLocation);
                }
                params.put("deviceType", "android4.5正事多" + brand + model);
                if (lng != null) {
                    params.put("lng", lng);
                }
                if (lat != null) {
                    params.put("lat", lat);
                }
                if (city != null) {
                    params.put("region", city);
                }
                return params;
            }
        };
        MySingleton.getInstance(LoginWithDXActivity.this).addToRequestQueue(request);
    }

    private void loginInSever(final String loginId, final String passWord, final String qiyeId) {
        final ProgressDialog pd = new ProgressDialog(LoginWithDXActivity.this);
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
                    if (code.equals("SUCCESS")) {
                        SharedPreferences sp = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = sp.edit();
                        editor1.putBoolean("isLogedIn", true);
                        editor1.putString("userId", loginId);
                        editor1.putString("passWord", passWord);
                        editor1.putString("qiyeId", qiyeId);
                        editor1.commit();
                        //finish();
                        loginHuanXin(loginId, passWord);
                        loginMimcInfo(loginId,passWord);
                    } else if (code.equals("1010") || code.equals("1011")) {
                        //pd.dismiss();
                        Toast.makeText(LoginWithDXActivity.this, "帐号或密码错误", Toast.LENGTH_SHORT).show();
                    } else if (code.equals("1009")) {
                        //pd.dismiss();
                        Toast.makeText(LoginWithDXActivity.this, "该手机号未被注册", Toast.LENGTH_SHORT).show();
                    } else if (code.equals("该用户已被禁止登录")) {
                        Toast.makeText(LoginWithDXActivity.this, "您已被禁止登陆,请联系客服", Toast.LENGTH_SHORT).show();
                    } else {
                        //pd.dismiss();
                        Toast.makeText(LoginWithDXActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uLoginId", loginId);
                params.put("uPassword", passWord);
                return params;
            }
        };
        MySingleton.getInstance(LoginWithDXActivity.this).addToRequestQueue(request);
    }

    private void loginMimcInfo(String usertel,String password){

        MIMCUser user = UserManager.getInstance().newUser(usertel,LoginWithDXActivity.this);
        if (user != null) {

            user.login();

        }
    }
    private void loginHuanXin(String usertel, String password) {
        final ProgressDialog pd = new ProgressDialog(LoginWithDXActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("LoginWithDXActivity", "EMClient.getInstance().onCancel");
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        if (pd != null && !pd.isShowing()) {
            pd.show();
        }
        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();
        // reset current user name before login
        DemoHelper.getInstance().setCurrentUserName(usertel);
        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        Log.d("LoginWithDXActivity", "EMClient.getInstance().login");
        EMClient.getInstance().login(usertel, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.d("LoginWithDXActivity", "login: onSuccess");
                if (!LoginWithDXActivity.this.isFinishing() && pd.isShowing()) {
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
                startService(new Intent(LoginWithDXActivity.this, ContactsService.class));
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (MainTwoActivity.instance != null) {
                            MainTwoActivity.instance.finish();
                        }
                        // 进入主页面
                        Intent intent = new Intent(LoginWithDXActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_LONG).show();
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d("LoginWithDXActivity", "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d("LoginWithDXActivity", "login: onError: " + code);
                startService(new Intent(LoginWithDXActivity.this, ContactsService.class));
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(LoginWithDXActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        if (MainTwoActivity.instance != null) {
                            MainTwoActivity.instance.finish();
                        }
                        // 进入主页面
                        Intent intent = new Intent(LoginWithDXActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    private void receiveDx() {
        //int numcode = (int) ((Math.random() * 9 + 1) * 100000);
        //str = String.valueOf(numcode);
        //Log.e("loginWithDxac,st", str);
        helper = new CountDownButtonHelper(btn_yanzheng, 60, 1);
        String url = FXConstant.URL_AUTHCODE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
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
                        Toast.makeText(LoginWithDXActivity.this, "操作频繁！", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        final String code = CodeUtil.getAuthCode(code1, code2);
                        if (TextUtils.isEmpty(code)) {
                            Toast.makeText(LoginWithDXActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
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
                                param.put("uId", number.trim());
                                param.put("deviceStr", code);
                                return param;
                            }
                        };

                        MySingleton.getInstance(LoginWithDXActivity.this).addToRequestQueue(request2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("uId", number.trim());
                return param;
            }
        };
        MySingleton.getInstance(LoginWithDXActivity.this).addToRequestQueue(request);
        helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
            @Override
            public void finish() {
//                        Toast.makeText(WJDLPaActivity.this, "请重新获取验证码！", Toast.LENGTH_LONG).show();
            }
        });
        helper.start();
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}
