package com.sangu.apptongji.widget.calendar.utils;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;


import com.sangu.apptongji.widget.calendar.entity.DayBean;

import org.joda.time.DateTime;
import org.joda.time.Months;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-02-22.
 */

public class Utils {
    /**
     * @param dateTime 今天
     * @param type     0，周日，1周一
     * @param toDayTime
     * @return
     */
    public static NCalendar getMonthCalendar2(DateTime dateTime, int type, DateTime toDayTime) {

        DateTime lastMonthDateTime = dateTime.plusMonths(-1);//上个月
        DateTime nextMonthDateTime = dateTime.plusMonths(1);//下个月

        int days = dateTime.dayOfMonth().getMaximumValue();//当月天数
        int lastMonthDays = lastMonthDateTime.dayOfMonth().getMaximumValue();//上个月的天数

        int firstDayOfWeek = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), 1, 0, 0, 0).getDayOfWeek();//当月第一天周几

        int endDayOfWeek = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), days, 0, 0, 0).getDayOfWeek();//当月最后一天周几

        NCalendar nCalendar = new NCalendar();
        List<DayBean> dayBeans = new ArrayList<>();

        //周日开始的
        if (type == 0) {
            //上个月
            if (firstDayOfWeek != 7) {
                for (int i = 0; i < firstDayOfWeek; i++) {
                    DateTime dateTime1 = new DateTime(lastMonthDateTime.getYear(), lastMonthDateTime.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 1), 0, 0, 0);
                    DayBean bean = new DayBean();
                    bean.setChangeAble(false);
                    bean.setCurrentMonth(false);
                    bean.setDateTime(dateTime1);
                    bean.setType(2);
                    dayBeans.add(bean);
                }
            }
            //当月
            for (int i = 0; i < days; i++) {
                DateTime dateTime1 = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), i + 1, 0, 0, 0);
                DayBean bean = new DayBean();
                /*Log.d("chen", "toDayTime  month" + toDayTime.getMonthOfYear() + "   dateTime1  month" + dateTime1.getMonthOfYear()
                        + "\n" + "toDayTime  day" + toDayTime.getDayOfMonth() + "   dateTime1  day" + dateTime1.getDayOfMonth());*/
                if ( dateTime1.getYear() < toDayTime.getYear() ||
                        dateTime1.getMonthOfYear() < toDayTime.getMonthOfYear() ||
                        (dateTime1.getMonthOfYear() == toDayTime.getMonthOfYear() && dateTime1.getDayOfMonth() <= toDayTime.getDayOfMonth())) {
                    bean.setChangeAble(false);

                } else {
                    bean.setChangeAble(true);
                }
                DateTime.Property week =  dateTime1.dayOfWeek();
                if (week.get() == 7 || week.get() == 6) {
                    bean.setType(2);
                } else {
                    bean.setType(0);
                }
                bean.setCurrentMonth(true);
                bean.setDateTime(dateTime1);
                bean.setAmXiaban("00:00");
                bean.setAmShangban("00:00");
                bean.setPmShangban("00:00");
                bean.setPmXiaban("00:00");
                bean.setChanged(false);
                dayBeans.add(bean);
            }
            //下个月
            if (endDayOfWeek == 7) {
                endDayOfWeek = 0;
            }
            for (int i = 0; i < 6 - endDayOfWeek; i++) {
                DateTime dateTime1 = new DateTime(nextMonthDateTime.getYear(), nextMonthDateTime.getMonthOfYear(), i + 1, 0, 0, 0);
                DayBean bean = new DayBean();
                bean.setChangeAble(false);
                bean.setCurrentMonth(false);
                bean.setDateTime(dateTime1);
                bean.setType(2);
                dayBeans.add(bean);
            }
        } else {
            //周一开始的
            for (int i = 0; i < firstDayOfWeek - 1; i++) {
                DateTime dateTime1 = new DateTime(lastMonthDateTime.getYear(), lastMonthDateTime.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 2), 0, 0, 0);
                DayBean bean = new DayBean();
                bean.setChangeAble(false);
                bean.setCurrentMonth(false);
                bean.setDateTime(dateTime1);
                bean.setType(2);
                dayBeans.add(bean);

            }
            Log.d("chen", "一开始的时间" + toDayTime.getMonthOfYear());
            for (int i = 0; i < days; i++) {
                DateTime dateTime1 = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), i + 1, 0, 0, 0);
                DayBean bean = new DayBean();
                if (dateTime1.getYear() < toDayTime.getYear() ||
                        dateTime1.getMonthOfYear() < toDayTime.getMonthOfYear() ||
                        (dateTime1.getMonthOfYear() == toDayTime.getMonthOfYear() && dateTime1.getDayOfMonth() <= toDayTime.getDayOfMonth())
                        ) {
                    bean.setChangeAble(false);

                } else {
                    bean.setChangeAble(true);
                }

                DateTime.Property week =  dateTime1.dayOfWeek();
                if (week.get() == 7 || week.get() == 6) {
                    bean.setType(2);
                } else {
                    bean.setType(0);
                }
                bean.setCurrentMonth(true);
                bean.setDateTime(dateTime1);
                bean.setAmXiaban("00:00");
                bean.setAmShangban("00:00");
                bean.setPmShangban("00:00");
                bean.setPmXiaban("00:00");
                bean.setChanged(false);
                dayBeans.add(bean);
            }
            for (int i = 0; i < 7 - endDayOfWeek; i++) {
                DateTime dateTime1 = new DateTime(nextMonthDateTime.getYear(), nextMonthDateTime.getMonthOfYear(), i + 1, 0, 0, 0);
                DayBean bean = new DayBean();
                bean.setChangeAble(false);
                bean.setCurrentMonth(false);
                bean.setDateTime(dateTime1);
                bean.setType(2);
                dayBeans.add(bean);
            }
        }

        nCalendar.dayBeanList = dayBeans;
        return nCalendar;

    }

    //格式化的日期
    public static class NCalendar {
        public List<DayBean> dayBeanList;
    }

    /**
     * dp转px
     *
     * @param context
     * @param
     * @return
     */
    public static float dp2px(Context context, int dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static float sp2px(Context context, float spVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * 获得两个日期距离几个月
     *
     * @return
     */
    public static int getIntervalMonths(DateTime dateTime1, DateTime dateTime2) {
        dateTime1 = dateTime1.withDayOfMonth(1).withTimeAtStartOfDay();
        dateTime2 = dateTime2.withDayOfMonth(1).withTimeAtStartOfDay();

        return Months.monthsBetween(dateTime1, dateTime2).getMonths();
    }

    //是否同月
    public static boolean isEqualsMonth(DateTime dateTime1, DateTime dateTime2) {
        return dateTime1.getYear() == dateTime2.getYear() && dateTime1.getMonthOfYear() == dateTime2.getMonthOfYear();
    }
}
