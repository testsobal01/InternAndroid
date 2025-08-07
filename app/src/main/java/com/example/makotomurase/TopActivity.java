package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        Button button = findViewById(R.id.START);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.START){Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}