package com.manning.aip.dealdroid.xml;

import android.util.Log;
import android.util.Xml;

import com.manning.aip.dealdroid.Constants;
import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DailyDealsXmlPullFeedParser implements DailyDealsFeedParser {

   static final String FEED_URL = "http://deals.ebay.com/feeds/xml";

   // names of the XML tags
   static final String EBAY_DAILY_DEALS = "EbayDailyDeals";
   static final String MORE_DEALS = "MoreDeals";
   static final String MORE_DEALS_SECTION = "MoreDealsSection";
   static final String SECTION_TITLE = "SectionTitle";

   static final String ITEM = "Item";
   static final String ITEM_ID = "ItemId";
   static final String END_TIME = "EndTime";
   static final String PICTURE_URL = "PictureURL";
   static final String SMALL_PICTURE_URL = "SmallPictureURL";
   static final String PICTURE_175_URL = "Picture175URL";
   static final String TITLE = "Title";
   static final String DESCRIPTION = "Description";
   static final String DEAL_URL = "DealURL";
   static final String CONVERTED_CURRENT_PRICE = "ConvertedCurrentPrice";
   static final String PRIMARY_CATEGORY_NAME = "PrimaryCategoryName";
   static final String LOCATION = "Location";
   static final String QUANTITY = "Quantity";
   static final String QUANTITY_SOLD = "QuantitySold";
   static final String MSRP = "MSRP";
   static final String SAVINGS_RATE = "SavingsRate";
   static final String HOT = "Hot";

   final URL feedUrl;

   public DailyDealsXmlPullFeedParser() {
      try {
         this.feedUrl = new URL(DailyDealsXmlPullFeedParser.FEED_URL);
      } catch (MalformedURLException e) {
         throw new RuntimeException(e);
      }
      Log.d(Constants.LOG_TAG, "DailyDealsXmlPullFeedParser instantiated for URL:" + feedUrl);
   }

   protected InputStream getInputStream() {
      try {
         // NOTE, be careful about just doing "url.openStream()"
         // it's a shortcut for openConnection().getInputStream() and doesn't set timeouts
         // (the defaults are "infinite" so it will wait forever if endpoint server is down)
         // do it properly with a few more lines of code . . .         
         URLConnection conn = feedUrl.openConnection();     
         conn.setConnectTimeout(3000);
         conn.setReadTimeout(5000);      
         return feedUrl.openConnection().getInputStream();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public ArrayList<Section> parse() {
      ///Log.d(Constants.LOG_TAG, "parse invoked");
      ArrayList<Section> sections = null;
      XmlPullParser parser = Xml.newPullParser();
      try {
         Section currentSection = null;
         Item currentItem = null;

         // auto-detect the encoding from the stream
         parser.setInput(this.getInputStream(), null);
         int eventType = parser.getEventType();

         while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
               case XmlPullParser.START_DOCUMENT:
                  ///Log.d(Constants.LOG_TAG, " start document");
                  sections = new ArrayList<Section>();
                  break;
               case XmlPullParser.START_TAG:
                  name = parser.getName();
                  ///Log.d(Constants.LOG_TAG, "  start tag: " + name);                  

                  // establish sections
                  if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.EBAY_DAILY_DEALS)) {
                     ///Log.d(Constants.LOG_TAG, "   created section: Daily Deals");
                     currentSection = new Section();
                     currentSection.setTitle("Daily Deals");
                  } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.SECTION_TITLE)) {
                     String title = parser.nextText();
                     ///Log.d(Constants.LOG_TAG, "   created more details section: " + title);
                     currentSection = new Section();
                     currentSection.setTitle(title);
                  } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.ITEM) && (currentSection != null)) {
                     currentItem = new Item();
                  }

                  // when MoreDeals starts, DailyDeals are over, more are nested (which is odd)
                  if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.MORE_DEALS)) {
                     ///Log.d(Constants.LOG_TAG, "   adding Daily Deals section to sections list");
                     sections.add(Section.getInstance(currentSection));
                     currentSection = null;
                  }

                  // establish items
                  if ((currentSection != null) && (currentItem != null)) {
                     if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.ITEM_ID)) {
                        try {
                           currentItem.setItemId(Long.valueOf(parser.nextText()));
                        } catch (NumberFormatException e) {
                           Log.e(Constants.LOG_TAG, "Error parsing itemId", e);
                        }
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.END_TIME)) {
                        try {
                           currentItem.setEndTime(Long.valueOf(parser.nextText()));
                        } catch (NumberFormatException e) {
                           Log.e(Constants.LOG_TAG, "Error parsing endTime", e);
                        }
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.PICTURE_URL)) {
                        currentItem.setPicUrl(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.SMALL_PICTURE_URL)) {
                        currentItem.setSmallPicUrl(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.PICTURE_175_URL)) {
                        currentItem.setPic175Url(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.TITLE)) {
                        currentItem.setTitle(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.DESCRIPTION)) {
                        currentItem.setDesc(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.DEAL_URL)) {
                        currentItem.setDealUrl(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.CONVERTED_CURRENT_PRICE)) {
                        currentItem.setConvertedCurrentPrice(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.PRIMARY_CATEGORY_NAME)) {
                        currentItem.setPrimaryCategoryName(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.LOCATION)) {
                        currentItem.setLocation(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.QUANTITY)) {
                        try {
                           currentItem.setQuantity(Integer.valueOf(parser.nextText()));
                        } catch (NumberFormatException e) {
                           Log.e(Constants.LOG_TAG, "Error parsing quantity", e);
                        }
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.QUANTITY_SOLD)) {
                        try {
                           currentItem.setQuantitySold(Integer.valueOf(parser.nextText()));
                        } catch (NumberFormatException e) {
                           Log.e(Constants.LOG_TAG, "Error parsing quantitySold", e);
                        }
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.MSRP)) {
                        currentItem.setMsrp(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.SAVINGS_RATE)) {
                        currentItem.setSavingsRate(parser.nextText());
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.HOT)) {
                        currentItem.setHot(Boolean.valueOf(parser.nextText()));
                     }
                  }
                  break;
               case XmlPullParser.END_TAG:
                  name = parser.getName();
                  ///Log.d(Constants.LOG_TAG, "  end tag: " + name);
                  if (name != null) {
                     if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.MORE_DEALS_SECTION)
                              && (currentSection != null)) {
                        ///Log.d(Constants.LOG_TAG, "   adding section to sections list: " + currentSection.title);
                        sections.add(Section.getInstance(currentSection));
                        currentSection = null;
                     } else if (name.equalsIgnoreCase(DailyDealsXmlPullFeedParser.ITEM) && (currentItem != null)) {
                        ///Log.d(Constants.LOG_TAG, "   adding item " + currentItem.title + " to current section");
                        currentSection.getItems().add(Item.getInstance(currentItem));
                        currentItem = null;
                     }
                  }
                  break;
            }
            eventType = parser.next();
         }
      } catch (Exception e) {
         Log.e(Constants.LOG_TAG, "Exception parsing XML", e);
         throw new RuntimeException(e);
      }
      return sections;
   }
}
