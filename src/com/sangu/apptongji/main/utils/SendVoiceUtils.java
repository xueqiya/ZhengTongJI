package com.sangu.apptongji.main.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.widget.Toast;

import com.fanxin.easeui.model.EaseVoiceRecorder;
import com.fanxin.easeui.utils.EaseCommonUtils;

/**
 * Created by Administrator on 2017-10-17.
 */

public class SendVoiceUtils {
    protected EaseVoiceRecorder voiceRecorder;
    protected PowerManager.WakeLock wakeLock;
    private Context context;

    public SendVoiceUtils(Context context, Handler Myhandle) {
        this.context = context;
        voiceRecorder = new EaseVoiceRecorder(Myhandle);
        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
    }

    public void startRecording() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(context, com.hyphenate.easeui.R.string.Send_voice_need_sdcard_support, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            wakeLock.acquire();
            voiceRecorder.startRecording(context);
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld())
                wakeLock.release();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            Toast.makeText(context, com.hyphenate.easeui.R.string.recoding_fail, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void discardRecording() {
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // stop recording
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
            }
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        if (wakeLock.isHeld())
            wakeLock.release();
        return voiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

    /**
     * 开始动画
     */
    public void start(View[] mViewWrapper,int j) {
        if (mViewWrapper == null || mViewWrapper.length <= 0) {
            return;
        }
        for (int i = 0; i < mViewWrapper.length; i++) {
            startAnimator(mViewWrapper[i], j);
        }
    }

    /**
     * 停止动画
     */
    public void stop(View[] mViewWrapper) {
        startAnimator(mViewWrapper[0], 200);
        startAnimator(mViewWrapper[1], 100);
        startAnimator(mViewWrapper[2], 50);
        startAnimator(mViewWrapper[3], 100);
        startAnimator(mViewWrapper[4], 200);
    }

    private void startAnimator(View viewWrapper, int height) {
        viewWrapper.clearAnimation();
        ObjectAnimator.ofInt(viewWrapper, "height", height).setDuration(300).start();
    }

}
