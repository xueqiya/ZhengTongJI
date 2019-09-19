package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.widget.CircleImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/8.
 */

public class SelectTradeUserActivity extends BaseActivity {


    private RelativeLayout rl_sex;
    private TextView tvName,tvNianLing;
    private TextView tvCompany,tv_company_count;
    private TextView tvTitleA,tvBao1,tvBao2,tvBao3,tvBao4,tvBu1,tvBu2,tvBu3,tvBu4,ivZYTP1,ivZYTP2,ivZYTP3,ivZYTP4;
    private TextView tvDistance;
    private ImageView ivSex;
    private CircleImageView ivHead;
    private TextView tvProject1,tvProject2,tvProject3,tvProject4;
    private TextView tvQianming;
    private LinearLayout ll_one,ll_two,ll_three,ll_four,card;

    private RelativeLayout rl_bg;
    private EditText et_phone;
    private String currentPhone="0";
    private Button bt_mid;

    private String dynamicSeq,createTime;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_selecttradeuser);

        dynamicSeq = getIntent().getStringExtra("dynamicSeq");
        if (dynamicSeq == null){
            dynamicSeq = "0";
        }
        createTime = getIntent().getStringExtra("createTime");
        if (createTime == null){
            createTime = "0";
        }

        initView();

    }

    private void initView(){

        bt_mid = (Button) findViewById(R.id.bt_mid);

        bt_mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SlectUserInfo();
            }
        });

        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        et_phone = (EditText) findViewById(R.id.et_phone);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        card = (LinearLayout) findViewById(R.id.card);
        ll_one = (LinearLayout) findViewById(R.id.ll_one);
        ll_two = (LinearLayout) findViewById(R.id.ll_two);
        ll_three = (LinearLayout) findViewById(R.id.ll_three);
        ll_four = (LinearLayout) findViewById(R.id.ll_four);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvTitleA = (TextView) findViewById(R.id.tv_titl);
        tvBao1 = (TextView) findViewById(R.id.tv_zy1_bao);
        tvBao2 = (TextView) findViewById(R.id.tv_zy2_bao);
        tvBao3 = (TextView) findViewById(R.id.tv_zy3_bao);
        tvBao4 = (TextView) findViewById(R.id.tv_zy4_bao);
        ivZYTP1 = (TextView) findViewById(R.id.iv_zy1_tupian);
        ivZYTP2 = (TextView) findViewById(R.id.iv_zy2_tupian);
        ivZYTP3 = (TextView) findViewById(R.id.iv_zy3_tupian);
        ivZYTP4 = (TextView) findViewById(R.id.iv_zy4_tupian);
        tvNianLing = (TextView) findViewById(R.id.tv_nianling);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tv_company_count = (TextView) findViewById(R.id.tv_company_count);
        ivHead = (CircleImageView) findViewById(R.id.iv_head);
        ivSex = (ImageView) findViewById(R.id.iv_sex);
        tvQianming = (TextView) findViewById(R.id.tv_qianming);
        tvProject1 = (TextView) findViewById(R.id.tv_project_one);
        tvProject2 = (TextView) findViewById(R.id.tv_project_two);
        tvProject3 = (TextView) findViewById(R.id.tv_project_three);
        tvProject4 = (TextView) findViewById(R.id.tv_project_four);
        tvDistance = (TextView) findViewById(R.id.tv_distance);

    }

    //修改动态状态并且存储师傅信息
    private void UpdateDynamicState(){

        String url = FXConstant.URL_UPDATE_DYNAMIC_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "updatePaidan volleyError" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //dynamic/updateDynamicPush  seq  time  orderState  01
                Map<String, String> params = new HashMap<String, String>();
                params.put("dynamicSeq",dynamicSeq );
                params.put("createTime", createTime);
                params.put("orderState","02");
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }



    private void InsertTradeInfo(){

        String url = FXConstant.URL_INSERTOFFLINEORDER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                JSONObject object1 = JSON.parseObject(s);
                String code = object1.getString("code");

                if (code.equals("SUCCESS")){

                    Toast.makeText(SelectTradeUserActivity.this,"派单完成",Toast.LENGTH_SHORT).show();
                    finish();

                }else {

                    Toast.makeText(SelectTradeUserActivity.this,"网络不稳定,稍后重试",Toast.LENGTH_SHORT).show();

                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SelectTradeUserActivity.this,"网络不稳定,稍后重试",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //dynamic/updateDynamicPush  seq  time  orderState  01
                Map<String, String> params = new HashMap<String, String>();

                params.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                params.put("merId",currentPhone);
                params.put("dynamicId",dynamicSeq );
                params.put("createTime", createTime);

                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    private void SlectUserInfo(){

        String phone = et_phone.getText().toString().trim();

        if (phone.length() != 11){

            Toast.makeText(SelectTradeUserActivity.this,"请输入正确手机号",Toast.LENGTH_SHORT).show();

            return;

        }

        if (currentPhone.equals(phone)){

            //直接修改状态

            UpdateDynamicState();

            InsertTradeInfo();

        }else {

            //先查询用户信息 在修改状态

            currentPhone = phone;
            String url = FXConstant.URL_Get_UserInfo + phone;
            StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    JSONObject object1 = JSON.parseObject(s);
                    String code = object1.getString("code");

                    if ("用户名为空".equals(code)){

                        //师傅不是平台的

                        LayoutInflater inflater2 = LayoutInflater.from(SelectTradeUserActivity.this);
                        RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog2 = new AlertDialog.Builder(SelectTradeUserActivity.this,R.style.Dialog).create();
                        dialog2.show();
                        dialog2.getWindow().setContentView(layout2);
                        dialog2.setCanceledOnTouchOutside(true);
                        dialog2.setCancelable(true);
                        TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                        Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                        final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                        btnOK2.setText("确定");
                        btnCancel2.setText("取消");
                        title_tv2.setText("未查到该用户信息,是否确定该账号提供的服务");
                        btnCancel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();

                            }
                        });

                        btnOK2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog2.dismiss();

                                //修改状态
                                UpdateDynamicState();
                                InsertTradeInfo();
                            }
                        });


                    }else if ("SUCCESS".equals(code)){

                        //师傅是平台的
                        JSONObject userInfo = object1.getJSONObject("userInfo");
                        String shareRed = userInfo.getString("shareRed");
                        String friendsNumber = userInfo.getString("friendsNumber");
                        String onceJine = null;
                        if (friendsNumber!=null&&!"".equals(friendsNumber)){
                            onceJine = friendsNumber.split("\\|")[0];
                        }
                        tvName.setText(TextUtils.isEmpty(userInfo.getString("uName")) ? userInfo.getString("uLoginId") : userInfo.getString("uName"));
                        if (shareRed!=null&&!"".equals(shareRed)&& Double.parseDouble(shareRed)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
                            tvName.setTextColor(Color.RED);
                        }else {
                            tvName.setTextColor(Color.BLACK);
                        }
                        String nianLing = TextUtils.isEmpty(userInfo.getString("uAge")) ? "**" : userInfo.getString("uAge");
                        if (nianLing==null||nianLing.equalsIgnoreCase("null")||"".equals(nianLing)){
                            nianLing = "**";
                        }
                        tvNianLing.setText(nianLing);

                        String company = TextUtils.isEmpty(userInfo.getString("uCompany")) ? "暂未加入企业" : userInfo.getString("uCompany");
                        String uNation = userInfo.getString("uNation");
                        String resv5 = userInfo.getString("resv5");
                        String resv6 = userInfo.getString("resv6");
                        if (company == null || company.equals("")) {
                            company = "暂未加入企业";
                        }
                        if ("00".equals(resv6)&&!"1".equals(uNation)){
                            company = "暂未加入企业";
                        }
                        if (resv5==null||"".equals(resv5)){
                            company = "暂未加入企业";
                        }
                        try {
                            company = URLDecoder.decode(company, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        String member = userInfo.getString("menberNum");
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

                        UserAll allUser = new UserAll();
                        com.alibaba.fastjson.JSONArray proList = userInfo.getJSONArray("userProfessions");
                        if (proList != null) {
                            for (int j = 0; j < proList.size(); j++) {
                                com.alibaba.fastjson.JSONObject proObj = proList.getJSONObject(j);
                                String upName = "";
                                String margin = "0";
                                String image = "";

                                if (proObj.getString("upName") != null){
                                    upName = proObj.getString("upName");
                                }
                                if (proObj.getString("margin") != null){
                                    margin = proObj.getString("margin");
                                }
                                if (proObj.getString("image") != null){
                                    image = proObj.getString("image");
                                }

                                String upTypeId = proObj.getString("upTypeId");

                                if (upTypeId.equals("1")) {
                                    allUser.setpL1(upName);
                                    allUser.setImage1(image);
                                    allUser.setMargen1(margin);
                                } else if (upTypeId.equals("2")) {
                                    allUser.setpL2(upName);
                                    allUser.setImage2(image);
                                    allUser.setMargen2(margin);
                                } else if (upTypeId.equals("3")) {
                                    allUser.setpL3(upName);
                                    allUser.setImage3(image);
                                    allUser.setMargen3(margin);
                                } else if (upTypeId.equals("4")) {
                                    allUser.setpL4(upName);
                                    allUser.setImage4(image);
                                    allUser.setMargen4(margin);
                                }
                            }
                        }

                        tvProject1.setText(allUser.getpL1());
                        tvProject2.setText(allUser.getpL2());
                        tvProject3.setText(allUser.getpL3());
                        tvProject4.setText(allUser.getpL4());
                        String image1 = allUser.getImage1();
                        String image2 = allUser.getImage2();
                        String image3 = allUser.getImage3();
                        String image4 = allUser.getImage4();
                        String margan1 = allUser.getMargen1();
                        String margan2 = allUser.getMargen2();
                        String margan3 = allUser.getMargen3();
                        String margan4 = allUser.getMargen4();
                        if (allUser.getpL1()==null||allUser.getpL1().equals("")){
                            ll_one.setVisibility(View.GONE);
                        }else {
                            ll_one.setVisibility(View.VISIBLE);
                        }
                        if (allUser.getpL2()==null||allUser.getpL2().equals("")){
                            ll_two.setVisibility(View.GONE);
                        }else {
                            ll_two.setVisibility(View.VISIBLE);
                        }
                        if (allUser.getpL3()==null||allUser.getpL3().equals("")){
                            ll_three.setVisibility(View.GONE);
                        }else {
                            ll_three.setVisibility(View.VISIBLE);
                        }
                        if (allUser.getpL4()==null||allUser.getpL4().equals("")){
                            ll_four.setVisibility(View.GONE);
                        }else {
                            ll_four.setVisibility(View.VISIBLE);
                        }
                        if (image1!=null&&!"".equals(image1)) {
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
                        if (image2!=null&&!"".equals(image2)) {
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
                        if (image3!=null&&!"".equals(image3)) {
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
                        if (image4!=null&&!"".equals(image4)) {
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

                        String head = "";

                        if (userInfo.getString("uImage") != null){
                            head = userInfo.getString("uImage");
                        }

                        if (head.length() > 40) {
                            tvTitleA.setVisibility(View.INVISIBLE);
                            ivHead.setVisibility(View.VISIBLE);
                            String[] orderProjectArray = head.split("\\|");
                            head = orderProjectArray[0];
                        }
                        if (!(head.equals("") || head.equals(null))) {
                            tvTitleA.setVisibility(View.INVISIBLE);
                            ivHead.setVisibility(View.VISIBLE);
                            //ImageLoader.getInstance().displayImage(FXConstant.URL_AVATAR+head,holder.ivHead, DemoApplication.mOptions);
                            Glide.with(SelectTradeUserActivity.this).load(FXConstant.URL_AVATAR+head).into(ivHead);

                        } else {
                            ivHead.setVisibility(View.INVISIBLE);
                            tvTitleA.setVisibility(View.VISIBLE);
                            tvTitleA.setText(TextUtils.isEmpty(allUser.getuName()) ? allUser.getuId() : allUser.getuName());
                        }
                        if (("00").equals(allUser.getuSex())) {
                            ivSex.setImageResource(R.drawable.nv);
                            //保 255 62 74  图 255 170 76
                            tvNianLing.setBackgroundColor(Color.rgb(234,121,219));
                            tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_red);
                        } else {
                            ivSex.setImageResource(R.drawable.nan);
                            tvNianLing.setBackgroundResource(R.color.accent_blue);
                            tvTitleA.setBackgroundResource(R.drawable.fx_bg_text_gra);
                        }

                        tvDistance.setText("");

                        String sign = allUser.getuSignaTure();
                        if (sign==null||"".equals(sign)){
                            sign = "未设置简介";
                        }

                        tvQianming.setText(sign);

                        rl_bg.setVisibility(View.VISIBLE);

                        LayoutInflater inflater2 = LayoutInflater.from(SelectTradeUserActivity.this);
                        RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                        final Dialog dialog2 = new AlertDialog.Builder(SelectTradeUserActivity.this,R.style.Dialog).create();
                        dialog2.show();
                        dialog2.getWindow().setContentView(layout2);
                        dialog2.setCanceledOnTouchOutside(true);
                        dialog2.setCancelable(true);
                        TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                        Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                        final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                        btnOK2.setText("确定");
                        btnCancel2.setText("取消");
                        title_tv2.setText("核对用户信息再次点击确定即完成");
                        btnCancel2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();

                            }
                        });

                        btnOK2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog2.dismiss();

                                //修改状态
                                UpdateDynamicState();
                                InsertTradeInfo();
                            }
                        });


                    }else {

                        Toast.makeText(SelectTradeUserActivity.this,"网络不稳定,稍后重试",Toast.LENGTH_SHORT).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                    Toast.makeText(SelectTradeUserActivity.this,"网络不稳定,稍后重试",Toast.LENGTH_SHORT).show();

                }

            });

            MySingleton.getInstance(SelectTradeUserActivity.this).addToRequestQueue(request);

        }


    }


}
