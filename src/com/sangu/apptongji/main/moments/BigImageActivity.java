package com.sangu.apptongji.main.moments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanxin.easeui.utils.FileStorage;
import com.sangu.apptongji.R;
import com.sangu.apptongji.main.FXConstant;
import com.sangu.apptongji.main.utils.ToastUtils;
import com.sangu.apptongji.ui.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class BigImageActivity extends BaseActivity {
    //  private JSONArray json = null;
    private String biaoshi,imageURL;
    private ImageCycleView mAdView;
    private ImageView sharedElement;
    private TextView tv_save;
    private String[] str = new String[]{"share1","share2","share3","share4","share5","share6","share7","share8"};
    private static final String STATE_CURRENT_PAGE_POSITION = "state_current_page_position";
    private static final String EXTRA_STARTING_ALBUM_POSITION = "extra_starting_item_position";
    private static final String EXTRA_CURRENT_ALBUM_POSITION = "extra_current_item_position";

//    private final SharedElementCallback mCallback = new SharedElementCallback() {
//        @Override
//        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
//            if (mIsReturning) {
//                if (sharedElement == null) {
//                    // If shared element is null, then it has been scrolled off screen and
//                    // no longer visible. In this case we cancel the shared element transition by
//                    // removing the shared element from the shared elements map.
//                    names.clear();
//                    sharedElements.clear();
//                } else if (mStartingPosition != mCurrentPosition) {
//                    // If the user has swiped to a different ViewPager page, then we need to
//                    // remove the old shared element and replace it with the new shared element
//                    // that should be transitioned instead.
//                    names.clear();
//                    names.add(sharedElement.getTransitionName());
//                    sharedElements.clear();
//                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
//                }
//            }
//        }
//    };
    private int mCurrentPosition;
    private int mStartingPosition;
    private boolean mIsReturning;
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_PAGE_POSITION, mCurrentPosition);
    }
//    @Override
//    public void finishAfterTransition() {
//        mIsReturning = true;
//        Intent data = new Intent();
//        data.putExtra(EXTRA_STARTING_ALBUM_POSITION, mStartingPosition);
//        data.putExtra(EXTRA_CURRENT_ALBUM_POSITION, mCurrentPosition);
//        setResult(RESULT_OK, data);
//        super.finishAfterTransition();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fx_activity_bigimage);
//        setEnterSharedElementCallback(mCallback);

        mStartingPosition = getIntent().getIntExtra(EXTRA_STARTING_ALBUM_POSITION, 0);
        if (savedInstanceState == null) {
            mCurrentPosition = mStartingPosition;
        } else {
            mCurrentPosition = savedInstanceState.getInt(STATE_CURRENT_PAGE_POSITION);
        }
        tv_save = (TextView) findViewById(R.id.tv_save);
        //  String jsonStr = getIntent().getStringExtra("jsonStr");
        final String[] images=getIntent().getStringArrayExtra("images");
        biaoshi = getIntent().getStringExtra("biaoshi");
        //   json = JSONArray.parseArray(jsonStr);
//        if (json == null) {
//            finish();
//            return;
//        }
        int page = getIntent().getIntExtra("page", 0);
        mAdView = (ImageCycleView) this.findViewById(R.id.ad_view);
        mAdView.setImageResources(images,page,biaoshi, mAdCycleViewListener);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = mAdView.getIndexs();
                String url;
                if (biaoshi.equals("11")){
                    url = FXConstant.URL_ZY_AVATAR;
                }else if (biaoshi.equals("14")){
                    url = FXConstant.URL_QIYE_TOUXIANG;
                }else if (biaoshi.equals("15")){
                    url = FXConstant.URL_QIYE_ZY;
                }else if (biaoshi.equals("13")){
                    url = FXConstant.URL_SOCIAL_PHOTO;
                }else if (biaoshi.equals("06")){
                    url = FXConstant.URL_SIGN_FOUR;
                }else {
                    url = FXConstant.URL_AVATAR;
                }
                imageURL = url + images[index];
                if (imageURL!=null) {
                    Log.e("IMAGECYCLE,big",imageURL);
                    LayoutInflater inflater1 = LayoutInflater.from(BigImageActivity.this);
                    RelativeLayout layout1 = (RelativeLayout) inflater1.inflate(R.layout.dialog_alert, null);
                    final Dialog dialog1 = new AlertDialog.Builder(BigImageActivity.this,R.style.Dialog).create();
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
                    title_tv1.setText("确定保存图片到内存卡吗？");
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
                            final ProgressDialog pd = new ProgressDialog(BigImageActivity.this);
                            pd.setMessage("正在保存...");
                            pd.setCanceledOnTouchOutside(false);
                            pd.show();
                            final String imageName = getNowTime() + ".png";
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    uploadImage2(imageURL, imageName,pd);
                                }
                            }).start();
                        }
                    });
                }
            }
        });
    }
    private String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }
    private void uploadImage2(final String imageUrl, String imageName,final ProgressDialog pd) {
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder().url(imageUrl).build();
            okhttp3.Response response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 2;
            Bitmap bm = BitmapFactory.decodeStream(is,null,opts);
            if (bm!=null) {
                saveToSD(bm,imageName,pd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToSD(Bitmap mBitmap, final String imageName,final ProgressDialog pd) {
        final File file = new FileStorage("savePath").createCropFile(imageName,null);
        final Uri imageUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(BigImageActivity.this, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
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
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            FileOutputStream fos = new FileOutputStream(f);
            int options = 100;
            // 如果大于200kb则再次压缩,最多压缩三次
            while (baos.toByteArray().length / 1024 > 200 && options > 10) {
                // 清空baos
                baos.reset();
                // 这里压缩options%，把压缩后的数据存放到baos中
                mBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 30;
            }
            fos.write(baos.toByteArray());
            fos.close();
            baos.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pd!=null&&pd.isShowing()){
                        pd.dismiss();
                    }
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(imageUri);
                    sendBroadcast(intent);
                    LayoutInflater inflaterDl = LayoutInflater.from(BigImageActivity.this);
                    RelativeLayout layout = (RelativeLayout) inflaterDl.inflate(R.layout.dialog_ok, null);
                    final Dialog dialog2 = new AlertDialog.Builder(BigImageActivity.this,R.style.Dialog).create();
                    if (isFinishing()) {
                        ToastUtils.showNOrmalToast(getApplicationContext(),"请重新打开页面！");
                        return;
                    }
                    dialog2.show();
                    dialog2.getWindow().setContentView(layout);
                    dialog2.setCanceledOnTouchOutside(false);
                    dialog2.setCancelable(false);
                    TextView tv_title = (TextView) dialog2.findViewById(R.id.title_tv);
                    Button btn_ok = (Button) dialog2.findViewById(R.id.btn_ok);
                    tv_title.setText("保存成功,保存地址:\n/sdcard/zhengshier/savePath/");
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onImageClick(int position, View imageView) {
//            mCurrentPosition = mAdView.getIndexs();
//            sharedElement = (ImageView) imageView;
//            sharedElement.setTransitionName(str[mCurrentPosition]);
//            finishAfterTransition();
            finish();
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
//            Glide.with(BigImageActivity.this).load(imageURL).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
//            ImageLoader.getInstance().displayImage(imageURL,imageView, DemoApplication.mOptions);
        }
    };

    @Override
    public void back(View view) {
        super.back(view);
        setResult(RESULT_OK);
    }
}
