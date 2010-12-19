package com.manning.aip.mymoviesdatabase.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class CategoryTable {

   public static final String TABLE_NAME = "category";

   public static class CategoryColumns implements BaseColumns {
      public static final String NAME = "name";
   }

   public static void onCreate(SQLiteDatabase db) {
      StringBuilder sb = new StringBuilder();

      // category table
      sb.append("CREATE TABLE " + CategoryTable.TABLE_NAME + " (");
      sb.append(BaseColumns._ID + " INTEGER PRIMARY KEY, ");
      sb.append(CategoryColumns.NAME + " TEXT UNIQUE NOT NULL");
      sb.append(");");
      db.execSQL(sb.toString());
   }

   public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS " + CategoryTable.TABLE_NAME);
      CategoryTable.onCreate(db);
   }

}
