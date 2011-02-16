package com.manning.aip.brewmap.xml;

import android.util.Log;
import android.util.Xml;

import com.manning.aip.brewmap.Constants;
import com.manning.aip.brewmap.model.Pub;

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
         throw new RuntimeException(e);
      }
   }

   @Override
   public ArrayList<Pub> parseCity(String city) {
      return parse(getUrl(FEED_URL_CITY, city));
   }

   @Override
   public ArrayList<Pub> parseState(String state) {
      return parse(getUrl(FEED_URL_CITY, state));
   }

   @Override
   public ArrayList<Pub> parsePiece(String piece) {
      return parse(getUrl(FEED_URL_CITY, piece));
   }

   private ArrayList<Pub> parse(URL url) {
      ///Log.d(Constants.LOG_TAG, "parse invoked");
      ArrayList<Pub> pubs = null;
      XmlPullParser parser = Xml.newPullParser();
      try {
         Pub currentPub = null;

         // auto-detect the encoding from the stream
         parser.setInput(this.getInputStream(url), null);
         int eventType = parser.getEventType();

         while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
               case XmlPullParser.START_DOCUMENT:
                  ///Log.d(Constants.LOG_TAG, " start document");
                  pubs = new ArrayList<Pub>();
                  break;
               case XmlPullParser.START_TAG:
                  name = parser.getName();
                  ///Log.d(Constants.LOG_TAG, "  start tag: " + name);                  

                  if (name.equalsIgnoreCase(BeerMappingXmlPullParser.LOCATION)) {
                     currentPub = new Pub();
                  }

                  if (currentPub != null && name.equalsIgnoreCase(BeerMappingXmlPullParser.NAME)) {
                     String value = parser.nextText();
                     currentPub.setName(value);
                  }

                  break;
               case XmlPullParser.END_TAG:
                  name = parser.getName();
                  ///Log.d(Constants.LOG_TAG, "  end tag: " + name);
                  if (name != null) {
                     if (name.equalsIgnoreCase(BeerMappingXmlPullParser.LOCATION) && (currentPub != null)) {
                        pubs.add(currentPub);
                        currentPub = null;
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
      return pubs;
   }
}