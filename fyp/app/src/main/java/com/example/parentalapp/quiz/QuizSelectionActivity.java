package com.example.parentalapp.quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.parentalapp.R;
import com.example.parentalapp.quiz.question.QuestionDisplayActivity;

public class QuizSelectionActivity extends AppCompatActivity implements SelectConfirmDialog.SelectConfirmDialogListener {

    public static String SUBJECT = "Subject";
    public static String DIFFICULTY = "Difficulty";
    Spinner spinner_subject, spinner_difficulty;
    Button button_confirm_selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selection);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // set drop down menu content
        spinner_subject = findViewById(R.id.spinner_subject);
        ArrayAdapter<String> arrayAdapter_subject = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.subject));
        arrayAdapter_subject.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_subject.setAdapter(arrayAdapter_subject);

        spinner_difficulty = findViewById(R.id.spinner_difficulty);
        ArrayAdapter<String> arrayAdapter_difficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.difficulty));
        arrayAdapter_difficulty.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_difficulty.setAdapter(arrayAdapter_difficulty);

        button_confirm_selection = findViewById(R.id.button_confirm_quiz_select);
        button_confirm_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, QuizMainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openConfirmDialog(){
        SelectConfirmDialog selectConfirmDialog = new SelectConfirmDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SUBJECT, spinner_subject.getSelectedItem().toString());
        bundle.putString(DIFFICULTY, spinner_difficulty.getSelectedItem().toString());
        selectConfirmDialog.setArguments(bundle);
        selectConfirmDialog.show(getSupportFragmentManager(), "Selection confirm dialog");
    }


    @Override
    public void startQuiz(){
        String subject = spinner_subject.getSelectedItem().toString();
        String difficulty = spinner_difficulty.getSelectedItem().toString();
        Intent i = new Intent(this, QuestionDisplayActivity.class);
        i.putExtra("subject", subject);
        i.putExtra("difficulty", difficulty);
        startActivity(i);
    }
}