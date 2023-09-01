package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Title extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        Button title = findViewById(R.id.button_start);
        title.setOnClickListener(this);
    }

    public void onClick(View v){
        Intent intent = new Intent(this,MainActivity.class);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }
}
