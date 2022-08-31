package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.AudioAttributes;
import android.os.Build;


public class Top_Menu extends AppCompatActivity implements View.OnClickListener {
    SoundPool soundPool;
    int sound1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_menu);

        Button button1 = findViewById(R.id.button_start);
        button1.setOnClickListener(this);

        Button button2 = findViewById(R.id.button_end);
        button2.setOnClickListener(this);

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

        sound1 = soundPool.load(this, R.raw.kettei, 1);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_start:
                soundPool.play(sound1, 1f, 1f, 0, 0,1f);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.button_end:
                soundPool.play(sound1, 1f, 1f, 0, 0,1f);
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
    }
}