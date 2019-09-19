package com.sangu.apptongji.main.qiye.model.impl;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.qiye.model.IKaoqinSetModel;
import com.sangu.apptongji.main.utils.MySingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018-02-23.
 */

public class KaoqinSetModel implements IKaoqinSetModel {
    private Context mcontext;

    public KaoqinSetModel(Context context) {
        this.mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getMonthDate(final String data, final String companyId, final IModel.AsyncCallback callback) {
        String url = FXConstant.URL_QUERY_KAOQIN_SET;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError("获取数据失败");
                Log.d("chen", "getMonthDate onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("companyId", companyId);
                params.put("timestamp", data);
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }

    @Override
    public void updateMonthDate(final String data, final String companyId, final String monthData, final IModel.AsyncCallback callback) {
        String url = FXConstant.URL_UPDATE_KAOQIN_SET;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError("获取数据失败");
                Log.d("chen", "updateMonthDate onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /*
                companyId
                timestamp
                backLog
                * */
                Map<String, String> params = new HashMap<String, String>();
                params.put("companyId", companyId);
                params.put("timestamp", data);
                params.put("backLog", monthData);
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
