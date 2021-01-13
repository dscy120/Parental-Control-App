package com.example.parentalapp.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SelectConfirmDialog extends AppCompatDialogFragment {
    private SelectConfirmDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Quiz Details")
                .setMessage("Confirm to proceed? Once started, the quiz must be complete without interruption.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // empty function
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.startQuiz();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (SelectConfirmDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    "must implement SelectConfirmDialogListener");
        }
    }

    public interface SelectConfirmDialogListener{
        void startQuiz();
    }
}
