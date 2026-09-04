package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.app.ActivityManager;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;
    CountDownTimer nextQuestionTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        String intentString = "";
        if (extra != null) {
            intentString = extra.getString("TOP");
        }

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

        Button btn4 = findViewById(R.id.button4);
        if (btn4 != null) {
            btn4.setOnClickListener(this);
        }

        pref = getSharedPreferences("Save", MODE_PRIVATE);
        prefEditor = pref.edit();

        setQuestionValue();
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
        } else if (id == R.id.button4) {
            if (nextQuestionTimer != null) {
                nextQuestionTimer.cancel();
            }

            TextView txtScore = findViewById(R.id.text_score);
            int finalScore = Integer.parseInt(txtScore.getText().toString());

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("FINAL_SCORE", finalScore);
            startActivity(intent);
            finish();
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
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

    public int generateRandomWarmColor() {
        Random random = new Random();
        int red = random.nextInt(106) + 150;
        int green = random.nextInt(151) + 50;
        int blue = random.nextInt(81);
        return Color.rgb(red, green, blue);
    }

    public int changeWarmColor() {
        Random random = new Random();
        int red3 = random.nextInt(81);
        int green3 = random.nextInt(151) + 50;
        int blue3 = random.nextInt(106) + 150;
        return Color.rgb(red3, green3, blue3);
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);

        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());

        TextView txtResult = (TextView) findViewById(R.id.text_result);

        String result;
        int score;

        if (isHigh) {
            if (question < answer) {
                result = "WIN";
                score = 2;
                int warmColor = generateRandomWarmColor();
                txtViewAnswer.setBackgroundColor(warmColor);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                int warmColor = changeWarmColor();
                txtViewQuestion.setBackgroundColor(warmColor);
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                int warmColor = generateRandomWarmColor();
                txtViewAnswer.setBackgroundColor(warmColor);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                int warmColor = changeWarmColor();
                txtViewQuestion.setBackgroundColor(warmColor);
            } else {
                result = "DRAW";
                score = 1;
            }
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");

        setNextQuestion();
        setScore(score);
    }

    private void setNextQuestion() {
        if (nextQuestionTimer != null) {
            nextQuestionTimer.cancel();
        }

        nextQuestionTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {
                // 途中経過（何もしない）
            }

            @Override
            public void onFinish() {
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

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("score_input", textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("AndroidTest","onResume completed");
        TextView textView = (TextView)findViewById(R.id.text_score);
        String readText = pref.getString("score_input", "0");
        textView.setText(readText);
    }
}