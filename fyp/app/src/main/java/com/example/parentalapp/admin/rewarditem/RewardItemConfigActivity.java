// Configure reward item list, add or delete custom item
// Includes an unresolved list
package com.example.parentalapp.admin.rewarditem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.parentalapp.R;
import com.example.parentalapp.admin.ParentMainActivity;
import com.example.parentalapp.admin.rewarditem.unresolves.UnresolvedReward;
import com.example.parentalapp.admin.rewarditem.unresolves.UnresolvedRewardDBHelper;
import com.example.parentalapp.admin.rewarditem.unresolves.UnresolvedRewardViewAdapter;
import com.example.parentalapp.reward.RewardDBHelper;
import com.example.parentalapp.reward.RewardItem;
import com.example.parentalapp.reward.RewardViewAdapter;

import java.util.ArrayList;

public class RewardItemConfigActivity extends AppCompatActivity implements RewardViewAdapter.RewardClickListener, UnresolvedRewardViewAdapter.UnresolvedRewardClickListener {

    private RecyclerView recyclerViewRewardItem;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<RewardItem> rewardItemList;
    private RewardDBHelper rewardDBHelper;
    private Button addItem;
    private boolean back = false;

    private RecyclerView recyclerViewUnresolveReward;
    private RecyclerView.Adapter adapterUnresolveReward;
    private RecyclerView.LayoutManager layoutManagerUnresolvedReward;
    private ArrayList<UnresolvedReward> unresolvedRewardList;
    private UnresolvedRewardDBHelper unresolvedRewardDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reward_item_config);

        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setRecyclerViewRewardItem();
        setRecyclerViewUnresolveReward();
    }

    @Override
    protected void onStart() {
        super.onStart();
        back = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), ParentMainActivity.class));
                back = true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        back = true;
        startActivity(new Intent(getApplicationContext(), ParentMainActivity.class));
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

    private void setRecyclerViewRewardItem(){
        rewardDBHelper = new RewardDBHelper(getApplicationContext());
        rewardItemList = rewardDBHelper.getRewardItem();

        recyclerViewRewardItem = findViewById(R.id.recyclerView_reward_item_setting);
        recyclerViewRewardItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new RewardViewAdapter(rewardItemList, this);

        recyclerViewRewardItem.setLayoutManager(layoutManager);
        recyclerViewRewardItem.setAdapter(adapter);

        addItem = findViewById(R.id.button_add_reward_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddRewardItemActivity.class));
            }
        });
    }

    private void setRecyclerViewUnresolveReward(){
        unresolvedRewardDBHelper = new UnresolvedRewardDBHelper(getApplicationContext());
        unresolvedRewardList = unresolvedRewardDBHelper.getUnresolvedReward();

        recyclerViewUnresolveReward = findViewById(R.id.recyclerView_unresolved_reward);
        recyclerViewUnresolveReward.setHasFixedSize(true);
        layoutManagerUnresolvedReward = new LinearLayoutManager(this);
        adapterUnresolveReward = new UnresolvedRewardViewAdapter(unresolvedRewardList, this);

        recyclerViewUnresolveReward.setLayoutManager(layoutManagerUnresolvedReward);
        recyclerViewUnresolveReward.setAdapter(adapterUnresolveReward);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh the list
        adapter.notifyDataSetChanged();
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

    @Override
    public void onUnresolvedClick(final int position) {
        // option to resolved the reward
        final UnresolvedReward unresolvedReward = unresolvedRewardList.get(position);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Resolve Item")
                .setMessage("Do you want to resolve this item?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // change reward status to resolved
                        unresolvedRewardDBHelper.changeStatus(unresolvedReward.getId(), UnresolvedRewardDBHelper.UNRESOLVED_REWARD_STATUS_RESOVLED);
                        unresolvedRewardList.remove(position);
                        adapterUnresolveReward.notifyItemRemoved(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // empty cancel button
                    }
                })
                .show();
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