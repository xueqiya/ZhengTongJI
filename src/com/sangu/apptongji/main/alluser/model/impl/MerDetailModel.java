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
import com.sangu.apptongji.main.alluser.entity.MerDetail;
import com.sangu.apptongji.main.alluser.model.IMerDetailModel;
import com.sangu.apptongji.main.alluser.presenter.OnMerListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-04-27.
 */

public class MerDetailModel implements IMerDetailModel {
    private Context mcontext;

    public MerDetailModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getThisUser(final String currentPage,final String hxid,final String leixing,final String type,final String beginDate,final String endDate, final OnMerListener onMerListener) {
        onMerListener.onStart();
        String url = FXConstant.URL_MINGZI_DAN;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    boolean hasMore = true;
                    JSONObject object = new JSONObject(s);
                    JSONArray array = object.optJSONArray("list");
                    JSONArray array1 = object.optJSONArray("transCount");
                    String size = object.optString("size");
                    if (array.length()<20){
                        hasMore = false;
                    }
                    String income = object.isNull("income")?"":object.getString("income");
                    String expenditure = object.isNull("expenditure")?"":object.getString("expenditure");
                    List<MerDetail> merDetails=JSONParser.parseMerDetailList(array);
                    onMerListener.onSuccess(merDetails,array1,size,income,expenditure,hasMore);
                    onMerListener.onFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onMerListener.onFailed();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("merId",hxid);
                params.put("currentPage",currentPage);
                if (leixing!=null&&!"".equals(leixing)){
                    params.put("transactionType",leixing);
                }
                if (type!=null&&!"".equals(type)){
                    params.put("accType",type);
                    if ("支出".equals(type)){
                        params.put("beginDate",beginDate);
                        params.put("endDate",endDate);
                    }
                }
                Log.e("merdetailmo,pa",params.toString());
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }

}
