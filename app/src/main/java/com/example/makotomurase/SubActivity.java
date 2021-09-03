package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.SoundPool;
import android.media.AudioManager;

public class SubActivity extends AppCompatActivity implements View.OnClickListener{

    private SoundPool m_soundPool;
    private int restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Button btn10 = (Button) findViewById(R.id.button10);
        btn10.setOnClickListener(this);
        m_soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
        restart = m_soundPool.load(this.getApplicationContext(), R.raw.restart, 1);

            }
    public void onClick(View view1) {


        switch(view1.getId()){
            case R.id.button10:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                SubActivity.this.finish();
                m_soundPool.play(restart, 1.0f, 1.0f, 1, 0, 1.0f);
                break;
        }

        /*
        Button button = findViewById(R.id.button10);
        button.setOnClickListener((View v) -> {
                startActivity(new Intent(this, MainActivity.class));
        });

         */

    }
}