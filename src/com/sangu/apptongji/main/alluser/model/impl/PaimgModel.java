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
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.model.IPaimgModel;
import com.sangu.apptongji.main.alluser.presenter.OnFindListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/8/29.
 */

public class PaimgModel implements IPaimgModel{
    private Context mcontext=null;
    public PaimgModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }
    @Override
    public void getUserList(final String qiyeId,final String com_id,final String currentPage,final OnFindListener onFindListener) {
        onFindListener.onStart();
        String url = FXConstant.URL_QUERY_READDETAIL;
        StringRequest request = new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    boolean hasMore = true;
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("list");
                    if (array==null||array.length()<20){
                        hasMore = false;
                    }else {
                        hasMore = true;
                    }
                    List<UserAll> users = JSONParser.parsePaimgList(array);
                    onFindListener.onSuccess(users,hasMore);
                    onFindListener.onFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null) {
                    onFindListener.onFailed(volleyError.getMessage());
                }else {
                    onFindListener.onFailed(null);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("currentPage", currentPage);
                params.put("companyId",qiyeId);
                params.put("com_id",com_id);
                Log.e("PaimgModel,param",params.toString());
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
