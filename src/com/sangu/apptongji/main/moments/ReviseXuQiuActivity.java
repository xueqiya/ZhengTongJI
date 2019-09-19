package com.sangu.apptongji.main.moments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.NomalVideoPlayActivity;
import com.sangu.apptongji.main.adapter.GridImageAdapter;
import com.sangu.apptongji.main.mapsearch.MapPickerActivity;
import com.sangu.apptongji.main.utils.CashierInputFilter;
import com.sangu.apptongji.main.utils.FullyGridLayoutManager;
import com.sangu.apptongji.main.utils.LimitInputTextWatcher;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.FileUtils;
import com.sangu.apptongji.utils.SelectPicPopupWindow;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017-10-26.
 */

public class ReviseXuQiuActivity extends BaseActivity {
    private LinearLayout llImage;
    /*图片适配器*/
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private SelectPicPopupWindow menuWindow=null;

    private String filePath,createTime,dynamicSeq;
    private ArrayList<String> imagePaths1 = null;
    private String mylat="",mylon="",street,image,video,videoPictures;
    // 显示位置的TextView
    // 发送按钮
    private Button btn_send=null;
    // 文本输入
    private EditText et_biaoqian=null;
    private EditText et_miaoshu=null;
    private EditText et_didian=null;
    private EditText et_chujia=null;
    private TextView tv_paidan_type=null;
    private TextView tv_qiehuan=null;
    private FrameLayout fl_video;
    private VideoView mVideoView;
    private EditText et_remark=null;
    private CustomProgressDialog dialog=null;
    ImgThread imgThread =null;
    private String [] str;
    private Handler myHeadImagehandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 4:
                    adapter.notifyDataSetChanged();
                    if (imgThread!=null) {
                        imgThread.interrupt();
                        imgThread = null;
                    }
                    if (dialog!=null&&dialog.isShowing()){
                        dialog.dismiss();
                    }
                    break;
            }
        }
    };
    private String dType;

    private LinearLayout rl_demandType;
    private TextView tv_demandType1,tv_demandType2,tv_demandType3,tv_demandTitle;
    private String demandType="0";

    class ImgThread extends Thread{
        @Override
        public void run() {
            super.run();
            if (str!=null&&str.length>0) {
                if (str.length > 0) {
                    uploadImage(str[0],1);
                }
                if (str.length > 1) {
                    uploadImage(str[1],2);
                }
                if (str.length > 2) {
                    uploadImage(str[2],3);
                }
                if (str.length > 3) {
                    uploadImage(str[3],4);
                }
                if (str.length > 4) {
                    uploadImage(str[4],5);
                }
                if (str.length > 5) {
                    uploadImage(str[5],6);
                }
                if (str.length > 6) {
                    uploadImage(str[6],7);
                }
                if (str.length > 7) {
                    uploadImage(str[7],8);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_renwu);
        dialog = CustomProgressDialog.createDialog(this);
        dialog.setMessage("正在加载数据...");
        dialog.show();
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        imagePaths1 = new ArrayList<>();
        llImage = (LinearLayout) findViewById(R.id.llImage);
        llImage.setFocusable(true);
        llImage.setFocusableInTouchMode(true);
        llImage.requestFocus();
        initRwView();
        initViews();
    }

    private void uploadImage(final String image, int index) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        String url = FXConstant.URL_SOCIAL_PHOTO+image;
        try {
            okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
            okhttp3.Response response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            Bitmap bm = BitmapFactory.decodeStream(is);
            if (bm!=null) {
                saveToSD(bm,image,null,index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveToSD(Bitmap mBitmap,String imageName,String childPath,int index) {
        File file = new FileStorage("catch").createCropFile(imageName,childPath);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            grantUriPermission(this.getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
            // 如果大于100kb则再次压缩,最多压缩三次
            while (baos.toByteArray().length / 1024 > 100 && options > 10) {
                // 清空baos
                baos.reset();
                // 这里压缩options%，把压缩后的数据存放到baos中
                mBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 30;
            }
            fos.write(baos.toByteArray());
            fos.close();
            baos.close();
            loadImage1(Environment.getExternalStorageDirectory() + "/zhengshier/catch/" + imageName,index);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadImage1(String filepath,int index){
        if (filepath!=null) {
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
        if (index==str.length){
            Message msg = new Message();
            msg.what = 4;
            myHeadImagehandler.sendMessage(msg);
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView!=null&&mVideoView.isPlaying()){
            mVideoView.stopPlayback();
        }
        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/zhengshier/catch/"));
    }

    private void initRwView() {
        String data = getIntent().getStringExtra("data");

        tv_demandType1 = (TextView) findViewById(R.id.tv_demandType1);
        tv_demandType2 = (TextView) findViewById(R.id.tv_demandType2);
        tv_demandType3 = (TextView) findViewById(R.id.tv_demandType3);

        rl_demandType = (LinearLayout) findViewById(R.id.rl_demandType);
        tv_demandTitle = (TextView) findViewById(R.id.tv1);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mVideoView = (VideoView) findViewById(R.id.uVideoView);
        fl_video = (FrameLayout) findViewById(R.id.fl_video);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_biaoqian = (EditText) findViewById(R.id.et_biaoqian);
        et_miaoshu = (EditText) findViewById(R.id.et_miaoshu);
        et_didian = (EditText) findViewById(R.id.et_didian);
        et_chujia = (EditText) findViewById(R.id.et_chujia);
       // tv_paidan_type = (TextView) findViewById(R.id.tv_paidan_type);
        et_remark = (EditText) findViewById(R.id.et_chujia1);
      //  tv_qiehuan = (TextView) findViewById(R.id.tv_qiehuan);
        mVideoView.setZOrderOnTop(true);
        et_chujia.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters={new CashierInputFilter()};
        et_chujia.setFilters(filters);
        et_biaoqian.addTextChangedListener(new LimitInputTextWatcher(et_biaoqian));
        et_didian.setFocusable(false);
        et_didian.setFocusableInTouchMode(false);
        et_didian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ReviseXuQiuActivity.this, MapPickerActivity.class).putExtra("biaoshi","01"),5);
            }
        });
//        tv_qiehuan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (tv_paidan_type.getText().toString().trim().equals("向所有人派单")){
//                    tv_paidan_type.setText("仅向企业派单");
//                }else {
//                    tv_paidan_type.setText("向所有人派单");
//                }
//            }
//        });
        btn_send.setText("确认修改");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                UserPermissionUtil.getUserPermission(ReviseXuQiuActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "5", new UserPermissionUtil.UserPermissionListener() {
                    @Override
                    public void onAllow() {

                        final String task_label = et_biaoqian.getText().toString().trim();
                        String chujia = et_chujia.getText().toString().trim();
                        String task_position = mylat+"|"+mylon;
                        String task_locaName = street;
                        String miaoshu = et_miaoshu.getText().toString().trim();
                        String remark = et_remark.getText().toString().trim();
                        String task_jurisdiction = "01";

//                if (tv_paidan_type.getText().equals("向所有人派单")){
//                    task_jurisdiction = "01";
//                }else {
//                    task_jurisdiction = "02";
//                }
                        if (chujia!=null&&!TextUtils.isEmpty(chujia)&&Double.parseDouble(chujia)<=0){
                            Toast.makeText(getApplicationContext(),"出价必须大于0！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (task_label==null||"".equals(task_label)||task_label.length()>6){
                            Toast.makeText(getApplicationContext(),"请输入标签,内容不得超过6位",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (task_position==null||"".equals(task_position)){
                            Toast.makeText(getApplicationContext(),"坐标获取错误,请从新选择",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (task_locaName==null||"".equals(task_locaName)){
                            Toast.makeText(getApplicationContext(),"位置获取错误,请从新选择",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (miaoshu==null||"".equals(miaoshu)){
                            Toast.makeText(getApplicationContext(),"请输入描述",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (remark !=null && remark.length()>10){

                            Toast.makeText(getApplicationContext(),"标注内容不得超过10个字",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        WeakReference<ReviseXuQiuActivity> reference = new WeakReference<ReviseXuQiuActivity>(ReviseXuQiuActivity.this);
                        dialog = CustomProgressDialog.createDialog(reference.get());
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setMessage("正在修改...");
                        dialog.show();
                        List<Param> params=new ArrayList<>();
                        if (chujia!=null&&!TextUtils.isEmpty(chujia)&&Double.parseDouble(chujia)>0){
                            params.add(new Param("floorPrice",Double.parseDouble(chujia)+""));
                            params.add(new Param("contrastTime",getNowTime()));
                        }
                        params.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
                        params.add(new Param("createTime",createTime));
                        params.add(new Param("dynamicSeq",dynamicSeq));
                        //&responseTime=&firstDistance=
                        params.add(new Param("responseTime","0"));
                        params.add(new Param("firstDistance","0"));
                        params.add(new Param("acceptNum","0"));

                        params.add(new Param("content",miaoshu));
                        params.add(new Param("dType",dType));
                        params.add(new Param("task_label",task_label));
                        params.add(new Param("task_position",task_position));
                        params.add(new Param("task_locaName",task_locaName));
                        if (remark != null && !TextUtils.isEmpty(remark)){

                            params.add(new Param("task_jurisdiction",remark));

                        }

                        params.add(new Param("demandType",demandType));

                        if (filePath==null) {
                            OkHttpManager.getInstance().postMoment(params, imagePaths1, FXConstant.URL_XIUGAI_PUBLISH, new OkHttpManager.HttpCallBack() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.d("chen", jsonObject.toJSONString());
                                    String code = jsonObject.getString("code");
                                    if (code.equals("SUCCESS")) {
                                        updateBmob();
                                        Toast.makeText(getApplicationContext(), "修改成功...", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (dialog != null && dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(String errorMsg) {
                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            sendVideo(miaoshu,params);
                        }

                    }

                    @Override
                    public void onBan() {

                        Toast.makeText(getApplicationContext(), "您的账户已被禁止发布派单", Toast.LENGTH_SHORT).show();

                    }

                });

            }
        });
        JSONObject json = JSON.parseObject(data);
        final String floorPrice = json.getString("floorPrice");
        String task_label = json.getString("task_label");
        String content = json.getString("content");
        image = json.getString("image1");
        video = json.getString("video");
        videoPictures = json.getString("videoPictures");
        createTime = json.getString("createTime");
        dType = json.getString("remark");
        dynamicSeq = json.getString("dynamicSeq");
        String task_position = json.getString("task_position");
        street = json.getString("task_locaName");
        String task_jurisdiction = json.getString("task_jurisdiction");
        demandType = json.getString("demandType");

        if (demandType.equals("1")){
            tv_demandTitle.setText("产   品");
        }else if (demandType.equals("2")){
            tv_demandTitle.setText("方   案");
        }else {
            tv_demandTitle.setText("服   务");
        }

        if (task_position!=null) {
            mylat = task_position.split("\\|")[0];
            mylon = task_position.split("\\|")[1];
        }
        if (floorPrice==null||Double.parseDouble(floorPrice)<=0){
            et_chujia.setText(null);
        }else {
            et_chujia.setText(floorPrice);
        }
        et_biaoqian.setText(task_label);
        et_miaoshu.setText(content);
        et_didian.setText(street);
        if ("02".equals(task_jurisdiction) || "01".equals(task_jurisdiction)){

        }else {
            et_remark.setText(task_jurisdiction);
        }

        tv_demandTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (rl_demandType.getVisibility() == View.GONE){

                    rl_demandType.setVisibility(View.VISIBLE);

                }else {

                    rl_demandType.setVisibility(View.GONE);

                }

            }
        });

        tv_demandType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_demandType.setVisibility(View.GONE);
                tv_demandTitle.setText("服   务");
                demandType = "0";
            }
        });
        tv_demandType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_demandType.setVisibility(View.GONE);
                tv_demandTitle.setText("产   品");
                demandType = "1";
            }
        });
        tv_demandType3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_demandType.setVisibility(View.GONE);
                tv_demandTitle.setText("方   案");
                demandType = "2";
            }
        });


//        if ("02".equals(task_jurisdiction)){
//            tv_paidan_type.setText("仅向企业派单");
//        }else {
//            tv_paidan_type.setText("向所有人派单");
//        }

        if (videoPictures!=null&&!"".equals(videoPictures)) {
            if (dialog!=null&&dialog.isShowing()){
                dialog.dismiss();
            }
            fl_video.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Uri uri = Uri.parse(FXConstant.URL_VIDEO+video);
            MediaController mc = new MediaController(this);
            mc.setVisibility(View.INVISIBLE);
            mVideoView.setMediaController(mc);
            mVideoView.setVideoURI(uri);
            mVideoView.start();
        }else if (image!=null&&!"".equals(image)){
            fl_video.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            str = image.split("\\|");
            imgThread = new ImgThread();
            imgThread.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 5:
                    mylat = data.getStringExtra("latitude");
                    mylon = data.getStringExtra("longitude");
                    street = data.getStringExtra("street");
                    Log.e("momentac,mylat",mylat+"");
                    Log.e("momentac,mylon",mylon+"");
                    Log.e("momentac,dizhi",street+"");
                    if (street!=null&&!"".equals(street)&&!street.equalsIgnoreCase("null")&&!"(null)".equals(street)){
                        street = subString(street);
                        Log.e("momentac,location",street+"");
                        et_didian.setText(street);
                    } else{
                        Toast.makeText(ReviseXuQiuActivity.this, "获取位置失败，请重新获取", Toast.LENGTH_SHORT).show();
                        et_didian.setText(null);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView!=null&&mVideoView.isPlaying()){
            mVideoView.stopPlayback();
        }
    }

    private String subString(String task_locaName) {
        String str = task_locaName;
        if (str.contains("区")||str.contains("县")){
            int i2;
            if (str.contains("区")) {
                i2 = task_locaName.indexOf("区");
            } else {
                i2 = task_locaName.indexOf("县");
            }
            if (str.contains("市")){
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    str = task_locaName.substring(i1 + 1, i2 + 1);
                }else {
                    str = task_locaName.substring(0, i2 + 1);
                }
            }else {
                str = task_locaName.substring(0, i2 + 1);
            }
        }else {
            if (str.contains("市")){
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    int i2 = task_locaName.indexOf("市");
                    str = task_locaName.substring(i1 + 1,i2 + 1);
                }else {
                    int i2 = task_locaName.indexOf("市");
                    str = task_locaName.substring(0, i2 + 1);
                }
            }else {
                if (str.contains("省")) {
                    int i1 = task_locaName.indexOf("省");
                    str = task_locaName.substring(i1+1,str.length());
                }else {
                    str= task_locaName;
                }
            }
        }
        return str;
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    private void sendVideo(final String content, List<Param> params) {
        OkHttpManager.getInstance().posts2(params, "videoPictures", imagePaths1,null, new ArrayList<String>(), "video", filePath
                , null, null, null, null, null, null, null, null, null, null, null, null, null, null, FXConstant.URL_XIUGAI_PUBLISH, new OkHttpManager.HttpCallBack() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("param,obj", jsonObject.toJSONString());
                        String code = jsonObject.getString("code");
                        if (code.equals("SUCCESS")) {
                            Toast.makeText(getApplicationContext(), "修改成功...", Toast.LENGTH_SHORT).show();
                            String timel1 = getNowTime();
                            SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putString("time4", timel1);
                            editor.putString("time8", timel1);
                            editor.commit();
                            SharedPreferences mSharedPreference = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
                            SharedPreferences.Editor meditor = mSharedPreference.edit();
                            meditor.putString("sendTime", getNowTime());
                            meditor.commit();
                            FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/catch/"));
                            FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/shipin/"));
                            FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/DCIM/mabeijianxi/"));
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            updateBmob();
                        } else {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMsg) {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateBmob() {
        String url = FXConstant.URL_UPDATE_DYNATIME;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                param.put("type4",getNowTime());
                return param;
            }
        };
        MySingleton.getInstance(ReviseXuQiuActivity.this).addToRequestQueue(request);
    }

    private void initViews(){
        FullyGridLayoutManager manager = new FullyGridLayoutManager(ReviseXuQiuActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(ReviseXuQiuActivity.this, onAddPicClickListener);
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
                        PictureConfig.getInstance().externalPicturePreview(ReviseXuQiuActivity.this, position, selectMedia);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(ReviseXuQiuActivity.this, selectMedia.get(position).getPath());
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
                        selectImgs();
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

    private void selectImgs(){
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (imagePaths1.size()>0){
            menuWindow = new SelectPicPopupWindow(ReviseXuQiuActivity.this,0,itemsOnClick);
        }else {
            menuWindow = new SelectPicPopupWindow(ReviseXuQiuActivity.this, 1, itemsOnClick);
        }
        //设置弹窗位置
        menuWindow.showAtLocation(ReviseXuQiuActivity.this.findViewById(R.id.llImage), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.item_popupwindows_camera:        //点击拍照按钮
                    if (imagePaths1.size()>0) {
                        goCamera(FunctionConfig.TYPE_VIDEO, 1, true, false);
                    }else {
                        goCamera(FunctionConfig.TYPE_VIDEO, 1, false, false);
                    }
                    break;
                case R.id.item_popupwindows_Photo:       //点击从相册中选择按钮
                    goCamera(FunctionConfig.TYPE_IMAGE,8,false,false);
                    break;
                default:
                    break;
            }
        }

    };
    private void goCamera(int type1,int count,boolean mode,boolean canCut){
        WeakReference<ReviseXuQiuActivity> reference = new WeakReference<ReviseXuQiuActivity>(ReviseXuQiuActivity.this);
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
            fl_video.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            imagePaths1.clear();
            // 多选回调
            selectMedia = resultList;
            if (selectMedia != null) {
                if (resultList.get(0).getType()==FunctionConfig.TYPE_VIDEO){
                    String path = resultList.get(0).getPath();
                    startActivityForResult(new Intent(ReviseXuQiuActivity.this, NomalVideoPlayActivity.class).putExtra("video_path",path),11);
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
            fl_video.setVisibility(View.GONE);
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
