package com.example.makotomurase;
//sakikooka-d-1
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContentsActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        Button btns = (Button) findViewById(R.id.buttonstart);
        btns.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.buttonstart:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }

    }



}