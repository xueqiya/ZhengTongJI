package com.sangu.apptongji.main.qiye;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.avtivity.DatePickActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.QianMingActivity;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.FileUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-01-10.
 */

public class QingjiaActivity extends BaseActivity implements View.OnClickListener,IUAZView{
    private Button btn_qingjia_qi;
    private Button btn_qingjia_zhi;
    private Button btn_commit;
    private EditText et_qingjia_qi;
    private EditText et_qingjia_zhi;
    private EditText et_qingjia_yuyin;
    private RelativeLayout rl_qi;
    private RelativeLayout rl_zhi;
    private TextView tv_lingdao;
    private TextView tv_yuangong;
    private TextView tev_yuangong;
    private TextView tv_yuangong_time;
    private TextView tev_lingdao;
    private TextView tv_lingdao_time;
    private ImageView iv_yuangong;
    private ImageView iv_lingdao;
    private String path1="",path2="",biaoshi,name, userId,qiyeId,createTime,remark,biaoshi2,managerId,lat,lng;
    private ProgressDialog dialog=null;
    RelativeLayout rl_sex;
    TextView tvName,tvNianLing;
    TextView tvCompany,tv_company_count;
    TextView tvTitleA,tvBao1,tvBao2,tvBao3,tvBao4,tvBu1,tvBu2,tvBu3,tvBu4,ivZYTP1,ivZYTP2,ivZYTP3,ivZYTP4;
    TextView tvDistance;
    ImageView ivSex;
    ImageView ivHead;
    TextView tvProject1,tvProject2,tvProject3,tvProject4;
    TextView tvQianming;
    LinearLayout ll_one,ll_two,ll_three,ll_four;

    private IUAZPresenter presenter=null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"));
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_qingjia);
        btn_qingjia_qi = (Button) findViewById(R.id.btn_qingjia_qi);
        btn_qingjia_zhi = (Button) findViewById(R.id.btn_qingjia_zhi);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        et_qingjia_qi = (EditText) findViewById(R.id.et_qingjia_qi);
        et_qingjia_zhi = (EditText) findViewById(R.id.et_qingjia_zhi);
        et_qingjia_yuyin = (EditText) findViewById(R.id.et_qingjia_yuyin);
        rl_qi = (RelativeLayout) findViewById(R.id.ll2);
        rl_zhi = (RelativeLayout) findViewById(R.id.ll3);
        tv_lingdao = (TextView) findViewById(R.id.tv_lingdao);
        tv_yuangong = (TextView) findViewById(R.id.tv_yuangong);
        tev_yuangong = (TextView) findViewById(R.id.tev_yuangong);
        tv_yuangong_time = (TextView) findViewById(R.id.tv_yuangong_time);
        tev_lingdao = (TextView) findViewById(R.id.tev_lingdao);
        tv_lingdao_time = (TextView) findViewById(R.id.tv_lingdao_time);
        iv_yuangong = (ImageView) findViewById(R.id.iv_yuangong);
        iv_lingdao = (ImageView) findViewById(R.id.iv_lingdao);
        presenter = new UAZPresenter(this,this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        managerId = this.getIntent().hasExtra("managerId")?this.getIntent().getStringExtra("managerId"):"";
        biaoshi = this.getIntent().hasExtra("biaoshi")?this.getIntent().getStringExtra("biaoshi"):"";
        biaoshi2 = this.getIntent().hasExtra("biaoshi2")?this.getIntent().getStringExtra("biaoshi2"):"";
        initView();
        if ("".equals(biaoshi)) {
            userId = DemoHelper.getInstance().getCurrentUsernName();
            qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
            init();
            setlistener();
        }else {
            name = this.getIntent().hasExtra("name")?this.getIntent().getStringExtra("name"):"";
            userId = this.getIntent().getStringExtra("userId");
            createTime = this.getIntent().getStringExtra("createTime");
            remark = this.getIntent().getStringExtra("remark");
            qiyeId = this.getIntent().getStringExtra("companyId");
            String leaveReason = this.getIntent().getStringExtra("leaveReason");
            String leaveTimeStart = this.getIntent().getStringExtra("leaveTimeStart");
            String leaveTimeEnd = this.getIntent().getStringExtra("leaveTimeEnd");
            String pic1 = this.getIntent().getStringExtra("pic1");
            String pic2 = this.getIntent().getStringExtra("pic2");
            et_qingjia_qi.setEnabled(false);
            et_qingjia_zhi.setEnabled(false);
            et_qingjia_yuyin.setEnabled(false);
            btn_qingjia_qi.setEnabled(false);
            btn_qingjia_zhi.setEnabled(false);
            et_qingjia_qi.setText(leaveTimeStart);
            et_qingjia_zhi.setText(leaveTimeEnd);
            et_qingjia_yuyin.setText(leaveReason);
            if (pic1.length()>0){
                String qingjiaTime = createTime.substring(0, 4) + "-" + createTime.substring(4, 6) + "-" + createTime.substring(6, 8) + " "
                        + createTime.substring(8, 10) + ":" + createTime.substring(10, 12);
                tev_yuangong.setVisibility(View.VISIBLE);
                tv_yuangong_time.setVisibility(View.VISIBLE);
                iv_yuangong.setVisibility(View.VISIBLE);
                tv_yuangong.setVisibility(View.INVISIBLE);
                tv_yuangong_time.setText(qingjiaTime);
                ImageLoader.getInstance().displayImage(FXConstant.URL_QINGJIA_QIANMING + pic1,iv_yuangong);
            }
            if (pic2!=null&&pic2.length()>0){
                btn_commit.setEnabled(false);
                btn_commit.setText("已批准");
                btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
                tev_lingdao.setVisibility(View.VISIBLE);
                tv_lingdao_time.setVisibility(View.VISIBLE);
                iv_lingdao.setVisibility(View.VISIBLE);
                tv_lingdao.setVisibility(View.INVISIBLE);
                tv_lingdao_time.setText(remark);
                ImageLoader.getInstance().displayImage(FXConstant.URL_QINGJIA_QIANMING + pic2,iv_lingdao);
                iv_lingdao.setEnabled(false);
                tv_lingdao.setEnabled(false);
            }else {
                tev_lingdao.setVisibility(View.INVISIBLE);
            }
            setlistener1();
        }
        Log.e("biaoshi2",biaoshi2);
        presenter.loadThisDetail(userId);
        if ("00".equals(biaoshi2)) {
            tv_lingdao.setEnabled(false);
            btn_commit.setText("等待批准");
            btn_commit.setEnabled(false);
            btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
        }
    }

    private void initView() {
        rl_sex = (RelativeLayout) this.findViewById(R.id.rl_sex);
        ll_one = (LinearLayout) this.findViewById(R.id.ll_one);
        ll_two = (LinearLayout) this.findViewById(R.id.ll_two);
        ll_three = (LinearLayout) this.findViewById(R.id.ll_three);
        ll_four = (LinearLayout) this.findViewById(R.id.ll_four);
        tvName = (TextView) this.findViewById(R.id.tv_name);
        tvTitleA = (TextView) this.findViewById(R.id.tv_titl);
        tvBao1 = (TextView) this.findViewById(R.id.tv_zy1_bao);
        tvBao2 = (TextView) this.findViewById(R.id.tv_zy2_bao);
        tvBao3 = (TextView) this.findViewById(R.id.tv_zy3_bao);
        tvBao4 = (TextView) this.findViewById(R.id.tv_zy4_bao);
        ivZYTP1 = (TextView) this.findViewById(R.id.iv_zy1_tupian);
        ivZYTP2 = (TextView) this.findViewById(R.id.iv_zy2_tupian);
        ivZYTP3 = (TextView) this.findViewById(R.id.iv_zy3_tupian);
        ivZYTP4 = (TextView) this.findViewById(R.id.iv_zy4_tupian);
        tvNianLing = (TextView) this.findViewById(R.id.tv_nianling);
        tvCompany = (TextView) this.findViewById(R.id.tv_company);
        tv_company_count = (TextView) this.findViewById(R.id.tv_company_count);
        //holder.tvTime = (TextView) convertView.findViewById(R.id.tv_date);
        ivHead = (ImageView) this.findViewById(R.id.iv_head);
        ivSex = (ImageView) this.findViewById(R.id.iv_sex);
        tvQianming = (TextView) this.findViewById(R.id.tv_qianming);
        tvProject1 = (TextView) this.findViewById(R.id.tv_project_one);
        tvProject2 = (TextView) this.findViewById(R.id.tv_project_two);
        tvProject3 = (TextView) this.findViewById(R.id.tv_project_three);
        tvProject4 = (TextView) this.findViewById(R.id.tv_project_four);
        tvDistance = (TextView) this.findViewById(R.id.tv_distance);
    }

    private void init() {
        tev_lingdao.setVisibility(View.INVISIBLE);
        tev_yuangong.setVisibility(View.INVISIBLE);
        tv_lingdao_time.setVisibility(View.INVISIBLE);
        tv_yuangong_time.setVisibility(View.INVISIBLE);
        et_qingjia_qi.setEnabled(false);
        et_qingjia_zhi.setEnabled(false);
    }
    private void setlistener() {
        tv_yuangong.setOnClickListener(this);
        iv_yuangong.setOnClickListener(this);
        btn_qingjia_qi.setOnClickListener(this);
        btn_qingjia_zhi.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        rl_qi.setOnClickListener(this);
        rl_zhi.setOnClickListener(this);
    }

    private void setlistener1() {
        rl_qi.setFocusable(true);
        rl_qi.setFocusableInTouchMode(true);
        tv_lingdao.setOnClickListener(this);
        iv_lingdao.setOnClickListener(this);
        btn_commit.setText("批准");
        btn_commit.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll2:
                Intent intent = new Intent(QingjiaActivity.this, DatePickActivity.class);
                intent.putExtra("date", "");
                startActivityForResult(intent, 0);
                break;
            case R.id.ll3:
                Intent intent1 = new Intent(QingjiaActivity.this, DatePickActivity.class);
                intent1.putExtra("date", "");
                startActivityForResult(intent1, 1);
                break;
            case R.id.btn_qingjia_qi:
                Intent intent3 = new Intent(QingjiaActivity.this, DatePickActivity.class);
                intent3.putExtra("date", "");
                startActivityForResult(intent3, 0);
                break;
            case R.id.btn_qingjia_zhi:
                Intent intent4 = new Intent(QingjiaActivity.this, DatePickActivity.class);
                intent4.putExtra("date", "");
                startActivityForResult(intent4, 1);
                break;
            case R.id.tv_lingdao:
                Intent intent5 = new Intent(QingjiaActivity.this, QianMingActivity.class);
                intent5.putExtra("biaoshi", "22");
                startActivityForResult(intent5, 3);
                break;
            case R.id.tv_yuangong:
                Intent intent6 = new Intent(QingjiaActivity.this, QianMingActivity.class);
                intent6.putExtra("biaoshi", "21");
                startActivityForResult(intent6, 2);
                break;
            case R.id.iv_lingdao:
                Intent intent7 = new Intent(QingjiaActivity.this, QianMingActivity.class);
                intent7.putExtra("biaoshi", "22");
                startActivityForResult(intent7, 3);
                break;
            case R.id.iv_yuangong:
                Intent intent8 = new Intent(QingjiaActivity.this, QianMingActivity.class);
                intent8.putExtra("biaoshi", "21");
                startActivityForResult(intent8, 2);
                break;
            case R.id.btn_commit:
                if ("".equals(biaoshi)){
                    if ("".equals(path1)){
                        Toast.makeText(QingjiaActivity.this,"签名之后才能申请！",Toast.LENGTH_SHORT).show();
                    }else {
                        tijiao("pic1");
                    }
                }else {
                    if ("".equals(path2)) {
                        Toast.makeText(QingjiaActivity.this,"签名之后才能批准！",Toast.LENGTH_SHORT).show();
                    }else {
                        tijiao("pic2");
                    }
                }
                break;
        }
    }
    public String getcurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        return dateFormat.format(date);
    }
    private void tijiao(String str) {
        dialog = new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("正在提交...");
        dialog.show();
        final List<File> files1 = new ArrayList<File>();
        List<Param> param = new ArrayList<>();
        String url = "";
        if ("pic1".equals(str)) {
            File file1 = new File(path1);
            if (file1.exists()) {
                files1.add(file1);
                Log.e("qingjiaac,","文件已添加s="+files1.size());
            }else {
                Log.e("qingjiaac,","文件不存在"+path1);
            }
            url = FXConstant.URL_INSERTE_QINGJIA;
            param.add(new Param("userId", userId));
            param.add(new Param("userName", tvName.getText().toString()));
            param.add(new Param("companyId", qiyeId));
            param.add(new Param("leaveTimeStart", et_qingjia_qi.getText().toString()));
            param.add(new Param("leaveTimeEnd", et_qingjia_zhi.getText().toString()));
            param.add(new Param("leaveReason", et_qingjia_yuyin.getText().toString()));
        }else {
            File file1 = new File(path2);
            if (file1.exists()) {
                files1.add(file1);
            }
            String time = getcurrentTime();
            time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " "
                    + time.substring(8, 10) + ":" + time.substring(10, 12);
            url = FXConstant.URL_UPDATE_QINGJIA;
            param.add(new Param("userId", userId));
            param.add(new Param("companyId", qiyeId));
            param.add(new Param("creatTime", createTime));
            param.add(new Param("remark", time));
        }
        if (files1.size()>0) {
            OkHttpManager.getInstance().post(str,param,files1,url, new OkHttpManager.HttpCallBack() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    dialog.dismiss();
                    String code = jsonObject.getString("code");
                    if (code.equals("SUCCESS")) {
                        FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory()+"/zhengshier/qianming/"));
                        if (!"".equals(biaoshi)){
                            daka();
                        }else {
                            Toast.makeText(QingjiaActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                            updateQjcount();
                        }
                    } else {
                        Toast.makeText(QingjiaActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String errorMsg) {
                    dialog.dismiss();
                    Toast.makeText(QingjiaActivity.this, "网络繁忙，请稍后再试" + errorMsg, Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), "必须先签名才可以提交！" , Toast.LENGTH_SHORT).show();
        }
    }
    private void sendPushMessage(final String hxid1) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String name = DemoApplication.getInstance().getCurrentUser().getName();
        if (name==null||"".equals(name)){
            name = DemoHelper.getInstance().getCurrentUsernName();
        }
        final String finalName = name;
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",hxid1);
                param.put("body","企业消息");
                param.put("type","06");
                param.put("userId",myId);
                param.put("companyId",qiyeId);
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(QingjiaActivity.this).addToRequestQueue(request);
    }

    private void updateQjcount() {
        String url = FXConstant.URL_UPDATE_UNREADQIYE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                sendPushMessage(managerId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
                sendPushMessage(managerId);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("leaveCount","1");
                param.put("companyId",qiyeId);
                return param;
            }
        };
        MySingleton.getInstance(QingjiaActivity.this).addToRequestQueue(request);
    }

    private void reduceQjcount() {
        String url = FXConstant.URL_UPDATE_UNREADQIYE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("leaveCount","-1");
                param.put("companyId",qiyeId);
                return param;
            }
        };
        MySingleton.getInstance(QingjiaActivity.this).addToRequestQueue(request);
    }

    private void tongji(final String remark, final String qiyeId, final String userId) {
        String url = FXConstant.URL_QIYE_YUANGONGTONGJI;
        StringRequest qDrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(QingjiaActivity.this, "提交成功!", Toast.LENGTH_SHORT).show();
                reduceQjcount();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("网络连接错误",volleyError+"");
                Toast.makeText(QingjiaActivity.this, "网络连接错误...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("companyId", qiyeId);
                params.put("userId", userId);
                if ("03".equals(remark)){
                    params.put("leaveTimes","1");
                }
                return params;
            }
        };
        MySingleton.getInstance(QingjiaActivity.this).addToRequestQueue(qDrequest);
    }
    private void daka() {
        final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        Log.e("qiyeId",qiyeId);
        String clockType = "01";//上班或者下班
        final String remark = "03";
        String url = FXConstant.URL_YUGONG_QIANDAO;
        final String finalClockType = clockType;
        StringRequest qDrequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                tongji(remark,qiyeId,userId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("网络连接错误",volleyError+"");
                Toast.makeText(QingjiaActivity.this, "网络连接错误...", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("companyId", qiyeId);
                params.put("userId", userId);
                params.put("remark", remark);
                params.put("clockType", finalClockType);
                return params;
            }
        };
        MySingleton.getInstance(QingjiaActivity.this).addToRequestQueue(qDrequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 0:
                    if (data!=null){
                        String qingjiaQi = data.getStringExtra("date");
                        qingjiaQi = qingjiaQi.substring(0, 4) + "-" + qingjiaQi.substring(4, 6) + "-" + qingjiaQi.substring(6, 8) + " "
                                + qingjiaQi.substring(8, 10) + ":" + qingjiaQi.substring(10, 12);
                        et_qingjia_qi.setText(qingjiaQi);
                    }
                    break;
                case 1:
                    if (data!=null){
                        String qingjiaZhi = data.getStringExtra("date");
                        qingjiaZhi = qingjiaZhi.substring(0, 4) + "-" + qingjiaZhi.substring(4, 6) + "-" + qingjiaZhi.substring(6, 8) + " "
                                + qingjiaZhi.substring(8, 10) + ":" + qingjiaZhi.substring(10, 12);
                        et_qingjia_zhi.setText(qingjiaZhi);
                    }
                    break;
                case 2:
                    if (data!=null){
                        path1 = data.getStringExtra("xdimageName");
                        tv_yuangong.setVisibility(View.INVISIBLE);
                        iv_yuangong.setVisibility(View.VISIBLE);
                        iv_yuangong.setImageURI(Uri.fromFile(new File(path1)));
                    }
                    break;
                case 3:
                    if (data!=null){
                        path2 = data.getStringExtra("jdimageName");
                        tv_lingdao.setVisibility(View.INVISIBLE);
                        iv_lingdao.setVisibility(View.VISIBLE);
                        iv_lingdao.setImageURI(Uri.fromFile(new File(path2)));
                    }
                    break;
            }
        }
    }

    @Override
    public void updateThisUser(Userful allUser) {
        tvName.setText(TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName());
        String nianLing = TextUtils.isEmpty(allUser.getuAge()) ? "27" : allUser.getuAge();
        tvNianLing.setText(nianLing);
        String company = TextUtils.isEmpty(allUser.getCompany()) ? "暂未加入企业" : allUser.getCompany();
        String uNation = allUser.getuNation();
        String resv5 = allUser.getResv5();
        String resv6 = allUser.getResv6();
        if (company == null || company.equals("")) {
            company = "暂未加入企业";
        }
        if ("00".equals(resv6)&&!"1".equals(uNation)){
            company = "暂未加入企业";
        }
        if (resv5==null||"".equals(resv5)){
            company = "暂未加入企业";
        }
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
        tvCompany.setText(company);
        tvProject1.setText(allUser.getUpName1());
        tvProject2.setText(allUser.getUpName2());
        tvProject3.setText(allUser.getUpName3());
        tvProject4.setText(allUser.getUpName4());
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
        if (!"".equals(image1)&&image1!=null) {
            ivZYTP1.setVisibility(View.VISIBLE);
        }else {
            ivZYTP1.setVisibility(View.GONE);
        }
        if (margan1!=null) {
            if (!"".equals(margan1) && Double.valueOf(margan1) > 0) {
                tvBao1.setVisibility(View.VISIBLE);
            }else {
                tvBao1.setVisibility(View.GONE);
            }
        }
        if (!"".equals(image2)&&image2!=null) {
            ivZYTP2.setVisibility(View.VISIBLE);
        }else {
            ivZYTP2.setVisibility(View.GONE);
        }
        if (margan2!=null) {
            if (!"".equals(margan2) && Double.valueOf(margan2) > 0) {
                tvBao2.setVisibility(View.VISIBLE);
            }else {
                tvBao2.setVisibility(View.GONE);
            }
        }
        if (!"".equals(image3)&&image3!=null) {
            ivZYTP3.setVisibility(View.VISIBLE);
        }else {
            ivZYTP3.setVisibility(View.GONE);
        }
        if (margan3!=null) {
            if (!"".equals(margan3) && Double.valueOf(margan3) > 0) {
                tvBao3.setVisibility(View.VISIBLE);
            }else {
                tvBao3.setVisibility(View.GONE);
            }
        }
        if (!"".equals(image4)&&image4!=null) {
            ivZYTP4.setVisibility(View.VISIBLE);
        }else {
            ivZYTP4.setVisibility(View.GONE);
        }
        if (margan4!=null) {
            if (!"".equals(margan4) && Double.valueOf(margan4) > 0) {
                tvBao4.setVisibility(View.VISIBLE);
            }else {
                tvBao4.setVisibility(View.GONE);
            }
        }
        String head = TextUtils.isEmpty(allUser.getImage()) ? "" : allUser.getImage();
        if (head.length() > 40) {
            tvTitleA.setVisibility(View.INVISIBLE);
            ivHead.setVisibility(View.VISIBLE);
            String[] orderProjectArray = head.split("\\|");
            head = orderProjectArray[0];
        }
        if (!(head.equals("") || head.equals(null))) {
            tvTitleA.setVisibility(View.INVISIBLE);
            ivHead.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+head,ivHead, DemoApplication.mOptions);
        } else {
            ivHead.setVisibility(View.INVISIBLE);
            tvTitleA.setVisibility(View.VISIBLE);
            tvTitleA.setText(TextUtils.isEmpty(allUser.getName()) ? allUser.getLoginId() : allUser.getName());
        }
        if (("00").equals(allUser.getSex())) {
            ivSex.setImageResource(R.drawable.nv);
            //保 255 62 74  图 255 170 76
            rl_sex.setBackgroundColor(Color.rgb(234,121,219));
            tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
        } else {
            ivSex.setImageResource(R.drawable.nan);
            tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }
        lat = DemoApplication.getInstance().getCurrentLat();
        lng = DemoApplication.getInstance().getCurrentLng();
        String resv3 = TextUtils.isEmpty(allUser.getResv3()) ? "" : allUser.getResv3();
        String resv1 = TextUtils.isEmpty(allUser.getResv1()) ? "" : allUser.getResv1();
        String resv2 = TextUtils.isEmpty(allUser.getResv2()) ? "" : allUser.getResv2();
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
                    tvDistance.setText("隐藏");
                }else {
                    tvDistance.setText(str + "km");
                }
            } else {
                tvDistance.setText("3km之内");
            }
        }else {
            tvDistance.setText("3km之内");
        }
        String sign = allUser.getSignaTure();
        if (sign==null||"".equals(sign)){
            sign = "未设置简介";
        }
        tvQianming.setText(sign);
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
