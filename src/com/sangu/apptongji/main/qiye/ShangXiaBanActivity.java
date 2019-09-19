package com.sangu.apptongji.main.qiye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.GridImageAdapter;
import com.sangu.apptongji.main.alluser.order.entity.Util;
import com.sangu.apptongji.main.mapsearch.MapPickerActivity;
import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.main.qiye.presenter.IMemberPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.MemberPresenter;
import com.sangu.apptongji.main.qiye.view.IMemberView;
import com.sangu.apptongji.main.utils.FullyGridLayoutManager;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.FileUtils;
import com.sangu.apptongji.utils.SelectPicPopupWindow;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-01-10.
 */

public class ShangXiaBanActivity extends BaseActivity implements IMemberView{
    private IMemberPresenter memberPresenter;
    private TimePicker timePickershb;
    private TimePicker timePickerxb;
    private Button btn_send;
    private EditText et_qy_dz;
    private ImageView sdv_image3;
    private CheckBox cb_shezhi;
    private ArrayList<String> imagePaths1=null;
    private List<LocalMedia> selectMedia = new ArrayList<>();
    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private SelectPicPopupWindow menuWindow=null;

    private String companyName=null,companId=null,comaddress=null,loginTime=null,path3 = "",resv6=null,companyShortName=null;
    private String lat="",lng="",lat1="",lng1="",pushStr=null;
    private CustomProgressDialog dialog=null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.common_datetime2);
        MySingleton.getInstance(this).getRequestQueue();
        dialog = CustomProgressDialog.createDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在加载数据...");
        dialog.show();
        memberPresenter = new MemberPresenter(this,this);
        timePickershb = (TimePicker) findViewById(R.id.time_picker);
        timePickerxb = (TimePicker) findViewById(R.id.time_picker2);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_qy_dz = (EditText) findViewById(R.id.et_qy_dz);
        sdv_image3 = (ImageView) findViewById(R.id.sdv_image3);
        cb_shezhi = (CheckBox) findViewById(R.id.cb_shezhi);
        imagePaths1 = new ArrayList<>();
        companyShortName = this.getIntent().getStringExtra("companyShortName");
        companyName = this.getIntent().getStringExtra("companyName");
        comaddress = this.getIntent().getStringExtra("comAddress");
        loginTime = this.getIntent().getStringExtra("loginTime");
        resv6 = this.getIntent().getStringExtra("resv6");
        companId = DemoApplication.getInstance().getCurrentQiYeId();
        memberPresenter.loadMemberList(0);
        if (comaddress!=null&&!comaddress.equals("")) {
            try {
                comaddress = URLDecoder.decode(comaddress, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        lat = DemoApplication.getInstance().getCurrentLat();
        lng = DemoApplication.getInstance().getCurrentLng();
        init();
        String shangbanTime = loginTime.split("\\|")[0];
        String xiabanTime = loginTime.split("\\|")[1];
        int sbHour = Integer.parseInt(shangbanTime.split(":")[0]);
        int sbMinute = Integer.parseInt(shangbanTime.split(":")[1]);
        int xbHour = Integer.parseInt(xiabanTime.split(":")[0]);
        int xbMinute = Integer.parseInt(xiabanTime.split(":")[1]);
        if ("1".equals(companyShortName)){
            cb_shezhi.setChecked(true);
            recyclerView.setVisibility(View.INVISIBLE);
            sdv_image3.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_QINGJIASHXB_FENXIANG+resv6,sdv_image3,DemoApplication.mOptions2);
        }
        et_qy_dz.setText(comaddress);
        timePickershb.setCurrentHour(sbHour);
        timePickershb.setCurrentMinute(sbMinute);
        timePickerxb.setCurrentHour(xbHour);
        timePickerxb.setCurrentMinute(xbMinute);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    private void init() {
        resizePikcer(timePickershb);// 调整timepicker大小
        resizePikcer(timePickerxb);// 调整timepicker大小
        timePickershb.setIs24HourView(true);
        timePickerxb.setIs24HourView(true);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflaterDl = LayoutInflater.from(ShangXiaBanActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_alert, null);
                final Dialog dialog = new AlertDialog.Builder(ShangXiaBanActivity.this,R.style.Dialog).create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
                Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
                TextView title_tv = (TextView) layout.findViewById(R.id.title_tv);
                title_tv.setText("确认提交么？");
                btnCancel.setText("取消");
                btnOK.setText("确定");
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                //确定按钮
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        send();
                    }
                });
            }
        });
        et_qy_dz.setFocusable(false);
        et_qy_dz.setFocusableInTouchMode(false);
        et_qy_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ShangXiaBanActivity.this, MapPickerActivity.class),0);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(ShangXiaBanActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(ShangXiaBanActivity.this, onAddPicClickListener);
        adapter.setList(selectMedia);
        adapter.setSelectMax(1);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (selectMedia.get(position).getType()) {
                    case FunctionConfig.TYPE_IMAGE:
                        // 预览图片 可长按保存 也可自定义保存路径
//                        PictureConfig.getInstance().externalPicturePreview(MainActivity.this, "/custom_file", position, selectMedia);
                        PictureConfig.getInstance().externalPicturePreview(ShangXiaBanActivity.this, position, selectMedia);
                        break;
                    case FunctionConfig.TYPE_VIDEO:
                        // 预览视频
                        if (selectMedia.size() > 0) {
                            PictureConfig.getInstance().externalPictureVideo(ShangXiaBanActivity.this, selectMedia.get(position).getPath());
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
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ShangXiaBanActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        menuWindow = new SelectPicPopupWindow(ShangXiaBanActivity.this,0,itemsOnClick1);
        //设置弹窗位置
        menuWindow.showAtLocation(ShangXiaBanActivity.this.findViewById(R.id.llImage), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private View.OnClickListener itemsOnClick1 = new View.OnClickListener() {
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
        WeakReference<ShangXiaBanActivity> reference = new WeakReference<ShangXiaBanActivity>(ShangXiaBanActivity.this);
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
                .setSelectMedia(null) // 已选图片，传入在次进去可选中，不能传入网络图片
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
                imagePaths1.add(media.getPath());
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String path = null;
            switch (requestCode) {
                case 0:
                    if (data!=null){
                        lat1 = data.getStringExtra("latitude");
                        lng1 = data.getStringExtra("longitude");
                        String dizhi = data.getStringExtra("street");
                        et_qy_dz.setText(dizhi);
                    }
                    break;
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
        }
    }

    private void send() {
        String comaddress = et_qy_dz.getText().toString().trim();
        final String shbTime = getDatashb();
        final String xbTime = getDataxb();
        String url = FXConstant.URL_UPDATE_QIYE;
        try {
            comaddress = URLEncoder.encode(comaddress,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (cb_shezhi.isChecked()) {
            if (imagePaths1.size() == 1) {
                path3 = imagePaths1.get(0);
            }else {
                Toast.makeText(ShangXiaBanActivity.this,"请上传一张图片",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (dialog!=null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("正在上传...");
            dialog.show();
        }
        List<Param> param=new ArrayList<>();
        param.add(new Param("loginTime",shbTime+"|"+xbTime));
        if (!"".equals(lat1)){
            param.add(new Param("comLatitude",lat1));
            param.add(new Param("comLongitude",lng1));
            param.add(new Param("comAddress", comaddress));
        }
        if (cb_shezhi.isChecked()){
            param.add(new Param("propagate_flg","1"));
        }else {
            param.add(new Param("propagate_flg","0"));
        }
        param.add(new Param("companyId",companId));
        param.add(new Param("companyName",companyName));
        final List<File> files2 = new ArrayList<File>();
        if (!"".equals(path3)) {
            File file = null;
            file = new File(path3);
            if (file.exists()) {
                files2.add(file);
            }
        }
        OkHttpManager.getInstance().post("propagatePic",param,files2,url,new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                if (dialog!=null&&dialog.isShowing()) {
                    dialog.dismiss();
                }
                String code = jsonObject.getString("code");
                if (code.equals("SUCCESS")) {
                    Toast.makeText(ShangXiaBanActivity.this,"设置成功！",Toast.LENGTH_SHORT).show();
                    if ((shbTime+"|"+xbTime).equals(loginTime)){
                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/bizchat/"));
                        setResult(RESULT_OK);
                        finish();
                    }else {
                        sendPushMessage(shbTime+"."+xbTime);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "设置失败,code:" + code, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(String errorMsg) {
                if (dialog!=null&&dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.e("参数错误信息",errorMsg);
                Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPushMessage(final String str) {
        String url = FXConstant.URL_SENDPUSHMSGLIST;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("shangxiaac,s",s);
                FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/bizchat/"));
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/bizchat/"));
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",pushStr);
                param.put("body","企业修改签到时间");
                param.put("type","14");
                param.put("userId","0");
                param.put("companyId",companId);
                param.put("companyName",str);
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                Log.e("shangxiaac,p",param.toString());
                return param;
            }
        };
        MySingleton.getInstance(ShangXiaBanActivity.this).addToRequestQueue(request);
    }

    private void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }
    private void resizeNumberPicker(NumberPicker np) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Util.dip2px(this, 45),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(Util.dip2px(this, 5), 0, Util.dip2px(this, 5), 0);
        np.setLayoutParams(params);

    }
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }
    private String getDatashb() {
        StringBuilder str = new StringBuilder()
                .append((timePickershb.getCurrentHour() < 10) ? "0" + timePickershb.getCurrentHour()
                        : timePickershb.getCurrentHour())
                .append(":")
                .append((timePickershb.getCurrentMinute() < 10) ? "0" + timePickershb.getCurrentMinute()
                        : timePickershb.getCurrentMinute());

        return str.toString();
    }
    private String getDataxb() {
        StringBuilder str = new StringBuilder()
                .append((timePickerxb.getCurrentHour() < 10) ? "0" + timePickerxb.getCurrentHour()
                        : timePickerxb.getCurrentHour())
                .append(":")
                .append((timePickerxb.getCurrentMinute() < 10) ? "0" + timePickerxb.getCurrentMinute()
                        : timePickerxb.getCurrentMinute());

        return str.toString();
    }

    @Override
    public void updateUserList(List<MemberInfo> users, boolean hasMore) {
        for (int i=0;i<users.size();i++){
            if (i==0) {
                pushStr = users.get(i).getuLoginId();
            }else {
                pushStr += ","+users.get(i).getuLoginId();
            }
        }
        if (dialog!=null&&dialog.isShowing()) {
            dialog.dismiss();
        }
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
