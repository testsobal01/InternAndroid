package com.example.makotomurase;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int soundOne;
    private static int soundTwo;

    public SoundPlayer(Context context) {

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        soundOne = soundPool.load(context, R.raw.mo, 1);
    }

    public void playSoundOne() {
        soundPool.play(soundOne, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
