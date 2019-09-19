package com.sangu.apptongji.main.qiye;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.qiye.adapter.KaoqinDetailAdapter;
import com.sangu.apptongji.main.qiye.entity.QiyeKaoQinDetailInfo;
import com.sangu.apptongji.main.qiye.presenter.IKaoqinDetailListPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.KaoqinDetailListPresenter;
import com.sangu.apptongji.main.qiye.view.IKaoqinDetailListView;
import com.sangu.apptongji.ui.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018-01-09.
 */

public class KaoQinDetaliActivity extends BaseActivity implements IKaoqinDetailListView, View.OnClickListener {

    private ProgressDialog pd;
    private IKaoqinDetailListPresenter kaoqinListPresenter;
    private XRecyclerView mRecyclerView;
    private RelativeLayout rl_paihang;
    private TextView tv_people_count,tv_working,tv_work_overtime,tv_late,tv_leave,textView20;
    private ImageView iv_avatar;
    private String currentType;
    private int currentPage = 1;
    private boolean isNext;
    boolean hasMore = true;
    private List<QiyeKaoQinDetailInfo> list;
    private KaoqinDetailAdapter adapter;
    private String cType = "xianqing";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_kaoqin_detail);
        cType = getIntent().getStringExtra("cType");
        if (cType == null) {
            cType = "xianqing";
        }
        currentType = getIntent().getStringExtra("type");
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("正在加载数据...");
        kaoqinListPresenter = new KaoqinDetailListPresenter(this,this);
        if (cType.equalsIgnoreCase("paihang")) {
            kaoqinListPresenter.loadKaoqinPaihangDetailList(String.valueOf(currentPage), getStartTime(), getCurrentTime(), currentType);
        } else {
            kaoqinListPresenter.loadKaoqinDetailList(String.valueOf(currentPage),getCurrentTime(),getStartTime(),getCurrentTime(),currentType);
        }
        tv_people_count = (TextView) findViewById(R.id.tv_people_count);
        tv_working = (TextView) findViewById(R.id.tv_working);
        tv_work_overtime = (TextView) findViewById(R.id.tv_work_over);
        tv_late = (TextView) findViewById(R.id.tv_late);
        tv_leave = (TextView) findViewById(R.id.tv_leave);
        textView20 = (TextView) findViewById(R.id.textView20);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        rl_paihang = (RelativeLayout) findViewById(R.id.rl_paihang);
        mRecyclerView = (XRecyclerView) findViewById(R.id.rv_kaoqin_detail);

        tv_working.setText(getIntent().getStringExtra("tv_working"));
        tv_work_overtime.setText(getIntent().getStringExtra("tv_work_overtime"));
        tv_late.setText(getIntent().getStringExtra("tv_late"));
        tv_leave.setText(getIntent().getStringExtra("tv_leave"));
        ImageLoader.getInstance().displayImage(getIntent().getStringExtra("iv_avatar"),iv_avatar, DemoApplication.mOptions);
        tv_people_count.setText(getIntent().getStringExtra("memberNum") + "人");

        tv_people_count.setOnClickListener(this);
        tv_working.setOnClickListener(this);
        tv_late.setOnClickListener(this);
        tv_leave.setOnClickListener(this);
        tv_work_overtime.setOnClickListener(this);

        switchTypeEvent();
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
                kaoqinListPresenter.loadKaoqinDetailList(String.valueOf(currentPage),getCurrentTime(),getStartTime(),getCurrentTime(),currentType);

            }

            @Override
            public void onLoadMore() {
                currentPage = currentPage + 1;
                isNext = true;
                kaoqinListPresenter.loadKaoqinDetailList(String.valueOf(currentPage),getCurrentTime(),getStartTime(),getCurrentTime(),currentType);

            }
        });
        mRecyclerView.refresh(false);
        adapter = new KaoqinDetailAdapter(this,list, cType);
        mRecyclerView.setAdapter(adapter);
    }

    private void switchTypeEvent() {
        if (cType.equalsIgnoreCase("paihang")) {
            tv_working.setText("上班时间排行");
            tv_late.setText("迟到请假排行");
            tv_work_overtime.setText("业绩销量排行");
            tv_leave.setText("流量引入排行");
            tv_people_count.setVisibility(View.INVISIBLE);
            rl_paihang.setVisibility(View.VISIBLE);
            textView20.setText("考勤排行");
            if (currentType.equalsIgnoreCase("workTime=01")) {
                tv_working.setTextColor(Color.RED);
            } else if (currentType.equalsIgnoreCase("lateTime=01")) {
                tv_late.setTextColor(Color.RED);
            } else if (currentType.equalsIgnoreCase("dayTransAmount=01")) {
                tv_work_overtime.setTextColor(Color.RED);
            } else if (currentType.equalsIgnoreCase("visitorTime=01")) {
                tv_leave.setTextColor(Color.RED);
            }
        } else {
            if (currentType.equalsIgnoreCase("workState=01")) {
                tv_working.setTextColor(Color.RED);
            } else if (currentType.equalsIgnoreCase("lateState=01")) {
                tv_late.setTextColor(Color.RED);
            } else if (currentType.equalsIgnoreCase("overTimeState=01")) {
                tv_work_overtime.setTextColor(Color.RED);
            } else if (currentType.equalsIgnoreCase("leaveState=01")) {
                tv_leave.setTextColor(Color.RED);
            }
        }

    }

    @Override
    public void updateKaoqinList(List<QiyeKaoQinDetailInfo> users,boolean hasMore) {
        this.hasMore = hasMore;
        if (list==null){
            list = new ArrayList<>();
        }
        if (isNext) {
            list.addAll(users);
            mRecyclerView.loadMoreComplete();
            adapter.notifyDataSetChanged();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        } else {
            this.list = users;
            adapter = new KaoqinDetailAdapter(this,list,cType);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.refreshComplete();
            if (!hasMore){
                mRecyclerView.setNoMore(true);
            }
        }
        if (pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }

    public String getStartTime() {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        return dateFormater.format(cal.getTime()) + "";
    }
    public String getCurrentTime() {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());

        return dateFormater.format(date);
    }

    @Override
    public void showLoading() {
        if (pd!=null&&!pd.isShowing()) {
            pd.show();
        }
    }

    @Override
    public void hideLoading() {
        if (pd!=null&&pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void showError() {
        if (mRecyclerView!=null){
            if (currentPage==1){
                mRecyclerView.refreshComplete();
            }else {
                mRecyclerView.loadMoreComplete();
            }
        }
        if (pd!=null&&pd.isShowing()) {
            pd.dismiss();
        }
        Toast.makeText(KaoQinDetaliActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_working:
                currentType = "workState=01";
                if (cType.equalsIgnoreCase("paihang")) {
                    currentType = "workTime=01";
                }
                setAllTextViewBeBlack();
                tv_working.setTextColor(Color.RED);
                kaoqinListPresenter.loadKaoqinDetailList(String.valueOf(currentPage),getCurrentTime(),getStartTime(),getCurrentTime(),currentType);
                break;
            case R.id.tv_work_over:
                currentType = "overTimeState=01";
                if (cType.equalsIgnoreCase("paihang")) {
                    currentType = "dayTransAmount=01";
                }
                setAllTextViewBeBlack();
                tv_work_overtime.setTextColor(Color.RED);
                kaoqinListPresenter.loadKaoqinDetailList(String.valueOf(currentPage),getCurrentTime(),getStartTime(),getCurrentTime(),currentType);
                break;
            case R.id.tv_late:
                currentType = "lateState=01";
                if (cType.equalsIgnoreCase("paihang")) {
                    currentType = "lateTime=01";
                }
                setAllTextViewBeBlack();
                tv_late.setTextColor(Color.RED);
                kaoqinListPresenter.loadKaoqinDetailList(String.valueOf(currentPage),getCurrentTime(),getStartTime(),getCurrentTime(),currentType);
                break;
            case R.id.tv_leave:
                currentType = "leaveState=01";
                if (cType.equalsIgnoreCase("paihang")) {
                    currentType = "visitorTime=01";
                }
                setAllTextViewBeBlack();
                tv_leave.setTextColor(Color.RED);
                kaoqinListPresenter.loadKaoqinDetailList(String.valueOf(currentPage),getCurrentTime(),getStartTime(),getCurrentTime(),currentType);
                break;

        }
    }

    private void setAllTextViewBeBlack() {
        tv_working.setTextColor(Color.BLACK);
        tv_work_overtime.setTextColor(Color.BLACK);
        tv_late.setTextColor(Color.BLACK);
        tv_leave.setTextColor(Color.BLACK);
    }
}
