package com.sangu.apptongji;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.GcmRegister;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.baidu.mapapi.SDKInitializer;
import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sangu.apptongji.main.alluser.entity.Userful;
import com.sangu.apptongji.main.db.TopUser;
import com.sangu.apptongji.main.db.TopUserDao;
import com.sangu.apptongji.main.mapsearch.LocationService;
import com.sangu.apptongji.main.utils.LocalUserUtil;
import com.sangu.apptongji.main.utils.OkHttpManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class DemoApplication extends Application {

	public static Context applicationContext;
	public static DisplayImageOptions mOptions;
	public static DisplayImageOptions mOptions2;
	public static DisplayImageOptions mOptions3;
	private static DemoApplication instance;
	// login user name
//	public static String currentUserNick;
	public LocationService locationService;
	private JSONObject userJson;
	private Userful user;
	private Map<String, TopUser> topUsers;
	private double yUE=0,qiyeYue=0;
	private String chzjine,txjine;
	private String qiyechzjine,qiyetxjine,qiyepaypassword,freezetype;
	private String lng,lat,locationState,score,withdrawals;
	private String paypassword,qiyeId,companyName,comAddress,remark,resv6;
	private List<Activity> activities=new ArrayList<>();
	private DisplayMetrics displayMetrics = null;

	public static DemoApplication getApp() {
		if (instance != null && instance instanceof DemoApplication) {
			return (DemoApplication) instance;
		} else {
			instance = new DemoApplication();
			instance.onCreate();
			return (DemoApplication) instance;
		}
	}
//	{
//		PlatformConfig.setWeixin("b95f416cbe068b7b2a055d784812c1a7","wx6dad66ed22a784f9");
//		PlatformConfig.setSinaWeibo("","");
//		PlatformConfig.setQQZone("1105742744","9zQGz0iMVYq4gsbm");
//	}

	public static boolean sRunningOnIceCreamSandwich;

	static {
		sRunningOnIceCreamSandwich = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}
	@Override
	public void onCreate() {

		super.onCreate();

		MultiDex.install(this);
        applicationContext = this;
        instance = this;
		// 初始化BmobSDK
//		Bmob.initialize(this, Constant.APPID);
//		// 使用推送服务时的初始化操作
		// 启动推送服务
//		BmobPush.startWork(instance);
		MobSDK.init(instance,"19ae5e0a56880","d15b1e7457e03757b2467a48b2fb6192");
		OkHttpManager.init(instance);
		LocalUserUtil.init(instance);
		initSmallVideo();
        DemoHelper.getInstance().init(applicationContext);
		SDKInitializer.initialize(this);
		initImageLoader(this);
		initCloudChannel(this);
		locationService = new LocationService(getApplicationContext());


	}



	/**
	 * 初始化云推送通道
	 * @param applicationContext
	 */
	private void initCloudChannel(final Context applicationContext) {
		PushServiceFactory.init(applicationContext);
		final CloudPushService pushService = PushServiceFactory.getCloudPushService();
		pushService.register(applicationContext, new CommonCallback() {
			@Override
			public void onSuccess(String response) {
				Log.e("demoapplication", "init cloudchannel success");
			}
			@Override
			public void onFailed(String errorCode, String errorMessage) {
				Log.e("demoapplication", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
			}
		});

		MiPushRegister.register(applicationContext, "2882303761517520397", "5331752052397"); // 初始化小米辅助推送
		HuaWeiRegister.register(applicationContext); // 接入华为辅助推送
		GcmRegister.register(applicationContext, "1042232533704", "AIzaSyDhH9OVgtVzn4xv1ia8JxnGG5PhssEI1wY"); // 接入FCM/GCM初始化推送
	}

	private void initSmallVideo() {
		// 设置拍摄视频缓存路径
		File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (DeviceUtils.isZte()) {
			if (dcim.exists()) {
				JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
			} else {
				JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/","/sdcard-ext/")+"/mabeijianxi/");
			}
		} else {
			JianXiCamera.setVideoCachePath(dcim + "/mabeijianxi/");
		}
		// 初始化拍摄
		JianXiCamera.initialize(false,null);
	}

	/**
	 * 初始化ImageLoader
	 */
	private void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(200 * 1024 * 1024) // 200 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				//.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

		mOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.rp_avatar)   //加载过程中
				.showImageForEmptyUri(R.drawable.rp_avatar) //uri为空时
				.showImageOnFail(R.drawable.rp_avatar)      //加载失败时
				.cacheOnDisk(true)
				.cacheInMemory(true)                             //允许cache在内存和磁盘中
				.bitmapConfig(Bitmap.Config.RGB_565)             //图片压缩质量参数
				.build();
		mOptions2 = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_error)   //加载过程中
				.showImageForEmptyUri(R.drawable.default_error) //uri为空时
				.showImageOnFail(R.drawable.default_error)      //加载失败时
				.cacheOnDisk(true)
				.cacheInMemory(true)                             //允许cache在内存和磁盘中
				.bitmapConfig(Bitmap.Config.RGB_565)             //图片压缩质量参数
				.build();

		mOptions3 = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_error)   //加载过程中
				.showImageForEmptyUri(R.drawable.default_error) //uri为空时
				.showImageOnFail(R.drawable.default_error)      //加载失败时
				.cacheOnDisk(true)
				.cacheInMemory(true)                             //允许cache在内存和磁盘中
				.bitmapConfig(Bitmap.Config.RGB_565)             //图片压缩质量参数
				.displayer(new RoundedBitmapDisplayer(90))
				.build();
	}
	public void saveCurrentLng(String lng){
			this.lng = lng;
	}
	public String getCurrentLng(){
		return this.lng;
	}
	public void saveCurrentLat(String lat){
		this.lat = lat;
	}
	public void saveCurrentlocationState(String locationState){
		this.locationState = locationState;
	}
	public String getCurrentLat(){
		return this.lat;
	}
	public String getCurrentlocationState(){
		return this.locationState;
	}
	public void saveCurrentPrice(Double price){
		this.yUE=price;
	}
	public void saveCurrentQiyePrice(Double price){
		this.qiyeYue=price;
	}
	public void saveCurrentChzYuE(String price1){
		this.chzjine=price1;
	}
	public String getCurrentChzYuE(){
		return this.chzjine;
	}
	public void saveCurrenttxYuE(String price2){
		this.txjine=price2;
	}
	public String getCurrenttxYuE(){
		return this.txjine;
	}
	public void saveCurrentQiyeChzYuE(String price1){
		this.qiyechzjine=price1;
	}
	public String getCurrentQiyeChzYuE(){
		if (qiyechzjine == null) {
			return "0.00";
		}
		return this.qiyechzjine;
	}
	public void saveCurrentQiyetxYuE(String price2){
		this.qiyetxjine=price2;
	}
	public String getCurrentQiyetxYuE(){
		if (qiyetxjine == null) {
			return "0.00";
		}
		return this.qiyetxjine;
	}
	public void saveCurrentScore(String score){
		this.score=score;
	}
	public String getCurrentScore(){
		if (score == null) {
			return "";
		}
		return this.score;
	}
	public void saveCurrentWithdrawals(String withdrawals){
		this.withdrawals=withdrawals;
	}
	public String getCurrentWithdrawals(){
		if (withdrawals == null) {
			return "";
		}
		return this.withdrawals;
	}
	public double getCurrenPrice(){
		if (yUE == 0) {
			return 0.00;
		}
		return this.yUE;
	}
	public double getCurrenQiyePrice(){
		if (qiyeYue == 0) {
			return 0.00;
		}
		return this.qiyeYue;
	}
	public void saveCurrentPayPass(String pass){
		this.paypassword=pass;
	}
	public String getCurrentPayPass(){
		if (paypassword==null){
			return "";
		}
		return this.paypassword;
	}
	public void saveCurrentQiyePayPass(String pass){
		this.qiyepaypassword=pass;
	}
	public String getCurrentQiyePayPass(){
		if (qiyepaypassword==null){
			return "";
		}
		return this.qiyepaypassword;
	}
	public  String  getFreezeCurrentType() {
		if (freezetype==null){
			return "00";
		}
		return this.freezetype;
	}
	public void saveCurrentUser(Userful user){
		this.user = user;
	}
	public void setCurrentQiYeId(String qiyeid) {
		this.qiyeId = qiyeid;
	}
	public void setCurrentResv6(String resv6) {
		this.resv6 = resv6;
	}
	public String getCurrentResv6() {
		if (resv6 == null) {
			return "";
		}
		return resv6;
	}
	public String getCurrentQiYeId() {
		if (qiyeId == null) {
			return "";
		}
		return qiyeId;
	}
	public void clear(){
		if (lat!=null){
			this.lat=null;
		}
		if (locationState!=null){
			this.locationState=null;
		}
		if (lng!=null){
			this.lng=null;
		}
		if (qiyeId != null) {
			this.qiyeId=null;
		}
		if (user != null) {
			this.user=null;
		}
		if (userJson != null) {
			this.userJson=null;
		}
		if (remark != null) {
			this.remark=null;
		}
		if (yUE != 0) {
			this.yUE=0;
		}
		if (chzjine != null) {
			this.chzjine=null;
		}
		if (txjine != null) {
			this.txjine=null;
		}
		if (paypassword != null) {
			this.paypassword=null;
		}
		if (companyName != null) {
			this.companyName=null;
		}
		if (comAddress != null) {
			this.comAddress=null;
		}
		if (qiyeYue != 0){
			this.qiyeYue =0;
		}
		if (qiyechzjine!=null){
			qiyechzjine=null;
		}
		if (qiyetxjine!=null){
			qiyetxjine=null;
		}
		if (qiyepaypassword!=null){
			qiyepaypassword =null;
		}
	}
	public void setCurrentQiYeRemark(String remark) {
		this.remark = remark;
	}
	public String getCurrentQiYeRemark() {
		if (remark == null) {
			return "";
		}
		return remark;
	}
	public String getCurrentCompanyName() {
		if (companyName == null) {
			return "";
		}
		return companyName;
	}
	public void setCurrentCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCurrentComAddress() {
		if (comAddress == null) {
			return "";
		}
		return comAddress;
	}
	public void setCurrentcomAddress(String comAddress) {
		this.comAddress = comAddress;
	}
	public Userful getCurrentUser(){
		if (user==null)
			return new Userful();
		return this.user;
	}

	public static DemoApplication getInstance() {
		return instance;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	public  void setUserJson( JSONObject userJson){

		this.userJson=userJson;
		LocalUserUtil.getInstance().setUserJson(userJson);

	}
	public  JSONObject getUserJson(){
		if(userJson==null){
			userJson=LocalUserUtil.getInstance().getUserJson();
		}
		return  userJson;
	}
	/**
	 * 获取置顶列表
	 */
	public Map<String, TopUser> getTopUserList() {
		if(topUsers==null){
			TopUserDao dao = new TopUserDao(instance);
			topUsers=dao.getTopUserList();
		}
		return topUsers;
	}
	/*
	* 设置置顶列表
	* */
	public void saveTopUserList( Map<String, TopUser> topUsers) {
		this.topUsers=topUsers;
 		TopUserDao dao = new TopUserDao(instance);
		dao.saveTopUserList(new ArrayList<TopUser>(topUsers.values()));
	}


	public static int getRandomStreamId() {
		Random random = new Random();
		int randint =(int)Math.floor((random.nextDouble()*10000.0 + 10000.0));
		return randint;
	}

	public float getScreenDensity() {
		if (this.displayMetrics == null) {
			setDisplayMetrics(getResources().getDisplayMetrics());
		}
		return this.displayMetrics.density;
	}

	public int getScreenHeight() {
		if (this.displayMetrics == null) {
			setDisplayMetrics(getResources().getDisplayMetrics());
		}
		return this.displayMetrics.heightPixels;
	}

	public int getScreenWidth() {
		if (this.displayMetrics == null) {
			setDisplayMetrics(getResources().getDisplayMetrics());
		}
		return this.displayMetrics.widthPixels;
	}

	public void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
		this.displayMetrics = DisplayMetrics;
	}

	public int dp2px(float f)
	{
		return (int)(0.5F + f * getScreenDensity());
	}

	public int px2dp(float pxValue) {
		return (int) (pxValue / getScreenDensity() + 0.5f);
	}

	//获取应用的data/data/....File目录
	public String getFilesDirPath() {
		return getFilesDir().getAbsolutePath();
	}

	//获取应用的data/data/....Cache目录
	public String getCacheDirPath() {
		return getCacheDir().getAbsolutePath();
	}

	public void saveActivity(Activity activity){
		if(activity!=null){
			activities.add(activity);
		}
	}

	public void finishActivities(){
		for(Activity activity:activities){
			if(activity!=null&&!activity.isFinishing()){
				activity.finish();
			}
		}
	}


	public void saveCurrentFreezetype(String freezetype) {
		this.freezetype = freezetype;
	}
}
