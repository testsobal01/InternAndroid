package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button btn1 = findViewById(R.id.button4);
        btn1.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R. id. button4) {
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);
        }
    }
}

