package com.example.parentalapp.quiz;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.quiz.record.AttemptDetailActivity;
import com.example.parentalapp.quiz.record.RecordMainActivity;

public class QuizMainActivity extends AppCompatActivity {

    Button doQuiz, history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_main);

        //back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        doQuiz = findViewById(R.id.button_doquiz);
        doQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseQuiz();
            }
        });
        history = findViewById(R.id.button_record);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RecordMainActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void chooseQuiz(){
        startActivity(new Intent(getApplicationContext(), QuizSelectionActivity.class));
    }
}