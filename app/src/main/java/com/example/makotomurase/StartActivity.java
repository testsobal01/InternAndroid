package com.example.makotomurase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);    //ボタンを押せるようにする
        Button btn4 = (Button) findViewById(R.id.startButton);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.startButton) {
            //次に起動したいアクティビティに切り替える操作
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
