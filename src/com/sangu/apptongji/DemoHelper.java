package com.sangu.apptongji;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.fanxin.easeui.EaseConstant;
import com.fanxin.easeui.controller.EaseUI;
import com.fanxin.easeui.controller.EaseUI.EaseEmojiconInfoProvider;
import com.fanxin.easeui.controller.EaseUI.EaseSettingsProvider;
import com.fanxin.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.fanxin.easeui.domain.EaseEmojicon;
import com.fanxin.easeui.domain.EaseEmojiconGroupEntity;
import com.fanxin.easeui.domain.EaseUser;
import com.fanxin.easeui.model.EaseAtMessageHelper;
import com.fanxin.easeui.model.EaseNotifier;
import com.fanxin.easeui.model.EaseNotifier.EaseNotificationInfoProvider;
import com.fanxin.easeui.utils.EaseCommonUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessage.Type;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.sangu.apptongji.db.DemoDBManager;
import com.sangu.apptongji.db.InviteMessgeDao;
import com.sangu.apptongji.db.UserDao;
import com.sangu.apptongji.domain.EmojiconExampleGroupData;
import com.sangu.apptongji.domain.InviteMessage;
import com.sangu.apptongji.domain.InviteMessage.InviteMesageStatus;
import com.sangu.apptongji.domain.RobotUser;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.chatheadimage.UserApiModel;
import com.sangu.apptongji.main.chatheadimage.UserInfoCacheSvc;
import com.sangu.apptongji.main.fragment.MainActivity;
import com.sangu.apptongji.main.service.GroupService;
import com.sangu.apptongji.main.utils.JSONUtil;
import com.sangu.apptongji.receiver.CallReceiver;
import com.sangu.apptongji.ui.ChatActivity;
import com.sangu.apptongji.ui.VideoCallActivity;
import com.sangu.apptongji.ui.VoiceCallActivity;
import com.sangu.apptongji.utils.PreferenceManager;
import com.sangu.apptongji.utils.UserProfileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DemoHelper {
    /**
     * data sync listener
     */
    static public interface DataSyncListener {
        /**
         * sync complete
         *
         * @param success true：data sync successful，false: failed to sync data
         */
        public void onSyncComplete(boolean success);
    }

    protected static final String TAG = "DemoHelper";

    private EaseUI easeUI;

    /**
     * EMEventListener
     */
    protected EMMessageListener messageListener = null;

    private Map<String, EaseUser> contactList;

    private Map<String, EaseUser> blackList;

    private Map<String, RobotUser> robotList;

    private UserProfileManager userProManager;

    private static DemoHelper instance = null;

    private DemoModel demoModel = null;

    /**
     * sync groups status listener
     */
    private List<DataSyncListener> syncGroupsListeners;
    /**
     * sync contacts status listener
     */
    private List<DataSyncListener> syncContactsListeners;
    /**
     * sync blacklist status listener
     */
    private List<DataSyncListener> syncBlackListListeners;

    private boolean isSyncingGroupsWithServer = false;
    private boolean isSyncingContactsWithServer = false;
    private boolean isSyncingBlackListWithServer = false;
    private boolean isGroupsSyncedWithServer = false;
    private boolean isContactsSyncedWithServer = false;
    private boolean isBlackListSyncedWithServer = false;

    public boolean isVoiceCalling;
    public boolean isVideoCalling;

    private String username;

    private Context appContext;

    private CallReceiver callReceiver;

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    private LocalBroadcastManager broadcastManager;

    private boolean isGroupAndContactListenerRegisted;

    private DemoHelper() {
    }

    public synchronized static DemoHelper getInstance() {
        if (instance == null) {
            instance = new DemoHelper();
        }
        return instance;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(Context context) {
        demoModel = new DemoModel(context);
        EMOptions options = initChatOptions();
        //use default options if options is null
        if (EaseUI.getInstance().init(context, options)) {
            appContext = context;

            //debug mode, you'd better set it to false, if you want release your App officially.
            EMClient.getInstance().setDebugMode(true);
            //get easeui instance
            easeUI = EaseUI.getInstance();
            //to set user's profile and avatar
            setEaseUIProviders();
            //initialize preference manager
            PreferenceManager.init(context);
            getUserProfileManager().init(context);

            // TODO: set Call options
            // min video kbps
            int minBitRate = PreferenceManager.getInstance().getCallMinVideoKbps();
            if (minBitRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setMinVideoKbps(minBitRate);
            }

            // max video kbps
            int maxBitRate = PreferenceManager.getInstance().getCallMaxVideoKbps();
            if (maxBitRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setMaxVideoKbps(maxBitRate);
            }

            // max frame rate
            int maxFrameRate = PreferenceManager.getInstance().getCallMaxFrameRate();
            if (maxFrameRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setMaxVideoFrameRate(maxFrameRate);
            }

            // audio sample rate
            int audioSampleRate = PreferenceManager.getInstance().getCallAudioSampleRate();
            if (audioSampleRate != -1) {
                EMClient.getInstance().callManager().getCallOptions().setAudioSampleRate(audioSampleRate);
            }

            // resolution
            String resolution = PreferenceManager.getInstance().getCallBackCameraResolution();
            if (resolution.equals("")) {
                resolution = PreferenceManager.getInstance().getCallFrontCameraResolution();
            }
            String[] wh = resolution.split("x");
            if (wh.length == 2) {
                try {
                    EMClient.getInstance().callManager().getCallOptions().setVideoResolution(new Integer(wh[0]).intValue(), new Integer(wh[1]).intValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // enabled fixed sample rate
            boolean enableFixSampleRate = PreferenceManager.getInstance().isCallFixedVideoResolution();
            EMClient.getInstance().callManager().getCallOptions().enableFixedVideoResolution(enableFixSampleRate);

            // Offline call push
            EMClient.getInstance().callManager().getCallOptions().setIsSendPushIfOffline(getModel().isPushCall());

            setGlobalListeners();
            broadcastManager = LocalBroadcastManager.getInstance(appContext);
            initDbDao();
        }
    }


    private EMOptions initChatOptions() {

        EMOptions options = new EMOptions();
        // set if accept the invitation automatically
        options.setAcceptInvitationAlways(false);
        // set if you need read ack
        options.setRequireAck(true);
        // set if you need delivery ack
        options.setRequireDeliveryAck(false);
        options.setAutoLogin(false);

        //you need apply & set your own id if you want to use google cloud messaging.
        options.setGCMNumber("1042232533704");
        //you need apply & set your own id if you want to use Mi push notification
        options.setMipushConfig("2882303761517520397", "5331752052397");
        //you need apply & set your own id if you want to use Huawei push notification
//        options.setHuaweiPushAppId("10709568");
        //set custom servers, commonly used in private deployment
        if(demoModel.isCustomServerEnable() && demoModel.getRestServer() != null && demoModel.getIMServer() != null) {
            options.setRestServer(demoModel.getRestServer());
            options.setIMServer(demoModel.getIMServer());
            if(demoModel.getIMServer().contains(":")) {
                options.setIMServer(demoModel.getIMServer().split(":")[0]);
                options.setImPort(Integer.valueOf(demoModel.getIMServer().split(":")[1]));
            }
        }

        if (demoModel.isCustomAppkeyEnabled() && demoModel.getCutomAppkey() != null && !demoModel.getCutomAppkey().isEmpty()) {
            options.setAppKey(demoModel.getCutomAppkey());
        }
        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());
        options.setDeleteMessagesAsExitGroup(getModel().isDeleteMessagesAsExitGroup());
        options.setAutoAcceptGroupInvitation(getModel().isAutoAcceptGroupInvitation());

        return options;
    }

    protected void setEaseUIProviders() {
        // set profile provider if you want easeUI to handle avatar and nickname
        easeUI.setUserProfileProvider(new EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });

        //set options
        easeUI.setSettingsProvider(new EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return demoModel.getSettingMsgSpeaker();
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return demoModel.getSettingMsgVibrate();
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return demoModel.getSettingMsgSound();
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if (message == null) {
                    return demoModel.getSettingMsgNotification();
                }
                if (!demoModel.getSettingMsgNotification()) {
                    return false;
                } else {
                    String chatUsename = null;
                    List<String> notNotifyIds = null;
                    // get user or group id which was blocked to show message notifications
                    if (message.getChatType() == ChatType.Chat) {
                        chatUsename = message.getFrom();
                        notNotifyIds = demoModel.getDisabledIds();
                    } else {
                        chatUsename = message.getTo();
                        notNotifyIds = demoModel.getDisabledGroups();
                    }

                    if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        });
        //set emoji icon provider
        easeUI.setEmojiconInfoProvider(new EaseEmojiconInfoProvider() {

            @Override
            public EaseEmojicon getEmojiconInfo(String emojiconIdentityCode) {
                EaseEmojiconGroupEntity data = EmojiconExampleGroupData.getData();
                for (EaseEmojicon emojicon : data.getEmojiconList()) {
                    if (emojicon.getIdentityCode().equals(emojiconIdentityCode)) {
                        return emojicon;
                    }
                }
                return null;
            }

            @Override
            public Map<String, Object> getTextEmojiconMapping() {
                return null;
            }
        });

        //set notification options, will use default if you don't set it
        easeUI.getNotifier().setNotificationInfoProvider(new EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, appContext);
                if (message.getType() == Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if (user != null) {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), user.getNick());
                    }
                    return user.getNick() + ": " + ticker;
                } else {
                    if (EaseAtMessageHelper.get().isAtMeMsg(message)) {
                        return String.format(appContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(appContext, ChatActivity.class);
                // open calling activity if there is call
                if (isVideoCalling) {
                    intent = new Intent(appContext, VideoCallActivity.class);
                } else if (isVoiceCalling) {
                    intent = new Intent(appContext, VoiceCallActivity.class);
                } else {
                    String type = message.getStringAttribute("type","0");
                    if (type==null||"".equals(type)||"0".equals(type)) {
                        ChatType chatType = message.getChatType();
                        if (chatType == ChatType.Chat) { // single chat message
                            intent.putExtra("userId", message.getFrom());
                            intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                        } else { // group chat message
                            // message.getTo() is the group id
                            intent.putExtra("userId", message.getTo());
                            if (chatType == ChatType.GroupChat) {
                                intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                            } else {
                                intent.putExtra("chatType", Constant.CHATTYPE_CHATROOM);
                            }
                        }
                    }else {
                        String chatImg = message.getStringAttribute(message.getFrom(),"0");
                        String name = message.getStringAttribute("name","0");
                        String shareRed = message.getStringAttribute("shareRed","无");
                        if (type.equals("单聊")) {
                            intent.putExtra("userId", message.getFrom());
                            intent.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                            intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                        }else if (type.equals("企业")) {
                            intent.putExtra("userId", message.getTo());
                            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                            intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "企业");
                        }else if (type.equals("群组")){
                            intent.putExtra("userId", message.getTo());
                            intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                            intent.putExtra(EaseConstant.EXTRA_USER_TYPE, "群组");
                        }
                        intent.putExtra(EaseConstant.EXTRA_USER_IMG,chatImg);
                        intent.putExtra(EaseConstant.EXTRA_USER_NAME,name);
                        intent.putExtra(EaseConstant.EXTRA_USER_SHARERED,shareRed);
                    }
                }
                return intent;
            }
        });
    }

    EMConnectionListener connectionListener;
    /**
     * set global listener
     */
    protected void setGlobalListeners() {
        syncGroupsListeners = new ArrayList<DataSyncListener>();
        syncContactsListeners = new ArrayList<DataSyncListener>();
        syncBlackListListeners = new ArrayList<DataSyncListener>();

        isGroupsSyncedWithServer = demoModel.isGroupsSynced();
        isContactsSyncedWithServer = demoModel.isContactSynced();
        isBlackListSyncedWithServer = demoModel.isBacklistSynced();
        // create the global connection listener
        connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                EMLog.d("global listener", "onDisconnect" + error);
                if (error == EMError.USER_REMOVED) {
                    onUserException(Constant.ACCOUNT_REMOVED);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    onUserException(Constant.ACCOUNT_CONFLICT);
                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
                    onUserException(Constant.ACCOUNT_FORBIDDEN);
                }
            }

            @Override
            public void onConnected() {
                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
                if (isGroupsSyncedWithServer) {
                    EMLog.d(TAG, "group and contact already synced with servre");
                } else {
                    if (!isGroupsSyncedWithServer) {
                        asyncFetchGroupsFromServer(null);
                    }

                    if (!isContactsSyncedWithServer) {
                        asyncFetchContactsFromServer(null);
                    }

                    if (!isBlackListSyncedWithServer) {
                        asyncFetchBlackListFromServer(null);
                    }
                }
            }
        };

        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }

        //register incoming call receiver
        appContext.registerReceiver(callReceiver, callFilter);
        //register connection listener
        EMClient.getInstance().addConnectionListener(connectionListener);
        //register group and contact event listener
        registerGroupAndContactListener();
        //register message event listener
        registerMessageListener();

    }

    private void initDbDao() {
        inviteMessgeDao = new InviteMessgeDao(appContext);
        userDao = new UserDao(appContext);
    }

    /**
     * register group and contact listener, you need register when login
     */
    public void registerGroupAndContactListener() {
        if (!isGroupAndContactListenerRegisted) {
            EMClient.getInstance().groupManager().addGroupChangeListener(new MyGroupChangeListener());
            EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
            isGroupAndContactListenerRegisted = true;
        }

    }

    /**
     * group change listener
     */
    class MyGroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            appContext.startService(new Intent(appContext, GroupService.class));
        }

        @Override
        public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {
            appContext.startService(new Intent(appContext, GroupService.class));
        }

        @Override
        public void onRequestToJoinAccepted(String s, String s1, String s2) {
            appContext.startService(new Intent(appContext, GroupService.class));
        }

        @Override
        public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {
            appContext.startService(new Intent(appContext, GroupService.class));
        }

        @Override
        public void onInvitationAccepted(String groupId, String invitee, String reason) {
            appContext.startService(new Intent(appContext, GroupService.class));
        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {
            appContext.startService(new Intent(appContext, GroupService.class));
        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            //user is removed from group
            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onGroupDestroyed(String groupId, String groupName) {
            appContext.startService(new Intent(appContext, GroupService.class));
//            // group is dismissed,
            //       broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
//            // got an invitation
            appContext.startService(new Intent(appContext, GroupService.class));
//            String st3 = appContext.getString(R.string.Invite_you_to_join_a_group_chat);
//            EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
//            msg.setChatType(ChatType.GroupChat);
//            msg.setFrom(inviter);
//            msg.setTo(groupId);
//            msg.setMsgId(UUID.randomUUID().toString());
//            msg.addBody(new EMTextMessageBody(inviter + " " + st3));
//            msg.setStatus(EMMessage.Status.SUCCESS);
//            // save invitation as messages
//            EMClient.getInstance().chatManager().saveMessage(msg);
//            // notify invitation message
//            getNotifier().vibrateAndPlayTone(msg);
//            EMLog.d(TAG, "onAutoAcceptInvitationFromGroup groupId:" + groupId);
//            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onMuteListAdded(String s, List<String> list, long l) {
        }

        @Override
        public void onMuteListRemoved(String s, List<String> list) {
        }

        @Override
        public void onAdminAdded(String s, String s1) {
        }

        @Override
        public void onAdminRemoved(String s, String s1) {
        }

        @Override
        public void onOwnerChanged(String s, String s1, String s2) {
        }

        @Override
        public void onMemberJoined(String s, String s1) {
        }

        @Override
        public void onMemberExited(String s, String s1) {
        }

        @Override
        public void onAnnouncementChanged(String s, String s1) {
        }

        @Override
        public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {
        }

        @Override
        public void onSharedFileDeleted(String s, String s1) {
        }
    }

    /***
     * 好友变化listener
     */
    public class MyContactListener implements EMContactListener {

        @Override
        public void onContactAdded(String username) {
            // save contact
            Map<String, EaseUser> localUsers = getContactList();
            Map<String, EaseUser> toAddUsers = new HashMap<String, EaseUser>();
            EaseUser user = new EaseUser(username);

            if (!localUsers.containsKey(username)) {
                userDao.saveContact(user);
            }
            toAddUsers.put(username, user);
            localUsers.putAll(toAddUsers);

            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onContactDeleted(String username) {
            Map<String, EaseUser> localUsers = DemoHelper.getInstance().getContactList();
            localUsers.remove(username);
            userDao.deleteContact(username);
            inviteMessgeDao.deleteMessage(username);

            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onContactInvited(String username, String reason) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    inviteMessgeDao.deleteMessage(username);
                }
            }
            // save invitation as message
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            Log.d(TAG, username + "apply to be your friend,reason: " + reason);
            // set invitation status
            msg.setStatus(InviteMesageStatus.BEINVITEED);
            notifyNewInviteMessage(msg);
            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onFriendRequestAccepted(String username) {
            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // save invitation as message
            InviteMessage msg = new InviteMessage();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            Log.d(TAG, username + "accept your request");
            msg.setStatus(InviteMesageStatus.BEAGREED);
            notifyNewInviteMessage(msg);
            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onFriendRequestDeclined(String s) {
            // your request was refused
            Log.d(username, username + " refused to your request");
        }

    }

    /**
     * save and notify invitation message
     *
     * @param msg
     */
    private void notifyNewInviteMessage(InviteMessage msg) {
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(appContext);
        }
        inviteMessgeDao.saveMessage(msg);
        //increase the unread message count
        inviteMessgeDao.saveUnreadMessageCount(1);
        // notify there is new message
        getNotifier().vibrateAndPlayTone(null);
    }

    /**
     * user met some exception: conflict, removed or forbidden
     */
    protected void onUserException(String exception){
        EMLog.e(TAG, "onUserException: " + exception);
        try {
            Intent intent = new Intent(appContext, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(exception, true);
            appContext.startActivity(intent);
        }catch (Exception e){
            if (e!=null){
                System.out.print(e);
            }
        }
    }
    /**
     * user has logged into another device
     */
    protected void onConnectionConflict() {
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ACCOUNT_CONFLICT, true);
        appContext.startActivity(intent);
    }

    /**
     * account is removed
     */
    protected void onCurrentAccountRemoved() {
        Intent intent = new Intent(appContext, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constant.ACCOUNT_REMOVED, true);
        appContext.startActivity(intent);
    }

    private EaseUser getUserInfo(String username){
        // To get instance of EaseUser, here we get it from the user list in memory
        // You'd better cache it if you get it from your server
        EaseUser user = null;
        if(username.equals(EMClient.getInstance().getCurrentUser()))
            return getUserProfileManager().getCurrentUserInfo();
//        user = getContactList().get(username);
//        if(user == null && getRobotList() != null){
//            user = getRobotList().get(username);
//        }
        if (user == null){
            UserApiModel userInfo = UserInfoCacheSvc.getByChatUserName(username);
            if (userInfo != null){
                user = new EaseUser(username);
                user.setAvatar(userInfo.getHeadImg());
                user.setNick(userInfo.getUsername());
            }
        }
        // if user is not in your contacts, set inital letter for him/her
        if(user == null){
            user = new EaseUser(username);
            EaseCommonUtils.setUserInitialLetter(user);
        }
        return user;
    }

    /**
     * Global listener
     * If this event already handled by an activity, you don't need handle it again
     * activityList.size() <= 0 means all activities already in background or not in Activity Stack
     */
    protected void registerMessageListener() {
        messageListener = new EMMessageListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    message.setMsgTime(System.currentTimeMillis());
                    String userName = message.getStringAttribute("userName","");
                    String userPic = message.getStringAttribute("userPic","");
                    String hxidFrom = message.getFrom();
                    EaseUser easeUser = new EaseUser(hxidFrom);
                    easeUser.setAvatar(userPic);
                    easeUser.setNick(userName);
                    // 先将头像和昵称保存在本地缓存
                    UserInfoCacheSvc.createOrUpdate(hxidFrom, userName, userPic);

                    getContactList();
                    contactList.put(hxidFrom,easeUser);
                    UserDao dao = new UserDao(DemoApplication.getApp());
                    List<EaseUser> users = new ArrayList<>();
                    users.add(easeUser);
                    dao.saveContactList(users);
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    // in background, do not refresh UI, notify it in notification bar
                    if (!easeUI.hasForegroundActivies()&&message.getChatType()!=ChatType.ChatRoom) {
                        getNotifier().onNewMsg(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "receive command message");
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//get your predefined action
                    if (!easeUI.hasForegroundActivies()) {
//                        if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                            RedPacketUtil.receiveRedPacketAckMessage(message);
//                            broadcastManager.sendBroadcast(new Intent(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION));
//                            Log.d("ack_redpacket-->","11111");
//                        }
                    }
                    if (action.equals(FXConstant.CMD_ADD_FRIEND)) {
                        try {
                            String userInfo = message.getStringAttribute(FXConstant.KEY_USER_INFO);
                            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();

                            for (InviteMessage inviteMessage : msgs) {
                                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(message.getFrom())) {
                                    inviteMessgeDao.deleteMessage(message.getFrom());
                                }
                            }
                            // save invitation as message
                            InviteMessage msg = new InviteMessage();
                            msg.setFrom(message.getFrom());
                            msg.setTime(System.currentTimeMillis());
                            msg.setReason(userInfo);
                            Log.d(TAG, message.getFrom() + "apply to be your friend,reason: " + userInfo);
                            // set invitation status
                            msg.setStatus(InviteMesageStatus.BEINVITEED);
                            notifyNewInviteMessage(msg);
                            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));

                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }

                    } else if (action.equals(FXConstant.CMD_AGREE_FRIEND)) {
                        try {
                            String userInfo = message.getStringAttribute(FXConstant.KEY_USER_INFO);
                            List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
                            for (InviteMessage inviteMessage : msgs) {
                                if (inviteMessage.getFrom().equals(message.getFrom())) {
                                    return;
                                }
                            }
                            // save invitation as message
                            InviteMessage msg = new InviteMessage();
                            msg.setFrom(message.getFrom());
                            msg.setReason(userInfo);
                            msg.setTime(System.currentTimeMillis());
                            Log.d(TAG, message.getFrom() + "accept your request");
                            msg.setStatus(InviteMesageStatus.BEAGREED);
                            notifyNewInviteMessage(msg);
                            EaseUser user= JSONUtil.Json2User(JSONObject.parseObject(userInfo));
                            getContactList().put(user.getUsername(),user);
                            userDao.saveContact(user);
                            broadcastManager.sendBroadcast(new Intent(Constant.ACTION_CONTACT_CHANAGED));
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }

                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action, message.toString()));
                }
            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }
//    protected void registerMessageListener() {
//        messageListener = new EMMessageListener() {
//            private BroadcastReceiver broadCastReceiver = null;
//
//            @Override
//            public void onMessageReceived(List<EMMessage> messages) {
//                for (EMMessage message : messages) {
//                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
//                    // in background, do not refresh UI, notify it in notification bar
//                    if(!easeUI.hasForegroundActivies()){
//                        getNotifier().onNewMsg(message);
//                    }
//                }
//            }
//
//            @Override
//            public void onCmdMessageReceived(List<EMMessage> messages) {
//                for (EMMessage message : messages) {
//                    EMLog.d(TAG, "receive command message");
//                    //get message body
//                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//                    final String action = cmdMsgBody.action();//获取自定义action
//                    //red packet code : 处理红包回执透传消息
//                    if(!easeUI.hasForegroundActivies()){
//                        if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                            RedPacketUtil.receiveRedPacketAckMessage(message);
//                            broadcastManager.sendBroadcast(new Intent(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION));
//                        }
//                    }
//                    //end of red packet code
//                    //获取扩展属性 此处省略
//                    //maybe you need get extension of your message
//                    //message.getStringAttribute("");
//                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action,message.toString()));
//                }
//            }
//
//            @Override
//            public void onMessageReadAckReceived(List<EMMessage> messages) {
//            }
//
//            @Override
//            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
//            }
//
//            @Override
//            public void onMessageChanged(EMMessage message, Object change) {
//
//            }
//        };
//
//        EMClient.getInstance().chatManager().addMessageListener(messageListener);
//    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    public boolean isLoggedIn(Context context) {
        SharedPreferences sp = context.getSharedPreferences("sangu_denglu_info", Context.MODE_PRIVATE);
        boolean isLogedIn = sp.getBoolean("isLogedIn",false);
        return isLogedIn;
    }
    /**
     * logout
     *
     * @param unbindDeviceToken whether you need unbind your device token
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        Log.d(TAG, "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    /**
     * get instance of EaseNotifier
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return easeUI.getNotifier();
    }

    public DemoModel getModel() {
        return (DemoModel) demoModel;
    }

    /**
     * update contact list
     *
     * @param
     */
    public void setContactList(Map<String, EaseUser> aContactList) {
        if (aContactList == null) {
            if (contactList != null) {
                contactList.clear();
            }
            return;
        }

        contactList = aContactList;
    }

    /**
     * save single contact
     */
    public void saveContact(EaseUser user) {
        contactList.put(user.getUsername(), user);
        demoModel.saveContact(user);
    }

    /**
     * update user list to cache and database
     *
     * @param
     */
    public void updateBlackList(List<EaseUser> blackInfoList) {
        for (EaseUser u : blackInfoList) {
            blackList.put(u.getUsername(), u);
        }
        ArrayList<EaseUser> mList = new ArrayList<EaseUser>();
        mList.addAll(blackList.values());
        demoModel.saveBlackList(mList);
    }

    /**
     * update contact list
     *
     * @param
     */
    public void setBlackList(Map<String, EaseUser> aBlackList) {
        if (aBlackList == null) {
            if (blackList != null) {
                blackList.clear();
            }
            return;
        }
        blackList = aBlackList;
    }

    /**
     * delete single contact
     */
    public void deleteBlack(String username) {
        userDao.deleteBlack(username);
    }
    /**
     * save single contact
     */
    public void saveBlack(EaseUser user) {
        blackList.put(user.getUsername(), user);
        demoModel.saveBlack(user);
    }

    /**
     * get contact list
     *
     * @return
     */
    public Map<String, EaseUser> getBlackList() {
        if (isLoggedIn() && blackList == null) {
            blackList = demoModel.getBlackList();
        }

        // return a empty non-null object to avoid app crash
        if (blackList == null) {
            return new Hashtable<String, EaseUser>();
        }
        return blackList;
    }

    /**
     * get contact list
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        if (isLoggedIn() && contactList == null) {
            contactList = demoModel.getContactList();
        }

        // return a empty non-null object to avoid app crash
        if (contactList == null) {
            return new Hashtable<String, EaseUser>();
        }

        return contactList;
    }

    /**
     * set current username
     *
     * @param username
     */
    public void setCurrentUserName(String username) {
        this.username = username;
        demoModel.setCurrentUserName(username);
    }

    /**
     * get current user's id
     */

    public String getCurrentUsernName() {
        if (username == null) {
            username = demoModel.getCurrentUsernName();
        }
        return username;
    }

    public void setRobotList(Map<String, RobotUser> robotList) {
        this.robotList = robotList;
    }

    public Map<String, RobotUser> getRobotList() {
        if (isLoggedIn() && robotList == null) {
            robotList = demoModel.getRobotList();
        }
        return robotList;
    }

    /**
     * update user list to cache and database
     *
     * @param
     */
    public void updateContactList(List<EaseUser> contactInfoList) {
        for (EaseUser u : contactInfoList) {
            contactList.put(u.getUsername(), u);
        }
        ArrayList<EaseUser> mList = new ArrayList<EaseUser>();
        mList.addAll(contactList.values());
        demoModel.saveContactList(mList);
    }

    public UserProfileManager getUserProfileManager() {
        if (userProManager == null) {
            userProManager = new UserProfileManager();
        }
        return userProManager;
    }

    void endCall() {
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSyncGroupListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncGroupsListeners.contains(listener)) {
            syncGroupsListeners.add(listener);
        }
    }

    public void removeSyncGroupListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncGroupsListeners.contains(listener)) {
            syncGroupsListeners.remove(listener);
        }
    }

    public void addSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncContactsListeners.contains(listener)) {
            syncContactsListeners.add(listener);
        }
    }

    public void removeSyncContactListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncContactsListeners.contains(listener)) {
            syncContactsListeners.remove(listener);
        }
    }

    public void addSyncBlackListListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (!syncBlackListListeners.contains(listener)) {
            syncBlackListListeners.add(listener);
        }
    }

    public void removeSyncBlackListListener(DataSyncListener listener) {
        if (listener == null) {
            return;
        }
        if (syncBlackListListeners.contains(listener)) {
            syncBlackListListeners.remove(listener);
        }
    }

    /**
     * Get group list from server
     * This method will save the sync state
     *
     * @throws HyphenateException
     */
    public synchronized void asyncFetchGroupsFromServer(final EMCallBack callback) {
        if (isSyncingGroupsWithServer) {
            return;
        }

        isSyncingGroupsWithServer = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if (!isLoggedIn()) {
                        isGroupsSyncedWithServer = false;
                        isSyncingGroupsWithServer = false;
                        noitifyGroupSyncListeners(false);
                        return;
                    }

                    demoModel.setGroupsSynced(true);

                    isGroupsSyncedWithServer = true;
                    isSyncingGroupsWithServer = false;

                    //notify sync group list success
                    noitifyGroupSyncListeners(true);

                    if (callback != null) {
                        callback.onSuccess();
                    }
                } catch (HyphenateException e) {
                    demoModel.setGroupsSynced(false);
                    isGroupsSyncedWithServer = false;
                    isSyncingGroupsWithServer = false;
                    noitifyGroupSyncListeners(false);
                    if (callback != null) {
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void noitifyGroupSyncListeners(boolean success) {
        for (DataSyncListener listener : syncGroupsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchContactsFromServer(final EMValueCallBack<List<String>> callback){
        if(isSyncingContactsWithServer){
            return;
        }

        isSyncingContactsWithServer = true;

        new Thread(){
            @Override
            public void run(){
                List<String> usernames = null;
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    // in case that logout already before server returns, we should return immediately
                    if(!isLoggedIn()){
                        isContactsSyncedWithServer = false;
                        isSyncingContactsWithServer = false;
                        notifyContactsSyncListener(false);
                        return;
                    }

                    Map<String, EaseUser> userlist = new HashMap<String, EaseUser>();
                    for (String username : usernames) {
                        EaseUser user = new EaseUser(username);
                        EaseCommonUtils.setUserInitialLetter(user);
                        userlist.put(username, user);
                    }
                    // save the contact list to cache
                    getContactList().clear();
                    getContactList().putAll(userlist);
                    // save the contact list to database
                    UserDao dao = new UserDao(appContext);
                    List<EaseUser> users = new ArrayList<EaseUser>(userlist.values());
                    dao.saveContactList(users);

                    demoModel.setContactSynced(true);
                    EMLog.d(TAG, "set contact syn status to true");

                    isContactsSyncedWithServer = true;
                    isSyncingContactsWithServer = false;

                    //notify sync success
                    notifyContactsSyncListener(true);

                    getUserProfileManager().asyncFetchContactInfosFromServer(usernames,new EMValueCallBack<List<EaseUser>>() {

                        @Override
                        public void onSuccess(List<EaseUser> uList) {
                            updateContactList(uList);
                            getUserProfileManager().notifyContactInfosSyncListener(true);
                        }

                        @Override
                        public void onError(int error, String errorMsg) {
                        }
                    });
                    if(callback != null){
                        callback.onSuccess(usernames);
                    }
                } catch (HyphenateException e) {
                    demoModel.setContactSynced(false);
                    isContactsSyncedWithServer = false;
                    isSyncingContactsWithServer = false;
                    notifyContactsSyncListener(false);
                    e.printStackTrace();
                    if(callback != null){
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyContactsSyncListener(boolean success){
        for (DataSyncListener listener : syncContactsListeners) {
            listener.onSyncComplete(success);
        }
    }

    public void asyncFetchBlackListFromServer(final EMValueCallBack<List<String>> callback){

        if(isSyncingBlackListWithServer){
            return;
        }

        isSyncingBlackListWithServer = true;

        new Thread(){
            @Override
            public void run(){
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getBlackListFromServer();

                    // in case that logout already before server returns, we should return immediately
                    if(!isLoggedIn()){
                        isBlackListSyncedWithServer = false;
                        isSyncingBlackListWithServer = false;
                        notifyBlackListSyncListener(false);
                        return;
                    }

                    demoModel.setBlacklistSynced(true);

                    isBlackListSyncedWithServer = true;
                    isSyncingBlackListWithServer = false;

                    notifyBlackListSyncListener(true);
                    if(callback != null){
                        callback.onSuccess(usernames);
                    }
                } catch (HyphenateException e) {
                    demoModel.setBlacklistSynced(false);

                    isBlackListSyncedWithServer = false;
                    isSyncingBlackListWithServer = true;
                    e.printStackTrace();

                    if(callback != null){
                        callback.onError(e.getErrorCode(), e.toString());
                    }
                }

            }
        }.start();
    }

    public void notifyBlackListSyncListener(boolean success){
        for (DataSyncListener listener : syncBlackListListeners) {
            listener.onSyncComplete(success);
        }
    }

    public boolean isSyncingGroupsWithServer() {
        return isSyncingGroupsWithServer;
    }

    public boolean isSyncingContactsWithServer() {
        return isSyncingContactsWithServer;
    }

    public boolean isSyncingBlackListWithServer() {
        return isSyncingBlackListWithServer;
    }

    public boolean isGroupsSyncedWithServer() {
        return isGroupsSyncedWithServer;
    }

    public boolean isContactsSyncedWithServer() {
        return isContactsSyncedWithServer;
    }

    public boolean isBlackListSyncedWithServer() {
        return isBlackListSyncedWithServer;
    }

    synchronized void reset() {
        isSyncingGroupsWithServer = false;
        isSyncingContactsWithServer = false;
        isSyncingBlackListWithServer = false;

        demoModel.setGroupsSynced(false);
        demoModel.setContactSynced(false);
        demoModel.setBlacklistSynced(false);

        isGroupsSyncedWithServer = false;
        isContactsSyncedWithServer = false;
        isBlackListSyncedWithServer = false;


        isGroupAndContactListenerRegisted = false;

        setContactList(null);
        setBlackList(null);
        setRobotList(null);
        getUserProfileManager().reset();
        DemoDBManager.getInstance().closeDB();
    }

    public void pushActivity(Activity activity) {
        easeUI.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        easeUI.popActivity(activity);
    }

}