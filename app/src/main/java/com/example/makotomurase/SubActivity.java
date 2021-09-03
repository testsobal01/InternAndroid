package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SubActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Button btn10 = (Button) findViewById(R.id.button10);
        btn10.setOnClickListener(this);

            }
    public void onClick(View view1) {

        switch(view1.getId()){
            case R.id.button10:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                SubActivity.this.finish();
                break;
        }

        /*
        Button button = findViewById(R.id.button10);
        button.setOnClickListener((View v) -> {
                startActivity(new Intent(this, MainActivity.class));
        });

         */

    }
}