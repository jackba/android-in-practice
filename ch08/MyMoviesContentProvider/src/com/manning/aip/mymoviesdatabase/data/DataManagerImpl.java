package com.manning.aip.mymoviesdatabase.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.util.Log;

import com.manning.aip.mymoviesdatabase.Constants;
import com.manning.aip.mymoviesdatabase.data.MovieTable.MovieColumns;
import com.manning.aip.mymoviesdatabase.model.Category;
import com.manning.aip.mymoviesdatabase.model.Movie;

import java.util.List;

/**
 * Android DataManagerImpl to encapsulate SQL and DB details.
 * Includes SQLiteOpenHelper, and uses Dao objects
 * to create/update/delete and otherwise manipulate data.
 *
 * @author ccollins
 *
 */
public class DataManagerImpl implements DataManager {

   private Context context;

   private SQLiteDatabase db;

   private CategoryDao categoryDao;
   private MovieDao movieDao;
   private MovieCategoryDao movieCategoryDao;

   public DataManagerImpl(Context context) {

      this.context = context;

      SQLiteOpenHelper openHelper = new OpenHelper(this.context);
      db = openHelper.getWritableDatabase();
      Log.i(Constants.LOG_TAG, "DataManagerImpl created, db open status: " + db.isOpen());

      categoryDao = new CategoryDao(db);
      movieDao = new MovieDao(db);
      movieCategoryDao = new MovieCategoryDao(db);
   }

   public SQLiteDatabase getDb() {
      return db;
   }

   private void openDb() {
      if (!db.isOpen()) {
         db = SQLiteDatabase.openDatabase(DataConstants.DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
         // since we pass db into DAO, have to recreate DAO if db is re-opened
         categoryDao = new CategoryDao(db);
         movieDao = new MovieDao(db);
         movieCategoryDao = new MovieCategoryDao(db);
      }
   }

   private void closeDb() {
      if (db.isOpen()) {
         db.close();
      }
   }

   private void resetDb() {
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
   @Override
   public Movie getMovie(long movieId) {
      Movie movie = movieDao.get(movieId);
      if (movie != null) {
         movie.getCategories().addAll(movieCategoryDao.getCategories(movie.getId()));
      }
      return movie;
   }

   @Override
   public List<Movie> getMovieHeaders() {
      // these movies don't have categories, but they're really used as "headers" anyway, it's ok
      return movieDao.getAll();
   }

   @Override
   public Movie findMovie(String name) {
      Movie movie = movieDao.find(name);
      if (movie != null) {
         movie.getCategories().addAll(movieCategoryDao.getCategories(movie.getId()));
      }
      return movie;
   }

   @Override
   public long saveMovie(Movie movie) {
      // NOTE could wrap entity manip functions in DataManagerImpl, make "manager" for each entity
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

   @Override
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

   @Override
   public Cursor getMovieCursor() {
      // note that query MUST have a column named _id
      return db.rawQuery("select " + MovieColumns._ID + ", " + MovieColumns.NAME + ", " + MovieColumns.THUMB_URL
               + " from " + MovieTable.TABLE_NAME, null);
   }

   // category
   @Override
   public Category getCategory(long categoryId) {
      return categoryDao.get(categoryId);
   }

   @Override
   public List<Category> getAllCategories() {
      return categoryDao.getAll();
   }

   @Override
   public Category findCategory(String name) {
      return categoryDao.find(name);
   }

   @Override
   public long saveCategory(Category category) {
      return categoryDao.save(category);
   }

   @Override
   public void deleteCategory(Category category) {
      categoryDao.delete(category);
   }

}