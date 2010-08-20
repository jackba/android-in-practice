package com.manning.aip.dealdroid;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;
import com.manning.aip.dealdroid.xml.DailyDealsFeedParser;
import com.manning.aip.dealdroid.xml.DailyDealsXmlPullFeedParser;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DealDroidApp extends Application {

   public DailyDealsFeedParser parser;   
   public LinkedHashMap<Long, Item> deals;
   public Map<Long, Bitmap> imageCache;
   public Section currentSection;
   public Item currentItem;

   public DealDroidApp() {
      this.parser = new DailyDealsXmlPullFeedParser();
      this.deals = new LinkedHashMap<Long, Item>(4);
      this.imageCache = new HashMap<Long, Bitmap>();
   }
   
   public Bitmap retrieveBitmap(final String urlString) {
      Log.d(Constants.LOG_TAG, "making HTTP trip for image:" + urlString);
      Bitmap bitmap = null;
      try {         
         URL url = new URL(urlString);         
         InputStream stream = url.openConnection().getInputStream();
         bitmap = BitmapFactory.decodeStream(stream);         
      } catch (Exception e) {
         Log.e(Constants.LOG_TAG, "Exception loading image", e);
      }
      return bitmap;
   }
}