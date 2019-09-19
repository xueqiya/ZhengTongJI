package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.utils.FileStorage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.FWFKInfo;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.alluser.presenter.IFWFKPresenter;
import com.sangu.apptongji.main.alluser.presenter.IOrderDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FWFKPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.OrderDetailPresenter;
import com.sangu.apptongji.main.alluser.view.IFWFKView;
import com.sangu.apptongji.main.alluser.view.IOrderDetailView;
import com.sangu.apptongji.main.qiye.QiYeYuGoActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2016-11-28.
 */

public class UTPaiDanDeActivity extends BaseActivity implements IOrderDetailView ,IFWFKView{
    private IOrderDetailPresenter orderDetailPresenter=null;
    private IFWFKPresenter presenter=null;
    private ImageView ivBack=null,iv_jiantou=null;
    private Button btnCommit2=null;
    private TextView tv_count_fankui=null;
    private RelativeLayout rl_btn1=null;
    private LinearLayout ll_btn=null,ll;
    private String orderState,pass,image1,image2,image3,image4,time1,time2,time3,time4,orderTime,biaoshi,biaoshi1,totalAmt;
    private TextView etBody1=null,etBody2=null,etBody3=null,etBody4=null,etBody5=null,etCount1=null,etCount2=null,etCount3=null,
            etCount4=null,etCount5=null,etDanjia1=null,etDanjia2=null,etDanjia3=null,etDanjia4=null,etDanjia5=null,etAllPrice=null;
    private TextView tv_time1=null,tv_time2=null,tv_time3=null,tv_time4=null,tv_paidan_qiye=null,tv_paidan_qiyeren=null,
            tv_paidan_caiwu=null,tv_paidan_kehu=null,tev_paidan_qiye=null,tv_fenxiang=null,tev_paidan_qiyeren=null,tev_paidan_caiwu=null,tev_paidan_kehu=null;
    private EditText etUBeizhu=null;
    private Button btnCommit=null,btnCansul=null;
    private LinearLayout ll1_paidan=null,ll2_paidan=null,ll2=null;
    private String orderId,merId,orderProject,orderNumber,orderAmt,orderSum,oImage,mImage,oSignature,mSignature,beizhu,yueding,resv2,resv4;
    private ImageView iv_paidan_qiye=null,iv_paidan_qiyeren=null,iv_paidan_caiwu=null,iv_paidan_kehu=null;
    private RelativeLayout llM_beizhu=null;
    private RelativeLayout countDown=null,rl_qianming=null,rl_ticheng=null;
    String hours = "00",mins="00",second="00";
    private TextView hoursTv=null, minutesTv=null,secondsTv=null,tv2=null,tv_all=null,tv_qiye_ticheng=null;
    private long mHour;
    private long mMin;
    private long mSecond;// 天 ,小时,分钟,秒
    private Double yuE;
    private String companyId,resv5Geren="",resv5Qiye="",resv5,resv5Qiyeb;
    private String oP1,oP2,oP3,oP4,oP5,oN1,oN2,oN3,oN4,oN5,oA1,oA2,oA3,oA4,oA5;
    private boolean isRun = true;
    private boolean isJian = true;
    private Handler timeHandler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                tv2.setText("距离约定时间");
                computeTime();
                if ((mHour==0&&mMin==0&&mSecond==0)||(mHour==00&&mMin==00&&mSecond==00)) {
                    isJian = false;
                }
            }else if (msg.what==2){
                tv2.setText("超出约定时间");
                addTime();
            }
            if (mHour<10){
                hours = "0"+mHour;
            }else {
                hours = String.valueOf(mHour);
            }
            if (mMin<10){
                mins = "0"+mMin;
            }else {
                mins = String.valueOf(mMin);
            }
            if (mSecond<10){
                second = "0"+mSecond ;
            }else {
                second = String.valueOf(mSecond);
            }
            hoursTv.setText(hours);
            minutesTv.setText(mins);
            secondsTv.setText(second);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 开启倒计时
     */
    private void startRun() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000ms
                        Message message = Message.obtain();
                        if (isJian){
                            message.what = 1;
                        }else {
                            message.what = 2;
                        }
                        timeHandler1.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void addTime() {
        mSecond++;
        if (mSecond>59){
            mMin++;
            mSecond = 0;
            if (mMin>59){
                mMin = 0;
                mHour++;
            }
        }
    }
    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
            }
        }
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_order_detial2);
        orderDetailPresenter = new OrderDetailPresenter(this,this);
        presenter = new FWFKPresenter(this,this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        llM_beizhu = (RelativeLayout) findViewById(R.id.llM_beizhu);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll1_paidan = (LinearLayout) findViewById(R.id.ll1_paidan);
        ll2_paidan = (LinearLayout) findViewById(R.id.ll2_paidan);
        countDown = (RelativeLayout) findViewById(R.id.countDown);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        iv_jiantou = (ImageView) findViewById(R.id.iv_jiantou);
        iv_paidan_qiye = (ImageView) findViewById(R.id.iv_paidan_qiye);
        iv_paidan_qiyeren = (ImageView) findViewById(R.id.iv_paidan_qiyeren);
        iv_paidan_caiwu = (ImageView) findViewById(R.id.iv_paidan_caiwu);
        iv_paidan_kehu = (ImageView) findViewById(R.id.iv_paidan_kehu);
        tev_paidan_qiye = (TextView) findViewById(R.id.tev_paidan_qiye);
        tev_paidan_qiyeren = (TextView) findViewById(R.id.tev_paidan_qiyeren);
        tev_paidan_caiwu = (TextView) findViewById(R.id.tev_paidan_caiwu);
        tev_paidan_kehu = (TextView) findViewById(R.id.tev_paidan_kehu);
        tv_fenxiang = (TextView) findViewById(R.id.tv_fenxiang);
        iv_jiantou.setVisibility(View.VISIBLE);
        ll1_paidan.setVisibility(View.VISIBLE);
        ll2_paidan.setVisibility(View.VISIBLE);
        rl_qianming = (RelativeLayout) findViewById(R.id.rl_qianming);
        rl_ticheng = (RelativeLayout) findViewById(R.id.rl_ticheng);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_jiantou.getLayoutParams();
        rl_qianming.removeView(iv_jiantou);
        rl_qianming.addView(iv_jiantou,params);
        ll2.setVisibility(View.VISIBLE);
        yuE = DemoApplication.getApp().getCurrenPrice();
        Intent intent = this.getIntent();
        companyId = intent.getStringExtra("companyId");
        orderId = intent.getStringExtra("orderId");
        orderState = intent.getStringExtra("orderState");
        merId = intent.getStringExtra("merId");
        pass = intent.getStringExtra("pypass");
        orderTime = intent.getStringExtra("orderTime");
        totalAmt = intent.hasExtra("totalAmt")?intent.getStringExtra("totalAmt"):"";
        biaoshi = intent.hasExtra("biaoshi")?intent.getStringExtra("biaoshi"):"";
        biaoshi1 = intent.hasExtra("biaoshi1")?intent.getStringExtra("biaoshi1"):"00";
        initView();
        orderDetailPresenter.loadOrderDetail(orderId);
    }
    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    private void initView3() {
        llM_beizhu.setVisibility(View.GONE);
        etUBeizhu.setEnabled(false);
        btnCommit.setVisibility(View.GONE);
        tv_paidan_caiwu.setEnabled(false);
        tv_paidan_qiye.setEnabled(false);
        if (image2.equals("")&&image4.equals("")&&orderState.equals("04")) {
            llM_beizhu.setVisibility(View.VISIBLE);
            tv_paidan_caiwu.setEnabled(true);
            tv_paidan_caiwu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!image1.equals("")||!image3.equals("")) {
                        Intent intent = new Intent(UTPaiDanDeActivity.this, QianMingActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("merId", merId);
                        intent.putExtra("balance", orderSum);
                        intent.putExtra("biaoshi", "1101");
                        startActivity(intent);
//                        finish();
                    }else {
                        Toast.makeText(UTPaiDanDeActivity.this,"等待客户签字！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (image1.equals("")&&image3.equals("")) {
                llM_beizhu.setVisibility(View.VISIBLE);
                btnCommit.setText("等待客户签字");
                btnCommit.setEnabled(false);
                btnCommit.setVisibility(View.VISIBLE);
                btnCansul.setVisibility(View.VISIBLE);
                btnCansul.setText("申请退款");
                etUBeizhu.setEnabled(true);
                btnCansul.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etUBeizhu.setFocusable(true);
                        etUBeizhu.setFocusableInTouchMode(true);
                        etUBeizhu.requestFocus();
                        final String beizhu = etUBeizhu.getText().toString().trim();
                        if (!TextUtils.isEmpty(beizhu)) {
                            Intent intent = new Intent(UTPaiDanDeActivity.this,QianMingActivity.class);
                            intent.putExtra("orderId",orderId);
                            intent.putExtra("remark", beizhu);
                            intent.putExtra("biaoshi","13");
                            startActivity(intent);
                        } else {
                            Toast.makeText(UTPaiDanDeActivity.this, "请写明退款原因", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }else {
                llM_beizhu.setVisibility(View.GONE);
                btnCommit.setText("财务签字");
                btnCommit.setEnabled(true);
                btnCommit.setVisibility(View.VISIBLE);
                btnCansul.setVisibility(View.GONE);
                btnCommit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UTPaiDanDeActivity.this, QianMingActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("merId", merId);
                        intent.putExtra("balance", orderSum);
                        intent.putExtra("biaoshi", "1101");
                        startActivity(intent);
//                        finish();
                    }
                });
            }
        }else if ((!image2.equals("")||!image4.equals(""))&&orderState.equals("04")){
            llM_beizhu.setVisibility(View.GONE);
            tv_paidan_qiye.setEnabled(true);
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setText("确认签收");
            btnCommit.setEnabled(true);
            btnCansul.setVisibility(View.GONE);
            if (resv5.length()>0||companyId.length()>0){
                tv_paidan_qiye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UTPaiDanDeActivity.this, QianMingActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("merId", merId);
                        intent.putExtra("balance", orderSum);
                        intent.putExtra("companyId", companyId);
                        intent.putExtra("companyAmt",resv5Qiye);
                        intent.putExtra("balance1", resv5Geren);
                        intent.putExtra("biaoshi", "11");
                        startActivity(intent);
                        finish();
                    }
                });
                btnCommit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UTPaiDanDeActivity.this, QianMingActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("merId", merId);
                        intent.putExtra("balance", orderSum);
                        intent.putExtra("companyId", companyId);
                        intent.putExtra("companyAmt",resv5Qiye);
                        intent.putExtra("balance1", resv5Geren);
                        intent.putExtra("biaoshi", "11");
                        startActivity(intent);
                        finish();
                    }
                });
            }else {
                tv_paidan_qiye.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UTPaiDanDeActivity.this, QianMingActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("merId", merId);
                        intent.putExtra("balance", orderSum);
                        intent.putExtra("biaoshi", "11");
                        startActivity(intent);
                        finish();
                    }
                });
                btnCommit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UTPaiDanDeActivity.this, QianMingActivity.class);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("merId", merId);
                        intent.putExtra("balance", orderSum);
                        intent.putExtra("biaoshi", "11");
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
    }

    private void initView2() {
        btnCansul.setVisibility(View.VISIBLE);
        btnCommit.setVisibility(View.VISIBLE);
        btnCansul.setEnabled(false);
        btnCommit.setEnabled(true);
        btnCansul.setText("等待对方提交");
        btnCansul.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        btnCommit.setText("申请退款");
        etUBeizhu.setEnabled(true);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUBeizhu.setFocusable(true);
                etUBeizhu.setFocusableInTouchMode(true);
                etUBeizhu.requestFocus();
                final String beizhu = etUBeizhu.getText().toString().trim();
                if (!TextUtils.isEmpty(beizhu)) {
                    Intent intent = new Intent(UTPaiDanDeActivity.this,QianMingActivity.class);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("remark", beizhu);
                    intent.putExtra("biaoshi","13");
                    startActivity(intent);
                } else {
                    Toast.makeText(UTPaiDanDeActivity.this, "请写明退款原因", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    @Override
    public void updataFuWuFanKuiList(List<FWFKInfo> fuWuFKinfo) {
        int count = fuWuFKinfo.size();
        tv_count_fankui.setText("["+count+"]");
        rl_btn1.setVisibility(View.VISIBLE);
        btnCommit2.setVisibility(View.VISIBLE);
        btnCommit2.setText("售后反馈");
        btnCommit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderState.equals("11")) {
                    new AlertDialog.Builder(UTPaiDanDeActivity.this)
                            .setTitle("确认")
                            .setMessage("确认申请售后吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    shenqingshouhou();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }else if (orderState.equals("10")){
                    startActivity(new Intent(UTPaiDanDeActivity.this, FWFKListActivity.class)
                            .putExtra("biaoshi", "00").putExtra("orderId", orderId).putExtra("resv5",resv5).putExtra("companyId",companyId));
                }
            }
        });
        tv_count_fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UTPaiDanDeActivity.this, FWFKListActivity.class)
                        .putExtra("biaoshi", "02").putExtra("orderId", orderId).putExtra("resv5",resv5).putExtra("companyId",companyId));
            }
        });
    }
    private void initView6() {
        llM_beizhu.setVisibility(View.GONE);
        etUBeizhu.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        ll_btn.setVisibility(View.INVISIBLE);
        rl_btn1.setVisibility(View.VISIBLE);
        btnCommit2.setVisibility(View.VISIBLE);
        tv_fenxiang.setVisibility(View.INVISIBLE);
        tv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentImage();
            }
        });
    }
    private void initView7() {
        rl_ticheng.setVisibility(View.VISIBLE);
        tv_qiye_ticheng.setText(resv5Qiyeb+"%");
        etBody1.setEnabled(false);
        etBody2.setEnabled(false);
        etBody3.setEnabled(false);
        etBody4.setEnabled(false);
        etBody5.setEnabled(false);
        hoursTv.setEnabled(false);
        tv2.setEnabled(false);
        tv_all.setEnabled(false);
        tv_fenxiang.setEnabled(false);
        minutesTv.setEnabled(false);
        secondsTv.setEnabled(false);
        etCount1.setEnabled(false);
        etCount2.setEnabled(false);
        etCount3.setEnabled(false);
        etCount4.setEnabled(false);
        etCount5.setEnabled(false);
        etDanjia1.setEnabled(false);
        etDanjia2.setEnabled(false);
        etDanjia3.setEnabled(false);
        etDanjia4.setEnabled(false);
        etDanjia5.setEnabled(false);
        etAllPrice.setEnabled(false);
        etUBeizhu.setEnabled(false);
        btnCommit.setEnabled(false);
        btnCansul.setEnabled(false);
        llM_beizhu.setEnabled(false);
        countDown.setEnabled(false);
        tv_paidan_qiyeren.setEnabled(false);
        tv_paidan_qiye.setEnabled(false);
        tv_paidan_kehu.setEnabled(false);
        tv_paidan_caiwu.setEnabled(false);
        iv_paidan_qiye.setEnabled(false);
        iv_paidan_qiyeren.setEnabled(false);
        iv_paidan_caiwu.setEnabled(false);
        iv_paidan_kehu.setEnabled(false);
    }
    private void initView1() {
        llM_beizhu.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
        if (!oImage.equals("")||!oSignature.equals("")){
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufive);
            if (resv4!=null&&!"".equals(resv4)&&!"NULL".equals(resv4)&&!orderState.equals("07")) {
                tev_paidan_qiye.setVisibility(View.VISIBLE);
                tev_paidan_qiye.setText("完成:");
                tv_time1.setText(resv4);
            }else {
                tev_paidan_qiye.setVisibility(View.INVISIBLE);
                tv_time1.setVisibility(View.INVISIBLE);
            }
        }
        tv_fenxiang.setVisibility(View.INVISIBLE);
        tv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentImage();
            }
        });
        if (orderState.equals("05")) {
            llM_beizhu.setVisibility(View.GONE);
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setText("申请售后");
            btnCommit.setEnabled(true);
            btnCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(UTPaiDanDeActivity.this)
                            .setTitle("确认")
                            .setMessage("确认申请售后吗？")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    shenqingshouhou();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }
        if ("07".equals(orderState)){
            llM_beizhu.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setText("退款成功");
            btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            btnCommit.setEnabled(false);
        }
    }
    private void shenqingshouhou() {
        String url = FXConstant.URL_SHOUHOU_UPDATE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(UTPaiDanDeActivity.this,"申请成功,等待商家回复！",Toast.LENGTH_SHORT).show();
                if (resv5.length()>0||companyId.length()>0){
                    updateQjcount();
                }else {
                    updateUscount(merId);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UTPaiDanDeActivity.this,"网络连接错误！",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderId",orderId);
                param.put("state","10");
                return param;
            }
        };
        MySingleton.getInstance(UTPaiDanDeActivity.this).addToRequestQueue(request);
    }
    private void updateUscount(final String hxid1) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderUnReadCount","1");
                param.put("userId",hxid1);
                return param;
            }
        };
        MySingleton.getInstance(UTPaiDanDeActivity.this).addToRequestQueue(request);
    }

    private void updateQjcount() {
        String url = FXConstant.URL_UPDATE_UNREADQIYE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("orderCount","1");
                param.put("companyId",companyId);
                return param;
            }
        };
        MySingleton.getInstance(UTPaiDanDeActivity.this).addToRequestQueue(request);
    }
    private void saveCurrentImage()
    {
        //获取当前屏幕的大小
//        int width = getWindow().getDecorView().getRootView().getWidth();
//        int height = getWindow().getDecorView().getRootView().getHeight();
//        //生成相同大小的图片
//        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888 );
//        //找到当前页面的跟布局
//        View view =  getWindow().getDecorView().getRootView();
//        //设置缓存
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache();
//        //从缓存中获取当前屏幕的图片
//        temBitmap = view.getDrawingCache();
//        //输出到sd卡
//        saveImg(temBitmap);
//        ScreenshotUtil.getBitmapByView(UTPaiDanDeActivity.this, ll, "订单", null,false);
        fenxiang();
    }

    private void saveImg(Bitmap mBitmap)  {
        File file = new FileStorage("fenxiang").createCropFile("dingdanCut.png",null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(UTPaiDanDeActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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
            fenxiang();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void fenxiang() {
        OnekeyShare oks = new OnekeyShare();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode(true);
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                } else if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                    paramsToShare.setShareType(Platform.SHARE_IMAGE);
                    paramsToShare.setTitle("正事多app");
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                } else if (platform.getName().equalsIgnoreCase(QZone.NAME)) {
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                }else if (platform.getName().equalsIgnoreCase(WechatMoments.NAME)){
                    paramsToShare.setShareType(Platform.SHARE_IMAGE);
                    paramsToShare.setTitle("正事多app");
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                }
            }
        });
        oks.show(this);
    }

    private void initView() {
        tv_count_fankui = (TextView) findViewById(R.id.tv_count_fankui);
        btnCommit2 = (Button) findViewById(R.id.btn_comit2);
        ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
        ll = (LinearLayout) findViewById(R.id.ll);
        rl_btn1 = (RelativeLayout) findViewById(R.id.rl_btn1);
        tv_paidan_qiyeren = (TextView) findViewById(R.id.tv_paidan_qiyeren);
        tv_paidan_qiye = (TextView) findViewById(R.id.tv_paidan_qiye);
        tv_paidan_kehu = (TextView) findViewById(R.id.tv_paidan_kehu);
        tv_paidan_caiwu = (TextView) findViewById(R.id.tv_paidan_caiwu);
        tv_time1 = (TextView) findViewById(R.id.tv_paidan_qiye_time);
        tv_time2 = (TextView) findViewById(R.id.tv_paidan_qiyeren_time);
        tv_time3 = (TextView) findViewById(R.id.tv_paidan_kehu_time);
        tv_time4 = (TextView) findViewById(R.id.tv_paidan_caiwu_time);
        etBody1 = (TextView) findViewById(R.id.et_body1);
        etBody2 = (TextView) findViewById(R.id.et_body2);
        etBody3 = (TextView) findViewById(R.id.et_body3);
        etBody4 = (TextView) findViewById(R.id.et_body4);
        etBody5 = (TextView) findViewById(R.id.et_body5);
        hoursTv = (TextView) findViewById(R.id.tv_hours);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv_all = (TextView) findViewById(R.id.tv_all);
        tv_qiye_ticheng = (TextView) findViewById(R.id.tv_qiye_ticheng);
        minutesTv = (TextView) findViewById(R.id.tv_mins);
        secondsTv = (TextView) findViewById(R.id.tv_mills);
        etCount1 = (TextView) findViewById(R.id.et_count1);
        etCount2 = (TextView) findViewById(R.id.et_count2);
        etCount3 = (TextView) findViewById(R.id.et_count3);
        etCount4 = (TextView) findViewById(R.id.et_count4);
        etCount5 = (TextView) findViewById(R.id.et_count5);
        etDanjia1 = (TextView) findViewById(R.id.et_danjia1);
        etDanjia2 = (TextView) findViewById(R.id.et_danjia2);
        etDanjia3 = (TextView) findViewById(R.id.et_danjia3);
        etDanjia4 = (TextView) findViewById(R.id.et_danjia4);
        etDanjia5 = (TextView) findViewById(R.id.et_danjia5);
        etAllPrice = (TextView) findViewById(R.id.et_all);
        etUBeizhu = (EditText) findViewById(R.id.et_Ubeizhu);
        btnCommit = (Button) findViewById(R.id.btn_comit);
        btnCansul = (Button) findViewById(R.id.btn_cansul);
        btnCansul.setEnabled(false);
        btnCommit.setEnabled(false);
        ll.setFocusable(true);
        ll.setFocusableInTouchMode(true);
        ll.requestFocus();
    }
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    @Override
    public void updateCurrentOrderDetail(OrderDetail orderDetail) {
        orderProject = TextUtils.isEmpty(orderDetail.getOrderProject())?"":orderDetail.getOrderProject();
        orderNumber = TextUtils.isEmpty(orderDetail.getOrderNumber())?"":orderDetail.getOrderNumber();
        orderAmt = TextUtils.isEmpty(orderDetail.getOrderAmt())?"":orderDetail.getOrderAmt();
        orderSum = TextUtils.isEmpty(orderDetail.getOrderSum())?"":orderDetail.getOrderSum();
        image1 = TextUtils.isEmpty(orderDetail.getImage1())?"":orderDetail.getImage1();
        image2 = TextUtils.isEmpty(orderDetail.getImage2())?"":orderDetail.getImage2();
        image3 = TextUtils.isEmpty(orderDetail.getImage3())?"":orderDetail.getImage3();
        image4 = TextUtils.isEmpty(orderDetail.getImage2())?"":orderDetail.getImage4();
        time1 = TextUtils.isEmpty(orderDetail.getTime1())?"":orderDetail.getTime1();
        time2 = TextUtils.isEmpty(orderDetail.getTime2())?"":orderDetail.getTime2();
        time3 = TextUtils.isEmpty(orderDetail.getTime3())?"":orderDetail.getTime3();
        time4 = TextUtils.isEmpty(orderDetail.getTime4())?"":orderDetail.getTime4();
        oSignature = TextUtils.isEmpty(orderDetail.getoSignature())?"":orderDetail.getoSignature();
        mSignature = TextUtils.isEmpty(orderDetail.getmSignature())?"":orderDetail.getmSignature();
        oImage = TextUtils.isEmpty(orderDetail.getoImage())?"":orderDetail.getoImage();
        mImage = TextUtils.isEmpty(orderDetail.getmImage())?"":orderDetail.getmImage();
        beizhu = TextUtils.isEmpty(orderDetail.getRemark())?"":orderDetail.getRemark();
        yueding = TextUtils.isEmpty(orderDetail.getResv1())?"":orderDetail.getResv1();
        resv2 = TextUtils.isEmpty(orderDetail.getResv2())?"":orderDetail.getResv2();
        resv4 = TextUtils.isEmpty(orderDetail.getResv4())?"":orderDetail.getResv4();
        resv5 = TextUtils.isEmpty(orderDetail.getResv5())?"":orderDetail.getResv5();
        if (resv5.length()>0){
            resv5Geren = resv5.split("\\|")[0];
            resv5Qiye = resv5.split("\\|")[1];
            resv5Qiyeb = Double.parseDouble(resv5Qiye)/(Double.parseDouble(resv5Qiye)+Double.parseDouble(resv5Geren))+"";
        }
        if (!resv2.equals("")){
            String[] res = resv2.split(",");
            isRun = false;
            countDown.setVisibility(View.GONE);
            tv_all.setVisibility(View.VISIBLE);
            tv_all.setText(res[1]);
            if (res[0].equals("0")){
                tv2.setText("距离约定时间");
            }else if (res[0].equals("1")){
                tv2.setText("超出约定时间");
            }
        }else {
            if (!yueding.equals("")) {
                String time1 = getNowTime();
                long year = Long.parseLong(yueding.substring(0, 4));
                long year1 = Long.parseLong(time1.substring(0, 4));
                long mou = Long.parseLong(yueding.substring(4, 6));
                long mou1 = Long.parseLong(time1.substring(4, 6));
                long day = Long.parseLong(yueding.substring(6, 8));
                long day1 = Long.parseLong(time1.substring(6, 8));
                long hour = Long.parseLong(yueding.substring(8, 10));
                long hour1 = Long.parseLong(time1.substring(8, 10));
                long min = Long.parseLong(yueding.substring(10, 12));
                long min1 = Long.parseLong(time1.substring(10, 12));
                mSecond = Long.parseLong(time1.substring(12, 14));
                long minAll = (year - year1) * 365 * 24 * 60 + (mou - mou1) * 30 * 24 * 60 + (day - day1) * 24 * 60 + (hour - hour1) * 60 + (min - min1);
                if (minAll < 0) {
                    isJian = false;
                    minAll = -minAll;
                    tv2.setText("超出约定时间");
                } else {
                    tv2.setText("距离约定时间");
                }
                mHour = minAll / 60;
                mMin = minAll - mHour * 60;
                if (mHour < 10) {
                    hours = "0" + mHour;
                } else {
                    hours = String.valueOf(mHour);
                }
                if (mMin < 10) {
                    mins = "0" + mMin;
                } else {
                    mins = String.valueOf(mMin);
                }
                if (mSecond < 10) {
                    second = "0" + mSecond;
                } else {
                    second = String.valueOf(mSecond);
                }
                hoursTv.setText(hours);
                minutesTv.setText(mins);
                secondsTv.setText(second);
                startRun();
            } else {
                isRun = false;
                countDown.setVisibility(View.GONE);
                tv_all.setVisibility(View.VISIBLE);
                tv_all.setText("未设置");
            }
        }
        if (time1==null||"".equals(time1)){
            tev_paidan_qiyeren.setVisibility(View.INVISIBLE);
        }else {
            tev_paidan_qiyeren.setVisibility(View.VISIBLE);
        }
        if (time2==null||"".equals(time2)){
            tev_paidan_kehu.setVisibility(View.INVISIBLE);
        }else {
            tev_paidan_kehu.setVisibility(View.VISIBLE);
        }
        if (time3==null||"".equals(time3)){
            tev_paidan_caiwu.setVisibility(View.INVISIBLE);
        }else {
            tev_paidan_caiwu.setVisibility(View.VISIBLE);
        }
        if (orderTime==null||"".equals(orderTime)){
            tev_paidan_qiye.setVisibility(View.INVISIBLE);
        }else {
            tev_paidan_qiye.setVisibility(View.VISIBLE);
        }
        tv_time2.setText(time1);
        tv_time3.setText(time2);
        tv_time4.setText(time3);
        tv_time1.setText(orderTime);
        if (!image3.equals("")){
            tv_paidan_kehu.setText("客户已确认");
        }
        if (!image1.equals("")){
            tv_paidan_kehu.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_PAIDANQIANZI + image1,iv_paidan_kehu);
        }
        if (!image4.equals("")){
            tv_paidan_caiwu.setText("财务已确认");
        }
        if (!image2.equals("")){
            tv_paidan_caiwu.setVisibility(View.INVISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_PAIDANQIANZI + image2,iv_paidan_caiwu);
        }
        if (!oSignature.equals("")){
            tv_paidan_qiye.setVisibility(View.VISIBLE);
            tv_paidan_qiye.setText("企业已确认");
        }
        if (!oImage.equals("")){
            tev_paidan_qiye.setText("完成");
            tv_paidan_qiye.setVisibility(View.INVISIBLE);
            String[] avatar = oImage.split("\\|");
            oImage = avatar[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN + oImage,iv_paidan_qiye);
        }
        if (!mSignature.equals("")){
            tv_paidan_qiyeren.setText("接单人已确认");
        }
        if (!mImage.equals("")){
            tv_paidan_qiyeren.setVisibility(View.INVISIBLE);
            String[] avatar = mImage.split("\\|");
            mImage = avatar[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN + mImage,iv_paidan_qiyeren);
        }
        int maxSplit = 6;
        String[] orderProjectArray = orderProject.split(",", maxSplit);
        oP1 = orderProjectArray[0];
        oP2 = orderProjectArray[1];
        oP3 = orderProjectArray[2];
        oP4 = orderProjectArray[3];
        oP5 = orderProjectArray[4];
        String[] orderNumberArray = orderNumber.split(",",maxSplit);
        oN1 = orderNumberArray[0];
        oN2 = orderNumberArray[1];
        oN3 = orderNumberArray[2];
        oN4 = orderNumberArray[3];
        oN5 = orderNumberArray[4];
        String[] orderAmtArray = orderAmt.split(",",maxSplit);
        oA1 = orderAmtArray[0];
        oA2 = orderAmtArray[1];
        oA3 = orderAmtArray[2];
        oA4 = orderAmtArray[3];
        oA5 = orderAmtArray[4];
        etBody1.setText(oP1);
        etBody2.setText(oP2);
        etBody3.setText(oP3);
        etBody4.setText(oP4);
        etBody5.setText(oP5);
        etCount1.setText(oN1);
        etCount2.setText(oN2);
        etCount3.setText(oN3);
        etCount4.setText(oN4);
        etCount5.setText(oN5);
        etDanjia1.setText(oA1);
        etDanjia2.setText(oA2);
        etDanjia3.setText(oA3);
        etDanjia4.setText(oA4);
        etDanjia5.setText(oA5);
        if (orderSum!=null&&!"".equals(orderSum)) {
            double jine = Double.parseDouble(orderSum);
            final String str = String.format("%.2f", jine);
            etAllPrice.setText(str + "元");
        }else {
            etAllPrice.setText("0.00元");
        }
        etUBeizhu.setText(beizhu);
        if ((mImage==null&&mSignature==null)||(mImage.equals("")&&mSignature.equals(""))||(mImage.equals("NULL")&&mSignature.equals("NULL"))){
            iv_jiantou.setImageResource(R.drawable.fuzejiantouone);
        }else if ((image1==null&&image3==null)||(image1.equals("")&&image3.equals(""))||(image1.equals("NULL")&&image3.equals("NULL"))){
            iv_jiantou.setImageResource(R.drawable.fuzejiantoutwo);
        }else if ((image2==null&&image4==null)||(image2.equals("")&&image4.equals(""))||(image2.equals("NULL")&&image4.equals("NULL"))){
            iv_jiantou.setImageResource(R.drawable.fuzejiantouthree);
        }else if ((oImage==null&&oSignature==null)||(oImage.equals("")&&oSignature.equals(""))||(oImage.equals("NULL")&&oSignature.equals("NULL"))){
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufour);
        }else {
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufive);
        }
        if (orderState.equals("03")){
            initView2();
        }else if (orderState.equals("05")||orderState.equals("07")){
            initView1();
        }else if(orderState.equals("04")){
            initView3();
        }else if (orderState.equals("06")){
            initView4();
        }else if (orderState.equals("08")||orderState.equals("09")){
            initView5();
        }else if (orderState.equals("10")||orderState.equals("11")){
            presenter.loadFWFKList(orderId);
            initView6();
        }
        if ("01".equals(biaoshi1)){
            tv_fenxiang.setVisibility(View.VISIBLE);
            tv_fenxiang.setEnabled(true);
            tv_fenxiang.setText("派单");
            tv_fenxiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UTPaiDanDeActivity.this, QiYeYuGoActivity.class).putExtra("companyId",companyId)
                            .putExtra("orderId",orderId).putExtra("totalAmt",totalAmt));
                }
            });
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orderDetailPresenter.loadOrderDetail(orderId);
    }
    private void initView4() {
        etUBeizhu.setEnabled(false);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
    }

    private void initView5() {
        etUBeizhu.setEnabled(false);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setText("申请平台介入");
        btnCommit.setEnabled(true);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(UTPaiDanDeActivity.this);
                builder.setMessage("确认申请平台介入吗?");
                builder.setTitle("确认");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String url = FXConstant.URL_Update_OrderState;
                        StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                try {
                                    JSONObject object = new JSONObject(s);
                                    String code = object.getString("code");
                                    if (code.equals("success")) {
                                        dialog.dismiss();
                                        Toast.makeText(UTPaiDanDeActivity.this, "申请平台介入成功！", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(UTPaiDanDeActivity.this, "申请失败！请重试...", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                dialog.dismiss();
                                Toast.makeText(UTPaiDanDeActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params3 = new HashMap<String, String>();
                                params3.put("orderId", orderId);
                                params3.put("orderState", "09");
                                params3.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                                return params3;
                            }
                        };
                        MySingleton.getInstance(UTPaiDanDeActivity.this).addToRequestQueue(request1);
                    }
                });
                builder.show();
            }
        });
    }
}
