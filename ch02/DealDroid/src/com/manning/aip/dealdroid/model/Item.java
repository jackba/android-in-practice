package com.manning.aip.dealdroid.model;

public final class Item {

   public long itemId;
   public long endTime;
   public String picUrl;
   public String smallPicUrl;
   public String pic175Url;
   public String title;
   public String desc;
   public String dealUrl;
   public String convertedCurrentPrice;
   public String primaryCategoryName;
   public String location;
   public int quantity;
   public int quantitySold;
   public String msrp;
   public String savingsRate;
   public boolean hot;

   // favor "copy constructor/getInstance" over clone, clone is tricky and error prone
   // (better yet use immutable objects, but sort of overkill for this example)
   public static Item getInstance(final Item item) {
      Item copy = new Item();
      copy.convertedCurrentPrice = item.convertedCurrentPrice;
      copy.dealUrl = item.dealUrl;
      copy.desc = item.desc;
      copy.endTime = item.endTime;
      copy.hot = item.hot;
      copy.itemId = item.itemId;
      copy.location = item.location;
      copy.msrp = item.msrp;
      copy.picUrl = item.picUrl;
      copy.primaryCategoryName = item.primaryCategoryName;
      copy.quantity = item.quantity;
      copy.quantitySold = item.quantitySold;
      copy.savingsRate = item.savingsRate;
      copy.smallPicUrl = item.smallPicUrl;
      copy.title = item.title;
      copy.pic175Url = item.pic175Url;
      return copy;
   }

   @Override
   public String toString() {
      return "Item [convertedCurrentPrice=" + this.convertedCurrentPrice + ", dealUrl=" + this.dealUrl + ", desc="
               + this.desc + ", endTime=" + this.endTime + ", hot=" + this.hot + ", itemId=" + this.itemId
               + ", location=" + this.location + ", msrp=" + this.msrp + ", pic175Url=" + this.pic175Url + ", picUrl="
               + this.picUrl + ", primaryCategoryName=" + this.primaryCategoryName + ", quantity=" + this.quantity
               + ", quantitySold=" + this.quantitySold + ", savingsRate=" + this.savingsRate + ", smallPicUrl="
               + this.smallPicUrl + ", title=" + this.title + "]";
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.convertedCurrentPrice == null) ? 0 : this.convertedCurrentPrice.hashCode());
      result = prime * result + ((this.dealUrl == null) ? 0 : this.dealUrl.hashCode());
      result = prime * result + ((this.desc == null) ? 0 : this.desc.hashCode());
      ///result = prime * result + (int) (this.endTime ^ (this.endTime >>> 32));
      ///result = prime * result + (this.hot ? 1231 : 1237);
      result = prime * result + (int) (this.itemId ^ (this.itemId >>> 32));
      result = prime * result + ((this.location == null) ? 0 : this.location.hashCode());
      result = prime * result + ((this.msrp == null) ? 0 : this.msrp.hashCode());
      result = prime * result + ((this.pic175Url == null) ? 0 : this.pic175Url.hashCode());
      result = prime * result + ((this.picUrl == null) ? 0 : this.picUrl.hashCode());
      result = prime * result + ((this.primaryCategoryName == null) ? 0 : this.primaryCategoryName.hashCode());
      ///result = prime * result + this.quantity;
      ///result = prime * result + this.quantitySold;
      result = prime * result + ((this.savingsRate == null) ? 0 : this.savingsRate.hashCode());
      result = prime * result + ((this.smallPicUrl == null) ? 0 : this.smallPicUrl.hashCode());
      result = prime * result + ((this.title == null) ? 0 : this.title.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Item other = (Item) obj;
      if (this.convertedCurrentPrice == null) {
         if (other.convertedCurrentPrice != null)
            return false;
      } else if (!this.convertedCurrentPrice.equals(other.convertedCurrentPrice))
         return false;
      if (this.dealUrl == null) {
         if (other.dealUrl != null)
            return false;
      } else if (!this.dealUrl.equals(other.dealUrl))
         return false;
      if (this.desc == null) {
         if (other.desc != null)
            return false;
      } else if (!this.desc.equals(other.desc))
         return false;
      //if (this.endTime != other.endTime)
      //   return false;
      //if (this.hot != other.hot)
      //   return false;
      if (this.itemId != other.itemId)
         return false;
      if (this.location == null) {
         if (other.location != null)
            return false;
      } else if (!this.location.equals(other.location))
         return false;
      if (this.msrp == null) {
         if (other.msrp != null)
            return false;
      } else if (!this.msrp.equals(other.msrp))
         return false;
      if (this.pic175Url == null) {
         if (other.pic175Url != null)
            return false;
      } else if (!this.pic175Url.equals(other.pic175Url))
         return false;
      if (this.picUrl == null) {
         if (other.picUrl != null)
            return false;
      } else if (!this.picUrl.equals(other.picUrl))
         return false;
      if (this.primaryCategoryName == null) {
         if (other.primaryCategoryName != null)
            return false;
      } else if (!this.primaryCategoryName.equals(other.primaryCategoryName))
         return false;
      //if (this.quantity != other.quantity)
      //   return false;
      //if (this.quantitySold != other.quantitySold)
      //   return false;
      if (this.savingsRate == null) {
         if (other.savingsRate != null)
            return false;
      } else if (!this.savingsRate.equals(other.savingsRate))
         return false;
      if (this.smallPicUrl == null) {
         if (other.smallPicUrl != null)
            return false;
      } else if (!this.smallPicUrl.equals(other.smallPicUrl))
         return false;
      if (this.title == null) {
         if (other.title != null)
            return false;
      } else if (!this.title.equals(other.title))
         return false;
      return true;
   }
}
