package com.manning.aip.brewmap.model;

public class BrewLocation {

   private int id;
   private String name;
   private String status;
   private String reviewLink;
   private String proxyLink;
   private Address address;
   private String phone;

   private double longitude;
   private double latitude;
   
   public BrewLocation() {
      this.address = new Address();
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getStatus() {
      return this.status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getReviewLink() {
      return this.reviewLink;
   }

   public void setReviewLink(String reviewLink) {
      this.reviewLink = reviewLink;
   }

   public String getProxyLink() {
      return this.proxyLink;
   }

   public void setProxyLink(String proxyLink) {
      this.proxyLink = proxyLink;
   }

   public Address getAddress() {
      return this.address;
   }

   public void setAddress(Address address) {
      this.address = address;
   }

   public String getPhone() {
      return this.phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public double getLongitude() {
      return this.longitude;
   }

   public void setLongitude(double longitude) {
      this.longitude = longitude;
   }

   public double getLatitude() {
      return this.latitude;
   }

   public void setLatitude(double latitude) {
      this.latitude = latitude;
   }   
   
   @Override
   public String toString() {
      return "BrewLocation [id=" + this.id + ", name=" + this.name + ", status=" + this.status + ", reviewLink="
               + this.reviewLink + ", proxyLink=" + this.proxyLink + ", address=" + this.address + ", phone="
               + this.phone + ", longitude=" + this.longitude + ", latitude=" + this.latitude + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.address == null) ? 0 : this.address.hashCode());
      result = prime * result + this.id;
      long temp;
      temp = Double.doubleToLongBits(this.latitude);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      temp = Double.doubleToLongBits(this.longitude);
      result = prime * result + (int) (temp ^ (temp >>> 32));
      result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
      result = prime * result + ((this.phone == null) ? 0 : this.phone.hashCode());
      result = prime * result + ((this.proxyLink == null) ? 0 : this.proxyLink.hashCode());
      result = prime * result + ((this.reviewLink == null) ? 0 : this.reviewLink.hashCode());
      result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof BrewLocation))
         return false;
      BrewLocation other = (BrewLocation) obj;
      if (this.address == null) {
         if (other.address != null)
            return false;
      } else if (!this.address.equals(other.address))
         return false;
      if (this.id != other.id)
         return false;
      if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude))
         return false;
      if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude))
         return false;
      if (this.name == null) {
         if (other.name != null)
            return false;
      } else if (!this.name.equals(other.name))
         return false;
      if (this.phone == null) {
         if (other.phone != null)
            return false;
      } else if (!this.phone.equals(other.phone))
         return false;
      if (this.proxyLink == null) {
         if (other.proxyLink != null)
            return false;
      } else if (!this.proxyLink.equals(other.proxyLink))
         return false;
      if (this.reviewLink == null) {
         if (other.reviewLink != null)
            return false;
      } else if (!this.reviewLink.equals(other.reviewLink))
         return false;
      if (this.status == null) {
         if (other.status != null)
            return false;
      } else if (!this.status.equals(other.status))
         return false;
      return true;
   }   
}
