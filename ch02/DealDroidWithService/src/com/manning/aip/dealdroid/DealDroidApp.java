package com.manning.aip.dealdroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DealDroidApp extends Application {

   private ConnectivityManager cMgr;
   private DailyDealsFeedParser parser;
   private List<Section> sectionList;
   private Map<Long, Bitmap> imageCache;
   private Item currentItem;
   private SharedPreferences prefs;

   //
   // getters/setters
   //
   public DailyDealsFeedParser getParser() {
      return this.parser;
   }

   public List<Section> getSectionList() {
      return this.sectionList;
   }

   public void setSectionList(List<Section> sectionList) {
      this.sectionList = sectionList;
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

   public SharedPreferences getPrefs() {
      return this.prefs;
   }

   //
   // lifecycle
   //
   @Override
   public void onCreate() {
      super.onCreate();
      this.cMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
      this.parser = new DailyDealsXmlPullFeedParser();
      this.sectionList = new ArrayList<Section>(6);
      this.imageCache = new HashMap<Long, Bitmap>();
      this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
   }

   @Override
   public void onTerminate() {
      // not guaranteed to be called
      super.onTerminate();
   }

   //
   // previous deal state (used by service)
   //
   public List<Long> getPreviousDealIds() {
      List<Long> previousDealIds = new ArrayList<Long>();
      previousDealIds.add(prefs.getLong(Constants.DEAL1, 0));
      previousDealIds.add(prefs.getLong(Constants.DEAL2, 0));
      previousDealIds.add(prefs.getLong(Constants.DEAL3, 0));
      previousDealIds.add(prefs.getLong(Constants.DEAL4, 0));
      previousDealIds.add(prefs.getLong(Constants.DEAL5, 0));
      return previousDealIds;
   }

   public void setPreviousDealIds(List<Long> previousDealIds) {
      // should never get this error, but it's a good idea to fail fast in case
      if ((previousDealIds == null) || previousDealIds.isEmpty() || previousDealIds.size() > 5) {
         throw new IllegalArgumentException("Error, previousDealIds null, or empty, or more than 5");
      }
      Editor editor = prefs.edit();
      editor.putLong(Constants.DEAL1, previousDealIds.get(0));
      editor.putLong(Constants.DEAL2, previousDealIds.get(1));
      editor.putLong(Constants.DEAL3, previousDealIds.get(2));
      editor.putLong(Constants.DEAL4, previousDealIds.get(3));
      // we added support for 5th deal later, some users may only have 4 in file
      // (one day last year eBay had 5 daily deals at once, it's normally 4)
      if (previousDealIds.size() == 4) {
         previousDealIds.add(0L);
      }
      editor.putLong(Constants.DEAL5, previousDealIds.get(5));
      editor.commit();
   }

   public List<Long> parseItemsIntoDealIds(List<Item> items) {
      List<Long> idList = new ArrayList<Long>();
      if ((items != null) && !items.isEmpty()) {
         for (Item item : items) {
            idList.add(item.getItemId());
         }
      }
      return idList;
   }

   //
   // helper methods (used by more than one other activity, so placed here)
   //
   public Bitmap retrieveBitmap(String urlString) {
      Log.d(Constants.LOG_TAG, "making HTTP trip for image:" + urlString);
      Bitmap bitmap = null;
      try {
         URL url = new URL(urlString);
         bitmap = BitmapFactory.decodeStream(url.openStream());
      } catch (MalformedURLException e) {
         Log.e(Constants.LOG_TAG, "Exception loading image, malformed URL", e);
      } catch (IOException e) {
         Log.e(Constants.LOG_TAG, "Exception loading image, IO error", e);
      }
      return bitmap;
   }

   public boolean connectionPresent() {
      NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
      if ((netInfo != null) && (netInfo.getState() != null)) {
         return netInfo.getState().equals(State.CONNECTED);
      } 
      return false;
   }
}