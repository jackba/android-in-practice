package com.manning.aip.mymoviesdatabase.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import com.manning.aip.mymoviesdatabase.data.MovieTable.MovieColumns;
import com.manning.aip.mymoviesdatabase.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDao implements Dao<Movie>, BaseColumns {

   private static final String INSERT =
            "insert into " + MovieTable.TABLE_NAME + "(" + MovieColumns.HOMEPAGE + ", " + MovieColumns.NAME + ", "
                     + MovieColumns.RATING + ", " + MovieColumns.TAGLINE + ", " + MovieColumns.THUMB_URL + ", "
                     + MovieColumns.IMAGE_URL + ", " + MovieColumns.TRAILER + ", " + MovieColumns.URL + ", "
                     + MovieColumns.YEAR + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

   private SQLiteDatabase db;
   private SQLiteStatement insertStatement;

   public MovieDao(SQLiteDatabase db) {
      this.db = db;
      insertStatement = db.compileStatement(MovieDao.INSERT);
   }

   @Override
   public void delete(Movie entity) {
      if (entity.getId() > 0) {
         db.delete(MovieTable.TABLE_NAME, BaseColumns._ID + " = ?", new String[] { String.valueOf(entity.getId()) });
      }
   }

   // get field slot error, show how it works by removing a column in the columns array here)
   @Override
   public Movie get(long id) {
      Movie movie = null;
      Cursor c =
               db.query(MovieTable.TABLE_NAME, new String[] { BaseColumns._ID, MovieColumns.HOMEPAGE,
                        MovieColumns.NAME, MovieColumns.RATING, MovieColumns.TAGLINE, MovieColumns.THUMB_URL,
                        MovieColumns.IMAGE_URL, MovieColumns.TRAILER, MovieColumns.URL, MovieColumns.YEAR },
                        BaseColumns._ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, "1");
      if (c.moveToFirst()) {
         movie = this.buildMovieFromCursor(c);
      }
      if (!c.isClosed()) {
         c.close();
      }
      return movie;
   }

   // as an oversimplification our db requires movie names to be unique
   // in real-life, we'd need to return multiple results here (if found)
   // and allow the user to select, or make query use other attributes in combination with name
   public Movie find(String name) {
      long movieId = 0L;
      String sql = "select _id from " + MovieTable.TABLE_NAME + " where movie_name like ? limit 1";
      Cursor c = db.rawQuery(sql, new String[] { name });
      if (c.moveToFirst()) {
         movieId = c.getLong(0);
      }
      if (!c.isClosed()) {
         c.close();
      }
      // we make another query here, which is another trip, 
      // this is a trade off we accept with such a small amount of data
      return this.get(movieId);
   }

   @Override
   public List<Movie> getAll() {
      List<Movie> list = new ArrayList<Movie>();
      Cursor c =
               db.query(MovieTable.TABLE_NAME, new String[] { BaseColumns._ID, MovieColumns.HOMEPAGE,
                        MovieColumns.NAME, MovieColumns.RATING, MovieColumns.TAGLINE, MovieColumns.THUMB_URL,
                        MovieColumns.IMAGE_URL, MovieColumns.TRAILER, MovieColumns.URL, MovieColumns.YEAR }, null,
                        null, null, null, MovieColumns.NAME, null);
      if (c.moveToFirst()) {
         do {
            Movie movie = this.buildMovieFromCursor(c);
            if (movie != null) {
               list.add(movie);
            }
         } while (c.moveToNext());
      }
      if (!c.isClosed()) {
         c.close();
      }
      return list;
   }

   @Override
   public long save(Movie entity) {
      insertStatement.clearBindings();
      insertStatement.bindString(1, entity.getHomepage());
      insertStatement.bindString(2, entity.getName());
      insertStatement.bindDouble(3, entity.getRating());
      insertStatement.bindString(4, entity.getTagline());
      insertStatement.bindString(5, entity.getThumbUrl());
      insertStatement.bindString(6, entity.getImageUrl());
      insertStatement.bindString(7, entity.getTrailer());
      insertStatement.bindString(8, entity.getUrl());
      insertStatement.bindLong(9, entity.getYear());
      return insertStatement.executeInsert();
   }

   @Override
   public void update(Movie entity) {
      final ContentValues values = new ContentValues();
      values.put(MovieColumns.HOMEPAGE, entity.getHomepage());
      values.put(MovieColumns.NAME, entity.getName());
      values.put(MovieColumns.RATING, entity.getRating());
      values.put(MovieColumns.TAGLINE, entity.getTagline());
      values.put(MovieColumns.THUMB_URL, entity.getThumbUrl());
      values.put(MovieColumns.IMAGE_URL, entity.getImageUrl());
      values.put(MovieColumns.TRAILER, entity.getTrailer());
      values.put(MovieColumns.URL, entity.getUrl());
      values.put(MovieColumns.YEAR, entity.getYear());
      db.update(MovieTable.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[] { String
               .valueOf(entity.getName()) });
   }

   private Movie buildMovieFromCursor(Cursor c) {
      Movie movie = null;
      if (c != null) {
         movie = new Movie();
         movie.setId(c.getLong(0));
         movie.setHomepage(c.getString(1));
         movie.setName(c.getString(2));
         movie.setRating(c.getInt(3));
         movie.setTagline(c.getString(4));
         movie.setThumbUrl(c.getString(5));
         movie.setImageUrl(c.getString(6));
         movie.setTrailer(c.getString(7));
         movie.setUrl(c.getString(8));
         movie.setYear(c.getInt(9));
      }
      return movie;
   }
}
