package com.sangu.apptongji.main.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.moments.SocialMainAdapter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/15.
 */

public class CollectionDynamicActivity extends BaseActivity {

    private TextView tv_lifeText,tv_locationText,tv_proText,tv_footprint;
    private XRecyclerView xl_recyclerView;
    private int currentPage = 1;
    private  String dType = "01",userId="0"; //userId等于0查收藏等于1查自己动态
    private List<JSONObject> datas=new ArrayList<>();
    private SocialMainAdapter adapter;
    private String isClick = "yes";
    private Dialog mWeiboDialog;
    private RelativeLayout rl_footprint;
  //private TextView tv_none;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_collectiondynamic);

        initView();

        String identify = getIntent().getStringExtra("identify");

        if (identify != null && identify.equals("1")){

            rl_footprint.setVisibility(View.VISIBLE);
            tv_lifeText.setTextColor(Color.parseColor("#ffbebebe"));

            dType = "02";
            SelectMineLocationDynamic();

        }else {
            SelectCollectionInfo();
        }

    }

    private void initView(){

        tv_lifeText = (TextView) findViewById(R.id.tv_liftText);
        tv_locationText = (TextView) findViewById(R.id.tv_locationText);
        tv_proText = (TextView) findViewById(R.id.tv_proText);
        rl_footprint = (RelativeLayout) findViewById(R.id.rl_footprint);
        tv_footprint = (TextView) findViewById(R.id.tv_footprint);
      //  tv_none = (TextView) findViewById(R.id.tv_none);

        xl_recyclerView = (XRecyclerView) findViewById(R.id.xl_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xl_recyclerView.setLayoutManager(layoutManager);

        xl_recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xl_recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xl_recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        rl_footprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClick = "yes";
                dType = "02";
                userId = "1";//查自己的坐标动态

                tv_footprint.setTextColor(Color.parseColor("#FF3E4A"));
                tv_lifeText.setTextColor(Color.parseColor("#ffbebebe"));
                tv_locationText.setTextColor(Color.parseColor("#ffbebebe"));
                tv_proText.setTextColor(Color.parseColor("#ffbebebe"));

                SelectMineLocationDynamic();

            }
        });

        tv_lifeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // tv_none.setVisibility(View.INVISIBLE);
                isClick = "yes";
                dType = "01";
                userId = "0";//查收藏

                tv_footprint.setTextColor(Color.parseColor("#ffbebebe"));
                tv_lifeText.setTextColor(Color.parseColor("#FF3E4A"));
                tv_locationText.setTextColor(Color.parseColor("#ffbebebe"));
                tv_proText.setTextColor(Color.parseColor("#ffbebebe"));

                SelectCollectionInfo();

            }

        });

        tv_locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //tv_none.setVisibility(View.INVISIBLE);
                isClick = "yes";
                dType = "02";
                userId = "0";//查收藏

                tv_footprint.setTextColor(Color.parseColor("#ffbebebe"));
                tv_lifeText.setTextColor(Color.parseColor("#ffbebebe"));
                tv_locationText.setTextColor(Color.parseColor("#FF3E4A"));
                tv_proText.setTextColor(Color.parseColor("#ffbebebe"));

                SelectCollectionInfo();

            }
        });

        tv_proText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   tv_none.setVisibility(View.INVISIBLE);
                isClick = "yes";
                dType = "03";
                userId = "0";//查收藏

                tv_footprint.setTextColor(Color.parseColor("#ffbebebe"));
                tv_lifeText.setTextColor(Color.parseColor("#ffbebebe"));
                tv_locationText.setTextColor(Color.parseColor("#ffbebebe"));
                tv_proText.setTextColor(Color.parseColor("#FF3E4A"));

                SelectCollectionInfo();

            }
        });


        //下拉刷新跟上拉加载
        xl_recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                currentPage = 1;

                String identify = getIntent().getStringExtra("identify");

                if (identify != null && identify.equals("1")){

                    SelectMineLocationDynamic();

                }else {
                    SelectCollectionInfo();
                }

            }

            @Override
            public void onLoadMore() {

                if (datas.size() % 20 == 0){

                    currentPage = currentPage+1;

                    String identify = getIntent().getStringExtra("identify");

                    if (identify != null && identify.equals("1")){

                        SelectMineLocationDynamic();

                    }else {
                        SelectCollectionInfo();
                    }

                }else {

                    xl_recyclerView.loadMoreComplete();
                    //tv_none.setVisibility(View.VISIBLE);
                    //tv_none.setText("已加载全部数据");
                    Toast.makeText(CollectionDynamicActivity.this,"已加载全部数据...",Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void SelectMineLocationDynamic(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CollectionDynamicActivity.this, "加载中...");
        String url = FXConstant.URL_PUBLISH_QUERY;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {

                    WeiboDialogUtils.closeDialog(mWeiboDialog);

                    JSONObject object = JSON.parseObject(s);

                    JSONArray collectionlist = object.getJSONArray("clist");

                    if (currentPage == 1) {

                        xl_recyclerView.refreshComplete();
                        if (datas != null && datas.size()>0) {

                            datas.clear();

                            adapter.notifyDataSetChanged();

                        }

                    }else {

                        xl_recyclerView.loadMoreComplete();

                    }


                    if (collectionlist.size() > 0){

                        for (int i = 0; i < collectionlist.size(); i++) {

                            JSONObject json = collectionlist.getJSONObject(i);

                            if (datas != null) {
                                datas.add(json);
                            }

                        }

                        if (currentPage == 1 && isClick.equals("yes")) {

                            isClick = "no";
                            adapter = new SocialMainAdapter(CollectionDynamicActivity.this,datas,dType,"取消收藏","00","",0,"0");
                            xl_recyclerView.setAdapter(adapter);


                            if (dType.equals("01")){
                                setAdapter1();
                            }else if (dType.equals("02")){
                                setAdapter2();
                            }else if (dType.equals("03")){
                                setAdapter3();
                            }

                        }else {

                            adapter.notifyDataSetChanged();
                        }

                    }else {

                        //   tv_none.setVisibility(View.VISIBLE);
                        //   tv_none.setText("还没有收藏");
                        Toast.makeText(CollectionDynamicActivity.this,"暂无足迹...",Toast.LENGTH_SHORT).show();

                    }

                }catch (JSONException e){

                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);
                if (currentPage == 1) {
                    xl_recyclerView.refreshComplete();
                } else {
                    xl_recyclerView.loadMoreComplete();
                }
                Toast.makeText(CollectionDynamicActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

            }

        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                param.put("currentPage",currentPage+"");
                param.put("dType",dType);
                if (DemoHelper.getInstance().isLoggedIn(CollectionDynamicActivity.this)) {
                    param.put("loginId",DemoHelper.getInstance().getCurrentUsernName());
                }

                return param;

            }
        };

        MySingleton.getInstance(CollectionDynamicActivity.this).addToRequestQueue(request);

    }

    private void SelectCollectionInfo(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(CollectionDynamicActivity.this, "加载中...");
        String url = FXConstant.URL_SELECTDYNAMICCOLLECTION;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {

                    WeiboDialogUtils.closeDialog(mWeiboDialog);

                    JSONObject object = JSON.parseObject(s);

                    JSONArray collectionlist = object.getJSONArray("dynamicInfoList");

                    if (currentPage == 1) {

                        xl_recyclerView.refreshComplete();
                        if (datas != null && datas.size()>0) {

                            datas.clear();

                            adapter.notifyDataSetChanged();

                        }

                    }else {

                        xl_recyclerView.loadMoreComplete();

                    }


                    if (collectionlist.size() > 0){

                        for (int i = 0; i < collectionlist.size(); i++) {

                            JSONObject json = collectionlist.getJSONObject(i);

                            if (datas != null) {
                                datas.add(json);
                            }

                        }

                        if (currentPage == 1 && isClick.equals("yes")) {

                            isClick = "no";
                            adapter = new SocialMainAdapter(CollectionDynamicActivity.this,datas,dType,"取消收藏","00","",0,"0");
                            xl_recyclerView.setAdapter(adapter);


                            if (dType.equals("01")){
                                setAdapter1();
                            }else if (dType.equals("02")){
                                setAdapter2();
                            }else if (dType.equals("03")){
                                setAdapter3();
                            }

                        }else {

                            adapter.notifyDataSetChanged();
                        }

                    }else {

                     //   tv_none.setVisibility(View.VISIBLE);
                     //   tv_none.setText("还没有收藏");
                        Toast.makeText(CollectionDynamicActivity.this,"暂无收藏...",Toast.LENGTH_SHORT).show();

                    }

                }catch (JSONException e){

                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);
                if (currentPage == 1) {
                    xl_recyclerView.refreshComplete();
                } else {
                    xl_recyclerView.loadMoreComplete();
                }
                Toast.makeText(CollectionDynamicActivity.this,"网络不稳定,请稍后重试",Toast.LENGTH_SHORT).show();

            }

        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
                param.put("currentPage",currentPage+"");
                param.put("type",dType);

                return param;

            }
        };

        MySingleton.getInstance(CollectionDynamicActivity.this).addToRequestQueue(request);

    }



    private void setAdapter1() {

        adapter.setOnItemClickListener(new SocialMainAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!DemoHelper.getInstance().isLoggedIn(CollectionDynamicActivity.this)){
                    Toast.makeText(CollectionDynamicActivity.this,"请您先登录！",Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject object = datas.get(position-1);
                String type;
                if (object.getString("fromUId") != null) {
                    type = "02";
                } else {
                    type = "01";
                }
                String sID = object.getString("uLoginId");
                String dynamic_seq = object.getString("dynamicSeq");
                String createTime = object.getString("createTime");
                Intent intent = new Intent(CollectionDynamicActivity.this, DynaDetaActivity.class);
                intent.putExtra("sID", sID);
                intent.putExtra("profession","");
                intent.putExtra("dynamicSeq",dynamic_seq);
                intent.putExtra("createTime",createTime);
                intent.putExtra("dType", dType);
                intent.putExtra("type", type);
                intent.putExtra("type2", "00");
                startActivityForResult(intent,0);
            }
        });

    }

    private void setAdapter2() {

        adapter.setOnItem2ClickListener(new SocialMainAdapter.MyItem2ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!DemoHelper.getInstance().isLoggedIn(CollectionDynamicActivity.this)){
                    Toast.makeText(CollectionDynamicActivity.this,"请您先登录！",Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject object = datas.get(position-1);
                String type;
                if (object.getString("fromUId") != null) {
                    type = "02";
                } else {
                    type = "01";
                }
                String sID = object.getString("uLoginId");
                String dynamic_seq = object.getString("dynamicSeq");
                String createTime = object.getString("createTime");
                Intent intent = new Intent(CollectionDynamicActivity.this, DynaDetaActivity.class);
                intent.putExtra("sID", sID);
                intent.putExtra("profession","");
                intent.putExtra("dynamicSeq",dynamic_seq);
                intent.putExtra("createTime",createTime);
                intent.putExtra("dType", dType);
                intent.putExtra("type", type);
                startActivityForResult(intent,0);
            }
        });

    }

    private void setAdapter3() {

        adapter.setOnItem3ClickListener(new SocialMainAdapter.MyItem3ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("chen", "点击进入");
                if (!DemoHelper.getInstance().isLoggedIn(CollectionDynamicActivity.this)){
                    Toast.makeText(CollectionDynamicActivity.this,"请您先登录！",Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject object = datas.get(position-1);
                String type;
                if (object.getString("fromUId") != null) {
                    type = "02";
                } else {
                    type = "01";
                }
                String sID = object.getString("uLoginId");
                String dynamic_seq = object.getString("dynamicSeq");
                String createTime = object.getString("createTime");
                String task_jurisdiction = object.getString("task_jurisdiction");

                //长度大于8的证明是链接 点击跳转网页  如果点击下方浏览一系列按钮 就跳到详情
                if (task_jurisdiction!= null && task_jurisdiction.length()>8){

                    Intent intent = new Intent(CollectionDynamicActivity.this, ProjectDynamicLinkDetailActivity.class);

                    intent.putExtra("orderType",object.getString("orderType"));
                    intent.putExtra("lat",object.getString("lat"));
                    intent.putExtra("lng",object.getString("lng"));
                    intent.putExtra("salePrice",object.getString("salePrice"));
                    intent.putExtra("price",object.getString("price"));
                    intent.putExtra("sID", sID);
                    intent.putExtra("profession","");
                    intent.putExtra("dynamicSeq",dynamic_seq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("task_jurisdiction",task_jurisdiction);
                    intent.putExtra("task_label",object.getString("task_label"));
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    startActivityForResult(intent,0);

                }else {

                    Intent intent = new Intent(CollectionDynamicActivity.this, DynaDetaActivity.class);
                    intent.putExtra("sID", sID);
                    intent.putExtra("profession","");
                    intent.putExtra("dynamicSeq",dynamic_seq);
                    intent.putExtra("createTime",createTime);
                    intent.putExtra("dType", dType);
                    intent.putExtra("type", type);
                    startActivityForResult(intent,0);

                }
            }
        });
    }

}
