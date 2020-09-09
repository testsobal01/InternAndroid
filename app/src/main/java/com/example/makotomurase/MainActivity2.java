package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.JetPlayer;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{


    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button btn3 = (Button) findViewById(R.id.button_start);
        btn3.setOnClickListener(this);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.aa);

    }


    public void onClick(View view){




        switch(view.getId()) {
            case R.id.button_start:


                mediaPlayer.start();



                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);


                break;

            }

    }


}