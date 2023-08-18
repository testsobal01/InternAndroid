package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        pref = getSharedPreferences("TeamProduct", MODE_PRIVATE);
        prefEditor = pref.edit();
        // 起動時に関数を呼び出す
        setQuestionValue();//値の設定
    }

    @Override
    public void onClick(View view) {//各ボタンを押したときの処理
        int id = view.getId();
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
            vib.vibrate(300);
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            vib.vibrate(300);
        } else if (id == R.id.button3) {

            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            vib.vibrate(300);
        }
    }

    private void clearAnswerValue() {//restartのときだけ
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(getString(R.string.size_2));
    }

    private void setQuestionValue() {//値1の変更
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
                result = getString(R.string.win_0);
                score = 2;
            } else if (question > answer) {
                result = getString(R.string.lose_0);
                score = -1;
            } else {
                result = getString(R.string.draw_0);
                score = 1;
            }
        } else {
            if (question > answer) {
                result = getString(R.string.win_0);
                score = 2;
            } else if (question < answer) {
                result = getString(R.string.lose_0);
                score = -1;
            } else {
                result = getString(R.string.draw_0);
                score = 1;
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();

        txtResult.setText(getString(R.string.label_score) + question + ":" + answer + "(" + result + ")");


        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
    }

    private void setNextQuestion() {//カウントダウン
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        new CountDownTimer(3000, 1000) {
            int score;
            @Override
            public void onTick(long l) {
                // 途中経過を受け取った時に何かしたい場合
                // 今回は特に何もしない
            }

            @Override
            public void onFinish() {
                // 3秒経過したら次の値をセット
                setQuestionValue();//再度値1の変更
                setScore(score);
            }
        }.start();
    }

    private void setScore(int score) {//スコア表示
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;//表示されるスコア

        txtScore.setText(Integer.toString(newScore));
        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("label_score", textView.getText().toString());
        prefEditor.commit();
    }

    private void clearScoreValue() {//restartのときだけ
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }

    @Override
    protected void onPause(){//保存
        super.onPause();
        TextView textView = (TextView)findViewById(R.id.text_score);//保存のためのテキストビュー取得
        prefEditor.putString("label_score", textView.getText().toString());//文字列保存
        prefEditor.commit();
    }

    @Override
    protected void onResume(){//読み込み
        super.onResume();
        TextView tv = (TextView) findViewById(R.id.text_score);
        String readText = pref.getString("label_score", "保存されていない");
        tv.setText(readText);

    }
}

