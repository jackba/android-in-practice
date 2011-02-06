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
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

//TODO make this return current location easiest way?
//http://stackoverflow.com/questions/3145089/what-is-the-simplest-and-most-robust-way-to-get-the-users-current-location-in-an/3145655#3145655

public class GetLocation extends Activity {

   public static final String LOC_DATA = "LOC_DATA";

   private LocationManager locationMgr;
   private NotificationManager notificationMgr;

   private LocationListener locationListener;

   private Location lastLocation;

   private TextView title;
   private TextView detail;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.title_detail);

      locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      notificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      // create LocationListener as an instance var so we can "removeUpdates" in onPause
      // (it's very important to removeUpdates when done, if you don't the Activity can't be cleaned up)
      locationListener = new LocListener();

      // there is also a listener specifically for GPS
      locationMgr.addGpsStatusListener(new GpsListener());

      title = (TextView) findViewById(R.id.title);
      detail = (TextView) findViewById(R.id.detail);
   }

   @Override
   protected void onResume() {
      super.onResume();

      Criteria criteria = new Criteria();
      criteria.setAccuracy(Criteria.ACCURACY_COARSE);
      criteria.setCostAllowed(false);
      String providerName = locationMgr.getBestProvider(criteria, true);
      if (providerName != null) {
         locationMgr.requestLocationUpdates(providerName, 3000, 100, new LocListener());
      } else {
         Toast.makeText(this, "Viable location provider not available, cannot establish current location",
                  Toast.LENGTH_SHORT).show();
      }

      title.setText("Get Location");
      detail.setText("Checking for location using provider: " + providerName);
   }

   @Override
   protected void onPause() {
      super.onPause();
      locationMgr.removeUpdates(locationListener);
   }

   private void fireNotification(String title, String message) {
      Intent intent = new Intent(GetLocation.this, LocationDetail.class);
      intent.putExtra(LOC_DATA, message);
      Notification notification =
               new Notification(android.R.drawable.ic_menu_compass, "Location Listener Update",
                        System.currentTimeMillis());
      notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
      notification.setLatestEventInfo(GetLocation.this, title, message,
               PendingIntent.getActivity(GetLocation.this, 0, intent, 0));
      notificationMgr.notify(0, notification);
   }

   // LocationListener impl
   public class LocListener implements LocationListener {

      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {
         Log.d("LocationListener", "Status changed to " + status);
         fireNotification("Status Change", "Status changed to: " + status);
      }

      @Override
      public void onLocationChanged(Location location) {
         Log.d("LocationListener", "Location changed to " + location);
         if (location == null) {
            return;
         }
         lastLocation = location;
         fireNotification("Location Change",
                  "lat/long: " + lastLocation.getLatitude() + " / " + lastLocation.getLongitude());
      }

      @Override
      public void onProviderDisabled(String provider) {
         Toast.makeText(GetLocation.this, "provider disabled", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onProviderEnabled(String provider) {
         Toast.makeText(GetLocation.this, "provider enabled", Toast.LENGTH_SHORT).show();
      }
   }

   //GpsStatus.Listener impl
   private class GpsListener implements GpsStatus.Listener {
      private boolean gpsFix;

      public void onGpsStatusChanged(int event) {
         switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
               if (lastLocation != null && (System.currentTimeMillis() - lastLocation.getTime()) < 3000) {
                  gpsFix = true;
               } else {
                  gpsFix = false;
               }

               Toast.makeText(GetLocation.this, "EVENT_SATELLITE_STATUS: " + gpsFix, Toast.LENGTH_SHORT).show();
               if (gpsFix) {
                  // TODO                  
               } else {
                  // TODO
               }

               break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
               gpsFix = true;
               Toast.makeText(GetLocation.this, "EVENT_FIRST_FIX: " + gpsFix, Toast.LENGTH_SHORT).show();
               // TODO (use fix)
               break;
         }
      }
   }
}