package com.manning.aip.brewmap;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LocationUtil {

   private static final int LOCATION_LISTEN_WAIT_TIME = 20000;
   private static final int FIX_RECENT_BUFFER_TIME = 30000;

   private LocationManager locationMgr;
   private LocationListener locationListener;
   private Handler handler;
   private Runnable handlerCallback;
   private Criteria criteria;
   private String providerName;

   public LocationUtil(LocationManager locationMgr, Handler handler) {
      this.locationMgr = locationMgr;
      this.handler = handler;
      
      // setup listener
      locationListener = new LocationListenerImpl();

      // get provider 
      criteria = new Criteria();
      // use Criteria to get provider (and could use COARSE, but doesn't work in emulator)
      // (FINE will use EITHER network/gps, whichever is the best enabled match, except in emulator must be gps)
      // (NOTE: network won't work unless enabled - Settings->Location & Security Settings->Use wireless networks)
      criteria.setAccuracy(Criteria.ACCURACY_COARSE);
      providerName = locationMgr.getBestProvider(criteria, true);

      // TODO if providerName is null throw an exception/bail out
   }

   public void getLocation() {      
      // first check last KNOWN location
      Location lastKnown = locationMgr.getLastKnownLocation(providerName);
      // if the lastKnownLocation is present (often WON'T BE) and within the last X seconds, just use it         
      // NOTE -- this does NOT WORK in the Emulator
      // if you send a DDMS "manual" time or geo fix, you get correct DATE, but fix time starts at 00:00 and increments by 1 second each time sent
      // to test this section (getLastLocation being recent enough), you need to use a real device
      // http://stackoverflow.com/questions/4889487/android-emulators-gps-location-gives-wrong-time
      if (lastKnown != null && lastKnown.getTime() >= (System.currentTimeMillis() - FIX_RECENT_BUFFER_TIME)) {
         // return lastKnown lat/long on Message via Handler
         sendLocationToHandler((int) (lastKnown.getLatitude() * 1e6), (int) (lastKnown.getLongitude() * 1e6));
      } else {
         // last known is relatively old, or doesn't exist, use a LocationListener 
         // wait for a good location update for X seconds
         listenForLocation(providerName, LOCATION_LISTEN_WAIT_TIME);
      }
   }

   private void sendLocationToHandler(int lat, int lon) {
      Message msg = Message.obtain(handler, 1, lat, lon);
      handler.sendMessage(msg);
   }

   private void listenForLocation(String providerName, int duration) {
      locationMgr.requestLocationUpdates(providerName, 0, 0, locationListener);
      handler.postDelayed(handlerCallback, duration);
   }

   private void endListenForLocation(Location loc) {
      locationMgr.removeUpdates(locationListener);
      handler.removeCallbacks(handlerCallback);
      if (loc != null) {
         sendLocationToHandler((int) (loc.getLatitude() * 1e6), (int) (loc.getLongitude() * 1e6));
      }
   }

   // LocationListener impl
   private class LocationListenerImpl implements LocationListener {
      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {
         Log.d("LocationListener", "Status changed to " + status);
         switch (status) {
            case LocationProvider.AVAILABLE:
               break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
               break;
            case LocationProvider.OUT_OF_SERVICE:
               endListenForLocation(null);
         }
      }

      @Override
      public void onLocationChanged(Location loc) {
         Log.d("LocationListener", "Location changed to " + loc);
         if (loc == null) {
            return;
         }
         endListenForLocation(loc);
      }

      @Override
      public void onProviderDisabled(String provider) {
         endListenForLocation(null);
      }

      @Override
      public void onProviderEnabled(String provider) {
      }
   }
}