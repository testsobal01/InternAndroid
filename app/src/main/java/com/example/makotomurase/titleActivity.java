package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titlescreen);

        Button jun = findViewById(R.id.superbutton1);
        jun.setOnClickListener(this);

    }

    // インテントの作成
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.superbutton1) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("JUN", "MainActivityからの呼び出し");
            startActivity(intent);

        }
    }
}