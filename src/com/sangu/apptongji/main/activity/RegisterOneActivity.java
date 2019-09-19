package com.sangu.apptongji.main.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.LimitInputTextWatcher;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-09-11.
 */

public class RegisterOneActivity extends BaseActivity implements View.OnClickListener{
    public static RegisterOneActivity instance = null;
    private List<String> list = new ArrayList<>();
    private ImageView iv_touxiang;
    private RelativeLayout rl_touxiang;
    private EditText et_name;
    private EditText et_zhuanye;
    private EditText et_birth;
    private RadioGroup mRgGender;
    private RadioButton mRbMale;
    private RadioButton mRbFemale;
    private Button btn_next;
    private String filePath,sex;
    private int age=0;
    private boolean isNoZY = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_register_one);
        instance = this;
        MySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sp = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        if (!sp.contains("isLogedIn")) {
            bundAndroidId();
        }
        initView();
        setListener();
    }

    private void bundAndroidId() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date curDate = new Date(System.currentTimeMillis());
        final String date = sDateFormat.format(curDate);
        final String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        String url = FXConstant.URL_INSERT_READHIS;
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
                param.put("deviceStr","android"+ANDROID_ID);
                param.put("resv2",date);
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void setListener() {
        et_name.addTextChangedListener(new LimitInputTextWatcher(et_name));
        et_zhuanye.addTextChangedListener(new LimitInputTextWatcher(et_zhuanye));
        et_birth.setFocusable(false);
        et_birth.setFocusableInTouchMode(false);

        et_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterOneActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy");
                        monthOfYear = monthOfYear+1;
                        int NowYear = Integer.parseInt(format.format(new Date()));
                        age = NowYear - year;
                        if (age<=5){
                            Toast.makeText(RegisterOneActivity.this, "选择的年龄不合法,请从新选择！", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String birth = year+"-"+monthOfYear+"-"+dayOfMonth;
                        et_birth.setText(birth);
                    }
                }, 1990, 1, 1).show();
            }
        });

        rl_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCamera();
            }
        });

        iv_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCamera();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                String zhuaYe = et_zhuanye.getText().toString().trim();
                String birth = et_birth.getText().toString().trim();
                if (TextUtils.isEmpty(name)||name.length()>10||name.length()<2){
                    Toast.makeText(getApplicationContext(),"请输入姓名,姓名不能少于2个字,不能超过10个字",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.startsWith("我")||name.startsWith("小")||name.startsWith("大")||name.startsWith("啊")||name.startsWith("老")){
                    Toast.makeText(getApplicationContext(),"商务社交,诚信为本,请使用真实姓名",Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < list.size(); i++) {
                    String x = list.get(i);  //x为敏感词汇
                    if (name.contains(x)){
                        Toast.makeText(getApplicationContext(),"商务社交,诚信为本，请使用真实姓名",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (!TextUtils.isEmpty(zhuaYe)) {
                    if (zhuaYe.equalsIgnoreCase("无")) {
                        isNoZY = true;
                    } else {
                        isNoZY = false;
                        if (zhuaYe.length() > 6) {
                            Toast.makeText(getApplicationContext(), "请输入专业,专业不能超过6个字", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    isNoZY = false;
                    Toast.makeText(getApplicationContext(),"请输入专业,专业不能超过6个字",Toast.LENGTH_SHORT).show();
                    return;
                }


                if (filePath==null||"".equals(filePath)||!new File(filePath).exists()){
                    Toast.makeText(getApplicationContext(),"请选择头像",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(birth)){
                    Toast.makeText(getApplicationContext(),"请选择生日",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mRbMale.isChecked()&&!mRbFemale.isChecked()){
                    Toast.makeText(getApplicationContext(),"请选择性别",Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(RegisterOneActivity.this,RegisterTwoActivity.class)
                .putExtra("name",name).putExtra("zhuaYe",zhuaYe).putExtra("birth",birth)
                .putExtra("sex",sex).putExtra("filePath",filePath).putExtra("age",age+"").putExtra("isNoZY",isNoZY));
            }
        });
    }

    private void initView() {
        rl_touxiang = (RelativeLayout) findViewById(R.id.rl_touxiang);
        iv_touxiang = (ImageView) findViewById(R.id.iv_touxiang);
        et_name = (EditText) findViewById(R.id.et_name);
        et_zhuanye = (EditText) findViewById(R.id.et_zhuanye);
        et_birth = (EditText) findViewById(R.id.et_birth);
        btn_next = (Button) findViewById(R.id.btn_next);
        mRgGender = (RadioGroup) findViewById(R.id.reg_baseinfo_rg_gender);
        mRbMale = (RadioButton) findViewById(R.id.reg_baseinfo_rb_male);
        mRbFemale = (RadioButton) findViewById(R.id.reg_baseinfo_rb_female);
        mRbMale.setOnClickListener(this);
        mRbFemale.setOnClickListener(this);
        list.add("师傅");
        list.add("经理");
        list.add("公司");
        list.add("企业");
        list.add("先生");
        list.add("小姐");
        list.add("女士");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_baseinfo_rb_male:
                mRbMale.setChecked(true);
                mRbFemale.setChecked(false);
                sex = "01";
                break;

            case R.id.reg_baseinfo_rb_female:
                mRbMale.setChecked(false);
                mRbFemale.setChecked(true);
                sex = "00";
                break;
        }
    }

    private void goCamera(){
        WeakReference<RegisterOneActivity> reference = new WeakReference<RegisterOneActivity>(RegisterOneActivity.this);
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(FunctionConfig.CROP_MODEL_1_1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
//                            .setOffsetX() // 自定义裁剪比例
//                            .setOffsetY() // 自定义裁剪比例
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(1) // 可选择图片的数量
                .setMinSelectNum(0)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(FunctionConfig.MODE_SINGLE) // 单选 or 多选
                .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(true) // 是否打开剪切选项
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
            filePath = media.getCompressPath();
            if (filePath!=null) {
                iv_touxiang.setImageURI(Uri.fromFile(new File(filePath)));
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode == FunctionConfig.CAMERA_RESULT){
                if (data != null) {
                    List<LocalMedia> medias = (List<LocalMedia>) data.getSerializableExtra(FunctionConfig.EXTRA_RESULT);
                    if (medias != null) {
                        filePath = medias.get(0).getCompressPath();
                        if (filePath!=null) {
                            iv_touxiang.setImageURI(Uri.fromFile(new File(filePath)));
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

}
