package com.manning.aip.mymoviesdatabase.data;

import android.content.Context;
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
      return movieDao.get(movieId);
   }

   public List<Movie> getAllMovies() {
      return movieDao.getAll();
   }

   public Movie findMovie(String name) {
      return movieDao.find(name);
   }

   public long saveMovie(Movie movie) {
      // NOTE could wrap entity manip functions in DataManager, make "manager" for each entity
      // here though, to keep it simpler, we use the DAOs directly (even when multiple are involved)
      long movieId = 0L;
      System.out.println("SAVE MOVIE: " + movie);

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
         movieId = 0L;
      } finally {
         // an "alias" for commit
         db.endTransaction();
      }

      return movieId;
   }

   public void deleteMovie(Movie movie) {
      movieDao.delete(movie);
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