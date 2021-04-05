// Page for accessing games and other entertainment apps
// Subject to screen time control
package com.example.parentalapp.playground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.admin.apprestrict.AppRestrictConfig;

import java.util.ArrayList;
import java.util.List;

import static com.example.parentalapp.MainActivity.ACTIVE_TIME_SERVICE;

public class PlaygroundActivity extends AppCompatActivity implements PlaygroundViewAdapter.PlaygroundClickListener {
    private SharedPreferences sharedPreferences;
    private ArrayList<ResolveInfo> newList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private AppRestrictConfig appRestrictConfig;
    private boolean applaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        appRestrictConfig = new AppRestrictConfig(this);
        availableApps();
        setAppList(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disable recent apps button
        if(!applaunch){
            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);

            activityManager.moveTaskToFront(getTaskId(), 0);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        applaunch = false;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // disable back button
    @Override
    public void onBackPressed() {
        // empty function
    }

    // disable recent apps button

    public void availableApps(){
        newList = appRestrictConfig.getApplicationList(true);
    }

    public void setAppList(Context context){
        recyclerView = findViewById(R.id.recyclerView_playground);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        adapter = new PlaygroundViewAdapter(newList, this, context);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAppClick(int position) {
        applaunch = true;

        ResolveInfo resolveInfo = newList.get(position);
        ComponentName name = new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name);
        Intent i = new Intent(Intent.ACTION_MAIN);

        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        i.setComponent(name);
        startActivity(i);
    }

    public void stopTimerService(View v){
        sharedPreferences.edit().putBoolean(ACTIVE_TIME_SERVICE, false).apply();
        stopService(new Intent(PlaygroundActivity.this, ScreenTimeService.class));
    }
}