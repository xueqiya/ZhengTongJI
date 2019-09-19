package com.sangu.apptongji.main.alluser.model.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.model.IProfileModel;
import com.sangu.apptongji.main.alluser.presenter.OnprofileListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/8/29.
 */

public class ProfileModel implements IProfileModel {
    private Context mcontext=null;
    public ProfileModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getCurrentUserInfo(final OnprofileListener onprofileListener) {
        onprofileListener.onStart();
        String url = FXConstant.URL_Get_UserInfo+ DemoHelper.getInstance().getCurrentUsernName()+"&areaProfession=1";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject object = jsonObject.getJSONObject("userInfo");
                    DemoApplication app = DemoApplication.getApp();
                    Userful user = JSONParser.parseUser(object);
                    app.saveCurrentUser(user);
                    String qiyeId = user.getResv5();
                    String resv6 = user.getResv6();
                    String remark = user.getuNation();
                    String comAddress = user.getCompanyAdress();
                    String companyName = user.getCompany();
                    String locationState = user.getLocationState();
                    app.setCurrentQiYeRemark(remark);
                    app.saveCurrentlocationState(locationState);
                    app.setCurrentCompanyName(companyName);
                    app.setCurrentcomAddress(comAddress);
                    app.setCurrentQiYeId(qiyeId);
                    app.setCurrentResv6(resv6);
                    SharedPreferences sp = mcontext.getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sp.edit();
                    editor1.putString("qiyeId",qiyeId);
                    editor1.commit();
                    onprofileListener.onSuccess(user);
                    onprofileListener.onFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onprofileListener.onFailed();
            }
        });
        MySingleton.getInstance(mcontext).addToRequestQueue(request);
    }
}
