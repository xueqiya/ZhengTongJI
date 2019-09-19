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
import com.sangu.apptongji.main.alluser.model.IFindModel;
import com.sangu.apptongji.main.alluser.presenter.OnFindListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2016/8/29.
 */

public class FindModel implements IFindModel{
    private Context mcontext=null;
    public FindModel(Context context) {
        mcontext=context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }
    @Override
    public void getUserList(final String u_id,final String str,final String overall_flg ,final String lng, final String lat, final String zy, final String bZj,
                            final String sex,final String ageStart,final String ageEnd,final String name, final String comName,
                            final boolean gongsi, final boolean jingyan, final boolean hongbao,final OnFindListener onFindListener) {
        onFindListener.onStart();
        String url = FXConstant.URL_Search_User;
        StringRequest request = new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    boolean hasMore = true;
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("clist");
                    if (array==null||array.length()<20){
                        hasMore = false;
                    }else {
                        hasMore = true;
                    }
                    List<UserAll> users = JSONParser.parseUserList(array);
                    onFindListener.onSuccess(users,hasMore);
                    onFindListener.onFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null) {
                    onFindListener.onFailed(volleyError.getMessage());
                }else {
                    onFindListener.onFailed(null);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                if ("1".equals(overall_flg)) {
                    if ((zy==null||"".equals(zy)) && bZj==null && !gongsi && !jingyan && !hongbao && sex==null && (ageStart==null||"".equals(ageStart)) && (ageEnd==null||"".equals(ageEnd))
                            && (name==null||"".equals(name)) && (comName==null||"".equals(comName))) {
                        params.put("currentPage", str);
                        params.put("overall_flg", "1");
                        params.put("long2", lng);
                        params.put("lat2", lat);
                    } else {
                        params.put("currentPage", str);
                        if (sex!=null&&!"".equals(sex)) {
                            params.put("sex", sex);
                        }
                        if (ageStart!=null&&!"".equals(ageStart) && ageEnd!=null&&!"".equals(ageEnd)) {
                            params.put("ageBegin", ageStart);
                            params.put("ageEnd", ageEnd);
                        }
                        if (bZj==null && !jingyan) {
                            params.put("long2", lng);
                            params.put("lat2", lat);
                        }
                        if (bZj!=null&&!"".equals(bZj)) {
                            params.put("margin", bZj);
                        }
                        if (zy!=null&&!"".equals(zy)) {
                            params.put("profession", zy);
                        }
                        if (name!=null&&!"".equals(name)) {
                            params.put("u_telephone", name);
                        }
                        if (comName!=null&&!"".equals(comName)) {
                            params.put("u_company", comName);
                        }
                        if (gongsi) {
                            params.put("company", "1");
                        }
                        if (jingyan) {
                            params.put("creatTime", "2");
                        }
                        if (hongbao) {
                            params.put("shareRedType", "01");
                        }
                    }
                }else {
                    params.put("overall_flg", "2");
                    params.put("currentPage", str);
                    if (!jingyan&&!gongsi){
                        params.put("long2", lng);
                        params.put("lat2", lat);
                    }
                    if (zy!=null&&!"0".equals(zy)){
                        params.put("u_profession","1");
                    }
                    if (bZj!=null&&!"0".equals(bZj)){
                        params.put("margin","1");
                    }
                    if (sex!=null&&!"0".equals(sex)){
                        params.put("u_sex",sex);
                    }
                    if (jingyan){
                        params.put("u_volume","1");
                    }
                    if (gongsi){
                        params.put("u_orderAmt","1");
                    }
                    if (hongbao){
                        params.put("shareRedType","1");
                    }
                    if (ageStart!=null&&!"".equals(ageStart)){
                        params.put("ageBegin",ageStart);
                    }
                    if (ageEnd!=null&&!"".equals(ageEnd)){
                        params.put("ageEnd",ageEnd);
                    }
                    if (comName!=null&&!"".equals(comName)){
                        params.put("company","1");
                    }
                }
                params.put("u_id",u_id);
                Log.e("findModel,param",params.toString());
                return params;
            }
        };
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
