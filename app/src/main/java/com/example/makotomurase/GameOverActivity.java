package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GameOverActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Button btnRe = findViewById(R.id.button_restart);
        btnRe.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.button_restart){
            Intent intent = new Intent(this, StartScreenActivity.class);
            startActivity(intent);
        }
    }
}