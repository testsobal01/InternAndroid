package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SubActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);

        Intent intent2 = getIntent();
        Bundle extra = intent2.getExtras();
        String intentString = extra.getString("KEY");

        TextView textView = (TextView) findViewById(R.id.contents_text);
        textView.setText(intentString);

    }
}