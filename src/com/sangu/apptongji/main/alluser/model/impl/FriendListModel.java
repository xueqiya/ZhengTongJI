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
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.model.IFriendListModel;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/7.
 */

public class FriendListModel implements IFriendListModel {
    private Context mcontext=null;

    public FriendListModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getFriendList(final String currentPage ,final AsyncCallback callback) {
        String url = FXConstant.URL_FriendList;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("uiList");
                    List<UserAll> users = JSONParser.parseFriendList(jsonArray);
                    callback.onSuccess(users);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("error", "联网的数据库错误" + volleyError.getMessage());
                callback.onError(null);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>param=new HashMap<>();
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                if (currentPage!=null) {
                    param.put("currentPage", currentPage);
                }
                return param;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
