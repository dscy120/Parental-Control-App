package com.example.parentalapp.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.parentalapp.R;

import static com.example.parentalapp.quiz.QuizSelectionActivity.SUBJECT;
import static com.example.parentalapp.quiz.QuizSelectionActivity.DIFFICULTY;

public class SelectConfirmDialog extends AppCompatDialogFragment {
    private SelectConfirmDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View customLayout = getActivity().getLayoutInflater().inflate(R.layout.dialog_confirm_quiz_detail, null);

        setDialogInfo(customLayout, getArguments().getString(SUBJECT), getArguments().getString(DIFFICULTY));

        builder.setView(customLayout);
        builder.setTitle("Confirm Quiz Details")
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

    private void setDialogInfo(View customLayout, String subj, String diff){
        TextView subject = customLayout.findViewById(R.id.textView_subj_confirm);
        TextView difficulty = customLayout.findViewById(R.id.textView_diff_confirm);
        subject.setText("Subject : " + subj);
        difficulty.setText("Difficulty : " + diff);
    }

    public interface SelectConfirmDialogListener{
        void startQuiz();
    }
}
