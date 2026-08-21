package com.example.makotomurase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MiniGame_resultActivity extends AppCompatActivity implements View.OnClickListener {



    TextView Score;
    Intent intent;

    Button secRe;


    Button secExit;

    Intent intentExit;

    Intent intentRe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mini_game_result);

//        Intent intent =getIntent();
//        Bundle extra =intent.getExtras();
//        String intentString = extra.getString("Score");
//
//        Score=findViewById(R.id.click);
//        Score.setText(intentString);

        intent = getIntent();
        Bundle extra =intent.getExtras();
        String intentString = extra.getString("Score");
        int Getint = Integer.parseInt(intentString);
        Score=findViewById(R.id.click);
        Score.setText(String.valueOf(Getint/30));


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;


        });


        secExit=findViewById(R.id.modeExit);
        secExit.setOnClickListener( this);



        secRe=findViewById(R.id.modeRe);
        secRe.setOnClickListener( this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.modeExit) {
            intentExit = new Intent(this, MainActivity.class);
            startActivity(intentExit);
        }



        if (id == R.id.modeRe) {
            intentRe = new Intent(this, MiniGameActivity.class);
            startActivity(intentRe);
        }


    }



}