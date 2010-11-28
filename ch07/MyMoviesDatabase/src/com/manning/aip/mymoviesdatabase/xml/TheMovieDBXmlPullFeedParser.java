package com.manning.aip.mymoviesdatabase.xml;

import android.util.Log;
import android.util.Xml;

import com.manning.aip.mymoviesdatabase.Constants;
import com.manning.aip.mymoviesdatabase.model.Movie;
import com.manning.aip.mymoviesdatabase.model.MovieSearchResult;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TheMovieDBXmlPullFeedParser implements MovieFeed {

   static final String SEARCH_FEED_URL =
            "http://api.themoviedb.org/2.1/Movie.search/en/xml/e83a393a2cd8bf5a978ee1909e32d531/";
   static final String INFO_FEED_URL =
            "http://api.themoviedb.org/2.1/Movie.getInfo/en/xml/e83a393a2cd8bf5a978ee1909e32d531/";

   // names of the XML tags   
   static final String MOVIE = "movie";
   static final String NAME = "name";
   static final String YEAR = "year";
   static final String RELEASED = "released";
   static final String RATING = "rating";
   static final String TAGLINE = "tagline";
   static final String TRAILER = "trailer";
   static final String URL = "url";
   static final String HOMEPAGE = "homepage";
   static final String IMAGE = "image";
   static final String IMAGES = "images";
   static final String THUMB = "thumb";
   static final String POSTER = "poster";
   private final String ID = "id";

   public TheMovieDBXmlPullFeedParser() {
   }

   protected InputStream getSearchInputStream(String name) {
      URL url = null;
      try {
         url = new URL(TheMovieDBXmlPullFeedParser.SEARCH_FEED_URL + name);
         Log.d(Constants.LOG_TAG, "Movie search URL: " + url);
      } catch (MalformedURLException e) {
         throw new RuntimeException(e);
      }

      try {
         return url.openConnection().getInputStream();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   protected InputStream getInfoInputStream(String tmdbId) {
      URL url = null;
      try {
         url = new URL(TheMovieDBXmlPullFeedParser.INFO_FEED_URL + tmdbId);
         Log.d(Constants.LOG_TAG, "Movie info URL: " + url);
      } catch (MalformedURLException e) {
         throw new RuntimeException(e);
      }

      try {
         return url.openConnection().getInputStream();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }

   public ArrayList<MovieSearchResult> search(String searchName) {
      ///Log.d(Constants.LOG_TAG, "parse invoked");
      ArrayList<MovieSearchResult> movies = new ArrayList<MovieSearchResult>();
      XmlPullParser parser = Xml.newPullParser();
      try {
         // auto-detect the encoding from the stream
         parser.setInput(this.getSearchInputStream(searchName), null);
         int eventType = parser.getEventType();

         MovieSearchResult movie = null;
         while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            switch (eventType) {
               case XmlPullParser.START_DOCUMENT:
                  //Log.d(Constants.LOG_TAG, " **** start document");
                  break;
               case XmlPullParser.START_TAG:
                  name = parser.getName();
                  String nextText = parser.nextText();
                  if (name.equalsIgnoreCase(MOVIE)) {
                     movie = new MovieSearchResult();
                  }
                  if (name.equalsIgnoreCase(NAME)) {
                     movie.setName(nextText);
                  }
                  if (name.equalsIgnoreCase(ID)) {
                     movie.setProviderId(nextText);
                  }
                  break;
               case XmlPullParser.END_TAG:
                  name = parser.getName();
                  if (name.equalsIgnoreCase(MOVIE)) {
                     if (movie != null) {
                        movies.add(movie);
                     }
                  }
                  break;
            }
            eventType = parser.next();
         }

      } catch (Exception e) {
         Log.e(Constants.LOG_TAG, "Exception parsing XML", e);
      }

      return movies;
   }

   public Movie get(String providerId) {
      Movie movie = null;
      XmlPullParser parser = Xml.newPullParser();
      try {
         // auto-detect the encoding from the stream
         parser.setInput(this.getInfoInputStream(providerId), null);
         int eventType = parser.getEventType();

         while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;
            
            switch (eventType) {
               case XmlPullParser.START_DOCUMENT:
                  break;
               case XmlPullParser.START_TAG: 
                  name = parser.getName();

                  // we handle image tags, which are empty and have only attributes, before we call parser.nextText()
                  // (nextX will move to the END_TAG and then ExpatParser will throw an exception 
                  // (can't get attributes on END)
                  if (name.equalsIgnoreCase(IMAGE)) {                     
                     String type = parser.getAttributeValue(parser.getNamespace(), "type");
                     String size = parser.getAttributeValue(parser.getNamespace(), "size");
                     String url = parser.getAttributeValue(parser.getNamespace(), "url");
                     if ((type != null && type.equalsIgnoreCase("poster"))
                              && (size != null && size.equalsIgnoreCase("thumb"))) {
                        movie.setThumbUrl(url);
                     }                     
                  }
                  
                  String nextText = parser.nextText();
                  if (name.equalsIgnoreCase(MOVIE)) {
                     movie = new Movie();
                  }
                  if (name.equalsIgnoreCase(ID)) {
                     movie.setProviderId(nextText);
                  }
                  if (name.equalsIgnoreCase(HOMEPAGE)) {
                     movie.setHomepage(nextText);
                  }
                  if (name.equalsIgnoreCase(NAME)) {
                     movie.setName(nextText);
                  }
                  if (name.equalsIgnoreCase(RATING)) {
                     // TODO catch parse issues
                     movie.setRating(Double.parseDouble(nextText));
                  }
                  if (name.equalsIgnoreCase(TAGLINE)) {
                     movie.setTagline(nextText);
                  }
                  if (name.equalsIgnoreCase(TRAILER)) {
                     movie.setTrailer(nextText);
                  }
                  if (name.equalsIgnoreCase(URL)) {
                     movie.setUrl(nextText);
                  }
                  if (name.equalsIgnoreCase(RELEASED)) {
                     if (nextText != null && !nextText.equals("0")) {
                        String yearString = nextText.substring(0, 4);
                        // TODO catch parse issues
                        movie.setYear(Integer.parseInt(yearString));
                     }
                  }                  
                  break;
               case XmlPullParser.END_TAG:
                  name = parser.getName();
                  if (name.equalsIgnoreCase(MOVIE)) {
                     return movie;
                  }                 
                  break;
            }
            eventType = parser.next();
         }

      } catch (Exception e) {
         Log.e(Constants.LOG_TAG, "Exception parsing XML", e);
      }
      return movie;
   }
}
