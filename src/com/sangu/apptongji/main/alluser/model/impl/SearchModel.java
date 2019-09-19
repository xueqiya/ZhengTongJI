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
import com.sangu.apptongji.main.alluser.model.ISearchModel;
import com.sangu.apptongji.main.alluser.order.entity.DIDAList;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-09-28.
 */

public class SearchModel implements ISearchModel {
    private Context mcontext=null;
    public SearchModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }
    @Override
    public void getThisDIDA(final String upId, final AsyncCallback callback) {
        String url = FXConstant.URL_Search_DingDanList;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "getThisDIDA" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    String time = object.getString("time");
                    Log.d("chen", "getThisDIDA2" + time);
                    JSONArray array = object.getJSONArray("oiInfos");
                    List<DIDAList> didaLists = JSONParser.parseDIDAList(array);
                    callback.onSuccess(didaLists,time);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("upId",upId);
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
