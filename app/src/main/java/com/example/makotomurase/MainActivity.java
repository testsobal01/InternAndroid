package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private TextView t1;

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

        pref = getSharedPreferences("ScoreBord", MODE_PRIVATE);
        prefEditor=pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();//<-乱数を発生されるメソッド

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                Vibrator vib1 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib1.vibrate(300);
                setAnswerValue();
                checkResult(true);
                Animation_winner();
                break;
            case R.id.button2:
                Vibrator vib2 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib2.vibrate(300);
                setAnswerValue();
                checkResult(false);
                break;
            case R.id.button3:
                Vibrator vib3 = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                vib3.vibrate(300);
                setQuestionValue();
                clearAnswerValue();
                break;

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
        TextView txtView = (TextView) findViewById(R.id.question);
        //画面右側のテキスト
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
                score = 2;
                t1=findViewById(R.id.question);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                t1=findViewById(R.id.answer);
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                t1=findViewById(R.id.question);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                t1=findViewById(R.id.answer);
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
        //scoreboard();
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

   protected void onResume(){
        super.onResume();
      TextView text=(TextView)findViewById(R.id.text_score);

      String readText= pref.getString("anum","保存されていない");
      text.setText(readText);

   }

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }

    protected void onPause(){
        super.onPause();
        //Toast.makeText(this,"",)
        TextView textView=(TextView)findViewById(R.id.text_score);

        prefEditor.putString("anum",textView.getText().toString());
        prefEditor.commit();
    }
    
    public void Animation_winner(){
        RotateAnimation rotate_win=new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotate_win.setDuration(300);
        rotate_win.setRepeatCount(5);
        rotate_win.setFillAfter(true);
        t1.startAnimation(rotate_win);
    }

}