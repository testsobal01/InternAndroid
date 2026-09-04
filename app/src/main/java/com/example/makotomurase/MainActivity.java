package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.graphics.Color;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.media.SoundPool;

import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;



import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import android.animation.Animator;


import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    int selectedScore = 10;

    private MediaPlayer mediaPlayer;
    private SoundPool soundPool1;
    private int soundId1;
    private SoundPool soundPool2;
    private int soundId2;
    private SoundPool soundPool3;
    private int soundId3;
    private SoundPool soundPool4;
    private int soundId4;
    private SoundPool soundPool5;
    private int soundId5;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Boolean doubleFlag = false;

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

        //select game score, add default value
        Spinner spinner = findViewById(R.id.selectscore);
        spinner.setSelection(0);

        // リスナーをセット
        spinner.setOnItemSelectedListener(this);

        Button btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(this);

        Button btn5 = findViewById(R.id.button5);
        btn5.setOnClickListener(this);

        // 起動時に関数を呼び出す
        setQuestionValue();

        // SoundPoolの初期化
         soundPool1 = new SoundPool.Builder()
                 .setMaxStreams(2)
                 .build();
        soundPool2 = new SoundPool.Builder()
                .setMaxStreams(2)
                .build();
        soundPool3 = new SoundPool.Builder()
                .setMaxStreams(2)
                .build();
        soundPool4 = new SoundPool.Builder()
                .setMaxStreams(2)
                .build();

        soundPool5 = new SoundPool.Builder()
                .setMaxStreams(2)
                .build();
        // サウンドファイルの読み込み
        soundId1 = soundPool1.load(this, R.raw.one, 1);
        soundId2 = soundPool2.load(this, R.raw.win, 1);
        soundId3 = soundPool3.load(this, R.raw.lose, 1);
        soundId4 = soundPool4.load(this, R.raw.draw, 1);
        soundId5 = soundPool5.load(this, R.raw.restart, 1);

        mediaPlayer = MediaPlayer.create(this, R.raw.morning);
        if(mediaPlayer !=null){
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        // get score from preferences
        sharedPreferences = getSharedPreferences("SCORE", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        TextView txtscore = findViewById(R.id.text_score);
        int savedscore = sharedPreferences.getInt("score",0);
        txtscore.setText(Integer.toString(savedscore));
    }



    private void showSettingDialog() {
        String[] settingItems = {
                getString(R.string.action_setting1),
                getString(R.string.action_setting2),
                getString(R.string.action_setting3)
        };

        new AlertDialog.Builder(this)
                .setTitle(R.string.setting)
                .setItems(settingItems,null)
                .setNegativeButton(R.string.close,null)
                .show();
    }

    //select game score
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = findViewById(R.id.selectscore);
        String selectedValue = spinner.getSelectedItem().toString();
        selectedScore = Integer.parseInt(selectedValue);
        setQuestionValue();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {


        if (view.getId() == R.id.button3) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VibrationEffect.createOneShot(
                    1000, VibrationEffect.DEFAULT_AMPLITUDE));

            soundPool5.play(soundId5, 1.0f, 1.0f, 0, 0, 1.0f);

        }

        int id = view.getId();
        if (id == R.id.button1) {
            soundPool1.play(soundId1, 1.0f, 1.0f, 0, 0, 1.0f);
            setAnswerValue();
            checkResult(true);

        } else if (id == R.id.button2) {
            soundPool1.play(soundId1, 1.0f, 1.0f, 0, 0, 1.0f);
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            soundPool1.play(soundId1, 1.0f, 1.0f, 0, 0, 1.0f);
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            //リスタート時背景色を元に戻す
            View background = findViewById(R.id.question);
            background.setBackgroundColor(Color.parseColor("#ff00ff"));
            View bg = findViewById(R.id.answer);
            bg.setBackgroundColor(Color.parseColor("#ffff00"));
        }else if (id == R.id.button4) {//TOPへ戻る
            //TOP画面への遷移の処理
            Intent subIntent = new Intent(getApplication(), StartActivity.class);
            startActivity(subIntent);
        }
        if (id == R.id.button5) {
            Toast.makeText(getApplicationContext(), "次の得点が２倍!", Toast.LENGTH_SHORT).show();
            // フラグをTrueにする
            doubleFlag = true;
            //１度だけの実行
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //save score
        TextView txtscore = findViewById(R.id.text_score);
        int savedscore = Integer.parseInt(txtscore.getText().toString());
        editor.putInt("score",savedscore);
        editor.commit();
        if(mediaPlayer !=null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Test","onResume completed");
        if(mediaPlayer !=null){
            mediaPlayer.start();
        }

    }

    @Override
    public  void onDestroy(){
        super.onDestroy();
        Log.d("Test","onDestroy completed");
        mediaPlayer.stop();
        mediaPlayer.release(); // メモリ解放（必須）
        mediaPlayer = null;

    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt( selectedScore+ 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt( selectedScore+ 1);

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
                score = 1;
              
                soundPool2.play(soundId2, 1.0f, 1.0f, 0, 0, 1.0f);

                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(
                        500, VibrationEffect.DEFAULT_AMPLITUDE));;

                //勝ったときに背景色変更
                View background = findViewById(R.id.question);
                background.setBackgroundColor(Color.parseColor("#2196F3"));
                View bg = findViewById(R.id.answer);
                bg.setBackgroundColor(Color.parseColor("#F57C00"));
              
                txtViewAnswer.setRotation(0f);
                txtViewAnswer.animate().rotation(360f).setDuration(600).start();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;

                soundPool3.play(soundId3, 1.0f, 1.0f, 0, 0, 1.0f);

                //負けた時に背景色変更
                View background = findViewById(R.id.question);
                background.setBackgroundColor(Color.parseColor("#F57C00"));
                View bg = findViewById(R.id.answer);
                bg.setBackgroundColor(Color.parseColor("#2196F3"));
              
                txtViewQuestion.setRotation(0f);
                txtViewQuestion.animate().rotation(360f).setDuration(600).start();
            } else {
                result = "DRAW";
                score = 0;

                soundPool4.play(soundId4, 1.0f, 1.0f, 0, 0, 1.0f);

                //引き分け時に背景色変更
                View background = findViewById(R.id.question);
                background.setBackgroundColor(Color.parseColor("#9CCC65"));
                View bg = findViewById(R.id.answer);
                bg.setBackgroundColor(Color.parseColor("#9CCC65"));
              
                txtViewQuestion.setRotation(0f);
                txtViewAnswer.setRotation(0f);
                txtViewQuestion.animate().rotation(360f).setDuration(600).start();
                txtViewAnswer.animate().rotation(360f).setDuration(600).start();
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 1;

                soundPool2.play(soundId2, 1.0f, 1.0f, 0, 0, 1.0f);

                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(VibrationEffect.createOneShot(
                        500, VibrationEffect.DEFAULT_AMPLITUDE));;

                //勝った時に背景色変更
                View background = findViewById(R.id.question);
                background.setBackgroundColor(Color.parseColor("#2196F3"));
                View bg = findViewById(R.id.answer);
                bg.setBackgroundColor(Color.parseColor("#F57C00"));
              
                txtViewAnswer.setRotation(0f);
                txtViewAnswer.animate().rotation(360f).setDuration(600).start();
            } else if (question < answer) {
                result = "LOSE";
                score = -1;

                soundPool3.play(soundId3, 1.0f, 1.0f, 0, 0, 1.0f);

                //負けた時に背景色変更
                View background = findViewById(R.id.question);
                background.setBackgroundColor(Color.parseColor("#F57C00"));
                View bg = findViewById(R.id.answer);
                bg.setBackgroundColor(Color.parseColor("#2196F3"));
              
                txtViewQuestion.setRotation(0f);
                txtViewQuestion.animate().rotation(360f).setDuration(600).start();
            } else {
                result = "DRAW";
                score = 0;

                soundPool4.play(soundId4, 1.0f, 1.0f, 0, 0, 1.0f);

                //引き分け時に背景色変更
                View background = findViewById(R.id.question);
                background.setBackgroundColor(Color.parseColor("#9CCC65"));
                View bg = findViewById(R.id.answer);
                bg.setBackgroundColor(Color.parseColor("#9CCC65"));
              
                txtViewQuestion.setRotation(0f);
                txtViewAnswer.setRotation(0f);
                txtViewQuestion.animate().rotation(360f).setDuration(600).start();
                txtViewAnswer.animate().rotation(360f).setDuration(600).start();
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
        // フラグがTrueだったら2倍にする
        if (doubleFlag == true) {
            score *= 2;
            doubleFlag = false;
        } else {
           score *= 1;
        }
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));

        //特定のnewScore点数を取得したら勝ち負けの状態遷移
        if(newScore >= 3){ //WIN画面へ
            //画面遷移の処理
            Intent winintent = new Intent(this, WinActivity.class);
            startActivity(winintent);
        }else if(newScore <= -3){ //LOSE画面へ
            //画面遷移の処理
            Intent loseintent = new Intent(this, LoseActivity.class);
            startActivity(loseintent);
        }
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }

}

