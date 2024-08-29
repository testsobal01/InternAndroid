package com.example.makotomurase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class NumberDialogFragment extends DialogFragment {

    private static final int MAX_VALUE = 50;
    private static final int MIN_VALUE = 19;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState){
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.number_dialog, null, false);

        NumberPicker np = view.findViewById(R.id.numberPicker);

        np.setMaxValue(MAX_VALUE);
        np.setMinValue(MIN_VALUE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("最大値を設定してください");
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });
        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            int num = Integer.parseInt(String.valueOf(np.getValue()));
            MainActivity callingActivity = (MainActivity) getActivity();
            assert callingActivity != null;
            callingActivity.onReturnValue(String.valueOf(num));
        });
        builder.setView(view);
        return  builder.create();
    }
}
