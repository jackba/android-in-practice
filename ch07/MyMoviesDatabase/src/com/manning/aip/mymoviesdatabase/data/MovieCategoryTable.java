package com.manning.aip.mymoviesdatabase.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class MovieCategoryTable {

   public static final String TABLE_NAME = "movie_category";

   public static class MovieCategoryColumns implements BaseColumns {
      public static final String MOVIE_ID = "movie_id";
      public static final String CATEGORY_ID = "category_id";
   }

   public static void onCreate(SQLiteDatabase db) {
      StringBuilder sb = new StringBuilder();

      // movie_category mapping table
      sb.append("CREATE TABLE " + MovieCategoryTable.TABLE_NAME + " (");
      
      sb.append(MovieCategoryColumns.MOVIE_ID + " INTEGER NOT NULL, ");
      sb.append(MovieCategoryColumns.CATEGORY_ID + " INTEGER NOT NULL, ");
      sb.append("FOREIGN KEY(" + MovieCategoryColumns.MOVIE_ID + ") REFERENCES " + MovieTable.TABLE_NAME + "("
               + BaseColumns._ID + "), ");
      sb.append("FOREIGN KEY(" + MovieCategoryColumns.CATEGORY_ID + ") REFERENCES " + CategoryTable.TABLE_NAME + "("
               + BaseColumns._ID + ") , ");
      sb.append("PRIMARY KEY ( " + MovieCategoryColumns.MOVIE_ID + ", " + MovieCategoryColumns.CATEGORY_ID + ")");
      sb.append(");");
      db.execSQL(sb.toString());
   }

   public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + MovieCategoryTable.TABLE_NAME);
      MovieCategoryTable.onCreate(db);
   }
}
