package com.manning.aip.mymoviesdatabase.model;

public class MovieSearchResult {

   private String name;
   private String providerId;

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getProviderId() {
      return this.providerId;
   }

   public void setProviderId(String providerId) {
      this.providerId = providerId;
   }

   @Override
   public String toString() {
      return name;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
      result = prime * result + ((this.providerId == null) ? 0 : this.providerId.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof MovieSearchResult))
         return false;
      MovieSearchResult other = (MovieSearchResult) obj;
      if (this.name == null) {
         if (other.name != null)
            return false;
      } else if (!this.name.equals(other.name))
         return false;
      if (this.providerId == null) {
         if (other.providerId != null)
            return false;
      } else if (!this.providerId.equals(other.providerId))
         return false;
      return true;
   }   
}
