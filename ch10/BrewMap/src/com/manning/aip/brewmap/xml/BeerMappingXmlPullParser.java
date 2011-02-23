package com.manning.aip.brewmap.xml;

import android.util.Log;
import android.util.Xml;

import com.manning.aip.brewmap.Constants;
import com.manning.aip.brewmap.model.BrewLocation;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class BeerMappingXmlPullParser implements BeerMappingParser {

   // have to obtain an API key from BeerMapping.com
   static final String FEED_URL_CITY = "http://beermapping.com/webservice/loccity/a58b9c5e468fe57d35710364dd0b1c71/";
   static final String FEED_URL_STATE = "http://beermapping.com/webservice/locstate/a58b9c5e468fe57d35710364dd0b1c71/";
   static final String FEED_URL_PIECE = "http://beermapping.com/webservice/locquery/a58b9c5e468fe57d35710364dd0b1c71/";

   // names of the XML tags
   static final String BMP_LOCATIONS = "bmp_locations";
   static final String LOCATION = "location";
   static final String ID = "id";
   static final String NAME = "name";
   static final String STATUS = "status";
   static final String REVIEW_LINK = "reviewlink";
   static final String PROXY_LINK = "proxylink";
   static final String BLOG_MAP = "blogmap";
   static final String STREET = "street";
   static final String CITY = "city";
   static final String STATE = "state";
   static final String ZIP = "zip";
   static final String COUNTRY = "country";
   static final String PHONE = "phone";
   static final String OVERALL = "overall";
   static final String IMAGECOUNT = "imagecount";

   public BeerMappingXmlPullParser() {

      Log.d(Constants.LOG_TAG, "BeerMappingXmlPullParser instantiated");
   }

   private URL getUrl(String prefix, String suffix) {
      try {
         return new URL(prefix + suffix);
      } catch (MalformedURLException e) {
         throw new RuntimeException(e);
      }
   }

   protected InputStream getInputStream(URL url) {
      try {
         URLConnection conn = url.openConnection();
         conn.setConnectTimeout(5000);
         conn.setReadTimeout(3000);
         return conn.getInputStream();
      } catch (IOException e) {
         Log.e(Constants.LOG_TAG, "Error getting input stream for url: " + url, e);
         return null;
      }
   }

   @Override
   public ArrayList<BrewLocation> parseCity(String city) {
      return parse(getUrl(FEED_URL_CITY, city));
   }

   @Override
   public ArrayList<BrewLocation> parseState(String state) {
      return parse(getUrl(FEED_URL_CITY, state));
   }

   @Override
   public ArrayList<BrewLocation> parsePiece(String piece) {
      return parse(getUrl(FEED_URL_CITY, piece));
   }

   private ArrayList<BrewLocation> parse(URL url) {
      ///Log.d(Constants.LOG_TAG, "parse invoked");
      ArrayList<BrewLocation> brewLocations = null;
      XmlPullParser parser = Xml.newPullParser();
      try {
         BrewLocation currentLocation = null;

         // auto-detect the encoding from the stream
         InputStream is = this.getInputStream(url);
         if (is == null) {
            return brewLocations;
         }
         parser.setInput(is, null);
         int eventType = parser.getEventType();

         while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
               case XmlPullParser.START_DOCUMENT:
                  ///Log.d(Constants.LOG_TAG, " start document");
                  brewLocations = new ArrayList<BrewLocation>();
                  break;
               case XmlPullParser.START_TAG:
                  name = parser.getName();
                  ///Log.d(Constants.LOG_TAG, "  start tag: " + name);                  

                  if (name.equalsIgnoreCase(BeerMappingXmlPullParser.LOCATION)) {
                     currentLocation = new BrewLocation();
                  }
                
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.ID)) {
                     String value = parser.nextText();
                     try {
                        currentLocation.setId(Integer.valueOf(value));
                     } catch (NumberFormatException e) {                        
                        Log.e(Constants.LOG_TAG, "Error parsing ID for pub", e);
                     }
                  }
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.NAME)) {
                     String value = parser.nextText();
                     currentLocation.setName(value);
                  }
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.STATUS)) {
                     String value = parser.nextText();
                     currentLocation.setStatus(value);
                  }
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.REVIEW_LINK)) {
                     String value = parser.nextText();
                     currentLocation.setReviewLink(value);
                  }
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.PROXY_LINK)) {
                     String value = parser.nextText();
                     currentLocation.setProxyLink(value);
                  }                  
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.STREET)) {
                     String value = parser.nextText();
                     currentLocation.getAddress().setStreet(value);
                  }
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.CITY)) {
                     String value = parser.nextText();
                     currentLocation.getAddress().setCity(value);
                  }
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.STATE)) {
                     String value = parser.nextText();
                     currentLocation.getAddress().setState(value);
                  }
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.ZIP)) {
                     String value = parser.nextText();
                     currentLocation.getAddress().setPostalCode(value);
                  }
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.COUNTRY)) {
                     String value = parser.nextText();
                     currentLocation.getAddress().setCountry(value);
                  }
                  if (currentLocation != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.PHONE)) {
                     String value = parser.nextText();
                     currentLocation.setPhone(value);
                  }
                  break;
               case XmlPullParser.END_TAG:
                  name = parser.getName();
                  ///Log.d(Constants.LOG_TAG, "  end tag: " + name);
                  if (name != null) {
                     if (name.equalsIgnoreCase(BeerMappingXmlPullParser.LOCATION) && (currentLocation != null)) {
                        brewLocations.add(currentLocation);
                        currentLocation = null;
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
      return brewLocations;
   }
}
