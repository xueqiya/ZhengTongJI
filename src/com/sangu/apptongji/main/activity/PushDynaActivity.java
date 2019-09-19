package com.sangu.apptongji.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.PushDynaAdapter;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-12-18.
 */

public class PushDynaActivity extends BaseActivity {
    private String task_label;
    private XRecyclerView mRecyclerView;
    private CustomProgressDialog mProgress=null;
    private PushDynaAdapter adapter;
    private List<JSONObject> articles=new ArrayList<>();
    private static final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_push_dyna);
        mProgress = CustomProgressDialog.createDialog(this);
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        task_label = getIntent().getStringExtra("task_label");
        inivView();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                deletePush();
            }
        },2000);
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
                param.put("type", "13");
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void inivView() {
        mRecyclerView = (XRecyclerView) findViewById(R.id.refresh_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getPushDynaList();
            }

            @Override
            public void onLoadMore() {

            }
        });
        mRecyclerView.refresh(false);
    }

    private void getPushDynaList() {
        if (mProgress!=null) {
            mProgress.show();
        }
        String url = FXConstant.URL_SEARCH_PUSH_DYNALIST;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = JSON.parseObject(s);
                JSONArray users_temp = jsonObject.getJSONArray("clist");
                mRecyclerView.refreshComplete();
                mRecyclerView.setNoMore(true);
                if (users_temp==null||users_temp.size()==0||users_temp.toString().equals("[]")){
                    articles = new ArrayList<>();
                    adapter = new PushDynaAdapter(PushDynaActivity.this,articles);
                    mRecyclerView.setAdapter(adapter);
                }else {
                    if (articles==null) {
                        articles = new ArrayList<JSONObject>();
                    }else {
                        articles.clear();
                    }
                    for (int i = 0; i < users_temp.size(); i++) {
                        JSONObject json = users_temp.getJSONObject(i);
                        if (articles!=null&&json!=null) {
                            articles.add(json);
                        }
                    }
                    adapter = new PushDynaAdapter(PushDynaActivity.this,articles);
                    mRecyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new PushDynaAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            JSONObject object = articles.get(position-1);
                            String type;
                            if (object.getString("fromUId") != null) {
                                type = "02";
                            } else {
                                type = "01";
                            }
                            String sID = object.getString("uLoginId");
                            String dynamic_seq = object.getString("dynamicSeq");
                            String createTime = object.getString("createTime");
                            Intent intent = new Intent(PushDynaActivity.this, DynaDetaActivity.class);
                            intent.putExtra("sID", sID);
                            intent.putExtra("profession","有");
                            intent.putExtra("dynamicSeq",dynamic_seq);
                            intent.putExtra("createTime",createTime);
                            intent.putExtra("dType", "05");
                            intent.putExtra("type", type);
                            intent.putExtra("type2", "00");
                            startActivityForResult(intent,0);
                        }
                    });
                    if (mProgress!=null&&mProgress.isShowing()&&PushDynaActivity.this!=null&&!PushDynaActivity.this.isFinishing()){
                        mProgress.dismiss();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mProgress!=null&&mProgress.isShowing()){
                    mProgress.dismiss();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("task_label",task_label);
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRecyclerView.refresh(false);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    protected void onDestroy() {
        if (mProgress!=null&&mProgress.isShowing()){
            mProgress.dismiss();
        }
        super.onDestroy();
    }
}
