package com.example.parentalapp.admin;

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

public class ParentMainActivity extends AppCompatActivity {

    Button timeSettings, appSelect, setRewardPoint, questionSetting, rewardItemSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

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
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}