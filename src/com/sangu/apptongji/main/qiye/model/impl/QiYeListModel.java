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
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.model.IQiYeListModel;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-30.
 */

public class QiYeListModel implements IQiYeListModel {
    private Context mcontext=null;
    public QiYeListModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getQiYeList(final String currentPage, final String lat, final String lng, final String comName, final String zhuanye, final boolean renshu, final boolean jingyan, final boolean hasbao, final OnQiyeListener onQiyeListener) {
        onQiyeListener.onStart();
        String url = FXConstant.URL_Get_QiyeList;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    boolean hasMore = true;
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("companyInfoList");
                    if (array==null||array.length()<20){
                        hasMore = false;
                    }else {
                        hasMore = true;
                    }
                    List<QiYeInfo> qiYeInfos = JSONParser.parseQiyeList(array);
                    onQiyeListener.onSuccess(qiYeInfos,hasMore);
                    onQiyeListener.onFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onQiyeListener.onFailed();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("currentPage",currentPage);
                if (jingyan||renshu||hasbao||(comName!=null&&!"".equals(comName)&&!"0".equals(comName))||(zhuanye!=null&&!"".equals(zhuanye)&&!"0".equals(zhuanye))){
                    param.put("overall_flg","01");
                }else {
                  //  param.put("overall_flg","01");
                  //  param.put("shareRed","1");
                }

                if (hasbao){
                    param.put("overall_flg","01");
                    param.put("shareRed","1");
                }

                if (!renshu&&!jingyan&&!hasbao){
                    param.put("comLatitude",lat);
                    param.put("comLongitude",lng);
                }
                if (jingyan) {
                    param.put("createTime","1");
                }
                if (renshu){
                    param.put("memberNum","1");
                }
                if (hasbao){
                    param.put("margin","1");
                }
                if (comName!=null&&!"".equals(comName)&&!"0".equals(comName)){
                    try {
                        String compName = URLEncoder.encode(comName,"UTF-8");
                        param.put("companyName",compName);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if (zhuanye!=null&&!"".equals(zhuanye)&&!"0".equals(zhuanye)){
                    param.put("upName",zhuanye);
                }
                Log.e("qiyeListModel",param.toString());
                return param;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
