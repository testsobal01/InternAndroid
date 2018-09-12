package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartScreen extends AppCompatActivity implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(this);
    }
}
