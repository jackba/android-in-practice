package com.manning.aip.dealdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;
import com.manning.aip.dealdroid.xml.DailyDealsFeedParser;
import com.manning.aip.dealdroid.xml.DailyDealsXmlPullFeedParser;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;

public class DealsApp extends Application {
	   public DailyDealsFeedParser parser;
	   public List<Section> sectionList;
	   public Map<Long, Bitmap> imageCache;
	   public Section currentSection;
	   public Item currentItem;
	   public SharedPreferences prefs;

	   @Override
	   public void onCreate() {
	      super.onCreate();
	      this.parser = new DailyDealsXmlPullFeedParser();
	      this.sectionList = new ArrayList<Section>(6);
	      this.imageCache = new HashMap<Long, Bitmap>();
	      this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
	   }

	   public List<Long> getPreviousDealIdsFromPrefs() {
	      List<Long> previousDealIds = new ArrayList<Long>();
	      previousDealIds.add(prefs.getLong(Constants.DEAL1, 0));
	      previousDealIds.add(prefs.getLong(Constants.DEAL2, 0));
	      previousDealIds.add(prefs.getLong(Constants.DEAL3, 0));
	      previousDealIds.add(prefs.getLong(Constants.DEAL4, 0));
	      return previousDealIds;
	   }

	   public void setPreviousDealIdsToPrefs(final List<Long> previousDealIds) {
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
	            idList.add(item.itemId);
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
