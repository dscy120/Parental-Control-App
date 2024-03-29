package com.example.parentalapp.reward;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;

import static com.example.parentalapp.reward.RewardDBHelper.REWARD_EFFECT_ITEM;
import static com.example.parentalapp.reward.RewardMainActivity.ALLOWANCE;
import static com.example.parentalapp.reward.RewardMainActivity.ITEM_NAME;
import static com.example.parentalapp.reward.RewardMainActivity.POINTS;
import static com.example.parentalapp.reward.RewardMainActivity.QUANTITY;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_ITEM_ID;

public class RewardDetailActivity extends AppCompatActivity implements RewardConfirmDialog.RewardConfirmDialogListener{

    private TextView textViewquantity, textViewItem, textViewPoint, textViewTime;
    private int id, itemQuantity, requiredPoints, allowance;
    private String itemName, effectItem;
    private Bundle bundleRewardDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_detail);

        // top back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initialize();
        setButtons();
    }

    private void initialize(){
        // set TextView
        textViewquantity = findViewById(R.id.textView_dialog_item_quantity);
        textViewItem = findViewById(R.id.textView_dialog_item_name);
        textViewPoint = findViewById(R.id.textView_dialog_required_points);
        textViewTime = findViewById(R.id.textView_effective_time);

        // get item details from previous activity
        id = getIntent().getExtras().getInt(REWARD_ITEM_ID);
        requiredPoints = getIntent().getExtras().getInt(POINTS);
        itemName = getIntent().getExtras().getString(ITEM_NAME);
        itemQuantity = 0;
        allowance = getIntent().getExtras().getInt(ALLOWANCE);
        effectItem = getIntent().getExtras().getString(REWARD_EFFECT_ITEM);

        updateText();
    }

    // back button
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

    // refresh textView content
    private void updateText(){
        textViewItem.setText(itemName);
        textViewPoint.setText("Required points: " + (requiredPoints * itemQuantity) );
        textViewquantity.setText(String.valueOf(itemQuantity));
        if(effectItem.compareTo("time") == 0){
            textViewTime.setText("Total: " + 15 * itemQuantity + " minutes");
        }else{
            textViewTime.setText("");
        }
    }

    // set add and minus button function
    private void setButtons(){
        Button button_add = findViewById(R.id.button_add_item_quantity);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requiredPoints * (itemQuantity + 1) <= allowance){
                    itemQuantity++;
                }else{
                    Toast.makeText(getApplicationContext(), "You don't have enough points!", Toast.LENGTH_SHORT).show();
                }
                updateText();
            }
        });

        Button button_reduce = findViewById(R.id.button_reduce_item_quantity);
        button_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemQuantity > 0){
                    itemQuantity--;
                }
                updateText();
            }
        });

        Button button_purchase = findViewById(R.id.button_purchase);
        button_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemQuantity > 0){
                    launchConfirmDialog();
                }else{
                    Toast.makeText(getApplicationContext(), "At least 1 item should be selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // opening the confirmation dialog
    private void launchConfirmDialog(){
        RewardConfirmDialog rewardConfirmDialog = new RewardConfirmDialog();
        bundleRewardDetail = new Bundle();
        bundleRewardDetail.putInt(REWARD_ITEM_ID, id);
        bundleRewardDetail.putString(ITEM_NAME, itemName);
        bundleRewardDetail.putString(POINTS, String.valueOf(requiredPoints));
        bundleRewardDetail.putString(QUANTITY, String.valueOf(itemQuantity));
        rewardConfirmDialog.setArguments(bundleRewardDetail);
        rewardConfirmDialog.show(getSupportFragmentManager(), "Reward Confirm Dialog");
    }

    // operation of the purchase
    @Override
    public void purchase() {
        Intent i = new Intent(this, RewardResultActivity.class);
        i.putExtras(bundleRewardDetail);
        startActivity(i);
    }
}