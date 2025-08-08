package com.example.makotomurase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    int randMax = 10;// 乱数の最大値
    /**
     * score用のpreference
     */
    SharedPreferences pref;
    SharedPreferences.Editor preEditer;

    /**
     * SEを鳴らす用
     */
    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        String intentString = extra.getString("KEY");

        audioPlayer= new AudioPlayer(this);// audioPlayerのインスタンスを作成


        Button btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        //preferenceのインスタンスを生成
        pref = getSharedPreferences("AndroidSeminor",MODE_PRIVATE);
        preEditer = pref.edit();

        // 起動時に関数を呼び出す
        setQuestionValue();
    }


    @Override
    protected void onPause() {
        super.onPause();

        TextView textScore = (TextView) findViewById(R.id.text_score);
        int nowScore = Integer.parseInt(textScore.getText().toString()); // 現時点のスコアを入手
        preEditer.putInt("score",nowScore);// スコアを保存
        preEditer.putInt("randMax",randMax);// 乱数の最大値を保存
        preEditer.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //もともとのスコアを呼び出す
        TextView textScore = (TextView) findViewById(R.id.text_score);
        textScore.setText(String.valueOf(pref.getInt("score",0)));

        randMax=pref.getInt("randMax",10);// もともとの最大値を呼び出す
        // 表示も変える
        TextView randMaxTextView = (TextView) findViewById(R.id.text_rand_max);
        randMaxTextView.setText(String.valueOf(randMax)+" ");
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    /**
     * NumberPickerを表示するメソッド
     * @param item　押されたアイコン
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_number_picker, null);

        // NumberPickerを取得して初期設定
        NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(10);
        numberPicker.setMaxValue(50);
        numberPicker.setValue(randMax); // 初期値

        // AlertDialogの作成
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_value))
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedValue = numberPicker.getValue();
                    Toast.makeText(this, getString(R.string.select) + selectedValue, Toast.LENGTH_SHORT).show();

                    randMax=selectedValue;// 乱数の最大値を変更
                    TextView randMaxTextView = (TextView) findViewById(R.id.text_rand_max);
                    randMaxTextView.setText(String.valueOf(randMax)+" ");
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();

        return true;
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

            TextView txtViewAnswer = findViewById(R.id.answer);
            txtViewAnswer.setBackgroundColor(Color.rgb(255,255,0));

            audioPlayer.playPushButtonSE();// SEを鳴らす
        }
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(getResources().getString(R.string.value));
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(randMax + 1);

        TextView txtView = findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));

        //設定されたいる数値を表示
        TextView randMaxTextView = (TextView) findViewById(R.id.text_rand_max);
        randMaxTextView.setText(String.valueOf(randMax)+" ");
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(randMax + 1);

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
                result = getResources().getString(R.string.w_result);
                score = 2;
                txtViewAnswer.setBackgroundColor(Color.rgb(255,69,0));
            } else if (question > answer) {
                result = getResources().getString(R.string.l_result);
                score = -1;
                txtViewAnswer.setBackgroundColor(Color.rgb(65,105,225));
            } else {
                result = getResources().getString(R.string.d_result);
                score = 1;
                txtViewAnswer.setBackgroundColor(Color.rgb(220,220,220));
            }
        } else {
            if (question > answer) {
                result = getResources().getString(R.string.w_result);
                score = 2;
                txtViewAnswer.setBackgroundColor(Color.rgb(255,69,0));
            } else if (question < answer) {
                result = getResources().getString(R.string.l_result);
                score = -1;
                txtViewAnswer.setBackgroundColor(Color.rgb(65,105,225));
            } else {
                result = getResources().getString(R.string.d_result);
                score = 1;
                txtViewAnswer.setBackgroundColor(Color.rgb(220,220,220));
            }
        }

        // 最後にまとめてToast表示の処理とTextViewへのセットを行う
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        audioPlayer.playResultSE(result);// SEを鳴らす
        txtResult.setText(getResources().getString(R.string.result)+ question + ":" + answer + "(" + result + ")");
        valueAnimation(result);

        // 続けて遊べるように値を更新
        setNextQuestion();
        // スコアを表示
        setScore(score);
    }

    /**
     * 結果に応じて数値をアニメーションする
     * @param result 結果
     */
    private void valueAnimation (String result){
        /** アニメーションの対象となるtextview */
        TextView target;
        /** animationを定義したxmlファイルの読み込み */
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.value_animater);

        if(result.equals("WIN")){
            target = (TextView) findViewById(R.id.answer);
        }else if(result.equals("LOSE")){
            target = (TextView) findViewById(R.id.question);
        }else{
            return;
        }

        target.startAnimation(animation); //アニメーションを実行

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
}

