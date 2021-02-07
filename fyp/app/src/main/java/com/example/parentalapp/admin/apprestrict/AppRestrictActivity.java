package com.example.parentalapp.admin.apprestrict;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;


import com.example.parentalapp.MainActivity;
import com.example.parentalapp.R;
import com.example.parentalapp.admin.ParentMainActivity;

import java.util.List;

public class AppRestrictActivity extends AppCompatActivity {
    private static AppRestrictConfig appRestrictConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_restrict);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.appRestrict, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        appRestrictConfig = new AppRestrictConfig(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), ParentMainActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            createAppAccessPreference();
        }

        private void createAppAccessPreference(){
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            // List of all installed apps
            PackageManager pm = this.getContext().getPackageManager();
            final List<ResolveInfo> pkgAppsList = pm.queryIntentActivities( mainIntent, 0);

            Context activityContext = getActivity();
            PreferenceScreen preferenceScreen = getPreferenceManager().createPreferenceScreen(activityContext);
            setPreferenceScreen(preferenceScreen);

            TypedValue themeTypedValue = new TypedValue();
            activityContext.getTheme().resolveAttribute(R.attr.preferenceTheme, themeTypedValue, true);
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(activityContext, themeTypedValue.resourceId);
            PreferenceCategory preferenceCategory = new PreferenceCategory(contextThemeWrapper);
            preferenceCategory.setTitle("Category Test");

            getPreferenceScreen().addPreference(preferenceCategory);

            for (ResolveInfo resolveInfo : pkgAppsList){
                final ActivityInfo activityInfo = resolveInfo.activityInfo;
                try {
                    CheckBoxPreference checkBoxPreference = new CheckBoxPreference(contextThemeWrapper);
                    checkBoxPreference.setKey(activityInfo.name);
                    final String appName = pm.getApplicationLabel(activityInfo.applicationInfo).toString();
                    checkBoxPreference.setTitle(pm.getApplicationLabel(activityInfo.applicationInfo));
                    Drawable icon = pm.getApplicationIcon(activityInfo.packageName);
                    checkBoxPreference.setIcon(icon);
                    checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object newValue) {
                            appRestrictConfig.changePermission(appName, (boolean)newValue);
                            return true;
                        }
                    });
                    preferenceCategory.addPreference(checkBoxPreference);
                }catch (PackageManager.NameNotFoundException ne){
                    System.out.println("Name not found");
                }

            }
        }
    }
}