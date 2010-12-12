package com.manning.aip.mymoviesdatabase.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.util.Log;

import com.manning.aip.mymoviesdatabase.Constants;
import com.manning.aip.mymoviesdatabase.R;
import com.manning.aip.mymoviesdatabase.model.Category;
import com.manning.aip.mymoviesdatabase.model.Movie;

import java.util.List;

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
   private MovieCategoryDao movieCategoryDao;

   public DataManager(final Context context) {

      this.context = context;

      OpenHelper openHelper = new OpenHelper(this.context);
      db = openHelper.getWritableDatabase();
      Log.i(Constants.LOG_TAG, "DataManager created, db open status: " + db.isOpen());

      categoryDao = new CategoryDao(db);
      movieDao = new MovieDao(db);
      movieCategoryDao = new MovieCategoryDao(db);
   }

   public SQLiteDatabase getDb() {
      return db;
   }

   public void openDb() {
      if (!db.isOpen()) {
         db = SQLiteDatabase.openDatabase(DataConstants.DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
         // since we pass db into DAO, have to recreate DAO if db is re-opened
         categoryDao = new CategoryDao(db);
         movieDao = new MovieDao(db);
         movieCategoryDao = new MovieCategoryDao(db);
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
   // "Manager" data methods that wrap DAOs
   //
   // this lets us encapsulate usage of DAOs 
   // we only expose methods app is actually using, and we can combine DAOs, with logic in one place
   //  

   // movie
   public Movie getMovie(long movieId) {
      Movie movie = movieDao.get(movieId);
      if (movie != null) {
         movie.getCategories().addAll(movieCategoryDao.getCategories(movie.getId()));
      }
      return movie;
   }

   public List<Movie> getMovieHeaders() {
      // these movies don't have categories, but they're really used as "headers" anyway, it's ok
      return movieDao.getAll();
   }

   public Movie findMovie(String name) {
      Movie movie = movieDao.find(name);
      if (movie != null) {
         movie.getCategories().addAll(movieCategoryDao.getCategories(movie.getId()));
      }
      return movie;
   }

   public long saveMovie(Movie movie) {
      // NOTE could wrap entity manip functions in DataManager, make "manager" for each entity
      // here though, to keep it simpler, we use the DAOs directly (even when multiple are involved)
      long movieId = 0L;

      // put it in a transaction, since we're touching multiple tables
      try {
         db.beginTransaction();

         // first save movie                                 
         movieId = movieDao.save(movie);

         // second, make sure categories exist, and save movie/category association
         // (this makes multiple queries, but usually not many cats, could just save and catch exception too, but that's ugly)
         if (movie.getCategories().size() > 0) {
            for (Category c : movie.getCategories()) {
               long catId = 0L;
               Category dbCat = categoryDao.find(c.getName());
               if (dbCat == null) {
                  catId = categoryDao.save(c);
               } else {
                  catId = dbCat.getId();
               }
               MovieCategoryKey mcKey = new MovieCategoryKey(movieId, catId);
               if (!movieCategoryDao.exists(mcKey)) {
                  movieCategoryDao.save(mcKey);
               }
            }
         }

         db.setTransactionSuccessful();
      } catch (SQLException e) {
         Log.e(Constants.LOG_TAG, "Error saving movie (transaction rolled back)", e);
         movieId = 0L;
      } finally {
         // an "alias" for commit
         db.endTransaction();
      }

      return movieId;
   }

   public boolean deleteMovie(long movieId) {
      boolean result = false;
      // NOTE switch this order around to see constraint error (foreign keys work)
      try {
         db.beginTransaction();
         // make sure to use getMovie and not movieDao directly, categories need to be included
         Movie movie = getMovie(movieId);
         if (movie != null) {
            for (Category c : movie.getCategories()) {
               movieCategoryDao.delete(new MovieCategoryKey(movie.getId(), c.getId()));
            }
            movieDao.delete(movie);
         }
         db.setTransactionSuccessful();
         result = true;
      } catch (SQLException e) {
         Log.e(Constants.LOG_TAG, "Error deleting movie (transaction rolled back)", e);
      } finally {
         db.endTransaction();
      }
      return result;
   }

   // category
   public Category getCategory(long categoryId) {
      return categoryDao.get(categoryId);
   }

   public List<Category> getAllCategories() {
      return categoryDao.getAll();
   }

   public Category findCategory(String name) {
      return categoryDao.find(name);
   }

   public long saveCategory(Category category) {
      return categoryDao.save(category);
   }

   public void deleteCategory(Category category) {
      categoryDao.delete(category);
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
      public void onOpen(final SQLiteDatabase db) {
         super.onOpen(db);
         if (!db.isReadOnly()) {
            // versions of SQLite older than 3.6.19 don't support foreign keys
            // and neither do any version compiled with SQLITE_OMIT_FOREIGN_KEY
            // http://www.sqlite.org/foreignkeys.html#fk_enable
            // 
            // to enable foreign keys on newer versions that allow it, we have to turn them on            
            db.execSQL("PRAGMA foreign_keys=ON;");

            // then we check to make sure they're on 
            // (if this returns no data they aren't even available, so we shouldn't even TRY to use them)
            Cursor c = db.rawQuery("PRAGMA foreign_keys", null);
            if (c.moveToFirst()) {
               int result = c.getInt(0);
               Log.i(Constants.LOG_TAG, "SQLite foreign key support (1 is on, 0 is off): " + result);
            } else {
               // could use this approach in onCreate, and not set foreign keys it not available, etc.
               Log.i(Constants.LOG_TAG, "SQLite foreign key support NOT AVAILABLE");
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
}