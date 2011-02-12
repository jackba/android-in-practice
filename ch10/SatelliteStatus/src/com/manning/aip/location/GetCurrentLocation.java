package com.manning.aip.location;

import android.app.Activity;
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

/**
 * Sample of getting the current Location with Android.
 * Tries to get "last known" location from a best match provider first. 
 * If this is present, and recent enough (20 seconds in this example), it is used. 
 * If last known location is not available, sets up a LocationListener and 
 * listens for updates for 20 seconds. If an update is obtained, uses that as location
 * and removes update listener. 
 * <p/>
 * NOTE: The emulator has several location related oddities, it doesn't support the network
 * provider, and it doesn't provide the correct time on mock GPS fixes. For a more 
 * accurate view of how this works use it on a real device. 
 * 
 * 
 * @author ccollins
 *
 */
public class GetCurrentLocation extends Activity {

   public static final String LOC_DATA = "LOC_DATA";
   
   private static final int LOCATION_LISTEN_WAIT_TIME = 20000; 
   private static final int FIX_RECENT_BUFFER_TIME = 30000; 

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
      // use Criteria to get provider (and could use COARSE, but doesn't work in emulator)
      // (FINE will use EITHER network/gps, whichever is the best enabled match, except in emulator must be gps)
      criteria.setAccuracy(Criteria.ACCURACY_COARSE);
      String providerName = locationMgr.getBestProvider(criteria, true);
      
      detail.setText("Checking for location using provider: " + providerName);

      // 3. get handle on loc listener
      locationListener = new LocationListenerImpl();

      // 4. if provider not null, use last known location (if recent), or listen for updates
      if (providerName != null) {
         // first check last KNOWN location
         Location lastKnown = locationMgr.getLastKnownLocation(providerName);
         // if the lastKnownLocation is present (often WON'T BE) and within the last X seconds, just use it         
         // NOTE -- this does NOT WORK in the Emulator
         // if you send a DDMS "manual" time or geo fix, you get correct DATE, but fix time starts at 00:00 and increments by 1 second each time sent
         // to test this section (getLastLocation being recent enough), you need to use a real device
         // http://stackoverflow.com/questions/4889487/android-emulators-gps-location-gives-wrong-time
         if (lastKnown != null && lastKnown.getTime() >= (System.currentTimeMillis() - FIX_RECENT_BUFFER_TIME)) {
            detail.setText("Current location (taken from last known using " + providerName + " provider): "
                     + lastKnown.getLatitude() + " / " + lastKnown.getLongitude());
         } else {
            // last known is relatively old, or doesn't exist, use a LocationListener 
            // wait for a good location update for X seconds
            listenForLocation(providerName, LOCATION_LISTEN_WAIT_TIME);
         }
      } else {
         detail.setText("ACCURACY_FINE location provider not available, unable to determine current location.");
      }      
   }

   @Override
   protected void onPause() {
      super.onPause();
      if (progressDialog.isShowing()) {
         progressDialog.hide();
      }
   }

   // TODO leaks ProgressDialog
   private void listenForLocation(String providerName, int duration) {
      progressDialog.show();
      locationMgr.requestLocationUpdates(providerName, 0, 0, locationListener);
      handler.postDelayed(handlerCallback, duration);
   }

   private void endListenForLocation(String message) {
      progressDialog.hide();
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
         endListenForLocation("No suitable location provider available, chosen provider is DISABLED (" + provider + ").");
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