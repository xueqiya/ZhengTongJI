package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.sangu.apptongji.ui.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-02.
 */

public class ZhangHuActivity extends BaseActivity {
    private Button btn_next;
    private EditText et_jine;
    private String pass,biaoshi,timestamp;
    private String orderId,hasId1,hasId2,hasId3,hasId4;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_zhanghu);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        btn_next = (Button) findViewById(R.id.btn_next);
        et_jine = (EditText) findViewById(R.id.et_jine);
        orderId = getIntent().getStringExtra("orderId");
        timestamp = getIntent().getStringExtra("timestamp");
        hasId1 = getIntent().getStringExtra("hasId1");
        hasId2 = getIntent().getStringExtra("hasId2");
        hasId3 = getIntent().getStringExtra("hasId3");
        hasId4 = getIntent().getStringExtra("hasId4");
        pass = this.getIntent().getStringExtra("payPass");
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = et_jine.getText().toString().trim();
                if ("03".equals(biaoshi)){
                    if (userId.equals(hasId1)||userId.equals(hasId2)||userId.equals(hasId3)||userId.equals(hasId4)){
                        LayoutInflater inflaterDl = LayoutInflater.from(ZhangHuActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                        final Dialog dialog2 = new AlertDialog.Builder(ZhangHuActivity.this,R.style.Dialog).create();
                        dialog2.show();
                        dialog2.getWindow().setContentView(layout);
                        dialog2.setCanceledOnTouchOutside(false);
                        dialog2.setCancelable(false);
                        TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                        Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                        tv_title.setText("该账号已在本个订单中,无需再次发送！");
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
                    }else {
                        queryUserInfo(userId);
                    }
                }else if ("04".equals(biaoshi)){
                    queryUserInfo(userId);
                }else {
                    queryUserInfo(userId);
                }
            }
        });
    }

    private void queryUserInfo(final String userId) {
        String url = FXConstant.URL_Get_UserInfo+userId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s==null||"".equals(s)){
                    LayoutInflater inflaterDl = LayoutInflater.from(ZhangHuActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(ZhangHuActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    tv_title.setText("用户信息查询错误，请确认账号");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                }else {
                    if ("03".equals(biaoshi) || "04".equals(biaoshi)) {
                        JSONObject jsonObject = JSON.parseObject(s);
                        JSONObject object = jsonObject.getJSONObject("userInfo");
                        String name = object.getString("uName");
                        name = name == null || "".equals(name) ? userId : name;
                        LayoutInflater inflaterDl = LayoutInflater.from(ZhangHuActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog = new AlertDialog.Builder(ZhangHuActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setCancelable(true);
                        TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                        Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                        final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                        btnOK.setText("确定");
                        btnCancel.setText("取消");
                        title_tv.setText("确认发送给 '" + name + "' 吗？");
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if ("04".endsWith(biaoshi)) {
                                    senddztoUser(userId);
                                } else {
                                    sendToUser(userId);
                                }
                            }
                        });
                    } else {
                        startActivity(new Intent(ZhangHuActivity.this, ZHuZhActivity.class)
                                .putExtra(FXConstant.JSON_KEY_HXID, userId).putExtra("payPass", pass).putExtra("biaoshi", biaoshi));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LayoutInflater inflaterDl = LayoutInflater.from(ZhangHuActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                final Dialog dialog2 = new AlertDialog.Builder(ZhangHuActivity.this,R.style.Dialog).create();
                dialog2.show();
                dialog2.getWindow().setContentView(layout);
                dialog2.setCanceledOnTouchOutside(false);
                dialog2.setCancelable(false);
                TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                tv_title.setText("用户信息查询错误，请确认账号");
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
            }
        });
        MySingleton.getInstance(ZhangHuActivity.this).addToRequestQueue(request);
    }

    private void senddztoUser(final String username) {
        String url = FXConstant.URL_FASONG_DZ_DANJU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("zhanghuac,s",s);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){
                    Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"发送失败！",Toast.LENGTH_SHORT).show();
                Log.e("zhanghuac",volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("timestamp",timestamp);
                param.put("u_id",username);
                return param;
            }
        };
        MySingleton.getInstance(ZhangHuActivity.this).addToRequestQueue(request);
    }

    private void sendToUser(final String username) {
        String url = FXConstant.URL_FASONG_DZDANJU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){
                    Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("order_id",orderId);
                param.put("send_id",DemoHelper.getInstance().getCurrentUsernName());
                param.put("u_id", username);
                return param;
            }
        };
        MySingleton.getInstance(ZhangHuActivity.this).addToRequestQueue(request);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}
