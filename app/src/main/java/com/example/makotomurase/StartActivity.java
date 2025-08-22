package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button start = findViewById(R.id.start);
        start.setOnClickListener(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm_main_theme); // スタート画面用のBGMを指定
        mediaPlayer.setLooping(true); // リピート再生を有効にする
        mediaPlayer.start(); // 再生開始

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.start) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // アプリが非表示になったらBGMを一時停止
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // アプリが再表示されたらBGMを再開
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // このアクティビティが破棄されるときにMediaPlayerを解放
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



}