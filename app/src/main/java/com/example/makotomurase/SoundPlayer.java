package com.example.makotomurase;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int loseSound;
    private static int winSound;
    private static int drawSound;

    public SoundPlayer(Context context) {
        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attr)
                .setMaxStreams(2)
                .build();

        loseSound = soundPool.load(context, R.raw.androidfalse, 1);
        winSound = soundPool.load(context, R.raw.androidtrue, 1);
        drawSound = soundPool.load(context,R.raw.androiddraw,1);
    }

    public void playLoseSound() {
        soundPool.play(loseSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playWinSound() {
        soundPool.play(winSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playDrawSound() {
        soundPool.play(drawSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
