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
 * Includes SQLiteOpenHelper, and uses Dao objects
 * to create/update/delete and otherwise manipulate data.
 *
 * @author ccollins
 *
 */
public class DataManager {

   private static final int DATABASE_VERSION = 1;

   private Context context;

   private SQLiteDatabase db;

   private CategoryDao categoryDao;
   private MovieDao movieDao;

   public DataManager(final Context context) {

      this.context = context;  

      OpenHelper openHelper = new OpenHelper(this.context);
      db = openHelper.getWritableDatabase();
      Log.i(Constants.LOG_TAG, "DataManager created, db open status: " + db.isOpen());
      
      categoryDao = new CategoryDao(db);
      movieDao = new MovieDao(db);
   }
   
   public SQLiteDatabase getDb() {
      return db;
   }

   public void openDb() {
      if (!db.isOpen()) {
         db = SQLiteDatabase.openDatabase(DataConstants.DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
         // since we pass db into DAO, have to recreate DAO if db is re-opened
         categoryDao = new CategoryDao(db);
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
   // getters/setters
   //
   public CategoryDao getCategoryDao() {
      return categoryDao;
   }
   
   public MovieDao getMovieDao() {
      return movieDao;
   }

   //
   // SQLiteOpenHelper   
   //
   private static class OpenHelper extends SQLiteOpenHelper {

      private Context context;
      
      OpenHelper(final Context context) {
         super(context, DataConstants.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
         this.context = context;
      }

      @Override
      public void onCreate(final SQLiteDatabase db) {
         Log.i(Constants.LOG_TAG, "DataHelper.OpenHelper onCreate creating database " + DataConstants.DATABASE_NAME);
        
         CategoryTable.onCreate(db);         
         // populate initial categories (one way, there are several, this works ok for small data set)
         CategoryDao categoryDao = new CategoryDao(db);
         String[] categories = context.getResources().getStringArray(R.array.tmdb_categories);
         for (String cat : categories) {
            categoryDao.save(new Category(cat));
         }
         
         MovieTable.onCreate(db);
         
         MovieCategoryTable.onCreate(db);
      }

      @Override
      public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
         Log
                  .i(Constants.LOG_TAG, "SQLiteOpenHelper onUpgrade - oldVersion:" + oldVersion + " newVersion:"
                           + newVersion); 
         // TODO DAO onUpgrades here         
      }
   }
}