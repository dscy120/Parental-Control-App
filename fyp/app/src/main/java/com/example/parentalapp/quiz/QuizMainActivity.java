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
import android.widget.Button;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.quiz.record.RecordMainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class QuizMainActivity extends AppCompatActivity {

    public static final String QUIZ_ATTEMPT_ALLOWED = "attempt_allowed";

    SharedPreferences sharedPreferences;
    Button doQuiz, history;
    private boolean back = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        doQuiz = findViewById(R.id.button_doquiz);
        doQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back = false;
                chooseQuiz();
            }
        });
        history = findViewById(R.id.button_record);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back = false;
                startActivity(new Intent(getApplicationContext(), RecordMainActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        back = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disable recent apps button
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
        if(!back){
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void chooseQuiz(){
        startActivity(new Intent(getApplicationContext(), QuizSelectionActivity.class));
    }
}