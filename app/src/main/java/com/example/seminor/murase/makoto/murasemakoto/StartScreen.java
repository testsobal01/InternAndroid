package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartScreen extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        Button strbutton = (Button) findViewById(R.id.startbutton);
        strbutton.setOnClickListener(this);
        Button strbutton2 = (Button) findViewById(R.id.nonstartbutton);
        strbutton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startbutton:
                Intent intent = new Intent(this, MainActivity.class );
                startActivity(intent);
                break;
            case R.id.nonstartbutton:
                this.finish();
                break;

        }
    }
}
