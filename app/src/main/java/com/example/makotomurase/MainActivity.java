package com.example.makotomurase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.hardware.camera2.CameraExtensionSession;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /**ランダムの最大値*/
    int max = 10;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    // タイマーの処理
    CountDownTimer timer = new CountDownTimer(3000,3000) {
        @Override
        public void onFinish() {
            // CUPの値をランダムに再設定
            setQuestionValue();
        }
        @Override
        public void onTick(long millisUntilFinished) {
            // 今のところ何もない
        }
    };

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

        pref=getSharedPreferences("AndroidSeminar",MODE_PRIVATE);
        prefEditor=pref.edit();
        Button settingsButton = findViewById(R.id.button4);
        settingsButton.setOnClickListener(view -> showSettingsDialog());

        // 起動時に関数を呼び出す
        setQuestionValue();
    }
    @Override


    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
            // 続けて遊べるように値を更新
            timer.start();
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            // 続けて遊べるように値を更新
            timer.start();
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            //バイブレーター
            MainActivity vibrate = new MainActivity();

            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(
                        200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(200);
            }
            // タイマーを止める
            timer.cancel();
        }

    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(R.string.num2);
        int num = 0;
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(max + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(max + 1);

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
        txtResult.setText(getString(R.string.result) + question + ":" + answer + "(" + result + ")");


        // スコアを表示
        setScore(score);
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

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this,"onPause",Toast.LENGTH_SHORT).show();

        TextView textView=(TextView) findViewById (R.id.text_score);
        int num = Integer.parseInt(textView.getText().toString());

        prefEditor.putInt("main_input",num);
        prefEditor.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("AndroidTest","onResume completed.");

        TextView textView=(TextView) findViewById(R.id.text_score);
        int num = Integer.parseInt(textView.getText().toString());

        int readText =pref.getInt("main_input",num);
        textView.setText(Integer.toString(readText));
    }
  
    private void showSettingsDialog() {
        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(50);
        new AlertDialog.Builder(this)
                .setTitle(R.string.setting_max)
                .setView(numberPicker)
                .setPositiveButton("OK", (dialog, which) -> dialog_result(numberPicker.getValue()))
                .setNegativeButton(R.string.close, null)
                .show();
    }

    private void dialog_result(int i){
        TextView textView = findViewById(R.id.setting_num);
        String i_str = String.valueOf(i);
        textView.setText(i_str);
        max = i;
    }
}

