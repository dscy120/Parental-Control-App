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
import android.widget.Toast;

import com.example.parentalapp.R;

import static com.example.parentalapp.admin.PinActivity.PASSWORD;
import static com.example.parentalapp.admin.SecurityQuestionActivity.SECURITY_CHECK;

public class ChangePasswordActivity extends AppCompatActivity {

    public static final String SECURITY_QUESTION = "security_question";
    public static final String SECURITY_ANSWER = "security_answer";

    private SharedPreferences sharedPreferences;
    private EditText editTextCurrent, editTextNew, editTextSecurityQuestion, editTextSecurityAnswer;
    private Button buttonConfirmPassword, buttonConfirmQuestion;
    private boolean back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        editTextCurrent = findViewById(R.id.editText_current_password);
        try{
            if(getIntent().getExtras().getBoolean(SECURITY_CHECK)){
                editTextCurrent.setText(sharedPreferences.getString(PASSWORD, "1234"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        editTextNew = findViewById(R.id.editText_new_password);

        buttonConfirmPassword = findViewById(R.id.button_confirm_change_password);
        buttonConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePassword();
            }
        });

        editTextSecurityQuestion = findViewById(R.id.editText_security_question);
        editTextSecurityQuestion.setText(sharedPreferences.getString(SECURITY_QUESTION, ""));

        editTextSecurityAnswer = findViewById(R.id.editText_security_answer);
        editTextSecurityAnswer.setText(sharedPreferences.getString(SECURITY_ANSWER, ""));

        buttonConfirmQuestion = findViewById(R.id.button_change_security_answer);
        buttonConfirmQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSecurityAnswer();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back = true;
                startActivity(new Intent(getApplicationContext(), ParentMainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        back = true;
        startActivity(new Intent(getApplicationContext(), ParentMainActivity.class));
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

    private void savePassword(){
        String savedPassword = sharedPreferences.getString(PASSWORD, "1234");
        String enteredCurrentPassword = editTextCurrent.getText().toString();
        String enteredNewPassword = editTextNew.getText().toString();
        if(enteredCurrentPassword.compareTo(savedPassword) == 0){
            sharedPreferences.edit().putString(PASSWORD, enteredNewPassword).apply();
            editTextCurrent.setText("");
            editTextNew.setText("");
            Toast.makeText(this, "Password changed to " + enteredNewPassword, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "The current password is wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveSecurityAnswer(){
        String securityQuestion = editTextSecurityQuestion.getText().toString();
        String securityAnswer = editTextSecurityAnswer.getText().toString();
        sharedPreferences.edit().putString(SECURITY_QUESTION, securityQuestion).apply();
        sharedPreferences.edit().putString(SECURITY_ANSWER, securityAnswer).apply();
        Toast.makeText(this, "Security question saved.", Toast.LENGTH_SHORT).show();
    }
}