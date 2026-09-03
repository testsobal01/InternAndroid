package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import android.media.AudioAttributes;
import android.media.SoundPool;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool soundPool;
    private int soundOne, soundTwo;
    private Button button1, button2, button3;

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


        // AudioAttributes 設定
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION) // 効果音向け
                .build();

        // SoundPool 初期化
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(2)
                .build();

        // 効果音をロード
        soundOne = soundPool.load(this, R.raw.hit, 1);
        soundTwo = soundPool.load(this, R.raw.kira, 1);


        // 起動時に関数を呼び出す
        setQuestionValue();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1.0f);
            setAnswerValue();
            checkResult(true);
        } else if (id == R.id.button2) {
            soundPool.play(soundTwo, 1.0f, 1.0f, 0, 0, 1.0f);
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
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

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
            } else {
                result = "DRAW";
                score = 1;
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

