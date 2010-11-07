package com.manning.aip.dealdroid;

import android.app.Application;
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
   private Section currentSection;
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

   public Section getCurrentSection() {
      return this.currentSection;
   }

   public void setCurrentSection(Section currentSection) {
      this.currentSection = currentSection;
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
   // helper methods (used by more than one other activity, so placed here)
   //
   public Bitmap retrieveBitmap(final String urlString) {
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

   public List<Long> getPreviousDealIds() {
      List<Long> previousDealIds = new ArrayList<Long>();
      previousDealIds.add(prefs.getLong(Constants.DEAL1, 0));
      previousDealIds.add(prefs.getLong(Constants.DEAL2, 0));
      previousDealIds.add(prefs.getLong(Constants.DEAL3, 0));
      previousDealIds.add(prefs.getLong(Constants.DEAL4, 0));
      return previousDealIds;
   }

   public void setPreviousDealIds(final List<Long> previousDealIds) {
      // should never get this error, but it's a good idea to fail fast in case
      if ((previousDealIds == null) || (previousDealIds.size() != 4)) {
         throw new IllegalArgumentException("Error, previousDealIds size must be 4");
      }
      Editor editor = prefs.edit();
      editor.putLong(Constants.DEAL1, previousDealIds.get(0));
      editor.putLong(Constants.DEAL2, previousDealIds.get(1));
      editor.putLong(Constants.DEAL3, previousDealIds.get(2));
      editor.putLong(Constants.DEAL4, previousDealIds.get(3));
      editor.commit();
   }

   public List<Long> parseItemsIntoDealIds(final List<Item> items) {
      List<Long> idList = new ArrayList<Long>();
      if ((items != null) && !items.isEmpty()) {
         for (Item item : items) {
            idList.add(item.getItemId());
         }
      }
      return idList;
   }

   public boolean connectionPresent() {
      ConnectivityManager cMgr = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
      if (cMgr != null) {
         NetworkInfo netInfo = cMgr.getActiveNetworkInfo();
         if (netInfo != null && netInfo.getState() != null) {
            return netInfo.getState().equals(State.CONNECTED);
         } else {
            return false;
         }
      }
      return false;
   }
}