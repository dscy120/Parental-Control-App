package com.example.parentalapp.admin;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.example.parentalapp.R;

import static com.example.parentalapp.admin.TimeSettingHelper.ACTIVE_HOUR_START;
import static com.example.parentalapp.admin.TimeSettingHelper.ACTIVE_MINUTE_START;
import static com.example.parentalapp.admin.TimeSettingHelper.ACTIVE_HOUR_END;
import static com.example.parentalapp.admin.TimeSettingHelper.ACTIVE_MINUTE_END;


public class GeneralSettingsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    public static final String time = "screenTime";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView remainTime, activeHourStart, activeHourEnd;
    private Button dialog;
    private NumberPicker numberPickerHour, numberPickerMinute;
    private boolean activeTimeStart = true;
    private TimeSettingHelper timeSettingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        editor = sharedPreferences.edit();

        timeSettingHelper = new TimeSettingHelper(getBaseContext());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        showRemainingTime();

        dialog = findViewById(R.id.button_screenTime_dialog);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimeDialog();
            }
        });

        setActiveHour();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, ParentActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openTimeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Time");

        final View customLayout = getLayoutInflater().inflate(R.layout.number_picker_time, null);
        builder.setView(customLayout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setTimerPreference();
                showRemainingTime();
            }
        });

        AlertDialog alertDialog = builder.create();

        setTimePickerDisplay(customLayout);

        alertDialog.show();
    }

    public void setTimePickerDisplay(View customLayout){
        Long remainingTime = getRemainingTime();

        numberPickerHour = customLayout.findViewById(R.id.countDownTimePicker_hour);
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);
        numberPickerHour.setValue(timeSettingHelper.convertHour(remainingTime));

        numberPickerMinute = customLayout.findViewById(R.id.countDownTimePicker_minute);
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);
        numberPickerMinute.setValue(timeSettingHelper.convertMinute(remainingTime));
    }

    public void setTimerPreference(){
        int hour = numberPickerHour.getValue();
        int minute = numberPickerMinute.getValue();
        editor.putString(time, String.valueOf(hour * 60 * 60 + minute * 60));
        editor.apply();
    }

    public Long getRemainingTime(){
        return Long.parseLong(sharedPreferences.getString(time, "0"));
    }

    public void showRemainingTime(){
        remainTime = findViewById(R.id.textView_remainTime);
        Long r = getRemainingTime();
        int h = timeSettingHelper.convertHour(r);
        int m = timeSettingHelper.convertMinute(r);
        String hour = h > 1 ? " hours " : " hour ";
        String minute = m > 1 ? " minutes " : " minute ";
        String remainTimeShow = h + hour + m + minute;
        remainTime.setText(remainTimeShow);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(activeTimeStart){
            editor.putInt(ACTIVE_HOUR_START, hourOfDay);
            editor.putInt(ACTIVE_MINUTE_START, minute);
            editor.apply();
            activeHourStart = findViewById(R.id.textView_ActiveHour_start);
            activeHourStart.setText(timeSettingHelper.getActiveHourStart());
        }else{
            editor.putInt(ACTIVE_HOUR_END, hourOfDay);
            editor.putInt(ACTIVE_MINUTE_END, minute);
            editor.apply();
            activeHourEnd = findViewById(R.id.textView_AcitveHour_end);
            activeHourEnd.setText(timeSettingHelper.getActiveHourEnd());
        }

    }

    public void setActiveHour(){
        activeHourStart = findViewById(R.id.textView_ActiveHour_start);
        activeHourStart.setText(timeSettingHelper.getActiveHourStart());
        activeHourStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTimeStart = true;
                DialogFragment timePicker = new ActiveHourFragment("start");
                timePicker.show(getSupportFragmentManager(), "Active Hour");
            }
        });

        activeHourEnd = findViewById(R.id.textView_AcitveHour_end);
        activeHourEnd.setText(timeSettingHelper.getActiveHourEnd());
        activeHourEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTimeStart = false;
                DialogFragment timePicker = new ActiveHourFragment("end");
                timePicker.show(getSupportFragmentManager(), "Active Hour");
            }
        });
    }
}