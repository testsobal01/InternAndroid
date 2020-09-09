package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class titleActivity extends AppCompatActivity implements View.OnClickListener {

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.start_button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        Button start_button = (Button) findViewById(R.id.start_button);
        start_button.setOnClickListener(this);
    }
}