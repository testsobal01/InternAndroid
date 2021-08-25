package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    TextView Anitext;

    SoundPool soundPool;    // 効果音を鳴らす本体（コンポ）
    int mp3button;          // 効果音データ（mp3）
    int mp3win;
    int mp3lose;
    int mp3draw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        pref = getSharedPreferences("team-e",MODE_PRIVATE);
        prefEditor = pref.edit();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(5)
                    .build();
        }

        mp3button = soundPool.load(this, R.raw.button, 1);
        mp3win = soundPool.load(this, R.raw.win, 1);
        mp3lose = soundPool.load(this, R.raw.lose, 1);
        mp3draw = soundPool.load(this, R.raw.draw, 1);

        // 起動時に関数を呼び出す
        setQuestionValue();
        Anitext = (TextView) findViewById(R.id.question);
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        Vibrator vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        switch (id) {
            case R.id.button1:
                vib.vibrate(1000);
                setAnswerValue();
                checkResult(true);
                break;
            case R.id.button2:
                vib.vibrate(2000);
                setAnswerValue();
                checkResult(false);
                break;
            case R.id.button3:
                soundPool.play(mp3button,1f , 1f, 0, 0, 1f);
                vib.vibrate(3000);
                setQuestionValue();
                clearAnswerValue();
                break;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("keep_score",textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView = (TextView)findViewById(R.id.text_score);
        String readText = pref.getString("keep_score","0");
        textView.setText(readText);
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(getString(R.string.atai2));
        txtView.setBackgroundColor(Color.parseColor("#ffff00"));

        LinearLayout layoutTextAnswer = (LinearLayout) findViewById(R.id.text_answer);
        layoutTextAnswer.setBackgroundColor(Color.parseColor("#ffffff"));

        TextView txtViewQuestion = (TextView) findViewById(R.id.question);
        txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);
        TextView txtView = (TextView) findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = (TextView) findViewById(R.id.question);
        TextView txtViewAnswer = (TextView) findViewById(R.id.answer);
        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());
        TextView txtResult = (TextView) findViewById(R.id.text_result);
        // 結果を示す文字列を入れる変数を用意
        String result;
        int score = 0;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                soundPool.play(mp3win,1f , 1f, 0, 0, 1f);
                score = 2;
                transAnimationTest(txtViewAnswer);
            } else if (question > answer) {
                result = "LOSE";
                soundPool.play(mp3lose,1f , 1f, 0, 0, 1f);
                transAnimationTest(txtViewQuestion);
                score = -1;
            } else {
                result = "DRAW";
                soundPool.play(mp3draw,1f , 1f, 0, 0, 1f);
                score = 1;
                transAnimationTest(txtViewQuestion);
                transAnimationTest(txtViewAnswer);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                soundPool.play(mp3win,1f , 1f, 0, 0, 1f);
                transAnimationTest(txtViewAnswer);
                score = 2;
            } else if (question < answer) {
                result = "LOSE";
                soundPool.play(mp3lose,1f , 1f, 0, 0, 1f);
                transAnimationTest(txtViewQuestion);
                score = -1;
            } else {
                result = "DRAW";
                soundPool.play(mp3draw,1f , 1f, 0, 0, 1f);
                transAnimationTest(txtViewQuestion);
                transAnimationTest(txtViewAnswer);
                score = 1;
            }
        }


        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");
        String res = getString(R.string.kekka);
        txtResult.setText(res + question + ":" + answer + "(" + result + ")");
        setAnswerColor(result);

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
            }
        }.start();
    }

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

    private void setAnswerColor(String _result) {
        LinearLayout layoutTextAnswer = (LinearLayout) findViewById(R.id.text_answer);
        TextView txtViewQuestion = (TextView) findViewById(R.id.question);
        TextView txtViewAnswer = (TextView) findViewById(R.id.answer);
        if (_result == "WIN") {
            layoutTextAnswer.setBackgroundColor(Color.parseColor("#00bfff"));
            txtViewQuestion.setBackgroundColor(Color.parseColor("#00bfff"));
            txtViewAnswer.setBackgroundColor(Color.parseColor("#dc143c"));
        }
        else if (_result == "LOSE") {
            layoutTextAnswer.setBackgroundColor(Color.parseColor("#dc143c"));
            txtViewQuestion.setBackgroundColor(Color.parseColor("#dc143c"));
            txtViewAnswer.setBackgroundColor(Color.parseColor("#00bfff"));
        }
        else {
            layoutTextAnswer.setBackgroundColor(Color.parseColor("#c0c0c0"));
            txtViewQuestion.setBackgroundColor(Color.parseColor("#c0c0c0"));
            txtViewAnswer.setBackgroundColor(Color.parseColor("#c0c0c0"));
        }
    }

    public void transAnimationTest( TextView v ){
        TranslateAnimation trans = new TranslateAnimation(
                // 自分の幅の2倍左の位置から開始
                Animation.RELATIVE_TO_SELF, 1,
                // 自分の幅の5倍左の位置（元の位置）で終了
                Animation.RELATIVE_TO_SELF, 0,
                // 縦には移動しない
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);

        // 2秒かけてアニメーションする
        trans.setDuration( 1000 );

        // アニメーション終了時の表示状態を維持する
        trans.setFillEnabled(true);
        trans.setFillAfter  (true);

        // アニメーションを開始
        v.startAnimation(trans);
    }
}