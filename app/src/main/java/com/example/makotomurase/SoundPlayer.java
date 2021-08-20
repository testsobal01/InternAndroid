package com.example.makotomurase;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;

    public SoundPlayer(Context context) {

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        hitSound = soundPool.load(context, R.raw.semi, 1);
    }

    public void playSemi() {
        soundPool.play(hitSound, 1.0f, 1.0f, 0, 0, 1.0f);
        //hitSound, 50, 50, 0, 0, 0);
    }
}