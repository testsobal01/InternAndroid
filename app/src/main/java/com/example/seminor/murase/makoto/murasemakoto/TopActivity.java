package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStart;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        btnStart = (Button)findViewById(R.id.btn_start);
        btnStart.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                startGame();
        }
    }

    private void startGame() {
        intent = new Intent(this, com.example.seminor.murase.makoto.murasemakoto.MainActivity.class);
        startActivity(intent);
    }
}
