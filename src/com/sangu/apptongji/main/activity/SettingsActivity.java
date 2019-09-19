package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.widget.EaseSwitchButton;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.util.EMLog;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.DemoModel;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.fragment.MainActivity;
import com.sangu.apptongji.main.fragment.MainTwoActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.zhengshiinfo.AboutZhengshiActivity;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.BlacklistActivity;
import com.sangu.apptongji.ui.DiagnoseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangfangyi on 2016/7/4.\
 * QQ:84543217
 */
public class SettingsActivity extends BaseActivity  implements View.OnClickListener {
    /**
     * new message notification
     */
    private RelativeLayout rl_switch_notification;
    /**
     * sound
     */
    private RelativeLayout rl_switch_sound;
    /**
     * vibration
     */
    private RelativeLayout rl_switch_vibrate;
    /**
     * speaker
     */
    private RelativeLayout rl_switch_speaker;


    /**
     * line between sound and vibration
     */
    private TextView textview1, textview2;

    private LinearLayout blacklistContainer;

    private LinearLayout userProfileContainer;

    /**
     * logout
     */
    private Button logoutBtn;

    private RelativeLayout rl_switch_chatroom_leave;

    private RelativeLayout rl_switch_delete_msg_when_exit_group;
    private RelativeLayout rl_switch_auto_accept_group_invitation;
    private RelativeLayout rl_switch_adaptive_video_encode;

    /**
     * Diagnose
     */
    private LinearLayout llDiagnose;
    /**
     * display name for APNs
     */
    private LinearLayout pushNick;

    private EaseSwitchButton notifiSwitch;
    private EaseSwitchButton soundSwitch;
    private EaseSwitchButton vibrateSwitch;
    private EaseSwitchButton speakerSwitch;
    private EaseSwitchButton ownerLeaveSwitch;
    private EaseSwitchButton switch_delete_msg_when_exit_group;
    private EaseSwitchButton switch_auto_accept_group_invitation;
    private EaseSwitchButton switch_adaptive_video_encode;
    private DemoModel settingsModel;
    private EMOptions chatOptions;

    private RelativeLayout re_qiye_yincang;
    private RelativeLayout re_qiye_zhaopin;
    private RelativeLayout re_qiye_jiameng;
    private RelativeLayout rl_about;
    private TextView textView13,textView1;
    private TextView tv_yincang;
    private String locationState=null;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_fragment_conversation_settings);
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        rl_switch_notification = (RelativeLayout) findViewById(R.id.rl_switch_notification);
        rl_switch_sound = (RelativeLayout) findViewById(R.id.rl_switch_sound);
        rl_switch_vibrate = (RelativeLayout) findViewById(R.id.rl_switch_vibrate);
        rl_switch_speaker = (RelativeLayout) findViewById(R.id.rl_switch_speaker);
        rl_switch_chatroom_leave = (RelativeLayout) findViewById(R.id.rl_switch_chatroom_owner_leave);
        rl_switch_delete_msg_when_exit_group = (RelativeLayout) findViewById(R.id.rl_switch_delete_msg_when_exit_group);
        rl_switch_auto_accept_group_invitation = (RelativeLayout) findViewById(R.id.rl_switch_auto_accept_group_invitation);
        rl_switch_adaptive_video_encode = (RelativeLayout) findViewById(R.id.rl_switch_adaptive_video_encode);

        notifiSwitch = (EaseSwitchButton) findViewById(R.id.switch_notification);
        soundSwitch = (EaseSwitchButton) findViewById(R.id.switch_sound);
        vibrateSwitch = (EaseSwitchButton) findViewById(R.id.switch_vibrate);
        speakerSwitch = (EaseSwitchButton) findViewById(R.id.switch_speaker);
        ownerLeaveSwitch = (EaseSwitchButton) findViewById(R.id.switch_owner_leave);
        switch_delete_msg_when_exit_group = (EaseSwitchButton) findViewById(R.id.switch_delete_msg_when_exit_group);
        switch_auto_accept_group_invitation = (EaseSwitchButton) findViewById(R.id.switch_auto_accept_group_invitation);
        switch_adaptive_video_encode = (EaseSwitchButton) findViewById(R.id.switch_adaptive_video_encode);
        LinearLayout llChange = (LinearLayout) findViewById(R.id.ll_change);
        logoutBtn = (Button) findViewById(R.id.btn_logout);
        if(!TextUtils.isEmpty(EMClient.getInstance().getCurrentUser())){
            logoutBtn.setText(getString(R.string.button_logout) );
        }
        //"(" + EMClient.getInstance().getCurrentUser() + ")"
        textview1 = (TextView) findViewById(R.id.textview1);
        textview2 = (TextView) findViewById(R.id.textview2);

        blacklistContainer = (LinearLayout) findViewById(R.id.ll_black_list);
        userProfileContainer = (LinearLayout) findViewById(R.id.ll_user_profile);
        llDiagnose=(LinearLayout) findViewById(R.id.ll_diagnose);
        pushNick=(LinearLayout) findViewById(R.id.ll_set_push_nick);

        settingsModel = DemoHelper.getInstance().getModel();
        chatOptions = EMClient.getInstance().getOptions();

        blacklistContainer.setOnClickListener(this);
        userProfileContainer.setOnClickListener(this);
        rl_switch_notification.setOnClickListener(this);
        rl_switch_sound.setOnClickListener(this);
        rl_switch_vibrate.setOnClickListener(this);
        rl_switch_speaker.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        llDiagnose.setOnClickListener(this);
        pushNick.setOnClickListener(this);
        rl_switch_chatroom_leave.setOnClickListener(this);
        rl_switch_delete_msg_when_exit_group.setOnClickListener(this);
        rl_switch_auto_accept_group_invitation.setOnClickListener(this);
        rl_switch_adaptive_video_encode.setOnClickListener(this);
        llChange.setOnClickListener(this);

        // the vibrate and sound notification are allowed or not?
        if (settingsModel.getSettingMsgNotification()) {
            notifiSwitch.openSwitch();
        } else {
            notifiSwitch.closeSwitch();
        }

        // sound notification is switched on or not?
        if (settingsModel.getSettingMsgSound()) {
            soundSwitch.openSwitch();
        } else {
            soundSwitch.closeSwitch();
        }

        // vibrate notification is switched on or not?
        if (settingsModel.getSettingMsgVibrate()) {
            vibrateSwitch.openSwitch();
        } else {
            vibrateSwitch.closeSwitch();
        }

        // the speaker is switched on or not?
        if (settingsModel.getSettingMsgSpeaker()) {
            speakerSwitch.openSwitch();
        } else {
            speakerSwitch.closeSwitch();
        }

        // if allow owner leave
        if(settingsModel.isChatroomOwnerLeaveAllowed()){
            ownerLeaveSwitch.openSwitch();
        }else{
            ownerLeaveSwitch.closeSwitch();
        }

        // delete messages when exit group?
        if(settingsModel.isDeleteMessagesAsExitGroup()){
            switch_delete_msg_when_exit_group.openSwitch();
        } else {
            switch_delete_msg_when_exit_group.closeSwitch();
        }

        if (settingsModel.isAutoAcceptGroupInvitation()) {
            switch_auto_accept_group_invitation.openSwitch();
        } else {
            switch_auto_accept_group_invitation.closeSwitch();
        }

        if (settingsModel.isAdaptiveVideoEncode()) {
            switch_adaptive_video_encode.openSwitch();
            EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(true);
        } else {
            switch_adaptive_video_encode.closeSwitch();
            EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(false);
        }

        locationState = this.getIntent().getStringExtra("locationState");
        rl_about = (RelativeLayout) findViewById(R.id.rl_about);
        re_qiye_yincang = (RelativeLayout) findViewById(R.id.re_qiye_yincang);
        re_qiye_zhaopin = (RelativeLayout) findViewById(R.id.re_qiye_zhaopin);
        re_qiye_jiameng = (RelativeLayout) findViewById(R.id.re_qiye_jiameng);
        textView13 = (TextView) findViewById(R.id.textView13);
        textView1 = (TextView) findViewById(R.id.textView1);
        tv_yincang = (TextView) findViewById(R.id.tv_yincang);
        textView13.setText("隐藏自己的位置");
        re_qiye_zhaopin.setVisibility(View.VISIBLE);
        re_qiye_zhaopin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(SettingsActivity.this, SupportFunctionAvtivity.class);

                startActivityForResult(intent3, 0);
            }
        });

        re_qiye_jiameng.setVisibility(View.VISIBLE);
        re_qiye_jiameng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingsActivity.this, CertificationActivity.class));

            }
        });

        if ("00".equals(locationState)){
            tv_yincang.setText("隐藏");
        }else {
            tv_yincang.setText("不隐藏");
        }
        rl_about.setVisibility(View.VISIBLE);
        rl_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, AboutZhengshiActivity.class));
            }
        });
        re_qiye_yincang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater4 = LayoutInflater.from(SettingsActivity.this);
                RelativeLayout layout4 = (RelativeLayout) inflater4.inflate(R.layout.dialog_alert, null);
                final Dialog dialog4 = new AlertDialog.Builder(SettingsActivity.this,R.style.Dialog).create();
                dialog4.show();
                dialog4.getWindow().setContentView(layout4);
                dialog4.setCanceledOnTouchOutside(true);
                dialog4.setCancelable(true);
                TextView title_tv4 = (TextView) layout4.findViewById(R.id.title_tv);
                Button btnCancel4 = (Button) layout4.findViewById(R.id.btn_cancel);
                final Button btnOK4 = (Button) layout4.findViewById(R.id.btn_ok);
                btnOK4.setText("确定");
                btnCancel4.setText("取消");
                if ("00".equals(locationState)) {
                    title_tv4.setText("确定不隐藏位置吗？");
                }else {
                    title_tv4.setText("确定隐藏位置吗？");
                }
                btnCancel4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog4.dismiss();
                    }
                });
                btnOK4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog4.dismiss();
                        if ("00".equals(locationState)) {
                            weizhi("不隐藏");
                        }else {
                            weizhi("隐藏");
                        }
                    }
                });
            }
        });
    }

    private void weizhi(final String shangbanzt) {
        String url = FXConstant.URL_UPDATE;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("yinsiset,s",s);
                if ("00".equals(locationState)) {
                    tv_yincang.setText("不隐藏");
                    locationState = "01";
                }else {
                    tv_yincang.setText("隐藏");
                    locationState = "00";
                }
                Toast.makeText(getApplicationContext(), "操作成功",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
                if ("不隐藏".equals(shangbanzt)) {
                    param.put("locationState", "01");
                    param.put("workState", "01");
                }else {
                    param.put("locationState", "00");
                    param.put("workState", "00");
                }
                return param;
            }
        };
        MySingleton.getInstance(SettingsActivity.this).addToRequestQueue(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_change:
//                RedPacketUtil.startChangeActivity(this);
                break;
            case R.id.rl_switch_notification:
                if (notifiSwitch.isSwitchOpen()) {
                    notifiSwitch.closeSwitch();
                    rl_switch_sound.setVisibility(View.GONE);
                    rl_switch_vibrate.setVisibility(View.GONE);
                    textview1.setVisibility(View.GONE);
                    textview2.setVisibility(View.GONE);

                    settingsModel.setSettingMsgNotification(false);
                } else {
                    notifiSwitch.openSwitch();
                    rl_switch_sound.setVisibility(View.VISIBLE);
                    rl_switch_vibrate.setVisibility(View.VISIBLE);
                    textview1.setVisibility(View.VISIBLE);
                    textview2.setVisibility(View.VISIBLE);
                    settingsModel.setSettingMsgNotification(true);
                }
                break;
            case R.id.rl_switch_sound:
                if (soundSwitch.isSwitchOpen()) {
                    soundSwitch.closeSwitch();
                    settingsModel.setSettingMsgSound(false);
                } else {
                    soundSwitch.openSwitch();
                    settingsModel.setSettingMsgSound(true);
                }
                break;
            case R.id.rl_switch_vibrate:
                if (vibrateSwitch.isSwitchOpen()) {
                    vibrateSwitch.closeSwitch();
                    settingsModel.setSettingMsgVibrate(false);
                } else {
                    vibrateSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case R.id.rl_switch_speaker:
                if (speakerSwitch.isSwitchOpen()) {
                    speakerSwitch.closeSwitch();
                    settingsModel.setSettingMsgSpeaker(false);
                } else {
                    speakerSwitch.openSwitch();
                    settingsModel.setSettingMsgVibrate(true);
                }
                break;
            case R.id.rl_switch_chatroom_owner_leave:
                if(ownerLeaveSwitch.isSwitchOpen()){
                    ownerLeaveSwitch.closeSwitch();
                    settingsModel.allowChatroomOwnerLeave(false);
                    chatOptions.allowChatroomOwnerLeave(false);
                }else{
                    ownerLeaveSwitch.openSwitch();
                    settingsModel.allowChatroomOwnerLeave(true);
                    chatOptions.allowChatroomOwnerLeave(true);
                }
                break;
            case R.id.rl_switch_delete_msg_when_exit_group:
                if(switch_delete_msg_when_exit_group.isSwitchOpen()){
                    switch_delete_msg_when_exit_group.closeSwitch();
                    settingsModel.setDeleteMessagesAsExitGroup(false);
                    chatOptions.setDeleteMessagesAsExitGroup(false);
                }else{
                    switch_delete_msg_when_exit_group.openSwitch();
                    settingsModel.setDeleteMessagesAsExitGroup(true);
                    chatOptions.setDeleteMessagesAsExitGroup(true);
                }
                break;
            case R.id.rl_switch_auto_accept_group_invitation:
                if(switch_auto_accept_group_invitation.isSwitchOpen()){
                    switch_auto_accept_group_invitation.closeSwitch();
                    settingsModel.setAutoAcceptGroupInvitation(false);
                    chatOptions.setAutoAcceptGroupInvitation(false);
                }else{
                    switch_auto_accept_group_invitation.openSwitch();
                    settingsModel.setAutoAcceptGroupInvitation(true);
                    chatOptions.setAutoAcceptGroupInvitation(true);
                }
                break;
            case R.id.rl_switch_adaptive_video_encode:
                EMLog.d("switch", "" + !switch_adaptive_video_encode.isSwitchOpen());
                if (switch_adaptive_video_encode.isSwitchOpen()){
                    switch_adaptive_video_encode.closeSwitch();
                    settingsModel.setAdaptiveVideoEncode(false);
                    EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(false);
                }else{
                    switch_adaptive_video_encode.openSwitch();
                    settingsModel.setAdaptiveVideoEncode(true);
                    EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(true);
                }
                break;
            case R.id.btn_logout:
                logout();
                break;
            case R.id.ll_black_list:
                startActivity(new Intent(SettingsActivity.this, BlacklistActivity.class));
                break;
            case R.id.ll_diagnose:
                startActivity(new Intent(SettingsActivity.this, DiagnoseActivity.class));
                break;
            case R.id.ll_set_push_nick:
                //   startActivity(new Intent(SettingsActivity.this, OfflinePushNickActivity.class));
                break;
            case R.id.ll_user_profile:
//                startActivity(new Intent(SettingsActivity.this, UserProfileActivity.class).putExtra("setting", true)
//                        .putExtra("username", EMClient.getInstance().getCurrentUser()));
                break;
            default:
                break;
        }
    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(SettingsActivity.this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        SharedPreferences sp = getSharedPreferences("sangu_shxb_zhuangtai", Context.MODE_PRIVATE);
        SharedPreferences sp1 = getSharedPreferences("sangu_bddh_time", Context.MODE_PRIVATE);
        SharedPreferences sp2 = getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        SharedPreferences sp3 = getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
        if (sp3!=null) {
            SharedPreferences.Editor editor1 = sp3.edit();
            editor1.clear();
            editor1.commit();
        }
        if (sp2!=null) {
            SharedPreferences.Editor editor1 = sp2.edit();
            editor1.clear();
            editor1.commit();
        }
        if (sp!=null) {
            SharedPreferences.Editor editor = sp.edit();
            if (editor!=null) {
                editor.clear();
                editor.commit();
            }
        }
        if (sp1!=null) {
            SharedPreferences.Editor editor = sp1.edit();
            if (editor!=null) {
                editor.clear();
                editor.commit();
            }
        }
        deleteInstall();
        DemoHelper.getInstance().logout(true,new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            if (pd != null && pd.isShowing() && !SettingsActivity.this.isDestroyed()) {
                                pd.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // show login screen
                        DemoApplication.getInstance().clear();
                        DemoHelper.getInstance().setCurrentUserName(null);
                        if (MainActivity.instance!=null) {
                            MainActivity.instance.finish();
                        }
                    }
                });
            }
            @Override
            public void onProgress(int progress, String status) {

            }
            @Override
            public void onError(int code, String message) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(SettingsActivity.this, "unbind devicetokens failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void deleteInstall() {
        String url = FXConstant.URL_DELETE_INSTALL;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("chen", "deleteInstall" + s);
                startActivity(new Intent(SettingsActivity.this, MainTwoActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("chen", "deleteInstall  onErrorResponse" + volleyError.getMessage());
                startActivity(new Intent(SettingsActivity.this, MainTwoActivity.class));
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("uId",DemoHelper.getInstance().getCurrentUsernName());
                Log.d("chen", "删除uid" + DemoHelper.getInstance().getCurrentUsernName());
                return param;
            }
        };
        MySingleton.getInstance(SettingsActivity.this).addToRequestQueue(request);
    }
}
