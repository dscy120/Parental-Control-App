package com.example.parentalapp.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.parentalapp.R;
import com.example.parentalapp.admin.questionbank.QuestionBankDBHelper;
import com.example.parentalapp.quiz.question.Question;
import com.example.parentalapp.quiz.question.QuizDBHelper;

import java.util.List;

import static com.example.parentalapp.quiz.QuizSelectionActivity.SUBJECT;
import static com.example.parentalapp.quiz.QuizSelectionActivity.DIFFICULTY;

public class SelectConfirmDialog extends AppCompatDialogFragment {
    private SelectConfirmDialogListener listener;
    private QuizDBHelper quizDBHelper;
    private String subject, difficulty;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View customLayout = getActivity().getLayoutInflater().inflate(R.layout.dialog_confirm_quiz_detail, null);

        subject = getArguments().getString(SUBJECT);
        difficulty = getArguments().getString(DIFFICULTY);

        quizDBHelper = new QuizDBHelper(getContext());
        final List<Question> questions = quizDBHelper.getQuestions(subject, difficulty);

        setDialogInfo(customLayout, subject, difficulty);

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
                        if(questions.size() <= 0){
                            Toast.makeText(getContext(), "There is no questions available.", Toast.LENGTH_SHORT).show();
                        }else{
                            listener.startQuiz();
                        }
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
