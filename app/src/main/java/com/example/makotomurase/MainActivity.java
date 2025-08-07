package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SoundPool soundPool;    // 効果音を鳴らす本体（コンポ）
    int mp3c,mp3r,mp3w,mp3l,mp3d;          // 効果音データ（mp3）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View layout = findViewById(R.id.answer);
        layout.setBackgroundColor(Color.YELLOW);

//        Intent intent = getIntent();
//        Bundle extra = intent.getExtras();
//        String intentString = extra.getString("KEY");
//
//        TextView textView = (TextView)findViewById(R.id.start_button);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        // 起動時に関数を呼び出す
        setQuestionValue();

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

        // ③ 読込処理(CDを入れる)
        mp3c = soundPool.load(this, R.raw.click, 1);
        mp3r = soundPool.load(this, R.raw.reset, 1);
        mp3w = soundPool.load(this, R.raw.winner, 1);
        mp3l = soundPool.load(this, R.raw.loser, 1);
        mp3d = soundPool.load(this, R.raw.draw, 1);
    }

    public void winner(){
        soundPool.play(mp3w,1f , 1f, 0, 0, 1f);
    }
    public void loser(){
        soundPool.play(mp3l,1f , 1f, 0, 0, 1f);
    }
    public void draw(){
        soundPool.play(mp3d,1f , 1f, 0, 0, 1f);
    }

    public void onC(){
        // ④ 再生処理(再生ボタン)
        soundPool.play(mp3c,1f , 1f, 0, 0, 1f);
        //(再生ファイル指定[mp3a],左右ボリューム[1f,1f],優先順位[0],ループ回数-1で無制限[0],再生速度[1f:通常,2f:倍速])
    }

    public void onR(){
        // ④ 再生処理 (再生ボタン)
        soundPool.play(mp3r,1f , 1f, 0, 0, 1f);
        //(再生ファイル指定[mp3a],左右ボリューム[1f,1f],優先順位[0],ループ回数-1で無制限[0],再生速度[1f:通常,2f:倍速])
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
            onC();
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            onC();
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            onR();
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

        View layout = findViewById(R.id.answer);
        // Highが押された
        if (isHigh) {
//            setContentView(R.layout.activity_main);

            // result には結果のみを入れる
            if (question < answer) {
                layout.setBackgroundColor(Color.RED);
                result = "WIN";
                score = 2;
                winner();
                Vibrator vib= (Vibrator) getSystemService(VIBRATOR_SERVICE);
               vib.vibrate(500);
            } else if (question > answer) {
                layout.setBackgroundColor(Color.BLUE);
                result = "LOSE";
                score = -1;
                loser();
            } else {
                layout.setBackgroundColor(Color.YELLOW);
                result = "DRAW";
                score = 1;
                draw();
            }
        } else {
            if (question > answer) {
                layout.setBackgroundColor(Color.RED);
                result = "WIN";
                score = 2;
                winner();
                Vibrator vib= (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);
            } else if (question < answer) {
                layout.setBackgroundColor(Color.BLUE);
                result = "LOSE";
                score = -1;
                loser();
            } else {
                layout.setBackgroundColor(Color.YELLOW);
                result = "DRAW";
                score = 1;
                draw();
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

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }

}

