package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartPage extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        Button btn = (Button) findViewById(R.id.start);
        btn.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
            switch (id) {
                case R.id.start:
                    Intent intent = new Intent (this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }



}


