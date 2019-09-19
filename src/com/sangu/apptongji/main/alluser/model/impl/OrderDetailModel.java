package com.sangu.apptongji.main.alluser.model.impl;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.model.IOrderDetailModel;
import com.sangu.apptongji.main.alluser.order.entity.OrderDetail;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2016/9/8.
 */

public class OrderDetailModel implements IOrderDetailModel {
    private Context mcontext=null;

    public OrderDetailModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getCurrentOrderDetail(final String orderId, final AsyncCallback callback) {
        String url = FXConstant.URL_Order_Detail;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
//                    Log.e("onResponse,s",s);
                    JSONObject object1 = new JSONObject(s);
                    JSONObject object = object1.getJSONObject("odi");
                    OrderDetail orderDetail = JSONParser.parseOrderDetail(object);
                    callback.onSuccess(orderDetail);
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
                params.put("orderId",orderId);
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
