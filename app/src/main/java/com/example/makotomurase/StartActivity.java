package com.example.makotomurase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(StartActivity.this, BettingActivity.class);
        startActivity(intent);

        //バイブレーション機能
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(500);
    }
}
