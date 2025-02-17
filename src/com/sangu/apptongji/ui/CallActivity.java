package com.sangu.apptongji.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMCallManager.EMCallPushProvider;
import com.hyphenate.chat.EMCallStateChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Status;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.EMServiceNotReadyException;
import com.hyphenate.media.EMCallSurfaceView;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.NetUtils;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.utils.PreferenceManager;

@SuppressLint("Registered")
public abstract class CallActivity extends BaseActivity implements IUAZView{
    public final static String TAG = "CallActivity";
    protected final int MSG_CALL_MAKE_VIDEO = 0;
    protected final int MSG_CALL_MAKE_VOICE = 1;
    protected final int MSG_CALL_ANSWER = 2;
    protected final int MSG_CALL_REJECT = 3;
    protected final int MSG_CALL_END = 4;
    protected final int MSG_CALL_RLEASE_HANDLER = 5;
    protected final int MSG_CALL_SWITCH_CAMERA = 6;

    public IUAZPresenter uazPresenter;
    protected boolean isInComingCall;
    protected boolean isRefused = false;
    protected String username;
    public String name,imageStr,shareRed;
    protected CallingState callingState = CallingState.CANCELLED;
    protected String callDruationText;
    protected String msgid;
    protected AudioManager audioManager;
    protected SoundPool soundPool;
    protected Ringtone ringtone;
    protected int outgoing;
    protected EMCallStateChangeListener callStateListener;
    protected EMCallSurfaceView localSurface;
    protected EMCallSurfaceView oppositeSurface;
    protected boolean isAnswered = false;
    protected int streamID = -1;

    EMCallPushProvider pushProvider;
    /**
     * 0：voice call，1：video call
     */
    protected int callType = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        username = getIntent().getStringExtra("username");
        uazPresenter = new UAZPresenter(this,this);
        uazPresenter.loadThisDetail(username);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        pushProvider = new EMCallPushProvider() {

            void updateMessageText(final EMMessage oldMsg, final String to) {
                // update local message text
                EMConversation conv = EMClient.getInstance().chatManager().getConversation(oldMsg.getTo());
                conv.removeMessage(oldMsg.getMsgId());
            }

            @Override
            public void onRemoteOffline(final String to) {

                //this function should exposed & move to Demo
                EMLog.d(TAG, "onRemoteOffline, to:" + to);

                final EMMessage message = EMMessage.createTxtSendMessage("You have an incoming call", to);
                // set the user-defined extension field
                message.setAttribute("em_apns_ext", true);

                message.setAttribute("is_voice_call", callType == 0 ? true : false);

                message.setMessageStatusCallback(new EMCallBack(){

                    @Override
                    public void onSuccess() {
                        EMLog.d(TAG, "onRemoteOffline success");
                        updateMessageText(message, to);
                    }

                    @Override
                    public void onError(int code, String error) {
                        EMLog.d(TAG, "onRemoteOffline Error");
                        updateMessageText(message, to);
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }
                });
                // send messages
                EMClient.getInstance().chatManager().sendMessage(message);
            }
        };
        EMClient.getInstance().callManager().setPushProvider(pushProvider);
    }

    @Override
    protected void onDestroy() {
        if (soundPool != null)
            soundPool.release();
        if (ringtone != null && ringtone.isPlaying())
            ringtone.stop();
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMicrophoneMute(false);

        if(callStateListener != null)
            EMClient.getInstance().callManager().removeCallStateChangeListener(callStateListener);

        if (pushProvider != null) {
            EMClient.getInstance().callManager().setPushProvider(null);
            pushProvider = null;
        }
        releaseHandler();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        handler.sendEmptyMessage(MSG_CALL_END);
        saveCallRecord();
        finish();
        super.onBackPressed();
    }

    Runnable timeoutHangup = new Runnable() {

        @Override
        public void run() {
            handler.sendEmptyMessage(MSG_CALL_END);
        }
    };

    HandlerThread callHandlerThread = new HandlerThread("callHandlerThread");
    { callHandlerThread.start(); }
    protected Handler handler = new Handler(callHandlerThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            EMLog.d("EMCallManager CallActivity", "handleMessage ---enter--- msg.what:" + msg.what);
            switch (msg.what) {
                case MSG_CALL_MAKE_VIDEO:
                case MSG_CALL_MAKE_VOICE:
                    try {
                        streamID = playMakeCallSounds();
                        if (msg.what == MSG_CALL_MAKE_VIDEO) {
                            EMClient.getInstance().callManager().makeVideoCall(username);
                        } else {
                            EMClient.getInstance().callManager().makeVoiceCall(username);
                        }

                        final int MAKE_CALL_TIMEOUT = 50 * 1000;
                        handler.removeCallbacks(timeoutHangup);
                        handler.postDelayed(timeoutHangup, MAKE_CALL_TIMEOUT);
                    } catch (EMServiceNotReadyException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                final String st2 = getResources().getString(R.string.Is_not_yet_connected_to_the_server);
                                Toast.makeText(CallActivity.this, st2, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    break;
                case MSG_CALL_ANSWER:
                    if (ringtone != null)
                        ringtone.stop();
                    if (isInComingCall) {
                        try {
                            if (NetUtils.hasDataConnection(CallActivity.this)) {
                                EMClient.getInstance().callManager().answerCall();
                                isAnswered = true;
                            } else {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        final String st2 = getResources().getString(R.string.Is_not_yet_connected_to_the_server);
                                        Toast.makeText(CallActivity.this, st2, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            saveCallRecord();
                            finish();
                            return;
                        }
                    }
                    break;
                case MSG_CALL_REJECT:
                    if (ringtone != null)
                        ringtone.stop();
                    try {
                        EMClient.getInstance().callManager().rejectCall();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        saveCallRecord();
                        finish();
                    }
                    callingState = CallingState.REFUSED;
                    break;
                case MSG_CALL_END:
                    if (soundPool != null)
                        soundPool.stop(streamID);
                    try {
                        EMClient.getInstance().callManager().endCall();
                    } catch (Exception e) {
                        saveCallRecord();
                        finish();
                    }

                    break;
                case MSG_CALL_RLEASE_HANDLER:
                    try {
                        EMClient.getInstance().callManager().endCall();
                    } catch (Exception e) {
                    }
                    handler.removeCallbacks(timeoutHangup);
                    handler.removeMessages(MSG_CALL_MAKE_VIDEO);
                    handler.removeMessages(MSG_CALL_MAKE_VOICE);
                    handler.removeMessages(MSG_CALL_ANSWER);
                    handler.removeMessages(MSG_CALL_REJECT);
                    handler.removeMessages(MSG_CALL_END);
                    callHandlerThread.quit();
                    break;
                case MSG_CALL_SWITCH_CAMERA:
                    EMClient.getInstance().callManager().switchCamera();
                    break;
                default:
                    break;
            }
            EMLog.d("EMCallManager CallActivity", "handleMessage ---exit--- msg.what:" + msg.what);
        }
    };

    void releaseHandler() {
        handler.sendEmptyMessage(MSG_CALL_RLEASE_HANDLER);
    }

    /**
     * play the incoming call ringtone
     *
     */
    protected int playMakeCallSounds() {
        try {
            // max volume
            float audioMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
            // current volume
            float audioCurrentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
            float volumeRatio = audioCurrentVolume / audioMaxVolume;

            audioManager.setMode(AudioManager.MODE_RINGTONE);
            audioManager.setSpeakerphoneOn(false);

            // play
            int id = soundPool.play(outgoing, // sound resource
                    0.3f, // left volume
                    0.3f, // right volume
                    1,    // priority
                    -1,   // loop，0 is no loop，-1 is loop forever
                    1);   // playback rate (1.0 = normal playback, range 0.5 to 2.0)
            return id;
        } catch (Exception e) {
            return -1;
        }
    }

    protected void openSpeakerOn() {
        try {
            if (!audioManager.isSpeakerphoneOn())
                audioManager.setSpeakerphoneOn(true);
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void closeSpeakerOn() {

        try {
            if (audioManager != null) {
                // int curVolume =
                // audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                if (audioManager.isSpeakerphoneOn())
                    audioManager.setSpeakerphoneOn(false);
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                // audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                // curVolume, AudioManager.STREAM_VOICE_CALL);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * save call record
     */
    protected void saveCallRecord() {
        @SuppressWarnings("UnusedAssignment") EMMessage message = null;
        @SuppressWarnings("UnusedAssignment") EMTextMessageBody txtBody = null;
        if (!isInComingCall) { // outgoing call
            message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            message.setTo(username);
        } else {
            message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
            message.setFrom(username);
        }

        String st1 = getResources().getString(R.string.call_duration);
        String st2 = getResources().getString(R.string.Refused);
        String st3 = getResources().getString(R.string.The_other_party_has_refused_to);
        String st4 = getResources().getString(R.string.The_other_is_not_online);
        String st5 = getResources().getString(R.string.The_other_is_on_the_phone);
        String st6 = getResources().getString(R.string.The_other_party_did_not_answer);
        String st7 = getResources().getString(R.string.did_not_answer);
        String st8 = getResources().getString(R.string.Has_been_cancelled);
        switch (callingState) {
            case NORMAL:
                txtBody = new EMTextMessageBody(st1 + callDruationText);
                break;
            case REFUSED:
                txtBody = new EMTextMessageBody(st2);
                break;
            case BEREFUSED:
                txtBody = new EMTextMessageBody(st3);
                break;
            case OFFLINE:
                txtBody = new EMTextMessageBody(st4);
                break;
            case BUSY:
                txtBody = new EMTextMessageBody(st5);
                break;
            case NO_RESPONSE:
                txtBody = new EMTextMessageBody(st6);
                break;
            case UNANSWERED:
                txtBody = new EMTextMessageBody(st7);
                break;
            case VERSION_NOT_SAME:
                txtBody = new EMTextMessageBody(getString(R.string.call_version_inconsistent));
            default:
                txtBody = new EMTextMessageBody(st8);
                break;
        }
        // set message extension
        if(callType == 0) {
            message.setAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, true);
        }else {
            message.setAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, true);
        }
        String userPic = PreferenceManager.getInstance().getCurrentUserAvatar();
        if (!TextUtils.isEmpty(userPic)){
            message.setAttribute("userPic",userPic);
        }
        String userName = PreferenceManager.getInstance().getCurrentUserNick();
        if (userName==null||"".equals(userName)){
            userName = DemoHelper.getInstance().getCurrentUsernName();
        }
        if (!TextUtils.isEmpty(userName)){
            message.setAttribute("userName",userName);
        }
        String msgType = "单聊";
        String chatImg = imageStr;
        if (chatImg==null){
            chatImg = "0";
        }
        String chatName = name;
        if (chatName==null){
            chatName = username;
        }
        if (shareRed==null||"".equals(shareRed)){
            shareRed = "无";
        }
        message.setAttribute(DemoHelper.getInstance().getCurrentUsernName(), userPic+"|"+userName);
        message.setAttribute(username, chatImg+"|"+chatName);
        message.setAttribute("name",chatName);
        message.setAttribute("type",msgType);
        message.setAttribute("shareRed",shareRed);
        // set message body
        message.addBody(txtBody);
        message.setMsgId(msgid);
        message.setStatus(Status.SUCCESS);

        // save
        EMClient.getInstance().chatManager().saveMessage(message);
    }

    @Override
    public void updateThisUser(Userful user2) {
        name = user2.getName();
        imageStr = TextUtils.isEmpty(user2.getImage())?"":user2.getImage();
        String share = user2.getShareRed();
        String friendsNumber = user2.getFriendsNumber();
        String onceJine = null;
        if (friendsNumber!=null&&!"".equals(friendsNumber)){
            onceJine = friendsNumber.split("\\|")[0];
        }
        if (share!=null&&!"".equals(share)&& Double.parseDouble(share)>0&&onceJine!=null&&!"".equals(onceJine)&&Double.parseDouble(onceJine)>0){
            shareRed = "有";
        }else {
            shareRed = "无";
        }
        if (imageStr!=null&&!"".equals(imageStr)){
            imageStr = imageStr.split("\\|")[0];
        }
        setView();
    }

    @Override
    public void showproLoading() {

    }

    @Override
    public void hideproLoading() {

    }

    @Override
    public void showproError() {

    }

    protected abstract void setView();

    enum CallingState {
        CANCELLED, NORMAL, REFUSED, BEREFUSED, UNANSWERED, OFFLINE, NO_RESPONSE, BUSY, VERSION_NOT_SAME
    }
}
