// Author: So Ching Yiu

// Home page of application
package com.example.parentalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;


import com.example.parentalapp.admin.PinActivity;
import com.example.parentalapp.admin.screentime.TimeSettingHelper;
import com.example.parentalapp.main.AccessAlertDialog;
import com.example.parentalapp.playground.PlaygroundActivity;
import com.example.parentalapp.playground.ScreenTimeService;
import com.example.parentalapp.quiz.QuizMainActivity;
import com.example.parentalapp.reward.RewardMainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.parentalapp.admin.PinActivity.PASSWORD;
import static com.example.parentalapp.quiz.QuizMainActivity.QUIZ_ATTEMPT_ALLOWED;

public class MainActivity extends AppCompatActivity {

    public static final String ACTIVE_TIME_SERVICE = "active_time_service";
    public static final String EXIT = "exit";
    public static final String SAVED_DATE = "saved_date";
    public static final int quizAttemptAllowed = 7;

    private SharedPreferences sharedPreferences;
    private TimeSettingHelper timeSettingHelper;
    private Button adminLogin, quiz, rewardStore;
    private boolean back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        // load settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        timeSettingHelper = new TimeSettingHelper(getBaseContext());
        setButton();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // reset quiz attempt allowed
        try{
            String savedDate = sharedPreferences.getString(SAVED_DATE, "1-1-1990");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date oldDate = sdf.parse(savedDate);

            Date newDate = sdf.parse(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

            if(oldDate.compareTo(newDate) < 0){
                sharedPreferences.edit().putInt(QUIZ_ATTEMPT_ALLOWED, quizAttemptAllowed).apply();
                timeSettingHelper.setRemainingScreenTime(7200);
            }

            sharedPreferences.edit().putString(SAVED_DATE, new SimpleDateFormat("dd-MM-yyyy").format(newDate)).apply();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(getIntent().getBooleanExtra(EXIT, false)){
            finish();
            System.exit(0);
        }else if(sharedPreferences.getBoolean(ACTIVE_TIME_SERVICE, false)){
            startActivity(new Intent (getApplicationContext(), PlaygroundActivity.class));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disable recent apps button
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);

        // transition animation direction
        if(!back){
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // actions for the application's first run
        if(sharedPreferences.getBoolean("first_run", true)){
            sharedPreferences.edit().putString(PASSWORD, "1234").apply();
            timeSettingHelper.setActiveHourStart(6);
            timeSettingHelper.setActiveMinuteStart(0);
            timeSettingHelper.setActiveHourEnd(23);
            timeSettingHelper.setActiveMinuteEnd(59);
            sharedPreferences.edit().putBoolean("first_run", false).apply();
            timeSettingHelper.setRemainingScreenTime(14400);
        }

        // Checks if the time service is active
        if(sharedPreferences.getBoolean(ACTIVE_TIME_SERVICE, false)){
            startActivity(new Intent (getApplicationContext(), PlaygroundActivity.class));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // disable back button
    @Override
    public void onBackPressed() {
        // empty function
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
        long remainingTime = timeSettingHelper.getRemainingTime();
        if (remainingTime <= 0){
            //Toast.makeText(getApplicationContext(), time,Toast.LENGTH_SHORT).show();
            showAlertDialog("Warning", "No Screen Time Remaining!");
        }else{
            if(!checkActiveHour()){
                showAlertDialog("Warning", "Not within active hour!");
            }else{
                sharedPreferences.edit().putBoolean(ACTIVE_TIME_SERVICE, true).apply();
                Intent serviceIntent = new Intent(this, ScreenTimeService.class);
                ContextCompat.startForegroundService(this, serviceIntent);
                Intent playGroundIntent = new Intent (getApplicationContext(), PlaygroundActivity.class);
                startActivity(playGroundIntent);
                onPause();
            }
        }

    }

    private void showAlertDialog(String title, String message){
        AccessAlertDialog accessAlertDialog = new AccessAlertDialog(title, message);
        accessAlertDialog.show(getSupportFragmentManager(), "access alert dialog");
    }

    private boolean checkActiveHour(){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String[] currentTime = format.format(Calendar.getInstance().getTime()).split(":");

        int currentHour = Integer.parseInt(currentTime[0]);
        int currentMinute = Integer.parseInt(currentTime[1]);

        int hourStart = timeSettingHelper.getActiveHourStart();
        int hourEnd = timeSettingHelper.getActiveHourEnd();
        int minuteStart = timeSettingHelper.getActiveMinuteStart();
        int minuteEnd = timeSettingHelper.getActiveMinuteEnd();

        if(currentHour - hourStart < hourEnd - hourStart){
            return true;
        }else if(currentHour == hourStart && currentMinute > minuteStart){
            return true;
        }else if(currentHour == hourEnd && currentMinute < minuteEnd){
            return true;
        }
        return false;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}