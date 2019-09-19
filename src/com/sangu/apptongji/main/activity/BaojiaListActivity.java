package com.sangu.apptongji.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

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

public class BaojiaListActivity extends BaseActivity {
    private TextView tvtitle=null,tv_create=null;
    private RadioButton rb_baojia_to=null,rb_baojia_from=null;
    private XRecyclerView mRecyclerView;
    private CustomProgressDialog mProgress=null;
    private PushDynaAdapter adapter;
    private List<JSONObject> articles=new ArrayList<>();
    private int currentPage = 1;
    private int type=0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_baojia_list);
        mProgress = CustomProgressDialog.createDialog(this);
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        initView();
        setListener();
    }

    private void setListener() {
        rb_baojia_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_baojia_to.setChecked(true);
                rb_baojia_from.setChecked(false);
                type = 0;
                mRecyclerView.refresh(false);
            }
        });
        rb_baojia_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb_baojia_from.setChecked(true);
                rb_baojia_to.setChecked(false);
                type = 1;
                mRecyclerView.refresh(false);
            }
        });
    }

    private void initView() {
        rb_baojia_to = (RadioButton) findViewById(R.id.rb_baojia_to);
        rb_baojia_from = (RadioButton) findViewById(R.id.rb_baojia_from);
        tvtitle = (TextView) findViewById(R.id.tvtitle);
        tv_create = (TextView) findViewById(R.id.tv_create);
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
                currentPage = 1;
                getBaojiaList();
            }

            @Override
            public void onLoadMore() {
                currentPage++;
                getBaojiaList();
            }
        });
        mRecyclerView.refresh(false);
    }

    private void getBaojiaList() {
        if (mProgress!=null) {
            mProgress.show();
        }
        String url = null;
        if (type==0) {
            url = FXConstant.URL_SEARCH_BAOJIA_TO;
        }else {
            url = FXConstant.URL_SEARCH_BAOJIA_FROM;
        }
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = JSON.parseObject(s);
                JSONArray users_temp = jsonObject.getJSONArray("clist");
                mRecyclerView.refreshComplete();
                if (users_temp==null||users_temp.size()!=20) {
                    mRecyclerView.setNoMore(true);
                }
                if (users_temp==null||users_temp.size()==0||users_temp.toString().equals("[]")){
                    articles = new ArrayList<>();
                    adapter = new PushDynaAdapter(BaojiaListActivity.this,articles);
                    mRecyclerView.setAdapter(adapter);
                }else {
                    if (articles==null) {
                        articles = new ArrayList<JSONObject>();
                    }else {
                        if (currentPage==1) {
                            articles.clear();
                        }
                    }
                    for (int i = 0; i < users_temp.size(); i++) {
                        JSONObject json = users_temp.getJSONObject(i);
                        if (articles!=null&&json!=null) {
                            articles.add(json);
                        }
                    }
                    adapter = new PushDynaAdapter(BaojiaListActivity.this,articles);
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
                            Intent intent = new Intent(BaojiaListActivity.this, DynaDetaActivity.class);
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
                }
                if (mProgress!=null&&mProgress.isShowing()){
                    mProgress.dismiss();
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
                if (type==0){
                    param.put("u_id", DemoHelper.getInstance().getCurrentUsernName());
                    param.put("currentPage",currentPage+"");
                }else {
                    param.put("userId", DemoHelper.getInstance().getCurrentUsernName());
                    param.put("currentPage",currentPage+"");
                }
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            mRecyclerView.refresh(false);
        }
    }
}
