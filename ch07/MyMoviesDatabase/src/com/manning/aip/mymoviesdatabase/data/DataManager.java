package com.manning.aip.mymoviesdatabase.data;

import com.manning.aip.mymoviesdatabase.model.Category;
import com.manning.aip.mymoviesdatabase.model.Movie;

import java.util.List;

/**
 * Android DataManager interface used to define data operations.
 * 
 * @author ccollins
 *
 */
public interface DataManager {  
   
   // movie
   public Movie getMovie(long movieId);

   public List<Movie> getMovieHeaders();

   public Movie findMovie(String name);

   public long saveMovie(Movie movie);
   
   public boolean deleteMovie(long movieId);
   
   // category
   public Category getCategory(long categoryId);

   public List<Category> getAllCategories();

   public Category findCategory(String name);

   public long saveCategory(Category category);

   public void deleteCategory(Category category);   
}