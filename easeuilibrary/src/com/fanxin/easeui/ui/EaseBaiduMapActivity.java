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
package com.fanxin.easeui.ui;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.fanxin.easeui.utils.FileStorage;
import com.hyphenate.easeui.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import static android.support.v4.content.FileProvider.getUriForFile;

public class EaseBaiduMapActivity extends EaseBaseActivity implements OnGetGeoCoderResultListener {
	private MapView mMapView = null;
	LocationClient mLocClient=null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private String biaoshi,dizhi;
	Button sendButton = null;
	Button btnXq2=null;
	Button btnDh2 = null;
	private Uri imageUri;//原图保存地址
	private GeoCoder mSearch = null;
	private LinearLayout ll_search;
	private EditText et_search;
	private TextView tv1;
	private View vi2;
	private int w,h;
	private double nowLat,nowLng;
	// LocationData locData = null;
	private BDLocation lastLocation = null;
	private String location;
	ProgressDialog progressDialog;
	private BaiduMap mBaiduMap;
	private LocationMode mCurrentMode;
	private boolean isFirst = true;

	public class BaiduSDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			String st1 = getResources().getString(R.string.Network_error);
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				String st2 = getResources().getString(R.string.please_check);
				Toast.makeText(getApplicationContext(), st2, Toast.LENGTH_SHORT).show();
			} else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				Toast.makeText(getApplicationContext(), st1, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private BaiduSDKReceiver mBaiduReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//initialize SDK with context, should call this before setContentView
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.ease_activity_baidumap);
		vi2 = LayoutInflater.from(EaseBaiduMapActivity.this).inflate(R.layout.item_find_frag,null);
		ll_search = (LinearLayout) findViewById(R.id.ll_search);
		et_search = (EditText) findViewById(R.id.et_search);
		tv1 = (TextView) findViewById(R.id.tv1);
		mMapView = (MapView) findViewById(R.id.bmapView);
		sendButton = (Button) findViewById(R.id.btn_location_send);
		btnDh2 = (Button) vi2.findViewById(R.id.btn_daohang);
		btnXq2 = (Button) vi2.findViewById(R.id.btn_xiangqing);
		Intent intent = getIntent();
		biaoshi = intent.getStringExtra("biaoshi");
		double latitude = intent.getDoubleExtra("latitude", 0);
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
		if ("01".equals(biaoshi)){
			WeakReference<EaseBaiduMapActivity> reference = new WeakReference<EaseBaiduMapActivity>(EaseBaiduMapActivity.this);
			mSearch = GeoCoder.newInstance();
			mSearch.setOnGetGeoCodeResultListener(reference.get());
			SharedPreferences sp = getSharedPreferences("sangu_dingwei_city", Context.MODE_PRIVATE);
			final String city = sp.getString("city","郑州市");
			ll_search.setVisibility(View.VISIBLE);
			et_search.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dizhi = null;
					startActivityForResult(new Intent(EaseBaiduMapActivity.this,SuggestActivity.class).putExtra("city",city),0);
				}
			});
		}else {
			ll_search.setVisibility(View.GONE);
		}
		initMapView();
		if (latitude == 0) {
			mMapView = new MapView(this, new BaiduMapOptions());
			mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
					mCurrentMode, true, null));
			showMapWithLocationClient();
		} else {
			double longtitude = intent.getDoubleExtra("longitude", 0);
			String address = intent.getStringExtra("address");
			LatLng p = new LatLng(latitude, longtitude);
			mMapView = new MapView(this,
					new BaiduMapOptions().mapStatus(new MapStatus.Builder()
							.target(p).build()));
			showMap(latitude, longtitude, address);
		}
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mBaiduReceiver = new BaiduSDKReceiver();
		registerReceiver(mBaiduReceiver, iFilter);
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getApplicationContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		OverlayOptions ooA = new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_150))
				.zIndex(4).draggable(true);
		mBaiduMap.addOverlay(ooA);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(result.getLocation(), 17.0f);
		mBaiduMap.animateMapStatus(u);
		double [] d = bd09togcj02(result.getLocation().longitude,result.getLocation().latitude);
		final String strlat = String.format("%.6f", d[1]);
		final String strlng = String.format("%.6f", d[0]);
		nowLat = Double.parseDouble(strlat);
		nowLng = Double.parseDouble(strlng);
	}

	private double[] bd09togcj02(double bd_lon, double bd_lat) {
		double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
		double x = bd_lon - 0.0065;
		double y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gg_lng = z * Math.cos(theta);
		double gg_lat = z * Math.sin(theta);
		return new double[] { gg_lng, gg_lat };
	}
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(getApplicationContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		mBaiduMap.clear();
		String city = result.getAddressDetail().city;
		String district = result.getAddressDetail().district;
		String street = result.getAddressDetail().street;
		location = city+district+street;
		Log.e("easebaiac",location);
		InfoWindow mInfoWindow = new InfoWindow(vi2, result.getLocation(),0);
		mBaiduMap.showInfoWindow(mInfoWindow);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
	}

	private void showMap(double latitude, double longtitude, String address) {
		sendButton.setVisibility(View.GONE);
		LatLng llA = new LatLng(latitude, longtitude);
		CoordinateConverter converter= new CoordinateConverter();
		converter.coord(llA);
		converter.from(CoordinateConverter.CoordType.COMMON);
		LatLng convertLatLng = converter.convert();
		OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory
				.fromResource(R.drawable.ease_icon_marka))
				.zIndex(4).draggable(true);
		mBaiduMap.addOverlay(ooA);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
		mBaiduMap.animateMapStatus(u);
	}

	private void showMapWithLocationClient() {
		String str1 = getResources().getString(R.string.Making_sure_your_location);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage(str1);
		progressDialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface arg0) {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				Log.d("map", "cancel retrieve location");
				finish();
			}
		});
		progressDialog.show();
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		UiSettings uiSettings = mBaiduMap.getUiSettings();
		if (!"01".equals(biaoshi)) {
			uiSettings.setScrollGesturesEnabled(false);
		}
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// open gps
		// option.setCoorType("bd09ll"); 
		// Johnson change to use gcj02 coordination. chinese national standard
		// so need to conver to bd09 everytime when draw on baidu map
		option.setCoorType("gcj02");
		option.setScanSpan(30000);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case 0:
				if (data != null) {
					String city = data.getStringExtra("city");
					String addr = data.getStringExtra("key");
					dizhi = data.getStringExtra("dizhi");
					if (city!=null) {
						Log.e("easebaiduac,city",city);
					}
					if (addr!=null) {
						Log.e("easebaiduac,city",addr);
					}
					if (data.getStringExtra("dizhi")!=null) {
						Log.e("easebaiduac,city",data.getStringExtra("dizhi"));
					}
					if (dizhi!=null) {
						Log.e("easebaiduac,city",dizhi);
					}
					et_search.setText(dizhi);
					mSearch.geocode(new GeoCodeOption().city(city).address(addr));
				}
				break;
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mLocClient != null) {
			mLocClient.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mLocClient != null) {
			mLocClient.start();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mLocClient != null)
			mLocClient.stop();
		mMapView.onDestroy();
		unregisterReceiver(mBaiduReceiver);
		super.onDestroy();
	}
	private void initMapView() {
		mMapView.setLongClickable(true);
		if ("01".equals(biaoshi)){
			mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
				@Override
				public void onMapLongClick(LatLng latLng) {
					double lat = latLng.latitude;
					double lng = latLng.longitude;
					String strLat = String.format("%.6f", lat);
					String strLng = String.format("%.6f", lng);
					nowLat = Double.parseDouble(strLat);
					nowLng = Double.parseDouble(strLng);
					mSearch.reverseGeoCode(new ReverseGeoCodeOption()
							.location(latLng));
				}
			});
			mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
				@Override
				public void onMapClick(LatLng latLng) {
					mBaiduMap.hideInfoWindow();
				}
				@Override
				public boolean onMapPoiClick(MapPoi mapPoi) {
					return false;
				}
			});
		}
		btnDh2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (location==null||"".equals(location)){
					Toast.makeText(getApplicationContext(),"位置选择错误，请重新选择",Toast.LENGTH_SHORT).show();
					return;
				}
				LayoutInflater inflater1 = LayoutInflater.from(EaseBaiduMapActivity.this);
				RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
				final Dialog dialog1 = new AlertDialog.Builder(EaseBaiduMapActivity.this,R.style.Dialog).create();
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
				title_tv1.setText("您选择的位置是："+location);
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
						Intent intent = new Intent();
						intent.putExtra("latitude", nowLat);
						intent.putExtra("longitude", nowLng);
						intent.putExtra("street", location);
						setResult(RESULT_OK, intent);
						finish();
					}
				});
			}
		});
	}

	/**
	 * format new location to string and show on screen
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			Log.d("map", "On location change received:" + location);
			Log.d("map", "addr:" + location.getAddrStr());
			sendButton.setEnabled(true);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

			if (lastLocation != null) {
				if (lastLocation.getLatitude() == location.getLatitude() && lastLocation.getLongitude() == location.getLongitude()) {
					Log.d("map", "same location, skip refresh");
					// mMapView.refresh(); //need this refresh?
					return;
				}
			}
			lastLocation = location;
			mBaiduMap.clear();
			LatLng llA = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
			CoordinateConverter converter= new CoordinateConverter();
			converter.coord(llA);
			converter.from(CoordinateConverter.CoordType.COMMON);
			LatLng convertLatLng = converter.convert();
			OverlayOptions ooA = new MarkerOptions().position(convertLatLng).icon(BitmapDescriptorFactory
					.fromResource(R.drawable.icon_geo))
					.zIndex(4).draggable(true);
			mBaiduMap.addOverlay(ooA);
			if (isFirst) {
				dizhi = lastLocation.getCity() + lastLocation.getDistrict() + lastLocation.getStreet();
				nowLat = lastLocation.getLatitude();
				nowLng = lastLocation.getLongitude();
				if ("00".equals(biaoshi)){
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 10.0f);
					mBaiduMap.animateMapStatus(u);
				}else {
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
					mBaiduMap.animateMapStatus(u);
				}
				isFirst = false;
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
		}
	}

	public void back(View v) {
		finish();
	}

	public void sendLocation(View view) {
		if ("00".equals(biaoshi)) {
			if (w==0||h==0){
				Toast.makeText(getApplicationContext(),"位置获取中，请稍后重试！",Toast.LENGTH_SHORT).show();
				return;
			}
			cutImage();
		}else if ("01".equals(biaoshi)) {
			if (dizhi==null||"".equals(dizhi)||nowLat<=0){
				Toast.makeText(getApplicationContext(),"位置获取失败，请重试！",Toast.LENGTH_SHORT).show();
				return;
			}
			Log.e("easebaiduac,dizhi",dizhi);
			Intent intent = this.getIntent();
			intent.putExtra("latitude", nowLat);
			intent.putExtra("longitude", nowLng);
			intent.putExtra("street", dizhi);
			this.setResult(RESULT_OK, intent);
			finish();
		}else {
			Intent intent = this.getIntent();
			intent.putExtra("latitude", lastLocation.getLatitude());
			intent.putExtra("longitude", lastLocation.getLongitude());
			intent.putExtra("city", lastLocation.getCity());
			intent.putExtra("district", lastLocation.getDistrict());
			intent.putExtra("street", lastLocation.getStreet());
			intent.putExtra("address", lastLocation.getCity() + lastLocation.getDistrict() + lastLocation.getStreet());
			this.setResult(RESULT_OK, intent);
			finish();
		}
		overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}

	private int dip2px(Context context, float dipValue) {
		Resources r = context.getResources();
		return (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
	}

	private void cutImage() {
		WeakReference<EaseBaiduMapActivity> reference = new WeakReference<EaseBaiduMapActivity>(EaseBaiduMapActivity.this);
		int x = getDeviceWidth();
		int y = getDeviceHeight();
		int screenshot_n = h-dip2px(reference.get(),110);
		int screenshot_width = x;
		saveBitmap(screenshot_n,screenshot_width,h+dip2px(reference.get(),-30));
	}

	/**
	 * 保存位图
	 *
	 * @param
	 */
	private void saveImg(Bitmap mBitmap)  {
		File file = new FileStorage("fenxiang").createCropFile("dituCut.png",null);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			imageUri = FileProvider.getUriForFile(EaseBaiduMapActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
			grantUriPermission(getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
		} else {
			imageUri = Uri.fromFile(file);
		}
		File f = new File(file.getPath());
		try {
			//如果文件不存在，则创建文件
			if(!f.exists()){
				f.createNewFile();
			}
			//输出流
			FileOutputStream out = new FileOutputStream(f);
			/** mBitmap.compress 压缩图片
			 *
			 *  Bitmap.CompressFormat.PNG   图片的格式
			 *   100  图片的质量（0-100）
			 *   out  文件输出流
			 */
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			Intent intent = EaseBaiduMapActivity.this.getIntent();
			intent.putExtra("latitude", lastLocation.getLatitude());
			intent.putExtra("longitude", lastLocation.getLongitude());
			intent.putExtra("city", lastLocation.getCity());
			intent.putExtra("district", lastLocation.getDistrict());
			intent.putExtra("street", lastLocation.getStreet());
			intent.putExtra("address", lastLocation.getCity() + lastLocation.getDistrict() + lastLocation.getStreet());
			EaseBaiduMapActivity.this.setResult(RESULT_OK, intent);
			finish();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存位图
	 *
	 * @param
	 */
	private void saveBitmap(int y, int screenshot_width, int screenshot_height) {
		Rect rect = new Rect(0,y,screenshot_width,screenshot_height);
//		Rect rect = new Rect(0, 0, 300, 300);// 左xy 右xy
		mBaiduMap.snapshotScope(rect, new BaiduMap.SnapshotReadyCallback() {
			@Override
			public void onSnapshotReady(Bitmap snapshot) {
				saveImg(snapshot);
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus){
			int[] location1 = new  int[2] ;
			tv1.getLocationInWindow(location1);
			w = location1[0];
			h = location1[1];
			Log.e("easebaidu,win1,w",location1[0]+"");
			Log.e("easebaidu,win1,h",location1[1]+"");
		}
	}

	/**
	 * 获取设备屏幕的宽
	 * @param
	 * @return
	 */
	private int getDeviceWidth(){
		Display display = getWindowManager().getDefaultDisplay();
		Point p = new Point();
		display.getSize(p);
		return p.x;
	}

	/**获取屏幕的高*/
	private int getDeviceHeight(){
		Display display = getWindowManager().getDefaultDisplay();
		Point p = new Point();
		display.getSize(p);
		return p.y;
	}

}
