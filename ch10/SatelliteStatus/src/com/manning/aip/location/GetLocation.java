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
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

//TODO make this return current location easiest way?
//http://stackoverflow.com/questions/3145089/what-is-the-simplest-and-most-robust-way-to-get-the-users-current-location-in-an/3145655#3145655

public class GetLocation extends Activity implements OnItemClickListener {

   public static final String LOC_DATA = "LOC_DATA";

   private LocationManager locationMgr;
   private NotificationManager notificationMgr;

   private LocationListener locationListener;

   private long lastLocationMillis;
   private Location lastLocation;

   private TextView title;
   private TextView detail;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.title_detail);

      locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      notificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      locationListener = new LocListener();

      //lMgr.addGpsStatusListener(listener);

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

   // http://code.google.com/p/android/issues/detail?id=9433
   // http://stackoverflow.com/questions/2021176/android-gps-status

   //isGPSFix = (SystemClock.elapsedRealtime() - mLastLocationMillis) < 3000;

   @Override
   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      Toast.makeText(this, "Clicked item: " + ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

   }

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
         lastLocationMillis = SystemClock.elapsedRealtime();
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

   private class GpsListener implements GpsStatus.Listener {
      boolean isGpsFix;

      public void onGpsStatusChanged(int event) {
         switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
               if (lastLocation != null)
                  isGpsFix = (SystemClock.elapsedRealtime() - lastLocationMillis) < 3000;

               if (isGpsFix) {
                  // fix is acquired, can use it here
               } else {
                  // fix lost
               }

               break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
               // fix acquired, use it
               isGpsFix = true;

               break;
         }
      }
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
}