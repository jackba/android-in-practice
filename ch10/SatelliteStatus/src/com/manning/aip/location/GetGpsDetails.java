package com.manning.aip.location;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class GetGpsDetails extends Activity {

   public static final String LOC_DATA = "LOC_DATA";

   private LocationManager locationMgr;
   private NotificationManager notificationMgr;

   private GpsStatus.Listener gpsListener;

   private TextView title;
   private TextView detail;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.title_detail);

      locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      notificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      gpsListener = new GpsListener();

      // get provider and request location updates
      Criteria criteria = new Criteria();
      criteria.setAccuracy(Criteria.ACCURACY_FINE); // can use Criteria
      criteria.setCostAllowed(false);
      String providerName = locationMgr.getBestProvider(criteria, true);
      if (providerName != null) {
         // TODO
      } else {
         Toast.makeText(this, "ACCURACY FINE location provider not available (is GPS enabled?)", Toast.LENGTH_SHORT)
                  .show();
      }

      // there is also a listener specifically for GPS status changes
      locationMgr.addGpsStatusListener(gpsListener);

      title = (TextView) findViewById(R.id.title);
      detail = (TextView) findViewById(R.id.detail);

      title.setText("Get Location");
      detail.setText("Checking for location using provider: " + providerName);
   }

   @Override
   protected void onPause() {
      super.onPause();      
      locationMgr.removeGpsStatusListener(gpsListener);
   }

   private void fireNotification(String title, String message) {
      Intent intent = new Intent(GetGpsDetails.this, LocationDetail.class);
      intent.putExtra(LOC_DATA, message);
      Notification notification =
               new Notification(android.R.drawable.ic_menu_compass, "Location Listener Update",
                        System.currentTimeMillis());
      notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
      notification.setLatestEventInfo(GetGpsDetails.this, title, message,
               PendingIntent.getActivity(GetGpsDetails.this, 0, intent, 0));
      notificationMgr.notify(0, notification);
   }


   // combine onLocationChanged and GpsStatus to be certain you have a good current GPS fix
   // can take several minutes to get a "fix" after GPS is enabled (icon at top blinking vs solid)
   // GpsStatus.Listener impl
   private class GpsListener implements GpsStatus.Listener {
      protected GpsStatus gpsStatus;
      protected boolean gpsFix;

      public void onGpsStatusChanged(int event) {
         Log.d("GpsListener", "Status changed to " + event);

         switch (event) {
            case GpsStatus.GPS_EVENT_STARTED:
               break;
            case GpsStatus.GPS_EVENT_STOPPED:
               break;
            // GPS_EVENT_SATELLITE_STATUS will be called frequently
            // all satellites in use will invoke it, don't rely on ONLY it
            // make sure 
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
               // TODO               
               break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
               gpsFix = true;
               Toast.makeText(GetGpsDetails.this, "EVENT_FIRST_FIX: first GPS fix", Toast.LENGTH_SHORT).show();
               // TODO (use fix)
               break;
         }
      }
   }
}