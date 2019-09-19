package com.sangu.apptongji.main.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.sangu.apptongji.main.widget.LoadingDialog;

public abstract class CoreActivity extends AppCompatActivity {
    public Toast mToast = null;
    private LoadingDialog dialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // 环形加载进度条
    public void showDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (dialog != null) {
            dialog = null;
        }
        dialog = new LoadingDialog(this);
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity从堆栈中移除
    }


    public void disDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(CoreActivity.this, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

}
