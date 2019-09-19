package com.sangu.apptongji.main.activity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSONObject;
import com.darwindeveloper.onecalendar.clases.Day;
import com.darwindeveloper.onecalendar.views.OneCalendarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.mapsearch.MapPickerActivity;
import com.sangu.apptongji.main.model.UserAttendance;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendancePersonageActivity extends CoreActivity  implements IQiYeDetailView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String name;
    private String uid;
    private String companyId;
    private String timeStart;
    private String timeEnd;
    @BindView(R.id.oneCalendar)
    OneCalendarView calendarView;
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
    private QiYeInfo qiYeInfo;
    private int type;
    private boolean isDay, isTime;
    private String mylat = "", mylon = "", street = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_personage);
        ButterKnife.bind(this);
        name = getIntent().getStringExtra("name");
        tvTitle.setText(name + "的考勤");
        companyId = getIntent().getStringExtra("companyId");
        uid = getIntent().getStringExtra("uid");
        timeStart = getIntent().getStringExtra("timeStart");
        timeEnd = getIntent().getStringExtra("timeEnd");
        tvSet.setText("位置");
        calendarView.setOnCalendarChangeListener(calendarChangeListener);
        calendarView.setOneCalendarClickListener(oneCalendarClickListener);
        day = calendarView.getCurrentDayMonth();
        month = calendarView.getCurrentMonth();
        year = calendarView.getCurrentYear();
        currentMonth = month;
        currentYear = year;
        tvToday.setText(day + "");
        queryComClockByTime();
        qiYeInfoPresenter = new QiYeInfoPresenter(AttendancePersonageActivity.this, this);
        qiYeInfoPresenter.loadQiYeInfo(companyId);
    }

    private OneCalendarView.OnCalendarChangeListener calendarChangeListener = new OneCalendarView.OnCalendarChangeListener() {
        @Override
        public void prevMonth() {
            year = calendarView.getYear();
            month = calendarView.getMonth();
            queryComClockByTime();
        }

        @Override
        public void nextMonth() {
            year = calendarView.getYear();
            month = calendarView.getMonth();
            queryComClockByTime();
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
            initTime(d);
            if(isCanSet()){
                return;
            }
            ArrayList<Day> list = calendarView.getFragment().getDaysByDate(year + getFormatMonth(month)+uid);
            if (list == null || list.size() == 0) {
                list = calendarView.getFragment().getDaysByMap(year, month);
            }
            Day newDay = d;
            if (d.getStatus().equals("0")) {
                newDay.setStatus("1");
            } else if (d.getStatus().equals("1")) {
                newDay.setStatus("2");
            } else if (d.getStatus().equals("2")) {
                newDay.setStatus("0");
            }
            list.remove(position);
            list.add(position, newDay);
            calendarView.getFragment().setDaysByMap(year + getFormatMonth(month)+uid, list);
            //set.add(year + getFormatMonth(month));
        }

        @Override
        public void dateOnLongClick(Day day, int position) {
        }
    };

    //查询个人考勤
    private void queryComClockByTime() {
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(year + getFormatMonth(month) + uid);
        if (list != null && list.size() > 0) {
            int index = calendarView.getCurrentDayMonth() + getOffset(month, year) - 1 + day;
            if (year == calendarView.getCurrentYear() && month == calendarView.getCurrentMonth() && day == calendarView.getCurrentDayMonth()) {
                initTime(list.get(index));
                return;
            } else {
                //tvToday.setText(day+"");
            }
        }
        showDialog();
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("uId", uid));
        params.add(new Param("timeStart", year + getFormatMonth(month) + "01"));
        params.add(new Param("timeEnd", getDateLastDay(year, month)));
        OkHttpManager.getInstance2().post(params, FXConstant.GET_COM_CLOCK_BY_TIME, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("query==uid==" + uid + "-" + companyId + "-" + timeStart + "-" + timeEnd + "-", jsonObject.getJSONArray("comList").size() + "");
                disDialog();
                try {
                    Gson gson = new Gson();
                    ArrayList<UserAttendance> users = gson.fromJson(jsonObject.getString("comList"), new TypeToken<List<UserAttendance>>() {
                    }.getType());
                    JSONObject comLog = jsonObject.getJSONObject("combacklog");
                    formatComLog(year, month, comLog.getString("backLog"), uid, users);
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(String errorMsg) {
                disDialog();
            }
        });
    }

    private void formatComLog(int year, int month, String log,String uid,  ArrayList<UserAttendance> users) {
        String[] dateArr = log.split("-");
        if (dateArr.length == 1) {
            return;
        }
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(year + getFormatMonth(month));
        if (list == null || list.size() == 0) {
            list = calendarView.getFragment().getDaysByMap(year, month);
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
            day.setPerson(true);
            if (i >= users.size()) {
                day.setStatus(state);
                day.setTime(time);
                day.setX(arr[2]);
                day.setY(arr[3]);
                day.setLegal(false);
            } else {
                UserAttendance attendance = users.get(i);
                String restState = attendance.getRestState();
                if (restState.length() == 2) {
                    restState = restState.substring(1);
                }
                day.setStatus(restState);
                day.setTime(attendance.getSetSignaTime());
                day.setX(attendance.getLatitude());
                day.setY(attendance.getLongitude());
                day.setLegal(true);
                if(!restState.equals("0")){
                    if(attendance.getLeaveEarly().equals("01")){
                        day.setWorkStatus("01");
                    }else{
                        String[] times = attendance.getSetSignaTime().split("\\|", -1);
                        if(signPattern.equals("00")){
                            if(TextUtils.isEmpty(attendance.getMornTime())||isLate(times[0],attendance.getMornTime())){
                                day.setWorkStatus("01");
                            }else if(TextUtils.isEmpty(attendance.getAfternoonTime())||isLate(times[0],attendance.getAfternoonTime())){
                                day.setWorkStatus("01");
                            }
                        }else{
                            if(TextUtils.isEmpty(attendance.getMornTime())||isLate(times[0],attendance.getMornTime())){
                                day.setWorkStatus("01");
                            }
                        }
                    }
                    if(attendance.getLeaveTime().equals("01")){
                        day.setWorkStatus("02");
                    }else if(attendance.getLeaveTime().equals("02")){
                        day.setWorkStatus("03");
                    }else if(attendance.getWorkState().equals("01")){
                        day.setWorkStatus("04");
                    }
                }
            }
            if (day.isSelected()) {
                initTime(list.get(i + start));
            }
            day.setAddress(arr[4]);
            list.remove(i + start);
            list.add(i + start, day);
        }
        Log.e("day====","setuid=="+(year + getFormatMonth(month) + uid));
        calendarView.getFragment().setDaysByMap(year + getFormatMonth(month) + uid, list);
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
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date1);
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
    public void updateQiyeInfo(QiYeInfo qiYeInfo) throws UnsupportedEncodingException {
        signPattern = qiYeInfo.getSignPattern();
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

    private boolean isLate(String normal,String work){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date normalDate = dateFormat.parse(normal);
            Date workDate = dateFormat.parse(work);
            int i = normalDate.compareTo(workDate);
            return i<0;
        } catch (ParseException e) {
        }
        return false;
    }

    @OnClick({R.id.tv_back, R.id.iv_back, R.id.tv_time1, R.id.tv_time2, R.id.tv_time3, R.id.tv_time4, R.id.tv_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_back:
                finish();
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
        }
    }

    private void getInitTime(TextView tv, int type) {
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(currentYear + getFormatMonth(currentMonth)+uid);
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
                if (et.after(bt)) {
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

    private void timeDialog() {
        AlertDialog dialog = new AlertDialog.Builder(AttendancePersonageActivity.this)
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

    private void changeDate(boolean isDay) {
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(currentYear + getFormatMonth(currentMonth)+uid);
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
                    list.remove(index + day);
                    list.add(index + day, dd);
                }
            }
        }
        calendarView.getFragment().setDaysByMap(currentYear + getFormatMonth(currentMonth)+uid, list);
        insertComUserLog();
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

    private void skipEmployeesAttendance(boolean isDay) {
        Intent intent = new Intent(AttendancePersonageActivity.this, EmployeesAttendanceActivity.class);
        intent.putExtra("companyId", companyId);
        intent.putExtra("day", isDay);
        intent.putExtra("date", currentYear + getFormatMonth(currentMonth) + getFormatDay(day));
        startActivityForResult(intent, 6);
    }

    //替换考勤 @parme isTime是否替换时间
    private void insertComUserLog() {
        showDialog();
        List<Param> params = new ArrayList<>();
        params.add(new Param("companyId", companyId));
        params.add(new Param("uid", uid));
        params.add(new Param("backLog", listToString()));
        OkHttpManager.getInstance2().post(params, FXConstant.SET_USER_BACK_LOG, new OkHttpManager.HttpCallBack() {
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
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(currentYear + getFormatMonth(currentMonth)+uid);
        for (int i = index; i < list.size(); i++) {
            Day dd = list.get(i);
            if (dd.isValid()) {
                sb.append(dd.getTime() + ",");
                sb.append(dd.getStatus() + ",");
                sb.append(dd.getX() + ",");
                sb.append(dd.getY() + ",");
                sb.append(dd.getAddress() + ",");
                sb.append(dd.getDate().getYear()+getFormatMonth(dd.getDate().getMonth())+getFormatDay(dd.getDate().getDay()) + "-");

            }
        }
        if (sb.toString().endsWith("-")) {
            return sb.toString().substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    private void setLocation() {
        Intent intent6 = new Intent(AttendancePersonageActivity.this, MapPickerActivity.class);
        intent6.putExtra("biaoshi", "01");
        startActivityForResult(intent6, 5);
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
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void locationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(AttendancePersonageActivity.this)
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

    private void changeLocation(boolean isDay) {
        ArrayList<Day> list = calendarView.getFragment().getDaysByDate(currentYear + getFormatMonth(currentMonth)+uid);
        if (list == null || list.size() == 0) {
            list = calendarView.getFragment().getDaysByMap(currentYear, currentMonth);
        }
        int index = getOffset(currentMonth, currentYear) - 1;
        Log.e("time====", index + "-" + day + "-" + type);
        if (isDay) {
            Day dd = list.get(index + day);
            dd.setAddress(this.street);
            dd.setX(this.mylon);
            dd.setY(this.mylat);
            list.remove(index + day);
            list.add(index + day, dd);
        } else {
            for (int i = index; i < list.size(); i++) {
                Day dd = list.get(i);
                if (dd.isValid()) {
                    dd.setAddress(this.street);
                    dd.setX(this.mylon);
                    dd.setY(this.mylat);
                    list.remove(i);
                    list.add(i, dd);
                }
            }
        }
        calendarView.getFragment().setDaysByMap(currentYear + getFormatMonth(currentMonth)+uid, list);
        insertComUserLog();
    }

    private boolean isCanSet() {
        String current = calendarView.getCurrentYear() + getFormatMonth(calendarView.getCurrentMonth()) + getFormatDay(calendarView.getCurrentDayMonth());
        String selected = year + getFormatMonth(month) + getFormatDay(day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date bt = sdf.parse(current);
            Date et = sdf.parse(selected);
            if (et.before(bt)) {
               return true;
            }
        } catch (ParseException e) {
        }
        return false;
    }

}
