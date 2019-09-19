package com.sangu.apptongji.main.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import java.util.HashMap;
import java.util.Map;

public class SetVipToUserActivity extends BaseActivity {

    EditText viplevel,vipday;

    TextView midBtn;

    String userId;

    private Dialog mWeiboDialog;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_setviptouser);

        userId = getIntent().getStringExtra("userId");

        viplevel = (EditText) findViewById(R.id.et_viplevel);

        vipday = (EditText) findViewById(R.id.et_vipday);

        midBtn = (TextView) findViewById(R.id.midBtn);


        midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (viplevel.getText().toString().trim() != null && vipday.getText().toString().trim() != null){


                    UpdateUserInfo();

                }else {

                    Toast.makeText(SetVipToUserActivity.this,"填写等级和天数",Toast.LENGTH_SHORT).show();

                }


            }
        });


    }


    private void UpdateUserInfo(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SetVipToUserActivity.this, "处理中...");

        String url = FXConstant.URL_UPDATE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Toast.makeText(SetVipToUserActivity.this, "设置成功", Toast.LENGTH_SHORT).show();

                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

                Toast.makeText(SetVipToUserActivity.this, "设置报错", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("uLoginId", userId);
                param.put("vipLevel", viplevel.getText().toString().trim());
                param.put("vip", vipday.getText().toString().trim());

                return param;
            }
        };
        MySingleton.getInstance(SetVipToUserActivity.this).addToRequestQueue(request);

    }


}
