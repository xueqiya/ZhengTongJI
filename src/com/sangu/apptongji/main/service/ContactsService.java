package com.sangu.apptongji.main.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.domain.EaseUser;
import com.fanxin.easeui.utils.EaseCommonUtils;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.db.UserDao;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lishao on 2016/7/7.\
 * QQ:84543217
 */
public class ContactsService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        getContactsInServer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void getBlacksInServer() {
        if (!DemoHelper.getInstance().isLoggedIn(this)) {
            return;
        }
        String url = FXConstant.URL_BLACKLIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
                    if (jsonArray!=null){
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            EaseUser easeUser = new EaseUser(object.getString("shield_id"));
                            userlist.put(easeUser.getUsername(), easeUser);
                        }
                        // save the contact list to database
                        UserDao dao = new UserDao(getApplicationContext());
                        List<EaseUser> users = new ArrayList<EaseUser>(userlist.values());
                        dao.saveBlackList(users);
                        DemoHelper.getInstance().getBlackList().clear();
                        DemoHelper.getInstance().getBlackList().putAll(userlist);
                    }
                    stopSelf();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("-----失败----",""+volleyError.getMessage());
                stopSelf();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("user_id", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(request);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getContactsInServer() {
        if (!DemoHelper.getInstance().isLoggedIn(this)) {
            return;
        }
        String url = FXConstant.URL_FriendList;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("uiList");
                    Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
                    if (jsonArray!=null){
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            EaseUser easeUser = new EaseUser(object.getString("uLoginId"));
                            easeUser.setNick(object.isNull("uName")?object.getString("uLoginId"):object.getString("uName"));
                            String avatar = object.isNull("uImage")?"":object.getString("uImage");
                            if (avatar.length()>40) {
                                String[] avatar1 = avatar.split("\\|");
                                avatar = avatar1[0];
                            }
                            easeUser.setAvatar(avatar);
                            easeUser.setUserInfo(object.toString());
                            EaseCommonUtils.setUserInitialLetter(easeUser);
                            userlist.put(easeUser.getUsername(), easeUser);
                        }
                        DemoHelper.getInstance().getContactList().clear();
                        DemoHelper.getInstance().getContactList().putAll(userlist);
                        // save the contact list to database
                        UserDao dao = new UserDao(getApplicationContext());
                        List<EaseUser> users = new ArrayList<EaseUser>(userlist.values());
                        dao.saveContactList(users);
                    }
                    getBlacksInServer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("-----失败----",""+volleyError.getMessage());
                getBlacksInServer();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(this.getApplicationContext()).addToRequestQueue(request);
    }
}
