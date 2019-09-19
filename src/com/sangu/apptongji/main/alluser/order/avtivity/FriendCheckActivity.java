package com.sangu.apptongji.main.alluser.order.avtivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.FriendCheckAdapter;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.presenter.IFriendListPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.FriendListPresenter;
import com.sangu.apptongji.main.alluser.view.IFriendListView;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.ui.BaseActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-11-02.
 */

public class FriendCheckActivity extends BaseActivity implements View.OnClickListener,IFriendListView {
    private IFriendListPresenter presenter;
    private XRecyclerView mRecyclerView=null;
    private List<UserAll> list=new ArrayList<>();
    private List<UserAll> delList=new ArrayList<>();
    private FriendCheckAdapter adapter;
    private EditText searchbox;
    private TextView tv_commit;
    private ArrayList<String> imagePaths1=new ArrayList<>();
    private ArrayList<String> imagePaths2=new ArrayList<>();
    private CustomProgressDialog mProgress=null;
    private int currentPage=1;
    private String biaoshi;
    private boolean isNext = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgress!=null){
            if (mProgress.isShowing()){
                mProgress.dismiss();
            }
            mProgress=null;
        }
        if (list!=null){
            list.clear();
            list=null;
        }
        if (imagePaths1!=null){
            imagePaths1.clear();
            imagePaths1=null;
        }
        if (imagePaths2!=null){
            imagePaths2.clear();
            imagePaths2=null;
        }
    }
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_friendlist);
        biaoshi = getIntent().getStringExtra("biaoshi");
        delList = (List<UserAll>) getIntent().getSerializableExtra("delList");
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        presenter = new FriendListPresenter(this,this);
        WeakReference<FriendCheckActivity> reference =  new WeakReference<FriendCheckActivity>(FriendCheckActivity.this);
        mRecyclerView = (XRecyclerView) findViewById(R.id.refresh_list);
        searchbox = (EditText) findViewById(R.id.searchbox);
        searchbox.setVisibility(View.GONE);
        imagePaths1 = new ArrayList<>();
        imagePaths2 = new ArrayList<>();
        RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
        mProgress = CustomProgressDialog.createDialog(reference.get());
        mProgress.setMessage("正在加载数据...");
        mProgress.setCancelable(true);
        mProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mProgress.dismiss();
            }
        });
        mProgress.show();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_dark);
        mRecyclerView.addItemDecoration(mRecyclerView.new DividerItemDecoration(dividerDrawable));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isNext = false;
                currentPage=1;
                presenter.loadFriendList(null);
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.loadMoreComplete();
                mRecyclerView.setNoMore(true);
//                isNext = false;
//                currentPage=1;
//                presenter.loadFriendList(null);
            }
        });
        mRecyclerView.refresh(false);
        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s); // 当数据改变时，调用过滤器;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater2 = LayoutInflater.from(FriendCheckActivity.this);
                RelativeLayout layout2 = (RelativeLayout) inflater2.inflate(R.layout.dialog_alert, null);
                final Dialog dialog2 = new AlertDialog.Builder(FriendCheckActivity.this,R.style.Dialog).create();
                dialog2.show();
                dialog2.getWindow().setContentView(layout2);
                dialog2.setCanceledOnTouchOutside(true);
                dialog2.setCancelable(true);
                TextView title_tv2 = (TextView) layout2.findViewById(R.id.title_tv);
                Button btnCancel2 = (Button) layout2.findViewById(R.id.btn_cancel);
                final Button btnOK2 = (Button) layout2.findViewById(R.id.btn_ok);
                TextView title2 = (TextView) layout2.findViewById(R.id.tv_title);
                title2.setText("温馨提示");
                btnOK2.setText("确定");
                btnCancel2.setText("取消");
                if ("01".equals(biaoshi)){
                    title_tv2.setText("确认邀请好友吗？");
                }else {
                    title_tv2.setText("确认建立多人聊天吗？");
                }
                btnCancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                btnOK2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                        final ProgressDialog progressDialog = new ProgressDialog(FriendCheckActivity.this);
                        if ("01".equals(biaoshi)){
                            progressDialog.setMessage("正在邀请");
                        }else {
                            progressDialog.setMessage("正在创建群组");
                        }
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String groupName = DemoApplication.getInstance().getCurrentUser().getName();
                                if (groupName==null||"".equals(groupName)){
                                    groupName = DemoHelper.getInstance().getCurrentUsernName();
                                }
                                groupName += "、";
                                String userGroup = "";
                                List<UserAll> lists = adapter.getselectedList();
                                String[] allMembers = new String[lists.size()];
                                String[] allMemberName = new String[lists.size()];
                                for (int i=0;i<lists.size();i++){
                                    String userId = lists.get(i).getuId();
                                    String userName = lists.get(i).getuName();
                                    if (userName==null||"".equals(userName)||userName.equalsIgnoreCase("null")){
                                        userName = userId;
                                    }
                                    allMembers[i] = userId;
                                    allMemberName[i] = userName;
                                    Log.e("friendcheckac,uid",userId);
                                    if (i!=lists.size()-1) {
                                        groupName += userName+"、";
                                        userGroup += userId+",";
                                    }else {
                                        groupName += userName;
                                        userGroup += userId;
                                    }
                                }
                                if ("01".equals(biaoshi)){
                                    progressDialog.dismiss();
                                    setResult(RESULT_OK, new Intent().putExtra("newmembers",allMembers).putExtra("newmemberName",allMemberName));
                                    finish();
                                }else {
                                    try {
                                        EMGroupOptions option = new EMGroupOptions();
                                        option.maxUsers = 2000;
                                        option.inviteNeedConfirm = false;
                                        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                                        final EMGroup emgroup = EMClient.getInstance().groupManager().createGroup(groupName, "temp", allMembers, "nothing", option);
                                        final String finalGroupName = groupName;
                                        final String finalUserGroup = userGroup;
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                String groupId = emgroup.getGroupId();
                                                Log.e("friendcheckac,g", groupId);
                                                Log.e("friendcheckac,fu", finalUserGroup);
                                                Log.e("friendcheckac,fn", finalGroupName);
                                                creatGroup(groupId, finalUserGroup, finalGroupName, progressDialog);
                                            }
                                        });
                                    } catch (final HyphenateException e) {
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                progressDialog.dismiss();
                                                Toast.makeText(FriendCheckActivity.this, "创建群聊失败:" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }
                        }).start();
                    }
                });
            }
        });
    }

    private void creatGroup(final String groupId, final String finalUserGroup, final String finalGroupName, final ProgressDialog progressDialog) {
        String url = FXConstant.URL_INSERT_GROUP;
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (progressDialog!=null&&progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                if (s!=null){
                    Log.e("friendcheckac,s",s);
                }
                JSONObject obj = JSON.parseObject(s);
                String code = obj.getString("code");
                if (code.equalsIgnoreCase("SUCCESS")){
                    Toast.makeText(getApplicationContext(),"创建成功!",Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
                param.put("groupId",groupId);
                param.put("groupName",finalGroupName);
                param.put("userGroup",finalUserGroup);
                return param;
            }
        };
        MySingleton.getInstance(FriendCheckActivity.this).addToRequestQueue(request);
    }

    @Override
    public void back(View view) {
        super.back(view);
    }

    @Override
    public void onClick(View v) {

    }

    public static List<UserAll> removeRepetitionItem(List<UserAll> allList,List<UserAll> deleteList){
        if(null == deleteList || deleteList.size() == 0){
            return allList;
        }
        Iterator<UserAll> it = allList.iterator();
        UserAll model = null;
        while (it.hasNext()) {
            model = it.next();
            for (int i=0;i<deleteList.size();i++) {
                if (model.getuId().equals(deleteList.get(i).getuId())) {
                    it.remove();
                }
            }
        }
        return allList;
    }

    @Override
    public void updateUserOrderList(List<UserAll> users,boolean hasMore) {
        if (delList!=null&&delList.size()>0&&users!=null) {
            users = removeRepetitionItem(users, delList);
        }
        if (users==null){
            if (isNext){
                mRecyclerView.loadMoreComplete();
            }else {
                mRecyclerView.refreshComplete();
            }
        }else {
            if (isNext) {
                list.addAll(users);
                mRecyclerView.loadMoreComplete();
                adapter.notifyDataSetChanged();
                if (!hasMore) {
                    mRecyclerView.setNoMore(true);
                }
            } else {
                this.list = users;
                adapter = new FriendCheckAdapter(list, FriendCheckActivity.this, "0");
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.refreshComplete();
            }
        }
        if (mProgress!=null&&mProgress.isShowing()){
            mProgress.dismiss();
        }
    }

}
