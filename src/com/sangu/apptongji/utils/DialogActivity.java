package com.sangu.apptongji.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.activity.PaidanListActivity;

/**
 * Created by Administrator on 2018-01-30.
 */

public class DialogActivity extends Activity  {
    private String companyName,type,pushType;
    private DynamicReceiver dynamicReceiver;
    private TextView title_tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);
        companyName = getIntent().getStringExtra("companyName");
        pushType = getIntent().getStringExtra("pushType");

        getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        title_tv2 = (TextView)findViewById(R.id.title_tv);
        Button btnCancel2 = (Button) findViewById(R.id.btn_cancel);
        final Button btnOK2 = (Button) findViewById(R.id.btn_ok);
        TextView title2 = (TextView) findViewById(R.id.tv_title);
        title2.setText("提示:");
        btnOK2.setText("确定");
        btnCancel2.setText("取消");
        if (pushType.equalsIgnoreCase("19")) {
            title_tv2.setText("您发送的订单有消息了");
        } else {
            title_tv2.setText("有您的派单消息是否前往查看");
        }
        btnCancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOK2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(DialogActivity.this, PaidanListActivity.class);
                intent2.putExtra("companyName", companyName);
                intent2.putExtra("pushType", Integer.valueOf(pushType));
                DialogActivity.this.startActivity(intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        //动态注册广播
        dynamicReceiver = new DynamicReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.broadcast.reflashPush");
        registerReceiver(dynamicReceiver, intentFilter);
    }


     class DynamicReceiver extends BroadcastReceiver {
        public DynamicReceiver () {
        }
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("chen", "我要刷新了通知！！");
            String companyName = intent.getStringExtra("companyName");
            String pushType = intent.getStringExtra("pushType");
            if (pushType.equalsIgnoreCase("19")) {
                title_tv2.setText("您发送的订单有消息了");
            }
            if (!TextUtils.isEmpty(companyName)) {
                DialogActivity.this.companyName = companyName;
                DialogActivity.this.pushType = pushType;
            }

        }
    }

    @Override
    protected void onDestroy() {
        //解除广播
        unregisterReceiver(dynamicReceiver);
        super.onDestroy();
    }
}