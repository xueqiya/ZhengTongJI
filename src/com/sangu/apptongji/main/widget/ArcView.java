package com.sangu.apptongji.main.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

import java.util.List;

/**
 * Created by Administrator on 2017-08-14.
 */

public class ArcView extends BaseView {

    private Paint mPaint;//扇形的画笔
    private Paint mTextPaint;//扇形的画笔
    private int centerX, centerY;//中心坐标
    double angles1=0;
    double angles2=0;
    double angles3=0;
    double angles4=0;
    double endles1=0;
    double endles2=0;
    double endles3=0;
    double endles4=0;

    private int maxNum;//扇形图的最大块数 超过的item就合并到其他

    double total;//数据的总和
    double[] datas = new double[]{};//数据集

    //颜色 默认的颜色
    private int[] mColors = {
            Color.RED, Color.rgb(255,127,0),
            Color.rgb(234,121,219), Color.rgb(62,197,255)
    };

    private int radius = 100;//半径


    @Override
    protected void onDrawSub(Canvas canvas) {
        RectF rect;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            radius = 80;
            centerX = radius + 20;
            centerY = radius + 20;
            rect = new RectF(20f, 30f, 180f, 190f);
        }else {
            radius = 140;
            centerX = radius + 20;
            centerY = radius + 40;
            rect = new RectF(20f, 40f, 300f, 320f);
        }

        int start = 0;
        for (int i = 0; i < (maxNum < datas.length ? maxNum : datas.length); i++) {
            if (i==0){
                mPaint.setColor(mColors[0 % mColors.length]);
                endles1 = ((datas[0] * 1.0f / total) * 360);
                canvas.drawArc(rect, start, (float) angles1, true, mPaint);
                start += endles1;
            }else if (i==1){
                mPaint.setColor(mColors[1 % mColors.length]);
                endles2 = ((datas[1] * 1.0f / total) * 360);
                canvas.drawArc(rect, start, (float) angles2, true, mPaint);
                start += endles2;
            }else if (i==2){
                mPaint.setColor(mColors[2 % mColors.length]);
                endles3 = ((datas[2] * 1.0f / total) * 360);
                canvas.drawArc(rect, start, (float) angles3, true, mPaint);
                start += endles3;
            }else{
                mPaint.setColor(mColors[3 % mColors.length]);
                endles4 = ((datas[3] * 1.0f / total) * 360);
                canvas.drawArc(rect, start, (float) angles4, true, mPaint);
                start += endles4;
            }
        }

        int start2 = 0;
        canvas.translate(centerX, centerY);//平移画布到中心
        mPaint.setStrokeWidth(4);
        for (int i = 0; i < (maxNum < datas.length ? maxNum : datas.length); i++) {
            float angles = (float) ((datas[i] * 1.0f / total) * 360);
            drawLine(canvas, start2, angles, mColors[i % mColors.length]);
            start2 += angles;
        }
    }

    private void drawLine(Canvas canvas, int start, float angles, int color) {
        mPaint.setColor(color);
        float stopX, stopY;
        stopX = (float) ((radius + 40) * Math.cos((2 * start + angles) / 2 * Math.PI / 180));
        stopY = (float) ((radius + 40) * Math.sin((2 * start + angles) / 2 * Math.PI / 180));

        //测量百分比大小
        Rect rect = new Rect();
        String percentage = angles / 3.60 + "";
        percentage = percentage.substring(0, percentage.length() > 4 ? 4 : percentage.length()) + "%";
        mTextPaint.getTextBounds(percentage, 0, percentage.length(), rect);
        int w = rect.width() - 10;
        //画百分比
        if ((angles/3.60)>10) {
            canvas.drawText(percentage, 0, percentage.length(), (stopX - w) / 2, stopY>0?(stopY-30)/2:(stopY+30)/2, mTextPaint);
        }
    }

    @Override
    protected void logic() {
        if (datas.length>0&&angles1<=endles1) {
            angles1 += (datas[0]/total)*30;
        }
        if (datas.length>1&&angles2<=endles2) {
            angles2 += (datas[1]/total)*30;
        }
        if (datas.length>2&&angles3<=endles3) {
            angles3 += (datas[2]/total)*30;
        }
        if (datas.length>3&&angles4<=endles4) {
            angles4 += (datas[3]/total)*30;
        }
    }

    public ArcView(Context context) {
        super(context);
        init();
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //初始化
    private void init() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            mPaint=new Paint();
            mPaint.setTextSize(60);

            mTextPaint = new Paint();
            mTextPaint.setTextSize(15);
            mTextPaint.setStrokeWidth(4);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(Color.WHITE);
        }else {
            mPaint=new Paint();
            mPaint.setTextSize(60);

            mTextPaint = new Paint();
            mTextPaint.setTextSize(30);
            mTextPaint.setStrokeWidth(4);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setColor(Color.WHITE);
        }
    }

    //setter
    public void setColors(int[] mColors) {
        this.mColors = mColors;
        invalidate();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        invalidate();
    }

    public abstract class ArcViewAdapter<T> {

        public void setData(List<T> list) {
            datas = new double[list.size()];
            for (int i = 0; i < list.size(); i++) {
                total += getValue(list.get(i));
                datas[i] = getValue(list.get(i));
            }

        }

        //通过传来的数据集的某个元素  得到具体的数字
        public abstract double getValue(T t);

    }

}
