package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    SoundPool soundPool;    // 効果音を鳴らす本体（コンポ）
    int mp3start;          // 効果音データ（mp3）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(5)
                    .build();
        }

        mp3start = soundPool.load(this, R.raw.start, 1);
    }
    public void startGame(View view)
    {
        soundPool.play(mp3start,1f , 1f, 0, 0, 1f);
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
    }
}