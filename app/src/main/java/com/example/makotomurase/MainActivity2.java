package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button btn3 = (Button) findViewById(R.id.button_start);
        btn3.setOnClickListener(this);


    }
    public void onClick(View view){

        switch(view.getId()) {
            case R.id.button_start:
                Log.d("hoge","hoge");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            }

    }


}