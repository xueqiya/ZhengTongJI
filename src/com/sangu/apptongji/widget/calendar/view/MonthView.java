package com.sangu.apptongji.widget.calendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;


import com.sangu.apptongji.widget.calendar.entity.DayBean;
import com.sangu.apptongji.widget.calendar.listener.OnClickMonthViewListener;
import com.sangu.apptongji.widget.calendar.utils.Attrs;
import com.sangu.apptongji.widget.calendar.utils.Utils;

import org.joda.time.DateTime;

/**
 * Created by Administrator on 2018-02-22.
 */

public class MonthView extends CalendarView {
    private int mRowNum;
    private int pager;

    private OnClickMonthViewListener mOnClickMonthViewListener;

    public MonthView(Context context, DayBean dayBean, OnClickMonthViewListener onClickMonthViewListener, int position, DateTime toDayTime) {
        super(context);
        this.pager = position;
        this.mInitialDateTime = dayBean.getDateTime();

        //0周日，1周一
        Utils.NCalendar nCalendar2 = Utils.getMonthCalendar2(dayBean.getDateTime(), Attrs.firstDayOfWeek,toDayTime);
        mOnClickMonthViewListener = onClickMonthViewListener;
        dayBeans = nCalendar2.dayBeanList;
        mRowNum = dayBeans.size() / 7;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        //绘制高度
        mHeight = getDrawHeight();
        mRectList.clear();
        for (int i = 0; i < mRowNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = new Rect(j * mWidth / 7, i * mHeight / mRowNum, j * mWidth / 7 + mWidth / 7, i * mHeight / mRowNum + mHeight / mRowNum);
                mRectList.add(rect);
                DayBean bean = dayBeans.get(i * 7 + j);
                DateTime dateTime = bean.getDateTime();
                Paint.FontMetricsInt fontMetrics = mDayPaint.getFontMetricsInt();

                int baseline;//让6行的第一行和5行的第一行在同一直线上，处理选中第一行的滑动
                if (mRowNum == 5) {
                    baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
                } else {
                    baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2 + (mHeight / 5 - mHeight / 6) / 2;
                }

                //当月和上下月的颜色不同
                if (bean.isCurrentMonth) {
                    //工作
                    if (bean.getType() == 0) {
                        mDayPaint.setColor(mWorkCircleColor);
                        int centerY = mRowNum == 5 ? rect.centerY() : (rect.centerY() + (mHeight / 5 - mHeight / 6) / 2);
                        canvas.drawCircle(rect.centerX(), centerY, mCircleRadius, mDayPaint);
                        mDayPaint.setColor(Color.WHITE);
                        canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX() , baseline - (mCircleRadius / 3), mDayPaint);
                        canvas.drawText(  "班", rect.centerX() , baseline + (mCircleRadius / 3), mDayPaint);

                    //活动
                    } else if (bean.getType() == 1) {
                        mDayPaint.setColor(mActivityCircleColor);
                        int centerY = mRowNum == 5 ? rect.centerY() : (rect.centerY() + (mHeight / 5 - mHeight / 6) / 2);
                        canvas.drawCircle(rect.centerX(), centerY, mCircleRadius, mDayPaint);
                        mDayPaint.setColor(Color.WHITE);
                        canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX() , baseline - (mCircleRadius / 3), mDayPaint);
                        canvas.drawText(  "集", rect.centerX() , baseline + (mCircleRadius / 3), mDayPaint);
                    //假期
                    } else {
                        mDayPaint.setColor(mVacationCircleColor);
                        int centerY = mRowNum == 5 ? rect.centerY() : (rect.centerY() + (mHeight / 5 - mHeight / 6) / 2);
                        canvas.drawCircle(rect.centerX(), centerY, mCircleRadius, mDayPaint);
                        mDayPaint.setColor(Color.WHITE);
                        canvas.drawText(dateTime.getDayOfMonth() + "", rect.centerX() , baseline - (mCircleRadius / 3), mDayPaint);
                        canvas.drawText(  "休", rect.centerX() , baseline + (mCircleRadius / 3), mDayPaint);

                    }

                }
            }
        }
    }

    /**
     * 月日历的绘制高度，
     * 为了月日历6行时，绘制农历不至于太靠下，绘制区域网上压缩一下
     *
     * @return
     */
    public int getDrawHeight() {
        return (int) (getMonthHeight() - Utils.dp2px(getContext(), 10));
    }

    /**
     * 月日历高度
     *
     * @return
     */
    public int getMonthHeight() {
        return Attrs.monthCalendarHeight;
    }

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < mRectList.size(); i++) {
                Rect rect = mRectList.get(i);
                if (rect.contains((int) e.getX(), (int) e.getY())) {
                    if (isCalendarClickable) {
                        mOnClickMonthViewListener.onClickCurrentMonth(pager,i);
                    }
                    break;
                }
            }
            return true;
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    public int getRowNum() {
        return mRowNum;
    }
    public void clear() {
        invalidate();
    }

    public DateTime getInitialDateTime() {
        return mInitialDateTime;
    }

}
