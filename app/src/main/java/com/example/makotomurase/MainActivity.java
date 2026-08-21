package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    AnimatorSet set;

    private SoundPlayer soundPlayer;
    private MediaPlayer mediaPlayer;
    private boolean isButton;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

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

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        Button btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(this);

        soundPlayer = new SoundPlayer(this);

        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("Score", MODE_PRIVATE);
        prefEditor = pref.edit();

        mediaPlayer = MediaPlayer.create(this, R.raw.maou_game_village10);
        mediaPlayer.setLooping(true);

        mediaPlayer.seekTo(0);
        mediaPlayer.start();

        TextView textView = findViewById(R.id.answer_value);
        TextView textView2 = findViewById(R.id.question);

        set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                R.animator.blink_animation);

        set.setTarget(textView);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onClick(View view) {
        TextView ColorChange1 = (TextView) findViewById(R.id.question);
        FrameLayout ColorChange2 = (FrameLayout) findViewById(R.id.answer);
        int id = view.getId();
        if (id == R.id.button4) {
            isButton = false;
            mediaPlayer.stop();
            soundPlayer.playtitleSound();
            Intent intent = new Intent(this, TitleActivity.class);
            startActivity(intent);
        }
        if (!isButton) {

            isButton = true;

            if (id == R.id.button1) {
                setAnswerValue();
                checkResult(true);
            } else if (id == R.id.button2) {
                setAnswerValue();
                checkResult(false);
            } else if (id == R.id.button3) {
                set.cancel();
                isButton = false;
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(
                            500,
                            VibrationEffect.DEFAULT_AMPLITUDE
                    ));
                }
                setQuestionValue();
                clearAnswerValue();
                clearScoreValue();
                ColorChange1.setBackgroundColor(Color.rgb(255,0,255));
                ColorChange2.setBackgroundColor(Color.rgb(255,255,0));
            }
        }
        else if (id == R.id.button3) {
            set.cancel();
            isButton = false;
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                ));
            }
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            ColorChange1.setBackgroundColor(Color.rgb(255,0,255));
            ColorChange2.setBackgroundColor(Color.rgb(255,255,0));
            TextView answerValue = (TextView) findViewById(R.id.answer_value);
            answerValue.setAlpha(1f);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        TextView textView = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("score_input",textView.getText().toString());
        prefEditor.commit();
        TextView textView1 = (TextView) findViewById(R.id.text_highscore);
        prefEditor.putString("score_input1",textView1.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mediaPlayer.start();
        TextView textView = (TextView)findViewById(R.id.text_score);
        String readText=pref.getString("score_input","0");
        textView.setText(readText);
        TextView textView1 = (TextView)findViewById(R.id.text_highscore);
        String readText1=pref.getString("score_input1","0");
        textView1.setText(readText1);
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer_value);
        txtView.setText(R.string.num);
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

        TextView txtView = findViewById(R.id.answer_value);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer_value);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        TextView colorChange1 = (TextView) findViewById(R.id.question);
        FrameLayout colorChange2 = (FrameLayout) findViewById(R.id.answer);
        TextView colorChange3 = (TextView) findViewById(R.id.text_result);


        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                set.start();
                result = "WIN";
                score = 2;
                colorChange1.setBackgroundColor(Color.RED);
                colorChange2.setBackgroundColor(Color.RED);
                colorChange3.setBackgroundColor(Color.parseColor("#eb6ea5"));
            } else if (question > answer) {
                set.cancel();
                result = "LOSE";
                score = -1;
                colorChange1.setBackgroundColor(Color.rgb(0,150,255));
                colorChange2.setBackgroundColor(Color.rgb(0,150,255));
                colorChange3.setBackgroundColor(Color.parseColor("#66ccff"));
            } else {
                set.cancel();
                result = "DRAW";
                score = 1;
                colorChange1.setBackgroundColor(Color.GRAY);
                colorChange2.setBackgroundColor(Color.GRAY);
                colorChange3.setBackgroundColor(Color.parseColor("#ffff66"));
            }
        } else {
            if (question > answer) {
                set.start();
                result = "WIN";
                score = 2;
                colorChange1.setBackgroundColor(Color.RED);
                colorChange2.setBackgroundColor(Color.RED);
                colorChange3.setBackgroundColor(Color.parseColor("#eb6ea5"));
            } else if (question < answer) {
                set.cancel();
                result = "LOSE";
                score = -1;
                colorChange1.setBackgroundColor(Color.rgb(0,150,255));
                colorChange2.setBackgroundColor(Color.rgb(0,150,255));
                colorChange3.setBackgroundColor(Color.parseColor("#66ccff"));
            } else {
                set.cancel();
                result = "DRAW";
                score = 1;
                colorChange1.setBackgroundColor(Color.GRAY);
                colorChange2.setBackgroundColor(Color.GRAY);
                colorChange3.setBackgroundColor(Color.parseColor("#ffff66"));
            }
        }




        if (result == "WIN") {
            soundPlayer.playWinSound();
        } else if (result == "LOSE") {
            soundPlayer.playLoseSound();
        } else {
            soundPlayer.playDrawSound();
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う

        txtResult.setText(question + ":" + answer + "(" + result + ")");

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);

    }

    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long l) {
                // 途中経過を受け取った時に何かしたい場合
                // 今回は特に何もしない
            }

            @Override
            public void onFinish() {
                // 3秒経過したら次の値をセット
                setQuestionValue();
                isButton = false;
            }
        }.start();
    }

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        TextView txthighScore = (TextView) findViewById(R.id.text_highscore);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        int newhighScore = Integer.parseInt(txthighScore.getText().toString()) ;
        if (newhighScore<newScore){
            newhighScore=newScore;
            txthighScore.setText(Integer.toString(newhighScore));
        }
        txtScore.setText(Integer.toString(newScore));
    }


    private void clearScoreValue() {
        
        soundPlayer.playResetSound();
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }
}

