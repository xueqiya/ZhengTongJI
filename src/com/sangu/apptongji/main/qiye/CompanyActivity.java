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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.FullyGridLayoutManager;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.FileUtils;
import com.sangu.apptongji.utils.SelectPicPopupWindow;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016-12-29.
 */

public class CompanyActivity extends BaseActivity implements IQiYeDetailView,View.OnClickListener{
    private ImageView iv_back;
    private TextView tv_company_name;
    private TextView tv_company_jianjie;
    private TextView tv_major1;
    private TextView tv_major2;
    private TextView tv_major3;
    private TextView tv_major4;
    private TextView tv_faren;
    private TextView tv_company_zhizhao;
    private TextView tv_company_phone;
    private TextView tv_company_address;
    private static final int UPDATE_COMJIANJIE = 5;
    private static final int UPDATE_MAJOR1 = 7;
    private static final int UPDATE_MAJOR2 = 8;
    private static final int UPDATE_MAJOR3 = 9;
    private static final int UPDATE_MAJOR4 = 10;
    private static final int UPDATE_COMADDRESS = 13;
    private boolean hasChange=false;
    private IQiYeInfoPresenter presenter;
    private String isFirst;
    private Button btn_send;
    private String image="",qiyeId="",name,signJianJie,address;
    private String image1="0",zy1Resv3="",upDescribe1="",upName1="";
    private String image2="0",zy2Resv3="",upDescribe2="",upName2="";
    private String image3="0",zy3Resv3="",upDescribe3="",upName3="";
    private String image4="0",zy4Resv3="",upDescribe4="",upName4="";
    private String [] str;
    private CustomProgressDialog pd;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private SelectPicPopupWindow menuWindow=null;

    private ArrayList<String> imagePaths1 = new ArrayList<>();
    private ArrayList<String> imagePaths2 = new ArrayList<>();
    HeadThread headThread=null;
    ImaThread imaThread=null;

    private boolean istuFirst = true;
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
                case 2:
                    adapter.notifyDataSetChanged();
                    if (imaThread!=null){
                        imaThread.interrupt();
                        imaThread = null;
                    }
                    if (pd!=null&&pd.isShowing()){
                        pd.dismiss();
                    }
                    break;
            }
        }
    };

    private void uploadImage(final String image, CustomProgressDialog pd) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        String url = FXConstant.URL_QIYE_TOUXIANG+image;
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
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
        File file = new FileStorage("comtoux").createCropFile(imageName,null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(CompanyActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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
            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comtoux/"+imageName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap compressScale(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if(baos.toByteArray().length / 1024>500) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1000 > 80) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private void saveToSD2(Bitmap mBitmap,String imageName,String childPath,int i) {
        File file = new FileStorage("catch").createCropFile(imageName,childPath);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(CompanyActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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
            mBitmap = compressScale(mBitmap);
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
            loadImage2(Environment.getExternalStorageDirectory() + "/zhengshier/catch/" + imageName);
            if (imagePaths2.size()>0&&i>=imagePaths2.size()-1){
                handler.sendEmptyMessage(2);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadImage2(String filepath){
        imagePaths1.add(filepath);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_qiyeinfo);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_company_name = (TextView) findViewById(R.id.tv_name);
        tv_company_jianjie = (TextView) findViewById(R.id.tv_sign);
        tv_major1 = (TextView) findViewById(R.id.tv_major1);
        tv_major2 = (TextView) findViewById(R.id.tv_major2);
        tv_major3 = (TextView) findViewById(R.id.tv_major3);
        tv_major4 = (TextView) findViewById(R.id.tv_major4);
        tv_faren = (TextView) findViewById(R.id.tv_faren);
        tv_company_zhizhao = (TextView) findViewById(R.id.tv_zhizhao);
        tv_company_phone = (TextView) findViewById(R.id.tv_phone);
        tv_company_address = (TextView) findViewById(R.id.tv_company_address);
        WeakReference<CompanyActivity> reference =  new WeakReference<CompanyActivity>(CompanyActivity.this);
        presenter = new QiYeInfoPresenter(this,reference.get());
        qiyeId = this.getIntent().getStringExtra("qiyeId");
        isFirst = this.getIntent().getStringExtra("isFirst");
        initView();
        initViews();
        if (qiyeId!=null&&!"".equals(qiyeId)) {
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
            presenter.loadQiYeInfo(qiyeId);
        }
    }
    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(CompanyActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(CompanyActivity.this, onAddPicClickListener);
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
                        PictureConfig.getInstance().externalPicturePreview(CompanyActivity.this, position, selectMedia);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(CompanyActivity.this, selectMedia.get(position).getPath());
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

    private void initView() {
        //设置监听
        this.findViewById(R.id.re_major1).setOnClickListener(this);
        this.findViewById(R.id.re_major2).setOnClickListener(this);
        this.findViewById(R.id.re_major3).setOnClickListener(this);
        this.findViewById(R.id.re_major4).setOnClickListener(this);
        this.findViewById(R.id.re_name).setOnClickListener(this);
        this.findViewById(R.id.re_sign).setOnClickListener(this);
        this.findViewById(R.id.re_faren).setOnClickListener(this);
        this.findViewById(R.id.re_zhizhao).setOnClickListener(this);
        this.findViewById(R.id.re_phone).setOnClickListener(this);
        this.findViewById(R.id.re_company_address).setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btn_send = (Button) this.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePaths1.size()>0){
                    if (imagePaths1.size()<=8) {
                        send();
                    }else {
                        Toast.makeText(CompanyActivity.this, "图片不能多于八张....",
                                Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CompanyActivity.this, "请选择图片....",
                            Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    private void selectImgs(){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        menuWindow = new SelectPicPopupWindow(CompanyActivity.this,0,itemsOnClick);
        //设置弹窗位置
        menuWindow.showAtLocation(CompanyActivity.this.findViewById(R.id.llImage), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
        WeakReference<CompanyActivity> reference = new WeakReference<CompanyActivity>(CompanyActivity.this);
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
            imagePaths2.clear();
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
                    imagePaths2.add(selectMedia.get(i).getPath());
                }
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
                if (imagePaths2.size()>0){
                    if (pd!=null){
                        pd.setMessage("正在处理图片资源...");
                        pd.show();
                    }else {
                        WeakReference<CompanyActivity> reference = new WeakReference<CompanyActivity>(CompanyActivity.this);
                        pd = CustomProgressDialog.createDialog(reference.get());
                        pd.setCancelable(false);
                        pd.setMessage("正在处理图片资源...");
                        pd.show();
                    }
                    imaThread = new ImaThread();
                    imaThread.start();
                }
            }
        }

        @Override
        public void onSelectSuccess(LocalMedia media) {
            imagePaths1.clear();
            imagePaths2.clear();
            // 单选回调
            selectMedia.add(media);
            if (selectMedia != null) {
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
                imagePaths2.add(media.getPath());
                if (imagePaths2.size()>0) {
                    if (pd!=null){
                        pd.setMessage("正在处理图片资源...");
                        pd.show();
                    }else {
                        WeakReference<CompanyActivity> reference = new WeakReference<CompanyActivity>(CompanyActivity.this);
                        pd = CustomProgressDialog.createDialog(reference.get());
                        pd.setCancelable(false);
                        pd.setMessage("正在处理图片资源...");
                        pd.show();
                    }
                    imaThread = new ImaThread();
                    imaThread.start();
                }
            }
        }
    };

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
        if (imagePaths2!=null){
            imagePaths2.clear();
            imagePaths2=null;
        }
        if (selectMedia!=null){
            selectMedia.clear();
            selectMedia=null;
        }
        if (headThread!=null){
            headThread.interrupt();
            headThread=null;
        }
        if (imaThread!=null){
            imaThread.interrupt();
            imaThread=null;
        }
    }
    private void send() {
        if (pd!=null){
            pd.setMessage("正在上传...");
            pd.show();
        }else {
            WeakReference<CompanyActivity> reference = new WeakReference<CompanyActivity>(CompanyActivity.this);
            pd = CustomProgressDialog.createDialog(reference.get());
            pd.setCancelable(false);
            pd.setMessage("正在上传...");
            pd.show();
        }
        String key = "comImage";
        List<Param> param=new ArrayList<>();
        param.add(new Param("companyId", qiyeId));
        param.add(new Param("companyName",name));
        OkHttpManager.getInstance().posts(param,key,imagePaths1,FXConstant.URL_UPDATE_QIYE,new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (pd!=null) {
                    pd.dismiss();
                }
                String code = jsonObject.getString("code");
                if (code.equals("SUCCESS")) {
                    FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/catch/"));
                    FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/comtoux/"));
                    Toast.makeText(getApplicationContext(), "更新成功！", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "更新失败,code:" + code, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(String errorMsg) {
                if (pd!=null) {
                    pd.dismiss();
                }
                Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试" + errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_name:
                Toast.makeText(CompanyActivity.this,"企业名称一经确定不可修改！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_sign:
                startActivityForResult(new Intent(CompanyActivity.this,CompanyUpdateActivity.class).putExtra("type", CompanyUpdateActivity.TYPE_SIGN).putExtra("companyId",qiyeId).putExtra("companyName",name).putExtra("info",signJianJie), UPDATE_COMJIANJIE);
                break;
            case R.id.re_faren:
                Toast.makeText(CompanyActivity.this,"法人名称一经确定不可修改！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_zhizhao:
                Toast.makeText(CompanyActivity.this,"企业执照一经确定不可修改！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_phone:
                Toast.makeText(CompanyActivity.this,"企业电话一经确定不可修改！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_major1:
                String isDYC;
                if (isFirst.equals("11")) {
                    isDYC = "11";
                }else {
                    isDYC = "12";
                }
                startActivityForResult(new Intent(CompanyActivity.this,CompanyUpdateTwoActivity.class).putExtra("type", CompanyUpdateTwoActivity.TYPE_MAJOR1).putExtra("isFirst",isDYC).putExtra("image",image1)
                        .putExtra("zyResv3",zy1Resv3).putExtra("upName",upName1).putExtra("upDescribe",upDescribe1).putExtra("companyId",qiyeId).putExtra("companyName",name), UPDATE_MAJOR1);
                break;
            case R.id.re_major2:
                String isDYC2;
                if (isFirst.equals("11")) {
                    isDYC2 = "11";
                }else {
                    isDYC2 = "12";
                }
                startActivityForResult(new Intent(CompanyActivity.this,CompanyUpdateTwoActivity.class).putExtra("type", CompanyUpdateTwoActivity.TYPE_MAJOR2).putExtra("isFirst",isDYC2).putExtra("image",image2)
                        .putExtra("zyResv3",zy2Resv3).putExtra("upName",upName2).putExtra("upDescribe",upDescribe2).putExtra("companyId",qiyeId).putExtra("companyName",name), UPDATE_MAJOR2);
                break;
            case R.id.re_major3:
                String isDYC3;
                if (isFirst.equals("11")) {
                    isDYC3 = "11";
                }else {
                    isDYC3 = "12";
                }
                startActivityForResult(new Intent(CompanyActivity.this,CompanyUpdateTwoActivity.class).putExtra("type", CompanyUpdateTwoActivity.TYPE_MAJOR3).putExtra("isFirst",isDYC3).putExtra("image",image3)
                        .putExtra("zyResv3",zy3Resv3).putExtra("upName",upName3).putExtra("upDescribe",upDescribe3).putExtra("companyId",qiyeId).putExtra("companyName",name), UPDATE_MAJOR3);
                break;
            case R.id.re_major4:
                String isDYC4;
                if (isFirst.equals("11")) {
                    isDYC4 = "11";
                }else {
                    isDYC4 = "12";
                }
                startActivityForResult(new Intent(CompanyActivity.this,CompanyUpdateTwoActivity.class).putExtra("type", CompanyUpdateTwoActivity.TYPE_MAJOR4).putExtra("isFirst",isDYC4).putExtra("image",image4)
                        .putExtra("zyResv3",zy4Resv3).putExtra("upName",upName4).putExtra("upDescribe",upDescribe4).putExtra("companyId",qiyeId).putExtra("companyName",name), UPDATE_MAJOR4);
                break;
            case R.id.re_company_address:
                startActivityForResult(new Intent(CompanyActivity.this,CompanyUpdateActivity.class).putExtra("type", CompanyUpdateActivity.TYPE_COMADDRESS).putExtra("companyId",qiyeId).putExtra("companyName",name).putExtra("info",address), UPDATE_COMADDRESS);
                break;
            case R.id.iv_back:
                finish();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UPDATE_MAJOR1:
                    String ma1 = data.getStringExtra("value");
                    if (ma1 != null) {
                        tv_major1.setText(ma1);
                        hasChange = true;
                    }
                    break;
                case UPDATE_MAJOR2:
                    String ma2 = data.getStringExtra("value");
                    if (ma2 != null) {
                        tv_major2.setText(ma2);
                        hasChange = true;
                    }
                    break;
                case UPDATE_MAJOR3:
                    String ma3 = data.getStringExtra("value");
                    if (ma3 != null) {
                        tv_major3.setText(ma3);
                        hasChange = true;
                    }
                    break;
                case UPDATE_MAJOR4:
                    String ma4 = data.getStringExtra("value");
                    if (ma4 != null) {
                        tv_major4.setText(ma4);
                        hasChange = true;
                    }
                    break;
                case UPDATE_COMADDRESS:
                    String comadd = data.getStringExtra("value");
                    if (comadd != null) {
                        tv_company_address.setText(comadd);
                        hasChange = true;
                    }
                    break;
                case UPDATE_COMJIANJIE:
                    String jianjie = data.getStringExtra("value");
                    if (jianjie != null) {
                        tv_company_jianjie.setText(jianjie);
                        hasChange = true;
                    }
                    break;
                case FunctionConfig.CAMERA_RESULT:
                        if (data != null) {
                            List<LocalMedia> medias = (List<LocalMedia>) data.getSerializableExtra(FunctionConfig.EXTRA_RESULT);
                            if (medias != null) {
                                selectMedia.add(medias.get(0));
                                adapter.setList(selectMedia);
                                adapter.notifyDataSetChanged();
                                imagePaths2.clear();
                                imagePaths2.add(selectMedia.get(0).getPath());
                                WeakReference<CompanyActivity> reference = new WeakReference<CompanyActivity>(CompanyActivity.this);
                                if (pd!=null){
                                    pd.setMessage("正在处理图片资源...");
                                    pd.show();
                                }else {
                                    pd = CustomProgressDialog.createDialog(reference.get());
                                    pd.setCancelable(false);
                                    pd.setMessage("正在处理图片资源...");
                                    pd.show();
                                }
                                imaThread = new ImaThread();
                                imaThread.start();
                            }
                        }
                    break;
//                case PHOTO_REQUEST_GALLERY:
//                    gridView1.setVisibility(View.VISIBLE);
//                    imagePaths1.clear();
//                    imagePaths2 = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
//                    if (imaThread == null) {
//                        if (pd!=null){
//                            pd.setMessage("正在处理图片资源...");
//                            pd.show();
//                        }else {
//                            pd = new CustomProgressDialog(this);
//                            pd.setCancelable(false);
//                            pd.setMessage("正在处理图片资源...");
//                            pd.show();
//                        }
//                        imaThread = new ImaThread();
//                        imaThread.start();
//                    }
//                    break;
            }
            presenter.loadQiYeInfo(qiyeId);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddHHmmssSS");
        return dateFormat.format(date);
    }

    class ImaThread extends Thread{
        @Override
        public void run() {
            super.run();
            for (int i = 0;i<imagePaths2.size();i++){
                Bitmap bm = BitmapFactory.decodeFile(imagePaths2.get(i));
                String imName = getNowTime() + i + ".png";
                saveToSD2(bm,imName,null,i);
            }
        }
    }

    class HeadThread extends Thread{
        @Override
        public void run() {
            super.run();
            if (istuFirst){
                File f1,f2,f3,f4,f5,f6,f7,f8;
                if (!image.equals("")&&image.length()>1) {
                    str = image.split("\\|");
                    if (str.length > 0) {
                        f1 = new FileStorage("comtoux").createCropFile(str[0],null);
                        if (f1.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comtoux/"+str[0]);
                        } else {
                            uploadImage(str[0], pd);
                        }
                    }
                    if (str.length > 1) {
                        f2 = new FileStorage("comtoux").createCropFile(str[1],null);
                        if (f2.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comtoux/"+str[1]);
                        }else {
                            uploadImage(str[1], pd);
                        }
                    }
                    if (str.length > 2) {
                        f3 = new FileStorage("comtoux").createCropFile(str[2],null);
                        if (f3.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comtoux/"+str[2]);
                        }else {
                            uploadImage(str[2], pd);
                        }
                    }
                    if (str.length > 3) {
                        f4 = new FileStorage("comtoux").createCropFile(str[3],null);
                        if (f4.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comtoux/"+str[3]);
                        }else {
                            uploadImage(str[3], pd);
                        }
                    }
                    if (str.length > 4) {
                        f5 = new FileStorage("comtoux").createCropFile(str[4],null);
                        if (f5.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comtoux/"+str[4]);
                        }else {
                            uploadImage(str[4], pd);
                        }
                    }
                    if (str.length > 5) {
                        f6 = new FileStorage("comtoux").createCropFile(str[5],null);
                        if (f6.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comtoux/"+str[5]);
                        }else {
                            uploadImage(str[5], pd);
                        }
                    }
                    if (str.length > 6) {
                        f7 = new FileStorage("comtoux").createCropFile(str[6],null);
                        if (f7.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comtoux/"+str[6]);
                        }else {
                            uploadImage(str[6], pd);
                        }
                    }
                    if (str.length > 7) {
                        f8 = new FileStorage("comtoux").createCropFile(str[7],null);
                        if (f8.exists()){
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/comtoux/"+str[7]);
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

    @Override
    public void back(View view){
        ckeckChange();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            ckeckChange();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ckeckChange(){
        if(hasChange){
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void updateQiyeInfo(QiYeInfo user) {
        image = TextUtils.isEmpty(user.getComImage())?"":user.getComImage();
        upDescribe1 = TextUtils.isEmpty(user.getUpDescribe1())?"":user.getUpDescribe1();
        upDescribe2 = TextUtils.isEmpty(user.getUpDescribe2())?"":user.getUpDescribe2();
        upDescribe3 = TextUtils.isEmpty(user.getUpDescribe3())?"":user.getUpDescribe3();
        upDescribe4 = TextUtils.isEmpty(user.getUpDescribe4())?"":user.getUpDescribe4();
        zy1Resv3 = TextUtils.isEmpty(user.getZy1resv3())?"01":user.getZy1resv3();
        zy2Resv3 = TextUtils.isEmpty(user.getZy2resv3())?"01":user.getZy2resv3();
        zy3Resv3 = TextUtils.isEmpty(user.getZy3resv3())?"01":user.getZy3resv3();
        zy4Resv3 = TextUtils.isEmpty(user.getZy4resv3())?"01":user.getZy4resv3();
        upName1 = TextUtils.isEmpty(user.getUpName1())?"":user.getUpName1();
        upName2 = TextUtils.isEmpty(user.getUpName2())?"":user.getUpName2();
        upName3 = TextUtils.isEmpty(user.getUpName3())?"":user.getUpName3();
        upName4 = TextUtils.isEmpty(user.getUpName4())?"":user.getUpName4();
        image1 = TextUtils.isEmpty(user.getZyImage1())?"0":user.getZyImage1();
        image2 = TextUtils.isEmpty(user.getZyImage2())?"0":user.getZyImage2();
        image3 = TextUtils.isEmpty(user.getZyImage3())?"0":user.getZyImage3();
        image4 = TextUtils.isEmpty(user.getZyImage4())?"0":user.getZyImage4();
        Log.e("imagecom",image);
        if (!image.equals("")) {
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
        name = TextUtils.isEmpty(user.getCompanyName())?"":user.getCompanyName();
        String faren = TextUtils.isEmpty(user.getResv3())?"":user.getResv3();
        String zhizhao = TextUtils.isEmpty(user.getComEmail())?"":user.getComEmail();
        String phone = TextUtils.isEmpty(user.getComTel())?"":user.getComTel();
        address = TextUtils.isEmpty(user.getComAddress())?"":user.getComAddress();
        signJianJie = TextUtils.isEmpty(user.getComSignature())?"":user.getComSignature();
        String name1 = "";
        try {
            name1 = URLDecoder.decode(name, "UTF-8");
            signJianJie = URLDecoder.decode(signJianJie, "UTF-8");
            faren = URLDecoder.decode(faren, "UTF-8");
            address = URLDecoder.decode(address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        tv_company_name.setText(name1);
        tv_company_jianjie.setText(signJianJie);
        tv_company_zhizhao.setText(zhizhao);
        tv_company_phone.setText(phone);
        tv_faren.setText(faren);
        tv_company_address.setText(address.equals("")?"未设置":address);
        tv_major1.setText(TextUtils.isEmpty(user.getUpName1())?"专业项目1":user.getUpName1());
        tv_major2.setText(TextUtils.isEmpty(user.getUpName2())?"专业项目2":user.getUpName2());
        tv_major3.setText(TextUtils.isEmpty(user.getUpName3())?"专业项目3":user.getUpName3());
        tv_major4.setText(TextUtils.isEmpty(user.getUpName4())?"专业项目4":user.getUpName4());
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
}