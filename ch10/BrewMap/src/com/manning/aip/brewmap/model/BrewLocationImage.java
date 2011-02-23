package com.manning.aip.brewmap.model;

public class BrewLocationImage {

   private int id;
   private String url;
   private String thumbUrl;
   private String caption;
   private String credit;
   private int width;
   private int height;

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getUrl() {
      return this.url;
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public String getThumbUrl() {
      return this.thumbUrl;
   }

   public void setThumbUrl(String thumbUrl) {
      this.thumbUrl = thumbUrl;
   }

   public String getCaption() {
      return this.caption;
   }

   public void setCaption(String caption) {
      this.caption = caption;
   }

   public String getCredit() {
      return this.credit;
   }

   public void setCredit(String credit) {
      this.credit = credit;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }
}
