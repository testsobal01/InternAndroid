package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

        pref = getSharedPreferences("highAndLow",MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
    }
    @Override
    protected void onPause(){
        super.onPause();
        TextView textView = (TextView)findViewById(R.id.text_score);

        prefEditor.putString("score",textView.getText().toString());
        prefEditor.commit();
    }
    @Override
    protected void onResume(){
        super.onResume();

        TextView textView = (TextView)findViewById(R.id.text_score);

        String readText = pref.getString("score","0");
        textView.setText(readText);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            TextView queColor = findViewById(R.id.question);
            TextView ansColor = findViewById(R.id.answer);
            ansColor.setBackgroundColor(Color.rgb(255,255,0));
            queColor.setBackgroundColor(Color.rgb(255,0,255));
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

        switch (score){
            case 2:
                TextView queColor = findViewById(R.id.question);
                TextView ansColor = findViewById(R.id.answer);
                ansColor.setBackgroundColor(Color.rgb(0,255,127));
                queColor.setBackgroundColor(Color.rgb(0,255,127));

                ObjectAnimator queAnimator = ObjectAnimator.ofFloat( queColor, "scaleX", 0f, 1f );
                ObjectAnimator ansAnimator = ObjectAnimator.ofFloat(ansColor, "scaleX", 0f, 1f);
                queAnimator.setDuration( 3000 );
                ansAnimator.setDuration( 3000 );
                queAnimator.start();
                ansAnimator.start();

                break;
            case 1:
                queColor = findViewById(R.id.question);
                ansColor = findViewById(R.id.answer);
                ansColor.setBackgroundColor(Color.rgb(	255,255,0));
                queColor.setBackgroundColor(Color.rgb(	255,255,0));
                break;
            case -1:
                queColor = findViewById(R.id.question);
                ansColor = findViewById(R.id.answer);
                ansColor.setBackgroundColor(Color.rgb(255,0,0));
                queColor.setBackgroundColor(Color.rgb(255,0,0));

                queAnimator = ObjectAnimator.ofFloat(queColor, "alpha", 0f,1f);
                ansAnimator = ObjectAnimator.ofFloat(ansColor, "alpha", 0f,1f);
                queAnimator.setDuration( 1000 );
                ansAnimator.setDuration( 1000 );
                queAnimator.start();
                ansAnimator.start();

                break;
        }

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

