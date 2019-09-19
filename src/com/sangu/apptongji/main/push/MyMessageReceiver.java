package com.sangu.apptongji.main.push;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.main.activity.NewFriendsActivity;
import com.sangu.apptongji.main.activity.PaidanListActivity;
import com.sangu.apptongji.main.activity.TouMingActivity;
import com.sangu.apptongji.main.activity.TradeTrackActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.ConsumeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.DanjuListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.DingdanActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.MerDetailListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.SelfYuEActivity;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.qiye.BaobiaoListActivity;
import com.sangu.apptongji.main.qiye.NewMajorActivity;
import com.sangu.apptongji.main.qiye.QingjiaListActivity;
import com.sangu.apptongji.main.qiye.QiyePaidanListActivity;
import com.sangu.apptongji.main.service.AlermService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: 正纬
 * @since: 15/4/9
 * @version: 1.1
 * @feature: 用于接收推送的通知和消息
 */
public class MyMessageReceiver extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    private static final String REC_TAG = "receiver,";
    private AlertDialog dialog2;

    /**
     * 推送通知的回调方法
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotification(final Context context, String title, String summary, final Map<String, String> extraMap) {
        Log.d("chen", "sssssss");
        Log.d("chen", title + summary + extraMap);
        // TODO 处理推送通知
        if (null != extraMap) {
            for (Map.Entry<String, String> entry : extraMap.entrySet()) {
                Log.i(REC_TAG, "@Get diy param : Key=" + entry.getKey() + " , Value=" + entry.getValue());
            }
        } else {
            Log.i(REC_TAG, "@收到通知 && 自定义消息为空");
        }
        Log.d("chen", "onNotification" + extraMap);
        //对type == 16进行特殊对待
        /*if (extraMap.get("type").equalsIgnoreCase("16")) {
            if (isAppAlive(context, "com.sangu.app")) {
                if (getRunningActivityName().contains("PaidanListActivity")) {
                    String companyName = extraMap.get("companyName");
                    Intent intent=new Intent();
                    intent.putExtra("type", "0");
                    intent.putExtra("companyName", companyName);
                    intent.putExtra("pushType", "16");
                    intent.setAction("com.broadcast.reflash");
                    context.sendBroadcast(intent);
                    ToastUtils.showNOrmalToast(context.getApplicationContext(),"您有新的订单请你查看");
                } else if (getRunningActivityName().contains("DialogActivity")) {
                    String companyName = extraMap.get("companyName");
                    Intent intent=new Intent();
                    intent.putExtra("companyName", companyName);
                    intent.putExtra("pushType", "16");
                    intent.setAction("com.broadcast.reflashPush");
                    context.sendBroadcast(intent);
                } else {
                    Intent intent2 = new Intent(context, DialogActivity.class);
                    String companyName = extraMap.get("companyName");
                    intent2.putExtra("companyName", companyName);
                    intent2.putExtra("pushType", "16");
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent2);
                }

            }
        } else if (extraMap.get("type").equalsIgnoreCase("19")) {
            if (isAppAlive(context, "com.sangu.app")) {
                if (getRunningActivityName().contains("PaidanListActivity")) {
                    String companyName = extraMap.get("companyId");
                    Intent intent=new Intent();
                    intent.putExtra("type", "0");
                    intent.putExtra("companyName", companyName);
                    intent.putExtra("pushType", "19");
                    intent.setAction("com.broadcast.reflash");
                    context.sendBroadcast(intent);
                    ToastUtils.showNOrmalToast(context.getApplicationContext(),"您有新的订单请你查看");
                } else if (getRunningActivityName().contains("DialogActivity")) {
                    String companyName = extraMap.get("companyId");
                    Intent intent=new Intent();
                    intent.putExtra("companyName", companyName);
                    intent.putExtra("pushType", "19");
                    intent.setAction("com.broadcast.reflashPush");
                    context.sendBroadcast(intent);
                } else {
                    Intent intent2 = new Intent(context, DialogActivity.class);
                    String companyName = extraMap.get("companyId");
                    intent2.putExtra("companyName", companyName);
                    intent2.putExtra("pushType", "19");
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent2);
                }

            }
        }*/
        //Log.i(REC_TAG, "收到一条推送通知 ： " + title + ", summary:" + summary);
//       MainApplication.setConsoleText("收到一条推送通知 ： " + title + ", summary:" + summary);
    }

    /**
     * 应用处于前台时通知到达回调。注意:该方法仅对自定义样式通知有效,相关详情请参考https://help.aliyun.com/document_detail/30066.html?spm=5176.product30047.6.620.wjcC87#h3-3-4-basiccustompushnotification-api
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     * @param openType
     * @param openActivity
     * @param openUrl
     */
    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.i(REC_TAG, "onNotificationReceivedInApp ： " + " : " + title + " : " + summary + "  " + extraMap + " : " + openType + " : " + openActivity + " : " + openUrl);
//        MainApplication.setConsoleText("onNotificationReceivedInApp ： " + " : " + title + " : " + summary);
    }

    /**
     * 推送消息的回调方法
     *
     * @param context
     * @param cPushMessage
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.d("chen", "推送消息的回调方法");
        Log.i(REC_TAG, "收到一条推送消息 ： " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
//        MainApplication.setConsoleText(cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
//        buildNotification(context, cPushMessage);
    }

//    public void buildNotification(Context context, CPushMessage message) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.list_item_push);
//        remoteViews.setImageViewResource(R.id.iv_head, R.drawable.ic_launcher);
//        remoteViews.setTextViewText(R.id.tv__xiangmu_name, message.getTitle());
//        remoteViews.setTextViewText(R.id.tv_balance, message.getContent());
//        remoteViews.setTextViewText(R.id.tv_jiaoyishijian, new SimpleDateFormat("HH:mm").format(new Date()));
//        Notification notification = new NotificationCompat.Builder(context)
//                .setContent(remoteViews)
//                .setContentTitle(message.getTitle())
//                .setContentText(message.getContent())
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setPriority(Notification.PRIORITY_DEFAULT)
//                .build();
//        notification.contentIntent = buildClickContent(context, message);
//        notification.deleteIntent = buildDeleteContent(context, message);
//        notificationManager.notify(message.hashCode(), notification);
//    }
//
//    public PendingIntent buildClickContent(Context context, CPushMessage message) {
//        Intent clickIntent = new Intent();
//        clickIntent.setAction("com.sangu.app.notificationclicked");
//        //添加其他数据
//        clickIntent.putExtra("message_key",  message);//将message放入intent中，方便通知自建通知的点击事件
//        return PendingIntent.getService(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }
//    public PendingIntent buildDeleteContent(Context context, CPushMessage message) {
//        Intent deleteIntent = new Intent();
//        deleteIntent.setAction("com.sangu.app.notificationdeleted");
//        //添加其他数据
//        deleteIntent.putExtra("message_key",  message);//将message放入intent中，方便通知自建通知的点击事件
//        return PendingIntent.getService(context, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }

    /**
     * 从通知栏打开通知的扩展处理
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.d("chen", "我被点击了" + extraMap);
        Log.i(REC_TAG, "onNotificationOpened ： " + " : " + title + " : " + summary + " : " + extraMap);
        if (isAppAlive(context, "com.sangu.app")) {
            Log.d("chen", "onNotificationOpened已打开" + extraMap);
            JSONObject object = JSON.parseObject(extraMap);
            String type = object.getString("type");
            Intent intent2 = new Intent();
            if ("000".equals(type)) {
                intent2 = new Intent(context, DingdanActivity.class).putExtra("biaoshi", "01");
            } else if ("001".equals(type)) {
                intent2 = new Intent(context, ConsumeActivity.class).putExtra("biaoshi", "01");
            } else if ("002".equals(type)) {
                intent2 = new Intent(context, DingdanActivity.class).putExtra("biaoshi", "00");
            } else if ("02".equals(type)) {
                intent2 = new Intent(context, NewFriendsActivity.class);
            } else if ("03".equals(type)) {
                String companyName = object.getString("companyName");
                String comAddress = object.getString("companyAdress");
                intent2 = new Intent(context, NewMajorActivity.class).putExtra("companyName", companyName).putExtra("comAddress", comAddress);
            } else if ("04".equals(type)) {
                intent2 = new Intent(context, DingdanActivity.class).putExtra("biaoshi", "01");
            } else if ("05".equals(type)) {
                intent2 = new Intent(context, QiyePaidanListActivity.class).putExtra("biaoshi", "00").putExtra("isQunzhu", "00");
            } else if ("06".equals(type)) {
                intent2 = new Intent(context, QingjiaListActivity.class).putExtra("biaoshi", "01");
            } else if ("07".equals(type)) {
                intent2 = new Intent(context, BaobiaoListActivity.class).putExtra("biaoshi", "01");
            } else if ("08".equals(type)) {
                intent2 = new Intent(context, SelfYuEActivity.class).putExtra("biaoshi", "000");
            } else if ("09".equals(type) || "21".equals(type)) {
                String dynamicSeq = object.getString("dynamicSeq");
                String createTime = object.getString("createTime");
                String dType = object.getString("dType");
                intent2 = new Intent(context, DynaDetaActivity.class).putExtra("pushType", type).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime).putExtra("dType", dType);
            } else if ("10".equals(type)) {
                String dynamicSeq = object.getString("dynamicSeq");
                String createTime = object.getString("createTime");
                intent2 = new Intent(context, DynaDetaActivity.class).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime)
                        .putExtra("dType", "05").putExtra("sID", DemoHelper.getInstance().getCurrentUsernName());
            } else if ("11".equals(type)) {
                intent2 = new Intent(context, DanjuListActivity.class).putExtra("biaoshi", "单据");
            } else if ("12".equals(type)) {
                intent2 = new Intent(context, DanjuListActivity.class).putExtra("biaoshi", "订单");
            } else if ("13".equals(type)) {
                /*String task_label = object.getString("dynamicSeq");
                intent2 = new Intent(context, PushDynaActivity.class).putExtra("task_label", task_label);*/
                String task_label = object.getString("dynamicSeq");
                intent2 = new Intent(context, PaidanListActivity.class).putExtra("task_label", task_label).putExtra("type", "0").putExtra("pushType", 13);
            } else if ("14".equals(type)) {
                String companyId = object.getString("companyId");
                String companyName = object.getString("companyName");
                String shangbanTime = companyName.split(".")[0];
                String xiabanTime = companyName.split(".")[1];
                SharedPreferences sp = context.getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(companyId + "_qiandao_shb", shangbanTime);
                editor.putString(companyId + "_qiandao_xb", xiabanTime);
                editor.commit();
                context.startService(new Intent(context, AlermService.class));
            } else if ("15".equals(type)) {
                SharedPreferences mSharedPreference = context.getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
                SharedPreferences.Editor meditor = mSharedPreference.edit();
                meditor.putString("fenXiangTime", getNowTime3());
                meditor.commit();
                String companyName = object.getString("companyName");
                intent2 = new Intent(context, TouMingActivity.class).putExtra("companyName", companyName);
            }else if ("16".equals(type)) {
                //进入派单界面
                intent2 = new Intent(context, PaidanListActivity.class);
                String companyName = object.getString("companyName");
                intent2.putExtra("pushType", 16);
                intent2.putExtra("companyName", companyName);
            }else if ("17".equals(type)) {
                //进入派单界面
                intent2 = new Intent(context, TradeTrackActivity.class);
                String companyName = object.getString("companyName");
                //只要不为空就表明删除推送
                intent2.putExtra("companyName", "123");
            } else if ("18".equals(type)) {
                //进入派单界面
                intent2 = new Intent(context, PaidanListActivity.class);
                intent2.putExtra("type", 1);
                intent2.putExtra("pushType", 18);
            }  else if ("19".equals(type)) {
                intent2 = new Intent(context, PaidanListActivity.class);
                String companyName = object.getString("companyId");
                intent2.putExtra("pushType", 19);
                intent2.putExtra("companyName", companyName);
            }else if ("20".equals(type)) { //给发需求的用户推荐附近的师傅

                String dynamicSeq = object.getString("dynamicSeq");
                String createTime = object.getString("createTime");
                intent2 = new Intent(context, DynaDetaActivity.class).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime)
                        .putExtra("dType", "05").putExtra("pushType", "20").putExtra("sID", DemoHelper.getInstance().getCurrentUsernName());

            }else if ("22".equals(type)) {
                //由用户对评论做出了奖励  直接跳转余额明细

                String dynamicSeq = object.getString("dynamicSeq");
                String createTime = object.getString("createTime");
                intent2 = new Intent(context, MerDetailListActivity.class).putExtra("pushType", "22").putExtra("biaoshi","000");

            }/*else if ("16".equals(type)) {
                //进入派单界面
                intent2 = new Intent(context, PaidanListActivity.class);
                String companyName = object.getString("companyName");
                intent2.putExtra("pushType", 16);
                intent2.putExtra("companyName", companyName);
            }else if ("17".equals(type)) {
                //进入派单界面
                intent2 = new Intent(context, TradeTrackActivity.class);
                String companyName = object.getString("companyName");
                intent2.putExtra("companyName", companyName);
            } else if ("18".equals(type)) {
                //进入派单界面
                intent2 = new Intent(context, PaidanListActivity.class);
                intent2.putExtra("type", 1);
                intent2.putExtra("pushType", 18);
            } else if ("19".equals(type)) {
                intent2 = new Intent(context, PaidanListActivity.class);
                String companyName = object.getString("companyId");
                intent2.putExtra("pushType", 19);
                intent2.putExtra("companyName", companyName);
            }*/

            try {
                context.startActivity(intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.e("mymessagerec", "未打开");
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage("com.sangu.app");
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Bundle args = new Bundle();
            args.putString("extraMap", extraMap);
            launchIntent.putExtra("launchBundle", args);
            context.startActivity(launchIntent);
        }
    }

    private String getNowTime3() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date);
    }

    private boolean isAppAlive(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通知删除回调
     *
     * @param context
     * @param messageId
     */
    @Override
    public void onNotificationRemoved(Context context, String messageId) {
        Log.d("chen", "onNotificationRemoved");
        Log.i(REC_TAG, "onNotificationRemoved ： " + messageId);
//        MainApplication.setConsoleText("onNotificationRemoved ： " + messageId);
    }

    /**
     * 无动作通知点击回调。当在后台或阿里云控制台指定的通知动作为无逻辑跳转时,通知点击回调为onNotificationClickedWithNoAction而不是onNotificationOpened
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.d("chen", "onNotificationClickedWithNoAction");
        Log.i(REC_TAG, "onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
//        MainApplication.setConsoleText("onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary + " : " + extraMap);
    }

    private String getRunningActivityName(){
        ActivityManager activityManager=(ActivityManager) DemoApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }
}