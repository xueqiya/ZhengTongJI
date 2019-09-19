package com.sangu.apptongji.main.qiye;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-01-19.
 */

public class YuanGongDetailActivity extends BaseActivity {
    private TextView tv_name=null,tvtitle=null;
    private Button btncommit=null;
    private String userId=null,companyId=null,userName=null,sex=null,resv6=null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_yuangong_xiangqing);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        tv_name = (TextView) findViewById(R.id.tv_name);
        tvtitle = (TextView) findViewById(R.id.tv_titl);
        btncommit = (Button) findViewById(R.id.btn_commit);
        userId = this.getIntent().getStringExtra("userId");
        companyId = this.getIntent().getStringExtra("companyId");
        userName = this.getIntent().getStringExtra("userName");
        sex = this.getIntent().getStringExtra("sex");
        resv6 = this.getIntent().getStringExtra("resv6");
        if ("00".equals(sex)){
            tvtitle.setBackgroundResource(R.drawable.fx_bg_text_red);
        }else {
            tvtitle.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }
        tv_name.setText(userName);
        tvtitle.setText(userName);
        btncommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("00".equals(resv6)) {
                    Toast.makeText(YuanGongDetailActivity.this, "不能删除自己！", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(YuanGongDetailActivity.this);
                    builder.setMessage("确认删除员工么?");
                    builder.setTitle("确认");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            shanchu();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    private void updateHbTimes(final String uId) {
        String url = FXConstant.URL_XIUGAI_HBTIMES;
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
                Map<String, String> param = new HashMap<String, String>();
                param.put("uLoginId", uId);
                param.put("shareTimes", "-1");
                param.put("homePageTimes", "-1");
                param.put("dynamicTimes", "-1");
                return param;
            }
        };
        MySingleton.getInstance(YuanGongDetailActivity.this).addToRequestQueue(request);
    }

    private void shanchu() {
        String url = FXConstant.URL_QIYE_SHANCHU;
        StringRequest qDrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateHbTimes(userId);
                Log.e("shanchu","1");
                shanchutongji();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("网络连接错误",volleyError+"");
                Toast.makeText(YuanGongDetailActivity.this, "网络连接错误...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("companyId", companyId);
                params.put("userId", userId);
                return params;
            }
        };
        MySingleton.getInstance(YuanGongDetailActivity.this).addToRequestQueue(qDrequest);
    }

    private void shanchutongji() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = FXConstant.URL_QIYE_SHANCHUTONGJI;
                StringRequest qDrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        EMClient.getInstance().groupManager().asyncRemoveUserFromGroup(companyId, userId, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(YuanGongDetailActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                        Log.e("shanchu,liebiao","2");
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                });
                            }
                            @Override
                            public void onError(int i, final String s) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(YuanGongDetailActivity.this, "删除失败！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            @Override
                            public void onProgress(int i, String s) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(YuanGongDetailActivity.this, "删除...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (volleyError!=null) {
                            Log.e("网络连接错误", volleyError + "");
                        }
                        Toast.makeText(YuanGongDetailActivity.this, "网络连接错误...", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("companyId", companyId);
                        params.put("userId", userId);
                        return params;
                    }
                };
                MySingleton.getInstance(YuanGongDetailActivity.this).addToRequestQueue(qDrequest);
            }
        }).start();
    }
}
