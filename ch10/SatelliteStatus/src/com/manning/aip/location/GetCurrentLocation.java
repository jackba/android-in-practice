package com.manning.aip.location;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

//TODO make this return current location easiest way?
//http://stackoverflow.com/questions/3145089/what-is-the-simplest-and-most-robust-way-to-get-the-users-current-location-in-an/3145655#3145655

// cover frequent questions -- and note relevant issues
// http://code.google.com/p/android/issues/detail?id=9433
// http://stackoverflow.com/questions/2021176/android-gps-status

public class GetCurrentLocation extends Activity {

   public static final String LOC_DATA = "LOC_DATA";

   private LocationManager locationMgr;

   private LocationListener locationListener;

   private Handler handler;
   private Runnable handlerCallback;

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

      locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

      // 1. setup Handler and callback (will be used to stop location listener at specified duration)
      handler = new Handler();
      handlerCallback = new Runnable() {
         public void run() {
            endListenForLocation("Unable to determine current location.");
         }
      };

      // 2. get provider 
      Criteria criteria = new Criteria();
      // use Criteria to get provider (and could use COARSE, but doesn't work in emulator, FINE means GPS)
      criteria.setAccuracy(Criteria.ACCURACY_FINE);
      String providerName = locationMgr.getBestProvider(criteria, true);
      providerName = LocationManager.GPS_PROVIDER;

      // 3. get handle on loc listener
      locationListener = new LocationListenerImpl();

      // 4. if provider not null, use last known location (if recent), or listen for updates
      if (providerName != null) {
         // first check last KNOWN location
         Location lastKnown = locationMgr.getLastKnownLocation(providerName);
         // if the lastKnownLocation is present (often WON'T BE) and within the last X seconds, just use it
         if (lastKnown != null) {
            System.out.println("*** lastKnown time - " + lastKnown.getTime());
            System.out.println("*** System - 20000 - " + (System.currentTimeMillis() - 20000));
         } else {
            System.out.println("*** lastKnown NULL");
         }
         if (lastKnown != null && lastKnown.getTime() >= (System.currentTimeMillis() - 20000)) {
            detail.setText("Current location (taken from last known using " + providerName + " provider): "
                     + lastKnown.getLatitude() + " / " + lastKnown.getLongitude());
         } else {
            // last known is relatively old, or doesn't exist, use a LocationListener 
            // wait for a good location update for X seconds
            listenForLocation(providerName, 20000);
         }
      } else {
         detail.setText("ACCURACY_FINE location provider not available, unable to determine current location.");
      }

      detail.setText("Checking for location using provider: " + providerName);
   }

   @Override
   protected void onPause() {
      super.onPause();
      if (progressDialog.isShowing()) {
         progressDialog.hide();
      }
   }

   private void listenForLocation(String providerName, int duration) {
      progressDialog.show();
      locationMgr.requestLocationUpdates(providerName, 0, 0, locationListener);
      handler.postDelayed(handlerCallback, duration);
   }

   private void endListenForLocation(String message) {
      if (progressDialog.isShowing()) {
         progressDialog.hide();
      }
      locationMgr.removeUpdates(locationListener);
      handler.removeCallbacks(handlerCallback);
      detail.setText(message);
   }

   // LocationListener impl
   private class LocationListenerImpl implements LocationListener {

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
               if (progressDialog.isShowing()) {
                  progressDialog.hide();
               }
               endListenForLocation("Location provider OUT OF SERVICE, unable to determine location at the current time.");
         }
         Log.d("GetCurrentLocation", "Status Change " + message);
      }

      @Override
      public void onLocationChanged(Location loc) {
         Log.d("LocationListener", "Location changed to " + loc);
         if (loc == null) {
            return;
         }
         // cancel the handler, we don't need to keep waiting, close the dialog, set the message         
         endListenForLocation("Current location (taken from on location): " + loc.getLatitude() + " / "
                  + loc.getLongitude());
      }

      @Override
      public void onProviderDisabled(String provider) {
         Toast.makeText(GetCurrentLocation.this, "provider disabled", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onProviderEnabled(String provider) {
         Toast.makeText(GetCurrentLocation.this, "provider enabled", Toast.LENGTH_SHORT).show();
      }
   }

   /*
   private void fireNotification(String title, String message) {
      Intent intent = new Intent(GetCurrentLocation.this, LocationDetail.class);
      intent.putExtra(LOC_DATA, message);
      Notification notification =
               new Notification(android.R.drawable.ic_menu_compass, "Location Listener Update",
                        System.currentTimeMillis());
      notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
      notification.setLatestEventInfo(GetCurrentLocation.this, title, message,
               PendingIntent.getActivity(GetCurrentLocation.this, 0, intent, 0));
      notificationMgr.notify(0, notification);
   }
   */
}