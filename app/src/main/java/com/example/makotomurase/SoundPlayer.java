package com.example.makotomurase;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;


public class SoundPlayer {

    private static SoundPool soundPool;
    private static int win;
    private static int lose;

    public SoundPlayer(Context context) {

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        win = soundPool.load(context, R.raw.se_maoudamashii_onepoint15, 1);
        lose = soundPool.load(context, R.raw.kiminomake, 1);
    }
    public void playwin() {
        soundPool.play(win, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playlose(){
        soundPool.play(lose,1.0f,1.0f,1,0,1.0f);



    }

}
