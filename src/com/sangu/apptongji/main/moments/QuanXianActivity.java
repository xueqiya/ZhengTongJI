package com.sangu.apptongji.main.moments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fanxin.easeui.widget.EaseSwitchButton;
import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

/**
 * Created by Administrator on 2016-11-17.
 */

public class QuanXianActivity extends BaseActivity implements View.OnClickListener{
    private EaseSwitchButton switchAllpeople;
    private EaseSwitchButton switchOnlyFriend;
    private EaseSwitchButton switchOnlyMyself;
    private EaseSwitchButton switch_fenxiang;
    private RelativeLayout rl_fenxiang;
    private RelativeLayout rlswitchAllpeople;
    private RelativeLayout rlswitchOnlyFriend;
    private RelativeLayout rlswitchOnlyMyself;
    private Button btn_send;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_quanxian);
        rl_fenxiang = (RelativeLayout) this.findViewById(R.id.rl_fenxiang);
        rlswitchAllpeople = (RelativeLayout) this.findViewById(R.id.rl_switch_allpeople);
        rlswitchOnlyFriend = (RelativeLayout) this.findViewById(R.id.rl_switch_onlyFriend);
        rlswitchOnlyMyself = (RelativeLayout) this.findViewById(R.id.rl_switch_onlyMyself);
        switchAllpeople = (EaseSwitchButton) this.findViewById(R.id.switch_allpeople);
        switchOnlyFriend = (EaseSwitchButton) this.findViewById(R.id.switch_onlyFriend);
        switchOnlyMyself = (EaseSwitchButton) this.findViewById(R.id.switch_onlyMyself);
        switch_fenxiang = (EaseSwitchButton) this.findViewById(R.id.switch_fenxiang);
        btn_send = (Button) this.findViewById(R.id.btn_send);
        switchAllpeople.openSwitch();
        switchOnlyMyself.closeSwitch();
        switchOnlyFriend.closeSwitch();
        switch_fenxiang.closeSwitch();
        btn_send.setOnClickListener(this);
        rlswitchAllpeople.setOnClickListener(this);
        rlswitchOnlyFriend.setOnClickListener(this);
        rlswitchOnlyMyself.setOnClickListener(this);
        rl_fenxiang.setOnClickListener(this);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_allpeople:
                if (switchAllpeople.isSwitchOpen()) {
                    switchAllpeople.closeSwitch();
                    switchOnlyFriend.openSwitch();
                    switchOnlyMyself.closeSwitch();
                } else {
                    switchAllpeople.openSwitch();
                    switchOnlyMyself.closeSwitch();
                    switchOnlyFriend.closeSwitch();
                }
                break;
            case R.id.rl_switch_onlyFriend:
                if (switchOnlyFriend.isSwitchOpen()) {
                    switchOnlyFriend.closeSwitch();
                    switchOnlyMyself.openSwitch();
                    switchAllpeople.closeSwitch();
                } else {
                    switchOnlyFriend.openSwitch();
                    switchAllpeople.closeSwitch();
                    switchOnlyMyself.closeSwitch();
                }
                break;
            case R.id.rl_switch_onlyMyself:
                if (switchOnlyMyself.isSwitchOpen()) {
                    switchOnlyMyself.closeSwitch();
                    switchAllpeople.openSwitch();
                    switchOnlyFriend.closeSwitch();
                } else {
                    switchOnlyMyself.openSwitch();
                    switchAllpeople.closeSwitch();
                    switchOnlyFriend.closeSwitch();
                }
                break;
            case R.id.rl_fenxiang:
                if (switch_fenxiang.isSwitchOpen()){
                    switch_fenxiang.closeSwitch();
                }else {
                    switch_fenxiang.openSwitch();
                }
                break;
            case R.id.btn_send:
                String str = "";
                String str1 = "";
                if (switch_fenxiang.isSwitchOpen()){
                    str1 = "01";
                }else {
                    str1 = "00";
                }
                if (switchAllpeople.isSwitchOpen()){
                    str = "01";
                }else if (switchOnlyFriend.isSwitchOpen()){
                    str = "02";
                }else if (switchOnlyMyself.isSwitchOpen()){
                    str = "03";
                }
                setResult(RESULT_FIRST_USER,new Intent().putExtra("quanxian",str).putExtra("fenxiang",str1));
                finish();
        }
    }
}
