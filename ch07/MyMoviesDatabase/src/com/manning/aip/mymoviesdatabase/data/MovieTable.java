package com.manning.aip.mymoviesdatabase.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class MovieTable {

   public static final String TABLE_NAME = "movie";       
   
   public static class MovieColumns implements BaseColumns {
      public static final String NAME = "movie_name";
      public static final String YEAR = "year";
      public static final String RATING = "rating";
      public static final String URL = "url";
      public static final String HOMEPAGE = "homepage";
      public static final String TRAILER = "trailer";      
   }
   
   public static void onCreate(SQLiteDatabase db) {
      StringBuilder sb = new StringBuilder();

      // book table
      sb.append("CREATE TABLE " + TABLE_NAME + " (");
      sb.append(MovieColumns._ID + " INTEGER PRIMARY KEY, ");
      sb.append(MovieColumns.NAME + " TEXT, "); // movie names aren't unique, will need a smart get
      sb.append(MovieColumns.YEAR + " TEXT NOT NULL CHECK(year > 1900), ");
      sb.append(MovieColumns.RATING + " INTEGER, ");
      sb.append(MovieColumns.URL + " TEXT, ");
      sb.append(MovieColumns.HOMEPAGE + " TEXT, ");
      sb.append(MovieColumns.TRAILER + " TEXT");
      
      sb.append(");");
      db.execSQL(sb.toString());
   }

   public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
      MovieTable.onCreate(db);
   }   
}
