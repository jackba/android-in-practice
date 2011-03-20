package com.manning.aip.maven;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class HelloMaven extends MapActivity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      MapView map = (MapView) findViewById(R.id.map);
      GeoPoint center = map.getMapCenter();

      Toast.makeText(
               this,
               "Position: " + center.getLatitudeE6() + "/"
                        + center.getLongitudeE6(), Toast.LENGTH_LONG).show();
   }

   @Override
   protected boolean isRouteDisplayed() {
      return false;
   }
}