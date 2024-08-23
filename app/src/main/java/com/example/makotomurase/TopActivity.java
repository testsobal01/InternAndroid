package com.example.makotomurase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TopActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);



        Button startbutton = findViewById(R.id.startbutton);
        startbutton.setOnClickListener(this);

        Button introbutton = findViewById(R.id.introbutton);
        introbutton.setOnClickListener(this);
    }

    public void onStartButtonClick(View view){
        finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.startbutton){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if(id == R.id.introbutton){
            AlertDialog.Builder builder = new AlertDialog.Builder(TopActivity.this);
            builder.setTitle("遊び方");
            builder.setMessage(" 右に表示される値が左の値よりも上(大きい)か下(小さい)かあてよう。画面上部にあるボタンをおしてね。数字は0から10まで表示されるよ。リスタートボタンでゲームをリセットしてやり直しできるよ。");
            builder.setPositiveButton(" ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        }
    }
}