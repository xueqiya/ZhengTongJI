package com.sangu.apptongji.main.moments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fanxin.easeui.EaseConstant;
import com.fanxin.easeui.ui.EaseBaiduMapActivity;
import com.fanxin.easeui.widget.EaseSwitchButton;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.sangu.apptongji.Constant;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.activity.NomalVideoPlayActivity;
import com.sangu.apptongji.main.activity.PaidanListActivity;
import com.sangu.apptongji.main.activity.SoftAgreementActivity;
import com.sangu.apptongji.main.activity.SoftUserAgreementActivity;
import com.sangu.apptongji.main.adapter.GridImageAdapter;
import com.sangu.apptongji.main.alluser.order.avtivity.ChongZhiActivity;
import com.sangu.apptongji.main.alluser.order.avtivity.WJPaActivity;
import com.sangu.apptongji.main.alluser.presenter.IPricePresenter;
import com.sangu.apptongji.main.alluser.presenter.impl.PricePresenter;
import com.sangu.apptongji.main.alluser.view.IPriceView;
import com.sangu.apptongji.main.mapsearch.MapPickerActivity;
import com.sangu.apptongji.main.utils.BitmapUtils;
import com.sangu.apptongji.main.utils.CashierInputFilter;
import com.sangu.apptongji.main.utils.FullyGridLayoutManager;
import com.sangu.apptongji.main.utils.LimitInputTextWatcher;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.main.utils.OkHttpManager;
import com.sangu.apptongji.main.utils.Param;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.main.utils.ZhifuHelpUtils;
import com.sangu.apptongji.main.widget.CustomProgressDialog;
import com.sangu.apptongji.main.widget.OnPasswordInputFinish;
import com.sangu.apptongji.main.widget.PasswordView;
import com.sangu.apptongji.ui.BaseActivity;
import com.sangu.apptongji.ui.ChatActivity;
import com.sangu.apptongji.utils.FileUtils;
import com.sangu.apptongji.utils.SelectPicPopupWindow;
import com.sangu.apptongji.utils.UserPermissionUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


public class MomentsPublishActivity extends BaseActivity implements OnClickListener ,IPriceView{
	private static final int SDK_PAY_FLAG = 21;
	private IPricePresenter presenter;
	private LinearLayout ll_location=null,llImage;
	private EditText et_location=null,et_location2=null;
	private LinearLayout ll_shy_A_yh=null;
	private LinearLayout ll_shh_A_xw=null;
	private EaseSwitchButton switchAllpeople=null;
	private EaseSwitchButton switchOnlyFriend=null;
	private EaseSwitchButton switchOnlyMyself=null;
	private EaseSwitchButton switchHuikui=null;
	private EaseSwitchButton switch_fenxiang=null;
	private RelativeLayout rl_hongbao=null;
	private LinearLayout rl_zhfjine=null;
	private RelativeLayout rl_shpxj=null;
	private EditText et_shpjg=null,et_hbjg=null,etzhfjg=null,etshpxj=null;
	/*图片适配器*/
	private List<LocalMedia> selectMedia = new ArrayList<>();
	private RecyclerView recyclerView;
	private GridImageAdapter adapter;
	private SelectPicPopupWindow menuWindow=null;

	private String filePath;
	private ArrayList<String> imagePaths1 = null;
	private MomentsPublishActivity instence=null;
	private String mylat="",mylon="",location="",city="",district="",street="";
	private ImageView iv_moshiyi=null,iv_moshier=null,iv_moshisan=null,iv_moshisi=null,iv_moshiwu=null;
	private CheckBox cb_moshiyi=null,cb_moshier=null,cb_moshisan=null,cb_moshisi=null,cb_moshiwu=null;
	// 显示位置的TextView
	private TextView tv_title=null,tv4=null,tv5=null,tv6=null;
	private TextView tv_shangpin_title=null;
	private String title=null;
	// 发送按钮
	private Button btn_send=null;
	// 文本输入
	private EditText et_biaoqian=null;
	private EditText et_miaoshu=null;
	private EditText et_didian=null;
	private EditText et_chujia=null;
	private EditText et_remark=null;
	private TextView tv_paidan_type=null;
	private TextView tv_qiehuan=null;
	private FrameLayout fl_video;
	private VideoView mVideoView;
	private int mPositionWhenPaused = -1;

	private LinearLayout ll_baozang;
	private EaseSwitchButton switch_baozang=null;
	private RelativeLayout rl_baozang,rl_switch_baozang;
	private EditText et_baocang_jine=null,et_baocang_shul;
	private TextView tv_time,tv_chose_time,tv_identify;
	private ImageView iv_bz_tupian,iv_del;
	private String baoZangIma = null,choTime="no";
	private boolean hasTupian = false;
	private boolean isBaoZang = false;

	private EditText et_content=null;
	private CustomProgressDialog dialog=null;
	double price=0.00;
	private int errorTime=3;
	private IWXAPI api=null;
	private String isSetRed="no";

	private LinearLayout rl_demandType;
	private TextView tv_demandType1,tv_demandType2,tv_demandType3,tv_demandTitle;
	private String demandType="0";

	//坐标动态选择是玩还是店
	private RelativeLayout rl_locationDynamicType;
	private TextView tv_play,tv_store;
    private String chatAuth = "0";

    RelativeLayout rl_biaozhu,rl_thridName,rl_thridPhone,rl_thridAdress;
    EditText et_thridName,et_thridPhone,et_thridAdress;

	//商业动态新增链接介绍
	EditText et_linkUrl;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG:
					@SuppressWarnings("unchecked")
					PayResult payResult = new PayResult((Map<String, String>) msg.obj);
					/**
					 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息
					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为9000则代表支付成功
					if (TextUtils.equals(resultStatus, "9000")) {
						// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
						Toast.makeText(MomentsPublishActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
					} else {
						// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
						Toast.makeText(MomentsPublishActivity.this, "充值失败", Toast.LENGTH_SHORT).show();
					}
					break;
			}
		}
	};
	private AlertDialog dialog4;

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		String zhifuZhuangtai = intent.getStringExtra("zhifu");
		if ("支付成功".equals(zhifuZhuangtai)){
			Toast.makeText(MomentsPublishActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
		}else {
			Toast.makeText(MomentsPublishActivity.this, "充值失败", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void back(View view) {
		super.back(view);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		instence = this;
		title = this.getIntent().getStringExtra("biaoshi");

        GetUserInfoChatAuth();

		if (title.equals("xuqiu")){
			setContentView(R.layout.activity_renwu);
			RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
			imagePaths1 = new ArrayList<>();
			llImage = (LinearLayout) findViewById(R.id.llImage);
			llImage.setFocusable(true);
			llImage.setFocusableInTouchMode(true);
			llImage.requestFocus();
			initRwView();
			initViews();

			final SharedPreferences mSharedPreferences5 = MomentsPublishActivity.this.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
			String userPromte = mSharedPreferences5.getString("userPromte2","0");

			if (userPromte.equals("1")){

				//阅读过

			}else {

				//没有阅读过
				initXQAlert();
			}

		}else {
			setContentView(R.layout.fx_activity_publish_moments);
			llImage = (LinearLayout) findViewById(R.id.llImage);
			llImage.setFocusable(true);
			llImage.setFocusableInTouchMode(true);
			llImage.requestFocus();
			api = WXAPIFactory.createWXAPI(this, null);
			api.registerApp(Constant.APP_ID);
			presenter = new PricePresenter(this,this);
			RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
			imagePaths1 = new ArrayList<>();
			price = DemoApplication.getInstance().getCurrenPrice();
			tv_title = (TextView) this.findViewById(R.id.tv_title);
			ll_location = (LinearLayout) this.findViewById(R.id.ll_location);
			initView();
			initViews();
			if (title.equals("shenghuo")) {
				tv_title.setText("发布案例动态");
			} else if (title.equals("xinwen")) {
				tv_title.setText("发布坐标分享");

				rl_locationDynamicType = (RelativeLayout) findViewById(R.id.rl_locationDynamicType);
				tv_play = (TextView) findViewById(R.id.tv_play);
				tv_store = (TextView) findViewById(R.id.tv_store);

				rl_locationDynamicType.setVisibility(View.VISIBLE);

				ll_location.setVisibility(View.VISIBLE);
				ll_baozang.setVisibility(View.VISIBLE);

				tv_play.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {

						demandType = "店";

						tv_play.setTextColor(Color.parseColor("#FFFFFF"));
						tv_play.setBackgroundResource(R.drawable.btn_corner_weizhigreen10);

						tv_store.setTextColor(Color.parseColor("#ffbebebe"));
						tv_store.setBackgroundResource(R.drawable.btn_stroke_weizhillightgery);

					}

				});

				tv_store.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {

						demandType = "玩";

						tv_play.setTextColor(Color.parseColor("#ffbebebe"));
						tv_play.setBackgroundResource(R.drawable.btn_stroke_weizhillightgery);


						tv_store.setTextColor(Color.parseColor("#FFFFFF"));
						tv_store.setBackgroundResource(R.drawable.btn_corner_weizhired10);

					}
				});

			} else if (title.equals("shangye")) {
				tv_title.setText("发布商业动态");
			} else if (title.equals("youhui")) {
				tv_title.setText("发布优惠活动");
			} else if (title.equals("xuqiu")) {
				tv_title.setText("发布需求动态");
			}
			if (title.equals("shenghuo") || title.equals("xinwen")) {
				initView1();
			} else if (title.equals("shangye") || title.equals("youhui") || title.equals("xuqiu")) {
				initView2();
				if (title.equals("youhui")) {
					initView3();
				}
				if (title.equals("xuqiu")) {
					initView4();
				}
			}
		}
	}

	private void initXQAlert(){

		LayoutInflater inflaterDl = LayoutInflater.from(MomentsPublishActivity.this);
		LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.dialog_issueprocess, null);
		final Dialog dialog2 = new AlertDialog.Builder(MomentsPublishActivity.this, R.style.Dialog).create();
		dialog2.show();
		dialog2.getWindow().setContentView(layout);
		WindowManager.LayoutParams params = dialog2.getWindow().getAttributes() ;
		Display display = MomentsPublishActivity.this.getWindowManager().getDefaultDisplay();
		params.width =(int) (display.getWidth()*0.85);                     //使用这种方式更改了dialog的框宽
		dialog2.getWindow().setAttributes(params);
		dialog2.setCancelable(true);
		dialog2.setCanceledOnTouchOutside(true);
		TextView tv_midBtn = (TextView) layout.findViewById(R.id.tv_midBtn);
		TextView tv_soft = (TextView) layout.findViewById(R.id.tv_softAgreement);
		TextView tv_user = (TextView) layout.findViewById(R.id.tv_userAgreement);
		final SharedPreferences mSharedPreferences5 = MomentsPublishActivity.this.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
		String userPromte = mSharedPreferences5.getString("userPromte2","0");

		if (userPromte.equals("1")){

			//阅读过
			tv_midBtn.setText("已同意");

		}

		tv_soft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(MomentsPublishActivity.this,SoftAgreementActivity.class);

				startActivity(intent);

			}
		});
		tv_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MomentsPublishActivity.this,SoftUserAgreementActivity.class);

				startActivity(intent);
			}
		});

		tv_midBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog2.dismiss();

				final SharedPreferences mSharedPreferences5 = MomentsPublishActivity.this.getSharedPreferences("zhuanfa", Context.MODE_PRIVATE);
				String userPromte = mSharedPreferences5.getString("userPromte2","0");
				SharedPreferences.Editor editor = mSharedPreferences5.edit();
				if (editor!=null) {
					editor.putString("userPromte2","1");
					editor.commit();
				}

			}
		});

	}

	private void initRwView() {

		rl_demandType = (LinearLayout) findViewById(R.id.rl_demandType);
		tv_demandType1 = (TextView)findViewById(R.id.tv_demandType1);
		tv_demandType2 = (TextView)findViewById(R.id.tv_demandType2);
		tv_demandType3 = (TextView)findViewById(R.id.tv_demandType3);
		tv_demandTitle = (TextView) findViewById(R.id.tv1);

		mVideoView = (VideoView) findViewById(R.id.uVideoView);
		fl_video = (FrameLayout) findViewById(R.id.fl_video);
		btn_send = (Button) findViewById(R.id.btn_send);
		et_biaoqian = (EditText) findViewById(R.id.et_biaoqian);
		et_miaoshu = (EditText) findViewById(R.id.et_miaoshu);
		et_didian = (EditText) findViewById(R.id.et_didian);
		et_chujia = (EditText) findViewById(R.id.et_chujia);
	//	tv_paidan_type = (TextView) findViewById(R.id.tv_paidan_type);
		tv_qiehuan = (TextView) findViewById(R.id.tv_qiehuan);
		mVideoView.setZOrderOnTop(true);
		et_chujia.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		InputFilter[] filters={new CashierInputFilter()};
		et_chujia.setFilters(filters);
		et_biaoqian.addTextChangedListener(new LimitInputTextWatcher(et_biaoqian));
		et_didian.setFocusable(false);
		et_didian.setFocusableInTouchMode(false);
		et_remark = (EditText) findViewById(R.id.et_chujia1);

		rl_biaozhu = (RelativeLayout) findViewById(R.id.rl_biaozhu);
		rl_thridName = (RelativeLayout) findViewById(R.id.rl_thridName);
		rl_thridPhone = (RelativeLayout) findViewById(R.id.rl_thridPhone);
		rl_thridAdress = (RelativeLayout) findViewById(R.id.rl_thridAdress);

		et_thridName = (EditText) findViewById(R.id.et_thridName);
		et_thridPhone = (EditText) findViewById(R.id.et_thridPhone);
		et_thridAdress = (EditText) findViewById(R.id.et_thridAdress);

		String type = getIntent().getStringExtra("type");
		if (type != null && type.equals("thrid")){

			rl_biaozhu.setVisibility(View.GONE);
			rl_thridName.setVisibility(View.VISIBLE);
			rl_thridPhone.setVisibility(View.VISIBLE);
			rl_thridAdress.setVisibility(View.VISIBLE);

		}

		TextView alert = (TextView)findViewById(R.id.tv_orderAlert);
		alert.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initXQAlert();
			}
		});

		et_didian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivityForResult(new Intent(MomentsPublishActivity.this, MapPickerActivity.class).putExtra("biaoshi","01"),5);
			}
		});
//		tv_qiehuan.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if (tv_paidan_type.getText().toString().trim().equals("向所有人派单")){
//					tv_paidan_type.setText("仅向企业派单");
//				}else {
//					tv_paidan_type.setText("向所有人派单");
//				}
//			}
//		});
		btn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				UserPermissionUtil.getUserPermission(MomentsPublishActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "5", new UserPermissionUtil.UserPermissionListener() {
					@Override
					public void onAllow() {
						LayoutInflater inflaterDl = LayoutInflater.from(MomentsPublishActivity.this);
						RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_resend_paidan, null);
						final Dialog dialog2 = new AlertDialog.Builder(MomentsPublishActivity.this, R.style.Dialog).create();
						dialog2.show();
						dialog2.getWindow().setContentView(layout);
						Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
						Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
						TextView title = (TextView) layout.findViewById(R.id.title_tv);
						title.setText("确定为“真实需求”吗\n" +
								"\n" +
								"此处不能发布；广告、营销、等违法信息\n" +
								"将会被禁止使用功能");
						btnCancel.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog2.dismiss();
							}
						});
						btnOK.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog2.dismiss();
								if (dialog != null && dialog.isShowing()) {
									dialog.dismiss();
								}

								String orderType = "01";
								final String task_label = et_biaoqian.getText().toString().trim();
								String chujia = et_chujia.getText().toString().trim();
								String remark = et_remark.getText().toString().trim();
								String task_position = mylat+"|"+mylon;
								String task_locaName = street;
								String miaoshu = et_miaoshu.getText().toString().trim();
								String thridName = et_thridName.getText().toString().trim();
								String thridPhone = et_thridPhone.getText().toString().trim();
								String thridAdress = et_thridAdress.getText().toString().trim();

								String task_jurisdiction = "01";

								if (chujia!=null&&!TextUtils.isEmpty(chujia)&&Double.parseDouble(chujia)<=0){

									Toast.makeText(getApplicationContext(),"出价必须大于0！",Toast.LENGTH_SHORT).show();
									return;
								}
								if (task_label==null||"".equals(task_label)||task_label.length()>6){

									Toast.makeText(getApplicationContext(),"请输入标签,内容不得超过6位",Toast.LENGTH_SHORT).show();
									return;
								}
								if (task_position==null||"".equals(task_position)){

									Toast.makeText(getApplicationContext(),"坐标获取错误,请从新选择",Toast.LENGTH_SHORT).show();
									return;
								}
								if (task_locaName==null||"".equals(task_locaName)){

									Toast.makeText(getApplicationContext(),"位置获取错误,请从新选择",Toast.LENGTH_SHORT).show();
									return;
								}
								if (miaoshu==null||"".equals(miaoshu)){

									Toast.makeText(getApplicationContext(),"请输入描述",Toast.LENGTH_SHORT).show();
									return;
								}
								if (imagePaths1.size()==0){

									Toast.makeText(getApplicationContext(),"请选择图片",Toast.LENGTH_SHORT).show();
									return;
								}

								if (remark !=null && remark.length()>10){

									Toast.makeText(getApplicationContext(),"标注内容不得超过10个字",Toast.LENGTH_SHORT).show();
									return;
								}

								String type = getIntent().getStringExtra("type");
								if (type != null && type.equals("thrid")){

									if (thridName !=null && thridName.length()>0){
									}else {
										Toast.makeText(getApplicationContext(),"请输入客户姓名",Toast.LENGTH_SHORT).show();
										return;
									}

									if (thridPhone !=null && thridPhone.length()>0){
									}else {
										Toast.makeText(getApplicationContext(),"请输入客户电话",Toast.LENGTH_SHORT).show();
										return;
									}

									if (thridAdress !=null && thridAdress.length()>0){
									}else {
										Toast.makeText(getApplicationContext(),"请输入客户地址",Toast.LENGTH_SHORT).show();
										return;
									}


								}

								LayoutInflater inflaterDl1 = LayoutInflater.from(MomentsPublishActivity.this);
								RelativeLayout layout1 = (RelativeLayout) inflaterDl1.inflate(R.layout.dialog_loading, null);
								dialog4 = new AlertDialog.Builder(MomentsPublishActivity.this, R.style.Dialog).create();
								dialog4.show();
								dialog4.getWindow().setContentView(layout1);
								dialog4.setCancelable(false);
								dialog4.setCanceledOnTouchOutside(false);

								WeakReference<MomentsPublishActivity> reference = new WeakReference<MomentsPublishActivity>(MomentsPublishActivity.this);
								dialog = CustomProgressDialog.createDialog(reference.get());
								dialog.setCanceledOnTouchOutside(false);
								dialog.setCanceledOnTouchOutside(false);
								dialog.setMessage("正在发布...");
								//dialog.show();
								List<Param> params=new ArrayList<>();
								if (chujia!=null&&!TextUtils.isEmpty(chujia)&&Double.parseDouble(chujia)>0){
									params.add(new Param("floorPrice",Double.parseDouble(chujia)+""));
									params.add(new Param("contrastTime",getNowTime()));
								}

								if (remark != null && !TextUtils.isEmpty(remark)){

									params.add(new Param("task_jurisdiction",remark));

								}else {
									params.add(new Param("task_jurisdiction","01"));
								}

								String newType = getIntent().getStringExtra("newType");

								if (newType != null && newType.equals("1")){

									params.add(new Param("newType","1"));

								}


								params.add(new Param("demandType",demandType));

								params.add(new Param("content",miaoshu));
								params.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
								params.add(new Param("task_label",task_label));
								params.add(new Param("task_position",task_position));
								params.add(new Param("task_locaName",task_locaName));
								params.add(new Param("dType","05"));
								params.add(new Param("orderType",orderType));
								params.add(new Param("deviceType","android4.5正事多"));
								params.add(new Param("authType","01"));

								if (getIntent().getStringExtra("type") != null && getIntent().getStringExtra("type").equals("thrid")){

									params.add(new Param("thridInfo",thridName+"|"+thridPhone+"|"+thridAdress));

								}
								if (filePath==null) {
									OkHttpManager.getInstance().postMoment(params, imagePaths1, FXConstant.URL_PUBLISH, new OkHttpManager.HttpCallBack() {
										@Override
										public void onResponse(JSONObject jsonObject) {
											if (jsonObject == null) {
												Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
												return;
											}
											Log.e("chen", "onResponse" + jsonObject.toString());
											String code = jsonObject.getString("code");
											if (code.equals("SUCCESS")) {
												Toast.makeText(getApplicationContext(), "发布成功...", Toast.LENGTH_SHORT).show();
												String timel1 = getNowTime();
												SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
												SharedPreferences.Editor editor = mSharedPreferences.edit();
												String newType = getIntent().getStringExtra("newType");

												if (newType != null && newType.equals("1")){

													editor.putString("time1", timel1);

												}

												editor.commit();
												SharedPreferences mSharedPreference = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
												SharedPreferences.Editor meditor = mSharedPreference.edit();
												meditor.putString("sendTime", getNowTime());
												meditor.commit();
												queryAllUser(task_label);
												startActivity(new Intent(MomentsPublishActivity.this, PaidanListActivity.class).putExtra("type","1"));
											} else if (code.equals("已存在")) {
												LayoutInflater inflaterDl = LayoutInflater.from(MomentsPublishActivity.this);
												RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_resend_paidan, null);
												final Dialog chongfu = new AlertDialog.Builder(MomentsPublishActivity.this, R.style.Dialog).create();
												chongfu.show();
												chongfu.setCancelable(true);
												chongfu.setCanceledOnTouchOutside(true);
												chongfu.getWindow().setContentView(layout);
												Button btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
												Button btnOK = (Button) layout.findViewById(R.id.btn_ok);
												TextView title = (TextView) layout.findViewById(R.id.title_tv);
												title.setText("请勿频繁发布相同需求");
												btnCancel.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														if (chongfu != null && chongfu.isShowing()) {
															chongfu.dismiss();
														}
													}
												});
												btnOK.setOnClickListener(new OnClickListener() {
													@Override
													public void onClick(View v) {
														if (chongfu != null && chongfu.isShowing()) {
															chongfu.dismiss();
														}
													}
												});
											} else {
												if (dialog != null && dialog.isShowing()) {
													dialog.dismiss();
												}
												Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
											}
											if (dialog4 != null && dialog4.isShowing()) {

												dialog4.dismiss();
											}
										}

										@Override
										public void onFailure(String errorMsg) {
											if (dialog != null && dialog.isShowing()) {
												dialog.dismiss();
											}
											if (dialog4 != null && dialog4.isShowing()) {

												dialog4.dismiss();
											}
											Log.e("chen", "onFailure" + errorMsg);
											Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
										}
									});
								}else {
									sendVideo(miaoshu,params);
								}



							}
						});


					}

					@Override
					public void onBan() {
						if (dialog4 != null && dialog4.isShowing()) {

							dialog4.dismiss();
						}
						ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发布派单");

					}
				});


			}
		});


		tv_demandTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (rl_demandType.getVisibility() == View.GONE){

					rl_demandType.setVisibility(View.VISIBLE);

				}else {

					rl_demandType.setVisibility(View.GONE);

				}

			}
		});

		tv_demandType1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

                rl_demandType.setVisibility(View.GONE);
				tv_demandTitle.setText("服   务");
				demandType = "0";
			}
		});
		tv_demandType2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

                rl_demandType.setVisibility(View.GONE);
				tv_demandTitle.setText("产   品");
				demandType = "1";
			}
		});
		tv_demandType3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

                rl_demandType.setVisibility(View.GONE);
				tv_demandTitle.setText("方   案");
				demandType = "2";
			}
		});


	}

	private void queryAllUser(final String task_label) {
		String url = FXConstant.URL_DYNAMIC_DUANXIN;
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				Log.e("momentpublic",s);
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				updateBmob();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				updateBmob();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> param = new HashMap<>();
				param.put("log",mylon);
				param.put("lat",mylat);
				param.put("userId",DemoHelper.getInstance().getCurrentUsernName());
				param.put("task_label",task_label);
				return param;
			}
		};
		MySingleton.getInstance(MomentsPublishActivity.this).addToRequestQueue(request);
	}

	private void initView4() {
		tv_shangpin_title.setText("设置需求金额");
		et_shpjg.setHint("请输入需求金额");
	}
	private void initViews(){
		recyclerView = (RecyclerView) findViewById(R.id.recycler);
		FullyGridLayoutManager manager = new FullyGridLayoutManager(MomentsPublishActivity.this, 4, GridLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(manager);
		adapter = new GridImageAdapter(MomentsPublishActivity.this, onAddPicClickListener);
		adapter.setList(selectMedia);
		adapter.setSelectMax(8);
		recyclerView.setAdapter(adapter);
		adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(int position, View v) {
				switch (selectMedia.get(position).getType()) {
					case FunctionConfig.TYPE_IMAGE:
						// 预览图片 可长按保存 也可自定义保存路径
//                        PictureConfig.getInstance().externalPicturePreview(MainActivity.this, "/custom_file", position, selectMedia);
						PictureConfig.getInstance().externalPicturePreview(MomentsPublishActivity.this, position, selectMedia);
						break;
					case FunctionConfig.TYPE_VIDEO:
						// 预览视频
						if (selectMedia.size() > 0) {
							PictureConfig.getInstance().externalPictureVideo(MomentsPublishActivity.this, selectMedia.get(position).getPath());
						}
						break;
				}
			}
		});
	}

	/**
	 * 删除图片回调接口
	 */
	private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
		@Override
		public void onAddPicClick(int type, int position) {
			switch (type) {
				case 0:
					if (filePath==null) {
						selectImgs();
					}else {
						Toast.makeText(getApplicationContext(),"动态视频只能选择一个",Toast.LENGTH_SHORT).show();
					}
					break;
				case 1:
					// 删除图片
					imagePaths1.remove(position);
					selectMedia.remove(position);
					if (filePath!=null){
						filePath=null;
					}
					adapter.notifyItemRemoved(position);
					break;
			}
		}
	};

	private void selectImgs(){
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(instence.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		if (imagePaths1.size()>0){
			menuWindow = new SelectPicPopupWindow(MomentsPublishActivity.this,0,itemsOnClick);
		}else {
			menuWindow = new SelectPicPopupWindow(MomentsPublishActivity.this, 1, itemsOnClick);
		}
		//设置弹窗位置
		menuWindow.showAtLocation(MomentsPublishActivity.this.findViewById(R.id.llImage), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	private View.OnClickListener itemsOnClick = new View.OnClickListener() {
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
				case R.id.item_popupwindows_camera:        //点击拍照按钮
					isBaoZang = false;
					if (imagePaths1.size()>0) {
						goCamera(FunctionConfig.TYPE_VIDEO, 1, true, false);
					}else {
						goCamera(FunctionConfig.TYPE_VIDEO, 1, false, false);
					}
					break;
				case R.id.item_popupwindows_Photo:       //点击从相册中选择按钮
					goCamera(FunctionConfig.TYPE_IMAGE,8,false,false);
					break;
				default:
					break;
			}
		}

	};
	private void goCamera(int type1,int count,boolean mode,boolean canCut){
		WeakReference<MomentsPublishActivity> reference = new WeakReference<MomentsPublishActivity>(MomentsPublishActivity.this);
		FunctionOptions options = new FunctionOptions.Builder()
				.setType(type1) // 图片or视频 FunctionConfig.TYPE_IMAGE  TYPE_VIDEO
				.setCropMode(FunctionConfig.CROP_MODEL_1_1) // 裁剪模式 默认、1:1、3:4、3:2、16:9
//                            .setOffsetX() // 自定义裁剪比例
//                            .setOffsetY() // 自定义裁剪比例
				.setCompress(true) //是否压缩
				.setEnablePixelCompress(true) //是否启用像素压缩
				.setEnableQualityCompress(true) //是否启质量压缩
				.setMaxSelectNum(count) // 可选择图片的数量
				.setMinSelectNum(0)// 图片或视频最低选择数量，默认代表无限制
				.setSelectMode(FunctionConfig.MODE_MULTIPLE) // 单选 or 多选
				.setShowCamera(true) //是否显示拍照选项 这里自动根据type 启动拍照或录视频
				.setEnablePreview(true) // 是否打开预览选项
				.setEnableCrop(canCut) // 是否打开剪切选项
				.setCircularCut(false)// 是否采用圆形裁剪
				.setPreviewVideo(true) // 是否预览视频(播放) mode or 多选有效
				.setCheckedBoxDrawable(0)
				.setRecordVideoDefinition(FunctionConfig.HIGH) // 视频清晰度
				.setRecordVideoSecond(60) // 视频秒数
				.setCustomQQ_theme(0)// 可自定义QQ数字风格，不传就默认是蓝色风格
				.setGif(false)// 是否显示gif图片，默认不显示
				.setCropW(0) // cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
				.setCropH(0) // cropH-->裁剪高度 值不能小于100 如果值大于图片原始宽高 将返回原图大小
				.setMaxB(102400) // 压缩最大值 例如:200kb  就设置202400，202400 / 1024 = 200kb
				.setPreviewColor(ContextCompat.getColor(reference.get(), R.color.blue)) //预览字体颜色
				.setCompleteColor(ContextCompat.getColor(reference.get(), R.color.blue)) //已完成字体颜色
				.setPreviewBottomBgColor(0) //预览图片底部背景色
				.setPreviewTopBgColor(0)//预览图片标题背景色
				.setBottomBgColor(0) //图片列表底部背景色
				.setGrade(Luban.THIRD_GEAR) // 压缩档次 默认三档
				.setCheckNumMode(false)
				.setCompressQuality(100) // 图片裁剪质量,默认无损
				.setImageSpanCount(4) // 每行个数
				.setVideoS(0)// 查询多少秒内的视频 单位:秒
				.setSelectMedia(selectMedia) // 已选图片，传入在次进去可选中，不能传入网络图片
				.setCompressFlag(1) // 1 系统自带压缩 2 luban压缩
				.setCompressW(0) // 压缩宽 如果值大于图片原始宽高无效
				.setCompressH(0) // 压缩高 如果值大于图片原始宽高无效
				.setThemeStyle(ContextCompat.getColor(reference.get(), R.color.blue)) // 设置主题样式
				.setNumComplete(false) // 0/9 完成  样式
				.setClickVideo(false)// 点击声音
				.setFreeStyleCrop(false) // 裁剪是移动矩形框或是图片
				.create();
		if (mode) {
			// 只拍照
			PictureConfig.getInstance().init(options).startOpenCamera(reference.get());
		} else {
			// 先初始化参数配置，在启动相册
			PictureConfig.getInstance().init(options).openPhoto(reference.get(), resultCallback);
		}
	}

	/**
	 * 图片回调方法
	 */
	private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
		@Override
		public void onSelectSuccess(List<LocalMedia> resultList) {
			fl_video.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			imagePaths1.clear();
			// 多选回调
			selectMedia = resultList;
			if (selectMedia != null) {
				if (resultList.get(0).getType()==FunctionConfig.TYPE_VIDEO){
					String path = resultList.get(0).getPath();
					startActivityForResult(new Intent(MomentsPublishActivity.this, NomalVideoPlayActivity.class).putExtra("video_path",path),11);
				}else {
					for (int i = 0; i < selectMedia.size(); i++) {
						imagePaths1.add(selectMedia.get(i).getCompressPath());
					}
					adapter.setList(selectMedia);
					adapter.notifyDataSetChanged();
				}
			}
		}

		@Override
		public void onSelectSuccess(LocalMedia media) {
			fl_video.setVisibility(View.GONE);
			recyclerView.setVisibility(View.VISIBLE);
			imagePaths1.clear();
			// 单选回调
			selectMedia.add(media);
			if (selectMedia != null) {
				if (media.getType()==FunctionConfig.TYPE_VIDEO){
					filePath = media.getPath();
					imagePaths1.add(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/mingpCut.png");
					adapter.setList(selectMedia);
					adapter.notifyDataSetChanged();
				}else {
					imagePaths1.add(media.getCompressPath());
					adapter.setList(selectMedia);
					adapter.notifyDataSetChanged();
				}
			}
		}
	};

	private void initView1() {
		ll_shy_A_yh.setVisibility(View.GONE);
		ll_shh_A_xw.setVisibility(View.GONE);
		if (title.equals("shenghuo")){
			ll_shh_A_xw.setVisibility(View.VISIBLE);
		}
//		rl_fenxiang.setEnabled(true);
		RelativeLayout rlswitchAllpeople = (RelativeLayout) this.findViewById(R.id.rl_switch_allpeople);
		RelativeLayout rlswitchOnlyFriend = (RelativeLayout) this.findViewById(R.id.rl_switch_onlyFriend);
		RelativeLayout rlswitchOnlyMyself = (RelativeLayout) this.findViewById(R.id.rl_switch_onlyMyself);
		switchAllpeople.openSwitch();
		switchOnlyFriend.closeSwitch();
		switchOnlyMyself.closeSwitch();
//		rl_fenxiang.setOnClickListener(this);
		rlswitchAllpeople.setOnClickListener(this);
		rlswitchOnlyFriend.setOnClickListener(this);
		rlswitchOnlyMyself.setOnClickListener(this);
	}

	private void initView2() {
//		rl_fenxiang.setVisibility(View.GONE);
		ll_shy_A_yh.setVisibility(View.VISIBLE);
		ll_shh_A_xw.setVisibility(View.GONE);
		//RelativeLayout rl_switch_huikui = (RelativeLayout) this.findViewById(R.id.rl_switch_huikui);
		rl_shpxj = (RelativeLayout) this.findViewById(R.id.rl_shpxj);
		rl_zhfjine = (LinearLayout) this.findViewById(R.id.rl_zhfjine);
		rl_hongbao = (RelativeLayout) this.findViewById(R.id.rl_hongbao);
		tv4 = (TextView) this.findViewById(R.id.textview4);
		tv5 = (TextView) this.findViewById(R.id.textview5);
		tv6 = (TextView) this.findViewById(R.id.textview6);
		et_location2 = (EditText) this.findViewById(R.id.et_location2);
		et_location2.setFocusable(false);
		et_location2.setFocusableInTouchMode(false);
		tv_identify = (TextView) this.findViewById(R.id.tv_identify);
		et_linkUrl = (EditText) this.findViewById(R.id.et_linkUrl);

		String lng = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLng()) ? "0" : DemoApplication.getInstance().getCurrentLng();
		String lat = TextUtils.isEmpty(DemoApplication.getInstance().getCurrentLat()) ? "0" : DemoApplication.getInstance().getCurrentLat();

		if (lng.equals("0") || lat.equals("0")){

		}else {

			mylat = lat;
			mylon = lng;
			SharedPreferences sp = getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
			final String city = sp.getString("city", "0");

			if (city.equals("0")){

			}else {
				et_location2.setText(city);
			}

		}

		et_location2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivityForResult(new Intent(MomentsPublishActivity.this, MapPickerActivity.class).putExtra("biaoshi","01"),5);
			}
		});

	//	rl_switch_huikui.setOnClickListener(this);
	//	rl_shpxj.setVisibility(View.GONE);
	//	tv6.setVisibility(View.GONE);

//		tv4.setVisibility(View.GONE);
//		tv5.setVisibility(View.GONE);
//		rl_zhfjine.setVisibility(View.GONE);
//		rl_hongbao.setVisibility(View.GONE);

		if (title.equals("shangye")){

			tv4.setVisibility(View.VISIBLE);
			tv5.setVisibility(View.VISIBLE);
			rl_zhfjine.setVisibility(View.VISIBLE);
			//rl_hongbao.setVisibility(View.VISIBLE);

			rl_shpxj.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (isSetRed.equals("no")){

						//开
						isSetRed = "yes";
						tv_identify.setVisibility(View.VISIBLE);
						rl_hongbao.setVisibility(View.VISIBLE);

					}else {
						//关
						isSetRed = "no";
						tv_identify.setVisibility(View.INVISIBLE);
						rl_hongbao.setVisibility(View.GONE);
					}

				}
			});

		}

	}

	private void initView3() {
		rl_shpxj.setVisibility(View.VISIBLE);
		tv6.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		String imageIndex = "";
		switch (v.getId()){
			case R.id.rl_switch_baozang:
				if (switch_baozang.isSwitchOpen()){
					switch_baozang.closeSwitch();
					rl_baozang.setVisibility(View.GONE);
				}else {
					switch_baozang.openSwitch();
					rl_baozang.setVisibility(View.VISIBLE);
				}
				break;
//			case R.id.tv_chose_time:
//				new DatePickerDialog(MomentsPublishActivity.this, new DatePickerDialog.OnDateSetListener() {
//					@Override
//					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//						SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//						SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");
//						long nowTime = Long.parseLong(format.format(new Date()));
//						monthOfYear = monthOfYear+1;
//						String time2 = format2.format(new Date());
//						String choseTime = String.valueOf(year)+String.valueOf(monthOfYear)+String.valueOf(dayOfMonth);
//						if (Long.parseLong(choseTime)<=nowTime){
//							Toast.makeText(MomentsPublishActivity.this, "选择的时间不合法,请从新选择！", Toast.LENGTH_LONG).show();
//							return;
//						}
//						//choTime = choseTime+time2;
//						String birth = year+"-"+monthOfYear+"-"+dayOfMonth;
//						//tv_time.setText(birth);
//					}
//				}, 2017,07,20).show();
//				break;
//			case R.id.tv_from:
//				tv_time.setText("永久有效");
//				choTime = "no";
//				break;
			case R.id.iv_bz_tupian:
				if (hasTupian){
					startActivity(new Intent(MomentsPublishActivity.this,BiImageActivity.class).putExtra("imagePath",baoZangIma));
				}else {
					isBaoZang = true;
					goCamera(FunctionConfig.TYPE_IMAGE,1,true,true);
				}
				break;
			case R.id.iv_del:
				iv_bz_tupian.setImageResource(R.drawable.fx_icon_add);
				hasTupian = false;
				baoZangIma = null;
				iv_del.setVisibility(View.GONE);
				break;
			case R.id.rl_switch_allpeople:
				if (switchAllpeople.isSwitchOpen()){
					switchAllpeople.closeSwitch();
					switchOnlyFriend.openSwitch();
					switchOnlyMyself.closeSwitch();
				}else {
					switchAllpeople.openSwitch();
					switchOnlyMyself.closeSwitch();
					switchOnlyFriend.closeSwitch();
				}
				break;
			case R.id.rl_switch_onlyFriend:
				if (switchOnlyFriend.isSwitchOpen()){
					switchOnlyFriend.closeSwitch();
					switchOnlyMyself.openSwitch();
					switchAllpeople.closeSwitch();
				}else {
					switchOnlyFriend.openSwitch();
					switchAllpeople.closeSwitch();
					switchOnlyMyself.closeSwitch();
				}
				break;
			case R.id.rl_switch_onlyMyself:
				if (switchOnlyMyself.isSwitchOpen()){
					switchOnlyMyself.closeSwitch();
					switchAllpeople.openSwitch();
					switchOnlyFriend.closeSwitch();
				}else {
					switchOnlyMyself.openSwitch();
					switchAllpeople.closeSwitch();
					switchOnlyFriend.closeSwitch();
				}
				break;
//			case R.id.rl_switch_huikui:
//				if (switchHuikui.isSwitchOpen()){
//					switchHuikui.closeSwitch();
//					tv4.setVisibility(View.GONE);
//					tv5.setVisibility(View.GONE);
//					rl_zhfjine.setVisibility(View.GONE);
//					rl_hongbao.setVisibility(View.GONE);
//				}else {
//					switchHuikui.openSwitch();
//					tv4.setVisibility(View.VISIBLE);
//					tv5.setVisibility(View.VISIBLE);
//					rl_zhfjine.setVisibility(View.VISIBLE);
//					rl_hongbao.setVisibility(View.VISIBLE);
//				}
//				break;
//			case R.id.rl_fenxiang:
//				if (switch_fenxiang.isSwitchOpen()){
//					switch_fenxiang.closeSwitch();
//				}else {
//					switch_fenxiang.openSwitch();
//				}
//				break;
			case R.id.cb_moshiyi:
				cb_moshiyi.setChecked(true);
				cb_moshier.setChecked(false);
				cb_moshisan.setChecked(false);
				cb_moshisi.setChecked(false);
				cb_moshiwu.setChecked(false);
				break;
			case R.id.cb_moshier:
				cb_moshier.setChecked(true);
				cb_moshiyi.setChecked(false);
				cb_moshisan.setChecked(false);
				cb_moshisi.setChecked(false);
				cb_moshiwu.setChecked(false);
				break;
			case R.id.cb_moshisan:
				cb_moshisan.setChecked(true);
				cb_moshier.setChecked(false);
				cb_moshiyi.setChecked(false);
				cb_moshisi.setChecked(false);
				cb_moshiwu.setChecked(false);
				break;
			case R.id.cb_moshisi:
				cb_moshisan.setChecked(false);
				cb_moshier.setChecked(false);
				cb_moshiyi.setChecked(false);
				cb_moshisi.setChecked(true);
				cb_moshiwu.setChecked(false);
				break;
			case R.id.cb_moshiwu:
				cb_moshisan.setChecked(false);
				cb_moshier.setChecked(false);
				cb_moshiyi.setChecked(false);
				cb_moshisi.setChecked(false);
				cb_moshiwu.setChecked(true);
				break;
			case R.id.iv_moshiyi:
//				cb_moshiyi.setChecked(true);
//				cb_moshier.setChecked(false);
//				cb_moshisan.setChecked(false);
				imageIndex = "01";
				startActivity(new Intent(MomentsPublishActivity.this,BiImageActivity.class).putExtra("imageIndex",imageIndex));
				break;
			case R.id.iv_moshier:
//				cb_moshier.setChecked(true);
//				cb_moshiyi.setChecked(false);
//				cb_moshisan.setChecked(false);
				imageIndex = "02";
				startActivity(new Intent(MomentsPublishActivity.this,BiImageActivity.class).putExtra("imageIndex",imageIndex));
				break;
			case R.id.iv_moshisan:
//				cb_moshisan.setChecked(true);
//				cb_moshier.setChecked(false);
//				cb_moshiyi.setChecked(false);
				imageIndex = "03";
				startActivity(new Intent(MomentsPublishActivity.this,BiImageActivity.class).putExtra("imageIndex",imageIndex));
				break;
			case R.id.iv_moshisi:
//				cb_moshisan.setChecked(true);
//				cb_moshier.setChecked(false);
//				cb_moshiyi.setChecked(false);
				imageIndex = "04";
				startActivity(new Intent(MomentsPublishActivity.this,BiImageActivity.class).putExtra("imageIndex",imageIndex));
				break;
			case R.id.iv_moshiwu:
//				cb_moshisan.setChecked(true);
//				cb_moshier.setChecked(false);
//				cb_moshiyi.setChecked(false);
				imageIndex = "05";
				startActivity(new Intent(MomentsPublishActivity.this,BiImageActivity.class).putExtra("imageIndex",imageIndex));
				break;
			case R.id.btn_send:
			//	switchAllpeople.openSwitch();

                if (title.equals("shangye")){

                    if (chatAuth.equals("1")){

                        LayoutInflater inflaterD5 = LayoutInflater.from(MomentsPublishActivity.this);
                        LinearLayout layout5 = (LinearLayout) inflaterD5.inflate(R.layout.dialog_prodynamicauth_alert, null);
                        final Dialog dialog5 = new AlertDialog.Builder(MomentsPublishActivity.this, R.style.Dialog).create();
                        dialog5.show();
                        dialog5.getWindow().setContentView(layout5);
                        WindowManager.LayoutParams params = dialog5.getWindow().getAttributes() ;
                        Display display = MomentsPublishActivity.this.getWindowManager().getDefaultDisplay();
                        params.width =(int) (display.getWidth()*0.7); //使用这种方式更改了dialog的框宽
                        dialog5.getWindow().setAttributes(params);
                        dialog5.setCancelable(true);
                        dialog5.setCanceledOnTouchOutside(true);

                        TextView midBtn = (TextView) layout5.findViewById(R.id.tv_mid);

                        midBtn.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog5.dismiss();

                                Intent intent2 = new Intent(MomentsPublishActivity.this, ChatActivity.class);
                                intent2.putExtra("userId", "18337101357");
                                intent2.putExtra("chatType", Constant.CHATTYPE_SINGLE);
                                intent2.putExtra(EaseConstant.EXTRA_USER_TYPE, "单聊");
                                //  intent2.putExtra(EaseConstant.EXTRA_USER_IMG,"zhengshiduo.png");
                                // intent2.putExtra(EaseConstant.EXTRA_USER_NAME,"李璐");
                                intent2.putExtra(EaseConstant.EXTRA_USER_SHARERED,"无");
                                startActivity(intent2);

                            }
                        });

                        return;

                    }

                }

				final String content = et_content.getText().toString().trim();
				SharedPreferences sp = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
				String sendTime = sp.getString("sendTime",null);
				String nowTime = getNowTime();
				if (sendTime!=null&&(Long.parseLong(nowTime)-(Long.parseLong(sendTime)))<200){
					new AlertDialog.Builder(MomentsPublishActivity.this)
							.setMessage("\n您的操作过于频繁,歇两分钟再发动态吧\n")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							})
							.show();
					return;
				}
				if (cb_moshisan.isChecked()){
					new AlertDialog.Builder(MomentsPublishActivity.this)
							.setMessage("\n依照相关法律，请加入获得相关资质的汽车租赁或网约车企业后，才能开启打车模板\n")
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							})
							.show();
					return;
				}
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(getApplicationContext(), "请输入文字内容....",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (imagePaths1.size() == 0) {
					Toast.makeText(getApplicationContext(), "请选择图片或者视频",
							Toast.LENGTH_SHORT).show();
					return;
				}else if (imagePaths1.size()>8){
					Toast.makeText(getApplicationContext(), "图片不能多于八张....",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (title.equals("shangye")||title.equals("youhui")||title.equals("xuqiu")){
					send(content);
				}else if (title.equals("xinwen")){

					if (demandType.equals("0")){

						Toast.makeText(MomentsPublishActivity.this,"请选择类型",Toast.LENGTH_SHORT).show();

						return;

					}

					if (location==null||"".equals(location)||location.equals(null)){
						Toast.makeText(MomentsPublishActivity.this,"请先选择地址",Toast.LENGTH_SHORT).show();
						return;
					}

					if (switch_baozang.isSwitchOpen()) {
						String baozangAll = et_baocang_jine.getText().toString().trim();
						String baozangEach = et_baocang_shul.getText().toString().trim();
						if (baoZangIma == null) {
							Toast.makeText(getApplicationContext(), "请选择宝藏图片",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (TextUtils.isEmpty(baozangAll)||Double.parseDouble(baozangAll)==0) {
							Toast.makeText(getApplicationContext(), "请输入宝藏总金额",
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (TextUtils.isEmpty(baozangEach)||Double.parseDouble(baozangEach)==0) {
							Toast.makeText(getApplicationContext(), "请输入单个宝藏金额",
									Toast.LENGTH_SHORT).show();
							return;
						}
						double d = (Double.parseDouble(baozangAll)*100)%(Double.parseDouble(baozangEach)*100);
						if (d!=0){
							Toast.makeText(getApplicationContext(), "请输入能均分总金额的单个金额大小",
									Toast.LENGTH_LONG).show();
							return;
						}
						zhifu(content);
					}else {
						send(content);
					}
				}else {
					send(content);
				}
				break;
		}
	}

	private void zhifu(final String content) {
		final String pass = DemoApplication.getInstance().getCurrentPayPass();
		double yue = DemoApplication.getInstance().getCurrenPrice();
		final double zonge = Double.parseDouble(et_baocang_jine.getText().toString().trim());
		if (pass==null||"".equals(pass)){
			ZhifuHelpUtils.showErrorMiMaSHZH(this,pass,"000");
			return;
		}
		if (pass.length()!=6||!ZhifuHelpUtils.isNumeric(pass)){
			ZhifuHelpUtils.showErrorMiMaXG(this,pass,"000");
			return;
		}
		if (errorTime<=0){
			ZhifuHelpUtils.showErrorLing(this);
			return;
		}
		if (DemoApplication.getInstance().getFreezeCurrentType().equalsIgnoreCase("01")) {
			ToastUtils.showNOrmalToast(this.getApplicationContext(), "您的账户已被冻结");
		} else {
			if (zonge>yue){
				LayoutInflater inflaterDl = LayoutInflater.from(MomentsPublishActivity.this);
				RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
				final Dialog dialog = new AlertDialog.Builder(MomentsPublishActivity.this,R.style.Dialog).create();
				dialog.show();
				dialog.getWindow().setContentView(layout);
				dialog.setCanceledOnTouchOutside(true);
				RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
				RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
				RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
				RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
				re_item1.setVisibility(View.GONE);
				TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
				TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
				TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
				TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
				tv_title.setText("钱包余额不足(剩余"+price+"元),请您选择其他支付方式");
				tv_item1.setText("");
				tv_item2.setText("微信充值");
				tv_item5.setText("支付宝充值");
				re_item1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
				re_item2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						rechargefromWx(zonge+"");
					}
				});
				re_item5.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						rechargefromZhFb(zonge+"");
					}
				});
				re_item3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				return;
			}else {
				LayoutInflater inflaterDl = LayoutInflater.from(MomentsPublishActivity.this);
				final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
				final Dialog dialog = new AlertDialog.Builder(MomentsPublishActivity.this,R.style.Dialog).create();
				dialog.show();
				dialog.getWindow().setContentView(layout);
				dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				dialog.getWindow().setGravity(Gravity.BOTTOM);
				dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
				WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
				lp.width = WindowManager.LayoutParams.MATCH_PARENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				dialog.getWindow().setAttributes(lp);
				final PasswordView pwdView = (PasswordView)layout.findViewById(R.id.pwd_view);
				pwdView.setOnFinishInput(new OnPasswordInputFinish() {
					@Override
					public void inputFinish() {
						if (pwdView.getStrPassword().equals(pass)) {
							dialog.dismiss();
							send(content);
						} else {
							int times;
							if (errorTime>0){
								times = errorTime-1;
							}else {
								times = 0;
							}
							reduceShRZFCount(times+"");
							if (times==0) {
								ZhifuHelpUtils.showErrorLing(MomentsPublishActivity.this);
							}else {
								ZhifuHelpUtils.showErrorTishi(MomentsPublishActivity.this,times + "",null,"000");
							}
							dialog.dismiss();
						}
					}
				});
				/**
				 *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
				 *  如果写了，会覆盖我们在自定义控件中提供的响应
				 *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
				pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				pwdView.getForgetTextView().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String bs;
						bs = "000";
						startActivity(new Intent(MomentsPublishActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
					}
				});
			}
		}
	}

	private void reduceShRZFCount(final String times) {
		String url = FXConstant.URL_UPDATEZHHU;
		StringRequest request3 = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				com.alibaba.fastjson.JSONObject object = JSON.parseObject(s);
				String code = object.getString("code");
				if (code.equals("SUCCESS")){
					if (errorTime>0) {
						errorTime--;
					}
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params1 = new HashMap<>();
				params1.put("merId", DemoHelper.getInstance().getCurrentUsernName());
				params1.put("enterErrorTimes", times+"");
				return params1;
			}
		};
		MySingleton.getInstance(MomentsPublishActivity.this).addToRequestQueue(request3);
	}

	//查看当前用户chatauth字段的值默认1  禁止发商业动态  允许

	private void GetUserInfoChatAuth(){

		String url = FXConstant.URL_Get_UserInfo+DemoHelper.getInstance().getCurrentUsernName();

		StringRequest request = new StringRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				JSONObject object = JSON.parseObject(s);
				String code = object.getString("code");

                com.alibaba.fastjson.JSONObject userInfo = object.getJSONObject("userInfo");

                chatAuth = userInfo.getString("chatAuth");

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {

			}
		});

		MySingleton.getInstance(MomentsPublishActivity.this).addToRequestQueue(request);

	}


	private void initView() {
		mVideoView = (VideoView) findViewById(R.id.uVideoView);
		fl_video = (FrameLayout) findViewById(R.id.fl_video);
		et_baocang_shul = (EditText) this.findViewById(R.id.et_baocang_shul);
		et_shpjg = (EditText) this.findViewById(R.id.et_shpjg);
		et_hbjg = (EditText) this.findViewById(R.id.et_hbjg);
		etzhfjg = (EditText) this.findViewById(R.id.etzhfjg);
		etshpxj = (EditText) this.findViewById(R.id.et_shpxj);
		tv_shangpin_title = (TextView) this.findViewById(R.id.tv_shangpin_title);
		iv_moshiyi = (ImageView) this.findViewById(R.id.iv_moshiyi);
		iv_moshier = (ImageView) this.findViewById(R.id.iv_moshier);
		iv_moshisan = (ImageView) this.findViewById(R.id.iv_moshisan);
		iv_moshisi = (ImageView) this.findViewById(R.id.iv_moshisi);
		iv_moshiwu = (ImageView) this.findViewById(R.id.iv_moshiwu);
		cb_moshiyi = (CheckBox) this.findViewById(R.id.cb_moshiyi);
		cb_moshier = (CheckBox) this.findViewById(R.id.cb_moshier);
		cb_moshisan = (CheckBox) this.findViewById(R.id.cb_moshisan);
		cb_moshisi = (CheckBox) this.findViewById(R.id.cb_moshisi);
		cb_moshiwu = (CheckBox) this.findViewById(R.id.cb_moshiwu);
		et_shpjg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		et_hbjg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		etzhfjg.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		etshpxj.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		InputFilter[] filters={new CashierInputFilter()};
		et_shpjg.setFilters(filters);
		et_hbjg.setFilters(filters);
		etzhfjg.setFilters(filters);
		etshpxj.setFilters(filters);

		ll_baozang = (LinearLayout) findViewById(R.id.ll_baozang);
		switch_baozang = (EaseSwitchButton) findViewById(R.id.switch_baozang);
		rl_baozang = (RelativeLayout) findViewById(R.id.rl_baozang);
		rl_switch_baozang = (RelativeLayout) findViewById(R.id.rl_switch_baozang);

//		if (title.equals("xinwen")) {
//
//			switch_baozang.openSwitch();
//			switch_baozang.setVisibility(View.INVISIBLE);
//			rl_baozang.setVisibility(View.VISIBLE);
//
//		}



		et_baocang_jine = (EditText) findViewById(R.id.et_baocang_jine);
		//tv_time = (TextView) findViewById(R.id.tv_from);
		//tv_chose_time = (TextView) findViewById(R.id.tv_chose_time);
		iv_bz_tupian = (ImageView) findViewById(R.id.iv_bz_tupian);
		iv_del = (ImageView) findViewById(R.id.iv_del);
		mVideoView.setZOrderOnTop(true);
		et_baocang_jine.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		et_baocang_jine.setFilters(filters);
		et_baocang_shul.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		et_baocang_shul.setFilters(filters);
		switchAllpeople = (EaseSwitchButton) this.findViewById(R.id.switch_allpeople);
		switchOnlyFriend = (EaseSwitchButton) this.findViewById(R.id.switch_onlyFriend);
		switchOnlyMyself = (EaseSwitchButton) this.findViewById(R.id.switch_onlyMyself);
		//switchHuikui = (EaseSwitchButton) this.findViewById(R.id.switch_huikui);
		switch_fenxiang = (EaseSwitchButton) this.findViewById(R.id.switch_fenxiang);
		Bitmap b1 = BitmapUtils.readBitMap(MomentsPublishActivity.this,R.drawable.xiadanmoshiyi);
		Bitmap b2 = BitmapUtils.readBitMap(MomentsPublishActivity.this,R.drawable.xiadanmoshier);
		Bitmap b3 = BitmapUtils.readBitMap(MomentsPublishActivity.this,R.drawable.xiadanmoshisan);
		Bitmap b4 = BitmapUtils.readBitMap(MomentsPublishActivity.this,R.drawable.xiadanmoshisi);
		Bitmap b5 = BitmapUtils.readBitMap(MomentsPublishActivity.this,R.drawable.xiadanmoshiwu);
		iv_moshiyi.setImageBitmap(b1);
		iv_moshier.setImageBitmap(b2);
		iv_moshisan.setImageBitmap(b3);
		iv_moshisi.setImageBitmap(b4);
		iv_moshiwu.setImageBitmap(b5);
		iv_moshiyi.setOnClickListener(this);
		iv_moshier.setOnClickListener(this);
		iv_moshisan.setOnClickListener(this);
		iv_moshisi.setOnClickListener(this);
		iv_moshiwu.setOnClickListener(this);
		cb_moshiyi.setOnClickListener(this);
		cb_moshier.setOnClickListener(this);
		cb_moshisan.setOnClickListener(this);
		cb_moshisi.setOnClickListener(this);
		cb_moshiwu.setOnClickListener(this);
		cb_moshiyi.setChecked(true);
		rl_switch_baozang.setOnClickListener(this);
		//tv_chose_time.setOnClickListener(this);
		//tv_time.setOnClickListener(this);
		iv_bz_tupian.setOnClickListener(this);
		iv_del.setOnClickListener(this);
		//switchHuikui.closeSwitch();
		switchAllpeople.closeSwitch();
		switchOnlyFriend.closeSwitch();
		switchOnlyMyself.closeSwitch();
		switch_fenxiang.closeSwitch();
		ll_shh_A_xw = (LinearLayout) this.findViewById(R.id.ll_shh_A_xw);
		ll_shy_A_yh = (LinearLayout) this.findViewById(R.id.ll_shy_A_yh);
		// 获取位置
		et_location = (EditText) this.findViewById(R.id.et_location);
		ll_location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(MomentsPublishActivity.this, EaseBaiduMapActivity.class).putExtra("biaoshi","00"),4);
			}
		});
		et_location.setFocusable(false);
		et_location.setFocusableInTouchMode(false);
		et_location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(MomentsPublishActivity.this, EaseBaiduMapActivity.class).putExtra("biaoshi","00"),4);
			}
		});
		et_content = (EditText) this.findViewById(R.id.et_content);
		btn_send = (Button) this.findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
	}
	// 发送
	private void send(final String content) {
		final String shpjg = TextUtils.isEmpty(et_shpjg.getText().toString().trim())?"":et_shpjg.getText().toString().trim();
		final String hbjg = TextUtils.isEmpty(et_hbjg.getText().toString().trim())?"":et_hbjg.getText().toString().trim();
		final String zhfjg = TextUtils.isEmpty(etzhfjg.getText().toString().trim())?"":etzhfjg.getText().toString().trim();
		final String shpxj = TextUtils.isEmpty(etshpxj.getText().toString().trim())?"":etshpxj.getText().toString().trim();
		if (title.equals("shangye")){
			if ("".equals(shpjg)||Double.parseDouble(shpjg)<=0){
				Toast.makeText(MomentsPublishActivity.this,"请输入线上价,并且金额不能为0",Toast.LENGTH_SHORT).show();
				return;
			}

			if (shpxj.equals("")||Double.parseDouble(shpxj)<=0){
				Toast.makeText(MomentsPublishActivity.this,"请输入线下价，并且金额不能为0",Toast.LENGTH_SHORT).show();
				return;
			}

			if (mylat.equals("") || mylat.equals("0") || mylon.equals("") || mylon.equals("0")){

				Toast.makeText(MomentsPublishActivity.this,"请选择地址",Toast.LENGTH_SHORT).show();
				return;

			}

			String link = et_linkUrl.getText().toString().trim();

			if (link != null && link.length()>0 && link.length()<8){

				Toast.makeText(MomentsPublishActivity.this,"请输入正确链接或不填写",Toast.LENGTH_SHORT).show();
				return;

			}

		}

		if (title.equals("youhui")){


			if (shpjg.equals("")){
				Toast.makeText(MomentsPublishActivity.this,"请输入商品原价",Toast.LENGTH_SHORT).show();
				return;
			}

			if (shpxj.equals("")){

				Toast.makeText(MomentsPublishActivity.this,"请输入商品现价",Toast.LENGTH_SHORT).show();
				return;

			}

		}

		if (title.equals("xuqiu")){
			if ("".equals(shpjg)||Double.parseDouble(shpjg)<=0){
				Toast.makeText(MomentsPublishActivity.this,"请输入需求金额,并且需求金额不能为0",Toast.LENGTH_SHORT).show();
				return;
			}
			final String pass = DemoApplication.getInstance().getCurrentPayPass();
			final double yue = DemoApplication.getInstance().getCurrenPrice();
			if (yue<Double.parseDouble(shpjg)){
				LayoutInflater inflater1 = LayoutInflater.from(MomentsPublishActivity.this);
				RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
				final Dialog dialog1 = new AlertDialog.Builder(MomentsPublishActivity.this,R.style.Dialog).create();
				dialog1.show();
				dialog1.getWindow().setContentView(layout1);
				dialog1.setCanceledOnTouchOutside(true);
				dialog1.setCancelable(true);
				TextView title_tv1 = (TextView) layout1.findViewById(R.id.title_tv);
				Button btnCancel1 = (Button) layout1.findViewById(R.id.btn_cancel);
				final Button btnOK1 = (Button) layout1.findViewById(R.id.btn_ok);
				TextView title = (TextView) layout1.findViewById(R.id.tv_title);
				title.setText("温馨提示");
				btnOK1.setText("前去充值");
				btnCancel1.setText("以后再说");
				title_tv1.setText("发布需求动态时,会对您账户余额进行验资，但不扣除金额，您的余额不足，无法发布！");
				btnCancel1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog1.dismiss();
					}
				});
				btnOK1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog1.dismiss();
						Intent intent2 = new Intent(MomentsPublishActivity.this, ChongZhiActivity.class);
						intent2.putExtra("papass",pass);
						intent2.putExtra("price",yue);
						intent2.putExtra("biaoshi","00");
						startActivityForResult(intent2,1);
					}
				});
				return;
			}
		}
		if (title.equals("shangye") && isSetRed.equals("yes")) {

			if (isSetRed.equals("yes")){

				//设置商业红包了
				if (hbjg.equals("") || zhfjg.equals("") || Double.valueOf(hbjg) <= 0 || Double.valueOf(zhfjg) <= 0) {
					Toast.makeText(MomentsPublishActivity.this, "总红包和红包量不能为空!", Toast.LENGTH_SHORT).show();
					return;
				}

				//计算红包是否正确
				Double a = Double.valueOf(hbjg)*100;
				Double b = Double.valueOf(zhfjg);
				final Double c = a/b;

				final String d = ""+new DecimalFormat("0.00").format(c);
				final String e = d.split("\\.")[1];

				if (e.equals("00")){

				}else {
					Toast.makeText(MomentsPublishActivity.this, "请输入对应的总红包与红包量", Toast.LENGTH_SHORT).show();
					return;
				}

			}

//			if (Double.valueOf(hbjg)<Double.valueOf(zhfjg)){
//				Toast.makeText(MomentsPublishActivity.this, "转发金额不能大于红包金额!", Toast.LENGTH_SHORT).show();
//				return;
//			}
			final String pass = DemoApplication.getInstance().getCurrentPayPass();
			final double yue = DemoApplication.getInstance().getCurrenPrice();
			if (pass==null||"".equals(pass)){
				ZhifuHelpUtils.showErrorMiMaSHZH(this,pass,"000");
				return;
			}
			if (pass.length()!=6||!ZhifuHelpUtils.isNumeric(pass)){
				ZhifuHelpUtils.showErrorMiMaXG(this,pass,"000");
				return;
			}
			if (errorTime<=0){
				ZhifuHelpUtils.showErrorLing(this);
				return;
			}
			double zonge = 0.00;
			if (title.equals("shangye") && isSetRed.equals("yes")){
				zonge = Double.valueOf(hbjg);
			}else {
			//	zonge = Double.valueOf(hbjg)+Double.parseDouble(shpjg);
			}
			if (zonge >yue){
				LayoutInflater inflaterDl = LayoutInflater.from(MomentsPublishActivity.this);
				RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.layout_setting_dialog, null);
				final Dialog dialog = new AlertDialog.Builder(MomentsPublishActivity.this,R.style.Dialog).create();
				dialog.show();
				dialog.getWindow().setContentView(layout);
				dialog.setCanceledOnTouchOutside(true);
				RelativeLayout re_item1 = (RelativeLayout) dialog.findViewById(R.id.re_item1);
				RelativeLayout re_item2 = (RelativeLayout) dialog.findViewById(R.id.re_item2);
				RelativeLayout re_item3 = (RelativeLayout) dialog.findViewById(R.id.re_item3);
				RelativeLayout re_item5 = (RelativeLayout) dialog.findViewById(R.id.re_item5);
				re_item1.setVisibility(View.GONE);
				TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
				TextView tv_item1 = (TextView) dialog.findViewById(R.id.tv_item1);
				TextView tv_item2 = (TextView) dialog.findViewById(R.id.tv_item2);
				TextView tv_item5 = (TextView) dialog.findViewById(R.id.tv_item5);
				tv_title.setText("钱包余额不足(剩余"+price+"元),请您先充值或者选择其他支付方式");
				tv_item1.setText("");
				tv_item2.setText("微信充值");
				tv_item5.setText("支付宝充值");
				re_item1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
				re_item2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						rechargefromWx(hbjg);
					}
				});
				re_item5.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						rechargefromZhFb(hbjg);
					}
				});
				re_item3.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				return;
			}else {

				LayoutInflater inflaterDl = LayoutInflater.from(MomentsPublishActivity.this);
				final RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_zhifu_pass, null);
				final Dialog dialog2 = new AlertDialog.Builder(MomentsPublishActivity.this,R.style.Dialog).create();
				dialog2.show();
				dialog2.getWindow().setContentView(layout);
				dialog2.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
				dialog2.getWindow().setGravity(Gravity.BOTTOM);
				dialog2.getWindow().getDecorView().setPadding(0, 0, 0, 0);
				WindowManager.LayoutParams lp = dialog2.getWindow().getAttributes();
				lp.width = WindowManager.LayoutParams.MATCH_PARENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				dialog2.getWindow().setAttributes(lp);
				final PasswordView pwdView = (PasswordView)layout.findViewById(R.id.pwd_view);
				pwdView.setOnFinishInput(new OnPasswordInputFinish() {
					@Override
					public void inputFinish() {

						UserPermissionUtil.getUserPermission(MomentsPublishActivity.this, DemoHelper.getInstance().getCurrentUsernName(), "4", new UserPermissionUtil.UserPermissionListener() {
							@Override
							public void onAllow() {
								if (pwdView.getStrPassword().equals(pass)) {
									dialog2.dismiss();
									String orderType = "";
									if (cb_moshiyi.isChecked()){
										orderType = "01";
									}
									if (cb_moshier.isChecked()){
										orderType = "02";
									}
									if (cb_moshisan.isChecked()){
										orderType = "03";
									}
									if (cb_moshisi.isChecked()){
										orderType = "04";
									}
									if (cb_moshiwu.isChecked()){
										orderType = "05";
									}
									WeakReference<MomentsPublishActivity> reference = new WeakReference<MomentsPublishActivity>(MomentsPublishActivity.this);
									dialog = CustomProgressDialog.createDialog(reference.get());
									dialog.setCanceledOnTouchOutside(true);
									dialog.setMessage("正在发布...");
									dialog.show();
									List<Param> params=new ArrayList<>();
									params.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
									params.add(new Param("content",content));
									if (title.equals("shenghuo")){
										params.add(new Param("dType","01"));
										params.add(new Param("authType","01"));
									}else if (title.equals("xinwen")){
										params.add(new Param("dType","02"));
										params.add(new Param("location",mylat+"|"+mylon));
										params.add(new Param("price",location));
										params.add(new Param("authType","01"));
										params.add(new Param("demandType",demandType));

									}else if (title.equals("shangye")){

										params.add(new Param("price",""+new DecimalFormat("0.00").format(Double.parseDouble(shpjg))));
										params.add(new Param("salePrice",""+new DecimalFormat("0.00").format(Double.parseDouble(shpxj))));
										params.add(new Param("dType","03"));
										params.add(new Param("orderType",orderType));
										params.add(new Param("authType","01"));
										params.add(new Param("lat",mylat));
										params.add(new Param("log",mylon));
										params.add(new Param("task_locaName",et_location2.getText().toString().trim()));

										String link = et_linkUrl.getText().toString().trim();
										if (link != null && link.length()>8){

											params.add(new Param("task_jurisdiction",link));

										}

									}else if (title.equals("youhui")){
										params.add(new Param("price",shpjg));
										params.add(new Param("salePrice",shpxj));
										params.add(new Param("dType","04"));
										params.add(new Param("orderType",orderType));
									}else if (title.equals("xuqiu")){
										params.add(new Param("price",""+new DecimalFormat("0.00").format(Double.parseDouble(shpjg))));
										params.add(new Param("dType","05"));
										params.add(new Param("orderType",orderType));
										params.add(new Param("authType","01"));
									}

//									if (switchAllpeople.isSwitchOpen()){
//										params.add(new Param("authType","01"));
//									}else if (switchOnlyFriend.isSwitchOpen()){
//										params.add(new Param("authType","02"));
//									}else if (switchOnlyMyself.isSwitchOpen()){
//										params.add(new Param("authType","03"));
//									}
									if (title.equals("shangye") && isSetRed.equals("yes")){

										Double a = Double.valueOf(hbjg)*100;
										Double b = Double.valueOf(zhfjg);
										final Double c = a/b;
										params.add(new Param("sum",""+new DecimalFormat("0.00").format(Double.parseDouble(hbjg))));

										params.add(new Param("oncePrice",""+new DecimalFormat("0.00").format(c/100)));
									}
									if (filePath==null) {
										OkHttpManager.getInstance().postMoment(params, imagePaths1, FXConstant.URL_PUBLISH, new OkHttpManager.HttpCallBack() {
											@Override
											public void onResponse(JSONObject jsonObject) {
												Log.e("param,obj", jsonObject.toJSONString());
												String code = jsonObject.getString("code");
												if (code.equals("SUCCESS")) {
													Toast.makeText(getApplicationContext(), "发布成功...", Toast.LENGTH_SHORT).show();
													String timel1 = getNowTime();
													SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
													SharedPreferences.Editor editor = mSharedPreferences.edit();
													if (title.equals("shenghuo")) {
														editor.putString("time2", timel1);
													} else if (title.equals("xinwen")) {
														editor.putString("time3", timel1);
														editor.putString("time5", timel1);
													} else if (title.equals("shangye")) {
														editor.putString("time4", timel1);
														editor.putString("time6", timel1);
													}

													editor.commit();
													if (switch_fenxiang.isSwitchOpen()) {
														if (dialog != null && dialog.isShowing()) {
															dialog.dismiss();
														}
														fenxiang(content);
													} else {
														if (dialog != null && dialog.isShowing()) {
															dialog.dismiss();
														}
													}
													updateBmob();


												} else {
													if (dialog != null && dialog.isShowing()) {
														dialog.dismiss();
													}
													Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
												}
											}

											@Override
											public void onFailure(String errorMsg) {
												if (dialog != null && dialog.isShowing()) {
													dialog.dismiss();
												}
												Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
											}
										});
									}else {
										sendVideo(content,params);
									}
								} else {
									int times;
									if (errorTime>0){
										times = errorTime-1;
									}else {
										times = 0;
									}
									reduceShRZFCount(times+"");
									if (times==0) {
										ZhifuHelpUtils.showErrorLing(MomentsPublishActivity.this);
									}else {
										ZhifuHelpUtils.showErrorTishi(MomentsPublishActivity.this,times + "",null,"000");
									}
									if (dialog != null){

										dialog.dismiss();
									}

									return;

								}
							}

							@Override
							public void onBan() {
								ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送动态");

							}
						});


					}
				});
				/**
				 *  可以用自定义控件中暴露出来的cancelImageView方法，重新提供相应
				 *  如果写了，会覆盖我们在自定义控件中提供的响应
				 *  可以看到这里toast显示 "Biu Biu Biu"而不是"Cancel"*/
				pwdView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog2.dismiss();
						return;
					}
				});
				pwdView.getForgetTextView().setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String bs;
						bs = "000";
						startActivity(new Intent(MomentsPublishActivity.this, WJPaActivity.class).putExtra("biaoshi",bs));
						return;
					}
				});
			}
		}else {


			UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "4", new UserPermissionUtil.UserPermissionListener() {
				@Override
				public void onAllow() {
					String orderType = "";
					if (cb_moshiyi.isChecked()){
						orderType = "01";
					}
					if (cb_moshier.isChecked()){
						orderType = "02";
					}
					if (cb_moshisan.isChecked()){
						orderType = "03";
					}
					if (cb_moshisi.isChecked()){
						orderType = "04";
					}
					if (cb_moshiwu.isChecked()){
						orderType = "05";
					}
					WeakReference<MomentsPublishActivity> reference = new WeakReference<MomentsPublishActivity>(MomentsPublishActivity.this);
					dialog = CustomProgressDialog.createDialog(reference.get());
					dialog.setCanceledOnTouchOutside(false);
					dialog.setMessage("正在发布...");
					dialog.show();
					final List<Param> params=new ArrayList<>();
					params.add(new Param("userId", DemoHelper.getInstance().getCurrentUsernName()));
					params.add(new Param("content",content));
					if (title.equals("shenghuo")){
						params.add(new Param("dType","01"));
						params.add(new Param("authType","01"));
					}else if (title.equals("xinwen")){
						params.add(new Param("dType","02"));
						params.add(new Param("location",mylat+"|"+mylon));
						params.add(new Param("price",location));
						params.add(new Param("authType","01"));
						params.add(new Param("demandType",demandType));
					}else if (title.equals("shangye")){
						params.add(new Param("price",shpjg));
						params.add(new Param("salePrice",""+new DecimalFormat("0.00").format(Double.parseDouble(shpxj))));
						params.add(new Param("dType","03"));
						params.add(new Param("orderType",orderType));
						params.add(new Param("authType","01"));
						params.add(new Param("lat",mylat));
						params.add(new Param("log",mylon));
						params.add(new Param("task_locaName",et_location2.getText().toString().trim()));

						String link = et_linkUrl.getText().toString().trim();
						if (link != null && link.length()>8){

							params.add(new Param("task_jurisdiction",link));

						}

					}else if (title.equals("youhui")){
						params.add(new Param("price",shpjg));
						params.add(new Param("salePrice",shpxj));
						params.add(new Param("dType","04"));
						params.add(new Param("orderType",orderType));
					}else if (title.equals("xuqiu")){
						params.add(new Param("price",shpjg));
						params.add(new Param("dType","05"));
						params.add(new Param("orderType",orderType));
					}


					if ("shangye".equals(title) && isSetRed.equals("yes")){
						params.add(new Param("sum",hbjg));
						params.add(new Param("oncePrice",zhfjg));
					}
					if ("xinwen".equals(title)){
						final List<File> files = new ArrayList<File>();
						File file1 = new File(Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang","dituCut.png");
						if (file1.exists()) {
							files.add(file1);
						}
						List<File> files2=new ArrayList<>();
						if (switch_baozang.isSwitchOpen()) {
							String gameRed = et_baocang_shul.getText().toString().trim();
							String gameAll= et_baocang_jine.getText().toString().trim();
							String gameGeShu = String.valueOf((int)(Double.parseDouble(gameAll)/Double.parseDouble(gameRed)));
							files2 = new ArrayList<File>();
							File file2 = new File(baoZangIma);
							if (file2.exists()) {
								files2.add(file2);
							}
							DecimalFormat df = new DecimalFormat("#####0.00");
							double gamePrice = Double.parseDouble(gameRed);
							final String str = df.format(gamePrice);//返回的是String类型的数据
							params.add(new Param("redSum",gameGeShu));
							params.add(new Param("gameRed",str));
							params.add(new Param("redTime","no"));
						}
						final List<File> files1 = new ArrayList<File>();
						if (imagePaths1.size()>0){
							File file = null;
							file = new File(imagePaths1.get(0));
							if (file.exists()) {
								files1.add(file);
							}
						}
						if (imagePaths1.size()>1){
							File file = null;
							file = new File(imagePaths1.get(1));
							if (file.exists()) {
								files1.add(file);
							}
						}
						if (imagePaths1.size()>2){
							File file = null;
							file = new File(imagePaths1.get(2));
							if (file.exists()) {
								files1.add(file);
							}
						}
						if (imagePaths1.size()>3){
							File file = null;
							file = new File(imagePaths1.get(3));
							if (file.exists()) {
								files1.add(file);
							}
						}
						if (imagePaths1.size()>4){
							File file = null;
							file = new File(imagePaths1.get(4));
							if (file.exists()) {
								files1.add(file);
							}
						}
						if (imagePaths1.size()>5){
							File file = null;
							file = new File(imagePaths1.get(5));
							if (file.exists()) {
								files1.add(file);
							}
						}
						if (imagePaths1.size()>6){
							File file = null;
							file = new File(imagePaths1.get(6));
							if (file.exists()) {
								files1.add(file);
							}
						}
						if (imagePaths1.size()>7){
							File file = null;
							file = new File(imagePaths1.get(7));
							if (file.exists()) {
								files1.add(file);
							}
						}
						if (filePath==null) {
							String str1 = "locaImage";
							String str = "redImage";
							OkHttpManager.getInstance().postTfile(params, str, files2, str1, files, "image", files1, FXConstant.URL_PUBLISH, new OkHttpManager.HttpCallBack() {
								@Override
								public void onResponse(JSONObject jsonObject) {
									Log.e("param,obj", jsonObject.toJSONString());
									String code = jsonObject.getString("code");
									if (code.equals("SUCCESS")) {
										Toast.makeText(getApplicationContext(), "发布成功...", Toast.LENGTH_SHORT).show();
										String timel1 = getNowTime();
										SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
										SharedPreferences.Editor editor = mSharedPreferences.edit();
										if (title.equals("shenghuo")) {
											editor.putString("time2", timel1);
										} else if (title.equals("xinwen")) {
											editor.putString("time3", timel1);
											editor.putString("time5", timel1);
										} else if (title.equals("shangye")) {
											editor.putString("time4", timel1);
											editor.putString("time6", timel1);
										}
										editor.commit();
										FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/catch/"));
										if (switch_fenxiang.isSwitchOpen()) {
											if (dialog != null && dialog.isShowing()) {
												dialog.dismiss();
											}
											fenxiang(content);
										} else {
											if (dialog != null && dialog.isShowing()) {
												dialog.dismiss();
											}
										}
										updateBmob();
									} else {
										if (dialog != null && dialog.isShowing()) {
											dialog.dismiss();
										}
										Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
									}
								}

								@Override
								public void onFailure(String errorMsg) {
									if (dialog != null && dialog.isShowing()) {
										dialog.dismiss();
									}
									Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
								}
							});
						}else {
							sendVideo(content,params);
						}
					}else {
						if (filePath==null) {

							OkHttpManager.getInstance().postMoment(params, imagePaths1, FXConstant.URL_PUBLISH, new OkHttpManager.HttpCallBack() {
								@Override
								public void onResponse(JSONObject jsonObject) {
									Log.e("param,obj", jsonObject.toJSONString());
									Log.d("chen", "onResponse" + jsonObject.toString());
									String code = jsonObject.getString("code");
									if (code.equals("SUCCESS")) {
										Toast.makeText(getApplicationContext(), "发布成功...", Toast.LENGTH_SHORT).show();
										String timel1 = getNowTime();
										SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
										SharedPreferences.Editor editor = mSharedPreferences.edit();
										if (title.equals("shenghuo")) {
											editor.putString("time2", timel1);
										} else if (title.equals("xinwen")) {
											editor.putString("time3", timel1);
											editor.putString("time5", timel1);
										} else if (title.equals("shangye")) {
											editor.putString("time4", timel1);
											editor.putString("time6", timel1);
										}
										editor.commit();
										SharedPreferences mSharedPreference = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
										SharedPreferences.Editor meditor = mSharedPreference.edit();
										meditor.putString("sendTime", getNowTime());
										meditor.commit();
										if (switch_fenxiang.isSwitchOpen()) {
											if (dialog != null && dialog.isShowing()) {
												dialog.dismiss();
											}
											fenxiang(content);
										} else {
											if (dialog != null && dialog.isShowing()) {
												dialog.dismiss();
											}
										}
										updateBmob();
									} else {
										if (dialog != null && dialog.isShowing()) {
											dialog.dismiss();
										}
										Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
									}
								}

								@Override
								public void onFailure(String errorMsg) {
									if (dialog != null && dialog.isShowing()) {
										dialog.dismiss();
									}
									Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
								}
							});

						}else {
							sendVideo(content,params);
						}
					}
				}

				@Override
				public void onBan() {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
					ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送动态");

				}
			});

		}
	}

	private void sendVideo(final String content, final List<Param> params) {


		UserPermissionUtil.getUserPermission(this, DemoHelper.getInstance().getCurrentUsernName(), "4", new UserPermissionUtil.UserPermissionListener() {
			@Override
			public void onAllow() {
				if ("xinwen".equals(title)){
					OkHttpManager.getInstance().posts2(params, "videoPictures", imagePaths1,null, new ArrayList<String>(), "video", filePath
							,"locaImage", Environment.getExternalStorageDirectory()+"/zhengshier/fenxiang/dituCut.png", "redImage", baoZangIma, null, null, null, null, null, null, null, null, null, null, FXConstant.URL_PUBLISH, new OkHttpManager.HttpCallBack() {
								@Override
								public void onResponse(JSONObject jsonObject) {
									Log.e("param,obj", jsonObject.toJSONString());

									String code = jsonObject.getString("code");
									if (code.equals("SUCCESS")) {
										Toast.makeText(getApplicationContext(), "发布成功...", Toast.LENGTH_SHORT).show();
										String timel1 = getNowTime();
										SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
										SharedPreferences.Editor editor = mSharedPreferences.edit();
										if (title.equals("shenghuo")) {
											editor.putString("time2", timel1);
										} else if (title.equals("xinwen")) {
											editor.putString("time3", timel1);
											editor.putString("time5", timel1);
										} else if (title.equals("shangye")) {
											editor.putString("time4", timel1);
											editor.putString("time6", timel1);
										}
										editor.commit();
										SharedPreferences mSharedPreference = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
										SharedPreferences.Editor meditor = mSharedPreference.edit();
										meditor.putString("sendTime", getNowTime());
										meditor.commit();
										FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/catch/"));
										FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/shipin/"));
										FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/DCIM/mabeijianxi/"));
										if (switch_fenxiang.isSwitchOpen()) {
											if (dialog != null && dialog.isShowing()) {
												dialog.dismiss();
											}
											fenxiang(content);
										} else {
											if (dialog != null && dialog.isShowing()) {
												dialog.dismiss();
											}
										}
										updateBmob();
										startActivity(new Intent(MomentsPublishActivity.this, PaidanListActivity.class).putExtra("type","1"));
									} else {
										if (dialog != null && dialog.isShowing()) {
											dialog.dismiss();
										}
										Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
									}
									if (dialog4 != null && dialog4.isShowing()) {

										dialog4.dismiss();
									}
								}

								@Override
								public void onFailure(String errorMsg) {
									if (dialog != null && dialog.isShowing()) {
										dialog.dismiss();
									}
									if (dialog4 != null && dialog4.isShowing()) {

										dialog4.dismiss();
									}
									Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
								}
							});
				}else {
					OkHttpManager.getInstance().posts2(params, "videoPictures", imagePaths1,null, new ArrayList<String>(), "video", filePath
							, null, null, null, null, null, null, null, null, null, null, null, null, null, null, FXConstant.URL_PUBLISH, new OkHttpManager.HttpCallBack() {
								@Override
								public void onResponse(JSONObject jsonObject) {
									Log.e("param,obj", jsonObject.toJSONString());
									Log.d("chen", "onResponse" + jsonObject.toString());
									String code = jsonObject.getString("code");
									if (code.equals("SUCCESS")) {
										Toast.makeText(getApplicationContext(), "发布成功...", Toast.LENGTH_SHORT).show();
										String timel1 = getNowTime();
										SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dynaClick", Context.MODE_PRIVATE);
										SharedPreferences.Editor editor = mSharedPreferences.edit();
										if (title.equals("shenghuo")) {
											editor.putString("time2", timel1);
										} else if (title.equals("xinwen")) {
											editor.putString("time3", timel1);
											editor.putString("time5", timel1);
										} else if (title.equals("shangye")) {
											editor.putString("time4", timel1);
											editor.putString("time6", timel1);
										}

										editor.commit();
										SharedPreferences mSharedPreference = getSharedPreferences("sangu_dynaSend", Context.MODE_PRIVATE);
										SharedPreferences.Editor meditor = mSharedPreference.edit();
										meditor.putString("sendTime", getNowTime());
										meditor.commit();
										FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/catch/"));
										FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/zhengshier/shipin/"));
										FileUtils.deleteAllFiles(new File(Environment.getExternalStorageDirectory() + "/DCIM/mabeijianxi/"));
										if (switch_fenxiang!=null&&switch_fenxiang.isSwitchOpen()) {
											if (dialog != null && dialog.isShowing()) {
												dialog.dismiss();
											}
											fenxiang(content);
										} else {
											if (dialog != null && dialog.isShowing()) {
												dialog.dismiss();
											}
										}
										updateBmob();
									} else {
										if (dialog != null && dialog.isShowing()) {
											dialog.dismiss();
										}
										Toast.makeText(getApplicationContext(), "网络繁忙，请稍后再试", Toast.LENGTH_SHORT).show();
									}
									if (dialog4 != null && dialog4.isShowing()) {

										dialog4.dismiss();
									}
								}

								@Override
								public void onFailure(String errorMsg) {
									if (dialog != null && dialog.isShowing()) {
										dialog.dismiss();
									}
									if (dialog4 != null && dialog4.isShowing()) {

										dialog4.dismiss();
									}
									Toast.makeText(getApplicationContext(), "网络连接错误", Toast.LENGTH_SHORT).show();
								}
							});
				}
			}

			@Override
			public void onBan() {
				if (dialog4 != null && dialog4.isShowing()) {

					dialog4.dismiss();
				}
				ToastUtils.showNOrmalToast(getApplicationContext(), "您的账户已被禁止发送动态");

			}
		});


	}

	private String getHostIP() {
		String hostIp = null;
		try {
			Enumeration nis = NetworkInterface.getNetworkInterfaces();
			InetAddress ia = null;
			while (nis.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) nis.nextElement();
				Enumeration<InetAddress> ias = ni.getInetAddresses();
				while (ias.hasMoreElements()) {
					ia = ias.nextElement();
					if (ia instanceof Inet6Address) {
						continue;// skip ipv6
					}
					String ip = ia.getHostAddress();
					if (!"127.0.0.1".equals(ip)) {
						hostIp = ia.getHostAddress();
						break;
					}
				}
			}
		} catch (SocketException e) {
			Log.i("yao", "SocketException");
			e.printStackTrace();
		}
		return hostIp;
	}
	private void rechargefromWx(String balance) {
		balance = (int)(Double.parseDouble(balance)*100)+"";
		Toast.makeText(MomentsPublishActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
		String wxUrl = FXConstant.URL_ZHIFUWX_DIAOQI;
		final String finalBalance = balance;
		StringRequest request = new StringRequest(Request.Method.POST, wxUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				SharedPreferences mSharedPreferences = getSharedPreferences("sangu_chongzhi", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = mSharedPreferences.edit();
				editor.putString("activity","MomentsPublishActivity");
				editor.commit();
				JSONObject object = JSONObject.parseObject(s);
				Log.e("chongzhiac,s", s);
				String appid="",mch_id="",nonce_str="",sign="",prepayId="",timestamp="";
				appid = object.getString("appid");
				nonce_str = object.getString("noncestr");
				mch_id = object.getString("partnerid");
				prepayId = object.getString("prepayid");
				timestamp = object.getString("timestamp");
				sign = object.getString("sign");
				PayReq req = new PayReq();
				req.appId = appid;
				req.partnerId = mch_id;
				req.prepayId = prepayId;
				req.packageValue = "Sign=WXPay";
				req.nonceStr = nonce_str;
				//这是得到一个时间戳(除以1000转化成秒数)
				req.timeStamp = timestamp;
				//调用获得签名的方法,这里直接把服务器返回来的sign给覆盖了,所以我不是很明白服务器为什么返回这个sign值,然后调起支付,基本上就可以了(我的反正是可以了....)
//                sign = OrderInfoUtil2_0.createSign(parameters);
				Log.e("TAG", "timestamp=====" + req.timeStamp);
				req.sign = sign;
				// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
				api.sendReq(req);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Toast.makeText(MomentsPublishActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
				Log.e("ChongZhiActivity",volleyError.getMessage());
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String ,String> param = new HashMap<>();
				param.put("body","正事多-钱包充值");
				param.put("detail","正事多-钱包充值");
				param.put("out_trade_no",getNowTime());
				param.put("total_fee", finalBalance);
				param.put("spbill_create_ip",getHostIP());
				param.put("attach",DemoHelper.getInstance().getCurrentUsernName());
				return param;
			}
		};
		MySingleton.getInstance(MomentsPublishActivity.this).addToRequestQueue(request);
	}

	private void rechargefromZhFb(final String balance){
		String chongzhiId=null;
		chongzhiId = DemoHelper.getInstance().getCurrentUsernName();
		try {
			chongzhiId = URLEncoder.encode(chongzhiId,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (!balance.equals("")) {
			String url = FXConstant.URL_ZhiFu;
			Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap2(Constant.APPID_WX, balance,chongzhiId,null,null,null,"正事多-钱包充值");
			final String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
			final String orderinfo = OrderInfoUtil2_0.getSign(params);
			StringRequest request = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
				@Override
				public void onResponse(String s) {
					try {
						JSONObject object = JSONObject.parseObject(s);
						String sign = object.getString("sign");
						sign = URLEncoder.encode(sign, "UTF-8");
						final String orderInfo = orderParam + "&" + "sign=" + sign;
						Runnable payRunnable = new Runnable() {
							@Override
							public void run() {
								PayTask alipay = new PayTask(MomentsPublishActivity.this);
								Map<String, String> result = alipay.payV2(orderInfo, true);
								Message msg = new Message();
								msg.what = SDK_PAY_FLAG;
								msg.obj = result;
								mHandler.sendMessage(msg);
							}
						};
						Thread payThread = new Thread(payRunnable);
						payThread.start();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError volleyError) {
					Toast.makeText(MomentsPublishActivity.this,"网络错误，请重试！",Toast.LENGTH_SHORT).show();
				}
			}){
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String,String> param = new HashMap<>();
					param.put("orderInfo",orderinfo);
					return param;
				}
			};
			MySingleton.getInstance(MomentsPublishActivity.this).addToRequestQueue(request);
		}else {
			Toast.makeText(MomentsPublishActivity.this, "充值金额不能为空！", Toast.LENGTH_SHORT).show();
		}
	}

	private void updateBmob() {
		String url = FXConstant.URL_UPDATE_DYNATIME;
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				setResult(RESULT_OK);
				finish();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				setResult(RESULT_OK);
				finish();
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> param = new HashMap<>();
				if (title.equals("shenghuo")){
					param.put("type2",getNowTime());
				}else if (title.equals("xinwen")){
					param.put("type3",getNowTime());
				}else if (title.equals("shangye")){
					param.put("type4",getNowTime());
				}else if (title.equals("xuqiu")){

					String newType = getIntent().getStringExtra("newType");
					if (newType != null && newType.equals("1")){

						param.put("type1",getNowTime());
					}
				}

				if (title.equals("xinwen") && switch_baozang.isSwitchOpen()) {
					param.put("type5",getNowTime());
				}
				if (title.equals("shangye") && isSetRed.equals("yes")) {
					param.put("type6",getNowTime());
				}

				return param;
			}
		};
		MySingleton.getInstance(MomentsPublishActivity.this).addToRequestQueue(request);
	}
	private String getNowTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return dateFormat.format(date);
	}
	private void fenxiang(String content) {
		OnekeyShare oks = new OnekeyShare();
		oks.setTheme(OnekeyShareTheme.CLASSIC);
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode(true);
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		oks.setImagePath(imagePaths1.get(0));
		oks.setText(content);
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			@Override
			public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
				if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
					paramsToShare.setImagePath(imagePaths1.get(0));
				} else if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
					paramsToShare.setShareType(Platform.SHARE_IMAGE);
					paramsToShare.setTitle("正事儿app");
					paramsToShare.setImagePath(imagePaths1.get(0));
				} else if (platform.getName().equalsIgnoreCase(QZone.NAME)) {
					paramsToShare.setImagePath(imagePaths1.get(0));
				}else if (platform.getName().equalsIgnoreCase(WechatMoments.NAME)){
					paramsToShare.setShareType(Platform.SHARE_IMAGE);
					paramsToShare.setTitle("正事儿app");
					paramsToShare.setImagePath(imagePaths1.get(0));
				}
			}
		});
		oks.show(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (title.equals("shangye")||title.equals("xuqiu")&&presenter!=null){
			presenter.updatePriceData(DemoHelper.getInstance().getCurrentUsernName());
		}
		SharedPreferences sp = getSharedPreferences("sangu_chongzhi_zhuangtai", Context.MODE_PRIVATE);
		String zhuangtai = sp.getString("zhuangtai","失败");
		if ("成功".equals(zhuangtai)){
			Toast.makeText(MomentsPublishActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
			if (sp!=null) {
				SharedPreferences.Editor editor = sp.edit();
				if (editor!=null) {
					editor.clear();
					editor.commit();
				}
			}
		}
		if (mVideoView!=null&&filePath!=null){
			if (mPositionWhenPaused >= 0) {
				mVideoView.seekTo(mPositionWhenPaused);
				mPositionWhenPaused = -1;
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mVideoView!=null&&filePath!=null){
			mPositionWhenPaused = mVideoView.getCurrentPosition();
			mVideoView.stopPlayback();
		}
	}

	@SuppressLint("SdCardPath")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case FunctionConfig.CAMERA_RESULT:
					if (data != null) {
						List<LocalMedia> medias = (List<LocalMedia>) data.getSerializableExtra(FunctionConfig.EXTRA_RESULT);
						if (medias != null) {
							if (isBaoZang) {
								baoZangIma = medias.get(0).getCompressPath();
								hasTupian = true;
								iv_bz_tupian.setImageURI(Uri.fromFile(new File(baoZangIma)));
								iv_del.setVisibility(View.VISIBLE);
							} else {
								selectMedia.add(medias.get(0));
								adapter.setList(selectMedia);
								adapter.notifyDataSetChanged();
								imagePaths1.add(selectMedia.get(0).getCompressPath());
							}
						}
					}
					break;
				case 4:
					mylat = data.getDoubleExtra("latitude",0)+"";
					mylon = data.getDoubleExtra("longitude",0)+"";
					city = data.getStringExtra("city");
					district = data.getStringExtra("district");
					street = data.getStringExtra("street");
					if (city!=null&&!"".equals(city)&&!city.equalsIgnoreCase("null")&&!"(null)".equals(city)){
						location = city;
					}
					if (district!=null&&!"".equals(district)&&!district.equalsIgnoreCase("null")&&!"(null)".equals(district)){
						location = location+district;
					}
					if (street!=null&&!"".equals(street)&&!street.equalsIgnoreCase("null")&&!"(null)".equals(street)){
						location = location+street;
					}
					if (city==null&&district==null&&street==null){
						Toast.makeText(MomentsPublishActivity.this, "获取位置失败，请重新获取", Toast.LENGTH_SHORT).show();
					}
					et_location.setText(location);
					if (location == null || location.equals("")) {
						Toast.makeText(MomentsPublishActivity.this, "获取位置失败，请重新获取", Toast.LENGTH_SHORT).show();
					}
					break;
				case 5:
					mylat = data.getStringExtra("latitude");
					mylon = data.getStringExtra("longitude");
					street = data.getStringExtra("street");
					Log.e("momentac,mylat",mylat+"");
					Log.e("momentac,mylon",mylon+"");
					Log.e("momentac,dizhi",street+"");
					if (street!=null&&!"".equals(street)&&!street.equalsIgnoreCase("null")&&!"(null)".equals(street)){
						street = subString(street);
						Log.e("momentac,location",street+"");

						if (title.equals("shangye")){
							et_location2.setText(street);
						}else {
							et_didian.setText(street);
						}


					} else{
						Toast.makeText(MomentsPublishActivity.this, "获取位置失败，请重新获取", Toast.LENGTH_SHORT).show();

						if (title.equals("shangye")){
							et_location2.setText(null);
						}else {
							et_didian.setText(null);
						}

					}
					break;
				case 11:
					filePath = data.getStringExtra("videoPath");
					String imagePath = data.getStringExtra("imagePath");
					imagePaths1.add(imagePath);
					fl_video.setVisibility(View.VISIBLE);
					mVideoView.setVisibility(View.VISIBLE);
					recyclerView.setVisibility(View.GONE);
					Uri uri = Uri.parse(filePath);
					mVideoView.setVideoURI(uri);
					mVideoView.start();
					mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mp) {
							//设置MediaPlayer的OnSeekComplete监听
							mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
								@Override
								public void onSeekComplete(MediaPlayer mp) {
									mp.start();
								}
							});
						}
					});
					mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							mp.start();
						}
					});
					break;
			}
		}
		if (resultCode == RESULT_FIRST_USER){
			if (data != null){
				String str = TextUtils.isEmpty(data.getStringExtra("quanxian"))?"":data.getStringExtra("quanxian");
				String str1 = data.getStringExtra("fenxiang");
				if (str1.equals("01")){
					switch_fenxiang.openSwitch();
				}else if (str1.equals("00")){
					switch_fenxiang.closeSwitch();
				}
				if (str.equals("01")){
					switchAllpeople.openSwitch();
					switchOnlyFriend.closeSwitch();
					switchOnlyMyself.closeSwitch();
				}else if (str.equals("02")){
					switchAllpeople.closeSwitch();
					switchOnlyFriend.openSwitch();
					switchOnlyMyself.closeSwitch();
				}else if (str.equals("03")){
					switchAllpeople.closeSwitch();
					switchOnlyFriend.closeSwitch();
					switchOnlyMyself.openSwitch();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	private String subString(String task_locaName) {
		String str = task_locaName;
		if (str.contains("区")||str.contains("县")){
			int i2;
			if (str.contains("区")) {
				i2 = task_locaName.indexOf("区");
			} else {
				i2 = task_locaName.indexOf("县");
			}
			if (str.contains("市")){
				if (str.contains("省")) {
					int i1 = task_locaName.indexOf("省");
					str = task_locaName.substring(i1 + 1, i2 + 1);
				}else {
					str = task_locaName.substring(0, i2 + 1);
				}
			}else {
				str = task_locaName.substring(0, i2 + 1);
			}
		}else {
			if (str.contains("市")){
				if (str.contains("省")) {
					int i1 = task_locaName.indexOf("省");
					int i2 = task_locaName.indexOf("市");
					str = task_locaName.substring(i1 + 1,i2 + 1);
				}else {
					int i2 = task_locaName.indexOf("市");
					str = task_locaName.substring(0, i2 + 1);
				}
			}else {
				if (str.contains("省")) {
					int i1 = task_locaName.indexOf("省");
					str = task_locaName.substring(i1+1,str.length());
				}else {
					str= task_locaName;
				}
			}
		}
		return str;
	}
	@Override
	public void updateCurrentPrice(Object success) {
		price = DemoApplication.getInstance().getCurrenPrice();
		com.alibaba.fastjson.JSONObject object = JSON.parseObject(String.valueOf(success));
		errorTime = object.getInteger("enterErrorTimes");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
		if (imagePaths1!=null){
			imagePaths1.clear();
			imagePaths1=null;
		}
		if (selectMedia!=null){
			selectMedia.clear();
			selectMedia=null;
		}
		if (instence!=null){
			instence=null;
		}
		if (mVideoView!=null) {
			mVideoView = null;
		}
	}
}