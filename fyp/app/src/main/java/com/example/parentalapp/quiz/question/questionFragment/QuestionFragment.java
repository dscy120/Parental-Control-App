package com.example.parentalapp.quiz.question.questionFragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.parentalapp.R;

public class QuestionFragment extends Fragment implements FragmentListener{

    private View view;
    private Activity activity;
    private ConstraintLayout constraintLayout;
    private FragmentListener listener;
    private Bundle bundle;

    private ColorStateList textColorDefault;
    final private int button_id = 23;
    final private int option_id = 60;

    @Override
    public void onInputSent(CharSequence[] input) {

    }

    public QuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question, container, false);
        activity = this.getActivity();
        constraintLayout = view.findViewById(R.id.questionLayout);

        bundle = getArguments();
        int questionType = bundle.getInt("question_type");
        setOptionText(questionType);

        return view;
    }

    private void setOptionText(int questionType){
        Button button_next = new Button(activity);
        button_next.setId(button_id);
        button_next.setText("Next");

        constraintLayout.addView(button_next);

        ConstraintSet set = new ConstraintSet();

        switch (questionType){
            case 1:
                //ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(100, 100);
                final RadioGroup radioGroup = new RadioGroup(activity);
                //textColorDefault = rb1.getTextColors();

                radioGroup.setId(option_id);

                for(int i = 1; i < 5; i++){
                    final RadioButton rb = new RadioButton(activity);
                    rb.setId(option_id + i);
                    rb.setText(bundle.getString("option" + i));
                    if(rb.getText().toString().compareTo("") != 0){
                        radioGroup.addView(rb);
                    }
                }
                constraintLayout.addView(radioGroup);

                button_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int checked_id = radioGroup.getCheckedRadioButtonId();
                        if (checked_id >= 0){
                            RadioButton ans = view.findViewById(checked_id);
                            CharSequence[] answerText = new CharSequence[1];
                            answerText[0] = ans.getText();
                            listener.onInputSent(answerText);
                        }else{
                            Toast.makeText(getContext(), "Please choose an answer", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;

            case 2:
                final EditText editText = new EditText(activity);
                editText.setId(option_id);
                constraintLayout.addView(editText);
                button_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence answer = editText.getText();
                        if (answer.toString().compareTo("") != 0){
                            CharSequence[] answerText = new CharSequence[1];
                            answerText[0] = answer;
                            listener.onInputSent(answerText);
                        } else {
                            Toast.makeText(getContext(), "Please fill in the answer", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            default:
                break;
        }

        set.clone(constraintLayout);
        set.constrainDefaultWidth(button_id, ConstraintSet.MATCH_CONSTRAINT_SPREAD);
        set.connect(button_id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        set.connect(button_id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        set.connect(button_id, ConstraintSet.TOP, option_id, ConstraintSet.BOTTOM);
        set.connect(button_id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        set.setHorizontalBias(button_id, 100);
        set.applyTo(constraintLayout);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof FragmentListener){
            listener = (FragmentListener) context;
        }else{
            throw new RuntimeException(context.toString() + "must implement MCFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}