package com.manning.aip.mymoviesdatabase.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.util.Log;

import com.manning.aip.mymoviesdatabase.Constants;
import com.manning.aip.mymoviesdatabase.R;
import com.manning.aip.mymoviesdatabase.model.Category;

/**
 * Android DataManager to encapsulate SQL and DB details.
 * Includes SQLiteOpenHelper, and uses Dao objects (in specified order)
 * to create/update and clear tables, and manipulate data.
 *
 * @author ccollins
 *
 */
public class DataManager {

   private static final int DATABASE_VERSION = 1;

   private Context context;

   // Use a wrapper BackupManager, because it's only available on API level 8 and above
   //private BackupManagerWrapper backupManager;

   private SQLiteDatabase db;

   //private CategoryDao categoryDAO;
   //private MovieDAO movieDAO;


   public DataManager(final Context context) {

      this.context = context;

      /*
      try { 
         BackupManagerWrapper.isAvailable();
         backupManager = new BackupManagerWrapper(this.context);
      } catch (Throwable t) {
         Log.i(Constants.LOG_TAG, "BackupManager not available (older Android version). BackupManager will not be used.");
      } 
      */     

      OpenHelper openHelper = new OpenHelper(this.context);
      db = openHelper.getWritableDatabase();
      Log.i(Constants.LOG_TAG, "DataManager created, db open status: " + db.isOpen());
   }
   
   public SQLiteDatabase getDb() {
      return db;
   }

   public void openDb() {
      if (!db.isOpen()) {
         db = SQLiteDatabase.openDatabase(DataConstants.DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);         
      }
   }

   public void closeDb() {
      if (db.isOpen()) {
         db.close();
      }
   }

   public void resetDb() {
      Log.i(Constants.LOG_TAG, "Resetting database connection (close and re-open).");
      closeDb();
      SystemClock.sleep(500);
      openDb();
   }

   //
   // wrapped DB methods
   //
   

   //
   // end DB methods
   //  

   //
   // SQLiteOpenHelper   
   //
   private static class OpenHelper extends SQLiteOpenHelper {

      private boolean dbCreated;
      private Context context;
      
      OpenHelper(final Context context) {
         super(context, DataConstants.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
         this.context = context;
      }

      @Override
      public void onCreate(final SQLiteDatabase db) {
         Log.i(Constants.LOG_TAG, "DataHelper.OpenHelper onCreate creating database " + DataConstants.DATABASE_NAME);
        
         CategoryTable.onCreate(db);
         
         // populate intial categories
         CategoryDao categoryDao = new CategoryDao(db);
         String[] categories = context.getResources().getStringArray(R.array.tmdb_categories);
         for (String cat : categories) {
            categoryDao.save(new Category(cat));
         }         
         
         dbCreated = true;
      }

      @Override
      public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
         Log
                  .i(Constants.LOG_TAG, "SQLiteOpenHelper onUpgrade - oldVersion:" + oldVersion + " newVersion:"
                           + newVersion); 
         
         
      }

      public boolean isDbCreated() {
         return dbCreated;
      }
   }
}