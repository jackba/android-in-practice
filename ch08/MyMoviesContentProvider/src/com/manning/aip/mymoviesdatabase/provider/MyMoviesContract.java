package com.manning.aip.mymoviesdatabase.provider;

import android.net.Uri;
import com.manning.aip.mymoviesdatabase.data.MovieTable;

import java.util.HashMap;

public final class MyMoviesContract {   

   public static final String AUTHORITY = "com.manning.aip.mymoviesdatabase";
   public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
   
   public static final class Movies {      
   
      public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, "movies");
      
      public static abstract class MovieColumns extends MovieTable.MovieColumns {         

         public static final String CATEGORIES = "category_names";

         static final HashMap<String, String> projectionMap = new HashMap<String, String>() {
            {
               put(_ID, _ID);
               put(NAME, NAME);
               put(RATING, RATING);
               put(TAGLINE, TAGLINE);
               put(THUMB_URL, THUMB_URL);
               put(IMAGE_URL, IMAGE_URL);
               put(TRAILER, TRAILER);
               put(URL, URL);
               put(YEAR, YEAR);
               put(CATEGORIES, "mcat.names");
            }
         };

         private MovieColumns() {
         }         
      }
      
      private Movies() {
      }
   }
   
   private MyMoviesContract() {
   }
}
