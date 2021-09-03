package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import android.provider.ContactsContract;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class TitleActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        Button btn_title = (Button) findViewById(R.id.button_title);
        btn_title.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button_title:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }

    }
}