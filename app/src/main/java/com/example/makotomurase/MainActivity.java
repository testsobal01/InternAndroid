package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.Color;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

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




        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();

        Button settingButton=findViewById(R.id.button_settings);
        settingButton.setOnClickListener(view -> showSettingsDialog());
    }

    private void showSettingsDialog() {
        String[] settingItems = {
                getString(R.string. action_settings),
                getString(R.string. action_settings2),
                getString(R.string.action_settings3)

        };
        new AlertDialog.Builder(this)
                .setTitle(R.string.settings)
                .setItems(settingItems,null)
                .setNegativeButton(R.string.close,null)
                .show();
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
            draw_resetResultAnime();
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("?");
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
        Vibrator VIB = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        // Highが押され
            if (isHigh) {

                // result には結果のみを入れる
                if (question < answer) {
                    result = "WIN";
                    score = 2;
                    VIB.vibrate(VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE));
                    txtResult.setBackgroundColor(Color.RED);
                    winResultAnime();

                } else if (question > answer) {
                    result = "LOSE";
                    score = -1;
                    VIB.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                    txtResult.setBackgroundColor(Color.BLUE);
                    loseResultAnime();

                } else {
                    result = "DRAW";
                    score = 1;
                    VIB.vibrate(VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE));
                    txtResult.setBackgroundColor(Color.GREEN);
                    draw_resetResultAnime();

                }
            } else {
                if (question > answer) {
                    result = "WIN";
                    score = 2;
                    VIB.vibrate(VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE));
                    txtResult.setBackgroundColor(Color.RED);
                    winResultAnime();
                } else if (question < answer) {
                    result = "LOSE";
                    score = -1;
                    VIB.vibrate(VibrationEffect.createOneShot(500,VibrationEffect.DEFAULT_AMPLITUDE));
                    txtResult.setBackgroundColor(Color.BLUE);
                    loseResultAnime();
                } else {
                    result = "DRAW";
                    score = 1;
                    VIB.vibrate(VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE));
                    txtResult.setBackgroundColor(Color.GREEN);
                    draw_resetResultAnime();
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

    private void winResultAnime(){
        TextView Que = findViewById(R.id.question);
        TextView Ans = findViewById(R.id.answer);
        Que.animate()
                .translationY(500f)
                .alpha(0.7f)
                .setDuration(1000)
                .start();
        Ans.animate()
                .alpha(1.0f)
                .translationY(-500f)
                .setDuration(1000)
                .start();
    }
    private void loseResultAnime(){
        TextView Que = findViewById(R.id.question);
        TextView Ans = findViewById(R.id.answer);
        Que.animate()
                .alpha(1.0f)
                .translationY(-500f)
                .setDuration(1000)
                .start();
        Ans.animate()
                .translationY(500f)
                .alpha(0.7f)
                .setDuration(1000)
                .start();
    }
    private void draw_resetResultAnime(){
        TextView Que = findViewById(R.id.question);
        TextView Ans = findViewById(R.id.answer);
        Que.animate()
                .translationY(0f)
                .alpha(1.0f)
                .setDuration(1000)
                .start();
        Ans.animate()
                .translationY(0f)
                .alpha(1.0f)
                .setDuration(1000)
                .start();
    }

    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.text_score);
        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();
    }

    protected void onResume() {
        super.onResume();
        Log.d("AndroidTest","onResume completed.");

        TextView textView = (TextView)findViewById(R.id.text_score);

        String readText = pref.getString("main_input", "保存されていません");
        textView.setText(readText);
    }
}

