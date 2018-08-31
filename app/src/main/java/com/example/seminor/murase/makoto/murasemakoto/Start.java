package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

      Button start =(Button) findViewById(R.id.btn_start);
      start.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
