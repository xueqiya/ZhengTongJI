package com.sangu.apptongji.main.qiye.model.impl;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.model.IQiYeDetailModel;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016-12-28.
 */

public class QiYeDetailModel implements IQiYeDetailModel {
    private Context mcontext=null;

    public QiYeDetailModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getQiYeInfo(String qiyeid, final OnQiyeListener onQiyeListener) {
        onQiyeListener.onStart();
        String url = FXConstant.URL_Get_QiyeInfo+qiyeid;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONObject jsonObject = object.getJSONObject("companyInfo");
                    QiYeInfo info = JSONParser.parseQiye(jsonObject);
                    String remark = jsonObject.isNull("remark")?"":jsonObject.getString("remark");
                    DemoApplication.getInstance().setCurrentQiYeRemark(remark);
                    onQiyeListener.onSuccess(info,true);
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
        });
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
