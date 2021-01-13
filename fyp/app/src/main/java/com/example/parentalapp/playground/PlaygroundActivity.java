// Desktop for accessing games and other entertainment apps
package com.example.parentalapp.playground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.parentalapp.R;

import java.util.ArrayList;
import java.util.List;

public class PlaygroundActivity extends AppCompatActivity {
    private ConstraintLayout main;
    private int button_layout_margin = 50, button_size = 170;
    private int appsPerRow = 5;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);
        sharedPreferences = getSharedPreferences("appPreference", MODE_PRIVATE);
        createDesktopShortcut(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        // Back button disabled
    }

    // Create shortcuts to installed apps
    public void createDesktopShortcut(Context context){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // List of all installed apps
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> pkgAppsList = pm.queryIntentActivities( mainIntent, 0);
        List<ResolveInfo> newList = new ArrayList<ResolveInfo>();

        // Check for allowed apps
        for(ResolveInfo r : pkgAppsList){
            ActivityInfo a = r.activityInfo;
            String appname = pm.getApplicationLabel(a.applicationInfo).toString();
            if (sharedPreferences.getBoolean(appname, false)){
                newList.add(r);
            }
        }

        // Creating shortcuts to other installed apps (dynamically)
        for (int i = 0; i < newList.size(); i++){
            Button b = new Button(this);
            b.setId(100 + i);
            final int id = b.getId();

            // button icon
            final ActivityInfo activityInfo = newList.get(i).activityInfo;
            try {
                Drawable icon = pm.getApplicationIcon(activityInfo.packageName);
                b.setBackground(icon);
                b.setText(pm.getApplicationLabel(activityInfo.applicationInfo));
            }catch (PackageManager.NameNotFoundException ne){
                System.out.println("Name not found");
            }

            // button launch app action
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ComponentName name = new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
                    Intent i = new Intent(Intent.ACTION_MAIN);

                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    i.setComponent(name);
                    startActivity(i);
                }
            });

            main = findViewById(R.id.playGroundLayout);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(button_size, button_size);
            main.addView(b, params);

            setButtonConstrain(id, i, newList.size());
        }
    }

    public void stopTimerService(View v){
        stopService(new Intent(PlaygroundActivity.this, ScreenTimeService.class));
    }

    // Sets button position
    public void setButtonConstrain(int id, int i, int totalSize){
        int modx = i % (appsPerRow);
        ConstraintSet set = new ConstraintSet();
        set.clone(main);
        set.constrainDefaultWidth(id, ConstraintSet.MATCH_CONSTRAINT_SPREAD);
        if (modx == 0){
            // link left edge to screen left edge if it's the first app of the row
            set.connect(id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
        }else{
            // link left edge to previous app's right edge on the row
            set.connect(id, ConstraintSet.LEFT, id - 1, ConstraintSet.RIGHT);
        }

        if (i == totalSize - 1 && modx < appsPerRow - 1){
            // exceptional case for last app when number of apps is not multiple of 5
            // link right edge to app's right edge on previous row if it is the last app
            set.connect(id, ConstraintSet.RIGHT, id - (appsPerRow - 1), ConstraintSet.LEFT);
        }else if (modx == appsPerRow - 1) {
            // link right edge to screen right edge if it is the last app on the row
            set.connect(id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
        }

        if (modx > 0){
            // link previous app's right edge to this left edge
            set.connect(id - 1, ConstraintSet.RIGHT, id, ConstraintSet.LEFT);
        }

        if (i < appsPerRow){
            // link first row apps' top edge to stop timer button edge
            set.connect(id, ConstraintSet.TOP, R.id.button_stopTimer, ConstraintSet.BOTTOM, button_layout_margin);
        }else{
            // link app's top edge to the app above it
            set.connect(id, ConstraintSet.TOP, id - appsPerRow, ConstraintSet.BOTTOM, button_layout_margin);
        }
        set.applyTo(main);
    }

}