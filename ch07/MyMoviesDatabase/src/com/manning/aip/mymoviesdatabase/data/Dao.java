package com.manning.aip.mymoviesdatabase.data;

import java.util.List;

public interface Dao<T> {

   long save(T entity);
   
   void update(T entity);
   
   void delete(T entity);
   
   T get (long id);
   
   List<T> getAll();
   
}
