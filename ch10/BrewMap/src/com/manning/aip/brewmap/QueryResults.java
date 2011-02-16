package com.manning.aip.brewmap;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.maps.MapActivity;

public class QueryResults extends MapActivity {
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_results);
        
        String resultString = getIntent().getStringExtra("RESULTS");
    }

   @Override
   protected boolean isRouteDisplayed() {
      // TODO Auto-generated method stub
      return false;
   }    
}