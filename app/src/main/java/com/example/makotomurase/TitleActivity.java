package com.example.makotomurase;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener{

    private SoundPlayer soundPlayer;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_title_actibity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.title), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button button_title = findViewById(R.id.button_start);
        button_title.setOnClickListener(this);
        Button button_howto = findViewById(R.id.button_howto);
        button_howto.setOnClickListener(this);


        soundPlayer = new SoundPlayer(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.maou_game_vehicle02);
        mediaPlayer.setLooping(true);

        mediaPlayer.seekTo(0);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    public void onClick(View view) {
    int id = view.getId();
    if (id == R.id.button_start) {
        mediaPlayer.stop();
        soundPlayer.playtitleSound();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    } else if (id == R.id.button_howto) {
        mediaPlayer.stop();
        soundPlayer.playtitleSound();
        Intent intent = new Intent(this, HowtoPlayActivity.class);
        startActivity(intent);
    }
    }
}