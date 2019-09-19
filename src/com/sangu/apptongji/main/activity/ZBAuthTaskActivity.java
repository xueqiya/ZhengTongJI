package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.SoundPlayUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;
import com.yalantis.ucrop.entity.LocalMedia;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZBAuthTaskActivity extends BaseActivity implements IUAZView {


    private TextView tv_pushComent,tv_midBtn;
    private ImageView commentImage;
    private IUAZPresenter uazPresenter;

    private ArrayList<String> imagePaths1=new ArrayList<>();
    private ArrayList<String> imagePaths2=new ArrayList<>();
    private List<LocalMedia> selectMedia1 = new ArrayList<>();
    private List<LocalMedia> selectMedia2 = new ArrayList<>();

    private Dialog mWeiboDialog;

    private LinearLayout ll_fristType,ll_secondType,ll_thridType;

    private TextView tv_pushDown,tv_midBtn2,tv_midBtn3;
    private ImageView image_downImage1,image_downImage2,wechatImage;

    private String currentTag="";//
    private TextView tv_example;

    private TextView tv_searchKey;
    private ImageView image_logo;

    private String keyWord="";

    private String wechatAuth = "0";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_zbauthtask);

        ll_fristType = (LinearLayout) findViewById(R.id.ll_fristType);
        ll_secondType = (LinearLayout) findViewById(R.id.ll_secondType);
        ll_thridType = (LinearLayout) findViewById(R.id.ll_thridType);

        String type = getIntent().getStringExtra("type");

        if (type.equals("0")){

            ll_fristType.setVisibility(View.VISIBLE);
            ll_secondType.setVisibility(View.GONE);
            ll_thridType.setVisibility(View.GONE);

        }else if (type.equals("1"))
        {
            ll_fristType.setVisibility(View.GONE);
            ll_secondType.setVisibility(View.VISIBLE);
            ll_thridType.setVisibility(View.GONE);

        }else {

            ll_fristType.setVisibility(View.GONE);
            ll_secondType.setVisibility(View.GONE);
            ll_thridType.setVisibility(View.VISIBLE);

        }

        initView();

    }


    private void initView(){

        tv_pushComent = (TextView) findViewById(R.id.tv_pushComent);
        tv_midBtn = (TextView) findViewById(R.id.tv_midBtn);
        commentImage = (ImageView) findViewById(R.id.commentImage);

        tv_pushComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("market://details?id="
                        + getPackageName());//需要评分的APP包名
                Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent5,1);

            }
        });

        tv_midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (imagePaths1.size()>0){

                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ZBAuthTaskActivity.this, "加载中...");

                    String url = FXConstant.URL_TASKIMAGEUPDATE;
                    List<Param> params=new ArrayList<>();
                    params.add(new Param("uLoginId", DemoHelper.getInstance().getCurrentUsernName()));

                    OkHttpManager.getInstance().posts2(params,"taskImage1", imagePaths1, null,new ArrayList<String>(), null, "", null, "", null, "", null, ""
                            , null, "", null, "", null, "", null, "", url, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {

                                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                                    Toast.makeText(getApplicationContext(),"提交成功！",Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(String errorMsg) {
                                    Log.e("zbauth",errorMsg);
                                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                                    Toast.makeText(getApplicationContext(),"网络不稳定，请稍后重试",Toast.LENGTH_SHORT).show();
                                }
                            });

                }else {

                    Toast.makeText(getApplicationContext(),"请上传截图后提交",Toast.LENGTH_SHORT).show();

                }

            }
        });

        commentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                goCamera(1,"01");

            }
        });


        uazPresenter = new UAZPresenter(this,this);
        uazPresenter.loadThisDetail(DemoHelper.getInstance().getCurrentUsernName());





        tv_pushDown = (TextView) findViewById(R.id.tv_pushDown);
        tv_midBtn2 = (TextView) findViewById(R.id.tv_midBtn2);
        image_downImage1 = (ImageView) findViewById(R.id.image_downImage1);
        image_downImage2 = (ImageView) findViewById(R.id.image_downImage2);


        tv_pushDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("market://search?");
                Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent5,1);


            }
        });

        tv_midBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (imagePaths1.size() > 1){

                    mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ZBAuthTaskActivity.this, "加载中...");

                    String url = FXConstant.URL_TASKIMAGEUPDATE;
                    List<Param> params=new ArrayList<>();
                    params.add(new Param("uLoginId", DemoHelper.getInstance().getCurrentUsernName()));

                    OkHttpManager.getInstance().posts2(params,"taskImage2", imagePaths1, null,new ArrayList<String>(), null, "", null, "", null, "", null, ""
                            , null, "", null, "", null, "", null, "", url, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {

                                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                                    Toast.makeText(getApplicationContext(),"提交成功！",Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(String errorMsg) {
                                    Log.e("zbauth",errorMsg);
                                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                                    Toast.makeText(getApplicationContext(),"网络不稳定，请稍后重试",Toast.LENGTH_SHORT).show();
                                }
                            });

                }else {

                    Toast.makeText(getApplicationContext(),"请上传截图后提交",Toast.LENGTH_SHORT).show();

                }

            }
        });


        image_downImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentTag = "1";//当前选中的第一张
                goCamera(2,"02");

            }
        });


        image_downImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentTag = "2";//当前选中的第二张

                goCamera(2,"02");

            }
        });


        tv_midBtn3 = (TextView) findViewById(R.id.tv_midBtn3);
        wechatImage = (ImageView) findViewById(R.id.wechatImage);

        tv_midBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (wechatAuth.equals("1")){

                    //领取奖励
                    UpdateMeraccountSubsidy();

                }else {

                    if (imagePaths1.size()>0){

                        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(ZBAuthTaskActivity.this, "加载中...");

                        String url = FXConstant.URL_TASKIMAGEUPDATE;
                        List<Param> params=new ArrayList<>();
                        params.add(new Param("uLoginId", DemoHelper.getInstance().getCurrentUsernName()));

                        OkHttpManager.getInstance().posts2(params,"taskImage3", imagePaths1, null,new ArrayList<String>(), null, "", null, "", null, "", null, ""
                                , null, "", null, "", null, "", null, "", url, new OkHttpManager.HttpCallBack() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {

                                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                                        Toast.makeText(getApplicationContext(),"提交成功！",Toast.LENGTH_SHORT).show();



                                        finish();
                                    }

                                    @Override
                                    public void onFailure(String errorMsg) {
                                        Log.e("zbauth",errorMsg);
                                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                                        Toast.makeText(getApplicationContext(),"网络不稳定，请稍后重试",Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }else {

                        Toast.makeText(getApplicationContext(),"请上传截图后提交",Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });


        wechatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentTag = "3";//当前选中的是朋友圈点赞任务的图片

                goCamera(2,"01");

            }
        });


        tv_example = (TextView) findViewById(R.id.tv_example);

        tv_example.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ZBAuthTaskActivity.this,ZBAuthExmpleActivity.class);

                startActivityForResult(intent,0);

            }
        });


        tv_searchKey = (TextView) findViewById(R.id.tv_searchKey);
        image_logo = (ImageView) findViewById(R.id.image_logo);

        tv_searchKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(ZBAuthTaskActivity.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", keyWord);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);

                Toast.makeText(ZBAuthTaskActivity.this,"关键词已成功复制到剪切板",Toast.LENGTH_SHORT).show();

                //"market://details?id=" + getPackageName()
                Uri uri = Uri.parse("market://search?");
                Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent5,1);

            }
        });

        GetNoticeInfo();

    }



    private void UpdateMeraccountSubsidy(){

        String url = FXConstant.URL_UPDATEZHHU;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);

                if (object.getString("code").equals("SUCCESS")){

                    SoundPlayUtils.play(2);
                    LayoutInflater inflaterDl = LayoutInflater.from(ZBAuthTaskActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_hongbao, null);
                    final Dialog dialog = new AlertDialog.Builder(ZBAuthTaskActivity.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);
                    TextView tv_title1 = (TextView) layout.findViewById(R.id.tv_title1);
                    TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
                    TextView tv_yue = (TextView) layout.findViewById(R.id.tv_yue);
                    TextView tv_pushclick = (TextView) layout.findViewById(R.id.tv_pushclick);
                    tv_pushclick.setVisibility(View.VISIBLE);
                    TextPaint tp = tv_title.getPaint();
                    tp.setFakeBoldText(true);
                    tv_title.setText("1元");
                    tv_title1.setText("奖励红包");
                    tv_yue.setText("正事多 补贴红包");

                    tv_pushclick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                            Toast.makeText(ZBAuthTaskActivity.this, "领取成功", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    });

                }else {

                    Toast.makeText(ZBAuthTaskActivity.this, "网络不稳定", Toast.LENGTH_SHORT).show();
                    finish();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(ZBAuthTaskActivity.this, "网络不稳定", Toast.LENGTH_SHORT).show();
                finish();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("merId",DemoHelper.getInstance().getCurrentUsernName());
                param.put("redPromoteBalance","1");
                param.put("redPromoteCount","0");
                param.put("redPromoteType","2"); //点赞的补贴 弄好之后字直接改状态

                return param;
            }

        };

        MySingleton.getInstance(ZBAuthTaskActivity.this).addToRequestQueue(request);

    }


    private void GetNoticeInfo(){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");


                String image1 = object1.getString("image1");//关键词
                String image2 = object1.getString("image2");//图标
                keyWord = image1;
                tv_searchKey.setText("【"+image1+"】");

                ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL+image2,image_logo);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();

                param.put("deviceType","androidzbAuth");

                return param;
            }
        };

        MySingleton.getInstance(ZBAuthTaskActivity.this).addToRequestQueue(request);

    }


    private void goCamera(int count,String type){
        List<LocalMedia> medias = new ArrayList<>();

        medias = selectMedia1;

        int selectMode;
        if ("01".equals(type)) {
            selectMode = FunctionConfig.MODE_SINGLE;

        } else {
            selectMode = FunctionConfig.MODE_MULTIPLE;
        }

        WeakReference<ZBAuthTaskActivity> reference = new WeakReference<ZBAuthTaskActivity>(ZBAuthTaskActivity.this);
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
//                            .setOffsetX() // 自定义裁剪比例
//                            .setOffsetY() // 自定义裁剪比例
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(count) // 可选择图片的数量
                .setMinSelectNum(0)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(selectMode) // 单选 or 多选
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

        PictureConfig.getInstance().init(options).openPhoto(reference.get(), resultCallback1);
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

            LocalMedia media;
            LocalMedia media2;

            String path;
            String path2;

            if (resultList.size() == 1){

                media = resultList.get(0);
                if (media.isCut() && !media.isCompressed()) {
                    // 裁剪过
                    path = media.getCutPath();
                } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                    // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                    path = media.getCompressPath();
                } else {
                    // 原图地址
                    path = media.getPath();
                }

                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (currentTag.equals("1")){

                    image_downImage1.setImageBitmap(bitmap);

                }else if (currentTag.equals("2")){

                    image_downImage2.setImageBitmap(bitmap);

                }

            }else if (resultList.size() == 2){

                media = resultList.get(0);
                if (media.isCut() && !media.isCompressed()) {
                    // 裁剪过
                    path = media.getCutPath();
                } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                    // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                    path = media.getCompressPath();
                } else {
                    // 原图地址
                    path = media.getPath();
                }

                Bitmap bitmap = BitmapFactory.decodeFile(path);
                image_downImage1.setImageBitmap(bitmap);

                media2 = resultList.get(1);
                if (media2.isCut() && !media2.isCompressed()) {
                    // 裁剪过
                    path2 = media2.getCutPath();
                } else if (media2.isCompressed() || (media2.isCut() && media2.isCompressed())) {
                    // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                    path2 = media2.getCompressPath();
                } else {
                    // 原图地址
                    path2 = media2.getPath();
                }

                Bitmap bitmap2 = BitmapFactory.decodeFile(path2);
                image_downImage2.setImageBitmap(bitmap2);
            }

            if (selectMedia1 != null) {

                for (int i=0;i<selectMedia1.size();i++){
                    imagePaths1.add(selectMedia1.get(i).getCompressPath());
                }
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
         //   imagePaths1.clear();
            // 单选回调
            selectMedia1.add(media);

            String path;
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图地址
                path = media.getPath();
            }

            if (selectMedia1 != null) {
//                adapter.setList(selectMedia1);
//                adapter.notifyDataSetChanged();

              //  String path = media.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(path);

                if (currentTag.equals("1")){


                    image_downImage1.setImageBitmap(bitmap);

                    if (imagePaths1.size()>0){
                        imagePaths1.set(0,selectMedia1.get(0).getCompressPath());
                    }else {
                        imagePaths1.add(selectMedia1.get(0).getCompressPath());
                    }


                }else if (currentTag.equals("2")){

                    image_downImage2.setImageBitmap(bitmap);

                    if (imagePaths1.size() == 0){
                        imagePaths1.add("");
                        imagePaths1.add(selectMedia1.get(0).getCompressPath());

                    }else if (imagePaths1.size() == 1){

                        imagePaths1.add(selectMedia1.get(0).getCompressPath());

                    }else {

                        imagePaths1.set(1,selectMedia1.get(0).getCompressPath());

                    }

                }else
                {

                    if (currentTag.equals("3")){

                        wechatImage.setImageBitmap(bitmap);
                        for (int i=0;i<selectMedia1.size();i++){
                            imagePaths1.add(selectMedia1.get(i).getCompressPath());
                        }

                    }else {

                        commentImage.setImageBitmap(bitmap);
                        for (int i=0;i<selectMedia1.size();i++){
                            imagePaths1.add(selectMedia1.get(i).getCompressPath());
                        }
                    }

                }



            }
        }
    };


    @Override
    public void back(View view) {
        super.back(view);
    }


    @Override
    public void updateThisUser(Userful user2) {

        String taskImage1 = user2.getTaskImage1();

        String taskImage2 = user2.getTaskImage2();

        String taskImage3 = user2.getTaskImage3();
        wechatAuth = user2.getWechatAuth();

        String type = getIntent().getStringExtra("type");

        if (type != null && type.equals("0")){

            if (taskImage1.length()>0){

                //已经提交过了

                tv_midBtn.setEnabled(false);
                tv_midBtn.setText("已提交");
                commentImage.setEnabled(false);

                String[] images = taskImage1.split("\\|");

                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+images[0],commentImage);

            }

        }else if (type != null && type.equals("1")){

            if (taskImage2.length()>0){

                //已经提交过了

                tv_midBtn2.setEnabled(false);
                tv_midBtn2.setText("已提交");
                image_downImage1.setEnabled(false);
                image_downImage2.setEnabled(false);

                String[] images = taskImage2.split("\\|");

                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+images[0],image_downImage1);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+images[1],image_downImage2);

            }

        }else if (type != null && type.equals("2")){


            if (taskImage3.length()>0){

                //已经提交过了

                if (wechatAuth.equals("1")){

                    tv_midBtn3.setText("领取奖励");

                }else if (wechatAuth.equals("2")){

                    tv_midBtn3.setEnabled(false);
                    tv_midBtn3.setText("已完成");

                }else {

                    tv_midBtn3.setEnabled(false);
                    tv_midBtn3.setText("已提交");

                }


                wechatImage.setEnabled(false);

                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+taskImage3,wechatImage);

            }

        }

    }

    @Override
    public void showproError() {

    }

    @Override
    public void showproLoading() {

    }

    @Override
    public void hideproLoading() {

    }

}
