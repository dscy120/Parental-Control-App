package com.example.parentalapp.quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentalapp.R;
import com.example.parentalapp.quiz.question.QuestionDisplayActivity;

import static com.example.parentalapp.quiz.QuizMainActivity.QUIZ_ATTEMPT_ALLOWED;

public class QuizSelectionActivity extends AppCompatActivity implements SelectConfirmDialog.SelectConfirmDialogListener {

    public static String SUBJECT = "Subject";
    public static String DIFFICULTY = "Difficulty";
    private Spinner spinner_subject, spinner_difficulty;
    private Button button_confirm_selection;
    private TextView attemptRemaining;
    private SharedPreferences sharedPreferences;
    private int allowed_attempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_selection);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        allowed_attempt = sharedPreferences.getInt(QUIZ_ATTEMPT_ALLOWED, 0);

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

        attemptRemaining = findViewById(R.id.textView_reamaining_attempt);
        attemptRemaining.setText("Remaining Attempts: " + allowed_attempt);

        button_confirm_selection = findViewById(R.id.button_confirm_quiz_select);
        button_confirm_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allowed_attempt > 0){
                    openConfirmDialog();
                }else{
                    Toast.makeText(getApplicationContext(), "No more remaining attempt allowed", Toast.LENGTH_SHORT).show();
                }
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

    @Override
    protected void onPause() {
        super.onPause();
        // disable recent apps button
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
        sharedPreferences.edit().putInt(QUIZ_ATTEMPT_ALLOWED, allowed_attempt - 1).apply();
        String subject = spinner_subject.getSelectedItem().toString();
        String difficulty = spinner_difficulty.getSelectedItem().toString();
        Intent i = new Intent(this, QuestionDisplayActivity.class);
        i.putExtra("subject", subject);
        i.putExtra("difficulty", difficulty);
        startActivity(i);
    }
}