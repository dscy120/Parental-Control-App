package com.example.parentalapp.reward;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;

import static com.example.parentalapp.reward.RewardMainActivity.ALLOWANCE;
import static com.example.parentalapp.reward.RewardMainActivity.ITEM_NAME;
import static com.example.parentalapp.reward.RewardMainActivity.POINTS;
import static com.example.parentalapp.reward.RewardMainActivity.QUANTITY;

public class RewardDetailActivity extends AppCompatActivity implements RewardConfirmDialog.RewardConfirmDialogListener{

    private TextView textViewquantity, textViewItem, textViewPoint;
    private int itemQuantity, requiredPoints, allowance;
    private String itemName;

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
                launchConfirmDialog();
            }
        });
    }

    private void launchConfirmDialog(){
        RewardConfirmDialog rewardConfirmDialog = new RewardConfirmDialog();
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_NAME, itemName);
        bundle.putString(POINTS, String.valueOf(requiredPoints));
        bundle.putString(QUANTITY, String.valueOf(itemQuantity));
        rewardConfirmDialog.setArguments(bundle);
        rewardConfirmDialog.show(getSupportFragmentManager(), "Reward Confirm Dialog");
    }

    @Override
    public void purchase() {
        // TODO: purchase action
    }
}