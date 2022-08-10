package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        Button stbtn = (Button) findViewById(R.id.startbutton);
        stbtn.setOnClickListener(this);
    }

    // 背景タッチイベント処理
    public void onClick(View view) {

    switch(view.getId()){
        case R.id.startbutton:
            Intent intent =new Intent(this,MainActivity.class);
            startActivity(intent);
            break;
    }

    }

}