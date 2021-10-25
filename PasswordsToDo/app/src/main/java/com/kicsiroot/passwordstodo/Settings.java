package com.kicsiroot.passwordstodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;

import com.google.android.material.appbar.AppBarLayout;

import java.util.prefs.Preferences;

public class Settings extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.SummaryProvider<androidx.preference.ListPreference> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new MySettingsFragment()).commit();

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setDisplayHomeAsUpEnabled(true);

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);



    }

    public static class MySettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref_main, rootKey);
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key){
       String darkMode = getString(R.string.dark_mode);
           if (key != null && sharedPreferences != null)
               if (key.equals(darkMode)){
                   final String[] darkModeValues = getResources().getStringArray(R.array.darkmodevalues);
                   //The apps theme is decided depending upon the saved preferences on app startup
                   String pref = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.dark_mode), getString(R.string.dark_mode_def));
                   //Comparing to see which preference is selected and applying those theme settings
                   assert pref != null;
                   if (pref.equals(darkModeValues[0]))
                       AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                   if (pref.equals(darkModeValues[1]))
                       AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                   if (pref.equals(darkModeValues[2]))
                       AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    @Override
    public CharSequence provideSummary(androidx.preference.ListPreference preference) {
        String key = preference.getKey();
        if (key != null)
            if (key.equals(getString(R.string.dark_mode)))
                return preference.getEntry();
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}