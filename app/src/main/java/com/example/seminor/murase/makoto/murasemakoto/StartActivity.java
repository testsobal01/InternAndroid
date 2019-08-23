package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button button = (Button)findViewById(R.id.buttonStart);
        button.setOnClickListener(new ButtonClickListener());
    }

    class ButtonClickListener implements View.OnClickListener {
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.buttonStart:
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
