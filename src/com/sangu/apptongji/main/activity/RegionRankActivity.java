package com.sangu.apptongji.main.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.R;
import com.sangu.apptongji.adapter.RegionRankAdapter;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.WeiboDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionRankActivity extends BaseActivity {

    private ListView listView;
    private Dialog mWeiboDialog;
    private RegionRankAdapter adapter;
    private List<JSONObject> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_regionrank);

        listView = (ListView) findViewById(R.id.listView);

        GetRegionRankList();

    }


    private void GetRegionRankList(){

        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(RegionRankActivity.this, "加载中...");

        String url = FXConstant.URL_SELECTREGIONALRANK;

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                WeiboDialogUtils.closeDialog(mWeiboDialog);

                JSONObject jsonObject = JSON.parseObject(s);

                JSONArray jsonArray = jsonObject.getJSONArray("code");


                for (Object object : jsonArray){

                    JSONObject object1 = (JSONObject) object;

                    datas.add(object1);
                }

                adapter = new RegionRankAdapter(RegionRankActivity.this,datas,"0");

                listView.setAdapter(adapter);

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

                return param;
            }
        };

        MySingleton.getInstance(RegionRankActivity.this).addToRequestQueue(request);

    }

}
