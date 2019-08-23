package com.example.seminor.murase.makoto.murasemakoto;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ResultActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        pref = getSharedPreferences("save_data",MODE_PRIVATE);
        prefEditor = pref.edit();

        int win = pref.getInt("win", 0);
        int lose = pref.getInt("lose", 0);
        int draw = pref.getInt("draw", 0);

        TextView wintext = (TextView) findViewById(R.id.win);
        TextView drawtext = (TextView) findViewById(R.id.draw);
        TextView losetext = (TextView) findViewById(R.id.lose);
        wintext.setText("WIN：" + win);
        drawtext.setText("DRAW：" + draw);
        losetext.setText("LOSE：" + lose);


    }
}
