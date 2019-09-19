package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.order.adapter.XuqiuListAdapter;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-16.
 */

public class XuqiuListActivity extends BaseActivity {
    private TextView tv_title;
    private TextView tv_back;
    private ListView list;
    private XuqiuListAdapter adapter;
    List<JSONObject> applys=null;
    private String dynamicId,createTime;
    private CustomProgressDialog mProgress=null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_friend_zhuanzhang);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_back = (TextView) findViewById(R.id.tv_back);
        list = (ListView) findViewById(R.id.list);
        applys = new ArrayList<JSONObject>();
        WeakReference<XuqiuListActivity> reference =  new WeakReference<XuqiuListActivity>(XuqiuListActivity.this);
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载请求列表...");
        mProgress.show();
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        dynamicId = this.getIntent().getStringExtra("dynamicId");
        createTime = this.getIntent().getStringExtra("createTime");
        tv_back.setText("我的动态");
        tv_title.setText("接单申请列表");
        adapter = new XuqiuListAdapter(this,applys,createTime,dynamicId);
        list.setAdapter(adapter);
        queryApplyList();
    }

    private void queryApplyList() {
        String url = FXConstant.URL_QUERY_APPLYORDER;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    if (mProgress!=null){
                        if (mProgress.isShowing()){
                            mProgress.dismiss();
                        }
                    }
                    JSONObject json = new JSONObject(s);
                    JSONArray array = json.getJSONArray("list");
                    if (array==null||"".equals(array)||"NULL".equals(array)||array.length()==0){
                        Toast.makeText(getApplicationContext(),"暂时无人请求接单",Toast.LENGTH_SHORT).show();
                    }else {
                        for (int i=0; i<array.length();i++) {
                            JSONObject users = array.getJSONObject(i);
                            applys.add(users);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgress!=null){
                    if (mProgress.isShowing()){
                        mProgress.dismiss();
                    }
                }
                Toast.makeText(XuqiuListActivity.this,"网络连接错误...",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("dynamicId",dynamicId);
                return param;
            }
        };
        MySingleton.getInstance(XuqiuListActivity.this).addToRequestQueue(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new AlertDialog.Builder(XuqiuListActivity.this)
                .setTitle("温馨提示")
                .setMessage("1、点击头像或者点击同意按钮进入对方详情对应专业进行下单即可进行交易\n 2、选择其中一个用户进行交易后即会生成需求订单,再次选择将会产生新的订单(建议用户根据需求选择)")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}
