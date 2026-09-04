package com.example.makotomurase;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private int startsound;

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

        Button button = findViewById(R.id.start);
        button.setOnClickListener(new ButtonClickListener());

        AudioAttributes audioAttributes = new AudioAttributes.Builder()

                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();

        startsound = soundPool.load(this,R.raw.start,1);

    }
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            int id = view.getId();

            if (id == R.id.start) {

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);

                soundPool.play(startsound, 60.0f, 60.0f, 0, 0, 1);
            }


        }
    }
}