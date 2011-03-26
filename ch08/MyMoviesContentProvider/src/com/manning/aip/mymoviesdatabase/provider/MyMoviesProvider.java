package com.manning.aip.mymoviesdatabase.provider;

import static com.manning.aip.mymoviesdatabase.provider.MyMoviesContract.AUTHORITY;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.manning.aip.mymoviesdatabase.data.CategoryTable;
import com.manning.aip.mymoviesdatabase.data.CategoryTable.CategoryColumns;
import com.manning.aip.mymoviesdatabase.data.MovieCategoryTable;
import com.manning.aip.mymoviesdatabase.data.MovieCategoryTable.MovieCategoryColumns;
import com.manning.aip.mymoviesdatabase.data.MovieTable;
import com.manning.aip.mymoviesdatabase.data.MovieTable.MovieColumns;
import com.manning.aip.mymoviesdatabase.data.OpenHelper;
import com.manning.aip.mymoviesdatabase.model.Category;

final public class MyMoviesProvider extends ContentProvider {

   final private static int MOVIES = 1;
   final private static int MOVIE_ID = 2;

   final private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

   static {
      uriMatcher.addURI(AUTHORITY, "movies", MOVIES);
      uriMatcher.addURI(AUTHORITY, "movies/#", MOVIE_ID);
   }

   private SQLiteDatabase db;

   @Override
   public boolean onCreate() {
      SQLiteOpenHelper openHelper = new OpenHelper(getContext());
      db = openHelper.getWritableDatabase();

      return true;
   }

   @Override
   public Cursor query(Uri uri, final String[] projection, String selection, String[] selectionArgs, String sortOrder) {

      switch (uriMatcher.match(uri)) {
         case MOVIE_ID:

            long movieId = ContentUris.parseId(uri);

            Set<String> projectionCols = new HashSet<String>(Arrays.asList(projection));
            if (!MyMoviesContract.Movies.MovieColumns.projectionMap.keySet().containsAll(projectionCols)) {
               throw new IllegalArgumentException("Unrecognized column(s) in projection");
            }

            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            StringBuilder tables = new StringBuilder(MovieTable.TABLE_NAME).append(" as outer_movie");

            LinkedList<String> newSelectionArgs = new LinkedList<String>();

            // we represent categories as comma-delimited list of category names.  in order to do so, we use a
            // sub-select across the join table to retrieve the movie's categories and SQLite's group_concat()
            // function to build the string representation.
            if (projectionCols.contains(MyMoviesContract.Movies.MovieColumns.CATEGORIES)) {
               tables.append(" left outer join (select group_concat(").append(CategoryColumns.NAME)
                        .append(") as names from ").append(MovieCategoryTable.TABLE_NAME).append(", ")
                        .append(CategoryTable.TABLE_NAME).append(" where ").append(MovieCategoryTable.TABLE_NAME)
                        .append(".").append(MovieCategoryColumns.MOVIE_ID).append("= ? and ")
                        .append(MovieCategoryTable.TABLE_NAME).append(".").append(MovieCategoryColumns.CATEGORY_ID)
                        .append("=").append(CategoryTable.TABLE_NAME).append(".").append(CategoryColumns._ID)
                        .append(") mcat");

               newSelectionArgs.add(String.valueOf(movieId));
            }

            StringBuilder where = new StringBuilder();
            where.append("outer_movie.").append(MovieColumns._ID).append("= ?");

            newSelectionArgs.add(String.valueOf(movieId));

            qb.setProjectionMap(MyMoviesContract.Movies.MovieColumns.projectionMap);
            qb.setTables(tables.toString());
            qb.appendWhere(where.toString());

            if (selectionArgs != null) {
               newSelectionArgs.addAll(Arrays.asList(selectionArgs));
            }
            String[] allSelectionArgs = newSelectionArgs.toArray(new String[0]);

            return qb.query(db, projection, selection, allSelectionArgs, null, null, sortOrder);

         case UriMatcher.NO_MATCH:
         default:
            throw new IllegalArgumentException("unrecognized URI " + uri);
      }
   }

   @Override
   public String getType(Uri uri) {
      return null;
   }

   @Override
   public Uri insert(Uri uri, ContentValues cv) {
      switch (uriMatcher.match(uri)) {
         case MOVIES:
            db.beginTransaction();
            long id = -1;
            try {
               String categoryNames = "";
               if (cv.containsKey(MyMoviesContract.Movies.MovieColumns.CATEGORIES)) {
                  categoryNames = cv.getAsString(MyMoviesContract.Movies.MovieColumns.CATEGORIES);
                  cv = new ContentValues(cv);
                  cv.remove(MyMoviesContract.Movies.MovieColumns.CATEGORIES);
               }
               id = db.insert(MovieTable.TABLE_NAME, null, cv);
               Iterable<Category> categories = getOrCreateCategories(categoryNames.split(","));
               for (Category cat : categories) {
                  ContentValues movieCat = new ContentValues();
                  movieCat.put(MovieCategoryColumns.MOVIE_ID, id);
                  movieCat.put(MovieCategoryColumns.CATEGORY_ID, cat.getId());
                  db.insert(MovieCategoryTable.TABLE_NAME, null, movieCat);
               }
               db.setTransactionSuccessful();
            } finally {
               db.endTransaction();
            }

            if (id >= 0) {
               return ContentUris.withAppendedId(MyMoviesContract.Movies.CONTENT_URI, id);
            } else {
               return null;
            }
         case UriMatcher.NO_MATCH:
         default:
            throw new IllegalArgumentException("unrecognized URI " + uri);
      }
   }

   @Override
   public int delete(Uri uri, String selection, String[] selectionArgs) {
      switch (uriMatcher.match(uri)) {
         case MOVIE_ID:
            long movieId = ContentUris.parseId(uri);

            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            StringBuilder where = new StringBuilder(MovieColumns._ID).append("=?");
            if (selection != null) {
               where.append(" and ").append(selection);
            }
            selection = where.toString();

            String[] allSelectionArgs = new String[1 + (selectionArgs == null ? 0 : selectionArgs.length)];
            allSelectionArgs[0] = String.valueOf(movieId);
            if (selectionArgs != null) {
               System.arraycopy(selectionArgs, 0, allSelectionArgs, 1, selectionArgs.length);
            }

            String categoriesSelection = new StringBuilder(MovieCategoryColumns.MOVIE_ID).append("=?").toString();
            String[] categoriesArgs = new String[] { String.valueOf(movieId) };

            int rowsDeleted = 0;
            db.beginTransaction();
            try {
               db.delete(MovieCategoryTable.TABLE_NAME, categoriesSelection, categoriesArgs);
               rowsDeleted = db.delete(MovieTable.TABLE_NAME, selection, allSelectionArgs);
               db.setTransactionSuccessful();
            } finally {
               db.endTransaction();
            }

            return rowsDeleted;

         case UriMatcher.NO_MATCH:
         default:
            throw new IllegalArgumentException("unrecognized URI " + uri);
      }
   }

   @Override
   public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
      switch (uriMatcher.match(uri)) {
         case MOVIE_ID:
            long movieId = ContentUris.parseId(uri);

            String categoryNames = null;
            int updated = 0;
            if (contentValues.containsKey(MyMoviesContract.Movies.MovieColumns.CATEGORIES)) {
               contentValues = new ContentValues(contentValues);
               categoryNames = contentValues.getAsString(MyMoviesContract.Movies.MovieColumns.CATEGORIES);
               contentValues.remove(MyMoviesContract.Movies.MovieColumns.CATEGORIES);
            }

            StringBuilder where = new StringBuilder(MovieColumns._ID).append("=?");
            if (selection != null) {
               where.append(" and ").append(selection);
            }
            selection = where.toString();
            String[] allSelectionArgs = new String[1 + (selectionArgs == null ? 0 : selectionArgs.length)];
            allSelectionArgs[0] = String.valueOf(movieId);
            if (selectionArgs != null) {
               System.arraycopy(selectionArgs, 0, allSelectionArgs, 1, selectionArgs.length);
            }

            db.beginTransaction();
            try {
               boolean updateCategories = false;
               if (contentValues.size() > 0) {
                  updated = db.update(MovieTable.TABLE_NAME, contentValues, selection, allSelectionArgs);
                  updateCategories = updated > 0;
               } else {
                  Cursor c =
                           db.query(MovieTable.TABLE_NAME, new String[] { MovieColumns._ID }, selection,
                                    allSelectionArgs, null, null, null);
                  updateCategories = c.getCount() > 0;
                  c.close();
               }

               if (categoryNames != null && updateCategories) {
                  String categoriesSelection = new StringBuilder(MovieCategoryColumns.MOVIE_ID).append("=?").toString();
                  String[] categoriesArgs = new String[] { String.valueOf(movieId) };
                  db.delete(MovieCategoryTable.TABLE_NAME, categoriesSelection, categoriesArgs);

                  Iterable<Category> categories = getOrCreateCategories(categoryNames.split(","));
                  for (Category cat : categories) {
                     ContentValues movieCat = new ContentValues();
                     movieCat.put(MovieCategoryColumns.MOVIE_ID, movieId);
                     movieCat.put(MovieCategoryColumns.CATEGORY_ID, cat.getId());
                     db.insert(MovieCategoryTable.TABLE_NAME, null, movieCat);
                  }
                  updated = 1;
               }
               db.setTransactionSuccessful();
            } finally {
               db.endTransaction();
            }

            return updated;
         case UriMatcher.NO_MATCH:
         default:
            throw new IllegalArgumentException("unrecognized URI " + uri);
      }
   }

   private Iterable<Category> getOrCreateCategories(String... names) {
      LinkedList<Category> categories = new LinkedList<Category>();
      for (String name : names) {
         String where = new StringBuilder(CategoryColumns.NAME).append("=?").toString();
         String[] whereArgs = new String[] { name };
         Cursor cursor =
                  db.query(CategoryTable.TABLE_NAME, new String[] { CategoryColumns._ID, CategoryColumns.NAME }, where,
                           whereArgs, null, null, null);
         if (cursor.moveToFirst()) {
            categories.add(new Category(cursor.getLong(0), cursor.getString(1)));
            cursor.close();
         } else {
            cursor.close();
            ContentValues cv = new ContentValues(1);
            cv.put(CategoryColumns.NAME, name);
            long id = db.insert(CategoryTable.TABLE_NAME, null, cv);
            categories.add(new Category(id, name));
         }
      }
      return categories;
   }
}
