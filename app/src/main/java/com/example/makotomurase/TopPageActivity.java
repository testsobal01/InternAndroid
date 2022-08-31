package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopPageActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_page);

        Button start = findViewById(R.id.startbutton);
        start.setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}