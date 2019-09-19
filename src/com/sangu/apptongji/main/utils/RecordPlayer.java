package com.sangu.apptongji.main.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Administrator on 2017-10-18.
 */

public class RecordPlayer {
    private static MediaPlayer mediaPlayer;

    private Context mcontext;

    public RecordPlayer(Context context) {
        this.mcontext = context;
    }

    // 播放录音文件
    public void playRecordFile(File file) {
        if (file.exists() && file != null) {
            if (mediaPlayer == null) {
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(mcontext, "com.sangu.app.fileprovider", file);//通过FileProvider创建一个content类型的Uri
                    mcontext.grantUriPermission(mcontext.getPackageName(), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    uri = Uri.fromFile(file);
                }
                mediaPlayer = MediaPlayer.create(mcontext, uri);
            }
            mediaPlayer.start();

            //监听MediaPlayer播放完成
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer paramMediaPlayer) {
                    // TODO Auto-generated method stub
                    //弹窗提示
                    Toast.makeText(mcontext,
                            "试听完毕",
                            Toast.LENGTH_SHORT).show();
                    if (playVoiceFinish != null) {
                        playVoiceFinish.playVoiceFinish();
                    }
                }
            });

        }
    }

    private PlayVoiceFinish playVoiceFinish;

    public void setPlayVoiceFinish(PlayVoiceFinish playVoiceFinish) {
        this.playVoiceFinish = playVoiceFinish;
    }

    public interface  PlayVoiceFinish{
        void playVoiceFinish();
    }

    // 暂停播放录音
    public void pausePalyer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.e("TAG", "暂停播放");
        }

    }

    // 暂停播放录音
    public boolean isPlaying() {
        if (mediaPlayer!=null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    // 停止播放录音
    public void stopPalyer() {
        // 这里不调用stop()，调用seekto(0),把播放进度还原到最开始
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            Log.e("TAG", "停止播放");
        }
    }
}
