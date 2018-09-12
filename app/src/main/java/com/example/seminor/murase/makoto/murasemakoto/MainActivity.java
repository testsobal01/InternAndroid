package com.example.seminor.murase.makoto.murasemakoto;


import android.content.Intent;

import android.content.SharedPreferences;

import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    int stopper=0;

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


        //プリファレンス
        TextView textView=(TextView)findViewById(R.id.text_score);
        pref=getSharedPreferences("saveData",MODE_PRIVATE);
        prefEditor=pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.returnstart:
                Intent intent = new Intent(this, StartActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onPause() {
        super.onPause();
        TextView textView=(TextView)findViewById(R.id.text_score);
        prefEditor.putString("score",textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textView=(TextView)findViewById(R.id.text_score);
        String readText = pref.getString("score","0");
        textView.setText(readText);
    }



    @Override
    public void onClick(View view) {
        int id = view.getId();
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vib.vibrate(1000);
          if(stopper==0){

        switch (id) {

            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                vib.vibrate(1000);//バイブレーションがなる

                break;

            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                vib.vibrate(1000);//バイブレーションがなる

                break;

            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();
                clearScoreValue();
                vib.cancel();//restartをおしたらバイブレーションがとまる
                break;
        }
          }//if
    }

    private void setQuestionValue() {
        Random r = new Random();
        if(stopper==0) {
            // 0から10の範囲で乱数を生成（+1する必要がある）
            int questionValue = r.nextInt(10 + 1);
            TextView txtView = (TextView) findViewById(R.id.question);
            txtView.setText(Integer.toString(questionValue));
        }
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
        String result="";
        int score = 0;

        // Highが押された
        if(stopper==0) {
            if (isHigh) {
                // result には結果のみを入れる
                if (question < answer) {
                    result = "WIN";
                    score = 2;
                } else if (question > answer) {
                    result = "LOOSE";
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
                    result = "LOOSE";
                    score = -1;
                } else {
                    result = "DRAW";
                    score = 1;
                }
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

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("?");
    }

    private void setNextQuestion() {
        // 第１引数がカウントダウン時間、第２引数は途中経過を受け取る間隔
        // 単位はミリ秒（1秒＝1000ミリ秒）
        if(stopper==0) {
            stopper=1;
            new CountDownTimer(3000, 1000) {

                @Override
                public void onTick(long l) {
                    // 途中経過を受け取った時に何かしたい場合
                    // 今回は特に何もしない
                }

                @Override
                public void onFinish() {
                    // 3秒経過したら次の値をセット
                    stopper=0;
                    setQuestionValue();
                    clearAnswerValue();
                }
            }.start();
        }
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
