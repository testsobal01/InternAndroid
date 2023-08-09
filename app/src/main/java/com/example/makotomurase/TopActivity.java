package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TopActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        Button TopButton = findViewById(R.id.top_button);
        TopButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view){
        //view（buttonの中身）を格納する変数id
        int id = view.getId();
        if(id == R.id.top_button){
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
        }
    }

}