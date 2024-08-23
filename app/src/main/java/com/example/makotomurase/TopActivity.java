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
        Button button4=findViewById(R.id.button4);
        button4.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if (id==R.id.button4) {
            Intent intent =new Intent(this,MainActivity.class);
            intent.putExtra("KEY","start");
            startActivity(intent);
        }

    }
}