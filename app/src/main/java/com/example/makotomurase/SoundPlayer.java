package com.example.makotomurase;

import  android.content.Context;
import  android.media.AudioAttributes;
import  android.media.AudioManager;
import android.media.MediaPlayer;
import  android.media.SoundPool;
import android.media.MediaPlayer;


public class SoundPlayer {

    private  static  SoundPool soundPool;

    private  static MediaPlayer mediaPlayer;
    private  static  int WinSound;
    private  static  int LoseSound;
    private static int DrawSound;



    public  SoundPlayer(Context context){

        soundPool = new SoundPool(3,AudioManager.STREAM_MUSIC,0);//最大同時再生数3、再生品質0

        WinSound = soundPool.load(context,R.raw.androidwinsound,1);
        LoseSound = soundPool.load(context,R.raw.androidlosesound,1);
        DrawSound = soundPool.load(context,R.raw.androiddrawsound02,1);









    }

    //正解したときに音を再生するメソッド
    public  void playWinSound(){

        soundPool.play(WinSound,1.0f,1.0f,1,0,1.0f);
    }

    //不正解だった時に音を再生するメソッド
    public  void playLoseSound(){

        soundPool.play(LoseSound,1.0f,1.0f,1,0,1.0f);

    }

    public  void playDrawSound(){

        soundPool.play(DrawSound,1.0f,1.0f,1,0,1.0f);

    }


}

