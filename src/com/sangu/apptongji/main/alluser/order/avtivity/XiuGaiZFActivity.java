package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/9/9.
 */

public class XiuGaiZFActivity extends BaseActivity implements View.OnClickListener,IPriceView{
    private IPricePresenter pricePresenter;
    private String pass,biaoshi=null,managerId;
    private EditText etYuan=null;
    private EditText etXiuGai=null;
    private EditText etQueren=null;
    private Button btnXiuGai=null;
    private ImageView iv_back=null;
    private TextView tv_wangji=null;
    private String id;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiugai_pass);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        pricePresenter = new PricePresenter(this,this);
        etYuan = (EditText) findViewById(R.id.et_yuanmima);
        tv_wangji = (TextView) findViewById(R.id.tv_wangji);
        etXiuGai = (EditText) findViewById(R.id.et_xinmima);
        etQueren = (EditText) findViewById(R.id.et_querenmima);
        btnXiuGai = (Button) findViewById(R.id.btn_xiugai);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        Intent intent = this.getIntent();
        pass = intent.getStringExtra("zhifupass");
        biaoshi = intent.getStringExtra("biaoshi");
        managerId = intent.getStringExtra("managerId");
        btnXiuGai.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_wangji.setOnClickListener(this);
        if ("000".equals(biaoshi)){
            id = DemoHelper.getInstance().getCurrentUsernName();
        }else {
            id = DemoApplication.getInstance().getCurrentQiYeId();
        }
        pricePresenter.updatePriceData(id);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_xiugai:
                String yuanPass = etYuan.getText().toString().trim();
                final String xinPass = etXiuGai.getText().toString().trim();
                String querenPass = etQueren.getText().toString().trim();
                if(TextUtils.isEmpty(yuanPass)||TextUtils.isEmpty(xinPass)||TextUtils.isEmpty(querenPass)){
                    Toast.makeText(XiuGaiZFActivity.this, "密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(yuanPass.equals(pass))) {
                    Toast.makeText(XiuGaiZFActivity.this, "原密码输入错误!请重新输入!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (xinPass.length()!=6){
                    Toast.makeText(XiuGaiZFActivity.this, "新密码必须为六位数纯数字!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.equals(xinPass)){
                    Toast.makeText(XiuGaiZFActivity.this, "新密码不能和近期密码相同!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (xinPass.equals(querenPass)){
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
                                    pricePresenter.updatePriceData(id);
                                    Toast.makeText(XiuGaiZFActivity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                                    if ("000".equals(biaoshi)){
                                        DemoApplication.getInstance().saveCurrentPayPass(xinPass);
                                    }else {
                                        DemoApplication.getInstance().saveCurrentQiyePayPass(xinPass);
                                    }
                                    finish();
                                } else {
                                    Toast.makeText(XiuGaiZFActivity.this, "密码修改失败！", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(XiuGaiZFActivity.this, "网络连接错误！", Toast.LENGTH_SHORT).show();
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
                            params.put("payPassWord", xinPass);
                            return params;
                        }
                    };
                    MySingleton.getInstance(XiuGaiZFActivity.this).addToRequestQueue(request);
                }else{
                    Toast.makeText(XiuGaiZFActivity.this, "两次密码不一样，请重新输入！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_wangji:
                startActivity(new Intent(XiuGaiZFActivity.this, WJPaActivity.class).putExtra("biaoshi",biaoshi).putExtra("managerId",managerId));
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void updateCurrentPrice(Object success) {
        if ("001".equals(biaoshi)){
            pass = DemoApplication.getInstance().getCurrentQiyePayPass();
        }else {
            pass = DemoApplication.getInstance().getCurrentPayPass();
        }
    }
}
