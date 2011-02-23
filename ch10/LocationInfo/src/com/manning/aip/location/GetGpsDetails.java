package com.manning.aip.location;

import android.app.Activity;
import android.content.Context;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GetGpsDetails extends Activity {

   private LocationManager locationMgr;

   private GpsStatus.Listener gpsListener;

   private TextView title;
   private TextView detail;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.title_detail);

      locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

      gpsListener = new GpsListener();      

      title = (TextView) findViewById(R.id.title);
      detail = (TextView) findViewById(R.id.detail);

      title.setText("Get GPS Status");
      detail.setText("Listening for GPS events . . .");
   }

   @Override
   protected void onResume() {
      super.onResume();
      // listener specifically for GPS status changes
      locationMgr.addGpsStatusListener(gpsListener);
   }
   
   @Override
   protected void onPause() {
      super.onPause();
      locationMgr.removeGpsStatusListener(gpsListener);
   }

   // use a GpsListener to be notified when the GPS is started/stopped, and when first "fix" is obtained
   private class GpsListener implements GpsStatus.Listener {
      public void onGpsStatusChanged(int event) {
         Log.d("GpsListener", "Status changed to " + event);
         switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
               detail.setText("GPS_EVENT_STARTED: GPS started");
               break;
            case GpsStatus.GPS_EVENT_STOPPED:
               detail.setText("GPS_EVENT_STOPPED: GPS stopped");
               break;
            // GPS_EVENT_SATELLITE_STATUS will be called frequently
            // all satellites in use will invoke it, don't rely on it alone
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
               // ignore              
               break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
               detail.setText("GPS_EVENT_FIRST_FIX: first GPS fix obtained");
               break;
         }
      }
   }
}