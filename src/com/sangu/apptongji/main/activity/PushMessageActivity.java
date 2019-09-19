package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import java.util.HashMap;
import java.util.Map;

public class PushMessageActivity extends BaseActivity {

    private String task_label,task_position,dynamicSeq,createTime,sID,deviceType,content,task_locaName,uName;

    private TextView tv_taskLabel,midBtn1,midBtn2,midBtn3;

    private EditText et_newTaskLabel,et_distance;

    private Dialog mWeiboDialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_pushmessagenotice);

        task_label = getIntent().getStringExtra("task_label");
        task_position = getIntent().getStringExtra("task_position");
        dynamicSeq = getIntent().getStringExtra("dynamicSeq");
        createTime = getIntent().getStringExtra("createTime");
        sID = getIntent().getStringExtra("sID");
        deviceType = getIntent().getStringExtra("deviceType");
        content = getIntent().getStringExtra("content");
        task_locaName = getIntent().getStringExtra("task_locaName");
        uName = getIntent().getStringExtra("uName");

        tv_taskLabel = (TextView) findViewById(R.id.tv_taskLabel);

        tv_taskLabel.setText(task_label);

        midBtn1 = (TextView) findViewById(R.id.midBtn1);
        midBtn2 = (TextView) findViewById(R.id.midBtn2);
        midBtn3 = (TextView) findViewById(R.id.midBtn3);

        et_newTaskLabel = (EditText) findViewById(R.id.et_newTaskLabel);
        et_distance = (EditText) findViewById(R.id.et_distance);


        midBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_distance.getText().toString().trim().length() > 0){

                    Intent intent = new Intent(PushMessageActivity.this,PushMessageDetailActivity.class);

                    if (et_newTaskLabel.getText().toString().trim() != null && et_newTaskLabel.getText().toString().trim().length() > 0){

                        intent.putExtra("task_label",et_newTaskLabel.getText().toString().trim());

                    }else {

                        intent.putExtra("task_label",task_label);
                    }

                    intent.putExtra("task_position",task_position);
                    intent.putExtra("dynamicSeq",dynamicSeq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("sID",sID);
                    intent.putExtra("deviceType",deviceType);
                    intent.putExtra("content",content);
                    intent.putExtra("distance",et_distance.getText().toString().trim());
                    intent.putExtra("task_locaName",task_locaName);
                    intent.putExtra("uName",uName);

                    startActivity(intent);

                }else {

                    Toast.makeText(PushMessageActivity.this,"输入距离限制！",Toast.LENGTH_SHORT).show();

                }

            }
        });


        midBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_distance.getText().toString().trim().length() > 0){

                    final LayoutInflater inflater1 = LayoutInflater.from(PushMessageActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog collectionDialog = new AlertDialog.Builder(PushMessageActivity.this, R.style.Dialog).create();
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

                    title_tv1.setText("是否确定向周边发送推送？");

                    btnOK1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            collectionDialog.dismiss();

                            SendPushRequest();

                        }
                    });


                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            collectionDialog.dismiss();
                        }
                    });

                }else {

                    Toast.makeText(PushMessageActivity.this,"输入距离限制！",Toast.LENGTH_SHORT).show();

                }

            }
        });



        midBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LayoutInflater inflater1 = LayoutInflater.from(PushMessageActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog collectionDialog = new AlertDialog.Builder(PushMessageActivity.this, R.style.Dialog).create();
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

                title_tv1.setText("是否确定给派单用户红包？");

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

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(PushMessageActivity.this, "处理中...");


        String url = FXConstant.URL_UPDATE;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

                Toast.makeText(PushMessageActivity.this,"设置成功",Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(PushMessageActivity.this,"设置失败",Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("uLoginId",sID);

                param.put("dynamicReward","1");

                return param;
            }
        };

        MySingleton.getInstance(PushMessageActivity.this).addToRequestQueue(request);

    }



    private void SendPushRequest(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(PushMessageActivity.this, "发送中...");


        String url = FXConstant.URL_SENDPUSHBYDISTANCE;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

                Toast.makeText(PushMessageActivity.this,"发送完成",Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(PushMessageActivity.this,"发送失败",Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("task_position",task_position);

                Integer km = Integer.valueOf(et_distance.getText().toString().trim())*1000 ;

                param.put("km",km+"");

                return param;
            }
        };

        MySingleton.getInstance(PushMessageActivity.this).addToRequestQueue(request);

    }

}
