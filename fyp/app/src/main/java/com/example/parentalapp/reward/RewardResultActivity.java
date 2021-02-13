package com.example.parentalapp.reward;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.parentalapp.R;
import com.example.parentalapp.admin.apprestrict.AppRestrictConfig;
import com.example.parentalapp.admin.rewardpoint.RewardPointConfig;
import com.example.parentalapp.admin.screentime.TimeSettingHelper;
import com.example.parentalapp.reward.rewardhistory.RewardHistoryDBHelper;

import static com.example.parentalapp.reward.RewardMainActivity.ITEM_NAME;
import static com.example.parentalapp.reward.RewardMainActivity.POINTS;
import static com.example.parentalapp.reward.RewardMainActivity.QUANTITY;

import static com.example.parentalapp.reward.RewardDBHelper.REWARD_EFFECT_ITEM;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_EFFECT_VALUE;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_ITEM_ID;

public class RewardResultActivity extends AppCompatActivity {

    private RewardDBHelper rewardDBHelper;
    private String itemName, effectItem, effectValue;
    private int id, quantity, point;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_result);

        Button button_return = findViewById(R.id.button_back_to_store);
        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RewardMainActivity.class));
            }
        });

        rewardDBHelper = new RewardDBHelper(getApplicationContext());

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
            }else if (effectItem.compareTo("app") == 0){
                // app restriction reward
                // TODO: allow user to choose app
                AppRestrictConfig appRestrictConfig = new AppRestrictConfig(this);
                appRestrictConfig.changePermission(itemName, true);
            }else if (effectItem.compareTo("custom") == 0){
                // custom reward
                // TODO: put reward into unresolved list

            }
        }
        deductPoint();
        addToHistory();
    }

    private void deductPoint(){
        RewardPointConfig rewardPointConfig = new RewardPointConfig(getApplicationContext());
        rewardPointConfig.deductRewardPoints(point * quantity);
    }

    private void addToHistory(){
        RewardHistoryDBHelper rewardHistoryDBHelper = new RewardHistoryDBHelper(getApplicationContext());
        rewardHistoryDBHelper.addRecord(itemName, id);
    }
}