package com.sangu.apptongji.main.alluser.model.impl;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.model.IGrMemModel;
import com.sangu.apptongji.main.alluser.presenter.OnGrMemListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-11-16.
 */

public class GrMemModel implements IGrMemModel {
    private Context mcontext=null;

    public GrMemModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getUserList(final String groupId, final String currentPage, final OnGrMemListener callback) {
        callback.onStart();
        String url = FXConstant.URL_SELECT_MEMBERLIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.optJSONArray("list");
                    JSONObject jsonObject = object.optJSONObject("groupInfo");
                    List<UserAll> liuLanDetails = JSONParser.parseUserList(array);
                    if (liuLanDetails.size()==20) {
                        callback.onSuccess(liuLanDetails, jsonObject,true);
                    }else {
                        callback.onSuccess(liuLanDetails, jsonObject,false);
                    }
                    callback.onFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onFailed();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("groupId",groupId);
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
