package com.manning.aip.dealdroid.xml;

import android.util.Log;
import android.util.Xml;

import com.manning.aip.dealdroid.Constants;
import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class DailyDealsXmlPullFeedParser extends DailyDealsBaseFeedParser {

   public DailyDealsXmlPullFeedParser(String feedUrl) {
      super(feedUrl);     
      Log.d(Constants.LOG_TAG, "DailyDealsXmlPullFeedParser instantiated for URL:" + feedUrl);
   }

   public List<Section> parse() {
      Log.d(Constants.LOG_TAG, "parse invoked");
      List<Section> sections = null;
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
                  Log.d(Constants.LOG_TAG, " start document");
                  sections = new ArrayList<Section>();
                  break;
               case XmlPullParser.START_TAG:
                  name = parser.getName();
                  ///Log.d(Constants.LOG_TAG, "  start tag: " + name);                  
                  
                  // establish sections
                  if (name.equalsIgnoreCase(EBAY_DAILY_DEALS)) {
                     Log.d(Constants.LOG_TAG, "   created section: Daily Deals");
                     currentSection = new Section();
                     currentSection.title = "Daily Deals";
                  } else if (name.equalsIgnoreCase(SECTION_TITLE)) {
                     String title = parser.nextText();
                     Log.d(Constants.LOG_TAG, "   created more details section: " + title);
                     currentSection = new Section();
                     currentSection.title = title;
                  } else if (name.equalsIgnoreCase(ITEM) && currentSection != null) {
                     currentItem = new Item();
                  }

                  // establish items
                  if (currentSection != null && currentItem != null) {
                     if (name.equalsIgnoreCase(ITEM_ID)) {
                        try {
                           currentItem.itemId = Long.valueOf(parser.nextText());
                        } catch (NumberFormatException e) {
                           Log.e(Constants.LOG_TAG, "Error parsing itemId", e);
                        }
                     } else if (name.equalsIgnoreCase(END_TIME)) {
                        try {
                           currentItem.endTime = Long.valueOf(parser.nextText());
                        } catch (NumberFormatException e) {
                           Log.e(Constants.LOG_TAG, "Error parsing endTime", e);
                        }
                     } else if (name.equalsIgnoreCase(PICTURE_URL)) {
                        currentItem.picUrl = parser.nextText();
                     } else if (name.equalsIgnoreCase(SMALL_PICTURE_URL)) {
                        currentItem.smallPicUrl = parser.nextText();
                     } else if (name.equalsIgnoreCase(TITLE)) {
                        currentItem.title = parser.nextText();
                     } else if (name.equalsIgnoreCase(DESCRIPTION)) {
                        currentItem.desc = parser.nextText();
                     } else if (name.equalsIgnoreCase(DEAL_URL)) {
                        currentItem.dealUrl = parser.nextText();
                     } else if (name.equalsIgnoreCase(CONVERTED_CURRENT_PRICE)) {
                        currentItem.convertedCurrentPrice = parser.nextText();
                     } else if (name.equalsIgnoreCase(PRIMARY_CATEGORY_NAME)) {
                        currentItem.primaryCategoryName = parser.nextText();
                     } else if (name.equalsIgnoreCase(LOCATION)) {
                        currentItem.location = parser.nextText();
                     } else if (name.equalsIgnoreCase(QUANTITY)) {
                        try {
                           currentItem.quantity = Integer.valueOf(parser.nextText());
                        } catch (NumberFormatException e) {
                           Log.e(Constants.LOG_TAG, "Error parsing quantity", e);
                        }
                     } else if (name.equalsIgnoreCase(QUANTITY_SOLD)) {
                        currentItem.picUrl = parser.nextText();
                     } else if (name.equalsIgnoreCase(MSRP)) {
                        currentItem.msrp = parser.nextText();
                     } else if (name.equalsIgnoreCase(SAVINGS_RATE)) {
                        currentItem.savingsRate = parser.nextText();
                     } else if (name.equalsIgnoreCase(HOT)) {
                        currentItem.hot = Boolean.valueOf(parser.nextText());
                     }                     
                  }
                  break;
               case XmlPullParser.END_TAG:
                  name = parser.getName();
                  ///Log.d(Constants.LOG_TAG, "  end tag: " + name);
                  if (name != null) {
                     if ((name.equalsIgnoreCase(EBAY_DAILY_DEALS) || name.equalsIgnoreCase(MORE_DEALS_SECTION))
                              && currentSection != null) {
                        ///Log.d(Constants.LOG_TAG, "   adding section to sections list: " + currentSection.title);
                        sections.add(Section.getInstance(currentSection));
                        currentSection = null;
                     } else if (name.equalsIgnoreCase(ITEM) && currentItem != null) {
                        ///Log.d(Constants.LOG_TAG, "   adding item " + currentItem.title + " to current section");
                        currentSection.items.add(Item.getInstance(currentItem));
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
