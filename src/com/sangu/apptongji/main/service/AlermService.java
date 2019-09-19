package com.sangu.apptongji.main.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.sangu.apptongji.receiver.TimeReceiver;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017-12-08.
 */

public class AlermService extends Service {
    private boolean shangbanIsNull = true;
    private boolean xiabanIsNull = true;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Service", "onCreate");
        Log.d("chen", "onCreate");
    }

    /**
     * 开启提醒
     */
    private void startRemind() {
        cancelAlert(this, 1);
        cancelAlert(this, 2);
        //得到日历实例，主要是为了下面的获取时间
        Calendar mCalendar1 = Calendar.getInstance();
        Calendar mCalendar2 = Calendar.getInstance();
        setCalendar(mCalendar1, 0);
        setCalendar(mCalendar2, 1);
        Intent intent = new Intent("ELITOR_CLOCK");
        //Intent intent = new Intent(this, TimeReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, 0);
        PendingIntent pi2 = PendingIntent.getBroadcast(this, 2, intent, 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        AlarmManager am2 = (AlarmManager) getSystemService(ALARM_SERVICE);

        //**********注意！！下面的两个根据实际需求任选其一即可*********

        /**
         * 单次提醒
         * mCalendar.getTimeInMillis() 上面设置的13点25分的时间点毫秒值
         */
        if (!shangbanIsNull) {
           // Log.d("chen", "开始设置闹铃");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                am.setExact(AlarmManager.RTC_WAKEUP, mCalendar1.getTimeInMillis(), pi);
            } else {
                am.set(AlarmManager.RTC_WAKEUP, mCalendar1.getTimeInMillis(), pi);
            }
            // am.set(AlarmManager.RTC_WAKEUP, mCalen        dar1.getTimeInMillis(), pi);
        }
        if (!xiabanIsNull) {
            //am2.set(AlarmManager.RTC_WAKEUP, mCalendar2.getTimeInMillis(), pi2);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                am2.setExact(AlarmManager.RTC_WAKEUP, mCalendar2.getTimeInMillis(), pi2);
            } else {
                am2.set(AlarmManager.RTC_WAKEUP, mCalendar2.getTimeInMillis(), pi2);
            }
        }
        /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000 * 10); // sleep 1000ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d("chen", "我在运行");
                }
            }

        });
        thread.start();*/
    }

    public static void cancelAlert(Context context, int type) {
//        Log.e("<<<<<<<<<<<<<<<<<", "cancelAlert");
        AlarmManager mAlarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeReceiver.class);
        intent.putExtra("type", type);
        PendingIntent pi = PendingIntent.getBroadcast(context, type, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager.cancel(pi);
    }

    private void setCalendar(Calendar mCalendar1, int type) {
        SharedPreferences sp = getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
        SharedPreferences sp2 = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        mCalendar1.setTimeInMillis(System.currentTimeMillis());
        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();
        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar1.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar1.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String companyId = sp2.getString("qiyeId", "");
        if (type == 0) {
            String shangbanTime = sp.getString(companyId + "_qiandao_shb", null);
            //String shangbanTime = "16:20";
           // Log.d("chen", "上班时间" + shangbanTime);
            if (shangbanTime != null) {
                int shbTime1 = Integer.parseInt(shangbanTime.substring(0, 2));
                int shbTime2 = Integer.parseInt(shangbanTime.substring(3, 5));
                if (shbTime2 >= 10) {
                    shbTime2 = shbTime2 - 10;
                } else {
                    shbTime1 = shbTime1 - 1;
                    shbTime2 = shbTime2 + 50;
                }
                //Log.d("chen", "设定上班时间" + shbTime1 + ":" + shbTime2);
                //设置在几点提醒  设置的为shbTime1点
                mCalendar1.set(Calendar.HOUR_OF_DAY, shbTime1);
                //设置在几分提醒  设置的为shbTime2分
                mCalendar1.set(Calendar.MINUTE, shbTime2);
                //下面这两个看字面意思也知道
                mCalendar1.set(Calendar.SECOND, 0);
                mCalendar1.set(Calendar.MILLISECOND, 0);
                shangbanIsNull = false;
            } else {
                shangbanIsNull = true;
            }
            //上面设置的就是xx点xx分的时间点
        } else {
            String xiabanTime = sp.getString(companyId + "_qiandao_xb", null);
            if (xiabanTime != null) {
                int xbTime1 = Integer.parseInt(xiabanTime.substring(0, 2));
                int xbTime2 = Integer.parseInt(xiabanTime.substring(3, 5));
                mCalendar1.set(Calendar.HOUR_OF_DAY, xbTime1);
                mCalendar1.set(Calendar.MINUTE, xbTime2);
                mCalendar1.set(Calendar.SECOND, 0);
                mCalendar1.set(Calendar.MILLISECOND, 0);
                xiabanIsNull = false;
            } else {
                xiabanIsNull = true;
            }
        }
        //获取上面设置的时间的毫秒值
        long selectTime = mCalendar1.getTimeInMillis();

        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if (systemTime > selectTime) {
            mCalendar1.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    @Override
    public void onDestroy() {
        Log.d("chen", "onDestroy");
        super.onDestroy();
        System.out.println("服务关闭");
        Intent startAlarmServiceIntent = new Intent(this, AlermService.class);
        startService(startAlarmServiceIntent);
    }


    @SuppressWarnings("deprecation")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service", "onStart");
        Log.d("chen", "onStartCommand");
        startRemind();
        return super.onStartCommand(intent, flags, startId);
    }
}
