package com.manning.aip.mymoviesdatabase.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.manning.aip.mymoviesdatabase.Constants;
import com.manning.aip.mymoviesdatabase.R;
import com.manning.aip.mymoviesdatabase.model.Category;

//
// SQLiteOpenHelper   
//
public class OpenHelper extends SQLiteOpenHelper {

   private static final int DATABASE_VERSION = 1;
   
   private Context context;

   public OpenHelper(final Context context) {
      super(context, DataConstants.DATABASE_NAME, null, DATABASE_VERSION);
      this.context = context;
   }

   @Override
   public void onOpen(final SQLiteDatabase db) {
      super.onOpen(db);
      if (!db.isReadOnly()) {
         // versions of SQLite older than 3.6.19 don't support foreign keys
         // and neither do any version compiled with SQLITE_OMIT_FOREIGN_KEY
         // http://www.sqlite.org/foreignkeys.html#fk_enable
         // 
         // make sure foreign key support is turned on if it's there (should be already, just a double-checker)          
         db.execSQL("PRAGMA foreign_keys=ON;");

         // then we check to make sure they're on 
         // (if this returns no data they aren't even available, so we shouldn't even TRY to use them)
         Cursor c = db.rawQuery("PRAGMA foreign_keys", null);
         if (c.moveToFirst()) {
            int result = c.getInt(0);
            Log.i(Constants.LOG_TAG, "SQLite foreign key support (1 is on, 0 is off): " + result);
         } else {
            // could use this approach in onCreate, and not rely on foreign keys it not available, etc.
            Log.i(Constants.LOG_TAG, "SQLite foreign key support NOT AVAILABLE");
            // if you had to here you could fall back to triggers
         }
         if (!c.isClosed()) {
            c.close();
         }
      }
   }

   @Override
   public void onCreate(final SQLiteDatabase db) {
      Log.i(Constants.LOG_TAG, "DataHelper.OpenHelper onCreate creating database " + DataConstants.DATABASE_NAME);

      CategoryTable.onCreate(db);
      // populate initial categories (one way, there are several, this works ok for small data set)
      // (this is just as an example, MyMovies really doesn't use/need the "category manager"
      CategoryDao categoryDao = new CategoryDao(db);
      String[] categories = context.getResources().getStringArray(R.array.tmdb_categories);
      for (String cat : categories) {
         categoryDao.save(new Category(0, cat));
      }

      MovieTable.onCreate(db);

      MovieCategoryTable.onCreate(db);
   }

   @Override
   public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
      Log
               .i(Constants.LOG_TAG, "SQLiteOpenHelper onUpgrade - oldVersion:" + oldVersion + " newVersion:"
                        + newVersion);

      MovieCategoryTable.onUpgrade(db, oldVersion, newVersion);

      MovieTable.onUpgrade(db, oldVersion, newVersion);

      CategoryTable.onUpgrade(db, oldVersion, newVersion);
   }
}
