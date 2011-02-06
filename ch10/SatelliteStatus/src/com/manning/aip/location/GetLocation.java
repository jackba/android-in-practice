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

//TODO make this return current location easiest way?
//http://stackoverflow.com/questions/3145089/what-is-the-simplest-and-most-robust-way-to-get-the-users-current-location-in-an/3145655#3145655

// cover frequent questions -- and note relevant issues
// http://code.google.com/p/android/issues/detail?id=9433
// http://stackoverflow.com/questions/2021176/android-gps-status

public class GetLocation extends Activity {

   public static final String LOC_DATA = "LOC_DATA";

   private LocationManager locationMgr;
   private NotificationManager notificationMgr;

   private LocationListener locationListener;
   private GpsStatus.Listener gpsListener;

   private Location lastLocation;

   private TextView title;
   private TextView detail;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.title_detail);

      locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      notificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      // create listener as an instance vars so we can "removeUpdates"/"removeGpsStatusListener" in onPause
      // (it's very important to remove listeners when done, if you don't the Activity can't be cleaned up)
      locationListener = new LocListener();
      gpsListener = new GpsListener();

      // get provider and request location updates
      Criteria criteria = new Criteria();
      criteria.setAccuracy(Criteria.ACCURACY_FINE); // can use Criteria
      criteria.setCostAllowed(false);
      String providerName = locationMgr.getBestProvider(criteria, true);
      if (providerName != null) {
         locationMgr.requestLocationUpdates(providerName, 3000, 100, new LocListener());
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
      locationMgr.removeUpdates(locationListener);
      locationMgr.removeGpsStatusListener(gpsListener);
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

      // there are reports of this not working on 2.1 and 2.2? 
      // seems to work fine for me on a Nexus One with 2.2 
      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {
         Log.d("LocationListener", "Status changed to " + status);
         String message = "Status changed to: ";
         switch (status) {
            case LocationProvider.AVAILABLE:
               message += status + " " + "AVAILABLE";
               break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
               message += status + " " + "TEMPORARILY_UNAVAILABLE";
               break;
            case LocationProvider.OUT_OF_SERVICE:
               message += status + " " + "OUT_OF_SERVICE";
               break;
         }
         fireNotification("Status Change", message);
      }

      @Override
      public void onLocationChanged(Location location) {
         Log.d("LocationListener", "Location changed to " + location);
         if (location == null) {
            return;
         }
         lastLocation = location;
         fireNotification("Location Change",
                  "lat / long:\n" + lastLocation.getLatitude() + " / " + lastLocation.getLongitude());
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
               // if lastLocation (established by onLocationChanged
               if (lastLocation != null && (System.currentTimeMillis() - lastLocation.getTime()) < 3000) {
                  gpsFix = true;
               } else {
                  gpsFix = false;
               }

               if (gpsFix) {
                  Toast.makeText(GetLocation.this, "EVENT_SATELLITE_STATUS: got GPS fix", Toast.LENGTH_SHORT).show();
               }

               GetLocation.this.locationMgr.getGpsStatus(gpsStatus);

               break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
               gpsFix = true;
               Toast.makeText(GetLocation.this, "EVENT_FIRST_FIX: first GPS fix", Toast.LENGTH_SHORT).show();
               // TODO (use fix)
               break;
         }
      }
   }
}