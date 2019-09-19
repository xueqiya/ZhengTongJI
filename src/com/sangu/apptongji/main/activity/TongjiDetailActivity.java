package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.sangu.apptongji.main.alluser.entity.LiuLanDetail;
import com.sangu.apptongji.main.alluser.entity.ZhuFaDetail;
import com.sangu.apptongji.main.alluser.presenter.ILiulanListPresenter;
import com.sangu.apptongji.main.alluser.presenter.IZhuFaListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.LiulanListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ZhuFaListPresenter;
import com.sangu.apptongji.main.alluser.view.ILiulanListView;
import com.sangu.apptongji.main.alluser.view.IZhuanFaListView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ScreenshotUtil;
import com.sangu.apptongji.main.utils.SingleBean;
import com.sangu.apptongji.main.widget.ArcView;
import com.sangu.apptongji.main.widget.SingleView;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Created by Administrator on 2017-08-30.
 */

public class TongjiDetailActivity extends BaseActivity implements ILiulanListView,IZhuanFaListView{
    private ArcView arcView1;
    private ArcView arcView2;
    private ArcView arcView3;
    private ArcView arc_order;
    private TextView tv_phone,tv_message,tv_chat,tv_sendorder,tv_order_count;
    private TextView tv_none1,tv_notOrder,tv_liulan_count,tv_zhuye,tv_xinzeng_zhuye,tv_fenxiang
            ,tv_shenghuo,tv_xinzeng_shenhuo,tv_zuobiao,tv_xinzneg_zuobiao,tv_shangye,tv_xinzneg_shangye;
    private TextView tv_none2,tv_zhufa_count,tv2_zhuye,tv2_xinzeng_zhuye
            ,tv2_shenghuo,tv2_xinzeng_shenhuo,tv2_zuobiao,tv2_xinzneg_zuobiao,tv2_shangye,tv2_xinzneg_shangye;
    private TextView tv_none3,tv_haoping,tv_xinzeng_haoping,tv_zhongping,tv_xinzeng_zhongping,tv_chaping
            ,tv_xinzeng_chaping,tv_pingjia_count,tv_count;
    private RelativeLayout rl1_all,rl2_all,rl1,rl2,rl3,rl4,rl5,rl6,rl7,rl8,rl3_all,rl9,rl10,rl11;
    private ILiulanListPresenter liuLanPresenter;
    private IZhuFaListPresenter zhuFaPresenter;
    private boolean isliuFirst = true;
    private boolean iszhuFirst = true;
    private boolean ispinFirst = true;
    private String hxid,biaoshi,qiyeId;
    private SingleView mMySingleChartView;
    private RelativeLayout llSingle;
    private List<SingleBean> singlelist = new ArrayList<>();
    private List<Integer> timeList = new ArrayList<>();
    private RelativeLayout rlSingle;
    private TextView tv_qq_ll,tv_weixin_ll,tv_weibo_ll,tv_app_ll,tv_qq_zf,tv_weixin_zf,tv_weibo_zf,tv_app_zf;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_tongji);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        liuLanPresenter = new LiulanListPresenter(this,this);
        zhuFaPresenter = new ZhuFaListPresenter(this,this);
        hxid = getIntent().getStringExtra(FXConstant.JSON_KEY_HXID);
        biaoshi = getIntent().getStringExtra("biaoshi");
        qiyeId = getIntent().getStringExtra("qiyeId");
        rl1 = (RelativeLayout) findViewById(R.id.rl1);
        rl2 = (RelativeLayout) findViewById(R.id.rl2);
        rl3 = (RelativeLayout) findViewById(R.id.rl3);
        rl4 = (RelativeLayout) findViewById(R.id.rl4);
        rl5 = (RelativeLayout) findViewById(R.id.rl5);
        rl6 = (RelativeLayout) findViewById(R.id.rl6);
        rl7 = (RelativeLayout) findViewById(R.id.rl7);
        rl8 = (RelativeLayout) findViewById(R.id.rl8);
        rl9 = (RelativeLayout) findViewById(R.id.rl9);
        rl10 = (RelativeLayout) findViewById(R.id.rl10);
        rl11 = (RelativeLayout) findViewById(R.id.rl11);
        rl1_all = (RelativeLayout) findViewById(R.id.rl1_all);
        rl2_all = (RelativeLayout) findViewById(R.id.rl2_all);
        rl3_all = (RelativeLayout) findViewById(R.id.rl3_all);
        rlSingle = (RelativeLayout) findViewById(R.id.rl4_all);
        arcView1 = (ArcView) findViewById(R.id.arc1);
        arcView2 = (ArcView) findViewById(R.id.arc2);
        arcView3 = (ArcView) findViewById(R.id.arc3);
        arc_order = (ArcView) findViewById(R.id.arc_order);
        tv_phone = (TextView) findViewById(R.id.tv2order_zhuye);
        tv_message = (TextView) findViewById(R.id.tv2_shenghuo_order);
        tv_chat = (TextView) findViewById(R.id.tv2_zuobiao_order);
        tv_sendorder = (TextView) findViewById(R.id.tv2_shangye_order);
        tv_order_count = (TextView) findViewById(R.id.tv_order_count);
        tv_qq_ll = (TextView) findViewById(R.id.tv_qq_ll);
        tv_weixin_ll = (TextView) findViewById(R.id.tv_weixin_ll);
        tv_weibo_ll = (TextView) findViewById(R.id.tv_weibo_ll);
        tv_app_ll = (TextView) findViewById(R.id.tv_app_ll);
        tv_qq_zf = (TextView) findViewById(R.id.tv_qq_zf);
        tv_weixin_zf = (TextView) findViewById(R.id.tv_weixin_zf);
        tv_weibo_zf = (TextView) findViewById(R.id.tv_weibo_zf);
        tv_app_zf = (TextView) findViewById(R.id.tv_app_zf);
        tv_fenxiang = (TextView) findViewById(R.id.tv_fenxiang);
        tv_none1 = (TextView) findViewById(R.id.tv_none1);
        tv_none2 = (TextView) findViewById(R.id.tv_none2);
        tv_notOrder = (TextView) findViewById(R.id.tv_notOrder);
        tv_none3 = (TextView) findViewById(R.id.tv_none3);
        tv_haoping = (TextView) findViewById(R.id.tv_haoping);
        tv_xinzeng_haoping = (TextView) findViewById(R.id.tv_xinzeng_haoping);
        tv_zhongping = (TextView) findViewById(R.id.tv_zhongping);
        tv_xinzeng_zhongping = (TextView) findViewById(R.id.tv_xinzeng_zhongping);
        tv_chaping = (TextView) findViewById(R.id.tv_chaping);
        tv_xinzeng_chaping = (TextView) findViewById(R.id.tv_xinzeng_chaping);
        tv_pingjia_count = (TextView) findViewById(R.id.tv_pingjia_count);
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_liulan_count = (TextView) findViewById(R.id.tv_liulan_count);
        tv_zhufa_count = (TextView) findViewById(R.id.tv_zhufa_count);
        tv_zhuye = (TextView) findViewById(R.id.tv_zhuye);
        tv_xinzeng_zhuye = (TextView) findViewById(R.id.tv_xinzeng_zhuye);
        tv_shenghuo = (TextView) findViewById(R.id.tv_shenghuo);
        tv_xinzeng_shenhuo = (TextView) findViewById(R.id.tv_xinzeng_shenhuo);
        tv_zuobiao = (TextView) findViewById(R.id.tv_zuobiao);
        tv_xinzneg_zuobiao = (TextView) findViewById(R.id.tv_xinzneg_zuobiao);
        tv_shangye = (TextView) findViewById(R.id.tv_shangye);
        tv_xinzneg_shangye = (TextView) findViewById(R.id.tv_xinzneg_shangye);
        tv2_zhuye = (TextView) findViewById(R.id.tv2_zhuye);
        tv2_xinzeng_zhuye = (TextView) findViewById(R.id.tv2_xinzeng_zhuye);
        tv2_shenghuo = (TextView) findViewById(R.id.tv2_shenghuo);
        tv2_xinzeng_shenhuo = (TextView) findViewById(R.id.tv2_xinzeng_shenhuo);
        tv2_zuobiao = (TextView) findViewById(R.id.tv2_zuobiao);
        tv2_xinzneg_zuobiao = (TextView) findViewById(R.id.tv2_xinzneg_zuobiao);
        tv2_shangye = (TextView) findViewById(R.id.tv2_shangye);
        tv2_xinzneg_shangye = (TextView) findViewById(R.id.tv2_xinzneg_shangye);
        if ("00".equals(biaoshi)) {
            rl1_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TongjiDetailActivity.this, LiuLanListActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid).putExtra("type", "00").putExtra("leixing", "homePage"));
                }
            });
            rl2_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TongjiDetailActivity.this, LiuLanListActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid).putExtra("type", "01").putExtra("leixing", "homePage"));
                }
            });
            rl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TongjiDetailActivity.this, LiuLanListActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid).putExtra("type", "00").putExtra("leixing", "homePage"));
                }
            });
            rl5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TongjiDetailActivity.this, LiuLanListActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid).putExtra("type", "01").putExtra("leixing", "homePage"));
                }
            });
            rl2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TongjiDetailActivity.this, LiuLanListActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid).putExtra("type", "00").putExtra("leixing", "lifeDynamics"));
                }
            });
            rl6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TongjiDetailActivity.this, LiuLanListActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid).putExtra("type", "01").putExtra("leixing", "lifeDynamics"));
                }
            });
            rl3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TongjiDetailActivity.this, LiuLanListActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid).putExtra("type", "00").putExtra("leixing", "locationDynamics"));
                }
            });
            rl7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TongjiDetailActivity.this, LiuLanListActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid).putExtra("type", "01").putExtra("leixing", "locationDynamics"));
                }
            });
            rl4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TongjiDetailActivity.this, LiuLanListActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid).putExtra("type", "00").putExtra("leixing", "businessDynamics"));
                }
            });
            rl8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(TongjiDetailActivity.this, LiuLanListActivity.class).putExtra(FXConstant.JSON_KEY_HXID, hxid).putExtra("type", "01").putExtra("leixing", "businessDynamics"));
                }
            });
        }
        rl3_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TongjiDetailActivity.this,PingJiaListActivity.class).putExtra("userId",hxid).putExtra("type","2").putExtra("biaoshi",biaoshi));
            }
        });
        rl9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TongjiDetailActivity.this,PingJiaListActivity.class).putExtra("userId",hxid).putExtra("type", "2").putExtra("biaoshi",biaoshi));
            }
        });
        rl10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TongjiDetailActivity.this,PingJiaListActivity.class).putExtra("userId",hxid).putExtra("type", "1").putExtra("biaoshi",biaoshi));
            }
        });
        rl11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TongjiDetailActivity.this,PingJiaListActivity.class).putExtra("userId",hxid).putExtra("type", "0").putExtra("biaoshi",biaoshi));
            }
        });
        tv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentImage();
            }
        });
        if (qiyeId!=null&&!"".equals(qiyeId)) {
            getSingleData();
        }else {
            rlSingle.setVisibility(View.GONE);
        }

        GetContactInfoData();
    }



    //请求订单咨询
    private void GetContactInfoData(){

        String url = FXConstant.URL_CONTACTDATA;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {

                    com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                    String code1 = object.getString("code");

                    if (code1.equals("无")){

                        //没有数据 直接全显示0
                        arc_order.setVisibility(View.INVISIBLE);
                        tv_notOrder.setVisibility(View.VISIBLE);


                    }else {

                        com.alibaba.fastjson.JSONObject code = object.getJSONObject("code");

                        //有数据  初始化统计图
                        arc_order.setVisibility(View.VISIBLE);
                        tv_notOrder.setVisibility(View.INVISIBLE);

                        Integer phone = code.getInteger("phone");
                        Integer message = code.getInteger("message");
                        Integer chat = code.getInteger("chat");
                        Integer messageOrder = code.getInteger("messageOrder");
                        Integer dynamicPush = code.getInteger("dynamicPush");
                        Integer sendOrder = messageOrder+dynamicPush;

                        tv_phone.setText("电话接单("+phone+")");
                        tv_message.setText("短信接单("+message+")");
                        tv_chat.setText("聊天接单("+chat+")");
                        tv_sendorder.setText("短信派单("+sendOrder+")");

                        Integer all = phone + message + chat + sendOrder;

                        tv_order_count.setText("订 单 "+all+" 次");

                        List<Times> times = new ArrayList<>();
                        Times t1 = new Times();
                        Times t2 = new Times();
                        Times t3 = new Times();
                        Times t4 = new Times();
                        int peopleSum1 = 0, peopleSum2 = 0, peopleSum3 = 0, peopleSum4 = 0;
                        peopleSum1 = phone;
                        t1.hour = peopleSum1;
                        t1.text = "电话接单";
                        times.add(t1);
                        peopleSum2 = message;
                        t2.hour = peopleSum2;
                        t2.text = "短信接单";
                        times.add(t2);
                        peopleSum3 = chat;
                        t3.hour = peopleSum3;
                        t3.text = "聊天接单";
                        times.add(t3);
                        peopleSum4 = sendOrder;
                        t4.hour = peopleSum4;
                        t4.text = "短信派单";
                        times.add(t4);
                        ArcView.ArcViewAdapter myAdapter = arc_order.new ArcViewAdapter<Times>() {
                            @Override
                            public double getValue(Times times) {
                                return times.hour;
                            }
                        };//设置adapter
                        myAdapter.setData(times);//设置数据集
                        arc_order.setMaxNum(4);//设置可以显示的最大数值 该数值之后的会合并为其他

                    }

                } catch (com.alibaba.fastjson.JSONException e) {
                    e.printStackTrace();

                }

            }

        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                arc_order.setVisibility(View.INVISIBLE);
                tv_notOrder.setVisibility(View.VISIBLE);

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();

                map.put("uId",hxid);

                return map;
            }
        };

        MySingleton.getInstance(TongjiDetailActivity.this).addToRequestQueue(request);

    }


    private void getSingleData() {
        Log.e("tongjiac,1",qiyeId);
        singlelist = new ArrayList<>();
        String url = FXConstant.URL_QUERY_READDCOUNT+qiyeId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                JSONArray array = object.getJSONArray("list");
                String sum = object.getString("sum");
                if (sum!=null&&!"".equals(sum)){
                    tv_count.setText("企业浏览"+sum+"次");
                }else {
                    tv_count.setText("企业浏览0次");
                }
                if (array==null||array.size()==0){
                    rlSingle.setVisibility(View.GONE);
                }else {
                    rlSingle.setVisibility(View.VISIBLE);
                    int count = array.size();
                    if (count>5){
                        count=5;
                    }
                    int beishu = 1;
                    for (int i=0;i<count;i++){
                        com.alibaba.fastjson.JSONObject object1 = array.getJSONObject(i);
                        com.alibaba.fastjson.JSONObject info = object1.getJSONObject("cInfo");
                        int times = object1.getInteger("times");
                        timeList.add(times);
                        if (i==0) {
                            beishu = getbeishu(times);
                        }
                        SingleBean bean = new SingleBean();
                        if (beishu>0) {
                            bean.setCount(times*beishu);
                        }else {
                            bean.setCount(times/beishu);
                        }
                        bean.setImaUrl(info.getString("uImage"));
                        bean.setName(info.getString("uName"));
                        bean.setSex(info.getString("uSex"));
                        bean.setLoginId(info.getString("uLoginId"));
                        singlelist.add(bean);
                    }
                    initSingleView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                rlSingle.setVisibility(View.GONE);
            }
        });
        MySingleton.getInstance(TongjiDetailActivity.this).addToRequestQueue(request);
    }

    private int getbeishu(int times) {
        int beishu = 1;
        if (times<5){
            beishu = 20;
        }else if (times<10){
            beishu = 10;
        }else if (times<20){
            beishu = 5;
        }else if (times<30){
            beishu = 3;
        }else if (times<50){
            beishu = 2;
        }else if (times<100){
            beishu = 1;
        }else if (times<500){
            beishu = -5;
        }else if (times<1000){
            beishu = -10;
        }else if (times<2000){
            beishu = -20;
        }else if (times<3000){
            beishu = -30;
        }else if (times<4000){
            beishu = -40;
        }else if (times<5000){
            beishu = -50;
        }
        return beishu;
    }

    private void initSingleView() {
        mMySingleChartView = (SingleView) findViewById(R.id.my_single_chart_view);
        mMySingleChartView.setList(singlelist);
        //原理同双柱
        mMySingleChartView.setListener(new SingleView.getNumberListener() {
            @Override
            public void getNumber(List<SingleBean> datas) {
                for (int i=0 ;i<datas.size();i++){
                    llSingle = (RelativeLayout) LayoutInflater.from(TongjiDetailActivity.this).inflate(R.layout.layout_pro_expense, null);
                    String image = singlelist.get(i).getImaUrl();
                    String name = singlelist.get(i).getName();
                    final String loginId = singlelist.get(i).getLoginId();
                    String sex = singlelist.get(i).getSex();
                    int x = datas.get(i).getX();
                    int y = datas.get(i).getY();
                    TextView tv1 = (TextView) llSingle.findViewById(R.id.tv1);
                    TextView tv = (TextView) llSingle.findViewById(R.id.tv);
                    ImageView iv1 = (ImageView) llSingle.findViewById(R.id.iv1);
                    tv.setText(timeList.get(i)+"");
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.leftMargin = x - 70;
                    params.topMargin = y - 30;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        llSingle.measure(0, 0);
                    }
                    llSingle.setLayoutParams(params);
                    if (image==null||"".equals(image)){
                        tv1.setVisibility(View.VISIBLE);
                        iv1.setVisibility(View.INVISIBLE);
                        tv1.setText(name==null?loginId:name);
                        if ("00".equals(sex)){
                            tv1.setBackgroundResource(R.drawable.fx_bg_text_red);
                        }else {
                            tv1.setBackgroundResource(R.drawable.fx_bg_text_gra);
                        }
                    }else {
                        image = image.split("\\|")[0];
                        tv1.setVisibility(View.INVISIBLE);
                        iv1.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+image,iv1, DemoApplication.mOptions);
                    }
                    iv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(TongjiDetailActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
                        }
                    });
                    tv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(TongjiDetailActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,loginId));
                        }
                    });
                    rlSingle.addView(llSingle);
                }
            }
        });
        mMySingleChartView.setItemOneListener(new SingleView.ItemOnclickListener() {
            @Override
            public void ItemOnclick(int position) {
                String hxId = singlelist.get(0).getLoginId();
                if (position==0&&singlelist.size()>0){
                    hxId = singlelist.get(0).getLoginId();
                }else if (position==1&&singlelist.size()>1){
                    hxId = singlelist.get(1).getLoginId();
                }else if (position==2&&singlelist.size()>2){
                    hxId = singlelist.get(2).getLoginId();
                }else if (position==3&&singlelist.size()>3){
                    hxId = singlelist.get(3).getLoginId();
                }else if (position==4&&singlelist.size()>4){
                    hxId = singlelist.get(4).getLoginId();
                }
                startActivity(new Intent(TongjiDetailActivity.this,PaiMgListActivity.class).putExtra("qiyeId",qiyeId).putExtra("com_id",hxId));
            }
        });
    }

    private void saveCurrentImage() {
        ScreenshotUtil.getBitmapByView(TongjiDetailActivity.this, findViewById(R.id.ll_all), "分享名片红包", null,12,false,0,0);
        LayoutInflater inflater5 = LayoutInflater.from(TongjiDetailActivity.this);
        final RelativeLayout layout5 = (RelativeLayout) inflater5.inflate(R.layout.dialog_fenxiang, null);
        final Dialog dialog = new AlertDialog.Builder(TongjiDetailActivity.this).create();
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
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setText(null);
        sp.setTitle(null);
        sp.setTitleUrl(null);
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qq.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到QQ好友失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                if (!hxid.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                    updateTJzhuanfa(hxid,0);
                }
                Toast.makeText(getApplicationContext(), "成功分享到QQ好友！", Toast.LENGTH_SHORT).show();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到QQ好友！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qq.share(sp);
    }

    private void fxtoqqz() {
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setText(null);
        sp.setTitle(null);
        sp.setTitleUrl(null);
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform qqz = ShareSDK.getPlatform(QZone.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        qqz.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到QQ空间失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                if (!hxid.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                    updateTJzhuanfa(hxid,0);
                }
                Toast.makeText(getApplicationContext(), "成功分享到QQ空间！", Toast.LENGTH_SHORT).show();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到QQ空间！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        qqz.share(sp);
    }

    private void fxtowxm() {
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("正事多app");
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微信朋友圈失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                if (!hxid.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                    updateTJzhuanfa(hxid,1);
                }
                Toast.makeText(getApplicationContext(), "成功分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微信朋友圈！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);
    }

    private void fxtowxf() {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("正事多app");
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform wx = ShareSDK.getPlatform(Wechat.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wx.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微信好友失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                if (!hxid.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                    updateTJzhuanfa(hxid,1);
                }
                Toast.makeText(getApplicationContext(), "成功分享到微信好友！", Toast.LENGTH_SHORT).show();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微信好友！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wx.share(sp);
    }

    private void fxtowb() {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle("正事多app");
        sp.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
        Platform wb = ShareSDK.getPlatform(SinaWeibo.NAME);
// 设置分享事件回调（注：回调放在不能保证在主线程调用，不可以在里面直接处理UI操作）
        wb.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                Toast.makeText(getApplicationContext(), "分享到微博失败！", Toast.LENGTH_SHORT).show();
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                if (!hxid.equals(DemoHelper.getInstance().getCurrentUsernName())) {
                    updateTJzhuanfa(hxid,2);
                }
                Toast.makeText(getApplicationContext(), "成功分享到微博！", Toast.LENGTH_SHORT).show();
            }

            public void onCancel(Platform arg0, int arg1) {
                Toast.makeText(getApplicationContext(), "取消分享到微博！", Toast.LENGTH_SHORT).show();
            }
        });
// 执行图文分享
        wb.share(sp);
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
                param.put("f_id", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(TongjiDetailActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (liuLanPresenter!=null) {
            liuLanPresenter.loadLiuLanList("1", hxid, null);
        }
        if (zhuFaPresenter!=null) {
            zhuFaPresenter.loadZhuFaList("1", hxid, null);
        }
        queryPinjiaList();
    }

    private void queryPinjiaList() {
        String url = FXConstant.URL_QUERY_PINJIALIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String praiseTime = object.getString("praiseTime");//好
                String pertinentTimes = object.getString("PertinentTimes");//中
                String badTime = object.getString("BadTime");//差
                if (praiseTime==null||"".equals(praiseTime)){
                    praiseTime="0";
                }
                if (pertinentTimes==null||"".equals(pertinentTimes)){
                    pertinentTimes="0";
                }
                if (badTime==null||"".equals(badTime)){
                    badTime="0";
                }

                if ("0".equals(praiseTime)&& "0".equals(pertinentTimes)&& "0".equals(badTime)){
                    tv_none3.setVisibility(View.VISIBLE);
                    arcView3.setVisibility(View.INVISIBLE);
                    tv_xinzeng_haoping.setVisibility(View.INVISIBLE);
                    tv_xinzeng_zhongping.setVisibility(View.INVISIBLE);
                    tv_xinzeng_chaping.setVisibility(View.INVISIBLE);
                }else {
                    SharedPreferences sp = getSharedPreferences("sangu_pingjia_count", Context.MODE_PRIVATE);
                    String praiseTime1 = sp.getString("praiseTime","0");
                    String pertinentTimes1 = sp.getString("pertinentTimes","0");
                    String badTime1 = sp.getString("badTime","0");
                    tv_none3.setVisibility(View.INVISIBLE);
                    arcView3.setVisibility(View.VISIBLE);
                    if (Integer.valueOf(praiseTime)>Integer.valueOf(praiseTime1)){
                        tv_xinzeng_haoping.setVisibility(View.VISIBLE);
                        tv_xinzeng_haoping.setText(String.valueOf(Integer.valueOf(praiseTime)-Integer.valueOf(praiseTime1)));
                    }else {
                        tv_xinzeng_haoping.setVisibility(View.INVISIBLE);
                    }
                    if (Integer.valueOf(pertinentTimes)>Integer.valueOf(pertinentTimes1)){
                        tv_xinzeng_zhongping.setVisibility(View.VISIBLE);
                        tv_xinzeng_zhongping.setText(String.valueOf(Integer.valueOf(pertinentTimes)-Integer.valueOf(pertinentTimes1)));
                    }else {
                        tv_xinzeng_zhongping.setVisibility(View.INVISIBLE);
                    }
                    if (Integer.valueOf(badTime)>Integer.valueOf(badTime1)){
                        tv_xinzeng_chaping.setVisibility(View.VISIBLE);
                        tv_xinzeng_chaping.setText(String.valueOf(Integer.valueOf(badTime)-Integer.valueOf(badTime1)));
                    }else {
                        tv_xinzeng_chaping.setVisibility(View.INVISIBLE);
                    }
                    int allCount = Integer.parseInt(praiseTime)+Integer.parseInt(pertinentTimes)+Integer.parseInt(badTime);
                    tv_haoping.setText("好评("+praiseTime+")");
                    tv_zhongping.setText("中评("+pertinentTimes+")");
                    tv_chaping.setText("差评("+badTime+")");
                    tv_pingjia_count.setText("评 价 "+allCount+" 次");
                    if (ispinFirst) {
                        List<Times> times = new ArrayList<>();
                        Times t1 = new Times();
                        Times t2 = new Times();
                        Times t3 = new Times();
                        int peopleSum1 = 0, peopleSum2 = 0, peopleSum3 = 0;
                        peopleSum1 = Integer.valueOf(praiseTime);
                        t1.hour = peopleSum1;
                        t1.text = "好评";
                        times.add(t1);
                        peopleSum2 = Integer.valueOf(pertinentTimes);
                        t2.hour = peopleSum2;
                        t2.text = "中评";
                        times.add(t2);
                        peopleSum3 = Integer.valueOf(badTime);
                        t3.hour = peopleSum3;
                        t3.text = "差评";
                        times.add(t3);
                        ArcView.ArcViewAdapter myAdapter = arcView3.new ArcViewAdapter<Times>() {
                            @Override
                            public double getValue(Times times) {
                                return times.hour;
                            }
                        };//设置adapter
                        myAdapter.setData(times);//设置数据集
                        arcView3.setMaxNum(3);//设置可以显示的最大数值 该数值之后的会合并为其他
                        ispinFirst = false;
                        if ("01".equals(biaoshi)) {
                            tv_xinzeng_haoping.setVisibility(View.INVISIBLE);
                            tv_xinzeng_zhongping.setVisibility(View.INVISIBLE);
                            tv_xinzeng_chaping.setVisibility(View.INVISIBLE);
                        }
                    }
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
                param.put("userId",hxid);
                param.put("currentPage","1");
                param.put("commentType","2");
                return param;
            }
        };
        MySingleton.getInstance(TongjiDetailActivity.this).addToRequestQueue(request);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void updateLiuLanList(List<LiuLanDetail> liuLanDetails, JSONObject object,String size, boolean hasMore) {
        SharedPreferences sp = getSharedPreferences("sangu_liulan_count", Context.MODE_PRIVATE);
        String homePagel = sp.getString("homePage","0");
        String lifeDynamicsl = sp.getString("lifeDynamics","0");
        String locationDynamicsl = sp.getString("locationDynamics","0");
        String businessDynamicsl = sp.getString("businessDynamics","0");
        String homePage;
        String lifeDynamics;
        String locationDynamics;
        String businessDynamics;
        if (object!=null&&!"".equals(object)){
            int qqCount=0,weixinCount=0,weiboCount=0,appCount=0;
            String qqHomePage = object.optString("qqHomePage");
            String qqLifeDynamic = object.optString("qqLifeDynamic");
            String qqLocationDynamic = object.optString("qqLocationDynamic");
            String qqBusinessDynamic = object.optString("qqBusinessDynamic");
            String weixinHomePage = object.optString("weixinHomePage");
            String weixinLifeDynamic = object.optString("weixinLifeDynamic");
            String weixinLocationDynamic = object.optString("weixinLocationDynamic");
            String weixinBusinessDynamic = object.optString("weixinBusinessDynamic");
            String weiboHomePage = object.optString("weiboHomePage");
            String weiboLifeDynamic = object.optString("weiboLifeDynamic");
            String weiboLocationDynamic = object.optString("weiboLocationDynamic");
            String weiboBusinessDynamic = object.optString("weiboBusinessDynamic");
            homePage = object.optString("homePage");
            lifeDynamics = object.optString("lifeDynamics");
            locationDynamics = object.optString("locationDynamics");
            businessDynamics = object.optString("businessDynamics");
            tv_none1.setVisibility(View.INVISIBLE);
            if (!"".equals(qqHomePage)&&!"".equals(qqLifeDynamic)&&!"".equals(qqLocationDynamic)&&!"".equals(qqBusinessDynamic)){
                qqCount = Integer.parseInt(qqHomePage)+Integer.parseInt(qqLifeDynamic)+Integer.parseInt(qqLocationDynamic)+Integer.parseInt(qqBusinessDynamic);
            }
            if (!"".equals(weixinHomePage)&&!"".equals(weixinLifeDynamic)&&!"".equals(weixinLocationDynamic)&&!"".equals(weixinBusinessDynamic)){
                weixinCount = Integer.parseInt(weixinHomePage)+Integer.parseInt(weixinLifeDynamic)+Integer.parseInt(weixinLocationDynamic)+Integer.parseInt(weixinBusinessDynamic);
            }
            if (!"".equals(weiboHomePage)&&!"".equals(weiboLifeDynamic)&&!"".equals(weiboLocationDynamic)&&!"".equals(weiboBusinessDynamic)){
                weiboCount = Integer.parseInt(weiboHomePage)+Integer.parseInt(weiboLifeDynamic)+Integer.parseInt(weiboLocationDynamic)+Integer.parseInt(weiboBusinessDynamic);
            }
            appCount = Integer.parseInt(size) - qqCount - weixinCount - weiboCount;
            tv_qq_ll.setText(qqCount+"");
            tv_weixin_ll.setText(weixinCount+"");
            tv_weibo_ll.setText(weiboCount+"");
            tv_app_ll.setText(appCount+"");
        }else {
            homePage = "0";
            lifeDynamics = "0";
            locationDynamics = "0";
            businessDynamics = "0";
            tv_none1.setVisibility(View.VISIBLE);
        }
        if (homePage==null||"".equals(homePage)||homePage.equalsIgnoreCase("null")){
            homePage = "0";
        }
        if (Integer.valueOf(homePage)>Integer.valueOf(homePagel)){
            tv_xinzeng_zhuye.setVisibility(View.VISIBLE);
            tv_xinzeng_zhuye.setText(String.valueOf(Integer.valueOf(homePage)-Integer.valueOf(homePagel)));
        }else {
            tv_xinzeng_zhuye.setVisibility(View.INVISIBLE);
        }
        if (lifeDynamics==null||"".equals(lifeDynamics)||lifeDynamics.equalsIgnoreCase("null")){
            lifeDynamics = "0";
        }
        if (Integer.valueOf(lifeDynamics)>Integer.valueOf(lifeDynamicsl)){
            tv_xinzeng_shenhuo.setVisibility(View.VISIBLE);
            tv_xinzeng_shenhuo.setText(String.valueOf(Integer.valueOf(lifeDynamics)-Integer.valueOf(lifeDynamicsl)));
        }else {
            tv_xinzeng_shenhuo.setVisibility(View.INVISIBLE);
        }
        if (locationDynamics==null||"".equals(locationDynamics)||locationDynamics.equalsIgnoreCase("null")){
            locationDynamics = "0";
        }
        if (Integer.valueOf(locationDynamics)>Integer.valueOf(locationDynamicsl)){
            tv_xinzneg_zuobiao.setVisibility(View.VISIBLE);
            tv_xinzneg_zuobiao.setText(String.valueOf(Integer.valueOf(locationDynamics)-Integer.valueOf(locationDynamicsl)));
        }else {
            tv_xinzneg_zuobiao.setVisibility(View.INVISIBLE);
        }
        if (businessDynamics==null||"".equals(businessDynamics)||businessDynamics.equalsIgnoreCase("null")){
            businessDynamics = "0";
        }
        if (Integer.valueOf(businessDynamics)>Integer.valueOf(businessDynamicsl)){
            tv_xinzneg_shangye.setVisibility(View.VISIBLE);
            tv_xinzneg_shangye.setText(String.valueOf(Integer.valueOf(businessDynamics)-Integer.valueOf(businessDynamicsl)));
        }else {
            tv_xinzneg_shangye.setVisibility(View.INVISIBLE);
        }
        if (size==null||"".equals(size)||size.equalsIgnoreCase("null")){
            size = "0";
        }
        tv_zhuye.setText("个人主页("+homePage+")");
        tv_shenghuo.setText("派单动态("+lifeDynamics+")");
        tv_zuobiao.setText("坐标动态("+locationDynamics+")");
        tv_shangye.setText("商业动态("+businessDynamics+")");
        if (size.equals("0")){
            arcView1.setVisibility(View.INVISIBLE);
            tv_none1.setVisibility(View.VISIBLE);
        }else {
            arcView1.setVisibility(View.VISIBLE);
            tv_none1.setVisibility(View.INVISIBLE);
            if (isliuFirst) {
                List<Times> times = new ArrayList<>();
                Times t1 = new Times();
                Times t2 = new Times();
                Times t3 = new Times();
                Times t4 = new Times();
                int peopleSum1 = 0, peopleSum2 = 0, peopleSum3 = 0, peopleSum4 = 0;
                peopleSum1 = Integer.valueOf(homePage);
                t1.hour = peopleSum1;
                t1.text = "个人主页";
                times.add(t1);
                peopleSum2 = Integer.valueOf(lifeDynamics);
                t2.hour = peopleSum2;
                t2.text = "派单动态";
                times.add(t2);
                peopleSum3 = Integer.valueOf(locationDynamics);
                t3.hour = peopleSum3;
                t3.text = "坐标动态";
                times.add(t3);
                peopleSum4 = Integer.valueOf(businessDynamics);
                t4.hour = peopleSum4;
                t4.text = "商业动态";
                times.add(t4);
                ArcView.ArcViewAdapter myAdapter = arcView1.new ArcViewAdapter<Times>() {
                    @Override
                    public double getValue(Times times) {
                        return times.hour;
                    }
                };//设置adapter
                myAdapter.setData(times);//设置数据集
                arcView1.setMaxNum(4);//设置可以显示的最大数值 该数值之后的会合并为其他
                isliuFirst = false;
            }
        }
        int liulan = Integer.valueOf(homePage)+Integer.valueOf(lifeDynamics)+Integer.valueOf(locationDynamics)+Integer.valueOf(businessDynamics);
        tv_liulan_count.setText("浏 览 "+liulan+" 次");
        if ("01".equals(biaoshi)) {
            tv_xinzeng_zhuye.setVisibility(View.INVISIBLE);
            tv_xinzeng_shenhuo.setVisibility(View.INVISIBLE);
            tv_xinzneg_zuobiao.setVisibility(View.INVISIBLE);
            tv_xinzneg_shangye.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateZhuFaList(List<ZhuFaDetail> zhuFaDetails, JSONObject object, String size,boolean hasMore) {
        SharedPreferences sp = getSharedPreferences("sangu_zhufa_count", Context.MODE_PRIVATE);
        String homePagez = sp.getString("homePage","0");
        String lifeDynamicsz = sp.getString("lifeDynamics","0");
        String locationDynamicsz = sp.getString("locationDynamics","0");
        String businessDynamicsz = sp.getString("businessDynamics","0");
        String homePage;
        String lifeDynamics;
        String locationDynamics;
        String businessDynamics;
        if (object!=null&&!"".equals(object)){
            int qqCount=0,weixinCount=0,weiboCount=0,appCount=0;
            String qqHomePage = object.optString("qqHomePage");
            String qqLifeDynamic = object.optString("qqLifeDynamic");
            String qqLocationDynamic = object.optString("qqLocationDynamic");
            String qqBusinessDynamic = object.optString("qqBusinessDynamic");
            String weixinHomePage = object.optString("weixinHomePage");
            String weixinLifeDynamic = object.optString("weixinLifeDynamic");
            String weixinLocationDynamic = object.optString("weixinLocationDynamic");
            String weixinBusinessDynamic = object.optString("weixinBusinessDynamic");
            String weiboHomePage = object.optString("weiboHomePage");
            String weiboLifeDynamic = object.optString("weiboLifeDynamic");
            String weiboLocationDynamic = object.optString("weiboLocationDynamic");
            String weiboBusinessDynamic = object.optString("weiboBusinessDynamic");
            homePage = object.optString("homePage");
            lifeDynamics = object.optString("lifeDynamics");
            locationDynamics = object.optString("locationDynamics");
            businessDynamics = object.optString("businessDynamics");
            tv_none2.setVisibility(View.INVISIBLE);
            if (!"".equals(qqHomePage)&&!"".equals(qqLifeDynamic)&&!"".equals(qqLocationDynamic)&&!"".equals(qqBusinessDynamic)){
                qqCount = Integer.parseInt(qqHomePage)+Integer.parseInt(qqLifeDynamic)+Integer.parseInt(qqLocationDynamic)+Integer.parseInt(qqBusinessDynamic);
            }
            if (!"".equals(weixinHomePage)&&!"".equals(weixinLifeDynamic)&&!"".equals(weixinLocationDynamic)&&!"".equals(weixinBusinessDynamic)){
                weixinCount = Integer.parseInt(weixinHomePage)+Integer.parseInt(weixinLifeDynamic)+Integer.parseInt(weixinLocationDynamic)+Integer.parseInt(weixinBusinessDynamic);
            }
            if (!"".equals(weiboHomePage)&&!"".equals(weiboLifeDynamic)&&!"".equals(weiboLocationDynamic)&&!"".equals(weiboBusinessDynamic)){
                weiboCount = Integer.parseInt(weiboHomePage)+Integer.parseInt(weiboLifeDynamic)+Integer.parseInt(weiboLocationDynamic)+Integer.parseInt(weiboBusinessDynamic);
            }
            int zhufaAll = Integer.valueOf(homePage)+Integer.valueOf(lifeDynamics)+Integer.valueOf(locationDynamics)+Integer.valueOf(businessDynamics);
            appCount = zhufaAll - qqCount - weixinCount - weiboCount;
            tv_qq_zf.setText(qqCount+"");
            tv_weixin_zf.setText(weixinCount+"");
            tv_weibo_zf.setText(weiboCount+"");
            tv_app_zf.setText(appCount+"");
        }else {
            homePage = "0";
            lifeDynamics = "0";
            locationDynamics = "0";
            businessDynamics = "0";
            tv_none2.setVisibility(View.VISIBLE);
        }
        if (homePage==null||"".equals(homePage)||homePage.equalsIgnoreCase("null")){
            homePage = "0";
        }
        if (Integer.valueOf(homePage)>Integer.valueOf(homePagez)){
            tv2_xinzeng_zhuye.setVisibility(View.VISIBLE);
            tv2_xinzeng_zhuye.setText(String.valueOf(Integer.valueOf(homePage)-Integer.valueOf(homePagez)));
        }else {
            tv2_xinzeng_zhuye.setVisibility(View.INVISIBLE);
        }
        if (lifeDynamics==null||"".equals(lifeDynamics)||lifeDynamics.equalsIgnoreCase("null")){
            lifeDynamics = "0";
        }
        if (Integer.valueOf(lifeDynamics)>Integer.valueOf(lifeDynamicsz)){
            tv2_xinzeng_shenhuo.setVisibility(View.VISIBLE);
            tv2_xinzeng_shenhuo.setText(String.valueOf(Integer.valueOf(lifeDynamics)-Integer.valueOf(lifeDynamicsz)));
        }else {
            tv2_xinzeng_shenhuo.setVisibility(View.INVISIBLE);
        }
        if (locationDynamics==null||"".equals(locationDynamics)||locationDynamics.equalsIgnoreCase("null")){
            locationDynamics = "0";
        }
        if (Integer.valueOf(locationDynamics)>Integer.valueOf(locationDynamicsz)){
            tv2_xinzneg_zuobiao.setVisibility(View.VISIBLE);
            tv2_xinzneg_zuobiao.setText(String.valueOf(Integer.valueOf(locationDynamics)-Integer.valueOf(locationDynamicsz)));
        }else {
            tv2_xinzneg_zuobiao.setVisibility(View.INVISIBLE);
        }
        if (businessDynamics==null||"".equals(businessDynamics)||businessDynamics.equalsIgnoreCase("null")){
            businessDynamics = "0";
        }
        if (Integer.valueOf(businessDynamics)>Integer.valueOf(businessDynamicsz)){
            tv2_xinzneg_shangye.setVisibility(View.VISIBLE);
            tv2_xinzneg_shangye.setText(String.valueOf(Integer.valueOf(businessDynamics)-Integer.valueOf(businessDynamicsz)));
        }else {
            tv2_xinzneg_shangye.setVisibility(View.INVISIBLE);
        }
        if (size==null||"".equals(size)||size.equalsIgnoreCase("null")){
            size = "0";
        }
        tv2_zhuye.setText("个人主页("+homePage+")");
        tv2_shenghuo.setText("派单动态("+lifeDynamics+")");
        tv2_zuobiao.setText("坐标动态("+locationDynamics+")");
        tv2_shangye.setText("商业动态("+businessDynamics+")");
        if (size.equals("0")){
            arcView2.setVisibility(View.INVISIBLE);
            tv_none2.setVisibility(View.VISIBLE);
        }else {
            arcView2.setVisibility(View.VISIBLE);
            tv_none2.setVisibility(View.INVISIBLE);
            if (iszhuFirst) {
                List<Times> times = new ArrayList<>();
                Times t1 = new Times();
                Times t2 = new Times();
                Times t3 = new Times();
                Times t4 = new Times();
                int peopleSum1 = 0, peopleSum2 = 0, peopleSum3 = 0, peopleSum4 = 0;
                peopleSum1 = Integer.valueOf(homePage);
                t1.hour = peopleSum1;
                t1.text = "个人主页";
                times.add(t1);
                peopleSum2 = Integer.valueOf(lifeDynamics);
                t2.hour = peopleSum2;
                t2.text = "派单动态";
                times.add(t2);
                peopleSum3 = Integer.valueOf(locationDynamics);
                t3.hour = peopleSum3;
                t3.text = "坐标动态";
                times.add(t3);
                peopleSum4 = Integer.valueOf(businessDynamics);
                t4.hour = peopleSum4;
                t4.text = "商业动态";
                times.add(t4);
                ArcView.ArcViewAdapter myAdapter = arcView2.new ArcViewAdapter<Times>() {
                    @Override
                    public double getValue(Times times) {
                        return times.hour;
                    }
                };//设置adapter
                myAdapter.setData(times);//设置数据集
                arcView2.setMaxNum(4);//设置可以显示的最大数值 该数值之后的会合并为其他
                iszhuFirst = false;
            }
        }
        int zhufa = Integer.valueOf(homePage)+Integer.valueOf(lifeDynamics)+Integer.valueOf(locationDynamics)+Integer.valueOf(businessDynamics);
        tv_zhufa_count.setText("转 发 "+zhufa+" 次");
        if ("01".equals(biaoshi)) {
            tv2_xinzeng_zhuye.setVisibility(View.INVISIBLE);
            tv2_xinzeng_shenhuo.setVisibility(View.INVISIBLE);
            tv2_xinzneg_zuobiao.setVisibility(View.INVISIBLE);
            tv2_xinzneg_shangye.setVisibility(View.INVISIBLE);
        }
    }

    static class Times {
        double hour;
        String text;
    }
}
