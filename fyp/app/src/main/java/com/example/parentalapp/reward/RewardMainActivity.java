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

public class RewardMainActivity extends AppCompatActivity implements RewardViewAdapter.RewardClickListener{

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView rewardPoints;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RewardItem> rewardItemList;
    private RewardDBHelper rewardDBHelper;
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

    private void setTextView(){
        rewardPoints = findViewById(R.id.textView_rewardPoint);
        String rewardPointText = sharedPreferences.getInt(REWARD_POINTS, 0) + " points";
        rewardPoints.setText(rewardPointText);
    }

    // Initialize or reset total reward points
    public void initPoints(){
        sharedPreferences.edit().putInt(REWARD_POINTS, 0);
    }

    @Override
    public void onRewardClick(int position) {

    }
}