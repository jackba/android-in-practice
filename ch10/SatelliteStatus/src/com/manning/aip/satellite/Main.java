package com.manning.aip.satellite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

// NOTE this is not anywhere near done, just committing for safe keeping

public class Main extends Activity implements OnItemClickListener {

   public static final String PROVIDER_NAME = "PROVIDER_NAME";
   
   private LocationManager lMgr;
   private ListView providersList;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      lMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

      ArrayAdapter<String> adapter =
               new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lMgr.getAllProviders());
      providersList = (ListView) findViewById(R.id.location_providers);
      providersList.setAdapter(adapter);

      providersList.setOnItemClickListener(this);
   }

   @Override
   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      TextView textView = (TextView) view;
      String providerName = textView.getText().toString();     
      Intent intent = new Intent(Main.this, ProviderDetail.class);
      intent.putExtra(PROVIDER_NAME, providerName);
      startActivity(intent);
   }   
}