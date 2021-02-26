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

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;

import static com.example.parentalapp.admin.PinActivity.PASSWORD;

public class ChangePasswordActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText editTextCurrent, editTextNew;
    private Button buttonConfirm;
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
        editTextNew = findViewById(R.id.editText_new_password);

        buttonConfirm = findViewById(R.id.button_confirm_change_password);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePassword();
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
}