package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button4:
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button startbutton = (Button) findViewById(R.id.button4);
        startbutton.setOnClickListener(this);
    }
}
