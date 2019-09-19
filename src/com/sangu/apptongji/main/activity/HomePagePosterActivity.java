package com.sangu.apptongji.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fanxin.easeui.widget.EaseSwitchButton;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.utils.SelectPicPopupWindow;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomePagePosterActivity extends CoreActivity implements IQiYeDetailView {
    private String companyId;
    private IQiYeInfoPresenter qiYeInfoPresenter = null;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.sb_type1)
    EaseSwitchButton sbType1;
    @BindView(R.id.sb_type2)
    EaseSwitchButton sbType2;
    @BindView(R.id.sb_type3)
    EaseSwitchButton sbType3;
    @BindView(R.id.sb_type4)
    EaseSwitchButton sbType4;
    @BindView(R.id.cb_qq)
    AppCompatCheckBox cbQQ;
    @BindView(R.id.cb_wx)
    AppCompatCheckBox cbWX;
    @BindView(R.id.et_url)
    EditText etURL;
    private SelectPicPopupWindow menuWindow = null;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    String path = "";
    String signShareType = "";
    private QiYeInfo qiYeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_poster);
        ButterKnife.bind(this);
        companyId = getIntent().getStringExtra("companyId");
        qiYeInfoPresenter = new QiYeInfoPresenter(HomePagePosterActivity.this, this);
        qiYeInfoPresenter.loadQiYeInfo(companyId);
    }

    @OnClick({R.id.tv_back, R.id.iv_back, R.id.iv_img, R.id.sb_type1, R.id.sb_type2, R.id.sb_type3, R.id.sb_type4,R.id.tv_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_back:
                finish();
                break;
            case R.id.iv_img:
                selectImgs();
                break;
            case R.id.sb_type1:
                if (sbType1.isSwitchOpen()) {
                    sbType1.closeSwitch();
                } else {
                    sbType1.openSwitch();
                    sbType2.closeSwitch();
                    sbType3.closeSwitch();
                }
                setSBType();
                break;
            case R.id.sb_type2:
                if (sbType2.isSwitchOpen()) {
                    sbType2.closeSwitch();
                } else {
                    sbType2.openSwitch();
                    sbType1.closeSwitch();
                    sbType3.closeSwitch();
                }
                setSBType();
                break;
            case R.id.sb_type3:
                if (sbType3.isSwitchOpen()) {
                    sbType3.closeSwitch();
                } else {
                    sbType3.openSwitch();
                    sbType1.closeSwitch();
                    sbType2.closeSwitch();
                }
                setSBType();
                break;
            case R.id.sb_type4:
                if (sbType4.isSwitchOpen()) {
                    sbType4.closeSwitch();
                } else {
                    sbType4.openSwitch();
                }
                break;
            case R.id.tv_commit:
                updateCompany();
                break;
        }
    }

    private void updateCompany() {
        if (signShareType.equals("02")) {
            String url = etURL.getText().toString().trim();
            if (TextUtils.isEmpty(url)) {
                showToast("请输入公司网站");
                return;
            }
        } else if (signShareType.equals("03") && !qiYeInfo.getSignShareType().equals("03")) {
            if (TextUtils.isEmpty(path)) {
                showToast("请选择公司海报");
                return;
            }
        }
        showDialog();
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("companyName", this.qiYeInfo.getCompanyName()));
        params.add(new Param("signShareChannel", getchannel()));
        params.add(new Param("signShareType", signShareType));
        params.add(new Param("signShareNeed", sbType4.isSwitchOpen() ? "01" : "00"));
        params.add(new Param("signShareContent", etURL.getText().toString().trim()));
        OkHttpManager.getInstance().postUpdateCompany(params, "signShareImageFile", path, FXConstant.URL_UPDATE_QIYE, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("update==", jsonObject.toJSONString());
                disDialog();
                try {
                    String code = jsonObject.getString("code");
                    if(code.equals("SUCCESS")){
                        finish();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                disDialog();
            }
        });
    }

    private String getchannel() {
        String wx = "0", qq = "0";
        if (cbQQ.isChecked()) {
            qq = "1";
        }
        if (cbWX.isChecked()) {
            wx = "1";
        }
        return qq + "|" + wx;
    }

    private void setSBType() {
        if (sbType1.isSwitchOpen()) {
            signShareType = "01";
        } else if (sbType2.isSwitchOpen()) {
            signShareType = "02";
        } else if (sbType3.isSwitchOpen()) {
            signShareType = "03";
        } else {
            sbType1.closeSwitch();
            sbType2.closeSwitch();
            sbType3.closeSwitch();
            signShareType = "00";
        }
    }

    private void setSignShareType(String signShareType, String path) {
        if (signShareType.equals("01")) {
            sbType1.openSwitch();
            sbType2.closeSwitch();
            sbType3.closeSwitch();
        } else if (signShareType.equals("02")) {
            sbType1.closeSwitch();
            sbType2.openSwitch();
            sbType3.closeSwitch();
        } else if (signShareType.equals("03")) {
            sbType1.closeSwitch();
            sbType2.closeSwitch();
            sbType3.openSwitch();
            Glide.with(HomePagePosterActivity.this)
                    .load(path)
                    .centerCrop()
                    .placeholder(R.color.color_f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivImg);
        }else{
            sbType1.closeSwitch();
            sbType2.closeSwitch();
            sbType3.closeSwitch();
        }
    }

    @Override
    public void updateQiyeInfo(QiYeInfo qiYeInfo) throws UnsupportedEncodingException {
        this.qiYeInfo = qiYeInfo;
        etURL.setText(qiYeInfo.getSignShareContent());
        String signShareChannel=qiYeInfo.getSignShareChannel();
        String[] channel=signShareChannel.split("\\|");
        if(channel.length==2){
            if(channel[0].equals("1")){
                cbQQ.setChecked(true);
            }
            if(channel[1].equals("1")){
                cbWX.setChecked(true);
            }
        }
        if(qiYeInfo.getSignShareNeed().equals("01")){
            sbType4.openSwitch();
        }
        setSignShareType(qiYeInfo.getSignShareType(), FXConstant.URL_QIYE_TOUXIANG + qiYeInfo.getSignShareImage());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }

    private void selectImgs() {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        menuWindow = new SelectPicPopupWindow(HomePagePosterActivity.this, 0, itemsOnClick);
        //设置弹窗位置
        menuWindow.showAtLocation(ivImg, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:        //点击拍照按钮
                    goCamera(true);
                    break;
                case R.id.item_popupwindows_Photo:       //点击从相册中选择按钮
                    goCamera(false);
                    break;
                default:
                    break;
            }
        }
    };

    private void goCamera(boolean mode) {
        WeakReference<HomePagePosterActivity> reference = new WeakReference<HomePagePosterActivity>(HomePagePosterActivity.this);
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
//                            .setOffsetX() // 自定义裁剪比例
//                            .setOffsetY() // 自定义裁剪比例
                .setCompress(false) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(1) // 可选择图片的数量
                .setMinSelectNum(0)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(FunctionConfig.MODE_SINGLE) // 单选 or 多选
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
                .setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
                .setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
                .setCompressW(0) // 压缩宽 如果值大于图片原始宽高无效
                .setCompressH(0) // 压缩高 如果值大于图片原始宽高无效
                .setThemeStyle(ContextCompat.getColor(reference.get(), R.color.blue)) // 设置主题样式
                .setNumComplete(false) // 0/9 完成  样式
                .setClickVideo(false)// 点击声音
                .setFreeStyleCrop(false) // 裁剪是移动矩形框或是图片
                .create();
        if (mode) {
            // 只拍照
            PictureConfig.getInstance().init(options).startOpenCamera(reference.get());
        } else {
            // 先初始化参数配置，在启动相册
            PictureConfig.getInstance().init(options).openPhoto(reference.get(), resultCallback);
        }
    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FunctionConfig.CAMERA_RESULT:
                    if (data != null) {
                        List<LocalMedia> medias = (List<LocalMedia>) data.getSerializableExtra(FunctionConfig.EXTRA_RESULT);
                        if (medias != null) {
                            LocalMedia media = medias.get(0);
                            int type = media.getType();
                            if (media.isCut() && !media.isCompressed()) {
                                // 裁剪过
                                path = media.getCutPath();
                            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                                path = media.getCompressPath();
                            } else {
                                // 原图
                                path = media.getPath();
                            }
                            if (media.isCompressed()) {
                                Log.i("compress image result", new File(media.getCompressPath()).length() / 1024 + "k");
                                Log.i("原图地址::", media.getPath());
                                Log.i("压缩地址::", media.getCompressPath());
                            }
                            Glide.with(HomePagePosterActivity.this)
                                    .load(path)
                                    .centerCrop()
                                    .placeholder(R.color.color_f6)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(ivImg);
                        }
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            LocalMedia media = resultList.get(0);
            int type = media.getType();
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }
            if (media.isCompressed()) {
                Log.i("compress image result", new File(media.getCompressPath()).length() / 1024 + "k");
                Log.i("原图地址::", media.getPath());
                Log.i("压缩地址::", media.getCompressPath());
            }
            Glide.with(HomePagePosterActivity.this)
                    .load(path)
                    .centerCrop()
                    .placeholder(R.color.color_f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivImg);
        }

        @Override
        public void onSelectSuccess(final LocalMedia media) {
            int type = media.getType();
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }
            if (media.isCompressed()) {
                Log.i("compress image result", new File(media.getCompressPath()).length() / 1024 + "k");
                Log.i("原图地址::", media.getPath());
                Log.i("压缩地址::", media.getCompressPath());
            }
            Glide.with(HomePagePosterActivity.this)
                    .load(path)
                    .centerCrop()
                    .placeholder(R.color.color_f6)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivImg);
        }
    };

}
