package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button start = findViewById(R.id.start);
        start.setOnClickListener(this);
    }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
}
