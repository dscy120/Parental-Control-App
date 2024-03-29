// Reward store main page
// For child user to exchange rewards using reward points
package com.example.parentalapp.reward;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.reward.apprestrict.RewardDetailAppActivity;
import com.example.parentalapp.reward.rewardhistory.RewardHistoryActivity;

import java.util.ArrayList;

import static com.example.parentalapp.admin.rewardpoint.RewardPointConfig.REWARD_POINTS;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_EFFECT_ITEM;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_ITEM_ID;

public class RewardMainActivity extends AppCompatActivity implements RewardViewAdapter.RewardClickListener{

    private SharedPreferences sharedPreferences;
    private TextView rewardPoints;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RewardItem> rewardItemList;
    private RewardDBHelper rewardDBHelper;
    private int allowance;
    private boolean back = false;

    public static final String ITEM_NAME = "item_name";
    public static final String POINTS = "points";
    public static final String ALLOWANCE = "allowance";
    public static final String QUANTITY = "quantity";

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

        allowance = sharedPreferences.getInt(REWARD_POINTS, 0);

        rewardPoints = findViewById(R.id.textView_rewardPoint);
        rewardPoints.setText(String.valueOf(allowance));

        // recyclerView setup
        rewardDBHelper = new RewardDBHelper(getApplicationContext());
        rewardItemList = rewardDBHelper.getRewardItem();

        recyclerView = findViewById(R.id.recyclerView_rewardList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RewardViewAdapter(rewardItemList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // launcher for purchase history
        Button button_test = findViewById(R.id.button_purchase_history);
        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RewardHistoryActivity.class));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                back = true;
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        back = true;
        startActivity(new Intent(this, MainActivity.class));
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

    // Initialize or reset total reward points
    public void initPoints(){
        sharedPreferences.edit().putInt(REWARD_POINTS, 0);
    }

    @Override
    public void onRewardClick(int position) {
        RewardItem rewardItem = rewardItemList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt(REWARD_ITEM_ID, rewardItem.getId());
        bundle.putString(ITEM_NAME, rewardItem.getName());
        bundle.putInt(POINTS, rewardItem.getPoint());
        bundle.putInt(ALLOWANCE, allowance);
        bundle.putString(REWARD_EFFECT_ITEM, rewardItem.getEffectItem());

        Intent i;
        if(rewardItem.getEffectItem().compareTo("app") == 0){
            i = new Intent(this, RewardDetailAppActivity.class);
        }else{
            i = new Intent(this, RewardDetailActivity.class);
        }
        i.putExtras(bundle);
        startActivity(i);
    }
}