package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-10-19.
 */

public class FWFKDetailActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener{
    private EditText et_gr_pp;
    private EditText et_dafen;
    private EditText et_jianyi;
    private TextView tv_xiadan;
    private TextView tv_jiedan;
    private ImageView iv_xiadan;
    private Button btn_queren;
    private ImageView iv_jiedan;
    private CheckBox cb_zy1;
    private CheckBox cb_zy2;
    private CheckBox cb_zy3;
    private CheckBox cb_zhiliang1;
    private CheckBox cb_zhiliang2;
    private CheckBox cb_zhiliang3;
    private CheckBox cb_dingjia1;
    private CheckBox cb_dingjia2;
    private CheckBox cb_dingjia3;
    private CheckBox cb_xfty1;
    private CheckBox cb_xfty2;
    private CheckBox cb_xfty3;
    private TextView tv_dp_pp_gr;
    private TextView tv_dafen;
    private TextView tv_jianyi;

    private String zy="",zhl="",dj="",xf="",path1 = "",path2 = "",biaoshi2,orderId,feedbackSeq;
    private String biaoshi,cdzy,cdzhl,cddj,cdxf,cduserSign,cdmerSign,cdbiaoti,cddafen,cdjianyi,resv5,companyId;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_fankui);
        et_gr_pp = (EditText) findViewById(R.id.et_dp_pp_gr);
        et_dafen = (EditText) findViewById(R.id.et_dafen);
        et_jianyi = (EditText) findViewById(R.id.et_jianyi);
        tv_xiadan = (TextView) findViewById(R.id.tv_xiadan);
        tv_jiedan = (TextView) findViewById(R.id.tv_jiedan);
        iv_xiadan = (ImageView) findViewById(R.id.iv_xiadan);
        iv_jiedan = (ImageView) findViewById(R.id.iv_jiedan);
        cb_zy1 = (CheckBox) findViewById(R.id.cb_zy1);
        cb_zy2 = (CheckBox) findViewById(R.id.cb_zy2);
        cb_zy3 = (CheckBox) findViewById(R.id.cb_zy3);
        cb_zhiliang1 = (CheckBox) findViewById(R.id.cb_zhiliang1);
        cb_zhiliang2 = (CheckBox) findViewById(R.id.cb_zhiliang2);
        cb_zhiliang3 = (CheckBox) findViewById(R.id.cb_zhiliang3);
        cb_dingjia1 = (CheckBox) findViewById(R.id.cb_dingjia1);
        cb_dingjia2 = (CheckBox) findViewById(R.id.cb_dingjia2);
        cb_dingjia3 = (CheckBox) findViewById(R.id.cb_dingjia3);
        cb_xfty1 = (CheckBox) findViewById(R.id.cb_xfty1);
        cb_xfty2 = (CheckBox) findViewById(R.id.cb_xfty2);
        cb_xfty3 = (CheckBox) findViewById(R.id.cb_xfty3);
        btn_queren = (Button) findViewById(R.id.btn_queren);
        tv_dp_pp_gr = (TextView) findViewById(R.id.tv_dp_pp_gr);
        tv_dafen = (TextView) findViewById(R.id.tv_dafen);
        tv_jianyi = (TextView) findViewById(R.id.tv_jianyi);

        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        orderId = this.getIntent().getStringExtra("orderId");
        feedbackSeq = this.getIntent().getStringExtra("feedbackSeq");
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        biaoshi2 = this.getIntent().getStringExtra("biaoshi2");
        cdmerSign = this.getIntent().getStringExtra("merSign");
        cdbiaoti = this.getIntent().getStringExtra("biaoti");
        resv5 = this.getIntent().hasExtra("resv5")?this.getIntent().getStringExtra("resv5"):"";
        companyId = this.getIntent().hasExtra("companyId")?this.getIntent().getStringExtra("companyId"):"";
        if (biaoshi.equals("12")) {
            setlistener1();
        }else if (biaoshi2.equals("00")&&biaoshi.equals("11")) {
            setlistener();
        }else if (biaoshi.equals("11")&&(biaoshi2.equals("02")||biaoshi2.equals("03"))){
            cdzy = this.getIntent().getStringExtra("zy");
            cdzhl = this.getIntent().getStringExtra("zhl");
            cddj = this.getIntent().getStringExtra("dj");
            cdxf = this.getIntent().getStringExtra("xf");
            cddafen = this.getIntent().getStringExtra("dafen");
            cdjianyi = this.getIntent().getStringExtra("jianyi");
            cduserSign = this.getIntent().getStringExtra("userSign");
            initView();
        }
    }

    private void initView() {
        et_gr_pp.setVisibility(View.INVISIBLE);
        et_jianyi.setVisibility(View.INVISIBLE);
        et_dafen.setVisibility(View.INVISIBLE);
        tv_dp_pp_gr.setVisibility(View.VISIBLE);
        tv_jianyi.setVisibility(View.VISIBLE);
        tv_dafen.setVisibility(View.VISIBLE);
        btn_queren.setVisibility(View.INVISIBLE);
        tv_dp_pp_gr.setText(cdbiaoti);
        tv_dafen.setText(cddafen);
        tv_jianyi.setText(cdjianyi);
        tv_xiadan.setVisibility(View.INVISIBLE);
        iv_xiadan.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(FXConstant.URL_FuwuFanKuiTuPian_Query+cduserSign,iv_xiadan);
        tv_jiedan.setVisibility(View.INVISIBLE);
        iv_jiedan.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(FXConstant.URL_FuwuFanKuiTuPian_Query+cdmerSign,iv_jiedan);
        if (cdzy!=null&&!"".equals(cdzy)) {
            if (cdzy.equals("01")) {
                cb_zy1.setChecked(true);
            } else if (cdzy.equals("02")) {
                cb_zy2.setChecked(true);
            } else {
                cb_zy3.setChecked(true);
            }
        }
        if (cdzhl!=null&&!"".equals(cdzhl)) {
            if (cdzhl.equals("01")) {
                cb_zhiliang1.setChecked(true);
            } else if (cdzhl.equals("02")) {
                cb_zhiliang2.setChecked(true);
            } else {
                cb_zhiliang3.setChecked(true);
            }
        }
        if (cddj!=null&&!"".equals(cddj)) {
            if (cddj.equals("01")) {
                cb_dingjia1.setChecked(true);
            } else if (cddj.equals("02")) {
                cb_dingjia2.setChecked(true);
            } else {
                cb_dingjia3.setChecked(true);
            }
        }
        if (cdxf!=null&&!"".equals(cdxf)) {
            if (cdxf.equals("01")) {
                cb_xfty1.setChecked(true);
            } else if (cdxf.equals("02")) {
                cb_xfty2.setChecked(true);
            } else {
                cb_xfty3.setChecked(true);
            }
        }
        cb_zy1.setEnabled(false);
        cb_zy2.setEnabled(false);
        cb_zy3.setEnabled(false);
        cb_zhiliang1.setEnabled(false);
        cb_zhiliang2.setEnabled(false);
        cb_zhiliang3.setEnabled(false);
        cb_dingjia1.setEnabled(false);
        cb_dingjia2.setEnabled(false);
        cb_dingjia3.setEnabled(false);
        cb_xfty1.setEnabled(false);
        cb_xfty2.setEnabled(false);
        cb_xfty3.setEnabled(false);
    }

    private void setlistener1() {
        tv_dafen.setVisibility(View.INVISIBLE);
        tv_dp_pp_gr.setVisibility(View.INVISIBLE);
        tv_jianyi.setVisibility(View.INVISIBLE);
        et_jianyi.setEnabled(false);
        et_dafen.setEnabled(false);
        btn_queren.setOnClickListener(this);
        tv_jiedan.setOnClickListener(this);
        iv_jiedan.setOnClickListener(this);
        cb_zy1.setEnabled(false);
        cb_zy2.setEnabled(false);
        cb_zy3.setEnabled(false);
        cb_dingjia1.setEnabled(false);
        cb_dingjia2.setEnabled(false);
        cb_dingjia3.setEnabled(false);
        cb_xfty1.setEnabled(false);
        cb_xfty2.setEnabled(false);
        cb_xfty3.setEnabled(false);
        cb_zhiliang1.setEnabled(false);
        cb_zhiliang2.setEnabled(false);
        cb_zhiliang3.setEnabled(false);
    }
    private void setlistener() {
        tv_dafen.setVisibility(View.INVISIBLE);
        tv_dp_pp_gr.setVisibility(View.INVISIBLE);
        tv_jianyi.setVisibility(View.INVISIBLE);
        tv_dp_pp_gr.setEnabled(false);
        et_gr_pp.setEnabled(false);
        btn_queren.setOnClickListener(this);
        tv_xiadan.setOnClickListener(this);
        cb_zy1.setOnCheckedChangeListener(this);
        cb_zy2.setOnCheckedChangeListener(this);
        cb_zy3.setOnCheckedChangeListener(this);
        cb_dingjia1.setOnCheckedChangeListener(this);
        cb_dingjia2.setOnCheckedChangeListener(this);
        cb_dingjia3.setOnCheckedChangeListener(this);
        cb_xfty1.setOnCheckedChangeListener(this);
        cb_xfty2.setOnCheckedChangeListener(this);
        cb_xfty3.setOnCheckedChangeListener(this);
        cb_zhiliang1.setOnCheckedChangeListener(this);
        cb_zhiliang2.setOnCheckedChangeListener(this);
        cb_zhiliang3.setOnCheckedChangeListener(this);
        tv_jiedan.setVisibility(View.INVISIBLE);
        iv_jiedan.setVisibility(View.VISIBLE);
        et_gr_pp.setText(cdbiaoti);
        ImageLoader.getInstance().displayImage(FXConstant.URL_FuwuFanKuiTuPian_Query+cdmerSign,iv_jiedan);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_zy1:
                cb_zy2.setChecked(false);
                cb_zy3.setChecked(false);
                zy = "01";
                break;
            case R.id.cb_zy2:
                cb_zy1.setChecked(false);
                cb_zy3.setChecked(false);
                zy = "02";
                break;
            case R.id.cb_zy3:
                cb_zy2.setChecked(false);
                cb_zy1.setChecked(false);
                zy = "03";
                break;
            case R.id.cb_dingjia1:
                cb_dingjia2.setChecked(false);
                cb_dingjia3.setChecked(false);
                dj = "01";
                break;
            case R.id.cb_dingjia2:
                cb_dingjia1.setChecked(false);
                cb_dingjia3.setChecked(false);
                dj = "02";
                break;
            case R.id.cb_dingjia3:
                cb_dingjia2.setChecked(false);
                cb_dingjia1.setChecked(false);
                dj = "03";
                break;
            case R.id.cb_zhiliang1:
                cb_zhiliang2.setChecked(false);
                cb_zhiliang3.setChecked(false);
                zhl = "01";
                break;
            case R.id.cb_zhiliang2:
                cb_zhiliang1.setChecked(false);
                cb_zhiliang3.setChecked(false);
                zhl = "02";
                break;
            case R.id.cb_zhiliang3:
                cb_zhiliang2.setChecked(false);
                cb_zhiliang1.setChecked(false);
                zhl = "03";
                break;
            case R.id.cb_xfty1:
                cb_xfty2.setChecked(false);
                cb_xfty3.setChecked(false);
                xf = "01";
                break;
            case R.id.cb_xfty2:
                cb_xfty1.setChecked(false);
                cb_xfty3.setChecked(false);
                xf = "02";
                break;
            case R.id.cb_xfty3:
                cb_xfty2.setChecked(false);
                cb_xfty1.setChecked(false);
                xf = "03";
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            if (data!=null){
                path1 = data.getStringExtra("xdimageName");
                tv_xiadan.setVisibility(View.INVISIBLE);
                iv_xiadan.setVisibility(View.VISIBLE);
                iv_xiadan.setImageURI(Uri.fromFile(new File(path1)));
            }
        }else if (requestCode == 1){
            if (data!=null){
                path2 = data.getStringExtra("jdimageName");
                tv_jiedan.setVisibility(View.INVISIBLE);
                iv_jiedan.setVisibility(View.VISIBLE);
                iv_jiedan.setImageURI(Uri.fromFile(new File(path2)));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_xiadan:
                startActivityForResult(new Intent(FWFKDetailActivity.this,QianMingActivity.class).putExtra("biaoshi","21"),0);
                break;
            case R.id.iv_xiadan:
                startActivityForResult(new Intent(FWFKDetailActivity.this,QianMingActivity.class).putExtra("biaoshi","21"),0);
                break;
            case R.id.tv_jiedan:
                startActivityForResult(new Intent(FWFKDetailActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),1);
                break;
            case R.id.iv_jiedan:
                startActivityForResult(new Intent(FWFKDetailActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),1);
                break;
            case R.id.btn_queren:
                sendToService();
                break;
        }
    }
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(date);
    }
    private void sendToService() {
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("正在提交...");
        dialog.show();
        final List<File> files1 = new ArrayList<File>();
        File file1 = null;
        file1 = new File(path1);
        if (file1.exists()) {
            files1.add(file1);
        }
        final List<File> file2 = new ArrayList<File>();
        File file = null;
        file = new File(path2);
        if (file.exists()) {
            file2.add(file);
        }
        List<Param> param = new ArrayList<>();
        String url = "";
        if ("00".equals(biaoshi2)){
            url=FXConstant.URL_FuwuFanKui_Update;
            param.add(new Param("updateTime",getNowTime()));
            param.add(new Param("feedbackSeq",feedbackSeq));
        }else {
            url=FXConstant.URL_FuwuFanKui_Create;
            param.add(new Param("userId", et_gr_pp.getText().toString().trim()));
            param.add(new Param("merId",DemoHelper.getInstance().getCurrentUsernName()));
            param.add(new Param("orderId",orderId));
        }
        if (!"".equals(zy)) {
            param.add(new Param("level1", zy));
        }
        if (!"".equals(zhl)) {
            param.add(new Param("level2", zhl));
        }
        if (!"".equals(dj)) {
            param.add(new Param("level3", dj));
        }
        if (!"".equals(xf)) {
            param.add(new Param("level4", xf));
        }
        if (!TextUtils.isEmpty(et_dafen.getText().toString().trim())) {
            param.add(new Param("score", et_dafen.getText().toString().trim()));
        }
        String advice = et_jianyi.getText().toString().trim();
        if (advice==null||TextUtils.isEmpty(advice)) {
            advice = "";
        }
        param.add(new Param("advice", advice));
        if (files1.size()>0||file2.size()>0) {
            final File finalFile = file;
            final File finalFile1 = file1;
            final String finalUrl = url;
            OkHttpManager.getInstance().post(param, files1, file2,url , new OkHttpManager.HttpCallBack() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    dialog.dismiss();
                    String code = jsonObject.getString("code");
                    if (code.equals("SUCCESS")) {
                        Toast.makeText(FWFKDetailActivity.this, "操作成功！", Toast.LENGTH_SHORT).show();
                        if (!"".equals(resv5)&&!"".equals(companyId)){
                            reduceQjcount();
                        }else if (finalUrl.equals(FXConstant.URL_FuwuFanKui_Update)){
                            reduceUscount();
                        }
                        if ("00".equals(biaoshi2)) {
                            updateOrderState();
                        }else {
                            setResult(RESULT_OK);
                            finish();
                        }
                        if (finalFile.exists()){
                            finalFile.delete();
                        }
                        if (finalFile1.exists()){
                            finalFile1.delete();
                        }
                    } else {
                        Toast.makeText(FWFKDetailActivity.this, "操作失败,code:" + code, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String errorMsg) {
                    dialog.dismiss();
                    Toast.makeText(FWFKDetailActivity.this, "网络繁忙，请稍后再试" + errorMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "必须先签名才可以提交！" , Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOrderState() {
        String url = FXConstant.URL_SHOUHOU_UPDATE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(FWFKDetailActivity.this,"网络连接错误！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderId",orderId);
                param.put("state","11");
                return param;
            }
        };
        MySingleton.getInstance(FWFKDetailActivity.this).addToRequestQueue(request);
    }

    private void reduceUscount() {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderUnReadCount","-1");
                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(FWFKDetailActivity.this).addToRequestQueue(request);
    }

    private void reduceQjcount() {
        if (companyId==null||"".equals(companyId)){
            companyId = DemoApplication.getInstance().getCurrentQiYeId();
        }
        String url = FXConstant.URL_UPDATE_UNREADQIYE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderCount","-1");
                param.put("companyId",companyId);
                return param;
            }
        };
        MySingleton.getInstance(FWFKDetailActivity.this).addToRequestQueue(request);
    }
}
