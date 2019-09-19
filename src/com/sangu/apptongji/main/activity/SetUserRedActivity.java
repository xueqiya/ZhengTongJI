package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import java.util.HashMap;
import java.util.Map;

public class SetUserRedActivity extends BaseActivity {

    TextView et_messageContent,tv_count,midBtn;

    String userId;

    private Dialog mWeiboDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_setuserred);

        userId = getIntent().getStringExtra("userId");

        et_messageContent = (TextView) findViewById(R.id.et_messageContent);
        tv_count = (TextView) findViewById(R.id.tv_count);
        midBtn = (TextView) findViewById(R.id.midBtn);


        et_messageContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tv_count.setText("一共" + et_messageContent.getText().toString().trim().length() + "字");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_messageContent.setText("【正事多】恭喜您获得了派单红包，登录正事多个人中心，进入等级·补贴领取，可直接提现！");


        tv_count.setText("一共" + et_messageContent.getText().toString().trim().length() + "字");


        midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LayoutInflater inflater1 = LayoutInflater.from(SetUserRedActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog collectionDialog = new AlertDialog.Builder(SetUserRedActivity.this, R.style.Dialog).create();
                collectionDialog.show();
                collectionDialog.getWindow().setContentView(layout1);
                collectionDialog.setCanceledOnTouchOutside(true);
                collectionDialog.setCancelable(true);
                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                title.setText("提示");
                btnOK1.setText("确定");
                btnCancel1.setText("取消");

                title_tv1.setText("是否确定发送短信？");

                btnOK1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        collectionDialog.dismiss();

                        UpdateUserInfo();

                    }
                });

                btnCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        collectionDialog.dismiss();

                    }
                });

            }
        });

    }

    private void UpdateUserInfo(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SetUserRedActivity.this, "处理中...");

        String url = FXConstant.URL_UPDATE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                SendMessageRequest(userId,et_messageContent.getText().toString().trim());

                Toast.makeText(SetUserRedActivity.this, "设置成功", Toast.LENGTH_SHORT).show();

                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

                Toast.makeText(SetUserRedActivity.this, "设置报错", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("uLoginId", userId);
                param.put("dynamicReward", "1");

                return param;
            }
        };
        MySingleton.getInstance(SetUserRedActivity.this).addToRequestQueue(request);

    }


    private void SendMessageRequest(final String message, final String phone){

        String url = FXConstant.URL_DUANXIN_TONGZHI;

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

                param.put("message",message);
                param.put("telNum",phone);

                return param;
            }
        };

        MySingleton.getInstance(SetUserRedActivity.this).addToRequestQueue(request);

    }


}
