package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.CodeUtil;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-09-11.
 */

public class RegisterTwoActivity extends BaseActivity {
    public static RegisterTwoActivity instance = null;
    private EditText et_phone;
    private EditText et_mima;
    private Button btn_next;
    private ImageView iv_hide = null;
    private ImageView iv_show = null;
    private String name, zhuaYe, birth, sex, filePath, phone, mima, str, age;
    private boolean isNoZY;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_register_two);
        instance = this;
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        age = getIntent().getStringExtra("age");
        name = getIntent().getStringExtra("name");
        zhuaYe = getIntent().getStringExtra("zhuaYe");
        birth = getIntent().getStringExtra("birth");
        sex = getIntent().getStringExtra("sex");
        filePath = getIntent().getStringExtra("filePath");
        isNoZY = getIntent().getBooleanExtra("isNoZY", true);
        SharedPreferences sp = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        if (!sp.contains("isLogedIn")) {
            bundAndroidId();
        }
        initView();
        setListener();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("deviceStr", "android" + ANDROID_ID);
                param.put("resv3", date);
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void setListener() {
        et_phone.setInputType(InputType.TYPE_CLASS_NUMBER);
        iv_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_hide.setVisibility(View.GONE);
                iv_show.setVisibility(View.VISIBLE);
                et_mima.setTransformationMethod(HideReturnsTransformationMethod
                        .getInstance());
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = et_mima.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        iv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_show.setVisibility(View.GONE);
                iv_hide.setVisibility(View.VISIBLE);
                et_mima.setTransformationMethod(PasswordTransformationMethod
                        .getInstance());
                // 切换后将EditText光标置于末尾
                CharSequence charSequence = et_mima.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = et_phone.getText().toString().trim();
                mima = et_mima.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || phone.length() != 11) {
                    Toast.makeText(RegisterTwoActivity.this, "请输入正确格式的电话号码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mima) || mima.length() < 3) {
                    Toast.makeText(RegisterTwoActivity.this, "请输入密码，密码不得少于3位！", Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater4 = LayoutInflater.from(RegisterTwoActivity.this);
                RelativeLayout layout4 = (RelativeLayout) inflater4.inflate(R.layout.dialog_alert, null);
                final Dialog dialog4 = new AlertDialog.Builder(RegisterTwoActivity.this).create();
                dialog4.show();
                dialog4.getWindow().setContentView(layout4);
                dialog4.setCanceledOnTouchOutside(true);
                dialog4.setCancelable(true);
                TextView tv_title = (TextView) layout4.findViewById(R.id.tv_title);
                TextView title_tv4 = (TextView) layout4.findViewById(R.id.title_tv);
                Button btnCancel4 = (Button) layout4.findViewById(R.id.btn_cancel);
                final Button btnOK4 = (Button) layout4.findViewById(R.id.btn_ok);
                btnOK4.setText("确定");
                btnCancel4.setText("取消");
                tv_title.setText("请确认当前手机号:");
                title_tv4.setText(phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7, 11));
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
                        String url1 = FXConstant.URL_Get_UserInfo + phone;
                        StringRequest request1 = new StringRequest(url1, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    org.json.JSONObject jsonObject = new org.json.JSONObject(s);
                                    String code = jsonObject.getString("code");
                                    if (code.equalsIgnoreCase("SUCCESS")) {
                                        ToastUtils.showNOrmalToast(getApplicationContext(), "该手机号已注册！");
                                    } else if (code.equalsIgnoreCase("用户名为空")) {
                                        /*int numcode = (int) ((Math.random() * 9 + 1) * 100000);
                                        str = String.valueOf(numcode);*/


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
                                                        Toast.makeText(RegisterTwoActivity.this, "操作频繁！", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    } else {
                                                        final String code = CodeUtil.getAuthCode(code1, code2);
                                                        if (TextUtils.isEmpty(code)) {
                                                            Toast.makeText(RegisterTwoActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }

                                                        String url = FXConstant.URL_SEND_AUTHCODE;
                                                        StringRequest request2 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                Log.d("chen", "发送验证码成功" + s);
                                                                startActivity(new Intent(RegisterTwoActivity.this, RegisterActivity.class)
                                                                        .putExtra("name", name).putExtra("zhuaYe", zhuaYe).putExtra("birth", birth)
                                                                        .putExtra("sex", sex).putExtra("filePath", filePath).putExtra("age", age)
                                                                        .putExtra("phone", phone).putExtra("mima", mima).putExtra("str", str).putExtra("isNoZY", isNoZY));
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

                                                        MySingleton.getInstance(RegisterTwoActivity.this).addToRequestQueue(request2);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                                Toast.makeText(getApplicationContext(), "网络连接中断", Toast.LENGTH_SHORT).show();
                                            }
                                        }) {
                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {
                                                Map<String, String> param = new HashMap<>();
                                                param.put("uId", phone.trim());
                                                /*param.put("uTelephone",phone);
                                                param.put("authCode",str);*/
                                                return param;
                                            }
                                        };
                                        MySingleton.getInstance(RegisterTwoActivity.this).addToRequestQueue(request);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Log.d("-----失败----", "" + volleyError.getMessage());
                                Toast.makeText(getApplicationContext(), "该账号不存在!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        MySingleton.getInstance(RegisterTwoActivity.this).addToRequestQueue(request1);


                    }
                });
            }
        });
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_mima = (EditText) findViewById(R.id.et_mima);
        btn_next = (Button) findViewById(R.id.btn_next);
        iv_hide = (ImageView) findViewById(R.id.iv_hide);
        iv_show = (ImageView) findViewById(R.id.iv_show);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}
