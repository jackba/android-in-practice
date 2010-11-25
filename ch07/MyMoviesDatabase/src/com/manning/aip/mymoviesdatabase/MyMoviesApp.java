package com.manning.aip.mymoviesdatabase;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import com.manning.aip.mymoviesdatabase.data.DataManager;
import com.manning.aip.mymoviesdatabase.util.ImageCache;

public class MyMoviesApp extends Application {

   DataManager dataManager;
   
   //private ConnectivityManager cMgr;
   private ImageCache imageCache;
   private SharedPreferences prefs;
   
   //
   // getters/setters
   //   
   public SharedPreferences getPrefs() {
      return this.prefs;
   }
   
   public DataManager getDataManager() {
      return this.dataManager;
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
      this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
      this.dataManager = new DataManager(this);
      this.imageCache = new ImageCache(cMgr);
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