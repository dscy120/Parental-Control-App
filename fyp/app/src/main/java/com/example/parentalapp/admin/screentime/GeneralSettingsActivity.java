package com.example.parentalapp.admin.screentime;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.parentalapp.R;
import com.example.parentalapp.admin.ParentMainActivity;


public class GeneralSettingsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private TextView remainTime, activeHourStart, activeHourEnd;
    private Button dialog;
    private NumberPicker numberPickerHour, numberPickerMinute;
    private boolean activeTimeStart = true;
    private TimeSettingHelper timeSettingHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
                finish();
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
        Long remainingTime = timeSettingHelper.getRemainingTime();

        numberPickerHour = customLayout.findViewById(R.id.countDownTimePicker_hour);
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);
        numberPickerHour.setValue(timeSettingHelper.convertHour(remainingTime));

        numberPickerMinute = customLayout.findViewById(R.id.countDownTimePicker_minute);
        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        };
        numberPickerMinute.setFormatter(formatter);
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);
        numberPickerMinute.setValue(timeSettingHelper.convertMinute(remainingTime));
    }

    public void setTimerPreference(){
        int hour = numberPickerHour.getValue();
        int minute = numberPickerMinute.getValue();
        timeSettingHelper.setRemainingScreenTime(hour * 60 * 60 + minute * 60);
    }

    public void showRemainingTime(){
        remainTime = findViewById(R.id.textView_remainTime);
        Long r = timeSettingHelper.getRemainingTime();
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
            timeSettingHelper.setActiveHourStart(hourOfDay);
            timeSettingHelper.setActiveMinuteStart(minute);
            activeHourStart = findViewById(R.id.textView_ActiveHour_start);
            activeHourStart.setText(timeSettingHelper.getActiveHourStartString());
        }else{
            timeSettingHelper.setActiveHourEnd(hourOfDay);
            timeSettingHelper.setActiveMinuteEnd(minute);
            activeHourEnd = findViewById(R.id.textView_AcitveHour_end);
            activeHourEnd.setText(timeSettingHelper.getActiveHourEndString());
        }

    }

    public void setActiveHour(){
        activeHourStart = findViewById(R.id.textView_ActiveHour_start);
        activeHourStart.setText(timeSettingHelper.getActiveHourStartString());
        activeHourStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeTimeStart = true;
                DialogFragment timePicker = new ActiveHourFragment("start");
                timePicker.show(getSupportFragmentManager(), "Active Hour");
            }
        });

        activeHourEnd = findViewById(R.id.textView_AcitveHour_end);
        activeHourEnd.setText(timeSettingHelper.getActiveHourEndString());
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