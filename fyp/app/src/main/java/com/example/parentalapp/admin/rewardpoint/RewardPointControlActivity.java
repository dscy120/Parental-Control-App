package com.example.parentalapp.admin.rewardpoint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.admin.ParentMainActivity;

public class RewardPointControlActivity extends AppCompatActivity {

    private TextView textViewCurrentPt;
    private EditText editTextAdd, editTextDeduct;
    private Button buttonAdd, buttonDeduct;
    private RewardPointHelper rewardPointHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_point_control);

        // back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        rewardPointHelper = new RewardPointHelper(getBaseContext());

        textViewCurrentPt = findViewById(R.id.textView_current_reward_points);

        editTextAdd = findViewById(R.id.editText_points_add);
        editTextDeduct = findViewById(R.id.editText_points_deduct);

        buttonAdd = findViewById(R.id.button_points_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rewardPointHelper.setRewardPoints(rewardPointHelper.getCurrentPoints() + Integer.parseInt(editTextAdd.getText().toString()));
                    editTextAdd.setText("");
                    updateText();
                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Please input a number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDeduct = findViewById(R.id.button_points_deduct);
        buttonDeduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int currentPoints = rewardPointHelper.getCurrentPoints();
                    int deductPoints = Integer.parseInt(editTextDeduct.getText().toString());
                    if (currentPoints - deductPoints >= 0){
                        rewardPointHelper.setRewardPoints(currentPoints - deductPoints);
                    }else{
                        rewardPointHelper.setRewardPoints(0);
                    }
                    editTextDeduct.setText("");
                    updateText();
                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "Please input a number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, ParentMainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateText(){
        textViewCurrentPt.setText(String.valueOf(rewardPointHelper.getCurrentPoints()));
    }
}