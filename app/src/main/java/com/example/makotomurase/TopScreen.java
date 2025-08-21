package com.example.makotomurase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TopScreen extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_screen);

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
    }
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

    }