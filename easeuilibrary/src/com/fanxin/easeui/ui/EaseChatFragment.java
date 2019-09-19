package com.fanxin.easeui.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fanxin.easeui.EaseConstant;
import com.fanxin.easeui.adapter.EaseMessageAdapter;
import com.fanxin.easeui.bean.ChatMsg;
import com.fanxin.easeui.controller.EaseUI;
import com.fanxin.easeui.domain.EaseEmojicon;
import com.fanxin.easeui.domain.EaseUser;
import com.fanxin.easeui.model.EaseAtMessageHelper;
import com.fanxin.easeui.utils.Constant;
import com.fanxin.easeui.utils.EaseCommonUtils;
import com.fanxin.easeui.utils.EaseUserUtils;
import com.fanxin.easeui.utils.UserManager;
import com.fanxin.easeui.widget.EaseAlertDialog;
import com.fanxin.easeui.widget.EaseChatExtendMenu;
import com.fanxin.easeui.widget.EaseChatInputMenu;
import com.fanxin.easeui.widget.EaseChatMessageList;
import com.fanxin.easeui.widget.EaseVoiceRecorderView;
import com.fanxin.easeui.widget.EaseVoiceRecorderView.EaseVoiceRecorderCallback;
import com.fanxin.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;
import com.xiaomi.mimc.MIMCGroupMessage;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCServerAck;
import com.xiaomi.mimc.MIMCUser;
import com.xiaomi.mimc.cipher.Base64;
import com.xiaomi.mimc.common.MIMCConstant;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * you can new an EaseChatFragment to use or you can inherit it to expand.
 * You need call setArguments to pass chatType and userId 
 * <br/>
 * <br/>
 * you can see ChatActivity in demo for your reference
 *
 */
public class EaseChatFragment extends EaseBaseFragment implements EMMessageListener, UserManager.OnHandleMIMCMsgListener {
    protected static final String TAG = "EaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;

    /**
     * params to fragment
     */
    protected Bundle fragmentArgs;
    protected int chatType;
    protected String toChatUsername,type;
    protected EaseChatMessageList messageList;
    protected EaseChatInputMenu inputMenu;

    protected EMConversation conversation;
    private EaseMessageAdapter eAdapter;
    protected InputMethodManager inputManager;
    protected ClipboardManager clipboard;

    protected Handler handler = new Handler();
    protected File cameraFile;
    protected EaseVoiceRecorderView voiceRecorderView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView listView;

    protected boolean isloading;
    protected boolean haveMoreData = true;
    protected int pagesize = 20;
    protected GroupListener groupListener;
    protected EMMessage contextMenuMessage;
    
    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;
    
    protected int[] itemStrings = { R.string.attach_take_pic, R.string.attach_picture, R.string.attach_location };
    protected int[] itemdrawables = { R.drawable.ease_chat_takepic_selector, R.drawable.ease_chat_image_selector,
            R.drawable.ease_chat_location_selector };
    protected int[] itemIds = { ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_LOCATION };
    private EMChatRoomChangeListener chatRoomChangeListener;
    public boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_chat, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        fragmentArgs = getArguments();
        // check if single chat or group chat
        chatType = fragmentArgs.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        // userId you are chat with or group id
        toChatUsername = fragmentArgs.getString(EaseConstant.EXTRA_USER_ID);
        // check type from chat Attributes
        type = fragmentArgs.getString(EaseConstant.EXTRA_USER_TYPE);


        UserManager userManager = UserManager.getInstance();
        userManager.setHandleMIMCMsgListener(this);

        super.onActivityCreated(savedInstanceState);
    }

    /**
     * init view
     */
    protected void initView() {
        // hold to record voice
        voiceRecorderView = (EaseVoiceRecorderView) getView().findViewById(R.id.voice_recorder);

        // message list layout
        messageList = (EaseChatMessageList) getView().findViewById(R.id.message_list);
        if(chatType != EaseConstant.CHATTYPE_SINGLE)
            messageList.setShowUserNick(true);
        listView = messageList.getListView();

        extendMenuItemClickListener = new MyItemClickListener();
        inputMenu = (EaseChatInputMenu) getView().findViewById(R.id.input_menu);
        registerExtendMenuItem();
        // init input menu
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.RECORD_AUDIO},1);
                    return false;
                }else {
                    return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderCallback() {

                        @Override
                        public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                            sendVoiceMessage(voiceFilePath, voiceTimeLength);
                        }
                    });
                }
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });

        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    protected void setUpView() {
        titleBar.setTitle(toChatUsername);
        if (chatType == EaseConstant.CHATTYPE_SINGLE) {
            // set title
            if(EaseUserUtils.getUserInfo(toChatUsername) != null){
                titleBar.setTitle(EaseUserUtils.getUserInfo(toChatUsername).getNick());
            }
            titleBar.setRightImageResource(R.drawable.ease_mm_title_remove);
        } else {
        	titleBar.setRightImageResource(R.drawable.ease_to_group_details_normal);
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                //group chat
                EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
                if (group != null)
                    titleBar.setTitle(group.getGroupName());
                // listen the event that user moved out group or group is dismissed
                groupListener = new GroupListener();
                EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);
            } else {
                onChatRoomViewCreation();
            }

        }
        if (chatType != EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
        }

        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                    emptyHistory();
                } else {
                    toGroupDetails();
                }
            }
        });

        setRefreshLayoutListener();
        
        // show forward message if the message is not null
        String forward_msg_id = getArguments().getString("forward_msg_id");
        if (forward_msg_id != null) {
            forwardMessage(forward_msg_id);
        }
    }
    
    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem(){
        for(int i = 0; i < itemStrings.length; i++){
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }
    
    
    protected void onConversationInit(){
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }
        
    }
    
    protected void onMessageListInit(){
        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ? 
                chatFragmentHelper.onSetCustomChatRowProvider() : null);
        setListItemClickListener();
        
        messageList.getListView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                inputMenu.hideExtendMenuContainer();
                return false;
            }
        });
        
        isMessageListInited = true;
    }
    
    protected void setListItemClickListener() {
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {
            
            @Override
            public void onUserAvatarClick(String username) {
                if(chatFragmentHelper != null){
                    chatFragmentHelper.onAvatarClick(username);
                }
            }
            
            @Override
            public void onUserAvatarLongClick(String username) {
                if(chatFragmentHelper != null){
                    chatFragmentHelper.onAvatarLongClick(username);
                }
            }
            
            @Override
            public void onResendClick(final EMMessage message) {
                new EaseAlertDialog(getActivity(), R.string.resend, R.string.confirm_resend, null, new EaseAlertDialog.AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        resendMessage(message);
                    }
                }, true).show();
            }
            
            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;
                if(chatFragmentHelper != null){
                    chatFragmentHelper.onMessageBubbleLongClick(message);
                }
            }
            
            @Override
            public boolean onBubbleClick(EMMessage message) {
                if(chatFragmentHelper != null){
                    eAdapter = messageList.getAdapter();
                    return chatFragmentHelper.onMessageBubbleClick(message,eAdapter);
                }
                return false;
            }

        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                            List<EMMessage> messages;
                            try {
                                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                                    messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                } else {
                                    messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                }
                            } catch (Exception e1) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }
                            if (messages.size() > 0) {
                                messageList.refreshSeekTo(messages.size() - 1);
                                if (messages.size() != pagesize) {
                                    haveMoreData = false;
                                }
                            } else {
                                haveMoreData = false;
                            }

                            isloading = false;

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 600);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) { 
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_MAP) { // location
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress);
                } else {
                    Toast.makeText(getActivity(), R.string.unable_to_get_loaction, Toast.LENGTH_SHORT).show();
                }
                
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(isMessageListInited)
            messageList.refresh();
        EaseUI.getInstance().pushActivity(getActivity());
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);
        
        if(chatType == EaseConstant.CHATTYPE_GROUP){
            EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(this);

        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
        if (groupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        }
        if(chatType == EaseConstant.CHATTYPE_CHATROOM){
            EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
        }
        
        if(chatRoomChangeListener != null){
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(chatRoomChangeListener);
        }
        
    }

    public void onBackPressed() {
        if (inputMenu!=null&&inputMenu.onBackPressed()) {
            getActivity().finish();
            if(chatType == EaseConstant.CHATTYPE_GROUP){
                EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
                EaseAtMessageHelper.get().cleanToAtUserList();
            }
            if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
                EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
            }
        }
    }

    protected void onChatRoomViewCreation() {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Joining......");
        EMClient.getInstance().chatroomManager().joinChatRoom(toChatUsername, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(final EMChatRoom value) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(getActivity().isFinishing() || !toChatUsername.equals(value.getId()))
                            return;
                        pd.dismiss();
                        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(toChatUsername);
                        if (room != null) {
                            titleBar.setTitle(room.getName());
                            EMLog.d(TAG, "join room success : " + room.getName());
                        } else {
                            titleBar.setTitle(toChatUsername);
                        }
                        addChatRoomChangeListenr();
                        onConversationInit();
                        onMessageListInit();
                    }
                });
            }

            @Override
            public void onError(final int error, String errorMsg) {
                // TODO Auto-generated method stub
                EMLog.d(TAG, "join room failure : " + error);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
                getActivity().finish();
            }
        });
    }
    

    protected void addChatRoomChangeListenr() {
        chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(toChatUsername)) {
                    showChatroomToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                    getActivity().finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                showChatroomToast("member : " + participant + " join the room : " + roomId);
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                showChatroomToast("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
            }

            @Override
            public void onRemovedFromChatRoom(String roomId, String roomName, String participant) {
                if (roomId.equals(toChatUsername)) {
                    String curUser = EMClient.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                        EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
                        getActivity().finish();
                    }else{
                        showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                    }
                }
            }

            @Override
            public void onMuteListAdded(String s, List<String> list, long l) {
                if (s.equals(toChatUsername)) {
                    for (String str:list){
                        showChatroomToast("member : " + str + " join the room : " + s);
                    }
                }
            }

            @Override
            public void onMuteListRemoved(String s, List<String> list) {
                if (s.equals(toChatUsername)) {
                    for (String str:list){
                        showChatroomToast("member : " + str + " was kicked from the room : " + s);
                    }
                }
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
            public void onAnnouncementChanged(String s, String s1) {

            }

        };
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }


    
    protected void showChatroomToast(final String toastContent){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), toastContent, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // implement methods in EMMessageListener
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation

            String str1 = message.getFrom();
            String str2 = message.getTo();
            String str3 = message.getUserName();
            String str4 = message.getMsgId();
            String str5 = message.conversationId();

            if (username.equals(toChatUsername)) {
                messageList.refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
    }

    @Override
    public void onMessageRead(List<EMMessage> list) {
        if(isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {
        if(isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {
        if(isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        if(isMessageListInited) {
            messageList.refresh();
        }
    }
    
    
    /**
     * handle the click event for extend menu
     *
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            if(chatFragmentHelper != null){
                if(chatFragmentHelper.onExtendMenuItemClick(itemId, view)){
                    return;
                }
            }
            switch (itemId) {
            case ITEM_TAKE_PICTURE:
                selectPicFromCamera();
                break;
            case ITEM_PICTURE:
                selectPicFromLocal();
                break;
            case ITEM_LOCATION:
                startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                break;

            default:
                break;
            }
        }

    }
    
    /**
     * input @
     * @param username
     */
    protected void inputAtUsername(String username, boolean autoAddAtSymbol){
        if(EMClient.getInstance().getCurrentUser().equals(username) ||
                chatType != EaseConstant.CHATTYPE_GROUP){
            return;
        }
        EaseAtMessageHelper.get().addAtUser(username);
        EaseUser user = EaseUserUtils.getUserInfo(username);
        if (user != null){
            username = user.getNick();
        }
        if(autoAddAtSymbol)
            inputMenu.insertText("@" + username + " ");
        else
            inputMenu.insertText(username + " ");
    }
    
    
    /**
     * input @
     * @param username
     */
    protected void inputAtUsername(String username){
        inputAtUsername(username, true);
    }
    

    //send message
    protected void sendTextMessage(String content) {
        if(EaseAtMessageHelper.get().containsAtUsername(content)){
            sendAtMessage(content);
        }else{
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
            sendMessage(message);
        }
    }
    
    /**
     * send @ message, only support group chat message
     * @param content
     */
    @SuppressWarnings("ConstantConditions")
    private void sendAtMessage(String content){
        if(chatType != EaseConstant.CHATTYPE_GROUP){
            EMLog.e(TAG, "only support group chat message");
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
        if(EMClient.getInstance().getCurrentUser().equals(group.getOwner()) && EaseAtMessageHelper.get().containsAtAll(content)){
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, EaseConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);
        }else {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG,
                    EaseAtMessageHelper.get().atListToJsonArray(EaseAtMessageHelper.get().getAtMessageUsernames(content)));
        }
        sendMessage(message);

    }
    
    
    protected void sendBigExpressionMessage(String name, String identityCode){
        EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        String size = EaseUserUtils.getAutoFileOrFilesSize(imagePath);
        EMMessage message;
        if (size!=null&&Double.parseDouble(size)<2024.00) {
            message = EMMessage.createImageSendMessage(imagePath, true, toChatUsername);
        }else {
            message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        }
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }

    protected void sendMessage(EMMessage message){
        if (message == null) {
            return;
        }
        if(chatFragmentHelper != null){
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP){
            message.setChatType(ChatType.GroupChat);
        }else if(chatType == EaseConstant.CHATTYPE_CHATROOM){
            message.setChatType(ChatType.ChatRoom);
        }

        //改成发送小米的文字消息
        UserManager userManager = UserManager.getInstance();

        MIMCUser user = userManager.getUser();
        if (user != null){

            Map<String,Object> map = new HashMap<String, Object>();
            Map<String,Object> map2 = new HashMap<String, Object>();

            map2.put("name",message.getStringAttribute("name","0"));
            map2.put("shareRed",message.getStringAttribute("shareRed","0"));
            map2.put("type",message.getStringAttribute("type","0"));
            map2.put("userName",message.getStringAttribute("userName","0"));
            map2.put("userPic",message.getStringAttribute("userPic","0"));
            map2.put(message.getTo(),message.getStringAttribute(message.getTo(),"0"));
            map2.put(message.getFrom(),message.getStringAttribute(message.getFrom(),"0"));

            map.put("version","0");
            map.put("msgId","1");
            map.put("msgType","103");
            EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
            map.put("content",txtBody.getMessage().getBytes());
            map.put("messageId",message.getMsgId());
            map.put("conversationId",message.conversationId());
            map.put("direction","发送");
            map.put("from",message.getFrom());
            map.put("to",message.getTo());
            map.put("timestamp",message.getMsgTime()+"");
            map.put("localTime",message.localTime()+"");
            map.put("chatType","单聊");
            map.put("status","0");
            map.put("isReadAcked","0");
            map.put("isDeliverAcked","0");
            map.put("isRead","0");
            map.put("body","文字消息");
            map.put("ext",map2);

            userManager.sendMsg(message.getTo(),txtBody.getMessage().getBytes(), Constant.TEXT,map);

         //   aliyunPushWithId(message.getTo());

            messageList.refreshSelectLastMimc(message);

        }


        //send message
    //    EMClient.getInstance().chatManager().sendMessage(message);
        //refresh ui
    //    if(isMessageListInited) {
     //       messageList.refreshSelectLast();
     //   }
    }


    private void aliyunPushWithId (final String userId){

        new Thread(new Runnable() {
            @Override
            public void run() {

                String url = "http://www.fulu86.com/aliyunPush/push";

                String json = "{\"u_id\":" + userId + ",\"body\":\"" + "聊天消息" + "\",\"type\":\"" +
                        "98" + ",\"dType\":\"" + "0" + ",\"fristId\":\"" + "0" + ",\"createTime\":\"" +
                        "0" + ",\"dynamicSeq\":\"" + "0" + ",\"companyAdress\":\"" + "0" + ",\"companyId\":\"" +
                        "0"  +  ",\"companyName\":\"" + "0" + "\",\"userId\":\"" + userId + "\"}";

                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request
                        .Builder()
                        .url(url)
                        .post(RequestBody.create(JSON, json))
                        .build();

                Call call = client.newCall(request);
                org.json.JSONObject data = null;
                try {
                    Response response = call.execute();
                    data = new org.json.JSONObject(response.body().string());

                    if (data != null){


                    }

                } catch (Exception e) {
                    //logger.warn("Get token exception: " + e);
                    e.printStackTrace();
                }

            }

        }).start();

    }


    @Override
    public void onHandleMessageObjec(JSONObject jsonObject) {

        String content = jsonObject.getString("content");

        String ext = jsonObject.getString("ext");

        JSONObject jsonObject1 = JSONObject.parseObject(ext);

        //接收到消息了
        EMMessage message = EMMessage.createTxtSendMessage(content, jsonObject.getString("from"));

        message.setFrom(jsonObject.getString("from"));
        message.setTo(jsonObject.getString("to"));
        message.setMsgId(jsonObject.getString("messageId"));

        message.setAttribute(jsonObject.getString("from"),jsonObject1.getString(jsonObject.getString("from")));
        message.setAttribute(jsonObject.getString("to"),jsonObject1.getString(jsonObject.getString("to")));
        message.setAttribute("name",jsonObject1.getString("name"));
        message.setAttribute("shareRed",jsonObject1.getString("shareRed"));
        message.setAttribute("type",jsonObject1.getString("type"));
        message.setAttribute("userName",jsonObject1.getString("userName"));
        message.setAttribute("userPic",jsonObject1.getString("userPic"));

        message.setDirection(EMMessage.Direct.RECEIVE);

        String str1 = message.getFrom();
        String str2 = message.getTo();
        String str3 = message.getUserName();
        String str4 = message.getMsgId();
        String str5 = message.conversationId();

        messageList.refreshSelectLastMimc(message);
        EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
      //  EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);

    }

    @Override
    public void onHandleMessage(ChatMsg chatMsg) {

        String content = new String(chatMsg.getMsg().getContent());




        //接收到消息了
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);


        String str1 = message.getFrom();
        String str2 = message.getTo();
        String str3 = message.getUserName();
        String str4 = message.getMsgId();
        String str5 = message.conversationId();

        String username = message.getFrom();


        // if the message is for current conversation
        if (username.equals(toChatUsername)) {
           // messageList.refreshSelectLast();
            //EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
        } else {
            //EaseUI.getInstance().getNotifier().onNewMsg(message);
        }

    }

    @Override
    public void onHandleGroupMessage(ChatMsg chatMsg) {

    }

    @Override
    public void onHandleStatusChanged(MIMCConstant.OnlineStatus status) {

    }

    @Override
    public void onHandleServerAck(MIMCServerAck serverAck) {

    }

    @Override
    public void onHandleCreateGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQueryGroupInfo(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQueryGroupsOfAccount(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleJoinGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQuitGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleKickGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleUpdateGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleDismissGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandlePullP2PHistory(String json, boolean isSuccess) {

    }

    @Override
    public void onHandlePullP2THistory(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleSendMessageTimeout(MIMCMessage message) {

    }

    @Override
    public void onHandleSendGroupMessageTimeout(MIMCGroupMessage groupMessage) {

    }

    @Override
    public void onHandleJoinUnlimitedGroup(long topicId, int code, String errMsg) {

    }

    @Override
    public void onHandleQuitUnlimitedGroup(long topicId, int code, String errMsg) {

    }

    @Override
    public void onHandleDismissUnlimitedGroup(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQueryUnlimitedGroupMembers(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQueryUnlimitedGroups(String json, boolean isSuccess) {

    }

    @Override
    public void onHandleQueryUnlimitedGroupOnlineUsers(String json, boolean isSuccess) {

    }



    public void resendMessage(EMMessage message){
        message.setStatus(EMMessage.Status.CREATE);
        EMClient.getInstance().chatManager().sendMessage(message);
        messageList.refresh();
    }
    
    //===================================================================================
    

    /**
     * send image
     * 
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }
    
    /**
     * send file
     * @param uri
     */
    protected void sendFileByUri(Uri uri){
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;

            try {
                cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            Toast.makeText(getActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        //limit the size < 10M
        if (file.length() > 10 * 1024 * 1024) {
            Toast.makeText(getActivity(), R.string.The_file_is_not_greater_than_10_m, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * capture new image
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getActivity(), R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.sangu.app.fileprovider", cameraFile);//通过FileProvider创建一个content类型的Uri
            getActivity().grantUriPermission(getActivity().getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imageUri = Uri.fromFile(cameraFile);
        }
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, imageUri),REQUEST_CODE_CAMERA);
    }

    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }



    /**
     * clear the conversation history
     * 
     */
    protected void emptyHistory() {
        String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getActivity(),null, msg, null,new EaseAlertDialog.AlertDialogUser() {
            
            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if(confirmed){
                    EMClient.getInstance().chatManager().deleteConversation(toChatUsername, true);
                    messageList.refresh();
                }
            }
        }, true).show();;
    }

    /**
     * open group detail
     * 
     */
    protected void toGroupDetails() {
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            if(chatFragmentHelper != null){
                chatFragmentHelper.onEnterToChatDetails();
            }
        }else if(chatType == EaseConstant.CHATTYPE_CHATROOM){
        	if(chatFragmentHelper != null){
        	    chatFragmentHelper.onEnterToChatDetails();
        	}
        }
    }

    /**
     * hide
     */
    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**
     * forward message
     * 
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {
        final EMMessage forward_msg = EMClient.getInstance().chatManager().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
        case TXT:
            if(forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
                sendBigExpressionMessage(((EMTextMessageBody) forward_msg.getBody()).getMessage(),
                        forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
            }else{
                // get the content and send it
                String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                sendTextMessage(content);
            }
            break;
        case IMAGE:
            // send image
            String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
            if (filePath != null) {
                File file = new File(filePath);
                if (!file.exists()) {
                    // send thumb nail if original image does not exist
                    filePath = ((EMImageMessageBody) forward_msg.getBody()).thumbnailLocalPath();
                }
                sendImageMessage(filePath);
            }
            break;
        default:
            break;
        }
        
        if(forward_msg.getChatType() == EMMessage.ChatType.ChatRoom){
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }

    /**
     * listen the group event
     * 
     */
    class GroupListener extends EaseGroupRemoveListener {

        @Override
        public void onRequestToJoinReceived(String s, String s1, String s2, String s3) {

        }

        @Override
        public void onRequestToJoinAccepted(String s, String s1, String s2) {

        }

        @Override
        public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {

        }

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            getActivity().runOnUiThread(new Runnable() {

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.you_are_group, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onGroupDestroyed(final String groupId, String groupName) {
        	// prompt group is dismissed and finish this activity
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.the_current_group, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
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
    
   
    public EaseChatFragmentHelper chatFragmentHelper;
    public void setChatFragmentListener(EaseChatFragmentHelper chatFragmentHelper){
        this.chatFragmentHelper = chatFragmentHelper;
    }
    
    public interface EaseChatFragmentHelper{
        /**
         * set message attribute
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * enter to chat detail
         */
        void onEnterToChatDetails();
        
        /**
         * on avatar clicked
         * @param username
         */
        void onAvatarClick(String username);
        
        /**
         * on avatar long pressed
         * @param username
         */
        void onAvatarLongClick(String username);
        
        /**
         * on message bubble clicked
         */
        boolean onMessageBubbleClick(EMMessage message,EaseMessageAdapter adapter);
        
        /**
         * on message bubble long pressed
         */
        void onMessageBubbleLongClick(EMMessage message);
        
        /**
         * on extend menu item clicked, return true if you want to override
         * @param view 
         * @param itemId 
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);
        
        /**
         * on set custom chat row provider
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();
    }
    
}
