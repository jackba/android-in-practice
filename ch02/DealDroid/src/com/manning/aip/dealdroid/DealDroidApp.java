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

   private DailyDealsFeedParser feed;

   private Section currentSection;
   private Item currentItem;

   private Map<Long, Bitmap> iconCache = new HashMap<Long, Bitmap>();

   private LinkedHashMap<Long, Item> deals;

   public DealDroidApp() {
      this.feed = new DailyDealsXmlPullFeedParser("http://deals.ebay.com/feeds/xml");
      this.deals = new LinkedHashMap<Long, Item>(4);
   }

   public DailyDealsFeedParser getFeed() {
      return this.feed;
   }

   public void setFeed(DailyDealsFeedParser feed) {
      this.feed = feed;
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

   public Map<Long, Bitmap> getIconCache() {
      return this.iconCache;
   }

   public void setIconCache(Map<Long, Bitmap> iconCache) {
      this.iconCache = iconCache;
   }

   public LinkedHashMap<Long, Item> getDeals() {
      return this.deals;
   }

   public void setDeals(LinkedHashMap<Long, Item> deals) {
      this.deals = deals;
   }

}