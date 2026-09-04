package com.example.makotomurase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView textFinalScore = findViewById(R.id.textFinalScore);
        TextView textHighScore = findViewById(R.id.textHighScore);
        Button buttonBackToTop = findViewById(R.id.buttonBackToTop);

        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);

        textFinalScore.setText("最終スコア: " + finalScore);

        SharedPreferences pref = getSharedPreferences("Save", MODE_PRIVATE);
        String readText = pref.getString("score_input", "0");
        textHighScore.setText("保存されたスコア: " + readText);

        buttonBackToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, Top.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("Top", "FromResult");

                startActivity(intent);
                finish();
            }
        });
    }
}