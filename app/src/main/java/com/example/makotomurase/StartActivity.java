package com.example.makotomurase;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.NumberPicker;

public class StartActivity extends AppCompatActivity {
    private Button buttonNewGame, buttonContinue, buttonSettings;
    private int range = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //TODO: Intentからモード取得
        init();
        applyAnimations();
    }

    private void init() {
        linkViewIds();
        registerViewListeners();
    }

    private void linkViewIds() {
        buttonNewGame = findViewById(R.id.button_start_new);
        buttonContinue = findViewById(R.id.button_start_continue);
        buttonSettings = findViewById(R.id.button_settings);
    }

    private void registerViewListeners() {
        buttonNewGame.setOnClickListener(view -> startMainActivity(Constrants.START_OPTION.NEW));
        buttonContinue.setOnClickListener(view -> startMainActivity(Constrants.START_OPTION.CONTINUE));
        buttonSettings.setOnClickListener(view -> showNumberPicker());
    }

    private void applyAnimations() {
        Animation animFade = AnimationUtils.loadAnimation(this, R.anim.animation_title);
        findViewById(R.id.text_title).startAnimation(animFade);
        findViewById(R.id.text_subtitle).startAnimation(animFade);

        Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.animation_rotate_background);
        findViewById(R.id.view_background).startAnimation(animRotate);
    }

    private void startMainActivity(Constrants.START_OPTION mode) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constrants.KEY_INTENT_START_OPTION, mode);
        intent.putExtra(Constrants.KEY_INTENT_RANGE, range);
        startActivity(intent);
    }

    @SuppressLint("ResourceType")
    private void showNumberPicker() {
        final Dialog dialog = new Dialog(StartActivity.this);
        dialog.setTitle(R.string.title_picker);
        dialog.setContentView(R.layout.layout_dialog_picker);
        Button btnHigh = dialog.findViewById(R.id.button_high);
        Button btnLow = dialog.findViewById(R.id.button_low);
        final NumberPicker nPicker = dialog.findViewById(R.id.numberPicker);
        nPicker.setMaxValue(Constrants.RANGE_RANDOM_MAX);
        nPicker.setMinValue(Constrants.RANGE_RANDOM_MIN);
        //TODO: データ保存の実装待ち
        nPicker.setValue(range);
        nPicker.setWrapSelectorWheel(false);
        btnHigh.setOnClickListener(v -> {
            range = nPicker.getValue();
            dialog.dismiss();
        });
        btnLow.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
