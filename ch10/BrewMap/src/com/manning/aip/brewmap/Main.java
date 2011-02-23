package com.manning.aip.brewmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.manning.aip.brewmap.model.BrewLocation;
import com.manning.aip.brewmap.xml.BeerMappingParser;
import com.manning.aip.brewmap.xml.BeerMappingXmlPullParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 2.2 emulator fails geocoding -- http://code.google.com/p/android/issues/detail?id=8816

public class Main extends Activity {

   private static final String CITY = "CITY";
   private static final String STATE = "STATE";
   private static final String PIECE = "PIECE";

   private BrewMapApp app;

   private LocationManager locationMgr;

   private ProgressDialog progressDialog;
   private ProgressDialog progressDialog2;
   private ProgressDialog progressDialog3;

   private Geocoder geocoder;

   private BeerMappingParser parser;

   private Handler handler;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      app = (BrewMapApp) getApplication();

      locationMgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

      progressDialog = new ProgressDialog(this);
      progressDialog.setCancelable(false);
      progressDialog.setMessage("Getting current location...");

      progressDialog2 = new ProgressDialog(this);
      progressDialog2.setCancelable(false);
      progressDialog2.setMessage("Retrieving brew location data...");

      progressDialog3 = new ProgressDialog(this);
      progressDialog3.setCancelable(false);
      progressDialog3.setTitle("Geocoding address");

      geocoder = new Geocoder(this);
      // note that API level 9 added the "isPresent" method which could be checked here

      parser = new BeerMappingXmlPullParser();

      // Use Handler and LocationHelper to check for current Location (see LocationHelper for details)
      // (will return last known location from FINE provider if recent, else will listen for updates for 20 seconds)
      handler = new Handler() {
         public void handleMessage(Message m) {
            Log.d("GetCurrentLocation", "Handler returned with message: " + m.toString());
            progressDialog.dismiss();
            if (m.what == LocationHelper.MESSAGE_CODE_LOCATION_FOUND) {
               Toast.makeText(Main.this, "HANDLER RETURNED -- lat:" + m.arg1 + " lon:" + m.arg2, Toast.LENGTH_SHORT)
                        .show();
            } else if (m.what == LocationHelper.MESSAGE_CODE_LOCATION_NULL) {
               Toast.makeText(Main.this, "HANDLER RETURNED -- unable to get location", Toast.LENGTH_SHORT).show();
            } else if (m.what == LocationHelper.MESSAGE_CODE_PROVIDER_NOT_PRESENT) {
               Toast.makeText(Main.this, "HANDLER RETURNED -- provider not present", Toast.LENGTH_SHORT).show();
            }
         }
      };

      final LocationHelper locationHelper = new LocationHelper(locationMgr, handler, Constants.LOG_TAG);

      final EditText input = (EditText) findViewById(R.id.input);
      Button near = (Button) findViewById(R.id.button_nearby);
      near.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            progressDialog.show();
            locationHelper.getCurrentLocation(30); // fire off async call to get current location, which will use handler    
         }
      });
      Button search = (Button) findViewById(R.id.button_search);
      search.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            if (input.getText() != null && !input.getText().toString().trim().equals("")) {               
               new ParseFeedTask().execute(new String[] { PIECE, input.getText().toString() });
               input.setText("");
            } else {
               Toast.makeText(Main.this, "Search criteria required", Toast.LENGTH_SHORT).show();
            }
            
         }
      });
   }

   @Override
   protected void onResume() {
      super.onResume();

      // determine if GPS is enabled or not, if not prompt user to enable it      
      if (!locationMgr.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("GPS is not enabled")
                  .setMessage("Would you like to go the location settings and enable GPS?").setCancelable(true)
                  .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
                     }
                  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                     }
                  });
         AlertDialog alert = builder.create();
         alert.show();
      }
   }

   @Override
   protected void onPause() {
      super.onPause();
      progressDialog.dismiss();
      progressDialog2.dismiss();
      progressDialog3.dismiss();
   }

   private void handleResults(List<BrewLocation> brewLocations) {
      if (brewLocations != null && !brewLocations.isEmpty()) {
         app.setPubs(brewLocations);
         startActivity(new Intent(this, MapResults.class));
      } else {
         Toast.makeText(this, "Brew locations empty!", Toast.LENGTH_SHORT).show();
      }
   }

   private class ParseFeedTask extends AsyncTask<String, Void, List<BrewLocation>> {

      @Override
      protected void onPreExecute() {
         progressDialog2.show();
      }

      @Override
      protected List<BrewLocation> doInBackground(String... args) {
         List<BrewLocation> result = new ArrayList<BrewLocation>();
         if (args == null || args.length != 2) {
            return result;
         }
         String type = args[0];
         String input = args[1];
         if (type.equals(CITY)) {
            result = parser.parseCity(input);
         } else if (type.equals(STATE)) {
            result = parser.parseState(input);
         } else if (type.equals(PIECE)) {
            result = parser.parsePiece(input);
         }

         return result;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected void onPostExecute(List<BrewLocation> brewLocations) {
         progressDialog2.dismiss();
         if (brewLocations != null && !brewLocations.isEmpty()) {
            new GeocodeAddressesTask().execute(brewLocations);
         } else {
            Toast.makeText(Main.this, "Nothing found for that location, please try again", Toast.LENGTH_SHORT).show();
         }
      }
   }

   private class GeocodeAddressesTask extends AsyncTask<List<BrewLocation>, ProgressBean, List<BrewLocation>> {

      @Override
      protected void onPreExecute() {
         progressDialog3.show();
      }

      @Override
      protected void onProgressUpdate(ProgressBean... values) {
         super.onProgressUpdate(values);
         ProgressBean bean = values[0];
         if (bean.current == 1) {
            progressDialog3.setMax(bean.total);
         }
         progressDialog3.setProgress(bean.current);
         progressDialog3.setMessage(bean.message);
      }

      @Override
      protected List<BrewLocation> doInBackground(List<BrewLocation>... args) {
         List<BrewLocation> result = new ArrayList<BrewLocation>();
         if (args == null) {
            return result;
         }

         // geocode the city/state/zip form addresses in the task too
         if (args[0] != null && !args[0].isEmpty()) {
            for (int i = 0; i < args[0].size(); i++) {
               BrewLocation bl = args[0].get(i);
               publishProgress(new ProgressBean(args[0].size(), i + 1, bl.getName()));
               try {
                  List<android.location.Address> addresses =
                           geocoder.getFromLocationName(bl.getAddress().getLocationName(), 1);
                  if (addresses != null && !addresses.isEmpty()) {
                     android.location.Address a = addresses.get(0); // just use first address
                     bl.setLatitude(a.getLatitude());
                     bl.setLongitude(a.getLongitude());
                     Log.d(Constants.LOG_TAG, "Geocoded BrewLocation Address: " + bl.getName() + " " + a);
                     if (bl.getLatitude() == 0 || bl.getLongitude() == 0) {
                        Log.d(Constants.LOG_TAG, "Skipping BrewLocation: " + bl.getName()
                                 + " because address was not geocoded.");
                     } else {
                        result.add(bl);
                     }
                  }
               } catch (IOException e) {
                  Log.e(Constants.LOG_TAG, "Error geocoding location name", e);
               }
            }
         }

         return result;
      }

      @Override
      protected void onPostExecute(List<BrewLocation> brewLocations) {
         progressDialog3.dismiss();
         handleResults(brewLocations);
      }
   }

   private class ProgressBean {
      protected int current;
      protected int total;
      protected String message;

      public ProgressBean(int current, int total, String message) {
         this.current = current;
         this.total = total;
         this.message = message;
      }
   }
}