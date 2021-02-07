package com.example.parentalapp.quiz.question;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.quiz.QuizMainActivity;
import static com.example.parentalapp.admin.rewardpoint.RewardPointConfig.REWARD_POINTS;
public class QuizResultActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Bundle bundle;
    TextView textView_score, textView_quizPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

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

        // decide reward point weighting of questions
        String difficulty = bundle.getString("Difficulty");
        int weighting = 100;
        if(difficulty.compareTo("Medium") == 0){
            weighting = 150;
        }else if(difficulty.compareTo("Hard") == 0){
            weighting = 200;
        }

        // add reward points
        int quizPoints = finalScore * weighting;
        textView_quizPoints = findViewById(R.id.textView_quizPoint);
        textView_quizPoints.setText(quizPoints + "pts");
        int totalRewardPoints = sharedPreferences.getInt(REWARD_POINTS, 0);
        sharedPreferences.edit().putInt(REWARD_POINTS, totalRewardPoints + quizPoints).apply();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), QuizMainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}