package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(this);

    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                Intent intent =new Intent(this, MainActivity.class);
                startActivity(intent);
                //setAnswerValue();
                //checkResult(true);
                break;
            //case R.id.button2:
                //setAnswerValue();
                //checkResult(false);
                //break;
            //case R.id.button3:
                //setQuestionValue();
                //clearAnswerValue();
                //break;

        }

    }

}