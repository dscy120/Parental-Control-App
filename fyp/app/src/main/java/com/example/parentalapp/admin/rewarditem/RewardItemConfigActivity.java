package com.example.parentalapp.admin.rewarditem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.admin.ParentMainActivity;
import com.example.parentalapp.reward.RewardDBHelper;
import com.example.parentalapp.reward.RewardItem;
import com.example.parentalapp.reward.RewardMainActivity;
import com.example.parentalapp.reward.RewardViewAdapter;

import java.util.ArrayList;

public class RewardItemConfigActivity extends AppCompatActivity implements RewardViewAdapter.RewardClickListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RewardItem> rewardItemList;
    private RewardDBHelper rewardDBHelper;
    private Button addItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reward_item_config);

        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        rewardDBHelper = new RewardDBHelper(getApplicationContext());
        rewardItemList = rewardDBHelper.getRewardItem();

        recyclerView = findViewById(R.id.recyclerView_reward_item_setting);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RewardViewAdapter(rewardItemList, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        addItem = findViewById(R.id.button_add_reward_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddRewardItemActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), ParentMainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // refresh the list
    }

    @Override
    public void onRewardClick(int position) {
        // Remove item
        RewardItem rewardItem = rewardItemList.get(position);
        if(rewardItem.getEffectItem().compareTo("time") == 0 || rewardItem.getEffectItem().compareTo("app") == 0){
            Toast.makeText(getApplicationContext(), "Default item cannot be modified", Toast.LENGTH_SHORT).show();
        }else{
            openDeleteDialog(rewardItem);
        }
    }

    private void openDeleteDialog(RewardItem rewardItem){
        // open delete dialog
        Bundle bundle = new Bundle();
        bundle.putInt(RewardDBHelper.REWARD_ITEM_ID, rewardItem.getId());

        DeleteRewardDialog deleteRewardDialog = new DeleteRewardDialog();
        deleteRewardDialog.setArguments(bundle);
        deleteRewardDialog.show(getSupportFragmentManager(), "delete reward dialog");
    }
}