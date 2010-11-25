package com.manning.aip.mymoviesdatabase;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity {

   private CheckBoxPreference showSplash;
   private CheckBoxPreference debugLogging;

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      addPreferencesFromResource(R.layout.preferences);
 
      // handle to preferences doesn't come from findViewById!
      showSplash = (CheckBoxPreference) getPreferenceScreen().findPreference("showsplash");
      debugLogging = (CheckBoxPreference) getPreferenceScreen().findPreference("debuglogging"); 
      
      setCheckBoxSummary(showSplash);
      setCheckBoxSummary(debugLogging);

      // listen to see if user changes pref, so we can update display of current value
      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
      prefs.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
         public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (key.equals("showsplash")) {
               setCheckBoxSummary(showSplash);
            } else if (key.equals("debuglogging")) {
               setCheckBoxSummary(debugLogging);
            }
         }
      });
   }
   
   private void setCheckBoxSummary(CheckBoxPreference pref) {
      if (pref.isChecked()) {
         pref.setSummary("Enabled");
      } else {
         pref.setSummary("Disabled");
      }
   }
}