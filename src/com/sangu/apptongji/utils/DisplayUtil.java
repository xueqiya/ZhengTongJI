package com.sangu.apptongji.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * Created by Administrator on 2017-04-11.
 */

public class DisplayUtil {


    public static int getMobileWidth(Activity context) {
        DisplayMetrics dMetrics = new DisplayMetrics();
        context.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        return dMetrics.widthPixels;
    }

    public static int getMobileHeight(Activity context) {
        DisplayMetrics dMetrics = new DisplayMetrics();
        context.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        return dMetrics.heightPixels;
    }

    public static int getStatusHeight(Activity context) {
        return context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }
}
