package com.manning.aip.location;

import android.app.Activity;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Printer;
import android.util.StringBuilderPrinter;
import android.widget.TextView;

import java.util.ArrayList;

// NOTE that "network" provider will always return null for getLastKnownLocation
// if settings->location and security->Use wireless networks is NOT CHECKED (very often it's not)

public class ProviderDetail extends Activity {

   private LocationManager lMgr;

   private TextView title;
   private TextView detail;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.provider_detail);

      title = (TextView) findViewById(R.id.title);
      detail = (TextView) findViewById(R.id.detail);      

      lMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
   }
   
   @Override 
   public void onResume() {
      super.onResume();      
            
      String providerName = getIntent().getStringExtra("PROVIDER_NAME");      
      Location lastLocation = lMgr.getLastKnownLocation(providerName);      
      LocationProvider provider = lMgr.getProvider(providerName);      
      
      StringBuilder sb = new StringBuilder();

      sb.append("location manager data");
      sb.append("\n--------------------------");
      if (lastLocation != null) {
         sb.append("\n");
         Printer printer = new StringBuilderPrinter(sb);
         lastLocation.dump(printer, "last location: ");
      } else {
         sb.append("\nlast location: null\n");
      }
      
      if (providerName.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
         GpsStatus gpsStatus = lMgr.getGpsStatus(null);
         sb.append("\ngps status");
         sb.append("\n--------------");
         sb.append("\ntime to first fix: " + gpsStatus.getTimeToFirstFix());
         sb.append("\nmax satellites: " + gpsStatus.getMaxSatellites());
         ArrayList<GpsSatellite> satellites = new ArrayList<GpsSatellite>();         
         for (GpsSatellite satellite : gpsStatus.getSatellites()) {
            satellites.add(satellite);           
         }  
         sb.append("\ncurrent satellites: " + satellites.size());
         if (satellites.size() > 0) {
            for (GpsSatellite satellite : satellites) {
               sb.append("\nsatellite: " + satellite.getPrn());
               sb.append("\n   azimuth " + satellite.getAzimuth());
               sb.append("\n   elevation " + satellite.getElevation());
               sb.append("\n   signal to noise ratio " + satellite.getSnr());
            }
         }
      }
      
      sb.append("\n");
      sb.append("\nprovider properties");
      sb.append("\n--------------------");
      sb.append("\naccuracy: " + provider.getAccuracy());
      sb.append("\npower requirement: " + provider.getPowerRequirement());
      sb.append("\nhas monetary cost: " + provider.hasMonetaryCost());
      sb.append("\nsupports altitude: " + provider.supportsAltitude());
      sb.append("\nsupports bearing: " + provider.supportsBearing());
      sb.append("\nsupports speed: " + provider.supportsSpeed());
      sb.append("\nrequires cell: " + provider.requiresCell());
      sb.append("\nrequires network: " + provider.requiresNetwork());    

      title.setText("Provider: " + providerName);
      detail.setText(sb.toString());
   }
}