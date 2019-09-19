package com.sangu.apptongji.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.R;
import com.sangu.apptongji.adapter.CompanyMemoAdapter;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.model.UserBackLog;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendanceActivity extends CoreActivity {
    @BindView(R.id.oneCalendar)
    OneCalendarView calendarView;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    private byte queryType = 0;//默认日统计
    private String companyId;
    private String comImage;
    @BindView(R.id.tv_work_state)
    TextView tvWorkState;
    @BindView(R.id.tv_overtime_state)
    TextView tvOvertimeState;
    @BindView(R.id.tv_late_state)
    TextView tvLateState;
    @BindView(R.id.tv_leave_state)
    TextView tvLeaveState;
    @BindView(R.id.rg_type)
    RadioGroup rgType;
    private int day;
    private int month;
    private int year;
    private Map<String, JSONObject> dayMap = new HashMap<>();
    private Map<String, JSONObject> monthMap = new HashMap<>();
    private Map<String, JSONObject> yearMap = new HashMap<>();
    @BindView(R.id.ll_left)
    LinearLayout llLeft;
    private String today;
    public static Map<String, ArrayList<UserBackLog>> userBackMap = new HashMap<>();

    private ListView listview;
    private List<JSONObject> datas = new ArrayList<>();
    private List<JSONObject> datas2 = new ArrayList<>();
    private CompanyMemoAdapter memoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        ButterKnife.bind(this);
        companyId = getIntent().getStringExtra("companyId");
        comImage = getIntent().getStringExtra("avatar");
        calendarView.setOnCalendarChangeListener(new OneCalendarView.OnCalendarChangeListener() {
            @Override
            public void prevMonth() {
                year = calendarView.getYear();
                month = calendarView.getMonth();
                if (queryType != 0) {
                    queryCom();
                }
                queryComCheck();
            }

            @Override
            public void nextMonth() {
                year = calendarView.getYear();
                month = calendarView.getMonth();
                if (queryType != 0) {
                    queryCom();
                }
                queryComCheck();
            }
        });
        day = calendarView.getCurrentDayMonth();
        month = calendarView.getCurrentMonth();
        year = calendarView.getCurrentYear();
        calendarView.setOneCalendarClickListener(oneCalendarClickListener);
        rgType.setOnCheckedChangeListener(checkedChangeListener);
        listview = (ListView) findViewById(R.id.listview);
        queryCom();
        queryComCheck();
        ImageLoader.getInstance().displayImage(comImage, ivAvatar, DemoApplication.mOptions3);
    }

    private OneCalendarView.OneCalendarClickListener oneCalendarClickListener = new OneCalendarView.OneCalendarClickListener() {
        @Override
        public void dateOnClick(Day d, int position) {
            year = d.getDate().getYear();
            month = d.getDate().getMonth();
            day = d.getDate().getDate();
            if (queryType == 0) {
                queryCom();
            }

            String currentMonth = getFormatMonth(month);
            String currentDay = getFormatDay(day);

            for (Object object : datas){

                JSONObject object1 = (JSONObject)object;

                if (datas2 != null){
                    datas2.clear();
                }
                if (object1.getString("memotime").equals(year+currentMonth+currentDay)){

                    datas2.add(object1);

                }

            }

            if (datas2.size()>0 && memoAdapter == null){

                memoAdapter = new CompanyMemoAdapter(AttendanceActivity.this,datas2,"0");
                listview.setAdapter(memoAdapter);

            }else {

                memoAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void dateOnLongClick(Day day, int position) {
        }
    };

    private String getFormatDay(int day) {
        if (day < 10)
            return "0" + day;
        else
            return "" + day;
    }

    private String getFormatMonth(int month) {
        if ((month + 1) < 10)
            return "0" + (month + 1);
        else
            return "" + (month + 1);
    }

    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_day:
                    queryType = 0;
                    queryCom();
                    break;
                case R.id.rb_month:
                    queryType = 1;
                    queryCom();
                    break;
                case R.id.rb_year:
                    queryType = 2;
                    queryCom();
                    break;
            }
        }
    };

    private void queryCom() {
        JSONObject jsonObject = null;
        if (queryType == 0) {
            jsonObject = dayMap.get(year + getFormatMonth(month) + getFormatDay(day));
        } else if (queryType == 1) {
            jsonObject = monthMap.get(year + getFormatMonth(month));
        } else if (queryType == 2) {
            jsonObject = yearMap.get(year + "");
        }
        if (jsonObject != null) {
            setInitComData(jsonObject);
            return;
        }
        showDialog();
        initComData();
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        if (queryType == 0) {
            params.add(new Param("timestamp", year + getFormatMonth(month) + getFormatDay(day)));
        } else if (queryType == 1) {
            params.add(new Param("timeStart", year + getFormatMonth(month) + "01"));
            params.add(new Param("timeEnd", getDateLastDay(year, month)));
        } else if (queryType == 2) {
            params.add(new Param("timeStart", year + "0101"));
            params.add(new Param("timeEnd", year + "1231"));
        }
        OkHttpManager.getInstance().post(params, FXConstant.URL_QIYE_KAOQIN, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (queryType == 0 && year == calendarView.getCurrentYear() && month == calendarView.getCurrentMonth() && day == calendarView.getCurrentDayMonth()) {
                    today = jsonObject.toJSONString();
                }
                disDialog();
                setComData(jsonObject);
                setInitComData(jsonObject);
            }

            @Override
            public void onFailure(String errorMsg) {
                disDialog();
            }
        });
    }

    //查询公司考勤
    private void queryComCheck() {
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(year + getFormatMonth(month));
        if (list != null && list.size() > 0) {
          //  return;
        }
        showDialog();
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("timestamp", year + getFormatMonth(month)));
        OkHttpManager.getInstance2().post(params, FXConstant.URL_QUERY_KAOQIN_SET, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("check=="+year + getFormatMonth(month)+"=", jsonObject.toJSONString());
                disDialog();
                try {
                    Gson gson = new Gson();
                    ArrayList<UserBackLog> list = gson.fromJson(jsonObject.getString("userBackLog"), new TypeToken<List<UserBackLog>>() {
                    }.getType());
                    userBackMap.put(year + getFormatMonth(month), list);
                    JSONObject comLog = jsonObject.getJSONObject("comBackLog");
                    String date = comLog.getString("timestamp");
                    String backLog = comLog.getString("backLog");

                    //企业的备忘录
                    JSONArray com = jsonObject.getJSONArray("com");

                    if (datas.size()>0){
                        datas.clear();
                    }
                    if (datas2.size()>0){
                        datas2.clear();
                    }

                    String currentMonth = getFormatMonth(month);
                    String currentDay = getFormatDay(day);

                    for (Object object : com){

                        JSONObject object1 = (JSONObject)object;

                        datas.add(object1);

                        if (object1.getString("memotime").equals(year+currentMonth+currentDay)){

                            datas2.add(object1);

                        }

                    }

                    if (datas2.size()>0){

                        memoAdapter = new CompanyMemoAdapter(AttendanceActivity.this,datas2,"0");
                        listview.setAdapter(memoAdapter);

                    }

                    formatComLog(date, backLog, com);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(String errorMsg) {
                disDialog();
            }
        });
    }

    private void formatComLog(String date, String log, JSONArray object) {
        if (TextUtils.isEmpty(log)) {
            return;
        }
        String[] dateArr = log.split("-");
        if (dateArr.length == 1) {
            return;
        }
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(date);
        if (list == null || list.size() == 0) {
            if (date.length() != 6) {
                return;
            }
            int y = Integer.valueOf(date.substring(0, 4));
            int m = Integer.valueOf(date.substring(4, 6));
            list = calendarView.getFragment().getDaysByMap(y, m - 1);
        }
        int start = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isValid()) {
                start = i;
                break;
            }
        }
        for (int i = 0; i < dateArr.length; i++) {
            String[] arr = dateArr[i].split(",");
            if (arr.length < 2) {
                continue;
            }
            String time = arr[0];
            String state = arr[1];
            Day day = list.get(i + start);

            day.setTime(time);
            day.setStatus(state);
            day.setX(arr[2]);
            day.setY(arr[3]);
            day.setAddress(arr[4]);
            list.remove(i + start);
            list.add(i + start, day);
        }

        if (object.size()>0){

            List<Object> arr = new ArrayList<>();
            for (Object object1:object){

                arr.add(object1);

            }

            calendarView.getFragment().setCompanyMemo(arr,new ArrayList<>());

        }

        calendarView.getFragment().setDaysByMap(date, list);
    }

    public String getDateLastDay(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置时间,当前时间不用设置
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(calendar.getTime());
    }

    private void setComData(JSONObject jsonObject) {
        if (queryType == 0) {
            dayMap.put(year + getFormatMonth(month) + getFormatDay(day), jsonObject);
        } else if (queryType == 1) {
            monthMap.put(year + getFormatMonth(month), jsonObject);
        } else if (queryType == 2) {
            yearMap.put(year + "", jsonObject);
        }
    }

    private void setInitComData(JSONObject jsonObject) {
        if (queryType == 0) {
            if (llLeft.getVisibility() == View.GONE) {
                llLeft.setVisibility(View.VISIBLE);
            }
            tvWorkState.setText("上班情况(" + jsonObject.getString("workState") + "人)");
            tvOvertimeState.setText("加班情况(" + jsonObject.getString("overTimeState") + "人)");
        } else {
            if (llLeft.getVisibility() == View.VISIBLE) {
                llLeft.setVisibility(View.GONE);
            }
        }
        tvLateState.setText("迟到情况(" + jsonObject.getString("lateState") + "人)");
        JSONArray array = null;
        try {
            array = jsonObject.getJSONArray("leaveUser");
            tvLeaveState.setText("请假情况(" + array.size() + "人)");
        } catch (Exception e) {
            tvLeaveState.setText("请假情况(0人)");
        }
    }

    private void initComData() {
        if (queryType == 0) {
            if (llLeft.getVisibility() == View.GONE) {
                llLeft.setVisibility(View.VISIBLE);
            }
            tvWorkState.setText("上班情况(-人)");
            tvOvertimeState.setText("加班情况(-人)");
        } else {
            if (llLeft.getVisibility() == View.VISIBLE) {
                llLeft.setVisibility(View.GONE);
            }
        }
        tvLateState.setText("迟到情况(-人)");
        tvLeaveState.setText("请假情况(-人)");
    }

    @OnClick({R.id.tv_back, R.id.iv_back, R.id.tv_menu, R.id.tv_work_state, R.id.tv_overtime_state, R.id.tv_late_state, R.id.tv_leave_state, R.id.tv_work_ranking, R.id.tv_sale_ranking, R.id.tv_late_ranking, R.id.tv_visitor_ranking, R.id.tv_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_menu:
                Intent intent = new Intent(AttendanceActivity.this, AttendanceExaminationApprovalActivity.class);
                intent.putExtra("companyId", companyId);
                startActivity(intent);
                break;
            case R.id.tv_work_state:
                skipAttendanceManagement("workState");
                break;
            case R.id.tv_overtime_state:
                skipAttendanceManagement("overTimeState");
                break;
            case R.id.tv_late_state:
                skipAttendanceManagement("lateState");
                break;
            case R.id.tv_leave_state:
                skipAttendanceManagement("leaveState");
                break;
            case R.id.tv_work_ranking:
                skipRankingStatistics("workTime");
                break;
            case R.id.tv_late_ranking:
                skipRankingStatistics("lateTime");
                break;
            case R.id.tv_sale_ranking:
                skipRankingStatistics("dayTransAmount");
                break;
            case R.id.tv_visitor_ranking:
                skipRankingStatistics("visitorTime");
                break;
            case R.id.tv_set:
                skipAttendanceSet();
                break;
        }
    }

    private void skipAttendanceSet() {
        initCurrentDate();
        Intent intent = new Intent(AttendanceActivity.this, AttendanceSetActivity.class);
        intent.putExtra("companyId", companyId);
        //startActivity(intent);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    queryComCheck();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initCurrentDate() {
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(calendarView.getCurrentYear() + getFormatMonth(calendarView.getCurrentMonth()));
        if (list != null && list.size() > 0) {
            int index = calendarView.getCurrentDayMonth() + getOffset(calendarView.getCurrentMonth(), calendarView.getCurrentYear()) - 1;
            for (int i = 0; i < list.size(); i++) {
                Day day = list.get(i);
                if (!day.isValid()) {
                    continue;
                }
                if (day.isSelected() && i != index) {
                    day.setSelected(false);
                    list.remove(i);
                    list.add(i, day);
                } else if (i == index && !day.isSelected()) {
                    day.setSelected(true);
                    list.remove(i);
                    list.add(i, day);
                }
            }
            calendarView.getFragment().setDaysByMap2(calendarView.getCurrentYear() + getFormatMonth(calendarView.getCurrentMonth()), list);
        }
    }

    private void skipRankingStatistics(String type) {
        Intent intent = new Intent(AttendanceActivity.this, RankingStatisticsActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("timeStart", calendarView.getCurrentYear() + getFormatMonth(calendarView.getCurrentMonth()) + "01");
        intent.putExtra("timeEnd", getDateLastDay(calendarView.getCurrentYear(), calendarView.getCurrentMonth()));
        intent.putExtra("companyId", companyId);
        startActivity(intent);
    }

    private void skipAttendanceManagement(String type) {
        if (queryType != 0 || year != calendarView.getCurrentYear() || month != calendarView.getCurrentMonth() || day != calendarView.getCurrentDayMonth()) {
            return;
        }
        Intent intent = new Intent(AttendanceActivity.this, AttendanceManagementActivity.class);
        intent.putExtra("avatar", comImage);
        intent.putExtra("type", type);
        intent.putExtra("timeStart", year + getFormatMonth(month) + "01");
        intent.putExtra("timeEnd", getDateLastDay(year, month));
        intent.putExtra("timestamp",year + getFormatMonth(month) + getFormatDay(day));
        intent.putExtra("companyId", companyId);
        intent.putExtra("today", today);
        startActivity(intent);
    }

    private int getOffset(int month, int year) {
        String nameFirstDay = getNameDay(1, month, year);
        int blankSpaces = 0;
        switch (nameFirstDay) {
            case "Monday":
                blankSpaces = 1;
                break;
            case "Tuesday":
                blankSpaces = 2;
                break;
            case "Wednesday":
                blankSpaces = 3;
                break;
            case "Thursday":
                blankSpaces = 4;
                break;
            case "Friday":
                blankSpaces = 5;
                break;
            case "Saturday":
                blankSpaces = 6;
                break;
        }
        return blankSpaces;
    }

    public String getNameDay(int day, int month, int year) {
        Date date1 = (new GregorianCalendar(year, month, day)).getTime();
        // Then get the day of week from the Date based on specific locale.
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date1);
    }

}
