package com.manning.aip.dealdroid;

import android.app.Application;
import android.graphics.Bitmap;

import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;
import com.manning.aip.dealdroid.xml.DailyDealsFeedParser;
import com.manning.aip.dealdroid.xml.DailyDealsXmlPullFeedParser;

import java.util.HashMap;
import java.util.Map;

public class DealDroidApp extends Application {
   
   private DailyDealsFeedParser feed;
   
   private Section currentSection;
   private Item currentItem;
   //private Section currentSection;
   private Map<Long, Bitmap> iconCache = new HashMap<Long, Bitmap>();

   public DealDroidApp() {
      this.feed = new DailyDealsXmlPullFeedParser("http://deals.ebay.com/feeds/xml");
   }
   
   public DailyDealsFeedParser getFeed() {
      return this.feed;
   }

   public void setFeed(DailyDealsFeedParser feed) {
      this.feed = feed;
   }

   public Item getCurrentItem() {
      return this.currentItem;
   }

   public void setCurrentItem(Item currentItem) {
      this.currentItem = currentItem;
   }

   public Section getCurrentSection() {
      return this.currentSection;
   }

   public void setCurrentSection(Section currentSection) {
      this.currentSection = currentSection;
   }

   public Map<Long, Bitmap> getIconCache() {
      return this.iconCache;
   }

   public void setIconCache(Map<Long, Bitmap> iconCache) {
      this.iconCache = iconCache;
   }

}