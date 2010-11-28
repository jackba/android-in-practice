package com.manning.aip.mymoviesdatabase.xml;

import android.util.Log;
import android.util.Xml;

import com.manning.aip.mymoviesdatabase.Constants;
import com.manning.aip.mymoviesdatabase.model.Category;
import com.manning.aip.mymoviesdatabase.model.Movie;
import com.manning.aip.mymoviesdatabase.model.MovieSearchResult;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TheMovieDBXmlPullFeedParser implements MovieFeed {

   // to use TheMovieDb.org feed you need an API key, get your own
   // (this one was created for the book and won't be around forever)
   // http://api.themoviedb.org/
   private static final String API_KEY = "e83a393a2cd8bf5a978ee1909e32d531";

   // feed urls
   private static final String SEARCH_FEED_URL = "http://api.themoviedb.org/2.1/Movie.search/en/xml/" + API_KEY + "/";
   private static final String INFO_FEED_URL = "http://api.themoviedb.org/2.1/Movie.getInfo/en/xml/" + API_KEY + "/";

   // names of the XML tags   
   private static final String MOVIE = "movie";
   private static final String NAME = "name";
   private static final String YEAR = "year";
   private static final String RELEASED = "released";
   private static final String RATING = "rating";
   private static final String TAGLINE = "tagline";
   private static final String TRAILER = "trailer";
   private static final String URL = "url";
   private static final String HOMEPAGE = "homepage";
   private static final String IMAGE = "image";
   private static final String IMAGES = "images";
   private static final String THUMB = "thumb";
   private static final String POSTER = "poster";
   private static final String CATEGORIES = "categories";
   private static final String CATEGORY = "category";
   private static final String TYPE = "type";
   private static final String SIZE = "size";
   private final String ID = "id";

   private InputStream getSearchInputStream(String name) {
      URL url = null;
      try {
         url = new URL(TheMovieDBXmlPullFeedParser.SEARCH_FEED_URL + URLEncoder.encode(name));
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

   private InputStream getInfoInputStream(String tmdbId) {
      URL url = null;
      try {
         url = new URL(TheMovieDBXmlPullFeedParser.INFO_FEED_URL + URLEncoder.encode(tmdbId));
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

   @Override
   public ArrayList<MovieSearchResult> search(String searchName) {
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

   @Override
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
                     String type = parser.getAttributeValue(parser.getNamespace(), TYPE);
                     String size = parser.getAttributeValue(parser.getNamespace(), SIZE);
                     String url = parser.getAttributeValue(parser.getNamespace(), URL);
                     if ((type != null && type.equalsIgnoreCase(POSTER))
                              && (size != null && size.equalsIgnoreCase(THUMB))) {
                        movie.setThumbUrl(url);
                     }
                  }
                  
                  if (name.equalsIgnoreCase(CATEGORY)) {
                     String categoryName = parser.getAttributeValue(parser.getNamespace(), NAME);
                     Category category = new Category(categoryName);
                     if (!movie.getCategories().contains(category)) {
                        movie.getCategories().add(category);
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
