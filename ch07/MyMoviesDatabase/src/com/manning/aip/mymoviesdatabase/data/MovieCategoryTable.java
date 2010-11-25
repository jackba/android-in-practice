package com.manning.aip.mymoviesdatabase.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.manning.aip.mymoviesdatabase.data.CategoryTable.CategoryColumns;
import com.manning.aip.mymoviesdatabase.data.MovieTable.MovieColumns;

public final class MovieCategoryTable {   

   public static final String TABLE_NAME = "movie_category";       
   
   public static class MovieCategoryColumns implements BaseColumns {
      public static final String MOVIE_ID = "movie_id";
      public static final String CATEGORY_ID = "category_id";
   }
   
   public static void onCreate(SQLiteDatabase db) {
      StringBuilder sb = new StringBuilder();

      // movie_category join table
      sb.append("CREATE TABLE " + TABLE_NAME + " (");
      sb.append(MovieCategoryColumns._ID + " INTEGER PRIMARY KEY, ");
      sb.append(MovieCategoryColumns.CATEGORY_ID + " INTEGER, ");
      sb.append(MovieCategoryColumns.MOVIE_ID + " INTEGER, ");
      sb.append("FOREIGN KEY(" + MovieCategoryColumns.CATEGORY_ID + ") REFERENCES " + CategoryTable.TABLE_NAME + "("
               + CategoryColumns._ID + ") ,");
      sb.append("FOREIGN KEY(" + MovieCategoryColumns.MOVIE_ID + ") REFERENCES " + MovieTable.TABLE_NAME + "("
               + MovieColumns._ID + ")");
      
      sb.append(");");
      db.execSQL(sb.toString());
   }

   public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
      MovieCategoryTable.onCreate(db);
   }
}
