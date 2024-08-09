package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btn1 = findViewById(R.id.button_home);
        btn1.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}