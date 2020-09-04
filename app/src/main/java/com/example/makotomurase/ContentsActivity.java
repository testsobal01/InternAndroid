package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ContentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
    }


    public void startGame(View view){
        int id = view.getId();
        switch (id){
            case R.id.start_button:
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
        }

    }
}