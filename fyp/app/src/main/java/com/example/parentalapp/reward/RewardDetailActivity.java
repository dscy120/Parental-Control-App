package com.example.parentalapp.reward;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;

import static com.example.parentalapp.reward.RewardMainActivity.ALLOWANCE;
import static com.example.parentalapp.reward.RewardMainActivity.ITEM_NAME;
import static com.example.parentalapp.reward.RewardMainActivity.POINTS;
import static com.example.parentalapp.reward.RewardMainActivity.QUANTITY;
import static com.example.parentalapp.reward.RewardDBHelper.REWARD_ITEM_ID;

public class RewardDetailActivity extends AppCompatActivity implements RewardConfirmDialog.RewardConfirmDialogListener{

    private TextView textViewquantity, textViewItem, textViewPoint;
    private int id, itemQuantity, requiredPoints, allowance;
    private String itemName;
    private Bundle bundleRewardDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_detail);

        //back button
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

        id = getIntent().getExtras().getInt(REWARD_ITEM_ID);
        requiredPoints = getIntent().getExtras().getInt(POINTS);
        itemName = getIntent().getExtras().getString(ITEM_NAME);
        itemQuantity = 0;
        allowance = getIntent().getExtras().getInt(ALLOWANCE);

        updateText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, RewardMainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateText(){
        textViewItem.setText(itemName);
        textViewPoint.setText("Required points: " + (requiredPoints * itemQuantity) );
        textViewquantity.setText(String.valueOf(itemQuantity));
    }

    private void setButtons(){
        Button button_add = findViewById(R.id.button_add_item_quantity);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requiredPoints * (itemQuantity + 1) <= allowance){
                    itemQuantity++;
                }else{
                    Toast.makeText(getApplicationContext(), "You don't have enough points", Toast.LENGTH_SHORT).show();
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

    @Override
    public void purchase() {
        // TODO: purchase action
        Intent i = new Intent(this, RewardResultActivity.class);
        i.putExtras(bundleRewardDetail);
        startActivity(i);
    }
}