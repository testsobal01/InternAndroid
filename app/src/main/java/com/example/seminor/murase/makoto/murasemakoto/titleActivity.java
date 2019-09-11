package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class titleActivity extends AppCompatActivity implements View.OnClickListener{

    int flag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        setTimer();

        Button btn1 = (Button) findViewById(R.id.btnToGame);
        btn1.setOnClickListener(this);
    }

    public void onClick(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if ( flag == -1){

            builder.setTitle("win");
            builder.setMessage("勝ち！！");

            builder.show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }else{

            builder.setTitle("おてつき");
            builder.setMessage("おてつきだよ！！");

            builder.show();
        }
    }

    public void setTimer(){
        Random rand = new Random();
        ImageView imageView = (ImageView) findViewById(R.id.army);
        flag = flag * -1;

        if ( flag == 1) {
            imageView.setImageResource(R.drawable.nomal);
        }else{
            imageView.setImageResource(R.drawable.weakpoint);
        }

        new CountDownTimer(rand.nextInt( 11)*1000 + 1000, 1000){
            @Override
            public void onTick(long l){

            }
            @Override
            public void onFinish(){
                setTimer();
            }

        }.start();

    }
}
