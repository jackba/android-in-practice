package com.manning.aip.satellite;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ProviderDetail extends Activity {

   private LocationManager lMgr;
   private LocationProvider provider;
   
   private TextView title;
   private TextView detail;
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.provider_detail);

      String providerName = getIntent().getStringExtra("PROVIDER_NAME");
      lMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      provider = lMgr.getProvider(providerName);      
      
      title = (TextView) findViewById(R.id.title);
      detail = (TextView) findViewById(R.id.detail);
      
      StringBuilder sb = new StringBuilder();
      sb.append("provider class: " + provider.getClass());
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
      sb.append("\nrequires satellite: " + provider.requiresSatellite());
      sb.append("\nprovider data");
      sb.append("\n--------------");    
      
      title.setText("Provider: " + providerName);      
      detail.setText(sb.toString());

      Toast.makeText(this, "Clicked item: " + providerName, Toast.LENGTH_SHORT).show();
   }

}