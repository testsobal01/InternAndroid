package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class TitleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        return true;
    }
}