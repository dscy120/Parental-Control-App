// Reward Detail page
package com.example.parentalapp.reward.apprestrict;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.parentalapp.R;
import com.example.parentalapp.admin.ParentMainActivity;
import com.example.parentalapp.admin.apprestrict.AppRestrictConfig;
import com.example.parentalapp.playground.PlaygroundViewAdapter;
import com.example.parentalapp.reward.RewardConfirmDialog;
import com.example.parentalapp.reward.RewardMainActivity;
import com.example.parentalapp.reward.RewardResultActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.parentalapp.reward.RewardDBHelper.REWARD_APP_POSITION;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_EFFECT_ITEM;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_ITEM_ID;
import static com.example.parentalapp.reward.RewardMainActivity.ALLOWANCE;
import static com.example.parentalapp.reward.RewardMainActivity.ITEM_NAME;
import static com.example.parentalapp.reward.RewardMainActivity.POINTS;
import static com.example.parentalapp.reward.RewardMainActivity.QUANTITY;

public class RewardDetailAppActivity extends AppCompatActivity implements PlaygroundViewAdapter.PlaygroundClickListener, RewardConfirmDialog.RewardConfirmDialogListener {

    private AppRestrictConfig appRestrictConfig;
    private PackageManager pm;
    private ArrayList<ResolveInfo> newList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Bundle bundleRewardDetail;
    private String itemName, effectItem;
    private int itemQuantity, requiredPoints, allowance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_detail_app);

        // top back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        pm = getPackageManager();
        initialize();

        // get the list of applications
        appRestrictConfig = new AppRestrictConfig(this);
        newList = appRestrictConfig.getApplicationList(false);

        setRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), RewardMainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void initialize(){
        requiredPoints = getIntent().getExtras().getInt(POINTS);
        itemQuantity = 1;
        allowance = getIntent().getExtras().getInt(ALLOWANCE);
        effectItem = getIntent().getExtras().getString(REWARD_EFFECT_ITEM);
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.recyclerView_app_reward);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new PlaygroundViewAdapter(newList, this, getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAppClick(int position) {
        if(allowance >= requiredPoints){
            ResolveInfo resolveInfo = newList.get(position);
            itemName = pm.getApplicationLabel(resolveInfo.activityInfo.applicationInfo).toString();

            // launch confirm dialog
            RewardConfirmDialog rewardConfirmDialog = new RewardConfirmDialog();
            bundleRewardDetail = new Bundle();
            bundleRewardDetail.putInt(REWARD_ITEM_ID, 2);
            bundleRewardDetail.putString(ITEM_NAME, itemName);
            bundleRewardDetail.putString(POINTS, String.valueOf(requiredPoints));
            bundleRewardDetail.putString(QUANTITY, String.valueOf(itemQuantity));
            bundleRewardDetail.putInt(REWARD_APP_POSITION, position);
            rewardConfirmDialog.setArguments(bundleRewardDetail);
            rewardConfirmDialog.show(getSupportFragmentManager(), "Reward Confirm Dialog");
        }else{
            Toast.makeText(getApplicationContext(), "You don't have enough points!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void purchase() {
        Intent i = new Intent(this, RewardResultActivity.class);
        i.putExtras(bundleRewardDetail);
        startActivity(i);
    }
}