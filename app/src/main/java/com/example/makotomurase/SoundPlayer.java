package com.example.makotomurase;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int maruSound,batuSound,drawSound;

    public SoundPlayer(Context context) {

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        maruSound = soundPool.load(context, R.raw.maru, 1);
        batuSound = soundPool.load(context, R.raw.batu, 1);
        drawSound = soundPool.load(context, R.raw.draw, 1);
    }

    public void playMaruSound() {
        soundPool.play(maruSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playBatuSound() {
        soundPool.play(batuSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playDrawSound() {
        soundPool.play(drawSound, 2.5f, 2.5f, 1, 0, 2.0f);
    }
}

