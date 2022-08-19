package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Typeface customFont = Typeface.createFromAsset(getAssets(), "onryou.otf");
        TextView myText = findViewById(R.id.title);
        myText.setTypeface(customFont);
    }

    public void startGame(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
