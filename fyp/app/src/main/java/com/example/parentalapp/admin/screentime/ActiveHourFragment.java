package com.example.parentalapp.admin.screentime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import static com.example.parentalapp.admin.screentime.TimeSettingHelper.ACTIVE_HOUR_END;
import static com.example.parentalapp.admin.screentime.TimeSettingHelper.ACTIVE_HOUR_START;
import static com.example.parentalapp.admin.screentime.TimeSettingHelper.ACTIVE_MINUTE_END;
import static com.example.parentalapp.admin.screentime.TimeSettingHelper.ACTIVE_MINUTE_START;

public class ActiveHourFragment extends DialogFragment {
    private SharedPreferences sharedPreferences;
    private String activeHour, activeMinute;

    public ActiveHourFragment(String time) {
        if (time.compareTo("start") == 0){
            activeHour = ACTIVE_HOUR_START;
            activeMinute = ACTIVE_MINUTE_START;
        }else{
            activeHour = ACTIVE_HOUR_END;
            activeMinute = ACTIVE_MINUTE_END;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int hour = sharedPreferences.getInt(activeHour, 0);
        int minute = sharedPreferences.getInt(activeMinute, 0);

        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
    }
}
