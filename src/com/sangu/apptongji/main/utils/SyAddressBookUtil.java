package com.sangu.apptongji.main.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.address.KyqInfo;
import com.sangu.apptongji.main.address.PhoneInfo;
import com.sangu.apptongji.main.callback.IError;
import com.sangu.apptongji.main.callback.IFailure;
import com.sangu.apptongji.main.callback.ISuccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-12-28.
 */

public final class SyAddressBookUtil {
    private final Context mContext;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private List<PhoneInfo> LISTS = new ArrayList<>();

    SyAddressBookUtil(
            List<PhoneInfo> lists,
            ISuccess success,
            IFailure failure,
            IError error,
            Context context) {
        this.LISTS = lists;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.mContext = context;
    }

    public static SyAddressBookBuilder builder() {
        return new SyAddressBookBuilder();
    }


    public void getKyqList(){
        final List list = new ArrayList();
        for (int i=0; i <LISTS.size(); i++){
            String num = LISTS.get(i).getNumber();
            list.add(num);
        }
        String str = list.toString();
        if (str!=null){
            str = str.substring(1,str.length()-1);
        }
        String url = FXConstant.URL_SEARCH_ADDRESS_FRIEND;
        final String finalStr = str;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray arrayKyq = object.getJSONArray("browse");
                    JSONArray array2 = object.getJSONArray("stranger");
                    JSONArray array3 = object.getJSONArray("friend");
                    double browSize = 0.0;
                    if (array2!=null&&array2.length()>0){
                        browSize += array2.length();
                    }
                    if (array3!=null&&array3.length()>0){
                        browSize += array3.length();
                    }
                    double d2 = LISTS.size();
                    Log.e("syaddress,per1",d2+"");
                    Log.e("syaddress,brow1",browSize+"");
                    Log.e("syaddress,LISTS1",LISTS.size()+"");
                    List<String> list_selected = new ArrayList<>();
                    List<KyqInfo> listsKyq = new ArrayList<>();
                    double per = (browSize/d2)*100;
                    Log.e("syaddress,per1",per+"");
                    if (arrayKyq!=null&&arrayKyq.length()>0) {
                        for (int i = 0; i < arrayKyq.length(); i++) {
                            String id = arrayKyq.getString(i);
                            KyqInfo kyqInfo = new KyqInfo();
                            for (int j = 0; j < LISTS.size(); j++) {
                                if (LISTS.get(j).getNumber().equals(id)) {
                                    kyqInfo.setName(LISTS.get(j).getName());
                                    kyqInfo.setId(LISTS.get(j).getNumber().trim());
                                    list_selected.add(LISTS.get(j).getNumber().trim());
                                    break;
                                }
                            }
                            listsKyq.add(kyqInfo);
                        }
                        SUCCESS.onSuccess(listsKyq,list_selected,arrayKyq,per);
                    }else {
                        SUCCESS.onSuccess(new ArrayList<KyqInfo>(),new ArrayList<String>(),null,per);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError!=null&&volleyError.getMessage()!=null) {
                    ERROR.onError(volleyError.getMessage());
                }else {
                    ERROR.onError(null);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("userIdList", finalStr);
                return params;
            }
        };
        MySingleton.getInstance(mContext).addToRequestQueue(request);
    }

}
