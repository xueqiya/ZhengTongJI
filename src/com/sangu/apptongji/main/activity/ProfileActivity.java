package com.sangu.apptongji.main.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.presenter.IProfilePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.ProfilePresenter;
import com.sangu.apptongji.main.alluser.view.IProfileView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.FXAlertDialog;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends BaseActivity implements View.OnClickListener ,IProfileView{
    private ImageView iv_back;
    private TextView tv_name;
    private TextView tv_gongsi;
    private TextView tv_sex;
    private TextView tv_sign;
    private TextView tv_birth;
    private TextView tv_major1,tv_major2,tv_major3,tv_major4;
    private TextView tv_consume1,tv_consume2,tv_consume3,tv_consume4,tv_consume5,tv_consume6;
    private TextView tv_hometown;
    private TextView tv_occupation;
    private TextView tv_company_address;
    private TextView tv_nainling;
    private static final int UPDATE_GONGSI = 4;
    private static final int UPDATE_NICK = 5;
    private static final int UPDATE_SIGN = 6;
    private static final int UPDATE_BIRTH = 7;
    private static final int UPDATE_MAJOR1 = 8;
    private static final int UPDATE_MAJOR2 = 9;
    private static final int UPDATE_MAJOR3 = 10;
    private static final int UPDATE_MAJOR4 = 11;
    private static final int UPDATE_CONSUME1 = 14;
    private static final int UPDATE_CONSUME2 = 15;
    private static final int UPDATE_CONSUME3 = 16;
    private static final int UPDATE_CONSUME4 = 17;
    private static final int UPDATE_CONSUME5 = 18;
    private static final int UPDATE_HOMETOWN = 19;
    private static final int UPDATE_CONSUME6 = 20;
    private static final int UPDATE_OCCUPATION = 21;
    private static final int UPDATE_COMADDRESS = 22;
    private static final int UPDATE_SEX_NV = 23;
    private static final int UPDATE_SEX_NAN = 24;
    private static final int UPDATE_NIANLING = 25;
    private boolean hasChange=false;
    private IProfilePresenter presenter;
    private String isFirst,iscFirst;
    private String sign,name,birth,nianLing,hometown,vocational;
    private String image1="",zy1Resv3,upDescribe1,upName1;
    private String image2="",zy2Resv3,upDescribe2,upName2;
    private String image3="",zy3Resv3,upDescribe3,upName3;
    private String image4="",zy4Resv3,upDescribe4,upName4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_myinfo);
        WeakReference<ProfileActivity> reference =  new WeakReference<ProfileActivity>(ProfileActivity.this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        presenter = new ProfilePresenter(this,reference.get());
        isFirst = this.getIntent().getStringExtra("isFirst");
        iscFirst = this.getIntent().getStringExtra("iscFirst");
        initView();
        presenter.updateData();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_name = (TextView) this.findViewById(R.id.tv_name);
        tv_gongsi = (TextView) this.findViewById(R.id.tv_gongsi);
        tv_sex = (TextView) this.findViewById(R.id.tv_sex);
        tv_sign = (TextView) this.findViewById(R.id.tv_sign);
        tv_birth = (TextView) this.findViewById(R.id.tv_birthday);
        tv_major1 = (TextView) this.findViewById(R.id.tv_major1);
        tv_major2 = (TextView) this.findViewById(R.id.tv_major2);
        tv_major3 = (TextView) this.findViewById(R.id.tv_major3);
        tv_major4 = (TextView) this.findViewById(R.id.tv_major4);
        tv_consume1 = (TextView)this.findViewById(R.id.tv_temp_consume1);
        tv_consume2 = (TextView)this.findViewById(R.id.tv_temp_consume2);
        tv_consume3 = (TextView)this.findViewById(R.id.tv_temp_consume3);
        tv_consume4 = (TextView)this.findViewById(R.id.tv_temp_consume4);
        tv_consume5 = (TextView)this.findViewById(R.id.tv_temp_consume5);
        tv_consume6 = (TextView)this.findViewById(R.id.tv_temp_consume6);
        tv_hometown = (TextView) this.findViewById(R.id.tv_hometown);
        tv_company_address = (TextView) this.findViewById(R.id.tv_company_address);
        tv_nainling = (TextView) this.findViewById(R.id.tv_nianling);
        tv_occupation = (TextView) this.findViewById(R.id.tv_occupation);
        //设置监听
        this.findViewById(R.id.re_name).setOnClickListener(this);
        this.findViewById(R.id.re_gongsi).setOnClickListener(this);
        this.findViewById(R.id.re_sex).setOnClickListener(this);
        this.findViewById(R.id.re_sign).setOnClickListener(this);
        this.findViewById(R.id.re_birthday).setOnClickListener(this);
        this.findViewById(R.id.re_major1).setOnClickListener(this);
        this.findViewById(R.id.re_major2).setOnClickListener(this);
        this.findViewById(R.id.re_major3).setOnClickListener(this);
        this.findViewById(R.id.re_major4).setOnClickListener(this);
        this.findViewById(R.id.re_consume1).setOnClickListener(this);
        this.findViewById(R.id.re_consume2).setOnClickListener(this);
        this.findViewById(R.id.re_consume3).setOnClickListener(this);
        this.findViewById(R.id.re_consume4).setOnClickListener(this);
        this.findViewById(R.id.re_consume5).setOnClickListener(this);
        this.findViewById(R.id.re_consume6).setOnClickListener(this);
        this.findViewById(R.id.re_hometown).setOnClickListener(this);
        this.findViewById(R.id.re_nianling).setOnClickListener(this);
        this.findViewById(R.id.re_occupation).setOnClickListener(this);
        this.findViewById(R.id.re_company_address).setOnClickListener(this);
        iv_back.setOnClickListener(this);
        //String nick = user.getuNickName().isEmpty()?"":user.getuNickName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_name:
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_NICK).putExtra("data",name), UPDATE_NICK);
                break;
            case R.id.re_gongsi:
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_GONGSI), UPDATE_GONGSI);
                break;
            case R.id.re_sex:
                showSexDialog();
                break;
            case R.id.re_sign:
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_SIGN).putExtra("data",sign), UPDATE_SIGN);
                break;
            case R.id.re_birthday:
                new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy");
                        monthOfYear = monthOfYear+1;
                        int NowYear = Integer.parseInt(format.format(new Date()));
                        int age = NowYear - year;
                        if (age<=5){
                            Toast.makeText(ProfileActivity.this, "选择的年龄不合法,请从新选择！", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String birth = year+"-"+monthOfYear+"-"+dayOfMonth;
                        tv_birth.setText(birth);
                        tijiao(2,null,birth+"",age+"");
                    }
                }, 1990, 0, 1).show();
                break;
            case R.id.re_major1:
                String isDYC;
                if ("11".equals(isFirst)) {
                    isDYC = "11";
                }else {
                    isDYC = "12";
                }
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR1).putExtra("isFirst",isDYC).putExtra("image",image1)
                        .putExtra("zyResv3",zy1Resv3).putExtra("upName",upName1).putExtra("upDescribe",upDescribe1), UPDATE_MAJOR1);
                break;
            case R.id.re_major2:
                String isDYC2;
                if ("11".equals(isFirst)) {
                    isDYC2 = "11";
                }else {
                    isDYC2 = "12";
                }
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR2).putExtra("isFirst",isDYC2).putExtra("image",image2)
                        .putExtra("zyResv3",zy2Resv3).putExtra("upName",upName2).putExtra("upDescribe",upDescribe2), UPDATE_MAJOR2);
                break;
            case R.id.re_major3:
                String isDYC3;
                if ("11".equals(isFirst)) {
                    isDYC3 = "11";
                }else {
                    isDYC3 = "12";
                }
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR3).putExtra("isFirst",isDYC3).putExtra("image",image3)
                        .putExtra("zyResv3",zy3Resv3).putExtra("upName",upName3).putExtra("upDescribe",upDescribe3), UPDATE_MAJOR3);
                break;
            case R.id.re_major4:
                String isDYC4;
                if ("11".equals(isFirst)) {
                    isDYC4 = "11";
                }else {
                    isDYC4 = "12";
                }
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateTwoActivity.class).putExtra("type", ProfileUpdateTwoActivity.TYPE_MAJOR4).putExtra("isFirst",isDYC4).putExtra("image",image4)
                        .putExtra("zyResv3",zy4Resv3).putExtra("upName",upName4).putExtra("upDescribe",upDescribe4), UPDATE_MAJOR4);
                break;
            case R.id.re_consume1:
                String isCON;
                if ("13".equals(iscFirst)){
                    isCON = "13";
                }else {
                    isCON = "14";
                }
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_CONSUME1).putExtra("iscFirst",isCON), UPDATE_CONSUME1);
                break;
            case R.id.re_consume2:
                String isCON2;
                if ("13".equals(iscFirst)){
                    isCON2 = "13";
                }else {
                    isCON2 = "14";
                }
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_CONSUME2).putExtra("iscFirst",isCON2), UPDATE_CONSUME2);
                break;
            case R.id.re_consume3:
                String isCON3;
                if ("13".equals(iscFirst)){
                    isCON3 = "13";
                }else {
                    isCON3 = "14";
                }
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_CONSUME3).putExtra("iscFirst",isCON3), UPDATE_CONSUME3);
                break;
            case R.id.re_consume4:
                String isCON4;
                if ("13".equals(iscFirst)){
                    isCON4 = "13";
                }else {
                    isCON4 = "14";
                }
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_CONSUME4).putExtra("iscFirst",isCON4), UPDATE_CONSUME4);
                break;
            case R.id.re_consume5:
                String isCON5;
                if ("13".equals(iscFirst)){
                    isCON5 = "13";
                }else {
                    isCON5 = "14";
                }
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_CONSUME5).putExtra("iscFirst",isCON5), UPDATE_CONSUME5);
                break;
            case R.id.re_consume6:
                String isCON6;
                if ("13".equals(iscFirst)){
                    isCON6 = "13";
                }else {
                    isCON6 = "14";
                }
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_CONSUME6).putExtra("iscFirst",isCON6), UPDATE_CONSUME6);
                break;
            case R.id.re_hometown:
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_HOMETOWN).putExtra("data",hometown), UPDATE_HOMETOWN);
                break;
            case R.id.re_occupation:
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_OCCUPATION).putExtra("data",vocational), UPDATE_OCCUPATION);
                break;
            case R.id.re_company_address:
//                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_COMADDRESS), UPDATE_COMADDRESS);
                Toast.makeText(ProfileActivity.this,"公司地址只能在企业详情里面编辑！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_nianling:
                startActivityForResult(new Intent(ProfileActivity.this,ProfileUpdateActivity.class).putExtra("type", ProfileUpdateActivity.TYPE_NIANLING).putExtra("data",nianLing), UPDATE_NIANLING);
                break;
            case R.id.iv_back:
                finish();
        }

    }

    private void showSexDialog() {
        final String sex = tv_sex.getText().toString().trim();
        String title = "性别";
        List<String> items = new ArrayList<String>();
        items.add("男");
        items.add("女");
        FXAlertDialog fxAlertDialog = new FXAlertDialog(ProfileActivity.this, title, items);
        fxAlertDialog.init(new FXAlertDialog.OnItemClickListner() {
            @Override
            public void onClick(int position) {
                switch (position) {
                    case 0:
                        if ("男".equals(sex)){
                            return;
                        }
                        tijiao(1,"01",null,null);
                        tv_sex.setText("男");
                        break;
                    case 1:
                        if ("女".equals(sex)){
                            return;
                        }
                        tijiao(1,"00",null,null);
                        tv_sex.setText("女");
                        break;
                }
            }
        });
    }

    private void tijiao(final int type, final String sex, final String birth, final String nianling){
        String url = FXConstant.URL_UPDATE;
        final StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(s);
                    String code = object.getString("code");
                    if (code.equals("SUCCESS")) {
                        Toast.makeText(ProfileActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("-----失败----", "" + volleyError.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                String loginId = DemoHelper.getInstance().getCurrentUsernName();
                param.put("uLoginId",loginId);
                if (type==1) {
                    param.put("uSex", sex);
                }else {
                    param.put("uBirthday", birth);
                    param.put("uAge",nianling);
                }
                return param;
            }
        };
        MySingleton.getInstance(ProfileActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UPDATE_GONGSI:
                    String fxid = data.getStringExtra("value");
                    if (fxid != null) {
                        tv_gongsi.setText(fxid);
                        hasChange = true;
                    }
                    break;
                case UPDATE_NICK:
                    String name = data.getStringExtra("value");
                    if (name != null) {
                        tv_gongsi.setText(name);
                        hasChange = true;
                    }
                    break;
                case UPDATE_SIGN:
                    String sign = data.getStringExtra("value");
                    if (sign != null) {
                        tv_sign.setText(sign);
                        hasChange = true;
                    }
                    break;
                case UPDATE_SEX_NV:
                    String sex1 = data.getStringExtra("value");
                    if (sex1 != null) {
                        tv_sex.setText(sex1);
                        hasChange = true;
                    }
                    break;
                case UPDATE_SEX_NAN:
                    String sex2 = data.getStringExtra("value");
                    if (sex2 != null) {
                        tv_sex.setText(sex2);
                        hasChange = true;
                    }
                    break;
                case UPDATE_BIRTH:
                    String birth = data.getStringExtra("value");
                    if (birth != null) {
                        tv_birth.setText(birth);
                        hasChange = true;
                    }
                    break;
                case UPDATE_MAJOR1:
                    String ma1 = data.getStringExtra("value");
                    if (ma1 != null) {
                        tv_major1.setText(ma1);
                        hasChange = true;
                    }
                    break;
                case UPDATE_MAJOR2:
                    String ma2 = data.getStringExtra("value");
                    if (ma2 != null) {
                        tv_major2.setText(ma2);
                        hasChange = true;
                    }
                    break;
                case UPDATE_MAJOR3:
                    String ma3 = data.getStringExtra("value");
                    if (ma3 != null) {
                        tv_major3.setText(ma3);
                        hasChange = true;
                    }
                    break;
                case UPDATE_MAJOR4:
                    String ma4 = data.getStringExtra("value");
                    if (ma4 != null) {
                        tv_major4.setText(ma4);
                        hasChange = true;
                    }
                    break;
                case UPDATE_CONSUME1:
                    String co1 = data.getStringExtra("value");
                    if (co1 != null) {
                        tv_consume1.setText(co1);
                        hasChange = true;
                    }
                    break;
                case UPDATE_CONSUME2:
                    String co2 = data.getStringExtra("value");
                    if (co2 != null) {
                        tv_consume2.setText(co2);
                        hasChange = true;
                    }
                    break;
                case UPDATE_CONSUME3:
                    String co3 = data.getStringExtra("value");
                    if (co3 != null) {
                        tv_consume3.setText(co3);
                        hasChange = true;
                    }
                    break;
                case UPDATE_CONSUME4:
                    String co4 = data.getStringExtra("value");
                    if (co4 != null) {
                        tv_consume4.setText(co4);
                        hasChange = true;
                    }
                    break;
                case UPDATE_CONSUME5:
                    String co5 = data.getStringExtra("value");
                    if (co5 != null) {
                        tv_consume5.setText(co5);
                        hasChange = true;
                    }
                    break;
                case UPDATE_CONSUME6:
                    String co6 = data.getStringExtra("value");
                    if (co6 != null) {
                        tv_consume6.setText(co6);
                        hasChange = true;
                    }
                    break;
                case UPDATE_OCCUPATION:
                    String occ = data.getStringExtra("value");
                    if (occ != null) {
                        tv_occupation.setText(occ);
                        hasChange = true;
                    }
                    break;
                case UPDATE_COMADDRESS:
                    String comadd = data.getStringExtra("value");
                    if (comadd != null) {
                        tv_company_address.setText(comadd);
                        hasChange = true;
                    }
                    break;
                case UPDATE_NIANLING:
                    String nianling = data.getStringExtra("value") + "岁";
                    if (nianling != null) {
                        tv_nainling.setText(nianling);
                        hasChange = true;
                    }
                    break;
            }
            presenter.updateData();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void back(View view){
        ckeckChange();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            ckeckChange();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void ckeckChange(){
        if(hasChange){
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void updateUserInfo(Userful user) {
        upDescribe1 = TextUtils.isEmpty(user.getUpDescribe1())?"":user.getUpDescribe1();
        upDescribe2 = TextUtils.isEmpty(user.getUpDescribe2())?"":user.getUpDescribe2();
        upDescribe3 = TextUtils.isEmpty(user.getUpDescribe3())?"":user.getUpDescribe3();
        upDescribe4 = TextUtils.isEmpty(user.getUpDescribe4())?"":user.getUpDescribe4();
        zy1Resv3 = TextUtils.isEmpty(user.getZy1resv3())?"01":user.getZy1resv3();
        zy2Resv3 = TextUtils.isEmpty(user.getZy2resv3())?"01":user.getZy2resv3();
        zy3Resv3 = TextUtils.isEmpty(user.getZy3resv3())?"01":user.getZy3resv3();
        zy4Resv3 = TextUtils.isEmpty(user.getZy4resv3())?"01":user.getZy4resv3();
        upName1 = TextUtils.isEmpty(user.getUpName1())?"":user.getUpName1();
        upName2 = TextUtils.isEmpty(user.getUpName2())?"":user.getUpName2();
        upName3 = TextUtils.isEmpty(user.getUpName3())?"":user.getUpName3();
        upName4 = TextUtils.isEmpty(user.getUpName4())?"":user.getUpName4();
        image1 = TextUtils.isEmpty(user.getZyImage1())?"":user.getZyImage1();
        image2 = TextUtils.isEmpty(user.getZyImage2())?"":user.getZyImage2();
        image3 = TextUtils.isEmpty(user.getZyImage3())?"":user.getZyImage3();
        image4 = TextUtils.isEmpty(user.getZyImage4())?"":user.getZyImage4();
        name = TextUtils.isEmpty(user.getName())?"":user.getName();
        String loginId = TextUtils.isEmpty(user.getLoginId())?"":user.getLoginId();
        String gongsi = TextUtils.isEmpty(user.getCompany())?"":user.getCompany();
        String companyaddress = TextUtils.isEmpty(user.getCompanyAdress())?"未设置":user.getCompanyAdress();
        String sex = TextUtils.isEmpty(user.getSex())?"":user.getSex();
        sign = TextUtils.isEmpty(user.getSignaTure())?"":user.getSignaTure();
        birth = TextUtils.isEmpty(user.getBirthday())?"":user.getBirthday();
        hometown = TextUtils.isEmpty(user.getHome())?"":user.getHome();
        vocational = TextUtils.isEmpty(user.getZhiYe())?"":user.getZhiYe();
        try {
            companyaddress = URLDecoder.decode(companyaddress,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        nianLing = TextUtils.isEmpty(user.getuAge()) ? "" : user.getuAge();
        tv_nainling.setText(nianLing);
        tv_name.setText(name.equals("")?loginId:name);
        tv_gongsi.setText(gongsi.equals("")?"未设置":gongsi);
        if(!sex.equals("")){
            tv_sex.setText(sex.equals("00")?"女":"男");
        }
        tv_sign.setText(sign.equals("")?"未设置":sign);
        tv_birth.setText(birth.equals("")?"未设置":birth);
        tv_company_address.setText(companyaddress);
        tv_occupation.setText(vocational.equals("")?"未设置":vocational);
        tv_hometown.setText(hometown.equals("")?"未设置":hometown);
        String maj1 = user.getUpName1();
        String maj2 = user.getUpName2();
        String maj3 = user.getUpName3();
        String maj4 = user.getUpName4();
        if (maj1==null||"".equals(maj1)||"null".equals(maj1)){
            maj1 = "点击编辑";
        }
        if (maj2==null||"".equals(maj2)||"null".equals(maj2)||"NULL".equals(maj2)){
            maj2 = "点击编辑";
        }
        if (maj3==null||"".equals(maj3)||"null".equals(maj3)||"NULL".equals(maj3)){
            maj3 = "点击编辑";
        }
        if (maj4==null||"".equals(maj4)||"null".equals(maj4)||"NULL".equals(maj4)){
            maj4 = "点击编辑";
        }
        tv_major1.setText(maj1);
        tv_major2.setText(maj2);
        tv_major3.setText(maj3);
        tv_major4.setText(maj4);
        tv_consume1.setText(TextUtils.isEmpty(user.getUcName1())?"关注消费1":user.getUcName1());
        tv_consume2.setText(TextUtils.isEmpty(user.getUcName2())?"关注消费2":user.getUcName2());
        tv_consume3.setText(TextUtils.isEmpty(user.getUcName3())?"关注消费3":user.getUcName3());
        tv_consume4.setText(TextUtils.isEmpty(user.getUcName4())?"关注消费4":user.getUcName4());
        tv_consume5.setText(TextUtils.isEmpty(user.getUcName5())?"关注消费5":user.getUcName5());
        tv_consume6.setText(TextUtils.isEmpty(user.getUcName6())?"关注消费6":user.getUcName6());
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
