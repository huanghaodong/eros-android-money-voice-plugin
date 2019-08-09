package com.example.huanghaodong.voiceplugin;

import android.content.Context;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


import java.io.IOException;

public class VoiceModule {
    private MediaPlayer mediaPlayer;
    private Context context;

    public VoiceModule setContext(Context context) {
        this.context = context;
        return this;
    }
    public void openAssetMusics() {

        try {
            //播放 assets/a2.mp3 音乐文件
            AssetFileDescriptor fd = context.getAssets().openFd("zf.mp3");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        mediaPlayer = MediaPlayer.create(context, R.raw.zf);
//        //用prepare方法，会报错误java.lang.IllegalStateExceptio
//        //mediaPlayer.prepare();
//        mediaPlayer.start();

    }

}
