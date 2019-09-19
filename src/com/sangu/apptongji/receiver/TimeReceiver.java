package com.sangu.apptongji.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.maplocation.BaiDuQiyeLocationActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Administrator on 2017-12-08.
 */

public class TimeReceiver extends BroadcastReceiver {
    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("chen", "通知用户");
        long sysTime = System.currentTimeMillis();//获取系统时间
        CharSequence sysTimeStr2 = DateFormat.format("EEEE", sysTime);//时间显示格式
        if ("星期六".equals(sysTimeStr2)||"星期日".equals(sysTimeStr2)){
            return;
        }
        CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);//时间显示格式
        SharedPreferences sp = context.getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
        SharedPreferences sp2 = context.getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        String companyId = sp2.getString("qiyeId","");
        String shangbanTime = sp.getString(companyId+"_qiandao_shb",null);
        String xiabanTime = sp.getString(companyId+"_qiandao_xb",null);
        String contentText = "您距离公司上班签到时间还有十分钟！";
        if (shangbanTime!=null&&xiabanTime!=null){
            String time = sysTimeStr.toString().substring(0,2);
            if (time.equals(xiabanTime.substring(0,2))){
                contentText = "您已到公司下班打卡时间！";
            }
        }

        System.out.println("recevice");
        nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pi = PendingIntent.getActivity(context,0,
                new Intent(context,BaiDuQiyeLocationActivity.class),0);
        Notification mNotification = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle("小贴士")
                .setContentText(contentText)
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.app_logo)
                .setWhen(System.currentTimeMillis())
                .build();
        nm.notify(1044, mNotification);
//        Intent ActIntent = new Intent(context,DialogActivity.class);
//        ActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(ActIntent);
    }

}
