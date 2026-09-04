package jp.codeforfun.catchtheball;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.makotomurase.R;

public class SoundPlayer {

    private static SoundPool soundPool;
    private static int dramSound;
    private static int buttonSound;
    private static int yheeeSound;
    private static int shockSound;
    private static int katiSound;
    private static int makeSound;
    private static int doutenSound;

    public SoundPlayer(Context context) {

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        dramSound = soundPool.load(context, R.raw.dram, 1);
        buttonSound = soundPool.load(context, R.raw.button, 1);
        yheeeSound = soundPool.load(context, R.raw.yheee, 1);
        shockSound = soundPool.load(context, R.raw.shock, 1);
        katiSound = soundPool.load(context, R.raw.kati, 1);
        makeSound = soundPool.load(context, R.raw.make, 1);
        doutenSound = soundPool.load(context, R.raw.douten, 1);
    }

    public void playDramSound() {
        soundPool.play(dramSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playButtonSound() {
        soundPool.play(buttonSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playYheeeSound() {
        soundPool.play(yheeeSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playShockSound() {
        soundPool.play(shockSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playKatiSound() {
        soundPool.play(katiSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playMakeSound() {
        soundPool.play(makeSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    public void playDoutenSound() {
        soundPool.play(doutenSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }


}