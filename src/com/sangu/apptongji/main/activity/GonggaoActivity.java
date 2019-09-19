package com.sangu.apptongji.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2017-06-22.
 */

public class GonggaoActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private LinearLayout ll4;
    private LinearLayout ll5;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_gonggao);
        ll1 = (LinearLayout) findViewById(R.id.ll1);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        ll4 = (LinearLayout) findViewById(R.id.ll4);
        ll5 = (LinearLayout) findViewById(R.id.ll5);
        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        ll4.setOnClickListener(this);
        ll5.setOnClickListener(this);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll1:
                startActivity(new Intent(GonggaoActivity.this,GonggaoDetailActivity.class).putExtra("imageType","01"));
                break;
            case R.id.ll2:
                startActivity(new Intent(GonggaoActivity.this,GonggaoDetailActivity.class).putExtra("imageType","02"));
                break;
            case R.id.ll3:
                startActivity(new Intent(GonggaoActivity.this,GonggaoDetailActivity.class).putExtra("imageType","03"));
                break;
            case R.id.ll4:
                startActivity(new Intent(GonggaoActivity.this,GonggaoDetailActivity.class).putExtra("imageType","04"));
                break;
            case R.id.ll5:
                startActivity(new Intent(GonggaoActivity.this,GonggaoDetailActivity.class).putExtra("imageType","05"));
                break;
        }
    }
}
