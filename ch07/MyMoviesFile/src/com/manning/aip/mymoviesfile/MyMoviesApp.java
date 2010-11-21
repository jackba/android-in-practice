package com.manning.aip.mymoviesfile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.manning.aip.mymoviesfile.util.ImageCache;

public class MyMoviesApp extends Application {

   //private ConnectivityManager cMgr;
   private ImageCache imageCache;
   private SharedPreferences prefs;

   //
   // getters/setters
   //
   public SharedPreferences getPrefs() {
      return this.prefs;
   }
   
   public ImageCache getImageCache() {
      return this.imageCache;
   }

   //
   // lifecycle
   //
   @Override
   public void onCreate() {
      super.onCreate();
      ConnectivityManager cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
      this.imageCache = new ImageCache(cMgr);
      this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
   }

   @Override
   public void onTerminate() {
      // not guaranteed to be called
      super.onTerminate();
   }

  /*
   public boolean connectionPresent() {
      NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
      if ((netInfo != null) && (netInfo.getState() != null)) {
         return netInfo.getState().equals(State.CONNECTED);
      }
      return false;
   }
   */
}