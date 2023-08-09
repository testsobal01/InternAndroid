package com.example.makotomurase;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class Clap {
    private SoundPool soundPool;
    private int soundId;

    public Clap(Context context) {
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(context, R.raw.push, 1);
    }

    public void play() {
        soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
    }
}

