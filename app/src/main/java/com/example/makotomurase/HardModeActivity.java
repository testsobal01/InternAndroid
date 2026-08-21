package com.example.makotomurase;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.os.Build;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageView;

public class HardModeActivity extends AppCompatActivity implements View.OnClickListener {
    int wincount;//勝利回数
    int losecount;//敗北回数
    int drawcount;//引き分け回数
    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private MediaPlayer mediaPlayer;

    SoundPool soundPool;
    int[] soundIds = new int[2];
    int[] seFiles = {R.raw.button01a, R.raw.button01b};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mode1), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                            | WindowInsetsCompat.Type.displayCutout());
            view.setPadding(insets.left, insets.top, insets.right, 0);
            return windowInsets;
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        soundPool = new SoundPool(soundIds.length, AudioManager.STREAM_MUSIC, 0);
        for (int i = 0; i < soundIds.length; i++) {
            soundIds[i] = soundPool.load(this, seFiles[i], 1);
        }


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
        showDialog();

        EdgeToEdge.enable(this);
        ImageView imageView2 = findViewById(R.id.image_view_2);
        imageView2.setImageResource(R.drawable.img_2);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
            soundPool.play(soundIds[0], 1.0F, 1.0F, 0, 0, 1.0F);
        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            soundPool.play(soundIds[0], 1.0F, 1.0F, 0, 0, 1.0F);
        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            clearWinRateValue();
            soundPool.play(soundIds[1], 1.0F, 1.0F, 0, 0, 1.0F);
            VibrationB();
        }

    }
    private void VibrationB() {
        Vibrator vibrator = (Vibrator)  getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(
                    500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }


    private void GameOver(int maxscore) {
        TextView Score = new TextView(this);

        new AlertDialog.Builder(this)
            .setTitle("敗北！")
            .setMessage(String.format("スコア",maxscore))
            .setPositiveButton("OK",(dialog,which)->{
                Intent intent1 = new Intent(this, HomeActivity.class);
                startActivity(intent1);
            })
            .setNegativeButton("continue",(dialog,which)->{
                setQuestionValue();
                clearAnswerValue();
                clearScoreValue();
                clearWinRateValue();
            })

            .show();
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

    private void showDialog(){
        new AlertDialog.Builder(this)
            .setTitle("ルール説明")
            .setMessage("負けたら終了だよ")
            .setPositiveButton("OK",(dialog,which)->{

            })

            .show();
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);

        TextView txtView = findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        final View BackGroud = findViewById(R.id.main);

        //言語識別用の変数
        Locale locale = Locale.getDefault();
        String lang  = String.valueOf(locale);

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
                wincount += 1;
                VibrationB();
            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                losecount += 1;
                int ts = wincount*2 + drawcount;
                GameOver(ts);

            } else {
                result = "DRAW";
                score = 1;
                drawcount += 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                wincount += 1;
                VibrationB();

            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                losecount += 1;
                int ts = wincount*2 + drawcount;
                GameOver(ts);

            } else {
                result = "DRAW";
                score = 1;
                drawcount += 1;
            }

        }




        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        //端末が英語の場合と日本語の場合の分岐
        if (lang.equals("ja_JP")){
            txtResult.setText("結果：" + question + ":" + answer + "(" + result + ")");
        }else if(lang.equals("en_US")){
            txtResult.setText("Result：" + question + ":" + answer + "(" + result + ")");
        }
        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
        //勝率を表示
        setWinrate(wincount,losecount,drawcount);

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
                // テキストのフェードアウト処理を追加
                // 3秒経過したら次の値をセット
                setQuestionValue();
                // テキストのフェードイン処理を追加
            }
        }.start();
    }

    private void setScore(int score) {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        int newScore = Integer.parseInt(txtScore.getText().toString()) + score;
        txtScore.setText(Integer.toString(newScore));
    }
    private void setWinrate(int win,int lose,int draw){
        TextView winScore = (TextView) findViewById(R.id.win_result);
        double winrate = (double) win/(win+lose+draw) *100;
        winScore.setText(String.format("%.3f",winrate));
    }
    private void clearWinRateValue(){
        TextView winScore = (TextView) findViewById(R.id.win_result);
        wincount = 0;
        losecount = 0;
        drawcount = 0;
        winScore.setText("");
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }

    private void fadein() {
        TextView textView = findViewById(R.id.question);
        textView.setAlpha(0f);
        textView.setVisibility(View.VISIBLE);
        textView.animate()
                .alpha(1f)
                .setDuration(3000)
                .setListener(null);
    }

    private void fadeout() {
        TextView textView = findViewById(R.id.question);
        textView.animate()
                .alpha(0f)
                .setDuration(3000)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        textView.setVisibility(View.GONE);
                    }
                })
                .start();
    }

}

