package com.manning.aip.location;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

   private LocationManager lMgr;
   private NotificationManager nMgr;

   private long lastLocationMillis;
   private Location lastLocation;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.get_location);

      lMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      
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
         Log.d("test", "Status changed to " + status);

         Notification notification =
                  new Notification(android.R.drawable.ic_menu_compass, "Status Change", System.currentTimeMillis());
         notification.setLatestEventInfo(GetLocation.this, "Location Status Change", "Status changed to " + status,
                  PendingIntent.getActivity(GetLocation.this, 0, new Intent(GetLocation.this,
                           GetLocation.class), 0));
         nMgr.notify(0, notification);
      }

      @Override
      public void onLocationChanged(Location location) {
         if (location == null)
            return;

         lastLocationMillis = SystemClock.elapsedRealtime();

         // Do something.

         lastLocation = location;

         Notification notification =
                  new Notification(android.R.drawable.ic_menu_compass, "Location Change", System.currentTimeMillis());
         notification.setLatestEventInfo(GetLocation.this, "Location Status Change", "Location changed to "
                  + location.toString(), PendingIntent.getActivity(GetLocation.this, 0, new Intent(
                  GetLocation.this, GetLocation.class), 0));
         nMgr.notify(0, notification);
      }

      @Override
      public void onProviderDisabled(String provider) {
         // TODO Auto-generated method stub

      }

      @Override
      public void onProviderEnabled(String provider) {
         // TODO Auto-generated method stub

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
}