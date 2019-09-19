package com.sangu.apptongji.main.qiye;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
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
import com.sangu.apptongji.main.utils.OpenFiles;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.widget.MyEditText;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import zhou.tools.fileselector.FileSelector;
import zhou.tools.fileselector.FileSelectorActivity;
import zhou.tools.fileselector.config.FileConfig;

/**
 * Created by Administrator on 2017-02-17.
 */

public class WorkstatementTwoActivity extends BaseActivity implements View.OnClickListener {
    private static final int REC_WENJIAN1 = 2;
    private static final int REC_WENJIAN2 = 3;
    private FileConfig fileConfig;
    private MyEditText et_jianyi;
    private MyEditText et_jianyi2;
    private Button btn_commit;
    private Button btn_wenjian1;
    private Button btn_wenjian2;
    private TextView tv_wenjian1;
    private TextView tv_wenjian2;
    private TextView tv_yonghu;
    private TextView tv_gongsi;
    private LinearLayout ll_shijian_yonghu;
    private LinearLayout ll_shijian_gongsi;
    private RelativeLayout rl_jianyi;
    private TextView tv_time_yonghu;
    private TextView tv_time_gongsi;
    private ImageView iv_yonghu;
    private ImageView iv_gongsi;

    private String biaoshi2,qiyeId,path1="",path2="",resv1,resv2,userName,biaoshi,managerId;
    private String userId,curPath1="",curPath2="",planAdvise,createTime,signature1,signature2,signatureTime1,signatureTime2;
    FileThread thread = null;
    LaowuThread thread2 = null;
    private Handler fileHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if (thread!=null&&!thread.isInterrupted()){
                        thread.interrupt();
                        thread=null;
                    }
                    String[] str1 = curPath1.split("\\.");
                    String index1 = str1[str1.length-1];
                    tv_wenjian1.setText("报表文件1"+"."+index1);
                    break;
                case 1:
                    if (thread2!=null&&!thread2.isInterrupted()){
                        thread2.interrupt();
                        thread2=null;
                    }
                    String[] str2 = curPath2.split("\\.");
                    String index2 = str2[str2.length-1];
                    tv_wenjian2.setText("报表文件2"+"."+index2);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_work_statementtwo);
        et_jianyi = (MyEditText) findViewById(R.id.et_jianyi);
        et_jianyi2 = (MyEditText) findViewById(R.id.et_jianyi2);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_wenjian1 = (Button) findViewById(R.id.btn_laowu);
        btn_wenjian2 = (Button) findViewById(R.id.btn_jiameng);
        tv_wenjian1 = (TextView) findViewById(R.id.tv_laowu);
        tv_wenjian2 = (TextView) findViewById(R.id.tv_jiameng);
        tv_yonghu = (TextView) findViewById(R.id.tv_yonghu);
        tv_gongsi = (TextView) findViewById(R.id.tv_gongsi);
        tv_time_yonghu = (TextView) findViewById(R.id.tv_time_yonghu);
        tv_time_gongsi = (TextView) findViewById(R.id.tv_time_gongsi);
        iv_yonghu = (ImageView) findViewById(R.id.iv_yonghu);
        iv_gongsi = (ImageView) findViewById(R.id.iv_gongsi);
        ll_shijian_yonghu = (LinearLayout) findViewById(R.id.ll_shijian_yonghu);
        ll_shijian_gongsi = (LinearLayout) findViewById(R.id.ll_shijian_gongsi);
        rl_jianyi = (RelativeLayout) findViewById(R.id.rl_jianyi);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        fileConfig = new FileConfig();
        File file = new File("/sdcard/bizchat/workstate1/");
        if (!file.exists()){
            file.mkdirs();
        }
        File file1 = new File("/sdcard/bizchat/workstate2/");
        if (!file1.exists()){
            file1.mkdirs();
        }
        Intent intent = this.getIntent();
        biaoshi2 = intent.hasExtra("biaoshi2") ? intent.getStringExtra("biaoshi2") : "";
        managerId = intent.hasExtra("managerId") ? intent.getStringExtra("managerId") : "";
        biaoshi = intent.hasExtra("biaoshi") ? intent.getStringExtra("biaoshi") : "";
        qiyeId = intent.hasExtra("qiyeId") ? intent.getStringExtra("qiyeId") : "";
        userId = intent.hasExtra("userId") ? intent.getStringExtra("userId") : "";
        signatureTime1 = intent.hasExtra("signatureTime1") ? intent.getStringExtra("signatureTime1") : "";
        signatureTime2 = intent.hasExtra("signatureTime2") ? intent.getStringExtra("signatureTime2") : "";
        createTime = intent.hasExtra("createTime") ? intent.getStringExtra("createTime") : "";
        signature2 = intent.hasExtra("signature2") ? intent.getStringExtra("signature2") : "";
        signature1 = intent.hasExtra("signature1") ? intent.getStringExtra("signature1") : "";
        planAdvise = intent.hasExtra("planAdvise") ? intent.getStringExtra("planAdvise") : "";
        resv1 = intent.hasExtra("resv1") ? intent.getStringExtra("resv1") : "";
        resv2 = intent.hasExtra("resv2") ? intent.getStringExtra("resv2") : "";
        userName = intent.hasExtra("userName") ? intent.getStringExtra("userName") : "";
        tv_wenjian1.setOnClickListener(this);
        tv_wenjian2.setOnClickListener(this);
        if ("yonghu".equals(biaoshi2)) {
            tv_yonghu.setOnClickListener(this);
            iv_yonghu.setOnClickListener(this);
            btn_wenjian1.setOnClickListener(this);
            btn_wenjian2.setOnClickListener(this);
            btn_commit.setOnClickListener(this);
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
            if (!"".equals(resv1)){
                curPath1 = "/sdcard/bizchat/workstate1/"+resv1;
                thread = new FileThread();
                thread.start();
            }
            if (!"".equals(resv2)){
                curPath2 = "/sdcard/bizchat/workstate2/"+resv2;
                thread2 = new LaowuThread();
                thread2.start();
            }
            initgongsi();
        }else if ("quanbu".equals(biaoshi2)){
            if (!"".equals(resv1)){
                curPath1 = "/sdcard/bizchat/workstate1/"+resv1;
                thread = new FileThread();
                thread.start();
            }
            if (!"".equals(resv2)){
                curPath2 = "/sdcard/bizchat/workstate2/"+resv2;
                thread2 = new LaowuThread();
                thread2.start();
            }
            initall();
        }
    }

    private void updateQjcount() {
        if (qiyeId==null||"".equals(qiyeId)){
            qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        }
        String url = FXConstant.URL_UPDATE_UNREADQIYE;
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                setResult(RESULT_OK);
                finish();
            }
        }, new com.android.volley.Response.ErrorListener() {
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
        MySingleton.getInstance(WorkstatementTwoActivity.this).addToRequestQueue(request);
    }

    private void reduceQjcount() {
        if (qiyeId==null||"".equals(qiyeId)){
            qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        }
        String url = FXConstant.URL_UPDATE_UNREADQIYE;
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                setResult(RESULT_OK);
                finish();
            }
        }, new com.android.volley.Response.ErrorListener() {
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
        MySingleton.getInstance(WorkstatementTwoActivity.this).addToRequestQueue(request);
    }

    class FileThread extends Thread {
        @Override
        public void run() {
            if (!"".equals(resv1)) {
                File file1 = new File(curPath1);
                if (!file1.exists()) {
                    uploadFile(FXConstant.URL_BAOBIAO_QIANMING + resv1, "wenjian1");
                } else {
                    fileHandler.sendEmptyMessage(0);
                }
            }
        }
    }
    class LaowuThread extends Thread {
        @Override
        public void run() {
            if (!"".equals(resv2)){
                File file2 = new File(curPath2);
                if (!file2.exists()) {
                    uploadFile(FXConstant.URL_BAOBIAO_QIANMING + resv2, "wenjian2");
                }else {
                    fileHandler.sendEmptyMessage(1);
                }
            }
        }
    }
    private void uploadFile(String url, final String doc){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        String SDPath = "";
        if ("wenjian1".equals(doc)){
            SDPath = curPath1;
        }else if ("wenjian2".equals(doc)){
            SDPath = curPath2;
        }
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            long total = response.body().contentLength();
            File file = new File(SDPath);
            fos = new FileOutputStream(file);
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                int progress = (int) (sum * 1.0f / total * 100);
            }
            fos.flush();
            if ("wenjian1".equals(doc)){
                fileHandler.sendEmptyMessage(0);
            }else if ("wenjian2".equals(doc)){
                fileHandler.sendEmptyMessage(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initgongsi() {
        et_jianyi.setFocusableInTouchMode(false);
        et_jianyi.setFocusable(false);
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
        initgongsi();
        et_jianyi2.setFocusableInTouchMode(false);
        et_jianyi2.setFocusable(false);
        String [] advise = planAdvise.split("\\|,");
        if (advise.length>0){
            et_jianyi.setText(advise[0]);
        }
        if (advise.length>1){
            et_jianyi2.setText(advise[1]);
        }
        tv_gongsi.setVisibility(View.INVISIBLE);
        ll_shijian_gongsi.setVisibility(View.VISIBLE);
        String time1 = signatureTime2.substring(0, 4) + "-" + signatureTime2.substring(4, 6) + "-" + signatureTime2.substring(6, 8) + " "
                + signatureTime2.substring(8, 10) + ":" + signatureTime2.substring(10, 12);
        tv_time_gongsi.setText(time1);
        ImageLoader.getInstance().displayImage(FXConstant.URL_BAOBIAO_QIANMING+signature2,iv_gongsi);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_yonghu:
                startActivityForResult(new Intent(WorkstatementTwoActivity.this, QianMingActivity.class).putExtra("biaoshi", "21"),0);
                break;
            case R.id.tv_gongsi:
                startActivityForResult(new Intent(WorkstatementTwoActivity.this, QianMingActivity.class).putExtra("biaoshi", "22"),1);
                break;
            case R.id.iv_yonghu:
                startActivityForResult(new Intent(WorkstatementTwoActivity.this, QianMingActivity.class).putExtra("biaoshi", "21"),0);
                break;
            case R.id.iv_gongsi:
                startActivityForResult(new Intent(WorkstatementTwoActivity.this, QianMingActivity.class).putExtra("biaoshi", "22"),1);
                break;
            case R.id.btn_laowu:
                Intent intent = new Intent(getApplicationContext(), FileSelectorActivity.class);
                fileConfig.startPath = Environment.getExternalStorageDirectory().getPath();
                fileConfig.rootPath = "/";
                intent.putExtra(FileConfig.FILE_CONFIG, fileConfig);
                startActivityForResult(intent,REC_WENJIAN1);
                break;
            case R.id.btn_jiameng:
                Intent intent1 = new Intent(getApplicationContext(), FileSelectorActivity.class);
                fileConfig.startPath = Environment.getExternalStorageDirectory().getPath();
                fileConfig.rootPath = "/";
                intent1.putExtra(FileConfig.FILE_CONFIG, fileConfig);
                startActivityForResult(intent1,REC_WENJIAN2);
                break;
            case R.id.tv_laowu:
                if (curPath1.endsWith(".doc")||curPath1.endsWith(".docx")){
                    Intent intent2 =  OpenFiles.getWordFileIntent(curPath1);
                    startActivity(intent2);
                }else if (curPath1.endsWith(".txt")){
                    Intent intent2 =  OpenFiles.getTextFileIntent(curPath1);
                    startActivity(intent2);
                }else if (curPath1.endsWith(".pdf")){
                    Intent intent2 =  OpenFiles.getPdfFileIntent(curPath1);
                    startActivity(intent2);
                }else if (curPath1.endsWith(".ppt")||curPath1.endsWith(".pptx")){
                    Intent intent2 =  OpenFiles.getPPTFileIntent(curPath1);
                    startActivity(intent2);
                }else if (curPath1.endsWith(".xlsx")||curPath1.endsWith(".xls")){
                    Intent intent2 =  OpenFiles.getExcelFileIntent(curPath1);
                    startActivity(intent2);
                }
                break;
            case R.id.tv_jiameng:
                if (curPath2.endsWith(".doc")||curPath2.endsWith(".docx")){
                    Intent intent3 =  OpenFiles.getWordFileIntent(curPath2);
                    startActivity(intent3);
                }else if (curPath2.endsWith(".txt")){
                    Intent intent3 =  OpenFiles.getTextFileIntent(curPath2);
                    startActivity(intent3);
                }else if (curPath2.endsWith(".pdf")){
                    Intent intent3 =  OpenFiles.getPdfFileIntent(curPath2);
                    startActivity(intent3);
                }else if (curPath2.endsWith(".ppt")||curPath2.endsWith(".pptx")){
                    Intent intent3 =  OpenFiles.getPPTFileIntent(curPath2);
                    startActivity(intent3);
                }else if (curPath2.endsWith(".xlsx")||curPath2.endsWith(".xls")){
                    Intent intent3 =  OpenFiles.getExcelFileIntent(curPath2);
                    startActivity(intent3);
                }
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
                    if ("".equals(curPath1)&&"".equals(curPath2)){
                        Toast.makeText(WorkstatementTwoActivity.this,"上传文件之后才能确认哦！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else if ("gongsi".equals(biaoshi2)){
                    path = path2;
                }
                if (!"".equals(path)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(WorkstatementTwoActivity.this);
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
                    Toast.makeText(WorkstatementTwoActivity.this,"签名之后才能确认哦！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void insertPlan(String biaoshi2) {
        String advise = et_jianyi.getText().toString().trim();
        String advise2 = et_jianyi2.getText().toString().trim();
        String url="";
        String str="";
        List<Param> param=new ArrayList<>();
        param.add(new Param("companyId", qiyeId));
        final List<File> files = new ArrayList<File>();
        Log.d("file1.curPath1----->>", curPath1);
        Log.d("file1.curPath2----->>", curPath2);
        if ("yonghu".equals(biaoshi2)) {
            final List<File> files1 = new ArrayList<File>();
            String str1 = "resvFail1";
            if (!"".equals(curPath1)) {
                File file1 = null;
                file1 = new File(curPath1);
                if (file1.exists()) {
                    files1.add(file1);
                }
            }
            final List<File> files2 = new ArrayList<File>();
            String str2 = "resvFail2";
            if (!"".equals(curPath2)) {
                File file = null;
                file = new File(curPath2);
                if (file.exists()) {
                    files2.add(file);
                }
            }
            param.add(new Param("planTitle", userName));
            param.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
            param.add(new Param("planAdvise", advise));
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
            OkHttpManager.getInstance().postTfile2(str,files,param, str1, files1, str2, files2, url, new OkHttpManager.HttpCallBack() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.e("jsonObject",jsonObject.toString());
                    Toast.makeText(WorkstatementTwoActivity.this,"操作成功！",Toast.LENGTH_SHORT).show();
                    sendPushMessage(managerId);
                    updateQjcount();
                    FileUtils.deleteAllFiles(files.get(0));
                }
                @Override
                public void onFailure(String errorMsg) {
                    Toast.makeText(WorkstatementTwoActivity.this,"网络连接错误，请重试！",Toast.LENGTH_SHORT).show();
                    Log.e("fanhuishuju,failure",errorMsg);
                }
            });
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
            OkHttpManager.getInstance().post(str, param, files, url, new OkHttpManager.HttpCallBack() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Toast.makeText(WorkstatementTwoActivity.this,"操作成功！",Toast.LENGTH_SHORT).show();
                    FileUtils.deleteAllFiles(files.get(0));
                    reduceQjcount();
                }
                @Override
                public void onFailure(String errorMsg) {
                    Log.e("fanhuishuju,failure",errorMsg);
                    Toast.makeText(WorkstatementTwoActivity.this,"网络连接错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sendPushMessage(final String hxid1) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        if (name==null||"".equals(name)){
            name = DemoHelper.getInstance().getCurrentUsernName();
        }
        final String finalName = name;
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new com.android.volley.Response.ErrorListener() {
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
        MySingleton.getInstance(WorkstatementTwoActivity.this).addToRequestQueue(request);
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
                case REC_WENJIAN1:
                    ArrayList<String> list1 = data.getStringArrayListExtra(FileSelector.RESULT);
                    String string = list1.toString();
                    string = string.substring(1,string.length()-1);
                    File file;
                    String a[]=new String[2];
                    //判断文件是否在sd卡中
                    if (string.indexOf(String.valueOf(Environment.getExternalStorageDirectory()))!=-1){
                        //对Uri进行切割
                        a = string.split(String.valueOf(Environment.getExternalStorageDirectory()));
                        //获取到file
                        file = new File(Environment.getExternalStorageDirectory(),a[1]);
                    }else if(string.indexOf(String.valueOf(Environment.getDataDirectory()))!=-1){ //判断文件是否在手机内存中
                        //对Uri进行切割
                        a =string.split(String.valueOf(Environment.getDataDirectory()));
                        //获取到file
                        file = new File(Environment.getDataDirectory(),a[1]);
                    }else{
                        //出现其他没有考虑到的情况
                        Toast.makeText(this,"文件路径解析失败！",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    curPath1 = file.getAbsolutePath().toString();
                    try{
                        if (curPath1.endsWith(".doc")||curPath1.endsWith(".docx")){
                            tv_wenjian1.setText("报表文件1.doc");
                        }else if (curPath1.endsWith(".txt")){
                            tv_wenjian1.setText("报表文件1.txt");
                        }else if (curPath1.endsWith(".pdf")){
                            tv_wenjian1.setText("报表文件1.pdf");
                        }else if (curPath1.endsWith(".ppt")||curPath1.endsWith(".pptx")){
                            tv_wenjian1.setText("报表文件1.ppt");
                        }else if (curPath1.endsWith(".xlsx")||curPath1.endsWith(".xls")){
                            tv_wenjian1.setText("报表文件1.xls");
                        }
                    }catch (Exception e){
                        //没有安装第三方的软件会提示
                        Toast.makeText(WorkstatementTwoActivity.this, "没有找到打开该文件的应用程序", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case REC_WENJIAN2:
                    ArrayList<String> list2 = data.getStringArrayListExtra(FileSelector.RESULT);
                    String string2 = list2.toString();
                    string2 = string2.substring(1,string2.length()-1);
                    File file2;
                    String a2[] = new String[2];
                    //判断文件是否在sd卡中
                    if (string2.indexOf(String.valueOf(Environment.getExternalStorageDirectory())) != -1) {
                        //对Uri进行切割
                        a2 = string2.split(String.valueOf(Environment.getExternalStorageDirectory()));
                        //获取到file
                        file2 = new File(Environment.getExternalStorageDirectory(), a2[1]);
                    } else if (string2.indexOf(String.valueOf(Environment.getDataDirectory())) != -1) { //判断文件是否在手机内存中
                        //对Uri进行切割
                        a2 = string2.split(String.valueOf(Environment.getDataDirectory()));
                        //获取到file
                        file2 = new File(Environment.getDataDirectory(), a2[1]);
                    } else {
                        //出现其他没有考虑到的情况
                        Toast.makeText(WorkstatementTwoActivity.this, "文件路径解析失败！", Toast.LENGTH_SHORT);
                        return;
                    }
                    curPath2 = file2.getAbsolutePath().toString();
                    try{
                        if (curPath2.endsWith(".doc")||curPath2.endsWith(".docx")){
                            tv_wenjian2.setText("报表文件2.doc");
                        }else if (curPath2.endsWith(".txt")){
                            tv_wenjian2.setText("报表文件2.txt");
                        }else if (curPath2.endsWith(".pdf")){
                            tv_wenjian2.setText("报表文件2.pdf");
                        }else if (curPath2.endsWith(".ppt")||curPath2.endsWith(".pptx")){
                            tv_wenjian2.setText("报表文件2.ppt");
                        }else if (curPath2.endsWith(".xlsx")||curPath2.endsWith(".xls")){
                            tv_wenjian2.setText("报表文件2.xls");
                        }
                    }catch (Exception e){
                        //没有安装第三方的软件会提示
                        Toast.makeText(WorkstatementTwoActivity.this, "没有找到打开该文件的应用程序", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (thread!=null&&!thread.isInterrupted()){
            thread.interrupt();
            thread=null;
        }
        if (thread2!=null&&!thread2.isInterrupted()){
            thread2.interrupt();
            thread2=null;
        }
    }
}
