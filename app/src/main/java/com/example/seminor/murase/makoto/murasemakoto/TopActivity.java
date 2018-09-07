package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
        Button sendbutton = findViewById(R.id.btn_start);
        sendbutton.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.btn_start:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
