package com.sangu.apptongji.main.qiye.model.impl;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.model.IQMajModel;
import com.sangu.apptongji.main.utils.MySingleton;

/**
 * Created by Administrator on 2017-05-16.
 */

public class MajQModel implements IQMajModel {
    private Context mcontext=null;

    public MajQModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getMajQList(final AsyncCallback callback) {
        String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        String url = FXConstant.URL_QUERY_ADD + qiyeId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("查询企业请求网络错误","网络连接错误");
            }
        });
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
