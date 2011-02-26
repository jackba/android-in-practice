package com.manning.aip.brewmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.manning.aip.brewmap.model.BrewLocation;

public class BrewLocationDetails extends BrewMapActivity implements OnClickListener {

   private TextView name;
   private TextView status;
   private TextView phone;
   private TextView address;

   private Button map;
   private Button call;
   private Button web;

   private BrewLocation brewLocation;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.location_details);

      name = (TextView) findViewById(R.id.location_name);
      name.setSelected(true); // so the marquee will work, if name is long
      status = (TextView) findViewById(R.id.location_status);
      phone = (TextView) findViewById(R.id.location_phone);
      address = (TextView) findViewById(R.id.location_address);

      map = (Button) findViewById(R.id.location_map_button);
      call = (Button) findViewById(R.id.location_call_button);
      web = (Button) findViewById(R.id.location_web_button);

      // can also set the click listener in XML (but that can be confusing for those looking at code)
      map.setOnClickListener(this);
      call.setOnClickListener(this);
      web.setOnClickListener(this);      
   }
   
   @Override
   protected void onResume() {
      super.onResume();
      int pubIndex = getIntent().getIntExtra(BrewMapApp.PUB_INDEX, -1);
      if (pubIndex != -1 && pubIndex <= app.getBrewLocations().size()) {
         brewLocation = app.getBrewLocations().get(pubIndex);
         name.setText(brewLocation.getName());
         status.setText(brewLocation.getStatus());
         phone.setText(brewLocation.getPhone());
         if (address != null && brewLocation.getAddress() != null) { // left address out of landscape layout
            address.setText(brewLocation.getAddress().toString());
         }
      } else {
         Toast.makeText(this, "Invalid pub data, cannot display detail info.", Toast.LENGTH_SHORT).show();
      }
   }

   public void onClick(View v) {
      if (v != null) {
         Intent i = new Intent();
         if (v.equals(map)) {
            i.setAction(Intent.ACTION_VIEW);
            if (brewLocation.getAddress() != null) {
               i.setData(Uri.parse("geo:0,0?q=" + brewLocation.getAddress().toString()));
            } else {
               Toast.makeText(BrewLocationDetails.this, "Address not available", Toast.LENGTH_SHORT).show();
            }
         } else if (v.equals(call)) {
            if (brewLocation.getPhone() != null) {
               i.setAction(Intent.ACTION_DIAL);
               i.setData(Uri.parse("tel:" + brewLocation.getPhone()));
            } else {
               Toast.makeText(BrewLocationDetails.this, "Phone not available", Toast.LENGTH_SHORT).show();
            }
         } else if (v.equals(web)) {
            if (brewLocation.getReviewLink() != null) {
               i.setAction(Intent.ACTION_VIEW);
               i.setData(Uri.parse(brewLocation.getReviewLink()));
            } else {
               Toast.makeText(BrewLocationDetails.this, "Link not available", Toast.LENGTH_SHORT).show();
            }
         }
         startActivity(i);
      }
   }
}