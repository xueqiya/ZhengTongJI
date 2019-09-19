package com.sangu.apptongji.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.recycle.dragrecyclerview.common.DividerItemDecoration;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.qiye.adapter.KaoqinDetailAdapter;
import com.sangu.apptongji.main.qiye.entity.QiyeKaoQinDetailInfo;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendanceManagementActivity extends CoreActivity {
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    private String companyId;
    private String comImage;
    private String timeStart;
    private String timeEnd;
    private String timestamp;
    private String type;
    private String today;
    private String workCount="-";
    private String lateCount="-";
    private String overTimeCount="-";
    private String leaveCount="-";
    private int currentPage = 1;
    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @BindView(R.id.tv_work_state)
    TextView tvWorkState;
    @BindView(R.id.tv_overtime_state)
    TextView tvOvertimeState;
    @BindView(R.id.tv_late_state)
    TextView tvLateState;
    @BindView(R.id.tv_leave_state)
    TextView tvLeaveState;
    @BindView(R.id.rv)
    XRecyclerView rv;
    private KaoqinDetailAdapter adapter;
    List<QiyeKaoQinDetailInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_management);
        ButterKnife.bind(this);
        companyId = getIntent().getStringExtra("companyId");
        comImage = getIntent().getStringExtra("avatar");
        timeStart = getIntent().getStringExtra("timeStart");
        timeEnd = getIntent().getStringExtra("timeEnd");
        timestamp=getIntent().getStringExtra("timestamp");
        today = getIntent().getStringExtra("today");
//        timeStart = "20180801";
//        timeEnd = "20180831";
        type = getIntent().getStringExtra("type");
        ImageLoader.getInstance().displayImage(comImage, ivAvatar, DemoApplication.mOptions3);
        setCount();
        setState();
        queryCom();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new KaoqinDetailAdapter(this, list, "");
        rv.addItemDecoration(new DividerItemDecoration(AttendanceManagementActivity.this, DividerItemDecoration.VERTICAL_LIST));
        rv.setAdapter(adapter);
        rv.setFootViewText("正在加载", "加载完成");
        rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                reLoad();
            }

            @Override
            public void onLoadMore() {
                currentPage++;
                rv.loadMoreComplete();
                queryCom();
            }
        });
        adapter.setOnItemClickListener(new KaoqinDetailAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(AttendanceManagementActivity.this, AttendancePersonageActivity.class);
                intent.putExtra("timeStart",timeStart);
                intent.putExtra("timeEnd", timeEnd);
                intent.putExtra("timestamp",timestamp);
                intent.putExtra("companyId", companyId);
                intent.putExtra("name", list.get(position-1).getuName());
                intent.putExtra("uid", list.get(position-1).getuId());
                startActivity(intent);
            }
        });
    }


    private void setState() {
        if (type.equals("workState")) {
            tvWorkState.setTextColor(0xffBE3A39);
            tvCurrent.setText(workCount+"人");
        } else if (type.equals("lateState")) {
            tvLateState.setTextColor(0xffBE3A39);
            tvCurrent.setText(lateCount+"人");
        } else if (type.equals("overTimeState")) {
            tvOvertimeState.setTextColor(0xffBE3A39);
            tvCurrent.setText(overTimeCount+"人");
        } else if (type.equals("leaveState")) {
            tvLeaveState.setTextColor(0xffBE3A39);
            tvCurrent.setText(leaveCount+"人");
        }
    }

    private void setCount() {
        if (TextUtils.isEmpty(today)) {
            return;
        }
        try {
            org.json.JSONObject obj = new org.json.JSONObject(today);
            workCount=obj.optString("workState");
            overTimeCount=obj.optString("overTimeState");
            lateCount=obj.optString("lateState");
            org.json.JSONArray array=obj.optJSONArray("leaveUser");
            leaveCount=array==null?"0":array.length()+"";
            tvWorkState.setText("上班情况(" + workCount + "人)");
            tvOvertimeState.setText("加班情况(" + overTimeCount + "人)");
            tvLateState.setText("迟到情况(" + lateCount + "人)");
            tvLeaveState.setText("请假情况(" + leaveCount + "人)");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void queryCom() {
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("timeStart", timeStart));
        params.add(new Param("timeEnd", timeEnd));
        params.add(new Param("timestamp", timestamp));
        params.add(new Param("currentPage", currentPage + ""));
        params.add(new Param(type, "01"));
        OkHttpManager.getInstance().post(params, FXConstant.URL_QIYE_KAOQIN_DETAIL, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("query==" + companyId+"-"+timeStart+"-"+timeEnd +"-"+type +"=", jsonObject.toJSONString());
                disDialog();
                if (currentPage == 1) {
                    rv.refreshComplete();     //刷新数据完成（取消刷新动画）
                }
//                else{
//                    rv.loadMoreComplete();    //加载数据完成（取消加载动画）
//                }
                rv.loadMoreComplete();    //加载数据完成（取消加载动画）
                try {
                    boolean hasMore = true;
                    org.json.JSONObject obj = new org.json.JSONObject(jsonObject.toJSONString());
                    JSONArray array = obj.getJSONArray("comClockList");
                    if (array == null || array.length() < 20) {
                        rv.setLoadingMoreEnabled(false);
                    } else {
                        rv.setLoadingMoreEnabled(true);
                    }
                    List<QiyeKaoQinDetailInfo> info = JSONParser.parseQiyeKaoQinDetailList(array, type+"=01");
                    list.addAll(info);
                    adapter.notifyDataSetChanged();
//                    onQiyeListener.onSuccess(info,hasMore);
//                    onQiyeListener.onFinish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                disDialog();
            }
        });
    }

    @OnClick({R.id.tv_back, R.id.iv_back, R.id.tv_work_state, R.id.tv_overtime_state, R.id.tv_late_state, R.id.tv_leave_state})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_work_state:
                recover("workState");
                break;
            case R.id.tv_overtime_state:
                recover("overTimeState");
                break;
            case R.id.tv_late_state:
                recover("lateState");
                break;
            case R.id.tv_leave_state:
                recover("leaveState");
                break;
        }
    }

    private void recover(String newType) {
        if (type.equals(newType)) {
            return;
        }
        if (type.equals("workState")) {
            tvWorkState.setTextColor(0xffB7B7B7);
        } else if (type.equals("lateState")) {
            tvLateState.setTextColor(0xffB7B7B7);
        } else if (type.equals("overTimeState")) {
            tvOvertimeState.setTextColor(0xffB7B7B7);
        } else if (type.equals("leaveState")) {
            tvLeaveState.setTextColor(0xffB7B7B7);
        }
        type = newType;
        setState();
        reLoad();
    }

    private void reLoad() {
        currentPage = 1;
        if (list.size() > 0) {
            list.clear();
        }
        queryCom();
    }
}
