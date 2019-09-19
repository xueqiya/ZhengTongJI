package com.sangu.apptongji.main.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.address.KyqInfo;
import com.sangu.apptongji.main.callback.IError;
import com.sangu.apptongji.main.callback.ISuccess;
import com.sangu.apptongji.main.chatheadimage.StringUtils;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.SyAddressBookUtil;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.UserPermissionUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2017-12-28.
 */

public class TouMingActivity extends BaseActivity {
    private CustomProgressDialog mProgress;
    private List<String> kyqIds = new ArrayList<>();
    private String companyName;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        companyName = getIntent().getStringExtra("companyName");
        MySingleton.getInstance(this).getRequestQueue();
        String nowTime = getNowTime2();
        SharedPreferences mSharedPreference = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
        String tiXianTime = mSharedPreference.getString("fenXiangTime",null);
        int size = StringUtils.getBetweenDays(nowTime,tiXianTime).size();
        if (tiXianTime==null||size>7){
            LayoutInflater inflater2 = LayoutInflater.from(TouMingActivity.this);
            RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
            final Dialog dialog2 = new AlertDialog.Builder(TouMingActivity.this,R.style.Dialog).create();
            dialog2.show();
            dialog2.getWindow().setContentView(layout2);
            dialog2.setCanceledOnTouchOutside(false);
            dialog2.setCancelable(false);
            TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
            Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
            final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
            TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
            title2.setText("分享名片推广");
            btnOK2.setText("一键分享");
            btnCancel2.setText("不分享悄悄赚");
            title_tv2.setText("把这个推广方式\n分享给更多客户吧");
            btnCancel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                    finish();
                }
            });
            btnOK2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog2.dismiss();
                    PermissionUtil permissionUtil = new PermissionUtil(TouMingActivity.this);
                    permissionUtil.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                            new PermissionListener() {
                                @Override
                                public void onGranted() {
                                    //所有权限都已经授权
                                    mProgress = CustomProgressDialog.createDialog(TouMingActivity.this);
                                    mProgress.setMessage("正在分享，请稍后...");
                                    mProgress.setCancelable(false);
                                    mProgress.show();
                                    getRemainRed();
                                }
                                @Override
                                public void onDenied(List<String> deniedPermission) {
                                    //Toast第一个被拒绝的权限
                                    Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限！",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                @Override
                                public void onShouldShowRationale(List<String> deniedPermission) {
                                    //Toast第一个勾选不在提示的权限
                                    Toast.makeText(getApplicationContext(),"您拒绝了访问通讯录权限，请前往设置手动打开权限！",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                }
            });
        }else {
            finish();
        }
    }

    private void getRemainRed() {
        SyAddressBookUtil.builder()
                .loader(getApplicationContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(List<KyqInfo> listsKyq, List<String> list_selected, org.json.JSONArray arrayKyq,double per) {
                        kyqIds = list_selected;
                        inserttoSer();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(String msg) {
                        if (mProgress!=null&&mProgress.isShowing()){
                            mProgress.dismiss();
                        }
                        ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接中断，同步失败");
                        finish();
                    }
                })
                .build()
                .getKyqList();
    }

    private void inserttoSer() {
        if (kyqIds==null||kyqIds.size()==0){
            if (mProgress!=null&&mProgress.isShowing()){
                mProgress.dismiss();
            }
            Log.e("tixianac,kyid","没有可同步id");
            ToastUtils.showNOrmalToast(getApplicationContext(),"没有可同步的通讯录");
            finish();
        }else {
            Log.e("tixianac,kyid","进入同步id"+kyqIds.size());
            String list = null;
            for (int i = 0; i < kyqIds.size(); i++) {
                String num = kyqIds.get(i);
                if (num.startsWith("+86")) {
                    num = num.substring(3, num.length());
                }
                if (num.startsWith("[+86")) {
                    num = num.substring(4, num.length());
                }
                if (num.startsWith("[")) {
                    num = num.substring(1, num.length());
                }
                if (num.endsWith("]")) {
                    num = num.substring(0, num.length() - 1);
                }
                if (num.length() == 11) {
                    if (i == 0) {
                        list = num;
                    } else {
                        list = list + "," + num;
                    }
                }
            }
            if (list == null || list.length() == 0) {
                Toast.makeText(getApplicationContext(), "请同意访问通讯录权限!", Toast.LENGTH_SHORT).show();
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
                        Log.e("addreliac,s", s);
                        if ("success".equals(code)) {
                            SharedPreferences mSharedPreference = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
                            SharedPreferences.Editor meditor = mSharedPreference.edit();
                            meditor.putString("tiXianTime", getNowTime2());
                            meditor.commit();
                            duanxin(600);
                        } else {
                            if (mProgress!=null&&mProgress.isShowing()){
                                mProgress.dismiss();
                            }
                            ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接中断，同步失败");
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (mProgress!=null&&mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                    ToastUtils.showNOrmalToast(getApplicationContext(),"网络连接中断，同步失败");
                    finish();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.e("addreliac,l", finalList);
                    Map<String, String> param = new HashMap<>();
                    param.put("u_id", id);
                    param.put("inviteId", finalList);//"13513895563,17729794711"
                    return param;
                }
            };
            MySingleton.getInstance(TouMingActivity.this).addToRequestQueue(request);
        }
    }

    private void duanxin(final int size) {
        if (kyqIds==null||kyqIds.size()==0){
            if (mProgress!=null&&mProgress.isShowing()){
                mProgress.dismiss();
            }
            ToastUtils.showNOrmalToast(getApplicationContext(),"没有可同步的通讯录");
            finish();
        }else {
            String list = null;
            int inviteSize = size;
            if (kyqIds.size() <= size) {
                inviteSize = kyqIds.size();
            }
            for (int i = size - 600; i < inviteSize; i++) {
                String num = kyqIds.get(i);
                if (num.startsWith("+86")) {
                    num = num.substring(3, num.length());
                }
                if (num.startsWith("[+86")) {
                    num = num.substring(4, num.length());
                }
                if (num.startsWith("[")) {
                    num = num.substring(1, num.length());
                }
                if (num.endsWith("]")) {
                    num = num.substring(0, num.length() - 1);
                }
                if (num.length() == 11) {
                    if (i == size - 600) {
                        list = num;
                    } else {
                        list = list + "," + num;
                    }
                }
            }
            if (list == null || list.length() == 0) {
                ToastUtils.showNOrmalToast(getApplicationContext(),"没有可同步的通讯录");
                finish();
            }
            final String id = DemoHelper.getInstance().getCurrentUsernName();
            final String name = DemoApplication.getInstance().getCurrentUser().getName();
            final String url = FXConstant.URL_DUANXIN_TONGZHI;
            final String finalList = list;
            final StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (kyqIds.size() <= size) {
                        if (mProgress != null && mProgress.isShowing()) {
                            mProgress.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "同步成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        int s2 = size + 600;
                        duanxin(s2);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (mProgress != null && mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                    ToastUtils.showNOrmalToast(getApplicationContext(),"没有可同步的通讯录");
                    finish();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    if (name == null || "".equals(name)) {
                        param.put("message", "【正事多】我（"+id+"）在正事多名片推广效果："+companyName);
                    } else {
                        param.put("message", "【正事多】我"+name+"在正事多名片推广效果："+companyName);
                    }
                    param.put("telNum", finalList);
                    Log.e("tixianac,p", param.toString());
                    return param;
                }
            };
            UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "3", new UserPermissionUtil.UserPermissionListener() {
                @Override
                public void onAllow() {
                    MySingleton.getInstance(TouMingActivity.this).addToRequestQueue(request);

                }

                @Override
                public void onBan() {
                    if (mProgress != null && mProgress.isShowing()) {
                        mProgress.dismiss();
                    }
                    ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送短信");

                }
            });
        }
    }

    private String getNowTime2() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Date date = new Date();
        return format.format(date);
    }

}
