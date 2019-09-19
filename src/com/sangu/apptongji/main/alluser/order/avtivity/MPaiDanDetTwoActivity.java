package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.fanxin.easeui.utils.FileStorage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.alluser.presenter.IOrderDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.OrderDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IOrderDetailView;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.qiye.QiYeYuGoActivity;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by user on 2016/9/1.
 */
public class MPaiDanDetTwoActivity extends BaseActivity implements IUAZView,IOrderDetailView {
    private IOrderDetailPresenter orderDetailPresenter;
    private IUAZPresenter uazPresenter;
    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_titl;
    private TextView tv_nianling;
    private TextView tv_company_count;
    private TextView tv_company;
    private TextView tv_distance;
    private TextView tv_project_one;
    private TextView tv_project_two;
    private TextView tv_project_three;
    private TextView tv_project_four;
    private TextView tv_zy1_bao;
    private TextView tv_zy2_bao;
    private TextView tv_zy3_bao;
    private TextView tv_zy4_bao;
    private TextView tv_qianming;
    private TextView iv_zy1_tupian;
    private TextView iv_zy2_tupian;
    private TextView iv_zy3_tupian;
    private TextView iv_zy4_tupian;
    private ImageView iv_sex;
    LinearLayout ll_one,ll_two,ll_three,ll_four;
    private ImageView iv_jiantou;
    private String orderState,orderBody,name,head,image1,image2,image3,image4,time1,time2,time3,time4,orderTime;
    private LinearLayout ll_btn,ll;
    private RelativeLayout rl_qianming;
    private EditText et_your_project,et_qianshou_dizhi,et_dingdan_bianhao,et_zhifu_jine,etUBeizhu;
    private TextView tv_saomiao,tv_zhifuxianshi,tv_fenxiang;
    private TextView tv_time1,tv_time2,tv_time3,tv_time4,tv_paidan_qiye,tv_paidan_qiyeren,tv_paidan_caiwu,tv_paidan_kehu,
            tev_paidan_qiye,tev_paidan_qiyeren=null,tev_paidan_caiwu=null,tev_paidan_kehu=null;
    private Button btnCommit,btnCansul;
    private String orderId,userId,orderProject,orderNumber,orderAmt,orderSum,oSignature,mSignature,oImage,mImage,zongjia,beizhu,yueding,nowTime,resv4,resv2;
    private LinearLayout ll1_paidan,ll2_paidan,ll2;
    RelativeLayout llM_beizhu;
    private ImageView iv_paidan_qiye,iv_paidan_qiyeren,iv_paidan_caiwu,iv_paidan_kehu;
    private CheckBox cb_fapiao;
    private String biaoshi,biaoshi1,totalAmt,companyId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morder_moshier);
        orderDetailPresenter = new OrderDetailPresenter(this,this);
        uazPresenter = new UAZPresenter(this,this);
        initView();
        llM_beizhu = (RelativeLayout) findViewById(R.id.llM_beizhu);
//        countDown = (RelativeLayout) findViewById(R.id.countDown);
        ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
        ll = (LinearLayout) findViewById(R.id.ll);
        iv_jiantou = (ImageView) findViewById(R.id.iv_jiantou);
        iv_paidan_qiye = (ImageView) findViewById(R.id.iv_paidan_qiye);
        iv_paidan_qiyeren = (ImageView) findViewById(R.id.iv_paidan_qiyeren);
        iv_paidan_caiwu = (ImageView) findViewById(R.id.iv_paidan_caiwu);
        iv_paidan_kehu = (ImageView) findViewById(R.id.iv_paidan_kehu);
        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        ll_two = (LinearLayout) findViewById(R.id.ll_two);
        ll_three = (LinearLayout) findViewById(R.id.ll_three);
        ll_four = (LinearLayout) findViewById(R.id.ll_four);
        ll.setFocusable(true);
        ll.setFocusableInTouchMode(true);
        ll.requestFocus();
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll1_paidan = (LinearLayout) findViewById(R.id.ll1_paidan);
        ll2_paidan = (LinearLayout) findViewById(R.id.ll2_paidan);
        ll2.setVisibility(View.VISIBLE);
        iv_jiantou.setVisibility(View.VISIBLE);
        ll1_paidan.setVisibility(View.VISIBLE);
        ll2_paidan.setVisibility(View.VISIBLE);
        rl_qianming = (RelativeLayout) findViewById(R.id.rl_qianming);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_jiantou.getLayoutParams();
        rl_qianming.removeView(iv_jiantou);
        rl_qianming.addView(iv_jiantou,params);
        Intent intent = this.getIntent();
        orderId = intent.getStringExtra("orderId");
        userId = intent.getStringExtra("userId");
        orderState = intent.getStringExtra("orderState");
        head = intent.getStringExtra("head");
        name = intent.getStringExtra("name");
        companyId = intent.getStringExtra("companyId");
        biaoshi = intent.hasExtra("biaoshi")?intent.getStringExtra("biaoshi"):"";
        totalAmt = intent.hasExtra("totalAmt")?intent.getStringExtra("totalAmt"):"";
        biaoshi1 = intent.hasExtra("biaoshi1")?intent.getStringExtra("biaoshi1"):"00";
        orderBody = intent.getStringExtra("orderBody");
        orderTime = intent.getStringExtra("orderTime");
        Log.e("mpaidantwo,state",orderState);
        uazPresenter.loadThisDetail(userId);
        orderDetailPresenter.loadOrderDetail(orderId);
    }
    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orderDetailPresenter.loadOrderDetail(orderId);
    }

    private void initView() {
        iv_head = (ImageView) findViewById(R.id.iv_head);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        tv_project_one = (TextView) findViewById(R.id.tv_project_one);
        tv_project_two = (TextView) findViewById(R.id.tv_project_two);
        tv_project_three = (TextView) findViewById(R.id.tv_project_three);
        tv_project_four = (TextView) findViewById(R.id.tv_project_four);
        tv_zy1_bao = (TextView) findViewById(R.id.tv_zy1_bao);
        tv_zy2_bao = (TextView) findViewById(R.id.tv_zy2_bao);
        tv_zy3_bao = (TextView) findViewById(R.id.tv_zy3_bao);
        tv_zy4_bao = (TextView) findViewById(R.id.tv_zy4_bao);
        tv_qianming = (TextView) findViewById(R.id.tv_qianming);
        iv_zy1_tupian = (TextView) findViewById(R.id.iv_zy1_tupian);
        iv_zy2_tupian = (TextView) findViewById(R.id.iv_zy2_tupian);
        iv_zy3_tupian = (TextView) findViewById(R.id.iv_zy3_tupian);
        iv_zy4_tupian = (TextView) findViewById(R.id.iv_zy4_tupian);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_nianling = (TextView) findViewById(R.id.tv_nianling);
        tv_company_count = (TextView) findViewById(R.id.tv_company_count);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_titl = (TextView) findViewById(R.id.tv_titl);
        tv_paidan_qiyeren = (TextView) findViewById(R.id.tv_paidan_qiyeren);
        tv_paidan_qiye = (TextView) findViewById(R.id.tv_paidan_qiye);
        tv_paidan_kehu = (TextView) findViewById(R.id.tv_paidan_kehu);
        tv_paidan_caiwu = (TextView) findViewById(R.id.tv_paidan_caiwu);
        tev_paidan_qiye = (TextView) findViewById(R.id.tev_paidan_qiye);
        tev_paidan_qiyeren = (TextView) findViewById(R.id.tev_paidan_qiyeren);
        tev_paidan_caiwu = (TextView) findViewById(R.id.tev_paidan_caiwu);
        tev_paidan_kehu = (TextView) findViewById(R.id.tev_paidan_kehu);
        tv_time1 = (TextView) findViewById(R.id.tv_paidan_qiye_time);
        tv_time2 = (TextView) findViewById(R.id.tv_paidan_qiyeren_time);
        tv_time3 = (TextView) findViewById(R.id.tv_paidan_kehu_time);
        tv_time4 = (TextView) findViewById(R.id.tv_paidan_caiwu_time);
        et_your_project = (EditText) findViewById(R.id.et_your_project);
        et_qianshou_dizhi = (EditText) findViewById(R.id.et_qianshou_dizhi);
        et_dingdan_bianhao = (EditText) findViewById(R.id.et_dingdan_bianhao);
        et_zhifu_jine = (EditText) findViewById(R.id.et_zhifu_jine);
        cb_fapiao = (CheckBox) findViewById(R.id.cb_fapiao);
        etUBeizhu = (EditText) findViewById(R.id.et_Ubeizhu);
        tv_zhifuxianshi = (TextView) findViewById(R.id.tv_zhifuxianshi);
        tv_fenxiang = (TextView) findViewById(R.id.tv_fenxiang);
        tv_saomiao = (TextView) findViewById(R.id.tv_saomiao);
        btnCommit = (Button) findViewById(R.id.btn_comit);
        btnCansul = (Button) findViewById(R.id.btn_cansul);
        etUBeizhu.setEnabled(false);
        btnCansul.setEnabled(false);
        btnCommit.setEnabled(false);
    }

    private void initView2() {
        llM_beizhu.setVisibility(View.GONE);
        etUBeizhu.setEnabled(false);
        etUBeizhu.setVisibility(View.GONE);
        btnCommit.setText("确认签收");
        ll_btn.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
    }
    private void initView12() {
        tv_zhifuxianshi.setText("实付：");
        llM_beizhu.setVisibility(View.GONE);
        etUBeizhu.setEnabled(false);
        etUBeizhu.setVisibility(View.GONE);
        btnCommit.setText("确认签收");
        ll_btn.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
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
        if (orderState.equals("10")){
            ll_btn.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setText("售后反馈单");
            btnCommit.setEnabled(true);
            btnCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MPaiDanDetTwoActivity.this,FWFKListActivity.class).putExtra("biaoshi","03").putExtra("orderId",orderId));
                }
            });
        }
        if ("05".equals(orderState)){
            ll_btn.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setText("交易完成");
            btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            btnCommit.setEnabled(false);
        }
        if ("07".equals(orderState)){
            ll_btn.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setText("退款成功");
            btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            btnCommit.setEnabled(false);
        }
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
//        ScreenshotUtil.getBitmapByView(MPaiDanDetTwoActivity.this, ll, "订单", null,false);
        fenxiang();
    }

    private void saveImg(Bitmap mBitmap)  {
        File file = new FileStorage("fenxiang").createCropFile("dingdanCut.png",null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(MPaiDanDetTwoActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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
                    paramsToShare.setTitle("正事儿app");
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                } else if (platform.getName().equalsIgnoreCase(QZone.NAME)) {
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                }else if (platform.getName().equalsIgnoreCase(WechatMoments.NAME)){
                    paramsToShare.setShareType(Platform.SHARE_IMAGE);
                    paramsToShare.setTitle("正事儿app");
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dingdanCut.png");
                }
            }
        });
        oks.show(this);
    }

    private void initView3() {
        etUBeizhu.setEnabled(false);
        etUBeizhu.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setEnabled(true);
        btnCommit.setVisibility(View.GONE);
        if (image1.equals("")&&image3.equals("")&&orderState.equals("04")) {
            btnCommit.setText("客户签字");
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MPaiDanDetTwoActivity.this, QianMingActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("userId",userId);
                    intent.putExtra("biaoshi", "1201");
                    startActivity(intent);
                    finish();
                }
            });
            tv_paidan_kehu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MPaiDanDetTwoActivity.this, QianMingActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("userId",userId);
                    intent.putExtra("biaoshi", "1201");
                    startActivity(intent);
                    finish();
                }
            });
        }else if (orderState.equals("03")){
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MPaiDanDetTwoActivity.this, QianMingActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("biaoshi", "12");
                    startActivity(intent);
                    finish();
                }
            });
            tv_paidan_qiyeren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MPaiDanDetTwoActivity.this, QianMingActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("biaoshi", "12");
                    startActivity(intent);
                    finish();
                }
            });
        }else if (image2.equals("")&&image4.equals("")){
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setEnabled(false);
            btnCommit.setText("等待财务签字");
        }else if (oImage.equals("")&&oSignature.equals("")){
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setEnabled(false);
            btnCommit.setText("等待企业签字");
        }
    }
    private void initView4() {
        etUBeizhu.setEnabled(false);
        btnCommit.setText("确认退款");
        btnCansul.setText("拒绝退款");
        btnCommit.setEnabled(true);
        btnCansul.setEnabled(true);
        btnCansul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MPaiDanDetTwoActivity.this,QianMingActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("biaoshi","15");
                startActivity(intent);
                finish();
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MPaiDanDetTwoActivity.this,QianMingActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("biaoshi","16");
                intent.putExtra("userId",userId);
                intent.putExtra("balance",orderSum);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView7() {
        btnCommit.setEnabled(false);
        btnCansul.setEnabled(false);
        llM_beizhu.setEnabled(false);
        tv_paidan_qiyeren.setEnabled(false);
        tv_paidan_qiye.setEnabled(false);
        tv_paidan_kehu.setEnabled(false);
        tv_paidan_caiwu.setEnabled(false);
        iv_paidan_qiye.setEnabled(false);
        iv_paidan_qiyeren.setEnabled(false);
        iv_paidan_caiwu.setEnabled(false);
        iv_paidan_kehu.setEnabled(false);
    }

    private void initView5() {
        btnCansul.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
    }
    private void initView6() {
        llM_beizhu.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setVisibility(View.VISIBLE);
        btnCommit.setText("售后反馈单");
        btnCommit.setEnabled(true);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MPaiDanDetTwoActivity.this,FWFKListActivity.class).putExtra("biaoshi","02").putExtra("orderId",orderId));
            }
        });
    }
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }
    @Override
    public void updateCurrentOrderDetail(OrderDetail orderDetail) {
        orderProject = orderDetail.getOrderProject();
        orderNumber = orderDetail.getOrderNumber();
        orderAmt = orderDetail.getOrderAmt();
        orderSum = orderDetail.getOrderSum();
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
        beizhu = orderDetail.getRemark();
        yueding = TextUtils.isEmpty(orderDetail.getResv1())?"":orderDetail.getResv1();
        resv2 = TextUtils.isEmpty(orderDetail.getResv2())?"":orderDetail.getResv2();
        resv4 = TextUtils.isEmpty(orderDetail.getResv4())?"":orderDetail.getResv4();
//        if (!resv2.equals("")){
//            String[] res = resv2.split(",");
//            isRun = false;
//            countDown.setVisibility(View.GONE);
//            btnCommit.setText("提交验收");
//            tv_all.setVisibility(View.VISIBLE);
//            tv_all.setText(res[1]);
//            if (res[0].equals("0")){
//                tv2.setText("距离约定时间");
//            }else if (res[0].equals("1")){
//                tv2.setText("超出约定时间");
//            }
//        }else {
//            if (!yueding.equals("")) {
//                btnCommit.setVisibility(View.GONE);
//                btnCansul.setEnabled(true);
//                btnCansul.setVisibility(View.VISIBLE);
//                btnCansul.setText("按约签到");
//                String time1 = getNowTime();
//                long year = Long.parseLong(yueding.substring(0, 4));
//                long year1 = Long.parseLong(time1.substring(0, 4));
//                long mou = Long.parseLong(yueding.substring(4, 6));
//                long mou1 = Long.parseLong(time1.substring(4, 6));
//                long day = Long.parseLong(yueding.substring(6, 8));
//                long day1 = Long.parseLong(time1.substring(6, 8));
//                long hour = Long.parseLong(yueding.substring(8, 10));
//                long hour1 = Long.parseLong(time1.substring(8, 10));
//                long min = Long.parseLong(yueding.substring(10, 12));
//                long min1 = Long.parseLong(time1.substring(10, 12));
//                mSecond = Long.parseLong(time1.substring(12, 14));
//                long minAll = (year - year1) * 365 * 24 * 60 + (mou - mou1) * 30 * 24 * 60 + (day - day1) * 24 * 60 + (hour - hour1) * 60 + (min - min1);
//                if (minAll < 0) {
//                    isJian = false;
//                    minAll = -minAll;
//                    tv2.setText("超出约定时间");
//                } else {
//                    isJian = true;
//                    tv2.setText("距离约定时间");
//                }
//                mHour = minAll / 60;
//                mMin = minAll - mHour * 60;
//                if (mHour < 10) {
//                    hours = "0" + mHour;
//                } else {
//                    hours = String.valueOf(mHour);
//                }
//                if (mMin < 10) {
//                    mins = "0" + mMin;
//                } else {
//                    mins = String.valueOf(mMin);
//                }
//                if (mSecond < 10) {
//                    second = "0" + mSecond;
//                } else {
//                    second = String.valueOf(mSecond);
//                }
//                hoursTv.setText(hours);
//                minutesTv.setText(mins);
//                secondsTv.setText(second);
//                startRun();
//                btnCansul.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String url = FXConstant.URL_Order_Detail_update;
//                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String s) {
//                                try {
//                                    JSONObject object = new JSONObject(s);
//                                    String code = object.getString("code");
//                                    if (code.equals("SUCCESS")) {
//                                        isRun = false;
//                                        btnCansul.setVisibility(View.GONE);
//                                        btnCommit.setVisibility(View.VISIBLE);
//                                        btnCommit.setText("提交验收");
//                                        btnCommit.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                Intent intent = new Intent(MPaiDanDetTwoActivity.this, QianMingActivity.class);
//                                                intent.putExtra("orderId", orderId);
//                                                intent.putExtra("biaoshi", "12");
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                        });
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError volleyError) {
//
//                            }
//                        }){
//                            @Override
//                            protected Map<String, String> getParams() throws AuthFailureError {
//                                Map<String ,String> param = new HashMap<String,String>();
//                                param.put("conventionTime",nowTime);
//                                param.put("orderId",orderId);
//                                return param;
//                            }
//                        };
//                        queue.add(request);
//                    }
//                });
//            } else {
        if (orderState.equals("06")){
            btnCommit.setText("确认退款");
        }
//                isRun = false;
//                countDown.setVisibility(View.GONE);
//                tv_all.setVisibility(View.VISIBLE);
//                tv_all.setText("未设置约定时间");
//            }
//        }
        if (time1==null||"".equals(time1)){
            tev_paidan_qiyeren.setVisibility(View.INVISIBLE);
        }
        if (time2==null||"".equals(time2)){
            tev_paidan_kehu.setVisibility(View.INVISIBLE);
        }
        if (time3==null||"".equals(time3)){
            tev_paidan_caiwu.setVisibility(View.INVISIBLE);
        }
        if (orderTime==null||"".equals(orderTime)){
            tev_paidan_qiye.setVisibility(View.INVISIBLE);
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
        if (orderState.equals("01")||orderState.equals("02")){
            initView2();
        }else if (orderState.equals("05")||orderState.equals("10")||orderState.equals("07")) {
            initView12();
        }else if (orderState.equals("03")||orderState.equals("04")){
            initView3();
        }else if (orderState.equals("06")){
            initView4();
        }else if (orderState.equals("08")||orderState.equals("09")){
            initView5();
        }else if (orderState.equals("11")){
            initView6();
        }
        if ("不可操作".equals(biaoshi)&&!orderState.equals("06")){
            initView7();
        }
        if ("01".equals(biaoshi1)&&orderState.equals("03")){
            tv_fenxiang.setVisibility(View.VISIBLE);
            tv_fenxiang.setEnabled(true);
            tv_fenxiang.setText("派单");
            tv_fenxiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MPaiDanDetTwoActivity.this, QiYeYuGoActivity.class).putExtra("companyId",companyId)
                            .putExtra("orderId",orderId).putExtra("totalAmt",totalAmt));
                }
            });
        }
        if (orderSum!=null&&!"".equals(orderSum)) {
            double jine = Double.parseDouble(orderSum);
            final String str = String.format("%.2f", jine);
            et_zhifu_jine.setText(str+"元");
        }else {
            et_zhifu_jine.setText("0.00元");
        }
        et_your_project.setText(orderProject);
        et_qianshou_dizhi.setText(orderNumber);
//        et_dingdan_bianhao.setText(orderAmt);
        et_zhifu_jine.setEnabled(false);
        et_zhifu_jine.setTextColor(Color.BLACK);
        et_your_project.setTextColor(Color.BLACK);
        et_qianshou_dizhi.setTextColor(Color.BLACK);
        et_dingdan_bianhao.setTextColor(Color.BLACK);
        et_your_project.setEnabled(false);
        et_qianshou_dizhi.setEnabled(false);
        et_dingdan_bianhao.setEnabled(false);
        if (beizhu.equals("")&&mImage.equals("")){
            llM_beizhu.setVisibility(View.VISIBLE);
        }else {
            llM_beizhu.setVisibility(View.VISIBLE);
            etUBeizhu.setText(beizhu);
            etUBeizhu.setTextColor(Color.BLACK);
        }
        cb_fapiao.setEnabled(false);
        if (time4.equals("01")){
            cb_fapiao.setChecked(true);
        }else {
            cb_fapiao.setChecked(false);
        }
        etUBeizhu.setText(beizhu);
        if (orderState.equals("10")){
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setEnabled(true);
            btnCommit.setText("售后反馈单");
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void updateThisUser(Userful allUser) {
        tv_name.setText(TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName());
        String nianLing = TextUtils.isEmpty(allUser.getuAge()) ? "27" : allUser.getuAge();
        tv_nianling.setText(nianLing);
        String company = TextUtils.isEmpty(allUser.getCompany()) ? "暂未加入企业" : allUser.getCompany();
        if (company == null || company.equals("")) {
            company = "暂未加入企业";
        }
        try {
            company = URLDecoder.decode(company, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String member = allUser.getMenberNum();
        if (member==null||"".equals(member)){
            member = "0";
        }
        if (!company.equals("暂未加入企业")){
            tv_company_count.setVisibility(View.VISIBLE);
        }else {
            tv_company_count.setVisibility(View.INVISIBLE);
        }
        tv_company_count.setText("("+member+"人"+")");
        tv_company.setText(company);
        tv_project_one.setText(allUser.getUpName1());
        tv_project_two.setText(allUser.getUpName2());
        tv_project_three.setText(allUser.getUpName3());
        tv_project_four.setText(allUser.getUpName4());
        String image1 = allUser.getZyImage1();
        String image2 = allUser.getZyImage2();
        String image3 = allUser.getZyImage3();
        String image4 = allUser.getZyImage4();
        String margan1 = allUser.getMargin1();
        String margan2 = allUser.getMargin2();
        String margan3 = allUser.getMargin3();
        String margan4 = allUser.getMargin4();
        if (allUser.getUpName1()==null||allUser.getUpName1().equals("")){
            ll_one.setVisibility(View.GONE);
        }else {
            ll_one.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName2()==null||allUser.getUpName2().equals("")){
            ll_two.setVisibility(View.GONE);
        }else {
            ll_two.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName3()==null||allUser.getUpName3().equals("")){
            ll_three.setVisibility(View.GONE);
        }else {
            ll_three.setVisibility(View.VISIBLE);
        }
        if (allUser.getUpName4()==null||allUser.getUpName4().equals("")){
            ll_four.setVisibility(View.GONE);
        }else {
            ll_four.setVisibility(View.VISIBLE);
        }
        if (image1!=null&&!"".equals(image1)) {
            iv_zy1_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy1_tupian.setVisibility(View.GONE);
        }
        if (margan1!=null) {
            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                tv_zy1_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy1_bao.setVisibility(View.GONE);
            }
        }
        if (image2!=null&&!"".equals(image2)) {
            iv_zy2_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy2_tupian.setVisibility(View.GONE);
        }
        if (margan2!=null) {
            if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                tv_zy2_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy2_bao.setVisibility(View.GONE);
            }
        }
        if (image3!=null&&!"".equals(image3)) {
            iv_zy3_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy3_tupian.setVisibility(View.GONE);
        }
        if (margan3!=null) {
            if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                tv_zy3_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy3_bao.setVisibility(View.GONE);
            }
        }
        if (image4!=null&&!"".equals(image4)) {
            iv_zy4_tupian.setVisibility(View.VISIBLE);
        }else {
            iv_zy4_tupian.setVisibility(View.GONE);
        }
        if (margan4!=null) {
            if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                tv_zy4_bao.setVisibility(View.VISIBLE);
            }else {
                tv_zy4_bao.setVisibility(View.GONE);
            }
        }
        String head = TextUtils.isEmpty(allUser.getImage()) ? "" : allUser.getImage();
        if (head.length() > 40) {
            tv_titl.setVisibility(View.INVISIBLE);
            iv_head.setVisibility(View.VISIBLE);
            String[] orderProjectArray = head.split("\\|");
            head = orderProjectArray[0];
        }
        if (!(head.equals("") || head.equals(null))) {
            tv_titl.setVisibility(View.INVISIBLE);
            iv_head.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+head,iv_head, DemoApplication.mOptions);
        } else {
            iv_head.setVisibility(View.INVISIBLE);
            tv_titl.setVisibility(View.VISIBLE);
            tv_titl.setText(TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName());
        }
        if (("00").equals(allUser.getSex())) {
            iv_sex.setImageResource(R.drawable.nv);
            //保 255 62 74  图 255 170 76
            iv_sex.setBackgroundColor(Color.rgb(234,121,219));
            tv_titl.setBackgroundResource(R.drawable.fx_bg_text_red);
        } else {
            iv_sex.setImageResource(R.drawable.nan);
            tv_titl.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }
        String resv3 = TextUtils.isEmpty(allUser.getResv3()) ? "" : allUser.getResv3();
        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
        String resv2 = TextUtils.isEmpty(allUser.getResv2()) ? "" : allUser.getResv2();
        String lat = DemoApplication.getInstance().getCurrentLat();
        String lng = DemoApplication.getInstance().getCurrentLng();
        if (lat!=null&&lng!=null) {
            if (!("".equals(lat) || "".equals(lng) || resv1.equals("") || resv2.equals(""))) {
                double latitude1 = Double.valueOf(lat);
                double longitude1 = Double.valueOf(lng);
                final LatLng ll1 = new LatLng(Double.parseDouble(resv2), Double.parseDouble(resv1));
                LatLng ll = new LatLng(latitude1, longitude1);
                double distance = DistanceUtil.getDistance(ll, ll1);
                double dou = distance / 1000;
                String str = String.format("%.2f", dou);//format 返回的是字符串
                if (str!=null&&dou>=10000){
                    tv_distance.setText("隐藏");
                }else {
                    tv_distance.setText(str + "km");
                }
            } else {
                tv_distance.setText("3km以外");
            }
        }else {
            tv_distance.setText("3km以外");
        }
        String sign = allUser.getSignaTure();
        if (sign==null||"".equals(sign)){
            sign = "未设置简介";
        }
        tv_qianming.setText(sign);
        if (orderState.equals("10")){
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setEnabled(true);
            btnCommit.setText("售后反馈单");
        }
    }

    @Override
    public void showproLoading() {

    }

    @Override
    public void hideproLoading() {

    }

    @Override
    public void showproError() {

    }
}