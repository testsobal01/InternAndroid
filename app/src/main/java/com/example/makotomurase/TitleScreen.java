package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TitleScreen extends AppCompatActivity implements View.OnClickListener {

    AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build();
    SoundPool soundPool = new SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(1)
            .build();

    int[]  act={0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        Button btn1 = findViewById(R.id.btn_start);
        btn1.setOnClickListener(this);

        act[0] = soundPool.load(this, R.raw.create, 1);
        act[1] = soundPool.load(this, R.raw.start, 1);
        soundPool.play(act[0],1f,1f,0,0,1f);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_start){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            soundPool.play(act[1],1f,1f,0,0,1f);

        }
    }
}