package com.sangu.apptongji.main.alluser.model.impl;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Friend;
import com.sangu.apptongji.main.alluser.model.IFriendQModel;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/8/31.
 */

public class FriendModel implements IFriendQModel {
    private Context mcontext=null;

    public FriendModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getFriendQList(final AsyncCallback callback) {
        String url = FXConstant.URL_CONFIRMQUERY_FRIEND;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray ary = jsonObject.getJSONArray("uiList");
                    List<Friend> friends = JSONParser.parseFriendQList(ary);
                    callback.onSuccess(friends);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("--查询好友请求网络错误--",volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>param=new HashMap<>();
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
