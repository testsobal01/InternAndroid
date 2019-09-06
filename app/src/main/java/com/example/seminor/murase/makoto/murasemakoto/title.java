package com.example.seminor.murase.makoto.murasemakoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class title extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(100);

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        return super.onTouchEvent(event);
    }

}
