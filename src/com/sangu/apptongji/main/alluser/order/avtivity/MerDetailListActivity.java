package com.sangu.apptongji.main.alluser.order.avtivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.MerDetailListAdapter;
import com.sangu.apptongji.main.alluser.entity.MerDetail;
import com.sangu.apptongji.main.alluser.presenter.IMerDetailPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.MerDetailPresenter;
import com.sangu.apptongji.main.alluser.view.IMerDetailView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import org.json.JSONArray;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-04-27.
 */

public class MerDetailListActivity extends BaseActivity implements IMerDetailView{
    private IMerDetailPresenter presenter=null;
    private XRecyclerView mRecyclerView=null;
    private List<MerDetail> details=null;
    MerDetailListAdapter adapter = null;
    private TextView tv_title = null;
    private TextView tv_back = null;
    private TextView tv_none = null;
    private CustomProgressDialog mProgress=null;
    private boolean isNext = false;
    private int currentPage = 1;
    private String biaoshi,detailId;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_merdetail_list);
        WeakReference<MerDetailListActivity> reference =  new WeakReference<MerDetailListActivity>(MerDetailListActivity.this);
        presenter = new MerDetailPresenter(this,reference.get());
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        biaoshi = getIntent().getStringExtra("biaoshi");
        mRecyclerView = (XRecyclerView) findViewById(R.id.list);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_none = (TextView) findViewById(R.id.tv_none);
        if ("001".equals(biaoshi)){
            detailId = DemoApplication.getInstance().getCurrentQiYeId();
        }else {
            detailId = DemoHelper.getInstance().getCurrentUsernName();
        }
        initlayout();
        tv_back.setText("个人详情");
        tv_title.setText("明细");
        details = new ArrayList<>();
        mRecyclerView.refresh(false);

        deletePush();

    }

    private void initlayout() {
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
                isNext = false;
                presenter.loadMerDetail(currentPage+"",detailId,null,null,null,null);
            }

            @Override
            public void onLoadMore() {
                currentPage = currentPage + 1;
                isNext = true;
                presenter.loadMerDetail(currentPage+"",detailId, null, null,null,null);
            }
        });
        mRecyclerView.refresh(false);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void updateUserList(final List<MerDetail> merDetails, JSONArray array,String size,String income,String expenditure, boolean hasMore) {
        if (isNext) {
            details.addAll(merDetails);
            mRecyclerView.loadMoreComplete();
            adapter.notifyDataSetChanged();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        } else {
            this.details = merDetails;
            adapter = new MerDetailListAdapter(details, MerDetailListActivity.this);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.refreshComplete();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
            adapter.setOnItemClickListener(new MerDetailListAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    MerDetail merDetail = details.get(position-1);
                    startActivity(new Intent(MerDetailListActivity.this, MerDetailActivity.class).putExtra("merDetail", merDetail));
                }
            });
            if (merDetails == null || merDetails.size() == 0) {
                if (currentPage==1) {
                    tv_none.setVisibility(View.VISIBLE);
                }
            } else {
                tv_none.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void showLoading() {
        if (mProgress!=null&&!mProgress.isShowing()) {
            mProgress.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mProgress!=null&&mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    @Override
    public void showError() {
        if (mProgress!=null&&mProgress.isShowing()) {
            mProgress.dismiss();
        }
        Toast.makeText(MerDetailListActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
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
                param.put("type","22");
                return param;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

}
