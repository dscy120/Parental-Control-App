package com.example.parentalapp.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;

public class PinActivity extends AppCompatActivity {
    private EditText editText_pin;
    private Button button_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editText_pin = (EditText)findViewById(R.id.editTextNumber);
        button_confirm = (Button)findViewById(R.id.button_confirm);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText_pin.getText().toString().equals("1234")){
                    Intent i = new Intent();
                    startActivity(new Intent(getApplicationContext(), ParentMainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Wrong pin",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}