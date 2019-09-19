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
package com.fanxin.easeui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.fanxin.easeui.EaseConstant;
import com.fanxin.easeui.utils.EaseCommonUtils;
import com.fanxin.easeui.utils.UserManager;
import com.fanxin.easeui.widget.EaseChatMessageList.MessageListItemClickListener;
import com.fanxin.easeui.widget.chatrow.EaseChatRow;
import com.fanxin.easeui.widget.chatrow.EaseChatRowBigExpression;
import com.fanxin.easeui.widget.chatrow.EaseChatRowFile;
import com.fanxin.easeui.widget.chatrow.EaseChatRowImage;
import com.fanxin.easeui.widget.chatrow.EaseChatRowLocation;
import com.fanxin.easeui.widget.chatrow.EaseChatRowText;
import com.fanxin.easeui.widget.chatrow.EaseChatRowVideo;
import com.fanxin.easeui.widget.chatrow.EaseChatRowVoice;
import com.fanxin.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.xiaomi.mimc.MIMCTokenFetcher;
import com.xiaomi.mimc.cipher.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EaseMessageAdapter extends BaseAdapter{

	private final static String TAG = "msg";

	private Context context;

	private static final int HANDLER_MESSAGE_REFRESH_LIST = 0;
	private static final int HANDLER_MESSAGE_SELECT_LAST = 1;
	private static final int HANDLER_MESSAGE_SEEK_TO = 2;

	private static final int MESSAGE_TYPE_RECV_TXT = 0;
	private static final int MESSAGE_TYPE_SENT_TXT = 1;
	private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
	private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
	private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
	private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
	private static final int MESSAGE_TYPE_SENT_VOICE = 6;
	private static final int MESSAGE_TYPE_RECV_VOICE = 7;
	private static final int MESSAGE_TYPE_SENT_VIDEO = 8;
	private static final int MESSAGE_TYPE_RECV_VIDEO = 9;
	private static final int MESSAGE_TYPE_SENT_FILE = 10;
	private static final int MESSAGE_TYPE_RECV_FILE = 11;
	private static final int MESSAGE_TYPE_SENT_EXPRESSION = 12;
	private static final int MESSAGE_TYPE_RECV_EXPRESSION = 13;


	public int itemTypeCount;

	// reference to conversation object in chatsdk
	private EMConversation conversation;
	//EMMessage[] messages = null;
	private List<EMMessage> messages = new ArrayList<>();
	private String toChatUsername;

	private MessageListItemClickListener itemClickListener;
	private EaseCustomChatRowProvider customRowProvider;

	private boolean showUserNick;
	private boolean showAvatar;
	private Drawable myBubbleBg;
	private Drawable otherBuddleBg;
	private String isLoad = "yes";

	private ListView listView;


	public EaseMessageAdapter(Context context, String username, int chatType, ListView listView) {
		this.context = context;
		this.listView = listView;
		toChatUsername = username;
		this.conversation = EMClient.getInstance().chatManager().getConversation(username, EaseCommonUtils.getConversationType(chatType), true);
	}


	Handler handler = new Handler() {
		private void refreshList() {
			// you should not call getAllMessages() in UI thread
			// otherwise there is problem when refreshing UI and there is new message arrive

			if (isLoad.equals("yes"))
			{
				isLoad = "no";
				getHistoryMessage();

			}else {
				notifyDataSetChanged();
			}
//			java.util.List<EMMessage> var = conversation.getAllMessages();
//			//messages = var.toArray(new EMMessage[var.size()]);
//			if (messages.size()>var.size())
//			{
//
//
//			}else {
//				messages = var;
//			}
//
//
//
//			conversation.markAllMessagesAsRead();
//			notifyDataSetChanged();
		}

		@Override
		public void handleMessage(android.os.Message message) {
			switch (message.what) {
				case HANDLER_MESSAGE_REFRESH_LIST:
					refreshList();
					break;
				case HANDLER_MESSAGE_SELECT_LAST:
					if (messages.size() > 0) {
						listView.setSelection(messages.size() - 1);
					}
					break;
				case HANDLER_MESSAGE_SEEK_TO:
					int position = message.arg1;
					listView.setSelection(position);
					break;
				default:
					break;
			}
		}
	};


    private void getHistoryMessage (){

    	new Thread(new Runnable() {
			@Override
			public void run() {

				UserManager userManager = UserManager.getInstance();

				String url = "https://mimc.chat.xiaomi.net/api/msg/p2p/queryOnCount/";

				String token = userManager.getUser().getAppAccount();

				String json = "{\"toAccount\":" + toChatUsername + ",\"fromAccount\":\"" + userManager.getUser().getAppAccount() + "\",\"utcToTime\":\"" +
						System.currentTimeMillis()+"" + "\",\"count\":\"" + "20" + "\"}";
				MediaType JSON = MediaType.parse("application/json;charset=utf-8");
				OkHttpClient client = new OkHttpClient();
				Request request = new Request
						.Builder()
						.url(url)
						.post(RequestBody.create(JSON, json))
						.addHeader("token",userManager.getUser().getToken())
						.addHeader("Content-Type","application/json;charset=UTF-8")
						.addHeader("Accept","application/json;charset=UTF-8")
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

						JSONObject object = data.getJSONObject("data");
						JSONArray array = object.getJSONArray("messages");

						for (int i = 0; i < array.length(); i++) {

							JSONObject object1 = (JSONObject) array.get(i);

							String fromAccount = object1.getString("fromAccount");
							String toAccount = object1.getString("toAccount");
							String payload = object1.getString("payload");

							String str2 = new String(Base64.decode(payload));

							JSONObject object2 = new JSONObject(str2);

							String content = new String(Base64.decode(object2.getString("content")));
							//String content = object2.getString("content");
							if (userManager.getAccount().equals(object2.getString("from"))) {

								EMMessage message = EMMessage.createTxtSendMessage(content, object2.getString("to"));

								message.setFrom(object2.getString("from"));
								message.setTo(object2.getString("to"));
								message.setMsgId(object2.getString("messageId"));

								JSONObject object3 = object2.getJSONObject("ext");
								message.setAttribute(object2.getString("from"), object3.getString(object2.getString("from")));
								message.setAttribute(object2.getString("to"), object3.getString(object2.getString("to")));
								message.setAttribute("name", object3.getString("name"));
								message.setAttribute("shareRed", object3.getString("shareRed"));
								message.setAttribute("type", object3.getString("type"));
								message.setAttribute("userName", object3.getString("userName"));
								message.setAttribute("userPic", object3.getString("userPic"));

								message.setDirection(EMMessage.Direct.SEND);

								messages.add(message);


								if (array.length() == messages.size()) {

									final int TIME_DELAY_REFRESH_SELECT_LAST = 100;
									handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST);
									handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST);
									handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST);
									handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST);

								}

								//	notifyDataSetChanged();

							} else {

								EMMessage message = EMMessage.createTxtSendMessage(content, object2.getString("from"));

								message.setFrom(object2.getString("from"));
								message.setTo(object2.getString("to"));
								message.setMsgId(object2.getString("messageId"));


								JSONObject object3 = object2.getJSONObject("ext");
								message.setAttribute(object2.getString("from"), object3.getString(object2.getString("from")));
								message.setAttribute(object2.getString("to"), object3.getString(object2.getString("to")));
								message.setAttribute("name", object3.getString("name"));
								message.setAttribute("shareRed", object3.getString("shareRed"));
								message.setAttribute("type", object3.getString("type"));
								message.setAttribute("userName", object3.getString("userName"));
								message.setAttribute("userPic", object3.getString("userPic"));


								message.setDirection(EMMessage.Direct.RECEIVE);
								messages.add(message);
								if (array.length() == messages.size()) {
									final int TIME_DELAY_REFRESH_SELECT_LAST = 100;
									handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST);
									handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST);
									handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST);
									handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST);

								}
								//	notifyDataSetChanged();

							}

						}

					}

				} catch (Exception e) {
					//logger.warn("Get token exception: " + e);
					e.printStackTrace();
				}

			}

		}).start();

    }

	public void refresh() {
		if (handler.hasMessages(HANDLER_MESSAGE_REFRESH_LIST)) {
			return;
		}
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST);
		handler.sendMessage(msg);
	}

	/**
	 * refresh and select the last
	 */
	public void refreshSelectLast() {
		final int TIME_DELAY_REFRESH_SELECT_LAST = 100;
		handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST);
		handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST);
		handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST);
		handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST);
	}

	public void refreshSelectLastMimc(EMMessage message){

		isLoad = "no";
		messages.add(message);

		final int TIME_DELAY_REFRESH_SELECT_LAST = 100;
		handler.removeMessages(HANDLER_MESSAGE_REFRESH_LIST);
		handler.removeMessages(HANDLER_MESSAGE_SELECT_LAST);
		handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_REFRESH_LIST, TIME_DELAY_REFRESH_SELECT_LAST);
		handler.sendEmptyMessageDelayed(HANDLER_MESSAGE_SELECT_LAST, TIME_DELAY_REFRESH_SELECT_LAST);

	}


	/**
	 * refresh and seek to the position
	 */
	public void refreshSeekTo(int position) {
		handler.sendMessage(handler.obtainMessage(HANDLER_MESSAGE_REFRESH_LIST));
		android.os.Message msg = handler.obtainMessage(HANDLER_MESSAGE_SEEK_TO);
		msg.arg1 = position;
		handler.sendMessage(msg);
	}


	public EMMessage getItem(int position) {
		if (messages != null && position < messages.size()) {
			return messages.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * get count of messages
	 */
	public int getCount() {
		return messages == null ? 0 : messages.size();
	}

	/**
	 * get number of message type, here 14 = (EMMessage.Type) * 2
	 */
	public int getViewTypeCount() {
		if(customRowProvider != null && customRowProvider.getCustomChatRowTypeCount() > 0){
			return customRowProvider.getCustomChatRowTypeCount() + 14;
		}
		return 14;
	}


	/**
	 * get type of item
	 */
	public int getItemViewType(int position) {
		EMMessage message = getItem(position);
		if (message == null) {
			return -1;
		}

		if(customRowProvider != null && customRowProvider.getCustomChatRowType(message) > 0){
			return customRowProvider.getCustomChatRowType(message) + 13;
		}

		if (message.getType() == EMMessage.Type.TXT) {
			if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
				return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_EXPRESSION : MESSAGE_TYPE_SENT_EXPRESSION;
			}
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
		}
		if (message.getType() == EMMessage.Type.IMAGE) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

		}
		if (message.getType() == EMMessage.Type.LOCATION) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
		}
		if (message.getType() == EMMessage.Type.VOICE) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;
		}
		if (message.getType() == EMMessage.Type.VIDEO) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO : MESSAGE_TYPE_SENT_VIDEO;
		}
		if (message.getType() == EMMessage.Type.FILE) {
			return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_FILE : MESSAGE_TYPE_SENT_FILE;
		}

		return -1;// invalid
	}

	protected EaseChatRow createChatRow(Context context, EMMessage message, int position) {
		EaseChatRow chatRow = null;
		if(customRowProvider != null && customRowProvider.getCustomChatRow(message, position, this) != null){
			return customRowProvider.getCustomChatRow(message, position, this);
		}
		switch (message.getType()) {
			case TXT:
				if(message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
					chatRow = new EaseChatRowBigExpression(context, message, position, this);
				}else{
					chatRow = new EaseChatRowText(context, message, position, this);
				}
				break;
			case LOCATION:
				chatRow = new EaseChatRowLocation(context, message, position, this);
				break;
			case FILE:
				chatRow = new EaseChatRowFile(context, message, position, this);
				break;
			case IMAGE:
				chatRow = new EaseChatRowImage(context, message, position, this);
				break;
			case VOICE:
				chatRow = new EaseChatRowVoice(context, message, position, this,conversation);
				break;
			case VIDEO:
				chatRow = new EaseChatRowVideo(context, message, position, this);
				break;
			default:
				break;
		}

		return chatRow;
	}


	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		EMMessage message = getItem(position);
		if(convertView == null){
			convertView = createChatRow(context, message, position);
		}

		//refresh ui with messages
		((EaseChatRow)convertView).setUpView(message, position, itemClickListener);

		return convertView;
	}


	public String getToChatUsername(){
		return toChatUsername;
	}



	public void setShowUserNick(boolean showUserNick) {
		this.showUserNick = showUserNick;
	}


	public void setShowAvatar(boolean showAvatar) {
		this.showAvatar = showAvatar;
	}


	public void setMyBubbleBg(Drawable myBubbleBg) {
		this.myBubbleBg = myBubbleBg;
	}


	public void setOtherBuddleBg(Drawable otherBuddleBg) {
		this.otherBuddleBg = otherBuddleBg;
	}


	public void setItemClickListener(MessageListItemClickListener listener){
		itemClickListener = listener;
	}

	public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider){
		customRowProvider = rowProvider;
	}


	public boolean isShowUserNick() {
		return showUserNick;
	}


	public boolean isShowAvatar() {
		return showAvatar;
	}


	public Drawable getMyBubbleBg() {
		return myBubbleBg;
	}


	public Drawable getOtherBuddleBg() {
		return otherBuddleBg;
	}

}
