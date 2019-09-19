package com.sangu.apptongji.main.alluser.order.avtivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.entity.MerDetail;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2017-04-27.
 */

public class MerDetailActivity extends BaseActivity {
    MerDetail merDetail=null;
    private TextView tv_jine_title=null;
    private TextView tv_jine=null;
    private TextView tv_type=null;
    private TextView tv_fangshi=null;
    private TextView tv_time=null;
    private TextView tv_danhao=null;
    private TextView tv_beizhu=null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_merdetail);
        tv_jine_title = (TextView) findViewById(R.id.tv_jine_title);
        tv_jine = (TextView) findViewById(R.id.tv_jine);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_fangshi = (TextView) findViewById(R.id.tv_fangshi);
        tv_time = (TextView) findViewById(R.id.tv_from);
        tv_danhao = (TextView) findViewById(R.id.tv_danhao);
        tv_beizhu = (TextView) findViewById(R.id.tv_beizhu);
        merDetail = (MerDetail) this.getIntent().getSerializableExtra("merDetail");
        initView();
    }

    private void initView() {
        String acc_type = merDetail.getAcc_type();
        String jine = merDetail.getTransaction_amount();
        String fangshi = merDetail.getPay_type();
        String danhao = merDetail.getOrder_id();
        String transType = merDetail.getTransaction_type();
        if (acc_type!=null&&"收入".equals(acc_type)){
            tv_jine_title.setText("入账金额");
            jine = String.format("%.2f", Double.parseDouble(jine));
            tv_jine.setText(jine);
        }else {
            tv_jine_title.setText("出账金额");
            jine = String.format("%.2f", Double.parseDouble(jine));
            tv_jine.setText(jine);
            tv_jine.setTextColor(Color.BLACK);
        }
        tv_type.setText(acc_type);
        tv_fangshi.setText(fangshi);
        String create = merDetail.getTimestamp();
        if (create!=null&&create.length()>13){
            String time = create.substring(0, 4) + "-" + create.substring(4, 6) + "-" + create.substring(6, 8) + " "
                    + create.substring(8, 10) + ":" + create.substring(10, 12);
            tv_time.setText(time);
        }
        tv_danhao.setText(danhao);
        tv_beizhu.setText("正事多-"+transType);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }
}
