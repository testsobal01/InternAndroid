package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Button button = (Button) findViewById(R.id.button4);

        button.setOnClickListener(this);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button4:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("Key", "ContentsActivityからの呼び出し");
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "ボタン１", Toast.LENGTH_SHORT).show();
                break;

        }
    }



}