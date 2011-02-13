package com.manning.aip.brewmap.model;

public class Pub {

   private int id;
   private String name;
   private String status;
   private String reviewLink;
   private String proxyLink;
   private Address address;
   private String phone;

   private double longitude;
   private double latitude;

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

}
