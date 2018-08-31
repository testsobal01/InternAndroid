package com.example.seminor.murase.makoto.murasemakoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener {

    //アプリ起動
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        //イベントリスナー設定
        Button btnStart = (Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
    }

    //ボタンクリック時処理
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btnStart:
                Intent intent = new Intent(this, MainActivity.class);   //インテント生成
                startActivity(intent);  //起動
                break;
        }

    }
}
