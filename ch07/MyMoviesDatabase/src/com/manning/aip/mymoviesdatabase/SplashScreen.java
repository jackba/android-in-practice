package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity {

   public static final int SPLASH_TIMEOUT = 2000;
   
   private MyMoviesApp app;
   
   private SharedPreferences prefs;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.splash_screen);
      
      app = (MyMoviesApp) getApplication();
      prefs = app.getPrefs();
   }

   @Override
   public void onStart() {
      super.onStart();
      checkPrefsAndSplash();
   }

   private void checkPrefsAndSplash() {

      boolean splashSeenOnce = prefs.getBoolean("splashseenonce", false);
      if (!splashSeenOnce) {
         Editor editor = prefs.edit();
         editor.putBoolean("splashseenonce", true);
         editor.commit();
      }
     
      boolean showSplash = prefs.getBoolean("showsplash", false);
      if (!showSplash && splashSeenOnce) {
         proceed();
      } else {
         new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
               proceed();
            }
         }, SPLASH_TIMEOUT);
      }
   }
   
   @Override
   public boolean onTouchEvent(MotionEvent event) {
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
         proceed();
      }
      return super.onTouchEvent(event);
   }

   private void proceed() {
      if (this.isFinishing()) {
         return;
      }
      startActivity(new Intent(SplashScreen.this, MyMovies.class));
      finish();
   }
}
