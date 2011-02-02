package com.manning.aip.satellite;

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

public class ProviderDetail extends Activity implements OnItemClickListener {

   private LocationManager lMgr;
   private NotificationManager nMgr;

   private long lastLocationMillis;
   private Location lastLocation;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      lMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
      nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

      String providerName = getIntent().getStringExtra("PROVIDER_NAME");
      lMgr.requestLocationUpdates(providerName, 1000, 1, new LocListener());

      if (providerName.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
         lMgr.addGpsStatusListener(new GpsListener());
      }
      
      Toast.makeText(this, "Clicked item: " + providerName, Toast.LENGTH_SHORT).show();
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
         notification.setLatestEventInfo(ProviderDetail.this, "Location Status Change", "Status changed to " + status,
                  PendingIntent.getActivity(ProviderDetail.this, 0, new Intent(ProviderDetail.this,
                           ProviderDetail.class), 0));
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
         notification.setLatestEventInfo(ProviderDetail.this, "Location Status Change", "Location changed to "
                  + location.toString(), PendingIntent.getActivity(ProviderDetail.this, 0, new Intent(
                  ProviderDetail.this, ProviderDetail.class), 0));
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