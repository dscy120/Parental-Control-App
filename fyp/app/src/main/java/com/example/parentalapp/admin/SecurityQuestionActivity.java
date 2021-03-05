package com.example.parentalapp.admin;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;

import static com.example.parentalapp.admin.ChangePasswordActivity.SECURITY_ANSWER;
import static com.example.parentalapp.admin.ChangePasswordActivity.SECURITY_QUESTION;

public class SecurityQuestionActivity extends AppCompatActivity {

    public static final String SECURITY_CHECK = "security_check";

    private SharedPreferences sharedPreferences;
    private TextView textViewQuestion;
    private EditText editTextAnswer;
    private Button buttonSubmitAnswer;
    private boolean back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);

        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        textViewQuestion = findViewById(R.id.textView_security_question);
        textViewQuestion.setText(sharedPreferences.getString(SECURITY_QUESTION, ""));

        editTextAnswer = findViewById(R.id.editText_security_answer_input);

        buttonSubmitAnswer = findViewById(R.id.button_submit_security_answer);
        buttonSubmitAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back = true;
                startActivity(new Intent(getApplicationContext(), PinActivity.class));
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
        if(!back){
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public void onBackPressed() {
        back = true;
        startActivity(new Intent(getApplicationContext(), PinActivity.class));
    }

    private void checkAnswer(){
        String correctAnswer = sharedPreferences.getString(SECURITY_ANSWER, "");
        String inputAnswer = editTextAnswer.getText().toString();
        if(inputAnswer.compareTo(correctAnswer) == 0){
            Toast.makeText(this, "Answer is correct. Please change your password immediately", Toast.LENGTH_LONG).show();
            Bundle b = new Bundle();
            b.putBoolean(SECURITY_CHECK, true);
            Intent i = new Intent(this, ChangePasswordActivity.class);
            i.putExtras(b);
            startActivity(i);
        }else{
            Toast.makeText(this, "Wrong answer.", Toast.LENGTH_SHORT).show();
        }
    }
}