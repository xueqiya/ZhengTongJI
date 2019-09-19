package com.sangu.apptongji.widget.calendar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;


import com.sangu.apptongji.widget.calendar.adapter.CalendarAdapter;
import com.sangu.apptongji.widget.calendar.adapter.MonthAdapter;
import com.sangu.apptongji.widget.calendar.entity.DayBean;
import com.sangu.apptongji.widget.calendar.listener.OnClickMonthViewListener;
import com.sangu.apptongji.widget.calendar.listener.OnMonthCalendarChangedListener;
import com.sangu.apptongji.widget.calendar.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-02-22.
 */

public class MonthCalendar extends CalendarPager implements OnClickMonthViewListener {
    private OnMonthCalendarChangedListener onMonthCalendarChangedListener;
    private OnDayClickListener onDayClickListener;

    private int lastPosition = -1;
    private MonthAdapter adapter;

    public MonthCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected CalendarAdapter getCalendarAdapter() {

        mPageSize = Utils.getIntervalMonths(startDateTime, endDateTime) + 1;
        mCurrPage = Utils.getIntervalMonths(startDateTime, mInitialDateTime);
        adapter = new MonthAdapter(getContext(), mPageSize, mCurrPage, mInitialDateTime, this);
        return adapter;
    }


    @Override
    public void onClickCurrentMonth(int pager , int postion) {
        DayBean dayBean = adapter.getCalendarViews().get(pager).dayBeans.get(postion);
        onDayClickListener.onDayClicked(dayBean);
        adapter.getCalendarViews().get(pager).invalidate();
    }

    @Override
    protected void initCurrentCalendarView(int position) {
        MonthView currView = (MonthView) calendarAdapter.getCalendarViews().get(position);
        MonthView lastView = (MonthView) calendarAdapter.getCalendarViews().get(position - 1);
        MonthView nextView = (MonthView) calendarAdapter.getCalendarViews().get(position + 1);


        if (currView == null) {
            return;
        }

        if (lastView != null)
            lastView.clear();

        if (nextView != null)
            nextView.clear();


        //只处理翻页
        if (lastPosition == -1) {
            if (onMonthCalendarChangedListener != null) {
                onMonthCalendarChangedListener.onMonthCalendarChanged(position,mInitialDateTime);
            }
        } else if (isPagerChanged) {
            int i = position - lastPosition;
            mInitialDateTime = mInitialDateTime.plusMonths(i);
                //日期越界
                if (mInitialDateTime.getMillis() > endDateTime.getMillis()) {
                    mInitialDateTime = endDateTime;
                } else if (mInitialDateTime.getMillis() < startDateTime.getMillis()) {
                    mInitialDateTime = startDateTime;
                }

                if (onMonthCalendarChangedListener != null) {
                    Log.d("chen", "onMonthCalendarChangedListener  position  --- " + position );
                    onMonthCalendarChangedListener.onMonthCalendarChanged(position,mInitialDateTime);
                }

        }
        lastPosition = position;
    }


    public void setOnMonthCalendarChangedListener(OnMonthCalendarChangedListener onMonthCalendarChangedListener) {
        this.onMonthCalendarChangedListener = onMonthCalendarChangedListener;
    }


    public MonthView getCurrectMonthView() {
        return (MonthView) calendarAdapter.getCalendarViews().get(getCurrentItem());
    }


    public void setDataWithInfo(List<DayBean> newList,int pager) {

        int newPosition =0;
        for (DayBean oldBean : adapter.getCalendarViews().get(pager).dayBeans) {
            if (oldBean.isCurrentMonth()) {
                DayBean newBean = newList.get(newPosition);
                oldBean.setType(newBean.getType());
                oldBean.setLat(newBean.getLat());
                oldBean.setLng(newBean.getLng());
                oldBean.setAmShangban(newBean.getAmShangban());
                oldBean.setAmXiaban(newBean.getAmXiaban());
                oldBean.setPmShangban(newBean.getPmShangban());
                oldBean.setPmXiaban(newBean.getPmXiaban());
                oldBean.setLocation(newBean.getLocation());
                oldBean.setChanged(false);
                newPosition++;
            }
        }
        adapter.getCalendarViews().get(pager).invalidate();
    }

    public void setOnDayClickListener(OnDayClickListener onDayClickListener) {
        this.onDayClickListener = onDayClickListener;

    }
    public interface OnDayClickListener{
        void onDayClicked(DayBean dayBean);
    }

    public List<DayBean> getPreMonthDayBean(int pager) {
        //剔除上个月的个下个月的数据
        List<DayBean> list = new ArrayList<>();
        for (DayBean bean : adapter.getCalendarViews().get(pager).dayBeans) {
            if (bean.isCurrentMonth) {
                list.add(bean);
            }
        }
        return list;
    }
    public List<DayBean> getMonthDayBean(int pager) {
        return adapter.getCalendarViews().get(pager).dayBeans;
    }

    public void reflashCurrentPager(int pager) {
        adapter.getCalendarViews().get(pager).invalidate();
    }

}
