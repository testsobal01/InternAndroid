package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{
    AnimatorSet set3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        Button startbutton = findViewById(R.id.button1);
        startbutton.setOnClickListener(this);

        set3 = (AnimatorSet) AnimatorInflater.loadAnimator(StartActivity.this,
                R.animator.team_e_animation);
        set3.setTarget(startbutton);
    }
    @Override//アニメーション実行
    protected void onStart() {
        super.onStart();
        //アニメーション開始
        set3.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id== R.id.button1)  {
       // Intent intent = new Intent(StartActivity.this,MainActivity.class);
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("KEY","StartACtivityからの呼び出し");
         startActivity(intent);
    }
    }

    public void StartActivity(Intent intent) {
    }
}


