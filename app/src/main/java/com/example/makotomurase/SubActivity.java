package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
    }

    public void onClick(View v) {
        // ①インテントの作成
        Intent intent = new Intent(this, MainActivity.class);

        // ②遷移先画面の起動
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}