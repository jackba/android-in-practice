package com.manning.aip.mymoviesdatabase.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import com.manning.aip.mymoviesdatabase.data.CategoryTable.CategoryColumns;
import com.manning.aip.mymoviesdatabase.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDao implements Dao<Category>, BaseColumns {

   private static final String INSERT =
            "insert into " + CategoryTable.TABLE_NAME + "(" + CategoryColumns.NAME + ") values (?)";

   private SQLiteDatabase db;
   private SQLiteStatement insertStatement;

   public CategoryDao(SQLiteDatabase db) {
      this.db = db;
      insertStatement = db.compileStatement(INSERT);
   }

   @Override
   public void delete(Category entity) {
      if (entity.getId() > 0) {
         db.delete(CategoryTable.TABLE_NAME, CategoryColumns._ID + " = ?", new String[] { String
                  .valueOf(entity.getId()) });
      }
   }

   // get field slot error, show how it works by removing a column in the columns array here)
   @Override
   public Category get(long id) {
      Category category = null;
      Cursor c =
               db.query(CategoryTable.TABLE_NAME, new String[] { CategoryColumns._ID, CategoryColumns.NAME }, CategoryColumns._ID + " = ?",
                        new String[] { String.valueOf(id) }, null, null, null, "1");
      if (c.moveToFirst()) {
         category = new Category();
         category.setId(c.getLong(0));
         category.setName(c.getString(1));
      }
      if (!c.isClosed()) {
         c.close();
      }
      return category;
   }

   @Override
   public List<Category> getAll() {
      List<Category> list = new ArrayList<Category>();
      Cursor c =
               db.query(CategoryTable.TABLE_NAME, new String[] { CategoryColumns._ID, CategoryColumns.NAME }, null,
                        null, null, null, CategoryColumns.NAME, null);
      if (c.moveToFirst()) {
         do {
            Category category = new Category();
            category.setId(c.getLong(0));
            category.setName(c.getString(1));
            list.add(category);
         } while (c.moveToNext());
      }
      if (!c.isClosed()) {
         c.close();
      }
      return list;
   }

   @Override
   public long save(Category entity) {
      insertStatement.clearBindings();
      insertStatement.bindString(1, entity.getName());
      return insertStatement.executeInsert();
   }

   @Override
   public void update(Category entity) {
      final ContentValues values = new ContentValues();
      values.put(CategoryColumns.NAME, entity.getName());
      db.update(CategoryTable.TABLE_NAME, values, CategoryColumns._ID + " = ?", new String[] { String.valueOf(entity
               .getName()) });
   }
}
