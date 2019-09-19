package com.sangu.apptongji.main.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.gxz.videoedit.VideoEditActivity;
import com.luck.picture.lib.ui.PictureBaseActivity;
import com.mabeijianxi.smallvideorecord2.LocalMediaCompress;
import com.mabeijianxi.smallvideorecord2.model.AutoVBRMode;
import com.mabeijianxi.smallvideorecord2.model.BaseMediaBitrateConfig;
import com.mabeijianxi.smallvideorecord2.model.LocalMediaConfig;
import com.mabeijianxi.smallvideorecord2.model.OnlyCompressOverBean;
import com.sangu.apptongji.R;

/**
 * Created by Administrator on 2017-08-02.
 */

public class NomalVideoPlayActivity extends PictureBaseActivity implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnClickListener {
    private String video_path = "";
    private ImageView picture_left_back;
    private MediaController mMediaController;
    private VideoView mVideoView;
    private ImageView iv_play;
    private Button btn_commit;
    private RelativeLayout rl_bottom;
    private TextView tv_bianji;
    private int mPositionWhenPaused = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_normal);
        video_path = getIntent().getStringExtra("video_path");
        picture_left_back = (ImageView) findViewById(R.id.picture_left_back);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        tv_bianji = (TextView) findViewById(R.id.tv_bianji);
        mVideoView.setZOrderOnTop(true);// 解决播放视频透明问题
        iv_play = (ImageView) findViewById(R.id.iv_play);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        mMediaController = new MediaController(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setMediaController(mMediaController);
        picture_left_back.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        tv_bianji.setOnClickListener(this);
    }


    public void onStart() {
        // Play Video
        mVideoView.setVideoPath(video_path);
        mVideoView.start();
        super.onStart();
    }

    public void onPause() {
        // Stop video when the activity is pause.
        mPositionWhenPaused = mVideoView.getCurrentPosition();
        mVideoView.stopPlayback();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaController!=null) {
            mMediaController = null;
        }
        if (mVideoView!=null) {
            mVideoView = null;
        }
    }

    public void onResume() {
        // Resume video player
        if (mPositionWhenPaused >= 0) {
            mVideoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
        }
        super.onResume();
    }

    public boolean onError(MediaPlayer player, int arg1, int arg2) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.start();
        iv_play.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.picture_left_back:
                finish();
                break;
            case R.id.iv_play:
                mVideoView.start();
                iv_play.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_commit:
                final ProgressDialog mProgressDialog = new ProgressDialog(NomalVideoPlayActivity.this, -1);
                mProgressDialog.setMessage("正在处理视频,过程可能会比较长,请耐心等待...");
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
                mProgressDialog.setCancelable(false);
                mProgressDialog.setIndeterminate(false);// 设置进度条是否不明确
                BaseMediaBitrateConfig compressMode = null;
                compressMode = new AutoVBRMode(30);
                compressMode.setVelocity("ultrafast");
                LocalMediaConfig.Buidler buidler = new LocalMediaConfig.Buidler();
                final LocalMediaConfig config = buidler
                        .setVideoPath(video_path)
                        .captureThumbnailsTime(1)
                        .doH264Compress(compressMode)
                        .setFramerate(25)
                        .setScale(2.0f)
                        .build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mProgressDialog!=null&&!mProgressDialog.isShowing()) {
                                    mProgressDialog.show();
                                }
                            }
                        });
                        OnlyCompressOverBean onlyCompressOverBean = new LocalMediaCompress(config).startCompress();
                        final String imagePath = onlyCompressOverBean.getPicPath();
                        final String videoPath = onlyCompressOverBean.getVideoPath();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (mProgressDialog != null) {
                                        mProgressDialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (videoPath!=null) {
                                    setResult(RESULT_OK,new Intent().putExtra("videoPath",videoPath).putExtra("imagePath",imagePath));
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext().getApplicationContext(),"视频文件处理失败，请从新选择",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                    }
                }).start();
                break;
            case R.id.tv_bianji:
                startActivityForResult(new Intent(NomalVideoPlayActivity.this,VideoEditActivity.class)
                        .putExtra("path",video_path),0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            String videoPath = data.getStringExtra("videoPath");
            String imagePath = data.getStringExtra("imagePath");
            setResult(RESULT_OK,new Intent().putExtra("videoPath",videoPath).putExtra("imagePath",imagePath));
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                if (Context.AUDIO_SERVICE.equals(name))
                    return getApplicationContext().getSystemService(name);
                return super.getSystemService(name);
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        int leng = mp.getDuration()/1000;
        Log.e("视频长度1",leng+"");
        if (leng>60){
            btn_commit.setEnabled(false);
            btn_commit.setBackgroundResource(R.drawable.fx_bg_btn_gray);
            rl_bottom.setVisibility(View.VISIBLE);
        }
        mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    // video started
                    mVideoView.setBackgroundColor(Color.TRANSPARENT);
                    return true;
                }
                return false;
            }
        });
    }

}
