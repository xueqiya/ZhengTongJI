package com.sangu.apptongji.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-12-14.
 */

public class JieDanSettingActivity extends BaseActivity implements View.OnClickListener{
    private CheckBox cb_quanguo,cb_juli;
    private CheckBox cb_zhy_xs,cb_buxian_xs,cb_bh;
    private CheckBox cb_zhy_ts,cb_juli_ts;

    private CheckBox cb_messageorder_first,cb_messageorder_second,cb_messageorder_thrid;
    private CheckBox cb_sendorderprice_frist,cb_sendorderprice_second;
    private CheckBox cb_businessHire_first,cb_businessHire_second,cb_businessHire_thrid;
    private CheckBox cb_trainlearn_first,cb_trainlearn_second,cb_trainlearn_thrid;

    private EditText et_juli,et_sendorderprice;
    private Button btn_queren;
    private Button btn_messageOrder;
    private RelativeLayout rl_juli;
    private String orderByUpName,orderByDistance,prompt;
    private String messageOrderType = "01",sendOrderPriceType= "0",businessHireType = "01",trainlearnType = "01";
    private CustomProgressDialog dialog;
    private TextView tv_setting;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_jiedan_sitting);
        dialog = CustomProgressDialog.createDialog(this);
        dialog.setMessage("正在加载数据");
        dialog.setCancelable(true);
        dialog.show();
        MySingleton.getInstance(this).getRequestQueue();
        rl_juli = (RelativeLayout) findViewById(R.id.rl_juli);
        btn_queren = (Button) findViewById(R.id.btn_queren);
        btn_messageOrder = (Button) findViewById(R.id.btn_messageOrder);
        et_juli = (EditText) findViewById(R.id.et_juli);
        cb_quanguo = (CheckBox) findViewById(R.id.cb_quanguo);
        cb_juli = (CheckBox) findViewById(R.id.cb_juli);
        cb_zhy_xs = (CheckBox) findViewById(R.id.cb_zhy_xs);
        cb_buxian_xs = (CheckBox) findViewById(R.id.cb_buxian_xs);
        cb_zhy_ts = (CheckBox) findViewById(R.id.cb_zhy_ts);
        cb_juli_ts = (CheckBox) findViewById(R.id.cb_juli_ts);
        cb_bh = (CheckBox) findViewById(R.id.cb_zhy_bh);
        cb_messageorder_first = (CheckBox) findViewById(R.id.cb_all);
        cb_messageorder_second = (CheckBox) findViewById(R.id.cb_gc);
        cb_messageorder_thrid = (CheckBox) findViewById(R.id.cb_gr);
        cb_sendorderprice_frist = (CheckBox) findViewById(R.id.cb_priceall);
        cb_sendorderprice_second = (CheckBox) findViewById(R.id.cb_sendorderprice);
        cb_businessHire_first = (CheckBox) findViewById(R.id.cb_first);
        cb_businessHire_second = (CheckBox) findViewById(R.id.cb_second);
        cb_businessHire_thrid = (CheckBox) findViewById(R.id.cb_thrid);
        cb_trainlearn_first = (CheckBox) findViewById(R.id.cb_trainlearn_first);
        cb_trainlearn_second = (CheckBox) findViewById(R.id.cb_trainlearn_second);
        cb_trainlearn_thrid = (CheckBox) findViewById(R.id.cb_trainlearn_thrid);
        et_sendorderprice = (EditText) findViewById(R.id.et_sendorderprice);
        et_sendorderprice.setFocusable(true);
        et_sendorderprice.setFocusableInTouchMode(true);
        et_sendorderprice.requestFocus();
        et_sendorderprice.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
        rl_juli.setFocusable(true);
        rl_juli.setFocusableInTouchMode(true);
        rl_juli.requestFocus();
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JieDanSettingActivity.this, GonggaoListActivity.class));
            }
        });

        setListener();
        getSettingDetail();
    }

    private void setListener() {
        et_juli.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_juli.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().length()>6){
                    et_juli.setText(s.subSequence(0, 6));
                    et_juli.setSelection(6);
                    return;
                }
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        et_juli.setText(s);
                        et_juli.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    et_juli.setText(s);
                    et_juli.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        et_juli.setText(s.subSequence(0, 1));
                        et_juli.setSelection(1);
                        return;
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        cb_quanguo.setOnClickListener(this);
        cb_juli.setOnClickListener(this);
        cb_zhy_xs.setOnClickListener(this);
        cb_buxian_xs.setOnClickListener(this);
        cb_zhy_ts.setOnClickListener(this);
        cb_juli_ts.setOnClickListener(this);
        cb_bh.setOnClickListener(this);
        cb_messageorder_first.setOnClickListener(this);
        cb_messageorder_second.setOnClickListener(this);
        cb_messageorder_thrid.setOnClickListener(this);
        cb_sendorderprice_frist.setOnClickListener(this);
        cb_sendorderprice_second.setOnClickListener(this);
        cb_businessHire_first.setOnClickListener(this);
        cb_businessHire_second.setOnClickListener(this);
        cb_businessHire_thrid.setOnClickListener(this);
        cb_trainlearn_first.setOnClickListener(this);
        cb_trainlearn_second.setOnClickListener(this);
        cb_trainlearn_thrid.setOnClickListener(this);
        btn_queren.setOnClickListener(this);
        btn_messageOrder.setOnClickListener(this);
    }

    private void getSettingDetail() {
        String url = FXConstant.URL_QUERY_JIEDANSET;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "查询" + s);
                JSONObject object = JSON.parseObject(s);
                JSONObject screenInfo = object.getJSONObject("screenInfo");
                if (screenInfo == null) {
                    ToastUtils.showNOrmalToast(getApplicationContext(),"获取数据失败，请重新获取！");
                    return;
                }
                orderByUpName = screenInfo.getString("orderByUpName");
                orderByDistance = screenInfo.getString("orderByDistance");
                prompt = screenInfo.getString("speedPush");
                messageOrderType = screenInfo.getString("messageOrder");
                sendOrderPriceType = screenInfo.getString("sendOrderPrice");
                businessHireType = screenInfo.getString("businessHire");
                trainlearnType = screenInfo.getString("trainLearn");
                String allPush = screenInfo.getString("allPush");

                if (trainlearnType==null||"".equals(trainlearnType)) {

                }else if (trainlearnType.equals("01")){
                    cb_trainlearn_first.setChecked(true);
                    cb_trainlearn_second.setChecked(false);
                    cb_trainlearn_thrid.setChecked(false);
                }else if (trainlearnType.equals("02")){
                    cb_trainlearn_first.setChecked(false);
                    cb_trainlearn_second.setChecked(true);
                    cb_trainlearn_thrid.setChecked(false);
                }else if (trainlearnType.equals("03")){
                    cb_trainlearn_first.setChecked(false);
                    cb_trainlearn_second.setChecked(false);
                    cb_trainlearn_thrid.setChecked(true);
                }

                if (businessHireType==null||"".equals(businessHireType)) {

                }else if (businessHireType.equals("01")){
                    cb_businessHire_first.setChecked(true);
                    cb_businessHire_second.setChecked(false);
                    cb_businessHire_thrid.setChecked(false);
                }else if (businessHireType.equals("02")){
                    cb_businessHire_first.setChecked(false);
                    cb_businessHire_second.setChecked(true);
                    cb_businessHire_thrid.setChecked(false);
                }else if (businessHireType.equals("03")){
                    cb_businessHire_first.setChecked(false);
                    cb_businessHire_second.setChecked(false);
                    cb_businessHire_thrid.setChecked(true);
                }

                if (sendOrderPriceType==null||"".equals(sendOrderPriceType)) {

                }else if (sendOrderPriceType.equals("0")){
                    cb_sendorderprice_frist.setChecked(true);
                    cb_sendorderprice_second.setChecked(false);
                }else if (Double.parseDouble(sendOrderPriceType) > 0){
                    cb_sendorderprice_frist.setChecked(false);
                    cb_sendorderprice_second.setChecked(true);
                    et_sendorderprice.setText(sendOrderPriceType);
                }

                if (messageOrderType==null||"".equals(messageOrderType)) {

                }else if (messageOrderType.equals("01")){
                    cb_messageorder_first.setChecked(true);
                    cb_messageorder_second.setChecked(false);
                    cb_messageorder_thrid.setChecked(false);
                }else if (messageOrderType.equals("02")){
                    cb_messageorder_first.setChecked(false);
                    cb_messageorder_second.setChecked(true);
                    cb_messageorder_thrid.setChecked(false);
                }else if (messageOrderType.equals("03")){
                    cb_messageorder_first.setChecked(false);
                    cb_messageorder_second.setChecked(false);
                    cb_messageorder_thrid.setChecked(true);
                }

                if (orderByDistance==null||"".equals(orderByDistance)){
                    cb_quanguo.setChecked(false);
                    cb_juli.setChecked(false);
                }else if ("0".equals(orderByDistance)){
                    cb_quanguo.setChecked(true);
                    cb_juli.setChecked(false);
                }else {
                    et_juli.setText(orderByDistance);
                    cb_quanguo.setChecked(false);
                    cb_juli.setChecked(true);
                }
                if (orderByUpName==null||"".equals(orderByUpName)){
                    cb_zhy_xs.setChecked(false);
                    cb_buxian_xs.setChecked(false);
                    cb_bh.setChecked(false);
                }else if ("0".equals(orderByUpName)){
                    cb_zhy_xs.setChecked(true);
                    cb_buxian_xs.setChecked(false);
                    cb_bh.setChecked(false);
                }else if ("2".equals(orderByUpName)){
                    cb_zhy_xs.setChecked(false);
                    cb_buxian_xs.setChecked(false);
                    cb_bh.setChecked(true);
                }else {
                    cb_zhy_xs.setChecked(false);
                    cb_buxian_xs.setChecked(true);
                    cb_bh.setChecked(false);
                }

                if (prompt==null||"".equals(prompt)){
                    if (!TextUtils.isEmpty(allPush) && allPush.equalsIgnoreCase("1")) {
                        cb_zhy_ts.setChecked(true);
                        cb_juli_ts.setChecked(false);
                    } else {
                        cb_zhy_ts.setChecked(false);
                        cb_juli_ts.setChecked(false);
                    }

                }else if ("0".equals(prompt) && allPush.equalsIgnoreCase("0")){
                    cb_zhy_ts.setChecked(false);
                    cb_juli_ts.setChecked(false);
                }else {
                    cb_zhy_ts.setChecked(true);
                    cb_juli_ts.setChecked(false);
                }
                if (!isFinishing()&&dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
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
                param.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cb_quanguo:
                cb_quanguo.setChecked(true);
                cb_juli.setChecked(false);
                break;
            case R.id.cb_juli:
                cb_quanguo.setChecked(false);
                cb_juli.setChecked(true);
                break;
            case R.id.cb_zhy_xs:
                cb_zhy_xs.setChecked(true);
                cb_buxian_xs.setChecked(false);
                cb_bh.setChecked(false);
                break;
            case R.id.cb_zhy_bh:
                cb_buxian_xs.setChecked(false);
                cb_zhy_xs.setChecked(false);
                cb_bh.setChecked(true);
                break;
            case R.id.cb_buxian_xs:
                cb_zhy_xs.setChecked(false);
                cb_buxian_xs.setChecked(true);
                cb_bh.setChecked(false);
                break;

            case R.id.cb_zhy_ts:
                cb_zhy_ts.setChecked(true);
                cb_juli_ts.setChecked(false);
                break;
            case R.id.cb_juli_ts:
                cb_zhy_ts.setChecked(false);
                cb_juli_ts.setChecked(true);
                break;
            case R.id.cb_all:
                cb_messageorder_first.setChecked(true);
                cb_messageorder_second.setChecked(false);
                cb_messageorder_thrid.setChecked(false);
                break;
            case R.id.cb_gc:
                cb_messageorder_first.setChecked(false);
                cb_messageorder_second.setChecked(true);
                cb_messageorder_thrid.setChecked(false);
                break;
            case R.id.cb_gr:
                cb_messageorder_first.setChecked(false);
                cb_messageorder_second.setChecked(false);
                cb_messageorder_thrid.setChecked(true);
                break;
            case R.id.cb_priceall:
                cb_sendorderprice_frist.setChecked(true);
                cb_sendorderprice_second.setChecked(false);
                break;
            case R.id.cb_sendorderprice:
                cb_sendorderprice_frist.setChecked(false);
                cb_sendorderprice_second.setChecked(true);
                break;
            case R.id.cb_first:
                cb_businessHire_first.setChecked(true);
                cb_businessHire_second.setChecked(false);
                cb_businessHire_thrid.setChecked(false);
                break;
            case R.id.cb_second:
                cb_businessHire_first.setChecked(false);
                cb_businessHire_second.setChecked(true);
                cb_businessHire_thrid.setChecked(false);
                break;
            case R.id.cb_thrid:
                cb_businessHire_first.setChecked(false);
                cb_businessHire_second.setChecked(false);
                cb_businessHire_thrid.setChecked(true);
                break;
            case R.id.cb_trainlearn_first:
                cb_trainlearn_first.setChecked(true);
                cb_trainlearn_second.setChecked(false);
                cb_trainlearn_thrid.setChecked(false);
                break;
            case R.id.cb_trainlearn_second:
                cb_trainlearn_first.setChecked(false);
                cb_trainlearn_second.setChecked(true);
                cb_trainlearn_thrid.setChecked(false);
                break;
            case R.id.cb_trainlearn_thrid:
                cb_trainlearn_first.setChecked(false);
                cb_trainlearn_second.setChecked(false);
                cb_trainlearn_thrid.setChecked(true);
                break;
            case R.id.btn_queren:
                if (dialog!=null){
                    dialog.setMessage("正在提交数据");
                }
                orderByDistance = et_juli.getText().toString().trim();
                if (orderByDistance==null|| TextUtils.isEmpty(orderByDistance)){
                    orderByDistance = "0";
                }
                submitJDSetting();
                break;
            case R.id.btn_messageOrder:

                Intent intent = new Intent(JieDanSettingActivity.this, MessageOrderIntroduceActivity.class);

                startActivityForResult(intent,0);

                break;
        }
    }

    private void submitJDSetting() {
        if (cb_zhy_xs.isChecked()){
            orderByUpName = "0";
        }else if (cb_bh.isChecked()){
            orderByUpName = "2";
        }else {
            orderByUpName = "1";
        }
        if (cb_juli_ts.isChecked()){
            prompt = "1";
        }else {
            prompt = "0";
        }
        if (cb_quanguo.isChecked()){
            orderByDistance = "0";
        }

        if (cb_messageorder_first.isChecked()){
            messageOrderType = "01";
        }
        if (cb_messageorder_second.isChecked()){
            messageOrderType = "02";
        }
        if (cb_messageorder_thrid.isChecked()){
            messageOrderType = "03";
        }

        if (cb_sendorderprice_frist.isChecked()){
            sendOrderPriceType = "0";
        }
        if (cb_sendorderprice_second.isChecked()){
            sendOrderPriceType = et_sendorderprice.getText().toString().trim();
            if (sendOrderPriceType==null|| TextUtils.isEmpty(sendOrderPriceType)){
                sendOrderPriceType = "0";
            }
        }

        if (cb_businessHire_first.isChecked()){
            businessHireType = "01";
        }
        if (cb_businessHire_second.isChecked()){
            businessHireType = "02";
        }
        if (cb_businessHire_thrid.isChecked()){
            businessHireType = "03";
        }

        if (cb_trainlearn_first.isChecked()){
            trainlearnType = "01";
        }
        if (cb_trainlearn_second.isChecked()){
            trainlearnType = "02";
        }
        if (cb_trainlearn_thrid.isChecked()){
            trainlearnType = "03";
        }

        String url = FXConstant.URL_INSERT_JIEDANSET;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("jiedanSetac,s",s);
                JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("SUCCESS".equals(code)){
                    ToastUtils.showNOrmalToast(JieDanSettingActivity.this,"设置成功");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (!isFinishing()&&dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                ToastUtils.showNOrmalToast(JieDanSettingActivity.this,"网络不稳定");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                param.put("orderByUpName",orderByUpName);
                param.put("orderByDistance",orderByDistance);

                param.put("messageOrder",messageOrderType);
                param.put("sendOrderPrice",sendOrderPriceType);
                param.put("businessHire",businessHireType);
                param.put("trainLearn",trainlearnType);

                if (prompt.equalsIgnoreCase("1")) {
                    param.put("speedPush","1");
                    param.put("allPush","0");
                } else if (prompt.equalsIgnoreCase("0")) {
                    param.put("allPush ","1");
                    param.put("speedPush","1");
                }
                Log.e("jiedanSetac,p",param.toString());
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
}
