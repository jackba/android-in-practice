package com.manning.aip.dealdroid.xml;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class DailyDealsBaseFeedParser implements DailyDealsFeedParser {

   // names of the XML tags
   static final String EBAY_DAILY_DEALS ="EbayDailyDeals";
   static final String MORE_DEALS = "MoreDeals";
   static final String MORE_DEALS_SECTION = "MoreDealsSection";
   static final String SECTION_TITLE = "SectionTitle";
   
   static final String ITEM = "Item";
   static final String ITEM_ID = "ItemId";
   static final String END_TIME = "EndTime";
   static final String PICTURE_URL = "PictureURL";
   static final String SMALL_PICTURE_URL = "SmallPictureURL";
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

   protected DailyDealsBaseFeedParser(String feedUrl){
       try {
           this.feedUrl = new URL(feedUrl);
       } catch (MalformedURLException e) {
           throw new RuntimeException(e);
       }
   }

   protected InputStream getInputStream() {
       try {
           return feedUrl.openConnection().getInputStream();
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }
}
