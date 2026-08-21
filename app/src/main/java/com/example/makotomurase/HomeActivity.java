package com.example.makotomurase;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    AnimatorSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button StartButton = findViewById(R.id.start);
        StartButton.setOnClickListener(this);
        Button Mode1Button = findViewById(R.id.mode1);
        Mode1Button.setOnClickListener(this);
        set = (AnimatorSet) AnimatorInflater.loadAnimator(HomeActivity.this, R.animator.blink_animation);
        set.setTarget(StartButton);
    }

    @Override
    protected void onStart(){
        super.onStart();
        set.start();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.start) {
            Intent intent1 = new Intent(this, MainActivity.class);
            startActivity(intent1);
        }

        if (id == R.id.mode1) {
            Intent intent2 = new Intent(this, HardModeActivity.class);
            startActivity(intent2);
        }
    }
}