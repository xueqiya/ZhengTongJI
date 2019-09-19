package com.sangu.apptongji.widget.calendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;


import com.sangu.apptongji.R;
import com.sangu.apptongji.widget.calendar.adapter.CalendarAdapter;
import com.sangu.apptongji.widget.calendar.utils.Attrs;
import com.sangu.apptongji.widget.calendar.utils.Utils;

import org.joda.time.DateTime;

/**
 * Created by Administrator on 2018-02-22.
 */

public abstract class CalendarPager extends ViewPager {
    protected CalendarAdapter calendarAdapter;
    protected DateTime startDateTime;
    protected DateTime endDateTime;
    protected int mPageSize;
    protected int mCurrPage;
    protected DateTime mInitialDateTime;//日历初始化datetime，即今天

    protected boolean isPagerChanged = true;//是否是手动翻页
    protected boolean isDefaultSelect = true;//是否默认选中

    protected boolean isCalendarClickable = false;

    private OnPageChangeListener onPageChangeListener;

    public CalendarPager(Context context) {
        this(context, null);
    }

    public CalendarPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CCalendar);
        String firstDayOfWeek = ta.getString(R.styleable.CCalendar_firstDayOfWeek);
        Attrs.monthCalendarHeight = (int) ta.getDimension(R.styleable.CCalendar_monthCalendarHeight, Utils.dp2px(context, 300));

        Attrs.calendarTextColor = ta.getColor(R.styleable.CCalendar_calendarTextColor, getResources().getColor(R.color.textColor));
        Attrs.workCircleColor = ta.getColor(R.styleable.CCalendar_workCircleColor, getResources().getColor(R.color.workCircleColor));
        Attrs.activityCircleColor = ta.getColor(R.styleable.CCalendar_activityCircleColor, getResources().getColor(R.color.activityCircleColor));
        Attrs.vacationCircleColor = ta.getColor(R.styleable.CCalendar_vacationCircleColor, getResources().getColor(R.color.vacationCircleColor));
        Attrs.calendarTextSize = ta.getDimension(R.styleable.CCalendar_calendarTextSize, Utils.sp2px(context, 15));
        Attrs.circleRadius = ta.getDimension(R.styleable.CCalendar_circleRadius, Utils.sp2px(context, 20));
        Attrs.isCalendarClickable = ta.getBoolean(R.styleable.CCalendar_calendarClickable, false);

        Attrs.firstDayOfWeek = "Monday".equals(firstDayOfWeek) ? 1 : 0;

        ta.recycle();

        mInitialDateTime = new DateTime().withTimeAtStartOfDay();

        startDateTime = new DateTime( "1901-01-01");
        endDateTime = new DateTime( "2099-12-31");

        setDateInterval(null, null);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                initCurrentCalendarView(mCurrPage);
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        setBackgroundColor(Color.WHITE);
    }

    public void setDateInterval(String startString,String endString) {
        if (startString != null && !"".equals(startString)) {
            startDateTime = new DateTime(startString);
        }
        if (endString != null && !"".equals(endString)) {
            endDateTime = new DateTime(endString);
        }


        if (mInitialDateTime.getMillis() < startDateTime.getMillis() || mInitialDateTime.getMillis() > endDateTime.getMillis()) {
            throw new RuntimeException(getResources().getString(R.string.range_date));
        }

        calendarAdapter = getCalendarAdapter();
        setAdapter(calendarAdapter);
        setCurrentItem(mCurrPage);


        if (onPageChangeListener != null) {
            removeOnPageChangeListener(onPageChangeListener);
        }

        onPageChangeListener = new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initCurrentCalendarView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        addOnPageChangeListener(onPageChangeListener);

    }
    protected abstract void initCurrentCalendarView(int position);

    protected abstract CalendarAdapter getCalendarAdapter();
    /**
     * 下一页，月日历即是下一月，周日历即是下一周
     */
    public void toNextPager() {
        setCurrentItem(getCurrentItem() + 1, true);
    }

    /**
     * 上一页
     */
    public void toLastPager() {
        setCurrentItem(getCurrentItem() - 1, true);
    }

}
