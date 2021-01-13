package com.example.parentalapp.quiz.question.questionFragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.parentalapp.R;
import com.example.parentalapp.quiz.QuizMainActivity;

public class QuizResultActivity extends AppCompatActivity {

    Bundle bundle;
    TextView textView_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        textView_score = findViewById(R.id.textView_score);
        bundle = getIntent().getExtras();

        int finalScore = bundle.getInt("Score");
        int totalQuestion = bundle.getInt("TotalQuestion");
        String result = finalScore + " / " + totalQuestion;
        textView_score.setText(result);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, QuizMainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}