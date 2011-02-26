package com.manning.aip.brewmap.model;

public class Address {

   private String street;
   private String city;
   private String state;
   private String postalCode;
   private String country;

   public String getStreet() {
      return this.street;
   }

   public void setStreet(String street) {
      this.street = street;
   }

   public String getCity() {
      return this.city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getState() {
      return this.state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getPostalCode() {
      return this.postalCode;
   }

   public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
   }

   public String getCountry() {
      return this.country;
   }

   public void setCountry(String country) {
      this.country = country;
   }   
   
   public String getLocationName() {
      return this.street + ", " + this.city + ", " + this.state + " " + this.postalCode;
   }

   @Override
   public String toString() {
      return this.street + "\n" + this.city + ", " + this.state + " "
               + this.postalCode + "\n" + this.country;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.city == null) ? 0 : this.city.hashCode());
      result = prime * result + ((this.country == null) ? 0 : this.country.hashCode());
      result = prime * result + ((this.postalCode == null) ? 0 : this.postalCode.hashCode());
      result = prime * result + ((this.state == null) ? 0 : this.state.hashCode());
      result = prime * result + ((this.street == null) ? 0 : this.street.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof Address))
         return false;
      Address other = (Address) obj;
      if (this.city == null) {
         if (other.city != null)
            return false;
      } else if (!this.city.equals(other.city))
         return false;
      if (this.country == null) {
         if (other.country != null)
            return false;
      } else if (!this.country.equals(other.country))
         return false;
      if (this.postalCode == null) {
         if (other.postalCode != null)
            return false;
      } else if (!this.postalCode.equals(other.postalCode))
         return false;
      if (this.state == null) {
         if (other.state != null)
            return false;
      } else if (!this.state.equals(other.state))
         return false;
      if (this.street == null) {
         if (other.street != null)
            return false;
      } else if (!this.street.equals(other.street))
         return false;
      return true;
   }
}
