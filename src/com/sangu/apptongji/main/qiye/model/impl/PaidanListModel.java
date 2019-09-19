package com.sangu.apptongji.main.qiye.model.impl;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.entity.PaiDanInfo;
import com.sangu.apptongji.main.qiye.model.IPaidanListModel;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-01-17.
 */

public class PaidanListModel implements IPaidanListModel {
    private Context mcontext=null;

    public PaidanListModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getPaidanList(final String companyId, final String userId, final OnQiyeListener onQiyeListener) {
        onQiyeListener.onStart();
        String url = FXConstant.URL_QIYE_PAIDANLIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.optJSONArray("orderInfo");
                    List<PaiDanInfo> paiDanInfos = JSONParser.parsePaidanList(array);
                    onQiyeListener.onSuccess(paiDanInfos,true);
                    onQiyeListener.onFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onQiyeListener.onFailed();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> param = new HashMap<>();
                param.put("companyId",companyId);
                if (!"".equals(userId)){
                    param.put("userId",userId);
                }
                return param;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
