package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.NomalVideoPlayActivity;
import com.sangu.apptongji.main.adapter.GridImageAdapter;
import com.sangu.apptongji.main.moments.BigImageActivity;
import com.sangu.apptongji.main.utils.FullyGridLayoutManager;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.SelectPicPopupWindow;
import com.sangu.apptongji.utils.WeiboDialogUtils;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class NewsOrderRefuseActivity extends BaseActivity {


    private String orderId,type;
    private EditText et_miaoshu;


    /*图片适配器*/
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private SelectPicPopupWindow menuWindow=null;
    private String filePath;
    private ArrayList<String> imagePaths1 = null;

    private TextView tv_midBtn;

    private Dialog mWeiboDialog;

    LinearLayout ll_one;
    RelativeLayout re_avatar,rl_midlayout;
    ImageView image1,image2,image3,image4;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_newsorder_refuse);

        orderId = getIntent().getStringExtra("orderId");
        type = getIntent().getStringExtra("type");

        imagePaths1 = new ArrayList<>();

        et_miaoshu = (EditText) findViewById(R.id.et_miaoshu);
        re_avatar = (RelativeLayout) findViewById(R.id.re_avatar);
        tv_midBtn = (TextView) findViewById(R.id.tv_midBtn);
        rl_midlayout = (RelativeLayout) findViewById(R.id.rl_midlayout);

        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        image1 = (ImageView) findViewById(R.id.image_1);
        image2 = (ImageView) findViewById(R.id.image_2);
        image3 = (ImageView) findViewById(R.id.image_3);
        image4 = (ImageView) findViewById(R.id.image_4);

        tv_midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_miaoshu.getText().toString().trim().length() > 0){

                }else {
                    Toast.makeText(NewsOrderRefuseActivity.this,"请您输入原因",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imagePaths1.size() > 0){

                }else {

                    Toast.makeText(NewsOrderRefuseActivity.this,"请上传对应图片",Toast.LENGTH_SHORT).show();
                    return;

                }

                final LayoutInflater inflater1 = LayoutInflater.from(NewsOrderRefuseActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog collectionDialog = new AlertDialog.Builder(NewsOrderRefuseActivity.this, R.style.Dialog).create();
                collectionDialog.show();
                collectionDialog.getWindow().setContentView(layout1);
                collectionDialog.setCanceledOnTouchOutside(true);
                collectionDialog.setCancelable(true);
                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                title.setText("温馨提示");
                btnOK1.setText("确定");
                btnCancel1.setText("取消");
                title_tv1.setText("是否确定拒绝？");

                btnCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        collectionDialog.dismiss();
                    }
                });

                btnOK1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        collectionDialog.dismiss();

                        UpdateOrderDetailInfo();

                    }
                });

            }
        });


        String show = getIntent().getStringExtra("show");
        String imageStr = getIntent().getStringExtra("resonImage");
        String reson = getIntent().getStringExtra("reson");

        if (show != null){

            et_miaoshu.setText(reson);
            et_miaoshu.setEnabled(false);

            re_avatar.setVisibility(View.GONE);
            rl_midlayout.setVisibility(View.GONE);
            ll_one.setVisibility(View.VISIBLE);

            if (imageStr != null){

                final String[] images = imageStr.split("\\|");
                int imNumb = images.length;

                image1.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN_FOUR+images[0],image1);

                image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(NewsOrderRefuseActivity.this, BigImageActivity.class);
                        intent.putExtra("images", images);
                        intent.putExtra("page", 0);
                        intent.putExtra("biaoshi","06");
                        startActivityForResult(intent,0);

                    }
                });

                if (imNumb > 1) {

                    image2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN_FOUR+images[1],image2);
                    image2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(NewsOrderRefuseActivity.this, BigImageActivity.class);
                            intent.putExtra("images", images);
                            intent.putExtra("page", 1);
                            intent.putExtra("biaoshi","06");
                            startActivityForResult(intent,0);
                        }
                    });
                    if (imNumb > 2) {

                        image3.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN_FOUR+images[2],image3);
                        image3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(NewsOrderRefuseActivity.this, BigImageActivity.class);
                                intent.putExtra("images", images);
                                intent.putExtra("page", 2);
                                intent.putExtra("biaoshi","06");
                                startActivityForResult(intent,0);
                            }
                        });
                        if (imNumb > 3) {

                            image4.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN_FOUR+images[3],image4);
                            image4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(NewsOrderRefuseActivity.this, BigImageActivity.class);
                                    intent.putExtra("images", images);
                                    intent.putExtra("page", 3);
                                    intent.putExtra("biaoshi","06");
                                    startActivityForResult(intent,0);
                                }
                            });
                        }

                    }

                }

            }

        }else {

            initViews();

        }



    }

    private void UpdateOrderDetailInfo(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(NewsOrderRefuseActivity.this, "加载中...");


        List<Param> params = new ArrayList<Param>();


        params.add(new Param("orderId", orderId));
        params.add(new Param("flg", "06"));


        String str = "";
        if (type.equals("0")){
            str = "conRefImage";
            params.add(new Param("state", "06"));
            params.add(new Param("conReson", et_miaoshu.getText().toString().trim()));
        }else {
            str = "merRefImage";
            params.add(new Param("state", "08"));
            params.add(new Param("merReson", et_miaoshu.getText().toString().trim()));
        }

        String url = FXConstant.URL_Order_Detail_update;

        final List<File> files = new ArrayList<File>();

        for (String path : imagePaths1){

            File file = new File(path);
            files.add(file);

        }

        OkHttpManager.getInstance().post(str, params, files, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                Log.e("qianming,111",jsonObject.toString());

                WeiboDialogUtils.closeDialog(mWeiboDialog);

                Toast.makeText(NewsOrderRefuseActivity.this, "已拒绝!", Toast.LENGTH_SHORT).show();

                if (type.equals("0")){

                    setResult(RESULT_OK,new Intent().putExtra("orderState","06"));

                }else {

                    setResult(RESULT_OK,new Intent().putExtra("orderState","08"));

                }

                finish();

            }

            @Override
            public void onFailure(String errorMsg) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(NewsOrderRefuseActivity.this, "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(NewsOrderRefuseActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(NewsOrderRefuseActivity.this, onAddPicClickListener);
        adapter.setList(selectMedia);
        adapter.setSelectMax(4);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (selectMedia.get(position).getType()) {
                    case FunctionConfig.TYPE_IMAGE:
                        // 预览图片 可长按保存 也可自定义保存路径
//                        PictureConfig.getInstance().externalPicturePreview(MainActivity.this, "/custom_file", position, selectMedia);
                        PictureConfig.getInstance().externalPicturePreview(NewsOrderRefuseActivity.this, position, selectMedia);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(NewsOrderRefuseActivity.this, selectMedia.get(position).getPath());
                        }
                        break;
                }
            }
        });
    }

    /**
     * 删除图片回调接口
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    if (filePath==null) {
                        goCamera(FunctionConfig.TYPE_IMAGE,4,false,false);
                    }else {
                        Toast.makeText(getApplicationContext(),"动态视频只能选择一个",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    // 删除图片
                    imagePaths1.remove(position);
                    selectMedia.remove(position);
                    if (filePath!=null){
                        filePath=null;
                    }
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    private void goCamera(int type1,int count,boolean mode,boolean canCut){
        WeakReference<NewsOrderRefuseActivity> reference = new WeakReference<NewsOrderRefuseActivity>(NewsOrderRefuseActivity.this);
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(type1) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(FunctionConfig.CROP_MODEL_1_1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
//                            .setOffsetX() // 自定义裁剪比例
//                            .setOffsetY() // 自定义裁剪比例
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(count) // 可选择图片的数量
                .setMinSelectNum(0)// 图片或视频最低选择数量，默认代表无限制
                .setSelectMode(FunctionConfig.MODE_MULTIPLE) // 单选 or 多选
                .setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
                .setEnablePreview(true) // 是否打开预览选项
                .setEnableCrop(canCut) // 是否打开剪切选项
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

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {

            recyclerView.setVisibility(View.VISIBLE);
            imagePaths1.clear();
            // 多选回调
            selectMedia = resultList;
            if (selectMedia != null) {
                if (resultList.get(0).getType()==FunctionConfig.TYPE_VIDEO){
                    String path = resultList.get(0).getPath();
                    startActivityForResult(new Intent(NewsOrderRefuseActivity.this, NomalVideoPlayActivity.class).putExtra("video_path",path),11);
                }else {
                    for (int i = 0; i < selectMedia.size(); i++) {
                        imagePaths1.add(selectMedia.get(i).getCompressPath());
                    }
                    adapter.setList(selectMedia);
                    adapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {

            recyclerView.setVisibility(View.VISIBLE);
            imagePaths1.clear();
            // 单选回调
            selectMedia.add(media);
            if (selectMedia != null) {
                if (media.getType()==FunctionConfig.TYPE_VIDEO){
                    filePath = media.getPath();
                    imagePaths1.add(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                    adapter.setList(selectMedia);
                    adapter.notifyDataSetChanged();
                }else {
                    imagePaths1.add(media.getCompressPath());
                    adapter.setList(selectMedia);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

}
