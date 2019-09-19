package com.sangu.apptongji.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.PaimgAdapter;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.presenter.IPaimgPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PaimgPresenter;
import com.sangu.apptongji.main.alluser.view.IPaimgView;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-09-30.
 */

public class PaiMgListActivity extends BaseActivity implements IPaimgView{
    private IPaimgPresenter paimgPresenter;
    private String qiyeId,com_id;
    private PaimgAdapter adapter;
    private List<UserAll> list = new ArrayList<>();
    private XRecyclerView mRecyclerView=null;
    private CustomProgressDialog mProgress=null;
    private int currentPage=0;
    boolean isNext = false;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_paimg_list);
        qiyeId = getIntent().getStringExtra("qiyeId");
        com_id = getIntent().getStringExtra("com_id");
        paimgPresenter = new PaimgPresenter(this,this);
        mRecyclerView = (XRecyclerView) findViewById(R.id.refresh_list);
        mProgress = CustomProgressDialog.createDialog(this);
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
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
                isNext = false;
                currentPage=1;
                paimgPresenter.loadUserList(qiyeId,com_id,"1");
            }

            @Override
            public void onLoadMore() {
                isNext = true;
                currentPage++;
                paimgPresenter.loadUserList(qiyeId,com_id,currentPage+"");
            }
        });
        mRecyclerView.refresh(false);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void updateUserList(List<UserAll> users, boolean hasMore) {
        String lat = DemoApplication.getInstance().getCurrentLat();
        String lng = DemoApplication.getInstance().getCurrentLng();
        if (isNext){
            list.addAll(users);
            mRecyclerView.loadMoreComplete();
            adapter.notifyDataSetChanged();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        }else {
            this.list = users;
            adapter = new PaimgAdapter(list, PaiMgListActivity.this,lat,lng);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.refreshComplete();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        }
        adapter.setOnItemClickListener(new PaimgAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String id = list.get(position-1).getuLoginId();
                startActivity(new Intent(PaiMgListActivity.this,UserDetailsActivity.class).putExtra(FXConstant.JSON_KEY_HXID,id));
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String mag) {

    }
}
