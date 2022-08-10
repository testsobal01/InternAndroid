package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    SoundPool soundPool;    // 効果音を鳴らす本体（コンポ）
    int mp3a;          // 効果音データ（mp3）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btn_start = (Button) findViewById(R.id.button_start);
        btn_start.setOnClickListener(this);

        // ② 初期化（電源を入れる・コピペOK）
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

        // ③ 読込処理(CDを入れる)
        mp3a = soundPool.load(this, R.raw.a, 1);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start:
                soundPool.play(mp3a,1f , 1f, 0, 0, 1f);
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(1000);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
    
}