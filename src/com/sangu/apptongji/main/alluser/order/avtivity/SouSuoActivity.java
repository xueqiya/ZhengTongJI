package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.adapter.AdvertPagerAdapter;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.AppDownDetailActivity;
import com.sangu.apptongji.main.activity.SouSuoAdvertClickActivity;
import com.sangu.apptongji.main.moments.MomentsPublishActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-09.
 */

public class SouSuoActivity extends BaseActivity implements View.OnClickListener{
    private EditText etSearch;
    private Button btn_clear;
    private Button btnSend;
    private LinearLayout llQueren;
    private Button btnQueren;
    private RadioButton radiozhy;
    private RadioButton radiogs;
    private RadioButton radioxm;
    private ImageView advertImageView;
    private TextView tv_advert;
    private String[] strings = {};
    private int currentType=1;
    private ViewPager advertViewPager2;
    AdvertPagerAdapter adapter2;
    private List<View> list2 = new ArrayList<View>();;
    private String[] advertImages = {};
    private String[] advertClickTypes = {};
    private String[] advertClickContents = {};
    private int currentItem = 0;
    private TextView tv_vpclick;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_newsousuo);
        etSearch = (EditText) findViewById(R.id.et_search);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btnSend = (Button) findViewById(R.id.btn_send);
        llQueren = (LinearLayout) findViewById(R.id.ll_queren);
        btnQueren = (Button) findViewById(R.id.btn_confirm);
        radiozhy = (RadioButton) findViewById(R.id.radiozhy);
        radiogs = (RadioButton) findViewById(R.id.radiogs);
        radioxm = (RadioButton) findViewById(R.id.radioxm);
        advertImageView = (ImageView) findViewById(R.id.image_advert);
        tv_advert = (TextView) findViewById(R.id.tv_advert);
        tv_advert.setVisibility(View.INVISIBLE);
        advertImageView.setVisibility(View.INVISIBLE);
        advertViewPager2 = (ViewPager) findViewById(R.id.vp_advertViewPager2);
        tv_vpclick = (TextView) findViewById(R.id.tv_vpclick);

        radiozhy.setOnClickListener(this);
        radiogs.setOnClickListener(this);
        radioxm.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        advertImageView.setOnClickListener(this);
        btnQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llQueren.setVisibility(View.INVISIBLE);
            }
        });

        GetNotice();

    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radioxm:
                radioxm.setChecked(true);
                radiozhy.setChecked(false);
                radiogs.setChecked(false);
                break;
            case R.id.radiozhy:
                radioxm.setChecked(false);
                radiozhy.setChecked(true);
                radiogs.setChecked(false);
                break;
            case R.id.radiogs:
                radioxm.setChecked(false);
                radiozhy.setChecked(false);
                radiogs.setChecked(true);
                break;
            case R.id.btn_send:
                String content = TextUtils.isEmpty(etSearch.getText().toString().trim())?"":etSearch.getText().toString().trim();
                Intent intent = new Intent();
                if (radiozhy.isChecked()) {
                    intent.putExtra("zhuanye", content);
                    insertSearchRecord("专业", content);
                }else if (radioxm.isChecked()){
                    intent.putExtra("name", content);
                    insertSearchRecord("账号", content);
                }else if (radiogs.isChecked()){
                    intent.putExtra("comName", content);
                    insertSearchRecord("企业", content);
                }
                setResult(RESULT_OK, intent);
                finish();

                break;
            case R.id.btn_clear:
                Intent intent1 = new Intent();
                intent1.putExtra("zhuanye","");
                intent1.putExtra("name","");
                intent1.putExtra("comName","");
                intent1.putExtra("baozhengjin","");
                intent1.putExtra("sex","");
                intent1.putExtra("ageStart","");
                intent1.putExtra("ageEnd","");
                intent1.putExtra("isclear","1");
                setResult(RESULT_OK, intent1);
                finish();
                break;

            case R.id.image_advert:


                String current = strings[currentType];

                if (current.equals("0")){

                    Intent intent2 = new Intent(SouSuoActivity.this, ChatActivity.class);
                    intent2.putExtra("userId", "13513895563");
                    intent2.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                    intent2.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                    //  intent2.putExtra(EaseConstant.EXTRA_USER_IMG,"zhengshiduo.png");
                    // intent2.putExtra(EaseConstant.EXTRA_USER_NAME,"李璐");
                    intent2.putExtra(EaseConstant.EXTRA_USER_SHARERED,"无");
                    startActivity(intent2);

                }else {
                    Intent intent2 = new Intent(SouSuoActivity.this, SouSuoAdvertClickActivity.class);
                    intent2.putExtra("url", current);
                    startActivity(intent2);
                }

                break;
        }
    }
    private void insertSearchRecord(final String type, final String content) {
        String url = FXConstant.URL_INSERT_SEARCH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "insertSearchRecord onResponse" + s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "insertSearchRecord onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //uId=&searchtype=&searchcontent=
                Map<String, String> param = new HashMap<>();
                param.put("uId", DemoHelper.getInstance().getCurrentUsernName());
                param.put("searchtype", type);
                param.put("searchcontent", content);
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    //查询广告
    private void GetNotice(){

        String url = FXConstant.URL_GONGGAO;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                com.alibaba.fastjson.JSONObject object1 = object.getJSONObject("notice");

                String type = object1.getString("type");

                if (type.equals("1")){

                    advertViewPager2.setVisibility(View.VISIBLE);

                    String image1 = object1.getString("image2");//广告类型
                    advertImages = image1.split("\\|");

                    String image2 = object1.getString("image3");//广告点击类型
                    advertClickTypes = image2.split("\\|");

                    String image4 = object1.getString("image4");//广告点击类型对应内容
                    advertClickContents = image4.split("\\|");

                    ImageView tempImageView;
                    for (int i=0 ; i<advertImages.length ; i++){

                        ImageView iv1 = new ImageView(SouSuoActivity.this);
                        ImageLoader.getInstance().displayImage(FXConstant.URL_ADVERTURL+advertImages[i],iv1);

                        if (i==0){
                            tempImageView = iv1;
                        }

                        list2.add(iv1);

                    }

                    adapter2 = new AdvertPagerAdapter(SouSuoActivity.this,list2);

                    advertViewPager2.setAdapter(adapter2);

                    tv_vpclick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String type1 = advertClickTypes[currentItem-1];
                            String type2 = advertClickContents[currentItem-1];

                            if (type1.equals("1")){

                                // 打电话
                                if (type2.length() != 11){

                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_CALL);
                                    //url:统一资源定位符
                                    //uri:统一资源标示符（更广）
                                    intent.setData(Uri.parse("tel:" + "13513895563"));
                                    //开启系统拨号器
                                    startActivity(intent);

                                }else {

                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_CALL);
                                    //url:统一资源定位符
                                    //uri:统一资源标示符（更广）
                                    intent.setData(Uri.parse("tel:" + type2));
                                    //开启系统拨号器
                                    startActivity(intent);

                                }

                            }else if (type1.equals("2")){

                                if (!DemoHelper.getInstance().isLoggedIn(SouSuoActivity.this)) {

                                    Toast.makeText(SouSuoActivity.this,"登陆之后才可以发布招标！",Toast.LENGTH_SHORT).show();

                                }else {

                                    //发布需求
                                    Intent intent2 = new Intent(SouSuoActivity.this, MomentsPublishActivity.class);
                                    intent2.putExtra("biaoshi", "xuqiu");
                                    startActivity(intent2);

                                }

                            }else if (type1.equals("3")){

                                //下载app  跳转app对应详情

                                Intent intent2 = new Intent(SouSuoActivity.this, AppDownDetailActivity.class);
                                intent2.putExtra("appType", "0");
                                intent2.putExtra("appIdentify", type2);
                                startActivity(intent2);

                            }else if (type1.equals("4")){

                                //打开网页

                                Uri uri = Uri.parse(type2);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                            }else if (type1.equals("5")){

                                //跳转应用内网页  可以电话咨询或者聊天咨询

                                Intent intent2 = new Intent(SouSuoActivity.this, SouSuoAdvertClickActivity.class);
                                intent2.putExtra("url", type2);
                                startActivity(intent2);

                            }

                        }
                    });



                    CountDownTimer cdt = new CountDownTimer(8000000, 2000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            if (currentItem==advertImages.length){

                                currentItem = 0;

                            }

                            if (currentItem == 0){

                                advertViewPager2.setCurrentItem(currentItem, false);

                            }else {

                                advertViewPager2.setCurrentItem(currentItem, true);

                            }

                            currentItem ++ ;

                        }
                        @Override
                        public void onFinish() {

                        }
                    };

                    cdt.start();

                }

                /*
                SharedPreferences sp = getSharedPreferences("sangu_gonggao_info", Context.MODE_PRIVATE);

                //记录对应的条跳转 分别是image2 3 4 的跳转链接
                String type3 = object1.getString("type3");
                strings = type3.split("\\|");
                if (type.equals("1")){

                    String image1 = object1.getString("image2");
                    String image2 = object1.getString("image3");
                    String image3 = object1.getString("image4");

                    String currentTag="1";

                    if (sp!=null) {
                        currentTag = sp.getString("currentTag","1");
                    }

                    if (currentTag.equals("2")){

                        if (sp != null) {
                            SharedPreferences.Editor editor1 = sp.edit();
                            editor1.putString("currentTag", "3");
                            editor1.commit();
                        }

                        String url = FXConstant.URL_ADVERTURL+image2;
                        currentType = 1;
                        ImageLoader.getInstance().displayImage(url,advertImageView);

                        advertImageView.setVisibility(View.VISIBLE);
                        tv_advert.setVisibility(View.VISIBLE);

                    }else if (currentTag.equals("3")){

                        if (sp != null) {
                            SharedPreferences.Editor editor1 = sp.edit();
                            editor1.putString("currentTag", "1");
                            editor1.commit();
                        }
                        String url = FXConstant.URL_ADVERTURL+image3;
                        currentType = 2;
                        ImageLoader.getInstance().displayImage(url,advertImageView);

                        advertImageView.setVisibility(View.VISIBLE);
                        tv_advert.setVisibility(View.VISIBLE);

                    }else {

                        if (sp != null) {
                            SharedPreferences.Editor editor1 = sp.edit();
                            editor1.putString("currentTag", "2");
                            editor1.commit();
                        }

                        String url = FXConstant.URL_ADVERTURL+image1;
                        currentType = 0;
                        ImageLoader.getInstance().displayImage(url,advertImageView);

                        advertImageView.setVisibility(View.VISIBLE);
                        tv_advert.setVisibility(View.VISIBLE);

                    }

                }
                */

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                //param.put("deviceType","android8");
                param.put("deviceType","androidsearch");
                return param;
            }
        };
        MySingleton.getInstance(SouSuoActivity.this).addToRequestQueue(request);

    }


}
