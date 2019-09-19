package com.sangu.apptongji.main.qiye.model.impl;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.entity.KaoqinInfo;
import com.sangu.apptongji.main.qiye.model.IKaoqinListModel;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-01-12.
 */

public class KaoqinListModel implements IKaoqinListModel {
    private Context mcontext=null;
    public KaoqinListModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }
    @Override
    public void getKaoqqinList(final String remark,final String startTime,final String endTime,final OnQiyeListener onQiyeListener) {
        onQiyeListener.onStart();
        final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        String url = FXConstant.URL_KAIQUN_LIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.optJSONArray("clockRecordInfos");
                    List<KaoqinInfo> kaoqinInfos = JSONParser.parseKaoqinList(array);
                    onQiyeListener.onSuccess(kaoqinInfos,true);
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
                param.put("companyId",qiyeId);
                param.put("remark",remark);
                param.put("startTime",startTime);
                param.put("endTime",endTime);
                return param;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }

    /*@Override
    public void getKaoqqinList(final OnQiyeListener onQiyeListener) {
        onQiyeListener.onStart();
        final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        //final String qiyeId = "5034667474945";
        String url = FXConstant.URL_QIYE_KAOQIN + qiyeId;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject object = new JSONObject(s);
                    QiyeKaoQinInfo qiyeKaoQinInfoes = JSONParser.parseQiyeKaoQin(object);
                    onQiyeListener.onSuccess(qiyeKaoQinInfoes,true);
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
        })*//*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("companyId",qiyeId);
                param.put("remark",remark);
                param.put("startTime",startTime);
                param.put("endTime",endTime);
                return param;
            }
        }*//*;
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }*/
}
