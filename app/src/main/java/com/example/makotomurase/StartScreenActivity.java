package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartScreenActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Button btn_start = findViewById(R.id.button_start);
        btn_start.setOnClickListener(this);
        Button btn_cancel = findViewById(R.id.button_cancel);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.button_start) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.button_cancel) {
            Toast.makeText(this, getString(R.string.cancel_toast), Toast.LENGTH_LONG).show();
        }
    }
}