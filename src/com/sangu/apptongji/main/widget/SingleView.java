package com.sangu.apptongji.main.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.utils.SingleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-09-28.
 */

public class SingleView extends View{
    private Paint mPaint, mChartPaint,mPaint2;
    private Rect mBound;
    private int mStartWidth, mHeight, mWidth, mChartWidth, mSize,mStartWidth2;
    private int lineColor, leftColor, lefrColorBottom,selectLeftColor;
    private List<SingleBean> list = new ArrayList<>();
    private getNumberListener listener;
    private ItemOnclickListener listener1;
    private List<Integer> selectIndexRoles = new ArrayList<>();

    public void setList(List<SingleBean> list) {
        this.list = list;
        mSize = getWidth() / 11;
        mStartWidth = getWidth() / 6;
        mStartWidth2 = getWidth() / 6;
        mChartWidth = getWidth() / 6 - mSize / 2;
        invalidate();
    }

    public SingleView(Context context) {
        this(context, null);
    }

    public SingleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyChartView, defStyleAttr, 0);
        int n = array.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.MyChartView_xyColor:
                    lineColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyChartView_leftColor:
                    // 默认颜色设置为黑色
                    leftColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyChartView_leftColorBottom:
                    lefrColorBottom = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.MyChartView_selectLeftColor:
                    // 默认颜色设置为黑色
                    selectLeftColor = array.getColor(attr, Color.BLACK);
                    break;
                default:
                    bringToFront();
            }
        }
        array.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = widthSize * 1 / 2;
        }
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = heightSize * 1 / 2;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mStartWidth = getWidth() / 6;
        mStartWidth2 = getWidth() / 6;
        mSize = mWidth / 11;
        mChartWidth = getWidth() / 6 - mSize / 2;
    }

    private void init() {
        mPaint = new Paint();
        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint.setAntiAlias(true);
        mBound = new Rect();
        mChartPaint = new Paint();
        mChartPaint.setAntiAlias(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {

        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            mSize = getWidth() / 11;
            mStartWidth = getWidth() / 6;
            mStartWidth2 = getWidth() / 6;
            mChartWidth = getWidth() / 6 - mSize / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(lineColor);
        mPaint2.setColor(lineColor);

        for (int i = 0; i < list.size(); i++) {
            //画数字
            mPaint.setTextSize(35);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.getTextBounds(String.valueOf(i + 1) + "", 0, String.valueOf(i).length(), mBound);
            canvas.drawText(String.valueOf(i + 1) + "", mStartWidth - mBound.width() * 1 / 2-15,
                    mHeight - 60 + mBound.height() * 1 / 2, mPaint);
            mStartWidth += getWidth() / 6;
        }

        for (int i = 0; i < list.size(); i++) {
            int size = mHeight / 120;
            mChartPaint.setStyle(Paint.Style.FILL);
            if (list.size() > 0) {
                if (selectIndexRoles.contains(i)){
                    mChartPaint.setShader(null);
                    mChartPaint.setColor(selectLeftColor);
                }
                else {
                    LinearGradient lg = new LinearGradient(mChartWidth, mChartWidth + mSize, mHeight - 100,
                            (float) (mHeight - 100 - list.get(i).getCount() * size), lefrColorBottom, leftColor, Shader.TileMode.MIRROR);
                    mChartPaint.setShader(lg);
                }
                //画柱状图
                RectF rectF = new RectF();
                rectF.left = mChartWidth;
                rectF.right = mChartWidth + mSize/2;
                rectF.bottom = mHeight - 100;
                rectF.top = mHeight - 100 - list.get(i).getCount() * size;
                canvas.drawRoundRect(rectF, 0, 0, mChartPaint);
                mChartWidth += getWidth() / 6;
            }
        }
        List<SingleBean> datas = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int size = mHeight / 120;
            SingleBean bean = new SingleBean();
            bean.setX(mStartWidth2 - mBound.width() * 1 / 2-15);
            bean.setY((int)(mHeight - 100 - list.get(i).getCount() * size - 20));
            datas.add(bean);
            mStartWidth2 += getWidth() / 6;
            if (i==list.size()-1){
                listener.getNumber(datas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int left = 0;
        int top = 300;
        int right = mWidth / 6;
        int bottom = mHeight;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < list.size(); i++) {
                    Rect rect = new Rect(left, top, right, bottom);
                    left += mWidth / 6;
                    right += mWidth / 6;
                    if (rect.contains(x, y)) {
                        if (listener1 != null){
                            listener1.ItemOnclick(i);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return false;
    }

    public void setListener(getNumberListener listener) {
        this.listener = listener;
    }

    public interface getNumberListener {
        void getNumber(List<SingleBean> beanList);
    }
    public void setItemOneListener(ItemOnclickListener listener) {
        this.listener1 = listener;
    }

    public interface ItemOnclickListener {
        void ItemOnclick(int position);
    }
}
