package com.sangu.apptongji.main.alluser.model.impl;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.GroupInfo;
import com.sangu.apptongji.main.alluser.model.IGroupModel;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-11-14.
 */

public class GroupModel implements IGroupModel {
    private Context mcontext=null;
    public GroupModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getUserList(final String u_id,final String currentPage,final AsyncCallback callback) {
        String url = FXConstant.URL_SELECT_GROUP;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("groupInfo");
                    String sum = object.getString("sum");
                    List<GroupInfo> groupInfos = JSONParser.parseGroupList(array,sum);
                    callback.onSuccess(groupInfos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError(null);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",u_id);
                param.put("currentPage",currentPage);
                return param;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
