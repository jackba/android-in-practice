package com.manning.aip.brewmap;

import java.util.List;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.manning.aip.brewmap.model.BrewLocation;

public class MapResults extends MapActivity {

   private MapView map;
   private List<Overlay> overlays;

   private BrewMapApp app;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.map_results);

      app = (BrewMapApp) getApplication();

      map = (MapView) findViewById(R.id.map);
      map.setBuiltInZoomControls(true);

      List<BrewLocation> brewLocations = app.getBrewLocations();
      BrewLocationOverlay brewLocationOverlay = new BrewLocationOverlay(this, brewLocations, this.getResources().getDrawable(R.drawable.beer_icon_small));
      overlays = map.getOverlays();
      overlays.add(brewLocationOverlay);

      // TODO determine the exact center of the set of coords (now being lazy, just using first point in set)
      map.getController().setCenter(
               new GeoPoint((int) (brewLocations.get(0).getLatitude() * 1e6), (int) (brewLocations.get(0).getLongitude() * 1e6)));

      // zoom to the span (without having to calculate bounding box rectangle ourselves, nice for the people Android)      
      map.getController().zoomToSpan(brewLocationOverlay.getLatSpanE6(), brewLocationOverlay.getLonSpanE6());
   }

   @Override
   protected boolean isRouteDisplayed() {
      // TODO Auto-generated method stub
      return false;
   }
}