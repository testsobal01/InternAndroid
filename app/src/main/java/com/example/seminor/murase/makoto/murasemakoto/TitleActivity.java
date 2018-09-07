package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        Button titlebutton = (Button)findViewById(R.id.titlebutton);
        titlebutton.setOnClickListener(this);

        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);

        int highscore = pref.getInt("high_score", 0);
        TextView scoredisp = (TextView)findViewById(R.id.scoredisp);
        scoredisp.setText("highscore  :  " + highscore);
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
