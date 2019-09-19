/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.sangu.apptongji.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.main.activity.RegisterOneActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.TiXianActivity;
import com.sangu.apptongji.main.utils.MySingleton;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

/** 微信客户端回调activity示例 */
public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler{
	private IWXAPI api=null;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}
	/**
	 * 处理微信发出的向第三方应用请求app message
	 * <p>
	 * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
	 * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
	 * 做点其他的事情，包括根本不打开任何页面
	 */
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null) {
			Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
			startActivity(iLaunchMyself);
		}
	}

	/**
	 * 处理微信向第三方应用发起的消息
	 * <p>
	 * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
	 * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
	 * 回调。
	 * <p>
	 * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
	 */
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null
				&& (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onReq(BaseReq req) {
		Log.d("微信支付回调>>wxrep>>", "onPayFinish, errCode = " + req.toString());
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d("微信支付回调>>wxresp>>", "onPayFinish, errCode = " + resp.toString());
		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				String state = ((SendAuth.Resp) resp).state;
				if ("sangutixian".equals(state)) {
					String code = ((SendAuth.Resp) resp).code;//需要转换一下才可以
					getopenId(code);
				}else {
					Toast.makeText(WXEntryActivity.this,"授权成功",Toast.LENGTH_SHORT).show();
					startActivity(new Intent(WXEntryActivity.this, RegisterOneActivity.class));
					finish();
				}
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				Toast.makeText(WXEntryActivity.this,"取消授权",Toast.LENGTH_SHORT).show();
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				Toast.makeText(WXEntryActivity.this,"授权被拒绝了",Toast.LENGTH_SHORT).show();
				//发送被拒绝
				break;
			default:
				Toast.makeText(WXEntryActivity.this,"取消授权",Toast.LENGTH_SHORT).show();
				//发送返回
				break;
		}
	}

	private void getopenId(final String code) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				try {
					JSONObject object = new JSONObject(s);
					final String openId = object.getString("openid");
					if (openId!=null){
						startActivity(new Intent(WXEntryActivity.this,TiXianActivity.class).putExtra("openId",openId));
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Toast.makeText(WXEntryActivity.this,"授权失败"+volleyError.getMessage(),Toast.LENGTH_LONG).show();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> param = new HashMap<>();
				param.put("appid",Constant.APP_ID);
				param.put("secret",Constant.APP_SCR);
				param.put("code",code);
				param.put("grant_type","authorization_code");
				return param;
			}
		};
		MySingleton.getInstance(WXEntryActivity.this).addToRequestQueue(request);
	}

}
