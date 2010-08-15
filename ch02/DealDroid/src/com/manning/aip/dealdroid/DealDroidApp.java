package com.manning.aip.dealdroid;

import android.app.Application;
import android.graphics.Bitmap;

import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;
import com.manning.aip.dealdroid.xml.DailyDealsFeedParser;
import com.manning.aip.dealdroid.xml.DailyDealsXmlPullFeedParser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DealDroidApp extends Application {

   public DailyDealsFeedParser parser;

   public Section currentSection;
   public Item currentItem;

   public Map<Long, Bitmap> iconCache = new HashMap<Long, Bitmap>();

   public LinkedHashMap<Long, Item> deals;

   public DealDroidApp() {
      this.parser = new DailyDealsXmlPullFeedParser("http://deals.ebay.com/feeds/xml");
      this.deals = new LinkedHashMap<Long, Item>(4);
   }
}