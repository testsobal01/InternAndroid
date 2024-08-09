package com.example.makotomurase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    public int numberpickervalue=10;

    AnimatorSet set1;
    AnimatorSet set2;

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }
  
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        LayoutInflater inflater = getLayoutInflater();
        android.view.View dialogView = inflater.inflate(R.layout.dialog, null);
        NumberPicker numberPicker = dialogView.findViewById(R.id.number_picker);
        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(50);
        numberPicker.setValue(10);
        new AlertDialog.Builder(this)
                .setTitle("最大値を設定してください")
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        TextView settt = (TextView) findViewById(R.id.settt);
                        settt.setText(numberPicker.getValue()+"が設定されています。");
                        numberpickervalue = numberPicker.getValue();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

        return true;
    }

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

        pref = getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        prefEditor = pref.edit();
        // 起動時に関数を呼び出す
        setQuestionValue();
        //↓animation設定
        set1 = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                R.animator.team_e_animation);
        set2 = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this,
                R.animator.team_e_animation);
        set1.setTarget(btn1);
        set2.setTarget(btn2);
    }
    @Override//アニメーション実行
    protected void onStart() {
        super.onStart();
        //アニメーション開始
        set1.start();
        set2.start();
    }
    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();
        TextView textView = findViewById(R.id.text_score);
        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();
        TextView textView = findViewById(R.id.text_score);
        String readText = pref.getString("main_input","保存されていません");
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
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(numberpickervalue + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(numberpickervalue + 1);

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

                vibration();
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

                vibration();
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

    private void vibration(){

        Vibrator vib = null;
        vib = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        vib.vibrate(5000);

    }

}

