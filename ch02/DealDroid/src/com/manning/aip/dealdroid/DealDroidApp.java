package com.manning.aip.dealdroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;
import android.util.Log;

import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;
import com.manning.aip.dealdroid.xml.DailyDealsFeedParser;
import com.manning.aip.dealdroid.xml.DailyDealsXmlPullFeedParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DealDroidApp extends Application {

   private DailyDealsFeedParser parser;
   private List<Section> sectionList;
   private Map<Long, Bitmap> imageCache;
   private Item currentItem;

   //
   // getters/setters
   //
   public DailyDealsFeedParser getParser() {
      return this.parser;
   }

   public List<Section> getSectionList() {
      return this.sectionList;
   }

   public Map<Long, Bitmap> getImageCache() {
      return this.imageCache;
   }

   public Item getCurrentItem() {
      return this.currentItem;
   }

   public void setCurrentItem(Item currentItem) {
      this.currentItem = currentItem;
   }

   //
   // lifecycle
   //
   @Override
   public void onCreate() {
      super.onCreate();
      this.parser = new DailyDealsXmlPullFeedParser();
      this.sectionList = new ArrayList<Section>(6);
      this.imageCache = new HashMap<Long, Bitmap>();
   }

   @Override
   public void onTerminate() {
      // not guaranteed to be called
      super.onTerminate();
   }

   //
   // helper methods (used by more than one other activity, so placed here, could be util class too)
   //
   public Bitmap retrieveBitmap(String urlString) {
      Log.d(Constants.LOG_TAG, "making HTTP trip for image:" + urlString);
      Bitmap bitmap = null;
      InputStream stream = null;
      try {
         URL url = new URL(urlString);
         stream = url.openConnection().getInputStream();
         bitmap = BitmapFactory.decodeStream(stream);
      } catch (MalformedURLException e) {
         Log.e(Constants.LOG_TAG, "Exception loading image, malformed URL", e);
      } catch (IOException e) {
         Log.e(Constants.LOG_TAG, "Exception loading image, IO error", e);
      } finally {
         try {
            if (stream != null) {
               stream.close();
            }
         } catch (IOException e) {
            Log.w(Constants.LOG_TAG, "Error closing stream", e);
         }
      }
      return bitmap;
   }

   public boolean connectionPresent() {
      ConnectivityManager cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (cMgr != null) {
         NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
         if ((netInfo != null) && (netInfo.getState() != null)) {
            return netInfo.getState().equals(State.CONNECTED);
         } else {
            return false;
         }
      }
      return false;
   }
}