package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textView_question;
    TextView textView_answer;

    @Override
    protected void onPause() {
        super.onPause();
        saveScoreValue();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveScoreValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_question = findViewById(R.id.question);
        textView_answer = findViewById(R.id.answer);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        // 起動時に関数を呼び出す
        setQuestionValue();
        setSaveScoreValue();
    }

    public void blinkText(TextView textView_question, long duration, long offset){
        Animation anm = new AlphaAnimation(0.0f, 1.0f);
        anm.setDuration(duration);
        anm.setStartOffset(offset);
        anm.setRepeatCount(1);
        textView_question.startAnimation(anm);
    }

    public void blinkText1(TextView textView_answer, long duration, long offset){
        Animation anm = new AlphaAnimation(0.0f, 1.0f);
        anm.setDuration(duration);
        anm.setStartOffset(offset);
        anm.setRepeatCount(1);
        textView_answer.startAnimation(anm);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
            vib.vibrate(800);
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            vib.vibrate(1500);
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            long vibratePattern[] = {500, 1000, 500, 1000};
            vib.vibrate(vibratePattern, -1);
        }

        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        txtViewQuestion.getText();
        String str=String.valueOf(txtViewQuestion.getText());
        txtViewAnswer.getText();
        String str2=String.valueOf(txtViewAnswer.getText());

        int num=Integer.parseInt(str);
        int num2=Integer.parseInt(str2);

            //Highの時
        if (id==R.id.button1) {
            if (num > num2) {
                txtViewQuestion.setBackgroundColor(Color.GREEN);
                txtViewAnswer.setBackgroundColor(Color.GRAY);
            }
            else if (num < num2) {
                txtViewQuestion.setBackgroundColor(Color.GRAY);
                txtViewAnswer.setBackgroundColor(Color.GREEN);
            }
            else {
                txtViewQuestion.setBackgroundColor(Color.WHITE);
                txtViewAnswer.setBackgroundColor(Color.WHITE);
            }
        }
            //Lowの時
        else if(id==R.id.button2){
            if (num < num2) {
                txtViewQuestion.setBackgroundColor(Color.GREEN);
                txtViewAnswer.setBackgroundColor(Color.GRAY);
            }
            else if (num > num2) {
                txtViewQuestion.setBackgroundColor(Color.GRAY);
                txtViewAnswer.setBackgroundColor(Color.GREEN);
            }
            else {
                txtViewQuestion.setBackgroundColor(Color.WHITE);
                txtViewAnswer.setBackgroundColor(Color.WHITE);
            }
        }

    }

    //koko
    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        String txtAnswer = txtView.getText().toString();
        //tuika
        String cleanValue2 = getString(R.string.tv_value2);
        txtView.setText(cleanValue2);
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

        // 結果を示す文字列を入れる変数を用意
        String result;
        int score;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = getString(R.string.toast_win);
                score = 2;
                blinkText(textView_question, 500, 500);
            } else if (question > answer) {
                result = getString(R.string.toast_lose);
                score = -1;
                blinkText(textView_answer, 500, 500);
            } else {
                result = getString(R.string.toast_draw);
                score = 1;
                blinkText(textView_question, 500, 500);
                blinkText(textView_answer, 500, 500);
            }
        } else {
            if (question > answer) {
                result = getString(R.string.toast_win);
                score = 2;
                blinkText(textView_question, 500, 500);
            } else if (question < answer) {
                result = getString(R.string.toast_lose);
                score = -1;
                blinkText(textView_answer, 500, 500);
            } else {
                result = getString(R.string.toast_draw);
                score = 1;
                blinkText(textView_question, 500, 500);
                blinkText(textView_answer, 500, 500);
            }
        }

        //日本語と英語表示にするための処理
        TextView tvResult = findViewById(R.id.tv_resultnum);
        //TextView txtResult = (TextView) findViewById(R.id.text_result);
        String strResult = tvResult.getText().toString();
        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        //
        tvResult.setText("");
        tvResult.setText(question + ":" + answer + "(" + result + ")");

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
        TextView tvResult =findViewById(R.id.tv_resultnum);
        TextView tvScore = findViewById(R.id.text_score);
        //TextView txtScore = (TextView) findViewById(R.id.text_score);
        //スコアを消すときは新しく作ったTextViewを消す
        tvResult.setText("");
        tvScore.setText("0");
    }

    private void saveScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        String saveScore=txtScore.getText().toString();

        SharedPreferences sp = getSharedPreferences("saveScore",MODE_PRIVATE);
        SharedPreferences.Editor loadScore = sp.edit();
        loadScore.putString("saveScore",saveScore);
        loadScore.commit();


    }
    private void setSaveScoreValue() {
        SharedPreferences score = getSharedPreferences("saveScore",MODE_PRIVATE);
        String saveScore = score.getString("saveScore","0");
        SharedPreferences.Editor loadScore = score.edit();
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText(saveScore);


    }
}

