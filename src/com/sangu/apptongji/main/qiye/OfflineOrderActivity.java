package com.sangu.apptongji.main.qiye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.utils.FileStorage;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.GridImageAdapter;
import com.sangu.apptongji.main.alluser.order.avtivity.FriendActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.QianMingActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.WJPaActivity;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.qiye.entity.OffSendOrderList;
import com.sangu.apptongji.main.utils.FullyGridLayoutManager;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.utils.ZhifuHelpUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yalantis.ucrop.entity.LocalMedia;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017-03-21.
 */

public class OfflineOrderActivity extends BaseActivity implements IPriceView{
    private static final int SDK_PAY_FLAG = 12;
    private ArrayList<String> imagePaths1=new ArrayList<>();
    private ArrayList<String> imagePaths2=new ArrayList<>();
    private String filepath="",biaoshi=null,orderDesc=null,paidanId=null,paidanName=null,infoPrice="0",orderPrice="",
            cusInfo=null,time1=null,time2=null,companyId=null,time_seq;
    OffSendOrderList offSendOrderListone=null;
    private IPricePresenter pricePresenter;
    boolean biaoshi2;
    private List<LocalMedia> selectMedia1 = new ArrayList<>();
    private List<LocalMedia> selectMedia2 = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private RecyclerView recyclerView2;
    private GridImageAdapter adapter2;

    private CustomProgressDialog mProgress=null;
    int allSize=0,downloadSize=0,allSize1=0,allSize2=0;
    String filePath1=null,filePath2=null,filePath3=null,filePath4=null,filePath5=null,filePath6=null,filePath7=null,filePath8=null;
    private EditText et_miaoshu=null,et_name=null,et_phone=null,et_dizhi=null,et_feiyong=null;
    private TextView tv_paidan_qiye=null,tv_paidan_qiye_time=null,tv_paidan_qiyeren=null,tv_paidan_qiyeren_time=null,
            tv_paidan_caiwu=null,tv_paidan_caiwu_time=null,tv_paidan_kehu=null,tv_paidan_kehu_time=null,tv_feiyong=null,
            tev_paidan_qiye=null,tev_paidan_qiyeren=null,tev_paidan_kehu=null,tv_delete=null;
    private RelativeLayout rl_xinxi2=null,rl_miaoshu=null;
    private ImageView iv_paidan_qiye=null,iv_paidan_qiyeren=null,iv_paidan_caiwu=null,iv_paidan_kehu=null,iv_jiantou=null;
    private Button btn_commit=null;
    private int errorTime=3;
    private IWXAPI api=null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (allSize==downloadSize){
                        imagePaths1.clear();
                        imagePaths2.clear();
                        int type = FunctionConfig.TYPE_IMAGE;
                        if (allSize1>0) {
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths1.add(filePath1);
                            media.setNum(imagePaths1.size());
                            media.setPath(filePath1);
                            selectMedia1.add(media);
                        }
                        if (allSize1>1){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths1.add(filePath2);
                            media.setNum(imagePaths1.size());
                            media.setPath(filePath2);
                            selectMedia1.add(media);
                        }
                        if (allSize1>2){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths1.add(filePath3);
                            media.setNum(imagePaths1.size());
                            media.setPath(filePath3);
                            selectMedia1.add(media);
                        }
                        if (allSize1>3){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths1.add(filePath4);
                            media.setNum(imagePaths1.size());
                            media.setPath(filePath4);
                            selectMedia1.add(media);
                        }
                        if (allSize1>0) {
                            adapter.notifyDataSetChanged();
                        }
                        if (allSize2>0) {
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths2.add(filePath5);
                            media.setNum(imagePaths2.size());
                            media.setPath(filePath5);
                            selectMedia2.add(media);
                        }
                        if (allSize2>1){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths2.add(filePath6);
                            media.setNum(imagePaths2.size());
                            media.setPath(filePath6);
                            selectMedia2.add(media);
                        }
                        if (allSize2>2){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths2.add(filePath7);
                            media.setNum(imagePaths2.size());
                            media.setPath(filePath7);
                            selectMedia2.add(media);
                        }
                        if (allSize2>3){
                            LocalMedia media = new LocalMedia();
                            media.setType(type);
                            media.setCompressed(false);
                            media.setCut(false);
                            media.setIsChecked(true);
                            imagePaths2.add(filePath8);
                            media.setNum(imagePaths2.size());
                            media.setPath(filePath8);
                            selectMedia2.add(media);
                        }
                        if (allSize2>0) {
                            adapter2.notifyDataSetChanged();
                        }
                        if (mProgress!=null&&mProgress.isShowing()) {
                            mProgress.dismiss();
                        }
                    }
                    break;
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(OfflineOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        uploadFile();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(OfflineOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_paidan_ofline);
        pricePresenter = new PricePresenter(this,this);
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        WeakReference<OfflineOrderActivity> reference =  new WeakReference<OfflineOrderActivity>(OfflineOrderActivity.this);
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
        imagePaths1 = new ArrayList<>();
        imagePaths2 = new ArrayList<>();
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        biaoshi2 = this.getIntent().hasExtra("biaoshi2");
        paidanId = this.getIntent().getStringExtra("paidanId");
        paidanName = this.getIntent().getStringExtra("paidanName");
        initView();
        initViews();
        rl_miaoshu.setFocusable(true);
        rl_miaoshu.setFocusableInTouchMode(true);
        rl_miaoshu.requestFocus();
        if (!biaoshi.equals("00")) {
            offSendOrderListone = (OffSendOrderList) (this.getIntent().hasExtra("offSendOrderList") ? getIntent().getSerializableExtra("offSendOrderList") : "");
            String paidanId = offSendOrderListone.getOrdId();
            time_seq = offSendOrderListone.getTime_seq();
            companyId = offSendOrderListone.getCompanyId();
            if (paidanId.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                initView2();
            }else {
                initView3();
            }
        }else {
            companyId = this.getIntent().getStringExtra("companyId");
            rl_xinxi2.setVisibility(View.INVISIBLE);
        }
    }

    private void initView3() {
        String userFile1 = offSendOrderListone.getBeforeService1();
        String userFile2 = offSendOrderListone.getBeforeService2();
        String userFile3 = offSendOrderListone.getBeforeService3();
        String userFile4 = offSendOrderListone.getBeforeService4();
        String userFile5 = offSendOrderListone.getAfterService1();
        String userFile6 = offSendOrderListone.getAfterService2();
        String userFile7 = offSendOrderListone.getAfterService3();
        String userFile8 = offSendOrderListone.getAfterService4();
        String sign1 = offSendOrderListone.getSignature1();
        String sign2 = offSendOrderListone.getSignature2();
        String sign3 = offSendOrderListone.getSignature3();
        String cusInfo = offSendOrderListone.getCusInfo();
        String orderInfo = offSendOrderListone.getOrderInfo();
        String [] order = new String[0];
        if (orderInfo!=null&&!"".equals(orderInfo)&&!"null".equals(orderInfo)){
            order = orderInfo.split("\\|");
        }
        if (order.length>0) {
            orderDesc = orderInfo.split("\\|")[0];
        }
        if (order.length>1) {
            infoPrice = orderInfo.split("\\|")[1];
        }
        if (order.length>2) {
            orderPrice = orderInfo.split("\\|")[2];
        }
        String time = offSendOrderListone.getTime();
        if (time!=null&&!"".equals(time)&&!"null".equals(time)) {
            if (time.length()>0) {
                time1 = time.split("\\|")[0];
            }
            if (time.length()>1) {
                time2 = time.split("\\|")[1];
            }
        }
        if (!"0".equals(orderDesc)&&!"null".equals(orderDesc)) {
            et_miaoshu.setText(orderDesc);
        }
        rl_xinxi2.setVisibility(View.INVISIBLE);
        String cusName="0";
        if (cusInfo!=null&&!"".equals(cusInfo)&&!"null".equals(cusInfo)&&cusInfo.length()>0) {
            cusName = cusInfo.split("\\|")[0];
        }
        String cusPhone = "0";
        if (cusInfo!=null&&!"".equals(cusInfo)&&!"null".equals(cusInfo)&&cusInfo.length()>1) {
            cusPhone = cusInfo.split("\\|")[1];
        }
        String cusAddr = "0";
        if (cusInfo!=null&&!"".equals(cusInfo)&&!"null".equals(cusInfo)&&cusInfo.length()>2) {
            cusAddr = cusInfo.split("\\|")[2];
        }
        iv_paidan_qiye.setEnabled(false);
        tv_paidan_qiye.setEnabled(false);
        tv_paidan_qiye.setVisibility(View.INVISIBLE);
        tv_paidan_qiye_time.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(FXConstant.URL_OFFLINE_SIGN+sign1,iv_paidan_qiye);
        String time4 = offSendOrderListone.getTime_seq();
        if (time4!=null&&time4.length()>11) {
            time4 = time4.substring(0, 4) + "-" + time4.substring(4, 6) + "-" + time4.substring(6, 8) + " "
                    + time4.substring(8, 10) + ":" + time4.substring(10, 12);
        }
        tv_paidan_qiye_time.setText(time4);
        if (sign1!=null&&!"".equals(sign1)&&!"null".equals(sign1)){
            tev_paidan_qiye.setVisibility(View.VISIBLE);
            tev_paidan_qiyeren.setVisibility(View.INVISIBLE);
            tev_paidan_kehu.setVisibility(View.INVISIBLE);
        }else if (sign2!=null&&!"".equals(sign2)&&!"null".equals(sign2)){
            tev_paidan_qiye.setVisibility(View.VISIBLE);
            tev_paidan_qiyeren.setVisibility(View.VISIBLE);
            tev_paidan_kehu.setVisibility(View.INVISIBLE);
        }else if (sign3!=null&&!"".equals(sign3)&&!"null".equals(sign3)){
            tev_paidan_qiye.setVisibility(View.VISIBLE);
            tev_paidan_qiyeren.setVisibility(View.VISIBLE);
            tev_paidan_kehu.setVisibility(View.VISIBLE);
        }
        if (sign2!=null&&!"".equals(sign2)&&!"null".equals(sign2)) {
            if (time1!=null&&!time1.equals("0")&&!"null".equals(time1)) {
                tev_paidan_qiyeren.setVisibility(View.VISIBLE);
                tv_paidan_qiyeren_time.setVisibility(View.VISIBLE);
                tv_paidan_qiyeren_time.setText(time1);
            }
            tv_paidan_qiyeren.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_OFFLINE_SIGN+sign2,iv_paidan_qiyeren);
        }
        if (sign3!=null&&!"".equals(sign3)&&!"null".equals(sign3)){
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufive);
            if (orderPrice!=null&&!"".equals(orderPrice)&&!"null".equals(orderPrice)){
                et_feiyong.setText(orderPrice);
            }else {
                et_feiyong.setText("0");
            }
            if (time2!=null&&!time2.equals("")&&!"null".equals(time2)) {
                tv_paidan_kehu_time.setVisibility(View.VISIBLE);
                tv_paidan_kehu_time.setText(time2);
                tev_paidan_kehu.setVisibility(View.VISIBLE);
            }
            tv_paidan_kehu.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_OFFLINE_SIGN+sign3,iv_paidan_kehu);
            btn_commit.setText("订单已完成");
            btn_commit.setEnabled(false);
            btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }
        if (sign2==null||"".equals(sign2)||"null".equals(sign2)){
            iv_jiantou.setImageResource(R.drawable.fuzejiantouone);
            btn_commit.setText("订单正在进行中");
            btn_commit.setEnabled(false);
            btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }
        if ((sign2!=null&&!"".equals(sign2)&&!"null".equals(sign2))&&(sign3==null||"".equals(sign3)||"null".equals(sign3))){
            iv_jiantou.setImageResource(R.drawable.fuzejiantoutwo);
            btn_commit.setText("订单正在进行中");
            btn_commit.setEnabled(false);
            btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }
        et_name.setEnabled(false);
        et_dizhi.setEnabled(false);
        et_phone.setEnabled(false);
        et_name.setText(cusName);
        et_phone.setText(cusPhone);
        et_dizhi.setText(cusAddr);
        et_miaoshu.setEnabled(false);
        recyclerView.setEnabled(false);
        recyclerView2.setEnabled(false);
        rl_xinxi2.setVisibility(View.INVISIBLE);
        if (userFile1 != null&&!"".equals(userFile1)&&!"null".equals(userFile1)){
            filePath1 = FXConstant.URL_OFFLINE_FUWU+userFile1;
            imagePaths1.add(filePath1);
        }
        if (userFile2 != null&&!"".equals(userFile2)&&!"null".equals(userFile2)){
            filePath2 = FXConstant.URL_OFFLINE_FUWU+userFile2;
            imagePaths1.add(filePath2);
        }
        if (userFile3 != null&&!"".equals(userFile3)&&!"null".equals(userFile3)){
            filePath3 = FXConstant.URL_OFFLINE_FUWU+userFile3;
            imagePaths1.add(filePath3);
        }
        if (userFile4 != null&&!"".equals(userFile4)&&!"null".equals(userFile4)){
            filePath4 = FXConstant.URL_OFFLINE_FUWU+userFile4;
            imagePaths1.add(filePath4);
        }
        if (userFile5 != null&&!"".equals(userFile5)&&!"null".equals(userFile5)){
            filePath5 = FXConstant.URL_OFFLINE_FUWU+userFile5;
            imagePaths2.add(filePath5);
        }
        if (userFile6 != null&&!"".equals(userFile6)&&!"null".equals(userFile6)){
            filePath6 = FXConstant.URL_OFFLINE_FUWU+userFile6;
            imagePaths2.add(filePath6);
        }
        if (userFile7 != null&&!"".equals(userFile7)&&!"null".equals(userFile7)){
            filePath7 = FXConstant.URL_OFFLINE_FUWU+userFile7;
            imagePaths2.add(filePath7);
        }
        if (userFile8 != null&&!"".equals(userFile8)&&!"null".equals(userFile8)){
            filePath8 = FXConstant.URL_OFFLINE_FUWU+userFile8;
            imagePaths2.add(filePath8);
        }
        tv_feiyong.setText("￥: "+infoPrice+"元");
        et_feiyong.setEnabled(false);
    }

    private void downloadFile(final String url, String fileName, final int size){
        File saveFile = new FileStorage("offline").createCropFile(fileName,null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(OfflineOrderActivity.this, "com.sangu.app.fileprovider", saveFile);//通过FileProvider创建一个content类型的Uri
            grantUriPermission(getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(saveFile);
        }
        File f = new File(saveFile.getPath());
        if (size==1) {
            filePath1 = f.getAbsolutePath();
        }else if (size==2){
            filePath2 = f.getAbsolutePath();
        }else if (size==3){
            filePath3 = f.getAbsolutePath();
        }else if (size==4){
            filePath4 = f.getAbsolutePath();
        }else if (size==5){
            filePath5 = f.getAbsolutePath();
        }else if (size==6){
            filePath6 = f.getAbsolutePath();
        }else if (size==7){
            filePath7 = f.getAbsolutePath();
        }else if (size==8){
            filePath8 = f.getAbsolutePath();
        }
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (f.exists()) {
            final File finalSaveFile = f;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    OkHttpClient client = new OkHttpClient();
                    try {
                        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
                        okhttp3.Response response = client.newCall(request).execute();
                        InputStream is = response.body().byteStream();
                        final Bitmap bm = BitmapFactory.decodeStream(is);
                        if (bm != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    saveToSD(bm, finalSaveFile);
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    private void saveToSD(Bitmap mBitmap,File f) {
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
            downloadSize++;
            if (downloadSize==allSize) {
                handler.sendEmptyMessage(1);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView2() {
        String userFile1 = offSendOrderListone.getBeforeService1();
        String userFile2 = offSendOrderListone.getBeforeService2();
        String userFile3 = offSendOrderListone.getBeforeService3();
        String userFile4 = offSendOrderListone.getBeforeService4();
        String userFile5 = offSendOrderListone.getAfterService1();
        String userFile6 = offSendOrderListone.getAfterService2();
        String userFile7 = offSendOrderListone.getAfterService3();
        String userFile8 = offSendOrderListone.getAfterService4();
        String sign1 = offSendOrderListone.getSignature1();
        String sign2 = offSendOrderListone.getSignature2();
        String sign3 = offSendOrderListone.getSignature3();
        if (sign1!=null&&!"".equals(sign1)&&!"null".equals(sign1)){
            tev_paidan_qiye.setVisibility(View.VISIBLE);
            tev_paidan_qiyeren.setVisibility(View.INVISIBLE);
            tev_paidan_kehu.setVisibility(View.INVISIBLE);
        }else if (sign2!=null&&!"".equals(sign2)&&!"null".equals(sign2)){
            tev_paidan_qiye.setVisibility(View.VISIBLE);
            tev_paidan_qiyeren.setVisibility(View.VISIBLE);
            tev_paidan_kehu.setVisibility(View.INVISIBLE);
        }else if (sign3!=null&&!"".equals(sign3)&&!"null".equals(sign3)){
            tev_paidan_qiye.setVisibility(View.VISIBLE);
            tev_paidan_qiyeren.setVisibility(View.VISIBLE);
            tev_paidan_kehu.setVisibility(View.VISIBLE);
        }
        if (sign3==null||"".equals(sign3)||"null".equals(sign3)) {
            mProgress.show();
            if (userFile1 != null&&!"".equals(userFile1)&&!"null".equals(userFile1)) {
                allSize++;
                allSize1++;
            }
            if (userFile2 != null&&!"".equals(userFile2)&&!"null".equals(userFile2)) {
                allSize++;
                allSize1++;
            }
            if (userFile3 != null&&!"".equals(userFile3)&&!"null".equals(userFile3)) {
                allSize++;
                allSize1++;
            }
            if (userFile4 != null&&!"".equals(userFile4)&&!"null".equals(userFile4)) {
                allSize++;
                allSize1++;
            }
            if (userFile5 != null&&!"".equals(userFile5)&&!"null".equals(userFile5)) {
                allSize++;
                allSize2++;
            }
            if (userFile6 != null&&!"".equals(userFile6)&&!"null".equals(userFile6)) {
                allSize++;
                allSize2++;
            }
            if (userFile7 != null&&!"".equals(userFile7)&&!"null".equals(userFile7)) {
                allSize++;
                allSize2++;
            }
            if (userFile8 != null&&!"".equals(userFile8)&&!"null".equals(userFile8)) {
                allSize++;
                allSize2++;
            }
        }
        if (sign3==null||"".equals(sign3)||"null".equals(sign3)) {
            if (userFile1 != null&&!"".equals(userFile1)&&!"null".equals(userFile1)) {
                filePath1 = FXConstant.URL_OFFLINE_FUWU+userFile1;
                downloadFile(filePath1,userFile1,1);
            }
            if (userFile2 != null&&!"".equals(userFile2)&&!"null".equals(userFile2)) {
                filePath2 = FXConstant.URL_OFFLINE_FUWU+userFile2;
                downloadFile(filePath2,userFile2,2);
            }
            if (userFile3 != null&&!"".equals(userFile3)&&!"null".equals(userFile3)) {
                filePath3 = FXConstant.URL_OFFLINE_FUWU+userFile3;
                downloadFile(filePath3,userFile3,3);
            }
            if (userFile4 != null&&!"".equals(userFile4)&&!"null".equals(userFile4)) {
                filePath4 = FXConstant.URL_OFFLINE_FUWU+userFile4;
                downloadFile(filePath4,userFile4,4);
            }
            if (userFile5 != null&&!"".equals(userFile5)&&!"null".equals(userFile5)) {
                filePath5 = FXConstant.URL_OFFLINE_FUWU+userFile5;
                downloadFile(filePath5,userFile5,5);
            }
            if (userFile6 != null&&!"".equals(userFile6)&&!"null".equals(userFile6)) {
                filePath6 = FXConstant.URL_OFFLINE_FUWU+userFile6;
                downloadFile(filePath6,userFile6,6);
            }
            if (userFile7 != null&&!"".equals(userFile7)&&!"null".equals(userFile7)) {
                filePath7 = FXConstant.URL_OFFLINE_FUWU+userFile7;
                downloadFile(filePath7,userFile7,7);
            }
            if (userFile8 != null&&!"".equals(userFile8)&&!"null".equals(userFile8)) {
                filePath8 = FXConstant.URL_OFFLINE_FUWU+userFile8;
                downloadFile(filePath8,userFile8,8);
            }
            if ((userFile1==null||"null".equals(userFile1))&&(userFile2==null||"null".equals(userFile2))&&(userFile3==null||"null".equals(userFile3))&&(userFile4==null||"null".equals(userFile4))&&(userFile5==null||"null".equals(userFile5))
                    &&(userFile6==null||"null".equals(userFile6))&&(userFile7==null||"null".equals(userFile7))&&(userFile8==null||"null".equals(userFile8))){
                if (mProgress!=null&&mProgress.isShowing()) {
                    mProgress.dismiss();
                }
            }
        }else {
            rl_xinxi2.setVisibility(View.INVISIBLE);
            if (userFile1 != null&&!"".equals(userFile1)&&!"null".equals(userFile1)){
                filePath1 = FXConstant.URL_OFFLINE_FUWU+userFile1;
                imagePaths1.add(filePath1);
            }
            if (userFile2 != null&&!"".equals(userFile2)&&!"null".equals(userFile2)){
                filePath2 = FXConstant.URL_OFFLINE_FUWU+userFile2;
                imagePaths1.add(filePath2);
            }
            if (userFile3 != null&&!"".equals(userFile3)&&!"null".equals(userFile3)){
                filePath3 = FXConstant.URL_OFFLINE_FUWU+userFile3;
                imagePaths1.add(filePath3);
            }
            if (userFile4 != null&&!"".equals(userFile4)&&!"null".equals(userFile4)){
                filePath4 = FXConstant.URL_OFFLINE_FUWU+userFile4;
                imagePaths1.add(filePath4);
            }
            if (userFile5 != null&&!"".equals(userFile5)&&!"null".equals(userFile5)){
                filePath5 = FXConstant.URL_OFFLINE_FUWU+userFile5;
                imagePaths2.add(filePath5);
            }
            if (userFile6 != null&&!"".equals(userFile6)&&!"null".equals(userFile6)){
                filePath6 = FXConstant.URL_OFFLINE_FUWU+userFile6;
                imagePaths2.add(filePath6);
            }
            if (userFile7 != null&&!"".equals(userFile7)&&!"null".equals(userFile7)){
                filePath7 = FXConstant.URL_OFFLINE_FUWU+userFile7;
                imagePaths2.add(filePath7);
            }
            if (userFile8!=null&&!"".equals(userFile8)&&!"null".equals(userFile8)){
                filePath8 = FXConstant.URL_OFFLINE_FUWU+userFile8;
                imagePaths2.add(filePath8);
            }
        }
        cusInfo = offSendOrderListone.getCusInfo();
        String orderInfo = offSendOrderListone.getOrderInfo();
        String [] order = new String[0];
        if (orderInfo!=null&&!"".equals(orderInfo)&&!"null".equals(orderInfo)){
            order = orderInfo.split("\\|");
        }
        if (order.length>0) {
            orderDesc = orderInfo.split("\\|")[0];
        }
        if (order.length>1) {
            infoPrice = orderInfo.split("\\|")[1];
        }
        if (order.length>2) {
            orderPrice = orderInfo.split("\\|")[2];
        }
        if (!"0".equals(orderDesc)) {
            et_miaoshu.setText(orderDesc);
        }
        String cusName="0";
        if (cusInfo!=null&&!"".equals(cusInfo)&&!"null".equals(cusInfo)&&cusInfo.length()>0) {
            cusName = cusInfo.split("\\|")[0];
        }
        String cusPhone = "0";
        if (cusInfo!=null&&!"".equals(cusInfo)&&!"null".equals(cusInfo)&&cusInfo.length()>1) {
            cusPhone = cusInfo.split("\\|")[1];
        }
        String cusAddr = "0";
        if (cusInfo!=null&&!"".equals(cusInfo)&&!"null".equals(cusInfo)&&cusInfo.length()>2) {
            cusAddr = cusInfo.split("\\|")[2];
        }
        String time = offSendOrderListone.getTime();
        if (time!=null&&!"".equals(time)&&!"null".equals(time)) {
            if (time.length()>0) {
                time1 = time.split("\\|")[0];
            }
            if (time.length()>1) {
                time2 = time.split("\\|")[1];
            }
        }
        iv_paidan_qiye.setEnabled(false);
        tv_paidan_qiye.setEnabled(false);
        tv_paidan_qiye.setVisibility(View.INVISIBLE);
        tv_paidan_qiye_time.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(FXConstant.URL_OFFLINE_SIGN+sign1,iv_paidan_qiye);
        String time4 = offSendOrderListone.getTime_seq();
        if (time4!=null&&time4.length()>11) {
            time4 = time4.substring(0, 4) + "-" + time4.substring(4, 6) + "-" + time4.substring(6, 8) + " "
                    + time4.substring(8, 10) + ":" + time4.substring(10, 12);
        }
        tv_paidan_qiye_time.setText(time4);
        if (sign2!=null&&!"".equals(sign2)&&!"null".equals(sign2)) {
            if (time1!=null&&!time1.equals("")&&!time1.equals("0")) {
                tv_paidan_qiyeren_time.setVisibility(View.VISIBLE);
                tv_paidan_qiyeren_time.setText(time1);
            }
            tv_paidan_qiyeren.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_OFFLINE_SIGN+sign2,iv_paidan_qiyeren);
        }
        if (sign3!=null&&!"".equals(sign3)&&!"null".equals(sign3)){
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufive);
            recyclerView.setEnabled(false);
            recyclerView2.setEnabled(false);
            et_miaoshu.setEnabled(false);
            et_feiyong.setEnabled(false);
            tv_paidan_qiye.setEnabled(false);
            iv_paidan_qiye.setEnabled(false);
            tv_paidan_qiyeren.setEnabled(false);
            iv_paidan_qiyeren.setEnabled(false);
            tv_paidan_kehu.setEnabled(false);
            iv_paidan_kehu.setEnabled(false);
            if (time2!=null&&!time2.equals("0")&&!"null".equals(time2)) {
                tv_paidan_kehu_time.setVisibility(View.VISIBLE);
                tv_paidan_kehu_time.setText(time2);
            }
            tv_paidan_kehu.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_OFFLINE_SIGN+sign3,iv_paidan_kehu);
            et_feiyong.setText(orderPrice);
            btn_commit.setEnabled(false);
            btn_commit.setText("交易完成");
            btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }
        et_name.setEnabled(false);
        et_dizhi.setEnabled(false);
        et_phone.setEnabled(false);
        et_name.setText(cusName);
        et_phone.setText(cusPhone);
        et_dizhi.setText(cusAddr);
        if (sign2==null||"".equals(sign2)||"null".equals(sign2)){
            tv_delete.setVisibility(View.VISIBLE);
            tev_paidan_qiye.setVisibility(View.VISIBLE);
            iv_jiantou.setImageResource(R.drawable.fuzejiantouone);
            recyclerView.setEnabled(false);
            recyclerView2.setEnabled(false);
            et_miaoshu.setEnabled(false);
            et_feiyong.setEnabled(false);
            btn_commit.setText("接单");
            btn_commit.setEnabled(true);
            if (infoPrice==null||"".equals(infoPrice)||"null".equals(infoPrice)||Double.parseDouble(infoPrice)<=0){
                et_name.setText(cusName);
                et_phone.setText(cusPhone);
                et_dizhi.setText(cusAddr);
                rl_xinxi2.setVisibility(View.INVISIBLE);
            }else {
                tv_feiyong.setText("￥: " + infoPrice + "元");
                if (cusName.length()>1) {
                    et_name.setText(cusName.substring(0, 1) + "***");
                }
                if (cusPhone.length()>3) {
                    et_phone.setText(cusPhone.substring(0, 3) + "***");
                }
                if (cusAddr.length()>3) {
                    et_dizhi.setText(cusAddr.substring(0, 3) + "***");
                }
                rl_xinxi2.setVisibility(View.VISIBLE);
            }
            tv_paidan_qiyeren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(OfflineOrderActivity.this, QianMingActivity.class).putExtra("biaoshi","21"),6);
                }
            });
            iv_paidan_qiyeren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(OfflineOrderActivity.this, QianMingActivity.class).putExtra("biaoshi","21"),6);
                }
            });
            rl_xinxi2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String payPass = DemoApplication.getInstance().getCurrentPayPass();
                    final Double yuE = DemoApplication.getInstance().getCurrenPrice();
                    if (filepath==null||"".equals(filepath)){
                        Toast.makeText(OfflineOrderActivity.this, "签名提交之后才可以查看客户信息！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (infoPrice!=null&&!"".equals(infoPrice)&&!"null".equals(infoPrice)&&Double.parseDouble(infoPrice)>0) {
                        LayoutInflater inflaterDl = LayoutInflater.from(OfflineOrderActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                        final Dialog dialog = new AlertDialog.Builder(OfflineOrderActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                        re_item5.setVisibility(View.VISIBLE);
                        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                        TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
                        tv_title.setText("请选择支付方式");
                        tv_item1.setText("余 额 支 付");
                        tv_item2.setText("微 信 支 付");
                        tv_item5.setText("支付宝支付");
                        re_item1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                zhifu();
                            }
                        });
                        re_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                rechargefromWx(infoPrice,companyId);
                            }
                        });
                        re_item5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                rechargefromZhFb(infoPrice,companyId);
                            }
                        });
                        re_item3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }else{
                        uploadFile();
                    }
                }
            });
            btn_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String payPass = DemoApplication.getInstance().getCurrentPayPass();
                    final Double yuE = DemoApplication.getInstance().getCurrenPrice();
                    if (payPass==null||"".equals(payPass)){
                        Toast.makeText(OfflineOrderActivity.this, "您还没有设置支付密码！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (filepath==null||"".equals(filepath)){
                        Toast.makeText(OfflineOrderActivity.this, "签名之后才可以提交！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (infoPrice!=null&&!"".equals(infoPrice)&&!"null".equals(infoPrice)&&Double.parseDouble(infoPrice)>0) {
                        LayoutInflater inflaterDl = LayoutInflater.from(OfflineOrderActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                        final Dialog dialog = new AlertDialog.Builder(OfflineOrderActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(true);
                        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                        re_item5.setVisibility(View.VISIBLE);
                        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                        TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
                        tv_title.setText("请选择支付方式");
                        tv_item1.setText("余 额 支 付");
                        tv_item2.setText("微 信 支 付");
                        tv_item5.setText("支付宝支付");
                        re_item1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                zhifu();
                            }
                        });
                        re_item2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                rechargefromWx(infoPrice,companyId);
                            }
                        });
                        re_item5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                rechargefromZhFb(infoPrice,companyId);
                            }
                        });
                        re_item3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }else{
                        uploadFile();
                    }
                }
            });
        }
        if ((sign2!=null&&!"".equals(sign2)&&!"null".equals(sign2))&&(sign3==null||"".equals(sign3)||"null".equals(sign3))){
            tev_paidan_qiye.setVisibility(View.VISIBLE);
            tev_paidan_qiyeren.setVisibility(View.VISIBLE);
            iv_jiantou.setImageResource(R.drawable.fuzejiantoutwo);
            btn_commit.setText("提交");
            tv_paidan_kehu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(OfflineOrderActivity.this, QianMingActivity.class).putExtra("biaoshi","21"),7);
                }
            });
            iv_paidan_kehu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(OfflineOrderActivity.this, QianMingActivity.class).putExtra("biaoshi","21"),7);
                }
            });
            btn_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initbtn2(time1);
                }
            });
        }
        if (sign3!=null&&!"".equals(sign3)&&!"null".equals(sign3)){
            tev_paidan_qiye.setVisibility(View.VISIBLE);
            tev_paidan_qiyeren.setVisibility(View.VISIBLE);
            tev_paidan_kehu.setVisibility(View.VISIBLE);
            tev_paidan_kehu.setText("完成");
            btn_commit.setText("交易完成");
            btn_commit.setEnabled(false);
            btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }
    }

    private void zhifu() {
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        final String payPass = DemoApplication.getInstance().getCurrentPayPass();
        final Double yuE = DemoApplication.getInstance().getCurrenPrice();
        if (payPass==null||"".equals(payPass)){
            ZhifuHelpUtils.showErrorMiMaSHZH(this,payPass,"000");
            return;
        }
        if (payPass.length()!=6||!ZhifuHelpUtils.isNumeric(payPass)){
            ZhifuHelpUtils.showErrorMiMaXG(this,payPass,"000");
            return;
        }
        if (errorTime<=0){
            ZhifuHelpUtils.showErrorLing(this);
            return;
        }
        if (yuE >= Double.parseDouble(infoPrice)) {
            if (mProgress!=null&&mProgress.isShowing()){
                mProgress.dismiss();
            }
            LayoutInflater inflaterDl = LayoutInflater.from(OfflineOrderActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(OfflineOrderActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            final PasswordView pwdView = (PasswordView)layout.findViewById(R.id.pwd_view);
            pwdView.setOnFinishInput(new OnPasswordInputFinish() {
                @Override
                public void inputFinish() {
                    final ProgressDialog pd = new ProgressDialog(OfflineOrderActivity.this);
                    pd.setMessage("正在支付...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    if (pwdView.getStrPassword().equals(payPass)) {
                        dialog.dismiss();
                        String url = FXConstant.URL_ZHUANZHANG;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                if (mProgress!=null&&mProgress.isShowing()){
                                    mProgress.dismiss();
                                }
                                Log.e("OfflineOrderActivity", "转账成功");
                                uploadFile();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(OfflineOrderActivity.this, "网络连接中断，请重新提交", Toast.LENGTH_SHORT).show();
                                if (mProgress!=null&&mProgress.isShowing()){
                                    mProgress.dismiss();
                                }
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> param = new HashMap<String, String>();
                                param.put("merId", companyId);
                                param.put("balance", infoPrice);
                                param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                param.put("qrCode", "1");
                                return param;
                            }
                        };
                        MySingleton.getInstance(OfflineOrderActivity.this).addToRequestQueue(request);
                    } else {
                        if (mProgress!=null&&mProgress.isShowing()){
                            mProgress.dismiss();
                        }
                        pd.dismiss();
                        int times;
                        if (errorTime>0){
                            times = errorTime-1;
                        }else {
                            times = 0;
                        }
                        reduceShRZFCount(times+"");
                        if (times==0) {
                            ZhifuHelpUtils.showErrorLing(OfflineOrderActivity.this);
                        }else {
                            ZhifuHelpUtils.showErrorTishi(OfflineOrderActivity.this,times + "",null,"000");
                        }
                        dialog.dismiss();
                    }
                }
            });
            /**
             *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供响应
             *  如果写了，会覆盖我们在自定义控件中提供的响应
             *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
            pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (mProgress!=null&&mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
            });
            pwdView.getForgetTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bs;
                    bs = "000";
                    startActivity(new Intent(OfflineOrderActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
                }
            });
        }else {
            LayoutInflater inflaterDl = LayoutInflater.from(OfflineOrderActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(OfflineOrderActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
            RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
            RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
            RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
            re_item1.setVisibility(View.GONE);
            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
            TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
            TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
            tv_title.setText("钱包余额不足(剩余"+yuE+"元),请您先充值或者选择其他支付方式");
            tv_item2.setText("微信支付");
            tv_item5.setText("支付宝支付");
            re_item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (mProgress!=null&&mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                    rechargefromWx(infoPrice,companyId);
                }
            });
            re_item5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (mProgress!=null&&mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                    rechargefromZhFb(infoPrice,companyId);
                }
            });
            re_item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mProgress!=null&&mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                    dialog.dismiss();
                }
            });
        }
        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    private void reduceShRZFCount(final String times) {
        String url = FXConstant.URL_UPDATEZHHU;
        StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if (code.equals("SUCCESS")){
                    if (errorTime>0) {
                        errorTime--;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                params1.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                params1.put("enterErrorTimes", times+"");
                return params1;
            }
        };
        MySingleton.getInstance(OfflineOrderActivity.this).addToRequestQueue(request3);
    }

    private void uploadFile() {
        final String nowTime = getnowTime();
        String url = FXConstant.URL_UPDATE_OFFSEND;
        final List<File> files = new ArrayList<File>();
        if (filepath!=null&&!"".equals(filepath)) {
            File file = null;
            file = new File(filepath);
            if (file.exists()) {
                files.add(file);
            }
        }
        List<Param> param=new ArrayList<>();
        param.add(new Param("companyId",companyId));
        param.add(new Param("time_seq",time_seq));
        param.add(new Param("time", nowTime+"|"+"0"));
        String str = "signature2";
        OkHttpManager.getInstance().post(str, param, files, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                Toast.makeText(OfflineOrderActivity.this, "接单成功！", Toast.LENGTH_SHORT).show();
                tv_delete.setVisibility(View.INVISIBLE);
                filepath = null;
                String cusName = cusInfo.split("\\|")[0];
                String cusPhone = cusInfo.split("\\|")[1];
                String cusAddr = cusInfo.split("\\|")[2];
                et_name.setEnabled(false);
                et_dizhi.setEnabled(false);
                et_phone.setEnabled(false);
                et_name.setText(cusName);
                et_phone.setText(cusPhone);
                et_dizhi.setText(cusAddr);
                rl_xinxi2.setVisibility(View.INVISIBLE);
                et_miaoshu.setEnabled(true);
                recyclerView.setEnabled(true);
                recyclerView2.setEnabled(true);
                et_feiyong.setEnabled(true);
                tv_paidan_qiyeren.setEnabled(false);
                iv_paidan_qiyeren.setEnabled(false);
                tev_paidan_qiyeren.setVisibility(View.VISIBLE);
                tv_paidan_qiyeren_time.setVisibility(View.VISIBLE);
                tv_paidan_qiyeren_time.setText(nowTime);
                tv_paidan_kehu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(OfflineOrderActivity.this, QianMingActivity.class).putExtra("biaoshi","21"),7);
                    }
                });
                iv_paidan_kehu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(OfflineOrderActivity.this, QianMingActivity.class).putExtra("biaoshi","21"),7);
                    }
                });
                iv_jiantou.setImageResource(R.drawable.fuzejiantoutwo);
                btn_commit.setEnabled(true);
                btn_commit.setText("提交");
                btn_commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initbtn2(nowTime);
                    }
                });
            }
            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }

    private void rechargefromWx(final String balance1,final String uId) {
        String zhuanZhangId = DemoHelper.getInstance().getCurrentUsernName();
        final String mubiaoId = zhuanZhangId+"_"+"1"+"_"+uId;
        String balance = (int)(Double.parseDouble(balance1)*100)+"";
        Toast.makeText(OfflineOrderActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = balance;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity","OfflineOrderActivity");
                    editor.commit();
                    JSONObject object = new JSONObject(s);
                    Log.e("chongzhiac,s", s);
                    String appid="",mch_id="",nonce_str="",sign="",prepayId="",timestamp="";
                    appid = object.getString("appid");
                    nonce_str = object.getString("noncestr");
                    mch_id = object.getString("partnerid");
                    prepayId = object.getString("prepayid");
                    timestamp = object.getString("timestamp");
                    sign = object.getString("sign");
                    PayReq req = new PayReq();
                    req.appId = appid;
                    req.partnerId = mch_id;
                    req.prepayId = prepayId;
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = nonce_str;
                    //这是得到一个时间戳(除以1000转化成秒数)
                    req.timeStamp = timestamp;
                    //调用获得签名的方法,这里直接把服务器返回来的sign给覆盖了,所以我不是很明白服务器为什么返回这个sign值,然后调起支付,基本上就可以了(我的反正是可以了....)
//                sign = OrderInfoUtil2_0.createSign(parameters);
                    Log.e("TAG", "timestamp=====" + req.timeStamp);
                    req.sign = sign;
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(OfflineOrderActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("body","正事多-转账");
                param.put("detail","正事多-转账");
                param.put("out_trade_no",getNowTime());
                param.put("total_fee", finalBalance);
                param.put("spbill_create_ip",getHostIP());
                param.put("attach",mubiaoId);
                return param;
            }
        };
        MySingleton.getInstance(OfflineOrderActivity.this).addToRequestQueue(request);
    }

    private String getNowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

    private String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }

    private void rechargefromZhFb(final String balance,final String uId){
        String chongzhiId=null;
        chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        try {
            chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (balance!=null&&Double.parseDouble(balance)>0) {
            String url = FXConstant.URL_ZhiFu;
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX,balance,chongzhiId,"1",uId,null,"正事多-转账");
            final String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            final String orderinfo = OrderInfoUtil2_0.getSign(params);
            StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String sign = object.getString("sign");
                        sign = URLEncoder.encode(sign, "UTF-8");
                        final String orderInfo = orderParam + "&" + "sign=" + sign;
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(OfflineOrderActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                handler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(OfflineOrderActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(OfflineOrderActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(OfflineOrderActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private void initbtn2(final String sign2Time){
        if (filepath==null||"".equals(filepath)){
            Toast.makeText(OfflineOrderActivity.this, "签名之后才可以提交！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mProgress!=null&&!mProgress.isShowing()){
            mProgress.setMessage("正在提交数据...");
            mProgress.show();
        }
        updateDataWithMany(sign2Time);
    }

    private void reduceQjcount() {
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
                param.put("offSendOrderCount","-1");
                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(OfflineOrderActivity.this).addToRequestQueue(request);
    }

    private void initViews(){
        et_feiyong.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(OfflineOrderActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(OfflineOrderActivity.this, onAddPicClickListener1);
        adapter.setList(selectMedia1);
        adapter.setSelectMax(4);
        adapter.setType(1);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (selectMedia1.get(position).getType()) {
                    case FunctionConfig.TYPE_IMAGE:
                        // 预览图片 可长按保存 也可自定义保存路径
                        PictureConfig.getInstance().externalPicturePreview(OfflineOrderActivity.this, position, selectMedia1);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia1.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(OfflineOrderActivity.this, selectMedia1.get(position).getPath());
                        }
                        break;
                }
            }
        });
        recyclerView2 = (RecyclerView) findViewById(R.id.recycler2);
        FullyGridLayoutManager manager2 = new FullyGridLayoutManager(OfflineOrderActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(manager2);
        adapter2 = new GridImageAdapter(OfflineOrderActivity.this, onAddPicClickListener2);
        adapter2.setList(selectMedia2);
        adapter2.setSelectMax(4);
        adapter2.setType(2);
        recyclerView2.setAdapter(adapter2);
        adapter2.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (selectMedia2.get(position).getType()) {
                    case FunctionConfig.TYPE_IMAGE:
                        // 预览图片 可长按保存 也可自定义保存路径
                        PictureConfig.getInstance().externalPicturePreview(OfflineOrderActivity.this, position, selectMedia2);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia2.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(OfflineOrderActivity.this, selectMedia2.get(position).getPath());
                        }
                        break;
                }
            }
        });

        tv_paidan_qiye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(OfflineOrderActivity.this, QianMingActivity.class).putExtra("biaoshi","21"),5);
            }
        });
        iv_paidan_qiye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(OfflineOrderActivity.this, QianMingActivity.class).putExtra("biaoshi","21"),5);
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflaterDl = LayoutInflater.from(OfflineOrderActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                final Dialog dialog = new AlertDialog.Builder(OfflineOrderActivity.this,R.style.Dialog).create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                final Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                btnOK.setText("确定");
                btnCancel.setText("取消");
                title_tv.setText("确定删除派单吗？");
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String url = FXConstant.URL_DELETE_OFFSEND;
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Log.e("offlineorac,s",s);
                                try {
                                    JSONObject object = new JSONObject(s);
                                    String code = object.getString("code");
                                    if ("success".equals(code)){
                                        reduceQjcount();
                                        Toast.makeText(OfflineOrderActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else {
                                        Toast.makeText(OfflineOrderActivity.this,"网络连接错误,删除失败",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(OfflineOrderActivity.this,"网络连接错误,删除失败",Toast.LENGTH_SHORT).show();
                                if (volleyError!=null&&volleyError.getMessage()!=null) {
                                    Log.e("offlineorac,e",volleyError.getMessage());
                                }
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> param = new HashMap<String, String>();
                                param.put("companyId",companyId);
                                param.put("time_seq",time_seq);
                                return param;
                            }
                        };
                        MySingleton.getInstance(OfflineOrderActivity.this).addToRequestQueue(request);
                    }
                });
            }
        });
        if ("00".equals(biaoshi)) {
            btn_commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("".equals(filepath)) {
                        Toast.makeText(OfflineOrderActivity.this, "请先在派单人处签名！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                        Toast.makeText(OfflineOrderActivity.this, "请输入客户名字！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                        Toast.makeText(OfflineOrderActivity.this, "请输入客户电话！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(et_dizhi.getText().toString().trim())) {
                        Toast.makeText(OfflineOrderActivity.this, "请输入客户地址！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (biaoshi2){
                        insertDataWithMany(paidanName,paidanId);
                    }else {
                        showdialog();
                    }
                }
            });
        }
    }

    private void insertDataWithMany(final String userName, final String userId) {
        final String cusName = et_name.getText().toString().trim();
        final String cusPhone = et_phone.getText().toString().trim();
        final String cusAdress = et_dizhi.getText().toString().trim();
        if (mProgress!=null){
            if (!mProgress.isShowing()){
                mProgress.setMessage("正在派单，请稍等...");
                mProgress.show();
            }
        }else {
            WeakReference<OfflineOrderActivity> reference =  new WeakReference<OfflineOrderActivity>(OfflineOrderActivity.this);
            mProgress = CustomProgressDialog.createDialog(reference.get());
            mProgress.setMessage("正在派单，请稍等...");
            mProgress.show();
            mProgress.setCancelable(true);
            mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mProgress.dismiss();
                }
            });
        }
        String orderInfo = null,cusInfo=null;
        final String orderDesc = TextUtils.isEmpty(et_miaoshu.getText().toString().trim())?"0":et_miaoshu.getText().toString().trim();
        final String infoPrice = TextUtils.isEmpty(et_feiyong.getText().toString().trim())?"0":et_feiyong.getText().toString().trim();
        orderInfo = orderDesc+"|"+infoPrice+"|"+"0";
        cusInfo = cusName+"|"+cusPhone+"|"+cusAdress;
        final List<File> files = new ArrayList<File>();
        if (filepath!=null&&!"".equals(filepath)) {
            File file = null;
            file = new File(filepath);
            if (file.exists()) {
                files.add(file);
            }
        }
        final List<File> files1 = new ArrayList<File>();
        if (imagePaths1.size()>0){
            File file = null;
            file = new File(imagePaths1.get(0));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>1){
            File file = null;
            file = new File(imagePaths1.get(1));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>2){
            File file = null;
            file = new File(imagePaths1.get(2));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>3){
            File file = null;
            file = new File(imagePaths1.get(3));
            if (file.exists()) {
                files1.add(file);
            }
        }
        final List<File> files2 = new ArrayList<File>();
        if (imagePaths2.size() > 0) {
            File file = null;
            file = new File(imagePaths2.get(0));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 1) {
            File file = null;
            file = new File(imagePaths2.get(1));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 2) {
            File file = null;
            file = new File(imagePaths2.get(2));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 3) {
            File file = null;
            file = new File(imagePaths2.get(3));
            if (file.exists()) {
                files2.add(file);
            }
        }
        List<Param> param=new ArrayList<>();
        param.add(new Param("companyId",companyId));
        param.add(new Param("ordName",userName));
        param.add(new Param("ordId", userId));
        param.add(new Param("sendIdentify","0"));
        param.add(new Param("cusInfo", cusInfo));
        param.add(new Param("orderInfo", orderInfo));
        param.add(new Param("Identification", "1"));
        String url = FXConstant.URL_INSERT_OFFSEND;
        String str = "signature1";
        String str1 = "beforeService";
        String str2 = "afterService";
        OkHttpManager.getInstance().postThfile(str, files, param, str1, files1, str2, files2, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                String code = jsonObject.getString("code");
                if ("success".equals(code)){
                    sendPushMessage(userId);
                    updateBmob(userId);
                    Toast.makeText(OfflineOrderActivity.this,"派单成功！",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                        mProgress=null;
                    }
                }
                Toast.makeText(OfflineOrderActivity.this,"网络连接错误,派单失败",Toast.LENGTH_SHORT).show();
                if (errorMsg!=null) {
                    Log.e("offlineorder,e", errorMsg);
                }
            }
        });
    }

    private void sendPushMessage(final String hxid1) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
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
                param.put("body","派单消息");
                param.put("type","05");
                param.put("userId",myId);
                param.put("companyId",companyId);
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(OfflineOrderActivity.this).addToRequestQueue(request);
    }

    private void updateBmob(final String userId) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
                Log.e("addfriend,s",s);
                duanxintongzhi(userId,"【正事多】 通知:您有一条新的派单消息");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
                Log.e("addfriend,e",volleyError.toString());
                duanxintongzhi(userId,"【正事多】 通知:您有一条新的派单消息");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("offSendOrderCount","1");
                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(OfflineOrderActivity.this).addToRequestQueue(request);
    }
    private void duanxintongzhi(final String id, final String message) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                setResult(RESULT_OK);
                finish();
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
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(OfflineOrderActivity.this).addToRequestQueue(request);
            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void updateDataWithMany(String sign2Time) {
        String orderPrice = TextUtils.isEmpty(et_feiyong.getText().toString().trim())?"0":et_feiyong.getText().toString().trim();
        String orderInfo = null;
        orderInfo = orderDesc+"|"+infoPrice+"|"+orderPrice;
        final List<File> files = new ArrayList<File>();
        if (filepath!=null&&!"".equals(filepath)) {
            File file = null;
            file = new File(filepath);
            if (file.exists()) {
                files.add(file);
            }
        }
        final List<File> files1 = new ArrayList<File>();
        if (imagePaths1.size()>0){
            File file = null;
            file = new File(imagePaths1.get(0));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>1){
            File file = null;
            file = new File(imagePaths1.get(1));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>2){
            File file = null;
            file = new File(imagePaths1.get(2));
            if (file.exists()) {
                files1.add(file);
            }
        }
        if (imagePaths1.size()>3){
            File file = null;
            file = new File(imagePaths1.get(3));
            if (file.exists()) {
                files1.add(file);
            }
        }
        final List<File> files2 = new ArrayList<File>();
        if (imagePaths2.size() > 0) {
            File file = null;
            file = new File(imagePaths2.get(0));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 1) {
            File file = null;
            file = new File(imagePaths2.get(1));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 2) {
            File file = null;
            file = new File(imagePaths2.get(2));
            if (file.exists()) {
                files2.add(file);
            }
        }
        if (imagePaths2.size() > 3) {
            File file = null;
            file = new File(imagePaths2.get(3));
            if (file.exists()) {
                files2.add(file);
            }
        }
        final String nowTime = getnowTime();
        List<Param> param=new ArrayList<>();
        param.add(new Param("companyId",companyId));
        param.add(new Param("time_seq",time_seq));
        param.add(new Param("time", sign2Time+"|"+nowTime));
        param.add(new Param("orderInfo",orderInfo));
        param.add(new Param("Identification", "1"));
        String url = FXConstant.URL_UPDATE_OFFSEND;
        String str = "signature3";
        String str1 = "beforeService";
        String str2 = "afterService";
        OkHttpManager.getInstance().postThfile(str, files, param, str1, files1, str2, files2, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                if (mProgress!=null&&mProgress.isShowing()){
                    mProgress.dismiss();
                }
                String code = jsonObject.getString("code");
                if ("success".equals(code)){
                    reduceQjcount();
                    Toast.makeText(OfflineOrderActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                    tv_paidan_kehu_time.setText(nowTime);
                    tv_paidan_kehu_time.setVisibility(View.VISIBLE);
                    tev_paidan_kehu.setVisibility(View.VISIBLE);
                    tev_paidan_kehu.setText("完成");
                    tv_paidan_qiyeren.setEnabled(false);
                    iv_paidan_qiyeren.setEnabled(false);
                    tv_paidan_kehu.setEnabled(false);
                    iv_paidan_kehu.setEnabled(false);
                    et_feiyong.setEnabled(false);
                    et_miaoshu.setEnabled(false);
                    recyclerView.setEnabled(false);
                    recyclerView2.setEnabled(false);
                    btn_commit.setText("交易完成");
                    btn_commit.setEnabled(false);
                    btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                }else {
                    Toast.makeText(OfflineOrderActivity.this, "网络错误,提交失败！", Toast.LENGTH_SHORT).show();
                    Log.e("offlineorac,e2", jsonObject.toString());
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
                Toast.makeText(OfflineOrderActivity.this,"网络连接错误,派单失败",Toast.LENGTH_SHORT).show();
                if (errorMsg!=null) {
                    Log.e("offlineorac,e", errorMsg);
                }
            }
        });
    }

    private void showdialog() {
        final String orderDesc = TextUtils.isEmpty(et_miaoshu.getText().toString().trim())?"0":et_miaoshu.getText().toString().trim();
        final String infoPrice = TextUtils.isEmpty(et_feiyong.getText().toString().trim())?"0":et_feiyong.getText().toString().trim();
        final String companyId = DemoApplication.getInstance().getCurrentQiYeId();
        final String cusName = et_name.getText().toString().trim();
        final String cusPhone = et_phone.getText().toString().trim();
        final String cusAdress = et_dizhi.getText().toString().trim();
        LayoutInflater inflaterDl = LayoutInflater.from(OfflineOrderActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(OfflineOrderActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        TextView tv_tishi = (TextView) dialog.findViewById(R.id.tv_tishi);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        tv_item1.setText("派单给好友");
        tv_item2.setText("派单给员工");
        if ("0".equals(infoPrice)){
            tv_tishi.setVisibility(View.VISIBLE);
        }
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(OfflineOrderActivity.this, FriendActivity.class);
                intent.putExtra("biaoshi","01");
                intent.putExtra("filepath",filepath);
                intent.putExtra("orderDesc",orderDesc);
                intent.putExtra("companyId",companyId);
                intent.putExtra("cusName",cusName);
                intent.putExtra("cusPhone",cusPhone);
                intent.putExtra("cusAdress",cusAdress);
                intent.putExtra("infoPrice",infoPrice);
                intent.putStringArrayListExtra("imagePaths1",imagePaths1);
                intent.putStringArrayListExtra("imagePaths2",imagePaths2);
                startActivityForResult(intent,9);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(OfflineOrderActivity.this, QiYeYuGoActivity.class);
                intent.putExtra("orderId","操作员工");
                intent.putExtra("biaoshi","01");
                intent.putExtra("filepath",filepath);
                intent.putExtra("orderDesc",orderDesc);
                intent.putExtra("companyId",companyId);
                intent.putExtra("cusName",cusName);
                intent.putExtra("cusPhone",cusPhone);
                intent.putExtra("cusAdress",cusAdress);
                intent.putExtra("infoPrice",infoPrice);
                intent.putStringArrayListExtra("imagePaths1",imagePaths1);
                intent.putStringArrayListExtra("imagePaths2",imagePaths2);
                startActivityForResult(intent,10);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 删除图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener1 = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    goCamera(4,"01");
                    break;
                case 1:
                    // 删除图片
                    imagePaths1.remove(position);
                    selectMedia1.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };
    /**
     * 删除图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener2 = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    goCamera(4,"02");
                    break;
                case 1:
                    // 删除图片
                    imagePaths2.remove(position);
                    selectMedia2.remove(position);
                    adapter2.notifyItemRemoved(position);
                    break;
            }
        }
    };

    private void goCamera(int count,String type){
        List<LocalMedia> medias = new ArrayList<>();
        if ("01".equals(type)) {
            medias = selectMedia1;
        } else {
            medias = selectMedia2;
        }
        WeakReference<OfflineOrderActivity> reference = new WeakReference<OfflineOrderActivity>(OfflineOrderActivity.this);
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
//                            .setOffsetX() // 自定义裁剪比例
//                            .setOffsetY() // 自定义裁剪比例
                .setCompress(false) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(count) // 可选择图片的数量
                .setMinSelectNum(0)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(FunctionConfig.MODE_MULTIPLE) // 单选 or 多选
                .setShowCamera(false) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(false) // 是否打开剪切选项
                .setCircularCut(false)// 是否采用圆形裁剪
                .setPreviewVideo(true) // 是否预览视频(播放) mode or 多选有效
                .setCheckedBoxDrawable(0)
                .setRecordVideoDefinition(FunctionConfig.HIGH) // 视频清晰度
                .setRecordVideoSecond(60) // 视频秒数
                .setCustomQQ_theme(0)// 可自定义QQ数字风格，不传就默认是蓝色风格
                .setGif(false)// 是否显示gif图片，默认不显示
                .setCropW(0) // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
                .setCropH(0) // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
                .setMaxB(102400) // 压缩最大值 例如:200kb  就设置202400，202400 / 1024 = 200kb
                .setPreviewColor(ContextCompat.getColor(reference.get(), R.color.blue)) //预览字体颜色
                .setCompleteColor(ContextCompat.getColor(reference.get(), R.color.blue)) //已完成字体颜色
                .setPreviewBottomBgColor(0) //预览图片底部背景色
                .setPreviewTopBgColor(0)//预览图片标题背景色
                .setBottomBgColor(0) //图片列表底部背景色
                .setGrade(Luban.THIRD_GEAR) // 压缩档次 默认三档
                .setCheckNumMode(false)
                .setCompressQuality(100) // 图片裁剪质量,默认无损
                .setImageSpanCount(4) // 每行个数
                .setVideoS(0)// 查询多少秒内的视频 单位:秒
                .setSelectMedia(medias) // 已选图片，传入在次进去可选中，不能传入网络图片
                .setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
                .setCompressW(0) // 压缩宽 如果值大于图片原始宽高无效
                .setCompressH(0) // 压缩高 如果值大于图片原始宽高无效
                .setThemeStyle(ContextCompat.getColor(reference.get(), R.color.blue)) // 设置主题样式
                .setNumComplete(false) // 0/9 完成  样式
                .setClickVideo(false)// 点击声音
                .setFreeStyleCrop(false) // 裁剪是移动矩形框或是图片
                .create();
        if ("01".equals(type)) {
            // 只拍照
            PictureConfig.getInstance().init(options).openPhoto(reference.get(), resultCallback1);
        } else {
            // 先初始化参数配置，在启动相册
            PictureConfig.getInstance().init(options).openPhoto(reference.get(), resultCallback2);
        }
    }

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback1 = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            imagePaths1.clear();
            // 多选回调
            selectMedia1 = resultList;
            Log.i("callBack_result", selectMedia1.size() + "");
            LocalMedia media = resultList.get(0);
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                String path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                String path = media.getCompressPath();
            } else {
                // 原图地址
                String path = media.getPath();
            }
            if (selectMedia1 != null) {
                adapter.setList(selectMedia1);
                adapter.notifyDataSetChanged();
                for (int i=0;i<selectMedia1.size();i++){
                    imagePaths1.add(selectMedia1.get(i).getPath());
                }
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            imagePaths1.clear();
            // 单选回调
            selectMedia1.add(media);
            if (selectMedia1 != null) {
                adapter.setList(selectMedia1);
                adapter.notifyDataSetChanged();
                for (int i=0;i<selectMedia1.size();i++){
                    imagePaths1.add(selectMedia1.get(i).getPath());
                }
            }
        }
    };
    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback2 = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            imagePaths2.clear();
            // 多选回调
            selectMedia2 = resultList;
            Log.i("callBack_result", selectMedia2.size() + "");
            LocalMedia media = resultList.get(0);
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                String path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                String path = media.getCompressPath();
            } else {
                // 原图地址
                String path = media.getPath();
            }
            if (selectMedia2 != null) {
                adapter2.setList(selectMedia2);
                adapter2.notifyDataSetChanged();
                for (int i=0;i<selectMedia2.size();i++){
                    imagePaths2.add(selectMedia2.get(i).getPath());
                }
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            imagePaths2.clear();
            // 单选回调
            selectMedia2.add(media);
            if (selectMedia2 != null) {
                adapter2.setList(selectMedia2);
                adapter2.notifyDataSetChanged();
                for (int i=0;i<selectMedia2.size();i++){
                    imagePaths2.add(selectMedia2.get(i).getPath());
                }
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 5:
                    if (data!=null){
                        filepath = data.getStringExtra("xdimageName");
                        tv_paidan_qiye.setVisibility(View.INVISIBLE);
                        iv_paidan_qiye.setVisibility(View.VISIBLE);
                        iv_paidan_qiye.setImageURI(Uri.fromFile(new File(filepath)));
                    }
                    break;
                case 6:
                    if (data!=null){
                        filepath = data.getStringExtra("xdimageName");
                        tv_paidan_qiyeren.setVisibility(View.INVISIBLE);
                        iv_paidan_qiyeren.setVisibility(View.VISIBLE);
                        iv_paidan_qiyeren.setImageURI(Uri.fromFile(new File(filepath)));
                    }
                    break;
                case 7:
                    if (data!=null){
                        filepath = data.getStringExtra("xdimageName");
                        tv_paidan_kehu.setVisibility(View.INVISIBLE);
                        iv_paidan_kehu.setVisibility(View.VISIBLE);
                        iv_paidan_kehu.setImageURI(Uri.fromFile(new File(filepath)));
                    }
                    break;
                case 9:
                    finish();
                    break;
                case 10:
                    finish();
                    break;
            }
        }
    }

    private void initView() {
        btn_commit = (Button) findViewById(R.id.btn_commit);
        et_miaoshu = (EditText) findViewById(R.id.et_miaoshu);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_dizhi = (EditText) findViewById(R.id.et_dizhi);
        et_feiyong = (EditText) findViewById(R.id.et_feiyong);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tev_paidan_qiye = (TextView) findViewById(R.id.tev_paidan_qiye);
        tev_paidan_qiyeren = (TextView) findViewById(R.id.tev_paidan_qiyeren);
        tev_paidan_kehu = (TextView) findViewById(R.id.tev_paidan_kehu);
        tv_paidan_qiye = (TextView) findViewById(R.id.tv_paidan_qiye);
        tv_paidan_qiye_time = (TextView) findViewById(R.id.tv_paidan_qiye_time);
        tv_paidan_qiyeren = (TextView) findViewById(R.id.tv_paidan_qiyeren);
        tv_paidan_qiyeren_time = (TextView) findViewById(R.id.tv_paidan_qiyeren_time);
        tv_paidan_caiwu = (TextView) findViewById(R.id.tv_paidan_caiwu);
        tv_paidan_caiwu_time = (TextView) findViewById(R.id.tv_paidan_caiwu_time);
        tv_paidan_kehu = (TextView) findViewById(R.id.tv_paidan_kehu);
        tv_paidan_kehu_time = (TextView) findViewById(R.id.tv_paidan_kehu_time);
        tv_feiyong = (TextView) findViewById(R.id.tv_feiyong);
        rl_xinxi2 = (RelativeLayout) findViewById(R.id.rl_xinxi2);
        rl_miaoshu = (RelativeLayout) findViewById(R.id.rl_miaoshu);
        iv_paidan_qiye = (ImageView) findViewById(R.id.iv_paidan_qiye);
        iv_paidan_qiyeren = (ImageView) findViewById(R.id.iv_paidan_qiyeren);
        iv_paidan_caiwu = (ImageView) findViewById(R.id.iv_paidan_caiwu);
        iv_jiantou = (ImageView) findViewById(R.id.iv_jiantou);
        iv_paidan_kehu = (ImageView) findViewById(R.id.iv_paidan_kehu);
    }

    private String getnowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(date);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("sangu_chongzhi_zhuangtai", Context.MODE_PRIVATE);
        String zhuangtai = sp.getString("zhuangtai","失败");
        if ("成功".equals(zhuangtai)){
            Toast.makeText(OfflineOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            if (sp!=null) {
                SharedPreferences.Editor editor = sp.edit();
                if (editor!=null) {
                    editor.clear();
                    editor.commit();
                }
            }
            uploadFile();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imagePaths1!=null){
            imagePaths1.clear();
            imagePaths1=null;
        }
        if (selectMedia1!=null){
            selectMedia1.clear();
            selectMedia1=null;
        }
        if (selectMedia2!=null){
            selectMedia2.clear();
            selectMedia2=null;
        }
        if (imagePaths2!=null){
            imagePaths2.clear();
            imagePaths2=null;
        }
    }

    @Override
    public void updateCurrentPrice(Object success) {
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }

}
