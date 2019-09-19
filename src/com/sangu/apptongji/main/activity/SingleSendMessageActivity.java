package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import java.util.HashMap;
import java.util.Map;

public class SingleSendMessageActivity extends BaseActivity {

    private EditText et_messageContent1;
    private TextView tv_count1,midBtn;

    private String userId;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_singlesendmessage);

        et_messageContent1 = (EditText) findViewById(R.id.et_messageContent1);
        tv_count1 = (TextView) findViewById(R.id.tv_count1);
        midBtn = (TextView) findViewById(R.id.midBtn);

        userId = getIntent().getStringExtra("userId");

        et_messageContent1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tv_count1.setText("一共" + et_messageContent1.getText().toString().trim().length() + "字");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tv_count1.setText("一共" + et_messageContent1.getText().toString().trim().length() + "字");

        midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LayoutInflater inflater1 = LayoutInflater.from(SingleSendMessageActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog collectionDialog = new AlertDialog.Builder(SingleSendMessageActivity.this, R.style.Dialog).create();
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

                        SendMessageRequest(et_messageContent1.getText().toString().trim(),userId);

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


    private void SendMessageRequest(final String message, final String phone){

        String url = FXConstant.URL_DUANXIN_TONGZHI;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Toast.makeText(SingleSendMessageActivity.this,"发送完成",Toast.LENGTH_SHORT).show();

                finish();


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

        MySingleton.getInstance(SingleSendMessageActivity.this).addToRequestQueue(request);

    }


}
