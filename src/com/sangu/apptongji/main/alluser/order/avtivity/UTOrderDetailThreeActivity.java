package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.fanxin.easeui.utils.FileStorage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.alluser.presenter.IOrderDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.OrderDetailPresenter;
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
 * Created by user on 2016/9/1.
 */
public class UTOrderDetailThreeActivity extends BaseActivity implements IOrderDetailView,BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener {
    private IOrderDetailPresenter orderDetailPresenter;
    private ImageView ivBack=null;
    private String orderState,pass,biaoshi,biaoshi1,totalAmt;
    private EditText etUBeizhu=null,et_my_weizhi=null,et_mudidi=null,et_zhifu_jine=null;
    private TextView tv_jifei_jine=null,tv_zhifuxianshi=null,tv_fenxiang=null,tv_qiye_ticheng=null;
    private String resv5Geren="",resv5Qiye="",resv5="",companyId;
    private Button btnCommit=null,btnCansul=null;
    private RelativeLayout rl_ticheng=null;
    private String orderId,merId,orderProject,orderNumber,orderAmt,orderSum,oImage,mImage,beizhu,yueding,resv2;
    private ImageView ivkehu=null,ivshangjia=null;
    private RelativeLayout llM_beizhu=null;
    private CheckBox cb_fapiao=null;
    private Button btnCommit2=null;
    private TextView tv_count_fankui=null;
    private RelativeLayout rl_btn1=null;
    private LinearLayout ll_btn=null,ll=null;

    private MapView mMapView=null;
    private BaiduMap mBaiduMap=null;
    LocationClient mLocClient=null;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    boolean isFirstLoc = true; // 是否首次定位
    String lng,lat,orderTime;
    private TextView tv_qiye_time_shanghu=null,tv_qiye_time_xiaofei=null,tv_qiye_xiaofei=null,tv_qiye_shanghu=null,tev_qiye_xiaofei=null,tev_qiye_shanghu=null;
//    private IFWFKPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morder_moshisan);
        orderDetailPresenter = new OrderDetailPresenter(this,this);
//        presenter = new FWFKPresenter(this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        mMapView = (MapView) findViewById(R.id.bmapView1);
        llM_beizhu = (RelativeLayout) findViewById(R.id.llM_beizhu);
        rl_ticheng = (RelativeLayout) findViewById(R.id.rl_ticheng);
        tv_zhifuxianshi = (TextView) findViewById(R.id.tv_zhifuxianshi);
        tv_qiye_ticheng = (TextView) findViewById(R.id.tv_qiye_ticheng);
        tv_fenxiang = (TextView) findViewById(R.id.tv_fenxiang);
        tev_qiye_xiaofei = (TextView) findViewById(R.id.tev_qiye_xiaofei);
        tev_qiye_shanghu = (TextView) findViewById(R.id.tev_qiye_shanghu);
        tv_qiye_time_shanghu = (TextView) findViewById(R.id.tv_qiye_time_shanghu);
        tv_qiye_time_xiaofei = (TextView) findViewById(R.id.tv_qiye_time_xiaofei);
        tv_qiye_xiaofei = (TextView) findViewById(R.id.tv_qiye_xiaofei);
        tv_qiye_shanghu = (TextView) findViewById(R.id.tv_qiye_shanghu);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivkehu = (ImageView) findViewById(R.id.iv_xiaofei);
        ivshangjia = (ImageView) findViewById(R.id.iv_shanghu);
        rl_ticheng.setVisibility(View.INVISIBLE);
//        yuE = DemoApplication.getApp().getCurrenPrice();
        Intent intent = this.getIntent();
        orderTime = intent.getStringExtra("orderTime");
        companyId = intent.getStringExtra("companyId");
        totalAmt = intent.hasExtra("totalAmt")?intent.getStringExtra("totalAmt"):"";
        biaoshi = intent.hasExtra("biaoshi")?intent.getStringExtra("biaoshi"):"";
        biaoshi1 = intent.hasExtra("biaoshi1")?intent.getStringExtra("biaoshi1"):"";
        orderId = intent.getStringExtra("orderId");
        orderState = intent.getStringExtra("orderState");
        merId = intent.getStringExtra("merId");
        pass = intent.getStringExtra("pypass");
        initView();
        orderDetailPresenter.loadOrderDetail(orderId);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMapClickListener(this);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        if (orderState.equals("03")){
            initView2();
        }else if (orderState.equals("05")||orderState.equals("07")){
            initView1();
        }
        else if(orderState.equals("04")){
            initView3();
        }else if (orderState.equals("06")){
            initView4();
        }else if (orderState.equals("08")||orderState.equals("09")){
            initView5();
        }else if (orderState.equals("10")||orderState.equals("11")){
//            presenter.loadFWFKList(orderId);
            initView6();
        }
        if ("不可操作".equals(biaoshi)){
            initView7();
        }
        if ("01".equals(biaoshi1)){
            tv_fenxiang.setVisibility(View.VISIBLE);
            tv_fenxiang.setEnabled(true);
            tv_fenxiang.setText("派单");
            tv_fenxiang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UTOrderDetailThreeActivity.this, QiYeYuGoActivity.class).putExtra("companyId",companyId)
                            .putExtra("orderId",orderId).putExtra("totalAmt",totalAmt));
                }
            });
        }
    }
//    @Override
//    public void updataFuWuFanKuiList(List<FWFKInfo> fuWuFKinfo) {
//        int count = fuWuFKinfo.size();
//        tv_count_fankui.setText("["+count+"]");
//        btnCommit2.setText("售后反馈");
//        btnCommit2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (orderState.equals("11")) {
//                    new AlertDialog.Builder(UTOrderDetailThreeActivity.this)
//                            .setTitle("确认")
//                            .setMessage("确认申请售后吗？")
//                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    shenqingshouhou();
//                                }
//                            })
//                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).show();
//                }else if (orderState.equals("10")){
//                    startActivity(new Intent(UTOrderDetailThreeActivity.this, FWFKListActivity.class)
//                            .putExtra("biaoshi", "00").putExtra("orderId", orderId));
//                }
//            }
//        });
//        tv_count_fankui.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(UTOrderDetailThreeActivity.this, FWFKListActivity.class)
//                        .putExtra("biaoshi", "02").putExtra("orderId", orderId));
//            }
//        });
//    }
    private void initView6() {
        llM_beizhu.setVisibility(View.GONE);
        etUBeizhu.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        ll_btn.setVisibility(View.INVISIBLE);
        rl_btn1.setVisibility(View.VISIBLE);
        btnCommit.setVisibility(View.VISIBLE);
        btnCommit.setEnabled(false);
        btnCommit.setText("交易完成");
        btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        tv_fenxiang.setVisibility(View.INVISIBLE);
        tv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentImage();
            }
        });
    }
    private void initView7() {
        et_my_weizhi.setEnabled(false);
        et_mudidi.setEnabled(false);
        et_zhifu_jine.setEnabled(false);
        etUBeizhu.setEnabled(false);
        btnCommit.setEnabled(false);
        btnCansul.setEnabled(false);
        llM_beizhu.setEnabled(false);
        ivkehu.setEnabled(false);
        tv_qiye_time_shanghu.setEnabled(false);
        tv_qiye_time_xiaofei.setEnabled(false);
        tv_qiye_xiaofei.setEnabled(false);
        tv_qiye_shanghu.setEnabled(false);
        ivshangjia.setEnabled(false);
    }
    private String getNowTime2() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
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
                    Intent intent = new Intent(UTOrderDetailThreeActivity.this,QianMingActivity.class);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("remark", beizhu);
                    intent.putExtra("biaoshi","13");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UTOrderDetailThreeActivity.this, "请写明退款原因", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void initView1() {
        tv_zhifuxianshi.setText("实付：");
        llM_beizhu.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setVisibility(View.VISIBLE);
        etUBeizhu.setEnabled(false);
        etUBeizhu.setVisibility(View.INVISIBLE);
        tv_fenxiang.setVisibility(View.INVISIBLE);
        tv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCurrentImage();
            }
        });
        if ("05".equals(orderState)) {
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setText("交易成功");
            btnCommit.setEnabled(false);
            btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
//            btnCommit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new AlertDialog.Builder(UTOrderDetailThreeActivity.this)
//                            .setTitle("确认")
//                            .setMessage("确认申请售后吗？")
//                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    shenqingshouhou();
//                                }
//                            })
//                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).show();
//                }
//            });
        }else {
            llM_beizhu.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setText("退款成功");
            btnCommit.setEnabled(false);
            btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }
    }
//    private void shenqingshouhou() {
//        String url = FXConstant.URL_SHOUHOU_UPDATE;
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Toast.makeText(UTOrderDetailThreeActivity.this,"申请成功,等待商家回复！",Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Toast.makeText(UTOrderDetailThreeActivity.this,"网络连接错误！",Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> param = new HashMap<>();
//                param.put("orderId",orderId);
//                param.put("state","10");
//                return param;
//            }
//        };
//        queue.add(request);
//    }
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
//        ScreenshotUtil.getBitmapByView(UTOrderDetailThreeActivity.this, ll, "订单", null,false);
        fenxiang();
    }

    private void saveImg(Bitmap mBitmap)  {
        File file = new FileStorage("fenxiang").createCropFile("dingdanCut.png",null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(UTOrderDetailThreeActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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
        et_my_weizhi = (EditText) findViewById(R.id.et_my_weizhi);
        et_mudidi = (EditText) findViewById(R.id.et_mudidi);
        et_zhifu_jine = (EditText) findViewById(R.id.et_zhifu_jine);
        cb_fapiao = (CheckBox) findViewById(R.id.cb_fapiao);
        etUBeizhu = (EditText) findViewById(R.id.et_Ubeizhu);
        btnCommit = (Button) findViewById(R.id.btn_comit);
        btnCansul = (Button) findViewById(R.id.btn_cansul);
        tv_count_fankui = (TextView) findViewById(R.id.tv_count_fankui);
        btnCommit2 = (Button) findViewById(R.id.btn_comit2);
        ll = (LinearLayout) findViewById(R.id.ll);
        ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
        rl_btn1 = (RelativeLayout) findViewById(R.id.rl_btn1);
        btnCansul.setEnabled(false);
        btnCommit.setEnabled(false);
        et_my_weizhi.setEnabled(false);
        et_mudidi.setEnabled(false);
        et_zhifu_jine.setEnabled(false);
        cb_fapiao.setEnabled(false);
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
        oImage = TextUtils.isEmpty(orderDetail.getoImage())?"":orderDetail.getoImage();
        mImage = TextUtils.isEmpty(orderDetail.getmImage())?"":orderDetail.getmImage();
        beizhu = TextUtils.isEmpty(orderDetail.getRemark())?"":orderDetail.getRemark();
//        yueding = TextUtils.isEmpty(orderDetail.getResv1())?"":orderDetail.getResv1();
//        resv2 = TextUtils.isEmpty(orderDetail.getResv2())?"":orderDetail.getResv2();
        String time4 = TextUtils.isEmpty(orderDetail.getTime4())?"00":orderDetail.getTime4();
        String shjTime1 = TextUtils.isEmpty(orderDetail.getTime1())?"":orderDetail.getTime1();
        String resv4 = TextUtils.isEmpty(orderDetail.getResv4())?"":orderDetail.getResv4();
        String oSigntrue = TextUtils.isEmpty(orderDetail.getoSignature())?"":orderDetail.getoSignature();
        String mSigntrue = TextUtils.isEmpty(orderDetail.getmSignature())?"":orderDetail.getmSignature();
        resv5 = TextUtils.isEmpty(orderDetail.getResv5())?"":orderDetail.getResv5();
        if (resv5.length()>0){
            resv5Geren = resv5.split("\\|")[0];
            resv5Qiye = resv5.split("\\|")[1];
        }
        tv_qiye_ticheng.setText(resv5Qiye+"元");
        if (!oSigntrue.equals("")){
            tv_qiye_xiaofei.setText("客户已确认");
        }
        if (!mSigntrue.equals("")){
            tv_qiye_shanghu.setText("商家已确认");
        }
        if (resv4==null||"".equals(resv4)){
            tev_qiye_xiaofei.setVisibility(View.VISIBLE);
            tev_qiye_xiaofei.setText("下单:");
            tv_qiye_time_xiaofei.setVisibility(View.VISIBLE);
            tv_qiye_time_xiaofei.setText(orderTime);
        }else {
            tev_qiye_xiaofei.setVisibility(View.VISIBLE);
            tev_qiye_xiaofei.setText("完成:");
            tv_qiye_time_xiaofei.setVisibility(View.VISIBLE);
            tv_qiye_time_xiaofei.setText(resv4);
        }
        if (shjTime1==null||"".equals(shjTime1)){
            tev_qiye_shanghu.setVisibility(View.INVISIBLE);
        }
        tv_qiye_time_shanghu.setText(shjTime1);
        if (!oImage.equals("")){
            tv_qiye_xiaofei.setVisibility(View.INVISIBLE);
            String[] avatar = oImage.split("\\|");
            oImage = avatar[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN + oImage,ivkehu);
        }
        if (!mImage.equals("")){
            tv_qiye_shanghu.setVisibility(View.INVISIBLE);
            String[] avatar = mImage.split("\\|");
            mImage = avatar[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN + mImage,ivshangjia);
        }
        if (orderSum!=null&&!"".equals(orderSum)) {
            double jine = Double.parseDouble(orderSum);
            final String str = String.format("%.2f", jine);
            et_zhifu_jine.setText(str + "元");
        }else {
            et_zhifu_jine.setText("0.00元");
        }
        String [] str =orderProject.split(",");
        if (str.length>0) {
            String orderProject1 = str[0];
            String orderProject2 = str[1];
            et_my_weizhi.setText(orderProject1);
            et_mudidi.setText(orderProject2);
        }
        et_zhifu_jine.setEnabled(false);
        et_my_weizhi.setTextColor(Color.BLACK);
        et_mudidi.setTextColor(Color.BLACK);
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
    }

    private void initView3() {
        etUBeizhu.setEnabled(true);
        btnCommit.setText("确认签收");
        btnCommit.setEnabled(true);
        btnCansul.setEnabled(true);
        btnCansul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUBeizhu.setFocusable(true);
                etUBeizhu.setFocusableInTouchMode(true);
                etUBeizhu.requestFocus();
                final String beizhu = etUBeizhu.getText().toString().trim();
                if (!TextUtils.isEmpty(beizhu)) {
                    Intent intent = new Intent(UTOrderDetailThreeActivity.this,QianMingActivity.class);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("remark", beizhu);
                    intent.putExtra("biaoshi","13");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UTOrderDetailThreeActivity.this, "请写明退款原因", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        if (resv5.length()>0||companyId.length()>0){
            ivkehu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UTOrderDetailThreeActivity.this, QianMingActivity.class);
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
                    Intent intent = new Intent(UTOrderDetailThreeActivity.this, QianMingActivity.class);
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
            btnCommit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UTOrderDetailThreeActivity.this, QianMingActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("merId", merId);
                    intent.putExtra("balance", orderSum);
                    intent.putExtra("biaoshi", "11");
                    startActivity(intent);
                    finish();
                }
            });
            tv_qiye_xiaofei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UTOrderDetailThreeActivity.this, QianMingActivity.class);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(UTOrderDetailThreeActivity.this);
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
                                        Toast.makeText(UTOrderDetailThreeActivity.this, "申请平台介入成功！", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(UTOrderDetailThreeActivity.this, "申请失败！", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                dialog.dismiss();
                                Toast.makeText(UTOrderDetailThreeActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
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
                        MySingleton.getInstance(UTOrderDetailThreeActivity.this).addToRequestQueue(request1);
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            lat = String.valueOf(location.getLatitude());
            lng = String.valueOf(location.getLongitude());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        if (mLocClient!=null){
            mLocClient.stop();
        }
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        if (mSearch != null) {
            mSearch.destroy();
        }
        if (mLocClient!=null) {
            mLocClient.stop();
            mLocClient=null;
        }
        // 关闭定位图层
        super.onDestroy();
    }
}
