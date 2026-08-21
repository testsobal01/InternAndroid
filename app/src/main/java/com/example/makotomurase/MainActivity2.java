package com.example.makotomurase;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    SoundPool soundPool;
    int mp3a;
    int mp3b;
    int mp3c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            soundPool=new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        else{
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(5)
                    .build();
            mp3a=soundPool.load(this,R.raw.rappa,1);
            mp3b=soundPool.load(this,R.raw.hit,1);
            mp3c=soundPool.load(this,R.raw.start,1);
        }
        Button button =findViewById(R.id.btnext);
        button.setOnClickListener(new ButtonClickListener());
    }
    private class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = new Intent(MainActivity2.this,MainActivity.class);
            startActivity(intent);
            soundPool.play(mp3c,1f,1f,0,0,1f);
        }
    }
}