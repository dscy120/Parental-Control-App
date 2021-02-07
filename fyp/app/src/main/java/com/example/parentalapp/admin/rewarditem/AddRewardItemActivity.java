package com.example.parentalapp.admin.rewarditem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.admin.ParentMainActivity;
import com.example.parentalapp.reward.RewardDBHelper;

public class AddRewardItemActivity extends AppCompatActivity {

    private EditText itemName, requiredPoints;
    private Button confirm;
    private RewardDBHelper rewardDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reward_item);

        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        rewardDBHelper = new RewardDBHelper(getApplicationContext());

        itemName = findViewById(R.id.editText_input_item_name);
        requiredPoints = findViewById(R.id.editText_input_points_required);

        confirm = findViewById(R.id.button_add_new_item);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addItem()){
                    startActivity(new Intent(getApplicationContext(), RewardItemConfigActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(), "Unable to add item", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), RewardItemConfigActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean addItem(){
        return rewardDBHelper.addItem(itemName.getText().toString(), Integer.parseInt(requiredPoints.getText().toString()), "custom", "");
    }
}