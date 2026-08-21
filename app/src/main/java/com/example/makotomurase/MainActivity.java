package com.example.makotomurase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.app.Activity;
import android.app.AlertDialog;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.app.Dialog;
import android.hardware.camera2.CameraExtensionSession;
import android.view.LayoutInflater;
import android.content.SharedPreferences;
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
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


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

    SoundPool soundPool;
    int mp3a;
    int mp3b;
    int mp3c;
    int mp3d;
    int mp3e;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            soundPool=new SoundPool(5,AudioManager.STREAM_MUSIC, 0);
        }else{
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(attr)
                    .setMaxStreams(5)
                    .build();
            mp3a=soundPool.load(this,R.raw.rappa,1);
            mp3b=soundPool.load(this,R.raw.hit,1);
            mp3c=soundPool.load(this,R.raw.win,1);
            mp3d=soundPool.load(this,R.raw.lose,1);
            mp3e=soundPool.load(this,R.raw.draw,1);

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

        pref = getSharedPreferences("AndroidSeminar", MODE_PRIVATE);
        prefEditor = pref.edit();
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
            YoYo.with(Techniques.Tada).duration(1000).repeat(1).playOn(findViewById(R.id.button1));
            // 続けて遊べるように値を更新
            timer.start();
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            YoYo.with(Techniques.Tada).duration(1000).repeat(1).playOn(findViewById(R.id.button2));
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
                soundPool.play(mp3c,1f,1f,0,0,1f);
                YoYo.with(Techniques.Tada).duration(1000).repeat(1).playOn(findViewById(R.id.answer));
              
                findViewById(R.id.answer).setBackgroundColor(Color.parseColor("#FF0000"));
                findViewById(R.id.question).setBackgroundColor(Color.parseColor("FF0000"));
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                soundPool.play(mp3d,1f,1f,0,0,1f);
                YoYo.with(Techniques.Tada).duration(1000).repeat(1).playOn(findViewById(R.id.question));
                findViewById(R.id.question).setBackgroundColor(Color.parseColor("#00BFFF"));
                findViewById(R.id.answer).setBackgroundColor(Color.parseColor("#00BFFF"));
            } else {
                result = "DRAW";
                score = 1;
                soundPool.play(mp3e,1f,1f,0,0,1f);
                YoYo.with(Techniques.Tada).duration(1000).repeat(2).playOn(findViewById(R.id.answer));
                YoYo.with(Techniques.Tada).duration(1000).repeat(2).playOn(findViewById(R.id.question));
                findViewById(R.id.question).setBackgroundColor(Color.parseColor("#FF8C00"));
                findViewById(R.id.answer).setBackgroundColor(Color.parseColor("#FF8C00"));
            }

        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                soundPool.play(mp3c,1f,1f,0,0,1f);
                YoYo.with(Techniques.Tada).duration(1000).repeat(1).playOn(findViewById(R.id.answer));
                findViewById(R.id.answer).setBackgroundColor(Color.parseColor("#FF0000"));
                findViewById(R.id.question).setBackgroundColor(Color.parseColor("#FF0000"));
            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                soundPool.play(mp3d,1f,1f,0,0,1f);
                YoYo.with(Techniques.Tada).duration(1000).repeat(1).playOn(findViewById(R.id.question));
                findViewById(R.id.question).setBackgroundColor(Color.parseColor("#00BFFF"));
                findViewById(R.id.answer).setBackgroundColor(Color.parseColor("#00BFFF"));
            } else {
                result = "DRAW";
                score = 1;
                soundPool.play(mp3e,1f,1f,0,0,1f);
                YoYo.with(Techniques.Tada).duration(1000).repeat(2).playOn(findViewById(R.id.answer));
                YoYo.with(Techniques.Tada).duration(1000).repeat(2).playOn(findViewById(R.id.question));
                findViewById(R.id.question).setBackgroundColor(Color.parseColor("#FF8C00"));
                findViewById(R.id.answer).setBackgroundColor(Color.parseColor("#FF8C00"));
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        txtResult.setText(getString(R.string.result) + question + ":" + answer + "(" + result + ")");


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


