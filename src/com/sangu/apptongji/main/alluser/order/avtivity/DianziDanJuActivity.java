package com.sangu.apptongji.main.alluser.order.avtivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.address.AddressListTwoActivity;
import com.sangu.apptongji.main.alluser.entity.DianziDanju;
import com.sangu.apptongji.main.alluser.presenter.IDjDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.DjDetailPresenter;
import com.sangu.apptongji.main.alluser.view.IDjDetailView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-07-20.
 */

public class DianziDanJuActivity extends BaseActivity implements View.OnClickListener,IDjDetailView{
    private IDjDetailPresenter presenter;
    private String biaoshi;
    private TextView tv_baocun;
    private EditText et_biaoti,et_neirong;
    private ImageView iv_sign1,iv_sign2,iv_sign3,iv_sign4,iv_sign5,iv_sign6,iv_sign7,iv_sign8;
    private TextView tv_sign1,tv_sign2,tv_sign3,tv_sign4,tv_sign5,tv_sign6,tv_sign7,tv_sign8;
    private TextView tev_sign1,tev_sign2,tev_sign3,tev_sign4,tev_sign5,tev_sign6,tev_sign7,tev_sign8;
    private TextView tv_sign1_time,tv_sign2_time,tv_sign3_time,tv_sign4_time,tv_sign5_time,tv_sign6_time,tv_sign7_time,tv_sign8_time;
    private LinearLayout ll_sign2,ll_sign3,ll_sign4;
    private View tv2,tv3,tv4;
    private RelativeLayout rl_button;
    private Button btn_tianjia,btn_quxiao;
    private int current_neirong=1;
    private String path1,path2,path3,path4,path5,path6,path7,path8,title;
    private String time1,time2,time3,time4,time5,time6,time7,time8,timestamp,image1,image2,image3,image4,image5,image6,image7,image8;
    private boolean isBaocun=false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_dianzi_danju);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        initView();
        setListener();
        biaoshi = getIntent().getStringExtra("biaoshi");
        timestamp = getIntent().getStringExtra("timestamp");
        if ("00".equals(biaoshi)){
            isBaocun = true;
        }else {
            isBaocun = false;
            tv_baocun.setText("发送");
            presenter = new DjDetailPresenter(this,this);
            presenter.loadDZdjDetail(DemoHelper.getInstance().getCurrentUsernName(),timestamp);
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    private void setListener() {
        tv_sign1.setOnClickListener(this);
        tv_sign2.setOnClickListener(this);
        tv_sign3.setOnClickListener(this);
        tv_sign4.setOnClickListener(this);
        tv_sign5.setOnClickListener(this);
        tv_sign6.setOnClickListener(this);
        tv_sign7.setOnClickListener(this);
        tv_sign8.setOnClickListener(this);
        tv_baocun.setOnClickListener(this);
        btn_tianjia.setOnClickListener(this);
        btn_quxiao.setOnClickListener(this);
    }

    private void initView() {
        tv_baocun = (TextView) findViewById(R.id.tv_baocun);
        tv_sign1 = (TextView) findViewById(R.id.tv_sign1);
        tv_sign2 = (TextView) findViewById(R.id.tv_sign2);
        tv_sign3 = (TextView) findViewById(R.id.tv_sign3);
        tv_sign4 = (TextView) findViewById(R.id.tv_sign4);
        tv_sign5 = (TextView) findViewById(R.id.tv_sign5);
        tv_sign6 = (TextView) findViewById(R.id.tv_sign6);
        tv_sign7 = (TextView) findViewById(R.id.tv_sign7);
        tv_sign8 = (TextView) findViewById(R.id.tv_sign8);
        tev_sign1 = (TextView) findViewById(R.id.tev_sign1);
        tev_sign2 = (TextView) findViewById(R.id.tev_sign2);
        tev_sign3 = (TextView) findViewById(R.id.tev_sign3);
        tev_sign4 = (TextView) findViewById(R.id.tev_sign4);
        tev_sign5 = (TextView) findViewById(R.id.tev_sign5);
        tev_sign6 = (TextView) findViewById(R.id.tev_sign6);
        tev_sign7 = (TextView) findViewById(R.id.tev_sign7);
        tev_sign8 = (TextView) findViewById(R.id.tev_sign8);
        tv_sign1_time = (TextView) findViewById(R.id.tv_sign1_time);
        tv_sign2_time = (TextView) findViewById(R.id.tv_sign2_time);
        tv_sign3_time = (TextView) findViewById(R.id.tv_sign3_time);
        tv_sign4_time = (TextView) findViewById(R.id.tv_sign4_time);
        tv_sign5_time = (TextView) findViewById(R.id.tv_sign5_time);
        tv_sign6_time = (TextView) findViewById(R.id.tv_sign6_time);
        tv_sign7_time = (TextView) findViewById(R.id.tv_sign7_time);
        tv_sign8_time = (TextView) findViewById(R.id.tv_sign8_time);
        iv_sign1 = (ImageView) findViewById(R.id.iv_sign1);
        iv_sign2 = (ImageView) findViewById(R.id.iv_sign2);
        iv_sign3 = (ImageView) findViewById(R.id.iv_sign3);
        iv_sign4 = (ImageView) findViewById(R.id.iv_sign4);
        iv_sign5 = (ImageView) findViewById(R.id.iv_sign5);
        iv_sign6 = (ImageView) findViewById(R.id.iv_sign6);
        iv_sign7 = (ImageView) findViewById(R.id.iv_sign7);
        iv_sign8 = (ImageView) findViewById(R.id.iv_sign8);
        et_biaoti = (EditText) findViewById(R.id.et_biaoti);
        et_neirong = (EditText) findViewById(R.id.et_neirong);
        ll_sign2 = (LinearLayout) findViewById(R.id.ll_sign2);
        ll_sign3 = (LinearLayout) findViewById(R.id.ll_sign3);
        ll_sign4 = (LinearLayout) findViewById(R.id.ll_sign4);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        rl_button = (RelativeLayout) findViewById(R.id.rl_button);
        btn_tianjia = (Button) findViewById(R.id.btn_tianjia);
        btn_quxiao = (Button) findViewById(R.id.btn_quxiao);
    }

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_baocun:
                if (isBaocun){
                    final String biaoti = et_biaoti.getText().toString().trim();
                    final String neirong = et_neirong.getText().toString().trim();
                    if (TextUtils.isEmpty(biaoti)){
                        Toast.makeText(getApplicationContext(),"请输入标题",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(neirong)){
                        Toast.makeText(getApplicationContext(),"请输入内容",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    LayoutInflater inflater1 = LayoutInflater.from(DianziDanJuActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(DianziDanJuActivity.this,R.style.Dialog).create();
                    dialog1.show();
                    dialog1.getWindow().setContentView(layout1);
                    dialog1.setCanceledOnTouchOutside(true);
                    dialog1.setCancelable(true);
                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                    title.setText("温馨提示");
                    btnOK1.setText("确定");
                    btnCancel1.setText("取消");
                    title_tv1.setText("确定保存该电子单据吗?");
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
                            String url = FXConstant.URL_UPDATE_DZ_DANJU;
                            List<Param> params=new ArrayList<>();
                            if ("00".equals(biaoshi)) {
                                url = FXConstant.URL_INSERT_DZ_DANJU;
                                params.add(new Param("u_id", DemoHelper.getInstance().getCurrentUsernName()));
                                params.add(new Param("content", neirong));
                                params.add(new Param("title", biaoti));
                                params.add(new Param("flag", "05"));
                            }else {
                                params.add(new Param("timestamp", timestamp));
                                params.add(new Param("u_id", DemoHelper.getInstance().getCurrentUsernName()));
                            }
                            if (path1!=null&&!"".equals(path1)){
                                params.add(new Param("time1", getNowTime()));
                            }
                            if (path2!=null&&!"".equals(path2)){
                                params.add(new Param("time2", getNowTime()));
                            }
                            if (path3!=null&&!"".equals(path3)){
                                params.add(new Param("time3", getNowTime()));
                            }
                            if (path4!=null&&!"".equals(path4)){
                                params.add(new Param("time4", getNowTime()));
                            }
                            if (path5!=null&&!"".equals(path5)){
                                params.add(new Param("time5", getNowTime()));
                            }
                            if (path6!=null&&!"".equals(path6)){
                                params.add(new Param("time6", getNowTime()));
                            }
                            if (path7!=null&&!"".equals(path7)){
                                params.add(new Param("time7", getNowTime()));
                            }
                            if (path8!=null&&!"".equals(path8)){
                                params.add(new Param("time8", getNowTime()));
                            }
                            OkHttpManager.getInstance().posts2(params, null, new ArrayList<String>(),null, new ArrayList<String>(), "image1", path1, "image2", path2, "image3", path3, "image4", path4
                                    , "image5", path5, "image6", path6, "image7", path7, "image8", path8, url, new OkHttpManager.HttpCallBack() {
                                        @Override
                                        public void onResponse(JSONObject jsonObject) {
                                            if ("01".equals(biaoshi)){
                                                updateUscount(DemoHelper.getInstance().getCurrentUsernName(),false);
                                            }
                                            Log.e("dianzidanjuac",jsonObject.toString());
                                            Toast.makeText(getApplicationContext(),"保存成功！",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(String errorMsg) {
                                            Log.e("dianzidanjuac,e",errorMsg);
                                            Toast.makeText(getApplicationContext(),"保存失败！",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                }else {
                    LayoutInflater inflaterDl = LayoutInflater.from(DianziDanJuActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_order_send, null);
                    final Dialog dialog = new AlertDialog.Builder(DianziDanJuActivity.this,R.style.Dialog).create();
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
                            PermissionUtil permissionUtil = new PermissionUtil(DianziDanJuActivity.this);
                            permissionUtil.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                                    new PermissionListener() {
                                        @Override
                                        public void onGranted() {
                                            //所有权限都已经授权
                                            Intent intent = new Intent(DianziDanJuActivity.this,AddressListTwoActivity.class);
                                            intent.putExtra("biaoshi","04");
                                            intent.putExtra("title",title);
                                            intent.putExtra("timestamp",timestamp);
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
                            Intent intent3 = new Intent(DianziDanJuActivity.this,FriendActivity.class);
                            intent3.putExtra("biaoshi","04");
                            intent3.putExtra("timestamp",timestamp);
                            startActivityForResult(intent3,0);
                        }
                    });
                    btn_phone3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            String userId = et_phone3.getText().toString().trim();
                            if (TextUtils.isEmpty(userId)||userId.length()!=11) {
                                Toast.makeText(getApplicationContext(), "请输入正确格式的电话号码!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            queryUserInfo(userId);
                        }
                    });
                    re_item4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btn_tianjia:
                if (current_neirong<4) {
                    current_neirong++;
                    if (current_neirong==2){
                        ll_sign2.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                    }else if (current_neirong==3){
                        ll_sign3.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);
                    }else if (current_neirong==4){
                        ll_sign4.setVisibility(View.VISIBLE);
                        tv4.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(DianziDanJuActivity.this,"最多添加八个签名哦...",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_quxiao:
                if (current_neirong>1) {
                    current_neirong--;
                    if (current_neirong==1){
                        if ((image3!=null&&!"".equals(image3))||(image4!=null&&!"".equals(image4))){
                            Toast.makeText(DianziDanJuActivity.this,"没有可以取消的内容了...",Toast.LENGTH_SHORT).show();
                        }else {
                            ll_sign2.setVisibility(View.GONE);
                            tv2.setVisibility(View.GONE);
                        }
                    }else if (current_neirong==2){
                        if ((image5!=null&&!"".equals(image5))||(image6!=null&&!"".equals(image6))){
                            Toast.makeText(DianziDanJuActivity.this,"没有可以取消的内容了...",Toast.LENGTH_SHORT).show();
                        }else {
                            ll_sign3.setVisibility(View.GONE);
                            tv3.setVisibility(View.GONE);
                        }
                    }else if (current_neirong==3){
                        if ((image7!=null&&!"".equals(image7))||(image8!=null&&!"".equals(image8))){
                            Toast.makeText(DianziDanJuActivity.this,"没有可以取消的内容了...",Toast.LENGTH_SHORT).show();
                        }else {
                            ll_sign4.setVisibility(View.GONE);
                            tv4.setVisibility(View.GONE);
                        }
                    }
                }else {
                    Toast.makeText(DianziDanJuActivity.this,"没有可以取消的内容了...",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_sign1:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),1);
                break;
            case R.id.iv_sign1:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),1);
                break;
            case R.id.tv_sign2:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),2);
                break;
            case R.id.iv_sign2:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),2);
                break;
            case R.id.tv_sign3:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),3);
                break;
            case R.id.iv_sign3:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),3);
                break;
            case R.id.tv_sign4:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),4);
                break;
            case R.id.iv_sign4:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),4);
                break;
            case R.id.tv_sign5:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),5);
                break;
            case R.id.iv_sign5:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),5);
                break;
            case R.id.tv_sign6:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),6);
                break;
            case R.id.iv_sign6:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),6);
                break;
            case R.id.tv_sign7:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),7);
                break;
            case R.id.iv_sign7:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),7);
                break;
            case R.id.tv_sign8:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),8);
                break;
            case R.id.iv_sign8:
                startActivityForResult(new Intent(DianziDanJuActivity.this,QianMingActivity.class).putExtra("biaoshi","22"),8);
                break;
        }
    }
    private void sendPushMessage2(final String userId) {
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
                param.put("body","电子单据消息");
                param.put("type","11");
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
        MySingleton.getInstance(DianziDanJuActivity.this).addToRequestQueue(request);
    }

    private void queryUserInfo(final String userId) {
        String url = FXConstant.URL_Get_UserInfo+userId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                sendPushMessage2(userId);
                if ("用户名为空".equals(code)){
                    SendMessage1(userId);
                }else {
                    senddztoUser(userId);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(DianziDanJuActivity.this).addToRequestQueue(request);
    }

    private void SendMessage1(final String userId) {
        final String str;
        if (title.length()>6){
            str = title.substring(0,6)+"...";
        }else {
            str = title;
        }
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                senddztoUser(userId);
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
                param.put("message", "【正事多】单据"+str+",需要您签字,请注册手机端“正事多”在电子单据中查看");
                param.put("telNum", userId);
                Log.e("utorderdeac,sm1",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(DianziDanJuActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void senddztoUser(final String username) {
        String url = FXConstant.URL_FASONG_DZ_DANJU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("friendac,s",s);
                updateUscount(username,true);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){
                    Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"发送失败！",Toast.LENGTH_SHORT).show();
                Log.e("friendac",volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("timestamp",timestamp);
                param.put("u_id",username);
                return param;
            }
        };
        MySingleton.getInstance(DianziDanJuActivity.this).addToRequestQueue(request);
    }

    private void updateUscount(final String username,final boolean add) {
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
                if (add) {
                    param.put("billCount", "1");
                }else {
                    param.put("billCount", "-1");
                }
                param.put("userId",username);
                return param;
            }
        };
        MySingleton.getInstance(DianziDanJuActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 1:
                    isBaocun = true;
                    tv_baocun.setText("保存");
                    path1 = data.getStringExtra("jdimageName");
                    tv_sign1.setVisibility(View.INVISIBLE);
                    iv_sign1.setVisibility(View.VISIBLE);
                    iv_sign1.setImageURI(Uri.fromFile(new File(path1)));
                    break;
                case 2:
                    isBaocun = true;
                    tv_baocun.setText("保存");
                    path2 = data.getStringExtra("jdimageName");
                    tv_sign2.setVisibility(View.INVISIBLE);
                    iv_sign2.setVisibility(View.VISIBLE);
                    iv_sign2.setImageURI(Uri.fromFile(new File(path2)));
                    break;
                case 3:
                    isBaocun = true;
                    tv_baocun.setText("保存");
                    path3 = data.getStringExtra("jdimageName");
                    tv_sign3.setVisibility(View.INVISIBLE);
                    iv_sign3.setVisibility(View.VISIBLE);
                    iv_sign3.setImageURI(Uri.fromFile(new File(path3)));
                    break;
                case 4:
                    isBaocun = true;
                    tv_baocun.setText("保存");
                    path4 = data.getStringExtra("jdimageName");
                    tv_sign4.setVisibility(View.INVISIBLE);
                    iv_sign4.setVisibility(View.VISIBLE);
                    iv_sign4.setImageURI(Uri.fromFile(new File(path4)));
                    break;
                case 5:
                    isBaocun = true;
                    tv_baocun.setText("保存");
                    path5 = data.getStringExtra("jdimageName");
                    tv_sign5.setVisibility(View.INVISIBLE);
                    iv_sign5.setVisibility(View.VISIBLE);
                    iv_sign5.setImageURI(Uri.fromFile(new File(path5)));
                    break;
                case 6:
                    isBaocun = true;
                    tv_baocun.setText("保存");
                    path6 = data.getStringExtra("jdimageName");
                    tv_sign6.setVisibility(View.INVISIBLE);
                    iv_sign6.setVisibility(View.VISIBLE);
                    iv_sign6.setImageURI(Uri.fromFile(new File(path6)));
                    break;
                case 7:
                    isBaocun = true;
                    tv_baocun.setText("保存");
                    path7 = data.getStringExtra("jdimageName");
                    tv_sign7.setVisibility(View.INVISIBLE);
                    iv_sign7.setVisibility(View.VISIBLE);
                    iv_sign7.setImageURI(Uri.fromFile(new File(path7)));
                    break;
                case 8:
                    isBaocun = true;
                    tv_baocun.setText("保存");
                    path8 = data.getStringExtra("jdimageName");
                    tv_sign8.setVisibility(View.INVISIBLE);
                    iv_sign8.setVisibility(View.VISIBLE);
                    iv_sign8.setImageURI(Uri.fromFile(new File(path8)));
                    break;
            }
        }
    }

    @Override
    public void updateDzdjDetail(DianziDanju djDetail) {
        title = djDetail.getTitle();
        String count = djDetail.getContent();
        time1 = djDetail.getTime1();
        time2 = djDetail.getTime2();
        time3 = djDetail.getTime3();
        time4 = djDetail.getTime4();
        time5 = djDetail.getTime5();
        time6 = djDetail.getTime6();
        time7 = djDetail.getTime7();
        time8 = djDetail.getTime8();
        image1 = djDetail.getImage1();
        image2 = djDetail.getImage2();
        image3 = djDetail.getImage3();
        image4 = djDetail.getImage4();
        image5 = djDetail.getImage5();
        image6 = djDetail.getImage6();
        image7 = djDetail.getImage7();
        image8 = djDetail.getImage8();
        et_biaoti.setText(title);
        et_neirong.setText(count);
        et_biaoti.setEnabled(false);
        et_neirong.setEnabled(false);
        String signTime1,signTime2,signTime3,signTime4,signTime5,signTime6,signTime7,signTime8;
        if ((image1!=null&&!"".equals(image1)&&!image1.equalsIgnoreCase("null"))||(image2!=null&&!"".equals(image2)&&!image2.equalsIgnoreCase("null"))){
            current_neirong = 1;
            if (image1!=null&&!"".equals(image1)&&!image1.equalsIgnoreCase("null")) {
                ImageLoader.getInstance().displayImage(FXConstant.URL_DZDANJU + image1, iv_sign1);
                signTime1 = time1.substring(0,4)+"-"+time1.substring(4,6)+"-"+time1.substring(6,8)+" " +time1.substring(8,10)+":"+time1.substring(10,12);
                tv_sign1.setVisibility(View.INVISIBLE);
                tev_sign1.setVisibility(View.VISIBLE);
                tv_sign1_time.setVisibility(View.VISIBLE);
                tv_sign1_time.setText(signTime1);
                tv_sign1.setEnabled(false);
                iv_sign1.setEnabled(false);
            }
            if (image2!=null&&!"".equals(image2)&&!image2.equalsIgnoreCase("null")) {
                ImageLoader.getInstance().displayImage(FXConstant.URL_DZDANJU + image2, iv_sign2);
                signTime2 = time2.substring(0,4)+"-"+time2.substring(4,6)+"-"+time2.substring(6,8)+" " +time2.substring(8,10)+":"+time2.substring(10,12);
                tv_sign2.setVisibility(View.INVISIBLE);
                tev_sign2.setVisibility(View.VISIBLE);
                tv_sign2_time.setVisibility(View.VISIBLE);
                tv_sign2_time.setText(signTime2);
                tv_sign2.setEnabled(false);
                iv_sign2.setEnabled(false);
            }
            if ((image3!=null&&!"".equals(image3)&&!image3.equalsIgnoreCase("null"))||(image4!=null&&!"".equals(image4)&&!image4.equalsIgnoreCase("null"))){
                current_neirong = 2;
                ll_sign2.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                if (image3!=null&&!"".equals(image3)&&!image3.equalsIgnoreCase("null")) {
                    ImageLoader.getInstance().displayImage(FXConstant.URL_DZDANJU + image3, iv_sign3);
                    signTime3 = time3.substring(0,4)+"-"+time3.substring(4,6)+"-"+time3.substring(6,8)+" " +time3.substring(8,10)+":"+time3.substring(10,12);
                    tv_sign3.setVisibility(View.INVISIBLE);
                    tev_sign3.setVisibility(View.VISIBLE);
                    tv_sign3_time.setVisibility(View.VISIBLE);
                    tv_sign3_time.setText(signTime3);
                    tv_sign3.setEnabled(false);
                    iv_sign3.setEnabled(false);
                }
                if (image4!=null&&!"".equals(image4)&&!image4.equalsIgnoreCase("null")) {
                    ImageLoader.getInstance().displayImage(FXConstant.URL_DZDANJU + image4, iv_sign4);
                    signTime4 = time4.substring(0,4)+"-"+time4.substring(4,6)+"-"+time4.substring(6,8)+" " +time4.substring(8,10)+":"+time4.substring(10,12);
                    tv_sign4.setVisibility(View.INVISIBLE);
                    tev_sign4.setVisibility(View.VISIBLE);
                    tv_sign4_time.setVisibility(View.VISIBLE);
                    tv_sign4_time.setText(signTime4);
                    tv_sign4.setEnabled(false);
                    iv_sign4.setEnabled(false);
                }
                if ((image5!=null&&!"".equals(image5)&&!image5.equalsIgnoreCase("null"))||(image6!=null&&!"".equals(image6)&&!image6.equalsIgnoreCase("null"))){
                    current_neirong = 3;
                    ll_sign3.setVisibility(View.VISIBLE);
                    tv3.setVisibility(View.VISIBLE);
                    if (image5!=null&&!"".equals(image5)&&!image5.equalsIgnoreCase("null")) {
                        ImageLoader.getInstance().displayImage(FXConstant.URL_DZDANJU + image5, iv_sign5);
                        signTime5 = time5.substring(0,4)+"-"+time5.substring(4,6)+"-"+time5.substring(6,8)+" " +time5.substring(8,10)+":"+time5.substring(10,12);
                        tv_sign5.setVisibility(View.INVISIBLE);
                        tev_sign5.setVisibility(View.VISIBLE);
                        tv_sign5_time.setVisibility(View.VISIBLE);
                        tv_sign5_time.setText(signTime5);
                        tv_sign5.setEnabled(false);
                        iv_sign5.setEnabled(false);
                    }
                    if (image6!=null&&!"".equals(image6)&&!image6.equalsIgnoreCase("null")) {
                        ImageLoader.getInstance().displayImage(FXConstant.URL_DZDANJU + image6, iv_sign6);
                        signTime6 = time6.substring(0,4)+"-"+time6.substring(4,6)+"-"+time6.substring(6,8)+" " +time6.substring(8,10)+":"+time6.substring(10,12);
                        tv_sign6.setVisibility(View.INVISIBLE);
                        tev_sign6.setVisibility(View.VISIBLE);
                        tv_sign6_time.setVisibility(View.VISIBLE);
                        tv_sign6_time.setText(signTime6);
                        tv_sign6.setEnabled(false);
                        iv_sign6.setEnabled(false);
                    }
                    if ((image7!=null&&!"".equals(image7)&&!image7.equalsIgnoreCase("null"))||(image8!=null&&!"".equals(image8)&&!image8.equalsIgnoreCase("null"))){
                        current_neirong = 4;
                        ll_sign4.setVisibility(View.VISIBLE);
                        tv4.setVisibility(View.VISIBLE);
                        if (image7!=null&&!"".equals(image7)&&!image7.equalsIgnoreCase("null")) {
                            ImageLoader.getInstance().displayImage(FXConstant.URL_DZDANJU + image7, iv_sign7);
                            signTime7 = time7.substring(0,4)+"-"+time7.substring(4,6)+"-"+time7.substring(6,8)+" " +time7.substring(8,10)+":"+time7.substring(10,12);
                            tv_sign7.setVisibility(View.INVISIBLE);
                            tev_sign7.setVisibility(View.VISIBLE);
                            tv_sign7_time.setVisibility(View.VISIBLE);
                            tv_sign7_time.setText(signTime7);
                            tv_sign7.setEnabled(false);
                            iv_sign7.setEnabled(false);
                        }
                        if (image8!=null&&!"".equals(image8)&&!image8.equalsIgnoreCase("null")) {
                            ImageLoader.getInstance().displayImage(FXConstant.URL_DZDANJU + image8, iv_sign8);
                            signTime8 = time8.substring(0,4)+"-"+time8.substring(4,6)+"-"+time8.substring(6,8)+" " +time8.substring(8,10)+":"+time8.substring(10,12);
                            tv_sign8.setVisibility(View.INVISIBLE);
                            tev_sign8.setVisibility(View.VISIBLE);
                            tv_sign8_time.setVisibility(View.VISIBLE);
                            tv_sign8_time.setText(signTime8);
                            tv_sign8.setEnabled(false);
                            iv_sign8.setEnabled(false);
                        }
                        rl_button.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ("01".equals(biaoshi)) {
            presenter.loadDZdjDetail(DemoHelper.getInstance().getCurrentUsernName(), timestamp);
        }
    }
    
}
