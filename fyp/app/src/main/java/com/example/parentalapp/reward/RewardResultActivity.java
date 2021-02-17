package com.example.parentalapp.reward;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentalapp.R;
import com.example.parentalapp.admin.apprestrict.AppRestrictConfig;
import com.example.parentalapp.admin.rewarditem.unresolves.UnresolvedRewardDBHelper;
import com.example.parentalapp.admin.rewardpoint.RewardPointConfig;
import com.example.parentalapp.admin.screentime.TimeSettingHelper;
import com.example.parentalapp.reward.rewardhistory.RewardHistoryDBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.parentalapp.reward.RewardMainActivity.ITEM_NAME;
import static com.example.parentalapp.reward.RewardMainActivity.POINTS;
import static com.example.parentalapp.reward.RewardMainActivity.QUANTITY;

import static com.example.parentalapp.reward.RewardDBHelper.REWARD_EFFECT_ITEM;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_EFFECT_VALUE;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_ITEM_ID;

public class RewardResultActivity extends AppCompatActivity {

    private TextView textViewResult;
    private boolean success = false;
    private RewardDBHelper rewardDBHelper;
    private UnresolvedRewardDBHelper unresolvedRewardDBHelper;
    private String itemName, effectItem, effectValue, currentDate;
    private int id, quantity, point;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_result);

        sharedPreferences = getSharedPreferences("appPreference", MODE_PRIVATE);

        Button button_return = findViewById(R.id.button_back_to_store);
        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RewardMainActivity.class));
            }
        });

        textViewResult = findViewById(R.id.textView_reward_result);

        rewardDBHelper = new RewardDBHelper(getApplicationContext());
        unresolvedRewardDBHelper = new UnresolvedRewardDBHelper(getApplicationContext());

        initialize();

        changeEffectItem();
    }

    // getting reward details
    private void initialize(){
        id = getIntent().getExtras().getInt(REWARD_ITEM_ID);
        itemName = getIntent().getExtras().getString(ITEM_NAME);
        quantity = Integer.parseInt(getIntent().getExtras().getString(QUANTITY));
        point = Integer.parseInt(getIntent().getExtras().getString(POINTS));
    }

    // process the reward
    private void changeEffectItem(){
        Cursor c = rewardDBHelper.getItemDetail(REWARD_ITEM_ID, String.valueOf(id));
        if(c.moveToFirst()){
            effectItem = c.getString(c.getColumnIndex(REWARD_EFFECT_ITEM));

            // check type of reward item
            if(effectItem.compareTo("time") == 0){
                // screen time reward
                effectValue = c.getString(c.getColumnIndex(REWARD_EFFECT_VALUE));
                TimeSettingHelper timeSettingHelper = new TimeSettingHelper(getApplicationContext());
                timeSettingHelper.addRemainingScreenTime(Long.parseLong(effectValue) * quantity);
                success = true;
            }else if (effectItem.compareTo("app") == 0){
                // app restriction reward
                if(!sharedPreferences.getBoolean(itemName, false)){
                    AppRestrictConfig appRestrictConfig = new AppRestrictConfig(this);
                    appRestrictConfig.changePermission(itemName, true);
                    success = true;
                }

            }else if (effectItem.compareTo("custom") == 0){
                // custom reward
                success = true;
                if(!unresolvedRewardDBHelper.addUnresolvedReward(itemName, id, quantity, UnresolvedRewardDBHelper.UNRESOLVED_REWARD_STATUS_UNRESOLVED)){
                    Toast.makeText(getApplicationContext(), "Unable to puchase item.", Toast.LENGTH_SHORT).show();
                    success = false;
                }
            }
        }
        showResult();
    }

    private void deductPoint(){
        RewardPointConfig rewardPointConfig = new RewardPointConfig(getApplicationContext());
        rewardPointConfig.deductRewardPoints(point * quantity);
    }

    private void addToHistory(){
        RewardHistoryDBHelper rewardHistoryDBHelper = new RewardHistoryDBHelper(getApplicationContext());
        rewardHistoryDBHelper.addRecord(itemName, id);
    }

    private void showResult(){
        if(success){
            deductPoint();
            addToHistory();
            textViewResult.setText("Reward Claimed!");
        }else{
            textViewResult.setText("Purchase failed.");
        }
    }

    @Override
    public void onBackPressed() {
        // empty function
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disable recent apps button
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}