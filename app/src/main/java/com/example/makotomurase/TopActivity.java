package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopActivity extends AppCompatActivity implements View.OnClickListener {
    int mp3a;
    
    SoundPool soundPool1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();


        soundPool1 = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(2)
                .build();

        mp3a = soundPool1.load(this, R.raw.start, 1);

        Button button = findViewById(R.id.START);
        button.setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.START){Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            soundPool1.play(mp3a,1f , 1f, 0, 0, 1f);

        }
    }
}