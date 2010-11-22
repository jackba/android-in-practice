package com.manning.aip.mymoviesfile.model;


public class Movie {

   private String providerId;
   private String name;
   private int year;   
   private double rating;
   private String url;
   private String homepage;
   private String trailer;
   private Director director;
   
   public String getProviderId() {
      return this.providerId;
   }
   public void setProviderId(String providerId) {
      this.providerId = providerId;
   }
   public String getName() {
      return this.name;
   }
   public void setName(String name) {
      this.name = name;
   }
   public int getYear() {
      return this.year;
   }
   public void setYear(int year) {
      this.year = year;
   }
   public double getRating() {
      return this.rating;
   }
   public void setRating(double rating) {
      this.rating = rating;
   }
   public String getUrl() {
      return this.url;
   }
   public void setUrl(String url) {
      this.url = url;
   }
   public String getHomepage() {
      return this.homepage;
   }
   public void setHomepage(String homepage) {
      this.homepage = homepage;
   }
   public String getTrailer() {
      return this.trailer;
   }
   public void setTrailer(String trailer) {
      this.trailer = trailer;
   }
   public Director getDirector() {
      return this.director;
   }
   public void setDirector(Director director) {
      this.director = director;
   }
   @Override
   public String toString() {
      return "Movie [director=" + this.director + ", homepage=" + this.homepage + ", name=" + this.name + ", rating="
               + this.rating + ", providerId=" + this.providerId + ", trailer=" + this.trailer + ", url=" + this.url
               + ", year=" + this.year + "]";
   }
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.director == null) ? 0 : this.director.hashCode());
      result = prime * result + ((this.homepage == null) ? 0 : this.homepage.hashCode());
      result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
      result = prime * result + ((this.providerId == null) ? 0 : this.providerId.hashCode());
      long temp;
      temp = Double.doubleToLongBits(this.rating);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + ((this.trailer == null) ? 0 : this.trailer.hashCode());
      result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
      result = prime * result + this.year;
      return result;
   }
   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof Movie))
         return false;
      Movie other = (Movie) obj;
      if (this.director == null) {
         if (other.director != null)
            return false;
      } else if (!this.director.equals(other.director))
         return false;
      if (this.homepage == null) {
         if (other.homepage != null)
            return false;
      } else if (!this.homepage.equals(other.homepage))
         return false;
      if (this.name == null) {
         if (other.name != null)
            return false;
      } else if (!this.name.equals(other.name))
         return false;
      if (Double.doubleToLongBits(this.rating) != Double.doubleToLongBits(other.rating))
         return false;
      if (this.providerId != other.providerId)
         return false;
      if (this.trailer == null) {
         if (other.trailer != null)
            return false;
      } else if (!this.trailer.equals(other.trailer))
         return false;
      if (this.url == null) {
         if (other.url != null)
            return false;
      } else if (!this.url.equals(other.url))
         return false;
      if (this.year != other.year)
         return false;
      return true;
   }   
}
