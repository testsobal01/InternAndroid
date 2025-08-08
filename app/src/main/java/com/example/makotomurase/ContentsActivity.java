package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContentsActivity extends AppCompatActivity implements View.OnClickListener {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        
        Button btn0 = findViewById(R.id.button0);
        btn0.setOnClickListener(this);

    }

    private void setQuestionValue() {
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button0) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("KEY", "ContentsActivityからの呼び出し");
            startActivity(intent);
        }

    }

}