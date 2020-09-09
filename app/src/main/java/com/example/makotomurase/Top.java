package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Top extends AppCompatActivity implements View.OnClickListener {

        @Override
        public void onClick(View view) {
           switch (view.getId()){
               case R.id.button4:
                   Intent intent=new Intent(this,MainActivity.class);
                   startActivity(intent);
                   Toast.makeText(getApplicationContext(),"button4",Toast.LENGTH_SHORT).show();
                   break;
           }
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);

        Button button=(Button)findViewById(R.id.button4);
        button.setOnClickListener(this);

    }
}