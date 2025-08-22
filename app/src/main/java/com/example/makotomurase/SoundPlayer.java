package com.example.makotomurase;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;

    private static int BGMSound;
    private static int BGM2Sound;


    public SoundPlayer(Context context) {

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        hitSound = soundPool.load(context, R.raw.hit, 1);
        overSound = soundPool.load(context, R.raw.over, 1);
        BGMSound = soundPool.load(context, R.raw.bgm, 1);
        BGM2Sound = soundPool.load(context, R.raw.bgm2, 1);
    }

    public void playHitSound() {
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playOverSound() {
        soundPool.play(overSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playBGMSound() {
        soundPool.play(BGMSound, 1.0f, 1.0f, 1, 100, 1.0f);
    }
    public void playBGM2Sound() {
        soundPool.play(BGM2Sound, 1.0f, 1.0f, 1, 100, 1.0f);
    }

}







