package com.sangu.apptongji.main.address;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.callback.IError;
import com.sangu.apptongji.main.callback.ISuccess;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.SyAddressBookUtil;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-15.
 */

public class AddressListActivity extends BaseActivity {
    private KyqAdapter adapterKyq=null;
    private ListView lvKyq=null;
    private TextView tv_tongbu;
    private ProgressDialog pd;
    private String redCount;
    private TextView tvCountKyq=null,tv_advice_all=null;
    private String allinCome="0";//所有红包赚的钱

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_addressfriend_list);
        WeakReference<AddressListActivity> reference =  new WeakReference<AddressListActivity>(this);
        pd = new ProgressDialog(reference.get());
        pd.setMessage("正在查询...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        lvKyq = (ListView) findViewById(R.id.lv_kyq);
        tv_tongbu = (TextView) findViewById(R.id.tv_tongbu);
        tvCountKyq = (TextView) findViewById(R.id.tv_count_kyq);
        tv_advice_all = (TextView) findViewById(R.id.tv_advice_all);
        searchFriend();
        getRedCount();

        String redAuth =  this.getIntent().getStringExtra("redAuth");

        if (redAuth==null||"".equals(redAuth)||redAuth.equalsIgnoreCase("null"))
        {


        }else
        {
            if (redAuth.equals("yes"))
            {
                //获取用户总共通过红包赚了多少钱
                tv_tongbu.setText("一键邀请");
                GetUserAllRedMoney();
            }
        }

    }

    //获取用户全部红包收入
    private void GetUserAllRedMoney(){

        String url = FXConstant.URL_MINGZI_DAN;

        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("chen", "onResponse" + s);

                try {

                    JSONObject object = new JSONObject(s);
                    String shareRedAmount = object.getString("income");

                    allinCome = shareRedAmount;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (volleyError!=null) {
                    Log.e("hongbao", volleyError.getMessage());
                    Log.d("chen", "hongbao" + volleyError.getMessage());
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();

                param.put("merId",DemoHelper.getInstance().getCurrentUsernName());
                param.put("currentPage","1");
                param.put("transactionType","红包");
                param.put("accType","收入");

                return param;
            }
        };
        MySingleton.getInstance(AddressListActivity.this).addToRequestQueue(request);

    }

    private void getRedCount() {
        String url = FXConstant.URL_SEARCH_RED;
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                redCount = s;
                if (s!=null) {
                    Log.e("addressliac",s);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        MySingleton.getInstance(AddressListActivity.this).addToRequestQueue(request);
    }

    //记录都给谁发短信了
    private void UpLoadPhoneToService(final String allPhone) {

        String url = FXConstant.URL_RECORD_PHONE;

        StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("chen", "onResponse" + s);

                try {

                    JSONObject object = new JSONObject(s);
                  //  String shareRedAmount = object.getString("income");

                  //  allinCome = shareRedAmount;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (volleyError!=null) {
                    Log.e("hongbao", volleyError.getMessage());
                    Log.d("chen", "hongbao" + volleyError.getMessage());
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();

                param.put("uId",DemoHelper.getInstance().getCurrentUsernName());
                param.put("fId",allPhone);

                return param;
            }
        };
        MySingleton.getInstance(AddressListActivity.this).addToRequestQueue(request);
    }

    //发送手机短信 顺便给手机号存到数据库
    private void SendPhoneMessage() {

        List <String> kyqIds = adapterKyq.getList_selected();
        if (kyqIds==null||kyqIds.size()==0){
            Toast.makeText(getApplicationContext(),"没有可同步的通讯录",Toast.LENGTH_SHORT).show();
            if (pd!=null&&pd.isShowing()) {
                pd.dismiss();
            }
            return;
        }
        String list = null;
        for (int i=0; i <kyqIds.size(); i++){
            String num = kyqIds.get(i);
            if (num.startsWith("+86")){
                num = num.substring(3,num.length());
            }
            if (num.startsWith("[+86")){
                num = num.substring(4,num.length());
            }
            if (num.startsWith("[")){
                num = num.substring(1,num.length());
            }
            if (num.endsWith("]")){
                num = num.substring(0,num.length()-1);
            }
            if (num.length()==11) {
                if (i==0) {
                    list = num;
                }else {
                    list = list+","+num;
                }
            }
        }
        if (list==null||list.length()==0){
            Toast.makeText(getApplicationContext(),"请同意访问通讯录权限!",Toast.LENGTH_SHORT).show();
            return;
        }
        final String id = DemoHelper.getInstance().getCurrentUsernName();

        String aaArray[] = list.split(",");
        HashSet<String> hs = new HashSet<String>();
        for(String s : aaArray){
            hs.add(s);
        }
        Iterator<String> it = hs.iterator();
        String aa_ = "";
        if(it.hasNext()){
            try {
                aa_ = hs.toString().replace("[", "").replace("]", "");//去除相同项的字符串
                aa_ = aa_.replace(", ", ",");
            } catch (Exception e) {
                e.printStackTrace();
                aa_ = hs.toString().replace("[", "").replace("]", "");//去除相同项的字符串
            }
        }
        final String finalList = aa_;

        UpLoadPhoneToService(finalList);

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + finalList));
        if (Double.parseDouble(allinCome) > 0)
        {
            sendIntent.putExtra("sms_body", "【正事多】APP里有很多人发名片红包，分享就能体现，我已经赚了"+allinCome+"元");

        }else
        {
            sendIntent.putExtra("sms_body", "【正事多】APP里有很多人发名片红包，分享就能体现");
        }

        this.startActivity(sendIntent);

    }


    private void inserttoSer() {
        if (pd!=null) {
            pd.setMessage("正在发送...");
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
        List <String> kyqIds = adapterKyq.getList_selected();
        if (kyqIds==null||kyqIds.size()==0){
            Toast.makeText(getApplicationContext(),"没有可同步的通讯录",Toast.LENGTH_SHORT).show();
            if (pd!=null&&pd.isShowing()) {
                pd.dismiss();
            }
            return;
        }
        String list = null;
        for (int i=0; i <kyqIds.size(); i++){
            String num = kyqIds.get(i);
            if (num.startsWith("+86")){
                num = num.substring(3,num.length());
            }
            if (num.startsWith("[+86")){
                num = num.substring(4,num.length());
            }
            if (num.startsWith("[")){
                num = num.substring(1,num.length());
            }
            if (num.endsWith("]")){
                num = num.substring(0,num.length()-1);
            }
            if (num.length()==11) {
                if (i==0) {
                    list = num;
                }else {
                    list = list+","+num;
                }
            }
        }
        if (list==null||list.length()==0){
            Toast.makeText(getApplicationContext(),"请同意访问通讯录权限!",Toast.LENGTH_SHORT).show();
            return;
        }
        final String id = DemoHelper.getInstance().getCurrentUsernName();
        String url = FXConstant.URL_INSERT_INVITE;
        String aaArray[] = list.split(",");
        HashSet<String> hs = new HashSet<String>();
        for(String s : aaArray){
            hs.add(s);
        }
        Iterator<String> it = hs.iterator();
        String aa_ = "";
        if(it.hasNext()){
            try {
                aa_ = hs.toString().replace("[", "").replace("]", "");//去除相同项的字符串
                aa_ = aa_.replace(", ", ",");
            } catch (Exception e) {
                e.printStackTrace();
                aa_ = hs.toString().replace("[", "").replace("]", "");//去除相同项的字符串
            }
        }
        final String finalList = aa_;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.getString("code");
                    Log.e("addreliac,s",s);
                    if ("success".equals(code)){
                        duanxin(600);
                    }else {
                        if (pd!=null&&pd.isShowing()) {
                            pd.dismiss();
                        }
                        Toast.makeText(getApplicationContext(),"操作频繁",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (pd!=null&&pd.isShowing()) {
                    pd.dismiss();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e("addreliac,l", finalList);
                Map<String,String> param = new HashMap<>();
                param.put("u_id",id);
                param.put("inviteId", finalList);//"13513895563,17729794711"
                return param;
            }
        };
        MySingleton.getInstance(AddressListActivity.this).addToRequestQueue(request);
    }

    private void duanxin(final int size) {
        if (redCount==null||"".equals(redCount)){
            redCount = "0";
        }
        final List <String> kyqInfos = adapterKyq.getList_selected();
        if (kyqInfos==null||kyqInfos.size()==0){
            Toast.makeText(getApplicationContext(),"没有可同步的通讯录",Toast.LENGTH_SHORT).show();
            if (pd!=null&&pd.isShowing()) {
                pd.dismiss();
            }
            return;
        }
        String list = null;
        int inviteSize = size;
        if (kyqInfos.size() <= size){
            inviteSize = kyqInfos.size();
        }
        for (int i=size-600; i < inviteSize; i++){
            String num = kyqInfos.get(i);
            if (num.startsWith("+86")){
                num = num.substring(3,num.length());
            }
            if (num.startsWith("[+86")){
                num = num.substring(4,num.length());
            }
            if (num.startsWith("[")){
                num = num.substring(1,num.length());
            }
            if (num.endsWith("]")){
                num = num.substring(0,num.length()-1);
            }
            if (num.length()==11) {
                if (i==size-600) {
                    list = num;
                }else {
                    list = list+","+num;
                }
            }
        }
        if (list==null||list.length()==0){
            Toast.makeText(getApplicationContext(),"请同意访问通讯录权限!",Toast.LENGTH_SHORT).show();
            return;
        }
        final String id = DemoHelper.getInstance().getCurrentUsernName();
        final String name = DemoApplication.getInstance().getCurrentUser().getName();
        final String url = FXConstant.URL_DUANXIN_TONGZHI;
        final String finalList = list;
        final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (kyqInfos.size() <= size){
                    if (pd!=null&&pd.isShowing()) {
                        pd.dismiss();
                    }
                    Toast.makeText(getApplicationContext(),"通知成功，等待对方回复！",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    int s2 = size+600;
                    duanxin(s2);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (pd!=null&&pd.isShowing()) {
                    pd.dismiss();
                }
                Toast.makeText(getApplicationContext(),"通知失败，网络错误！",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                if (name==null||"".equals(name)){
                    param.put("message","【正事多】我是"+id+"在(正事多)看到"+redCount+"人发名片红包,还支持各行业接单、派单、抢单，商务社交定位聊天，一个全能全新的APP，特别推荐");
                }else {
                    param.put("message","【正事多】我是"+name+"在(正事多)看到"+redCount+"人发名片红包,还支持各行业接单、派单、抢单，商务社交定位聊天，一个全能全新的APP，特别推荐");
                }
                param.put("telNum", finalList);
                Log.e("addreliac,p",param.toString());
                return param;
            }
        };
        UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
            @Override
            public void onAllow() {
                MySingleton.getInstance(AddressListActivity.this).addToRequestQueue(request);

            }

            @Override
            public void onBan() {
                if (pd!=null&&pd.isShowing()) {
                    pd.dismiss();
                }
                ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

            }
        });
    }

    private void searchFriend() {
        MyRunnable runnable = new MyRunnable(this);
        new Thread(runnable).start();
    }

    private static class MyRunnable implements Runnable{
        WeakReference<AddressListActivity> mactivity;
        public MyRunnable(AddressListActivity activity){
            mactivity = new WeakReference<AddressListActivity>(activity);
        }

        @Override
        public void run() {
            SyAddressBookUtil.builder()
                    .loader(mactivity.get())
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(List<KyqInfo> listsKyq,List<String> list_selected, JSONArray arrayKyq,double per) {
                            if (arrayKyq==null||arrayKyq.length() == 0||arrayKyq.length()==1) {
                                mactivity.get().tvCountKyq.setText("0位好友可邀请");
                            } else {
                                mactivity.get().tvCountKyq.setText(arrayKyq.length() + "位好友可邀请");

                                String redAuth =  mactivity.get().getIntent().getStringExtra("redAuth");

                                if (redAuth==null||"".equals(redAuth)||redAuth.equalsIgnoreCase("null"))
                                {

                                    mactivity.get().adapterKyq = new KyqAdapter(listsKyq,list_selected,mactivity.get(),"0");

                                }else {
                                    if (redAuth.equals("yes")) {

                                        List<String> list_selected1 = new ArrayList<>();
                                        mactivity.get().adapterKyq = new KyqAdapter(listsKyq,list_selected1,mactivity.get(),redAuth);

                                    }
                                }

                                mactivity.get().lvKyq.setAdapter(mactivity.get().adapterKyq);
                            }



                            mactivity.get().tv_tongbu.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                 //点击最下方同步的时候 判断是否是红包权限跳转过来的 是的话就用他自己的短信

                                    String redAuth =  mactivity.get().getIntent().getStringExtra("redAuth");

                                   LayoutInflater inflater3 = LayoutInflater.from(mactivity.get());
                                    RelativeLayout layout3 = (RelativeLayout) inflater3.inflate(R.layout.dialog_alert, null);
                                    final Dialog dialog3 = new AlertDialog.Builder(mactivity.get(),R.style.Dialog).create();
                                    dialog3.show();
                                    dialog3.getWindow().setContentView(layout3);
                                    dialog3.setCanceledOnTouchOutside(true);
                                    dialog3.setCancelable(true);
                                    TextView title_tv3 = (TextView) layout3.findViewById(R.id.title_tv);
                                    Button btnCancel3 = (Button) layout3.findViewById(R.id.btn_cancel);
                                    final Button btnOK3 = (Button) layout3.findViewById(R.id.btn_ok);
                                    btnOK3.setText("确定");
                                    btnCancel3.setText("取消");

                                    if (redAuth==null||"".equals(redAuth)||redAuth.equalsIgnoreCase("null"))
                                    {

                                        title_tv3.setText("确认同步邀请通讯录到好友列表么？");

                                    }else {
                                        if (redAuth.equals("yes")) {

                                            title_tv3.setText("点击确定,邀请好友注册");

                                        }
                                    }


                                    btnCancel3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog3.dismiss();
                                        }
                                    });
                                    btnOK3.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog3.dismiss();
                                            if (mactivity.get().adapterKyq == null) {
                                                ToastUtils.showNOrmalToast(mactivity.get().getApplicationContext(),"数据尚未处理完成...");
                                                return;
                                            }
                                            if (mactivity.get() != null) {
                                                String redAuth =  mactivity.get().getIntent().getStringExtra("redAuth");

                                                if (redAuth==null||"".equals(redAuth)||redAuth.equalsIgnoreCase("null"))
                                                {

                                                    mactivity.get().inserttoSer();

                                                }else {
                                                    if (redAuth.equals("yes")) {

                                                        //处理发短信的逻辑 手机短信

                                                        mactivity.get().SendPhoneMessage();

                                                    }
                                                }

                                            }
                                        }
                                    });
                                }
                            });
                            if (mactivity.get().pd!=null&&mactivity.get().pd.isShowing()) {
                                mactivity.get().pd.dismiss();
                            }
                        }
                    })
                    .error(new IError() {
                        @Override
                        public void onError(String msg) {
                            if (mactivity.get().pd!=null&&mactivity.get().pd.isShowing()) {
                                mactivity.get().pd.dismiss();
                            }
                        }
                    })
                    .build()
                    .getKyqList();
        }
    }

}
