package com.example.makotomurase;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundManager {

    private SoundPool soundPool;
    private int winSound;
    private int loseSound;
    private int drawSound;

    public SoundManager(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(2)
                .build();

        winSound = soundPool.load(context, R.raw.win, 1);
        loseSound = soundPool.load(context, R.raw.lose, 1);
        drawSound = soundPool.load(context, R.raw.draw, 1);
    }

    public void win() {
        soundPool.play(winSound, 1.0f, 1.0f, 0, 0, 1);
    }
    public void lose() {
        soundPool.play(loseSound, 1.0f, 1.0f, 0, 0, 1);
    }
    public void draw() {
        soundPool.play(drawSound, 1.0f, 1.0f, 0, 0, 1);
    }
}
