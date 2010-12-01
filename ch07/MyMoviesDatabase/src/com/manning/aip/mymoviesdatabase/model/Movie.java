package com.manning.aip.mymoviesdatabase.model;

import java.util.LinkedHashSet;
import java.util.Set;

public class Movie extends ModelBase {

   private String providerId;
   private String name;
   private int year;
   private double rating;
   private String url;
   private String homepage;
   private String trailer;
   private String tagline;
   private String thumbUrl;
   private String imageUrl;
   private Set<Category> categories;

   // note, in the real-world making these model beans immutable would be a better approach
   // (that is to say, not making them JavaBeans, but makign immutable model classes with Builder)

   public Movie() {
      this.categories = new LinkedHashSet<Category>();
   }

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

   public String getTagline() {
      return this.tagline;
   }

   public void setTagline(String tagline) {
      this.tagline = tagline;
   }

   public String getThumbUrl() {
      return this.thumbUrl;
   }

   public void setThumbUrl(String thumbUrl) {
      this.thumbUrl = thumbUrl;
   }

   public String getImageUrl() {
      return this.imageUrl;
   }

   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }

   public Set<Category> getCategories() {
      return this.categories;
   }

   public void setCategories(Set<Category> categories) {
      this.categories = categories;
   }

   @Override
   public String toString() {
      return "Movie [categories=" + this.categories + ", homepage=" + this.homepage + ", name=" + this.name
               + ", providerId=" + this.providerId + ", rating=" + this.rating + ", tagline=" + this.tagline
               + ", thumbUrl=" + this.thumbUrl + ", imageUrl=" + this.imageUrl + ", trailer=" + this.trailer + ", url="
               + this.url + ", year=" + this.year + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = super.hashCode();
      result = prime * result + ((this.categories == null) ? 0 : this.categories.hashCode());
      result = prime * result + ((this.homepage == null) ? 0 : this.homepage.hashCode());
      // upper name so hashCode is consistent with equals (equals ignores case)
      result = prime * result + ((this.name == null) ? 0 : this.name.toUpperCase().hashCode());
      result = prime * result + ((this.providerId == null) ? 0 : this.providerId.hashCode());
      long temp;
      temp = Double.doubleToLongBits(this.rating);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + ((this.tagline == null) ? 0 : this.tagline.hashCode());
      result = prime * result + ((this.thumbUrl == null) ? 0 : this.thumbUrl.hashCode());
      result = prime * result + ((this.imageUrl == null) ? 0 : this.imageUrl.hashCode());
      result = prime * result + ((this.trailer == null) ? 0 : this.trailer.hashCode());
      result = prime * result + ((this.url == null) ? 0 : this.url.hashCode());
      result = prime * result + this.year;
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (!super.equals(obj))
         return false;
      if (!(obj instanceof Movie))
         return false;
      Movie other = (Movie) obj;
      if (this.categories == null) {
         if (other.categories != null)
            return false;
      } else if (!this.categories.equals(other.categories))
         return false;
      if (this.homepage == null) {
         if (other.homepage != null)
            return false;
      } else if (!this.homepage.equals(other.homepage))
         return false;
      if (this.name == null) {
         if (other.name != null)
            return false;
         // name check ignores case
      } else if (!this.name.equalsIgnoreCase(other.name))
         return false;
      if (this.providerId == null) {
         if (other.providerId != null)
            return false;
      } else if (!this.providerId.equals(other.providerId))
         return false;
      if (Double.doubleToLongBits(this.rating) != Double.doubleToLongBits(other.rating))
         return false;
      if (this.tagline == null) {
         if (other.tagline != null)
            return false;
      } else if (!this.tagline.equals(other.tagline))
         return false;
      if (this.thumbUrl == null) {
         if (other.thumbUrl != null)
            return false;
      } else if (!this.thumbUrl.equals(other.thumbUrl))
         return false;
      if (this.imageUrl == null) {
         if (other.imageUrl != null)
            return false;
      } else if (!this.imageUrl.equals(other.imageUrl))
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
