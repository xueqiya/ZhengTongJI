package com.sangu.apptongji.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.avtivity.SouSuoActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.SouSuoThreeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.SouSuoTwoActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Administrator on 2016-12-07.
 */

public class MainTwoActivity extends BaseActivity implements View.OnClickListener{
    public static MainTwoActivity instance;
    private FragmentFind fragmentFind;
    private FragmentProfileTwo fragmentProfile;
    private Fragment[] fragments;
    private TextView tv_sousuo=null;
    private Button[] mTabs;
    Bundle bundle;
    private int index;
    private int currentTabIndex;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        instance = this;
        setContentView(R.layout.fx_activity_two_main);
        WeakReference<MainTwoActivity> reference =  new WeakReference<MainTwoActivity>(MainTwoActivity.this);
        initView();
        MySingleton.getInstance(this).getRequestQueue();
        SharedPreferences sp = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        if (!sp.contains("isLogedIn")) {
            bundAndroidId();
        }
        bundle = new Bundle();
        fragmentFind = new FragmentFind();
        fragmentProfile = new FragmentProfileTwo();
        fragmentFind.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentFind).add(R.id.fragment_container,fragmentProfile).hide(fragmentProfile).show(fragmentFind).commit();
        fragments = new Fragment[]{fragmentFind,fragmentProfile};
        tv_sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = fragmentFind.getcurrentIndex();
                if (currentTabIndex==0) {
                    if (index==0) {
                        startActivityForResult(new Intent(MainTwoActivity.this, SouSuoActivity.class), 1);
                    }else if (index==1){
                        startActivityForResult(new Intent(MainTwoActivity.this, SouSuoTwoActivity.class),2);
                    }else if (index==2){
                        startActivityForResult(new Intent(MainTwoActivity.this, SouSuoThreeActivity.class),3);
                    }else {
                        Toast.makeText(MainTwoActivity.this,"暂未处理操作",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainTwoActivity.this,"暂未处理操作",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bundAndroidId() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        Date curDate = new Date(System.currentTimeMillis());
        final String date = sDateFormat.format(curDate);
        final String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID);
        String url = FXConstant.URL_INSERT_READHIS;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
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
                param.put("deviceStr","android"+ANDROID_ID);
                param.put("resv1",date);
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void setRobot(int type){
        fragmentFind.setRobot(type);
    }

    public void setSousuoColor(int index){
        if (index==0) {
            tv_sousuo.setBackgroundResource(R.drawable.fx_bg_text_gra);
        }else if (index==1){
            tv_sousuo.setBackgroundResource(R.drawable.fx_bg_text_gray);
        }else if (index==2){
            tv_sousuo.setBackgroundResource(R.drawable.fx_bg_text_orange);
        }else if (index==3){
            tv_sousuo.setBackgroundResource(R.drawable.fx_bg_text_red);
        }
    }

    private void initView() {
        tv_sousuo = (TextView) findViewById(R.id.tv_sousuo);
        mTabs = new Button[2];
        mTabs[0] = (Button) findViewById(R.id.btn_find);
        mTabs[1] = (Button) findViewById(R.id.btn_profile);
        // select first tab
        mTabs[0].setSelected(true);
    }

    public void onTabClicked(View view) {
        JCVideoPlayer.releaseAllVideos();
        switch (view.getId()) {
            case R.id.btn_find:
                index = 0;
                setSousuoColor(0);
                break;
            case R.id.btn_conversation:
                Toast.makeText(MainTwoActivity.this,"登陆之后才可以查看消息和好友哦！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_address_list:
                Toast.makeText(MainTwoActivity.this,"登陆之后才可以查看消息和好友哦！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_profile:
                index = 1;
                setSousuoColor(1);
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab selected
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&data!=null) {
            switch (requestCode) {
                case 1:
                    String zhY = data.hasExtra("zhuanye") ? data.getStringExtra("zhuanye") : "";
                    String name = data.hasExtra("name") ? data.getStringExtra("name") : "";
                    String comName = data.hasExtra("comName") ? data.getStringExtra("comName") : "";
                    String isclear = data.hasExtra("isclear") ? data.getStringExtra("isclear") : "";
                    try {
                        comName = URLEncoder.encode(comName, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    bundle.putString("zhY", zhY);
                    bundle.putString("name", name);
                    bundle.putString("comName", comName);
                    bundle.putString("isclear", isclear);
                    Log.e("MainAc,2", "刷新person");
                    fragmentFind.onRefreshPer();
                    break;
                case 2:
                    String content = data.getStringExtra("data");
                    bundle.putString("content", content);
                    Log.e("MainAc,2", "刷新dyna");
                    fragmentFind.onRefreshDyna();
                    break;
                case 3:
                    String baozhjin = data.getStringExtra("baozhjin");
                    String qiye_mch = data.getStringExtra("qiye_mch");
                    String qiye_zhy = data.getStringExtra("qiye_zhy");
                    String isclear2 = data.getStringExtra("isclear");
                    bundle.putString("baozhjin", baozhjin);
                    bundle.putString("qiye_mch", qiye_mch);
                    bundle.putString("qiye_zhy", qiye_zhy);
                    bundle.putString("isclear", isclear2);
                    Log.e("MainAc,2", "刷新qiye");
                    fragmentFind.onRefreshQiye();
                    break;
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance=null;
    }

    @Override
    public void onClick(View v) {

    }
}
