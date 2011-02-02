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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity implements OnItemClickListener {

   private NotificationManager nMgr;
   private LocationManager lMgr;
   private ListView providersList;

   private long lastLocationMillis;
   private Location lastLocation;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      lMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

      ArrayAdapter<String> adapter =
               new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lMgr.getAllProviders());
      providersList = (ListView) findViewById(R.id.location_providers);
      providersList.setAdapter(adapter);

      providersList.setOnItemClickListener(this);

      //lMgr.getLastKnownLocation(provider);
      //lMgr.getGpsStatus(status);
      //lMgr.getBestProvider(criteria, enabledOnly)

      lMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocListener());
      lMgr.addGpsStatusListener(new GpsListener());

      //LocationProvider gpsProvider = lMgr.getProvider(LocationManager.GPS_PROVIDER);

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
         notification.setLatestEventInfo(Main.this, "Location Status Change", "Status changed to " + status,
                  PendingIntent.getActivity(Main.this, 0, new Intent(Main.this, Main.class), 0));
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
         notification.setLatestEventInfo(Main.this, "Location Status Change",
                  "Location changed to " + location.toString(),
                  PendingIntent.getActivity(Main.this, 0, new Intent(Main.this, Main.class), 0));
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