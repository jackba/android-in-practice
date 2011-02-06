package com.manning.aip.location;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

// NOTE that "network" provider will always return null for getLastKnownLocation
// if settings->location and security->Use wireless networks is NOT CHECKED (very often it's not)

public class LocationDetail extends Activity {

   private TextView title;
   private TextView detail;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.title_detail);

      title = (TextView) findViewById(R.id.title);
      detail = (TextView) findViewById(R.id.detail);
      
      title.setText("Location Listener Change");
      String message = getIntent().getStringExtra(GetLocation.LOC_DATA);
      detail.setText(message);
   }
}