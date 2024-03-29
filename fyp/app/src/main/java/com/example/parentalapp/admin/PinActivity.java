package com.example.parentalapp.admin;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;

public class PinActivity extends AppCompatActivity {
    public static final String PASSWORD = "password";

    private SharedPreferences sharedPreferences;
    private EditText editText_pin;
    private Button button_confirm, button_forgort_password;
    private boolean back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String password = sharedPreferences.getString(PASSWORD, "1234");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editText_pin = findViewById(R.id.editTextNumber);

        button_confirm = findViewById(R.id.button_confirm);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_pin.getText().toString().equals(password)){
                    Intent i = new Intent();
                    startActivity(new Intent(getApplicationContext(), ParentMainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Wrong pin",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_forgort_password = findViewById(R.id.button_forget_password);
        button_forgort_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SecurityQuestionActivity.class));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back = true;
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}