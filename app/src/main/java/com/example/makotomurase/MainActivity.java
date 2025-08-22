package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.media.MediaPlayer;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    private SoundPlayer soundPlayer;
    private MediaPlayer mediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPlayer = new SoundPlayer(this);

        playBGM2Sound();



        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        pref = getSharedPreferences("team-e", MODE_PRIVATE);
        prefEditor = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();


    }

    private void playBGM2Sound() {
        mediaPlayer = mediaPlayer.create(this, R.raw.bgm2);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        TextView textView = (TextView)findViewById(R.id.text_score);
        prefEditor.putString("main_input",textView.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView textView = (TextView)findViewById(R.id.text_score);
        String readText = pref.getString("main_input","0");
        textView.setText(readText);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        TextView questionTxtView = findViewById(R.id.question);
        if (id == R.id.button1) {
            setAnswerValue();
            checkResult(true);
            //ボタンを押したら0.1秒間バイブする
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(100);

            soundPlayer.playHitSound();

        } else if (id == R.id.button2) {
            setAnswerValue();
            checkResult(false);
            //ボタンを押したら0.1秒間バイブする
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(100);

            soundPlayer.playHitSound();

        } else if (id == R.id.button3) {
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            //ボタンを押したら0.3秒間バイブする
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(300);

            soundPlayer.playOverSound();

            questionTxtView.setBackgroundResource(R.color.white);
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(R.string.answer);
        txtView.setBackgroundResource(R.color.white);
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
        ImageView imageView = findViewById(R.id.my_image_view);

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

        if(result == "WIN"){
            txtViewQuestion.setBackgroundResource(R.color.BrightBlue);
            txtViewAnswer.setBackgroundResource(R.color.BrightRed);
            imageView.setImageResource(R.drawable.trump);
            // win_animation.xmlで定義した勝利時のアニメーションを読み込む
            Animation winAnimation = AnimationUtils.loadAnimation(this, R.anim.win_animation);
            // 勝利時の演出として、txtViewQuestionとtxtViewAnswerにアニメーションを適用
            txtViewQuestion.startAnimation(winAnimation);
            txtViewAnswer.startAnimation(winAnimation);
        }else if(result == "LOSE"){
            txtViewQuestion.setBackgroundResource(R.color.BrightRed);
            txtViewAnswer.setBackgroundResource(R.color.BrightBlue);
            imageView.setImageResource(R.drawable.trumpsad);
            // lose_animation.xmlで定義した勝利時のアニメーションを読み込む
            Animation loseAnimation = AnimationUtils.loadAnimation(this, R.anim.lose_animation);
            // 敗北時の演出として、txtViewQuestionとtxtViewAnswerにアニメーションを適用
            txtViewQuestion.startAnimation(loseAnimation);
            txtViewAnswer.startAnimation(loseAnimation);
        }else{
            txtViewQuestion.setBackgroundResource(R.color.BrightGreen);
            txtViewAnswer.setBackgroundResource(R.color.BrightGreen);
            imageView.setImageResource(R.drawable.ozisan);
            // draw_animation.xmlで定義した勝利時のアニメーションを読み込む
            Animation drawAnimation = AnimationUtils.loadAnimation(this, R.anim.draw_animation);
            // 引き分け時の演出として、txtViewQuestionとtxtViewAnswerにアニメーションを適用
            txtViewQuestion.startAnimation(drawAnimation);
            txtViewAnswer.startAnimation(drawAnimation);
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
        if(newScore > 0){
            txtScore.setTextColor(getResources().getColor(R.color.BrightRed));
        }else if(newScore < 0){
            txtScore.setTextColor(getResources().getColor(R.color.BrightBlue));
        }else{
            txtScore.setTextColor(getResources().getColor(R.color.BrightGreen));
        }
        txtScore.setText(Integer.toString(newScore));
    }

    private void clearScoreValue() {
        TextView txtScore = (TextView) findViewById(R.id.text_score);
        txtScore.setText("0");
    }
}

