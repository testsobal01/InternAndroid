package com.example.makotomurase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button sbtn = findViewById(R.id.startbutton);
        sbtn.setOnClickListener(this);

        //username input button
        Button btn_username_input = findViewById(R.id.inputUsername);
        btn_username_input.setOnClickListener(view -> showInputDialog());
    }

    //process input username
    private void showInputDialog() {
        EditText input = new EditText(this);
        TextView userName = findViewById(R.id.inputTxt);
        input.setText(userName.getText());

        new AlertDialog.Builder(this)
                .setTitle("input username")
                .setView(input)
                .setPositiveButton("OK", (dialog, which) ->
                        userName.setText(input.getText().toString()))
                .setNegativeButton("Cancel",null)
                .show();
        sharedPreferences = getSharedPreferences("USERNAME",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("USERNAME", userName.getText().toString());
        editor.commit();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.startbutton) {
            //画面遷移の処理
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}