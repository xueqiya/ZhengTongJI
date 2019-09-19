package com.sangu.apptongji.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018-02-12.
 */

public class UserPermissionUtil {

    public static void getUserPermission(Context context, final String uid, final String type, final UserPermissionListener listener) {
        RequestQueue queue = MySingleton.getInstance(context.getApplicationContext()).getRequestQueue();
        String url = FXConstant.URL_SELECT_PROHIBIT;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "getUserPermission" + s);
                try {
                    JSONObject object = new JSONObject(s);
                    String result = object.isNull("code") ? "fail" : object.getString("code");
                    if (result.equalsIgnoreCase("SUCCESS")) {
                        listener.onAllow();
                    } else {
                        listener.onBan();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "getUserPermission volleyError" + volleyError.getMessage());
                listener.onAllow();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uId", uid);
                params.put("prohibittype", type);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(request);
    }
    public interface UserPermissionListener{
        void onAllow();

        void onBan();
    }
}
