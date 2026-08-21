package com.example.makotomurase;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HowtoPlayActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPlayer soundPlayer;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_howto_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.howto), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button button_back = findViewById(R.id.button_back);
        button_back.setOnClickListener(this);

        Button button_to_game = findViewById(R.id.button_to_game);
        button_to_game.setOnClickListener(this);

        soundPlayer = new SoundPlayer(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.maou_game_field09);
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
        if (id == R.id.button_back) {
            mediaPlayer.stop();
            soundPlayer.playtitleSound();
            Intent intent = new Intent(this, TitleActivity.class);
            startActivity(intent);
        } else if (id == R.id.button_to_game) {
            mediaPlayer.stop();
            soundPlayer.playtitleSound();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}