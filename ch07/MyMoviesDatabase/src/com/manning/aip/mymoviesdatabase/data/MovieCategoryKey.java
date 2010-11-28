package com.manning.aip.mymoviesdatabase.data;

public class MovieCategoryKey {

   private final long movieId;
   private final long categoryId;
   
   public MovieCategoryKey(long movieId, long categoryId) {
      this.movieId = movieId;
      this.categoryId = categoryId;
   }

   public long getMovieId() {
      return this.movieId;
   }

   public long getCategoryId() {
      return this.categoryId;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (this.categoryId ^ (this.categoryId >>> 32));
      result = prime * result + (int) (this.movieId ^ (this.movieId >>> 32));
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (!(obj instanceof MovieCategoryKey)) {
         return false;
      }
      MovieCategoryKey other = (MovieCategoryKey) obj;
      if (this.categoryId != other.categoryId) {
         return false;
      }
      if (this.movieId != other.movieId) {
         return false;
      }
      return true;
   }
}
