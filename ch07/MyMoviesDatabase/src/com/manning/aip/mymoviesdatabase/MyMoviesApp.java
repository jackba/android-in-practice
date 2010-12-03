package com.manning.aip.mymoviesdatabase;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;

import com.manning.aip.mymoviesdatabase.data.DataManager;
import com.manning.aip.mymoviesdatabase.util.ImageCache;

public class MyMoviesApp extends Application {

   private ConnectivityManager cMgr;
   private DataManager dataManager;
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
      cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
      prefs = PreferenceManager.getDefaultSharedPreferences(this);
      dataManager = new DataManager(this);
      imageCache = new ImageCache();
   }

   @Override
   public void onTerminate() {
      // not guaranteed to be called
      super.onTerminate();
   }

   //
   // util/helpers for app
   //
   public boolean connectionPresent() {
      NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
      if ((netInfo != null) && (netInfo.getState() != null)) {
         return netInfo.getState().equals(State.CONNECTED);
      }
      return false;
   }
}