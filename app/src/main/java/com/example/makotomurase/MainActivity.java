package com.example.makotomurase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//追記バイブ
import android.os.Vibrator;
import android.os.VibrationEffect;

//追記アニメーション
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.animation.OvershootInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    Boolean isDoingAnimation = false;

    int ren = 0;
    int sairen = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        pref = getSharedPreferences("files", MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);

            //追記バイブ
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(200);
                }
            }
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);

            //追記バイブ
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(200);
                }
            }
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
        }
    }

    @Override

    protected void onPause(){
        super.onPause();
        Toast.makeText(this,"onPause", Toast.LENGTH_SHORT).show();
        TextView textView = (TextView)findViewById(R.id.text_score);

        prefEditor.putString("main_input",textView.getText().toString());

        prefEditor.commit();
    }

    @Override

    protected void onResume(){
        super.onResume();
        Toast.makeText(this,"onResume", Toast.LENGTH_SHORT).show();
        TextView textView = (TextView)findViewById(R.id.text_score);

        String readText = pref.getString("main_input", "保存されていません");
        textView.setText(readText);
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
        int score = 0;
        String color;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                ren += 1;
                TextView rentext = findViewById(R.id.ren);
                rentext.setText("現在の連勝記録：" + ren);
                if(ren > sairen){
                    sairen = ren ;
                }
                txtResult.setBackgroundColor(Color.rgb(242,83,194));
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                ren = 0;
                TextView rentext = findViewById(R.id.ren);
                rentext.setText("現在の連勝記録：0");
                txtResult.setBackgroundColor(Color.rgb(110,108,210));
            } else {
                result = "DRAW";
                score = 1;
                ren = 0;
                TextView rentext = findViewById(R.id.ren);
                rentext.setText("現在の連勝記録：0");
                txtResult.setBackgroundColor(Color.rgb(78,220,220));
            }
            TextView rentext = findViewById(R.id.sairen);
            rentext.setText("最高連勝記録：" + sairen);
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                ren += 1;
                TextView rentext = findViewById(R.id.ren);
                rentext.setText("現在の連勝記録：" + ren);
                if(ren > sairen){
                    sairen = ren ;
                }
                txtResult.setBackgroundColor(Color.rgb(242,83,194));
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                ren = 0;
                TextView rentext = findViewById(R.id.ren);
                rentext.setText("現在の連勝記録：0");
                txtResult.setBackgroundColor(Color.rgb(110,108,210));
            } else {
                result = "DRAW";
                score = 1;
                ren = 0;
                TextView rentext = findViewById(R.id.ren);
                rentext.setText("現在の連勝記録：0");
                txtResult.setBackgroundColor(Color.rgb(78,220,220));
            }
            TextView rentext = findViewById(R.id.sairen);
            rentext.setText("最高連勝記録：" + sairen);
        }

        //追記アニメーション
        if ("WIN".equals(result)) {
            animateTextJump(txtViewAnswer);   // 勝ち→ジャンプ（文字サイズだけ）
        } else if ("LOSE".equals(result)) {
            animateTextShake(txtViewAnswer);  // 負け→シェイク（文字の横幅だけ）
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        String resultText = getString(R.string.text_result);
        txtResult.setText(resultText+"：" + question + ":" + answer + "(" + result + ")");


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
        String check = txtScore.getText().toString();
        int newscore =score;
        if(check == "保存されていません"){
            newscore = score;
            System.out.println("aaa" + newscore);
        }
        else{
            newscore = Integer.parseInt(txtScore.getText().toString()) + score;
            System.out.println("bbb" + newscore);

        }
        txtScore.setText(Integer.toString(newscore));
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }

    //追記アニメーション
    private void resetTextOnlyAnim(TextView tv) {
        tv.animate().cancel();
        tv.setTextScaleX(1f); // 横伸縮だけを元に戻す
    }

    //勝ち：文字サイズだけ“大きく→戻す”
    private void animateTextJump(TextView tv) {
        resetTextOnlyAnim(tv);
        if(isDoingAnimation){
            return;
        }
        final float startPx = tv.getTextSize();
        final float peakPx  = startPx * 1.6f;

        ValueAnimator va = ValueAnimator.ofFloat(startPx, peakPx, startPx);
        va.setDuration(1000);
        va.setInterpolator(new OvershootInterpolator());
        va.addUpdateListener(anim ->
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) anim.getAnimatedValue())
        );
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isDoingAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isDoingAnimation = false;

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isDoingAnimation = false;

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        va.start();
    }

    //負け：文字の横幅だけ“ガタガタ→戻す”
    private void animateTextShake(TextView tv) {
        resetTextOnlyAnim(tv);
        if(isDoingAnimation){
            return;
        }
        ValueAnimator va = ValueAnimator.ofFloat(1.0f, 0.7f, 1.3f, 0.8f, 1.2f, 0.9f, 1.1f, 1.0f);
        va.setDuration(1000);
        va.setInterpolator(new LinearInterpolator());
        va.addUpdateListener(anim -> tv.setTextScaleX((float) anim.getAnimatedValue()));
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isDoingAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isDoingAnimation = false;

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isDoingAnimation = false;

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        va.start();
    }
}


