package com.manning.aip.location;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

public class GetLocationWithGPS extends Activity {

   public static final String LOC_DATA = "LOC_DATA";

   private LocationManager locationMgr;
   private GpsListener gpsListener;
   private GpsStatus gpsStatus;
   private Handler handler;

   private TextView title; 
   private TextView detail;
   private TextView gpsEvents;
   private TextView satelliteStatus;
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
      gpsEvents = (TextView) findViewById(R.id.gps_events);
      satelliteStatus = (TextView) findViewById(R.id.satellite_status);

      title.setText("Get Location");
      detail.setText("getting current location...");

      locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      gpsListener = new GpsListener();   

      handler = new Handler() {
         public void handleMessage(Message m) {
            Log.d("GetLocationWithGPS", "Handler returned with message: " + m.toString());
            progressDialog.dismiss();
            if (m.what == LocationHelper.MESSAGE_CODE_LOCATION_FOUND) {
               detail.setText("Current location -- lat:" + m.arg1 + " lon:" + m.arg2);
            } else if (m.what == LocationHelper.MESSAGE_CODE_LOCATION_NULL) {
               detail.setText("HANDLER RETURNED -- unable to get location");
            } else if (m.what == LocationHelper.MESSAGE_CODE_PROVIDER_NOT_PRESENT) {
               detail.setText("HANDLER RETURNED -- provider not present");
            }
         }
      };
   }

   @Override
   protected void onResume() {
      super.onResume();

      // determine if GPS is enabled or not, if not prompt user to enable it
      if (!locationMgr.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("GPS is not enabled")
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
      } else {
         LocationHelper locationHelper = new LocationHelper(locationMgr, handler);
         progressDialog.show();
         locationHelper.getCurrentLocation();
      }
      
      locationMgr.addGpsStatusListener(gpsListener);
   }

   @Override
   protected void onPause() {
      super.onPause();
      progressDialog.dismiss();
      locationMgr.removeGpsStatusListener(gpsListener);
   }
   
   // you can also use a GpsListener to be notified when the GPS is started/stopped, and when first "fix" is obtained
   private class GpsListener implements GpsStatus.Listener {
      public void onGpsStatusChanged(int event) {
         Log.d("GpsListener", "Status changed to " + event);
         switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
               gpsEvents.setText("GPS_EVENT_STARTED");
               break;
            case GpsStatus.GPS_EVENT_STOPPED:
               gpsEvents.setText("GPS_EVENT_STOPPED");
               break;
            // GPS_EVENT_SATELLITE_STATUS will be called frequently
            // all satellites in use will invoke it, don't rely on it alone
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
               // this is *very* chatty, you probably don't want to listen for this
               gpsStatus = locationMgr.getGpsStatus(gpsStatus);
               Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
               StringBuilder sb = new StringBuilder();
               for (GpsSatellite sat : sats) {
                  sb.append("\nSatellite NUM:" + sat.getPrn() + " AZIMUTH:" + sat.getAzimuth() + " ELEVATION:" + sat.getElevation() + " SIG/NOISE:" + sat.getSnr());
               }
               satelliteStatus.setText(sb.toString());           
               break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
               gpsEvents.setText("GPS_EVENT_FIRST_FIX");
               break;
         }
      }
   }
}