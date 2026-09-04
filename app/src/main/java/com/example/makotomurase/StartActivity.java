package com.example.makotomurase;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    AnimatorSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button = findViewById(R.id.buttonstart);
        button.setOnClickListener(this);



        set = (AnimatorSet) AnimatorInflater.loadAnimator(StartActivity.this,
                R.animator.start_animator);
        //アニメーション対称のオブジェクトを設定
        set.setTarget(button);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //アニメーションの開始を宣言
        set.start();
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("START", "StartActivityからの呼び出し");
        startActivity(intent);
    }
}