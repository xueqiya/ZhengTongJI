package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.fanxin.easeui.EaseConstant;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;
import com.yalantis.ucrop.entity.LocalMedia;

import org.json.JSONException;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017-04-05.
 */

public class CertificationActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_name=null;
    private EditText et_shenfenzheng=null;
    private EditText et_yinhangka=null;
    private EditText et_phone=null;
    private EditText et_kaihuhang=null;
    private ImageView iv_zhengmian=null;
    private ImageView iv_hand_zhengmian=null;
    private ImageView iv_hand_del2=null;
    private ImageView iv_hand_fanmian=null;
    private ImageView iv_hand_del1=null;
    private ImageView iv_del1=null;
    private ImageView iv_fanmian=null;
    private ImageView iv_del2=null;
    private TextView tv_zhuangtai=null;
    private TextView tv_error=null;
    private TextView tv_xiugai=null;
    private TextView tv_kefu=null;
    private Button btn_commit=null;
    String path1=null,path2=null,path3=null,path4=null;
    private int zhengfan;
    private CustomProgressDialog mProgress=null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgress!=null){
            if (mProgress.isShowing()){
                mProgress.dismiss();
            }
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_shiming_renzheng);
        WeakReference<CertificationActivity> reference =  new WeakReference<CertificationActivity>(CertificationActivity.this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        mProgress.show();
        initView();
        setlistener();
        queryRenzheng();
    }

    private void queryRenzheng() {
        String url = FXConstant.URL_CHAXUN_RENZHENG;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
                Log.d("chen", "onResponse" + s);
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("SUCCESS")){
                        org.json.JSONObject object1 = object.getJSONObject("list");
                        if (object1!=null&&!"".equals(object1)){
                            String userName = object1.getString("userName");
                            String uId = object1.getString("uId");
                            String bankCard = object1.getString("bankCard");
                            String phoneNumber = object1.getString("phoneNumber");
                            String bankName = object1.getString("bankName");
                            String image1 = object1.getString("image1");
                            String image2 = object1.getString("image2");
                            String image3 = object1.getString("image3");
                            String image4 = object1.getString("image4");
                            String id = object1.getString("id");
                            String examine = object1.getString("examine");
                            String reason = object1.getString("reason");
                            if ("正在审核".equals(examine)||"审核通过".equals(examine)) {
                                et_name.setText(userName);
                                et_shenfenzheng.setText(id);
                              //  et_yinhangka.setText(bankCard);
                                et_phone.setText(phoneNumber);
                              //  et_kaihuhang.setText(bankName);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_RENZHENG_TUPIAN+image1,iv_zhengmian);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_RENZHENG_TUPIAN+image2,iv_fanmian);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_RENZHENG_TUPIAN+image3,iv_hand_zhengmian);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_RENZHENG_TUPIAN+image4,iv_hand_fanmian);
                                btn_commit.setVisibility(View.INVISIBLE);
                                tv_zhuangtai.setVisibility(View.VISIBLE);
                                tv_zhuangtai.setText("审核状态："+examine);
                                et_name.setEnabled(false);
                              //  et_kaihuhang.setEnabled(false);
                                et_shenfenzheng.setEnabled(false);
                               // et_yinhangka.setEnabled(false);
                                et_phone.setEnabled(false);
                                btn_commit.setEnabled(false);
                                iv_zhengmian.setEnabled(false);
                                iv_fanmian.setEnabled(false);
                                iv_hand_zhengmian.setEnabled(false);
                                iv_hand_fanmian.setEnabled(false);
                            }else {
                                et_name.setText(userName);
                                et_shenfenzheng.setText(id);
                              //  et_yinhangka.setText(bankCard);
                                et_phone.setText(phoneNumber);
                              //  et_kaihuhang.setText(bankName);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_RENZHENG_TUPIAN+image1,iv_zhengmian);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_RENZHENG_TUPIAN+image2,iv_fanmian);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_RENZHENG_TUPIAN+image3,iv_hand_zhengmian);
                                ImageLoader.getInstance().displayImage(FXConstant.URL_RENZHENG_TUPIAN+image4,iv_hand_fanmian);
                                btn_commit.setVisibility(View.INVISIBLE);
                                tv_zhuangtai.setVisibility(View.VISIBLE);
                                tv_zhuangtai.setText("审核状态："+examine);
                                tv_error.setText("失败原因：" + reason);
                                tv_error.setVisibility(View.VISIBLE);
                                tv_xiugai.setVisibility(View.VISIBLE);
                                tv_xiugai.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        updateRenzheng();
                                    }
                                });
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null) {
                    Log.e("cerfication", volleyError.getMessage());
                    Log.d("chen", "cerfication" + volleyError.getMessage());
                }
                Toast.makeText(CertificationActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(CertificationActivity.this).addToRequestQueue(request);
    }

    private void updateRenzheng() {
        String shenfenzheng = et_shenfenzheng.getText().toString().trim();
       // String yinhangka = et_yinhangka.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
       // String kaihuhang = et_kaihuhang.getText().toString().trim();
        String name = et_name.getText().toString().trim();

        if (TextUtils.isEmpty(shenfenzheng)) {
            ToastUtils.showNOrmalToast(CertificationActivity.this.getApplicationContext(),"身份证不能为空");
            return;
        }
//        if (TextUtils.isEmpty(yinhangka)) {
//            ToastUtils.showNOrmalToast(CertificationActivity.this.getApplicationContext(),"银行卡不能为空");
//            return;
//        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showNOrmalToast(CertificationActivity.this.getApplicationContext(),"手机号不能为空");
            return;
        }
//        if (TextUtils.isEmpty(kaihuhang)) {
//            ToastUtils.showNOrmalToast(CertificationActivity.this.getApplicationContext(),"开户行不能为空");
//            return;
//        }
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showNOrmalToast(CertificationActivity.this.getApplicationContext(),"名字不能为空");
            return;
        }
        List<Param> param=new ArrayList<>();
        param.add(new Param("userName",name));
        param.add(new Param("examine","正在审核"));
        param.add(new Param("ID", shenfenzheng));
       // param.add(new Param("bankCard",yinhangka));
        param.add(new Param("phoneNumber", phone));
       // param.add(new Param("bankName", kaihuhang));
        param.add(new Param("uId", DemoHelper.getInstance().getCurrentUsernName()));
        Log.d("chen", name + shenfenzheng  + phone + DemoHelper.getInstance().getCurrentUsernName() + "");
        final List<File> files1 = new ArrayList<File>();
        if (path1!=null&&!"".equals(path1)) {
            File file1 = null;
            file1 = new File(path1);
            if (file1.exists()) {
                files1.add(file1);
            }
        }
        final List<File> files2 = new ArrayList<File>();
        if (path2!=null&&!"".equals(path2)) {
            File file = null;
            file = new File(path2);
            if (file.exists()) {
                files2.add(file);
            }
        }
        final List<File> files3 = new ArrayList<File>();
        if (path3!=null&&!"".equals(path3)) {
            File file3 = null;
            file3 = new File(path3);
            if (file3.exists()) {
                files3.add(file3);
            }
        }
        final List<File> files4 = new ArrayList<File>();
        if (path4!=null&&!"".equals(path4)) {
            File file4 = null;
            file4 = new File(path4);
            if (file4.exists()) {
                files4.add(file4);
            }
        }
        String str1 = "image1";
        String str2 = "image2";
        String str3 = "image3";
        String str4 = "image4";
        OkHttpManager.getInstance().postTfile(param,str1,files1,str2,files2,str3,files3,str4,files4, FXConstant.URL_XIUGAI_RENZHENG,new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("chen", "updateRenzheng onResponse" + jsonObject.toString());
                String code = jsonObject.getString("code");
                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
                if (code.equals("SUCCESS")) {
                    Toast.makeText(CertificationActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    btn_commit.setVisibility(View.INVISIBLE);
                    tv_zhuangtai.setVisibility(View.VISIBLE);
                    tv_zhuangtai.setText("审核状态：正在审核");
                    et_name.setEnabled(false);
                    et_shenfenzheng.setEnabled(false);
                //    et_yinhangka.setEnabled(false);
                    et_phone.setEnabled(false);
                 //   et_kaihuhang.setEnabled(false);
                    iv_fanmian.setEnabled(false);
                    iv_hand_fanmian.setEnabled(false);
                    iv_zhengmian.setEnabled(false);
                    iv_hand_zhengmian.setEnabled(false);
                    tv_xiugai.setVisibility(View.INVISIBLE);
                } else {
                    Log.e("参数错误信息","code:"+code);
                    Toast.makeText(CertificationActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(String errorMsg) {
                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
                Log.d("chen", "updateRenzheng onFailure" + errorMsg);
                Log.e("参数错误信息",errorMsg);
                Toast.makeText(CertificationActivity.this, "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setlistener() {
        TextChange textChange = new TextChange();
        et_shenfenzheng.addTextChangedListener(textChange);
        et_phone.addTextChangedListener(textChange);
      //  et_yinhangka.addTextChangedListener(textChange);
     //   et_kaihuhang.addTextChangedListener(textChange);
        iv_zhengmian.setOnClickListener(this);
        iv_hand_zhengmian.setOnClickListener(this);
        iv_fanmian.setOnClickListener(this);
        iv_hand_fanmian.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        iv_del1.setOnClickListener(this);
        iv_hand_del1.setOnClickListener(this);
        iv_del2.setOnClickListener(this);
        iv_hand_del2.setOnClickListener(this);
        tv_kefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent11 = new Intent(CertificationActivity.this, ChatActivity.class);
                intent11.putExtra("userId", "18337101357");
                intent11.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                intent11.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                intent11.putExtra(EaseConstant.EXTRA_USER_IMG,"zhengshiduo.png");
                intent11.putExtra(EaseConstant.EXTRA_USER_NAME,"正事多客服");
                intent11.putExtra(EaseConstant.EXTRA_USER_SHARERED,"无");
                startActivity(intent11);
            }
        });
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_shenfenzheng = (EditText) findViewById(R.id.et_shenfenzheng);
      //  et_yinhangka = (EditText) findViewById(R.id.et_yinhangka);
        et_phone = (EditText) findViewById(R.id.et_phone);
      //  et_kaihuhang = (EditText) findViewById(R.id.et_kaihuhang);
        iv_zhengmian = (ImageView) findViewById(R.id.iv_zhengmian);
        iv_hand_zhengmian = (ImageView) findViewById(R.id.iv_hand_zhengmian);
        iv_hand_del2 = (ImageView) findViewById(R.id.iv_hand_del2);
        iv_hand_fanmian = (ImageView) findViewById(R.id.iv_hand_fanmian);
        iv_hand_del1 = (ImageView) findViewById(R.id.iv_hand_del1);
        iv_del1 = (ImageView) findViewById(R.id.iv_del1);
        iv_del2 = (ImageView) findViewById(R.id.iv_del2);
        iv_fanmian = (ImageView) findViewById(R.id.iv_fanmian);
        tv_zhuangtai = (TextView) findViewById(R.id.tv_zhuangtai);
        tv_error = (TextView) findViewById(R.id.tv_error);
        tv_xiugai = (TextView) findViewById(R.id.tv_xiugai);
        tv_kefu = (TextView) findViewById(R.id.tv_kefu);
        btn_commit = (Button) findViewById(R.id.btn_commit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_del1:
                iv_zhengmian.setImageResource(R.drawable.fx_icon_add);
                iv_del1.setVisibility(View.GONE);
                break;
            case R.id.iv_del2:
                iv_fanmian.setImageResource(R.drawable.fx_icon_add);
                iv_del2.setVisibility(View.GONE);
                break;
            case R.id.iv_hand_del1:
                iv_hand_zhengmian.setImageResource(R.drawable.fx_icon_add);
                iv_hand_del1.setVisibility(View.GONE);
                break;
            case R.id.iv_hand_del2:
                iv_hand_fanmian.setImageResource(R.drawable.fx_icon_add);
                iv_hand_del2.setVisibility(View.GONE);
                break;
            case R.id.iv_zhengmian:
                zhengfan = 1;
                goCamera();
                break;
            case R.id.iv_fanmian:
                zhengfan = 2;
                goCamera();
                break;
            case R.id.iv_hand_zhengmian:
                zhengfan = 3;
                goCamera();
                break;
            case R.id.iv_hand_fanmian:
                zhengfan = 4;
                goCamera();
                break;
            case R.id.btn_commit:
                if (path1==null||"".equals(path1)){
                    Toast.makeText(CertificationActivity.this,"请选择正面身份证!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (path2==null||"".equals(path2)){
                    Toast.makeText(CertificationActivity.this,"请选择反面身份证!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (path3==null||"".equals(path3)){
                    Toast.makeText(CertificationActivity.this,"请选择手持正面身份证!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (path4==null||"".equals(path4)){
                    Toast.makeText(CertificationActivity.this,"请选择手持反面身份证!",Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater2 = LayoutInflater.from(CertificationActivity.this);
                RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                final Dialog dialog2 = new AlertDialog.Builder(CertificationActivity.this).create();
                dialog2.show();
                dialog2.getWindow().setContentView(layout2);
                dialog2.setCanceledOnTouchOutside(true);
                dialog2.setCancelable(true);
                TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                btnOK2.setText("确定");
                btnCancel2.setText("取消");
                title_tv2.setText("确认提交认证吗？");
                btnCancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                btnOK2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                        if (mProgress!=null){
                            if (!mProgress.isShowing()){
                                mProgress.setMessage("正在提交...");
                                mProgress.show();
                            }
                        }
                        tijiao();
                    }
                });
                break;
        }
    }

    private void goCamera(){
        WeakReference<CertificationActivity> reference = new WeakReference<CertificationActivity>(CertificationActivity.this);
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
//                            .setOffsetX() // 自定义裁剪比例
//                            .setOffsetY() // 自定义裁剪比例
                .setCompress(false) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(8) // 可选择图片的数量
                .setMinSelectNum(0)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(FunctionConfig.MODE_SINGLE) // 单选 or 多选
                .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
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
                .setSelectMedia(null) // 已选图片，传入在次进去可选中，不能传入网络图片
                .setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
                .setCompressW(0) // 压缩宽 如果值大于图片原始宽高无效
                .setCompressH(0) // 压缩高 如果值大于图片原始宽高无效
                .setThemeStyle(ContextCompat.getColor(reference.get(), R.color.blue)) // 设置主题样式
                .setNumComplete(false) // 0/9 完成  样式
                .setClickVideo(false)// 点击声音
                .setFreeStyleCrop(false) // 裁剪是移动矩形框或是图片
                .create();
        // 先初始化参数配置，在启动相册
        PictureConfig.getInstance().init(options).openPhoto(reference.get(), resultCallback);
    }

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {

        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            // 单选回调
            if (zhengfan==2){
                path2 = media.getPath();
                File file2 = new File(path2);
                Glide.with(CertificationActivity.this).load(file2).into(iv_fanmian);
                //iv_fanmian.setImageURI(Uri.fromFile(new File(path2)));
                iv_del2.setVisibility(View.VISIBLE);
            }else if(zhengfan == 1) {
                path1 = media.getPath();
                File file2 = new File(path1);
                Glide.with(CertificationActivity.this).load(file2).into(iv_zhengmian);
                //iv_zhengmian.setImageURI(Uri.fromFile(new File(path1)));
                iv_del1.setVisibility(View.VISIBLE);
            }else if(zhengfan == 3) {
                //手持正面
                path3 = media.getPath();
                File file2 = new File(path3);
                Glide.with(CertificationActivity.this).load(file2).into(iv_hand_zhengmian);
                //iv_hand_zhengmian.setImageURI(Uri.fromFile(new File(path3)));
                iv_hand_del1.setVisibility(View.VISIBLE);
            } else if(zhengfan == 4) {
                path4 = media.getPath();
                File file2 = new File(path4);
                Glide.with(CertificationActivity.this).load(file2).into(iv_hand_fanmian);
               // iv_hand_fanmian.setImageURI(Uri.fromFile(new File(path4)));
                iv_hand_del2.setVisibility(View.VISIBLE);
            }
        }
    };

    private void tijiao() {
        String shenfenzheng = et_shenfenzheng.getText().toString().trim();
       // String yinhangka = et_yinhangka.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
      //  String kaihuhang = et_kaihuhang.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(shenfenzheng)) {
            ToastUtils.showNOrmalToast(CertificationActivity.this.getApplicationContext(),"身份证不能为空");
            return;
        }
//        if (TextUtils.isEmpty(yinhangka)) {
//            ToastUtils.showNOrmalToast(CertificationActivity.this.getApplicationContext(),"银行卡不能为空");
//            return;
//        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showNOrmalToast(CertificationActivity.this.getApplicationContext(),"手机号不能为空");
            return;
        }
//        if (TextUtils.isEmpty(kaihuhang)) {
//            ToastUtils.showNOrmalToast(CertificationActivity.this.getApplicationContext(),"开户行不能为空");
//            return;
//        }
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showNOrmalToast(CertificationActivity.this.getApplicationContext(),"名字不能为空");
            return;
        }
        List<Param> param=new ArrayList<>();
        param.add(new Param("userName",name));
        param.add(new Param("ID", shenfenzheng));
      //  param.add(new Param("bankCard",yinhangka));
        param.add(new Param("phoneNumber", phone));
      //  param.add(new Param("bankName", kaihuhang));
        param.add(new Param("uId", DemoHelper.getInstance().getCurrentUsernName()));
        final List<File> files1 = new ArrayList<File>();
        File file1 = null;
        file1 = new File(path1);
        if (file1.exists()) {
            files1.add(file1);
        }
        final List<File> files2 = new ArrayList<File>();
        File file = null;
        file = new File(path2);
        if (file.exists()) {
            files2.add(file);
        }

        final List<File> files3 = new ArrayList<File>();
        if (path3!=null&&!"".equals(path3)) {
            File file3 = null;
            file3 = new File(path3);
            if (file3.exists()) {
                files3.add(file3);
            }
        }
        final List<File> files4 = new ArrayList<File>();
        if (path4!=null&&!"".equals(path4)) {
            File file4 = null;
            file4 = new File(path4);
            if (file4.exists()) {
                files4.add(file4);
            }
        }
        String str1 = "image1";
        String str2 = "image2";
        String str3 = "image3";
        String str4 = "image4";
        if (files1.size()>0&&files2.size()>0&&files3.size()>0&&files4.size()>0) {
            OkHttpManager.getInstance().postTfile(param,str1,files1,str2,files2,str3,files3,str4,files4, FXConstant.URL_SHIMING_RENZHENG,new OkHttpManager.HttpCallBack() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Log.d("chen", "提交" + jsonObject.toString());
                    String code = jsonObject.getString("code");
                    if (mProgress!=null){
                        if (mProgress.isShowing()){
                            mProgress.dismiss();
                        }
                    }
                    if (code.equals("SUCCESS")) {
                        Toast.makeText(CertificationActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        btn_commit.setVisibility(View.INVISIBLE);
                        tv_zhuangtai.setVisibility(View.VISIBLE);
                      //  tv_error.setVisibility(View.VISIBLE);
                        et_name.setEnabled(false);
                        et_shenfenzheng.setEnabled(false);
                     //   et_yinhangka.setEnabled(false);
                        et_phone.setEnabled(false);
                     //   et_kaihuhang.setEnabled(false);
                        iv_fanmian.setEnabled(false);
                        iv_zhengmian.setEnabled(false);
                        iv_hand_fanmian.setEnabled(false);
                        iv_hand_zhengmian.setEnabled(false);
                    } else {
                        Log.e("参数错误信息","code:"+code);
                        Toast.makeText(CertificationActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(String errorMsg) {
                    if (mProgress!=null){
                        if (mProgress.isShowing()){
                            mProgress.dismiss();
                        }
                    }
                    Log.d("chen", "提交onFailure" + errorMsg);
                    Log.e("参数错误信息",errorMsg);
                    Toast.makeText(CertificationActivity.this, "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            if (mProgress!=null){
                if (mProgress.isShowing()){
                    mProgress.dismiss();
                }
            }
            Toast.makeText(CertificationActivity.this, "必须先上传身份证正反面和手持身份证正反面！" , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FunctionConfig.CAMERA_RESULT:
                    if (data != null) {
                        List<LocalMedia> medias = (List<LocalMedia>) data.getSerializableExtra(FunctionConfig.EXTRA_RESULT);
                        if (zhengfan==2){
                            path2 = medias.get(0).getPath();
                            File file2 = new File(path2);
                            Glide.with(CertificationActivity.this).load(file2).into(iv_fanmian);
                            //iv_fanmian.setImageURI(Uri.fromFile(new File(path2)));
                        }else if (zhengfan == 1){
                            path1 = medias.get(0).getPath();
                            File file2 = new File(path1);
                            Glide.with(CertificationActivity.this).load(file2).into(iv_zhengmian);
                            //iv_zhengmian.setImageURI(Uri.fromFile(new File(path1)));
                        }else if (zhengfan == 3){
                            path3 = medias.get(0).getPath();
                            File file2 = new File(path3);
                            Glide.with(CertificationActivity.this).load(file2).into(iv_hand_zhengmian);
                            //iv_hand_zhengmian.setImageURI(Uri.fromFile(new File(path3)));
                        }else if (zhengfan == 4){
                            path4 = medias.get(0).getPath();
                            File file2 = new File(path4);
                            Glide.with(CertificationActivity.this).load(file2).into(iv_hand_fanmian);
                            //iv_hand_fanmian.setImageURI(Uri.fromFile(new File(path4)));
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    // EditText监听器
    class TextChange implements TextWatcher {
        @Override
        public void afterTextChanged(Editable arg0) {

        }
        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
        }
        @Override
        public void onTextChanged(CharSequence cs, int start, int before,
                                  int count) {
            boolean Sign1=false,Sign2 = false,Sign3=false;
            String shenfenzheng = et_shenfenzheng.getText().toString().trim();
          //  String yinhangka = et_yinhangka.getText().toString().trim();
            String phone = et_phone.getText().toString().trim();
            try {
                if (shenfenzheng.length()>14) {
                    Sign1 = IDCardValidate(shenfenzheng);
                }
//                if (yinhangka.length()>14) {
//                    Sign2 = checkBankCard(yinhangka);
//                }
                if (phone.length() == 11) {
                    Sign3 = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("chen", Sign1 + "" + Sign2+ "" + Sign3 + "");
            if (Sign1 & Sign3) {
                btn_commit.setEnabled(true);
                btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_green);
            }
            // 在layout文件中，对Button的text属性应预先设置默认值，否则刚打开程序的时候Button是无显示的
            else {
                btn_commit.setEnabled(false);
                btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            }
        }

    }
    /**
     * 判断是否是银行卡号
     * @param cardId
     * @return
     */
    private boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        Log.e("身份证是否有错误,bit",bit+"");
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }//
    private char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 判断是否是手机号
     * @param phone
     * @return
     */
    private boolean checkPhone(String phone) {
        Pattern pattern = Pattern
                .compile("^(13[0-9]|15[0-9]|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr
     *            身份证号
     * @return true 有效：false 无效
     * @throws ParseException
     */
    public static boolean IDCardValidate(String IDStr) throws ParseException {
        String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        String Ai = "";
        // ================ 号码的长度18位 ================
        if (IDStr.length() != 18) {
            return false;
        }
        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        }
        if (isNumeric(Ai) == false) {
            //errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            return false;
        }
        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 日
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
//          errorInfo = "身份证生日无效。";
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                //errorInfo = "身份证生日不在有效范围。";
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            //errorInfo = "身份证月份无效";
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            //errorInfo = "身份证日期无效";
            return false;
        }
        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            //errorInfo = "身份证地区编码错误。";
            return false;
        }
        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                //errorInfo = "身份证无效，不是合法的身份证号码";
                return false;
            }
        } else {
            return true;
        }
        return true;
    }

    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    @SuppressWarnings("unchecked")
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
//      hashtable.put("71", "台湾");
//      hashtable.put("81", "香港");
//      hashtable.put("82", "澳门");
//      hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 功能：判断字符串是否为日期格式
     *
     * @param
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 功能：身份证的有效验证
     *
     * @param IDStr
     *            身份证号
     * @return 有效：返回"" 无效：返回String信息
     * @throws ParseException
     *//*
    private boolean IDCardValidate(String IDStr) throws ParseException {
        String errorInfo = "正确";// 记录错误信息
        String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2" };
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            Log.e("身份证是否有错误",errorInfo);
            return false;
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
            Log.e("身份证是否有错误",errorInfo);
            return false;
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            Log.e("身份证是否有错误",errorInfo);
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                errorInfo = "身份证生日不在有效范围。";
                Log.e("身份证是否有错误",errorInfo);
                return false;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            Log.e("身份证是否有错误",errorInfo);
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            Log.e("身份证是否有错误",errorInfo);
            return false;
        }
        // =====================(end)=====================
        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            Log.e("身份证是否有错误",errorInfo);
            return false;
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                Log.e("身份证是否有错误",errorInfo);
                return false;
            }
        } else {
            Log.e("身份证是否有错误,1",errorInfo);
            return true;
        }
        // =====================(end)=====================
        Log.e("身份证是否有错误,2",errorInfo);
        return true;
    }
    *//**
     * 功能：判断字符串是否为数字
     *
     * @param str
     * @return
     *//*
    private boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    *//**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     *//*
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    *//**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     *//*
    private boolean isDataFormat(String str) {
        boolean flag = false;
        // String
        // regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }*/
}
