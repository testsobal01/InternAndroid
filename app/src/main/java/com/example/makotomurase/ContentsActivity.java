package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ContentsActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        Button btn1 = findViewById(R.id.start);
        btn1.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.start) {
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }

    }
}