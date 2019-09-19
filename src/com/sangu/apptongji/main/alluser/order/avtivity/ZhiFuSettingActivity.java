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
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/9/9.
 */
public class ZhiFuSettingActivity extends BaseActivity implements View.OnClickListener{
    private EditText etZhFP=null;
    private EditText etQZhFP=null;
    private Button btnSetzfp=null;
    private ImageView iv_back=null;
    private String pass=null,biaoshi=null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhifu_setting);
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        if (biaoshi.equals("001")){
            pass = DemoApplication.getInstance().getCurrentQiyePayPass();
        }else {
            pass = DemoApplication.getInstance().getCurrentPayPass();
        }
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        etZhFP = (EditText) findViewById(R.id.et_zhifumima);
        etQZhFP = (EditText) findViewById(R.id.et_querenzhifumima);
        btnSetzfp = (Button) findViewById(R.id.btn_setzfp);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        btnSetzfp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setzfp:
                if (pass==null||"".equals(pass)) {
                    final String pass = etZhFP.getText().toString().trim();
                    final String qPass = etQZhFP.getText().toString().trim();
                    if (TextUtils.isEmpty(pass) || TextUtils.isEmpty(qPass)) {
                        Toast.makeText(ZhiFuSettingActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pass.length()!=6){
                        Toast.makeText(ZhiFuSettingActivity.this, "新密码必须为六位数纯数字!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pass.equals(qPass)) {
                        String url;
                        if ("000".equals(biaoshi)){
                            url = FXConstant.URL_UPDATE;
                        }else {
                            url = FXConstant.URL_UPDATE_QIYE;
                        }
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject object = new JSONObject(s);
                                    String code = object.getString("code");
                                    if (code.equals("SUCCESS")) {
                                        Toast.makeText(ZhiFuSettingActivity.this, "密码设置成功！", Toast.LENGTH_SHORT).show();
                                        if ("000".equals(biaoshi)){
                                            DemoApplication.getInstance().saveCurrentPayPass(pass);
                                            Log.e("zhifusett,geren","geren");
                                        }else {
                                            DemoApplication.getInstance().saveCurrentQiyePayPass(pass);
                                            Log.e("zhifusett,qiye","qiye");
                                        }
                                        finish();
                                    } else {
                                        Toast.makeText(ZhiFuSettingActivity.this, "密码设置失败！", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(ZhiFuSettingActivity.this, "网络连接错误！", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                if ("000".equals(biaoshi)){
                                    params.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                                }else {
                                    params.put("companyId", DemoApplication.getInstance().getCurrentQiYeId());
                                }
                                params.put("payPassWord", pass);
                                return params;
                            }
                        };
                        MySingleton.getInstance(ZhiFuSettingActivity.this).addToRequestQueue(request);
                    } else {
                        Toast.makeText(ZhiFuSettingActivity.this, "两次密码不一样，请重新输入！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ZhiFuSettingActivity.this, "已存在支付密码，不能重复设置！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
