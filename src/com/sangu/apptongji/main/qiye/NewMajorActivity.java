package com.sangu.apptongji.main.qiye;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.adapter.QueryListAdapter;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.entity.QueryInfo;
import com.sangu.apptongji.main.qiye.presenter.IMajQPresenter;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.MajQPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQMajView;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-30.
 */

public class NewMajorActivity extends BaseActivity implements IQiYeDetailView,IQMajView{
    private QueryListAdapter adapter=null;
    private IMajQPresenter majQPresenter = null;
    private List<QueryInfo> list = new ArrayList<>();
    private String comAddress=null,companyName=null;
    private ListView lv_new_major=null;
    private IQiYeInfoPresenter presenter;
    String recruitImages=null,joinImages=null,recruitBody,joinBody,joinMoney=null,recruitMoney=null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (list!=null){
            list.clear();
            list=null;
        }
    }
    private Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    try {
                        String name,avatar,loginId,resv5,sex,userResv5,shareRed,friendsNumber;
                        String queryInfo = (String) msg.obj;
                        Log.e("newmajorac,qu",queryInfo);
                        JSONObject object = new JSONObject(queryInfo);
                        JSONArray queryList = object.getJSONArray("friendRequestList");
                        if (queryList!=null){
                            for (int i=0;i<queryList.length();i++){
                                JSONObject object1 = queryList.getJSONObject(i);
                                JSONObject userInfo = object1.getJSONObject("userInfo");
                                QueryInfo info = new QueryInfo();
                                name = userInfo.isNull("uName")?"":userInfo.getString("uName");
                                sex = userInfo.isNull("uSex")?"01":userInfo.getString("uSex");
                                avatar = userInfo.isNull("uImage")?"":userInfo.getString("uImage");
                                loginId = userInfo.getString("uLoginId");
                                resv5 = object1.isNull("remark")?"":object1.getString("remark");
                                userResv5 = userInfo.isNull("resv5")?"":userInfo.getString("resv5");
                                shareRed = userInfo.isNull("shareRed")?"":userInfo.getString("shareRed");
                                friendsNumber = userInfo.isNull("friendsNumber")?"":userInfo.getString("friendsNumber");
                                info.setFriendsNumber(friendsNumber);
                                info.setShareRed(shareRed);
                                info.setName(name);
                                info.setSex(sex);
                                info.setAvatar(avatar);
                                info.setuLoginId(loginId);
                                info.setResv5(resv5);
                                info.setUserResv5(userResv5);
                                info.setTime(object1.isNull("requestTime")?"":object1.getString("requestTime"));
                                list.add(info);
                            }
                        }
                        adapter = new QueryListAdapter(list,NewMajorActivity.this,companyName,comAddress,recruitImages,recruitBody,recruitMoney,joinImages,joinBody,joinMoney);
                        lv_new_major.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_new_major);
        presenter = new QiYeInfoPresenter(this,this);
        majQPresenter = new MajQPresenter(this,this);
        companyName = this.getIntent().getStringExtra("companyName");
        comAddress = this.getIntent().getStringExtra("comAddress");
        lv_new_major = (ListView) this.findViewById(R.id.lv_new_yuangong);
        String qiyeId = DemoApplication.getInstance().getCurrentQiYeId();
        presenter.loadQiYeInfo(qiyeId);
        deletePush();
    }

    private void deletePush() {
        String url = FXConstant.URL_DELETE_PUSH;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                param.put("type","03");
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    public void back(View view) {
        setResult(RESULT_OK);
        super.back(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (list.size()>0){
            list.clear();
        }
        majQPresenter.loadMajQList();
    }

    @Override
    public void updateQiyeInfo(QiYeInfo user2) throws UnsupportedEncodingException {
        joinMoney = TextUtils.isEmpty(user2.getJoinMoney())?"":user2.getJoinMoney();
        joinImages = TextUtils.isEmpty(user2.getJoinImages())?"":user2.getJoinImages();
        joinBody = TextUtils.isEmpty(user2.getJoinBody())?"":user2.getJoinBody();
        recruitMoney = TextUtils.isEmpty(user2.getRecruitMoney())?"":user2.getRecruitMoney();
        recruitImages = TextUtils.isEmpty(user2.getRecruitImages())?"":user2.getRecruitImages();
        recruitBody = TextUtils.isEmpty(user2.getRecruitBody())?"":user2.getRecruitBody();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void updateMajQList(Object obj) {
        Message msg = new Message();
        msg.what = 0;
        msg.obj = obj ;
        myhandler.sendMessage(msg);
    }
}
