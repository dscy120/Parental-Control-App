// Main page
package com.example.parentalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.parentalapp.admin.PinActivity;
import com.example.parentalapp.playground.PlaygroundActivity;
import com.example.parentalapp.playground.ScreenTimeService;
import com.example.parentalapp.quiz.QuizMainActivity;
import com.example.parentalapp.reward.RewardMainActivity;

import static com.example.parentalapp.admin.GeneralSettingsActivity.time;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private Button adminLogin, quiz, rewardStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        setButton();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        // Back button is disabled
    }

    // Reserved if need option menu
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    // menu content in menu/activity_main_drawer.xml
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this, PinActivity.class));
                return true;
            case R.id.appSelection:
                startActivity(new Intent(this, AppRestrictActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */

    void setButton(){
        adminLogin = findViewById(R.id.button_adminLogin);
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PinActivity.class));
            }
        });

        quiz = findViewById(R.id.button_launchquiz);
        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), QuizMainActivity.class));
            }
        });

        rewardStore = findViewById(R.id.button_launchReward);
        rewardStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RewardMainActivity.class));
            }
        });
    }

    public void startTimerService(View v){
        // check remaining time before launching to playground
        long remainingTime = Long.parseLong(sharedPreferences.getString(time, "0"));
        if (remainingTime <= 0){
            Toast.makeText(getApplicationContext(),"No Screen Time Remaining",Toast.LENGTH_SHORT).show();
        }else{
            Intent serviceIntent = new Intent(this, ScreenTimeService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
            Intent playGroundIntent = new Intent (getApplicationContext(), PlaygroundActivity.class);
            startActivity(playGroundIntent);
        }

    }

}