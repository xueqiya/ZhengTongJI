package com.sangu.apptongji.main.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.R;
import com.sangu.apptongji.adapter.RegionRankAdapter;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.moments.SocialMainAdapter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HidenDynamicActivity extends BaseActivity {

    private XRecyclerView listView;
    private Dialog mWeiboDialog;
    private SocialMainAdapter adapter;
    private List<JSONObject> datas = new ArrayList<>();
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_hidendynamic);


        LinearLayoutManager layoutManager = new LinearLayoutManager(HidenDynamicActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView = (XRecyclerView) findViewById(R.id.listView);
        listView.setLayoutManager(layoutManager);

        listView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        listView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        listView.setArrowImageView(R.drawable.iconfont_downgrey);

        listView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                currentPage = 1;

                GetDynamicList();

            }

            @Override
            public void onLoadMore() {

                if (datas.size() % 20 == 0){

                    currentPage ++;
                    GetDynamicList();

                }else {

                    listView.refreshComplete();

                }

            }
        });

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(HidenDynamicActivity.this, "加载中...");
        GetDynamicList();

    }


    private void GetDynamicList(){

        String url = FXConstant.URL_SELECTDYNAMICBYAUTH;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);

                if (currentPage ==1){

                    datas.clear();

                    listView.refreshComplete();
                }else {

                    listView.loadMoreComplete();
                }

                JSONObject jsonObject = JSON.parseObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("clist");


                for (Object object : jsonArray){

                    JSONObject object1 = (JSONObject) object;

                    datas.add(object1);
                }

                if (currentPage ==1){

                    adapter = new SocialMainAdapter(HidenDynamicActivity.this,datas,"05","安装","00","0"+"|"+"3"+"|"+"05"+"|0",3,"0");

                    listView.setAdapter(adapter);

                }else {

                    adapter.notifyDataSetChanged();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                WeiboDialogUtils.closeDialog(mWeiboDialog);

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> param = new HashMap<>();

                param.put("currentPage",""+currentPage);

                return param;
            }
        };

        MySingleton.getInstance(HidenDynamicActivity.this).addToRequestQueue(request);

    }


}
