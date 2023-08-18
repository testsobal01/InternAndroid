package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        //color change
        TextView txt=(TextView)findViewById(R.id.main_text);
        txt.setTextColor(Color.WHITE);
        //font change
        Typeface customFont = Typeface.createFromAsset(getAssets(), "Caprasimo-Regular.ttf");
        TextView myTextStart = findViewById(R.id.main_text);
        TextView myTextTap = findViewById(R.id.button1);
        myTextStart.setTypeface(customFont);
        myTextTap.setTypeface(customFont);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

}