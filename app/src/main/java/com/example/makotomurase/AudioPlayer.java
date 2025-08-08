package com.example.makotomurase;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * SEを再生するクラス
 */
public class AudioPlayer {

    private static SoundPool soundPool;
    private AudioAttributes audioAttributes;
    private static int winSE;
    private static int loseSE;
    private static int drawSE;
    private static int pushButtonSE;

    public AudioPlayer(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(2)
                    .build();

        } else {
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        }

        winSE = soundPool.load(context, R.raw.win_se, 1);
        loseSE = soundPool.load(context, R.raw.lose_se, 1);
        drawSE = soundPool.load(context, R.raw.draw_se, 1);
        pushButtonSE = soundPool.load(context, R.raw.push_button_se, 1);
    }

    /**
     * WINの時のSEを鳴らすメソッド
     */
    public void playWinSE() {
        soundPool.play(winSE, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    /**
     * LOSEの時のSEを鳴らすメソッド
     */
    public void playLoseSE() {
        soundPool.play(loseSE, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    /**
     * DRAWの時のSEを鳴らすメソッド
     */
    public void playDrawSE() {
        soundPool.play(drawSE, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    /**
     * 結果い応じてSEを鳴らす
     * @param result　結果
     */
    public void playResultSE(String result, Context context){
        if(result.equals(context.getResources().getString(R.string.w_result))) playWinSE();
        else if (result.equals(context.getResources().getString(R.string.l_result))) playLoseSE();
        else if(result.equals(context.getResources().getString(R.string.d_result))) playDrawSE();
    }

    /**
     * ボタンを押した時のSEを鳴らすメソッド
     */
    public void playPushButtonSE() {
        soundPool.play(pushButtonSE, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
