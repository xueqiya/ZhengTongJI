package com.sangu.apptongji.main.qiye;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-29.
 */

public class CompanyUpdateActivity extends BaseActivity {
    public static final int TYPE_SIGN = 102;
    public static final int TYPE_COMADDRESS = 202;
    private String companyId,companyName,infoDetail;
    private TextView titleTV=null;
    private TextView saveTV=null;
    private EditText infoET=null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_update_info);
        final int type = getIntent().getIntExtra("type", 0);
        companyName = this.getIntent().getStringExtra("companyName");
        companyId = this.getIntent().getStringExtra("companyId");
        infoDetail = this.getIntent().getStringExtra("info");
        String title = "";
        titleTV = (TextView) findViewById(R.id.tv_title);
        saveTV = (TextView) findViewById(R.id.tv_save);
        infoET = (EditText) findViewById(R.id.et_info);
        infoET.setText(infoDetail);
        infoET.setSelection(infoET.getText().length());
        switch (type){
            case TYPE_SIGN:
                title = "修改企业简介";
                titleTV.setText(title);
                break;
            case TYPE_COMADDRESS:
                title = "修改公司地址";
                titleTV.setText(title);
                break;
        }
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = infoET.getText().toString().trim();
                if (info!=null&&!TextUtils.isEmpty(info)) {
                    try {
                        info = URLEncoder.encode(info, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    final String finalInfo = info;
                    String url = FXConstant.URL_UPDATE_QIYE;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject object = new JSONObject(s);
                                String code = object.getString("code");
                                if (code.equals("SUCCESS")) {
                                    Toast.makeText(CompanyUpdateActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CompanyUpdateActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.d("-----失败----", "" + volleyError.getMessage());
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("companyId",companyId);
                            param.put("companyName",companyName);
                            switch (type) {
                                case TYPE_SIGN:
                                    param.put("comSignature", finalInfo);
                                    setResult(RESULT_OK, new Intent().putExtra("value", finalInfo));
                                    break;
                                case TYPE_COMADDRESS:
                                    param.put("comAddress", finalInfo);
                                    setResult(RESULT_OK, new Intent().putExtra("value", finalInfo));
                                    break;
                            }
                            return param;
                        }
                    };
                    MySingleton.getInstance(CompanyUpdateActivity.this).addToRequestQueue(request);
                }else {
                    Toast.makeText(CompanyUpdateActivity.this,"内容不能为空！",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
