package com.sangu.apptongji.main.qiye.model.impl;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.entity.QiyeKaoQinDetailInfo;
import com.sangu.apptongji.main.qiye.model.IKaoqinDetailListModel;
import com.sangu.apptongji.main.qiye.presenter.OnQiyeListener;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017-01-12.
 */

public class KaoqinDetailListModel implements IKaoqinDetailListModel {
    private Context mcontext=null;
    public KaoqinDetailListModel(Context context) {
        mcontext = context;
        RequestQueue queue = MySingleton.getInstance(mcontext.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void getKaoqqinList(String currentPage, String timeEnd, String timeStart, String timestamp, final String workState, final OnQiyeListener onQiyeListener) {
        onQiyeListener.onStart();
        final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        //final String qiyeId = "284399843651093012";
        /*
        http://192.168.1.120/company/queryComClock?
companyId=5034667474945&currentPage=1&timeEnd=20180109&timeStart=20180101&timestamp=20180109&workState=01
          companyId = 5034667474945   企业id
    currentPage = 1   分页
    timeEnd = 20180109   当天日期
    timeStart = 20180101  当月月初日期
    timestamp = 20180109 当天日期

    workState  01  上班
lateState  01  迟到
overTimeState  01加班
leaveState 01 请假
        * */
        String url = FXConstant.URL_QIYE_KAOQIN_DETAIL + qiyeId + "&currentPage=" + currentPage + "&timeEnd=" + timeEnd + "&timeStart=" + timeStart
                + "&timestamp=" + timestamp + "&" + workState;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    boolean hasMore = true;
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("comClockList");
                    if (array==null||array.length()<20){
                        hasMore = false;
                    }else {
                        hasMore = true;
                    }
                    List<QiyeKaoQinDetailInfo> info = JSONParser.parseQiyeKaoQinDetailList(array,workState);
                    onQiyeListener.onSuccess(info,hasMore);
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

    @Override
    public void getKaoqqinList(String currentPage, String timeEnd, String timeStart, final String workState, final OnQiyeListener onQiyeListener) {
        final String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
       // final String qiyeId = "284399843651093012";
        String url = FXConstant.URL_QIYE_KAOQIN_DETAIL + qiyeId + "&currentPage=" + currentPage + "&timeEnd=" + timeEnd + "&timeStart=" + timeStart
                 + "&" + workState+"&countType=01";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    boolean hasMore = true;
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("comClockList");
                    if (array==null||array.length()<20){
                        hasMore = false;
                    }else {
                        hasMore = true;
                    }
                    List<QiyeKaoQinDetailInfo> info = JSONParser.parseQiyeKaoQinDetailList(array,workState);
                    onQiyeListener.onSuccess(info,hasMore);
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
