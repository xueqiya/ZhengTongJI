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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.utils.OpenClientUtil;
import com.fanxin.easeui.model.EaseImageCache;
import com.fanxin.easeui.utils.EaseLoadLocalBigImgTask;
import com.fanxin.easeui.utils.FileStorage;
import com.fanxin.easeui.widget.photoview.EasePhotoView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * download and show original image
 *
 */
public class EaseShowBigImageActivity extends EaseBaseActivity {
	private static final String TAG = "ShowBigImage";
	private String imageName;
	private ProgressDialog pd;
	private EasePhotoView image;
	private int default_res = R.drawable.ease_default_image;
	private String localFilePath;
	private Bitmap bitmap;
	private boolean isDownloaded;
	private ProgressBar loadLocalPb;
	private TextView tv_save;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.ease_activity_show_big_image);
		super.onCreate(savedInstanceState);
		tv_save = (TextView) findViewById(R.id.tv_save);
		image = (EasePhotoView) findViewById(R.id.image);
		loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
		default_res = getIntent().getIntExtra("default_image", R.drawable.ease_default_avatar);
		Uri uri = getIntent().getParcelableExtra("uri");
		localFilePath = getIntent().getExtras().getString("localUrl");
		String msgId = getIntent().getExtras().getString("messageId");
		EMLog.d(TAG, "show big msgId:" + msgId );
		//show the image if it exist in local path
		if (uri != null && new File(uri.getPath()).exists()) {
			EMLog.d(TAG, "showbigimage file exists. directly show it");
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			// int screenWidth = metrics.widthPixels;
			// int screenHeight =metrics.heightPixels;
			bitmap = EaseImageCache.getInstance().get(uri.getPath());
			if (bitmap == null) {
				EaseLoadLocalBigImgTask task = new EaseLoadLocalBigImgTask(this, uri.getPath(), image, loadLocalPb, ImageUtils.SCALE_IMAGE_WIDTH,
						ImageUtils.SCALE_IMAGE_HEIGHT);
				if (android.os.Build.VERSION.SDK_INT > 10) {
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					task.execute();
				}
			} else {
				image.setImageBitmap(bitmap);
			}
		} else if(msgId != null) {
			downloadImage(msgId);
		}else {
			image.setImageResource(default_res);
		}
		imageName = getNowTime() + ".png";
//		image.setOnLongClickListener(new View.OnLongClickListener() {
//			@Override
//			public boolean onLongClick(View v) {
//
//				return true;
//			}
//		});
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveImg(bitmap);
			}
		});
	}
	private String getNowTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
		return dateFormat.format(date);
	}
	private void saveImg(Bitmap mBitmap)  {
		File file = new FileStorage("baocun").createCropFile(imageName,null);
		Uri imageUri;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			imageUri = FileProvider.getUriForFile(EaseShowBigImageActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
			grantUriPermission(getPackageName(), imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
		} else {
			imageUri = Uri.fromFile(file);
		}
		File f = new File(file.getPath());
		if (bitmap==null||f.exists()){
			Toast.makeText(getApplicationContext(),"该图片已保存到手机！",Toast.LENGTH_SHORT).show();
			return;
		}
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
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("保存成功,保存路径: "+f.getPath());
			builder.setTitle("提示");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * download image
	 *
	 * @param
	 */
	@SuppressLint("NewApi")
	private void downloadImage(final String msgId) {
		EMLog.e(TAG, "download with messageId: " + msgId);
		String str1 = getResources().getString(R.string.Download_the_pictures);
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage(str1);
		pd.show();
		File temp = new File(localFilePath);
		final String tempPath = temp.getParent() + "/temp_" + temp.getName();
		final EMCallBack callback = new EMCallBack() {
			public void onSuccess() {
				EMLog.e(TAG, "onSuccess" );
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						new File(tempPath).renameTo(new File(localFilePath));

						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(metrics);
						int screenWidth = metrics.widthPixels;
						int screenHeight = metrics.heightPixels;

						bitmap = ImageUtils.decodeScaleImage(localFilePath, screenWidth, screenHeight);
						if (bitmap == null) {
							image.setImageResource(default_res);
						} else {
							image.setImageBitmap(bitmap);
							EaseImageCache.getInstance().put(localFilePath, bitmap);
							isDownloaded = true;
						}
						if (EaseShowBigImageActivity.this.isFinishing() || EaseShowBigImageActivity.this.isDestroyed()) {
							return;
						}
						if (pd != null) {
							pd.dismiss();
						}
					}
				});
			}

			public void onError(int error, String msg) {
				EMLog.e(TAG, "offline file transfer error:" + msg);
				File file = new File(tempPath);
				if (file.exists()&&file.isFile()) {
					file.delete();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (EaseShowBigImageActivity.this.isFinishing() || EaseShowBigImageActivity.this.isDestroyed()) {
							return;
						}
						image.setImageResource(default_res);
						pd.dismiss();
					}
				});
			}

			public void onProgress(final int progress, String status) {
				EMLog.d(TAG, "Progress: " + progress);
				final String str2 = getResources().getString(R.string.Download_the_pictures_new);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (EaseShowBigImageActivity.this.isFinishing() || EaseShowBigImageActivity.this.isDestroyed()) {
							return;
						}
						pd.setMessage(str2 + progress + "%");
					}
				});
			}
		};

		EMMessage msg = EMClient.getInstance().chatManager().getMessage(msgId);
		msg.setMessageStatusCallback(callback);

		EMLog.e(TAG, "downloadAttachement");
		EMClient.getInstance().chatManager().downloadAttachment(msg);
	}

	@Override
	public void onBackPressed() {
		if (isDownloaded)
			setResult(RESULT_OK);
		finish();
	}
}
