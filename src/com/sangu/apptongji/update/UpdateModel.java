package com.sangu.apptongji.update;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016-12-19.
 */

public class UpdateModel implements IUpdateModel {
    private Context mcontext=null;

    public UpdateModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getUpdateInfo(final AsyncCallback callback) {
        String url = FXConstant.URL_SEARCH_UPDATE;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject jsonObject = object.getJSONObject("versionInfo");
                    ApkInfo info = JSONParser.parseApkInfo(jsonObject);
                    callback.onSuccess(info);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
