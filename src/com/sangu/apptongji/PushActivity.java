package com.sangu.apptongji;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.sangu.apptongji.main.fragment.MainActivity;

import java.util.Map;

import static com.sangu.apptongji.main.utils.OkHttpManager.context;

/**
 * Created by Administrator on 2018-02-08.
 */
//只是一个消息跳转的平台  如果要改这个类的位置就要后台配合你修改默认的通知打开类最好不要动这类可能导致老版本不能用

public class PushActivity extends AndroidPopupActivity {

    @Override
    protected void onSysNoticeOpened(String title, String content, Map<String, String> extraMap) {
        Log.d("chen", "Receive XiaoMi notification, title: " + title + ", content: " + content + ", extraMap: " + extraMap);
        Log.d("chen", "onSysNoticeOpened打开activity" + extraMap + "type" + extraMap.get("type") +"companyName" + extraMap.get("companyName")
                +"companyAdress" + extraMap.get("companyAdress"));
        String msg = JSON.toJSONString(extraMap);
        Intent launchIntent = new Intent(context, MainActivity.class);
        launchIntent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        Bundle args = new Bundle();
        args.putString("extraMap", msg);
        launchIntent.putExtra("launchBundle", args);
        context.startActivity(launchIntent);
        finish();


    }

}
