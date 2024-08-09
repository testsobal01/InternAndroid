package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
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


        b_2();
        // 起動時に関数を呼び出す
        setQuestionValue();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Vibrator vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);

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
            vib.vibrate(100);
        } else if (id == R.id.button4) {
            b_15();
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
        Vibrator vib=(Vibrator)getSystemService(VIBRATOR_SERVICE);

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
                vib.vibrate(100);
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                vib.vibrate(1000);
            } else {
                result = "DRAW";
                score = 1;
                vib.vibrate(100);
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                vib.vibrate(100);
            } else if (question < answer) {
                result = "LOSE";
                score = -1;

                vib.vibrate(1000);}

            else {
                result = "DRAW";
                score = 1;
                vib.vibrate(100);
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

    public void b_2(){
        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    //b_2
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();

        //画面上のテキストを保存
        TextView keepScoreText = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("score_keep", keepScoreText.getText().toString());
        prefEditor.commit();
    }


    protected void onResume(){
        super.onResume();
        Toast.makeText(this, "oR", Toast.LENGTH_SHORT).show();

        TextView keepScoreText = (TextView)findViewById(R.id.text_score);
        //ここができない
        String keepScoreTextString = pref.getString("score_keep", "保存なし");
        keepScoreText.setText(keepScoreTextString);
    }

    public void b_15() {
        PopupWindow mPopupWindow = new PopupWindow(MainActivity.this);
        View popupView = getLayoutInflater().inflate(R.layout.popup_window, null);

        mPopupWindow.setContentView(popupView);

        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        mPopupWindow.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setWidth((int) width);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        mPopupWindow.showAtLocation(findViewById(R.id.button4), Gravity.CENTER, 0, 0);
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {}
            @Override
            public void onFinish() {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        }.start();
    }
}


