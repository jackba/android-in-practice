package com.manning.aip.brewmap;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import com.manning.aip.brewmap.model.Pub;

import java.util.ArrayList;
import java.util.List;

public class PubOverlay extends ItemizedOverlay {

   private List<Pub> pubs;
   private List<OverlayItem> items;

   public PubOverlay(List<Pub> pubs, Drawable marker) {
      super(boundCenterBottom(marker));
      this.pubs = pubs;
      items = new ArrayList<OverlayItem>();
      populate();
   }

   @Override
   protected OverlayItem createItem(int i) {
      Pub pub = pubs.get(i);
      // GeoPoint uses lat/long in microdegrees format (1e6)
      GeoPoint point = new GeoPoint((int) (pub.getLatitude() * 1e6), (int) (pub.getLongitude() * 1e6));
      return new OverlayItem(point, pub.getName(), "");
   }

   @Override
   public int size() {
      return pubs.size();
   }

}
