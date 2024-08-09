package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.AnimationUtils;

import java.util.IllegalFormatCodePointException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    public SoundPool soundPool;
    public int[] action = {0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        // 起動時に関数を呼び出す
        setQuestionValue();



        //効果音
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(3).build();

        action[0] = soundPool.load(this, R.raw.win, 1);
        action[1] = soundPool.load(this, R.raw.lose, 1);
        action[2] = soundPool.load(this, R.raw.draw, 1);

        pref = getSharedPreferences("Score",MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TextView score = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("Score",score.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("Score","0");
        txtScore.setText(readText);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            Vibrator vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);
            setAnswerValue();
            checkResult(true);


        } else if (id == R.id.button2) {
            Vibrator vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            Vibrator vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(1000);
            setQuestionValue();
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
        int sound;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                sound = 0;
              
                findViewById(R.id.answer).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anime));
                findViewById(R.id.question).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anime2));
                findViewById(R.id.answer).setBackgroundResource(R.color.red);
                findViewById(R.id.question).setBackgroundResource(R.color.red);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                sound = 1;
              
                findViewById(R.id.answer).setBackgroundResource(R.color.blue);
                findViewById(R.id.question).setBackgroundResource(R.color.blue);
            } else {
                result = "DRAW";
                score = 1;
                sound = 2;
              
                findViewById(R.id.question).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anime3));
                findViewById(R.id.answer).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anime3));
                findViewById(R.id.answer).setBackgroundResource(R.color.green);
                findViewById(R.id.question).setBackgroundResource(R.color.green);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                sound = 0;

                findViewById(R.id.answer).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anime));
                findViewById(R.id.question).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anime2));
                findViewById(R.id.answer).setBackgroundResource(R.color.red);
                findViewById(R.id.question).setBackgroundResource(R.color.red);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                sound = 1;
              
                findViewById(R.id.answer).setBackgroundResource(R.color.blue);
                findViewById(R.id.question).setBackgroundResource(R.color.blue);
            } else {
                result = "DRAW";
                score = 1;
                sound = 2;
              
                findViewById(R.id.question).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anime3));
                findViewById(R.id.answer).startAnimation(AnimationUtils.loadAnimation(this, R.anim.anime3));
                findViewById(R.id.answer).setBackgroundResource(R.color.green);
                findViewById(R.id.question).setBackgroundResource(R.color.green);
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        //効果音を再生
        soundPool.play(action[sound], 1f , 1f, 0, 0, 1f);

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
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
                findViewById(R.id.answer).setBackgroundResource(R.color.yellow);
                findViewById(R.id.question).setBackgroundResource(R.color.purple);
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
    private void animation(){
    }

}

