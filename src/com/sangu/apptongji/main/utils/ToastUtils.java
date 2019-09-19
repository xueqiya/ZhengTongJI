package com.sangu.apptongji.main.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-12-15.
 */

public class ToastUtils {

    private static Toast mToastNormal;

    /**
     * 普通的toast提示
     * */
    public static void showNOrmalToast(Context mContext, String message){
        ToastUtils.cancel();
        mToastNormal = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        mToastNormal.show();
    }
    /**
     *toast取消
     */
    public static void cancel(){
        if(mToastNormal != null){
            mToastNormal.cancel();
            mToastNormal = null;
        }
    }

}
