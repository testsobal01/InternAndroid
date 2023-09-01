package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Button btn3 = (Button) findViewById(R.id.button4);
        btn3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id= view.getId();
        if (id==R.id.button4){
            Intent intent=new Intent(this, MainActivity.class);
            intent.putExtra("KEY","MainActivity2からの呼び出し");
            startActivity(intent);
        }

    }
}
