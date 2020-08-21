package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    プリファレンスの生成
     */
    SharedPreferences pref;
    SharedPreferences.Editor prefEditer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(this);

        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        Button btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        // 起動時に関数を呼び出す
        setQuestionValue();

        /*
        プリファレンスの保存
         */
        //"AndroidSeminor"は、保存する先のファイル名のようなもの
        pref = getSharedPreferences("AndroidSeminor", MODE_PRIVATE);
        prefEditer = pref.edit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                Toast.makeText(this,"Settingsが選択されました",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings2:
                Toast.makeText(this,"Settings2が選択されました",Toast.LENGTH_SHORT).show();
            case R.id.action_settings3:
                Toast.makeText(this,"Settings3が選択されました",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button1:
                setAnswerValue();
                checkResult(true);
                break;
            case R.id.button2:
                setAnswerValue();
                checkResult(false);
                break;
            case R.id.button3:
                setQuestionValue();
                clearAnswerValue();
                TextView ans = (TextView) findViewById(R.id.answer);
                TextView ques = (TextView) findViewById(R.id.question);
                ans.setBackgroundColor(Color.rgb(255, 255, 0));
                ques.setBackgroundColor(Color.rgb(255, 0, 255));

                break;

        }

    }

    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "onPause" , Toast.LENGTH_SHORT).show();

        //画面上からスコアの値を保存するため、テキストビューを取得
        TextView textView = (TextView)findViewById(R.id.text_score);
        //"main_input"というキー名（箱）に、文字列を保存
        prefEditer.putString("main_input", textView.getText().toString());
        prefEditer.commit();
    }

    @Override
    protected void onResume(){
        super.onResume();
        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();

        /*
        プリファレンスの読み込み
         */
        //画面上に文字列をセットするため、テキストビューを取得
        TextView textView = (TextView)findViewById(R.id.text_score);
        //保存した値をキー名(main_input)を指定して取得
        //一度も保存されていない場合もありえるので、その時に代わりに表示する文字列も指定する
        String readText = pref.getString("main_input", "");
        textView.setText(readText);
    }

    private void clearAnswerValue() {
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText("値2");
    }

    private void setQuestionValue() {
        Random r = new Random();
        // 0から10の範囲で乱数を生成（+1する必要がある）
        int questionValue = r.nextInt(10 + 1);
        TextView txtView = (TextView) findViewById(R.id.question);
        txtView.setText(Integer.toString(questionValue));
    }

    private void setAnswerValue() {
        Random r = new Random();
        int answerValue = r.nextInt(10 + 1);
        TextView txtView = (TextView) findViewById(R.id.answer);
        txtView.setText(Integer.toString(answerValue));
    }

    private void checkResult(boolean isHigh) {
        TextView txtViewQuestion = (TextView) findViewById(R.id.question);
        TextView txtViewAnswer = (TextView) findViewById(R.id.answer);
        int question = Integer.parseInt(txtViewQuestion.getText().toString());
        int answer = Integer.parseInt(txtViewAnswer.getText().toString());
        TextView txtResult = (TextView) findViewById(R.id.text_result);
        // 結果を示す文字列を入れる変数を用意
        String result;
        int score = 0;

        //Vibratorインスタンスの追加
        Vibrator vib;

        // Highが押された
        if (isHigh) {
            // result には結果のみを入れる
            if (question < answer) {
                result = "WIN";
                score = 2;
                background(true);

                /*
                バイブレーションの追加（勝利した際のみバイブレーションする）
                 */
                vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);

            } else if (question > answer) {
                result = "LOSE";
                score = -1;
                background(false);
            } else {
                result = "DRAW";
                score = 1;
            }
        } else {
            if (question > answer) {
                result = "WIN";
                score = 2;
                background(false);

                /*
                バイブレーションの追加（勝利した際のみバイブレーションする）
                 */
                vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(500);

            } else if (question < answer) {
                result = "LOSE";
                score = -1;
                background(true);
            } else {
                result = "DRAW";
                score = 1;
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

    private void background(boolean check){
        TextView ans = (TextView) findViewById(R.id.answer);
        TextView ques = (TextView) findViewById(R.id.question);

        if(check){
            ans.setBackgroundColor(Color.rgb(200, 0, 0));
            ques.setBackgroundColor(Color.rgb(0, 100, 200));
        }else{
            ans.setBackgroundColor(Color.rgb(0, 0, 200));
            ques.setBackgroundColor(Color.rgb(200, 0, 0));
        }

    }

}