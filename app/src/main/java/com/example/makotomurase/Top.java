package com.example.makotomurase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Top extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_top);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.button_top).setOnClickListener(this);
        Button button_Top = (Button) findViewById(R.id.button_top);
        button_Top.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
        int id = view.getId();
        if (id == R.id.button_top){
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("Top","");
            startActivity(intent);
        }
    }
}