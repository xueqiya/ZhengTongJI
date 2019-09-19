package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.UserDetailsActivity;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuFLocationActivity;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.utils.ZhifuHelpUtils;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.FileUtils;
import com.sangu.apptongji.utils.WeiboDialogUtils;
import com.sangu.apptongji.widget.CircleImageView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewsOrderDetailActivity extends BaseActivity  implements IUAZView,IPriceView {

    private String dynamicSeq,createTime,floorPrice,task_label,task_position,task_locaName,dynamicContent,dynamicUserId,
            dynamicUname,contentImage,thridInfo,thridName,thridPhone,thridAdress,orderId;


    private TextView tv_taskLabel,tv_taskAdress,tv_content,tv_floorPrice,tv_position,tv_thridName,tv_thridPhone,tv_thridAdress;
    private ImageView image_1,image_2,image_3,image_4;
    private TextView tv_startTime,tv_endTime;

    private LinearLayout ll_image;


    //派单人用户信息相关
    private CircleImageView iv_head;
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

    private IUAZPresenter uazPresenter;



    //接单人用户信息相关
    private CircleImageView iv_headorder;
    private TextView tv_nameorder;
    private TextView tv_titlorder;
    private TextView tv_nianlingorder;
    private TextView tv_company_countorder;
    private TextView tv_companyorder;
    private TextView tv_distanceorder;
    private TextView tv_project_oneorder;
    private TextView tv_project_twoorder;
    private TextView tv_project_threeorder;
    private TextView tv_project_fourorder;
    private TextView tv_zy1_baoorder;
    private TextView tv_zy2_baoorder;
    private TextView tv_zy3_baoorder;
    private TextView tv_zy4_baoorder;
    private TextView tv_qianmingorder;
    private TextView iv_zy1_tupianorder;
    private TextView iv_zy2_tupianorder;
    private TextView iv_zy3_tupianorder;
    private TextView iv_zy4_tupianorder;
    private ImageView iv_sexorder;
    LinearLayout ll_oneorder,ll_twoorder,ll_threeorder,ll_fourorder;

    private String conId,merId;

    private int loadCount=0;

    private LinearLayout ll_thridInfo;
    private TextView tv_midBtn,tv_leftBtn,tv_rightBtn;

    private String orderState="0";
    private String beginTime,endTime;
    private EditText et_orderPrice,et_remark;
    private TextView tv_orderPrice,tv_orderPriceTitle,tv_finalPrice,tv_updatePrice;

    private RelativeLayout rl_merRef,rl_userRef,rl_midlayout;
    private TextView tv_userRef,tv_merRef,tv_uReson,tv_mReson,tv_remarkLabel;
    private String conName,merName,path="";
    private JSONObject json;

    private LinearLayout ll_sign1,ll_leftright;
    private ImageView iv_sign1,iv_sign2;
    private TextView tv_sign1,tv_sign1_time,tev_sign1;//左边签名区  签名时间  时间标题
    private TextView tv_sign2,tv_sign2_time,tev_sign2;//右边签名区  签名时间  时间标题

    private String lastSum="0";//付款的时候用到的价格

    //余额相关
    private String pass;
    private double yuE;
    private int errorTime=3;
    private static final int SDK_PAY_FLAG = 1;
    private IWXAPI api=null;
    private IPricePresenter pricePresenter;
    private Dialog mWeiboDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
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

                        //付款成功之后还要把签字信息上传上去 这样刚好恢复原来05的流程
                        UpdateOrderStateLast();


                      //  sendPushMessage(merId,"000");
                       // Toast.makeText(NewsOrderDetailActivity.this, "支付成功,您已完成验资", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(NewsOrderDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_newsorderdetail);

        pricePresenter = new PricePresenter(this,this);
        api = WXAPIFactory.createWXAPI(this,null);
        api.registerApp(Constant.APP_ID);
        pricePresenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());


        initView();

        initTopUserView();

        if (!orderState.equals("0")){
            initBtnTitleInfo();
        }

    }

    private void initBottomView(){

        iv_headorder = (CircleImageView) findViewById(R.id.iv_headorder);
        iv_sexorder = (ImageView) findViewById(R.id.iv_sexorder);
        tv_project_oneorder = (TextView) findViewById(R.id.tv_project_oneorder);
        tv_project_twoorder = (TextView) findViewById(R.id.tv_project_twoorder);
        tv_project_threeorder = (TextView) findViewById(R.id.tv_project_threeorder);
        tv_project_fourorder = (TextView) findViewById(R.id.tv_project_fourorder);
        tv_zy1_baoorder = (TextView) findViewById(R.id.tv_zy1_baoorder);
        tv_zy2_baoorder = (TextView) findViewById(R.id.tv_zy2_baoorder);
        tv_zy3_baoorder = (TextView) findViewById(R.id.tv_zy3_baoorder);
        tv_zy4_baoorder = (TextView) findViewById(R.id.tv_zy4_baoorder);
        tv_qianmingorder = (TextView) findViewById(R.id.tv_qianmingorder);
        iv_zy1_tupianorder = (TextView) findViewById(R.id.iv_zy1_tupianorder);
        iv_zy2_tupianorder = (TextView) findViewById(R.id.iv_zy2_tupianorder);
        iv_zy3_tupianorder = (TextView) findViewById(R.id.iv_zy3_tupianorder);
        iv_zy4_tupianorder = (TextView) findViewById(R.id.iv_zy4_tupianorder);
        tv_distanceorder = (TextView) findViewById(R.id.tv_distanceorder);
        tv_companyorder = (TextView) findViewById(R.id.tv_companyorder);
        tv_nianlingorder = (TextView) findViewById(R.id.tv_nianlingorder);
        tv_company_countorder = (TextView) findViewById(R.id.tv_company_countorder);
        tv_nameorder = (TextView) findViewById(R.id.tv_nameorder);
        tv_titlorder = (TextView) findViewById(R.id.tv_titlorder);
        ll_oneorder = (LinearLayout) findViewById(R.id.ll_oneorder);
        ll_twoorder = (LinearLayout) findViewById(R.id.ll_twoorder);
        ll_threeorder = (LinearLayout) findViewById(R.id.ll_threeorder);
        ll_fourorder = (LinearLayout) findViewById(R.id.ll_fourorder);

        uazPresenter = new UAZPresenter(this,this);

        if (merId != null){

            uazPresenter.loadThisDetail(merId);

        }else {

            uazPresenter.loadThisDetail(DemoHelper.getInstance().getCurrentUsernName());
        }

    }

    private void initTopUserView(){

        iv_head = (CircleImageView) findViewById(R.id.iv_head);
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
        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        ll_two = (LinearLayout) findViewById(R.id.ll_two);
        ll_three = (LinearLayout) findViewById(R.id.ll_three);
        ll_four = (LinearLayout) findViewById(R.id.ll_four);

        uazPresenter = new UAZPresenter(this,this);
        uazPresenter.loadThisDetail(dynamicUserId);



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

    @Override
    public void updateThisUser(final Userful allUser) {

        if (loadCount == 0){

            loadCount = 1;

            tv_name.setText(TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName());
            if (allUser.getName() != null){
                conName = allUser.getName();
            }
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

            iv_head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(NewsOrderDetailActivity.this, UserDetailsActivity.class);
                    intent.putExtra("hxid", allUser.getLoginId());
                    startActivity(intent);

                }
            });

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

            initBottomView();

        }else if (loadCount == 1){

            loadCount = 2;

            tv_nameorder.setText(TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName());
            if (allUser.getName() != null){
                merName = allUser.getName();
            }
            String nianLing = TextUtils.isEmpty(allUser.getuAge()) ? "27" : allUser.getuAge();
            tv_nianlingorder.setText(nianLing);
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
                tv_company_countorder.setVisibility(View.VISIBLE);
            }else {
                tv_company_countorder.setVisibility(View.INVISIBLE);
            }
            tv_company_countorder.setText("("+member+"人"+")");
            tv_companyorder.setText(company);
            tv_project_oneorder.setText(allUser.getUpName1());
            tv_project_twoorder.setText(allUser.getUpName2());
            tv_project_threeorder.setText(allUser.getUpName3());
            tv_project_fourorder.setText(allUser.getUpName4());
            String image1 = allUser.getZyImage1();
            String image2 = allUser.getZyImage2();
            String image3 = allUser.getZyImage3();
            String image4 = allUser.getZyImage4();
            String margan1 = allUser.getMargin1();
            String margan2 = allUser.getMargin2();
            String margan3 = allUser.getMargin3();
            String margan4 = allUser.getMargin4();
            if (allUser.getUpName1()==null||allUser.getUpName1().equals("")){
                ll_oneorder.setVisibility(View.GONE);
            }else {
                ll_oneorder.setVisibility(View.VISIBLE);
            }
            if (allUser.getUpName2()==null||allUser.getUpName2().equals("")){
                ll_twoorder.setVisibility(View.GONE);
            }else {
                ll_twoorder.setVisibility(View.VISIBLE);
            }
            if (allUser.getUpName3()==null||allUser.getUpName3().equals("")){
                ll_threeorder.setVisibility(View.GONE);
            }else {
                ll_threeorder.setVisibility(View.VISIBLE);
            }
            if (allUser.getUpName4()==null||allUser.getUpName4().equals("")){
                ll_fourorder.setVisibility(View.GONE);
            }else {
                ll_fourorder.setVisibility(View.VISIBLE);
            }
            if (image1!=null&&!"".equals(image1)) {
                iv_zy1_tupianorder.setVisibility(View.VISIBLE);
            }else {
                iv_zy1_tupianorder.setVisibility(View.GONE);
            }
            if (margan1!=null) {
                if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                    tv_zy1_baoorder.setVisibility(View.VISIBLE);
                }else {
                    tv_zy1_baoorder.setVisibility(View.GONE);
                }
            }
            if (image2!=null&&!"".equals(image2)) {
                iv_zy2_tupianorder.setVisibility(View.VISIBLE);
            }else {
                iv_zy2_tupianorder.setVisibility(View.GONE);
            }
            if (margan2!=null) {
                if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                    tv_zy2_baoorder.setVisibility(View.VISIBLE);
                }else {
                    tv_zy2_baoorder.setVisibility(View.GONE);
                }
            }
            if (image3!=null&&!"".equals(image3)) {
                iv_zy3_tupianorder.setVisibility(View.VISIBLE);
            }else {
                iv_zy3_tupianorder.setVisibility(View.GONE);
            }
            if (margan3!=null) {
                if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                    tv_zy3_baoorder.setVisibility(View.VISIBLE);
                }else {
                    tv_zy3_baoorder.setVisibility(View.GONE);
                }
            }
            if (image4!=null&&!"".equals(image4)) {
                iv_zy4_tupianorder.setVisibility(View.VISIBLE);
            }else {
                iv_zy4_tupianorder.setVisibility(View.GONE);
            }
            if (margan4!=null) {
                if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                    tv_zy4_baoorder.setVisibility(View.VISIBLE);
                }else {
                    tv_zy4_baoorder.setVisibility(View.GONE);
                }
            }
            String head = TextUtils.isEmpty(allUser.getImage()) ? "" : allUser.getImage();
            if (head.length() > 40) {
                tv_titlorder.setVisibility(View.INVISIBLE);
                iv_headorder.setVisibility(View.VISIBLE);
                String[] orderProjectArray = head.split("\\|");
                head = orderProjectArray[0];
            }
            if (!(head.equals("") || head.equals(null))) {
                tv_titlorder.setVisibility(View.INVISIBLE);
                iv_headorder.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+head,iv_headorder, DemoApplication.mOptions);
            } else {
                iv_headorder.setVisibility(View.INVISIBLE);
                tv_titlorder.setVisibility(View.VISIBLE);
                tv_titlorder.setText(TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName());
            }

            iv_headorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(NewsOrderDetailActivity.this, UserDetailsActivity.class);
                    intent.putExtra("hxid", allUser.getLoginId());
                    startActivity(intent);

                }
            });

            if (("00").equals(allUser.getSex())) {
                iv_sexorder.setImageResource(R.drawable.nv);
                //保 255 62 74  图 255 170 76
                iv_sexorder.setBackgroundColor(Color.rgb(234,121,219));
                tv_titlorder.setBackgroundResource(R.drawable.fx_bg_text_red);
            } else {
                iv_sexorder.setImageResource(R.drawable.nan);
                tv_titlorder.setBackgroundResource(R.drawable.fx_bg_text_gra);
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
                        tv_distanceorder.setText("隐藏");
                    }else {
                        tv_distanceorder.setText(str + "km");
                    }
                } else {
                    tv_distanceorder.setText("3km以外");
                }
            }else {
                tv_distanceorder.setText("3km以外");
            }
            String sign = allUser.getSignaTure();
            if (sign==null||"".equals(sign)){
                sign = "未设置简介";
            }
            tv_qianmingorder.setText(sign);

        }




    }

    private void initView(){


        dynamicSeq = getIntent().getStringExtra("dynamicSeq");
        createTime = getIntent().getStringExtra("createTime");
        task_label = getIntent().getStringExtra("task_label");
        task_position = getIntent().getStringExtra("task_position");
        dynamicContent =getIntent().getStringExtra("content");
        dynamicUname = getIntent().getStringExtra("uName");
        dynamicUserId = getIntent().getStringExtra("hxid");
        floorPrice = getIntent().getStringExtra("floorPrice");
        task_locaName = getIntent().getStringExtra("task_locaName");
        contentImage = getIntent().getStringExtra("contentImage");
        thridInfo = getIntent().getStringExtra("thridInfo");
        orderId = getIntent().getStringExtra("orderId");

        if (getIntent().getStringExtra("orderState") != null){

            orderState = getIntent().getStringExtra("orderState");
        }

        if (getIntent().getStringExtra("conId") != null){

            dynamicUserId = getIntent().getStringExtra("conId");
            conId = getIntent().getStringExtra("conId");

        }
        if (getIntent().getStringExtra("merId") != null){

            merId = getIntent().getStringExtra("merId");

        }

        if (conId == null){
            conId = dynamicUserId;
        }

        if (merId == null){
            merId = DemoHelper.getInstance().getCurrentUsernName();
        }


        ll_thridInfo = findViewById(R.id.ll_thridInfo);

        tv_taskLabel = (TextView) findViewById(R.id.tv_taskLabel);
        tv_taskAdress = (TextView) findViewById(R.id.tv_taskAdress);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_floorPrice = (TextView) findViewById(R.id.tv_floorPrice);
        tv_position = (TextView) findViewById(R.id.tv_position);
        tv_thridName = (TextView) findViewById(R.id.tv_thridName);
        tv_thridPhone = (TextView) findViewById(R.id.tv_thridPhone);
        tv_thridAdress = (TextView) findViewById(R.id.tv_thridAdress);

        tv_startTime = (TextView) findViewById(R.id.tv_startTime);
        tv_endTime = (TextView) findViewById(R.id.tv_endTime);

        ll_image = (LinearLayout) findViewById(R.id.ll_image);
        image_1 = (ImageView) findViewById(R.id.image_1);
        image_2 = (ImageView) findViewById(R.id.image_2);
        image_3 = (ImageView) findViewById(R.id.image_3);
        image_4 = (ImageView) findViewById(R.id.image_4);

        tv_thridName = (TextView) findViewById(R.id.tv_thridName);
        tv_thridPhone = (TextView) findViewById(R.id.tv_thridPhone);
        tv_thridAdress = (TextView) findViewById(R.id.tv_thridAdress);


        tv_midBtn = (TextView) findViewById(R.id.tv_midBtn);
        tv_leftBtn = (TextView) findViewById(R.id.tv_leftBtn);
        tv_rightBtn = (TextView) findViewById(R.id.tv_rightBtn);

        tv_orderPrice = (TextView) findViewById(R.id.tv_orderSum);
        et_orderPrice = (EditText) findViewById(R.id.et_orderPrice);
        tv_orderPriceTitle = (TextView) findViewById(R.id.tv_orderPriceTitle);
        tv_finalPrice = (TextView) findViewById(R.id.finalSum);
        tv_updatePrice = (TextView) findViewById(R.id.tv_updatePrice);
        et_remark = (EditText) findViewById(R.id.et_remark);
        tv_remarkLabel = (TextView) findViewById(R.id.tv_remarkLabel);


        //拒签相关
        rl_merRef = (RelativeLayout) findViewById(R.id.rl_merRef);
        rl_userRef = (RelativeLayout) findViewById(R.id.rl_userRef);

        tv_merRef = (TextView) findViewById(R.id.tv_merRef);
        tv_userRef = (TextView) findViewById(R.id.tv_userRef);
        tv_mReson = (TextView) findViewById(R.id.tv_mReson);
        tv_uReson = (TextView) findViewById(R.id.tv_uReson);


        //签名相关
        ll_sign1 = (LinearLayout) findViewById(R.id.ll_sign1);
        iv_sign1 = (ImageView) findViewById(R.id.iv_sign1);
        iv_sign2 = (ImageView) findViewById(R.id.iv_sign2);
        tv_sign1 = (TextView) findViewById(R.id.tv_sign1);
        tv_sign1_time = (TextView) findViewById(R.id.tv_sign1_time);
        tev_sign1 = (TextView) findViewById(R.id.tev_sign1);
        tv_sign2 = (TextView) findViewById(R.id.tv_sign2);
        tv_sign2_time = (TextView) findViewById(R.id.tv_sign2_time);
        tev_sign2 = (TextView) findViewById(R.id.tev_sign2);

        ll_leftright = (LinearLayout) findViewById(R.id.ll_leftright);
        rl_midlayout = (RelativeLayout) findViewById(R.id.rl_midlayout);

        String biaoshi = getIntent().getStringExtra("biaoshi");
        if (biaoshi != null && biaoshi.equals("03")){

            SetDynamicInfo();

        }





        //位置导航处理
        tv_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String lat = task_position.split("\\|")[0];
                final String lng = task_position.split("\\|")[1];

                String[] strLat = new String[]{lat};
                final String[] strLong = new String[]{lng};
                final String[] strLoginId = new String[]{conId};
                final String[] strName = new String[]{conName};
                final String[] strSex = new String[]{"01"};
                Intent intent = new Intent(NewsOrderDetailActivity.this, BaiDuFLocationActivity.class);
                intent.putExtra("lat", strLat);
                intent.putExtra("lng", strLong);
                intent.putExtra("loginId", strLoginId);
                intent.putExtra("name", strName);
                intent.putExtra("sex", strSex);
                intent.putExtra("biaoshi","导航");
                startActivity(intent);

            }

        });




        //中间按钮点击相关所有操作
        tv_midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderState.equals("0")){


                    if (!et_orderPrice.getText().toString().trim().equals("") && et_orderPrice.getText().toString().trim().length()>0 && Double.valueOf(et_orderPrice.getText().toString().trim()) > 0){

                    }else {
                        Toast.makeText(NewsOrderDetailActivity.this,"请输入您的报价",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //签名提交报价 这是第一种状态

                    Intent intent = new Intent(NewsOrderDetailActivity.this,QianMingActivity.class);

                    intent.putExtra("dynamicSeq",dynamicSeq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("task_label",task_label);
                    intent.putExtra("beginTime",beginTime);
                    intent.putExtra("endTime",endTime);
                    intent.putExtra("conId",dynamicUserId);
                    intent.putExtra("merId",DemoHelper.getInstance().getCurrentUsernName());
                    intent.putExtra("conName",conName);
                    intent.putExtra("merName",merName);
                    intent.putExtra("thridInfo",thridInfo);
                    intent.putExtra("orderSum",et_orderPrice.getText().toString().trim());
                    intent.putExtra("remark",et_remark.getText().toString().trim());
                    intent.putExtra("biaoshi","0");//0代表提交报价过去的签字
                    startActivityForResult(intent,0);

                }else if (orderState.equals("02")){


                    Intent intent = new Intent(NewsOrderDetailActivity.this,QianMingActivity.class);

                    intent.putExtra("dynamicSeq",dynamicSeq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("task_label",task_label);
                    intent.putExtra("beginTime",beginTime);
                    intent.putExtra("endTime",endTime);
                    intent.putExtra("conId",dynamicUserId);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("merId",merId);
                    intent.putExtra("conName",conName);
                    intent.putExtra("merName",merName);
                    intent.putExtra("thridInfo",thridInfo);
                    intent.putExtra("orderSum",et_orderPrice.getText().toString().trim());
                    intent.putExtra("remark",et_remark.getText().toString().trim());
                    intent.putExtra("biaoshi","1");//1代表同意报价跳转过去签字
                    startActivityForResult(intent,1);

                }else if (orderState.equals("03")){


                    if (thridInfo != null && !thridInfo.equals("")){

                        //第三方的 需要先填写验证码才能继续提交
                        //对比验证码是否正确 正确才继续操作

                        final EditText inputServer = new EditText(NewsOrderDetailActivity.this);
                        inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                        inputServer.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewsOrderDetailActivity.this);
                        builder.setTitle("输入签收码").setView(inputServer)
                                .setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                              //  inputServer.getText().toString().trim();

                                if (inputServer.getText().toString().trim().length() > 0 && json.getString("authCode") != null && inputServer.getText().toString().trim().equals(json.getString("authCode"))){

                                    Intent intent = new Intent(NewsOrderDetailActivity.this,QianMingActivity.class);

                                    intent.putExtra("dynamicSeq",dynamicSeq);
                                    intent.putExtra("createTime",createTime);
                                    intent.putExtra("task_label",task_label);
                                    intent.putExtra("beginTime",beginTime);
                                    intent.putExtra("endTime",endTime);
                                    intent.putExtra("conId",dynamicUserId);
                                    intent.putExtra("orderId",orderId);
                                    intent.putExtra("merId",merId);
                                    intent.putExtra("conName",conName);
                                    intent.putExtra("merName",merName);
                                    intent.putExtra("thridInfo",thridInfo);
                                    intent.putExtra("biaoshi","12");//12代表提交订单  恢复以前的流程
                                    startActivityForResult(intent,0);

                                }else {

                                    Toast.makeText(getApplicationContext(),"签收码错误",Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                        builder.show();


                    }else {

                        Intent intent = new Intent(NewsOrderDetailActivity.this,QianMingActivity.class);

                        intent.putExtra("dynamicSeq",dynamicSeq);
                        intent.putExtra("createTime",createTime);
                        intent.putExtra("task_label",task_label);
                        intent.putExtra("beginTime",beginTime);
                        intent.putExtra("endTime",endTime);
                        intent.putExtra("conId",dynamicUserId);
                        intent.putExtra("orderId",orderId);
                        intent.putExtra("merId",merId);
                        intent.putExtra("conName",conName);
                        intent.putExtra("merName",merName);
                        intent.putExtra("thridInfo",thridInfo);
                        intent.putExtra("biaoshi","12");//12代表提交订单  恢复以前的流程
                        startActivityForResult(intent,0);

                    }

                }else if (orderState.equals("08")){

                    //申请平台接入

                    //发短信 然后改变状态

                    final LayoutInflater inflater1 = LayoutInflater.from(NewsOrderDetailActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog collectionDialog = new AlertDialog.Builder(NewsOrderDetailActivity.this, R.style.Dialog).create();
                    collectionDialog.show();
                    collectionDialog.getWindow().setContentView(layout1);
                    collectionDialog.setCanceledOnTouchOutside(true);
                    collectionDialog.setCancelable(true);
                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                    title.setText("温馨提示");
                    btnOK1.setText("确定");
                    btnCancel1.setText("取消");
                    title_tv1.setText("是否确定申请平台介入？");


                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            collectionDialog.dismiss();
                        }
                    });

                    btnOK1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            collectionDialog.dismiss();

                            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(NewsOrderDetailActivity.this, "加载中...");


                            String url = FXConstant.URL_Order_Detail_update;
                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    WeiboDialogUtils.closeDialog(mWeiboDialog);

                                    JSONObject jsonObject = JSON.parseObject(s);

                                    String code = jsonObject.getString("code");

                                    if (code.equals("SUCCESS")){

                                        duanxintongzhi("18337101357","【正事多】用户"+DemoHelper.getInstance().getCurrentUsernName()+"申请平台介入订单","01");
                                        duanxintongzhi("13513895563","【正事多】用户"+DemoHelper.getInstance().getCurrentUsernName()+"申请平台介入订单","01");
                                        Toast.makeText(getApplicationContext(),"申请平台介入成功",Toast.LENGTH_SHORT).show();
                                        orderState = "09";

                                        initBtnTitleInfo();

                                    }else {

                                        Toast.makeText(getApplicationContext(),"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                                    Toast.makeText(getApplicationContext(),"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
                                }
                            }){
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<>();
                                    params.put("orderId",orderId);
                                    params.put("flg","06");
                                    params.put("state","09");
                                    return params;
                                }
                            };
                            MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request);

                        }
                    });

                }

            }
        });

        //左边按钮点击相关操作
        tv_leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderState.equals("04")){

                    //签收付款的时候需要先签字 然后回来当前页面进行支付

                    if (json.getString("finalSum") != null){

                        if (Double.valueOf(json.getString("finalSum")) == Double.valueOf(json.getString("orderSum"))){

                            final LayoutInflater inflater1 = LayoutInflater.from(NewsOrderDetailActivity.this);
                            RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                            final Dialog collectionDialog = new AlertDialog.Builder(NewsOrderDetailActivity.this, R.style.Dialog).create();
                            collectionDialog.show();
                            collectionDialog.getWindow().setContentView(layout1);
                            collectionDialog.setCanceledOnTouchOutside(true);
                            collectionDialog.setCancelable(true);
                            TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                            Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                            final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                            TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                            title.setText("温馨提示");
                            btnOK1.setText("确定");
                            btnCancel1.setText("取消");
                            title_tv1.setText("接单人修改了最终成交价，是否继续签收付款？");

                            btnCancel1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    collectionDialog.dismiss();
                                }
                            });

                            btnOK1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    collectionDialog.dismiss();

                                    Intent intent = new Intent(NewsOrderDetailActivity.this,QianMingActivity.class);

                                    intent.putExtra("biaoshi","2");//2代表签字之后就回来然后进行付款操作

                                    startActivityForResult(intent,2);

                                }
                            });

                        }else {

                            Intent intent = new Intent(NewsOrderDetailActivity.this,QianMingActivity.class);

                            intent.putExtra("biaoshi","2");//2代表签字之后就回来然后进行付款操作

                            startActivityForResult(intent,2);

                        }

                    }else {

                        Intent intent = new Intent(NewsOrderDetailActivity.this,QianMingActivity.class);

                        intent.putExtra("biaoshi","2");//2代表签字之后就回来然后进行付款操作

                        startActivityForResult(intent,2);

                    }


                }else if (orderState.equals("06"))
                {

                    final LayoutInflater inflater1 = LayoutInflater.from(NewsOrderDetailActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog collectionDialog = new AlertDialog.Builder(NewsOrderDetailActivity.this, R.style.Dialog).create();
                    collectionDialog.show();
                    collectionDialog.getWindow().setContentView(layout1);
                    collectionDialog.setCanceledOnTouchOutside(true);
                    collectionDialog.setCancelable(true);
                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                    title.setText("温馨提示");
                    btnOK1.setText("确定");
                    btnCancel1.setText("取消");
                    title_tv1.setText("是否确定同意用户的拒签请求？");


                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            collectionDialog.dismiss();
                        }
                    });

                    btnOK1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            collectionDialog.dismiss();

                            UpdateOrderDetailInfo();

                        }
                    });

                }

            }
        });

        //右边按钮点击相关操作
        tv_rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NewsOrderDetailActivity.this,NewsOrderRefuseActivity.class);

                intent.putExtra("orderId",orderId);

                if (orderState.equals("04")){

                    intent.putExtra("type","0"); //客户拒签

                }else {

                    intent.putExtra("type","1"); //接单人拒绝
                }

                startActivityForResult(intent,0);
                //要更新状态的

            }
        });


        tv_uReson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(NewsOrderDetailActivity.this,NewsOrderRefuseActivity.class);

                intent.putExtra("orderId",orderId);

                intent.putExtra("type","0");

                intent.putExtra("show","user");
                intent.putExtra("reson",json.getString("conReson"));
                intent.putExtra("resonImage",json.getString("conRefImage"));

                startActivity(intent);

            }
        });

        tv_mReson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(NewsOrderDetailActivity.this,NewsOrderRefuseActivity.class);

                intent.putExtra("orderId",orderId);

                intent.putExtra("type","1");

                intent.putExtra("show","mer");
                intent.putExtra("reson",json.getString("merReson"));
                intent.putExtra("resonImage",json.getString("merRefImage"));

                startActivity(intent);

            }
        });


        // 选择开始和结束时间
        tv_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(NewsOrderDetailActivity.this, DatePickActivity.class);
                intent3.putExtra("date", "");
                startActivityForResult(intent3, 10);
            }
        });

        // 选择开始和结束时间
        tv_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(NewsOrderDetailActivity.this, DatePickActivity.class);
                intent3.putExtra("date", "");
                startActivityForResult(intent3, 11);
            }
        });

        tv_updatePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText inputServer = new EditText(NewsOrderDetailActivity.this);
                inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                inputServer.setHint("输入成交价");
                inputServer.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                AlertDialog.Builder builder = new AlertDialog.Builder(NewsOrderDetailActivity.this);
                builder.setTitle("输入最终成交价").setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        inputServer.getText().toString().trim();

                        if (inputServer.getText().toString().trim().length() > 0){

                            UpdateFinalPrice(inputServer.getText().toString().trim());

                        }else {

                            Toast.makeText(getApplicationContext(),"请输入正确的价格",Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                builder.show();

                /*
                LayoutInflater inflaterD5 = LayoutInflater.from(NewsOrderDetailActivity.this);
                LinearLayout layout5 = (LinearLayout) inflaterD5.inflate(R.layout.dialog_updateprice, null);
                final Dialog dialog5 = new AlertDialog.Builder(NewsOrderDetailActivity.this, R.style.Dialog).create();
                dialog5.show();
                dialog5.getWindow().setContentView(layout5);
                WindowManager.LayoutParams params = dialog5.getWindow().getAttributes() ;
                Display display = NewsOrderDetailActivity.this.getWindowManager().getDefaultDisplay();
                params.width =(int) (display.getWidth()*0.75); //使用这种方式更改了dialog的框宽
                dialog5.getWindow().setAttributes(params);
                dialog5.setCancelable(true);
                dialog5.setCanceledOnTouchOutside(true);

                final EditText editText = (EditText)layout5.findViewById(R.id.et_input);
                TextView tv_cancle = (TextView)layout5.findViewById(R.id.tv_cancel);
                TextView tv_right = (TextView)layout5.findViewById(R.id.tv_right);
                dialog5.setOnShowListener(new DialogInterface.OnShowListener() {
                    public void onShow(DialogInterface dialog) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                });




                tv_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog5.dismiss();

                    }
                });

                tv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog5.dismiss();

                        //修改订单最终价格

                        if (editText.getText().toString().trim().length() > 0){

                            UpdateFinalPrice(editText.getText().toString().trim());

                        }else {

                            Toast.makeText(getApplicationContext(),"请输入正确的价格",Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                */

            }
        });


    }


    private void UpdateFinalPrice(final String price){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(NewsOrderDetailActivity.this, "加载中...");


        String url = FXConstant.URL_Order_Detail_update;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

                JSONObject jsonObject = JSON.parseObject(s);

                String code = jsonObject.getString("code");

                if (code.equals("SUCCESS")){

                    Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();

                    QueryOrderDetailInfo();

                }else {

                    Toast.makeText(getApplicationContext(),"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(getApplicationContext(),"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("orderId",orderId);
                params.put("finalSum",price);
                return params;
            }
        };
        MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request);


    }


    @Override
    protected void onStart() {
        super.onStart();

        if (orderId != null){

            QueryOrderDetailInfo();

        }

    }


    private void UpdateOrderDetailInfo(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(NewsOrderDetailActivity.this, "加载中...");


        String url = FXConstant.URL_Order_Detail_update;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

                JSONObject jsonObject = JSON.parseObject(s);

                String code = jsonObject.getString("code");

                if (code.equals("SUCCESS")){

                    Toast.makeText(getApplicationContext(),"操作成功",Toast.LENGTH_SHORT).show();
                    orderState = "07";

                    initBtnTitleInfo();
                    QueryOrderDetailInfo();

                }else {

                    Toast.makeText(getApplicationContext(),"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);
                Toast.makeText(getApplicationContext(),"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("orderId",orderId);
                params.put("flg","06");
                params.put("state","07");
                return params;
            }
        };
        MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request);

    }

    private void QueryOrderDetailInfo (){

        String url = FXConstant.URL_Order_Detail;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                JSONObject jsonObject = JSON.parseObject(s);

                JSONObject odi = jsonObject.getJSONObject("odi");

                json = odi;

                if (odi.getString("dynamicSeq") != null){

                    dynamicSeq = odi.getString("dynamicSeq");
                    createTime = odi.getString("create_time");

                    QueryDynamicInfo();

                }

                if (odi.getString("orderRemark") != null){

                    et_remark.setVisibility(View.GONE);
                    tv_remarkLabel.setVisibility(View.VISIBLE);
                    tv_remarkLabel.setText("备注：" + odi.getString("orderRemark"));

                }else {
                    et_remark.setVisibility(View.GONE);
                    tv_remarkLabel.setVisibility(View.VISIBLE);
                    tv_remarkLabel.setText("备注：无");
                }

                et_orderPrice.setVisibility(View.GONE);
                tv_orderPriceTitle.setVisibility(View.GONE);

                tv_orderPrice.setVisibility(View.VISIBLE);
                tv_orderPrice.setText("报价："+odi.getString("orderSum")+"元");

                if (orderState !=null && Double.valueOf(orderState) > 2){

                    if (odi.getString("finalSum") != null){

                        lastSum = odi.getString("finalSum");
                        tv_finalPrice.setVisibility(View.VISIBLE);
                        tv_finalPrice.setText("成交价："+odi.getString("finalSum")+"元");

                    }else {

                        lastSum = odi.getString("orderSum");
                        tv_finalPrice.setVisibility(View.VISIBLE);
                        tv_finalPrice.setText("成交价："+odi.getString("orderSum")+"元");

                    }

                }


                //加载右边签字相关
                if (odi.getString("mImage") != null){

                    tv_sign2.setText("");

                    ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN + odi.getString("mImage"),iv_sign2);

                    if (odi.getString("time1") != null){

                        tev_sign2.setVisibility(View.VISIBLE);
                        tev_sign2.setText("交单：");
                        tv_sign2_time.setVisibility(View.VISIBLE);
                        tv_sign2_time.setText(odi.getString("time1"));

                    }

                }

                //加载左边签字相关
                if (odi.getString("oImage") != null){

                    tv_sign1.setText("");

                    ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN + odi.getString("oImage"),iv_sign1);

                    if (odi.getString("resv4") != null){

                        tev_sign1.setVisibility(View.VISIBLE);
                        tev_sign1.setText("签收：");
                        tv_sign1_time.setVisibility(View.VISIBLE);
                        tv_sign1_time.setText(odi.getString("resv4"));

                    }

                }

                //加载开始时间跟结束时间
                if (odi.getString("beginTime") != null){

                    beginTime = odi.getString("beginTime");
                    String qingjiaQi = beginTime.substring(0, 4) + "-" + beginTime.substring(4, 6) + "-" + beginTime.substring(6, 8) + " "
                            + beginTime.substring(8, 10) + ":" + beginTime.substring(10, 12);
                    tv_startTime.setText("开始："+qingjiaQi);
                }

                if (odi.getString("endTime") != null){

                    endTime = odi.getString("endTime");
                    String qingjiaQi = endTime.substring(0, 4) + "-" + endTime.substring(4, 6) + "-" + endTime.substring(6, 8) + " "
                            + endTime.substring(8, 10) + ":" + endTime.substring(10, 12);
                    tv_endTime.setText("结束："+qingjiaQi);

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("orderId",orderId);
                return params;
            }
        };
        MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request);

    }

    //根据不同订单状态 来判断下方按钮改如何显示
    private void initBtnTitleInfo (){


        if (Double.valueOf(orderState) > 2){

            tv_startTime.setEnabled(false);
            tv_endTime.setEnabled(false);

        }


        if (orderState.equals("02")){

            if (merId.equals(DemoHelper.getInstance().getCurrentUsernName())){

                tv_midBtn.setEnabled(false);
                tv_midBtn.setText("等待客户同意");

            }else {

                tv_midBtn.setText("(先确认)同意报价签合约");

            }

        }else if (orderState.equals("03"))
        {

            ll_sign1.setVisibility(View.VISIBLE);
            if (merId.equals(DemoHelper.getInstance().getCurrentUsernName())){

                tv_updatePrice.setVisibility(View.VISIBLE);
                tv_midBtn.setText("提交订单");

            }else {

                tv_midBtn.setEnabled(false);
                tv_midBtn.setText("等待师傅提交订单");

            }

        }else if (orderState.equals("04"))
        {
            ll_sign1.setVisibility(View.VISIBLE);
            tv_updatePrice.setText("");

            if (merId.equals(DemoHelper.getInstance().getCurrentUsernName())){

                tv_midBtn.setEnabled(false);
                tv_midBtn.setText("等待客户签收付款");

            }else {

                rl_midlayout.setVisibility(View.GONE);
                ll_leftright.setVisibility(View.VISIBLE);
            }

        }else if (orderState.equals("05"))
        {
            ll_sign1.setVisibility(View.VISIBLE);
            tv_updatePrice.setText("");


            rl_midlayout.setVisibility(View.VISIBLE);
            ll_leftright.setVisibility(View.GONE);

            tv_midBtn.setEnabled(false);
            tv_midBtn.setBackgroundResource(R.color.light_grey);
            tv_midBtn.setText("交易完成");

        }else if (orderState.equals("06"))
        {
            ll_sign1.setVisibility(View.GONE);
            tv_updatePrice.setText("");

            //客户拒绝签收
            rl_userRef.setVisibility(View.VISIBLE);


            if (merId.equals(DemoHelper.getInstance().getCurrentUsernName())){

                rl_midlayout.setVisibility(View.GONE);
                ll_leftright.setVisibility(View.VISIBLE);

                tv_leftBtn.setText("同意");
                tv_rightBtn.setText("拒绝");

            }else {

                rl_midlayout.setVisibility(View.VISIBLE);
                ll_leftright.setVisibility(View.GONE);

                tv_midBtn.setEnabled(false);
                tv_midBtn.setText("等待接单人操作");

            }

        }else if (orderState.equals("07"))
        {

            ll_sign1.setVisibility(View.GONE);
            tv_updatePrice.setText("");


            rl_midlayout.setVisibility(View.VISIBLE);
            ll_leftright.setVisibility(View.GONE);

            tv_midBtn.setEnabled(false);
            tv_midBtn.setBackgroundResource(R.color.light_grey);
            tv_midBtn.setText("交易结束");

            //客户拒绝签收
            rl_userRef.setVisibility(View.VISIBLE);


            //接单人同意
            rl_merRef.setVisibility(View.VISIBLE);
            tv_merRef.setText("接单人同意");
            tv_mReson.setText("");

        }else if (orderState.equals("08"))
        {
            ll_sign1.setVisibility(View.GONE);
            tv_updatePrice.setText("");


            rl_midlayout.setVisibility(View.VISIBLE);
            ll_leftright.setVisibility(View.GONE);

            tv_midBtn.setEnabled(true);
            tv_midBtn.setText("申请平台介入");

            //客户拒绝签收
            rl_userRef.setVisibility(View.VISIBLE);


            //接单人同意
            rl_merRef.setVisibility(View.VISIBLE);
            tv_merRef.setText("接单人拒绝");

        }else if (orderState.equals("09")) {

            ll_sign1.setVisibility(View.VISIBLE);
            tv_updatePrice.setText("");


            rl_midlayout.setVisibility(View.VISIBLE);
            ll_leftright.setVisibility(View.GONE);

            tv_midBtn.setEnabled(false);
            tv_midBtn.setText("平台介入中");

        }

    }

    private void SetDynamicInfo(){

        tv_taskLabel.setText("标题："+task_label);
        tv_taskAdress.setText("坐标："+task_locaName);
        tv_content.setText("详情："+dynamicContent);

        if (floorPrice != null && Double.valueOf(floorPrice) > 0){

            tv_floorPrice.setText("出价："+floorPrice+"元");

        }else {

            tv_floorPrice.setVisibility(View.INVISIBLE);
        }

        //动态内容图片
        if (contentImage != null && !contentImage.equals("")){

            ll_image.setVisibility(View.VISIBLE);
            String[] images = contentImage.split("\\|");

            image_1.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[0],image_1, DemoApplication.mOptions2);
            // ((ViewHolderOnezhf) holder).image_1.setOnClickListener(new ImageListener(images, 0,rel_time, dynamicSeq,sID));

            if (images.length > 1){

                image_2.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[1],image_2, DemoApplication.mOptions2);


            }else if (images.length > 2)
            {
                image_3.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[2],image_3, DemoApplication.mOptions2);

            }else if (images.length > 3)
            {
                image_4.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_SOCIAL_PHOTO+images[3],image_4, DemoApplication.mOptions2);

            }

        }

        //第三方客户信息
        if (thridInfo != null && !thridInfo.equals("")){

            String[] strings = thridInfo.split("\\|");

            if (strings.length == 3){

                ll_thridInfo.setVisibility(View.VISIBLE);

                tv_thridName.setText("姓名："+strings[0]);
                tv_thridPhone.setText("电话："+strings[1]);
                tv_thridAdress.setText("地址："+strings[2]);

            }

        }

    }

    private void QueryDynamicInfo(){

        String url = FXConstant.URL_PUBLISHDETAIL_QUERY;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                JSONArray array = object.getJSONArray("clist");
                if (array==null||array.size()==0){
                    ToastUtils.showNOrmalToast(getApplicationContext(),"网络不稳定...");
                    return;
                }

                JSONObject json = array.getJSONObject(0);


                task_label = json.getString("task_label");
                task_position = json.getString("task_position");
                dynamicContent = json.getString("content");
                dynamicUname = json.getString("uName");
                dynamicUserId = json.getString("uId");
                floorPrice = json.getString("floorPrice");
                task_locaName = json.getString("task_locaName");
                contentImage = json.getString("image1");
                thridInfo = json.getString("thridInfo");


                SetDynamicInfo();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "网络不稳定,请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("dynamicSeq",dynamicSeq);
                param.put("createTime",createTime);
                //myuserID.equals(sID)
                if (DemoHelper.getInstance().isLoggedIn(NewsOrderDetailActivity.this)) {
                    param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                }
                return param;
            }
        };

        MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 0:
                    if (data!=null){

                        orderState = data.getStringExtra("orderState");

                        initBtnTitleInfo();

                    }
                    break;
                case 1:
                    if (data!=null){

                        orderState = data.getStringExtra("orderState");

                        initBtnTitleInfo();

                    }
                    break;
                case 2:
                    if (data!=null){

                       //客户签收的时候回来加载签名 然后提交
                        path = data.getStringExtra("xdimageName");
                        tv_sign1.setText("");
                        iv_sign1.setImageURI(Uri.fromFile(new File(path)));

                        if (json != null){

                            lastSum = json.getString("orderSum");

                        }

                        if (json != null && json.getString("finalSum") != null){

                            lastSum = json.getString("finalSum");

                        }


                        final LayoutInflater inflater1 = LayoutInflater.from(NewsOrderDetailActivity.this);
                        RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                        final Dialog collectionDialog = new AlertDialog.Builder(NewsOrderDetailActivity.this, R.style.Dialog).create();
                        collectionDialog.show();
                        collectionDialog.getWindow().setContentView(layout1);
                        collectionDialog.setCanceledOnTouchOutside(true);
                        collectionDialog.setCancelable(true);
                        TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                        Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                        final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                        TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                        title.setText("温馨提示");
                        btnOK1.setText("确定");
                        btnCancel1.setText("取消");
                        title_tv1.setText("点击确定签收付款");


                        btnCancel1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                collectionDialog.dismiss();
                            }
                        });

                        btnOK1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                collectionDialog.dismiss();

                                LayoutInflater inflaterDl = LayoutInflater.from(NewsOrderDetailActivity.this);
                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                                final Dialog dialog = new AlertDialog.Builder(NewsOrderDetailActivity.this,R.style.Dialog).create();
                                dialog.show();
                                dialog.getWindow().setContentView(layout);
                                dialog.setCanceledOnTouchOutside(true);
                                RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                                RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                                RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
                                RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
                                re_item5.setVisibility(View.VISIBLE);
                                re_item2.setVisibility(View.GONE);
                                TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                                TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
                                TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
                                TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
                                tv_title.setText("请选择支付方式");
                                tv_item1.setText("余额支付(微信)");
                                tv_item2.setText("微 信 充 值");
                                tv_item5.setText("支付宝支付");
                                re_item1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.dismiss();

                                        zhifu3(lastSum);
                                    }
                                });
                                re_item2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();

                                        Intent intent2 = new Intent(NewsOrderDetailActivity.this, ChongZhiActivity.class);
                                        intent2.putExtra("papass",pass);
                                        intent2.putExtra("price",yuE);
                                        intent2.putExtra("biaoshi","00");
                                        startActivityForResult(intent2,100);

                                    }
                                });
                                re_item5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        rechargefromZhFb(lastSum,orderId);
                                    }
                                });
                                re_item3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                            }
                        });

                    }
                    break;

                case 10:
                    if (data!=null){
                        String qingjiaQi = data.getStringExtra("date");
                        beginTime = qingjiaQi+"00";

                        qingjiaQi = qingjiaQi.substring(0, 4) + "-" + qingjiaQi.substring(4, 6) + "-" + qingjiaQi.substring(6, 8) + " "
                                + qingjiaQi.substring(8, 10) + ":" + qingjiaQi.substring(10, 12);
                        tv_startTime.setText("开始："+qingjiaQi);
                    }
                    break;

                case 11:
                    if (data!=null){
                        String qingjiaQi = data.getStringExtra("date");
                        endTime = qingjiaQi+"00";
                        qingjiaQi = qingjiaQi.substring(0, 4) + "-" + qingjiaQi.substring(4, 6) + "-" + qingjiaQi.substring(6, 8) + " "
                                + qingjiaQi.substring(8, 10) + ":" + qingjiaQi.substring(10, 12);
                        tv_endTime.setText("结束："+qingjiaQi);
                    }
                    break;
            }
        }

    }


    private void zhifu3(String sum) {
        if (pass==null||"".equals(pass)){
            ZhifuHelpUtils.showErrorMiMaSHZH(this,pass,"000");
            return;
        }
        if (pass.length()!=6||!ZhifuHelpUtils.isNumeric(pass)){
            ZhifuHelpUtils.showErrorMiMaXG(this,pass,"000");
            return;
        }
        if (errorTime<=0){
            ZhifuHelpUtils.showErrorLing(this);
            return;
        }
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
            final double orderSu = Double.valueOf(sum);
            Double d = yuE - orderSu;
            if (d >= 0) {
                LayoutInflater inflaterDl = LayoutInflater.from(NewsOrderDetailActivity.this);
                final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
                final Dialog dialog = new AlertDialog.Builder(NewsOrderDetailActivity.this,R.style.Dialog).create();
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
                        final ProgressDialog pd = new ProgressDialog(NewsOrderDetailActivity.this);
                        pd.setMessage("正在支付...");
                        pd.setCanceledOnTouchOutside(false);
                        pd.show();
                        if (pwdView.getStrPassword().equals(pass)) {
                            dialog.dismiss();
                            fukuan();
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
                                ZhifuHelpUtils.showErrorLing(NewsOrderDetailActivity.this);
                            }else {
                                ZhifuHelpUtils.showErrorTishi(NewsOrderDetailActivity.this,times + "",null,"000");
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
                        startActivity(new Intent(NewsOrderDetailActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
                    }
                });
            } else {
                LayoutInflater inflaterDl = LayoutInflater.from(NewsOrderDetailActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
                final Dialog dialog = new AlertDialog.Builder(NewsOrderDetailActivity.this,R.style.Dialog).create();
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
                        rechargefromWx(lastSum,orderId);
                    }
                });
                re_item5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        rechargefromZhFb(lastSum,orderId);
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
            ToastUtils.showNOrmalToast(NewsOrderDetailActivity.this, "您的账户已被冻结");
        }
    }

    private void UpdateOrderStateLast(){

        List<Param> params = new ArrayList<Param>();

        String time2 = getNowTime2();
        time2 = time2.substring(0, 4) + "-" + time2.substring(4, 6) + "-" + time2.substring(6, 8) + " "
                + time2.substring(8, 10) + ":" + time2.substring(10, 12);

        params.add(new Param("balance", lastSum));
        params.add(new Param("resv4", time2));
        params.add(new Param("orderId", orderId));
        params.add(new Param("merId", merId));
        params.add(new Param("orderState", "05"));
        String str = "o_image";
        String url = FXConstant.URL_Update_OrderState;

        final List<File> files = new ArrayList<File>();
        File file = new File(path);
        files.add(file);

        OkHttpManager.getInstance().post(str, params, files, url, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                Log.e("qianming,111",jsonObject.toString());

                if (dynamicSeq!=null&&!"".equals(dynamicSeq)){
                    ChangeState(dynamicSeq,createTime);
                }
                reduceUscount(0);

                if (dynamicSeq != null){


                  //  duanxintongzhi(merId,"【正事多】您接的派单客户已签收，交易资金可在余额里查看，10天后全款自动进入账号（平台不抽成）自觉保证承诺服务，以免影响资金到账","");

                }

                Toast.makeText(NewsOrderDetailActivity.this, "签收成功！", Toast.LENGTH_SHORT).show();

                FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/qianming/"));
                finish();

            }

            @Override
            public void onFailure(String errorMsg) {

                Toast.makeText(NewsOrderDetailActivity.this, "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void duanxintongzhi(final String id, final String message,final String type) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

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

    }

    private void reduceUscount(final int type) {
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
                if (type==1){
                    param.put("orderUnReadCount","-1");
                }else {
                    param.put("cusmUnReadCount", "-1");
                }
                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request);
    }

    private void ChangeState(final String dynamicSeq, final String createTime) {
        String url =FXConstant.URL_TONGJI_LIULANCISHU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("dynamicActivity","成功,s="+s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dynamicActivity","失败,e="+volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("createTime",createTime);
                param.put("dynamicSeq",dynamicSeq);
                param.put("orderState","02");
                return param;
            }
        };
        MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request);
    }


    private void fukuan() {
        String url = FXConstant.URL_DingDan_Pay;
        StringRequest request1 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = JSON.parseObject(s);
                    String code = object.getString("code");
                    if (code.equals("success")) {
                      //  sendPushMessage(merId,"000");
                      //  Toast.makeText(NewsOrderDetailActivity.this, "付款成功,您已完成验资！", Toast.LENGTH_SHORT).show();

                        UpdateOrderStateLast();

                        //付款成功之后还要把签字信息上传上去 这样刚好恢复原来05的流程



                    } else {
                        Toast.makeText(NewsOrderDetailActivity.this, "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                } catch (com.alibaba.fastjson.JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(NewsOrderDetailActivity.this, "网络不稳定,请稍后重试", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", orderId);
                params.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("balance", lastSum);
                params.put("orderState", "04");
                return params;
            }
        };
        if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("00")) {
            MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request1);

        } else {
            ToastUtils.showNOrmalToast(NewsOrderDetailActivity.this, "您的账户已被冻结");
        }
    }

    private void rechargefromWx(String zongjia, String uId) {
        String chongzhiId=null;
        chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        zongjia = (int)(Double.parseDouble(zongjia)*100)+"";
        if (Double.parseDouble(zongjia)>0) {
            final String mubiaoId = chongzhiId + "_" + "10" + "_" + uId;
            Toast.makeText(NewsOrderDetailActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
            String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
            final String finalBalance = zongjia;
            StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSharedPreferences.edit();
                        editor.putString("activity", "NewsOrderDetailActivity");
                        editor.commit();
                        org.json.JSONObject object = new org.json.JSONObject(s);
                        Log.e("chongzhiac,s", s);
                        String appid = "", mch_id = "", nonce_str = "", sign = "", prepayId = "", timestamp = "";
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
                    Toast.makeText(NewsOrderDetailActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("body", "正事多-订单支付");
                    param.put("detail", "正事多-订单支付");
                    param.put("out_trade_no", getNowTime2());
                    param.put("total_fee", finalBalance);
                    param.put("spbill_create_ip", getHostIP());
                    param.put("attach", mubiaoId);
                    return param;
                }
            };
            MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(NewsOrderDetailActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
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

    protected void rechargefromZhFb(final String balance, final String uId){
        String chongzhiId=null;
        chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
        try {
            chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (balance!=null&&Double.parseDouble(balance)>0) {
            String url = FXConstant.URL_ZhiFu;
            Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX,balance,chongzhiId,"10",uId,null,"正事多-订单支付");
            final String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
            final String orderinfo = OrderInfoUtil2_0.getSign(params);
            StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        org.json.JSONObject object = new org.json.JSONObject(s);
                        String sign = object.getString("sign");
                        sign = URLEncoder.encode(sign, "UTF-8");
                        final String orderInfo = orderParam + "&" + "sign=" + sign;
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(NewsOrderDetailActivity.this);
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
                    Toast.makeText(NewsOrderDetailActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> param = new HashMap<>();
                    param.put("orderInfo",orderinfo);
                    return param;
                }
            };
            MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request);
        }else {
            Toast.makeText(NewsOrderDetailActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
        }
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
        MySingleton.getInstance(NewsOrderDetailActivity.this).addToRequestQueue(request3);
    }


    @Override
    public void updateCurrentPrice(Object success) {
        yuE = DemoApplication.getInstance().getCurrenPrice();
        pass = DemoApplication.getInstance().getCurrentPayPass();
        com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
        errorTime = object.getInteger("enterErrorTimes");
    }



}
