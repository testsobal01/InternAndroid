package com.example.makotomurase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class TOP extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top);
        //ボタンを押した時
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //インテントの追加（これが遷移用ロジックです）
                Intent intent = new Intent(TOP.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
