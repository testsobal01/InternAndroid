package com.example.makotomurase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MiniGameActivity extends AppCompatActivity implements View.OnClickListener {

    Button cat;
    Button exit;
    TextView miniGameCount;
    TextView countDown_sec;


    int count=0;
    int memory=0;

    int sec = 30;
    int sec_lie =30;
    int sec_user;
    Intent intent;
    Intent toResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mini_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        cat = findViewById(R.id.nyaaaaa);
        cat.setOnClickListener(this);
        exit = findViewById(R.id.Exit);
        exit.setOnClickListener(this);
        //ボタンが押されたときonClickへつなげる


        sec_user=30000;

       countDown();



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        miniGameCount=findViewById(R.id.MiniGameCount);

        if (id == R.id.nyaaaaa){
            count=memory+1;
            memory=count;

            miniGameCount.setText(String.valueOf(count));
        }

        if (id == R.id.Exit){
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }


    }




    private void countDown() {
        countDown_sec = findViewById(R.id.sec);
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        new CountDownTimer(sec_user, 1000) {
            @Override
            public void onTick(long l) {
                // 途中経過を受け取った時に何かしたい場合
                sec=sec_lie-1;
                sec_lie=sec;
                countDown_sec.setText(String.valueOf(sec));
                //秒数カウント
            }

            @Override
            public void onFinish() {
                // 30秒たったら移動する
                Ido();
            }
        }.start();
    }
    public void Ido(){
        toResult = new Intent(this, MiniGame_resultActivity.class);
        toResult.putExtra("Score",Integer.toString(count));
        startActivity(toResult);

        //TopActivityの所にリザルトを入れる
    }




}