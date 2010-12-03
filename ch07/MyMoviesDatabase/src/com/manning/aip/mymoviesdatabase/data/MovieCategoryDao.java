package com.manning.aip.mymoviesdatabase.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import com.manning.aip.mymoviesdatabase.data.CategoryTable.CategoryColumns;
import com.manning.aip.mymoviesdatabase.data.MovieCategoryTable.MovieCategoryColumns;
import com.manning.aip.mymoviesdatabase.model.Category;

import java.util.ArrayList;
import java.util.List;

// special DAO in this case, so doesn't implement Dao
// doesn't return an entity, and key is not long (composite key)
// this is used for the movie/category mapping table (not for an entity)
public class MovieCategoryDao implements BaseColumns {

   private static final String INSERT =
            "insert into " + MovieCategoryTable.TABLE_NAME + "(" + MovieCategoryColumns.MOVIE_ID + ", "
                     + MovieCategoryColumns.CATEGORY_ID + ") values (?, ?)";

   private SQLiteDatabase db;
   private SQLiteStatement insertStatement;

   public MovieCategoryDao(SQLiteDatabase db) {
      this.db = db;
      insertStatement = db.compileStatement(MovieCategoryDao.INSERT);
   }

   public void delete(MovieCategoryKey key) {
      if ((key.getMovieId() > 0) && (key.getCategoryId() > 0)) {
         db.delete(MovieCategoryTable.TABLE_NAME, MovieCategoryColumns.MOVIE_ID + " = ? and "
                  + MovieCategoryColumns.CATEGORY_ID + " = ?", new String[] { String.valueOf(key.getMovieId()),
                  String.valueOf(key.getCategoryId()) });
      }
   }

   public long save(MovieCategoryKey entity) {
      insertStatement.clearBindings();
      insertStatement.bindLong(1, entity.getMovieId());
      insertStatement.bindLong(2, entity.getCategoryId());
      return insertStatement.executeInsert();
   }

   public boolean exists(MovieCategoryKey key) {
      boolean result = false;
      Cursor c =
               db.query(MovieCategoryTable.TABLE_NAME, new String[] { MovieCategoryColumns.MOVIE_ID,
                        MovieCategoryColumns.CATEGORY_ID }, MovieCategoryColumns.MOVIE_ID + " = ? and "
                        + MovieCategoryColumns.CATEGORY_ID + " = ?", new String[] { String.valueOf(key.getMovieId()),
                        String.valueOf(key.getCategoryId()) }, null, null, null, "1");
      if (c.moveToFirst()) {
         result = true; // don't just "return true" here, or Cursor won't get closed ;)
      }
      if (!c.isClosed()) {
         c.close();
      }
      return result;
   }

   public List<Category> getCategories(long movieId) {
      List<Category> list = new ArrayList<Category>();
      // join movie_category and category, so we can get category name in one query
      String sql =
               "select " + MovieCategoryColumns.CATEGORY_ID + ", " + CategoryColumns.NAME + " from "
                        + MovieCategoryTable.TABLE_NAME + ", " + CategoryTable.TABLE_NAME + " where "
                        + MovieCategoryColumns.MOVIE_ID + " = ? and " + MovieCategoryColumns.CATEGORY_ID + " = "
                        + BaseColumns._ID;
      Cursor c = db.rawQuery(sql, new String[] { String.valueOf(movieId) });
      if (c.moveToFirst()) {
         do {
            Category category = new Category(c.getLong(0), c.getString(1));
            list.add(category);
         } while (c.moveToNext());
      }
      if (!c.isClosed()) {
         c.close();
      }
      return list;
   }

   // if we had more sophisticated searching we might want movies by category
   /*
   public List<Movie> getMovies(long categoryId) {
      List<Movie> list = new ArrayList<Movie>();
      
      return list;
   }
   */
}
