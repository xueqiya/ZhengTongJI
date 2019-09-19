package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.sangu.apptongji.main.utils.LimitInputTextWatcher;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lsk on 2016/7/25.\
 */
public class ProfileUpdateActivity extends BaseActivity {
    public static final int TYPE_NICK = 102;
    public static final int TYPE_GONGSI = 112;
    public static final int TYPE_SIGN = 202;
    public static final int TYPE_BIRTH = 302;
    public static final int TYPE_CONSUME1 = 1002;
    public static final int TYPE_CONSUME2 = 1102;
    public static final int TYPE_CONSUME3 = 1202;
    public static final int TYPE_CONSUME4 = 1302;
    public static final int TYPE_CONSUME5 = 1402;
    public static final int TYPE_CONSUME6 = 1502;
    public static final int TYPE_HOMETOWN = 1602;
    public static final int TYPE_OCCUPATION = 1702;
    public static final int TYPE_COMADDRESS = 1802;
    public static final int TYPE_NAN = 1902;
    public static final int TYPE_NV = 2002;
    public static final int TYPE_NIANLING = 2102;
    private String iscFirst=null,data;
    private List<String> list = new ArrayList<>();
    //private String defaultStr;

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
        list.add("师傅");
        list.add("经理");
        list.add("公司");
        list.add("企业");
        list.add("先生");
        list.add("小姐");
        list.add("女士");
        final int type = getIntent().getIntExtra("type", 0);
        iscFirst = this.getIntent().getStringExtra("iscFirst");
        data = this.getIntent().getStringExtra("data");
        String title = "";
        //defaultStr = getIntent().getStringExtra("default");
        titleTV = (TextView) findViewById(R.id.tv_title);
        saveTV = (TextView) findViewById(R.id.tv_save);
        infoET = (EditText) findViewById(R.id.et_info);
        switch (type){
            case TYPE_NICK:
                infoET.addTextChangedListener(new LimitInputTextWatcher(infoET));
                break;
            case TYPE_GONGSI:
                title = "修改公司名称";
                titleTV.setText(title);
                break;
            case TYPE_SIGN:
                title = "修改简介";
                titleTV.setText(title);
                break;
            case TYPE_BIRTH:
                title = "修改生日";
                titleTV.setText(title);
                break;
            case TYPE_HOMETOWN:
                title = "修改家乡";
                titleTV.setText(title);
                break;
            case TYPE_OCCUPATION:
                title = "修改职业";
                titleTV.setText(title);
                break;
            case TYPE_COMADDRESS:
                title = "修改公司地址";
                titleTV.setText(title);
                break;
            case TYPE_NIANLING:
                title = "修改年龄";
                titleTV.setText(title);
                break;
            case TYPE_CONSUME1:
                title = "修改消费1";
                titleTV.setText(title);
                break;
            case TYPE_CONSUME2:
                title = "修改消费2";
                titleTV.setText(title);
                break;
            case TYPE_CONSUME3:
                title = "修改消费3";
                titleTV.setText(title);
                break;
            case TYPE_CONSUME4:
                title = "修改消费4";
                titleTV.setText(title);
                break;
            case TYPE_CONSUME5:
                title = "修改消费5";
                titleTV.setText(title);
                break;
            case TYPE_CONSUME6:
                title = "修改消费6";
                titleTV.setText(title);
                break;
            case TYPE_NAN:
                infoET.setVisibility(View.GONE);
                title = "确认修改为性别男？";
                titleTV.setText(title);
                break;
            case TYPE_NV:
                infoET.setVisibility(View.GONE);
                title = "确认修改为性别女？";
                titleTV.setText(title);
                break;
        }
        if (data!=null) {
            infoET.setText(data);
            infoET.setSelection(infoET.getText().length());
        }
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        saveTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String info = infoET.getText().toString().trim();
                if (type==TYPE_NICK){
                    if (TextUtils.isEmpty(info)||info.length()>10||info.length()<2){
                        Toast.makeText(getApplicationContext(),"请输入姓名,姓名不能少于2个字,不能超过10个字",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (info.startsWith("我")||info.startsWith("小")||info.startsWith("大")||info.startsWith("啊")||info.startsWith("老")){
                        Toast.makeText(getApplicationContext(),"商务社交,诚信为本,请使用真实姓名",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        String x = list.get(i);  //x为敏感词汇
                        if (info.contains(x)){
                            Toast.makeText(getApplicationContext(),"商务社交,诚信为本，请使用真实姓名",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (data != null && data.equalsIgnoreCase(info)) {
                        finish();
                        return;
                    } else if (data != null && !data.equalsIgnoreCase(info)) {

                        LayoutInflater inflater2 = LayoutInflater.from(ProfileUpdateActivity.this);
                        RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog2 = new AlertDialog.Builder(ProfileUpdateActivity.this).create();
                        dialog2.show();
                        dialog2.getWindow().setContentView(layout2);
                        dialog2.setCanceledOnTouchOutside(true);
                        dialog2.setCancelable(true);
                        TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                        Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                        final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                        btnOK2.setText("确定");
                        btnCancel2.setText("取消");
                        title_tv2.setText("修改名字需要进行实名认证，是否前往认证");
                        btnCancel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
                        btnOK2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(ProfileUpdateActivity.this, CertificationActivity.class));
                                dialog2.dismiss();
                            }
                        });

                        return;
                    }
                }
                if (info!=null&&!TextUtils.isEmpty(info)) {
                    if (type == TYPE_SIGN) {
                        if (info.length() > 700) {
                            ToastUtils.showNOrmalToast(getApplicationContext(),"简介描述文字不能大于700字");
                            return;
                        }
                    }
                    String url = FXConstant.URL_UPDATE;
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            try {
                                JSONObject object = new JSONObject(s);
                                String code = object.getString("code");
                                if (code.equals("SUCCESS")) {
                                    Toast.makeText(ProfileUpdateActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ProfileUpdateActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
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
                            String loginId = DemoHelper.getInstance().getCurrentUsernName();
                            param.put("uLoginId",loginId);
                            switch (type) {
                                case TYPE_NV:
                                    param.put("uSex", "00");
                                    setResult(RESULT_OK, new Intent().putExtra("value", "女"));
                                    break;
                                case TYPE_NAN:
                                    param.put("uSex", "01");
                                    setResult(RESULT_OK, new Intent().putExtra("value", "男"));
                                case TYPE_NICK:
                                    param.put("uName", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_NIANLING:
                                    param.put("uAge",info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_BIRTH:
                                    param.put("uBirthday", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_GONGSI:
                                    param.put("uCompany", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_SIGN:
                                    param.put("uSignaTure", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_CONSUME1:
                                    if (iscFirst.equals("13")){
                                        param.put("userConsumer2.ucId", loginId+"2");
                                        param.put("userConsumer3.ucId", loginId+"3");
                                        param.put("userConsumer4.ucId", loginId+"4");
                                        param.put("userConsumer5.ucId", loginId+"5");
                                        param.put("userConsumer6.ucId", loginId+"6");
                                    }
                                    param.put("userConsumer1.ucId", loginId+"1");
                                    param.put("userConsumer1.ucName", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_CONSUME2:
                                    if (iscFirst.equals("13")){
                                        param.put("userConsumer1.ucId", loginId+"1");
                                        param.put("userConsumer3.ucId", loginId+"3");
                                        param.put("userConsumer4.ucId", loginId+"4");
                                        param.put("userConsumer5.ucId", loginId+"5");
                                        param.put("userConsumer6.ucId", loginId+"6");
                                    }
                                    param.put("userConsumer2.ucId", loginId+"2");
                                    param.put("userConsumer2.ucName", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_CONSUME3:
                                    if (iscFirst.equals("13")){
                                        param.put("userConsumer2.ucId", loginId+"2");
                                        param.put("userConsumer1.ucId", loginId+"1");
                                        param.put("userConsumer4.ucId", loginId+"4");
                                        param.put("userConsumer5.ucId", loginId+"5");
                                        param.put("userConsumer6.ucId", loginId+"6");
                                    }
                                    param.put("userConsumer3.ucId", loginId+"3");
                                    param.put("userConsumer3.ucName", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_CONSUME4:
                                    if (iscFirst.equals("13")){
                                        param.put("userConsumer2.ucId", loginId+"2");
                                        param.put("userConsumer3.ucId", loginId+"3");
                                        param.put("userConsumer1.ucId", loginId+"1");
                                        param.put("userConsumer5.ucId", loginId+"5");
                                        param.put("userConsumer6.ucId", loginId+"6");
                                    }
                                    param.put("userConsumer4.ucId", loginId+"4");
                                    param.put("userConsumer4.ucName", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_CONSUME5:
                                    if (iscFirst.equals("13")){
                                        param.put("userConsumer2.ucId", loginId+"2");
                                        param.put("userConsumer3.ucId", loginId+"3");
                                        param.put("userConsumer4.ucId", loginId+"4");
                                        param.put("userConsumer1.ucId", loginId+"1");
                                        param.put("userConsumer6.ucId", loginId+"6");
                                    }
                                    param.put("userConsumer5.ucId", loginId+"5");
                                    param.put("userConsumer5.ucName", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_CONSUME6:
                                    if (iscFirst.equals("13")){
                                        param.put("userConsumer2.ucId", loginId+"2");
                                        param.put("userConsumer3.ucId", loginId+"3");
                                        param.put("userConsumer4.ucId", loginId+"4");
                                        param.put("userConsumer5.ucId", loginId+"5");
                                        param.put("userConsumer1.ucId", loginId+"1");
                                    }
                                    param.put("userConsumer6.ucId", loginId+"6");
                                    param.put("userConsumer6.ucName", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_HOMETOWN:
                                    param.put("uCity", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_OCCUPATION:
                                    param.put("uVocational", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                                case TYPE_COMADDRESS:
                                    param.put("uCompanyAddress", info);
                                    setResult(RESULT_OK, new Intent().putExtra("value", info));
                                    break;
                            }
                            return param;
                        }

                    };
                    MySingleton.getInstance(ProfileUpdateActivity.this).addToRequestQueue(request);
                }else {
                    Toast.makeText(ProfileUpdateActivity.this,"内容不能为空！",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
