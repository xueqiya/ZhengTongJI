package com.sangu.apptongji.widget.calendar.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;


import com.sangu.apptongji.widget.calendar.entity.DayBean;
import com.sangu.apptongji.widget.calendar.utils.Attrs;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-02-22.
 */

public class CalendarView extends View {
    protected List<DayBean> dayBeans;
    protected DateTime mInitialDateTime;//初始传入的datetime，
    protected int mTextColor;//字体颜色
    protected int mWorkCircleColor;//工作颜色
    protected int mActivityCircleColor;//活动颜色
    protected int mVacationCircleColor;//假期颜色

    protected float mCircleRadius;//圆的半径
    protected List<Rect> mRectList;//点击用的矩形集合
    protected int mWidth;
    protected int mHeight;

    protected Paint mDayPaint;

    protected float mTextSize;

    protected boolean isCalendarClickable = false;



    public CalendarView(Context context) {
        super(context);
        mTextColor = Attrs.calendarTextColor;
        mWorkCircleColor = Attrs.workCircleColor;
        mActivityCircleColor = Attrs.activityCircleColor;
        mVacationCircleColor = Attrs.vacationCircleColor;
        mCircleRadius = Attrs.circleRadius;
        mTextSize = Attrs.calendarTextSize;
        isCalendarClickable = Attrs.isCalendarClickable;
        mDayPaint = getPaint(mTextColor, mTextSize);
        mRectList = new ArrayList<>();
    }

    private Paint getPaint(int paintColor, float paintSize) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setTextSize(paintSize);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }


}
