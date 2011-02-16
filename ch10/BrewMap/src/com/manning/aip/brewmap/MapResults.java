package com.manning.aip.brewmap;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import java.util.List;

public class MapResults extends MapActivity {

   private MapView map;
   private MapController controller;
   private List<Overlay> overlays;
   private Drawable drawable;
   
   private BrewMapApp app;   
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_results);
        
        app = (BrewMapApp) getApplication();
        
        map = (MapView) findViewById(R.id.map);
        map.setBuiltInZoomControls(true);
        
        overlays = map.getOverlays();
        overlays.add(new PubOverlay(app.getPubs(), this.getResources().getDrawable(R.drawable.beer_icon)));        
    }

   @Override
   protected boolean isRouteDisplayed() {
      // TODO Auto-generated method stub
      return false;
   }    
}