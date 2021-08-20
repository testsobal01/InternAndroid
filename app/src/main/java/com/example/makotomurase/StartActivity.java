package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.AudioManager;
import android.media.SoundPool;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    private SoundPool soundPool;
    private int sound_start, sound_tap, sound_win, sound_lose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
        sound_start = soundPool.load(this.getApplicationContext(), R.raw.start, 1);
        sound_tap = soundPool.load(this.getApplicationContext(), R.raw.start, 1);

        Button btn = (Button) findViewById(R.id.Startbutton);
        btn.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Startbutton:
                soundPool.play(sound_start, 1.0F, 1.0F, 0, 0, 1.0F);    // 音楽再生
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}