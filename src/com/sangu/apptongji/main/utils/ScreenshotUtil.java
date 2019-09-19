package com.sangu.apptongji.main.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.fanxin.easeui.utils.FileStorage;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 截图操作
 * Created by Administrator on 2017-05-10.
 */
public class ScreenshotUtil {
    /**
     * 因为页面是可以滑动 的所以截取
     * 截取scrollview或者view的屏幕
     **/
    public static void getBitmapByView(Context mContext, final View view,String str1,String str2,int type,boolean ispinjie,int liulan, int zhuanfa) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        int widthT = px2dip(mContext,width);
        int heightT = px2dip(mContext,height);
        int subDis = (widthT-320)/2;
        int subDis1 = (widthT-340)/2;
        // 获取View实际高度
        Bitmap bitmap = null;
        if (view!=null) {
            bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
        }
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1;
        if (ispinjie){

            if (str1.equals("分享动态红包") || str1.equals("分享名片红包") || str1.equals("分享企业红包")) {

                v1 = inflater.inflate(R.layout.activity_productshare,null);
                TextView tv_title = (TextView) v1.findViewById(R.id.tv_title);
                TextView tv_qq = (TextView) v1.findViewById(R.id.tv_qqcount);
                TextView tv_weixin = (TextView) v1.findViewById(R.id.tv_weixincount);
                TextView tv_weibo = (TextView) v1.findViewById(R.id.tv_weibocount);
                RelativeLayout rl2 = (RelativeLayout) v1.findViewById(R.id.rl2);
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                params.setMargins(dip2px(mContext,subDis),0,dip2px(mContext,subDis),0);
                rl2.setLayoutParams(params);


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");// HH:mm:ss
//获取当前时间
                Date date = new Date(System.currentTimeMillis());
                String time = simpleDateFormat.format(date).substring(0,8);

                SharedPreferences sp =mContext.getSharedPreferences("sangu_shareCount_info", Context.MODE_PRIVATE);

                //主页 次单位
                String count1 = "40.1";
                String count2 = "35.3";
                String count3 = "15.4";

                //动态  人单位
                String count4 = "16.3";
                String count5 = "19.2";
                String count6 = "13.1";

                if (sp != null){

                    String saveTime = sp.getString("saveTime","1");


                    if (saveTime.equals("1")){
                        //没有存储 存一下默认值
                        SharedPreferences.Editor editor1 = sp.edit();
                        editor1.putString("saveTime", time);
                        editor1.putString("qqCount", "40.1");
                        editor1.putString("weixinCount", "35.3");
                        editor1.putString("weiboCount", "15.4");
                        editor1.putString("locationCount", "400.2");
                        editor1.putString("dynamicqq", "16.3");
                        editor1.putString("dynamicweixin", "19.2");
                        editor1.putString("dynamicweibo", "13.1");

                        editor1.commit();

                    }else {
                        //有值判断是否同一天 是直接用 否+1再用 顺便存一下

                        if (time.equals(saveTime)){
                            //同一天

                            count1 = sp.getString("qqCount","40.1");
                            count2 = sp.getString("weixinCount","35.3");
                            count3 = sp.getString("weiboCount","15.4");
                            count4 = sp.getString("dynamicqq","16.3");
                            count5 = sp.getString("dynamicweixin","19.2");
                            count6 = sp.getString("dynamicweibo","13.1");

                        }else {
                            //不同一天

                            String qqCount = sp.getString("qqCount","40.1");
                            String weixinCount = sp.getString("weixinCount","35.3");
                            String weiboCount = sp.getString("weiboCount","15.4");
                            String locationCount = sp.getString("locationCount","400.2");
                            String dynamicqq = sp.getString("dynamicqq","16.3");
                            String dynamicweixin = sp.getString("dynamicweixin","19.2");
                            String dynamicweibo = sp.getString("dynamicweibo","13.1");

                            Double qq = Double.parseDouble(qqCount)+1.1;
                            Double weixin = Double.parseDouble(weixinCount)+1.1;
                            Double weibo = Double.parseDouble(weiboCount)+1.1;
                            Double location = Double.parseDouble(locationCount)+1.1;
                            Double dyqq = Double.parseDouble(dynamicqq)+0.3;
                            Double dyweixin = Double.parseDouble(dynamicweixin)+0.3;
                            Double dyweibo = Double.parseDouble(dynamicweibo)+0.2;

                            SharedPreferences.Editor editor1 = sp.edit();
                            editor1.putString("saveTime", time);
                            editor1.putString("qqCount", new DecimalFormat("0.0").format(qq));
                            editor1.putString("weixinCount",new DecimalFormat("0.0").format(weixin));
                            editor1.putString("weiboCount", new DecimalFormat("0.0").format(weibo));
                            editor1.putString("locationCount", new DecimalFormat("0.0").format(location));
                            editor1.putString("dynamicqq", new DecimalFormat("0.0").format(dyqq));
                            editor1.putString("dynamicweixin", new DecimalFormat("0.0").format(dyweixin));
                            editor1.putString("dynamicweibo", new DecimalFormat("0.0").format(dyweibo));
                            editor1.commit();

                            count1 = sp.getString("qqCount","40.1");
                            count2 = sp.getString("weixinCount","35.3");
                            count3 = sp.getString("weiboCount","15.4");
                            count4 = sp.getString("dynamicqq","16.3");
                            count5 = sp.getString("dynamicweixin","19.2");
                            count6 = sp.getString("dynamicweibo","13.1");

                        }

                    }

                }

                if (str1.equals("分享动态红包")){
                    //商业动态

                    tv_qq.setText(count4+"万人");
                    tv_weixin.setText(count5+"万人");
                    tv_weibo.setText(count6+"万人");

                }else {
                    //用户主页

                    tv_title.setText("正事多-能接单的主页名片");

                    tv_qq.setText(count1+"万次");
                    tv_weixin.setText(count2+"万次");
                    tv_weibo.setText(count3+"万次");

                }


            }else {

                v1 = inflater.inflate(R.layout.head_two,null);
                TextView tv1 = (TextView) v1.findViewById(R.id.tv1);
                TextView tv2 = (TextView) v1.findViewById(R.id.tv2);
                TextView tv3 = (TextView) v1.findViewById(R.id.tv3);
                TextView tv31 = (TextView) v1.findViewById(R.id.tv31);
                TextView tv32 = (TextView) v1.findViewById(R.id.tv32);
                RelativeLayout rl2 = (RelativeLayout) v1.findViewById(R.id.rl2);
                RelativeLayout rl3 = (RelativeLayout) v1.findViewById(R.id.rl3);
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                params.setMargins(dip2px(mContext,subDis1),0,0,0);
                LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                params2.setMargins(dip2px(mContext,250+subDis1+subDis1),0,0,0);
                rl2.setLayoutParams(params);
                rl3.setLayoutParams(params2);
                if (type>0) {
                    tv31.setVisibility(View.VISIBLE);
                    tv32.setVisibility(View.VISIBLE);
                    tv1.setText("正事多-智能商务工具");
                    tv3.setText(type+"");
                }else {
                    tv31.setVisibility(View.GONE);
                    tv32.setVisibility(View.GONE);
                    tv1.setText(str1);
                    if (type==-1) {
                        tv3.setText("来自主页红包");
                    }else {
                        tv3.setText("来自商业动态");
                    }
                }
                if (str2!=null&&!"".equals(str2)){
                    double jine1 = Double.parseDouble(str2);
                    str2 = String.format("%.2f", jine1);
                }
                tv2.setText("￥"+str2+"元");

            }

        }else {


            if (type==4 || type==6){

                v1 = inflater.inflate(R.layout.activity_productshare,null);
                TextView tv_title = (TextView) v1.findViewById(R.id.tv_title);
                TextView tv_qq = (TextView) v1.findViewById(R.id.tv_qqcount);
                TextView tv_weixin = (TextView) v1.findViewById(R.id.tv_weixincount);
                TextView tv_weibo = (TextView) v1.findViewById(R.id.tv_weibocount);
                RelativeLayout rl2 = (RelativeLayout) v1.findViewById(R.id.rl2);
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                params.setMargins(dip2px(mContext,subDis),0,dip2px(mContext,subDis),0);
                rl2.setLayoutParams(params);


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");// HH:mm:ss
//获取当前时间
                Date date = new Date(System.currentTimeMillis());
                String time = simpleDateFormat.format(date).substring(0,8);

                SharedPreferences sp =mContext.getSharedPreferences("sangu_shareCount_info", Context.MODE_PRIVATE);

                //主页 次单位
                String count1 = "40.1";
                String count2 = "35.3";
                String count3 = "15.4";

                //动态  人单位
                String count4 = "16.3";
                String count5 = "19.2";
                String count6 = "13.1";

                if (sp != null){

                    String saveTime = sp.getString("saveTime","1");

                    if (saveTime.equals("1")){
                        //没有存储 存一下默认值
                        SharedPreferences.Editor editor1 = sp.edit();
                        editor1.putString("saveTime", time);
                        editor1.putString("qqCount", "40.1");
                        editor1.putString("weixinCount", "35.3");
                        editor1.putString("weiboCount", "15.4");
                        editor1.putString("locationCount", "400.2");
                        editor1.putString("dynamicqq", "16.3");
                        editor1.putString("dynamicweixin", "19.2");
                        editor1.putString("dynamicweibo", "13.1");

                        editor1.commit();

                    }else {
                        //有值判断是否同一天 是直接用 否+1再用 顺便存一下

                        if (time.equals(saveTime)){
                            //同一天

                            count1 = sp.getString("qqCount","40.1");
                            count2 = sp.getString("weixinCount","35.3");
                            count3 = sp.getString("weiboCount","15.4");
                            count4 = sp.getString("dynamicqq","16.3");
                            count5 = sp.getString("dynamicweixin","19.2");
                            count6 = sp.getString("dynamicweibo","13.1");

                        }else {
                            //不同一天

                            String qqCount = sp.getString("qqCount","40.1");
                            String weixinCount = sp.getString("weixinCount","35.3");
                            String weiboCount = sp.getString("weiboCount","15.4");
                            String locationCount = sp.getString("locationCount","400.2");
                            String dynamicqq = sp.getString("dynamicqq","16.3");
                            String dynamicweixin = sp.getString("dynamicweixin","19.2");
                            String dynamicweibo = sp.getString("dynamicweibo","13.1");

                            Double qq = Double.parseDouble(qqCount)+1.1;
                            Double weixin = Double.parseDouble(weixinCount)+1.1;
                            Double weibo = Double.parseDouble(weiboCount)+1.1;
                            Double location = Double.parseDouble(locationCount)+1.1;
                            Double dyqq = Double.parseDouble(dynamicqq)+0.3;
                            Double dyweixin = Double.parseDouble(dynamicweixin)+0.3;
                            Double dyweibo = Double.parseDouble(dynamicweibo)+0.2;

                            SharedPreferences.Editor editor1 = sp.edit();
                            editor1.putString("saveTime", time);
                            editor1.putString("qqCount", new DecimalFormat("0.0").format(qq));
                            editor1.putString("weixinCount",new DecimalFormat("0.0").format(weixin));
                            editor1.putString("weiboCount", new DecimalFormat("0.0").format(weibo));
                            editor1.putString("locationCount", new DecimalFormat("0.0").format(location));
                            editor1.putString("dynamicqq", new DecimalFormat("0.0").format(dyqq));
                            editor1.putString("dynamicweixin", new DecimalFormat("0.0").format(dyweixin));
                            editor1.putString("dynamicweibo", new DecimalFormat("0.0").format(dyweibo));
                            editor1.commit();

                            count1 = sp.getString("qqCount","40.1");
                            count2 = sp.getString("weixinCount","35.3");
                            count3 = sp.getString("weiboCount","15.4");
                            count4 = sp.getString("dynamicqq","16.3");
                            count5 = sp.getString("dynamicweixin","19.2");
                            count6 = sp.getString("dynamicweibo","13.1");

                        }

                    }

                }

                if (type == 4){
                    //商业动态

                    tv_qq.setText(count4+"万人");
                    tv_weixin.setText(count5+"万人");
                    tv_weibo.setText(count6+"万人");

                }else {
                    //用户主页

                    tv_title.setText("正事多-能接单的主页名片");

                    tv_qq.setText(count1+"万次");
                    tv_weixin.setText(count2+"万次");
                    tv_weibo.setText(count3+"万次");

                }

            }else{

                v1 = inflater.inflate(R.layout.activity_fenxiang,null);
                TextView tv_title = (TextView) v1.findViewById(R.id.tv_title);
                TextView tv_titlet = (TextView) v1.findViewById(R.id.tv_titlet);
                TextView tv_location = (TextView) v1.findViewById(R.id.tv_location);
                RelativeLayout rl2 = (RelativeLayout) v1.findViewById(R.id.rl2);
                TextView tv_longtitle = (TextView) v1.findViewById(R.id.tv_longtitle);
                ImageView iv2 = (ImageView)v1.findViewById(R.id.iv2);
                String iconImage = DemoApplication.getInstance().getCurrentUser().getImage();
                String[] strings = iconImage.split("\\|");
                File f1 = new FileStorage("touxiang").createCropFile(strings[0],null);
                if (f1.exists()) {
                    try {
                        FileInputStream fis = new FileInputStream(f1.getPath());
                        Bitmap bitma2p  = BitmapFactory.decodeStream(fis);
                        iv2.setImageBitmap(bitma2p);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }

                }

                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                params.setMargins(dip2px(mContext,subDis),0,dip2px(mContext,subDis),0);
                rl2.setLayoutParams(params);

                final String name = DemoApplication.getInstance().getCurrentUser().getName();
                if (name != null){
                    tv_longtitle.setText(name+"推荐");
                }

                if (str2==null){
                    tv_title.setText("正事多-智能商务工具");
                    tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                    if (type==1){
                        tv_title.setText("我的订单案例");;
                        tv_title.setTextSize(14);
                        tv_titlet.setText("案例是服务能力的最好证明！");
                        tv_titlet.setTextSize(12);
                        tv_location.setText("来自【正事多】APP接派单平台");
                        tv_location.setTextSize(12);

                    }else if (type==3 || type==2){

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");// HH:mm:ss
//获取当前时间
                        Date date = new Date(System.currentTimeMillis());
                        String time = simpleDateFormat.format(date).substring(0,8);

                        SharedPreferences sp =mContext.getSharedPreferences("sangu_shareCount_info", Context.MODE_PRIVATE);

                        String count = "400.2";

                        if (sp != null){

                           String saveTime = sp.getString("saveTime","1");

                            if (saveTime.equals("1")){
                                //没有存储 存一下默认值
                                SharedPreferences.Editor editor1 = sp.edit();
                                editor1.putString("saveTime", time);
                                editor1.putString("qqCount", "40.1");
                                editor1.putString("weixinCount", "35.3");
                                editor1.putString("weiboCount", "15.4");
                                editor1.putString("locationCount", "400.2");
                                editor1.putString("dynamicqq", "16.3");
                                editor1.putString("dynamicweixin", "19.2");
                                editor1.putString("dynamicweibo", "13.1");

                                editor1.commit();

                            }else {
                                //有值判断是否同一天 是直接用 否+1再用 顺便存一下

                                if (time.equals(saveTime)){
                                    //同一天

                                    count = sp.getString("locationCount","400.2");

                                }else {
                                    //不同一天

                                    String qqCount = sp.getString("qqCount","40.1");
                                    String weixinCount = sp.getString("weixinCount","35.3");
                                    String weiboCount = sp.getString("weiboCount","15.4");
                                    String locationCount = sp.getString("locationCount","400.2");
                                    String dynamicqq = sp.getString("dynamicqq","16.3");
                                    String dynamicweixin = sp.getString("dynamicweixin","19.2");
                                    String dynamicweibo = sp.getString("dynamicweibo","13.1");

                                    Double qq = Double.parseDouble(qqCount)+1.1;
                                    Double weixin = Double.parseDouble(weixinCount)+1.1;
                                    Double weibo = Double.parseDouble(weiboCount)+1.1;
                                    Double location = Double.parseDouble(locationCount)+1.1;
                                    Double dyqq = Double.parseDouble(dynamicqq)+0.31;
                                    Double dyweixin = Double.parseDouble(dynamicweixin)+0.32;
                                    Double dyweibo = Double.parseDouble(dynamicweibo)+0.31;

                                    SharedPreferences.Editor editor1 = sp.edit();
                                    editor1.putString("saveTime", time);
                                    editor1.putString("qqCount", new DecimalFormat("0.0").format(qq));
                                    editor1.putString("weixinCount", new DecimalFormat("0.0").format(weixin));
                                    editor1.putString("weiboCount", new DecimalFormat("0.0").format(weibo));
                                    editor1.putString("locationCount", new DecimalFormat("0.0").format(location));
                                    editor1.putString("dynamicqq", new DecimalFormat("0.0").format(dyqq));
                                    editor1.putString("dynamicweixin", new DecimalFormat("0.0").format(dyweixin));
                                    editor1.putString("dynamicweibo", new DecimalFormat("0.0").format(dyweibo));
                                    editor1.commit();

                                    count = sp.getString("locationCount","400.2");

                                }

                            }

                        }

                        tv_title.setText("正事多-坐标宝藏定位导航");
                        tv_title.setTextSize(15);
                        tv_titlet.setText(count+"万人正在使用");
                        tv_titlet.setTextSize(13);
                        tv_location.setText("店面-景点-展会-活动-约会发位置");
                        tv_location.setTextSize(12);

                    }else if (type==4){

                        tv_title.setText("正事多-我的线上运营平台");
                        tv_title.setTextSize(15);
                        tv_titlet.setText("来自：商业动态");
                        tv_location.setText("分享领红包--需要就下单");
                        tv_location.setTextSize(13);

                    }else if (type==5 || type==51 || type==52 || type==53){

                        if (type==5){
                            tv_title.setText(" 搜索、咨询、太麻烦");;
                            tv_title.setTextSize(14);
                            tv_titlet.setText("找人干活 发派单 收到报价再优选");
                            tv_titlet.setTextSize(12);
                            tv_location.setText("根据坐标位置接单派单，及时又便宜");
                            tv_location.setTextSize(11);
                        }else if (type==51){
                            tv_title.setText("正事多APP移动商务");;
                            tv_title.setTextSize(14);
                            tv_titlet.setText("只要位置好，随时有单接");
                            tv_titlet.setTextSize(12);
                            tv_location.setText("360行 派单能找人 附近能接单");
                            tv_location.setTextSize(12);
                        }else if (type==52){
                            tv_title.setText("正事多APP-招标 投标");;
                            tv_title.setTextSize(14);
                            tv_titlet.setText("工程 家居 个人 企业 工业");
                            tv_titlet.setTextSize(12);
                            tv_location.setText("采购 服务 定做 有需求 发招标");
                            tv_location.setTextSize(12);
                        }else if (type==53){
                            tv_title.setText("正事多APP移动商务");;
                            tv_title.setTextSize(14);
                            tv_titlet.setText("只要位置好，随时有单接");
                            tv_titlet.setTextSize(12);
                            tv_location.setText("360行 派单能找人 附近能接单");
                            tv_location.setTextSize(12);
                        }


                    }else if (type==6){

                        tv_titlet.setText("浏览:"+liulan+"次  "+"转发:"+ zhuanfa+"次");
                        tv_titlet.setTextSize(12);
                        tv_location.setText("接单 派单 通讯 管理 定位");


                    }else if (type==7){
                        tv_titlet.setText("我当前的状态(接单)");
                        tv_location.setText("接单 派单 定位 考勤");
                    }else if (type==8){
                        tv_titlet.setText("我当前的状态(需求)");
                        tv_location.setText("发需求 快派单 在线派单");
                    }else if (type==9){
                        tv_titlet.setText("我当前的状态(管理)");
                        tv_location.setText("定位考勤 整合管理 业绩管理");
                    }else if (type==10){
                        tv_titlet.setText("我当前的状态(社交)");
                        tv_location.setText("通讯 聊天 坐标 红包");
                    }else if (type==11){
                        tv_titlet.setText("来自"+str1+"的专业");
                        tv_location.setText("接单 派单 交易 管理 定位");
                    }else if (type==12){
                        tv_titlet.setText("用户源统计");
                        tv_location.setText("转发分享 营销推广 数据统计");
                    }
                }else {
                    tv_titlet.setText(str2);
                    tv_titlet.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
                    tv_location.setText("驴友推荐 解密宝藏 赚红包");
                    tv_location.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                }

            }

        }
        Bitmap v;
        Bitmap head = convertViewToBitmap(v1);
        if (bitmap!=null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                if (ispinjie) {

                    if (str1.equals("分享动态红包") || str1.equals("分享名片红包") || str1.equals("分享企业红包")) {

                        v = toConformBitmap(head, bitmap, 255, 255, 255);

                    }else {

                        v = toConformBitmap(head, bitmap, 235, 137, 44);
                    }

                } else {
                    v = toConformBitmap(head, bitmap, 255, 255, 255);
                }
                if (!head.isRecycled()) {
                    head.recycle();
                }
            } else {
                v = bitmap;
            }
        }else {
            v = head;
        }

        File file;
        if (str1.equals("订单")){
            file = new FileStorage("fenxiang").createCropFile("dingdanCut.png",null);
        }else {
            file = new FileStorage("fenxiang").createCropFile("mingpCut.png",null);
        }
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(mContext, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            mContext.grantUriPermission(mContext.getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
            v.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void getShareRegionByView(Context mContext){

        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        int widthT = px2dip(mContext,width);
        int heightT = px2dip(mContext,height);
        int subDis = (widthT-320)/2;
        int subDis1 = (widthT-340)/2;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1;

        v1 = inflater.inflate(R.layout.activity_fenxiang,null);
        TextView tv_title = (TextView) v1.findViewById(R.id.tv_title);
        TextView tv_titlet = (TextView) v1.findViewById(R.id.tv_titlet);
        TextView tv_location = (TextView) v1.findViewById(R.id.tv_location);
        RelativeLayout rl2 = (RelativeLayout) v1.findViewById(R.id.rl2);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.setMargins(dip2px(mContext,subDis),0,dip2px(mContext,subDis),0);
        rl2.setLayoutParams(params);

        Bitmap head = convertViewToBitmap(v1);

        File file = new FileStorage("fenxiang").createCropFile("mingpCut.png",null);

        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(mContext, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            mContext.grantUriPermission(mContext.getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
            head.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void getBitmapByV2(Context mContext, final View view) {
        Bitmap bitmap = Bitmap.createBitmap(dip2px(mContext,60), dip2px(mContext,40), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v1 = inflater.inflate(R.layout.head_three,null);
        Bitmap v;
        Bitmap head = convertViewToBitmap(v1);
        if (bitmap!=null) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                v = toConformBitmap(bitmap,head, 62,197,255);
                if (!head.isRecycled()) {
                    head.recycle();
                }
            } else {
                v = BitmapUtils.readBitMap(mContext,R.drawable.share_jiedan);
            }
        }else {
            v = BitmapUtils.readBitMap(mContext,R.drawable.share_jiedan);
        }
        File file = new FileStorage("fenxiang").createCropFile("mingpCut.png",null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(mContext, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            mContext.grantUriPermission(mContext.getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
            v.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储资源为ID的图片
     * @param id
     */
    public static void saveDrawableById(Context mContext,int id) {
        Bitmap bitmap = BitmapUtils.readBitMap(mContext,id);
        File file = new FileStorage("fenxiang").createCropFile("mingpCut2.png",null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(mContext, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            mContext.grantUriPermission(mContext.getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int dip2px(Context context, float dpValue) {
        if (context!=null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        }
        return 0;
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = DemoApplication.getApp().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int sp2px(float spValue) {
        final float fontScale = DemoApplication.getApp().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static void getBitmapByView2(Context mContext, final View view,boolean ispinjie) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        Bitmap v;
        v = bitmap;
        File file = new FileStorage("fenxiang").createCropFile("mingpCut.png",null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(mContext, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            mContext.grantUriPermission(mContext.getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
            v.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        if (view!=null) {
            view.destroyDrawingCache();
            view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.setDrawingCacheEnabled(true);
            return view.getDrawingCache(true);
        }else {
            return null;
        }
    }

    /**
     * 合并图片
     *
     * @param head
     * @param kebiao
     * @return
     */
    public static Bitmap toConformBitmap(Bitmap head, Bitmap kebiao,int r1,int r2,int r3) {
        if (head == null) {
            return null;
        }
        int headWidth = head.getWidth();
        int kebianwidth = kebiao.getWidth();

        int headHeight = head.getHeight();
        int kebiaoheight = kebiao.getHeight();
        //生成三个图片合并大小的Bitmap  为防止出现oom采用RGB_565 不适用ARGB_8888
        Bitmap newbmp = Bitmap.createBitmap(kebianwidth, headHeight + kebiaoheight, Bitmap.Config.RGB_565);
        Canvas cv = new Canvas(newbmp);
        cv.drawBitmap(head, 0, 0, null);// 在 0，0坐标开始画入headBitmap

        //因为手机不同图片的大小的可能小了 就绘制白色的界面填充剩下的界面
        if (headWidth < kebianwidth) {
            //System.out.println("绘制头");
            Bitmap ne = Bitmap.createBitmap(kebianwidth - headWidth, headHeight, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(ne);
            canvas.drawColor(Color.rgb(r1,r2,r3));
            cv.drawBitmap(ne, headWidth, 0, null);
        }
        cv.drawBitmap(kebiao, 0, headHeight, null);// 在 0，headHeight坐标开始填充课表的Bitmap
        cv.save();
        //cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        cv.restore();// 存储
        //回收
        head.recycle();
        kebiao.recycle();
        return newbmp;
    }





}
