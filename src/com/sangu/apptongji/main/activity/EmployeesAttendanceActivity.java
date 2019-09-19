package com.sangu.apptongji.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.UserLogAdapter;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.widget.SlideRecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmployeesAttendanceActivity extends CoreActivity {
    private String companyId;
    private boolean isDay;
    private String date;
    @BindView(R.id.rv)
    SlideRecyclerView rv;
    private List<UserAll> list = new ArrayList<>();
    private UserLogAdapter adapter = null;
    private String lng = "116.407170", lat = "39.904690";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees_attendance);
        ButterKnife.bind(this);
        companyId = getIntent().getStringExtra("companyId");
        isDay = getIntent().getBooleanExtra("day", false);
        date = getIntent().getStringExtra("date");
        if (isDay) {
            getUserLogByDay();
        } else {
            getUserLogByMonth();
        }
        lng = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "116.407170" : DemoApplication.getInstance().getCurrentLng();
        lat = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "39.904690" : DemoApplication.getInstance().getCurrentLat();
        LinearLayoutManager layoutManager = new LinearLayoutManager(EmployeesAttendanceActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_dark));
        rv.addItemDecoration(itemDecoration);
        adapter = new UserLogAdapter(list, EmployeesAttendanceActivity.this, lat, lng);
        rv.setAdapter(adapter);
        adapter.setOnDeleteClickListener(new UserLogAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                list.remove(position);
                adapter.notifyDataSetChanged();
                rv.closeMenu();
            }
        });
    }

    @OnClick({R.id.tv_back, R.id.iv_back, R.id.tv_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_menu:
                backData();
                break;
        }
    }

    private void backData() {
        StringBuilder sb = new StringBuilder();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                UserAll user = list.get(i);
                if (i == list.size() - 1) {
                    sb.append(user.getuId());
                }else{
                    sb.append(user.getuId()+",");
                }
            }
        }
        Intent intent =  new Intent();              //回传intent不需要参数了
        intent.putExtra( "id",sb.toString());                 //给意图intent添加key、value
        setResult(RESULT_OK, intent);                      //设置 返回标号 ，这里是2；回传的是intent
        finish();
    }

    private void getUserLogByDay() {
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("timestamp", date));
        OkHttpManager.getInstance().post(params, FXConstant.GET_USER_LOG_BY_DAY, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("userlog==day=" + companyId + "=", jsonObject.toJSONString());
                try {
                    org.json.JSONObject obj = new org.json.JSONObject(jsonObject.toJSONString());
                    list.addAll(JSONParser.parseUserListNew(obj.getJSONArray("code")));
                    adapter.notifyDataSetChanged();
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

    private void getUserLogByMonth() {
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("timestamp", date));
        OkHttpManager.getInstance().post(params, FXConstant.GET_USER_LOG_BY_MONTH, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("userlog==month=" + companyId + "=", jsonObject.toJSONString());
                try {
                    org.json.JSONObject obj = new org.json.JSONObject(jsonObject.toJSONString());
                    list.addAll(JSONParser.parseUserListNew(obj.getJSONArray("code")));
                    adapter.notifyDataSetChanged();
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
}
