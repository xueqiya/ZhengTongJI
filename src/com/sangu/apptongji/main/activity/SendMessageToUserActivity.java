package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
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
import com.fanxin.easeui.widget.EaseSwitchButton;
import com.sangu.apptongji.R;
import com.sangu.apptongji.adapter.UserNewsAdapter;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendMessageToUserActivity extends BaseActivity {

    private EditText et_messageCount,et_messageContent1,et_messageContent2,et_newTask;
    private TextView tv_fromLabel,tv_taskLabel,tv_content,tv_count1,tv_count2,midBtn,tv_rightSetRed;
    private String task_label,task_position,dynamicSeq,createTime,sID,distance,deviceType,content,task_locaName,uName;

    private List<UserAll> resultList;

    private List<UserAll> resultList1 = new ArrayList<>();
    private List<UserAll> resultList2 = new ArrayList<>();

    private String messagePhone=""; // 短信派单短信的手机号拼接
    private String otherPhone = ""; // 普通短信的手机号拼接

    private int resultCount = 0;
    private Dialog mWeiboDialog;

    EaseSwitchButton switch_allpeople;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_sendmesagetouser);

        et_messageCount = (EditText) findViewById(R.id.et_messageCount);
        et_messageContent1 = (EditText) findViewById(R.id.et_messageContent1);
        et_messageContent2 = (EditText) findViewById(R.id.et_messageContent2);

        switch_allpeople = (EaseSwitchButton) findViewById(R.id.switch_allpeople);

        switch_allpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (switch_allpeople.isSwitchOpen()){

                    switch_allpeople.closeSwitch();

                }else {

                    switch_allpeople.openSwitch();
                }

            }
        });

        tv_fromLabel = (TextView) findViewById(R.id.tv_fromLabel);
        tv_taskLabel = (TextView) findViewById(R.id.tv_taskLabel);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_count1 = (TextView) findViewById(R.id.tv_count1);
        tv_count2 = (TextView) findViewById(R.id.tv_count2);
        midBtn = (TextView) findViewById(R.id.midBtn);
        et_newTask = (EditText) findViewById(R.id.et_newTask);

        tv_rightSetRed = (TextView) findViewById(R.id.tv_rightSetRed);

        task_label = getIntent().getStringExtra("task_label");
        task_position = getIntent().getStringExtra("task_position");
        dynamicSeq = getIntent().getStringExtra("dynamicSeq");
        createTime = getIntent().getStringExtra("createTime");
        sID = getIntent().getStringExtra("sID");
        distance = getIntent().getStringExtra("distance");
        deviceType = getIntent().getStringExtra("deviceType");
        content = getIntent().getStringExtra("content");
        task_locaName = getIntent().getStringExtra("task_locaName");
        resultList = (List<UserAll>)getIntent().getSerializableExtra("datas");

        String isHave = "";
        for (UserAll all:resultList){
            isHave = "";
            if (Double.parseDouble(all.getMessageOrderCount()) > 0){

                isHave = "yes";
                resultList1.add(all);  //有短信派单的

                if (messagePhone.length() == 0){
                    messagePhone = all.getuLoginId();
                }else {
                    messagePhone = messagePhone+ "," + all.getuLoginId();
                }
            }

            if (isHave.equals("yes")){

            }else {

                String margan1 = all.getMargen1();
                String margan2 = all.getMargen2();
                String margan3 = all.getMargen3();
                String margan4 = all.getMargen4();

                Double allmargin = 0.0;
                if (margan1 != null){
                    if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                        allmargin = allmargin + Double.valueOf(margan1);
                    }
                }
                if (margan2 != null){
                    if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                        allmargin = allmargin + Double.valueOf(margan2);
                    }
                }
                if (margan3 != null){
                    if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                        allmargin = allmargin + Double.valueOf(margan3);
                    }
                }
                if (margan4 != null){
                    if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                        allmargin = allmargin + Double.valueOf(margan4);
                    }
                }

                if (allmargin > 99){
                    isHave = "yes";
                    resultList1.add(all);  //没有短信派单，但是有质保

                    if (messagePhone.length() == 0){
                        messagePhone = all.getuLoginId();
                    }else {
                        messagePhone = messagePhone+ "," + all.getuLoginId();
                    }

                }else {

                    resultList2.add(all);

                    if (otherPhone.length() == 0){
                        otherPhone = all.getuLoginId();
                    }else {
                        otherPhone = otherPhone+ "," + all.getuLoginId();
                    }

                }

            }

        }


        uName = getIntent().getStringExtra("uName");

        tv_fromLabel.setText("来源："+deviceType);
        tv_taskLabel.setText("标签："+task_label);
        tv_content.setText("内容："+content);

        et_newTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             //   et_messageContent1.setText("【正事多】" + task_locaName + "客户发单(需要" + et_newTask.getText().toString().trim() + ")向“正事多”旗下平台会员派单，接单可登陆（正事多APP）看详情图");
                et_messageContent1.setText("【正事多】" + task_locaName + "客户(需要" + task_label + ")通过“正事多”旗下平台发单，可登陆（正事多APP）看附近联系接单");

                String name = uName.substring(0,1);


                //【正事多】金水区李先生12326262（需要家电维修）请及时联系；来自正事多短信派单系统
                et_messageContent2.setText("【正事多】" + task_locaName + name + "先生" + sID + "(需要" + et_newTask.getText().toString().trim() + ")请及时联系，来自正事多短信派单系统");


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    //【正事多】金水区客户发单（需要维修家电）向“正事多”旗下平台会员派单，接单可登陆（正事多APP）看详情图
        et_messageContent1.setText("【正事多】" + task_locaName + "客户(需要" + task_label + ")通过“正事多”旗下平台发单，可登陆（正事多APP）看附近联系接单");

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

        String name = uName.substring(0,1);


        //【正事多】金水区李先生12326262（需要家电维修）请及时联系；来自正事多短信派单系统
        et_messageContent2.setText("【正事多】" + task_locaName + name + "先生" + sID + "(需要" + task_label + ")请及时联系，来自正事多短信派单系统");


        et_messageContent2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                tv_count2.setText("一共" + et_messageContent2.getText().toString().trim().length() + "字");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tv_count2.setText("一共" + et_messageContent2.getText().toString().trim().length() + "字");


        //【正事多】您的派单在“正事多”旗下派单系统匹配到，附近80 位相关接单人供您（优选）请留意来电报价或留言，管理派单请登录（正事多APP）通用版


        midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final LayoutInflater inflater1 = LayoutInflater.from(SendMessageToUserActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog collectionDialog = new AlertDialog.Builder(SendMessageToUserActivity.this, R.style.Dialog).create();
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


                        if (switch_allpeople.isSwitchOpen()){

                            if (messagePhone.length() != 0){

                                SendMessageRequest(et_messageContent2.getText().toString().trim(),messagePhone);

                                for (UserAll all : resultList1){

                                    UpdateMessageOrderCount(all);

                                }

                            }

                            SendMessageRequest(et_messageContent1.getText().toString().trim(),otherPhone);

                        }


                        UpdateDynamicInfoRecomd();

                        //【正事多】您的派单在“正事多”旗下派单系统匹配到，附近80 位相关接单人供您（优选）请留意来电报价或留言，管理派单请登录（正事多APP）通用版

                        SendMessageRequest("【正事多】您的派单在“正事多”旗下派单系统匹配到，附近"+resultList.size()+"位相关接单人供您（优选）打开（正事多APP）可查看匹配列表",sID);

                        resultCount = 0;
                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(SendMessageToUserActivity.this, "发送中...");
                        for (UserAll all : resultList){

                            InsertMemoDynamic(all.getuLoginId());

                        }


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


        tv_rightSetRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SendMessageToUserActivity.this,SetUserRedActivity.class);

                intent.putExtra("userId",sID);

                startActivityForResult(intent,0);

            }
        });


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

        MySingleton.getInstance(SendMessageToUserActivity.this).addToRequestQueue(request);

    }


    private void UpdateMessageOrderCount(final UserAll all){


        String url = FXConstant.URL_UPDATEMESSAGECOUNT;

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

                param.put("uLoginId",all.getuLoginId());

                Integer integer = 0;
                if (et_messageCount.getText().toString().trim().length() != 0){

                    if (Integer.valueOf(all.getMessageOrderCount()) > Integer.valueOf(et_messageCount.getText().toString().trim())){

                        integer = Integer.valueOf(all.getMessageOrderCount()) - Integer.valueOf(et_messageCount.getText().toString().trim());

                    }else {

                        integer = 0;

                    }

                }else {

                    integer = Integer.valueOf(all.getMessageOrderCount()) - 1;

                }

                param.put("messageOrderCount",integer+"");

                return param;
            }
        };

        MySingleton.getInstance(SendMessageToUserActivity.this).addToRequestQueue(request);

    }


    private void InsertMemoDynamic(final String userId){


        String url = FXConstant.URL_INSERTMEMODYNAMIC;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                resultCount ++ ;

                if (resultCount == resultList.size()){

                    WeiboDialogUtils.closeDialog(mWeiboDialog);

                    Toast.makeText(SendMessageToUserActivity.this,"发送完成",Toast.LENGTH_SHORT).show();

                    finish();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(SendMessageToUserActivity.this,"报错报错!",Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("dynamic_seq",dynamicSeq);
                param.put("dynamic_createtime",createTime);
                param.put("id",userId);

                return param;
            }
        };

        MySingleton.getInstance(SendMessageToUserActivity.this).addToRequestQueue(request);

    }


    private void UpdateDynamicInfoRecomd(){

        String url = FXConstant.URL_UPDATEDYNAMICRECOMD;

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

                param.put("userId",sID);
                param.put("dynamicSeq",dynamicSeq);
                param.put("createTime",createTime);
                param.put("recommendCount",resultList.size()+"");
                param.put("newTaskLabel",task_label);

                return param;
            }
        };

        MySingleton.getInstance(SendMessageToUserActivity.this).addToRequestQueue(request);

    }


}
