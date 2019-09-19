/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sangu.apptongji.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.widget.EaseAlertDialog;
import com.fanxin.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.fanxin.easeui.widget.EaseSwitchButton;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.NetUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.adapter.group.GroupMemListAdapter;
import com.sangu.apptongji.main.alluser.entity.UserAll;
import com.sangu.apptongji.main.alluser.order.avtivity.FriendCheckActivity;
import com.sangu.apptongji.main.alluser.presenter.IGrMemPresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.GrMemPresenter;
import com.sangu.apptongji.main.alluser.view.IGrMemView;
import com.sangu.apptongji.main.utils.MySingleton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDetailsActivity extends BaseActivity implements OnClickListener,IGrMemView {
	private static final String TAG = "GroupDetailsActivity";
	private static final int REQUEST_CODE_ADD_USER =0;
	private static final int REQUEST_CODE_EXIT = 1;
	private static final int REQUEST_CODE_EXIT_DELETE = 2;
	private static final int REQUEST_CODE_EDIT_GROUPNAME = 5;

	private String groupId,updateName,groupName,groupImg;
	//	private ProgressBar loadingPB;
	private Button exitBtn;
	private Button deleteBtn;
	private EMGroup group;
	private ProgressDialog progressDialog;
	private IGrMemPresenter presenter;
	private RelativeLayout rl_switch_block_groupmsg;
	private XRecyclerView mRecyclerView;
	public static GroupDetailsActivity instance;
	private GroupMemListAdapter adapter;
	private List<UserAll> datas = new ArrayList<>();
	private boolean showDelete = false;

	private RelativeLayout clearAllHistory;
	//	private RelativeLayout blacklistLayout;
	private RelativeLayout rl_invite_member;
	private RelativeLayout changeGroupNameLayout;
	private RelativeLayout idLayout;
	private TextView idText,tv_delete;
	private EaseSwitchButton switchButton;
	private GroupChangeListener groupChangeListener;
	private RelativeLayout searchLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		groupId = getIntent().getStringExtra("groupId");
		group = EMClient.getInstance().groupManager().getGroup(groupId);
		// we are not supposed to show the group if we don't find the group
		if(group == null){
			finish();
			return;
		}

		setContentView(R.layout.em_activity_group_details);
		instance = this;
		presenter = new GrMemPresenter(this,this);
		MySingleton.getInstance(getApplicationContext()).getRequestQueue();
		groupName = group.getGroupName();
		mRecyclerView = (XRecyclerView) findViewById(R.id.xrv_group_member);
		clearAllHistory = (RelativeLayout) findViewById(R.id.clear_all_history);
//		loadingPB = (ProgressBar) findViewById(R.id.progressBar);
		exitBtn = (Button) findViewById(R.id.btn_exit_grp);
		deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);
//		blacklistLayout = (RelativeLayout) findViewById(R.id.rl_blacklist);
		rl_invite_member = (RelativeLayout) findViewById(R.id.rl_invite_member);
		changeGroupNameLayout = (RelativeLayout) findViewById(R.id.rl_change_group_name);
		idLayout = (RelativeLayout) findViewById(R.id.rl_group_id);
//		idLayout.setVisibility(View.VISIBLE);
		idText = (TextView) findViewById(R.id.tv_group_id_value);
		tv_delete = (TextView) findViewById(R.id.tv_delete);

		rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
		switchButton = (EaseSwitchButton) findViewById(R.id.switch_btn);
		searchLayout = (RelativeLayout) findViewById(R.id.rl_search);

		idText.setText(groupId);
		if (group.getOwner() == null || "".equals(group.getOwner()) || !group.getOwner().equals(EMClient.getInstance().getCurrentUser())) {
			exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.GONE);
			tv_delete.setVisibility(View.GONE);
//			blacklistLayout.setVisibility(View.GONE);
//			changeGroupNameLayout.setVisibility(View.GONE);
		}
		// show dismiss button if you are owner of group
		if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
			exitBtn.setVisibility(View.GONE);
			deleteBtn.setVisibility(View.VISIBLE);
			tv_delete.setVisibility(View.VISIBLE);
		}

		groupChangeListener = new GroupChangeListener();
		EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);

		((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount() + ")");

		// 保证每次进详情看到的都是最新的group
		updateGroup();
		updateGroupFromServer();
		clearAllHistory.setOnClickListener(this);
//		blacklistLayout.setOnClickListener(this);
		changeGroupNameLayout.setOnClickListener(this);
		rl_switch_block_groupmsg.setOnClickListener(this);
		searchLayout.setOnClickListener(this);
		rl_invite_member.setOnClickListener(this);
		initrxView();
	}

	private void initrxView() {
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
				presenter.loadGroupList(groupId,"0");
			}

			@Override
			public void onLoadMore() {
				presenter.loadGroupList(groupId,"0");
			}
		});
		adapter = new GroupMemListAdapter(GroupDetailsActivity.this,datas,showDelete,onDeleteClickListener);
		mRecyclerView.setAdapter(adapter);
		mRecyclerView.refresh(false);
		tv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDelete = !showDelete;
				if (showDelete){
					tv_delete.setText("取  消");
				}else {
					tv_delete.setText("删除成员");
				}
				adapter = new GroupMemListAdapter(GroupDetailsActivity.this,datas,showDelete,onDeleteClickListener);
				mRecyclerView.setAdapter(adapter);
			}
		});
	}

	private void updateGroupFromServer() {
		String url = FXConstant.URL_SELECT_GROUPINFO+groupId;
		StringRequest request = new StringRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				JSONObject object = JSON.parseObject(s);
				JSONObject groupInfo = object.getJSONObject("groupInfo");
				if (groupInfo!=null) {
					groupName = group.getGroupName();
					updateName = groupInfo.getString("updateName");
					groupImg = groupInfo.getString("groupImage");
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {

			}
		});
		MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String st1 = getResources().getString(R.string.being_added);
		String st2 = getResources().getString(R.string.is_quit_the_group_chat);
		String st3 = getResources().getString(R.string.chatting_is_dissolution);
		String st4 = getResources().getString(R.string.are_empty_group_of_news);
		String st5 = getResources().getString(R.string.is_modify_the_group_name);
		final String st6 = getResources().getString(R.string.Modify_the_group_name_successful);
		final String st7 = getResources().getString(R.string.change_the_group_name_failed_please);

		if (resultCode == RESULT_OK) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(GroupDetailsActivity.this);
				progressDialog.setMessage(st1);
				progressDialog.setCanceledOnTouchOutside(false);
			}
			switch (requestCode) {
				case REQUEST_CODE_ADD_USER:// 添加群成员
					final String[] newmembers = data.getStringArrayExtra("newmembers");
					final String[] newmemberName = data.getStringArrayExtra("newmemberName");
					progressDialog.setMessage(st1);
					progressDialog.show();
					addMembersToGroup(newmembers,newmemberName);
					break;
				case REQUEST_CODE_EXIT: // 退出群
					progressDialog.setMessage(st2);
					progressDialog.show();
					exitGrop();
					break;
				case REQUEST_CODE_EXIT_DELETE: // 解散群
					progressDialog.setMessage(st3);
					progressDialog.show();
					deleteGrop();
					break;

				case REQUEST_CODE_EDIT_GROUPNAME: //修改群名称
					final String returnData = data.getStringExtra("data");
					if(!TextUtils.isEmpty(returnData)){
						progressDialog.setMessage(st5);
						progressDialog.show();

						new Thread(new Runnable() {
							public void run() {
								try {
									EMClient.getInstance().groupManager().changeGroupName(groupId, returnData);
									runOnUiThread(new Runnable() {
										public void run() {
											updateGroupInServer(returnData,null,0);
											((TextView) findViewById(R.id.group_name)).setText(returnData + "(" + group.getAffiliationsCount()
													+ ")");
											progressDialog.dismiss();
											Toast.makeText(getApplicationContext(), st6, Toast.LENGTH_SHORT).show();
										}
									});

								} catch (HyphenateException e) {
									e.printStackTrace();
									runOnUiThread(new Runnable() {
										public void run() {
											progressDialog.dismiss();
											Toast.makeText(getApplicationContext(), st7, Toast.LENGTH_SHORT).show();
										}
									});
								}
							}
						}).start();
					}
					break;
				default:
					break;
			}
		}
	}

	protected void addUserToBlackList(final String username) {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage(getString(R.string.Are_moving_to_blacklist));
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().groupManager().blockUser(groupId, username);
					runOnUiThread(new Runnable() {
						public void run() {
							refreshMembers();
							pd.dismiss();
							Toast.makeText(getApplicationContext(), R.string.Move_into_blacklist_success,Toast.LENGTH_SHORT).show();
						}
					});
				} catch (HyphenateException e) {
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(), R.string.failed_to_move_into,Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	private void refreshMembers(){
		mRecyclerView.refresh(false);
	}

	/**
	 * 点击退出群组按钮
	 *
	 * @param view
	 */
	public void exitGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class), REQUEST_CODE_EXIT);

	}

	/**
	 * 点击解散群组按钮
	 *
	 * @param view
	 */
	public void exitDeleteGroup(View view) {
		startActivityForResult(new Intent(this, ExitGroupDialog.class).putExtra("deleteToast", getString(R.string.dissolution_group_hint)),
				REQUEST_CODE_EXIT_DELETE);

	}

	/**
	 * 清空群聊天记录
	 */
	private void clearGroupHistory() {

		EMConversation conversation = EMClient.getInstance().chatManager().getConversation(group.getGroupId(), EMConversationType.GroupChat);
		if (conversation != null) {
			conversation.clearAllMessages();
		}
		Toast.makeText(this, R.string.messages_are_empty,Toast.LENGTH_SHORT).show();
	}

	/**
	 * 退出群组
	 *
	 * groupId
	 */
	private void exitGrop() {
		String st1 = getResources().getString(R.string.Exit_the_group_chat_failure);
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().groupManager().leaveGroup(groupId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String exitId = DemoHelper.getInstance().getCurrentUsernName();
							String exitName = DemoApplication.getInstance().getCurrentUser().getName();
							exitGropInServer(exitId,exitName,1);
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.Exit_the_group_chat_failure) + " " + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}).start();
	}
	public String getString(String s, String s1)//s是需要删除某个子串的字符串s1是需要删除的子串
	{
		int postion = s.indexOf(s1);
		int length = s1.length();
		int Length = s.length();
		String newString = s.substring(0,postion) + s.substring(postion + length, Length);
		return newString;//返回已经删除好的字符串
	}
	private int getIndex(String[] imgUrl,String uId){
		if (imgUrl!=null&&uId!=null){
			for (int i=0;i<imgUrl.length;i++){
				if (imgUrl[i].indexOf(uId) != -1){
					return i;
				}
			}
		}
		return -1;
	}

	private void exitGropInServer(final String userId, final String username, final int type) {
		final String[] userName = {username};
		if (userName[0] ==null||"".equals(userName[0])){
			userName[0] = userId;
		}
		groupName = group.getGroupName();
		if (groupName.indexOf(userName[0]) != -1){
			String name = groupName.split("、")[0];
			if (name.equals(userName[0])){
				groupName = getString(groupName, userName[0] +"、");
			}else {
				groupName = getString(groupName, "、"+ userName[0]);
			}
		}
		if (groupImg!=null&&groupImg.indexOf(userId) != -1) {
			String[] img = groupImg.split("\\|");
			int index = getIndex(img, userId);
			if (index == 0) {
				groupImg = getString(groupImg, img[0] + "|");
			} else if (index != -1) {
				groupImg = getString(groupImg, "|"+img[index]);
			}
		}
		String url = FXConstant.URL_EXIT_GROUP;
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				Log.e("groupdetails,sexit",s);
				if (!"1".equals(updateName)) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								EMClient.getInstance().groupManager().changeGroupName(groupId,groupName);
							} catch (Exception e) {
								e.printStackTrace();
							}
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (type!=1) {
										updateGroupFromServer();
									}
									((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount() + ")");
								}
							});
						}
					}).start();
				}
				if (type==1){
					setResult(RESULT_OK);
					finish();
					if(ChatActivity.activityInstance != null)
						ChatActivity.activityInstance.finish();
				}else {
					refreshMembers();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.e("groupdetails,sexit","错误");
				setResult(RESULT_OK);
				finish();
				if(ChatActivity.activityInstance != null)
					ChatActivity.activityInstance.finish();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> param = new HashMap<>();
				param.put("groupId", groupId);
				if (!"1".equals(updateName)) {
					param.put("groupName", groupName);
				}
				param.put("groupImage", groupImg);
				param.put("u_id", userId);
				return param;
			}
		};
		MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
	}

	/**
	 * 解散群组
	 *
	 *  groupId
	 */
	private void deleteGrop() {
		final String st5 = getResources().getString(R.string.Dissolve_group_chat_tofail);
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().groupManager().destroyGroup(groupId);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							deleteGropInServer();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), st5 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}).start();
	}

	private void deleteGropInServer() {
		String url = FXConstant.URL_DELETE_GROUP+groupId;
		StringRequest request = new StringRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				setResult(RESULT_OK);
				finish();
				if(ChatActivity.activityInstance != null)
					ChatActivity.activityInstance.finish();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				setResult(RESULT_OK);
				finish();
				if(ChatActivity.activityInstance != null)
					ChatActivity.activityInstance.finish();
			}
		});
		MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
	}

	/**
	 * 增加群成员
	 *
	 * @param newmembers
	 */
	private void addMembersToGroup(final String[] newmembers,final String[] newmemberName) {
		final String st6 = getResources().getString(R.string.Add_group_members_fail);
		new Thread(new Runnable() {
			public void run() {
				try {
					// 创建者调用add方法
					if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
						EMClient.getInstance().groupManager().addUsersToGroup(groupId, newmembers);
					} else {
						// 一般成员调用invite方法
						EMClient.getInstance().groupManager().inviteUser(groupId, newmembers, null);
					}
					runOnUiThread(new Runnable() {
						public void run() {
							updateGroup();
							addMembersToServer(newmembers,newmemberName);
							((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount() + ")");
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), st6 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}).start();
	}

	private void addMembersToServer(final String[] newmembers,final String[] newmemberName) {
		groupName = group.getGroupName()+"、";
		String inviteId = "";
		for (int i=0;i<newmembers.length;i++){
			if (i!=newmembers.length-1) {
				groupName += newmemberName[i]+"、";
				inviteId += newmembers[i]+",";
			}else {
				groupName += newmemberName[i];
				inviteId += newmembers[i];
			}
		}
		String url = FXConstant.URL_INVITE_MEMBER;
		final String finalInviteId = inviteId;
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				Log.e("groupdetails,s",s);
				if ("0".equals(updateName)&&groupName.length()<40) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								EMClient.getInstance().groupManager().changeGroupName(groupId,groupName);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
				refreshMembers();
				progressDialog.dismiss();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {

			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> param = new HashMap<>();
				param.put("groupId", groupId);
				param.put("userGroup", finalInviteId);
				if ("0".equals(updateName)&&groupName.length()<40) {
					param.put("groupName", groupName);
				}
				Log.e("groupdetails,param",param.toString());
				return param;
			}
		};
		MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
	}

	private void updateGroupInServer(final String groupNewName,final String memImg,final int type) {
		String url = FXConstant.URL_UPDATE_GROUPINFO;
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				Log.e("groupdeac,supdate",s);
				if (type==1){
					setResult(RESULT_OK);
					finish();
					if (ChatActivity.activityInstance != null)
						ChatActivity.activityInstance.finish();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.e("groupdeac,supdate","错误");
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> param = new HashMap<>();
				param.put("groupId", groupId);
				if (groupNewName!=null) {
					param.put("groupName", groupNewName);
				}else if (memImg!=null){
					param.put("groupImage", memImg);
				}
				return param;
			}
		};
		MySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rl_switch_block_groupmsg: // 屏蔽或取消屏蔽群组
				toggleBlockGroup();
				break;

			case R.id.rl_invite_member: // 邀请好友进群租
				startActivityForResult(new Intent(GroupDetailsActivity.this, FriendCheckActivity.class)
								.putExtra("biaoshi","01").putExtra("delList", (Serializable) datas)
						,REQUEST_CODE_ADD_USER);
				break;

			case R.id.clear_all_history: // 清空聊天记录
				String st9 = getResources().getString(R.string.sure_to_empty_this);
				new EaseAlertDialog(GroupDetailsActivity.this, null, st9, null, new AlertDialogUser() {

					@Override
					public void onResult(boolean confirmed, Bundle bundle) {
						if(confirmed){
							clearGroupHistory();
						}
					}
				}, true).show();
				break;
//			case R.id.rl_blacklist: // 黑名单列表
//				startActivity(new Intent(GroupDetailsActivity.this, GroupBlacklistActivity.class).putExtra("groupId", groupId));
//				break;
			case R.id.rl_change_group_name:
				if (group.getOwner() == null || "".equals(group.getOwner()) || !group.getOwner().equals(EMClient.getInstance().getCurrentUser())) {
					Toast.makeText(getApplicationContext(),"只有群主才能更改名字!",Toast.LENGTH_SHORT).show();
					return;
				}
				startActivityForResult(new Intent(this, EditActivity.class).putExtra("data", group.getGroupName()), REQUEST_CODE_EDIT_GROUPNAME);
				break;
			case R.id.rl_search:
				startActivity(new Intent(this, GroupSearchMessageActivity.class).putExtra("groupId", groupId));
				break;
			default:
				break;
		}

	}

	private void toggleBlockGroup() {
		if(switchButton.isSwitchOpen()){
			EMLog.d(TAG, "change to unblock group msg");
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(GroupDetailsActivity.this);
				progressDialog.setCanceledOnTouchOutside(false);
			}
			progressDialog.setMessage(getString(R.string.Is_unblock));
			progressDialog.show();
			new Thread(new Runnable() {
				public void run() {
					try {
						EMClient.getInstance().groupManager().unblockGroupMessage(groupId);
						runOnUiThread(new Runnable() {
							public void run() {
								switchButton.closeSwitch();
								progressDialog.dismiss();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								Toast.makeText(getApplicationContext(), R.string.remove_group_of, Toast.LENGTH_LONG).show();
							}
						});

					}
				}
			}).start();

		} else {
			String st8 = getResources().getString(R.string.group_is_blocked);
			final String st9 = getResources().getString(R.string.group_of_shielding);
			EMLog.d(TAG, "change to block group msg");
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(GroupDetailsActivity.this);
				progressDialog.setCanceledOnTouchOutside(false);
			}
			progressDialog.setMessage(st8);
			progressDialog.show();
			new Thread(new Runnable() {
				public void run() {
					try {
						EMClient.getInstance().groupManager().blockGroupMessage(groupId);
						runOnUiThread(new Runnable() {
							public void run() {
								switchButton.openSwitch();
								progressDialog.dismiss();
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
						runOnUiThread(new Runnable() {
							public void run() {
								progressDialog.dismiss();
								Toast.makeText(getApplicationContext(), st9, Toast.LENGTH_LONG).show();
							}
						});
					}

				}
			}).start();
		}
	}

	/**
	 * 删除群成员
	 *
	 * @param userId
	 */
	protected void deleteMembersFromGroup(final String userId,final String userName) {
		final String st13 = getResources().getString(R.string.Are_removed);
		final String st14 = getResources().getString(R.string.Delete_failed);
		final ProgressDialog deleteDialog = new ProgressDialog(GroupDetailsActivity.this);
		deleteDialog.setMessage(st13);
		deleteDialog.setCanceledOnTouchOutside(false);
		deleteDialog.show();
		Log.e("groupdeac,usid",userId);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 删除被选中的成员
					EMClient.getInstance().groupManager().removeUserFromGroup(groupId, userId);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							deleteDialog.dismiss();
							exitGropInServer(userId,userName,0);
//							refreshMembers();
							((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "("
									+ group.getAffiliationsCount() + ")");
						}
					});
				} catch (final Exception e) {
					deleteDialog.dismiss();
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), st14 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}

			}
		}).start();
	}

	protected void updateGroup() {
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().groupManager().getGroupFromServer(groupId);

					runOnUiThread(new Runnable() {
						public void run() {
							((TextView) findViewById(R.id.group_name)).setText(group.getGroupName() + "(" + group.getAffiliationsCount()
									+ ")");
//							loadingPB.setVisibility(View.INVISIBLE);
//							refreshMembers();
							if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
								// 显示解散按钮
								exitBtn.setVisibility(View.GONE);
								deleteBtn.setVisibility(View.VISIBLE);
							} else {
								// 显示退出按钮
								exitBtn.setVisibility(View.VISIBLE);
								deleteBtn.setVisibility(View.GONE);
							}

							// update block
							EMLog.d(TAG, "group msg is blocked:" + group.isMsgBlocked());
							if (group.isMsgBlocked()) {
								switchButton.openSwitch();
							} else {
								switchButton.closeSwitch();
							}
						}
					});

				} catch (Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
//							loadingPB.setVisibility(View.INVISIBLE);
						}
					});
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
	}
	public OnDeleteClickListener onDeleteClickListener;

	public interface OnDeleteClickListener {
		void onItemClick(View view, int position);
	}
	public void setOnItemDelClickListener(OnDeleteClickListener listener){
		this.onDeleteClickListener = listener;
	}
	@Override
	public void updataGroupList(List<UserAll> userAlls, org.json.JSONObject groupInfo, boolean hasMore) {
		datas = userAlls;
		mRecyclerView.refreshComplete();
		mRecyclerView.setNoMore(true);
		adapter = new GroupMemListAdapter(GroupDetailsActivity.this, datas,showDelete,onDeleteClickListener);
		mRecyclerView.setAdapter(adapter);
		setOnItemDelClickListener(new OnDeleteClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				UserAll userAll = datas.get(position-1);
				String name = userAll.getuName();
				final String uId = userAll.getuId();
				if (name==null||"".equals(name)){
					name = uId;
				}
				final String st12 = getResources().getString(R.string.not_delete_myself);
				LayoutInflater inflater1 = LayoutInflater.from(GroupDetailsActivity.this);
				RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
				final Dialog dialog1 = new AlertDialog.Builder(GroupDetailsActivity.this,R.style.Dialog).create();
				dialog1.show();
				dialog1.getWindow().setContentView(layout1);
				dialog1.setCanceledOnTouchOutside(true);
				dialog1.setCancelable(true);
				TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
				Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
				final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
				TextView title = (TextView) layout1.findViewById(R.id.tv_title);
				title.setText("温馨提示");
				btnOK1.setText("确定");
				btnCancel1.setText("取消");
				title_tv1.setText("确定删除"+name+"吗？");
				btnCancel1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog1.dismiss();
					}
				});
				final String finalName = name;
				btnOK1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog1.dismiss();
						if (EMClient.getInstance().getCurrentUser().equals(uId)) {
							new EaseAlertDialog(GroupDetailsActivity.this, st12).show();
							return;
						}
						if (!NetUtils.hasNetwork(getApplicationContext())) {
							Toast.makeText(getApplicationContext(), getString(R.string.network_unavailable),Toast.LENGTH_SHORT).show();
							return;
						}
						deleteMembersFromGroup(uId, finalName);
					}
				});
			}
		});
	}

	@Override
	public void showproLoading() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(GroupDetailsActivity.this);
			progressDialog.setMessage("正在加载数据");
			progressDialog.setCanceledOnTouchOutside(true);
		}else {
			progressDialog.setMessage("正在加载数据");
			progressDialog.setCanceledOnTouchOutside(true);
		}
		progressDialog.show();
	}

	@Override
	public void hideproLoading() {
		if (progressDialog!=null&&progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}

	@Override
	public void showproError() {
		if (progressDialog!=null&&progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}

	private class GroupChangeListener implements EMGroupChangeListener{

		@Override
		public void onInvitationReceived(String groupId, String groupName,
										 String inviter, String reason) {
			// TODO Auto-generated method stub

		}

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
		public void onInvitationAccepted(String groupId, String inviter,
										 String reason) {
			runOnUiThread(new Runnable(){

				@Override
				public void run() {
					refreshMembers();
				}

			});

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee,
										 String reason) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
		}

		@Override
		public void onGroupDestroyed(String groupId, String groupName) {

		}

		@Override
		public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
			// TODO Auto-generated method stub

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

}
