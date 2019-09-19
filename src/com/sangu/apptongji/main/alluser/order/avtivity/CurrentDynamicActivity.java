package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.moments.MomentsPublishActivity;
import com.sangu.apptongji.main.moments.SocialMainAdapter;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-11-21.
 */

public class CurrentDynamicActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_none;
    private TextView tv_title_name;
    private TextView tv_add;
    private RadioButton radio_dynamic_ShHuo;
    private RadioButton radio_dynamic_XinWen;
    private RadioButton radio_dynamic_ShYe;
    private RadioButton radio_dynamic_YouHui;
    private RadioButton radio_dynamic_xuqiu;
    private XRecyclerView mRecyclerView;
    private RelativeLayout re_edittext;
    private String dType = "05", profession=null;
    private String isHaveMargin = "0";
    private int isVip = 0;
    private String vipLevel = "1";
    private List<JSONObject> articles=new ArrayList<JSONObject>();
    private SocialMainAdapter adapter;
    private String userId,biaoshi;
    private int page = 1;
    private String isShareDynamic;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fx_activity_social_main2);
        tv_none = (TextView) findViewById(R.id.tv_none);
        re_edittext = (RelativeLayout) findViewById(R.id.re_edittext);
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        tv_add = (TextView) findViewById(R.id.tv_add);
        radio_dynamic_ShHuo = (RadioButton) findViewById(R.id.radio_dynamic_ShHuo);
        radio_dynamic_XinWen = (RadioButton) findViewById(R.id.radio_dynamic_XinWen);
        radio_dynamic_ShYe = (RadioButton) findViewById(R.id.radio_dynamic_ShYe);
        radio_dynamic_YouHui = (RadioButton) findViewById(R.id.radio_dynamic_YouHui);
        radio_dynamic_xuqiu = (RadioButton) findViewById(R.id.radio_dynamic_xuqiu);
        mRecyclerView = (XRecyclerView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        userId = this.getIntent().getStringExtra("userId");
        biaoshi = this.getIntent().getStringExtra("biaoshi");
        if (biaoshi.equals("00")){
            tv_add.setVisibility(View.VISIBLE);
            tv_add.setOnClickListener(this);
            tv_title_name.setText("我的动态");

        }else {
            tv_add.setVisibility(View.INVISIBLE);
            tv_title_name.setText("他的动态");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                adapter.hideCommentEditText();
                return false;
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData("01",page,dType,null);
            }

            @Override
            public void onLoadMore() {
                page = page + 1;
                getData("01",page, dType, null);
            }
        });
        initFile();
        initView();
        setlistener();
    }

    private void setlistener() {
        adapter.setOnItemClickListener(new SocialMainAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JSONObject object = articles.get(position-1);
                String type;
                if (object.getString("fromUId")!=null){
                    type = "02";
                }else {
                    type = "01";
                }
                String dynamic_seq = object.getString("dynamicSeq");
                String createTime = object.getString("createTime");
                Intent intent = new Intent(CurrentDynamicActivity.this, DynaDetaActivity.class);
                intent.putExtra("sID",DemoHelper.getInstance().getCurrentUsernName());
                intent.putExtra("dynamicSeq",dynamic_seq);
                intent.putExtra("createTime",createTime);
                intent.putExtra("dType",dType);
                intent.putExtra("type",type);
                intent.putExtra("type2","01");
                startActivity(intent);
            }
        });
    }

    private void initView() {
        radio_dynamic_ShHuo.setOnClickListener(this);
        radio_dynamic_XinWen.setOnClickListener(this);
        radio_dynamic_ShYe.setOnClickListener(this);
        radio_dynamic_YouHui.setOnClickListener(this);
        radio_dynamic_xuqiu.setOnClickListener(this);
        adapter = new SocialMainAdapter(this,articles,dType,profession,"01",isHaveMargin+"|"+vipLevel+"|0"+"|0",isVip,"0");
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                adapter.hideCommentEditText();
                return false;
            }
        });
        mRecyclerView.refresh(false);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode==RESULT_OK){

            switch (requestCode){

                case 2:

                    //直接加载生活动态然后提示那个分享案例动态
                    isShareDynamic = "yes";
                    page = 1;
                    dType="01";
                    getData("01",page,dType,null);


                    break;

            }

        }

    }


    private void getData(final String dynamicType,final int page_num,final String dtType,final String selectCon) {
        if (TextUtils.isEmpty(userId)) {
            ToastUtils.showNOrmalToast(this,"获取数据失败，请重新打开次页面");
            tv_none.setVisibility(View.VISIBLE);
            return;
        }
        List<Param> params=new ArrayList<>();
        params.add(new Param("currentPage", page_num+""));
        params.add(new Param("dType", dtType));
        params.add(new Param("userId", userId));
        if (DemoHelper.getInstance().isLoggedIn(this)) {
            params.add(new Param("loginId",DemoHelper.getInstance().getCurrentUsernName()));
        }
        OkHttpManager.getInstance().post(params, FXConstant.URL_PUBLISH_QUERY, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                String loginId = DemoHelper.getInstance().getCurrentUsernName();
                JSONArray friendList = jsonObject.getJSONArray("friendList");
                JSONArray users_temp = jsonObject.getJSONArray("clist");
                profession = jsonObject.getString("profession");
                isHaveMargin = jsonObject.getString("margin");
                isVip = jsonObject.getIntValue("vip");
                vipLevel = jsonObject.getString("vipLevel");

                if (page_num==1){
                    mRecyclerView.refreshComplete();
                }else {
                    mRecyclerView.loadMoreComplete();
                }
                if (users_temp==null||users_temp.size()==0||users_temp.toString().equals("[]")){
                    if (page_num==1) {
                        if (articles != null) {
                            articles.clear();
                        }
                        tv_none.setVisibility(View.VISIBLE);
                    }
                }else {
                    tv_none.setVisibility(View.INVISIBLE);
                }
                if (users_temp!=null&&users_temp.size()<20){
                    if (page_num>1) {
                        mRecyclerView.setNoMore(true);
                    }
                }

                if (users_temp!=null) {
                    if (page_num == 1) {
                        //	datas = users_temp;
                        if (articles!=null) {
                            articles.clear();
                        }
                        for (int i = 0; i < users_temp.size(); i++) {
                            JSONObject json = users_temp.getJSONObject(i);
                            String authType = TextUtils.isEmpty(json.getString("authType"))?"":json.getString("authType");
                            String uLoginId = json.getString("uLoginId");
                            if (authType.equals("01")) {
                                String sID = json.getString("uId");
                                if (articles!=null) {
                                    articles.add(json);
                                }
                            } else if (authType.equals("03")) {//authType=02\03
                                if (uLoginId.equals(loginId)) {
                                    String sID = json.getString("uId");
                                    if (articles!=null) {
                                        articles.add(json);
                                    }
                                }
                            }else if (authType.equals("02")){
                                if (uLoginId.equals(loginId)) {
                                    String sID = json.getString("uId");
                                    if (articles!=null) {
                                        articles.add(json);
                                    }
                                }else {
                                    if (friendList != null) {
                                        for (int j = 0; j < friendList.size(); j++) {
                                            JSONObject json1 = friendList.getJSONObject(j);
                                            String id = json1.getString("fId");
                                            if (id.equals(uLoginId)) {
                                                String sID = json.getString("uId");
                                                if (articles!=null) {
                                                    articles.add(json);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < users_temp.size(); i++) {
                            JSONObject json = users_temp.getJSONObject(i);
                            String authType = json.getString("authType");
                            String uLoginId = json.getString("uLoginId");
                            if (authType.equals("01")) {
                                if (articles!=null) {
                                    articles.add(json);
                                }
                            } else if (authType.equals("03")) {//authType=02\03
                                if (uLoginId.equals(loginId)) {
                                    if (articles!=null) {
                                        articles.add(json);
                                    }
                                }
                            }else if (authType.equals("02")){
                                if (uLoginId.equals(loginId)) {
                                    if (articles!=null) {
                                        articles.add(json);
                                    }
                                }else {
                                    if (friendList != null) {
                                        for (int j = 0; j < friendList.size(); j++) {
                                            JSONObject json1 = friendList.getJSONObject(j);
                                            String id = json1.getString("fId");
                                            if (id.equals(uLoginId)) {
                                                if (articles!=null) {
                                                    articles.add(json);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if ("05".equals(dType)){

                        profession = jsonObject.getString("profession");
                        isHaveMargin = jsonObject.getString("margin");
                        isVip = jsonObject.getIntValue("vip");
                        vipLevel = jsonObject.getString("vipLevel");
                        adapter = new SocialMainAdapter(CurrentDynamicActivity.this,articles,dType,profession,"00",isHaveMargin+"|"+vipLevel+"|0"+"|0",isVip,"0");
                        mRecyclerView.setAdapter(adapter);
                        setAdapter4();

                    }else {

                        if (isShareDynamic != null && isShareDynamic.equals("yes")){

                            radio_dynamic_ShHuo.setTextColor(Color.parseColor("#FF0000"));
                            radio_dynamic_XinWen.setTextColor(Color.parseColor("#D3D3D3"));
                            radio_dynamic_ShYe.setTextColor(Color.parseColor("#D3D3D3"));
                            radio_dynamic_YouHui.setTextColor(Color.parseColor("#D3D3D3"));
                            radio_dynamic_xuqiu.setTextColor(Color.parseColor("#D3D3D3"));

                            radio_dynamic_ShHuo.setChecked(true);
                            radio_dynamic_XinWen.setChecked(false);
                            radio_dynamic_ShYe.setChecked(false);
                            radio_dynamic_YouHui.setChecked(false);
                            radio_dynamic_xuqiu.setChecked(false);
                            adapter = new SocialMainAdapter(CurrentDynamicActivity.this,articles,dType,profession,"01",isHaveMargin+"|"+vipLevel+"|0"+"|0",isVip,"yes");
                            mRecyclerView.setAdapter(adapter);
                            setAdapter1();
                            isShareDynamic = "no";

                        }else {

                            profession = null;
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }

                        }



                    }
                }
            }
            @Override
            public void onFailure(String errorMsg) {
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (articles!=null){
            articles.clear();
            articles=null;
        }
        if (adapter!=null){
            adapter = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.refresh(false);
    }

    private void setAdapter1() {
        adapter.setOnItemClickListener(new SocialMainAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (re_edittext.getVisibility()==View.VISIBLE){
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }else {
                    JSONObject object = articles.get(position-1);
                    String type;
                    if (object.getString("fromUId") != null) {
                        type = "02";
                    } else {
                        type = "01";
                    }
                    String sID = object.getString("uLoginId");
                    String dynamic_seq = object.getString("dynamicSeq");
                    String createTime = object.getString("createTime");
                    Intent intent = new Intent(CurrentDynamicActivity.this, DynaDetaActivity.class);
                    intent.putExtra("sID", sID);
                    intent.putExtra("profession",profession);
                    intent.putExtra("dynamicSeq",dynamic_seq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    intent.putExtra("type2", "00");
                    startActivityForResult(intent,0);
                }
            }
        });
    }

    private void setAdapter2() {
        adapter.setOnItem2ClickListener(new SocialMainAdapter.MyItem2ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (re_edittext.getVisibility()==View.VISIBLE){
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }else {
                    JSONObject object = articles.get(position-1);
                    String type;
                    if (object.getString("fromUId") != null) {
                        type = "02";
                    } else {
                        type = "01";
                    }
                    String sID = object.getString("uLoginId");
                    String dynamic_seq = object.getString("dynamicSeq");
                    String createTime = object.getString("createTime");
                    Intent intent = new Intent(CurrentDynamicActivity.this, DynaDetaActivity.class);
                    intent.putExtra("sID", sID);
                    intent.putExtra("profession",profession);
                    intent.putExtra("dynamicSeq",dynamic_seq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    startActivityForResult(intent,0);
                }
            }
        });
    }

    private void setAdapter3() {
        adapter.setOnItem3ClickListener(new SocialMainAdapter.MyItem3ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (re_edittext.getVisibility()==View.VISIBLE){
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }else {
                    JSONObject object = articles.get(position-1);
                    String type;
                    if (object.getString("fromUId") != null) {
                        type = "02";
                    } else {
                        type = "01";
                    }
                    String sID = object.getString("uLoginId");
                    String dynamic_seq = object.getString("dynamicSeq");
                    String createTime = object.getString("createTime");
                    Intent intent = new Intent(CurrentDynamicActivity.this, DynaDetaActivity.class);
                    intent.putExtra("sID", sID);
                    intent.putExtra("profession",profession);
                    intent.putExtra("dynamicSeq",dynamic_seq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    startActivityForResult(intent,0);
                }
            }
        });
    }
    private void setAdapter4() {
        adapter.setOnItem4ClickListener(new SocialMainAdapter.MyItem4ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (re_edittext.getVisibility()==View.VISIBLE){
                    re_edittext.setVisibility(View.INVISIBLE);
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }else {
                    JSONObject object = articles.get(position-1);
                    String type;
                    if (object.getString("fromUId") != null) {
                        type = "02";
                    } else {
                        type = "01";
                    }
                    String sID = object.getString("uLoginId");
                    String dynamic_seq = object.getString("dynamicSeq");
                    String createTime = object.getString("createTime");
                    Intent intent = new Intent(CurrentDynamicActivity.this, DynaDetaActivity.class);
                    intent.putExtra("sID", sID);
                    intent.putExtra("profession",profession);
                    intent.putExtra("dynamicSeq",dynamic_seq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    intent.putExtra("type2", "00");
                    startActivityForResult(intent,0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radio_dynamic_ShHuo:
                adapter.hideCommentEditText();
                radio_dynamic_ShHuo.setChecked(true);
                radio_dynamic_XinWen.setChecked(false);
                radio_dynamic_ShYe.setChecked(false);
                radio_dynamic_YouHui.setChecked(false);
                radio_dynamic_xuqiu.setChecked(false);
                dType = "01";
                mRecyclerView.refresh(false);
                adapter = new SocialMainAdapter(CurrentDynamicActivity.this,articles,dType,profession,"01",isHaveMargin+"|"+vipLevel+"|0"+"|0",isVip,"0");
                mRecyclerView.setAdapter(adapter);
                setAdapter1();
                break;
            case R.id.radio_dynamic_XinWen:
                adapter.hideCommentEditText();
                radio_dynamic_ShHuo.setChecked(false);
                radio_dynamic_XinWen.setChecked(true);
                radio_dynamic_ShYe.setChecked(false);
                radio_dynamic_YouHui.setChecked(false);
                radio_dynamic_xuqiu.setChecked(false);
                dType = "02";
                mRecyclerView.refresh(false);
                adapter = new SocialMainAdapter(CurrentDynamicActivity.this,articles,dType,profession,"01",isHaveMargin+"|"+vipLevel+"|0"+"|0",isVip,"0");
                mRecyclerView.setAdapter(adapter);
                setAdapter2();
                break;
            case R.id.radio_dynamic_ShYe:
                adapter.hideCommentEditText();
                radio_dynamic_ShHuo.setChecked(false);
                radio_dynamic_XinWen.setChecked(false);
                radio_dynamic_ShYe.setChecked(true);
                radio_dynamic_YouHui.setChecked(false);
                radio_dynamic_xuqiu.setChecked(false);
                dType = "03";
                mRecyclerView.refresh(false);
                adapter = new SocialMainAdapter(CurrentDynamicActivity.this,articles,dType,profession,"01",isHaveMargin+"|"+vipLevel+"|0"+"|0",isVip,"0");
                mRecyclerView.setAdapter(adapter);
                setAdapter3();
                break;
            case R.id.radio_dynamic_YouHui:
                adapter.hideCommentEditText();
                radio_dynamic_ShHuo.setChecked(false);
                radio_dynamic_XinWen.setChecked(false);
                radio_dynamic_ShYe.setChecked(false);
                radio_dynamic_xuqiu.setChecked(false);
                radio_dynamic_YouHui.setChecked(true);
                dType = "04";
                mRecyclerView.refresh(false);
                adapter = new SocialMainAdapter(CurrentDynamicActivity.this,articles,dType,profession,"01",isHaveMargin+"|"+vipLevel+"|0"+"|0",isVip,"0");
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItem3ClickListener(new SocialMainAdapter.MyItem3ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        JSONObject object = articles.get(position-1);
                        String type;
                        if (object.getString("fromUId")!=null){
                            type = "02";
                        }else {
                            type = "01";
                        }
                        String dynamic_seq = object.getString("dynamicSeq");
                        String createTime = object.getString("createTime");
                        Intent intent = new Intent(CurrentDynamicActivity.this, DynaDetaActivity.class);
                        intent.putExtra("sID",DemoHelper.getInstance().getCurrentUsernName());
                        intent.putExtra("dynamicSeq",dynamic_seq);
                        intent.putExtra("createTime",createTime);
                        intent.putExtra("dType",dType);
                        intent.putExtra("type",type);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.radio_dynamic_xuqiu:
                adapter.hideCommentEditText();
                radio_dynamic_ShHuo.setChecked(false);
                radio_dynamic_XinWen.setChecked(false);
                radio_dynamic_ShYe.setChecked(false);
                radio_dynamic_YouHui.setChecked(false);
                radio_dynamic_xuqiu.setChecked(true);
                dType = "05";
                mRecyclerView.refresh(false);
                break;
            case R.id.tv_add:
                showdialog();
                break;
        }
    }
    private void showdialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(CurrentDynamicActivity.this);
        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_buttom2_dialog, null);
        final Dialog dialog = new AlertDialog.Builder(CurrentDynamicActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
        RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
        RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
        RelativeLayout re_item4 = (RelativeLayout) dialog.findViewById(R.id.re_item4);
        RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
        RelativeLayout re_item6 = (RelativeLayout) dialog.findViewById(R.id.re_item6);
        re_item4.setVisibility(View.GONE);
        if (biaoshi.equals("00")) {
            re_item1.setVisibility(View.VISIBLE);
        }
        re_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CurrentDynamicActivity.this,MomentsPublishActivity.class);
                intent1.putExtra("biaoshi","shenghuo");
                startActivityForResult(intent1,2);
                dialog.dismiss();
            }
        });
        re_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(CurrentDynamicActivity.this,MomentsPublishActivity.class);
                intent2.putExtra("biaoshi","xinwen");
                startActivity(intent2);
                dialog.dismiss();
            }
        });
        re_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(CurrentDynamicActivity.this,MomentsPublishActivity.class);
                intent3.putExtra("biaoshi","shangye");
                startActivity(intent3);
                dialog.dismiss();
            }
        });
        re_item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(CurrentDynamicActivity.this,MomentsPublishActivity.class);
                intent4.putExtra("biaoshi","youhui");
                startActivity(intent4);
                dialog.dismiss();
            }
        });
        re_item6.setVisibility(View.VISIBLE);
        re_item6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(CurrentDynamicActivity.this,MomentsPublishActivity.class);
                intent6.putExtra("biaoshi","xuqiu");
                startActivity(intent6);
                dialog.dismiss();
            }
        });
        re_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public void initFile() {
        File dir = new File("/sdcard/bizchat");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void back(View view) {
        super.back(view);
    }




}
