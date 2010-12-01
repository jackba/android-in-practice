package com.manning.aip.mymoviesdatabase.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class MovieTable {

   public static final String TABLE_NAME = "movie";       
   
   public static class MovieColumns implements BaseColumns {
      public static final String HOMEPAGE = "homepage";
      public static final String NAME = "movie_name";
      public static final String RATING = "rating";
      public static final String TAGLINE = "tagline";      
      public static final String THUMB_URL = "thumb_url";
      public static final String IMAGE_URL = "image_url";
      public static final String TRAILER = "trailer";      
      public static final String URL = "url";      
      public static final String YEAR = "year";      
   }
   
   public static void onCreate(SQLiteDatabase db) {
      StringBuilder sb = new StringBuilder();

      // book table
      sb.append("CREATE TABLE " + TABLE_NAME + " (");
      sb.append(MovieColumns._ID + " INTEGER PRIMARY KEY, ");
      sb.append(MovieColumns.HOMEPAGE + " TEXT, ");
      sb.append(MovieColumns.NAME + " TEXT UNIQUE NOT NULL, "); // movie names aren't unique, but for simplification we constrain
      sb.append(MovieColumns.RATING + " INTEGER, ");
      sb.append(MovieColumns.TAGLINE + " TEXT, ");
      sb.append(MovieColumns.THUMB_URL + " TEXT, ");
      sb.append(MovieColumns.IMAGE_URL + " TEXT, ");
      sb.append(MovieColumns.TRAILER + " TEXT, ");      
      sb.append(MovieColumns.URL + " TEXT, ");
      sb.append(MovieColumns.YEAR + " INTEGER");      
      sb.append(");");
      db.execSQL(sb.toString());
   }

   public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
      MovieTable.onCreate(db);
   }   
}
