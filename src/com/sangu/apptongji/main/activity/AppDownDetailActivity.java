package com.sangu.apptongji.main.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.adapter.AppCommentApapter;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.update.VersionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/6.
 */

public class AppDownDetailActivity extends BaseActivity{

    private ListView lv_appcommentlistview;

    private AppCommentApapter appCommentApapter;

    private TextView tv_midDownLoad;

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;

    private ImageView image_appLogo;
    private TextView tv_appName;

    private String currentApp;
    private String pageName="",pathName="",appName="";
    private TextView tv_downcount;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_appdetail);

        lv_appcommentlistview = (ListView) findViewById(R.id.lv_appcommentlistview);

        appCommentApapter = new AppCommentApapter(AppDownDetailActivity.this);

        lv_appcommentlistview.setAdapter(appCommentApapter);
      //  setHeight();

        image1 = (ImageView) findViewById(R.id.image_detail1);
        image2 = (ImageView) findViewById(R.id.image_detail2);
        image3 = (ImageView) findViewById(R.id.image_detail3);

        image_appLogo = (ImageView) findViewById(R.id.image_appLogo);
        tv_appName = (TextView) findViewById(R.id.tv_appName);
        tv_downcount = (TextView) findViewById(R.id.tv_downcount);
        currentApp = getIntent().getStringExtra("appType");

        String appIdentify = getIntent().getStringExtra("appIdentify");

        if (appIdentify != null){

            //查询信息
            GetAppDetailInfo(appIdentify);

        }else {

            //默认正事多
            image_appLogo.setImageResource(R.drawable.zhengshiduo);
            tv_appName.setText("正 事 多");

            image1.setImageResource(R.drawable.zhengshiduod1);
            image2.setImageResource(R.drawable.zhengshiduod2);
            image3.setImageResource(R.drawable.zhengshiduod3);

        }

        /*
        if (currentApp != null){

            if (currentApp.equals("1")){

                image_appLogo.setImageResource(R.drawable.zhaobiaodaquan);
                tv_appName.setText("招标大全");

                image1.setImageResource(R.drawable.zhaobiaodaquand1);
                image2.setImageResource(R.drawable.zhaobiaodaquand2);
                image3.setImageResource(R.drawable.zhaobiaodaquand3);

            }else if (currentApp.equals("2"))
            {
                image_appLogo.setImageResource(R.drawable.zhuangxiusheji);
                tv_appName.setText("装修设计");

                image1.setImageResource(R.drawable.zhuangxiushejid1);
                image2.setImageResource(R.drawable.zhuangxiushejid2);
                image3.setImageResource(R.drawable.zhuangxiushejid3);

            }else if (currentApp.equals("3"))
            {

                image_appLogo.setImageResource(R.drawable.fujinshifu);
                tv_appName.setText("附近师傅");

                image1.setImageResource(R.drawable.fujinshifud1);
                image2.setImageResource(R.drawable.fujinshifud2);
                image3.setImageResource(R.drawable.fujinshifud3);

            }else
            {
                image_appLogo.setImageResource(R.drawable.zhengshiduo);
                tv_appName.setText("正 事 多");

                image1.setImageResource(R.drawable.zhengshiduod1);
                image2.setImageResource(R.drawable.zhengshiduod2);
                image3.setImageResource(R.drawable.zhengshiduod3);

            }

        }else {

            currentApp = "0";

        }
    */


        tv_midDownLoad = (TextView) findViewById(R.id.tv_downLoadClick);

        tv_midDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final VersionManager.AppVersion version = new VersionManager.AppVersion();

                tv_midDownLoad.setText("正在安装");
                tv_midDownLoad.setEnabled(false);

                if (pageName != null && !pageName.equals("") && pathName != null && !pathName.equals("")){

                    version.setApkUrl(FXConstant.URL_DOWNLOAD_APK + pageName);
                    // 设置文件名
                    version.setFileName(pageName);
                    // 设置文件在sd卡的目录
                    version.setFilePath(pathName + "/update");
                    version.setAppName(appName);
                    version.setUpdateType("download");

                }else {

                    version.setApkUrl(FXConstant.URL_DOWNLOAD_APK + "Zhengshier.apk");
                    // 设置文件名
                    version.setFileName("Zhengshiduo.apk");
                    // 设置文件在sd卡的目录
                    version.setFilePath("zhengshier/update");
                    version.setAppName("正事多");
                    version.setUpdateType("download");

                }

                final VersionManager manager = VersionManager
                        .getInstance(AppDownDetailActivity.this, version);


                manager.setOnUpdateListener(new VersionManager.OnUpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(AppDownDetailActivity.this, "下载成功等待安装", Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onError(String msg) {
                        Toast.makeText(AppDownDetailActivity.this, "更新失败" + msg,Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onDownloading() {
                        Toast.makeText(AppDownDetailActivity.this, "正在下载...",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void hasNewVersion(boolean has) {
                        if (has) {
                            Toast.makeText(AppDownDetailActivity.this, "正在后台下载",Toast.LENGTH_LONG).show();
                            manager.downLoad();
                        }
                    }
                });
                manager.checkUpdateInfo();

            }
        });

    }


    private void GetAppDetailInfo(final String appIdentify){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");

                if (object1 != null){

                    //有值
                    //image1==logo  image2==下载次数  image3==三张拼接的图
                    //content==名字  type2==apk名字  type3==文件夹名字

                    String applogo = object1.getString("image1");
                    ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL+applogo,image_appLogo, DemoApplication.mOptions);

                    String downCount = object1.getString("image2");
                    tv_downcount.setText(downCount+"次下载");

                    String detailImage = object1.getString("image3");
                    String[] images = detailImage.split("\\|");
                    ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL+images[0],image1);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL+images[1],image2);
                    ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL+images[2],image3);

                    appName = object1.getString("content");
                    tv_appName.setText(appName);

                    pageName = object1.getString("type2");
                    pathName = object1.getString("type3");

                }else {

                    //未查到 默认正事多

                    image_appLogo.setImageResource(R.drawable.zhengshiduo);
                    tv_appName.setText("正 事 多");

                    image1.setImageResource(R.drawable.zhengshiduod1);
                    image2.setImageResource(R.drawable.zhengshiduod2);
                    image3.setImageResource(R.drawable.zhengshiduod3);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("deviceType",appIdentify);
                return param;
            }
        };
        MySingleton.getInstance(AppDownDetailActivity.this).addToRequestQueue(request);



    }


    public void setHeight(){

        int height = 240;
        int count = appCommentApapter.getCount();
        for(int i=0;i<count;i++){
            View temp = appCommentApapter.getView(i,null,lv_appcommentlistview);
            temp.measure(0,0);
            height += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = this.lv_appcommentlistview.getLayoutParams();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        params.height = height;
        lv_appcommentlistview.setLayoutParams(params);

    }

}
