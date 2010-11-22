package com.manning.aip.mymoviesfile.xml;

import android.util.Log;
import android.util.Xml;

import com.manning.aip.mymoviesfile.Constants;
import com.manning.aip.mymoviesfile.model.Movie;
import com.manning.aip.mymoviesfile.model.MovieSearchResult;

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
   static final String RATING = "rating";
   static final String IMAGE = "image";
   static final String THUMB = "thumb";
   private final String ID = "id";

   public TheMovieDBXmlPullFeedParser() {
   }

   protected InputStream getSearchInputStream(String name) {
      URL url = null;
      try {
         url = new URL(TheMovieDBXmlPullFeedParser.SEARCH_FEED_URL + name);
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
         throw new RuntimeException(e);
      }

      return movies;
   }

   public Movie get(String providerId) {
      throw new UnsupportedOperationException("Not yet implemented");
   }
}
