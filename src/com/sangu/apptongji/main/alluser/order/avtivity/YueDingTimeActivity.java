package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2016-11-01.
 */

public class YueDingTimeActivity extends BaseActivity{
    private Button btn_commit;
    private EditText etStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuedingshijian);
        etStartTime = (EditText) this.findViewById(R.id.repair_time_et);
        btn_commit = (Button) this.findViewById(R.id.btn_send);
        etStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YueDingTimeActivity.this, DatePickActivity.class);
                intent.putExtra("date", etStartTime.getText().toString());
                startActivityForResult(intent, 1);
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            // 选择预约时间的页面被关闭
            String date = data.getStringExtra("date");
            String time = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " "
                    + date.substring(8, 10) + ":" + date.substring(10, 12);
            if (data!=null) {
                if (!etStartTime.getText().toString().equals(date)) {
                    etStartTime.setText(time);
                    setResult(RESULT_OK,new Intent().putExtra("time",data.getStringExtra("date")));
                }
            }
        }
    }
}
