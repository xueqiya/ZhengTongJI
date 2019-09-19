package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFiveActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailFourActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailThreeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.UOrderDetailTwoActivity;
import com.sangu.apptongji.main.moments.BigImageActivity;
import com.sangu.apptongji.main.qiye.CompanyUpdateTwoActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2016-09-23.
 */

public class ZYDetailActivity extends BaseActivity {
    private String remark,body,imageStr,decribe,create,margen,liulanCiShu,zyType;
    private String pass,hxid,upId,count="0",jine="0",distance,url,zhuanye,comName,name,fxUpName;
    private TextView tv_xm_name=null,tv_xm_jingyan=null,tv_bao_jine=null,tv_jiaoyiliang=null,tv_jy_jine=null,tv_zy_miaoshu=null,
            tv_liulan_cishu=null,tv_fenxiang=null,tv_temp_bao_jine=null;
    private ImageView iv1=null;
    private ImageView iv2=null;
    private ImageView iv3=null;
    private ImageView iv4=null;
    private ImageView iv5=null;
    private ImageView iv6=null;
    private ImageView iv7=null;
    private ImageView iv8=null;
    private Button btn_xiadan=null;
    private String myId=null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_zy);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        distance = this.getIntent().hasExtra("distance")?this.getIntent().getStringExtra("distance"):"0.0m";
        zyType = this.getIntent().hasExtra("zyType")?this.getIntent().getStringExtra("zyType"):"01";
        decribe = this.getIntent().hasExtra("decribe")?this.getIntent().getStringExtra("decribe"):"无";
        remark = this.getIntent().hasExtra("remark")?this.getIntent().getStringExtra("remark"):"0,0";
        imageStr = this.getIntent().hasExtra("image")?this.getIntent().getStringExtra("image"):"";
        name = this.getIntent().hasExtra("name")?this.getIntent().getStringExtra("name"):"";
        fxUpName = this.getIntent().hasExtra("fxUpName")?this.getIntent().getStringExtra("fxUpName"):"";
        comName = this.getIntent().hasExtra("comName")?this.getIntent().getStringExtra("comName"):"";
        zhuanye = this.getIntent().hasExtra("zhuanye")?this.getIntent().getStringExtra("zhuanye"):"";
        create = this.getIntent().hasExtra("create")?this.getIntent().getStringExtra("create"):"";
        body = this.getIntent().hasExtra("body")?this.getIntent().getStringExtra("body"):"";
        margen = this.getIntent().hasExtra("margen")?this.getIntent().getStringExtra("margen"):"";
        liulanCiShu = this.getIntent().hasExtra("liulancishu")?this.getIntent().getStringExtra("liulancishu"):"";
        pass = this.getIntent().hasExtra("pass")?this.getIntent().getStringExtra("pass"):"";
        hxid = this.getIntent().hasExtra("hxid")?this.getIntent().getStringExtra("hxid"):"";
        upId = this.getIntent().hasExtra("upId")?this.getIntent().getStringExtra("upId"):"";
        if ("001".equals(distance)){
            url = FXConstant.URL_QIYE_ZY;
            myId = DemoApplication.getInstance().getCurrentQiYeId();
        }else {
            url = FXConstant.URL_ZY_AVATAR;
            myId = DemoHelper.getInstance().getCurrentUsernName();
        }
        initView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLiuLanCiShu();
            }
        },1000);
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    private void updateLiulancishu() {
        String url = FXConstant.URL_ADD_USERCISHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add","增加浏览次数成功"+s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null) {
                    Log.e("Userdetailac,add", "增加浏览次数错误");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uLoginId",hxid);
                param.put("homePage","1");
                param.put("v_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(ZYDetailActivity.this).addToRequestQueue(request);
    }

    private void updateLiuLanCiShu() {
        String url = FXConstant.URL_Search_LiuLanCiShu;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("接收的专业",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null){
                    Log.e("接收的专业,e",volleyError.toString());
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("upId",upId);
                return param;
            }
        };
        MySingleton.getInstance(ZYDetailActivity.this).addToRequestQueue(request);
    }

    class ImageListener implements View.OnClickListener {
        String[] images;
        int page;

        public ImageListener(String[] images, int page) {
            this.images = images;
            this.page = page;
        }
        @Override
        public void onClick(View v) {
            String biaoshi = "11";
            if ("001".equals(distance)){
                biaoshi = "15";
            }
            Intent intent = new Intent(getApplicationContext() , BigImageActivity.class);
            intent.putExtra("images", images);
            intent.putExtra("page", page);
            intent.putExtra("biaoshi",biaoshi);
            startActivity(intent);
        }
    }

    private void initView() {
        btn_xiadan = (Button) this.findViewById(R.id.btn_xiadan);
        tv_xm_name = (TextView) this.findViewById(R.id.tv_xm_name);
        tv_xm_jingyan = (TextView) this.findViewById(R.id.tv_xm_jingyan);
        tv_bao_jine = (TextView) this.findViewById(R.id.tv_bao_jine);
        tv_jiaoyiliang = (TextView) this.findViewById(R.id.tv_jiaoyiliang);
        tv_jy_jine = (TextView) this.findViewById(R.id.tv_jy_jine);
        tv_zy_miaoshu = (TextView) this.findViewById(R.id.tv_zy_miaoshu);
        tv_liulan_cishu = (TextView) this.findViewById(R.id.tv_liulan_cishu);
        tv_fenxiang = (TextView) this.findViewById(R.id.tv_fenxiang);
        tv_temp_bao_jine = (TextView) this.findViewById(R.id.tv_temp_bao_jine);
        iv1 = (ImageView) this.findViewById(R.id.iv1);
        iv2 = (ImageView) this.findViewById(R.id.iv2);
        iv3 = (ImageView) this.findViewById(R.id.iv3);
        iv4 = (ImageView) this.findViewById(R.id.iv4);
        iv5 = (ImageView) this.findViewById(R.id.iv5);
        iv6 = (ImageView) this.findViewById(R.id.iv6);
        iv7 = (ImageView) this.findViewById(R.id.iv7);
        iv8 = (ImageView) this.findViewById(R.id.iv8);
        if (imageStr!=null&&!"".equals(imageStr)) {
            String[] images = imageStr.split("\\|");
            int imNumb = images.length;
            iv1.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(url + images[0],iv1,DemoApplication.mOptions2);
            iv1.setOnClickListener(new ImageListener(images, 0));
            // 六张图的时间情况比较特殊
            if (imNumb == 6) {
                iv2.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(url + images[1],iv2,DemoApplication.mOptions2);
                iv2.setOnClickListener(new ImageListener(images,1));
                iv3.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(url + images[2],iv3,DemoApplication.mOptions2);
                iv3.setOnClickListener(new ImageListener(images,2));
                iv4.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(url + images[3],iv4,DemoApplication.mOptions2);
                iv4.setOnClickListener(new ImageListener(images,3));
                iv6.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(url + images[4],iv6,DemoApplication.mOptions2);
                iv6.setOnClickListener(new ImageListener(images,4));
                iv7.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(url + images[5],iv7,DemoApplication.mOptions2);
                iv7.setOnClickListener(new ImageListener(images,5));
            } else {
                if (imNumb > 1) {
                    iv2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(url + images[1],iv2,DemoApplication.mOptions2);
                    iv2.setOnClickListener(new ImageListener(images, 1));
                    if (imNumb > 2) {
                        iv3.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(url + images[2],iv3,DemoApplication.mOptions2);
                        iv3.setOnClickListener(new ImageListener(images, 2));
                        if (imNumb > 3) {
                            iv4.setVisibility(View.VISIBLE);
                            ImageLoader.getInstance().displayImage(url + images[3],iv4,DemoApplication.mOptions2);
                            iv4.setOnClickListener(new ImageListener(images, 3));
                            if (imNumb > 4) {
                                iv5.setVisibility(View.VISIBLE);
                                ImageLoader.getInstance().displayImage(url + images[4],iv5,DemoApplication.mOptions2);
                                iv5.setOnClickListener(new ImageListener(images, 4));
                                if (imNumb > 5) {
                                    iv6.setVisibility(View.VISIBLE);
                                    ImageLoader.getInstance().displayImage(url + images[5],iv6,DemoApplication.mOptions2);
                                    iv6.setOnClickListener(new ImageListener(images, 5));
                                    if (imNumb > 6) {
                                        iv7.setVisibility(View.VISIBLE);
                                        ImageLoader.getInstance().displayImage(url + images[6],iv7,DemoApplication.mOptions2);
                                        iv7.setOnClickListener(new ImageListener(images, 6));
                                        if (imNumb > 7) {
                                            iv8.setVisibility(View.VISIBLE);
                                            ImageLoader.getInstance().displayImage(url + images[7],iv8,DemoApplication.mOptions2);
                                            iv8.setOnClickListener(new ImageListener(images, 7));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (remark!=null&&!"".equals(remark)) {
            String[] array = remark.split(",");
            if (array.length==1) {
                count = array[0];
                if (count.equals("")) {
                    count = "0";
                }
            }else if (array.length == 2){
                count = array[0];
                if (count.equals("")) {
                    count = "0";
                }
                jine = array[1];
                if (jine.equals("")||jine.equals(null)){
                    jine = "0";
                }
            }
            tv_jy_jine.setText(jine+"元");
            tv_jiaoyiliang.setText(count+"次");
        }
        if (liulanCiShu==null||"".equals(liulanCiShu)){
            liulanCiShu = "0";
        }
        if (decribe==null||"".equals(decribe)){
            decribe = "未设置";
        }
        tv_zy_miaoshu.setText(decribe);
        tv_liulan_cishu.setText(liulanCiShu+"次");
        if (create!=null&&!create.equals("")&&create.length()>10) {
            String time = create.substring(0, 4) + "-" + create.substring(4, 6) + "-" + create.substring(6, 8) + " "
                    + create.substring(8, 10) + ":" + create.substring(10, 12);
            tv_xm_jingyan.setText(time);
        }
        tv_xm_name.setText(body);
        tv_bao_jine.setText(margen+"元");
        final String wodezhanghao = DemoHelper.getInstance().getCurrentUsernName();
        btn_xiadan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DemoHelper.getInstance().isLoggedIn(ZYDetailActivity.this)) {
                    showDialog(wodezhanghao);
                }else {
                    startActivity(new Intent(ZYDetailActivity.this, LoginActivity.class));
                }
            }
        });
//        tv_temp_bao_jine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String price = String.valueOf(DemoApplication.getInstance().getCurrenPrice());
//                if (margen.equals("0")){
//                    startActivityForResult(new Intent(ZYDetailActivity.this, BZJJNActivity.class).putExtra("YUE",String.valueOf(price)).putExtra("upId",upId).putExtra("maj",body),0);
//                }else {
//                    startActivityForResult(new Intent(ZYDetailActivity.this, BZJZJActivity.class).putExtra("JINE",margen).putExtra("YUE",String.valueOf(price)).putExtra("upId",upId).putExtra("maj",body),0);
//                }
//            }
//        });




//        if (hxid!=null&&hxid.equals(myId)){
//            tv_fenxiang.setText("编辑");
//            tv_fenxiang.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int type = ProfileUpdateTwoActivity.TYPE_MAJOR1;
//                    if ("02".equals(zhuanye)) {
//                        type = ProfileUpdateTwoActivity.TYPE_MAJOR2;
//                    }else if ("03".equals(zhuanye)){
//                        type = ProfileUpdateTwoActivity.TYPE_MAJOR3;
//                    }else if ("04".equals(zhuanye)){
//                        type = ProfileUpdateTwoActivity.TYPE_MAJOR4;
//                    }else {
//                        type = ProfileUpdateTwoActivity.TYPE_MAJOR1;
//                    }
//                    if ("001".equals(distance)){
//                        startActivity(new Intent(ZYDetailActivity.this, CompanyUpdateTwoActivity.class).putExtra("type", type).putExtra("image", imageStr)
//                                .putExtra("zyResv3", zyType).putExtra("upName", body).putExtra("upDescribe", decribe).putExtra("companyId",hxid).putExtra("companyName",comName));
//                        finish();
//                    }else {
//                        startActivity(new Intent(ZYDetailActivity.this, ProfileUpdateTwoActivity.class).putExtra("type", type).putExtra("image", imageStr)
//                                .putExtra("zyResv3", zyType).putExtra("upName", body).putExtra("upDescribe", decribe));
//                        finish();
//                    }
//                }
//            });
//        }else {
//            tv_fenxiang.setText("分享");
//            tv_fenxiang.setVisibility(View.VISIBLE);
//            tv_fenxiang.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    saveCurrentImage();
//                }
//            });
//        }



        tv_fenxiang.setText("编辑");
        tv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = ProfileUpdateTwoActivity.TYPE_MAJOR1;
                if ("02".equals(zhuanye)) {
                    type = ProfileUpdateTwoActivity.TYPE_MAJOR2;
                }else if ("03".equals(zhuanye)){
                    type = ProfileUpdateTwoActivity.TYPE_MAJOR3;
                }else if ("04".equals(zhuanye)){
                    type = ProfileUpdateTwoActivity.TYPE_MAJOR4;
                }else {
                    type = ProfileUpdateTwoActivity.TYPE_MAJOR1;
                }
                if ("001".equals(distance)){
                    startActivity(new Intent(ZYDetailActivity.this, CompanyUpdateTwoActivity.class).putExtra("type", type).putExtra("image", imageStr)
                            .putExtra("zyResv3", zyType).putExtra("upName", body).putExtra("upDescribe", decribe).putExtra("companyId",hxid).putExtra("companyName",comName));
                    finish();
                }else {
                    startActivity(new Intent(ZYDetailActivity.this, ProfileUpdateTwoActivity.class).putExtra("type", type).putExtra("image", imageStr)
                            .putExtra("zyResv3", zyType).putExtra("upName", body).putExtra("upDescribe", decribe).putExtra("userId", hxid));
                    finish();
                }
            }
        });


    }

    private void saveCurrentImage(){
        String chuDname;
        if (name==null||"".equals(name)){
            try {
                comName = URLDecoder.decode(comName,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            chuDname = comName;
        }else {
            chuDname = name;
        }
        ScreenshotUtil.getBitmapByView(ZYDetailActivity.this, findViewById(R.id.ll1), chuDname, null,11,false,0,0);
        String filep = Environment.getExternalStorageDirectory() + "/zhengshier/fenxiang/mingpCut2.png";
        if (!new File(filep).exists()){
            ScreenshotUtil.saveDrawableById(ZYDetailActivity.this,R.drawable.share_mingpian);
        }
        fenxiang();
    }

    private void updateTJzhuanfa(final String loginId,final int type) {
        String url = FXConstant.URL_TONGJI_ZHUANFA;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Userdetailac,add","增加浏览次数成功"+s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null) {
                    Log.e("Userdetailac,add", "增加浏览次数错误");
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uLoginId",loginId);
                param.put("homePage", "1");
                if (type==0) {
                    param.put("type", "qqHomePage");
                }else if (type==1){
                    param.put("type", "weixinHomePage");
                }else if (type==2){
                    param.put("type", "weiboHomePage");
                }
                param.put("f_id",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(ZYDetailActivity.this).addToRequestQueue(request);
    }

    private void fenxiang() {
        LayoutInflater inflater5 = LayoutInflater.from(ZYDetailActivity.this);
        final RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_fenxiang, null);
        final Dialog dialog = new AlertDialog.Builder(ZYDetailActivity.this,R.style.Dialog).create();
        dialog.show();
        Window window = dialog.getWindow();
        dialog.show();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        //这句就是设置dialog横向满屏了。
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.getWindow().setContentView(layout5);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        TextView tv4 = (TextView) layout5.findViewById(R.id.tv4);
        TextView tv5 = (TextView) layout5.findViewById(R.id.tv5);
        RelativeLayout rl1 = (RelativeLayout) layout5.findViewById(R.id.rl1);
        RelativeLayout rl2 = (RelativeLayout) layout5.findViewById(R.id.rl2);
        RelativeLayout rl3 = (RelativeLayout) layout5.findViewById(R.id.rl3);
        RelativeLayout rl4 = (RelativeLayout) layout5.findViewById(R.id.rl4);
        RelativeLayout rl5 = (RelativeLayout) layout5.findViewById(R.id.rl5);
        RelativeLayout rl6 = (RelativeLayout) layout5.findViewById(R.id.rl6);
        rl6.setVisibility(View.INVISIBLE);
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtoqqz();
            }
        });
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtowxm();
            }
        });
        rl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtowb();
            }
        });
        rl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtoqqf();
            }
        });
        rl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                fxtowxf();
            }
        });
    }

    private void fxtoqqf() {
        final QQ.ShareParams sp = new QQ.ShareParams();
        LayoutInflater inflaterDl = LayoutInflater.from(ZYDetailActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(ZYDetailActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qq.share(sp);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitleUrl("http://www.fulu86.com/Details_zhuanye.html?upId=" + upId);
                sp.setSiteUrl("http://www.fulu86.com/Details_zhuanye.html?upId=" + upId);
                sp.setSite("分享链接");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                sp.setText("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包");
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qq.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qq.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fxtoqqz() {
        final QZone.ShareParams sp = new QZone.ShareParams();
        LayoutInflater inflaterDl = LayoutInflater.from(ZYDetailActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(ZYDetailActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle(null);
                sp.setText(null);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//                        updateLiulancishu();
//                        updateTJzhuanfa(hxid,0);
                        Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qzone.share(sp);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitleUrl("http://www.fulu86.com/Details_zhuanye.html?upId=" + upId);
                sp.setSiteUrl("http://www.fulu86.com/Details_zhuanye.html?upId=" + upId);
                sp.setSite("分享链接");
                sp.setTitle("正事多-接单派单工具");
                sp.setText("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包");
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                qzone.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//                        updateLiulancishu();
//                        updateTJzhuanfa(hxid,0);
                        Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                qzone.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fxtowxm() {
        final WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        LayoutInflater inflaterDl = LayoutInflater.from(ZYDetailActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(ZYDetailActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//                        updateLiulancishu();
//                        updateTJzhuanfa(hxid,1);
                        Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitle("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包");
                sp.setUrl("http://www.fulu86.com/Details_zhuanye.html?upId=" + upId);
                Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//                        updateLiulancishu();
//                        updateTJzhuanfa(hxid,1);
                        Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fxtowxf() {
        final Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        LayoutInflater inflaterDl = LayoutInflater.from(ZYDetailActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(ZYDetailActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("【正事多】里面有接单派单名片红包");
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                sp.setTitle("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包");
                sp.setUrl("http://www.fulu86.com/Details_zhuanye.html?upId=" + upId);
                Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wx.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        Toast.makeText(getApplicationContext(), "成功分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wx.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void fxtowb() {
        final SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        LayoutInflater inflaterDl = LayoutInflater.from(ZYDetailActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(ZYDetailActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        tv_item1.setText("分享图文");
        tv_item2.setText("分享链接");
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//                        updateLiulancishu();
//                        updateTJzhuanfa(hxid,2);
                        Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wb.share(sp);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String url = "http://www.fulu86.com/Details_zhuanye.html?upId=" + upId;
                sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut2.png");
                sp.setText("我转发他的主页名片，"+fxUpName+"不限行业接派单，全民分享赚红包"+url);
                sp.setTitle("正事多-接单派单工具");
                Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
                wb.setPlatformActionListener(new PlatformActionListener() {
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
                    }

                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//                        updateLiulancishu();
//                        updateTJzhuanfa(hxid,2);
                        Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
                    }

                    public void onCancel(Platform arg0, int arg1) {
                        Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
                    }
                });
// 执行图文分享
                wb.share(sp);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void addToDingdan(final String wodezhanghao, final String hxid, final String zy1 ,final String typeDetail) {
        String bs;
        if (hxid!=null&&hxid.length()>11){
            bs = "02";
        }else {
            bs = "00";
        }
        if (hxid.equals(wodezhanghao)) {
            bs = "08";
        }
        if ("01".equals(zyType)) {
            Intent intent = new Intent(ZYDetailActivity.this, UOrderDetailActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", body);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", bs);
            startActivity(intent);
        }else if ("02".equals(zyType)){
            Intent intent = new Intent(ZYDetailActivity.this, UOrderDetailTwoActivity.class);
            intent.putExtra("distance", distance);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", body);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", bs);
            startActivity(intent);
        }else if ("03".equals(zyType)){
            Intent intent = new Intent(ZYDetailActivity.this, UOrderDetailThreeActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", body);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", bs);
            startActivity(intent);
        }else if ("04".equals(zyType)){
            Intent intent = new Intent(ZYDetailActivity.this, UOrderDetailFourActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", body);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", bs);
            startActivity(intent);
        }else if ("05".equals(zyType)){
            Intent intent = new Intent(ZYDetailActivity.this, UOrderDetailFiveActivity.class);
            intent.putExtra("wodezhanghao", wodezhanghao);
            intent.putExtra("hxid", hxid);
            intent.putExtra("zy1", zy1);
            intent.putExtra("orderBody", body);
            intent.putExtra("pypass", pass);
            intent.putExtra("typeDetail", typeDetail);
            intent.putExtra("biaoshi", bs);
            startActivity(intent);
        }
    }

    private void showDialog(final String wodezhanghao) {
        final String[] typeDetail = new String[1];
        LayoutInflater inflaterDl = LayoutInflater.from(ZYDetailActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(ZYDetailActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        View v2 = dialog.findViewById(R.id.v2);
        v2.setVisibility(View.GONE);
        re_item2.setVisibility(View.GONE);
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                typeDetail[0] = "01";
                addToDingdan(wodezhanghao,hxid,upId, typeDetail[0]);
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                typeDetail[0] = "02";
                addToDingdan(wodezhanghao,hxid,upId, typeDetail[0]);
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
        setResult(RESULT_OK);
    }
}
