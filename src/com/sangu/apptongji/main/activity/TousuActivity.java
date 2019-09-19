package com.sangu.apptongji.main.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-05-11.
 */

public class TousuActivity extends BaseActivity {
    private EditText et_neirong;
    private Button btn_tijiao;
    String hxid;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_tousu);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        btn_tijiao = (Button) findViewById(R.id.btn_tijiao);
        et_neirong = (EditText) findViewById(R.id.et_neirong);
        hxid = getIntent().getStringExtra(FXConstant.JSON_KEY_HXID);
        btn_tijiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_neirong.getText().toString().trim())){
                    Toast.makeText(getApplicationContext(), "投诉内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                duanxintongzhi();
            }
        });
    }

    private void duanxintongzhi() {
        String id = DemoHelper.getInstance().getCurrentUsernName();
        String content = et_neirong.getText().toString().trim();
        final String message;
        message = "【正事多】 通知：用户" + id + "在正事多平台投诉账号"+hxid+"用户!投诉内容为："+content;
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if ("SUCCESS".equals(code)){
                        Toast.makeText(getApplicationContext(), "投诉成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "投诉失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("volleyError",volleyError.getMessage());
                Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message",message);
                param.put("telNum","13513895563");//"13513895563"
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(TousuActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}
