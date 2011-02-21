package com.manning.aip.brewmap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.manning.aip.brewmap.model.Pub;

public class PubDetails extends Activity {

   private BrewMapApp app;

   private TextView text;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.pub_details);

      app = (BrewMapApp) getApplication();

      text = (TextView) findViewById(R.id.details_text);

      int pubIndex = getIntent().getIntExtra("PUB_INDEX", -1);

      if (pubIndex != -1 && pubIndex <= app.getPubs().size()) {
         Pub pub = app.getPubs().get(pubIndex);
         text.setText("PUB DETAILS - " + pub.getName());
      } else {
         Toast.makeText(this, "Invalid pub index, cannot display detail info.", Toast.LENGTH_SHORT).show();
        
      }
      

   }

}