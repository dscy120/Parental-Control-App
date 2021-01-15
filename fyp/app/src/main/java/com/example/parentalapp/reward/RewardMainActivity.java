package com.example.parentalapp.reward;

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

public class RewardMainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView rewardPoints;
    public static final String REWARD_POINTS = "reward_points";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_main);

        //back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // set reward points
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        initPoints();

        setTextView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setTextView(){
        rewardPoints = findViewById(R.id.textView_rewardPoint);
        String rewardPointText = sharedPreferences.getInt(REWARD_POINTS, 0) + " points";
        rewardPoints.setText(rewardPointText);
    }

    // Initialize or reset total reward points
    public void initPoints(){
        sharedPreferences.edit().putInt(REWARD_POINTS, 0);
    }
}