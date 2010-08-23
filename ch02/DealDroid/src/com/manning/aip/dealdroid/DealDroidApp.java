package com.manning.aip.dealdroid;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

   public DailyDealsFeedParser parser;   
   public List<Section> sectionList;
   public Map<Long, Bitmap> imageCache;
   public Section currentSection;
   public Item currentItem;

   public DealDroidApp() {
      this.parser = new DailyDealsXmlPullFeedParser();
      this.sectionList = new ArrayList<Section>(6);
      this.imageCache = new HashMap<Long, Bitmap>();
   }

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
}