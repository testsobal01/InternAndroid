package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);



        Button startbutton = findViewById(R.id.startbutton);
        startbutton.setOnClickListener(this);
        //startbutton.setBackground(getResources().getDrawable(R.drawable.button_top_start,null));
    }

    public void onStartButtonClick(View view){
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.startbutton){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}