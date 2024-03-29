// Background Service that keeps track of the remaining screen time of user
// Remaining time is shown in notification bar
package com.example.parentalapp.playground;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.admin.screentime.TimeSettingHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static com.example.parentalapp.MainActivity.ACTIVE_TIME_SERVICE;
import static com.example.parentalapp.playground.ScreenTimeApp.CHANNEL_ID;

public class ScreenTimeService extends Service {

    public static final int NOTIFICATION_CHANNEL = 101;
    private TimeSettingHelper timeSettingHelper;
    private ScreenTimer timer;
    private SharedPreferences sharedPreferences;

    private long remainingTime;

    @Override
    public void onCreate() {
        super.onCreate();
        timeSettingHelper = new TimeSettingHelper(getBaseContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //timer part
        remainingTime = timeSettingHelper.getRemainingTime() * 1000; // get remaining Time in preferences.xml
        timer = new ScreenTimer(remainingTime, 1000);
        timer.start();

        // notification part

        // create new notification with text
        Notification notification = setNotification("");
        startForeground(NOTIFICATION_CHANNEL, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        timeSettingHelper.setRemainingScreenTime(remainingTime / 1000);
        sharedPreferences.edit().putBoolean(ACTIVE_TIME_SERVICE, false).apply();
        // jump back to main page
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        mainActivity.addCategory(Intent.CATEGORY_LAUNCHER);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
        Toast.makeText(getBaseContext(), "Screen time stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Notification setNotification(String input){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Screen Time Remaining")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();
        return notification;
    }

    public void updateNotification(String input){
        Notification notification = setNotification(input);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_CHANNEL, notification);
    }

    public class ScreenTimer extends CountDownTimer {

        public ScreenTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            remainingTime = millisUntilFinished;
            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            //Track remaining time on notification
            updateNotification(hms);

            if(!checkActiveHour()){
                stopSelf();
            }

            // Show time on main activity
            // EventBus post
            //EventBus.getDefault().post(new MessageEvent(hms));

        }

        // check current time against the active hour set by admin
        private boolean checkActiveHour(){
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            String[] currentTime = format.format(Calendar.getInstance().getTime()).split(":");

            int currentHour = Integer.parseInt(currentTime[0]);
            int currentMinute = Integer.parseInt(currentTime[1]);

            int hourStart = timeSettingHelper.getActiveHourStart();
            int hourEnd = timeSettingHelper.getActiveHourEnd();
            int minuteStart = timeSettingHelper.getActiveMinuteStart();
            int minuteEnd = timeSettingHelper.getActiveMinuteEnd();

            if(currentHour - hourStart < hourEnd - hourStart){
                return true;
            }else if(currentHour == hourStart && currentMinute > minuteStart){
                return true;
            }else if(currentHour == hourEnd && currentMinute < minuteEnd){
                return true;
            }
            return false;
        }

        @Override
        public void onFinish() {
            stopSelf();
        }
    }
}
