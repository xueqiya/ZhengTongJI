package com.sangu.apptongji.main.alluser.model.impl;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.model.IPriceModel;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/9/8.
 */

public class PriceModel implements IPriceModel {
    private Context mcontext=null;
    public PriceModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }
    @Override
    public void getCurrentPrice(final String id, final AsyncCallback callback) {
        String url = FXConstant.URL_Query_YuE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object1 = new JSONObject(s);
                    JSONObject object = object1.getJSONObject("merAccount");
                    DemoApplication app = DemoApplication.getApp();
                    app.saveCurrentFreezetype(object.isNull("freezetype") ? "00" : object.getString("freezetype"));
                    if (id.length()>12){
                        app.saveCurrentQiyePayPass(object.isNull("paypassword") ? "" : object.getString("paypassword"));
                        app.saveCurrentQiyePrice(Double.parseDouble(object.isNull("balance") ? "" : object.getString("balance")));
                        app.saveCurrentQiyeChzYuE(object.isNull("resv2") ? "" : object.getString("resv2"));
                        app.saveCurrentQiyetxYuE(object.isNull("resv3") ? "" : object.getString("resv3"));
                    }else {
                        app.saveCurrentPrice(Double.parseDouble(object.isNull("balance") ? "" : object.getString("balance")));
                        app.saveCurrentChzYuE(object.isNull("resv2") ? "" : object.getString("resv2"));
                        app.saveCurrenttxYuE(object.isNull("resv3") ? "" : object.getString("resv3"));
                        app.saveCurrentPayPass(object.isNull("paypassword") ? "" : object.getString("paypassword"));
                        app.saveCurrentWithdrawals(object.isNull("withdrawals") ? "" : object.getString("withdrawals"));
                    }
                    callback.onSuccess(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError("获取数据失败");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("merId", id);
                return param;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
