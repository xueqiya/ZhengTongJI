package com.sangu.apptongji.main.moments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fanxin.easeui.widget.chatrow.EaseChatRowVoicePlayClickListener;
import com.hyphenate.EMError;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.alluser.presenter.IQuickPublishPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.QuickPublishPresenter;
import com.sangu.apptongji.main.utils.RecordPlayer;
import com.sangu.apptongji.main.utils.SendVoiceUtils;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.utils.AssetsUtils;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.sangu.apptongji.widget.AudioRecorderButton;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import static com.sangu.apptongji.widget.AudioRecorderButton.STATE_DOWN;
import static com.sangu.apptongji.widget.AudioRecorderButton.STATE_MOVE;
import static com.sangu.apptongji.widget.AudioRecorderButton.STATE_UP;

/**
 * Created by Administrator on 2018-01-16.
 */

public class QuickPublishActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_quick_miaoshu;
    private ImageView iv_voice;
    private RelativeLayout rl2_quick_send,rl_yuyin_contain,tl_edit_contain;
    private AudioRecorderButton btn_voice;
    private TextView tv_audio_time,tv_yuyin_time;
    private TextView tv_quick_send,tv_title;
    private IQuickPublishPresenter presenter;
    public MyLocationListenner myListener = new MyLocationListenner();
    private String lng;
    private String lat;
    private LocationClient mLocClient;
    private LocationClientOption option;
    private boolean isLuYin = false,isShiTing = false;
    private RecordPlayer player;
    String filePath=null;
    SendVoiceUtils utils;
    int length;
    private AlertDialog dialog;
    private String sid,file,content;
    private List<String> keywords;


     Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int i = msg.what;
            switch (i) {
                case 4:
                    if (isLuYin) {
                        //获取现在录音时间
                        tv_audio_time.setText("录音:" + msg.arg1 + "''");
                    }
                    break;

            }
        }
    };
    private MediaPlayer mMediaPlayer;
    private CustomProgressDialog mProgress = null;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_quick_publish);
        sid = getIntent().getStringExtra("sid");
        file = getIntent().getStringExtra("file");
        content = getIntent().getStringExtra("content");
        et_quick_miaoshu = (EditText) findViewById(R.id.et_quick_miaoshu);
        rl2_quick_send = (RelativeLayout) findViewById(R.id.rl2_quick_send);
        tl_edit_contain = (RelativeLayout) findViewById(R.id.tl_edit_contain);
        rl_yuyin_contain = (RelativeLayout) findViewById(R.id.rl_yuyin_contain);
        btn_voice = (AudioRecorderButton) findViewById(R.id.btn_voice);
        tv_yuyin_time = (TextView) findViewById(R.id.tv_yuyin_time);
        tv_quick_send = (TextView) findViewById(R.id.tv_quick_send);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        presenter = new QuickPublishPresenter(this);
        rl_yuyin_contain.setOnClickListener(this);
        rl2_quick_send.setOnClickListener(this);
        btn_voice.setOnClickStateListstener(new AudioRecorderButton.OnClickStateListstener() {
            @Override
            public void onStateChange(int state) {
                stateHasChange(state);
            }
        });
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("gcj02");
        option.setScanSpan(30000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mProgress = CustomProgressDialog.createDialog(this);
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });

        if (!TextUtils.isEmpty(sid)) {
            tv_quick_send.setText("快速派人");
            tv_title.setText("快速派人");
        }
        initVoice();
        if (!TextUtils.isEmpty(content)) {
            et_quick_miaoshu.setText(content);

        }
        if (!TextUtils.isEmpty(file)) {
            tl_edit_contain.setVisibility(View.GONE);
            rl_yuyin_contain.setVisibility(View.VISIBLE);
            filePath = file;
        }
        mMediaPlayer = new MediaPlayer();
        keywords = AssetsUtils.getKeyWordFilter(this);
    }

    private void stateHasChange(final int state) {
        PermissionUtil permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                new PermissionListener() {
                    @Override
                    public void onGranted() {
                        if (state == STATE_DOWN) {
                            if (isShiTing){
                                if (player!=null&&player.isPlaying()) {
                                    player.stopPalyer();
                                }
                            }

                            //没在录音,点击关闭播放，开始录音，显示录音动画和试听
                            filePath = null;
                            length = 0;
                            if (EaseChatRowVoicePlayClickListener.isPlaying)
                                EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
                            utils.startRecording();
                            isLuYin = true;
                            isShiTing = false;

                            LayoutInflater inflater = LayoutInflater.from(QuickPublishActivity.this);
                            RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.dialog_luyin, null);
                            dialog = new AlertDialog.Builder(QuickPublishActivity.this,R.style.Dialog).create();
                            dialog.show();
                            dialog.getWindow().setContentView(layout);
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.setCancelable(true);
                            tv_audio_time = (TextView) layout.findViewById(R.id.tv_audio_time);

                        } else if (state == STATE_MOVE) {

                        } else if (state == STATE_UP) {

                            try {
                                dialog.dismiss();
                                length = utils.stopRecoding();
                                if (length > 0) {
                                    filePath = utils.getVoiceFilePath();
                                    tl_edit_contain.setVisibility(View.GONE);
                                    rl_yuyin_contain.setVisibility(View.VISIBLE);
                                    tv_yuyin_time.setText(length + "s");
                                } else if (length == EMError.FILE_INVALID) {
                                    Toast.makeText(QuickPublishActivity.this.getApplicationContext(), com.hyphenate.easeui.R.string.Recording_without_permission, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(QuickPublishActivity.this.getApplicationContext(), com.hyphenate.easeui.R.string.The_recording_time_is_too_short, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(QuickPublishActivity.this.getApplicationContext(), com.hyphenate.easeui.R.string.send_failure_please, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        Toast.makeText(QuickPublishActivity.this.getApplicationContext(), "您拒绝了获取麦克风的权限！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        Toast.makeText(QuickPublishActivity.this.getApplicationContext(), "您拒绝了获取麦克风的权限！", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void initVoice() {
        WeakReference<QuickPublishActivity> references =  new WeakReference<QuickPublishActivity>(QuickPublishActivity.this);
        utils = new SendVoiceUtils(references.get(),this.myhandler);
        player = new RecordPlayer(references.get());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl2_quick_send:
                UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "6", new UserPermissionUtil.UserPermissionListener() {
                    @Override
                    public void onAllow() {
                        if (mProgress != null) {
                            mProgress.show();
                        }
                        if (isShiTing){
                            if (player!=null&&player.isPlaying()) {
                                player.stopPalyer();
                            }
                        }
                        //如果编辑框展示说明是文字输入模式  反之则是语音模式
                        if (tl_edit_contain.isShown()) {
                            String msg = et_quick_miaoshu.getText().toString();
                            if (TextUtils.isEmpty(msg)) {
                                if (mProgress != null && mProgress.isShowing()) {
                                    mProgress.dismiss();
                                }
                                ToastUtils.showNOrmalToast(QuickPublishActivity.this, "请用语音或文字简单描述需求");
                            } else {
                                String keyword = getBanKeyword(msg);
                                if ( keyword != null) {
                                    ToastUtils.showNOrmalToast(QuickPublishActivity.this, "信息中包含敏感词汇“" +  keyword +"”请重新编辑信息");
                                    if (mProgress != null) {
                                        mProgress.dismiss();
                                    }
                                    return;
                                }
                                //Log.d("chen", DemoHelper.getInstance().getCurrentUsernName() + "lng" + lng + "  lat" + lat);
                                if (TextUtils.isEmpty(sid)) {
                                    presenter.quickPublish(DemoHelper.getInstance().getCurrentUsernName(), "", msg, lng, lat);
                                } else {
                                    presenter.quickUpdate(DemoHelper.getInstance().getCurrentUsernName(),sid,msg,"00");
                                }

                            }
                        } else {

                            if (TextUtils.isEmpty(sid)) {
                                //如果是语音就把content传空
                                presenter.quickVoicePublish(DemoHelper.getInstance().getCurrentUsernName(),"",filePath,lng,lat);
                            } else {
                                presenter.quickVoiceUpdate(DemoHelper.getInstance().getCurrentUsernName(),sid,"",filePath,"00");
                            }

                        }
                    }

                    @Override
                    public void onBan() {
                        ToastUtils.showNOrmalToast(QuickPublishActivity.this.getApplicationContext(), "您的账户已被禁止发布极速派单");
                    }
                });


                /*if (mProgress != null) {
                    mProgress.dismiss();
                }*/
                break;
            case R.id.rl_yuyin_contain:

                isShiTing = true;
                //点击试听，停止录音，播放本地录音文件
                if (filePath==null) {
                    Toast.makeText(QuickPublishActivity.this.getApplicationContext(), "获取录音失败", Toast.LENGTH_SHORT).show();
                }else {
                    if (new File(filePath).exists()) {
                        iv_voice.setImageResource(R.drawable.voice_play);
                        final AnimationDrawable animationDrawable = (AnimationDrawable) iv_voice.getDrawable();
                        animationDrawable.start();
                        player.playRecordFile(new File(filePath));
                        player.setPlayVoiceFinish(new RecordPlayer.PlayVoiceFinish() {
                            @Override
                            public void playVoiceFinish() {
                                iv_voice.setImageResource(R.drawable.ease_chatfrom_voice_playing_f3);
                                animationDrawable.stop();
                                isShiTing = false;
                            }
                        });

                    } else {
                        iv_voice.setImageResource(R.drawable.voice_play);
                        AnimationDrawable animationDrawable = (AnimationDrawable) iv_voice.getDrawable();
                        animationDrawable.start();
                        playVoice(filePath,iv_voice);
                    }
                }
                break;
        }
    }

    private void playVoice(String file, final ImageView imageView) {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(FXConstant.URL_UPLOAD_SPEED + "/" + file); // 设置数据源
                mMediaPlayer.prepare(); // prepare自动播放
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //android.graphics.drawable.BitmapDrawable cannot be cast to android.graphics.drawable.AnimationDrawable
                        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                        animationDrawable.stop();
                        imageView.setImageResource(R.drawable.ease_chatfrom_voice_playing_f3);
                        isShiTing = false;
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }



    }

    private String getBanKeyword(String msg) {
        for (String keyword : keywords) {
            if (msg.contains(keyword)) {
                return keyword;
            }
        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocClient!=null){
            mLocClient.stop();
        }
    }

    @Override
    protected void onDestroy() {
        if (myListener!=null){
            mLocClient.unRegisterLocationListener(myListener);
            myListener = null;
        }
        if (mLocClient != null) {
            mLocClient.stop();
            mLocClient = null;
        }
        if (option !=null ){
            option.setOpenGps(false);
            option = null;
        }
        super.onDestroy();
    }


    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            lng = "" + location.getLongitude();

            lat = "" + location.getLatitude();
            if (lng.equalsIgnoreCase("4.9E-324")) {
                ToastUtils.showNOrmalToast(QuickPublishActivity.this,"定位出错了发送位置会出现错误，请开启GPS，再发送一次");
                lng = "116.407170";
                lat = "39.904690";
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
