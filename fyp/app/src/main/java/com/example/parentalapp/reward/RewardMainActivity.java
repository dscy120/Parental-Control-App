package com.example.parentalapp.reward;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.quiz.record.RecordItem;
import com.example.parentalapp.quiz.record.RecordViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.parentalapp.admin.rewardpoint.RewardPointHelper.REWARD_POINTS;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_ITEM_ID;

public class RewardMainActivity extends AppCompatActivity implements RewardViewAdapter.RewardClickListener{

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView rewardPoints;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RewardItem> rewardItemList;
    private RewardDBHelper rewardDBHelper;
    private int allowance;

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

        rewardDBHelper = new RewardDBHelper(getApplicationContext());
        rewardItemList = rewardDBHelper.getRewardItem();

        recyclerView = findViewById(R.id.recyclerView_rewardList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RewardViewAdapter(rewardItemList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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

        Intent i = new Intent(this, RewardDetailActivity.class);
        i.putExtras(bundle);
        startActivity(i);
    }
}