package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.TextureView;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    private SoundPlayer soundPlayer;
    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;

    private MediaPlayer mediaPlayer;

    //プリファレンスの生成
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    //アニメーションを定義するAnimatorSetオブジェクトを宣言する
    AnimatorSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.displayCutout());
            view.setPadding(insets.left, insets.top, insets.right, 0);
            return windowInsets;
        });




        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        mediaPlayer.start();

        //ボタン押したらへこむように見えるやつ
        View.OnTouchListener darkenTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                ImageButton imageButton = (ImageButton) view;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setAlpha(0.6f);
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        view.setAlpha(1.0f);
                        break;
                }
                return false;
            }
        };


        soundPlayer = new SoundPlayer(this);
        Intent intent=new Intent(this,StartActivity.class);
        startActivity(intent);

        ImageButton btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        btn1.setOnTouchListener(darkenTouchListener);

        ImageButton btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        btn2.setOnTouchListener(darkenTouchListener);

        ImageButton btn3 = (ImageButton) findViewById(R.id.button3);
        btn3.setOnClickListener(this);
        btn3.setOnTouchListener(darkenTouchListener);

        // 起動時に関数を呼び出す
        setQuestionValue();
        set = new AnimatorSet();

        //"AndroidSemonor"は、スコアを保存する先のファイル名的な奴
        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();

    }

    //onCreateの中だとStartタイミングが早すぎて間に合わない。
    //onCreateの後の工程のonStartにてstartを実装する
    @Override
    protected void onStart() {
        super.onStart();

        }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        Vibrator vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
        //バイブ
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(
                    200, VibrationEffect.DEFAULT_AMPLITUDE));}


        if (id == R.id.button1) {
            soundPlayer.playHitSound();
            setAnswerValue();
            checkResult(true);
        } else if (id == R.id.button2) {
            soundPlayer.playHitSound();
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            set.pause();
            soundPlayer.playHitSound();
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
        }


    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        TextView cutInText = findViewById(R.id.cut_in_text);

        TextView myLayout=findViewById(R.id.question);
        TextView myLayout1=findViewById(R.id.answer);

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                TextView textView = findViewById(R.id.answer);
                flash(textView);
                myLayout.setBackgroundColor(Color.GREEN);
                myLayout1.setBackgroundColor(Color.GREEN);
                triggerRandomCutIn(cutInText);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                myLayout.setBackgroundColor(Color.BLUE);
                myLayout1.setBackgroundColor(Color.BLUE);
                TextView textview = findViewById(R.id.question);
                flash(textview);
            } else {
                result = "DRAW";
                score = 1;
                myLayout.setBackgroundColor(Color.GRAY);
                myLayout1.setBackgroundColor(Color.GRAY);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                TextView textView = findViewById(R.id.answer);
                flash(textView);
                triggerRandomCutIn(cutInText);

                myLayout.setBackgroundColor(Color.GREEN);
                myLayout1.setBackgroundColor(Color.GREEN);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                TextView textview = findViewById(R.id.question);
                flash(textview);
                myLayout.setBackgroundColor(Color.BLUE);
                myLayout1.setBackgroundColor(Color.BLUE);
            } else {
                result = "DRAW";
                score = 1;
                myLayout.setBackgroundColor(Color.GRAY);
                myLayout1.setBackgroundColor(Color.GRAY);
            }
        }





        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
    }

    void flash(TextView winner){
        if (set != null) {
            if (set.isRunning()) {
                set.pause();
            }
        }

        set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                R.animator.blink_animation);
        //アニメーション対称のオブジェクトを設定
        set.setTarget(winner);
        if (set.isPaused()){
            set.start();
        }else {
            set.start();
        }
    }

    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                // 途中経過を受け取った時に何かしたい場合
                // 今回は特に何もしない
            }

            @Override
            public void onFinish() {
                // 3秒経過したら次の値をセット
                setQuestionValue();

                TextView myLayout=findViewById(R.id.question);
                TextView myLayout1=findViewById(R.id.answer);
                myLayout.setBackgroundColor(Color.parseColor("#ff00ff"));
                myLayout1.setBackgroundColor(Color.parseColor("#ffff00"));
            }
        }.start();
    }

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }

    @Override
    protected void onPause(){
        super.onPause();

        TextView textView = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();

        TextView textView = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("main_input","0");
        textView.setText(readText);
    }

    private void triggerRandomCutIn(TextView textView){
        if(textView==null)return;

        Random r=new Random();

        int chance = r.nextInt(50);


            String[] sillyPhrase={
                    "おなかすいたね",
                    "WINといか大勝利だべ",
                    "所詮運ゲーだからね",
                    "調子乗らないほうがいいよ",
                    "もしかして天才!!??",
                    "王手",
                    "チェックメイトですの",
                    "賭ケグルイましょう？？？",
                    "一生のおねがいだからもう一回",
                    "う、うぉw",
                    "WIN",
                    "WIN",
                    "WIN",
                    "WIN",
                    "WIN",
                    "WIN",
            };
            int index = r.nextInt(sillyPhrase.length);
            textView.setText(sillyPhrase[index]);
            textView.setTextColor(Color.parseColor("#E91E63"));


        showTextCutInAnimation(textView);
    }

    private void showTextCutInAnimation(final TextView textView){
        textView.setVisibility(View.VISIBLE);
        textView.setScaleX(0f);
        textView.setScaleY(0f);
        textView.setAlpha(0f);

        textView.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .alpha(1.0f)
                .setDuration(250)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        textView.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .alpha(0f)
                                .setStartDelay(1200)
                                .setDuration(400)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setVisibility(View.GONE);
                                    }
                                })
                                .start();
                    }
                })
                .start();
    }

}





