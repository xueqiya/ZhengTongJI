package com.sangu.apptongji.main.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.sangu.apptongji.R;
import com.fanxin.easeui.domain.EaseUser;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.GridAdapter;
import com.sangu.apptongji.main.db.TopUser;
import com.sangu.apptongji.main.db.TopUserDao;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.widget.ExpandGridView;
import com.sangu.apptongji.ui.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatSettingGroupActivity extends BaseActivity implements
        OnClickListener {
    private TextView tv_groupname;
    // 成员总数
    private TextView tv_m_total;
    // 成员列表
    private ExpandGridView gridview;
    // 修改群名称、置顶、、、、
    private RelativeLayout re_change_groupname;
    private RelativeLayout rl_switch_chattotop;
    private RelativeLayout rl_switch_block_groupmsg;
    private RelativeLayout re_clear;

    // 状态变化
    private ImageView iv_switch_chattotop;
    private ImageView iv_switch_unchattotop;
    private ImageView iv_switch_block_groupmsg;
    private ImageView iv_switch_unblock_groupmsg;
    // 删除并退出

    private Button exitBtn;

    private String hxid;
    // 群名称
    private String group_name;
    //头像相关用户封装
    private JSONArray jsonArray;
    private String groupId;

    private EMGroup group;
    private GridAdapter adapter;
    public static ChatSettingGroupActivity instance;
    public static final int RESULT_FINISH = 100;

    // 置顶列表
    private Map<String, TopUser> topMap = new HashMap<String, TopUser>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_chat_setting_group);
        instance = this;
        hxid = DemoHelper.getInstance().getCurrentUsernName();
        topMap = DemoApplication.getInstance().getTopUserList();
        initView();
        initData();
        updateGroup();
    }

    private void initView() {
        tv_groupname = (TextView) findViewById(R.id.tv_groupname);
        tv_m_total = (TextView) findViewById(R.id.tv_m_total);
        gridview = (ExpandGridView) findViewById(R.id.gridview);
        re_change_groupname = (RelativeLayout) findViewById(R.id.re_change_groupname);
        rl_switch_chattotop = (RelativeLayout) findViewById(R.id.rl_switch_chattotop);
        rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
        re_clear = (RelativeLayout) findViewById(R.id.re_clear);
        iv_switch_chattotop = (ImageView) findViewById(R.id.iv_switch_chattotop);
        iv_switch_unchattotop = (ImageView) findViewById(R.id.iv_switch_unchattotop);
        iv_switch_block_groupmsg = (ImageView) findViewById(R.id.iv_switch_block_groupmsg);
        iv_switch_unblock_groupmsg = (ImageView) findViewById(R.id.iv_switch_unblock_groupmsg);
        exitBtn = (Button) findViewById(R.id.btn_exit_grp);

    }

    private void initData() {
        // 获取传过来的groupid
        groupId = getIntent().getStringExtra("groupId");
        if (groupId == null) {
            finish();
            return;
        }
        // 获取本地该群数据
        group = EMClient.getInstance().groupManager().getGroup(groupId);
        if (group == null) {
            try {
                group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                if (group == null) {
                    Toast.makeText(ChatSettingGroupActivity.this, "该群已经被解散...",
                            Toast.LENGTH_SHORT).show();
                    setResult(RESULT_FINISH);
                    finish();
                    return;
                }
            } catch (HyphenateException e) {
                setResult(RESULT_FINISH);
                finish();
                return;
            }

        }
        initGroupInfo();
        tv_m_total.setText("(" + String.valueOf(group.getAffiliationsCount()) + ")");
        re_change_groupname.setOnClickListener(this);
        rl_switch_chattotop.setOnClickListener(this);
        rl_switch_block_groupmsg.setOnClickListener(this);
        re_clear.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

    }

    //群名称相关
    private void initGroupInfo() {
        try {
            // 转化成json，然后解析
            JSONObject jsonObject = JSONObject.parseObject(group.getGroupName());
            // 获取显示的群名
            group_name = jsonObject.getString("groupname");
            jsonArray = jsonObject.getJSONArray("jsonArray");
            // 获取群成员信息
            if (!TextUtils.isEmpty(group_name)) {
                tv_groupname.setText(group_name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    // 显示群成员头像昵称的gridview
    private void showMembers(List<EaseUser> members) {
        adapter = new GridAdapter(this, members, hxid.equals(group.getOwner()) ? true : false,groupId);
        gridview.setAdapter(adapter);
        // 设置OnTouchListener,为了让群主方便地推出删除模》
        gridview.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (adapter.isInDeleteMode) {
                            adapter.isInDeleteMode = false;
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_block_groupmsg: // 屏蔽群组
                if (iv_switch_block_groupmsg.getVisibility() == View.VISIBLE) {
                    System.out.println("change to unblock group msg");
                    try {
                        EMClient.getInstance().groupManager().unblockGroupMessage(groupId);
                        iv_switch_block_groupmsg.setVisibility(View.INVISIBLE);
                        iv_switch_unblock_groupmsg.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("change to block group msg");
                    try {
                        EMClient.getInstance().groupManager().blockGroupMessage(groupId);
                        iv_switch_block_groupmsg.setVisibility(View.VISIBLE);
                        iv_switch_unblock_groupmsg.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case R.id.re_clear: // 清空聊天记录
                clearGroupHistory();
                break;
            case R.id.re_change_groupname:
                showNameAlert();
                break;
            case R.id.rl_switch_chattotop:
                // 当前状态是已经置顶,点击后取消置顶
                if (iv_switch_chattotop.getVisibility() == View.VISIBLE) {
                    iv_switch_chattotop.setVisibility(View.INVISIBLE);
                    iv_switch_unchattotop.setVisibility(View.VISIBLE);

                    if (topMap.containsKey(group.getGroupId())) {
                        topMap.remove(group.getGroupId());
                        TopUserDao topUserDao = new TopUserDao(
                                ChatSettingGroupActivity.this);
                        topUserDao.deleteTopUser(group.getGroupId());
                    }
                } else {
                    // 当前状态是未置顶点击后置顶
                    iv_switch_chattotop.setVisibility(View.VISIBLE);
                    iv_switch_unchattotop.setVisibility(View.INVISIBLE);

                    if (!topMap.containsKey(group.getGroupId())) {
                        TopUser topUser = new TopUser();
                        topUser.setTime(System.currentTimeMillis());
                        // 1---表示是群组
                        topUser.setType(1);
                        topUser.setUserName(group.getGroupId());
                        Map<String, TopUser> map = new HashMap<String, TopUser>();
                        map.put(group.getGroupId(), topUser);
                        topMap.putAll(map);
                        TopUserDao topUserDao = new TopUserDao(
                                ChatSettingGroupActivity.this);
                        topUserDao.saveTopUser(topUser);
                    }

                }

                break;

            case R.id.btn_exit_grp:
                exitGroup();
                break;

            default:
                break;
        }

    }

    /**
     * 清空群聊天记录
     */
    public void clearGroupHistory() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(group.getGroupId(), EMConversation.EMConversationType.GroupChat);
        if (conversation != null) {
            conversation.clearAllMessages();
        }
        Toast.makeText(this, R.string.messages_are_empty, Toast.LENGTH_SHORT).show();

    }


    protected void updateGroup() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    group = EMClient.getInstance().groupManager()
                            .getGroupFromServer(groupId);

                    runOnUiThread(new Runnable() {
                        public void run() {

                            if (group != null) {

                                // 设置初始屏蔽初始状态
                                if (group.isMsgBlocked()) {
                                    iv_switch_block_groupmsg
                                            .setVisibility(View.VISIBLE);
                                    iv_switch_unblock_groupmsg
                                            .setVisibility(View.INVISIBLE);
                                } else {
                                    iv_switch_block_groupmsg
                                            .setVisibility(View.INVISIBLE);
                                    iv_switch_unblock_groupmsg
                                            .setVisibility(View.VISIBLE);
                                }
                                // 设置置顶的初始状态

                                if (topMap.containsKey(group.getGroupId())) {

                                    // 当前状态是已经置顶

                                    iv_switch_chattotop
                                            .setVisibility(View.VISIBLE);
                                    iv_switch_unchattotop
                                            .setVisibility(View.INVISIBLE);

                                } else {
                                    // 当前状态是未置顶
                                    iv_switch_chattotop
                                            .setVisibility(View.INVISIBLE);
                                    iv_switch_unchattotop
                                            .setVisibility(View.VISIBLE);

                                }
                            }

                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                        }
                    });
                }
            }
        }).start();
    }

    private void showNameAlert() {

        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.fx_dialog_alert_et);
        // 设置能弹出输入法
        dlg.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        // 为确认按钮添加事件,执行退出应用操作
        Button ok = (Button) window.findViewById(R.id.btn_ok);
        final EditText ed_name = (EditText) window.findViewById(R.id.ed_name);
        ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String newName = ed_name.getText().toString().trim();
                if (TextUtils.isEmpty(newName) || newName.equals(group_name)) {
                    return;
                }
                JSONObject newJSON = new JSONObject();
                newJSON.put("groupname", newName);
                newJSON.put("jsonArray", jsonArray);
                String updateStr = newJSON.toJSONString();
                changeGroupName(updateStr, newName);
                dlg.cancel();
            }
        });
        // 关闭alert对话框架
        Button cancel = (Button) window.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });

    }

    private void changeGroupName(final String updateStr, final String newGroupName) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在修改群名称...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        if (hxid.equals(group.getOwner())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().changeGroupName(groupId,
                                updateStr);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateLocalData(newGroupName);
                                Toast.makeText(ChatSettingGroupActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (HyphenateException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "修改群名称失败...", Toast.LENGTH_SHORT).show();
                            }
                        });
                        e.printStackTrace();
                    }


                }
            }).start();

        } else {
            updateGroupName(groupId, updateStr, newGroupName, progressDialog);
        }


    }

    private void updateLocalData(String newGroupName) {
        tv_groupname.setText(newGroupName);
        group_name = newGroupName;
    }

    /**
     * 删除群成员
     * 当删除的是自己，则就是退群或者解散群
     */
    protected void exitGroup( ) {
        final ProgressDialog deleteDialog = new ProgressDialog(
                ChatSettingGroupActivity.this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        if ( hxid.equals(group.getOwner())) {
            //自己是群主，解散群
            destroyGroup( progressDialog);
         }else{
            leaveGroup( progressDialog);

        }


//
//        deleteDialog.setMessage("正在移除...");
//        deleteDialog.setCanceledOnTouchOutside(false);
//        deleteDialog.show();
//        try {
//            EMClient.getInstance().groupManager().removeUserFromGroup(groupId,
//                    username);
//            for (int i = 0; i < members.size(); i++) {
//                EaseUser user = members.get(i);
//                if (user.getUsername().equals(username)) {
//                    // 移除被删成员信息
//                    members.remove(user);
//                    adapter.notifyDataSetChanged();
//                    m_total = members.size();
//                    tv_m_total.setText("(" + String.valueOf(m_total) + ")");
//                    JSONObject newJSON = new JSONObject();
//                    newJSON.put("groupname", group_name);
//                    // 在封装数据里面取出删除成员，并且更新
//                    for (int n = 0; n < jsonarray.size(); n++) {
//
//                        JSONObject jsontemp = (JSONObject) jsonarray.get(n);
//                        if (jsontemp.getString("hxid").equals(username)) {
//                            jsonarray.remove(jsontemp);
//                        }
//                    }
//
//                    newJSON.put("jsonArray", jsonarray);
//                    String updateStr = newJSON.toJSONString();
//                    Log.e("updateStr------>>>>>0", updateStr);
//
//                    EMClient.getInstance().groupManager().changeGroupName(groupId,
//                            updateStr);
//
//                }
//
//            }
//
//            deleteDialog.dismiss();
//            Toast.makeText(ChatSettingGroupActivity.this, "移除成功",
//                    Toast.LENGTH_LONG).show();
//        } catch (HyphenateException e) {
//            deleteDialog.dismiss();
//            Toast.makeText(ChatSettingGroupActivity.this, "移除失败",
//                    Toast.LENGTH_LONG).show();
//            e.printStackTrace();
//        }

    }


    private void updateGroupName(String groupId, String updateStr, final String newGroupName, final ProgressDialog progressDialog) {
        List<Param> params = new ArrayList<>();
        params.add(new Param("groupId", groupId));
        params.add(new Param("groupName", updateStr));
        OkHttpManager.getInstance().post(params, FXConstant.URL_UPDATE_Groupnanme, new OkHttpManager.HttpCallBack() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                int code=jsonObject.getIntValue("code");
                if(code==1){
                    if (newGroupName != null) {
                        updateLocalData(newGroupName);
                    }
                    Toast.makeText(getApplicationContext(), "修改群信息成功...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(String errorMsg) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "修改群信息失败...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //解散群组
    private void destroyGroup( final ProgressDialog progressDialog) {
        progressDialog.setMessage("正在解散群...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().destroyGroup(groupId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(ChatSettingGroupActivity.this, "解散成功",
                                    Toast.LENGTH_LONG).show();
                            setResult(RESULT_FINISH);
                            finish();

                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(ChatSettingGroupActivity.this, "解散失败",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    //推出群组
    private void leaveGroup( final ProgressDialog progressDialog) {
        progressDialog.setMessage("正在退出群组...");
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().leaveGroup(groupId);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isChange = false;
                            JSONObject newJSON = new JSONObject();
                            newJSON.put("groupname", group_name);
                            for (int n = 0; n < jsonArray.size(); n++) {
                                JSONObject jsontemp = jsonArray.getJSONObject(n);
                                if (jsontemp.getString("hxid").equals(hxid)) {
                                    jsonArray.remove(jsontemp);
                                    isChange = true;
                                }
                            }
                            if (isChange) {
                                newJSON.put("jsonArray", jsonArray);
                                String updateStr = newJSON.toJSONString();
                                // 群成员退出以后要更新群信息，也就封装的群名..
                                updateGroupName(groupId, updateStr, null, progressDialog);
                            }

                            progressDialog.dismiss();
                            Toast.makeText(ChatSettingGroupActivity.this, "退出成功",
                                    Toast.LENGTH_LONG).show();
                            setResult(RESULT_FINISH);
                            finish();

                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    Toast.makeText(ChatSettingGroupActivity.this, "退出失败",
                            Toast.LENGTH_LONG).show();
                } catch (JSONException e) {


                }
            }
        }).start();


    }


    public void back(View view) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        if (topMap!=null) {
            topMap.clear();
        }
    }

}
