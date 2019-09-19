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
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.model.IUAZModel;
import com.sangu.apptongji.main.alluser.presenter.OnprofileListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sangu.apptongji.main.utils.OkHttpManager.context;

/**
 * Created by Administrator on 2016-09-26.
 */

public class UAZModel implements IUAZModel {
    private Context mcontext=null;

    public UAZModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }
    @Override
    public void getThisUser(final String hxid, final OnprofileListener onprofileListener) {
        onprofileListener.onStart();
        String url = FXConstant.URL_Get_UserInfo+hxid;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject object = jsonObject.getJSONObject("userInfo");
                    Userful user2 = JSONParser.parseUser(object);
                    onprofileListener.onSuccess(user2);
                    onprofileListener.onFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    onprofileListener.onFailed();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        onprofileListener.onFailed();
                    }
                });
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
    @Override
    public void sendContactTrack(final String u_id, final String contact_id, final String type) {
        Log.d("chen", "sendContactTrack u_id  " + u_id + "contact_id  " + contact_id + "type " + type);
        /*
        * http://www.fulu86.com/data/insertContact?u_id=18337101357&contact_id=15513994458&type=电话
        * */
        String url = FXConstant.URL_INSERT_CONTACT_TRACK;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "sendContactTrack onResponse" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "sendContactTrack onErrorResponse" + volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("u_id", u_id);
                param.put("contact_id", contact_id);
                param.put("type", type);
                return param;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);


    }
}
