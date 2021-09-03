package com.example.makotomurase;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;
    private static int resetSound;
    private static int celebSound;

    public SoundPlayer(Context context) {

        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC,0);

        hitSound = soundPool.load(context, R.raw.hit, 1);
        overSound = soundPool.load(context, R.raw.over, 1);
        resetSound = soundPool.load(context, R.raw.reset, 1);
        celebSound = soundPool.load(context, R.raw.celeb, 1);
    }

    public void playHitSound() {
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playOverSound() {
        soundPool.play(overSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playResetSound() {
        soundPool.play(resetSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playCelebSound() {
        soundPool.play(celebSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
