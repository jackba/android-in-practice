package com.manning.aip.brewmap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PubDetails extends Activity {

   private BrewMapApp app;
   
   private TextView text;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.pub_details);

      app = (BrewMapApp) getApplication();
      
      text = (TextView) findViewById(R.id.details_text);
      
   }

  
}