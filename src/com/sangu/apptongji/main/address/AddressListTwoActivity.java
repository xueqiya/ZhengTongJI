package com.sangu.apptongji.main.address;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-15.
 */

public class AddressListTwoActivity extends BaseActivity {
    private List<PhoneInfo> lists=null;
    private SendAdapter adapterSend=null;
    private ListView lvKyq=null;
    private TextView tv_tongbu,tv_title;
    private TextView tvCountKyq=null;
    private EditText searchbox;
    private String orderId,hasId1,hasId2,hasId3,hasId4,orderBody,biaoshi,title,timestamp;
    private String flg,upId,orderProject,orderNumber,orderAmt,orderSum,conventionTime,time4;
    private ArrayList<String> imagePaths1=new ArrayList<>();
    private ArrayList<String> imagePaths2=new ArrayList<>();
    private CustomProgressDialog mProgress=null;

    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lists!=null){
            lists.clear();
            lists=null;
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_addressfriend_list);
        WeakReference<AddressListTwoActivity> reference =  new WeakReference<AddressListTwoActivity>(this);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        biaoshi = getIntent().getStringExtra("biaoshi");
        imagePaths1 = getIntent().getStringArrayListExtra("imagePaths1");
        imagePaths2 = getIntent().getStringArrayListExtra("imagePaths2");
        flg = getIntent().getStringExtra("flg");
        upId = getIntent().getStringExtra("upId");
        orderProject = getIntent().getStringExtra("orderProject");
        orderNumber = getIntent().getStringExtra("orderNumber");
        orderAmt = getIntent().getStringExtra("orderAmt");
        orderSum = getIntent().getStringExtra("orderSum");
        conventionTime = getIntent().getStringExtra("conventionTime");
        time4 = getIntent().getStringExtra("time4");
        timestamp = getIntent().getStringExtra("timestamp");
        title = getIntent().getStringExtra("title");
        orderBody = getIntent().getStringExtra("orderBody");
        orderId = getIntent().getStringExtra("orderId");
        hasId1 = getIntent().getStringExtra("hasId1");
        hasId2 = getIntent().getStringExtra("hasId2");
        hasId3 = getIntent().getStringExtra("hasId3");
        hasId4 = getIntent().getStringExtra("hasId4");
        searchbox = (EditText) findViewById(R.id.searchbox);
        lvKyq = (ListView) findViewById(R.id.lv_kyq);
        tv_tongbu = (TextView) findViewById(R.id.tv_tongbu);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tvCountKyq = (TextView) findViewById(R.id.tv_count_kyq);
        tv_title.setText("手机通讯录");
        searchbox.setVisibility(View.VISIBLE);
        tv_tongbu.setVisibility(View.GONE);
        tvCountKyq.setVisibility(View.GONE);
        lists = GetPhoneNumberFromMobile.getPhoneNumberFromMobile(reference.get());
        adapterSend = new SendAdapter(lists,this);
        lvKyq.setAdapter(adapterSend);
        adapterSend.notifyDataSetChanged();
        lvKyq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhoneInfo phoneInfo = (PhoneInfo) parent.getAdapter().getItem(position);
                final String userId = phoneInfo.getNumber().trim();
                if ("03".equals(biaoshi)) {
                    if (userId.equals(hasId1) || userId.equals(hasId2) || userId.equals(hasId3) || userId.equals(hasId4)) {
                        LayoutInflater inflaterDl = LayoutInflater.from(AddressListTwoActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                        final Dialog dialog2 = new AlertDialog.Builder(AddressListTwoActivity.this,R.style.Dialog).create();
                        dialog2.show();
                        dialog2.getWindow().setContentView(layout);
                        dialog2.setCanceledOnTouchOutside(false);
                        dialog2.setCancelable(false);
                        TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                        Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                        tv_title.setText("该账号已在本个订单中,无需再次发送！");
                        btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
                        return;
                    }
                }
                LayoutInflater inflater1 = LayoutInflater.from(AddressListTwoActivity.this);
                RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                final Dialog dialog1 = new AlertDialog.Builder(AddressListTwoActivity.this,R.style.Dialog).create();
                dialog1.show();
                dialog1.getWindow().setContentView(layout1);
                dialog1.setCanceledOnTouchOutside(true);
                dialog1.setCancelable(true);
                TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                title.setText("温馨提示");
                btnOK1.setText("确定");
                btnCancel1.setText("取消");
                title_tv1.setText("确定发送给该用户吗？"+userId);
                btnCancel1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                btnOK1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                        queryUserInfo(userId);
                    }
                });
            }
        });
        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterSend.getFilter().filter(s); // 当数据改变时，调用过滤器;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void queryUserInfo(final String userId) {
        String url = FXConstant.URL_Get_UserInfo+userId;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("用户名为空".equals(code)){
                    if ("03".equals(biaoshi)) {
                        sendPushMessage2(userId,1);
                        SendMessage(userId, 0, "");
                        SendMessage1(userId);
                    }else if ("04".equals(biaoshi)){
                        sendPushMessage2(userId,2);
                        SendMessage3(userId);
                    }else if ("05".equals(biaoshi)){
                        insertOrder2(userId,"00");
                    }
                }else {
                    if ("03".equals(biaoshi)) {
                        sendPushMessage2(userId,1);
                        com.alibaba.fastjson.JSONObject userInfo = object.getJSONObject("userInfo");
                        String name = userInfo.getString("uName");
                        if (name == null || "".equals(name)) {
                            name = "";
                        } else {
                            name = name + ":";
                        }
                        SendMessage(userId, 1, name);
                    }else if ("04".equals(biaoshi)){
                        sendPushMessage2(userId,2);
                        senddztoUser(userId);
                    }else if ("05".equals(biaoshi)){
                        insertOrder2(userId,"01");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);
    }
    private void sendPushMessage2(final String userId,final int type) {
        final String myId = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_SENDPUSHMSG;
        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
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
                param.put("u_id",userId);
                if (type==0) {
                    param.put("body","订单消息");
                    param.put("type","001");
                }else if (type==1){
                    param.put("body","订单消息");
                    param.put("type","12");
                }else {
                    param.put("body","电子单据消息");
                    param.put("type","11");
                }
                param.put("userId",myId);
                param.put("companyId","0");
                param.put("companyName","0");
                param.put("companyAdress","0");
                param.put("dynamicSeq","0");
                param.put("createTime","0");
                param.put("fristId","0");
                param.put("dType","0");
                return param;
            }
        };
        MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);
    }
    private void insertOrder2(final String userId,final String biaoshi){
        String url = FXConstant.URL_INSERT_ORDER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    String code = obj.getString("code");
                    JSONObject object = obj.getJSONObject("orderInfo");
                    orderId = object.getString("orderId");
                    if (code.equals("数据更新成功")) {
                        bianji2(userId,biaoshi);
                    } else {
                        Toast.makeText(AddressListTwoActivity.this, "下单失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("onResponse,s",volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userId);
                params.put("merId", DemoHelper.getInstance().getCurrentUsernName());
                params.put("orderBody", orderBody);
                params.put("flg", flg);
                params.put("type", "01");
                params.put("upId", upId);
                return params;
            }
        };
        MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);
    }

    private void bianji2(final String userId, final String biaoshi){
        if ("04".equals(flg)){
            String path1 = null,path2 = null,path3 = null,path4 = null,path5 = null,path6 = null,path7 = null,path8 = null;
            if (imagePaths1!=null&&imagePaths1.size()>0){
                path1 = imagePaths1.get(0);
            }
            if (imagePaths1!=null&&imagePaths1.size()>1){
                path2 = imagePaths1.get(1);
            }
            if (imagePaths1!=null&&imagePaths1.size()>2){
                path3 = imagePaths1.get(2);
            }
            if (imagePaths1!=null&&imagePaths1.size()>3){
                path4 = imagePaths1.get(3);
            }
            if (imagePaths2!=null&&imagePaths2.size() > 0) {
                path5 = imagePaths2.get(0);
            }
            if (imagePaths2!=null&&imagePaths2.size() > 1) {
                path6 = imagePaths2.get(1);
            }
            if (imagePaths2!=null&&imagePaths2.size() > 2) {
                path7 = imagePaths2.get(2);
            }
            if (imagePaths2!=null&&imagePaths2.size() > 3) {
                path8 = imagePaths2.get(3);
            }
            List<Param> param=new ArrayList<>();
            if (conventionTime!=null&&!"".equals(conventionTime)) {
                param.add(new Param("conventionTime",conventionTime));
            }
            param.add(new Param("state", "02"));
            param.add(new Param("orderId",orderId));
            param.add(new Param("userId", userId));
            if (orderProject!=null) {
                param.add(new Param("orderProject", orderProject));
            }
            if (orderNumber!=null) {
                param.add(new Param("orderNumber", orderNumber));
            }
            if (orderSum!=null) {
                param.add(new Param("orderSum", orderSum));
            }
            String url = FXConstant.URL_INSERT_OrderDetail;
            OkHttpManager.getInstance().posts2(param, null, new ArrayList<String>(), null, new ArrayList<String>(),"image01", path1, "image02", path2, "image03", path3, "image04", path4
                    , "image05", path5, "image06", path6, "image07", path7, "image08", path8, url, new OkHttpManager.HttpCallBack() {
                        @Override
                        public void onResponse(com.alibaba.fastjson.JSONObject jsonObject) {
                            String code = jsonObject.getString("code");
                            if (code.equals("数据更新成功")) {
                                sendPushMessage2(userId,0);
                                updateUscount(userId,0);
                                if ("00".equals(biaoshi)) {
                                    SendMessage5(userId);
                                } else {
                                    SendMessage4(userId);
                                }
                            } else {
                                Toast.makeText(AddressListTwoActivity.this, "编辑错误！", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(String errorMsg) {
                            Toast.makeText(AddressListTwoActivity.this,"网络连接错误,派单失败",Toast.LENGTH_SHORT).show();
                            if (errorMsg!=null) {
                                Log.e("offlineorder,e", errorMsg);
                            }
                        }
                    });
        }else {
            String url = FXConstant.URL_INSERT_OrderDetail;
            StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String code = object.getString("code");
                        if (code.equals("数据更新成功")) {
                            updateUscount(userId,0);
                            if ("00".equals(biaoshi)) {
                                SendMessage5(userId);
                            } else {
                                SendMessage4(userId);
                            }
                        } else {
                            Toast.makeText(AddressListTwoActivity.this, "网络错误，请重新编辑！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(AddressListTwoActivity.this, "网络错误，请重新编辑！", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params1 = new HashMap<>();
                    params1.put("orderId", orderId);
                    params1.put("userId", userId);
                    params1.put("state", "02");
                    if ("02".equals(flg)) {
                        params1.put("time4", time4);
                    }
                    params1.put("orderProject", orderProject);
                    params1.put("orderNumber", orderNumber);
                    if (!"05".equals(flg)) {
                        params1.put("orderAmt", orderAmt);
                    }
                    params1.put("orderSum", orderSum);
                    if (conventionTime != null && !"".equals(conventionTime)) {
                        params1.put("conventionTime", conventionTime);
                    }
                    return params1;
                }
            };
            MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request3);
        }
    }

    private void updateUscount(final String hxid1,final int type) {
        String url = FXConstant.URL_UPDATE_UNREADUSER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("addfriend,s",s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("addfriend,e",volleyError.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                if (type==0) {
                    param.put("orderUnReadCount", "1");
                }else if (type==1){
                    param.put("billCount", "1");
                }else if (type==2){
                    param.put("thirdPartyCount", "1");
                }
                param.put("userId",hxid1);
                return param;
            }
        };
        MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);
    }

    private void SendMessage5(final String userId) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】您有一个（"+orderBody+")的订单，需要验资，请在“正事多”手机端操作完成后，即可提供相应服务");
                param.put("telNum", userId);
                Log.e("utorderdeac,sm1",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void SendMessage4(final String userId) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】您有一个（"+orderBody+")的订单，需要验资，请在“正事多”手机端操作完成后，即可提供相应服务");
                param.put("telNum", userId);
                Log.e("utorderdeac,sm",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void SendMessage3(final String userId) {
        final String str;
        if (title!=null&&title.length()>6){
            str = title.substring(0,6)+"...";
        }else {
            str = title;
        }
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                senddztoUser(userId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "发送失败，网络错误！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】单据"+str+",需要您签字,请注册手机端“正事多”在电子单据中查看");
                param.put("telNum", userId);
                Log.e("utorderdeac,sm1",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void senddztoUser(final String username) {
        String url = FXConstant.URL_FASONG_DZ_DANJU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateUscount(username,1);
                Log.e("friendac,s",s);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){
                    Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"发送失败！",Toast.LENGTH_SHORT).show();
                Log.e("friendac",volleyError.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("timestamp",timestamp);
                param.put("u_id",username);
                return param;
            }
        };
        MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);
    }

    private void SendMessage1(final String userId) {
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                sendToUser(userId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), "发送失败，网络错误！", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】订单("+orderBody+"),需要您验收签字,请注册手机端“正事多”在电子单据中查看");
                param.put("telNum", userId);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void SendMessage(final String userId, final int type,final String name) {
        String lists;
        if (type==1){
            lists = userId+","+hasId3;
        }else {
            lists = hasId3;
        }
        if (hasId1!=null&&!"".equals(hasId1)){
            lists = lists+","+hasId1;
        }
        String url = FXConstant.URL_DUANXIN_TONGZHI;
        final String finalLists = lists;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (type==1) {
                    sendToUser(userId);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (type==1) {
                    sendToUser(userId);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("message", "【正事多】订单(" + orderBody + "),需要("+name+userId+")的用户签字验收");
                param.put("telNum", finalLists);
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {

                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void sendToUser(final String username) {
        String url = FXConstant.URL_FASONG_DZDANJU;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                updateUscount(username,2);
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
                String code = object.getString("code");
                if ("success".equals(code)){
                    Toast.makeText(getApplicationContext(),"发送成功！",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"网络连接中断,发送失败！",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("order_id",orderId);
                param.put("send_id",DemoHelper.getInstance().getCurrentUsernName());
                param.put("u_id",username);
                return param;
            }
        };
        MySingleton.getInstance(AddressListTwoActivity.this).addToRequestQueue(request);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}
