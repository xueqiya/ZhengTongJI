package com.sangu.apptongji.main.alluser.order.avtivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.utils.FileStorage;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.FileUtils;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by user on 2016/9/2.
 */

public class QianMingActivity extends BaseActivity{
    private PaintView mView=null;
    private String orderId,merId,balance,biaoshi,beizhu,companyId,companyAmt="",balance1="";
    public String imageName;
    private Bitmap imageBitmap = null;

    private String dynamicSeq="0",createTime,task_label,orderRemark,conId="",orderSum,beginTime,endTime,conName,merName,thridInfo;

    private Dialog mWeiboDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qianming);
        PermissionUtil permissionUtil = new PermissionUtil(QianMingActivity.this);
        permissionUtil.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                new PermissionListener() {
                    @Override
                    public void onGranted() {
                        //所有权限都已经授权
                    }
                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        //Toast第一个被拒绝的权限
                        Toast.makeText(getApplicationContext(),"您拒绝了访问内存卡权限！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        //Toast勾选不在提示的权限
                        Toast.makeText(getApplicationContext(),"您拒绝了访问内存卡权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        imageName = getNowTime() + ".png";
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.tablet_view);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        mView = new PaintView(this);
        frameLayout.addView(mView);
        mView.requestFocus();
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        orderId = this.getIntent().getStringExtra("orderId");
        balance1 = this.getIntent().hasExtra("balance1")?this.getIntent().getStringExtra("balance1"):"";
        companyId = this.getIntent().hasExtra("companyId")?this.getIntent().getStringExtra("companyId"):"";
        companyAmt = this.getIntent().hasExtra("companyAmt")?this.getIntent().getStringExtra("companyAmt"):"";

       // GetOrderDetailInfo();

        if (biaoshi.equals("11")) {
            merId = this.getIntent().getStringExtra("merId");
            balance = this.getIntent().getStringExtra("balance");
        }
        Button btnClear = (Button) findViewById(R.id.tablet_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mView.clear();
            }
        });
        Button btnOk = (Button) findViewById(R.id.tablet_ok);
        if (biaoshi.equals("1101")){
            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String time3 = getNowTime1();
                    time3 = time3.substring(0, 4) + "-" + time3.substring(4, 6) + "-" + time3.substring(6, 8) + " "
                            + time3.substring(8, 10) + ":" + time3.substring(10, 12);
                    imageBitmap = mView.getCachebBitmap();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QianMingActivity.this);
                    builder.setMessage("确认签收吗?");
                    builder.setTitle("确认");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final String finalTime = time3;
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            if (!imageBitmap.equals(null)) {
                                saveImg(imageBitmap);
                            }
                            final List<File> files = new ArrayList<File>();
                            File file = null;
                            file = new FileStorage("qianming").createCropFile(imageName,null);
                            if (file.exists()) {
                                files.add(file);
                            }
                            List<Param> params = new ArrayList<Param>();
                            if (biaoshi.equals("1101")) {
                                if (!(imageBitmap.equals(null) || imageBitmap.equals(""))) {
                                    params.add(new Param("key", "image2"));
                                    params.add(new Param("value", imageName));
                                } else {
                                    params.add(new Param("image4", "已确认"));
                                }
                                params.add(new Param("time3", finalTime));
                                params.add(new Param("orderId", orderId));
                            }
                            String str = "image2";
                            String url = FXConstant.URL_Order_Detail_update;
                            OkHttpManager.getInstance().post(str,params, files, url, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                    String code = jsonObject.getString("code");
                                    if (code.equals("SUCCESS")) {
                                        if (orderId!=null){
                                            updateUscount();
                                        }
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "签收成功！", Toast.LENGTH_SHORT).show();
                                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"));
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "签收失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(QianMingActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });
        }else if (biaoshi.equals("11")) {
            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String time3 = getNowTime1();
                    time3 = time3.substring(0, 4) + "-" + time3.substring(4, 6) + "-" + time3.substring(6, 8) + " "
                            + time3.substring(8, 10) + ":" + time3.substring(10, 12);
                    imageBitmap = mView.getCachebBitmap();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QianMingActivity.this);
                    builder.setMessage("确认签收吗?");
                    builder.setTitle("确认");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final String finalTime = time3;
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            if (!imageBitmap.equals(null)) {
                                saveImg(imageBitmap);
                            }
                            dialog.dismiss();
                            updatePingjia();
                            final List<File> files = new ArrayList<File>();
                            File file = null;
                            file = new FileStorage("qianming").createCropFile(imageName,null);
                            if (file.exists()) {
                                files.add(file);
                            }
                            ArrayList<String> imagePaths1 = getIntent().getStringArrayListExtra("imageUrl1");
                            ArrayList<String> imagePaths2 = getIntent().getStringArrayListExtra("imageUrl2");
                            if (imagePaths1!=null&&imagePaths1.size()>0){
                                List<Param> params = new ArrayList<Param>();
                                if (!(imageBitmap == null || imageBitmap.equals(""))) {
                                    params.add(new Param("key", "o_image"));
                                    params.add(new Param("value", imageName));
                                } else {
                                    params.add(new Param("o_signature", "已确认"));
                                }
                                if (companyId != null && companyId.length() > 0) {
                                    params.add(new Param("balance", balance1));
                                    params.add(new Param("companyAmt", companyAmt));
                                    params.add(new Param("companyId", companyId));
                                } else {
                                    params.add(new Param("balance", balance));
                                }
                                params.add(new Param("resv4", finalTime));
                                params.add(new Param("orderId", orderId));
                                params.add(new Param("merId", merId));
                                params.add(new Param("orderState", "05"));
                                String str = "o_image";
                                String url = FXConstant.URL_Update_OrderState;
                                final File finalFile = file;
                                String path1 = null,path2 = null,path3 = null,path4 = null,path5 = null,path6 = null,path7 = null,path8 = null;
                                if (imagePaths1.size()>0){
                                    path1 = imagePaths1.get(0);
                                    Log.e("qianming,p11",path1);
                                }
                                if (imagePaths1.size()>1){
                                    path2 = imagePaths1.get(1);
                                    Log.e("qianming,p12",path2);
                                }
                                if (imagePaths1.size()>2){
                                    path3 = imagePaths1.get(2);
                                    Log.e("qianming,p13",path3);
                                }
                                if (imagePaths1.size()>3){
                                    path4 = imagePaths1.get(3);
                                    Log.e("qianming,p14",path4);
                                }
                                if (imagePaths2.size() > 0) {
                                    path5 = imagePaths2.get(0);
                                    Log.e("qianming,p15",path5);
                                }
                                if (imagePaths2.size() > 1) {
                                    path6 = imagePaths2.get(1);
                                    Log.e("qianming,p16",path6);
                                }
                                if (imagePaths2.size() > 2) {
                                    path7 = imagePaths2.get(2);
                                    Log.e("qianming,p17",path7);
                                }
                                if (imagePaths2.size() > 3) {
                                    path8 = imagePaths2.get(3);
                                    Log.e("qianming,p18",path8);
                                }
                                List<Param> params1 = new ArrayList<Param>();
                                params1.add(new Param("orderId", orderId));
                                String url1 = FXConstant.URL_Order_Detail_update;
                                OkHttpManager.getInstance().posts2(params1, null,new ArrayList<String>(),null, new ArrayList<String>(), "image01", path1, "image02", path2, "image03", path3, "image04", path4
                                        , "image05", path5, "image06", path6, "image07", path7, "image08", path8, url1, new OkHttpManager.HttpCallBack() {
                                            @Override
                                            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                                Log.e("qianming,s11",jsonObject.toString());
                                                FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/offline/"));
                                            }

                                            @Override
                                            public void onFailure(String errorMsg) {
                                                Toast.makeText(QianMingActivity.this,"网络连接错误,请稍后再试",Toast.LENGTH_SHORT).show();
                                                if (errorMsg!=null) {
                                                    Log.e("qianming,sl1", errorMsg);
                                                }
                                            }
                                        });
                                OkHttpManager.getInstance().post(str, params, files, url, new OkHttpManager.HttpCallBack() {
                                    @Override
                                    public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                        Log.e("qianming,s111",jsonObject.toString());
                                        String dynamicSeq = getIntent().getStringExtra("dynamicSeq");
                                        String create_time = getIntent().getStringExtra("create_time");
                                        if (dynamicSeq!=null&&!"".equals(dynamicSeq)){
                                            ChangeState(dynamicSeq,create_time);
                                        }
                                        if (companyId != null && companyId.length() > 0) {
                                            tongji("1", companyId, merId);
                                        } else {
                                            reduceUscount(0);
                                            Toast.makeText(QianMingActivity.this, "签收成功！", Toast.LENGTH_SHORT).show();
                                        }
                                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/qianming/"));
                                        finish();
//                                    if (code.equals("success")||code.equals("SUCCESS")) {
//                                        dialog.dismiss();
//                                        Toast.makeText(QianMingActivity.this, "签收成功！", Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    } else {
//                                        dialog.dismiss();
//                                        Toast.makeText(QianMingActivity.this, "签收失败！", Toast.LENGTH_SHORT).show();
//                                    }
                                    }

                                    @Override
                                    public void onFailure(String errorMsg) {
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                List<Param> params = new ArrayList<Param>();
                                if (!(imageBitmap == null || imageBitmap.equals(""))) {
                                    params.add(new Param("key", "o_image"));
                                    params.add(new Param("value", imageName));
                                } else {
                                    params.add(new Param("o_signature", "已确认"));
                                }
                                if (companyId != null && companyId.length() > 0) {
                                    params.add(new Param("balance", balance1));
                                    params.add(new Param("companyAmt", companyAmt));
                                    params.add(new Param("companyId", companyId));
                                } else {
                                    params.add(new Param("balance", balance));
                                }
                                params.add(new Param("resv4", finalTime));
                                params.add(new Param("orderId", orderId));
                                params.add(new Param("merId", merId));
                                params.add(new Param("orderState", "05"));
                                String str = "o_image";
                                String url = FXConstant.URL_Update_OrderState;
                                final File finalFile = file;
                                OkHttpManager.getInstance().post(str, params, files, url, new OkHttpManager.HttpCallBack() {
                                    @Override
                                    public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                        Log.e("qianming,111",jsonObject.toString());
                                        String dynamicSeq = getIntent().getStringExtra("dynamicSeq");
                                        String create_time = getIntent().getStringExtra("create_time");
                                        if (dynamicSeq!=null&&!"".equals(dynamicSeq)){
                                            ChangeState(dynamicSeq,create_time);
                                        }
                                        if (companyId != null && companyId.length() > 0) {
                                            tongji("1", companyId, merId);
                                        } else {
                                            reduceUscount(0);

                                            if (!dynamicSeq.equals("0")){


                                                duanxintongzhi(merId,"【正事多】您接的派单客户已签收，交易资金可在余额里查看，10天后全款自动进入账号（平台不抽成）自觉保证承诺服务，以免影响资金到账","");

                                            }

                                            Toast.makeText(QianMingActivity.this, "签收成功！", Toast.LENGTH_SHORT).show();
                                        }
                                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/qianming/"));
                                        finish();
//                                    if (code.equals("success")||code.equals("SUCCESS")) {
//                                        dialog.dismiss();
//                                        Toast.makeText(QianMingActivity.this, "签收成功！", Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    } else {
//                                        dialog.dismiss();
//                                        Toast.makeText(QianMingActivity.this, "签收失败！", Toast.LENGTH_SHORT).show();
//                                    }
                                    }

                                    @Override
                                    public void onFailure(String errorMsg) {
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                    builder.show();
                }
            });
        }else if (biaoshi.equals("1201")){
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String userId = QianMingActivity.this.getIntent().getStringExtra("userId");
                    String time2 = getNowTime1();
                    time2 = time2.substring(0, 4) + "-" + time2.substring(4, 6) + "-" + time2.substring(6, 8) + " "
                            + time2.substring(8, 10) + ":" + time2.substring(10, 12);
                    imageBitmap = mView.getCachebBitmap();
                    //imageSign.setImageBitmap(imageBitmap);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QianMingActivity.this);
                    builder.setMessage("确认提交吗?");
                    builder.setTitle("确认");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final String finalTime = time2;
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            if (!imageBitmap.equals(null)) {
                                saveImg(imageBitmap);
                            }
                            final List<File> files = new ArrayList<File>();
                            File file = null;
                            file = new FileStorage("qianming").createCropFile(imageName,null);
                            if (file.exists()) {
                                files.add(file);
                            }
                            List<Param> params = new ArrayList<Param>();
                            if (biaoshi.equals("1201")) {
                                if (!(imageBitmap.equals(null) || imageBitmap.equals(""))) {
                                    params.add(new Param("key", "image1"));
                                    params.add(new Param("value", imageName));
                                } else {
                                    params.add(new Param("image3", "已确认"));
                                }
                                params.add(new Param("time2", finalTime));
                                params.add(new Param("orderId", orderId));
                            }
                            String url = FXConstant.URL_Order_Detail_update;
                            final File finalFile = file;
                            OkHttpManager.getInstance().post("image1",params, files, url, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                    String code = jsonObject.getString("code");
                                    Log.e("返回的值",code);
                                    if (code.equals("SUCCESS")) {
                                        if (userId!=null) {
                                            reduceUscount(1);
                                            addUscount(userId);
                                            sendPushMessage(userId);
                                        }
                                        if (userId==null&&orderId!=null){
                                            updateUscount();
                                        }
                                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"));
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK);
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(QianMingActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });
        }else if (biaoshi.equals("12")){
            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final String userId = QianMingActivity.this.getIntent().getStringExtra("userId");
                    String time1 = getNowTime1();
                    time1 = time1.substring(0, 4) + "-" + time1.substring(4, 6) + "-" + time1.substring(6, 8) + " "
                            + time1.substring(8, 10) + ":" + time1.substring(10, 12);
                    imageBitmap = mView.getCachebBitmap();
                    //imageSign.setImageBitmap(imageBitmap);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QianMingActivity.this);
                    builder.setMessage("确认提交吗?");
                    builder.setTitle("确认");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final String finalTime = time1;
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            if (!imageBitmap.equals(null)) {
                                saveImg(imageBitmap);
                            }
                            final List<File> files = new ArrayList<File>();
                            File file = null;
                            file = new FileStorage("qianming").createCropFile(imageName,null);
                            if (file.exists()) {
                                files.add(file);
                            }
                            List<Param> params = new ArrayList<Param>();
                            if (!(imageBitmap.equals(null) || imageBitmap.equals(""))) {
                                params.add(new Param("key", "m_image"));
                                params.add(new Param("value", imageName));
                            } else {
                                params.add(new Param("m_signature", "已确认"));
                            }
                            params.add(new Param("time1", finalTime));
                            params.add(new Param("orderId", orderId));
                            params.add(new Param("orderState", "04"));
                            String url = FXConstant.URL_Update_OrderState;
                            OkHttpManager.getInstance().post("m_image", params, files, url, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                    String code = jsonObject.getString("code");
                                    if (code.equals("success")) {
                                        dialog.dismiss();
                                        if (userId != null) {
                                            reduceUscount(1);
                                            addUscount(userId);
                                            sendPushMessage(userId);
                                        }
                                        Toast.makeText(QianMingActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/qianming/"));
                                        setResult(RESULT_OK,new Intent().putExtra("orderState","04"));
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(QianMingActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });
        }else if (biaoshi.equals("15")){
            btnOk.setText("拒绝退款");
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String time1 = getNowTime1();
                    time1 = time1.substring(0, 4) + "-" + time1.substring(4, 6) + "-" + time1.substring(6, 8) + " "
                            + time1.substring(8, 10) + ":" + time1.substring(10, 12);
                    imageBitmap = mView.getCachebBitmap();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QianMingActivity.this);
                    builder.setMessage("确认拒绝退款吗?");
                    builder.setTitle("确认");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final String finalTime = time1;
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            if (!imageBitmap.equals(null)) {
                                saveImg(imageBitmap);
                            }
                            final List<File> files = new ArrayList<File>();
                            File file = null;
                            file = new FileStorage("qianming").createCropFile(imageName,null);
                            if (file.exists()) {
                                files.add(file);
                            }
                            List<Param> params = new ArrayList<Param>();
                            if (!(imageBitmap.equals(null) || imageBitmap.equals(""))) {
                                params.add(new Param("key", "m_image"));
                                params.add(new Param("value", imageName));
                            } else {
                                params.add(new Param("m_signature", "已确认"));
                            }
                            params.add(new Param("time1", finalTime));
                            params.add(new Param("orderId", orderId));
                            params.add(new Param("orderState", "08"));
                            String url = FXConstant.URL_Update_OrderState;
                            OkHttpManager.getInstance().post("m_image",params, files, url, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                    String code = jsonObject.getString("code");
                                    if (code.equals("success")) {
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "退款成功！", Toast.LENGTH_SHORT).show();
                                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"));
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "退款失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(QianMingActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });
        }else if (biaoshi.equals("16")){
            final String balance = QianMingActivity.this.getIntent().getStringExtra("balance");
            final String userId = QianMingActivity.this.getIntent().getStringExtra("userId");
            Log.e("qianming,id",userId);
            btnOk.setText("确认退款");
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String time1 = getNowTime1();
                    time1 = time1.substring(0, 4) + "-" + time1.substring(4, 6) + "-" + time1.substring(6, 8) + " "
                            + time1.substring(8, 10) + ":" + time1.substring(10, 12);
                    imageBitmap = mView.getCachebBitmap();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QianMingActivity.this);
                    builder.setMessage("确认同意退款吗?");
                    builder.setTitle("确认");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final String finalTime = time1;
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            if (!imageBitmap.equals(null)) {
                                saveImg(imageBitmap);
                            }
                            final List<File> files = new ArrayList<File>();
                            File file = null;
                            file = new FileStorage("qianming").createCropFile(imageName,null);
                            if (file.exists()) {
                                files.add(file);
                            }
                            List<Param> params = new ArrayList<Param>();
                            if (!(imageBitmap.equals(null) || imageBitmap.equals(""))) {
                                params.add(new Param("key", "m_image"));
                                params.add(new Param("value", imageName));
                            } else {
                                params.add(new Param("m_signature", "已确认"));
                            }
                            params.add(new Param("time1", finalTime));
                            params.add(new Param("orderId", orderId));
                            params.add(new Param("userId", userId));
                            params.add(new Param("balance", balance));
                            params.add(new Param("orderState", "07"));
                            String url = FXConstant.URL_Update_OrderState;
                            OkHttpManager.getInstance().post("m_image",params, files, url, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                    String code = jsonObject.getString("code");
                                    if (code.equals("success")) {
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "退款成功！", Toast.LENGTH_SHORT).show();
                                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"));
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "退款失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(QianMingActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });
        }else if (biaoshi.equals("21")){
            btnOk.setText("确认");
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageBitmap = mView.getCachebBitmap();
                    if (!imageBitmap.equals(null)) {
                        saveImg(imageBitmap);
                    }
                    setResult(RESULT_OK,new Intent().putExtra("xdimageName",Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"+imageName));
                    finish();
                }
            });
        }else if (biaoshi.equals("22")){
            btnOk.setText("确认");
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageBitmap = mView.getCachebBitmap();
                    if (!imageBitmap.equals(null)) {
                        saveImg(imageBitmap);
                    }
                    setResult(RESULT_OK,new Intent().putExtra("jdimageName",Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"+imageName));
                    finish();
                }
            });
        }else if (biaoshi.equals("0")){

            //标示0  代表新的派单订单详情提交报价的时候的签字
            //走原来报价的流程，只不过新增详情的时候 增加一下签名

            dynamicSeq = getIntent().getStringExtra("dynamicSeq");
            createTime = getIntent().getStringExtra("createTime");
            task_label = getIntent().getStringExtra("task_label");
            beginTime = getIntent().getStringExtra("beginTime");
            endTime = getIntent().getStringExtra("endTime");
            orderSum = getIntent().getStringExtra("orderSum");
            orderRemark = getIntent().getStringExtra("remark");
            conId = getIntent().getStringExtra("conId");
            merId = getIntent().getStringExtra("merId");
            conName = getIntent().getStringExtra("conName");
            merName = getIntent().getStringExtra("merName");
            thridInfo = getIntent().getStringExtra("thridInfo");

            btnOk.setText("确认");
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imageBitmap = mView.getCachebBitmap();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QianMingActivity.this);
                    builder.setMessage("是否确认提交报价并在线签约?");
                    builder.setTitle("提示：");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(QianMingActivity.this, "加载中...");

                            if (!imageBitmap.equals(null)) {
                                saveImg(imageBitmap);
                            }else {
                                Toast.makeText(QianMingActivity.this,"请您先签字",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            final List<File> files = new ArrayList<File>();
                            File file = null;
                            file = new FileStorage("qianming").createCropFile(imageName,null);
                            if (file.exists()) {
                                files.add(file);
                            }
                            List<Param> params = new ArrayList<Param>();
                            if (!(imageBitmap.equals(null) || imageBitmap.equals(""))) {
                                params.add(new Param("key", "image07"));
                                params.add(new Param("value", imageName));
                            }

                            params.add(new Param("type", "01"));
                            params.add(new Param("flg", "06"));
                            params.add(new Param("orderBody", task_label));
                            params.add(new Param("task_label", task_label));
                            params.add(new Param("createTime", createTime));
                            params.add(new Param("dynamicSeq", dynamicSeq));
                            params.add(new Param("orderSum", orderSum));
                            params.add(new Param("userId", merId));
                            params.add(new Param("orderProject", ",,,,,,,,,,,"));
                            params.add(new Param("orderNumber", ",,,,,,,,,,,"));
                            params.add(new Param("orderAmt", ",,,,,,,,,,,"));

                            if (orderRemark != null){
                                params.add(new Param("orderRemark", orderRemark));
                            }

                            if (beginTime != null){
                                params.add(new Param("beginTime", beginTime));
                            }

                            if (endTime != null){
                                params.add(new Param("endTime", endTime));
                            }

                            //第三方客户信息
                            if (thridInfo != null && !thridInfo.equals("")){

                                String[] strings = thridInfo.split("\\|");

                                if (strings.length == 3){

                                    params.add(new Param("thridName", strings[0]));
                                    params.add(new Param("thridTel", strings[1]));
                                    params.add(new Param("thridAdress", strings[2]));

                                }

                            }


                            String url = FXConstant.URL_INSERT_DYNAMIC_ORDER;
                            OkHttpManager.getInstance().post("image07",params, files, url, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                    String code = jsonObject.getString("code");
                                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                                    if (code.equals("success")) {

                                        //成功之后
                                        //推送 加 修改动态报价次数 加 新增联系记录 加发送短信通知
                                        sendPushMessage(conId);
                                        addPinglinCount(createTime,dynamicSeq);
                                        insertDynamicContact(dynamicSeq,createTime,DemoHelper.getInstance().getCurrentUsernName(),conId,"00");

                                        //接单报价给用户发短信
                                        //收到%@(%@)对您发布的%@需求报价%@元,(沟通确认后)在线同意报价方案即可开始服务

                                        duanxintongzhi(conId,"【正事多】收到"+merName+"("+merId+")"+"对您发布的"+task_label+"需求报价"+orderSum+"元,(沟通确认后)在线同意报价方案即可开始服务","111");

                                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"));
                                        Toast.makeText(QianMingActivity.this, "报价成功，等待对方同意之后即可开始服务!", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK,new Intent().putExtra("orderState","02"));
                                        finish();

                                    } else {

                                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                                        Toast.makeText(QianMingActivity.this, "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(String errorMsg) {

                                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                                    Toast.makeText(QianMingActivity.this, "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });

        }else if (biaoshi.equals("1")) {

            //1 的时候代表客户进来  同意报价的签字

            dynamicSeq = getIntent().getStringExtra("dynamicSeq");
            createTime = getIntent().getStringExtra("createTime");
            beginTime = getIntent().getStringExtra("beginTime");
            endTime = getIntent().getStringExtra("endTime");
            conId = getIntent().getStringExtra("conId");
            merId = getIntent().getStringExtra("merId");
            conName = getIntent().getStringExtra("conName");
            merName = getIntent().getStringExtra("merName");
            thridInfo = getIntent().getStringExtra("thridInfo");
            orderId = getIntent().getStringExtra("orderId");
            task_label = getIntent().getStringExtra("task_label");

            btnOk.setText("确认");
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    imageBitmap = mView.getCachebBitmap();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QianMingActivity.this);
                    builder.setMessage("是否确认同意报价并在线签约？");
                    builder.setTitle("提示：");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {

                            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(QianMingActivity.this, "加载中...");

                            if (!imageBitmap.equals(null)) {
                                saveImg(imageBitmap);
                            }else {
                                Toast.makeText(QianMingActivity.this,"请您先签字",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            final List<File> files = new ArrayList<File>();
                            File file = null;
                            file = new FileStorage("qianming").createCropFile(imageName,null);
                            if (file.exists()) {
                                files.add(file);
                            }
                            List<Param> params = new ArrayList<Param>();
                            if (!(imageBitmap.equals(null) || imageBitmap.equals(""))) {
                                params.add(new Param("key", "image08"));
                                params.add(new Param("value", imageName));
                            }

                            params.add(new Param("state", "03"));
                            params.add(new Param("flg", "06"));
                            params.add(new Param("orderId", orderId));
                            if (beginTime != null){
                                params.add(new Param("beginTime", beginTime));
                            }

                            if (endTime != null){
                                params.add(new Param("endTime", endTime));
                            }

                            //第三方客户信息
                            String authCoude = "";
                            if (thridInfo != null && !thridInfo.equals("")){

                                String[] strings = thridInfo.split("\\|");

                                if (strings.length == 3){

                                    //第三方的时候，这一步还需要多传一个验证码

                                    String str="0123456789";
                                    StringBuilder sb=new StringBuilder(4);
                                    for(int i=0;i<4;i++)
                                    {
                                        char ch=str.charAt(new Random().nextInt(str.length()));
                                        sb.append(ch);
                                    }

                                    authCoude = sb+"";

                                    params.add(new Param("authCode", authCoude));

                                }

                            }

                            final String codeStr = authCoude;

                            String url = FXConstant.URL_Order_Detail_update;
                            OkHttpManager.getInstance().post("image08",params, files, url, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                    String code = jsonObject.getString("code");

                                    if (code.equals("SUCCESS")) {

                                        //同意报价之后的操作

                                        updateUscount(conId);
                                        ChangeState();
                                        sendPushMessage(merId);

                                        if (thridInfo != null && !thridInfo.equals("")){

                                            //有第三方的情况下

                                            //发给师傅
                                            duanxintongzhi(merId,"【正事多】客户"+conName+"同意你对"+task_label+"需求的报价方案并签约，请按照约定完成服务后，向客户索要签收码提交订单给客户签收，客户未签收前不能再次接单","0");

                                            //发给客户
                                            String[] strings = thridInfo.split("\\|");
                                            duanxintongzhi(strings[1],"【正事多】"+conName+"("+conId+")派"+merName+"("+merId+")为您提供服务,签收码："+codeStr+"（请在服务满意同意签收时向对方提供签收码）","0");

                                            //发给商家
                                            duanxintongzhi(conId,"【正事多】您派遣师傅"+merName+"("+merId+")提供【"+task_label+"】服务的签收码："+codeStr+"已发送到客户"+conName+"("+conId+")","0");


                                        }else {

                                            duanxintongzhi(merId,"【正事多】客户"+conName+"同意你对"+task_label+"需求的报价方案并签约，请按照约定完成服务后，向客户索要签收码提交订单给客户签收，客户未签收前不能再次接单","0");

                                        }


                                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                                        Toast.makeText(QianMingActivity.this, "您已同意接单人的报价", Toast.LENGTH_SHORT).show();
                                        setResult(RESULT_OK,new Intent().putExtra("orderState","03"));
                                        finish();

                                    } else {

                                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                                        Toast.makeText(QianMingActivity.this, "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(String errorMsg) {

                                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                                    Toast.makeText(QianMingActivity.this, "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });


        }else if (biaoshi.equals("2")){

            //标示2就简单签个字会返回去 付款逻辑什么的还是在订单详情页面处理

            btnOk.setText("确认");

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    imageBitmap = mView.getCachebBitmap();
                    if (!imageBitmap.equals(null)) {
                        saveImg(imageBitmap);
                    }
                    setResult(RESULT_OK,new Intent().putExtra("xdimageName",Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"+imageName));
                    finish();

                }
            });

        }else if (biaoshi.equals("3")){

            //提交订单



        }else {
            beizhu = this.getIntent().getStringExtra("remark");
            btnOk.setText("申请退款");
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String time3 = getNowTime1();
                    time3 = time3.substring(0, 4) + "-" + time3.substring(4, 6) + "-" + time3.substring(6, 8) + " "
                            + time3.substring(8, 10) + ":" + time3.substring(10, 12);
                    imageBitmap = mView.getCachebBitmap();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(QianMingActivity.this);
                    builder.setMessage("确认退款吗?");
                    builder.setTitle("确认");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final String finalTime = time3;
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            if (!imageBitmap.equals(null)) {
                                saveImg(imageBitmap);
                            }
                            final List<File> files = new ArrayList<File>();
                            File file = null;
                            file = new FileStorage("qianming").createCropFile(imageName,null);
                            if (file.exists()) {
                                files.add(file);
                            }
                            List<Param> params = new ArrayList<Param>();
                            if (!(imageBitmap==null || imageBitmap.equals(""))) {
                                params.add(new Param("key", "o_image"));
                                params.add(new Param("value", imageName));
                            } else {
                                params.add(new Param("o_signature", "已确认"));
                            }
                            params.add(new Param("resv4", finalTime));
                            params.add(new Param("orderId", orderId));
                            params.add(new Param("remark", beizhu));
                            params.add(new Param("orderState", "06"));
                            String str = "o_image";
                            String url = FXConstant.URL_Update_OrderState;
                            OkHttpManager.getInstance().post(str,params, files, url, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                                    String code = jsonObject.getString("code");
                                    if (code.equals("success")) {
                                        dialog.dismiss();
                                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"));
                                        Toast.makeText(QianMingActivity.this, "申请退款成功，请等待商家回复！", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(QianMingActivity.this, "申请退款失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(String errorMsg) {
                                    dialog.dismiss();
                                    Toast.makeText(QianMingActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });
        }
    }



    //改变动态里边的报价的状态
    private void ChangeState() {
        String url = FXConstant.URL_DYNAMIC_CHANGE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("uorderdac,chasta",s);
                chandyOrderState();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("dynamic_seq",dynamicSeq);
                param.put("timestamp",createTime);
                param.put("u_id",merId);
                return param;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);
    }

    private void chandyOrderState() {
        String url =FXConstant.URL_TONGJI_LIULANCISHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("dynamicActivity","成功,s="+s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dynamicActivity","失败,e="+volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("createTime",createTime);
                param.put("dynamicSeq",dynamicSeq);
                param.put("orderState","01");
                return param;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);
    }

    private void updateUscount(final String hxid1) {
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
                param.put("orderUnReadCount","1");
                param.put("userId",hxid1);
                return param;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);
    }

    private void duanxintongzhi(final String id, final String message,final String type) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                param.put("telNum",id);
                return param;
            }
        };

        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);

    }

    private void GetOrderDetailInfo(){

        String url = FXConstant.URL_Order_Detail;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object1 = new JSONObject(s);
                    JSONObject object = object1.getJSONObject("odi");

                    dynamicSeq = object.getString("dynamicSeq");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
               // Toast.makeText(getApplicationContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("orderId",orderId);
                return params;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);

    }


    private void insertDynamicContact(final String dynamicSeq, final String createTime, final String uId, final String contactId, final String type) {
        String url = FXConstant.URL_UPDATE_INSERT_DYNAMIC_CONTACT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "insertDynamicContact onResponse" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "insertDynamicContact onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //uId=&dynamicSeq=&createTime=&dis=&responsetime=&ordernum=&status=
                Map<String, String> param = new HashMap<>();
                param.put("dynamicSeq", dynamicSeq);
                param.put("createTime", createTime);
                param.put("uId", uId);
                param.put("contactId", contactId);
                param.put("type", type);
                return param;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);

    }

    private void addPinglinCount(final String createTime, final String dynamicSeq){
        String url = FXConstant.URL_ADD_PINGLUN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("uorderdeac","评论增加成功");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("createTime",createTime);
                param.put("dynamicSeq",dynamicSeq);
                return param;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);
    }

    private void sendPushMessage(final String hxid1) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
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

                if (biaoshi.equals("0")){

                    param.put("u_id",conId);
                    param.put("body","报价消息");
                    param.put("type","10");
                    param.put("userId",conId);
                    param.put("companyId", "0");
                    param.put("companyName","0");
                    param.put("companyAdress","0");
                    param.put("dynamicSeq",dynamicSeq);
                    param.put("createTime",createTime);
                    param.put("fristId","0");
                    param.put("dType","05");

                }else if (biaoshi.equals("1")){

                    //同意报价之后的推送
                    param.put("u_id",hxid1);
                    param.put("body","订单消息");
                    param.put("type","000");
                    param.put("userId",hxid1);
                    param.put("companyId", "0");
                    param.put("companyName","0");
                    param.put("companyAdress","0");
                    param.put("dynamicSeq","0");
                    param.put("createTime","0");
                    param.put("fristId","0");
                    param.put("dType","0");

                }else{

                    param.put("u_id",hxid1);
                    param.put("body","订单消息");
                    param.put("type","001");
                    param.put("userId",myId);
                    param.put("companyId", "0");
                    param.put("companyName","0");
                    param.put("companyAdress","0");
                    param.put("dynamicSeq","0");
                    param.put("createTime","0");
                    param.put("fristId","0");
                    param.put("dType","0");

                }

                return param;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);
    }

    private void updateUscount() {
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
                param.put("thirdPartyCount", "-1");
                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);
    }

    private void ChangeState(final String dynamicSeq, final String createTime) {
        String url =FXConstant.URL_TONGJI_LIULANCISHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("dynamicActivity","成功,s="+s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dynamicActivity","失败,e="+volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("createTime",createTime);
                param.put("dynamicSeq",dynamicSeq);
                param.put("orderState","02");
                return param;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);
    }

    private void addUscount(final String userId) {
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
                param.put("cusmUnReadCount","1");
                param.put("userId",userId);
                return param;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);
    }
    private void reduceUscount(final int type) {
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
                if (type==1){
                    param.put("orderUnReadCount","-1");
                }else {
                    param.put("cusmUnReadCount", "-1");
                }
                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);
    }

    private void updatePingjia() {
//        final WeakReference<QianMingActivity> reference = new WeakReference<QianMingActivity>(QianMingActivity.this);
//        BmobQuery<PermissCountList> query = new BmobQuery<>();
//        query.addWhereEqualTo("userId", DemoHelper.getInstance().getCurrentUsernName());
//        query.addWhereEqualTo("merId", merId);
//        query.findObjects(reference.get(),new FindListener<PermissCountList>() {
//            @Override
//            public void onSuccess(List<PermissCountList> list) {
//                if (list == null || list.size() == 0) {
//                    PermissCountList permissCountList = new PermissCountList();
//                    permissCountList.setMerId(merId);
//                    permissCountList.setUserId(DemoHelper.getInstance().getCurrentUsernName());
//                    permissCountList.setPermissCount("1");
//                    permissCountList.save(reference.get());
//                } else {
//                    PermissCountList permissCountList = list.get(0);
//                    String objId = permissCountList.getObjectId();
//                    String str = permissCountList.getPermissCount();
//                    int bbcount = 0;
//                    if (str == null || str.equals("0")) {
//                        bbcount = 1;
//                    } else {
//                        bbcount = Integer.valueOf(str) + 1;
//                    }
//                    permissCountList.setPermissCount(bbcount + "");
//                    permissCountList.setObjectId(objId);
//                    permissCountList.update(reference.get(), new UpdateListener() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onFailure(int i, String s) {
//
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//        });
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
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(request);
    }

    private void tongji(final String remark, final String qiyeId, final String userId) {
        String url = FXConstant.URL_QIYE_YUANGONGTONGJI;
        StringRequest qDrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(QianMingActivity.this, "签收成功！", Toast.LENGTH_SHORT).show();
                reduceQjcount();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("网络连接错误",volleyError+"");
                Toast.makeText(QianMingActivity.this, "网络连接错误...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("companyId", qiyeId);
                params.put("userId", userId);
                params.put("totalTransAmount",balance);
                params.put("dayTransAmount",balance);
                return params;
            }
        };
        MySingleton.getInstance(QianMingActivity.this).addToRequestQueue(qDrequest);
    }
    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }
    private String getNowTime1() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return dateFormat.format(date);
    }

    private void saveImg(Bitmap mBitmap)  {
        File file = new FileStorage("qianming").createCropFile(imageName,null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(QianMingActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            grantUriPermission(getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(file);
        }
        File f = new File(file.getPath());
        try {
            //如果文件不存在，则创建文件
            if(!f.exists()){
                f.createNewFile();
            }
            //输出流
            FileOutputStream out = new FileOutputStream(f);
            /** mBitmap.compress 压缩图片
             *
             *  Bitmap.CompressFormat.PNG   图片的格式
             *   100  图片的质量（0-100）
             *   out  文件输出流
             */
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void back(View v){
        finish();
    }

    class PaintView extends View {
        private Paint paint;
        private Canvas cacheCanvas;
        private Bitmap cachebBitmap;
        private Path path;

        public Bitmap getCachebBitmap() {
            return cachebBitmap;
        }

        public PaintView(Context context) {
            super(context);
            init();
        }

        private void init() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(10);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            path = new Path();
            cachebBitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
            cacheCanvas = new Canvas(cachebBitmap);
            cacheCanvas.drawColor(Color.WHITE);
        }

        public void clear() {
            if (cacheCanvas != null) {
                paint.setColor(Color.WHITE);
                cacheCanvas.drawPaint(paint);
                paint.setColor(Color.BLACK);
                cacheCanvas.drawColor(Color.WHITE);
                invalidate();
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            // canvas.drawColor(BRUSH_COLOR);
            canvas.drawBitmap(cachebBitmap, 0, 0, null);
            canvas.drawPath(path, paint);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {

            int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
            int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
            if (curW >= w && curH >= h) {
                return;
            }

            if (curW < w)
                curW = w;
            if (curH < h)
                curH = h;

            Bitmap newBitmap = Bitmap.createBitmap(curW, curH, Bitmap.Config.ARGB_8888);
            Canvas newCanvas = new Canvas();
            newCanvas.setBitmap(newBitmap);
            if (cachebBitmap != null) {
                newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
            }
            cachebBitmap = newBitmap;
            cacheCanvas = newCanvas;
        }
        private float cur_x, cur_y;
        @Override
        public boolean onTouchEvent(MotionEvent event) {

            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    cur_x = x;
                    cur_y = y;
                    path.moveTo(cur_x, cur_y);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    path.quadTo(cur_x, cur_y, x, y);
                    cur_x = x;
                    cur_y = y;
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    cacheCanvas.drawPath(path, paint);
                    path.reset();
                    break;
                }
            }
            invalidate();
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
