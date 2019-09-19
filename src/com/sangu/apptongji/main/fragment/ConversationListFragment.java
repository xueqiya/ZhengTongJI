package com.sangu.apptongji.main.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanxin.easeui.bean.ChatMsg;
import com.fanxin.easeui.utils.UserManager;
import com.fanxin.easeui.widget.EaseConversationList;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.db.InviteMessgeDao;
import com.sangu.apptongji.main.activity.BaojiaListActivity;
import com.sangu.apptongji.main.activity.JieDanSettingActivity;
import com.sangu.apptongji.main.activity.NewFriendsActivity;
import com.sangu.apptongji.main.activity.PaidanListActivity;
import com.sangu.apptongji.main.activity.PaidanStatistics;
import com.sangu.apptongji.main.activity.TradeTrackActivity;
import com.sangu.apptongji.main.adapter.ConversationAdapter;
import com.sangu.apptongji.main.alluser.entity.PushDetail;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.alluser.order.avtivity.ConsumeActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.DanjuListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.DingdanActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.MerDetailListActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.SelfYuEActivity;
import com.sangu.apptongji.main.alluser.presenter.IPushPresenter;
import com.sangu.apptongji.main.alluser.presenter.IUAZPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PushPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.UAZPresenter;
import com.sangu.apptongji.main.alluser.view.IPushView;
import com.sangu.apptongji.main.alluser.view.IUAZView;
import com.sangu.apptongji.main.moments.DynaDetaActivity;
import com.sangu.apptongji.main.qiye.BaobiaoListActivity;
import com.sangu.apptongji.main.qiye.NewMajorActivity;
import com.sangu.apptongji.main.qiye.QingjiaListActivity;
import com.sangu.apptongji.main.qiye.QiyePaidanListActivity;
import com.sangu.apptongji.main.qiye.entity.QiYeInfo;
import com.sangu.apptongji.main.qiye.presenter.IQiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.presenter.impl.QiYeInfoPresenter;
import com.sangu.apptongji.main.qiye.view.IQiYeDetailView;
import com.sangu.apptongji.main.service.AlermService;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.ChatActivity;
import com.xiaomi.mimc.MIMCGroupMessage;
import com.xiaomi.mimc.MIMCMessage;
import com.xiaomi.mimc.MIMCServerAck;
import com.xiaomi.mimc.cipher.Base64;
import com.xiaomi.mimc.common.MIMCConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConversationListFragment extends Fragment implements IUAZView,IQiYeDetailView,IPushView,UserManager.OnHandleMIMCMsgListener {
    private TextView errorText;
    private TextView tv_time;
    private final static int MSG_REFRESH = 2;
    protected EditText query;
    protected boolean hidden;
    private IPushPresenter pushPresenter;
    protected IUAZPresenter uazPresenter;
    private IQiYeInfoPresenter presenter;
    private RelativeLayout rl_jiedan;
    private RelativeLayout rl_paidan;
    private RelativeLayout rl_tongzhi;
    private RelativeLayout rl_count;
    private RelativeLayout rl_baojia;
    private TextView tv_count_tongzhi,tv_unread_jiedan,tv_unread_paidan,tv_unread_tongzhi;
    private List<PushDetail> pushList = new ArrayList<>();

    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected List<Userful> users = new ArrayList<Userful>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;
    protected boolean isConflict;
    protected boolean isFinish = false;
    protected String talkName = "";
    protected String task_label = "";
    private String paidanSId = null;
    private int paidanType = 0;
    private int pushSize=0;
    private int ucdingdanSize=0;
    private int uddingdanSize=0;
    private int qydingdanSize=0;
    private int qyPaidanSize=0;
    private int oflPaidanSize=0;
    private int dtPaidanSize=0;
    private int jiluPaidanSize=0;
    private boolean isMySendPaidan = false;

    private List<JSONObject> datas = new ArrayList<JSONObject>();
    private ListView listview_message;

    TimeThread thread = null;
    protected EMConversationListener convListener = new EMConversationListener() {
        @Override
        public void onCoversationUpdate() {
            refresh();
        }
    };
    private ConversationAdapter adapter;
    private Context mContext;
    protected Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                case 1:
                    if (isFinish){
                       // adapter = new ConversationAdapter(getActivity(), 0, conversationList,users);
                     //   conversationListView.init(conversationList, adapter);
                        if (listItemClickListener != null) {
                            conversationListView.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    EMConversation conversation = conversationListView.getItem(position);
                                    listItemClickListener.onListItemClicked(conversation);
                                }
                            });
                        }
                        registerForContextMenu(conversationListView);
                        conversationListView.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                EMConversation conversation = conversationListView.getItem(position);
                                String username = conversation.conversationId();
                                //String name = conversation.getUserName();
                                if (username.equals(DemoHelper.getInstance().getCurrentUsernName()))
                                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                                else {
                                    String uLoginId = conversation.conversationId();
                                    EMMessage eMsg = conversation.getLastMessage();
                                    String chatImg = eMsg.getStringAttribute(uLoginId,"0");
                                    String name = eMsg.getStringAttribute("name","0");
                                    String type = eMsg.getStringAttribute("type","单聊");
                                    String shareRed = eMsg.getStringAttribute("shareRed","无");
                                    // start chat acitivity
                                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                                    if (conversation.isGroup()) {
                                        if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                                            // it's group chat
                                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                                        } else {
                                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                                        }
                                    }
                                    Log.e("chatfrag,contype",type);
                                    Log.e("chatfrag,conname",name);
                                    Log.e("chatfrag,conimg",chatImg);
                                    // it's single chat
                                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                                    intent.putExtra(Constant.EXTRA_USER_IMG, chatImg);
                                    intent.putExtra(Constant.EXTRA_USER_NAME, name);
                                    intent.putExtra(Constant.EXTRA_USER_TYPE, type);
                                    intent.putExtra(Constant.EXTRA_USER_SHARERED, shareRed);
                                    startActivity(intent);
                                }
                            }
                        });
                        conversationListView.setConversationListHelper(new EaseConversationList.EaseConversationListHelper() {
                            @Override
                            public String onSetItemSecondaryText(EMMessage lastMessage) {
//                                if (lastMessage.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                                    try {
//                                        String sendNick = lastMessage.getStringAttribute(RedPacketConstant.EXTRA_RED_PACKET_SENDER_NAME);
//                                        String receiveNick = lastMessage.getStringAttribute(RedPacketConstant.EXTRA_RED_PACKET_RECEIVER_NAME);
//                                        String msg;
//                                        if (lastMessage.direct() == EMMessage.Direct.RECEIVE) {
//                                            msg = String.format(getResources().getString(R.string.money_msg_someone_take_money), receiveNick);
//                                        } else {
//                                            if (sendNick.equals(receiveNick)) {
//                                                msg = getResources().getString(R.string.money_msg_take_money);
//                                            } else {
//                                                msg = String.format(getResources().getString(R.string.money_msg_take_someone_money), sendNick);
//                                            }
//                                        }
//                                        return msg;
//                                    } catch (HyphenateException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                                return null;
                            }
                        });
                        isFinish=false;
                        conversationListView.refresh();
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    long sysTime = System.currentTimeMillis();//获取系统时间
                    CharSequence sysTimeStr = DateFormat.format("HH:mm", sysTime);//时间显示格式
                    tv_time.setText(sysTimeStr); //更新时间
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fx_fragment_conversation, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        pushPresenter = new PushPresenter(getActivity(),this);
        uazPresenter = new UAZPresenter(getActivity(),this);
        presenter = new QiYeInfoPresenter(getActivity(),this);
        initView();
        setUpView();


    }

    @Override
    public void updatePushList(List<PushDetail> pushLists) {
        this.pushList = pushLists;
        dtPaidanSize = 0;
        jiluPaidanSize = 0;
        oflPaidanSize = 0;
        paidanSId = null;
        isMySendPaidan = false;
        qyPaidanSize = 0;
        uddingdanSize = 0;
        ucdingdanSize = 0;
        qydingdanSize = 0;
        if (pushLists!=null&&pushLists.size()>0){
            int paidanSize=0,dingdanSize=0;
            pushSize = pushLists.size();

            for (int i=0;i<pushLists.size();i++){
                if (pushLists.get(i).getType().equals("15")){
                    pushSize--;
                }
            }
//            if (pushSize>0) {
//                tv_unread_tongzhi.setVisibility(View.VISIBLE);
//            }else {
//                tv_unread_tongzhi.setVisibility(View.INVISIBLE);
//            }
            tv_count_tongzhi.setText(pushSize+"");
            for (int i=0;i<pushLists.size();i++){
                    String type = pushLists.get(i).getType();
                if ("05".equals(type)){
                    paidanSize++;
                    oflPaidanSize++;
                }else if ("04".equals(type)){
                    paidanSize++;
                    qyPaidanSize++;
                }else if ("000".equals(type)){
                    dingdanSize++;
                    uddingdanSize++;
                }else if ("001".equals(type)){
                    dingdanSize++;
                    ucdingdanSize++;
                }else if ("002".equals(type)){
                    dingdanSize++;
                    qydingdanSize++;
                }else if ("13".equals(type)){
                    paidanSize++;
                    dtPaidanSize++;
                    task_label = pushLists.get(i).getDynamicSeq();
                } else if ("16".equalsIgnoreCase(type)) {
                    paidanSize++;
                    paidanSId = pushLists.get(i).getCompanyName();
                    paidanType = 16;
                } else if ("17".equalsIgnoreCase(type)) {
                    paidanSize++;
                    jiluPaidanSize++;
                } else if ("18".equalsIgnoreCase(type)) {
                    paidanSize++;
                    paidanType = 18;
                } else if ("19".equalsIgnoreCase(type)){
                    isMySendPaidan = true;
                    paidanSId = pushLists.get(i).getCompanyId();
                    paidanSize++;
                    paidanType = 19;
                }
            }
            if (paidanSize>0){
                tv_unread_paidan.setVisibility(View.VISIBLE);
            }else {
                tv_unread_paidan.setVisibility(View.INVISIBLE);
            }
            if (dingdanSize>0){
                tv_unread_jiedan.setVisibility(View.VISIBLE);
            }else {
                tv_unread_jiedan.setVisibility(View.INVISIBLE);
            }
        }else {
            pushSize = 0;
          //  tv_unread_tongzhi.setVisibility(View.INVISIBLE);
            tv_unread_jiedan.setVisibility(View.INVISIBLE);
            tv_unread_paidan.setVisibility(View.INVISIBLE);
            tv_count_tongzhi.setText("0");
        }
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    myhandler.sendEmptyMessage(2);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    @Override
    public void updateThisUser(Userful user2) {
        String userId = user2.getLoginId();
        if ("18337101357".equals(userId)||"22222222222".equals(userId)){
            talkName = "客服";
        }else {
            talkName = TextUtils.isEmpty(user2.getName()) ? user2.getLoginId() : user2.getName();
        }
        String image = TextUtils.isEmpty(user2.getImage())?"":user2.getImage();
        Userful user = new Userful();
        if (image.length()>40){
            image = image.split("\\|")[0];
        }
        user.setName(talkName);
        user.setImage(image);
        user.setType("单聊");
        user.setLoginId(user2.getLoginId());
        users.add(user);
        if (conversationList.size()==users.size()){
            isFinish = true;
        }
        myhandler.sendEmptyMessage(0);
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

    @Override
    public void updateQiyeInfo(QiYeInfo qiYeInfo) {
        talkName = TextUtils.isEmpty(qiYeInfo.getCompanyName())?qiYeInfo.getCompanyId():qiYeInfo.getCompanyName();
        String image = TextUtils.isEmpty(qiYeInfo.getComImage())?"":qiYeInfo.getComImage();
        Userful user = new Userful();
        if (image.length()>40){
            image = image.split("\\|")[0];
        }
        user.setName(talkName);
        user.setImage(image);
        user.setType("企业");
        user.setLoginId(qiYeInfo.getCompanyId());
        users.add(user);
        if (users.size()==conversationList.size()){
            isFinish=true;
        }
        myhandler.sendEmptyMessage(1);
    }


    protected void initView() {
        rl_jiedan = (RelativeLayout) getView().findViewById(R.id.rl_jiedan);
        rl_paidan = (RelativeLayout) getView().findViewById(R.id.rl_paidan);
        rl_tongzhi = (RelativeLayout) getView().findViewById(R.id.rl_tongzhi);
        rl_count = (RelativeLayout) getView().findViewById(R.id.rl_count);
        rl_baojia = (RelativeLayout) getView().findViewById(R.id.rl_baojia);
        tv_count_tongzhi = (TextView) getView().findViewById(R.id.tv_count_tongzhi);
        tv_unread_jiedan = (TextView) getView().findViewById(R.id.tv_unread_jiedan);
        tv_unread_paidan = (TextView) getView().findViewById(R.id.tv_unread_paidan);
      //  tv_unread_tongzhi = (TextView) getView().findViewById(R.id.tv_unread_tongzhi);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
        View errorView = View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        tv_time = (TextView) getView().findViewById(R.id.tv_from);
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        errorItemContainer.setVisibility(View.GONE);
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, PaidanStatistics.class));
            }
        });


        listview_message = (ListView) getView().findViewById(R.id.listview_message);

    }


    private void GetMessageListInfo(){


        new Thread(new Runnable() {
            @Override
            public void run() {

                final UserManager userManager = UserManager.getInstance();

                String url = "https://mimc.chat.xiaomi.net/api/contact/";

                String token = userManager.getUser().getAppAccount();

                OkHttpClient client = new OkHttpClient();
                Request request = new Request
                        .Builder()
                        .url(url)
                        .addHeader("token",userManager.getUser().getToken())
                        .addHeader("Content-Type","application/json;charset=UTF-8")
                        .build();

                Call call = client.newCall(request);
                JSONObject data = null;
                try {
                    Response response = call.execute();
                    data = new JSONObject(response.body().string());
                    int code = data.getInt("code");
                    if (code != 200) {
                        //logger.warn("Error, code = " + code);

                    }else {

                        JSONArray array = data.getJSONArray("data");

                        if (datas.size()>0){

                            datas.clear();

                        }

                        for (int i=0 ; i<array.length() ; i ++){

                            JSONObject object = (JSONObject)array.get(i);

                            String extra = object.getString("extra");
                            String timestamp = object.getString("timestamp");

                            if (extra != null && !extra.equals("") && !extra.equals("null")){

                                if (Long.valueOf(extra)<Long.valueOf(timestamp)){

                                    //有新消息
                                    datas.add(object);

                                }

                            }else {

                                datas.add(object);
                            }

                        }

                    }

                } catch (Exception e) {
                    //logger.warn("Get token exception: " + e);
                    e.printStackTrace();
                }


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter = new ConversationAdapter(getActivity(), 0, datas,users);

                        listview_message.setAdapter(adapter);

                        listview_message.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                                JSONObject jsonObject = datas.get(i);
                                try {

                                    final String nameId = jsonObject.getString("name");

                                    final String timestamp = jsonObject.getString("timestamp");

                                    final LayoutInflater inflater1 = LayoutInflater.from(getActivity());
                                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                                    final Dialog collectionDialog = new AlertDialog.Builder(getActivity(), R.style.Dialog).create();
                                    collectionDialog.show();
                                    collectionDialog.getWindow().setContentView(layout1);
                                    collectionDialog.setCanceledOnTouchOutside(true);
                                    collectionDialog.setCancelable(true);
                                    TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
                                    Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
                                    final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
                                    TextView title = (TextView) layout1.findViewById(R.id.tv_title);
                                    title.setText("温馨提示");
                                    btnOK1.setText("确定");
                                    btnCancel1.setText("取消");

                                    title_tv1.setText("是否确定删除会话？");

                                    btnCancel1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            collectionDialog.dismiss();
                                        }
                                    });

                                    btnOK1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            collectionDialog.dismiss();

                                            UpdateMessageExta(nameId,timestamp);

                                        }
                                    });

                                }catch (JSONException e){

                                    e.printStackTrace();

                                }



                                return true;
                            }
                        });


                        listview_message.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                JSONObject jsonObject = datas.get(i);

                                try {

                                    JSONObject jsonObject1  = jsonObject.getJSONObject("lastMessage");

                                    String nameId = jsonObject.getString("name");

                                    String payload = jsonObject1.getString("payload");

                                    String str2 = new String(Base64.decode(payload));

                                    JSONObject object2 = new JSONObject(str2);

                                    JSONObject json = object2.getJSONObject("ext");

                                    String username = jsonObject.getString("name");

                                    //String name = conversation.getUserName();
                                    if (username.equals(DemoHelper.getInstance().getCurrentUsernName()))
                                        Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                                    else {
                                        String uLoginId = username;

                                        SharedPreferences sp = getActivity().getSharedPreferences("sangu_message_info", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();

                                        editor.putString(username + "message","0");
                                        editor.commit();


                                      //  EMMessage eMsg = conversation.getLastMessage();
                                        String userInfo = json.getString(jsonObject.getString("name"));
                                        String[] userinfoStrs = userInfo.split("\\|");

                                        String chatImg = userinfoStrs[0];
                                        String name = userinfoStrs[1];
                                        String type = "单聊";
                                        String shareRed = json.getString("shareRed");
                                        // start chat acitivity
                                        Intent intent = new Intent(getActivity(), ChatActivity.class);

                                        Log.e("chatfrag,contype",type);
                                        Log.e("chatfrag,conname",name);
                                        Log.e("chatfrag,conimg",chatImg);
                                        // it's single chat
                                        intent.putExtra(Constant.EXTRA_USER_ID, username);
                                        intent.putExtra(Constant.EXTRA_USER_IMG, chatImg);
                                        intent.putExtra(Constant.EXTRA_USER_NAME, name);
                                        intent.putExtra(Constant.EXTRA_USER_TYPE, type);
                                        intent.putExtra(Constant.EXTRA_USER_SHARERED, shareRed);
                                        startActivity(intent);

                                    }

                                }catch (JSONException e){

                                    e.printStackTrace();

                                }

                            }
                        });

                    }
                });


            }

        }).start();

    }


    private void UpdateMessageExta (final String account, final String timestamp){

        new Thread(new Runnable() {
            @Override
            public void run() {

                final UserManager userManager = UserManager.getInstance();

                String url = "https://mimc.chat.xiaomi.net/api/contact/p2p/extra/update";

                String token = userManager.getUser().getAppAccount();

                String json = "{\"account\":" + account + ",\"extra\":\"" + timestamp + "\"}";
                MediaType JSON = MediaType.parse("application/json;charset=utf-8");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request
                        .Builder()
                        .url(url)
                        .post(RequestBody.create(JSON, json))
                        .addHeader("token",userManager.getUser().getToken())
                        .addHeader("Content-Type","application/json;charset=UTF-8")
                        .build();

                Call call = client.newCall(request);
                JSONObject data = null;
                try {
                    Response response = call.execute();
                    data = new JSONObject(response.body().string());
                    int code = data.getInt("code");
                    if (code != 200) {
                        //logger.warn("Error, code = " + code);

                    }else {

                        GetMessageListInfo();

                    }

                }catch (Exception e){

                    e.printStackTrace();

                }

            }

        }).start();

    }

    protected void setUpView() {
        conversationList.addAll(loadConversationList());
        for (int a = 0 ;a<conversationList.size();a++){
            EMConversation conversation = conversationList.get(a);
            String uLoginId = conversation.conversationId();
            EMMessage eMsg = conversation.getLastMessage();
            String chatImg = eMsg.getStringAttribute(uLoginId,"0");
            String name = eMsg.getStringAttribute("name",null);
            String type = eMsg.getStringAttribute("type",null);
            String shareRed = eMsg.getStringAttribute("shareRed","无");
            if (chatImg!=null){
                Log.e("conversafr,ci",chatImg);
            }
            if (name!=null){
                Log.e("conversafr,na",name);
            }
            if (type!=null){
                Log.e("conversafr,ty",type);
            }
            if (shareRed!=null){
                Log.e("conversafr,sr",shareRed);
            }
            if (name!=null&&!"".equals(name)&&!"0".equals(name)){
                if (!"单聊".equals(type)){
                    talkName = name;
                }else {
                    String[] imgs = chatImg.split("\\|");
                    talkName = imgs[imgs.length-1];
                }
                Userful user = new Userful();
                user.setName(talkName);
                user.setImage(chatImg);
                user.setType(type);
                user.setShareRed(shareRed);
                user.setLoginId(uLoginId);
                users.add(user);
                if (users.size()==conversationList.size()){
                    isFinish=true;
                }
                myhandler.sendEmptyMessage(1);
            }else {
                if (conversation.getType() != EMConversation.EMConversationType.GroupChat) {
                    uazPresenter.loadThisDetail(uLoginId);
                } else if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                    presenter.loadQiYeInfo(uLoginId);
                }
            }
        }
        EMClient.getInstance().addConnectionListener(connectionListener);
        rl_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pushSize==0) {
                    try {
                      //  startActivity(new Intent(getActivity(), GonggaoListActivity.class));
                        startActivity(new Intent(getActivity(), JieDanSettingActivity.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    String type = pushList.get(0).getType();
                    startActivityIntent(type);
                }
            }
        });
        rl_tongzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // startActivity(new Intent(getActivity(), GonggaoListActivity.class));
                startActivity(new Intent(getActivity(), JieDanSettingActivity.class));
//                if (pushSize==0) {
//                    startActivity(new Intent(getActivity(), GonggaoListActivity.class));
//                }else {
//                    String type = pushList.get(0).getType();
//                    startActivityIntent(type);
//                }
            }
        });
        rl_jiedan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ucdingdanSize>0){
                    startActivity(new Intent(getActivity(),ConsumeActivity.class).putExtra("biaoshi", "01"));
                }else if (uddingdanSize>0){
                    startActivity(new Intent(getActivity(),DingdanActivity.class).putExtra("biaoshi", "01"));
                } else if (qydingdanSize > 0) {
                    startActivity(new Intent(getActivity(), DingdanActivity.class).putExtra("biaoshi", "00"));
                } else if (pushList.size() != 0 && pushList.get(0).getType().equalsIgnoreCase("13")){
                    Intent intent = new Intent(getActivity(), PaidanListActivity.class).putExtra("pushType", 13);
                    intent.putExtra("companyName", pushList.get(0).getCompanyName());
                    startActivity(intent);
                }
            }
        });
        rl_baojia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),BaojiaListActivity.class));
            }
        });
//        rl_shezhi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        rl_paidan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qyPaidanSize>0){
                    startActivity(new Intent(getActivity(),DingdanActivity.class).putExtra("biaoshi", "01"));
                }else if (oflPaidanSize>0){
                    startActivity(new Intent(getActivity(),QiyePaidanListActivity.class).putExtra("biaoshi", "00").putExtra("isQunzhu", "00"));
                } else if (dtPaidanSize > 0) {
                    //startActivity(new Intent(getActivity(), PushDynaActivity.class).putExtra("task_label", task_label));
                    PermissionUtil permissionUtil = new PermissionUtil((MainActivity) mContext);
                    permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_WIFI_STATE},
                            new PermissionListener() {
                                @Override
                                public void onGranted() {
                                    //所有权限都已经授权
                                    try {
                                        startActivity(new Intent(getActivity(), PaidanListActivity.class).putExtra("task_label", task_label).putExtra("pushType", 13));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onDenied(List<String> deniedPermission) {
                                    //Toast第一个被拒绝的权限
                                    ToastUtils.showNOrmalToast(mContext.getApplicationContext(),"由于您没有开启定位权限，无法进去接收派单界面");

                                }

                                @Override
                                public void onShouldShowRationale(List<String> deniedPermission) {
                                    //Toast第一个勾选不在提示的权限
                                    ToastUtils.showNOrmalToast(mContext.getApplicationContext(),"由于您没有开启定位权限，无法进去接收派单界面");
                                }
                            });
                } else if (jiluPaidanSize > 0) {
                    //随便传一个只有有值就会删除type17的推送
                    startActivity(new Intent(getActivity(), TradeTrackActivity.class).putExtra("companyName", "hh"));
                } else {
                    PermissionUtil permissionUtil = new PermissionUtil((MainActivity) mContext);
                    permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_WIFI_STATE},
                            new PermissionListener() {
                                @Override
                                public void onGranted() {
                                    //所有权限都已经授权
                                    try {
                                        //如果paidanSId 为空，表明不用把点击人数加1  应该是师傅端给我消息了，所以只用删除一下推送
                                        if (paidanType != 0) {
                                            startActivity(new Intent(getActivity(), PaidanListActivity.class).putExtra("pushType", paidanType).putExtra("type", 1));
                                        } else {
                                            startActivity(new Intent(getActivity(), PaidanListActivity.class));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onDenied(List<String> deniedPermission) {
                                    //Toast第一个被拒绝的权限
                                    ToastUtils.showNOrmalToast(mContext.getApplicationContext(),"由于您没有开启定位权限，无法进去接收派单界面");

                                }

                                @Override
                                public void onShouldShowRationale(List<String> deniedPermission) {
                                    //Toast第一个勾选不在提示的权限
                                    ToastUtils.showNOrmalToast(mContext.getApplicationContext(),"由于您没有开启定位权限，无法进去接收派单界面");
                                }
                            });


                }
            }
        });
    }

    private void startActivityIntent(final String type) {
        Log.d("chen", "startActivity " + type);
        Intent intent2 = new Intent(getActivity(), PaidanListActivity.class).putExtra("pushType", type);
        if ("000".equals(type)) {
            intent2 = new Intent(getActivity(), DingdanActivity.class).putExtra("biaoshi", "01");
        } else if ("001".equals(type)) {
            intent2 = new Intent(getActivity(), ConsumeActivity.class).putExtra("biaoshi", "01");
        } else if ("002".equals(type)) {
            intent2 = new Intent(getActivity(), DingdanActivity.class).putExtra("biaoshi", "00");
        } else if ("02".equals(type)) {
            intent2 = new Intent(getActivity(), NewFriendsActivity.class);
        } else if ("03".equals(type)) {
            String companyName = pushList.get(0).getCompanyName();
            String comAddress = pushList.get(0).getCompanyAdress();
            intent2 = new Intent(getActivity(), NewMajorActivity.class).putExtra("companyName", companyName).putExtra("comAddress", comAddress);
        } else if ("04".equals(type)) {
            intent2 = new Intent(getActivity(), DingdanActivity.class).putExtra("biaoshi", "01");
        } else if ("05".equals(type)) {
            intent2 = new Intent(getActivity(), QiyePaidanListActivity.class).putExtra("biaoshi", "00").putExtra("isQunzhu", "00");
        } else if ("06".equals(type)) {
            intent2 = new Intent(getActivity(), QingjiaListActivity.class).putExtra("biaoshi", "01");
        } else if ("07".equals(type)) {
            try {
                intent2 = new Intent(getActivity(), BaobiaoListActivity.class).putExtra("biaoshi", "01");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("08".equals(type)) {
            intent2 = new Intent(getActivity(), SelfYuEActivity.class).putExtra("biaoshi", "000");
        } else if ("09".equals(type) || "21".equals(type)) {
            String dynamicSeq = pushList.get(0).getDynamicSeq();
            String createTime = pushList.get(0).getCreateTime();
            String dType = pushList.get(0).getdType();
            intent2 = new Intent(getActivity(), DynaDetaActivity.class).putExtra("pushType", type).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime).putExtra("dType", dType);
        } else if ("10".equals(type)) {
            String dynamicSeq = pushList.get(0).getDynamicSeq();
            String createTime = pushList.get(0).getCreateTime();
            intent2 = new Intent(getActivity(), DynaDetaActivity.class).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime)
                    .putExtra("dType", "05").putExtra("sID", DemoHelper.getInstance().getCurrentUsernName());
        } else if ("11".equals(type)) {
            intent2 = new Intent(getActivity(), DanjuListActivity.class).putExtra("biaoshi", "单据");
        } else if ("12".equals(type)) {
            intent2 = new Intent(getActivity(), DanjuListActivity.class).putExtra("biaoshi", "订单");
        }else if ("13".equals(type)){
            String task_label = pushList.get(0).getDynamicSeq();
            //intent2 = new Intent(getActivity(), PushDynaActivity.class).putExtra("task_label",task_label);
            intent2 = new Intent(getActivity(), PaidanListActivity.class).putExtra("task_label",task_label);
            intent2.putExtra("type", 0);
            intent2.putExtra("pushType", 13);

        } else if ("14".equals(type)) {
            String companyId = pushList.get(0).getCompanyId();
            String companyName = pushList.get(0).getCompanyName();
            String shangbanTime = companyName.split(".")[0];
            String xiabanTime = companyName.split(".")[1];
            SharedPreferences sp = getActivity().getSharedPreferences("sangu_qiandao_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(companyId + "_qiandao_shb", shangbanTime);
            editor.putString(companyId + "_qiandao_xb", xiabanTime);
            editor.commit();
            getActivity().startService(new Intent(getActivity(), AlermService.class));
        }else if("16".equalsIgnoreCase(type)){
            intent2 = new Intent(getActivity(), PaidanListActivity.class);
            intent2.putExtra("companyName", pushList.get(0).getCompanyName());
            intent2.putExtra("pushType", 16);
        }else if("17".equalsIgnoreCase(type)){
            intent2 = new Intent(getActivity(), TradeTrackActivity.class);
            intent2.putExtra("companyName", pushList.get(0).getCompanyName());
        }else if("18".equalsIgnoreCase(type)){
            intent2 = new Intent(getActivity(), PaidanListActivity.class);
            intent2.putExtra("type", 1);
            intent2.putExtra("pushType", 18);
        }else if("19".equalsIgnoreCase(type)){
            intent2 = new Intent(getActivity(), PaidanListActivity.class);
            intent2.putExtra("type", 0);
            //为了便于管理 暂时把CompanyId 发送为companyName
            intent2.putExtra("companyName", pushList.get(0).getCompanyId());
            intent2.putExtra("pushType", 19);
        }else if ("20".equals(type)) { //给发需求的用户推荐附近的师傅

            String dynamicSeq = pushList.get(0).getDynamicSeq();
            String createTime = pushList.get(0).getCreateTime();
            intent2 = new Intent(getActivity(), DynaDetaActivity.class).putExtra("dynamicSeq", dynamicSeq).putExtra("createTime", createTime)
                    .putExtra("dType", "05").putExtra("pushType", "20").putExtra("sID", DemoHelper.getInstance().getCurrentUsernName());

        }else if ("22".equals(type)) {
            //由用户对评论做出了奖励  直接跳转余额明细


            intent2 = new Intent(getActivity(), MerDetailListActivity.class).putExtra("pushType", "22").putExtra("biaoshi","000");

        }/*else if("16".equalsIgnoreCase(type)){
            intent2 = new Intent(getActivity(), PaidanListActivity.class);
            intent2.putExtra("companyName", pushList.get(0).getCompanyName());
            intent2.putExtra("pushType", 16);
        }else if("17".equalsIgnoreCase(type)){
            intent2 = new Intent(getActivity(), TradeTrackActivity.class);
            intent2.putExtra("companyName", pushList.get(0).getCompanyName());
        }else if("18".equalsIgnoreCase(type)){
            intent2 = new Intent(getActivity(), PaidanListActivity.class);
            intent2.putExtra("type", 1);
            intent2.putExtra("pushType", 18);
        }else if("19".equalsIgnoreCase(type)){
            intent2 = new Intent(getActivity(), PaidanListActivity.class);
            intent2.putExtra("type", 0);
            //为了便于管理 暂时把CompanyId 发送为companyName
            intent2.putExtra("companyName", pushList.get(0).getCompanyId());
            intent2.putExtra("pushType", 19);
        }*/else {
            Log.d("chen", "接收到一条通知类型是" + type);
        }
        if ("13".equalsIgnoreCase(type)||"17".equalsIgnoreCase(type)||"18".equalsIgnoreCase(type)) {
            //判断是否开启定位权限，如果没有开启就不让进入派单
            PermissionUtil permissionUtil = new PermissionUtil((MainActivity) mContext);
            final Intent finalIntent = intent2;
            permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_WIFI_STATE},
                    new PermissionListener() {
                        @Override
                        public void onGranted() {
                            //所有权限都已经授权
                            try {
                                startActivity(finalIntent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onDenied(List<String> deniedPermission) {
                            //Toast第一个被拒绝的权限
                            ToastUtils.showNOrmalToast(mContext.getApplicationContext(),"由于您没有开启定位权限，无法进去接收派单界面");

                        }

                        @Override
                        public void onShouldShowRationale(List<String> deniedPermission) {
                            //Toast第一个勾选不在提示的权限
                            ToastUtils.showNOrmalToast(mContext.getApplicationContext(),"由于您没有开启定位权限，无法进去接收派单界面");
                        }
                    });
        } else {
            try {
                startActivity(intent2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0 && conversation.getType() != EMConversation.EMConversationType.ChatRoom || (conversation.getType() == EMConversation.EMConversationType.Chat && DemoHelper.getInstance().getContactList().containsKey(conversation.conversationId()))) {
                    try {
                        sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().localTime(), conversation));
                    } catch (NullPointerException e) {
                    }
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    public void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.GONE);
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        try {
            // delete conversation
            users.remove(((AdapterContextMenuInfo) item.getMenuInfo()).position);
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();
        // update unread count
     //   ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;
                case MSG_REFRESH: {
                    if (conversationList!=null) {
                        conversationList.clear();
                    }
                    conversationList.addAll(loadConversationList());
                    if (users!=null) {
                        users.clear();
                    }
                    for (int a = 0 ;a<conversationList.size();a++){
                        EMConversation conversation = conversationList.get(a);
                        String uLoginId = conversation.conversationId();
                        EMMessage eMsg = conversation.getLastMessage();
                        String chatImg = eMsg.getStringAttribute(uLoginId,"0");
                        String name = eMsg.getStringAttribute("name",null);
                        String type = eMsg.getStringAttribute("type",null);
                        String shareRed = eMsg.getStringAttribute("shareRed","无");
                        if (chatImg!=null){
                            Log.e("conversafr,ci",chatImg);
                        }
                        if (name!=null){
                            Log.e("conversafr,na",name);
                        }
                        if (type!=null){
                            Log.e("conversafr,ty",type);
                        }
                        if (shareRed!=null){
                            Log.e("conversafr,sr",shareRed);
                        }
                        if (name!=null&&!"".equals(name)&&!"0".equals(name)){
                            if (!"单聊".equals(type)){
                                talkName = name;
                            }else {
                                String[] imgs = chatImg.split("\\|");
                                talkName = imgs[imgs.length-1];
                            }
                            Userful user = new Userful();
                            user.setName(talkName);
                            user.setImage(chatImg);
                            user.setType(type);
                            user.setShareRed(shareRed);
                            user.setLoginId(uLoginId);
                            users.add(user);
                            if (users.size()==conversationList.size()){
                                isFinish=true;
                            }
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            myhandler.sendEmptyMessage(1);
                        }else {
                            if (conversation.getType() != EMConversation.EMConversationType.GroupChat) {
                                if (uLoginId != null && uazPresenter != null) {
                                    uazPresenter.loadThisDetail(uLoginId);
                                }
                            } else if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                                if (uLoginId != null && presenter != null) {
                                    presenter.loadQiYeInfo(uLoginId);
                                }
                            }
                        }
                    }
                    Log.d("coversation--->", "refresh");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * connected to server
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * refresh ui
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    public void refreshPushList(){
        if (pushPresenter!=null){
            pushPresenter.loadPushList(DemoHelper.getInstance().getCurrentUsernName());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pushPresenter!=null){
            pushPresenter.loadPushList(DemoHelper.getInstance().getCurrentUsernName());
        }
        if (!hidden) {
            refresh();
        }
        if (thread==null) {
            thread = new TimeThread();
            thread.start(); //启动新的线程
        }
        if (thread.isInterrupted()){
            thread.start();
        }

        GetMessageListInfo();

        UserManager userManager = UserManager.getInstance();
        userManager.setHandleMIMCMsgListener(this);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (thread!=null){
            thread.interrupt();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
        if (users!=null){
            users.clear();
            users=null;
        }
        if (thread!=null){
            thread.interrupt();
            thread = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }
    @Override
    public void showLoading() {
    }
    @Override
    public void hideLoading() {
    }
    @Override
    public void showError() {
    }

    public interface EaseConversationListItemClickListener {
        /**
         * click event for conversation list
         *
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * set conversation list item click listener
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }



    @Override
    public void onHandleMessageObjec(com.alibaba.fastjson.JSONObject jsonObject) {

        SharedPreferences sp = getActivity().getSharedPreferences("sangu_message_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String from = jsonObject.getString("from");

        String count = sp.getString(from + "message","0");
        String playTime = sp.getString("playTimemessage","0");

        if (count.equals("0")){

            editor.putString(from + "message", "1");

        }else {

            editor.putString(from + "message", (Integer.valueOf(count)+1)+"");
        }

        if (playTime.equals("0")){

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone rt = RingtoneManager.getRingtone(getActivity(), uri);
            rt.play();

            editor.putString("playTimemessage", System.currentTimeMillis()+"");

        }else {

            String timestamp = jsonObject.getString("timestamp");

            if ((Long.valueOf(timestamp)-Long.valueOf(playTime))>3000){

                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone rt = RingtoneManager.getRingtone(getActivity(), uri);
                rt.play();

                editor.putString("playTimemessage", System.currentTimeMillis()+"");

            }

        }

        editor.commit();

        GetMessageListInfo();

        ((MainActivity) getActivity()).addConersationListPush(0);

    }

    @Override
    public void onHandleMessage(ChatMsg chatMsg) {

        GetMessageListInfo();

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


}