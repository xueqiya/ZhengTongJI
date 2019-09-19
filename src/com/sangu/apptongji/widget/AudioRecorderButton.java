package com.sangu.apptongji.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;


/**
 * Created by Administrator on 2018-01-22.
 */

public class AudioRecorderButton extends Button {

    /**
     * AudioRecorderButton的三个状态
     */
    public static final int STATE_DOWN = 1;
    public static final int STATE_MOVE = 2;
    public static final int STATE_UP = 3;
    private OnClickStateListstener liststener;

    //构造方法
    public AudioRecorderButton(Context context) {
        super(context, null);
    }
    public AudioRecorderButton(final Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * 复写onTouchEvent
     * @see android.widget.TextView#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();   //获取当前Action
        int x = (int) event.getX();       //获取当前的坐标
        int y = (int) event.getY();

        switch (action) {

            case MotionEvent.ACTION_DOWN:
                liststener.onStateChange(STATE_DOWN);
                break;

            case MotionEvent.ACTION_MOVE:
                liststener.onStateChange(STATE_MOVE);
                break;

            case MotionEvent.ACTION_UP:
                liststener.onStateChange(STATE_UP);
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setOnClickStateListstener(OnClickStateListstener onClickStateListstener) {
        this.liststener = onClickStateListstener;

    }

    public interface  OnClickStateListstener{
        void onStateChange(int state);
    }



}
