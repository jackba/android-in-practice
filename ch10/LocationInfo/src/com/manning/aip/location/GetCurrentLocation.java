package com.manning.aip.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.widget.TextView;

public class GetCurrentLocation extends Activity {

   public static final String LOC_DATA = "LOC_DATA";

   private LocationManager locationMgr;

   private Handler handler;

   private TextView title;
   private TextView detail;
   private ProgressDialog progressDialog;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.title_detail);

      progressDialog = new ProgressDialog(this);
      progressDialog.setCancelable(false);
      progressDialog.setMessage("Checking for current location...");

      title = (TextView) findViewById(R.id.title);
      detail = (TextView) findViewById(R.id.detail);

      title.setText("Get Location");
      detail.setText("getting current location...");      

      locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

      handler = new Handler() {
         public void handleMessage(Message m) {
            if (progressDialog.isShowing()) {
               progressDialog.hide();
               if (m.what == LocationHelper.MESSAGE_CODE_LOCATION_FOUND) {                  
                  detail.setText("Current location -- lat:" + m.arg1 + " lon:" + m.arg2);
               } else if (m.what == LocationHelper.MESSAGE_CODE_LOCATION_NULL) {
                  detail.setText("HANDLER RETURNED -- unable to get location");
               } else if (m.what == LocationHelper.MESSAGE_CODE_PROVIDER_NOT_PRESENT) {
                  detail.setText("HANDLER RETURNED -- provider not present");
               }
            }
         }
      };      
      
      // determine if GPS is enabled or not, if not prompt user to enable it
      if (!locationMgr.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("GPS Settings are not enabled")
                  .setMessage("Would you like to go the location settings and enable GPS?").setCancelable(true)
                  .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
                     }
                  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                     }
                  });
         AlertDialog alert = builder.create();
         alert.show();
      }
   }
   
   @Override
   protected void onResume() {
      super.onResume();
      LocationHelper locationHelper = new LocationHelper(locationMgr, handler);
      progressDialog.show();
      locationHelper.getCurrentLocation();
   }

   @Override
   protected void onPause() {
      super.onPause();
      if (progressDialog.isShowing()) {
         progressDialog.hide();
      }
   }   
}