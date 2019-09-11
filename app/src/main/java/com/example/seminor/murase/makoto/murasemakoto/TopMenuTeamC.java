package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TopMenuTeamC extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_menu_team_c);

        TextView topMenuAppName = (TextView)findViewById(R.id.top_menu_app_name);
        topMenuAppName.setText("HIGH & LOW");

        Button topMenuButton = (Button)findViewById(R.id.btn_top_men);
        topMenuButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //topページのボタンを押されたらMainActivityに遷移
            case R.id.btn_top_men:
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                break;
        }
    }


}
