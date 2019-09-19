package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.mapsearch.MapPickerActivity;
import com.sangu.apptongji.main.utils.CashierInputFilter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-11-23.
 */

public class UOrderDetailThreeActivity extends BaseActivity implements IPriceView,View.OnClickListener,BaiduMap.OnMapClickListener,
        OnGetRoutePlanResultListener {
    private static final int SDK_PAY_FLAG = 1;
    private TextView tv_paidan_qiye_time;
    private TextView tv_paidan_qiyeren_time;
    private TextView tv_paidan_caiwu_time;
    private TextView tv_paidan_kehu_time;
    private ImageView iv_jiantou;
    private LinearLayout ll2;
    private LinearLayout ll_xiadan;
    private LinearLayout ll1_paidan;
    private LinearLayout ll2_paidan;
    private MapView mMapView;
    private EditText et_my_weizhi;
    private EditText et_mudidi;
    private EditText et_zhifu_jine;
    private TextView tv_jifei_jine;
    private ImageView iv_xiaofei;
    private ImageView iv_shanghu;
    private Button btn_yanzi;
    private CheckBox cb_fapiao;
    private LinearLayout ll;
    private TextView tev_paidan_qiye=null;
    private TextView tev_paidan_qiyeren=null;
    private TextView tev_paidan_caiwu=null;
    private TextView tev_paidan_kehu=null;
    private BaiduMap mBaiduMap=null;
    LocationClient mLocClient=null;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode=null;
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    DrivingRouteResult nowResultdrive  = null;
    int nowSearchType = -1 ; // 当前进行的检索，供判断浏览节点时结果使用。
    private String startNodeStr = "";
    private String endNodeStr = "";
    private String hxid=null,pass=null,merId=null,zy1=null,orderId=null,zongjia=null,orderProject=null,time=null,time4=null,
            typeDetail=null,biaoshi=null,createTime="",dynamicSeq="",companyName=null,companyAdress=null,managerId=null,orderBody;
    private int errorTime=3;
    private Double yuE;
    private ProgressDialog pd=null;
    String lng=null,lat=null,startCity=null,endcity=null;
    private IWXAPI api=null;
    boolean isFirstLoc = true; // 是否首次定位
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if (hxid.length()>12){
                            sendPushMessage(managerId,"002");
                        }else {
                            sendPushMessage(hxid,"000");
                        }
                        Toast.makeText(UOrderDetailThreeActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
                        zongjia = et_zhifu_jine.getText().toString().trim();
                        final double orderSu = Double.valueOf(zongjia);
                        yuE = yuE - orderSu;
                        DemoApplication.getApp().saveCurrentPrice(yuE);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(UOrderDetailThreeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String zhifuZhuangtai = intent.getStringExtra("zhifu");
        if (zhifuZhuangtai!=null&&"支付成功".equals(zhifuZhuangtai)){
            if (hxid.length()>12){
                sendPushMessage(managerId,"002");
            }else {
                sendPushMessage(hxid,"000");
            }
            Toast.makeText(UOrderDetailThreeActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
            zongjia = et_zhifu_jine.getText().toString().trim();
            final double orderSu = Double.valueOf(zongjia);
            yuE = yuE - orderSu;
            DemoApplication.getApp().saveCurrentPrice(yuE);
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_xiadan_moshisan);
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        tv_paidan_qiye_time = (TextView) findViewById(R.id.tv_paidan_qiye_time);
        tv_paidan_qiyeren_time = (TextView) findViewById(R.id.tv_paidan_qiyeren_time);
        tv_paidan_caiwu_time = (TextView) findViewById(R.id.tv_paidan_caiwu_time);
        tv_paidan_kehu_time = (TextView) findViewById(R.id.tv_paidan_kehu_time);
        iv_jiantou = (ImageView) findViewById(R.id.iv_jiantou);
        iv_xiaofei = (ImageView) findViewById(R.id.iv_xiaofei);
        iv_shanghu = (ImageView) findViewById(R.id.iv_shanghu);
        btn_yanzi = (Button) findViewById(R.id.btn_yanzi);
        cb_fapiao = (CheckBox) findViewById(R.id.cb_fapiao);
        ll = (LinearLayout) findViewById(R.id.ll);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll_xiadan = (LinearLayout) findViewById(R.id.ll_xiadan);
        ll1_paidan = (LinearLayout) findViewById(R.id.ll1_paidan);
        ll2_paidan = (LinearLayout) findViewById(R.id.ll2_paidan);
        mMapView = (MapView) findViewById(R.id.bmapView1);
        et_my_weizhi = (EditText) findViewById(R.id.et_my_weizhi);
        et_mudidi = (EditText) findViewById(R.id.et_mudidi);
        et_zhifu_jine = (EditText) findViewById(R.id.et_zhifu_jine);
        tv_jifei_jine = (TextView) findViewById(R.id.tv_jifei_jine);
        tev_paidan_qiye = (TextView) findViewById(R.id.tev_paidan_qiye);
        tev_paidan_qiyeren = (TextView) findViewById(R.id.tev_paidan_qiyeren);
        tev_paidan_caiwu = (TextView) findViewById(R.id.tev_paidan_caiwu);
        tev_paidan_kehu = (TextView) findViewById(R.id.tev_paidan_kehu);
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("正在发送请求...");
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
        et_my_weizhi.setFocusable(false);
        et_my_weizhi.setFocusableInTouchMode(false);
        et_mudidi.setFocusable(false);
        et_mudidi.setFocusableInTouchMode(false);
        et_my_weizhi.setOnClickListener(this);
        et_mudidi.setOnClickListener(this);
        btn_yanzi.setOnClickListener(this);
        tev_paidan_qiye.setVisibility(View.INVISIBLE);
        tev_paidan_qiyeren.setVisibility(View.INVISIBLE);
        tev_paidan_caiwu.setVisibility(View.INVISIBLE);
        tev_paidan_kehu.setVisibility(View.INVISIBLE);
        tv_paidan_qiye_time.setVisibility(View.INVISIBLE);
        tv_paidan_qiyeren_time.setVisibility(View.INVISIBLE);
        tv_paidan_caiwu_time.setVisibility(View.INVISIBLE);
        tv_paidan_kehu_time.setVisibility(View.INVISIBLE);
        iv_jiantou.setVisibility(View.INVISIBLE);
        companyName = this.getIntent().hasExtra("companyName")?this.getIntent().getStringExtra("companyName"):"0";
        companyAdress = this.getIntent().hasExtra("companyAdress")?this.getIntent().getStringExtra("companyAdress"):"0";
        managerId = this.getIntent().getStringExtra("managerId");
        hxid = this.getIntent().getStringExtra("hxid");
        zy1 = this.getIntent().getStringExtra("zy1");
        typeDetail = this.getIntent().getStringExtra("typeDetail");
        orderBody = this.getIntent().getStringExtra("orderBody");
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        createTime = this.getIntent().hasExtra("createTime")?this.getIntent().getStringExtra("createTime"):"";
        dynamicSeq = this.getIntent().hasExtra("dynamicSeq")?this.getIntent().getStringExtra("dynamicSeq"):"";
        if (typeDetail.equals("01")){
            ll_xiadan.setVisibility(View.VISIBLE);
            ll1_paidan.setVisibility(View.GONE);
            ll2_paidan.setVisibility(View.GONE);
            iv_jiantou.setVisibility(View.GONE);
        }else if (typeDetail.equals("02")){
            ll_xiadan.setVisibility(View.GONE);
            ll2.setVisibility(View.VISIBLE);
            ll1_paidan.setVisibility(View.VISIBLE);
            ll2_paidan.setVisibility(View.VISIBLE);
        }
        if (biaoshi.equals("01")){
            String balance = this.getIntent().getStringExtra("balance");
            et_zhifu_jine.setText(balance);
            et_zhifu_jine.setEnabled(false);
        }
        et_zhifu_jine.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters={new CashierInputFilter()};
        et_zhifu_jine.setFilters(filters);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_yanzi:
                final String sum = et_zhifu_jine.getText().toString().trim();
                if (!TextUtils.isEmpty(sum)&&Double.parseDouble(sum)>0) {
                    LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailThreeActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                    final Dialog dialog = new AlertDialog.Builder(UOrderDetailThreeActivity.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.setCanceledOnTouchOutside(true);
                    RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                    RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                    RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                    RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                    re_item5.setVisibility(View.VISIBLE);
                    TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                    TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                    TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                    TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
                    tv_title.setText("请选择支付方式");
                    tv_item1.setText("余 额 支 付");
                    tv_item2.setText("微 信 支 付");
                    tv_item5.setText("支付宝支付");
                    re_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            zhifu3(sum);
                        }
                    });
                    re_item2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            insertOrder("02");
                        }
                    });
                    re_item5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            insertOrder("01");
                        }
                    });
                    re_item3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(UOrderDetailThreeActivity.this, "请输入总价！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.et_my_weizhi:
                startActivityForResult(new Intent(UOrderDetailThreeActivity.this, MapPickerActivity.class).putExtra("lat",lat).putExtra("lng",lng),0);
                break;
            case R.id.et_mudidi:
                startActivityForResult(new Intent(UOrderDetailThreeActivity.this, MapPickerActivity.class).putExtra("lat",lat).putExtra("lng",lng),1);
                break;
        }
    }

    private void zhifu3(String sum) {
        if (pass==null||"".equals(pass)){
            showErrorMiMaSHZH();
            return;
        }
        if (pass.length()!=6||!isNumeric(pass)){
            showErrorMiMaXG();
            return;
        }
        if (errorTime<=0){
            showErrorLing();
            return;
        }
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
        final double orderSu = Double.valueOf(sum);
        Double d = yuE - orderSu;
        if (d >= 0) {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailThreeActivity.this);
            final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailThreeActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(lp);
            final PasswordView pwdView = (PasswordView)layout.findViewById(R.id.pwd_view);
            pwdView.setOnFinishInput(new OnPasswordInputFinish() {
                @Override
                public void inputFinish() {
                    pd = new ProgressDialog(UOrderDetailThreeActivity.this);
                    pd.setMessage("正在支付...");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();
                    if (pwdView.getStrPassword().equals(pass)) {
                        dialog.dismiss();
                        insertOrder("00");
                    } else {
                        pd.dismiss();
                        int times;
                        if (errorTime>0){
                            times = errorTime-1;
                        }else {
                            times = 0;
                        }
                        reduceShRZFCount(times+"");
                        if (times==0) {
                            showErrorLing();
                        }else {
                            showErrorTishi(times + "");
                        }
                        dialog.dismiss();
                    }
                }
            });
            /**
             *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
             *  如果写了，会覆盖我们在自定义控件中提供的响应
             *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
            pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            pwdView.getForgetTextView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String bs;
                    bs = "000";
                    startActivity(new Intent(UOrderDetailThreeActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
                }
            });
        } else {
            LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailThreeActivity.this);
            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
            final Dialog dialog = new AlertDialog.Builder(UOrderDetailThreeActivity.this,R.style.Dialog).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            dialog.setCanceledOnTouchOutside(true);
            RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
            RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
            RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
            RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
            re_item1.setVisibility(View.GONE);
            TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
            TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
            TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
            TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
            tv_title.setText("钱包余额不足(剩余"+yuE+"元),请您先充值或者选择其他支付方式");
            tv_item1.setText("");
            tv_item2.setText("微信支付");
            tv_item5.setText("支付宝支付");
            re_item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            re_item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    insertOrder("02");
                }
            });
            re_item5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    insertOrder("01");
                }
            });
            re_item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        } else {
            ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
        }
    }

    private void showErrorLing() {
        LayoutInflater inflaterDl = LayoutInflater.from(UOrderDetailThreeActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
        final Dialog dialog2 = new AlertDialog.Builder(UOrderDetailThreeActivity.this,R.style.Dialog).create();
        dialog2.show();
        dialog2.getWindow().setContentView(layout);
        dialog2.setCanceledOnTouchOutside(false);
        dialog2.setCancelable(false);
        TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
        Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
        tv_title.setText("支付密码错误次数达到上限,请次日重试或联系客服");
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
    }

    private void showErrorTishi(String s) {
        LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailThreeActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailThreeActivity.this,R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("温馨提示");
        btnOK1.setText("忘记密码");
        btnCancel1.setText("重新输入");
        title_tv1.setText("支付密码错误，您还可以输入"+s+"次");
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        btnOK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                String bs;
                bs = "000";
                startActivity(new Intent(UOrderDetailThreeActivity.this, WJPaActivity.class).putExtra("biaoshi",bs).putExtra("managerId",managerId));
            }
        });
    }

    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if(!isNum.matches() ){
            return false;
        }
        return true;
    }

    private void showErrorMiMaXG() {
        LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailThreeActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailThreeActivity.this,R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("温馨提示");
        btnOK1.setText("前去设置");
        btnCancel1.setText("以后再说");
        title_tv1.setText("支付系统更新,请先修改您的支付密码为6为纯数字组合");
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        btnOK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                String bs;
                bs = "000";
                startActivity(new Intent(UOrderDetailThreeActivity.this, XiuGaiZFActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",pass));
            }
        });
    }

    private void showErrorMiMaSHZH() {
        LayoutInflater inflater1 = LayoutInflater.from(UOrderDetailThreeActivity.this);
        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
        final Dialog dialog1 = new AlertDialog.Builder(UOrderDetailThreeActivity.this,R.style.Dialog).create();
        dialog1.show();
        dialog1.getWindow().setContentView(layout1);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.setCancelable(true);
        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
        title.setText("温馨提示");
        btnOK1.setText("前去设置");
        btnCancel1.setText("以后再说");
        title_tv1.setText("您还没有设置支付密码!");
        btnCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        btnOK1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                String bs;
                bs = "000";
                startActivity(new Intent(UOrderDetailThreeActivity.this,ZhiFuSettingActivity.class).putExtra("biaoshi",bs).putExtra("zhifupass",pass));
            }
        });
    }

    private void reduceShRZFCount(final String times) {
        String url = FXConstant.URL_UPDATEZHHU;
        StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if (code.equals("SUCCESS")){
                    if (errorTime>0) {
                        errorTime--;
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                params1.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                params1.put("enterErrorTimes", times+"");
                return params1;
            }
        };
        MySingleton.getInstance(UOrderDetailThreeActivity.this).addToRequestQueue(request3);
    }

    private void insertOrder(final String biaoshi) {
        String url = FXConstant.URL_INSERT_ORDER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    String code = obj.getString("code");
                    JSONObject object = obj.getJSONObject("orderInfo");
                    orderId = object.getString("orderId");
                    merId = object.getString("merId");
                    Log.e("merId",merId);
                    if (code.equals("数据更新成功")) {
                        if (hxid.length()>12){
                            updateQjcount();
                        }else {
                            updateUscount(hxid);
                        }
                        bianji(biaoshi);
                    } else {
                        Toast.makeText(UOrderDetailThreeActivity.this, "下单失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UOrderDetailThreeActivity.this, "网络连接错误，下单失败！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("merId", hxid);
                params.put("orderBody",orderBody);
                params.put("flg", "03");
                params.put("type", typeDetail);
                params.put("upId", zy1);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailThreeActivity.this).addToRequestQueue(request);
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
        MySingleton.getInstance(UOrderDetailThreeActivity.this).addToRequestQueue(request);
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
                param.put("companyId",hxid);
                return param;
            }
        };
        MySingleton.getInstance(UOrderDetailThreeActivity.this).addToRequestQueue(request);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode == 0) {
                if (data != null) {
                    startNodeStr = data.getStringExtra("street");
                    startCity = data.getStringExtra("city");
                    et_my_weizhi.setText(startNodeStr);
                }
            }else if (requestCode==1){
                if (data != null){
                    endcity = data.getStringExtra("city");
                    endNodeStr = data.getStringExtra("street");
                    et_mudidi.setText(endNodeStr);
                }
            }
            if (!startNodeStr.equals("")&&!endNodeStr.equals("")){
                startSearch();
            }
        }
    }

    private void bianji(final String biaoshi){
        if (cb_fapiao.isChecked()){
            time4 = "01";
        }else {
            time4 = "00";
        }
        time = "";
        zongjia = et_zhifu_jine.getText().toString().trim();
        orderProject = et_my_weizhi.getText().toString().trim()+","+et_mudidi.getText().toString().trim();
        String url = FXConstant.URL_INSERT_OrderDetail;
        StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("数据更新成功")) {
                        if ("01".equals(biaoshi)) {
                            rechargefromZhFb(zongjia,orderId);
                        }else if ("02".equals(biaoshi)){
                            rechargefromWx(zongjia,orderId);
                        }else {
                            fukuan();
                        }
                    } else {
                        Toast.makeText(UOrderDetailThreeActivity.this, "编辑错误！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UOrderDetailThreeActivity.this, "验资失败！", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params1 = new HashMap<>();
                params1.put("orderId", orderId);
                params1.put("time4", time4);
                params1.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                params1.put("orderProject", orderProject);
                params1.put("orderSum", zongjia);
                if (!time.equals("")) {
                    params1.put("conventionTime", time);
                }
                return params1;
            }
        };
        MySingleton.getInstance(UOrderDetailThreeActivity.this).addToRequestQueue(request3);
    }
    private void rechargefromWx(String zongjia, String uId) {
        String chongzhiId=null;
        if (hxid.length()>12) {
            chongzhiId = DemoApplication.getInstance().getCurrentQiYeId();
        }else {
            chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        }
        try {
            chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        zongjia = (int)(Double.parseDouble(zongjia)*100)+"";
        final String mubiaoId = chongzhiId+"_"+"2"+"_"+uId;
        Toast.makeText(UOrderDetailThreeActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
        final String finalBalance = zongjia;
        StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("activity","UOrderDetailThreeActivity");
                    editor.commit();
                    JSONObject object = new JSONObject(s);
                    Log.e("chongzhiac,s", s);
                    String appid="",mch_id="",nonce_str="",sign="",prepayId="",timestamp="";
                    appid = object.getString("appid");
                    nonce_str = object.getString("noncestr");
                    mch_id = object.getString("partnerid");
                    prepayId = object.getString("prepayid");
                    timestamp = object.getString("timestamp");
                    sign = object.getString("sign");
                    PayReq req = new PayReq();
                    req.appId = appid;
                    req.partnerId = mch_id;
                    req.prepayId = prepayId;
                    req.packageValue = "Sign=WXPay";
                    req.nonceStr = nonce_str;
                    //这是得到一个时间戳(除以1000转化成秒数)
                    req.timeStamp = timestamp;
                    //调用获得签名的方法,这里直接把服务器返回来的sign给覆盖了,所以我不是很明白服务器为什么返回这个sign值,然后调起支付,基本上就可以了(我的反正是可以了....)
//                sign = OrderInfoUtil2_0.createSign(parameters);
                    Log.e("TAG", "timestamp=====" + req.timeStamp);
                    req.sign = sign;
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UOrderDetailThreeActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("body","正事多-订单支付");
                param.put("detail","正事多-订单支付");
                param.put("out_trade_no",getNowTime2());
                param.put("total_fee", finalBalance);
                param.put("spbill_create_ip",getHostIP());
                param.put("attach",mubiaoId);
                return param;
            }
        };
        MySingleton.getInstance(UOrderDetailThreeActivity.this).addToRequestQueue(request);
    }

    private String getNowTime2() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

    private String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;
    }

    private void rechargefromZhFb(final String balance,final String uId){
        String chongzhiId=null;
        if (hxid.length()>12) {
            chongzhiId = DemoApplication.getInstance().getCurrentQiYeId();
        }else {
            chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        }
        try {
            chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (balance!=null&&Double.parseDouble(balance)>0) {
            String url = FXConstant.URL_ZhiFu;
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX,balance,chongzhiId,"2",uId,null,"正事多-订单支付");
            final String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            final String orderinfo = OrderInfoUtil2_0.getSign(params);
            StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String sign = object.getString("sign");
                        sign = URLEncoder.encode(sign, "UTF-8");
                        final String orderInfo = orderParam + "&" + "sign=" + sign;
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(UOrderDetailThreeActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(UOrderDetailThreeActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(UOrderDetailThreeActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(UOrderDetailThreeActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
    }

    private void fukuan() {
        zongjia = et_zhifu_jine.getText().toString().trim();
        final double orderSu = Double.valueOf(zongjia);
        String url = FXConstant.URL_DingDan_Pay;
        StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("success")) {
                        if (pd!=null&&pd.isShowing()) {
                            pd.dismiss();
                        }
                        if (hxid.length()>12){
                            sendPushMessage(managerId,"002");
                        }else {
                            sendPushMessage(hxid,"000");
                        }
                        Toast.makeText(UOrderDetailThreeActivity.this, "付款成功,您已完成验资！", Toast.LENGTH_SHORT).show();
                    } else {
                        if (pd!=null&&pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(UOrderDetailThreeActivity.this, "付款失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (pd!=null&&pd.isShowing()) {
                    pd.dismiss();
                }
                Toast.makeText(UOrderDetailThreeActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", orderId);
                params.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("balance", zongjia);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailThreeActivity.this).addToRequestQueue(request1);
    }
    private void sendPushMessage(final String hxid1,final String type) {
        if ("002".equals(type)) {
            if (createTime!=null&&!"".equals(createTime)) {
                tongji();
            }
            duanxintongzhi(managerId,"【正事多】 通知:您有一条新的订单消息",type);
        }else {
            if (createTime!=null&&!"".equals(createTime)) {
                tongji();
            }
            duanxintongzhi(hxid1,"【正事多】 通知:您有一条新的订单消息",type);
        }
//        final String myId = DemoHelper.getInstance().getCurrentUsernName();
//        String url = FXConstant.URL_QUERY_INSTALL+hxid1;
//        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                try {
//                    if (s==null||"".equals(s)){
//                        Log.e("addfriac","设备信息为空");
//                        if ("002".equals(type)) {
//                            if (createTime!=null&&!"".equals(createTime)) {
//                                tongji();
//                            }
//                            duanxintongzhi(managerId,"【正事多】 通知:您有一条新的订单消息",type);
//                        }else {
//                            if (createTime!=null&&!"".equals(createTime)) {
//                                tongji();
//                            }
//                            duanxintongzhi(hxid1,"【正事多】 通知:您有一条新的订单消息",type);
//                        }
//                    }else {
//                        JSONObject object = new JSONObject(s);
//                        String deviceStr = object.getString("deviceStr");
//                        String deviceType = object.getString("deviceType");
//                        BmobQuery<MybmobInstallation> query1 = new BmobQuery<>();
//                        if ("ios".equals(deviceType)){
//                            query1.addWhereEqualTo("deviceToken",deviceStr);
//                            bmobPushManager.setQuery(query1);
//                        }else if ("android".equals(deviceType)){
//                            query1.addWhereEqualTo("installationId",deviceStr);
//                            bmobPushManager.setQuery(query1);
//                        }
//                        String str = "{\"aps\":{\"alert\":\"您有一条新的订单消息\",\"badge\":1,\"sound\":\"cheering.caf\"},\"info\":{\"companyAdress\":"+companyAdress+",\"companyId\":"+hxid1+",\"companyName\":"+companyName+",\"type\":\""+type+"\",\"userId\":\""+myId+"\"}}";
//                        JSONObject obj = null;
//                        try {
//                            obj = new JSONObject(str);
//                        } catch (JSONException e1) {
//                            e1.printStackTrace();
//                        }
//                        Log.e("uorderde,1","发送推送");
//                        bmobPushManager.pushMessage(obj, new PushListener() {
//                            @Override
//                            public void onSuccess() {
//                                if ("002".equals(type)) {
//                                    if (createTime!=null&&!"".equals(createTime)) {
//                                        tongji();
//                                    }
//                                    duanxintongzhi(managerId,"【正事多】 通知:您有一条新的订单消息",type);
//                                }else {
//                                    if (createTime!=null&&!"".equals(createTime)) {
//                                        tongji();
//                                    }
//                                    duanxintongzhi(hxid1,"【正事多】 通知:您有一条新的订单消息",type);
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(int i, String s) {
//                                if (s!=null){
//                                    Log.e("uorderde,e",s);
//                                }
//                                if ("002".equals(type)) {
//                                    if (createTime!=null&&!"".equals(createTime)) {
//                                        tongji();
//                                    }
//                                    duanxintongzhi(managerId,"【正事多】 通知:您有一条新的订单消息",type);
//                                }else {
//                                    if (createTime!=null&&!"".equals(createTime)) {
//                                        tongji();
//                                    }
//                                    duanxintongzhi(hxid1,"【正事多】 通知:您有一条新的订单消息",type);
//                                }
//                            }
//                        });
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//        queue.add(request);
    }
    private void duanxintongzhi(final String id, final String message,final String type) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                yuE = yuE - Double.valueOf(zongjia);
                DemoApplication.getApp().saveCurrentPrice(yuE);
                setResult(RESULT_OK, new Intent().putExtra("value", yuE));
                if ("002".equals(type)) {
                    startActivity(new Intent(UOrderDetailThreeActivity.this, ConsumeActivity.class).putExtra("biaoshi", "00"));
                }else {
                    startActivity(new Intent(UOrderDetailThreeActivity.this, ConsumeActivity.class).putExtra("biaoshi", "01"));
                }
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                yuE = yuE - Double.valueOf(zongjia);
                DemoApplication.getApp().saveCurrentPrice(yuE);
                setResult(RESULT_OK, new Intent().putExtra("value", yuE));
                if ("002".equals(type)) {
                    startActivity(new Intent(UOrderDetailThreeActivity.this, ConsumeActivity.class).putExtra("biaoshi", "00"));
                }else {
                    startActivity(new Intent(UOrderDetailThreeActivity.this, ConsumeActivity.class).putExtra("biaoshi", "01"));
                }
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message",message);
                param.put("telNum",id);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(UOrderDetailThreeActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void tongji() {
        String url = FXConstant.URL_TONGJI_XIAOLIANG;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("uorderDetailActivity","统计连接错误");
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("createTime", createTime);
                params.put("dynamicSeq", dynamicSeq);
                return params;
            }
        };
        MySingleton.getInstance(UOrderDetailThreeActivity.this).addToRequestQueue(request);
    }

    private void startSearch() {
        route = null;
        mBaiduMap.clear();
        // 处理搜索按钮响应
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withCityNameAndPlaceName(startCity, startNodeStr);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(endcity, endNodeStr);
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode).to(enNode));
        nowSearchType = 1;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mBaiduMap.hideInfoWindow();
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
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(UOrderDetailThreeActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            result.getSuggestAddrInfo();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            route = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            routeOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    @Override
    public void updateCurrentPrice(Object success) {
        yuE = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }


    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
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
        yuE = DemoApplication.getApp().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
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
