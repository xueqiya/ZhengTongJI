package com.sangu.apptongji.main.qiye;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.mapsearch.MapPickerActivity;
import com.sangu.apptongji.main.qiye.presenter.IKaoQinSetPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.KaoQinSetPresenter;
import com.sangu.apptongji.main.qiye.view.IKaoQinSetView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.widget.calendar.entity.DayBean;
import com.sangu.apptongji.widget.calendar.listener.OnMonthCalendarChangedListener;
import com.sangu.apptongji.widget.calendar.view.MonthCalendar;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.List;

import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by Administrator on 2018-02-23.
 */

public class KaoQinSetActivity extends BaseActivity implements View.OnClickListener, IKaoQinSetView {

    private TextView tv_save,tv_am_shangban,tv_pm_shangban,tv_am_xiaban,tv_pm_xiaban
            ,tv_qiandao_location,tv_month,tv_location,tv_day;
    private MonthCalendar monthcalendar;
    private IKaoQinSetPresenter kaoQinSetPresenter;
    private String currentTimeStamp = "";
    private int currentPager;
    private String companyId = "";
    private int currentDay = -1;
    //上半部分显示的日子
    private DateTime titleDateTime;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_kaoqin_set);
        companyId = getIntent().getStringExtra("companyId");
        initViews();
        initDate();
        monthcalendar = (MonthCalendar) findViewById(R.id.monthcalendar);
        monthcalendar.setOnMonthCalendarChangedListener(new OnMonthCalendarChangedListener() {
            @Override
            public void onMonthCalendarChanged(final int pager, DateTime dateTime) {
                String data = dateTime.toLocalDate().toString();
                Log.d("chen", "时间" + data);
                String[] datas = data.split("-");
                titleDateTime = dateTime;
                //检查上一个月是否更改过信息
                if (currentDay != -1 && checkIsChanged(currentPager)) {
                    showSaveDialog(currentPager, currentTimeStamp);
                }
                currentTimeStamp = datas[0] + datas[1];
                currentPager = pager;
                currentDay = dateTime.getDayOfMonth();
                tv_month.setText(datas[0] + "年" + datas[1] + "月");
                kaoQinSetPresenter.getMonthDate(currentTimeStamp,companyId);

            }
        });
        monthcalendar.setOnDayClickListener(new MonthCalendar.OnDayClickListener() {
            @Override
            public void onDayClicked(DayBean dayBean) {
                DateTime dateTime = dayBean.getDateTime();
                tv_day.setText(dateTime.getDayOfMonth() + "");
                tv_am_shangban.setText("上午上班：" + dayBean.getAmShangban());
                tv_am_xiaban.setText("上午下班：" + dayBean.getAmXiaban());
                tv_pm_shangban.setText("下午上班：" + dayBean.getPmShangban());
                tv_pm_xiaban.setText("下午下班：" + dayBean.getPmXiaban());
                tv_qiandao_location.setText(dayBean.getLocation());
                if (dayBean.isChangeAble()) {
                    if (dayBean.getType() == 0) {
                        dayBean.setType(1);
                        dayBean.setChanged(true);
                    } else if (dayBean.getType() == 1) {
                        dayBean.setType(2);
                        dayBean.setChanged(true);
                    } else if (dayBean.getType() == 2) {
                        dayBean.setType(0);
                        dayBean.setChanged(true);
                    }

                } else {
                    Log.d("chen", "点击我是没有效果的！");
                }
            }
        });
    }

    private void showSaveDialog(final int currentPager, final String currentTimeStamp) {
        LayoutInflater inflater = LayoutInflater.from(KaoQinSetActivity.this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_kaoqin_set, null);
        final AlertDialog dialog = new AlertDialog.Builder(KaoQinSetActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView btn_cancel = (TextView) layout.findViewById(R.id.btn_cancel);
        TextView btn_ok = (TextView) layout.findViewById(R.id.btn_ok);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //改为更改数据所以现在是页数和时间都是上个月的
                updatePreMonthData(currentPager,currentTimeStamp);
                dialog.dismiss();
            }
        });
    }

    /**
     * 上传更改过后的数据
     * @param pager
     * @param timeStamp
     */

    private void updatePreMonthData(int pager, String timeStamp) {
        List<DayBean> list = monthcalendar.getPreMonthDayBean(pager);
        StringBuffer allTime = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            DayBean dayBean = list.get(i);
            StringBuffer itemTime = new StringBuffer();
           // Log.d("chen", dayBean.toString());
            int type = dayBean.getType();
            if (type == 0) {
                type = 1;
            } else if (type == 1) {
                type = 2;
            } else {
                type = 0;
            }
            itemTime.append(dayBean.getAmShangban() + "|" + dayBean.getPmXiaban() + "|" + dayBean.getAmXiaban() + "|" + dayBean.getPmShangban() + "," +
                    type);
            if (!TextUtils.isEmpty(dayBean.getLat())) {
                itemTime.append("," + dayBean.getLat());
            }
            if ( !TextUtils.isEmpty(dayBean.getLng())) {
                itemTime.append("," + dayBean.getLng());
            }
            if (!TextUtils.isEmpty(dayBean.getLocation())) {
                itemTime.append("," + dayBean.getLocation());
            }
            //如果不是最后一个就加上每天的分割附
            if (i != list.size() - 1) {
                itemTime.append("-");
            }
            allTime.append(itemTime);
        }
        Log.d("chen", "时间 " + timeStamp + "      俺要开始更新数据了！！ --->" + allTime.toString());
        kaoQinSetPresenter.updateMonthDate(timeStamp,companyId,allTime.toString());
    }

    /**
     * 是否修改过数据
     * @param pager 第几页
     * @return
     */
    private boolean checkIsChanged(int pager) {
        List<DayBean> dayBeanList = monthcalendar.getPreMonthDayBean(pager);
        for (DayBean bean : dayBeanList) {
            //Log.d("chen", "checkIsChanged" + bean.isChanged());
            if (bean.isChanged()) {
                return true;
            }
        }
        return false;

    }

    private void initDate() {
        kaoQinSetPresenter = new KaoQinSetPresenter(this,this);

    }

    private void initViews() {
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_am_shangban = (TextView) findViewById(R.id.tv_am_shangban);
        tv_pm_shangban = (TextView) findViewById(R.id.tv_pm_shangban);
        tv_am_xiaban = (TextView) findViewById(R.id.tv_am_xiaban);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_pm_xiaban = (TextView) findViewById(R.id.tv_pm_xiaban);
        tv_qiandao_location = (TextView) findViewById(R.id.tv_qiandao_location);
        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_location = (TextView) findViewById(R.id.tv_location);
        monthcalendar = (MonthCalendar) findViewById(R.id.monthcalendar);
        tv_save.setOnClickListener(this);
        tv_am_shangban.setOnClickListener(this);
        tv_pm_shangban.setOnClickListener(this);
        tv_am_xiaban.setOnClickListener(this);
        tv_pm_xiaban.setOnClickListener(this);
        tv_qiandao_location.setOnClickListener(this);
        tv_month.setOnClickListener(this);
        tv_location.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                if (checkIsChanged(currentPager)) {
                    showSaveDialog(currentPager,currentTimeStamp);
                }
                break;
            case R.id.tv_am_shangban:
                TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
                picker.setUseWeight(false);
                picker.setCycleDisable(false);
                picker.setRangeStart(0, 0);//00:00
                picker.setRangeEnd(23, 59);//23:59
                int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
                picker.setSelectedItem(currentHour, currentMinute);
                picker.setTopLineVisible(false);
                picker.setPadding(ConvertUtils.toPx(this, 15));
                picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        Log.d("chen", hour + ":" + minute);
                        showChooseTimeDialog(0, hour + ":" + minute);
                    }
                });
                picker.show();
                break;
            case R.id.tv_pm_shangban:
                TimePicker picker2 = new TimePicker(this, TimePicker.HOUR_24);
                picker2.setUseWeight(false);
                picker2.setCycleDisable(false);
                picker2.setRangeStart(0, 0);//00:00
                picker2.setRangeEnd(23, 59);//23:59
                int currentHour2 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int currentMinute2 = Calendar.getInstance().get(Calendar.MINUTE);
                picker2.setSelectedItem(currentHour2, currentMinute2);
                picker2.setTopLineVisible(false);
                picker2.setPadding(ConvertUtils.toPx(this, 15));
                picker2.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        Log.d("chen", hour + ":" + minute);
                        showChooseTimeDialog(3, hour + ":" + minute);
                    }
                });
                picker2.show();
                break;
            case R.id.tv_am_xiaban:
                TimePicker picker3 = new TimePicker(this, TimePicker.HOUR_24);
                picker3.setUseWeight(false);
                picker3.setCycleDisable(false);
                picker3.setRangeStart(0, 0);//00:00
                picker3.setRangeEnd(23, 59);//23:59
                int currentHour3 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int currentMinute3 = Calendar.getInstance().get(Calendar.MINUTE);
                picker3.setSelectedItem(currentHour3, currentMinute3);
                picker3.setTopLineVisible(false);
                picker3.setPadding(ConvertUtils.toPx(this, 15));
                picker3.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        Log.d("chen", hour + ":" + minute);
                        showChooseTimeDialog(2, hour + ":" + minute);
                    }
                });
                picker3.show();
                break;
            case R.id.tv_pm_xiaban:
                TimePicker picker4 = new TimePicker(this, TimePicker.HOUR_24);
                picker4.setUseWeight(false);
                picker4.setCycleDisable(false);
                picker4.setRangeStart(0, 0);//00:00
                picker4.setRangeEnd(23, 59);//23:59
                int currentHour4 = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int currentMinute4 = Calendar.getInstance().get(Calendar.MINUTE);
                picker4.setSelectedItem(currentHour4, currentMinute4);
                picker4.setTopLineVisible(false);
                picker4.setPadding(ConvertUtils.toPx(this, 15));
                picker4.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        Log.d("chen", hour + ":" + minute);
                        showChooseTimeDialog(1, hour + ":" + minute);
                    }
                });
                picker4.show();
                break;
            case R.id.tv_qiandao_location:
                    //显示的地址
                break;
            case R.id.tv_month:
                    //显示的月份
                break;
            case R.id.tv_location:
                startActivityForResult(new Intent(KaoQinSetActivity.this, MapPickerActivity.class),0);
                break;
            default:
                break;

        }

    }

    private void showChooseTimeDialog(final int type, final String text) {
        LayoutInflater inflater = LayoutInflater.from(KaoQinSetActivity.this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_kaoqin_set_choose, null);
        final AlertDialog dialog = new AlertDialog.Builder(KaoQinSetActivity.this,R.style.Dialog).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button btn_today = (Button) layout.findViewById(R.id.btn_today);
        Button btn_month = (Button) layout.findViewById(R.id.btn_month);
        Button btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
        btn_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTime(type, text, false);
                dialog.dismiss();
            }
        });
        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTime(type, text, true);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void changeTime(int type, String text, boolean isAll) {
        //是否修改本月全部
        if (isAll) {
            for (DayBean dayBean : monthcalendar.getMonthDayBean(currentPager)) {
                if (dayBean.isCurrentMonth) {
                    if (type == 0) {
                        dayBean.setAmShangban(text);
                        tv_am_shangban.setText("上午上班：" + text);
                    } else if (type == 1) {
                        dayBean.setPmXiaban(text);
                        tv_pm_xiaban.setText("下午下班：" + text);
                    } else if (type == 2) {
                        dayBean.setAmXiaban(text);
                        tv_am_xiaban.setText("上午下班：" + text);
                    } else if (type == 3) {
                        dayBean.setPmShangban(text);
                        tv_pm_shangban.setText("下午上班：" + text);
                    }
                    dayBean.setChanged(true);
                }
            }

        } else {
            for (DayBean dayBean : monthcalendar.getMonthDayBean(currentPager)) {
                if (dayBean.isCurrentMonth && dayBean.getDateTime().getDayOfMonth() == currentDay) {
                    if (type == 0) {
                        dayBean.setAmShangban(text);
                        tv_am_shangban.setText("上午上班：" + text);
                    } else if (type == 1) {
                        dayBean.setPmXiaban(text);
                        tv_pm_xiaban.setText("下午下班：" + text);
                    } else if (type == 2) {
                        dayBean.setAmXiaban(text);
                        tv_am_xiaban.setText("上午下班：" + text);
                    } else if (type == 3) {
                        dayBean.setPmShangban(text);
                        tv_pm_shangban.setText("下午上班：" + text);
                    }
                    dayBean.setChanged(true);
                    break;
                }

            }
        }
        monthcalendar.reflashCurrentPager(currentPager);
    }

    @Override
    public void updateMonthData(List<DayBean> list, String data) {
        tv_day.setText(currentDay + "");
        if (!currentTimeStamp.equalsIgnoreCase(data) || list == null) {
            tv_am_shangban.setText("上午上班：" + "00:00");
            tv_am_xiaban.setText("上午下班：" + "00:00");
            tv_pm_shangban.setText("下午上班：" + "00:00");
            tv_pm_xiaban.setText("下午下班：" + "00:00");
            tv_qiandao_location.setText("");
            return;
        }
        if (titleDateTime != null) {
            DayBean dayBean = list.get(titleDateTime.getDayOfMonth() - 1 );
            Log.d("chen", "头日期  " + titleDateTime.getDayOfMonth());
            tv_qiandao_location.setText(dayBean.getLocation());
            tv_am_shangban.setText("上午上班：" + dayBean.getAmShangban());
            tv_am_xiaban.setText("上午下班：" + dayBean.getAmXiaban());
            tv_pm_shangban.setText("下午上班：" + dayBean.getPmShangban());
            tv_pm_xiaban.setText("下午下班：" + dayBean.getPmXiaban());
        }
        monthcalendar.setDataWithInfo(list, currentPager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (data!=null){
                        final String lat = data.getStringExtra("latitude");
                        final String lng = data.getStringExtra("longitude");
                        final String dizhi = data.getStringExtra("street");

                        LayoutInflater inflater = LayoutInflater.from(KaoQinSetActivity.this);
                        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_kaoqin_set_choose, null);
                        final AlertDialog dialog = new AlertDialog.Builder(KaoQinSetActivity.this,R.style.Dialog).create();
                        dialog.show();
                        dialog.getWindow().setContentView(layout);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);
                        Button btn_today = (Button) layout.findViewById(R.id.btn_today);
                        Button btn_month = (Button) layout.findViewById(R.id.btn_month);
                        Button btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
                        btn_today.setText("设为当天签到地点");
                        btn_month.setText("设为当月签到地点");

                        btn_today.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (DayBean dayBean : monthcalendar.getMonthDayBean(currentPager)) {
                                    if (dayBean.isCurrentMonth && dayBean.getDateTime().getDayOfMonth() == currentDay) {
                                        dayBean.setLat(lat);
                                        dayBean.setLng(lng);
                                        dayBean.setLocation(dizhi);
                                        dayBean.setChanged(true);
                                        tv_qiandao_location.setText(dizhi);
                                        break;
                                    }

                                }
                                monthcalendar.reflashCurrentPager(currentPager);
                                dialog.dismiss();
                            }
                        });
                        btn_month.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                for (DayBean dayBean : monthcalendar.getMonthDayBean(currentPager)) {
                                    if (dayBean.isCurrentMonth) {
                                        dayBean.setLat(lat);
                                        dayBean.setLng(lng);
                                        dayBean.setLocation(dizhi);
                                        tv_qiandao_location.setText(dizhi);
                                        dayBean.setChanged(true);
                                    }
                                }
                                monthcalendar.reflashCurrentPager(currentPager);
                                dialog.dismiss();
                            }
                        });
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                    }
                    break;
            }
        }
    }
}
