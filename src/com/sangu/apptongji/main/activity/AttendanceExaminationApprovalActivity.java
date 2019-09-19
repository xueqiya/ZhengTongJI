package com.sangu.apptongji.main.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sangu.apptongji.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//考勤审批
public class AttendanceExaminationApprovalActivity extends AppCompatActivity {
    @BindView(R.id.rv)
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_examination_approval);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.tv_back, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
