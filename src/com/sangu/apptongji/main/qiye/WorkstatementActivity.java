package com.sangu.apptongji.main.qiye;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.sangu.apptongji.main.alluser.order.avtivity.QianMingActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.widget.MyEditText;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-02-17.
 */

public class WorkstatementActivity extends BaseActivity implements View.OnClickListener {
    private ScrollView scll;
    private LinearLayout ll_neirong1;
    private MyEditText et_neirong1;
    private LinearLayout ll_neirong2;
    private MyEditText et_neirong2;
    private LinearLayout ll_neirong3;
    private MyEditText et_neirong3;
    private LinearLayout ll_neirong4;
    private MyEditText et_neirong4;
    private LinearLayout ll_neirong5;
    private MyEditText et_neirong5;
    private MyEditText et_jianyi;
    private MyEditText et_jianyi2;
    private Button btn_tianjia;
    private Button btn_quxiao;
    private Button btn_commit;
    private TextView tv_yonghu;
    private TextView tv_gongsi;
    private LinearLayout ll_shijian_yonghu;
    private LinearLayout ll_shijian_gongsi;
    private RelativeLayout rl_jianyi;
    private TextView tv_time_yonghu;
    private TextView tv_time_gongsi;
    private ImageView iv_yonghu;
    private ImageView iv_gongsi;

    private String biaoshi2,qiyeId,path1="",path2="",userName,biaoshi,managerId;
    private String userId,planContent,planAdvise,createTime,signature1,signature2,signatureTime1,signatureTime2;
    private int current_neirong=1;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_work_statement);
        scll = (ScrollView) findViewById(R.id.scll);
        ll_neirong1 = (LinearLayout) findViewById(R.id.ll_neirong1);
        ll_neirong2 = (LinearLayout) findViewById(R.id.ll_neirong2);
        ll_neirong3 = (LinearLayout) findViewById(R.id.ll_neirong3);
        ll_neirong4 = (LinearLayout) findViewById(R.id.ll_neirong4);
        ll_neirong5 = (LinearLayout) findViewById(R.id.ll_neirong5);
        ll_shijian_yonghu = (LinearLayout) findViewById(R.id.ll_shijian_yonghu);
        ll_shijian_gongsi = (LinearLayout) findViewById(R.id.ll_shijian_gongsi);
        rl_jianyi = (RelativeLayout) findViewById(R.id.rl_jianyi);
        et_neirong1 = (MyEditText) findViewById(R.id.et_neirong1);
        et_neirong2 = (MyEditText) findViewById(R.id.et_neirong2);
        et_neirong3 = (MyEditText) findViewById(R.id.et_neirong3);
        et_neirong4 = (MyEditText) findViewById(R.id.et_neirong4);
        et_neirong5 = (MyEditText) findViewById(R.id.et_neirong5);
        et_jianyi = (MyEditText) findViewById(R.id.et_jianyi);
        et_jianyi2 = (MyEditText) findViewById(R.id.et_jianyi2);
        btn_tianjia = (Button) findViewById(R.id.btn_tianjia);
        btn_quxiao = (Button) findViewById(R.id.btn_quxiao);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        tv_yonghu = (TextView) findViewById(R.id.tv_yonghu);
        tv_gongsi = (TextView) findViewById(R.id.tv_gongsi);
        tv_time_yonghu = (TextView) findViewById(R.id.tv_time_yonghu);
        tv_time_gongsi = (TextView) findViewById(R.id.tv_time_gongsi);
        iv_yonghu = (ImageView) findViewById(R.id.iv_yonghu);
        iv_gongsi = (ImageView) findViewById(R.id.iv_gongsi);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        Intent intent = this.getIntent();
        managerId = intent.hasExtra("managerId") ? intent.getStringExtra("managerId") : "";
        biaoshi2 = intent.hasExtra("biaoshi2") ? intent.getStringExtra("biaoshi2") : "";
        biaoshi = intent.hasExtra("biaoshi") ? intent.getStringExtra("biaoshi") : "";
        qiyeId = intent.hasExtra("qiyeId") ? intent.getStringExtra("qiyeId") : "";
        userId = intent.hasExtra("userId") ? intent.getStringExtra("userId") : "";
        signatureTime1 = intent.hasExtra("signatureTime1") ? intent.getStringExtra("signatureTime1") : "";
        signatureTime2 = intent.hasExtra("signatureTime2") ? intent.getStringExtra("signatureTime2") : "";
        createTime = intent.hasExtra("createTime") ? intent.getStringExtra("createTime") : "";
        signature2 = intent.hasExtra("signature2") ? intent.getStringExtra("signature2") : "";
        signature1 = intent.hasExtra("signature1") ? intent.getStringExtra("signature1") : "";
        planAdvise = intent.hasExtra("planAdvise") ? intent.getStringExtra("planAdvise") : "";
        planContent = intent.hasExtra("planContent") ? intent.getStringExtra("planContent") : "";
        userName = intent.hasExtra("userName") ? intent.getStringExtra("userName") : "";
        if ("yonghu".equals(biaoshi2)) {
            tv_yonghu.setOnClickListener(this);
            iv_yonghu.setOnClickListener(this);
            btn_commit.setOnClickListener(this);
            btn_quxiao.setOnClickListener(this);
            btn_tianjia.setOnClickListener(this);
        }else if ("gongsi".equals(biaoshi2)) {
            if ("00".equals(biaoshi)) {
                btn_commit.setText("等待回复");
                btn_commit.setEnabled(false);
                et_jianyi2.setEnabled(false);
            }else {
                tv_gongsi.setOnClickListener(this);
                iv_gongsi.setOnClickListener(this);
                btn_commit.setOnClickListener(this);
                rl_jianyi.setOnClickListener(this);
            }
            initgongsi();
        }else if ("quanbu".equals(biaoshi2)){
            initall();
        }
    }

    private void updateQjcount() {
        if (qiyeId==null||"".equals(qiyeId)){
            qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        }
        String url = FXConstant.URL_UPDATE_UNREADQIYE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("reportCount","1");
                param.put("companyId",qiyeId);
                return param;
            }
        };
        MySingleton.getInstance(WorkstatementActivity.this).addToRequestQueue(request);
    }

    private void reduceQjcount() {
        if (qiyeId==null||"".equals(qiyeId)){
            qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        }
        String url = FXConstant.URL_UPDATE_UNREADQIYE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("reportCount","-1");
                param.put("companyId",qiyeId);
                return param;
            }
        };
        MySingleton.getInstance(WorkstatementActivity.this).addToRequestQueue(request);
    }

    private void initgongsi() {
        et_neirong1.setFocusableInTouchMode(false);
        et_neirong1.setFocusable(false);
        et_neirong2.setFocusableInTouchMode(false);
        et_neirong2.setFocusable(false);
        et_neirong3.setFocusableInTouchMode(false);
        et_neirong3.setFocusable(false);
        et_neirong4.setFocusableInTouchMode(false);
        et_neirong4.setFocusable(false);
        et_neirong5.setFocusableInTouchMode(false);
        et_neirong5.setFocusable(false);
        et_jianyi.setFocusableInTouchMode(false);
        et_jianyi.setFocusable(false);
        String content1="",content2="",content3="",content4="",content5="";
        String [] content = planContent.split("\\|,");
        if (content.length>0){
            content1 = content[0];
        }
        if (content.length>1){
            content2 = content[1];
        }
        if (content.length>2){
            content3 = content[2];
        }
        if (content.length>3){
            content4 = content[3];
        }
        if (content.length>4){
            content5 = content[4];
        }
        et_neirong1.setText(content1);
        if (!"".equals(content2)){
            ll_neirong2.setVisibility(View.VISIBLE);
            et_neirong2.setText(content2);
        }
        if (!"".equals(content3)){
            ll_neirong3.setVisibility(View.VISIBLE);
            et_neirong3.setText(content3);
        }
        if (!"".equals(content4)){
            ll_neirong4.setVisibility(View.VISIBLE);
            et_neirong4.setText(content4);
        }
        if (!"".equals(content5)){
            ll_neirong5.setVisibility(View.VISIBLE);
            et_neirong5.setText(content5);
        }
        et_jianyi.setText(planAdvise);
        et_jianyi2.setVisibility(View.VISIBLE);
        tv_yonghu.setVisibility(View.INVISIBLE);
        ll_shijian_yonghu.setVisibility(View.VISIBLE);
        String time1 = signatureTime1.substring(0, 4) + "-" + signatureTime1.substring(4, 6) + "-" + signatureTime1.substring(6, 8) + " "
                + signatureTime1.substring(8, 10) + ":" + signatureTime1.substring(10, 12);
        tv_time_yonghu.setText(time1);
        ImageLoader.getInstance().displayImage(FXConstant.URL_BAOBIAO_QIANMING+signature1,iv_yonghu);
    }

    private void initall() {
        et_neirong1.setFocusableInTouchMode(false);
        et_neirong1.setFocusable(false);
        et_neirong2.setFocusableInTouchMode(false);
        et_neirong2.setFocusable(false);
        et_neirong3.setFocusableInTouchMode(false);
        et_neirong3.setFocusable(false);
        et_neirong4.setFocusableInTouchMode(false);
        et_neirong4.setFocusable(false);
        et_neirong5.setFocusableInTouchMode(false);
        et_neirong5.setFocusable(false);
        et_jianyi.setFocusableInTouchMode(false);
        et_jianyi.setFocusable(false);
        String content1="",content2="",content3="",content4="",content5="";
        String [] content = planContent.split("\\|,");
        if (content.length>0){
            content1 = content[0];
        }
        if (content.length>1){
            content2 = content[1];
        }
        if (content.length>2){
            content3 = content[2];
        }
        if (content.length>3){
            content4 = content[3];
        }
        if (content.length>4){
            content5 = content[4];
        }
        et_neirong1.setText(content1);
        if (!"".equals(content2)){
            ll_neirong2.setVisibility(View.VISIBLE);
            et_neirong2.setText(content2);
        }
        if (!"".equals(content3)){
            ll_neirong3.setVisibility(View.VISIBLE);
            et_neirong3.setText(content3);
        }
        if (!"".equals(content4)){
            ll_neirong4.setVisibility(View.VISIBLE);
            et_neirong4.setText(content4);
        }
        if (!"".equals(content5)){
            ll_neirong5.setVisibility(View.VISIBLE);
            et_neirong5.setText(content5);
        }
        et_jianyi.setText(planAdvise);
        tv_yonghu.setVisibility(View.INVISIBLE);
        ll_shijian_yonghu.setVisibility(View.VISIBLE);
        String time1 = signatureTime1.substring(0, 4) + "-" + signatureTime1.substring(4, 6) + "-" + signatureTime1.substring(6, 8) + " "
                + signatureTime1.substring(8, 10) + ":" + signatureTime1.substring(10, 12);
        tv_time_yonghu.setText(time1);
        ImageLoader.getInstance().displayImage(FXConstant.URL_BAOBIAO_QIANMING+signature1,iv_yonghu);
        String [] advise = planAdvise.split("\\|,");
        if (advise.length>0){
            et_jianyi.setText(advise[0]);
        }
        if (advise.length>1){
            et_jianyi2.setVisibility(View.VISIBLE);
            et_jianyi2.setFocusableInTouchMode(false);
            et_jianyi2.setFocusable(false);
            et_jianyi2.setText(advise[1]);
        }
        tv_gongsi.setVisibility(View.INVISIBLE);
        ll_shijian_gongsi.setVisibility(View.VISIBLE);
        String time2 = signatureTime2.substring(0, 4) + "-" + signatureTime2.substring(4, 6) + "-" + signatureTime2.substring(6, 8) + " "
                + signatureTime2.substring(8, 10) + ":" + signatureTime2.substring(10, 12);
        tv_time_gongsi.setText(time2);
        ImageLoader.getInstance().displayImage(FXConstant.URL_BAOBIAO_QIANMING+signature2,iv_gongsi);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_tianjia:
                if (current_neirong<5) {
                    current_neirong++;
                    if (current_neirong==2){
                        ll_neirong2.setVisibility(View.VISIBLE);
                    }else if (current_neirong==3){
                        ll_neirong3.setVisibility(View.VISIBLE);
                    }else if (current_neirong==4){
                        ll_neirong4.setVisibility(View.VISIBLE);
                    }else if (current_neirong==5){
                        ll_neirong5.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(WorkstatementActivity.this,"最多添加五个内容哦...",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_quxiao:
                if (current_neirong>1) {
                    current_neirong--;
                    if (current_neirong==1){
                        ll_neirong2.setVisibility(View.GONE);
                    }else if (current_neirong==2){
                        ll_neirong3.setVisibility(View.GONE);
                    }else if (current_neirong==3){
                        ll_neirong4.setVisibility(View.GONE);
                    }else if (current_neirong==4){
                        ll_neirong5.setVisibility(View.GONE);
                    }
                }else {
                    Toast.makeText(WorkstatementActivity.this,"没有可以取消的内容了...",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_yonghu:
                startActivityForResult(new Intent(WorkstatementActivity.this, QianMingActivity.class).putExtra("biaoshi", "21"),0);
                break;
            case R.id.tv_gongsi:
                startActivityForResult(new Intent(WorkstatementActivity.this, QianMingActivity.class).putExtra("biaoshi", "22"),1);
                break;
            case R.id.iv_yonghu:
                startActivityForResult(new Intent(WorkstatementActivity.this, QianMingActivity.class).putExtra("biaoshi", "21"),0);
                break;
            case R.id.iv_gongsi:
                startActivityForResult(new Intent(WorkstatementActivity.this, QianMingActivity.class).putExtra("biaoshi", "22"),1);
                break;
            case R.id.rl_jianyi:
                et_jianyi2.setFocusable(true);
                et_jianyi2.setFocusableInTouchMode(true);
                et_jianyi2.requestFocus();
                et_jianyi2.requestFocusFromTouch();
                break;
            case R.id.btn_commit:
                String path="";
                if ("yonghu".equals(biaoshi2)) {
                    path = path1;
                }else if ("gongsi".equals(biaoshi2)){
                    path = path2;
                }
                if (!"".equals(path)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(WorkstatementActivity.this);
                    builder.setMessage("确定提交么");
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
                            insertPlan(biaoshi2);
                        }
                    });
                    builder.show();
                }else {
                    Toast.makeText(WorkstatementActivity.this,"签名之后才能确认哦！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void insertPlan(final String biaoshi2) {
        String content="";
        String content1 = et_neirong1.getText().toString().trim();
        String content2 = et_neirong2.getText().toString().trim();
        String content3 = et_neirong3.getText().toString().trim();
        String content4 = et_neirong4.getText().toString().trim();
        String content5 = et_neirong5.getText().toString().trim();
        if ("".equals(content1)&&"".equals(content2)&&"".equals(content3)&&"".equals(content4)&&"".equals(content5)){
            Toast.makeText(WorkstatementActivity.this,"请先编写内容！",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!"".equals(content1)){
            content = content1;
        }
        if (!"".equals(content2)){
            if (!"".equals(content)) {
                content = content + "|," + content2;
            }else {
                content = content2;
            }
        }
        if (!"".equals(content3)){
            if (!"".equals(content)) {
                content = content + "|," + content3;
            }else {
                content = content3;
            }
        }
        if (!"".equals(content4)){
            if (!"".equals(content)) {
                content = content + "|," + content4;
            }else {
                content = content4;
            }
        }
        if (!"".equals(content5)){
            if (!"".equals(content)) {
                content = content + "|," + content5;
            }else {
                content = content5;
            }
        }
        String advise = et_jianyi.getText().toString().trim();
        String advise2 = et_jianyi2.getText().toString().trim();
        String url="";
        String str="";
        List<Param> param=new ArrayList<>();
        param.add(new Param("companyId", qiyeId));
        final List<File> files = new ArrayList<File>();
        if ("yonghu".equals(biaoshi2)) {
            param.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
            param.add(new Param("planContent", content));
            param.add(new Param("planAdvise", advise));
            param.add(new Param("planTitle", userName));
            url = FXConstant.URL_QIYE_CHUANGJIANBAOBIAO;
            str = "signatureFail1";
            if (!"".equals(path1)) {
                File file1 = null;
                file1 = new File(path1);
                if (file1.exists()) {
                    files.add(file1);
                }else {
                    try {
                        file1.createNewFile();
                        files.add(file1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if ("gongsi".equals(biaoshi2)) {
            if (!TextUtils.isEmpty(et_jianyi2.getText().toString().trim())){
                param.add(new Param("planAdvise",advise+"|,"+advise2));
            }
            param.add(new Param("userId", userId));
            param.add(new Param("createTime", createTime));
            url = FXConstant.URL_QIYE_XIUGAIANBAOBIAO;
            str = "signatureFail2";
            if (!"".equals(path2)) {
                File file1 = null;
                file1 = new File(path2);
                if (file1.exists()) {
                    files.add(file1);
                }else {
                    try {
                        file1.createNewFile();
                        files.add(file1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        OkHttpManager.getInstance().post(str, param, files, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Toast.makeText(WorkstatementActivity.this,"操作成功！",Toast.LENGTH_SHORT).show();
                FileUtils.deleteAllFiles(files.get(0));
                if ("yonghu".equals(biaoshi2)) {
                    sendPushMessage(managerId);
                    updateQjcount();
                }else {
                    reduceQjcount();
                }
            }
            @Override
            public void onFailure(String errorMsg) {
                Log.e("fanhuishuju,failure",errorMsg);
                Toast.makeText(WorkstatementActivity.this,"网络连接错误，请重试！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPushMessage(final String hxid1) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        if (name==null||"".equals(name)){
            name = DemoHelper.getInstance().getCurrentUsernName();
        }
        final String finalName = name;
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                param.put("u_id",hxid1);
                param.put("body","工作报表消息");
                param.put("type","07");
                param.put("userId",myId);
                param.put("companyId", qiyeId);
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(WorkstatementActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 0:
                    if (data!=null){
                        path1 = data.getStringExtra("xdimageName");
                        tv_yonghu.setVisibility(View.INVISIBLE);
                        iv_yonghu.setVisibility(View.VISIBLE);
                        iv_yonghu.setImageURI(Uri.fromFile(new File(path1)));
                    }
                    break;
                case 1:
                    if (data!=null){
                        path2 = data.getStringExtra("jdimageName");
                        tv_gongsi.setVisibility(View.INVISIBLE);
                        iv_gongsi.setVisibility(View.VISIBLE);
                        iv_gongsi.setImageURI(Uri.fromFile(new File(path2)));
                    }
                    break;
            }
        }
    }
    @Override
    public void back(View view) {
        super.back(view);
    }

}
