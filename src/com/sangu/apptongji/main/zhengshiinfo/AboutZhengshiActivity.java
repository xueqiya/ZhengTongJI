package com.sangu.apptongji.main.zhengshiinfo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2017-01-23.
 */

public class AboutZhengshiActivity extends BaseActivity implements View.OnClickListener{
    private TextView tv_about;
    private TextView tv_pingfen;
    private TextView tv_bangzhu;
    private TextView tv_jieshao;
    private TextView tv_kaifahezuo;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_about_zhengshier);
        tv_about = (TextView) findViewById(R.id.tv_about_zhshi);
        tv_pingfen = (TextView) findViewById(R.id.tv_pingfen);
        tv_bangzhu = (TextView) findViewById(R.id.tv_helpto_use);
        tv_jieshao = (TextView) findViewById(R.id.tv_jieshao);
        tv_kaifahezuo = (TextView) findViewById(R.id.tv_kaifang);
        tv_about.setOnClickListener(this);
        tv_pingfen.setOnClickListener(this);
        tv_bangzhu.setOnClickListener(this);
        tv_jieshao.setOnClickListener(this);
        tv_kaifahezuo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_about_zhshi:
                startActivity(new Intent(AboutZhengshiActivity.this,ZhengshiDetailTwoActivity.class));
                break;
            case R.id.tv_pingfen:
                try {
                    Uri uri = Uri.parse("market://details?id="
                            + getPackageName());//需要评分的APP包名
                    Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                    intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent5);
                } catch (Exception e) {
                    Toast.makeText(AboutZhengshiActivity.this, "跳转失败,请下载应用宝之后评分", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_helpto_use:
                startActivity(new Intent(AboutZhengshiActivity.this,ZhengshiDetailActivity.class));
                break;
            case R.id.tv_jieshao:
                startActivity(new Intent(AboutZhengshiActivity.this,GongNengActivity.class));
                break;
            case R.id.tv_kaifang:
                startActivity(new Intent(AboutZhengshiActivity.this,KefuActivity.class));
                break;
        }
    }
}
