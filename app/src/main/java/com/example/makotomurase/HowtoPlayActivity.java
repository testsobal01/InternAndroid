package com.example.makotomurase;

import android.content.Intent;
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

        soundPlayer = new SoundPlayer(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_back) {
            soundPlayer.playtitleSound();
            Intent intent = new Intent(this, TitleActivity.class);
            startActivity(intent);
        }
    }
}