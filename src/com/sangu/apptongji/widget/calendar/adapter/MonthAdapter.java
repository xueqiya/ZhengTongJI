package com.sangu.apptongji.widget.calendar.adapter;

import android.content.Context;
import android.view.ViewGroup;


import com.sangu.apptongji.widget.calendar.entity.DayBean;
import com.sangu.apptongji.widget.calendar.listener.OnClickMonthViewListener;
import com.sangu.apptongji.widget.calendar.view.MonthView;

import org.joda.time.DateTime;

/**
 * Created by Administrator on 2018-02-22.
 */

public class MonthAdapter extends CalendarAdapter{
    private OnClickMonthViewListener mOnClickMonthViewListener;

    public MonthAdapter(Context mContext, int count, int curr, DateTime dateTime, OnClickMonthViewListener onClickMonthViewListener) {
        super(mContext, count, curr, dateTime);
        this.mOnClickMonthViewListener = onClickMonthViewListener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        MonthView nMonthView = (MonthView) mCalendarViews.get(position);
        if (nMonthView == null) {
            int i = position - mCurr;
            DateTime dateTime = this.mDateTime.plusMonths(i);
            DayBean bean = new DayBean();
            bean.setDateTime(dateTime);
            nMonthView = new MonthView(mContext, bean, mOnClickMonthViewListener,position,mDateTime);
            mCalendarViews.put(position, nMonthView);
        }
        container.addView(nMonthView);
        return nMonthView;
    }

}
