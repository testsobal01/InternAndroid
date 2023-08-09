package com.example.makotomurase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class top extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top);
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.start_button){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
