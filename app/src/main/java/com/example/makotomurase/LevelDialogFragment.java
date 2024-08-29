package com.example.makotomurase;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.makotomurase.MainActivity;
import com.example.makotomurase.R;

public class LevelDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null, false);

       final NumberPicker np1 = (NumberPicker) view.findViewById(R.id.pickup);
        np1.setMinValue(10);
        np1.setMaxValue(50);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("最大値を設定してください");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MainActivity mainActivity = (MainActivity) getActivity();

                int rate = Integer.parseInt(String.valueOf(np1.getValue()));
                mainActivity.getLimit(rate);

            }
        });
        builder.setView(view);
        return builder.create();

    }
}