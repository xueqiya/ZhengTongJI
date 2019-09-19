package com.sangu.apptongji.main.qiye.model.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.entity.MemberInfo;
import com.sangu.apptongji.main.qiye.model.IMemberModel;
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
 * Created by Administrator on 2017-01-03.
 */

public class MemberModel implements IMemberModel {
    private Context mcontext=null;
    public MemberModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getMemberList(final int currentPage,final OnQiyeListener onQiyeListener) {
        onQiyeListener.onStart();
        SharedPreferences sp2 = mcontext.getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        final String qiyeId = sp2.getString("qiyeId","");
        String url = FXConstant.URL_QUERY_QIYEMAJAR;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.getJSONArray("userInfoList");
                    String totalCompanyCount = object.getString("companyTotal");
                    List<MemberInfo> memberInfoList = JSONParser.parseMemberList(array);
                    if (memberInfoList.size()==20) {
                        onQiyeListener.onSuccess(memberInfoList, true);
                    }else {
                        onQiyeListener.onSuccess(memberInfoList, false);
                    }
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
                Map<String,String> param = new HashMap<>();
                param.put("companyId",qiyeId);
                param.put("v_id", DemoHelper.getInstance().getCurrentUsernName());
                if (currentPage>0) {
                    param.put("currentPage", currentPage + "");
                }
                return param;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
