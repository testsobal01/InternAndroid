package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContentsActivity extends AppCompatActivity implements View.OnClickListener{

    private SoundPlayer soundPlayer;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        soundPlayer = new SoundPlayer(this);

        playBGMSound();

        Button button = findViewById(R.id.button4);
        button.setOnClickListener(this);

    }

    private void playBGMSound() {
        mediaPlayer = mediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button4);{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            mediaPlayer.release();
            mediaPlayer = null;


        }


    }

}