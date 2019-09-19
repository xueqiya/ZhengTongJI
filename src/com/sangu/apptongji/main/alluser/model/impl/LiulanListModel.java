package com.sangu.apptongji.main.alluser.model.impl;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.LiuLanDetail;
import com.sangu.apptongji.main.alluser.model.ILiulanListModel;
import com.sangu.apptongji.main.alluser.presenter.OnLiuLanListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-30.
 */

public class LiulanListModel implements ILiulanListModel {
    private Context mcontext=null;

    public LiulanListModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getThisUser(final String currentPage, final String uLoginId, final String leixing, final OnLiuLanListener callback) {
        callback.onStart();
        String url = FXConstant.URL_Search_LiuLanList;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.optJSONArray("visitor");
                    JSONObject jsonObject = object.optJSONObject("amount");
                    String size = object.optString("size");
                    List<LiuLanDetail> liuLanDetails = JSONParser.parseLiulanList(array);
                    if (liuLanDetails.size()==20) {
                        callback.onSuccess(liuLanDetails, jsonObject,size,true);
                    }else {
                        callback.onSuccess(liuLanDetails, jsonObject,size,false);
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
                params.put("uLoginId",uLoginId);
                params.put("currentPage",currentPage);
                if (leixing!=null){
                    params.put(leixing,"1");
                }
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
