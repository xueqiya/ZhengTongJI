package com.sangu.apptongji.main.activity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.adapter.CompanyMemoAdapter;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.mapsearch.MapPickerActivity;
import com.sangu.apptongji.main.model.UserBackLog;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.JSONParser;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendanceSetActivity extends CoreActivity implements IQiYeDetailView {
    private String companyId;
    @BindView(R.id.oneCalendar)
    OneCalendarView calendarView;
    @BindView(R.id.fb)
    FloatingActionButton fb;
    @BindView(R.id.tv_time1)
    TextView tvTime1;
    @BindView(R.id.tv_time2)
    TextView tvTime2;
    @BindView(R.id.tv_time3)
    TextView tvTime3;
    @BindView(R.id.tv_time4)
    TextView tvTime4;
    @BindView(R.id.tv_today)
    TextView tvToday;
    @BindView(R.id.tv_set)
    TextView tvSet;
    private int day;
    private int month;
    private int year;
    private int currentMonth;
    private int currentYear;
    private int currentHour;
    private int currentMinute;
    private IQiYeInfoPresenter qiYeInfoPresenter = null;
    private String signPattern = "00";
    private String companyName = "";
    public static HashSet<String> set = new HashSet<>();
    private String mylat = "", mylon = "", location = "", city = "", district = "", street = "";
    private QiYeInfo qiYeInfo;
    private int type;
    private boolean isDay, isTime;

    private ListView listview;
    private List<JSONObject> datas = new ArrayList<>();
    private List<JSONObject> datas2 = new ArrayList<>();
    private CompanyMemoAdapter memoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_set);
        ButterKnife.bind(this);
        companyId = getIntent().getStringExtra("companyId");
        tvSet.setText("位置");
        calendarView.setOnCalendarChangeListener(calendarChangeListener);
        calendarView.setOneCalendarClickListener(oneCalendarClickListener);
        day = calendarView.getCurrentDayMonth();
        month = calendarView.getCurrentMonth();
        year = calendarView.getCurrentYear();
        currentMonth = month;
        currentYear = year;
        tvToday.setText(day + "");

        listview = (ListView)findViewById(R.id.listview);

        queryComCheck();
        qiYeInfoPresenter = new QiYeInfoPresenter(AttendanceSetActivity.this, this);
        qiYeInfoPresenter.loadQiYeInfo(companyId);
    }

    private OneCalendarView.OnCalendarChangeListener calendarChangeListener = new OneCalendarView.OnCalendarChangeListener() {
        @Override
        public void prevMonth() {
            year = calendarView.getYear();
            month = calendarView.getMonth();
            queryComCheck();
        }

        @Override
        public void nextMonth() {
            year = calendarView.getYear();
            month = calendarView.getMonth();
            queryComCheck();
        }
    };

    private OneCalendarView.OneCalendarClickListener oneCalendarClickListener = new OneCalendarView.OneCalendarClickListener() {
        @Override
        public void dateOnClick(Day d, int position) {
            year = d.getDate().getYear();
            month = d.getDate().getMonth();
            day = d.getDate().getDate();
            currentMonth = month;
            currentYear = year;
            tvToday.setText(day + "");
            setFBShowOrHide();
            initTime(d);
            if (!DemoHelper.getInstance().getCurrentUsernName().equals(qiYeInfo.getManagerId())) {
                return;
            }
            ArrayList<Day> list = calendarView.getFragment().getDaysByDate(year + getFormatMonth(month));
            if (list == null || list.size() == 0) {
                list = calendarView.getFragment().getDaysByMap(year, month);
            }

            Day newDay = d;

            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String currentTime = dateFormat.format(date);
            String currentYear1 = currentTime.substring(0,4);
            String currentMonth1 = currentTime.substring(4,6);
            String currentDay1 = currentTime.substring(6,8);


            if ((Integer.valueOf(currentYear1)>year||Integer.valueOf(currentYear1)==year) &&
                    (Integer.valueOf(currentMonth1)>(month+1)||Integer.valueOf(currentMonth1)==(month+1)) &&
                    (Integer.valueOf(currentDay1)>day)){

            }else {

                if (d.getStatus().equals("0")) {
                    newDay.setStatus("1");
                } else if (d.getStatus().equals("1")) {
                    newDay.setStatus("2");
                } else if (d.getStatus().equals("2")) {
                    newDay.setStatus("0");
                }

            }

            list.remove(position);
            list.add(position, newDay);
            calendarView.getFragment().setDaysByMap(year + getFormatMonth(month), list);
            set.add(year + getFormatMonth(month));


            String currentMonth = getFormatMonth(month);
            String currentDay = getFormatDay(day);

            datas2.clear();
            for (Object object : datas){

                JSONObject object1 = (JSONObject)object;

                if (object1.getString("memotime").equals(year+currentMonth+currentDay)){

                    datas2.add(object1);

                }

            }

            if (datas2.size()>0){

                memoAdapter = new CompanyMemoAdapter(AttendanceSetActivity.this,datas2,"0");
                listview.setAdapter(memoAdapter);

            }else {
                memoAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void dateOnLongClick(Day day, int position) {
        }
    };

    private void changeSignPattern() {
        if (signPattern.equals("00")) {
            tvTime2.setVisibility(View.VISIBLE);
            tvTime3.setVisibility(View.VISIBLE);
        } else {
            tvTime2.setVisibility(View.GONE);
            tvTime3.setVisibility(View.GONE);
        }
    }

    private void initTime(Day day) {
        if (!day.isValid()) {
            return;
        }
        if (TextUtils.isEmpty(day.getTime())) {
            tvTime1.setText("上班:00:00");
            tvTime2.setText("下班:00:00");
            tvTime3.setText("上班:00:00");
            tvTime4.setText("下班:00:00");
        } else {
            String[] times = day.getTime().split("\\|", -1);
            if (times.length == 4) {
                tvTime1.setText("上班:" + getFormatTime(times[0]));
                tvTime2.setText("下班:" + getFormatTime(times[2]));
                tvTime3.setText("上班:" + getFormatTime(times[3]));
                tvTime4.setText("下班:" + getFormatTime(times[1]));
            } else {
                tvTime1.setText("上班:00:00");
                tvTime2.setText("下班:00:00");
                tvTime3.setText("上班:00:00");
                tvTime4.setText("下班:00:00");
            }
        }
    }

    private String getFormatTime(String time) {
        if (TextUtils.isEmpty(time)) {
            return "00:00";
        }
        return time;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        calendarView.getFragment().removeData(set);
    }

    private void showTime(final int hour, final int minute, final int type) {
        currentHour = hour;
        currentMinute = minute;
        this.type = type;
        TimePickerDialog dialog = new TimePickerDialog(this, timeSetListener,
                hour,
                minute,
                true);
//        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "取消", dialog);
//        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "设为当月时间", dialog);
//        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "设为当天时间", dialog);
        dialog.show();
    }

    private void timeDialog() {
        AlertDialog dialog = new AlertDialog.Builder(AttendanceSetActivity.this)
                .setMessage("请选择")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("设为当月时间", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeDate(false);
                    }
                })
                .setPositiveButton("设为当天时间", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeDate(true);
                    }
                }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    //考勤有冲突
    private void clashDialog(final boolean isDay, boolean isTime) {
        this.isDay = isDay;
        this.isTime = isTime;
        AlertDialog dialog = new AlertDialog.Builder(AttendanceSetActivity.this)
                .setTitle("提示")
                .setMessage("当前有员工已被调整考勤")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("查看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        skipEmployeesAttendance(isDay);
                    }
                })
                .setPositiveButton("替换", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isDay) {
                            getUserLogByDay();
                        } else {
                            getUserLogByMonth();
                        }
                    }
                }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void skipEmployeesAttendance(boolean isDay) {
        Intent intent = new Intent(AttendanceSetActivity.this, EmployeesAttendanceActivity.class);
        intent.putExtra("companyId", companyId);
        intent.putExtra("day", isDay);
        intent.putExtra("date", currentYear + getFormatMonth(currentMonth) + getFormatDay(day));
        startActivityForResult(intent, 6);
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener = new android.app.TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            currentHour = hourOfDay;
            currentMinute = minute;
            if (type == 0) {
                tvTime1.setText("上班:" + getFormatDay(currentHour) + ":" + getFormatDay(currentMinute));
            } else if (type == 2) {
                tvTime2.setText("下班:" + getFormatDay(currentHour) + ":" + getFormatDay(currentMinute));
            } else if (type == 3) {
                tvTime3.setText("上班:" + getFormatDay(currentHour) + ":" + getFormatDay(currentMinute));
            } else if (type == 1) {
                tvTime4.setText("下班:" + getFormatDay(currentHour) + ":" + getFormatDay(currentMinute));
            }
            timeDialog();
        }
    };

    private void changeDate(boolean isDay) {
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(currentYear + getFormatMonth(currentMonth));
        if (list == null || list.size() == 0) {
            list = calendarView.getFragment().getDaysByMap(currentYear, currentMonth);
        }
        int index = getOffset(currentMonth, currentYear) - 1;
        Log.e("time====", index + "-" + day + "-" + type);
        if (isDay) {
            Day dd = list.get(index + day);
            String time = dd.getTime();
            String[] arr = {"", "", "", ""};
            if (!TextUtils.isEmpty(time) && time.split("\\|", -1).length == 4) {
                arr = time.split("\\|", -1);
            }
            arr[type] = getFormatDay(currentHour) + ":" + getFormatDay(currentMinute);
            dd.setTime(arrayToString(arr, "|"));

            list.remove(index + day);
            list.add(index + day, dd);
        } else {
            for (int i = index; i < list.size(); i++) {
                Day dd = list.get(i);
                if (dd.isValid()) {
                    String time = dd.getTime();
                    String[] arr = {"", "", "", ""};
                    if (!TextUtils.isEmpty(time) && time.split("\\|", -1).length == 4) {
                        arr = time.split("\\|", -1);
                    }
                    arr[type] = getFormatDay(currentHour) + ":" + getFormatDay(currentMinute);
                    dd.setTime(arrayToString(arr, "|"));

                    if (dd.getAddress() == null){
                        dd.setAddress("无");
                    }
                    if (dd.getX() == null){
                        dd.setX("无");
                    }
                    if (dd.getY() == null){
                        dd.setY("无");
                    }

                    list.remove(index + day);
                    list.add(index + day, dd);
                }
            }
        }
        calendarView.getFragment().setDaysByMap(currentYear + getFormatMonth(currentMonth), list);
        ArrayList<UserBackLog> userBackLogs = AttendanceActivity.userBackMap.get(currentYear + getFormatMonth(currentMonth));
        if (isDay) {
            String timestamp = currentYear + getFormatMonth(currentMonth) + getFormatDay(day);
            for (int i = 0; i < userBackLogs.size(); i++) {
                UserBackLog log = userBackLogs.get(i);
                if (log.getTimestamp().equals(timestamp)) {
                    clashDialog(true, true);
                    return;
                }
            }
        } else if (userBackLogs != null && userBackLogs.size() > 0) {
            clashDialog(false, true);
            return;
        }
        insertComUserLog();
    }

    private void changeLocation(boolean isDay) {
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(currentYear + getFormatMonth(currentMonth));
        if (list == null || list.size() == 0) {
            list = calendarView.getFragment().getDaysByMap(currentYear, currentMonth);
        }
        int index = getOffset(currentMonth, currentYear) - 1;
        Log.e("time====", index + "-" + day + "-" + type);
        if (isDay) {
            Day dd = list.get(index + day);
            dd.setAddress(this.street);
            dd.setX(this.mylat);
            dd.setY(this.mylon);
            list.remove(index + day);
            list.add(index + day, dd);
        } else {
            for (int i = index; i < list.size(); i++) {
                Day dd = list.get(i);
                if (dd.isValid()) {

                    if (dd.getTime() == null){
                        dd.setTime("00:00|00:00|00:00|00:00");
                    }

                    dd.setAddress(this.street);
                    dd.setX(this.mylat);
                    dd.setY(this.mylon);
                    list.remove(i);
                    list.add(i, dd);
                }
            }
        }
        calendarView.getFragment().setDaysByMap(currentYear + getFormatMonth(currentMonth), list);
        ArrayList<UserBackLog> userBackLogs = AttendanceActivity.userBackMap.get(currentYear + getFormatMonth(currentMonth));
        if (isDay) {
            String timestamp = currentYear + getFormatMonth(currentMonth);
            for (int i = 0; i < userBackLogs.size(); i++) {
                UserBackLog log = userBackLogs.get(i);
                if (log.getTimestamp().equals(timestamp)) {
                    clashDialog(true, false);
                    return;
                }
            }
        } else if (userBackLogs != null && userBackLogs.size() > 0) {
            clashDialog(false, false);
            return;
        }
        insertComUserLog();
    }

    //查询公司考勤
    private void queryComCheck() {
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(year + getFormatMonth(month));
        if (list != null && list.size() > 0) {
            int index = getOffset(month, year) - 1+day;
            Log.e("com==",list.size()+"=="+index);
            if (year == calendarView.getCurrentYear() && month == calendarView.getCurrentMonth() && day == calendarView.getCurrentDayMonth()) {
                initTime(list.get(index));
            //    return;
            }else{
                //tvToday.setText(day+"");
            }
        }
        showDialog();
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("timestamp", year + getFormatMonth(month)));
        OkHttpManager.getInstance2().post(params, FXConstant.URL_QUERY_KAOQIN_SET, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                disDialog();
                try {

                    Gson gson = new Gson();
                    ArrayList<UserBackLog> list = gson.fromJson(jsonObject.getString("userBackLog"), new TypeToken<List<UserBackLog>>() {
                    }.getType());

                    AttendanceActivity.userBackMap.put(year + getFormatMonth(month), list);
                    JSONObject comLog = jsonObject.getJSONObject("comBackLog");
                    String date = comLog.getString("timestamp");
                    String backLog = comLog.getString("backLog");

                    if (backLog == null || backLog.length()<20){

                        tvTime1.setText("上班:00:00");
                        tvTime2.setText("下班:00:00");
                        tvTime3.setText("上班:00:00");
                        tvTime4.setText("下班:00:00");

                    }

                    //企业的备忘录
                    JSONArray com = jsonObject.getJSONArray("com");


                    //企业对个人的考勤安排
                    JSONArray comtoperson = jsonObject.getJSONArray("comtoperson");

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

                    for (Object object : comtoperson){

                        JSONObject object1 = (JSONObject)object;

                        datas.add(object1);

                        if (object1.getString("memotime").equals(year+currentMonth+currentDay)){

                            datas2.add(object1);

                        }

                    }

                    if (datas2.size()>0){

                        memoAdapter = new CompanyMemoAdapter(AttendanceSetActivity.this,datas2,"0");
                        listview.setAdapter(memoAdapter);

                    }

                    formatComLog(date, backLog,com,comtoperson);

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

    //更换考勤模式
    private void updateMode() {
        showDialog();
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("companyName", companyName));
        params.add(new Param("signPattern", signPattern.equals("00") ? "01" : "00"));
        OkHttpManager.getInstance2().post(params, FXConstant.URL_UPDATE_QIYE, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("mode==", jsonObject.toJSONString());
                disDialog();
                try {
                    String code = jsonObject.getString("code");
                    if (code.equals("SUCCESS")) {
                        signPattern = signPattern.equals("00") ? "01" : "00";
                        changeSignPattern();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                disDialog();
            }
        });
    }

    public String getNameDay(int day, int month, int year) {
        Date date1 = (new GregorianCalendar(year, month, day)).getTime();
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date1);
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

    private void formatComLog(String date, String log, JSONArray object, JSONArray object2) {
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
//            if (!state.equals(day.getStatus())) {
//                day.setStatus(state);
//                list.remove(i + start);
//                list.add(i + start, day);
//            }
            day.setStatus(state);
            day.setTime(time);
            day.setX(arr[2]);
            day.setY(arr[3]);
            day.setAddress(arr[4]);
            day.setTypeIdentify("设置企业");
            list.remove(i + start);
            list.add(i + start, day);
        }


        List<Object> arr = new ArrayList<>();
        List<Object> arr2 = new ArrayList<>();
        if (object.size()>0){

            for (Object object1:object){

                arr.add(object1);

            }

        }

        if (object2.size()>0){

            for (Object object1:object2){

                arr2.add(object1);

            }

        }

        calendarView.getFragment().setCompanyMemo(arr,arr2);

        calendarView.getFragment().setDaysByMap(date, list);
    }

    private void setFBShowOrHide() {
        String current = calendarView.getCurrentYear() + getFormatMonth(calendarView.getCurrentMonth()) + getFormatDay(calendarView.getCurrentDayMonth());
        String selected = year + getFormatMonth(month) + getFormatDay(day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date bt = sdf.parse(current);
            Date et = sdf.parse(selected);
            if (et.before(bt)) {
                fb.hide();
            } else {
                fb.show();
            }
        } catch (ParseException e) {
            fb.hide();
        }
    }

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

    @OnClick({R.id.tv_back, R.id.iv_back, R.id.tv_location, R.id.tv_mode, R.id.tv_poster, R.id.tv_time1, R.id.tv_time2, R.id.tv_time3, R.id.tv_time4, R.id.tv_set,R.id.tv_menu,R.id.fb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_location:
                attendanceLocation();
                break;
            case R.id.tv_mode:
                attendanceMode();
                break;
            case R.id.tv_poster:
                homePagePoster();
                break;
            case R.id.tv_time1:
                getInitTime(tvTime1, 0);
                break;
            case R.id.tv_time2:
                getInitTime(tvTime2, 2);
                break;
            case R.id.tv_time3:
                getInitTime(tvTime3, 3);
                break;
            case R.id.tv_time4:
                getInitTime(tvTime4, 1);
                break;
            case R.id.tv_set:
                setLocation();
                break;
            case R.id.tv_menu:
                insertComUserLog();
                break;
            case R.id.fb:
                InsertMemorandum();
                break;
        }
    }


    private void InsertMemorandum(){

        Intent intent = new Intent(AttendanceSetActivity.this,CompanyMemorandumActivity.class);

        String month1 = "";

        if ((month+1)<10){
            month1 = "0"+(month+1);
        }else {
            month1 = (month+1)+"";
        }

        String day1 = "";
        if (day<10){
            day1 = "0"+day;
        }else {
            day1 = day + "";
        }

        intent.putExtra("companyId",companyId);
        intent.putExtra("memoTime",year+month1+day1);

        startActivityForResult(intent,0);

    }


    private void setLocation() {
        Intent intent6 = new Intent(AttendanceSetActivity.this, MapPickerActivity.class);
        intent6.putExtra("biaoshi", "01");
        startActivityForResult(intent6, 5);
    }

    private void locationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(AttendanceSetActivity.this)
                .setMessage("请选择")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("设为当月签到地点", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeLocation(false);
                    }
                })
                .setPositiveButton("设为当天签到地点", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeLocation(true);
                    }
                }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @SuppressLint("SdCardPath")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 5:
                    mylat = data.getStringExtra("latitude");
                    mylon = data.getStringExtra("longitude");
                    street = data.getStringExtra("street");
                    Log.e("momentac,mylat", mylat + "");
                    Log.e("momentac,mylon", mylon + "");
                    Log.e("momentac,dizhi", street + "");
                    if (street != null && !"".equals(street) && !street.equalsIgnoreCase("null") && !"(null)".equals(street)) {
                        locationDialog();
                    } else {
                        showToast("获取位置失败，请重新获取");
                    }
                    break;
                case 6://返回冲突用户列表
                    String uid = data.getStringExtra("id");
                    Log.e("uid==", uid);
                    updateComUserLog(uid);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //替换考勤 @parme isTime是否替换时间
    private void updateComUserLog(String uid) {
        showDialog();
        List<Param> params = new ArrayList<>();
        params.add(new Param("uid", uid));
        String ts = this.currentYear + getFormatMonth(this.currentMonth);
        if (isDay) {
            ts = ts + getFormatDay(this.day);
        }
        params.add(new Param("timestamp", ts));
        if (isTime) {
            params.add(new Param("worktime" + (this.type + 1), getFormatDay(currentHour) + ":" + getFormatDay(currentMinute)));
        } else {
            params.add(new Param("lat", this.mylat));
            params.add(new Param("log", this.mylon));
            params.add(new Param("address", this.street));
        }
        OkHttpManager.getInstance2().post(params, FXConstant.UPDATE_COM_USER_LOGO, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                disDialog();
                Log.e("update==", jsonObject.toJSONString());
                String code = jsonObject.getString("code");
                if (code.equals("SUCCESS")) {
                    insertComUserLog();
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                disDialog();
            }
        });
    }

    private String listToString() {
        StringBuilder sb = new StringBuilder();
        int index = getOffset(currentMonth, currentYear) - 1;
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(currentYear + getFormatMonth(currentMonth));
        for (int i = index; i < list.size(); i++) {
            Day dd = list.get(i);
            if (dd.isValid()) {
                sb.append(dd.getTime() + ",");
                sb.append(dd.getStatus() + ",");
                sb.append(dd.getX() + ",");
                sb.append(dd.getY() + ",");
                sb.append(dd.getAddress() + "-");
            }
        }
        if (sb.toString().endsWith("-")) {
            return sb.toString().substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    //替换考勤 @parme isTime是否替换时间
    private void insertComUserLog() {
        showDialog();
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("timestamp", this.currentYear + getFormatMonth(this.currentMonth)));
        params.add(new Param("backLog", listToString()));
        OkHttpManager.getInstance2().post(params, FXConstant.URL_UPDATE_KAOQIN_SET, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                disDialog();
                Log.e("insert==content=", listToString());
                Log.e("insert==", jsonObject.toJSONString());
                String code = jsonObject.getString("code");
                if (code.equals("SUCCESS")) {
                    Intent intent = new Intent();              //回传intent不需要参数了
                    setResult(RESULT_OK, intent);
                    finish();

               //     Toast.makeText(AttendanceSetActivity.this,"修改成功",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(String errorMsg) {
                disDialog();
            }
        });
    }

    private void getInitTime(TextView tv, int type) {
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(currentYear + getFormatMonth(currentMonth));
        if (list == null || list.size() == 0) {
            list = calendarView.getFragment().getDaysByMap(currentYear, currentMonth);
        }
        int index = calendarView.getCurrentDayMonth() + getOffset(month, year) - 1;
        Day d = list.get(index);
        if (!TextUtils.isEmpty(d.getTime())) {
            String current = calendarView.getCurrentYear() + getFormatMonth(calendarView.getCurrentMonth()) + getFormatDay(calendarView.getCurrentDayMonth());
            String selected = currentYear + getFormatMonth(currentMonth) + getFormatDay(day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                Date bt = sdf.parse(current);
                Date et = sdf.parse(selected);
                if (!et.after(bt) && !d.getTime().contains("00:00")) {
                    return;
                }
            } catch (ParseException e) {
                return;
            }
        }
        String time = tv.getText().toString();
        if (TextUtils.isEmpty(time)) {
            return;
        }
        String[] arr = time.split(":", -1);
        if (arr.length != 3)
            return;
        showTime(Integer.valueOf(arr[1]), Integer.valueOf(arr[2]), type);
    }

    //考勤坐标
    private void attendanceLocation() {

    }

    //考勤模式
    private void attendanceMode() {
        updateMode();
    }

    //主页海报
    private void homePagePoster() {
        Intent intent = new Intent(AttendanceSetActivity.this, HomePagePosterActivity.class);
        intent.putExtra("companyId", companyId);
        startActivity(intent);
    }

    @Override
    public void updateQiyeInfo(QiYeInfo qiYeInfo) throws UnsupportedEncodingException {
        Log.e("company==", qiYeInfo.toString());
        this.qiYeInfo = qiYeInfo;
        companyName = qiYeInfo.getCompanyName();
        signPattern = qiYeInfo.getSignPattern();
        changeSignPattern();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError() {

    }


    private String arrayToString(String[] array, String sign) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i == (array.length - 1))
                sb.append(array[i]);
            else {
                sb.append(array[i] + sign);
            }
        }
        return sb.toString();
    }

    private void getUserLogByDay() {
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("timestamp", currentYear + getFormatMonth(currentMonth) + getFormatDay(day)));
        OkHttpManager.getInstance().post(params, FXConstant.GET_USER_LOG_BY_DAY, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("userlog==day=" + companyId + "=", jsonObject.toJSONString());
                try {
                    org.json.JSONObject obj = new org.json.JSONObject(jsonObject.toJSONString());
                    List<UserAll> list = JSONParser.parseUserListNew(obj.getJSONArray("code"));
                    StringBuilder sb = new StringBuilder();
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            UserAll user = list.get(i);
                            if (i == list.size() - 1) {
                                sb.append(user.getuId());
                            } else {
                                sb.append(user.getuId() + ",");
                            }
                        }
                    }
                    updateComUserLog(sb.toString());
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
        params.add(new Param("timestamp", currentYear + getFormatMonth(currentMonth) + getFormatDay(day)));
        OkHttpManager.getInstance().post(params, FXConstant.GET_USER_LOG_BY_MONTH, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("userlog==month=" + companyId + "=", jsonObject.toJSONString());
                try {
                    org.json.JSONObject obj = new org.json.JSONObject(jsonObject.toJSONString());
                    List<UserAll> list = JSONParser.parseUserListNew(obj.getJSONArray("code"));
                    StringBuilder sb = new StringBuilder();
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            UserAll user = list.get(i);
                            if (i == list.size() - 1) {
                                sb.append(user.getuId());
                            } else {
                                sb.append(user.getuId() + ",");
                            }
                        }
                    }
                    updateComUserLog(sb.toString());
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
