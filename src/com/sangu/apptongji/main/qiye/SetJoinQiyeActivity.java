package com.sangu.apptongji.main.qiye;

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
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.sangu.apptongji.main.utils.FullyGridLayoutManager;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.SelectPicPopupWindow;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017-04-28.
 */

public class SetJoinQiyeActivity extends BaseActivity {

    private CheckBox cb_shoufeijiaru;
    private EditText et_jiarujine;
    private EditText et_content;
    private Button btn_commit;
    private ArrayList<String> imagePaths1=null;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private SelectPicPopupWindow menuWindow=null;

    private String image=null,body=null,feiyong=null;
    private String [] str;
    public String biaoshi,companyName,companId;
    private boolean istuFirst = true;
    HeadThread headThread=null;
    CustomProgressDialog pd = null;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (pd!=null&&pd.isShowing()) {
                        pd.dismiss();
                    }
                    adapter.notifyDataSetChanged();
                    if (headThread!=null) {
                        headThread.interrupt();
                        headThread = null;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (headThread!=null) {
            headThread.interrupt();
            headThread = null;
        }
        if (selectMedia!=null){
            selectMedia.clear();
            selectMedia=null;
        }
        if (imagePaths1!=null){
            imagePaths1.clear();
            imagePaths1=null;
        }
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_joinqiye_set);
        WeakReference<SetJoinQiyeActivity> reference =  new WeakReference<SetJoinQiyeActivity>(SetJoinQiyeActivity.this);
        pd = CustomProgressDialog.createDialog(reference.get());
        pd.setMessage("正在加载数据...");
        pd.setCancelable(true);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                pd.dismiss();
            }
        });
        pd.show();
        imagePaths1 = new ArrayList<>();
        body = this.getIntent().getStringExtra("body");
        feiyong = this.getIntent().getStringExtra("feiyong");
        image = this.getIntent().getStringExtra("images");
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        companId = this.getIntent().getStringExtra("companId");
        companyName = this.getIntent().getStringExtra("companyName");
        initView();
        initViews();
    }
    private void initView() {
        btn_commit = (Button) findViewById(R.id.btn_commit);
        et_content = (EditText) findViewById(R.id.et_content);
        et_jiarujine = (EditText) findViewById(R.id.et_jiarujine);
        cb_shoufeijiaru = (CheckBox) findViewById(R.id.cb_shoufeijiaru);
        et_jiarujine.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et_jiarujine.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable edt)
            {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });
        et_content.setText(body);
        if (feiyong!=null&&Double.parseDouble(feiyong)>0){
            cb_shoufeijiaru.setChecked(true);
            et_jiarujine.setText(feiyong);
            et_jiarujine.setEnabled(true);
        }else {
            et_jiarujine.setEnabled(false);
            cb_shoufeijiaru.setChecked(false);
        }
        cb_shoufeijiaru.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_shoufeijiaru.isChecked()){
                    et_jiarujine.setEnabled(true);
                }else {
                    et_jiarujine.setEnabled(false);
                    et_jiarujine.setText(null);
                }
            }
        });
        Log.e("setjoinqiye,2",image);
        if (image!=null&&!"".equals(image)) {
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
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString().trim();
                String feiyong = et_jiarujine.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(getApplicationContext(), "请输入文字内容....",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imagePaths1.size() == 0) {
                    Toast.makeText(getApplicationContext(), "请选择图片....",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else if (imagePaths1.size()>8){
                    Toast.makeText(getApplicationContext(), "图片不能多于八张....",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pd!=null&&!pd.isShowing()) {
                    pd.show();
                }
                String key = null;
                List<Param> params=new ArrayList<>();
                params.add(new Param("companyName", companyName));
                params.add(new Param("companyId", companId));
                if (biaoshi.equals("0")){
                    key = "recruitImages";
                    params.add(new Param("recruitBody",content));
                    if (cb_shoufeijiaru.isChecked()) {
                        params.add(new Param("recruitMoney", feiyong));
                    }else {
                        params.add(new Param("recruitMoney", "0"));
                    }
                }else {
                    key = "joinImages";
                    params.add(new Param("joinBody",content));
                    if (cb_shoufeijiaru.isChecked()) {
                        params.add(new Param("joinMoney", feiyong));
                    }else {
                        params.add(new Param("joinMoney", "0"));
                    }
                }
                OkHttpManager.getInstance().posts(params,key,imagePaths1, FXConstant.URL_UPDATE_QIYE, new OkHttpManager.HttpCallBack() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (pd!=null&&pd.isShowing()) {
                            pd.dismiss();
                        }
                        Log.e("param,obj",jsonObject.toJSONString());
                        String code = jsonObject.getString("code");
                        Log.e("param,code",code);
                        if (code.equals("SUCCESS")) {
                            Toast.makeText(getApplicationContext(), "提交成功",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"网络繁忙，请稍后再试",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(String errorMsg) {
                        if (pd!=null&&pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(getApplicationContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(SetJoinQiyeActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(SetJoinQiyeActivity.this, onAddPicClickListener);
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
                        PictureConfig.getInstance().externalPicturePreview(SetJoinQiyeActivity.this, position, selectMedia);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(SetJoinQiyeActivity.this, selectMedia.get(position).getPath());
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
                    imagePaths1.remove(position);
                    selectMedia.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    private void selectImgs(){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SetJoinQiyeActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        menuWindow = new SelectPicPopupWindow(SetJoinQiyeActivity.this,0,itemsOnClick);
        //设置弹窗位置
        menuWindow.showAtLocation(SetJoinQiyeActivity.this.findViewById(R.id.llImage), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
        WeakReference<SetJoinQiyeActivity> reference = new WeakReference<SetJoinQiyeActivity>(SetJoinQiyeActivity.this);
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
            // 多选回调
            selectMedia = resultList;
            Log.i("callBack_result", selectMedia.size() + "");
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
            if (selectMedia != null) {
                adapter.setList(selectMedia);
                adapter.notifyDataSetChanged();
                for (int i=0;i<selectMedia.size();i++){
                    loadImage2(selectMedia.get(i).getPath());
                }
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
                for (int i=0;i<selectMedia.size();i++){
                    loadImage2(selectMedia.get(i).getPath());
                }
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case FunctionConfig.CAMERA_RESULT:
                    if (data != null) {
                        List<LocalMedia> medias = (List<LocalMedia>) data.getSerializableExtra(FunctionConfig.EXTRA_RESULT);
                        if (selectMedia != null) {
                            selectMedia.add(medias.get(0));
                            adapter.setList(selectMedia);
                            adapter.notifyDataSetChanged();
                            imagePaths1.add(medias.get(0).getPath());
                        }
                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    class HeadThread extends Thread{
        @Override
        public void run() {
            super.run();
            if (istuFirst){
                File f1,f2,f3,f4,f5,f6,f7,f8;
                if (image!=null&&!"".equals(image)&&image.length()>1) {
                    str = image.split("\\|");
                    if (str.length > 0) {
                        f1 = new FileStorage("qiyeImage").createCropFile(str[0],null);
                        if (f1.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/qiyeImage/"+str[0]);
                        } else {
                            uploadImage(str[0], pd);
                        }
                    }
                    if (str.length > 1) {
                        f2 = new FileStorage("qiyeImage").createCropFile(str[1],null);
                        if (f2.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/qiyeImage/"+str[1]);
                        } else {
                            uploadImage(str[1], pd);
                        }
                    }
                    if (str.length > 2) {
                        f3 = new FileStorage("qiyeImage").createCropFile(str[2],null);
                        if (f3.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/qiyeImage/"+str[2]);
                        } else {
                            uploadImage(str[2], pd);
                        }
                    }
                    if (str.length > 3) {
                        f4 = new FileStorage("qiyeImage").createCropFile(str[3],null);
                        if (f4.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/qiyeImage/"+str[3]);
                        } else {
                            uploadImage(str[3], pd);
                        }
                    }
                    if (str.length > 4) {
                        f5 = new FileStorage("qiyeImage").createCropFile(str[4],null);
                        if (f5.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/qiyeImage/"+str[4]);
                        } else {
                            uploadImage(str[4], pd);
                        }
                    }
                    if (str.length > 5) {
                        f6 = new FileStorage("qiyeImage").createCropFile(str[5],null);
                        if (f6.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/qiyeImage/"+str[5]);
                        } else {
                            uploadImage(str[5], pd);
                        }
                    }
                    if (str.length > 6) {
                        f7 = new FileStorage("qiyeImage").createCropFile(str[6],null);
                        if (f7.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/qiyeImage/"+str[6]);
                        } else {
                            uploadImage(str[6], pd);
                        }
                    }
                    if (str.length > 7) {
                        f8 = new FileStorage("qiyeImage").createCropFile(str[7],null);
                        if (f8.exists()) {
                            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/qiyeImage/"+str[7]);
                        } else {
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
    private void uploadImage(final String image, final CustomProgressDialog pd) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        String url=null;
        if ("0".equals(biaoshi)){
            url = FXConstant.URL_QIYE_ZHAOPIN+image;
        }else {
            url = FXConstant.URL_QIYE_JIAMENG+image;
        }
        try {
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            Bitmap bm = BitmapFactory.decodeStream(is);
            if (bm!=null) {
                saveToSD(bm,image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToSD(Bitmap mBitmap,String imageName) {
        File file = new FileStorage("qiyeImage").createCropFile(imageName,null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(SetJoinQiyeActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            FileOutputStream fos = new FileOutputStream(f);
            int options = 100;
            // 如果大于80kb则再次压缩,最多压缩三次
            while (baos.toByteArray().length / 1024 > 200 && options > 10) {
                // 清空baos
                baos.reset();
                // 这里压缩options%，把压缩后的数据存放到baos中
                mBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 30;
            }
            fos.write(baos.toByteArray());
            fos.close();
            baos.close();
            loadImage1(Environment.getExternalStorageDirectory()+"/zhengshier/qiyeImage/"+imageName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
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
    private void loadImage2(String filepath){
        imagePaths1.add(filepath);
    }

}
