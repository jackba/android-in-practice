package com.manning.aip.satellite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ProviderDetail extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.provider_detail);

      String providerName = getIntent().getStringExtra("PROVIDER_NAME");


      Toast.makeText(this, "Clicked item: " + providerName, Toast.LENGTH_SHORT).show();
   }

}