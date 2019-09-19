package com.sangu.apptongji.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.fanxin.easeui.ui.EaseBaseActivity;
import com.sangu.apptongji.main.utils.ToastUtils;


public class BaseActivity extends EaseBaseActivity {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        mContext = BaseActivity.this;
//        DemoApplication.getInstance().saveActivity(this);
    }

    /**
     * 处理onStop方法,
     * */
    @Override
    protected void onStop() {
        super.onStop();
        ToastUtils.cancel();
    }


    /**
     * 处理onPause方法
     * */
    @Override
    protected void onPause() {
        super.onPause();
        ToastUtils.cancel();
    }

    /**
     * 处理onDestory方法
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtils.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void back(View view) {
        finish();
//        finishAfterTransition();
    }

}
