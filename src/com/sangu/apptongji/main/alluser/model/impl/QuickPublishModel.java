package com.sangu.apptongji.main.alluser.model.impl;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.model.IModel;
import com.sangu.apptongji.main.alluser.model.IQuickPublishModel;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-01-16.
 */

public class QuickPublishModel implements IQuickPublishModel {
    private Context mcontext = null;

    public QuickPublishModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    public void quickSend(final String userid, final String title, final String content, final String lng, final String lat, final IModel.AsyncCallback callback) {
        String url = FXConstant.URL_INSERT_KUAISUDINGDAN;

        //http://192.168.1.120/speedList/insertList?uId=15513994458&content=123&&log=1&lat=1
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError("获取数据失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uId", userid);
                params.put("content", content);
                params.put("log", lng);
                params.put("lat", lat);
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }

    @Override
    public void quickUpdate(final String userid, final String sId, final String content, final String display, final IModel.AsyncCallback callback) {
        String url = FXConstant.URL_UPDATE_KUAISUDINGDAN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callback.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError("获取数据失败");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uId", userid);
                params.put("sId", sId);
                params.put("content", content);
                params.put("display", display);
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }

    @Override
    public void quickVoicePublish(final String uId, final String content, String file, final String lng, final String lat, final IModel.AsyncCallback callback) {
        File fileVoice = new File(file);
        final List<File> files1 = new ArrayList<File>();
        files1.add(fileVoice);
        //String url = FXConstant.URL_INSERT_KUAISUDINGDAN + "?uId=" + uId + "&content=" + content + "&log=" + lng + "&lat=" + lat + "&file=" + files1;
        List<Param> params = new ArrayList<>();
        params.add(new Param("uId", uId));
        params.add(new Param("content", content));
        params.add(new Param("log", lng));
        params.add(new Param("lat", lat));
        OkHttpManager.getInstance().postQuickPaidanWithVoice(params, "file", files1, FXConstant.URL_INSERT_KUAISUDINGDAN, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                callback.onSuccess("发送成功");
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(errorMsg);

            }
        });

    }

    @Override
    public void quickVoiceUpdate(final String uId, final String content, String file, final String sId, final String display, final IModel.AsyncCallback callback) {
        File fileVoice = new File(file);
        final List<File> files1 = new ArrayList<File>();
        files1.add(fileVoice);
        //String url = FXConstant.URL_INSERT_KUAISUDINGDAN + "?uId=" + uId + "&content=" + content + "&log=" + lng + "&lat=" + lat + "&file=" + files1;
        List<Param> params = new ArrayList<>();
        params.add(new Param("uId", uId));
        params.add(new Param("sId", sId));
        params.add(new Param("content", content));
        params.add(new Param("display", display));
        OkHttpManager.getInstance().postQuickPaidanWithVoice(params, "file", files1, FXConstant.URL_UPDATE_KUAISUDINGDAN, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                callback.onSuccess("发送成功");
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onError(errorMsg);

            }
        });

    }
}
