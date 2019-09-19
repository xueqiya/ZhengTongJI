package com.sangu.apptongji.main.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sangu.apptongji.R;
import com.sangu.apptongji.ui.BaseActivity;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2017-07-10.
 */

public class VideoPlayActivity extends BaseActivity {
    private JCVideoPlayerStandard jcVideoPlayerStandard;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_play_video);
        getWindow().getDecorView().setVisibility(View.VISIBLE);
        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
        String url = getIntent().getStringExtra("videoUrl");
        String url2 = getIntent().getStringExtra("imageUrl");
        String name = getIntent().getStringExtra("name");
        boolean setUp = jcVideoPlayerStandard.setUp(url, JCVideoPlayer.SCREEN_LAYOUT_LIST,
                name+"的视频");
        if (setUp) {
            Glide.with(getApplicationContext()).load(url2)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .error(R.drawable.default_error)
                    .crossFade().into(jcVideoPlayerStandard.thumbImageView);
        }else {
            Toast.makeText(getApplicationContext(),"视频播放失败",Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }
}
