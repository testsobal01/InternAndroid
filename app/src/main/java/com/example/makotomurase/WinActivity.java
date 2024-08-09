package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WinActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        Button btn1 = findViewById(R.id.button_win);
        btn1.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_win) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }
}