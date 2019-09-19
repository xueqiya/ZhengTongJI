package com.sangu.apptongji.main.alluser.model.impl;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.DianziDanju;
import com.sangu.apptongji.main.alluser.model.IDzdanjuListModel;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/9/1.
 */

public class DzdanjuListModel implements IDzdanjuListModel {
    private Context mcontext=null;

    public DzdanjuListModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getDzdanjuList(final String u_id, final String currentPage, final AsyncCallback callback) {
        String url = FXConstant.URL_CHAXUN_DZ_DANJU_LIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray ary = jsonObject.getJSONArray("list");
                    if (ary==null||ary.length()==0){
                        callback.onSuccess(null);
                    }else {
                        List<DianziDanju> dianziDanjus = JSONParser.parseDZdanjuList(ary);
                        callback.onSuccess(dianziDanjus);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("error", "联网的数据库错误" + volleyError.getMessage());
                callback.onError(volleyError);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>param=new HashMap<>();
                param.put("u_id", u_id);
                param.put("currentPage", currentPage);
                return param;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
