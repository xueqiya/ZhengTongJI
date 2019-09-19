package com.sangu.apptongji.main.alluser.model.impl;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.BaoZInfo;
import com.sangu.apptongji.main.alluser.model.IbzModel;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-08-08.
 */

public class BzModel implements IbzModel {
    private Context mcontext=null;

    public BzModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getBaozList(final AsyncCallback callback) {
        String url = FXConstant.URL_QUERY_BZLIST;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray ary = jsonObject.getJSONArray("clist");
                    List<BaoZInfo> baozInfos = JSONParser.parseBaozList(ary);
                    callback.onSuccess(baozInfos);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("error", "联网的数据库错误" + volleyError.getMessage());
            }
        });
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
