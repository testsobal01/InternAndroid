package com.example.makotomurase;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    private static SoundPool soundPool;
    private static int draw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btn_s = findViewById(R.id.start_button);
        btn_s.setOnClickListener(this);

        AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(3).build();
        draw = soundPool.load(this,R.raw.draw,1);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.start_button){

            soundPool.play(draw,1f,1f,0,0,1f);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}

