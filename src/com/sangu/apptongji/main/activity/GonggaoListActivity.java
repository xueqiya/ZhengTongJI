package com.sangu.apptongji.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2017-12-08.
 */

public class GonggaoListActivity extends BaseActivity {
    private RelativeLayout rl_gonggao;
    private TextView tv_setting;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_gonggao_list);
        rl_gonggao = (RelativeLayout) findViewById(R.id.rl_gonggao);
        tv_setting = (TextView) findViewById(R.id.tv_setting);
        rl_gonggao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GonggaoListActivity.this, GonggaoActivity.class));
            }
        });
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GonggaoListActivity.this, JieDanSettingActivity.class));
            }
        });
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}
