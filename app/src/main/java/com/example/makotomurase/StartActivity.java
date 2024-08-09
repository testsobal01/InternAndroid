package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.Loader;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.widget.Toast;


public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    AnimatorSet set3;

    public SoundPool soundPool;
    public int soundSound;
    public int soundBGM1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        Button startbutton = findViewById(R.id.button1);
        startbutton.setOnClickListener(this);
      
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                // USAGE_MEDIA
                // USAGE_GAME
                .setUsage(AudioAttributes.USAGE_GAME)
                // CONTENT_TYPE_MUSIC
                // CONTENT_TYPE_SPEECH, etc.
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(2)
                .build();

        soundBGM1 = soundPool.load(this, R.raw.bgm1, 1);
        soundSound = soundPool.load(this, R.raw.sound, 1);

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            Log.d("debug","sampleId="+sampleId);
            Log.d("debug","status="+status);
        });
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (0 == status) {
                    Toast.makeText(getApplicationContext(), "LoadComplete", Toast.LENGTH_LONG).show();
                    soundPool.play(soundBGM1, 1.0f, 1.0f, 0, -1, 1);
                }
            }
        });

        set3 = (AnimatorSet) AnimatorInflater.loadAnimator(StartActivity.this,
                R.animator.team_e_animation);
        set3.setTarget(startbutton);
    }
  
    @Override//アニメーション実行
    protected void onStart() {
        super.onStart();
        //アニメーション開始
        set3.start();
    }

    @Override
    public void onClick(View v) {
        soundPool.play(soundSound, 1.0f, 1.0f, 0, 0, 1);
        soundPool.stop(soundBGM1);
        soundPool.release();
        int id = v.getId();
        if (id== R.id.button1)  {
            // Intent intent = new Intent(StartActivity.this,MainActivity.class);
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("KEY","StartACtivityからの呼び出し");
            startActivity(intent);
        }
    }

    public void StartActivity(Intent intent) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        soundPool.play(soundBGM1, 1.0f, 1.0f, 0, 0, 1);
    }
}


