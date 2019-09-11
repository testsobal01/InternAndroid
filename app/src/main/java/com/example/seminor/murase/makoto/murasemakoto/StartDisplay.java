package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowContentFrameStats;
import android.widget.Button;

public class StartDisplay extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_display);

        Button button=(Button)findViewById(R.id.buttonS);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.buttonS:
                Intent intent=new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
