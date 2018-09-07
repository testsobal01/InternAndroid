package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class TopActivity extends AppCompatActivity  implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        Button button = (Button) findViewById(R.id.main_text);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_text:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
}
}
