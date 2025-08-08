package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContentsActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * SEを鳴らす用
     */
    private AudioPlayer audioPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        Button button = findViewById(R.id.button0);
        button.setOnClickListener(this);

        audioPlayer= new AudioPlayer(this);// audioPlayerのインスタンスを作成
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button0){
            audioPlayer.playPushButtonSE();// SEを鳴らす

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("KEY","ContentActivityからの呼び出し");
            startActivity(intent);
        }
    }
}