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
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.model.IPaidanFinishModel;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/7.
 */

public class PaidanFinishModel implements IPaidanFinishModel {
    private Context mcontext=null;

    public PaidanFinishModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }
    @Override
    public void getShifuList(final String dynamicSeq, final String createTime, final String sId, final String state, final AsyncCallback callback) {
        String url = FXConstant.URL_QUERY_DEAL_CONTACT;
        Log.d("chen", "getShifuList" + dynamicSeq + "   "+ createTime + "   " + sId);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", sId + "收到师傅列表"+s);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("code");
                    List<UserAll> users = JSONParser.parsePaidanShifuList(jsonArray);
                    callback.onSuccess(users);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("error", "联网的数据库错误" + volleyError.getMessage());
                Log.d("chen", "getShifuList onErrorResponse--" + volleyError.getMessage());
                callback.onError(null);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>param=new HashMap<>();
                param.put("dynamicSeq", dynamicSeq);
                param.put("createTime", createTime);
                param.put("contactId", sId);
                param.put("type", "00");

                return param;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }

    @Override
    public void updateShifuContace(String uId, String sId, String type, final AsyncCallback callback) {

    }
}
