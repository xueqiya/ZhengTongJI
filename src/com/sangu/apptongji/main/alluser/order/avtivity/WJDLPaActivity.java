package com.sangu.apptongji.main.alluser.order.avtivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.CountDownButtonHelper;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.CodeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/9/9.
 */

public class WJDLPaActivity extends BaseActivity implements View.OnClickListener{
    private EditText etTell=null;
    private EditText etYanzheng=null;
    private EditText etXinmima=null;
    private Button btnTijiao=null;
    private ImageView iv_back=null;
    private Button btn=null;
    private String str,number;
    CountDownButtonHelper helper=null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wangji_mima);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        btn = (Button) findViewById(R.id.btn);
        etTell = (EditText) findViewById(R.id.et_shoujihao);
        etYanzheng = (EditText) findViewById(R.id.et_yanzhengma);
        etXinmima = (EditText) findViewById(R.id.et_xindepass);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btnTijiao = (Button) findViewById(R.id.btn_tijiao);

        btn.setOnClickListener(this);
        btnTijiao.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void back(View view) {
        if (helper!=null){
            helper = null;
        }
        super.back(view);
    }

    private void loadUserInfo(final String loginId) {
        String url = FXConstant.URL_Get_UserInfo+loginId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String data) {
                try {
                    org.json.JSONObject jsonObject = new org.json.JSONObject(data);
                    String code = jsonObject.getString("code");
                    if ("用户名为空".equals(code)){
                        Toast.makeText(getApplicationContext(),"该账号不存在!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    /*String s = "";
                    s = String.valueOf(Math.random()*9000+1000);
                    str = s.substring(0,s.indexOf("."));*/
                    helper = new CountDownButtonHelper(btn, 60, 1);
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
                                    Toast.makeText(WJDLPaActivity.this, "操作频繁！", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    final String code = CodeUtil.getAuthCode(code1, code2);
                                    if (TextUtils.isEmpty(code)) {
                                        Toast.makeText(WJDLPaActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
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

                                    MySingleton.getInstance(WJDLPaActivity.this).addToRequestQueue(request2);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            /*try {
                                JSONObject object = new JSONObject(s);
                                String code = object.getString("code");
                                if (code.equals("SUCCESS")){
                                    Toast.makeText(WJDLPaActivity.this,"获取短信验证码成功！",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(WJDLPaActivity.this,"获取短信验证码失败！",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> param = new HashMap<>();
                            param.put("uId", number.trim());
                            /*param.put("uTelephone",number);
                            param.put("authCode",str);*/
                            return param;
                        }
                    };
                    MySingleton.getInstance(WJDLPaActivity.this).addToRequestQueue(request);
                    helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                        @Override
                        public void finish() {
//                        Toast.makeText(WJDLPaActivity.this, "请重新获取验证码！", Toast.LENGTH_LONG).show();
                        }
                    });
                    helper.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("-----失败----",""+volleyError.getMessage());
                Toast.makeText(getApplicationContext(),"网络连接中断",Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(WJDLPaActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                number = etTell.getText().toString().trim();
                if (TextUtils.isEmpty(number)){
                    Toast.makeText(WJDLPaActivity.this,"输入电话之后才能获取验证码！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number.length()!=11){
                    Toast.makeText(WJDLPaActivity.this,"请输入正确格式的电话号码！",Toast.LENGTH_SHORT).show();
                    return;
                }
                loadUserInfo(number);
                break;
            case R.id.btn_tijiao:
                final String usertel1 = etTell.getText().toString().trim();
                final String yanzheng = etYanzheng.getText().toString().trim();
                final String xPass = etXinmima.getText().toString().trim();
                if (!usertel1.equals(number)){
                    Toast.makeText(WJDLPaActivity.this,"账号不匹配,请重新输入！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!TextUtils.isEmpty(xPass)) {
                    if (yanzheng.equals(str)) {
                        String url1 = FXConstant.URL_UPDATE_DLMM;
                        StringRequest request1 = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject object = new JSONObject(s);
                                    String code = object.getString("code");
                                    if (code.equals("SUCCESS")) {
                                        Toast.makeText(WJDLPaActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                                        DemoApplication.getInstance().saveCurrentPayPass(xPass);
                                        finish();
                                    } else {
                                        Toast.makeText(WJDLPaActivity.this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
//                                Log.i("修改密码失败返回的值", volleyError.getMessage());
                                Toast.makeText(WJDLPaActivity.this, "网络连接错误！", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("password", xPass);
                                params.put("uLoginId", usertel1);
                                return params;
                            }
                        };
                        MySingleton.getInstance(WJDLPaActivity.this).addToRequestQueue(request1);
                    } else {
                        Toast.makeText(WJDLPaActivity.this, "验证码错误，请重新输入！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(WJDLPaActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
