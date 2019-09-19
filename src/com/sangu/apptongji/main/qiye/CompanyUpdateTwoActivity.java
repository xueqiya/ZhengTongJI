package com.sangu.apptongji.main.qiye;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.fanxin.easeui.utils.FileStorage;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.GridImageAdapter;
import com.sangu.apptongji.main.moments.BiImageActivity;
import com.sangu.apptongji.main.utils.FullyGridLayoutManager;
import com.sangu.apptongji.main.utils.LimitInputTextWatcher;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.FileUtils;
import com.sangu.apptongji.utils.SelectPicPopupWindow;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016-12-29.
 */

public class CompanyUpdateTwoActivity extends BaseActivity implements View.OnClickListener{
    public static final int TYPE_MAJOR1 = 4;
    public static final int TYPE_MAJOR2 = 5;
    public static final int TYPE_MAJOR3 = 6;
    public static final int TYPE_MAJOR4 = 7;
    private ImageView iv_moshiyi=null,iv_moshier=null,iv_moshisan=null,iv_moshisi=null,iv_moshiwu=null;
    private CheckBox cb_moshiyi=null,cb_moshier=null,cb_moshisan=null,cb_moshisi=null,cb_moshiwu=null;
    private String image,zyResv3="01",upName,upDescribe,companyId,companyName;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private SelectPicPopupWindow menuWindow=null;
    private ArrayList<String> imagePaths1 = new ArrayList<>();

    // 发送按钮
    private Button btn_send;
    private ImageView ivBack;
    // 文本输入
    private EditText et_content;
    private EditText et_content2;
    private TextView titleTV;
    private int type;
    private String isFirst;
    private String [] str;
    private CustomProgressDialog pd ;
    private boolean istuFirst = true;
    HeadThread headThread=null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    adapter.notifyDataSetChanged();
                    if (headThread!=null) {
                        headThread.interrupt();
                        headThread = null;
                    }
                    if (pd!=null&&pd.isShowing()){
                        pd.dismiss();
                    }
                    break;
            }
        }
    };

    private void uploadImage(String image, CustomProgressDialog pd) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        String url = FXConstant.URL_QIYE_ZY+image;
        try {
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
            okhttp3.Response response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            Bitmap bm = BitmapFactory.decodeStream(is);
            if (bm!=null) {
                saveToSD(bm, image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToSD(Bitmap mBitmap,String imageName) {
        if (mBitmap!=null) {
            File file = new FileStorage("comzhuye").createCropFile(imageName,null);
            Uri imageUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                imageUri = FileProvider.getUriForFile(CompanyUpdateTwoActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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
                loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comzhuye/"+imageName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_update_two_info);
        isFirst = this.getIntent().getStringExtra("isFirst");
        type = getIntent().getIntExtra("type", 0);
        image = this.getIntent().getStringExtra("image");
        zyResv3 = this.getIntent().getStringExtra("zyResv3");
        upName = this.getIntent().getStringExtra("upName");
        upDescribe = this.getIntent().getStringExtra("upDescribe");
        companyName = this.getIntent().getStringExtra("companyName");
        companyId = this.getIntent().getStringExtra("companyId");
        WeakReference<CompanyUpdateTwoActivity> reference =  new WeakReference<CompanyUpdateTwoActivity>(CompanyUpdateTwoActivity.this);
        pd = CustomProgressDialog.createDialog(reference.get());
        pd.setCancelable(true);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                pd.dismiss();
            }
        });
        pd.setMessage("正在加载数据...");
        pd.show();
        String title = "";
        initView();
        initViews();
        switch (type) {
            case TYPE_MAJOR1:
                title = "修改专业1";
                titleTV.setText(title);
                break;
            case TYPE_MAJOR2:
                title = "修改专业2";
                titleTV.setText(title);
                break;
            case TYPE_MAJOR3:
                title = "修改专业3";
                titleTV.setText(title);
                break;
            case TYPE_MAJOR4:
                title = "修改专业4";
                titleTV.setText(title);
                break;
        }
        if (upName!=null&&!"".equals(upName)) {
            et_content.setText(upName);
            et_content.setSelection(et_content.getText().length());
        }
        if (upDescribe!=null) {
            et_content2.setText(upDescribe);
            et_content2.setSelection(et_content2.getText().length());
        }
        if ("05".equals(zyResv3)){
            cb_moshisan.setChecked(false);
            cb_moshier.setChecked(false);
            cb_moshiyi.setChecked(false);
            cb_moshisi.setChecked(false);
            cb_moshiwu.setChecked(true);
        }else if ("04".equals(zyResv3)){
            cb_moshisan.setChecked(false);
            cb_moshier.setChecked(false);
            cb_moshiyi.setChecked(false);
            cb_moshisi.setChecked(true);
            cb_moshiwu.setChecked(false);
        }else if ("03".equals(zyResv3)){
            cb_moshisan.setChecked(true);
            cb_moshier.setChecked(false);
            cb_moshiyi.setChecked(false);
            cb_moshisi.setChecked(false);
            cb_moshiwu.setChecked(false);
        }else if ("02".equals(zyResv3)){
            cb_moshier.setChecked(true);
            cb_moshisan.setChecked(false);
            cb_moshiyi.setChecked(false);
            cb_moshisi.setChecked(false);
            cb_moshiwu.setChecked(false);
        }else {
            cb_moshiyi.setChecked(true);
            cb_moshier.setChecked(false);
            cb_moshisan.setChecked(false);
            cb_moshisi.setChecked(false);
            cb_moshiwu.setChecked(false);
        }
        if (image!=null&&!image.equals("")&&!image.equalsIgnoreCase("null")&&upName!=null&&!"".equals(upName)) {
            if (istuFirst) {
                if (headThread == null) {
                    headThread = new HeadThread();
                    headThread.start();
                }
            }
        }else {
            if (pd!=null&&pd.isShowing()) {
                pd.dismiss();
            }
        }
    }

    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(CompanyUpdateTwoActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(CompanyUpdateTwoActivity.this, onAddPicClickListener);
        adapter.setList(selectMedia);
        adapter.setSelectMax(8);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (selectMedia.get(position).getType()) {
                    case FunctionConfig.TYPE_IMAGE:
                        // 预览图片 可长按保存 也可自定义保存路径
//                        PictureConfig.getInstance().externalPicturePreview(MainActivity.this, "/custom_file", position, selectMedia);
                        PictureConfig.getInstance().externalPicturePreview(CompanyUpdateTwoActivity.this, position, selectMedia);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(CompanyUpdateTwoActivity.this, selectMedia.get(position).getPath());
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
                    selectImgs();
                    break;
                case 1:
                    // 删除图片
                    selectMedia.remove(position);
                    imagePaths1.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    private void selectImgs(){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(CompanyUpdateTwoActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        menuWindow = new SelectPicPopupWindow(CompanyUpdateTwoActivity.this,0,itemsOnClick);
        //设置弹窗位置
        menuWindow.showAtLocation(CompanyUpdateTwoActivity.this.findViewById(R.id.llImage), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
    private void goCamera(boolean mode){
        WeakReference<CompanyUpdateTwoActivity> reference = new WeakReference<CompanyUpdateTwoActivity>(CompanyUpdateTwoActivity.this);
        FunctionOptions options = new FunctionOptions.Builder()
                .setType(FunctionConfig.TYPE_IMAGE) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
                .setCropMode(1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
//                            .setOffsetX() // 自定义裁剪比例
//                            .setOffsetY() // 自定义裁剪比例
                .setCompress(true) //是否压缩
                .setEnablePixelCompress(true) //是否启用像素压缩
                .setEnableQualityCompress(true) //是否启质量压缩
                .setMaxSelectNum(8) // 可选择图片的数量
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
            imagePaths1.clear();
            // 多选回调
            selectMedia = resultList;
//            Log.i("callBack_result", selectMedia.size() + "");
//            LocalMedia media = resultList.get(0);
//            if (media.isCut() && !media.isCompressed()) {
//                // 裁剪过
//                String path = media.getCutPath();
//            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
//                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
//                String path = media.getCompressPath();
//            } else {
//                // 原图地址
//                String path = media.getPath();
//            }
            if (selectMedia != null) {
                for (int i=0;i<selectMedia.size();i++){
                    imagePaths1.add(selectMedia.get(i).getCompressPath());
                }
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
//                if (imagePaths2.size()>0){
//                    if (pd!=null){
//                        pd.setMessage("正在处理图片资源...");
//                        pd.show();
//                    }else {
//                        WeakReference<CompanyUpdateTwoActivity> reference =  new WeakReference<CompanyUpdateTwoActivity>(CompanyUpdateTwoActivity.this);
//                        pd = CustomProgressDialog.createDialog(reference.get());
//                        pd.setCancelable(false);
//                        pd.setMessage("正在处理图片资源...");
//                        pd.show();
//                    }
//                    imaThread = new ImaThread();
//                    imaThread.start();
//                }
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            imagePaths1.clear();
            // 单选回调
            selectMedia.add(media);
            if (selectMedia != null) {
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
                imagePaths1.add(media.getCompressPath());
//                if (imagePaths2.size()>0) {
//                    if (pd!=null){
//                        pd.setMessage("正在处理图片资源...");
//                        pd.show();
//                    }else {
//                        WeakReference<CompanyUpdateTwoActivity> reference =  new WeakReference<CompanyUpdateTwoActivity>(CompanyUpdateTwoActivity.this);
//                        pd = CustomProgressDialog.createDialog(reference.get());
//                        pd.setCancelable(false);
//                        pd.setMessage("正在处理图片资源...");
//                        pd.show();
//                    }
//                    imaThread = new ImaThread();
//                    imaThread.start();
//                }
            }
        }
    };

    private void initView() {
        titleTV = (TextView) findViewById(R.id.tv_title);
        iv_moshiyi = (ImageView) this.findViewById(R.id.iv_moshiyi);
        iv_moshier = (ImageView) this.findViewById(R.id.iv_moshier);
        iv_moshisan = (ImageView) this.findViewById(R.id.iv_moshisan);
        iv_moshisi = (ImageView) this.findViewById(R.id.iv_moshisi);
        iv_moshiwu = (ImageView) this.findViewById(R.id.iv_moshiwu);
        cb_moshiyi = (CheckBox) this.findViewById(R.id.cb_moshiyi);
        cb_moshier = (CheckBox) this.findViewById(R.id.cb_moshier);
        cb_moshisan = (CheckBox) this.findViewById(R.id.cb_moshisan);
        cb_moshisi = (CheckBox) this.findViewById(R.id.cb_moshisi);
        cb_moshiwu = (CheckBox) this.findViewById(R.id.cb_moshiwu);
        iv_moshiyi.setOnClickListener(this);
        iv_moshier.setOnClickListener(this);
        iv_moshisan.setOnClickListener(this);
        iv_moshisi.setOnClickListener(this);
        iv_moshiwu.setOnClickListener(this);
        cb_moshiyi.setOnClickListener(this);
        cb_moshier.setOnClickListener(this);
        cb_moshisan.setOnClickListener(this);
        cb_moshisi.setOnClickListener(this);
        cb_moshiwu.setOnClickListener(this);
        iv_moshiyi.setImageResource(R.drawable.xiadanmoshiyi);
        iv_moshier.setImageResource(R.drawable.xiadanmoshier);
        iv_moshisan.setImageResource(R.drawable.xiadanmoshisan);
        iv_moshisi.setImageResource(R.drawable.xiadanmoshisi);
        iv_moshiwu.setImageResource(R.drawable.xiadanmoshiwu);
        ivBack = (ImageView) this.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_content = (EditText) this.findViewById(R.id.et_info);
        et_content2 = (EditText) findViewById(R.id.et_info2);
        btn_send = (Button) this.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString().trim();
                String content2 = et_content2.getText().toString().trim();
                if (upName==null||"".equals(upName)){
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(CompanyUpdateTwoActivity.this, "请输入专业名称！",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    if (TextUtils.isEmpty(content)) {
                        content = "";
                    }
                }
                if (imagePaths1.size()<=8&&content.length()<=6) {
                    send(content, content2);
                }else {
                    Toast.makeText(CompanyUpdateTwoActivity.this, "图片不能多于八张,专业名称不超过六个字！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        et_content.addTextChangedListener(new LimitInputTextWatcher(et_content));
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    // 发送
    private void send(final String info , final String info2) {
        String upType = "";
        if (cb_moshisan.isChecked()){
            Toast.makeText(getApplicationContext(),"依照相关法律，请加入获得相关资质的汽车租赁或网约车企业后，才能开启打车模板",Toast.LENGTH_LONG).show();
            return;
        }
        if (pd!=null){
            pd.setMessage("正在上传...");
            pd.show();
        }else {
            WeakReference<CompanyUpdateTwoActivity> reference =  new WeakReference<CompanyUpdateTwoActivity>(CompanyUpdateTwoActivity.this);
            pd = CustomProgressDialog.createDialog(reference.get());
            pd.setCancelable(false);
            pd.setMessage("正在上传...");
            pd.show();
        }
        String str = null;
        List<Param> param=new ArrayList<>();
        param.add(new Param("companyId", companyId));
        param.add(new Param("companyName",companyName));
        switch(type) {
            case TYPE_MAJOR1:
                if (cb_moshiyi.isChecked()){
                    upType = "01";
                }
                if (cb_moshier.isChecked()){
                    upType = "02";
                }
                if (cb_moshisan.isChecked()){
                    upType = "03";
                }
                if (cb_moshisi.isChecked()){
                    upType = "04";
                }
                if (cb_moshiwu.isChecked()){
                    upType = "05";
                }
                param.add(new Param("userProfessionList[0].upId", companyId+"1"));
                if (!upName.equals(info)) {
                    param.add(new Param("userProfessionList[0].upName", info));
                }
                if (!"".equals(info)) {
                    param.add(new Param("userProfessionList[0].upDescribe", info2));
                    param.add(new Param("userProfessionList[0].upType", upType));
                    str = "userProfessionList[0].image";
                }
                setResult(RESULT_OK, new Intent().putExtra("value", info));
                break;
            case TYPE_MAJOR2:
                if (cb_moshiyi.isChecked()){
                    upType = "01";
                }
                if (cb_moshier.isChecked()){
                    upType = "02";
                }
                if (cb_moshisan.isChecked()){
                    upType = "03";
                }
                if (cb_moshisi.isChecked()){
                    upType = "04";
                }
                if (cb_moshiwu.isChecked()){
                    upType = "05";
                }
                param.add(new Param("userProfessionList[1].upId", companyId + "2"));
                if (!upName.equals(info)) {
                    param.add(new Param("userProfessionList[1].upName", info));
                }
                if (!"".equals(info)) {
                    param.add(new Param("userProfessionList[1].upDescribe", info2));
                    param.add(new Param("userProfessionList[1].upType", upType));
                    str = "userProfessionList[1].image";
                }
                setResult(RESULT_OK, new Intent().putExtra("value", info));
                break;
            case TYPE_MAJOR3:
                if (cb_moshiyi.isChecked()){
                    upType = "01";
                }
                if (cb_moshier.isChecked()){
                    upType = "02";
                }
                if (cb_moshisan.isChecked()){
                    upType = "03";
                }
                if (cb_moshisi.isChecked()){
                    upType = "04";
                }
                if (cb_moshiwu.isChecked()){
                    upType = "05";
                }
                param.add(new Param("userProfessionList[2].upId", companyId + "3"));
                if (!upName.equals(info)) {
                    param.add(new Param("userProfessionList[2].upName", info));
                }
                if (!"".equals(info)) {
                    param.add(new Param("userProfessionList[2].upDescribe", info2));
                    param.add(new Param("userProfessionList[2].upType", upType));
                    str = "userProfessionList[2].image";
                }
                setResult(RESULT_OK, new Intent().putExtra("value", info));
                break;
            case TYPE_MAJOR4:
                if (cb_moshiyi.isChecked()){
                    upType = "01";
                }
                if (cb_moshier.isChecked()){
                    upType = "02";
                }
                if (cb_moshisan.isChecked()){
                    upType = "03";
                }
                if (cb_moshisi.isChecked()){
                    upType = "04";
                }
                if (cb_moshiwu.isChecked()){
                    upType = "05";
                }
                param.add(new Param("userProfessionList[3].upId", companyId + "4"));
                if (!upName.equals(info)) {
                    param.add(new Param("userProfessionList[3].upName", info));
                }
                if (!"".equals(info)) {
                    param.add(new Param("userProfessionList[3].upDescribe", info2));
                    param.add(new Param("userProfessionList[3].upType", upType));
                    str = "userProfessionList[3].image";
                }
                setResult(RESULT_OK, new Intent().putExtra("value", info));
                break;
        }
        OkHttpManager.getInstance().postMoments(str,param,imagePaths1,FXConstant.URL_UPDATE_QIYE,new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                pd.dismiss();
                String code = jsonObject.getString("code");
                if (code.equals("SUCCESS")) {
                    FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/catch/"));
                    FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/comzhuye/"));
                    Toast.makeText(getApplicationContext(), "更新成功！", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "更新失败,code:" + code, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(String errorMsg) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private void loadImage1(String filepath){
        imagePaths1.add(filepath);
        int type = FunctionConfig.TYPE_IMAGE;
        LocalMedia media = new LocalMedia();
        media.setType(type);
        media.setCompressed(false);
        media.setCut(false);
        media.setIsChecked(true);
        media.setNum(imagePaths1.size());
        media.setPath(filepath);
        selectMedia.add(media);
    }
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddHHmmssSS");
        return dateFormat.format(date);
    }
    class HeadThread extends Thread{
        @Override
        public void run() {
            super.run();
            if (istuFirst){
                File f1,f2,f3,f4,f5,f6,f7,f8;
                if (image!=null&&!image.equals("")&&image.length()>1) {
                    str = image.split("\\|");
                    if (str.length > 0) {
                        f1 = new FileStorage("comzhuye").createCropFile(str[0],null);
                        if (f1.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comzhuye/"+str[0]);
                        } else {
                            uploadImage(str[0], pd);
                        }
                    }
                    if (str.length > 1) {
                        f2 = new FileStorage("comzhuye").createCropFile(str[1],null);
                        if (f2.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comzhuye/"+str[1]);
                        }else {
                            uploadImage(str[1], pd);
                        }
                    }
                    if (str.length > 2) {
                        f3 = new FileStorage("comzhuye").createCropFile(str[2],null);
                        if (f3.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comzhuye/"+str[2]);
                        }else {
                            uploadImage(str[2], pd);
                        }
                    }
                    if (str.length > 3) {
                        f4 = new FileStorage("comzhuye").createCropFile(str[3],null);
                        if (f4.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comzhuye/"+str[3]);
                        }else {
                            uploadImage(str[3], pd);
                        }
                    }
                    if (str.length > 4) {
                        f5 = new FileStorage("comzhuye").createCropFile(str[4],null);
                        if (f5.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comzhuye/"+str[4]);
                        }else {
                            uploadImage(str[4], pd);
                        }
                    }
                    if (str.length > 5) {
                        f6 = new FileStorage("comzhuye").createCropFile(str[5],null);
                        if (f6.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comzhuye/"+str[5]);
                        }else {
                            uploadImage(str[5], pd);
                        }
                    }
                    if (str.length > 6) {
                        f7 = new FileStorage("comzhuye").createCropFile(str[6],null);
                        if (f7.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comzhuye/"+str[6]);
                        }else {
                            uploadImage(str[6], pd);
                        }
                    }
                    if (str.length > 7) {
                        f8 = new FileStorage("comzhuye").createCropFile(str[7],null);
                        if (f8.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comzhuye/"+str[7]);
                        }else {
                            uploadImage(str[7], pd);
                        }
                    }
                }
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
            istuFirst = false;
        }
    }

    @Override
    public void onClick(View v) {
        String imageIndex = "";
        switch (v.getId()){
            case R.id.cb_moshiyi:
                cb_moshiyi.setChecked(true);
                cb_moshier.setChecked(false);
                cb_moshisan.setChecked(false);
                cb_moshisi.setChecked(false);
                cb_moshiwu.setChecked(false);
                break;
            case R.id.cb_moshier:
                cb_moshier.setChecked(true);
                cb_moshiyi.setChecked(false);
                cb_moshisan.setChecked(false);
                cb_moshisi.setChecked(false);
                cb_moshiwu.setChecked(false);
                break;
            case R.id.cb_moshisan:
                cb_moshisan.setChecked(true);
                cb_moshier.setChecked(false);
                cb_moshiyi.setChecked(false);
                cb_moshisi.setChecked(false);
                cb_moshiwu.setChecked(false);
                break;
            case R.id.cb_moshisi:
                cb_moshisan.setChecked(false);
                cb_moshier.setChecked(false);
                cb_moshiyi.setChecked(false);
                cb_moshisi.setChecked(true);
                cb_moshiwu.setChecked(false);
                break;
            case R.id.cb_moshiwu:
                cb_moshisan.setChecked(false);
                cb_moshier.setChecked(false);
                cb_moshiyi.setChecked(false);
                cb_moshisi.setChecked(false);
                cb_moshiwu.setChecked(true);
                break;
            case R.id.iv_moshiyi:
//                cb_moshiyi.setChecked(true);
//                cb_moshier.setChecked(false);
//                cb_moshisan.setChecked(false);
                imageIndex = "01";
                startActivity(new Intent(CompanyUpdateTwoActivity.this,BiImageActivity.class).putExtra("imageIndex",imageIndex));
                break;
            case R.id.iv_moshier:
//                cb_moshier.setChecked(true);
//                cb_moshiyi.setChecked(false);
//                cb_moshisan.setChecked(false);
                imageIndex = "02";
                startActivity(new Intent(CompanyUpdateTwoActivity.this,BiImageActivity.class).putExtra("imageIndex",imageIndex));
                break;
            case R.id.iv_moshisan:
//                cb_moshisan.setChecked(true);
//                cb_moshier.setChecked(false);
//                cb_moshiyi.setChecked(false);
                imageIndex = "03";
                startActivity(new Intent(CompanyUpdateTwoActivity.this,BiImageActivity.class).putExtra("imageIndex",imageIndex));
                break;
            case R.id.iv_moshisi:
//                cb_moshisan.setChecked(true);
//                cb_moshier.setChecked(false);
//                cb_moshiyi.setChecked(false);
                imageIndex = "04";
                startActivity(new Intent(CompanyUpdateTwoActivity.this,BiImageActivity.class).putExtra("imageIndex",imageIndex));
                break;
            case R.id.iv_moshiwu:
//                cb_moshisan.setChecked(true);
//                cb_moshier.setChecked(false);
//                cb_moshiyi.setChecked(false);
                imageIndex = "05";
                startActivity(new Intent(CompanyUpdateTwoActivity.this,BiImageActivity.class).putExtra("imageIndex",imageIndex));
                break;
        }

    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FunctionConfig.CAMERA_RESULT:
                    if (data != null) {
                        List<LocalMedia> medias = (List<LocalMedia>) data.getSerializableExtra(FunctionConfig.EXTRA_RESULT);
                        if (medias != null) {
                            selectMedia.add(medias.get(0));
                            adapter.setList(selectMedia);
                            adapter.notifyDataSetChanged();
//                            imagePaths1.clear();
                            imagePaths1.add(selectMedia.get(0).getCompressPath());
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        if (imagePaths1!=null){
            imagePaths1.clear();
            imagePaths1=null;
        }
        if (selectMedia!=null){
            selectMedia.clear();
            selectMedia=null;
        }
        if (headThread!=null){
            headThread.interrupt();
            headThread=null;
        }
    }
}

