package com.sangu.apptongji.main.zhengshiinfo;

import android.os.Bundle;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2017-01-24.
 */

public class ZhengshiDetailActivity extends BaseActivity{
private TextView tv1,tv2,tv3;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_zhengshier_detail);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv1.setText("一、为什么使用正事多交易？\n" +
                "1、只要登录就会展示在所有用户附近\n" +
                "2、直接展示每个人的专业便于筛选\n" +
                "3、先验资，原笔迹签收，更能落实保障\n" +
                "4、适合多行业的下单模式......");
        tv2.setText("二、如何使用正事多？\n" +
                "1、实名注册，确定专业，不要频繁修改\n" +
                "2、为专业注入保障金，提高信任度\n" +
                "3、用心编辑每个专业，图文并茂");
        tv3.setText("三、如何在正事多接单？\n" +
                "1、保持定位功能开启，保持登陆状态\n" +
                "2、通过发布动态、分享、刷新位置\n" +
                "3、让好友帮助转发，把你传播的更远\n" +
                "4、注入动态转发回馈(红包)激励大家帮你传播信息");
    }
}
