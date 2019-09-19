package com.sangu.apptongji.main.alluser.order.avtivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.fanxin.easeui.utils.FileStorage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.address.AddressListTwoActivity;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.alluser.presenter.IOrderDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.OrderDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IOrderDetailView;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.qiye.QiYeYuGoActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
 * Created by user on 2016/9/1.
 */
public class MOrderDetailTwoActivity extends BaseActivity implements IOrderDetailView,IUAZView {
    private IOrderDetailPresenter orderDetailPresenter;
    private IUAZPresenter uazPresenter;
    private ImageView iv_head;
    private TextView tv_name;
    private TextView tv_titl;
    private TextView tv_company_count;
    private TextView tv_nianling;
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
    private LinearLayout ll3_paidan,ll2,ll2_paidan;
    private ImageView iv_paidan_id1,iv_paidan_caiwu,iv_paidan_kehu,iv_jiantou;
    private TextView tv_paidan_id1,tev_paidan_id1,tv_paidan_id1_time,tv_paidan_caiwu,tev_paidan_caiwu,tv_paidan_caiwu_time
            ,tev_paidan_kehu,tv_paidan_kehu_time,tv_paidan_kehu,tv_ticheng;
    private RelativeLayout rl_paidan_id1,rl_ticheng;

    private String orderState;
    private LinearLayout ll_btn,ll;
    RelativeLayout llM_beizhu;
    private EditText et_your_project,et_qianshou_dizhi,et_dingdan_bianhao,et_zhifu_jine,etUBeizhu;
    private CheckBox cb_fapiao;
    private TextView tv_saomiao,tv_zhifuxianshi,tv_fenxiang;
    private Button btnCommit,btnCansul;
    private String orderId,userId,orderTime,orderBody,name,head,orderProject,orderNumber,orderAmt,orderSum,oImage,mImage,zongjia,beizhu,yueding,nowTime,resv2;
    private ImageView ivkehu,ivshangjia;
    private String biaoshi,biaoshi1,totalAmt,companyId,send_id1,send_id2,image1,image2,image3,image4,time1,time2,time3,resv4,resv5,oSignature,mSignature;
    private TextView tv_qiye_time_shanghu,tv_qiye_time_xiaofei,tv_qiye_xiaofei,tv_qiye_shanghu,tev_qiye_xiaofei=null,
            tev_qiye_shanghu=null,tv_fasong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morder_moshier);
        initView();
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        orderDetailPresenter = new OrderDetailPresenter(this,this);
        uazPresenter = new UAZPresenter(this,this);
        ivkehu = (ImageView) findViewById(R.id.iv_paidan_qiye);
        tv_fasong = (TextView) findViewById(R.id.tv_fasong);
        tev_qiye_xiaofei = (TextView) findViewById(R.id.tev_paidan_qiye);
        tev_qiye_shanghu = (TextView) findViewById(R.id.tev_paidan_qiyeren);
        tv_qiye_time_shanghu = (TextView) findViewById(R.id.tv_paidan_qiyeren_time);
        tv_qiye_time_xiaofei = (TextView) findViewById(R.id.tv_paidan_qiye_time);
        tv_qiye_xiaofei = (TextView) findViewById(R.id.tv_paidan_qiye);
        tv_qiye_shanghu = (TextView) findViewById(R.id.tv_paidan_qiyeren);
        ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
        ll = (LinearLayout) findViewById(R.id.ll);
        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        ll_two = (LinearLayout) findViewById(R.id.ll_two);
        ll_three = (LinearLayout) findViewById(R.id.ll_three);
        ll_four = (LinearLayout) findViewById(R.id.ll_four);
        llM_beizhu = (RelativeLayout) findViewById(R.id.llM_beizhu);
        ivshangjia = (ImageView) findViewById(R.id.iv_paidan_qiyeren);
        ll.setFocusable(true);
        ll.setFocusableInTouchMode(true);
        ll.requestFocus();
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
        if ("01".equals(biaoshi)){
            iv_jiantou.setVisibility(View.VISIBLE);
            ll2_paidan.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll3_paidan.setVisibility(View.VISIBLE);
        }
        if ("02".equals(biaoshi)){
            iv_jiantou.setVisibility(View.VISIBLE);
            ll2_paidan.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll3_paidan.setVisibility(View.GONE);
        }

        if (orderState.equals("01")||orderState.equals("02")||orderState.equals("04")){
            initView2();
        }else if (orderState.equals("05")||orderState.equals("10")||orderState.equals("07")) {
            initView12();
        }else if (orderState.equals("03")){
            searchUserInfo();
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
                    startActivity(new Intent(MOrderDetailTwoActivity.this, QiYeYuGoActivity.class).putExtra("companyId",companyId)
                            .putExtra("orderId",orderId).putExtra("totalAmt",totalAmt));
                }
            });
        }
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
    }

    private void initView() {
        tv_paidan_id1 = (TextView) findViewById(R.id.tv_paidan_id1);
        tev_paidan_id1 = (TextView) findViewById(R.id.tev_paidan_id1);
        tv_paidan_id1_time = (TextView) findViewById(R.id.tv_paidan_id1_time);
        tv_paidan_caiwu = (TextView) findViewById(R.id.tv_paidan_caiwu);
        tev_paidan_caiwu = (TextView) findViewById(R.id.tev_paidan_caiwu);
        tv_paidan_caiwu_time = (TextView) findViewById(R.id.tv_paidan_caiwu_time);
        tev_paidan_kehu = (TextView) findViewById(R.id.tev_paidan_kehu);
        tv_paidan_kehu_time = (TextView) findViewById(R.id.tv_paidan_kehu_time);
        tv_paidan_kehu = (TextView) findViewById(R.id.tv_paidan_kehu);
        tv_ticheng = (TextView) findViewById(R.id.tv_qiye_ticheng);
        iv_paidan_id1 = (ImageView) findViewById(R.id.iv_paidan_id1);
        iv_paidan_caiwu = (ImageView) findViewById(R.id.iv_paidan_caiwu);
        iv_paidan_kehu = (ImageView) findViewById(R.id.iv_paidan_kehu);
        iv_jiantou = (ImageView) findViewById(R.id.iv_jiantou);
        ll3_paidan = (LinearLayout) findViewById(R.id.ll3_paidan);
        ll2_paidan = (LinearLayout) findViewById(R.id.ll2_paidan);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        rl_paidan_id1 = (RelativeLayout) findViewById(R.id.rl_paidan_id1);
        rl_ticheng = (RelativeLayout) findViewById(R.id.rl_ticheng);

        iv_head = (ImageView) findViewById(R.id.iv_head);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        et_your_project = (EditText) findViewById(R.id.et_your_project);
        et_qianshou_dizhi = (EditText) findViewById(R.id.et_qianshou_dizhi);
        et_dingdan_bianhao = (EditText) findViewById(R.id.et_dingdan_bianhao);
        et_zhifu_jine = (EditText) findViewById(R.id.et_zhifu_jine);
        cb_fapiao = (CheckBox) findViewById(R.id.cb_fapiao);
        etUBeizhu = (EditText) findViewById(R.id.et_Ubeizhu);
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
        tv_zhifuxianshi = (TextView) findViewById(R.id.tv_zhifuxianshi);
        tv_fenxiang = (TextView) findViewById(R.id.tv_fenxiang);
        tv_saomiao = (TextView) findViewById(R.id.tv_saomiao);
        btnCommit = (Button) findViewById(R.id.btn_comit);
        btnCansul = (Button) findViewById(R.id.btn_cansul);
        btnCansul.setEnabled(false);
        btnCommit.setEnabled(false);
    }

    private void initView7() {
        tv_fenxiang.setEnabled(false);
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
        rl_ticheng.setVisibility(View.VISIBLE);
    }

    private void initView2() {
        etUBeizhu.setEnabled(false);
        etUBeizhu.setVisibility(View.GONE);
        btnCommit.setText("确认签收");
        ll_btn.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        if ("04".equals(orderState)){
            ll_btn.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.VISIBLE);
            btnCommit.setText("等待对方签收");
            btnCommit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            if (!"01".equals(biaoshi1)) {
                tv_fasong.setVisibility(View.VISIBLE);
            }
            tv_fasong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase(null)){
                        LayoutInflater inflaterDl = LayoutInflater.from(MOrderDetailTwoActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                        final Dialog dialog2 = new AlertDialog.Builder(MOrderDetailTwoActivity.this,R.style.Dialog).create();
                        dialog2.show();
                        dialog2.getWindow().setContentView(layout);
                        dialog2.setCanceledOnTouchOutside(false);
                        dialog2.setCancelable(false);
                        TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                        Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                        tv_title.setText("单个订单最多发送给两个用户，该订单已达到上限！");
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
                        return;
                    }
                    LayoutInflater inflaterDl = LayoutInflater.from(MOrderDetailTwoActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_order_send, null);
                    final Dialog dialog = new AlertDialog.Builder(MOrderDetailTwoActivity.this,R.style.Dialog).create();
                    dialog.show();
                    dialog.getWindow().setContentView(layout);
                    dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                    dialog.setCanceledOnTouchOutside(true);
                    RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                    RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                    RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
                    final EditText et_phone3 = (EditText) dialog.findViewById(R.id.et_phone3);
                    Button btn_phone3 = (Button) dialog.findViewById(R.id.btn_phone3);
                    re_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            PermissionUtil permissionUtil = new PermissionUtil(MOrderDetailTwoActivity.this);
                            permissionUtil.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                    new PermissionListener() {
                                        @Override
                                        public void onGranted() {
                                            //所有权限都已经授权
                                            Intent intent = new Intent(MOrderDetailTwoActivity.this,AddressListTwoActivity.class);
                                            intent.putExtra("biaoshi","03");
                                            intent.putExtra("orderId",orderId);
                                            intent.putExtra("orderBody",orderBody);
                                            intent.putExtra("hasId1",send_id1);
                                            intent.putExtra("hasId2",send_id2);
                                            intent.putExtra("hasId3",userId);
                                            intent.putExtra("hasId4",DemoHelper.getInstance().getCurrentUsernName());
                                            startActivityForResult(intent,0);
                                        }
                                        @Override
                                        public void onDenied(List<String> deniedPermission) {
                                            //Toast第一个被拒绝的权限
                                            Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限！",Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onShouldShowRationale(List<String> deniedPermission) {
                                            //Toast第一个勾选不在提示的权限
                                            Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                    re_item2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent3 = new Intent(MOrderDetailTwoActivity.this,FriendActivity.class);
                            intent3.putExtra("biaoshi","03");
                            intent3.putExtra("orderId",orderId);
                            intent3.putExtra("orderBody",orderBody);
                            intent3.putExtra("hasId1",send_id1);
                            intent3.putExtra("hasId2",send_id2);
                            intent3.putExtra("hasId3",userId);
                            intent3.putExtra("hasId4",DemoHelper.getInstance().getCurrentUsernName());
                            startActivityForResult(intent3,0);
                        }
                    });
                    btn_phone3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            String userName = et_phone3.getText().toString().trim();
                            if (TextUtils.isEmpty(userName)||userName.length()!=11) {
                                Toast.makeText(getApplicationContext(), "请输入正确格式的电话号码!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (userName.equals(DemoHelper.getInstance().getCurrentUsernName())||userName.equals(userId)||userName.equals(send_id1)||userName.equals(send_id2)){
                                LayoutInflater inflaterDl = LayoutInflater.from(MOrderDetailTwoActivity.this);
                                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                                final Dialog dialog2 = new AlertDialog.Builder(MOrderDetailTwoActivity.this,R.style.Dialog).create();
                                dialog2.show();
                                dialog2.getWindow().setContentView(layout);
                                dialog2.setCanceledOnTouchOutside(false);
                                dialog2.setCancelable(false);
                                TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                                Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                                tv_title.setText("该账号已在本个订单中,无需再次发送！");
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog2.dismiss();
                                    }
                                });
                                return;
                            }
                            queryUserInfo(userName);
                        }
                    });
                    re_item4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }
    private void queryUserInfo(final String userName) {
        String url = FXConstant.URL_Get_UserInfo+userName;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                sendPushMessage2(userName,1);
                if ("用户名为空".equals(code)){
                    SendMessage(userName,0,"");
                    SendMessage1(userName);
                }else {
                    com.alibaba.fastjson.JSONObject userInfo = object.getJSONObject("userInfo");
                    String name = userInfo.getString("uName");
                    if (name == null||"".equals(name)){
                        name="";
                    }else {
                        name = name+":";
                    }
                    SendMessage(userName,1,name);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(MOrderDetailTwoActivity.this).addToRequestQueue(request);
    }
    private void sendPushMessage2(final String userId,final int type) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                param.put("u_id",userId);
                param.put("body","订单消息");
                if (type==0) {
                    param.put("type","000");
                }else {
                    param.put("type","12");
                }
                param.put("userId",myId);
                param.put("companyId","0");
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(MOrderDetailTwoActivity.this).addToRequestQueue(request);
    }
    private void SendMessage1(final String userName) {
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        final String loginId = DemoHelper.getInstance().getCurrentUsernName();
        if (name==null||"".equals(name)){
            name = loginId;
        }
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final String finalName = name;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                sendToUser(userName);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "发送失败，网络错误！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】订单("+orderBody+"),需要您验收签字,请注册手机端“正事多”在电子单据中查看");
                param.put("telNum", userName);
                Log.e("utorderdeac,sm1",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(MOrderDetailTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void SendMessage(final String userName, final int type,final String name) {
        String lists;
        if (type==1){
            lists = userName+","+userId;
        }else {
            lists = userId;
        }
        if (send_id1!=null&&!"".equals(send_id1)){
            lists = lists+","+send_id1;
        }
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final String finalLists = lists;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (type==1) {
                    sendToUser(userName);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type==1) {
                    sendToUser(userName);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】订单(" + orderBody + "),需要("+name+userName+")的用户签字验收");
                param.put("telNum", finalLists);
                Log.e("utorderdeac,sm",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(MOrderDetailTwoActivity.this).addToRequestQueue(request);
            }

            @Override
            public void onBan() {
                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void sendToUser(final String username) {
        String url = FXConstant.URL_FASONG_DZDANJU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateaddUscount(username);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){
                    Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络连接中断,发送失败！",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("order_id",orderId);
                param.put("send_id",DemoHelper.getInstance().getCurrentUsernName());
                param.put("u_id",username);
                return param;
            }
        };
        MySingleton.getInstance(MOrderDetailTwoActivity.this).addToRequestQueue(request);
    }
    private void updateaddUscount(final String username) {
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
                param.put("thirdPartyCount","1");
                param.put("userId",username);
                return param;
            }
        };
        MySingleton.getInstance(MOrderDetailTwoActivity.this).addToRequestQueue(request);
    }

    private void initView12() {
        tv_zhifuxianshi.setText("实付：");
        etUBeizhu.setEnabled(false);
        etUBeizhu.setVisibility(View.GONE);
        btnCommit.setText("确认签收");
        ll_btn.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
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
                    startActivity(new Intent(MOrderDetailTwoActivity.this,FWFKListActivity.class).putExtra("biaoshi","03").putExtra("orderId",orderId));
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
//        ScreenshotUtil.getBitmapByView(MOrderDetailTwoActivity.this, ll, "订单", null,false);
        fenxiang();
    }

    private void saveImg(Bitmap mBitmap)  {
        File file = new FileStorage("fenxiang").createCropFile("dingdanCut.png",null);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(MOrderDetailTwoActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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
    private void searchUserInfo() {
        String url = FXConstant.URL_Get_UserInfo+userId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = JSON.parseObject(s);
                JSONObject userInfo = object.getJSONObject("userInfo");
                String name = TextUtils.isEmpty(userInfo.getString("uName"))?userId:userInfo.getString("uName");
                String comName = userInfo.getString("uCompany");
                if (comName!=null){
                    try {
                        comName = URLDecoder.decode(comName,"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                String str;
                if (comName!=null&&!"".equals(comName)&&!comName.equalsIgnoreCase("null")){
                    str = comName;
                }else {
                    str = name;
                }
                tv_qiye_xiaofei.setText("("+str+")已验资");
                tv_qiye_xiaofei.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(MOrderDetailTwoActivity.this).addToRequestQueue(request);
    }

    private void initView3() {
        etUBeizhu.setEnabled(false);
        etUBeizhu.setVisibility(View.GONE);
        btnCansul.setVisibility(View.GONE);
        btnCommit.setText("提交订单");
        btnCommit.setEnabled(true);
        btnCommit.setVisibility(View.VISIBLE);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MOrderDetailTwoActivity.this,QianMingActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("userId",userId);
                intent.putExtra("biaoshi","12");
                startActivity(intent);
                finish();
            }
        });
        if (!"01".equals(biaoshi1)) {
            tv_fasong.setVisibility(View.VISIBLE);
        }
        tv_fasong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase(null)){
                    LayoutInflater inflaterDl = LayoutInflater.from(MOrderDetailTwoActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(MOrderDetailTwoActivity.this,R.style.Dialog).create();
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    tv_title.setText("单个订单最多发送给两个用户，该订单已达到上限！");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    return;
                }
                LayoutInflater inflaterDl = LayoutInflater.from(MOrderDetailTwoActivity.this);
                RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_order_send, null);
                final Dialog dialog = new AlertDialog.Builder(MOrderDetailTwoActivity.this,R.style.Dialog).create();
                dialog.show();
                dialog.getWindow().setContentView(layout);
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialog.setCanceledOnTouchOutside(true);
                RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
                RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
                RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
                final EditText et_phone3 = (EditText) dialog.findViewById(R.id.et_phone3);
                Button btn_phone3 = (Button) dialog.findViewById(R.id.btn_phone3);
                re_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        PermissionUtil permissionUtil = new PermissionUtil(MOrderDetailTwoActivity.this);
                        permissionUtil.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                new PermissionListener() {
                                    @Override
                                    public void onGranted() {
                                        //所有权限都已经授权
                                        Intent intent = new Intent(MOrderDetailTwoActivity.this,AddressListTwoActivity.class);
                                        intent.putExtra("biaoshi","03");
                                        intent.putExtra("orderId",orderId);
                                        intent.putExtra("orderBody",orderBody);
                                        intent.putExtra("hasId1",send_id1);
                                        intent.putExtra("hasId2",send_id2);
                                        intent.putExtra("hasId3",userId);
                                        intent.putExtra("hasId4",DemoHelper.getInstance().getCurrentUsernName());
                                        startActivityForResult(intent,0);
                                    }
                                    @Override
                                    public void onDenied(List<String> deniedPermission) {
                                        //Toast第一个被拒绝的权限
                                        Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限！",Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onShouldShowRationale(List<String> deniedPermission) {
                                        //Toast第一个勾选不在提示的权限
                                        Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                re_item2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent3 = new Intent(MOrderDetailTwoActivity.this,FriendActivity.class);
                        intent3.putExtra("biaoshi","03");
                        intent3.putExtra("orderId",orderId);
                        intent3.putExtra("orderBody",orderBody);
                        intent3.putExtra("hasId1",send_id1);
                        intent3.putExtra("hasId2",send_id2);
                        intent3.putExtra("hasId3",userId);
                        intent3.putExtra("hasId4",DemoHelper.getInstance().getCurrentUsernName());
                        startActivityForResult(intent3,0);
                    }
                });
                btn_phone3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String userName = et_phone3.getText().toString().trim();
                        if (TextUtils.isEmpty(userName)||userName.length()!=11) {
                            Toast.makeText(getApplicationContext(), "请输入正确格式的电话号码!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (userName.equals(DemoHelper.getInstance().getCurrentUsernName())||userName.equals(userId)||userName.equals(send_id1)||userName.equals(send_id2)){
                            LayoutInflater inflaterDl = LayoutInflater.from(MOrderDetailTwoActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                            final Dialog dialog2 = new AlertDialog.Builder(MOrderDetailTwoActivity.this,R.style.Dialog).create();
                            dialog2.show();
                            dialog2.getWindow().setContentView(layout);
                            dialog2.setCanceledOnTouchOutside(false);
                            dialog2.setCancelable(false);
                            TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                            Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                            tv_title.setText("该账号已在本个订单中,无需再次发送！");
                            btn_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog2.dismiss();
                                }
                            });
                            return;
                        }
                        queryUserInfo(userName);
                    }
                });
                re_item4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        tv_qiye_shanghu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MOrderDetailTwoActivity.this,QianMingActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("userId",userId);
                intent.putExtra("biaoshi","12");
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            orderDetailPresenter.loadOrderDetail(orderId);
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
                Intent intent = new Intent(MOrderDetailTwoActivity.this,QianMingActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("biaoshi","15");
                startActivity(intent);
                finish();
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MOrderDetailTwoActivity.this,QianMingActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("biaoshi","16");
                intent.putExtra("userId",userId);
                intent.putExtra("balance",orderSum);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView5() {
        btnCansul.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
    }
    private void initView6() {
        btnCansul.setVisibility(View.GONE);
        btnCommit.setVisibility(View.VISIBLE);
        btnCommit.setText("售后反馈单");
        btnCommit.setEnabled(true);
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MOrderDetailTwoActivity.this,FWFKListActivity.class).putExtra("biaoshi","02").putExtra("orderId",orderId));
            }
        });
    }
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }
    @Override
    public void updateThisUser(Userful allUser) {
        String trName = TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName();
        tv_name.setText(trName);
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
        if (orderState.equals("03")){
            String str;
            if (company!=null&&!"".equals(company)&&!company.equalsIgnoreCase("null")){
                str = company;
            }else {
                str = trName;
            }
            tv_qiye_xiaofei.setText(str+"已验资");
            tv_qiye_xiaofei.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
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

    @Override
    public void updateCurrentOrderDetail(OrderDetail orderDetail) {
        image1 = TextUtils.isEmpty(orderDetail.getImage1())?"":orderDetail.getImage1();
        image2 = TextUtils.isEmpty(orderDetail.getImage2())?"":orderDetail.getImage2();
        image3 = TextUtils.isEmpty(orderDetail.getImage3())?"":orderDetail.getImage3();
        image4 = TextUtils.isEmpty(orderDetail.getImage2())?"":orderDetail.getImage4();
        time1 = TextUtils.isEmpty(orderDetail.getTime1())?"":orderDetail.getTime1();
        time2 = TextUtils.isEmpty(orderDetail.getTime2())?"":orderDetail.getTime2();
        time3 = TextUtils.isEmpty(orderDetail.getTime3())?"":orderDetail.getTime3();
        resv4 = TextUtils.isEmpty(orderDetail.getResv4())?"":orderDetail.getResv4();
        resv5 = TextUtils.isEmpty(orderDetail.getResv5())?"":orderDetail.getResv5();
        oSignature = TextUtils.isEmpty(orderDetail.getoSignature())?"":orderDetail.getoSignature();
        mSignature = TextUtils.isEmpty(orderDetail.getmSignature())?"":orderDetail.getmSignature();
        send_id1 = orderDetail.getSend_id1();
        send_id2 = orderDetail.getSend_id2();
        orderProject = orderDetail.getOrderProject();
        orderNumber = orderDetail.getOrderNumber();
        orderAmt = orderDetail.getOrderAmt();
        orderSum = orderDetail.getOrderSum();
        oImage = TextUtils.isEmpty(orderDetail.getoImage())?"":orderDetail.getoImage();
        mImage = TextUtils.isEmpty(orderDetail.getmImage())?"":orderDetail.getmImage();
        beizhu = orderDetail.getRemark();
        yueding = TextUtils.isEmpty(orderDetail.getResv1())?"":orderDetail.getResv1();
        resv2 = TextUtils.isEmpty(orderDetail.getResv2())?"":orderDetail.getResv2();
        String time4 = TextUtils.isEmpty(orderDetail.getTime4())?"00":orderDetail.getTime4();
        if (resv5!=null&&!"".equals(resv5)&&!resv5.equalsIgnoreCase("null")){
            String[] str = resv5.split("\\|");
            if (str.length>2){
                String baifen = str[2];
                tv_ticheng.setText(baifen);
            }else {
                tv_ticheng.setText("100%");
            }
        }else {
            tv_ticheng.setText("100%");
        }
        if (send_id2!=null&&!"".equals(send_id2)&&!send_id2.equalsIgnoreCase("null")){
            iv_jiantou.setVisibility(View.VISIBLE);
            ll2_paidan.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll3_paidan.setVisibility(View.GONE);
            biaoshi = "02";
            if (time2==null||"".equals(time2)){
                tev_paidan_kehu.setVisibility(View.INVISIBLE);
                tv_paidan_kehu_time.setVisibility(View.INVISIBLE);
            }else {
                tev_paidan_kehu.setVisibility(View.VISIBLE);
                tv_paidan_kehu_time.setVisibility(View.VISIBLE);
                tv_paidan_kehu_time.setText(time2);
            }
            if (time3==null||"".equals(time3)){
                tev_paidan_caiwu.setVisibility(View.INVISIBLE);
                tv_paidan_caiwu_time.setVisibility(View.INVISIBLE);
            }else {
                tev_paidan_caiwu.setVisibility(View.VISIBLE);
                tv_paidan_caiwu_time.setVisibility(View.VISIBLE);
                tv_paidan_caiwu_time.setText(time3);
            }
            if (!image3.equals("")){
                tv_paidan_kehu.setText("客户已确认");
            }
            if (!image1.equals("")){
                tv_paidan_kehu.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_PAIDANQIANZI + image1,iv_paidan_kehu);
            }
            if (!image4.equals("")){
                tv_paidan_caiwu.setText("客户已确认");
            }
            if (!image2.equals("")){
                tv_paidan_caiwu.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_PAIDANQIANZI + image2,iv_paidan_caiwu);
            }
        }else if (send_id1!=null&&!"".equals(send_id1)&&!send_id1.equalsIgnoreCase("null")){
            iv_jiantou.setVisibility(View.VISIBLE);
            ll2_paidan.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll3_paidan.setVisibility(View.VISIBLE);
            biaoshi = "01";
            if (time2==null||"".equals(time2)){
                rl_paidan_id1.setVisibility(View.GONE);
                tev_paidan_id1.setVisibility(View.INVISIBLE);
            }else {
                rl_paidan_id1.setVisibility(View.VISIBLE);
                tev_paidan_id1.setVisibility(View.VISIBLE);
                tv_paidan_id1_time.setText(time2);
            }
            if (!image3.equals("")){
                tv_paidan_id1.setText("客户已确认");
            }
            if (!image1.equals("")){
                tv_paidan_id1.setVisibility(View.INVISIBLE);
                ImageLoader.getInstance().displayImage(FXConstant.URL_PAIDANQIANZI + image1,iv_paidan_id1);
            }
        }else {
            iv_jiantou.setVisibility(View.GONE);
            ll2_paidan.setVisibility(View.GONE);
            ll2.setVisibility(View.GONE);
            ll3_paidan.setVisibility(View.GONE);
            biaoshi = "00";
        }
        if (!oSignature.equals("")){
            tv_qiye_xiaofei.setText("客户已确认");
        }
        if (!mSignature.equals("")){
            tv_qiye_shanghu.setText("商家已确认");
        }
        if (time1==null||"".equals(time1)){
            tev_qiye_shanghu.setVisibility(View.INVISIBLE);
            tv_qiye_time_shanghu.setVisibility(View.INVISIBLE);
        }else {
            tev_qiye_shanghu.setVisibility(View.VISIBLE);
            tv_qiye_time_shanghu.setVisibility(View.VISIBLE);
        }
        if (resv4==null||"".equals(resv4)){
            tev_qiye_xiaofei.setVisibility(View.VISIBLE);
            tv_qiye_time_xiaofei.setVisibility(View.VISIBLE);
            tv_qiye_time_xiaofei.setText(orderTime);
        }else {
            tev_qiye_xiaofei.setVisibility(View.VISIBLE);
            tev_qiye_xiaofei.setText("完成:");
            tv_qiye_time_xiaofei.setVisibility(View.VISIBLE);
            tv_qiye_time_xiaofei.setText(resv4);
        }
        tv_qiye_time_shanghu.setText(time1);
        if ((mImage==null&&mSignature==null)||(mImage.equals("")&&mSignature.equals(""))||(mImage.equalsIgnoreCase("null")&&mSignature.equalsIgnoreCase("null"))){
            iv_jiantou.setImageResource(R.drawable.fuzejiantouone);
        }else if ((image1==null&&image3==null)||(image1.equals("")&&image3.equals(""))||(image1.equalsIgnoreCase("null")&&image3.equalsIgnoreCase("null"))){
            if (send_id2==null||"".equals(send_id2)||send_id2.equalsIgnoreCase(null)) {
                iv_jiantou.setImageResource(R.drawable.fuzejiantousix);
            }else {
                iv_jiantou.setImageResource(R.drawable.fuzejiantoutwo);
            }
        }else if ((image2==null&&image4==null)||(image2.equals("")&&image4.equals(""))||(image2.equalsIgnoreCase("null")&&image4.equalsIgnoreCase("null"))){
            if (send_id2==null||"".equals(send_id2)||send_id2.equalsIgnoreCase(null)) {
                iv_jiantou.setImageResource(R.drawable.fuzejiantoufour);
            }else {
                iv_jiantou.setImageResource(R.drawable.fuzejiantouthree);
            }
        }else if ((oImage==null&&oSignature==null)||(oImage.equals("")&&oSignature.equals(""))||(oImage.equalsIgnoreCase("null")&&oSignature.equals("null"))){
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufour);
        }else {
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufive);
        }
        if (oImage!=null&&resv4!=null&&!"".equals(oImage)&&!"".equals(resv4)){
            iv_jiantou.setImageResource(R.drawable.fuzejiantoufive);
        }
        if (!oImage.equals("")){
            tv_qiye_xiaofei.setVisibility(View.INVISIBLE);
            String[] avatar = oImage.split("\\|");
            oImage = avatar[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN + oImage,ivkehu);
        }
        if (!mImage.equals("")) {
            tv_qiye_shanghu.setVisibility(View.INVISIBLE);
            String[] avatar = mImage.split("\\|");
            mImage = avatar[0];
            ImageLoader.getInstance().displayImage(FXConstant.URL_SIGN + mImage,ivshangjia);
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
        et_your_project.setTextColor(Color.BLACK);
        et_qianshou_dizhi.setTextColor(Color.BLACK);
        et_dingdan_bianhao.setTextColor(Color.BLACK);
        et_your_project.setEnabled(false);
        et_qianshou_dizhi.setEnabled(false);
        et_dingdan_bianhao.setEnabled(false);
        if (beizhu.equals("")){
            llM_beizhu.setVisibility(View.GONE);
        }else {
            etUBeizhu.setText(beizhu);
            etUBeizhu.setTextColor(Color.BLACK);
        }
        cb_fapiao.setEnabled(false);
        if (time4.equals("01")){
            cb_fapiao.setChecked(true);
        }else {
            cb_fapiao.setChecked(false);
        }
    }
    private Bitmap decodeResource(Resources resources, int id) {
        int densityDpi = resources.getDisplayMetrics().densityDpi;
        Bitmap bitmap;
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ALPHA_8;
        if (densityDpi > DisplayMetrics.DENSITY_HIGH) {
            opts.inTargetDensity = value.density;
            bitmap = BitmapFactory.decodeResource(resources, id, opts);
        }else{
            bitmap = BitmapFactory.decodeResource(resources, id);
        }
        return bitmap;
    }

    private Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
        return resizedBitmap;
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

}