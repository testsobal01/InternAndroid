package com.example.makotomurase;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int winSound;
    private static int loseSound;

    private static int drawSound;

    public SoundPlayer(Context context) {

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        winSound = soundPool.load(context,R.raw.win,1);
        loseSound = soundPool.load(context, R.raw.lose, 1);
        drawSound = soundPool.load(context,R.raw.draw,1);
    }

    public void playWinSound() {
        soundPool.play(winSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playLoseSound() {
        soundPool.play(loseSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playDrawSound() {
        soundPool.play(drawSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
