package com.example.parentalapp.admin;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.admin.apprestrict.AppRestrictActivity;
import com.example.parentalapp.admin.questionbank.QuestionBankActivity;
import com.example.parentalapp.admin.rewarditem.RewardItemConfigActivity;
import com.example.parentalapp.admin.rewardpoint.RewardPointControlActivity;
import com.example.parentalapp.admin.screentime.GeneralSettingsActivity;

import org.apache.log4j.chainsaw.Main;

import static com.example.parentalapp.MainActivity.EXIT;

public class ParentMainActivity extends AppCompatActivity {

    Button timeSettings, appSelect, setRewardPoint, questionSetting, rewardItemSetting;
    private boolean back = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        timeSettings = findViewById(R.id.button_timeSettings);
        timeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GeneralSettingsActivity.class));
            }
        });

        appSelect = findViewById(R.id.button_appSelect);
        appSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AppRestrictActivity.class));
            }
        });

        setRewardPoint = findViewById(R.id.button_set_reward_point);
        setRewardPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RewardPointControlActivity.class));
            }
        });

        questionSetting = findViewById(R.id.button_question_setting);
        questionSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QuestionBankActivity.class));
            }
        });

        rewardItemSetting = findViewById(R.id.button_reward_item_setting);
        rewardItemSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RewardItemConfigActivity.class));
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
    public void onBackPressed() {
        back = true;
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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

    public void QuitApp(View view){
        getPackageManager().clearPackagePreferredActivities(getPackageName());
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra(EXIT, true);
        startActivity(i);
    }
}