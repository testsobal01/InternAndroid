package com.example.makotomurase;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Log;
import android.widget.TextView;

public class AnumationD implements Animator.AnimatorListener {

    private  TextView textView;
    private int size=0;
    public void setAnimetion(TextView textView){
        this.textView=textView;
        // PropertyValuesHolderを使ってＸ軸方向移動範囲のpropertyを保持
        PropertyValuesHolder vhX = PropertyValuesHolder.ofFloat(
                "translationX",
                0.0f,
               0f );

        // PropertyValuesHolderを使ってＹ軸方向移動範囲のpropertyを保持
        PropertyValuesHolder vhY = PropertyValuesHolder.ofFloat(
                "translationY",
                0.0f,
                0.0f );

        // PropertyValuesHolderを使って回転範囲のpropertyを保持
        PropertyValuesHolder vhRotaion = PropertyValuesHolder.ofFloat(
                "rotation",
                0.0f,
                0.0f );

        // ObjectAnimatorにセットする
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                textView,
                vhX ,
                vhY ,
                vhRotaion );

        // 再生時間を設定 3000msec=3sec
        objectAnimator.setDuration(3000);

        // リスナーを設定
        objectAnimator.addListener(this);

        // アニメーションを開始する
        objectAnimator.start();
    }
    @Override
    public void onAnimationStart(Animator animation) {
        Log.d("debug","onAnimationStart()");
    }

    // アニメーションがキャンセルされると呼ばれる
    @Override
    public void onAnimationCancel(Animator animation) {
        Log.d("debug","onAnimationCancel()");
    }

    // アニメーション終了時
    @Override
    public void onAnimationEnd(Animator animation) {
        Log.d("debug","onAnimationEnd()");
    }

    // 繰り返しでコールバックされる
    @Override
    public void onAnimationRepeat(Animator animation) {
        size++;
        textView.setTextSize(size);
        Log.d("debug","onAnimationRepeat()");
    }
}
