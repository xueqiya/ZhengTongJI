package com.sangu.apptongji.main.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.hyphenate.chat.EMClient;
import com.permissionutil.PermissionListener;
import com.permissionutil.PermissionUtil;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.sangu.apptongji.DemoApplication;
import com.sangu.apptongji.DemoHelper;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.chatheadimage.StringUtils;
import com.sangu.apptongji.main.fragment.MainActivity;
import com.sangu.apptongji.main.fragment.MainTwoActivity;
import com.sangu.apptongji.main.service.ContactsService;
import com.sangu.apptongji.main.utils.MySingleton;
import com.sangu.apptongji.ui.BaseActivity;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 主程序入口开屏页
 *
 */
public class SplashActivity extends BaseActivity implements SplashADListener {
	private RelativeLayout rootLayout=null;
	LocationClient mLocClient=null;
	LocationClientOption option=null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private MyLocationConfiguration.LocationMode mCurrentMode=null;
	private static final int sleepTime = 1000;
	private static final String SKIP_TEXT = "点击跳过 %d";
	private SplashAD splashAD;
	private ViewGroup container;
	private TextView skipView;
	private ImageView splashHolder;

	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			String lng = "" + location.getLongitude();
			String lat = "" + location.getLatitude();

			String locations=null,location1 = null;
			String province = location.getProvince();
			String city = location.getCity();
			String district = location.getDistrict();
			String street = location.getStreet();
			DemoApplication.getInstance().saveCurrentLng(lng);
			DemoApplication.getInstance().saveCurrentLat(lat);
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());
			String date = sDateFormat.format(curDate);
			if (city!=null&&!"null".equals(city)){
				location1 = city;
			}
			if (district!=null&&!"null".equals(district)){
				location1 = location1 + district;
			}
			if (street!=null&&!"null".equals(street)){
				location1 = location1 + street;
			}
			SharedPreferences mSharedPreferences = getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putString("city",city);
			editor.putString("location",location1);
			editor.commit();
			if (province!=null&&!"null".equals(province)){
				locations = province;
			}
			if (city!=null&&!"null".equals(city)){
				locations = locations + city;
			}
			if (district!=null&&!"null".equals(district)){
				locations = locations + district;
			}
			if (street!=null&&!"null".equals(street)){
				locations = locations + street;
			}
			if (DemoHelper.getInstance().isLoggedIn(SplashActivity.this)) {
				updateCurrentaddr(locations,date,lat,lng,city);
				insertLoginLog(lat,lng,city);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}


	private void insertLoginLog(final String lat, final String lng, final String location) {
		if (DemoHelper.getInstance().getCurrentUsernName() == null || TextUtils.isEmpty(DemoHelper.getInstance().getCurrentUsernName())) {
			return;
		}
		RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		final String time = format.format(new Date(System.currentTimeMillis()));

		StringRequest request = new StringRequest(Request.Method.POST,FXConstant.URL_INSERT_LOGIN, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				Log.d("chen", "登陆插入log" + s);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {
				Log.d("chen", "登陆插入logonErrorResponse" + volleyError.getMessage());
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				//u_id loginTime（时间戳） deviceType  resv1（经度） resv2（纬度） uLocation地址
				Map<String,String> param = new HashMap<>();
				param.put("u_id",DemoHelper.getInstance().getCurrentUsernName());
				param.put("loginTime",time);
				param.put("deviceType","android4.5正事多");

				if (lng!=null) {
					param.put("resv1", lng);
				}
				if (lat!=null) {
					param.put("resv2", lat);
				}

				if ( location != null) {
					param.put("uLocation",location);
				}
				return param;
			}
		};
		MySingleton.getInstance(this).addToRequestQueue(request);
	}

	private void updateCurrentaddr(final String uLocation, final String date, final String lat, final String lng, final String city) {
		final String brand = android.os.Build.BRAND;
		final String model = android.os.Build.MODEL;
		String url = FXConstant.URL_UPDATE_TIME;
		StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String s) {
				Log.e("发送位置spl",lat+"\\"+lng+uLocation);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError volleyError) {

			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> params = new HashMap<String,String>();
				params.put("uLoginId", DemoHelper.getInstance().getCurrentUsernName());
				params.put("loginTime",date);
				if (uLocation!=null) {
					params.put("uLocation", uLocation);
				}
				params.put("deviceType","android4.5正事多"+brand+model);
				if (lng!=null) {
					params.put("lng", lng);
				}
				if (lat!=null) {
					params.put("lat", lat);
				}
				if (city!=null) {
					params.put("region", city);
				}
				return params;
			}
		};
		MySingleton.getInstance(SplashActivity.this).addToRequestQueue(request);
	}
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fx_activity_splash);
		WeakReference<SplashActivity> reference =  new WeakReference<SplashActivity>(this);
		RequestQueue queue = MySingleton.getInstance(getApplicationContext()).getRequestQueue();
		mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
		mLocClient = new LocationClient(reference.get());
		mLocClient.registerLocationListener(myListener);
		option = new LocationClientOption();
		option.setCoorType("gcj02");
		option.setScanSpan(5000);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
		rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
		//AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		//animation.setDuration(1500);
		//rootLayout.startAnimation(animation);
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());
		String date = sDateFormat.format(curDate);
		updateCurrentaddr(null,date,null,null,null);
		requestPermissionsAndLogin();

		if (DemoHelper.getInstance().isLoggedIn(SplashActivity.this)&&!StringUtils.isBlank(DemoHelper.getInstance().getCurrentUsernName())) {

			container = (ViewGroup) findViewById(R.id.splash_container);
			splashHolder = (ImageView) findViewById(R.id.splash_holder);

			fetchSplashAD(this, rootLayout, null, "1107067462", "6090950021628440", this, 0);

		}

	}

	private void requestPermissionsAndLogin() {
		new Thread(new Runnable() {
			public void run() {
				if (DemoHelper.getInstance().isLoggedIn(SplashActivity.this)&&!StringUtils.isBlank(DemoHelper.getInstance().getCurrentUsernName())) {
					// auto login mode, make sure all group and conversation is loaed before enter the main screen
//					RedPacket.getInstance().initRPToken(DemoHelper.getInstance().getCurrentUsernName(), DemoHelper.getInstance().getCurrentUsernName(), EMClient.getInstance().getChatConfig().getAccessToken(), new RPCallback() {
//						@Override
//						public void onSuccess() {
//
//						}
//
//						@Override
//						public void onError(String s, String s1) {
//
//						}
//					});
					long start = System.currentTimeMillis();
					EMClient.getInstance().groupManager().loadAllGroups();
					EMClient.getInstance().chatManager().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					//wait
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					startService(new Intent(SplashActivity.this, ContactsService.class));
					//enter main screen
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							PermissionUtil permissionUtil = new PermissionUtil(SplashActivity.this);
							permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},
									new PermissionListener() {
										@Override
										public void onGranted() {
											//所有权限都已经授权
//											Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//											if(getIntent().getBundleExtra("launchBundle") != null){
//												intent.putExtra("launchBundle",getIntent().getBundleExtra("launchBundle"));
//											}
//											startActivity(intent);
//											finish();
										}
										@Override
										public void onDenied(List<String> deniedPermission) {
											//Toast第一个被拒绝的权限
											Toast.makeText(getApplicationContext(),"拒绝权限软件将会运行异常,请手动打开权限！",Toast.LENGTH_LONG).show();
//											Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//											if(getIntent().getBundleExtra("launchBundle") != null){
//												intent.putExtra("launchBundle",getIntent().getBundleExtra("launchBundle"));
//											}
//											startActivity(intent);
//											finish();
										}
										@Override
										public void onShouldShowRationale(List<String> deniedPermission) {
											//Toast第一个勾选不在提示的权限
											Toast.makeText(getApplicationContext(),"拒绝权限软件将会运行异常,请手动打开权限！",Toast.LENGTH_LONG).show();
//											Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//											if(getIntent().getBundleExtra("launchBundle") != null){
//												intent.putExtra("launchBundle",getIntent().getBundleExtra("launchBundle"));
//											}
//											startActivity(intent);
//											finish();
										}
									});
						}
					});
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (!SplashActivity.this.isDestroyed()) {
								PermissionUtil permissionUtil = new PermissionUtil(SplashActivity.this);
								permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
										new PermissionListener() {
											@Override
											public void onGranted() {
												//所有权限都已经授权
												startActivity(new Intent(SplashActivity.this, MainTwoActivity.class));
												finish();
											}
											@Override
											public void onDenied(List<String> deniedPermission) {
												//Toast第一个被拒绝的权限
												Toast.makeText(getApplicationContext(),"拒绝权限软件将会运行异常！",Toast.LENGTH_LONG).show();
												startActivity(new Intent(SplashActivity.this, MainTwoActivity.class));
												finish();
											}
											@Override
											public void onShouldShowRationale(List<String> deniedPermission) {
												//Toast第一个勾选不在提示的权限
												Toast.makeText(getApplicationContext(),"拒绝权限软件将会运行异常,请手动打开权限！",Toast.LENGTH_LONG).show();
												startActivity(new Intent(SplashActivity.this, MainTwoActivity.class));
												finish();
											}
										});
							}
						}
					});
				}
			}
		}).start();
	}


	@Override
	protected void onStart() {
		super.onStart();
		/*new Thread(new Runnable() {
			public void run() {
				if (DemoHelper.getInstance().isLoggedIn(SplashActivity.this)&&!StringUtils.isBlank(DemoHelper.getInstance().getCurrentUsernName())) {
					// auto login mode, make sure all group and conversation is loaed before enter the main screen
//					RedPacket.getInstance().initRPToken(DemoHelper.getInstance().getCurrentUsernName(), DemoHelper.getInstance().getCurrentUsernName(), EMClient.getInstance().getChatConfig().getAccessToken(), new RPCallback() {
//						@Override
//						public void onSuccess() {
//
//						}
//
//						@Override
//						public void onError(String s, String s1) {
//
//						}
//					});
					long start = System.currentTimeMillis();
					EMClient.getInstance().groupManager().loadAllGroups();
					EMClient.getInstance().chatManager().loadAllConversations();
					long costTime = System.currentTimeMillis() - start;
					//wait
					if (sleepTime - costTime > 0) {
						try {
							Thread.sleep(sleepTime - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					startService(new Intent(SplashActivity.this, ContactsService.class));
					//enter main screen
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							PermissionUtil permissionUtil = new PermissionUtil(SplashActivity.this);
							permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
									new PermissionListener() {
										@Override
										public void onGranted() {
											//所有权限都已经授权
											Intent intent = new Intent(SplashActivity.this, MainActivity.class);
											if(getIntent().getBundleExtra("launchBundle") != null){
												intent.putExtra("launchBundle",getIntent().getBundleExtra("launchBundle"));
											}
											startActivity(intent);
											finish();
										}
										@Override
										public void onDenied(List<String> deniedPermission) {
											//Toast第一个被拒绝的权限
											Toast.makeText(getApplicationContext(),"拒绝权限软件将会运行异常,请手动打开权限！",Toast.LENGTH_LONG).show();
											Intent intent = new Intent(SplashActivity.this, MainActivity.class);
											if(getIntent().getBundleExtra("launchBundle") != null){
												intent.putExtra("launchBundle",getIntent().getBundleExtra("launchBundle"));
											}
											startActivity(intent);
											finish();
										}
										@Override
										public void onShouldShowRationale(List<String> deniedPermission) {
											//Toast第一个勾选不在提示的权限
											Toast.makeText(getApplicationContext(),"拒绝权限软件将会运行异常,请手动打开权限！",Toast.LENGTH_LONG).show();
											Intent intent = new Intent(SplashActivity.this, MainActivity.class);
											if(getIntent().getBundleExtra("launchBundle") != null){
												intent.putExtra("launchBundle",getIntent().getBundleExtra("launchBundle"));
											}
											startActivity(intent);
											finish();
										}
									});
						}
					});
				}else {
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							PermissionUtil permissionUtil = new PermissionUtil(SplashActivity.this);
							permissionUtil.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
									new PermissionListener() {
										@Override
										public void onGranted() {
											//所有权限都已经授权
											startActivity(new Intent(SplashActivity.this, MainTwoActivity.class));
											finish();
										}
										@Override
										public void onDenied(List<String> deniedPermission) {
											//Toast第一个被拒绝的权限
											Toast.makeText(getApplicationContext(),"拒绝权限软件将会运行异常！",Toast.LENGTH_LONG).show();
											startActivity(new Intent(SplashActivity.this, MainTwoActivity.class));
											finish();
										}
										@Override
										public void onShouldShowRationale(List<String> deniedPermission) {
											//Toast第一个勾选不在提示的权限
											Toast.makeText(getApplicationContext(),"拒绝权限软件将会运行异常,请手动打开权限！",Toast.LENGTH_LONG).show();
											startActivity(new Intent(SplashActivity.this, MainTwoActivity.class));
											finish();
										}
									});
						}
					});
				}
			}
		}).start();*/
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mLocClient!=null){
			mLocClient.stop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (myListener!=null){
			mLocClient.unRegisterLocationListener(myListener);
			myListener = null;
		}
		if (mLocClient != null) {
			mLocClient.stop();
			mLocClient = null;
		}
		if (option !=null ){
			option=null;
		}
	}


	/**
	 * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
	 *
	 * @param activity        展示广告的 activity
	 * @param adContainer     展示广告的大容器
	 * @param skipContainer   自定义的跳过按钮：传入该 view 给 SDK 后，SDK 会自动给它绑定点击跳过事件。SkipView 的样式可以由开发者自由定制，其尺寸限制请参考 activity_splash.xml 或下面的注意事项。
	 * @param appId           应用 ID
	 * @param posId           广告位 ID
	 * @param adListener      广告状态监听器
	 * @param fetchDelay      拉取广告的超时时长：即开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长）取值范围[3000, 5000]，设为0表示使用广点通 SDK 默认的超时时长。
	 */
	private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
							   String appId, String posId, SplashADListener adListener, int fetchDelay) {
		splashAD = new SplashAD(activity, adContainer, null, appId, posId, adListener, fetchDelay);
	}

	@Override
	public void onADPresent() {
		Log.i("AD_DEMO", "SplashADPresent11111111111111");
		//splashHolder.setVisibility(View.GONE); // 广告展示后一定要把预设的开屏图片隐藏起来
	}

	@Override
	public void onADDismissed() {
		Log.i("AD_DEMO", "SplashADDismissed222222222222");
		next();
	}

	private void next() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		if(getIntent().getBundleExtra("launchBundle") != null){
			intent.putExtra("launchBundle",getIntent().getBundleExtra("launchBundle"));
		}
		startActivity(intent);

		finish();
		//防止用户回退看到此页面
	}

	@Override
	public void onNoAD(AdError error) {
		Log.i("AD_DEMO", String.format("LoadSplashADFail, eCode=%d, errorMsg=%s", error.getErrorCode(), error.getErrorMsg()));
		/** 如果加载广告失败，则直接跳转 */
		// this.startActivity(new Intent(this, MainActivity.class));
		next();
	}

	@Override
	public void onADClicked() {
		Log.i("AD_DEMO", "SplashADClicked");
	}

	/**
	 * 倒计时回调，返回广告还将被展示的剩余时间。
	 * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
	 *
	 * @param millisUntilFinished 剩余毫秒数
	 */
	@Override
	public void onADTick(long millisUntilFinished) {
		Log.i("AD_DEMO", "SplashADTick " + millisUntilFinished + "ms");
	//	skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
	}

	@Override
	public void onADExposure() {
		Log.i("AD_DEMO", "SplashADExposure");
	}

	//防止用户返回键退出 APP
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_HOME) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
