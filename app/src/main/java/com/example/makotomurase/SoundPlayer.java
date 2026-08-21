package com.example.makotomurase;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundPlayer {

    private SoundPool soundPool;
    private int title_to_game_sound;
    private int Win_sound;
    private int Lose_sound;
    private int Draw_sound;
    private int Reset_sound;
    public SoundPlayer(Context context) {

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        title_to_game_sound = soundPool.load(context, R.raw.maou_se_system37, 1);
        Win_sound = soundPool.load(context, R.raw.maou_se_system24, 1);
        Lose_sound = soundPool.load(context, R.raw.maou_se_system25, 1);
        Draw_sound = soundPool.load(context, R.raw.maou_se_system28, 1);
        Reset_sound = soundPool.load(context, R.raw.maou_se_system34, 1);
    }

    public void playtitleSound() {
        soundPool.play(title_to_game_sound,2.0f, 2.0f,0, 0, 1.0f);
    }

    public void playWinSound() {
        soundPool.play(Win_sound,1.0f, 1.0f,0, 0, 1.0f);
    }

    public void playLoseSound() {
        soundPool.play(Lose_sound,1.0f, 1.0f,0, 0, 1.0f);
    }

    public void playDrawSound() {
        soundPool.play(Draw_sound,1.0f, 1.0f,0, 0, 1.0f);
    }
    public void playResetSound() {
        soundPool.play(Reset_sound,1.0f, 1.0f,0, 0, 1.0f);
    }

}