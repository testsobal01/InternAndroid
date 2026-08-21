package com.example.makotomurase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.os.VibratorManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //効果音
    public SoundPool soundPool;
    public int[] action = { 0,0,0,0 };
    public int streak = 0;

    public int max;

    SharedPreferences pref;
    SharedPreferences.Editor prefEditor;

    AnimatorSet rightset;
    AnimatorSet leftset;

    public int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 効果音
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(3)
                .build();
        action[0] = soundPool.load(this, R.raw.button01, 1);
        action[1] = soundPool.load(this, R.raw.button02, 1);
        action[2] = soundPool.load(this, R.raw.win01, 1);
        action[3] = soundPool.load(this, R.raw.lose01, 1);

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

        ImageButton option = (ImageButton) findViewById(R.id.option);
        option.setOnClickListener(this);

        Button retry = (Button) findViewById(R.id.retry);
        retry.setOnClickListener(this);

        setScore(0);

        max = 10;
        count=0;
        // 起動時に関数を呼び出す
        setQuestionValue();

        pref = getSharedPreferences("Score", MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TextView textview = (TextView) findViewById(R.id.text_score);
        TextView textview2 = (TextView) findViewById(R.id.text_streak);

        prefEditor.putString("score_input", textview.getText().toString());
        prefEditor.putString("streak_input", textview2.getText().toString());
        prefEditor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView textview = (TextView) findViewById(R.id.text_score);
        TextView textview2 = (TextView) findViewById(R.id.text_streak);

        String readText = pref.getString("score_input","0");
        String readText2 = pref.getString("streak_input", "0");

        textview.setText(readText);
        textview2.setText(readText2);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        TextView txtViewQuestion = findViewById(R.id.question);
        TextView txtViewAnswer = findViewById(R.id.answer);
        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button retry = findViewById(R.id.retry);
        Log.d("COUNT",String.valueOf(count)+"現在のカウント");
        //効果音
        soundPool.play(action[1], 10f , 1f, 0, 0, 1f);

        if (id == R.id.button1) {
            count += 1;
            setAnswerValue();
            checkResult(true);
        } else if (id == R.id.button2) {
            count += 1;
            setAnswerValue();
            checkResult(false);
        } else if (id == R.id.button3) {
            count = 0;
            setQuestionValue();
            clearAnswerValue();
            clearScoreValue();
            clearStreakValue();
        }else if (id == R.id.option){
            NumberPicker np = new NumberPicker(this);
            np.setMinValue(10);
            np.setMaxValue(50);

            new AlertDialog.Builder(this)
                    .setView(np)
                    .setTitle(R.string.numtitle)
                    .setPositiveButton(R.string.ok, (dialog,which)->{
                        TextView maxvalue = findViewById(R.id.maxvalue);
                        txtViewQuestion.setText(String.valueOf(np.getValue()));
                        maxvalue.setText(np.getValue()+"が設定されています");
                        max = np.getValue();
                    })
                    .setNegativeButton(R.string.canncel, (Dialog, which)->{})
                    .show();
        }else if (id == R.id.retry){
            btn1.setEnabled(true);
            btn2.setEnabled(true);
            //アニメーション
            clearScoreValue();
            AnimatorSet rightset = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rightretry_animation);
            rightset.setTarget(txtViewAnswer);
            rightset.start();
            AnimatorSet leftset = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.leftretry_animation);
            leftset.setTarget(txtViewQuestion);
            leftset.start();
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        String txt = getString(R.string.num2);
        txtView.setText(txt);
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
        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button retry = findViewById(R.id.retry);
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
                result = getString(R.string.WIN);
                score = 2;
                streak = 1;
                //効果音
                soundPool.play(action[2], 10f , 1f, 0, 0, 1f);
                txtViewQuestion.setBackgroundColor(Color.parseColor("#FF8C00"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#FF4500"));

            } else if (question > answer) {
                result = getString(R.string.LOSE);
                score = -1;
                streak = 0;
                soundPool.play(action[3], 100f , 1f, 0, 0, 1f);
                txtViewQuestion.setBackgroundColor(Color.parseColor("#808080"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#A9A9A9"));
            } else {
                result = getString(R.string.DRAW);
                score = 1;
                txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#ffff00"));
            }
        } else {
            if (question > answer) {
                result = getString(R.string.WIN);
                score = 2;
                streak = 1;
                //効果音
                soundPool.play(action[2], 10f , 1f, 0, 0, 1f);
                txtViewQuestion.setBackgroundColor(Color.parseColor("#FF8C00"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#FF4500"));
            } else if (question < answer) {
                result = getString(R.string.LOSE);
                score = -1;
                streak = 0;
                soundPool.play(action[3], 100f , 1f, 0, 0, 1f);
                txtViewQuestion.setBackgroundColor(Color.parseColor("#808080"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#A9A9A9"));
            } else {
                result = getString(R.string.DRAW);
                score = 1;
                txtViewQuestion.setBackgroundColor(Color.parseColor("#ff00ff"));
                txtViewAnswer.setBackgroundColor(Color.parseColor("#ffff00"));
            }
        }

        // スコアを表示
        setScore(score);
        
        winningStreak(streak);

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast toast=Toast.makeText(this, result, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 100,-1000);
        toast.show();
        txtResult.setText("：" + question + ":" + answer + "(" + result + ")");

        if(count == 5){
            ImageView winner = findViewById(R.id.winner);
            ImageView looser = findViewById(R.id.looser);
            btn1.setEnabled(false);
            btn2.setEnabled(false);
            retry.setEnabled(false);
            winner.setAlpha(0f);
            looser.setAlpha(0f);

            TextView txtScore = (TextView) findViewById(R.id.text_score);
            if(Integer.parseInt(txtScore.getText().toString()) >= 4){
                winner.setAlpha(1.0f);
            } else{
                looser.setAlpha(1.0f);
            }
            count = 0;
            AnimatorSet rightset = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.rightlose_animation);
            rightset.setTarget(txtViewAnswer);
            rightset.start();
            AnimatorSet leftset = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.leftlose_animation);
            leftset.setTarget(txtViewQuestion);
            leftset.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    retry.setEnabled(true);
                }
            });
            leftset.start();
        }

        // 続けて遊べるように値を更新
        setNextQuestion();

    }

    private void setNextQuestion() {
        Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(
                50,100));

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

    private void winningStreak(int streak) {
        int newStreak = 0;
        TextView txtStreak = (TextView) findViewById(R.id.text_streak);
        TextView maxstreak = (TextView) findViewById(R.id.streaknum);
        if(streak==0)
            txtStreak.setText(Integer.toString(newStreak));
        else {
            newStreak = Integer.parseInt(txtStreak.getText().toString()) + streak;
            txtStreak.setText(Integer.toString(newStreak));
            maxstreak.setText(Integer.toString(newStreak));
        }
    }

    private void clearStreakValue() {
        TextView txtStreak = (TextView) findViewById(R.id.text_streak);
        TextView maxstreak = (TextView) findViewById(R.id.streaknum);
        txtStreak.setText("0");
        maxstreak.setText("0");
    }

}

