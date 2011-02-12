package com.manning.aip.astromap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;

public class Splash extends Activity {

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.splash);
   }

   @Override
   public boolean onTouchEvent(final MotionEvent e) {
      if (e.getAction() == MotionEvent.ACTION_DOWN) {
         startActivity(new Intent(Splash.this, Main.class));
      }
      return true;
   }
}