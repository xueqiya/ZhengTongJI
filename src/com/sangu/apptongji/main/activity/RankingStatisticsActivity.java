package com.sangu.apptongji.main.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.recycle.dragrecyclerview.common.DividerItemDecoration;
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

public class RankingStatisticsActivity extends CoreActivity {
    private String companyId;
    private String comImage;
    private String timeStart;
    private String timeEnd;
    private String type;
    private int currentPage = 1;
    @BindView(R.id.tv_work_ranking)
    TextView tvWorkRanking;
    @BindView(R.id.tv_sale_ranking)
    TextView tvSaleRanking;
    @BindView(R.id.tv_late_ranking)
    TextView tvLateRanking;
    @BindView(R.id.tv_visitor_ranking)
    TextView tvVisitorRanking;
    @BindView(R.id.rv)
    XRecyclerView rv;
    private KaoqinDetailAdapter adapter;
    List<QiyeKaoQinDetailInfo> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_statistics);
        ButterKnife.bind(this);
        companyId = getIntent().getStringExtra("companyId");
        timeStart = getIntent().getStringExtra("timeStart");
        timeEnd = getIntent().getStringExtra("timeEnd");
//        timeStart = "20180801";
//        timeEnd = "20180831";
        type = getIntent().getStringExtra("type");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        adapter = new KaoqinDetailAdapter(this, list, "paihang");
        rv.addItemDecoration(new DividerItemDecoration(RankingStatisticsActivity.this, DividerItemDecoration.VERTICAL_LIST));
        rv.setAdapter(adapter);
        setState();
        queryCom();
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
    }

    private void queryCom() {
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("timeStart", timeStart));
        params.add(new Param("timeEnd", timeEnd));
        params.add(new Param("currentPage", currentPage + ""));
        params.add(new Param(type, "01"));
        OkHttpManager.getInstance().post(params, FXConstant.URL_QUERY_QIYE_KAOQIN_PAIHANG, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("query==" + companyId + "=", jsonObject.toJSONString());
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
                    JSONArray array = obj.getJSONArray("comClock");
                    if (array == null || array.length() < 20) {
                        rv.setLoadingMoreEnabled(false);
                    } else {
                        rv.setLoadingMoreEnabled(true);
                    }
                    List<QiyeKaoQinDetailInfo> info = JSONParser.parseQiyeKaoQinDetailList(array, type);
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

    @OnClick({R.id.tv_back, R.id.iv_back, R.id.tv_work_ranking, R.id.tv_sale_ranking, R.id.tv_late_ranking, R.id.tv_visitor_ranking})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_work_ranking:
                recover("workTime");
                break;
            case R.id.tv_sale_ranking:
                recover("dayTransAmount");
                break;
            case R.id.tv_late_ranking:
                recover("lateTime");
                break;
            case R.id.tv_visitor_ranking:
                recover("visitorTime");
                break;
        }
    }

    private void recover(String newType) {
        if (type.equals(newType)) {
            return;
        }
        if (type.equals("workTime")) {
            tvWorkRanking.setTextColor(0xffB7B7B7);
        } else if (type.equals("lateTime")) {
            tvLateRanking.setTextColor(0xffB7B7B7);
        } else if (type.equals("dayTransAmount")) {
            tvSaleRanking.setTextColor(0xffB7B7B7);
        } else if (type.equals("visitorTime")) {
            tvVisitorRanking.setTextColor(0xffB7B7B7);
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

    private void setState() {
        if (type.equals("workTime")) {
            tvWorkRanking.setTextColor(0xffBE3A39);
        } else if (type.equals("lateTime")) {
            tvLateRanking.setTextColor(0xffBE3A39);
        } else if (type.equals("dayTransAmount")) {
            tvSaleRanking.setTextColor(0xffBE3A39);
        } else if (type.equals("visitorTime")) {
            tvVisitorRanking.setTextColor(0xffBE3A39);
        }
    }

}
