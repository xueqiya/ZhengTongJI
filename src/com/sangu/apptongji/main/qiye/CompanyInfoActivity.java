package com.sangu.apptongji.main.qiye;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.ZYDetailActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.BZJJNActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.BZJZJActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.ChongZhiActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.DingdanActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.HongbaoDetailListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.TiXianActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.YuEActivity;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Administrator on 2016-12-28.
 */

public class CompanyInfoActivity extends BaseActivity implements IPriceView,IQiYeDetailView,View.OnClickListener{
    private SwipeRefreshLayout mRefreshLayout;
    private ImageView iv_avatar;
    private RelativeLayout re_myinfo;
    private RelativeLayout re_chongzhi;
    private RelativeLayout re_yuE;
    private RelativeLayout re_tixian;
    private RelativeLayout re_qiye_kaoqin;
    private RelativeLayout re_qiye_shxban;
    private RelativeLayout re_my_dingdan;
    private RelativeLayout re_my_xiaofei;
    private RelativeLayout re_yuangong_qingjia;
    private RelativeLayout re_qiye_paidan;
    private RelativeLayout re_qiye_setting;
    private RelativeLayout re_hetong;
    private RelativeLayout rl_zhuanfa;
    private RelativeLayout re_baogao;
    private TextView tv_name;
    private TextView tv_jianjie;
    private TextView tv_zy1;
    private TextView tv_zy2;
    private TextView tv_zy3;
    private TextView tv_zy4;
    private TextView zy1_baozj;
    private TextView zy2_baozj;
    private TextView zy3_baozj;
    private TextView zy4_baozj;
    private TextView zy1_jiedancishu;
    private TextView zy2_jiedancishu;
    private TextView zy3_jiedancishu;
    private TextView zy4_jiedancishu;
    private TextView zy1_jine;
    private TextView zy2_jine;
    private TextView zy3_jine;
    private TextView zy4_jine;
    private TextView all_jiedancishu;
    private TextView all_xiaofeicishu;
    private TextView qiye_faren;
    private TextView tv_company_zhizhao;
    private TextView tv_qiye_phone;
    private TextView tv_company_address;
    private TextView tv_unread_ruzhang;
    private TextView unread_qingjia_number;
    private TextView unread_baobiao_number;
    private TextView tv_unread_chuzhang;
    private TextView tv_chongzhijine,tv_jine;
    private TextView tv_titl;
    private TextView tv_tixianjine;
    private TextView lingqian;
    private Button btn_zhy1;
    private Button btn_zhy2;
    private Button btn_zhy3;
    private Button btn_zhy4;
    private Handler myhandler = new Handler(){};
    private IQiYeInfoPresenter presenter;
    private IPricePresenter pricePresenter;
    private String decribe1="",decribe2="",decribe3="",decribe4="",margen1="",margen2="",margen3="",margen4="";
    private String image1="",resv31="",ZY1="";
    private String image2="",resv32="",ZY2="";
    private String image3="",resv33="",ZY3="";
    private String image4="",resv34="",ZY4="";
    private String pass="",price="",baozhengjin,recruitBody=null,recruitImages=null,fxUpName
            ,joinImages=null,joinBody=null,recruitMoney,joinMoney,friendsNumber,shareRed,managerId,memberNum;
    private double yuE;
//    private String valueM,valueU;
    private String remark1="",remark2="",remark3="",remark4="",create1="",create2="",create3="",create4="",
        cishu1,cishu2,cishu3,cishu4,margin_time1,margin_time2,margin_time3,margin_time4;
    private String liulancishu1="",liulancishu2="",liulancishu3="",liulancishu4="",upId1="",upId2="",upId3="",upId4="";
    private String qiyeId,resv6="",loginTime,companyName,companyShortName="0",comImage,comSignature,comTel,comEmail,comAddress,resv1,resv2,resv3;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_qiye);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swr_layout);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        re_myinfo = (RelativeLayout) findViewById(R.id.re_myinfo);
        re_chongzhi = (RelativeLayout) findViewById(R.id.re_chongzhi);
        re_yuE = (RelativeLayout) findViewById(R.id.re_yuE);
        re_tixian = (RelativeLayout) findViewById(R.id.re_tixian);
        re_qiye_kaoqin = (RelativeLayout) findViewById(R.id.re_qiye_kaoqin);
        re_qiye_shxban = (RelativeLayout) findViewById(R.id.re_qiye_shxban);
        re_my_dingdan = (RelativeLayout) findViewById(R.id.re_my_dingdan);
        re_my_xiaofei = (RelativeLayout) findViewById(R.id.re_my_xiaofei);
        re_yuangong_qingjia = (RelativeLayout) findViewById(R.id.re_yuangong_qingjia);
        re_qiye_paidan = (RelativeLayout) findViewById(R.id.re_qiye_paidan);
        re_qiye_setting = (RelativeLayout) findViewById(R.id.re_qiye_setting);
        re_hetong = (RelativeLayout) findViewById(R.id.re_hetong);
        rl_zhuanfa = (RelativeLayout) findViewById(R.id.rl_zhuanfa);
        re_baogao = (RelativeLayout) findViewById(R.id.re_baogao);
        tv_jine = (TextView) findViewById(R.id.tv_jine);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_jianjie = (TextView) findViewById(R.id.tv_jianjie);
        tv_zy1 = (TextView) findViewById(R.id.tv_zy1);
        tv_zy2 = (TextView) findViewById(R.id.tv_zy2);
        tv_zy3 = (TextView) findViewById(R.id.tv_zy3);
        tv_zy4 = (TextView) findViewById(R.id.tv_zy4);
        zy1_baozj = (TextView) findViewById(R.id.zy1_baozj);
        zy2_baozj = (TextView) findViewById(R.id.zy2_baozj);
        zy3_baozj = (TextView) findViewById(R.id.zy3_baozj);
        zy4_baozj = (TextView) findViewById(R.id.zy4_baozj);
        zy1_jiedancishu = (TextView) findViewById(R.id.zy1_jiedancishu);
        zy2_jiedancishu = (TextView) findViewById(R.id.zy2_jiedancishu);
        zy3_jiedancishu = (TextView) findViewById(R.id.zy3_jiedancishu);
        zy4_jiedancishu = (TextView) findViewById(R.id.zy4_jiedancishu);
        zy1_jine = (TextView) findViewById(R.id.zy1_jine);
        zy2_jine = (TextView) findViewById(R.id.zy2_jine);
        zy3_jine = (TextView) findViewById(R.id.zy3_jine);
        zy4_jine = (TextView) findViewById(R.id.zy4_jine);
        all_jiedancishu = (TextView) findViewById(R.id.all_jiedancishu);
        all_xiaofeicishu = (TextView) findViewById(R.id.all_xiaofeicishu);
        qiye_faren = (TextView) findViewById(R.id.qiye_faren);
        tv_company_zhizhao = (TextView) findViewById(R.id.tv_company_zhizhao);
        tv_qiye_phone = (TextView) findViewById(R.id.tv_qiye_phone);
        tv_company_address = (TextView) findViewById(R.id.tv_company_address);
        tv_unread_ruzhang = (TextView) findViewById(R.id.tv_unread_ruzhang);
        unread_qingjia_number = (TextView) findViewById(R.id.unread_qingjia_number);
        unread_baobiao_number = (TextView) findViewById(R.id.unread_baobiao_number);
        tv_unread_chuzhang = (TextView) findViewById(R.id.tv_unread_chuzhang);
        tv_chongzhijine = (TextView) findViewById(R.id.tv_chongzhijine);
        tv_titl = (TextView) findViewById(R.id.tv_titl);
        tv_tixianjine = (TextView) findViewById(R.id.tv_tixianjine);
        lingqian = (TextView) findViewById(R.id.lingqian);
        btn_zhy1 = (Button) findViewById(R.id.btn_zhy1);
        btn_zhy2 = (Button) findViewById(R.id.btn_zhy2);
        btn_zhy3 = (Button) findViewById(R.id.btn_zhy3);
        btn_zhy4 = (Button) findViewById(R.id.btn_zhy4);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        presenter = new QiYeInfoPresenter(this,this);
        pricePresenter = new PricePresenter(this,this);
        qiyeId = getIntent().getStringExtra("resv5");
        baozhengjin = getIntent().getStringExtra("baozhengjin");
        Log.e("companyInfo,qiyeId",qiyeId);
        presenter.loadQiYeInfo(qiyeId);
        pricePresenter.updatePriceData(qiyeId);
        initView();
        mRefreshLayout.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_light, android.R.color.holo_green_light);
        iv_avatar.setOnClickListener(this);
        zy1_baozj.setOnClickListener(this);
        zy2_baozj.setOnClickListener(this);
        zy3_baozj.setOnClickListener(this);
        zy4_baozj.setOnClickListener(this);
        btn_zhy4.setOnClickListener(this);
        btn_zhy3.setOnClickListener(this);
        btn_zhy2.setOnClickListener(this);
        btn_zhy1.setOnClickListener(this);
        re_myinfo.setOnClickListener(this);
        re_chongzhi.setOnClickListener(this);
        re_yuE.setOnClickListener(this);
        re_tixian.setOnClickListener(this);
        re_qiye_shxban.setOnClickListener(this);
        re_qiye_kaoqin.setOnClickListener(this);
        re_my_dingdan.setOnClickListener(this);
        re_my_xiaofei.setOnClickListener(this);
        re_yuangong_qingjia.setOnClickListener(this);
        re_qiye_paidan.setOnClickListener(this);
        re_qiye_setting.setOnClickListener(this);
        re_hetong.setOnClickListener(this);
        re_baogao.setOnClickListener(this);
        rl_zhuanfa.setOnClickListener(this);
    }

    private void initView() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadQiYeInfo(qiyeId);
                pricePresenter.updatePriceData(qiyeId);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryUnreadCount();
        pricePresenter.updatePriceData(qiyeId);
    }

    private void queryUnreadCount() {
        String url = FXConstant.URL_QUERY_UNREADQIYE+ qiyeId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    if (s==null||"".equals(s)){
                        Log.e("dingdanac","offSendOrderCount为空");
                    }else {
                        JSONObject object = new JSONObject(s);
                        int qjCount = object.getInt("leaveCount");
                        int bbCount = object.getInt("reportCount");
                        int ddCount = object.getInt("orderCount");
                        if (qjCount>0){
                            unread_qingjia_number.setVisibility(View.VISIBLE);
                            unread_qingjia_number.setText(String.valueOf(qjCount));
                        }else {
                            unread_qingjia_number.setVisibility(View.INVISIBLE);
                        }
                        if (bbCount>0){
                            unread_baobiao_number.setVisibility(View.VISIBLE);
                            unread_baobiao_number.setText(String.valueOf(bbCount));
                        }else {
                            unread_baobiao_number.setVisibility(View.INVISIBLE);
                        }
                        if (ddCount>0){
                            tv_unread_ruzhang.setVisibility(View.VISIBLE);
                            tv_unread_ruzhang.setText(String.valueOf(ddCount));
                        }else {
                            tv_unread_ruzhang.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("dingdanac,e",volleyError.toString());
            }
        });
        MySingleton.getInstance(CompanyInfoActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zy1_baozj:
                if (margen1==null||"".equals(margen1)||Double.parseDouble(margen1)==0){
                    startActivityForResult(new Intent(CompanyInfoActivity.this, BZJJNActivity.class).putExtra("YUE",String.valueOf(price)).putExtra("upId",upId1).putExtra("maj",ZY1)
                            .putExtra("biaoshi","01").putExtra("managerId",managerId),0);
                }else {
                    Log.e("conpanyinfo,mati",margin_time1);
                    startActivityForResult(new Intent(CompanyInfoActivity.this, BZJZJActivity.class).putExtra("JINE",margen1).putExtra("YUE",String.valueOf(price)).putExtra("upId",upId1).putExtra("maj",ZY1)
                            .putExtra("createTime",create1).putExtra("biaoshi","01").putExtra("margin_time",margin_time1).putExtra("managerId",managerId),0);
                }
                break;
            case R.id.zy2_baozj:
                if (margen2==null||"".equals(margen2)||Double.parseDouble(margen2)==0){
                    startActivityForResult(new Intent(CompanyInfoActivity.this, BZJJNActivity.class).putExtra("YUE",String.valueOf(price)).putExtra("upId",upId2).putExtra("maj",ZY2)
                            .putExtra("biaoshi","01").putExtra("managerId",managerId),0);
                }else {
                    startActivityForResult(new Intent(CompanyInfoActivity.this, BZJZJActivity.class).putExtra("JINE",margen2).putExtra("YUE",String.valueOf(price)).putExtra("upId",upId2).putExtra("maj",ZY2)
                            .putExtra("createTime",create2).putExtra("biaoshi","01").putExtra("margin_time",margin_time2).putExtra("managerId",managerId),0);
                }
                break;
            case R.id.zy3_baozj:
                if (margen3==null||"".equals(margen3)||Double.parseDouble(margen3)==0){
                    startActivityForResult(new Intent(CompanyInfoActivity.this, BZJJNActivity.class).putExtra("YUE",String.valueOf(price)).putExtra("upId",upId3).putExtra("maj",ZY3)
                            .putExtra("biaoshi","01").putExtra("managerId",managerId),0);
                }else {
                    startActivityForResult(new Intent(CompanyInfoActivity.this, BZJZJActivity.class).putExtra("JINE",margen3).putExtra("YUE",String.valueOf(price)).putExtra("upId",upId3).putExtra("maj",ZY3)
                            .putExtra("createTime",create3).putExtra("biaoshi","01").putExtra("margin_time",margin_time3).putExtra("managerId",managerId),0);
                }
                break;
            case R.id.zy4_baozj:
                if (margen4==null||"".equals(margen4)||Double.parseDouble(margen4)==0){
                    startActivityForResult(new Intent(CompanyInfoActivity.this, BZJJNActivity.class).putExtra("YUE",String.valueOf(price)).putExtra("upId",upId4).putExtra("maj",ZY4)
                            .putExtra("biaoshi","01").putExtra("managerId",managerId),0);
                }else {
                    startActivityForResult(new Intent(CompanyInfoActivity.this, BZJZJActivity.class).putExtra("JINE",margen4).putExtra("YUE",String.valueOf(price)).putExtra("upId",upId4).putExtra("maj",ZY4)
                            .putExtra("createTime",create4).putExtra("biaoshi","01").putExtra("margin_time",margin_time4).putExtra("managerId",managerId),0);
                }
                break;
            case R.id.btn_zhy1:
                Intent intenti1 = new Intent(CompanyInfoActivity.this, CompanyActivity.class);
                if (("".equals(upId1)||upId1==null)&&("".equals(upId2)||upId2==null)&&("".equals(upId3)||upId3==null)&&("".equals(upId4)||upId4==null)){
                    intenti1.putExtra("isFirst","11");
                }else {
                    intenti1.putExtra("isFirst","12");
                }
                intenti1.putExtra("qiyeId",qiyeId);
                startActivityForResult(intenti1,0);
                break;
            case R.id.btn_zhy2:
                Intent intenti2 = new Intent(CompanyInfoActivity.this, CompanyActivity.class);
                if (("".equals(upId1)||upId1==null)&&("".equals(upId2)||upId2==null)&&("".equals(upId3)||upId3==null)&&("".equals(upId4)||upId4==null)){
                    intenti2.putExtra("isFirst","11");
                }else {
                    intenti2.putExtra("isFirst","12");
                }
                intenti2.putExtra("qiyeId",qiyeId);
                startActivityForResult(intenti2,0);
                break;
            case R.id.btn_zhy3:
                Intent intenti3 = new Intent(CompanyInfoActivity.this, CompanyActivity.class);
                if (("".equals(upId1)||upId1==null)&&("".equals(upId2)||upId2==null)&&("".equals(upId3)||upId3==null)&&("".equals(upId4)||upId4==null)){
                    intenti3.putExtra("isFirst","11");
                }else {
                    intenti3.putExtra("isFirst","12");
                }
                intenti3.putExtra("qiyeId",qiyeId);
                startActivityForResult(intenti3,0);
                break;
            case R.id.btn_zhy4:
                Intent intenti4 = new Intent(CompanyInfoActivity.this, CompanyActivity.class);
                if (("".equals(upId1)||upId1==null)&&("".equals(upId2)||upId2==null)&&("".equals(upId3)||upId3==null)&&("".equals(upId4)||upId4==null)){
                    intenti4.putExtra("isFirst","11");
                }else {
                    intenti4.putExtra("isFirst","12");
                }
                intenti4.putExtra("qiyeId",qiyeId);
                startActivityForResult(intenti4,0);
                break;
            case R.id.iv_avatar:
                Intent intenti = new Intent(CompanyInfoActivity.this, CompanyActivity.class);
                if (("".equals(upId1)||upId1==null)&&("".equals(upId2)||upId2==null)&&("".equals(upId3)||upId3==null)&&("".equals(upId4)||upId4==null)){
                    intenti.putExtra("isFirst","11");
                }else {
                    intenti.putExtra("isFirst","12");
                }
                intenti.putExtra("qiyeId",qiyeId);
                startActivityForResult(intenti,0);
                break;
            case R.id.re_myinfo:
                Intent intentm = new Intent(CompanyInfoActivity.this, CompanyActivity.class);
                if (("".equals(upId1)||upId1==null)&&("".equals(upId2)||upId2==null)&&("".equals(upId3)||upId3==null)&&("".equals(upId4)||upId4==null)){
                    intentm.putExtra("isFirst","11");
                }else {
                    intentm.putExtra("isFirst","12");
                }
                intentm.putExtra("qiyeId",qiyeId);
                startActivityForResult(intentm,0);
                break;
            case R.id.re_qiye_shxban:
                startActivityForResult(new Intent(CompanyInfoActivity.this,ShangXiaBanActivity.class).putExtra("companyName",companyName)
                        .putExtra("comAddress",comAddress).putExtra("loginTime",loginTime).putExtra("companyShortName",companyShortName).putExtra("resv6",resv6),0);
                break;
            case R.id.re_qiye_kaoqin:
                Intent intent = new Intent(CompanyInfoActivity.this, KaoQinActivity.class);

                //新的考勤界面
              //  Intent intent = new Intent(CompanyInfoActivity.this, AttendanceActivity.class);

                intent.putExtra("avatar", FXConstant.URL_QIYE_TOUXIANG + comImage);
                intent.putExtra("memberNum", memberNum);
                intent.putExtra("loginTime", loginTime);
                intent.putExtra("loginTime", loginTime);
                intent.putExtra("companyId",qiyeId);
                startActivity(intent);
                break;
            case R.id.re_yuangong_qingjia:
                startActivity(new Intent(CompanyInfoActivity.this,QingjiaListActivity.class).putExtra("biaoshi","01"));
                break;
            case R.id.re_qiye_paidan:
                startActivity(new Intent(CompanyInfoActivity.this,QiyePaidanListActivity.class).putExtra("biaoshi","01").putExtra("isQunzhu","01"));
                break;
            case R.id.re_my_dingdan:
                startActivity(new Intent(CompanyInfoActivity.this,DingdanActivity.class).putExtra("biaoshi","00"));
                break;
            case R.id.re_qiye_setting:
                startActivityForResult(new Intent(CompanyInfoActivity.this,JoinQiyeShfeiActivity.class).putExtra("companyName",companyName).putExtra("biaoshi","00"),1);
                break;
            case R.id.re_yuE:
                Intent intent11 = new Intent(CompanyInfoActivity.this,YuEActivity.class);
                intent11.putExtra("papass",pass);
                intent11.putExtra("image",comImage);
                intent11.putExtra("managerId",managerId);
                intent11.putExtra("name",companyName);
                intent11.putExtra("biaoshi","001");
                startActivityForResult(intent11,3);
                break;
            case R.id.re_chongzhi:
                String item1= "钱包余额充值";
                String item2= "直 接 充 值";
                showDialog(item1,item2);
                break;
            case R.id.re_tixian:
                String item4= "提现到支付宝";
                String item5= "提 现 到 微 信";
                String item6= "提现到账户余额";
                showDialog2(item4,item5,item6);
                break;
            case R.id.re_hetong:
                startActivity(new Intent(CompanyInfoActivity.this,HeTongListActivity.class).putExtra("biaoshi","gongsi").putExtra("image1",recruitImages).putExtra("image2",joinImages).putExtra("body1",recruitBody)
                        .putExtra("body2",joinBody).putExtra("feiyong1",recruitMoney).putExtra("feiyong2",joinMoney).putExtra("companyName",companyName));
                break;
            case R.id.re_baogao:
                startActivity(new Intent(CompanyInfoActivity.this,BaobiaoListActivity.class).putExtra("biaoshi","01"));
                break;
            case R.id.rl_zhuanfa:
                startActivity(new Intent(CompanyInfoActivity.this, HongbaoDetailListActivity.class).putExtra("friendsNumber",friendsNumber).putExtra("shareRed",shareRed).putExtra("managerId",managerId).putExtra("biaoshi","01"));
                break;
        }
    }
    private void showDialog2(String item4,String item5,String item6) {
        final String[] biaoshi2 = new String[1];
        LayoutInflater inflaterDl = LayoutInflater.from(CompanyInfoActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(CompanyInfoActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
        re_item1.setVisibility(View.GONE);
        re_item5.setVisibility(View.VISIBLE);
        tv_item1.setText(item4);
        tv_item2.setText(item5);
        tv_item5.setText(item6);
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaoshi2[0] = "00";
                Intent intent2 = new Intent(CompanyInfoActivity.this, TiXianActivity.class);
                intent2.putExtra("baozhengjin",baozhengjin);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("managerId",managerId);
                intent2.putExtra("biaoshi","01");
                intent2.putExtra("biaoshi2", biaoshi2[0]);
                startActivityForResult(intent2,1);
                dialog.dismiss();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaoshi2[0] = "00";
                Intent intent2 = new Intent(CompanyInfoActivity.this, TiXianActivity.class);
                intent2.putExtra("baozhengjin",baozhengjin);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("managerId",managerId);
                intent2.putExtra("biaoshi","01");
                intent2.putExtra("biaoshi2", biaoshi2[0]);
                startActivityForResult(intent2,1);
                dialog.dismiss();
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaoshi2[0] = "01";
                Intent intent2 = new Intent(CompanyInfoActivity.this, TiXianActivity.class);
                intent2.putExtra("baozhengjin",baozhengjin);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("managerId",managerId);
                intent2.putExtra("biaoshi","01");
                intent2.putExtra("biaoshi2", biaoshi2[0]);
                startActivityForResult(intent2,1);
                dialog.dismiss();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    private void showDialog(String item1,String item2) {
        final String[] biaoshi2 = new String[1];
        LayoutInflater inflaterDl = LayoutInflater.from(CompanyInfoActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_moddel_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(CompanyInfoActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
        TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
        re_item4.setVisibility(View.GONE);
        tv_item1.setText(item1);
        tv_item2.setText(item2);
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaoshi2[0] = "00";
                Intent intent2 = new Intent(CompanyInfoActivity.this, ChongZhiActivity.class);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("biaoshi","01");
                intent2.putExtra("biaoshi2", biaoshi2[0]);
                startActivityForResult(intent2,1);
                dialog.dismiss();
            }
        });
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biaoshi2[0] = "01";
                Intent intent2 = new Intent(CompanyInfoActivity.this, ChongZhiActivity.class);
                intent2.putExtra("papass",pass);
                intent2.putExtra("price",yuE);
                intent2.putExtra("biaoshi","01");
                intent2.putExtra("biaoshi2", biaoshi2[0]);
                startActivityForResult(intent2,1);
                dialog.dismiss();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void doCl(View v){
        String zyType;
        switch (v.getId()){
            case R.id.tv_zy1:
                if (tv_zy1.getText().equals("填写专业1")){
                    String isDYC;
                    if (("".equals(upId1)||upId1==null)&&("".equals(upId2)||upId2==null)&&("".equals(upId3)||upId3==null)&&("".equals(upId4)||upId4==null)){
                        isDYC = "11";
                    }else {
                        isDYC = "12";
                    }
                    startActivityForResult(new Intent(CompanyInfoActivity.this,CompanyUpdateTwoActivity.class).putExtra("type", CompanyUpdateTwoActivity.TYPE_MAJOR1).
                            putExtra("isFirst",isDYC).putExtra("image",image1).putExtra("companyId",qiyeId).putExtra("companyName",companyName),0);
                }else {
                    zyType = resv31;
                    Intent intent = new Intent(CompanyInfoActivity.this, ZYDetailActivity.class);
                    intent.putExtra("distance", "001");
                    intent.putExtra("zhuanye","01");
                    intent.putExtra("zyType", zyType);
                    intent.putExtra("fxUpName", fxUpName);
                    intent.putExtra("comName",companyName);
                    intent.putExtra("decribe", decribe1);
                    intent.putExtra("remark", remark1);
                    intent.putExtra("image", image1);
                    intent.putExtra("create", create1);
                    intent.putExtra("biaoshi", "01");
                    intent.putExtra("body", ZY1);
                    intent.putExtra("margen", margen1);
                    intent.putExtra("pass", pass);
                    intent.putExtra("hxid", qiyeId);
                    intent.putExtra("upId", upId1);
                    intent.putExtra("liulancishu", liulancishu1);
                    startActivityForResult(intent,3);
                }
                break;
            case R.id.tv_zy2:
                if (tv_zy2.getText().equals("填写专业2")){
                    String isDYC;
                    if (("".equals(upId1)||upId1==null)&&("".equals(upId2)||upId2==null)&&("".equals(upId3)||upId3==null)&&("".equals(upId4)||upId4==null)){
                        isDYC = "11";
                    }else {
                        isDYC = "12";
                    }
                    startActivityForResult(new Intent(CompanyInfoActivity.this,CompanyUpdateTwoActivity.class)
                            .putExtra("type", CompanyUpdateTwoActivity.TYPE_MAJOR2).putExtra("isFirst",isDYC).putExtra("image",image2).putExtra("companyId",qiyeId).putExtra("companyName",companyName),0);
                }else {
                    zyType = resv32;
                    Intent intent2 = new Intent(CompanyInfoActivity.this, ZYDetailActivity.class);
                    intent2.putExtra("distance", "001");
                    intent2.putExtra("zhuanye","02");
                    intent2.putExtra("zyType", zyType);
                    intent2.putExtra("fxUpName", fxUpName);
                    intent2.putExtra("comName",companyName);
                    intent2.putExtra("decribe", decribe2);
                    intent2.putExtra("remark", remark2);
                    intent2.putExtra("image", image2);
                    intent2.putExtra("biaoshi", "01");
                    intent2.putExtra("create", create2);
                    intent2.putExtra("body", ZY2);
                    intent2.putExtra("margen", margen2);
                    intent2.putExtra("pass", pass);
                    intent2.putExtra("hxid", qiyeId);
                    intent2.putExtra("upId", upId2);
                    intent2.putExtra("liulancishu", liulancishu2);
                    startActivityForResult(intent2,3);
                }
                break;
            case R.id.tv_zy3:
                if (tv_zy3.getText().equals("填写专业3")){
                    String isDYC;
                    if (("".equals(upId1)||upId1==null)&&("".equals(upId2)||upId2==null)&&("".equals(upId3)||upId3==null)&&("".equals(upId4)||upId4==null)){
                        isDYC = "11";
                    }else {
                        isDYC = "12";
                    }
                    startActivityForResult(new Intent(CompanyInfoActivity.this,CompanyUpdateTwoActivity.class)
                            .putExtra("type", CompanyUpdateTwoActivity.TYPE_MAJOR3).putExtra("isFirst",isDYC).putExtra("image",image3).putExtra("companyId",qiyeId).putExtra("companyName",companyName),0);
                }else {
                    zyType = resv33;
                    Intent intent3 = new Intent(CompanyInfoActivity.this, ZYDetailActivity.class);
                    intent3.putExtra("distance", "001");
                    intent3.putExtra("zhuanye","03");
                    intent3.putExtra("zyType", zyType);
                    intent3.putExtra("fxUpName", fxUpName);
                    intent3.putExtra("comName",companyName);
                    intent3.putExtra("decribe", decribe3);
                    intent3.putExtra("remark", remark3);
                    intent3.putExtra("image", image3);
                    intent3.putExtra("create", create3);
                    intent3.putExtra("biaoshi", "01");
                    intent3.putExtra("body", ZY3);
                    intent3.putExtra("margen", margen3);
                    intent3.putExtra("pass", pass);
                    intent3.putExtra("hxid", qiyeId);
                    intent3.putExtra("upId", upId3);
                    intent3.putExtra("liulancishu", liulancishu3);
                    startActivityForResult(intent3,3);
                }
                break;
            case R.id.tv_zy4:
                if (tv_zy4.getText().equals("填写专业4")){
                    String isDYC;
                    if (("".equals(upId1)||upId1==null)&&("".equals(upId2)||upId2==null)&&("".equals(upId3)||upId3==null)&&("".equals(upId4)||upId4==null)){
                        isDYC = "11";
                    }else {
                        isDYC = "12";
                    }
                    startActivityForResult(new Intent(CompanyInfoActivity.this,CompanyUpdateTwoActivity.class).putExtra("type", CompanyUpdateTwoActivity.TYPE_MAJOR4)
                            .putExtra("type", CompanyUpdateTwoActivity.TYPE_MAJOR4).putExtra("isFirst",isDYC).putExtra("image",image4).putExtra("companyId",qiyeId).putExtra("companyName",companyName),0);
                }else {
                    zyType = resv34;
                    Intent intent4 = new Intent(CompanyInfoActivity.this, ZYDetailActivity.class);
                    intent4.putExtra("distance", "001");
                    intent4.putExtra("zhuanye","04");
                    intent4.putExtra("zyType", zyType);
                    intent4.putExtra("fxUpName", fxUpName);
                    intent4.putExtra("comName",companyName);
                    intent4.putExtra("decribe", decribe4);
                    intent4.putExtra("remark", remark4);
                    intent4.putExtra("image", image4);
                    intent4.putExtra("biaoshi", "01");
                    intent4.putExtra("create", create4);
                    intent4.putExtra("body", ZY4);
                    intent4.putExtra("margen", margen4);
                    intent4.putExtra("pass", pass);
                    intent4.putExtra("hxid", qiyeId);
                    intent4.putExtra("upId", upId4);
                    intent4.putExtra("liulancishu", liulancishu4);
                    startActivityForResult(intent4,3);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            presenter.loadQiYeInfo(qiyeId);
            pricePresenter.updatePriceData(qiyeId);
        }
    }


    @Override
    public void updateQiyeInfo(QiYeInfo user2) {
        shareRed = user2.getShareRed();
        managerId = user2.getManagerId();
        loginTime = TextUtils.isEmpty(user2.getLoginTime())?"08:30|17:30":user2.getLoginTime();
        qiyeId = user2.getCompanyId();
        companyShortName = user2.getCompanyShortName();
        resv6 = user2.getResv6();
        comImage = user2.getComImage();
        companyName = user2.getCompanyName();
        comSignature = user2.getComSignature();
        comEmail = user2.getComEmail();
        comTel = user2.getComTel();
        comAddress = user2.getComAddress();
        resv3 = user2.getResv3();
        margin_time1 = user2.getMargin_time1();
        margin_time2 = user2.getMargin_time2();
        margin_time3 = user2.getMargin_time3();
        margin_time4 = user2.getMargin_time4();
        resv31 = TextUtils.isEmpty(user2.getZy1resv3())?"01":user2.getZy1resv3();
        resv32 = TextUtils.isEmpty(user2.getZy2resv3())?"01":user2.getZy2resv3();
        resv33 = TextUtils.isEmpty(user2.getZy3resv3())?"01":user2.getZy3resv3();
        resv34 = TextUtils.isEmpty(user2.getZy4resv3())?"01":user2.getZy4resv3();
        decribe1 = TextUtils.isEmpty(user2.getUpDescribe1())?"":user2.getUpDescribe1();
        decribe2 = TextUtils.isEmpty(user2.getUpDescribe2())?"":user2.getUpDescribe2();
        decribe3 = TextUtils.isEmpty(user2.getUpDescribe3())?"":user2.getUpDescribe3();
        decribe4 = TextUtils.isEmpty(user2.getUpDescribe4())?"":user2.getUpDescribe4();
        joinMoney = TextUtils.isEmpty(user2.getJoinMoney())?"":user2.getJoinMoney();
        joinImages = TextUtils.isEmpty(user2.getJoinImages())?"":user2.getJoinImages();
        joinBody = TextUtils.isEmpty(user2.getJoinBody())?"":user2.getJoinBody();
        recruitMoney = TextUtils.isEmpty(user2.getRecruitMoney())?"":user2.getRecruitMoney();
        recruitImages = TextUtils.isEmpty(user2.getRecruitImages())?"":user2.getRecruitImages();
        recruitBody = TextUtils.isEmpty(user2.getRecruitBody())?"":user2.getRecruitBody();
        margen1 = TextUtils.isEmpty(user2.getMargin1())?"0":user2.getMargin1();
        margen2 = TextUtils.isEmpty(user2.getMargin2())?"0":user2.getMargin2();
        margen3 = TextUtils.isEmpty(user2.getMargin3())?"0":user2.getMargin3();
        margen4 = TextUtils.isEmpty(user2.getMargin4())?"0":user2.getMargin4();
        liulancishu1 = TextUtils.isEmpty(user2.getZy1resv1())?"0":user2.getZy1resv1();
        liulancishu2 = TextUtils.isEmpty(user2.getZy2resv1())?"0":user2.getZy2resv1();
        liulancishu3 = TextUtils.isEmpty(user2.getZy3resv1())?"0":user2.getZy3resv1();
        liulancishu4 = TextUtils.isEmpty(user2.getZy4resv1())?"0":user2.getZy4resv1();
        remark1 = TextUtils.isEmpty(user2.getRemark1())?"":user2.getRemark1();
        remark2 = TextUtils.isEmpty(user2.getRemark2())?"":user2.getRemark2();
        remark3 = TextUtils.isEmpty(user2.getRemark3())?"":user2.getRemark3();
        remark4 = TextUtils.isEmpty(user2.getRemark4())?"":user2.getRemark4();
        image1 = TextUtils.isEmpty(user2.getZyImage1())?"":user2.getZyImage1();
        image2 = TextUtils.isEmpty(user2.getZyImage2())?"":user2.getZyImage2();
        image3 = TextUtils.isEmpty(user2.getZyImage3())?"":user2.getZyImage3();
        image4 = TextUtils.isEmpty(user2.getZyImage4())?"":user2.getZyImage4();
        create1 = TextUtils.isEmpty(user2.getCreateTime1())?"":user2.getCreateTime1();
        create2 = TextUtils.isEmpty(user2.getCreateTime2())?"":user2.getCreateTime2();
        create3 = TextUtils.isEmpty(user2.getCreateTime3())?"":user2.getCreateTime3();
        create4 = TextUtils.isEmpty(user2.getCreateTime4())?"":user2.getCreateTime4();
        upId1 = TextUtils.isEmpty(user2.getUpId1())?"":user2.getUpId1();
        upId2 = TextUtils.isEmpty(user2.getUpId2())?"":user2.getUpId2();
        upId3 = TextUtils.isEmpty(user2.getUpId3())?"":user2.getUpId3();
        upId4 = TextUtils.isEmpty(user2.getUpId4())?"":user2.getUpId4();
        ZY1 = TextUtils.isEmpty(user2.getUpName1())?"填写专业1":user2.getUpName1();
        ZY2 = TextUtils.isEmpty(user2.getUpName2())?"填写专业2":user2.getUpName2();
        ZY3 = TextUtils.isEmpty(user2.getUpName3())?"填写专业3":user2.getUpName3();
        ZY4 = TextUtils.isEmpty(user2.getUpName4())?"填写专业4":user2.getUpName4();
        memberNum = user2.getMemberNum();
        tv_zy1.setText(ZY1);
        tv_zy2.setText(ZY2);
        tv_zy3.setText(ZY3);
        tv_zy4.setText(ZY4);
        if (shareRed!=null&&!"".equals(shareRed)&&!shareRed.equalsIgnoreCase("null")) {
            double jine1 = Double.parseDouble(shareRed);
            String hbYue = String.format("%.2f", jine1);
            hbYue = hbYue + " 元";
            tv_jine.setText(hbYue);
        }else {
            tv_jine.setText("0.00 元");
        }
        if (margen1!=null&&!"".equals(margen1)) {
            double jine = Double.parseDouble(margen1);
            margen1 = String.format("%.2f", jine);
        }
        if (margen2!=null&&!"".equals(margen2)) {
            double jine = Double.parseDouble(margen2);
            margen2 = String.format("%.2f", jine);
        }
        if (margen3!=null&&!"".equals(margen3)) {
            double jine = Double.parseDouble(margen3);
            margen3 = String.format("%.2f", jine);
        }
        if (margen4!=null&&!"".equals(margen4)) {
            double jine = Double.parseDouble(margen4);
            margen4 = String.format("%.2f", jine);
        }
        zy1_baozj.setText(margen1+"元");
        zy2_baozj.setText(margen2+"元");
        zy3_baozj.setText(margen3+"元");
        zy4_baozj.setText(margen4+"元");
        if (!(remark1.equals("")||remark1.equals(null))){
            String [] array = remark1.split(",");
            if (array.length==1) {
                cishu1 = array[0].trim();
            }else if (array.length==2){
                cishu1 = array[0].trim();
                String jine1 = array[1];
                if (jine1.equals("")||jine1.equals(null)){
                    jine1 = "0";
                }
                double jine = Double.parseDouble(jine1);
                jine1 = String.format("%.2f", jine);
                zy1_jine.setText(jine1+"元");
            }
            if (cishu1.equals("")||cishu1.equals(null)){
                cishu1 = "0";
            }
            zy1_jiedancishu.setText(cishu1+"次");
        }
        if (!(remark2.equals("")||remark2.equals(null))){
            String [] array = remark2.split(",");
            cishu2 = "";
            if (array.length==1) {
                cishu2 = array[0].trim();
            }else if (array.length==2){
                cishu2 = array[0].trim();
                String jine1 = array[1];
                if (jine1.equals("")||jine1.equals(null)){
                    jine1 = "0";
                }
                double jine = Double.parseDouble(jine1);
                jine1 = String.format("%.2f", jine);
                zy2_jine.setText(jine1+"元");
            }
            if (cishu2.equals("")||cishu2.equals(null)){
                cishu2 = "0";
            }
            zy2_jiedancishu.setText(cishu2+"次");
        }
        if (!(remark3.equals("")||remark3.equals(null))){
            String [] array = remark3.split(",");
            cishu3 = "";
            if (array.length==1) {
                cishu3 = array[0].trim();
            }else if (array.length==2){
                cishu3 = array[0].trim();
                String jine1 = array[1];
                if (jine1.equals("")||jine1.equals(null)){
                    jine1 = "0";
                }
                double jine = Double.parseDouble(jine1);
                jine1 = String.format("%.2f", jine);
                zy3_jine.setText(jine1+"元");
            }
            if (cishu3.equals("")||cishu3.equals(null)){
                cishu3 = "0";
            }
            zy3_jiedancishu.setText(cishu3+"次");
        }
        if (!(remark4.equals("")||remark4.equals(null))){
            String [] array = remark4.split(",");
            cishu4 = "";
            if (array.length==1) {
                cishu4 = array[0].trim();
            }else if (array.length==2){
                cishu4 = array[0].trim();
                String jine1 = array[1];
                if (jine1.equals("")||jine1.equals(null)){
                    jine1 = "0";
                }
                double jine = Double.parseDouble(jine1);
                jine1 = String.format("%.2f", jine);
                zy4_jine.setText(jine1+"元");
            }
            if (cishu4.equals("")||cishu4.equals(null)){
                cishu4 = "0";
            }
            zy4_jiedancishu.setText(cishu4+"次");
        }
        if (ZY1==null||"填写专业1".equals(ZY1)){
            btn_zhy1.setVisibility(View.VISIBLE);
        }else {
            btn_zhy1.setVisibility(View.GONE);
            if (fxUpName==null){
                fxUpName = ZY1+"，";
            }
        }
        if (ZY2==null||"填写专业2".equals(ZY2)){
            btn_zhy2.setVisibility(View.VISIBLE);
        }else {
            btn_zhy2.setVisibility(View.GONE);
            if (fxUpName==null){
                fxUpName = ZY2+"，";
            }else {
                fxUpName += ZY2+"，";
            }
        }
        if (ZY3==null||"填写专业3".equals(ZY3)){
            btn_zhy3.setVisibility(View.VISIBLE);
        }else {
            btn_zhy3.setVisibility(View.GONE);
            if (fxUpName==null){
                fxUpName = ZY3+"，";
            }else {
                fxUpName += ZY3+"，";
            }
        }
        if (ZY4==null||"填写专业4".equals(ZY4)){
            btn_zhy4.setVisibility(View.VISIBLE);
        }else {
            btn_zhy4.setVisibility(View.GONE);
            if (fxUpName==null){
                fxUpName = ZY4+"，";
            }else {
                fxUpName += ZY4+"，";
            }
        }
        String comName = "",comAddr="";
        try {
            comName = URLDecoder.decode(companyName, "UTF-8");
            comSignature = URLDecoder.decode(comSignature, "UTF-8");
            comAddr = URLDecoder.decode(comAddress, "UTF-8");
            resv3 = URLDecoder.decode(resv3, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        tv_name.setText(comName);
        tv_jianjie.setText(comSignature);
        tv_company_address.setText(comAddr);
        tv_company_zhizhao.setText(comEmail);
        tv_qiye_phone.setText(comTel);
        qiye_faren.setText(resv3);
        if (comImage.length()>1){
            comImage = comImage.split("\\|")[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_QIYE_TOUXIANG+comImage,iv_avatar,DemoApplication.mOptions);
        }else {
            iv_avatar.setVisibility(View.INVISIBLE);
            tv_titl.setVisibility(View.VISIBLE);
            tv_titl.setText("企");
        }
    }
    @Override
    public void updateCurrentPrice(Object success) {
        String chongzhiJe = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentQiyeChzYuE())?"0.0":DemoApplication.getInstance().getCurrentQiyeChzYuE();
        String tixianJe = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentQiyetxYuE())?"0.0":DemoApplication.getInstance().getCurrentQiyetxYuE();
        yuE = DemoApplication.getInstance().getCurrenQiyePrice();
        pass = DemoApplication.getInstance().getCurrentQiyePayPass();
        if (chongzhiJe!=null&&!"".equals(chongzhiJe)) {
            double jine = Double.parseDouble(chongzhiJe);
            chongzhiJe = String.format("%.2f", jine);
        }
        if (tixianJe!=null&&!"".equals(tixianJe)) {
            double jine = Double.parseDouble(tixianJe);
            tixianJe = String.format("%.2f", jine);
        }
        tv_chongzhijine.setText(chongzhiJe);
        tv_tixianjine.setText(tixianJe);
        String jinE = String.format("%.2f", yuE);
        lingqian.setText(jinE+"");
    }
    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    Runnable errorReload = new Runnable() {
        @Override
        public void run() {
            presenter.loadQiYeInfo(qiyeId);
            myhandler.removeCallbacks(errorReload);
        }
    };
    @Override
    public void showError() {
        mRefreshLayout.setRefreshing(false);
        myhandler.postDelayed(errorReload,5000);
    }

}
