package com.manning.aip.dealdroid;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import java.util.ArrayList;
import java.util.List;

public class DealList extends ListActivity {

   private DealDroidApp app;
   private DealsAdapter dealsAdapter;
   private ArrayAdapter<Section> spinnerAdapter;
   private int currentSelectedSection;
   private ProgressDialog progressDialog;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.deallist);
      
      progressDialog = new ProgressDialog(this);
      progressDialog.setMax(2);
      progressDialog.setCancelable(false);
      progressDialog.setMessage(getString(R.string.deal_list_retrieving_data));

      // Use Application object for app wide state
      app = (DealDroidApp) getApplication();

      // Adapter for deals
      dealsAdapter = new DealsAdapter();

      // ListView (start with first section at index 0);
      setListAdapter(dealsAdapter);
      
      // get Sections list from application (parsing feed if necessary)
      if (app.getSectionList().isEmpty()) {
         if (app.connectionPresent()) {
            new ParseFeedTask().execute();
         } else {
            Toast.makeText(this, getString(R.string.deal_list_network_unavailable), Toast.LENGTH_LONG).show();
         }
      } else {
         resetListAdapter(app.getSectionList().get(0).getItems());
      }  

      // Spinner
      Spinner sectionSpinner = (Spinner) findViewById(R.id.section_spinner);
      spinnerAdapter =
               new ArrayAdapter<Section>(DealList.this, android.R.layout.simple_spinner_item, app.getSectionList());
      spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      sectionSpinner.setAdapter(spinnerAdapter);
      sectionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            if (currentSelectedSection != position) {
               currentSelectedSection = position;
               resetListAdapter(app.getSectionList().get(position).getItems());
            }
         }

         @Override
         public void onNothingSelected(AdapterView<?> parentView) {
            // ignore
         }
      });      

      scheduleAlarmReceiver();
   }
   
   private void resetListAdapter(List<Item> items) {
      dealsAdapter.clear();
      for (Item i : items) {
         dealsAdapter.add(i);
      }
   }

   @Override
   public void onStart() {
      super.onStart();
      boolean forceReload = this.getIntent().getBooleanExtra(Constants.FORCE_RELOAD, false);
      this.getIntent().removeExtra(Constants.FORCE_RELOAD);
      if (forceReload) {
         if (app.connectionPresent()) {
            new ParseFeedTask().execute();
         } else {
            Toast.makeText(this, getString(R.string.deal_list_network_unavailable), Toast.LENGTH_LONG).show();
         }
      }
   }

   @Override
   protected void onListItemClick(final ListView listView, final View view, final int position, final long id) {
      view.setBackgroundColor(android.R.color.background_light);
      app.setCurrentItem(app.getSectionList().get(currentSelectedSection).getItems().get(position));
      Intent dealDetails = new Intent(DealList.this, DealDetails.class);
      startActivity(dealDetails);
   }

   @Override
   public void onPause() {
      if (progressDialog.isShowing()) {
         progressDialog.dismiss();
      }
      super.onPause();
   }

   // Schedule AlarmManager to invoke DealAlarmReceiver and cancel any existing current PendingIntent
   // we do this because we *also* invoke the receiver from a BOOT_COMPLETED receiver
   // so that we make sure the service runs either when app is installed/started, or when device boots
   private void scheduleAlarmReceiver() {
      AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
      PendingIntent pendingIntent =
               PendingIntent.getBroadcast(this, 0, new Intent(this, DealAlarmReceiver.class),
                        PendingIntent.FLAG_CANCEL_CURRENT);

      // Use inexact repeating which is easier on battery (system can phase events and not wake at exact times)
      alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, Constants.ALARM_TRIGGER_AT_TIME,
               Constants.ALARM_INTERVAL, pendingIntent);
   }

   // TODO use ViewHolder with the "withService" version of DealDroid
   
   // Use a custom Adapter to control the layout and views
   private class DealsAdapter extends ArrayAdapter<Item> {      

      public DealsAdapter() {
         super(DealList.this, R.layout.list_item, new ArrayList<Item>());
      }

      @Override
      public View getView(int position, View convertView, ViewGroup parent) {

         if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
         }

         TextView text = (TextView) convertView.findViewById(R.id.deal_title);
         ImageView image = (ImageView) convertView.findViewById(R.id.deal_img);
         image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ddicon));

         Item item = getItem(position);

         if (item != null) {
            text.setText(item.getTitle());
            Bitmap bitmap = app.getImageCache().get(item.getItemId());
            if (bitmap != null) {
               image.setImageBitmap(bitmap);
            } else {
               // put item ID on image as TAG for use in task
               image.setTag(item.getItemId());
               // separate thread/via task, for retrieving each image
               // (note that this is brittle as is, should stop all threads in onPause)               
               new RetrieveImageTask(image).execute(item.getSmallPicUrl());
            }
         }

         return convertView;
      }
   }
   
   // Use an AsyncTask<Params, Progress, Result> to easily perform tasks off of the UI Thread
   private class ParseFeedTask extends AsyncTask<Void, Integer, List<Section>> {

      @Override
      protected void onPreExecute() {
         if (progressDialog.isShowing()) {
            progressDialog.dismiss();
         }
      }

      @Override
      protected List<Section> doInBackground(Void... args) {
         publishProgress(1);
         List<Section> sections = app.getParser().parse();
         publishProgress(2);
         return sections;
      }

      @Override
      protected void onProgressUpdate(Integer... progress) {
         int currentProgress = progress[0];
         if ((currentProgress == 1) && !progressDialog.isShowing()) {
            progressDialog.show();
         } else if ((currentProgress == 2) && progressDialog.isShowing()) {
            progressDialog.dismiss();
         }
         progressDialog.setProgress(progress[0]);
      }

      @Override
      protected void onPostExecute(List<Section> taskSectionList) {
         if (!taskSectionList.isEmpty()) {
            app.getSectionList().clear();
            app.getSectionList().addAll(taskSectionList);
            spinnerAdapter.notifyDataSetChanged();            
            
            resetListAdapter(app.getSectionList().get(0).getItems());
         } else {
            Toast.makeText(DealList.this, getString(R.string.deal_list_missing_data), Toast.LENGTH_LONG).show();
         }
      }
   }

   private class RetrieveImageTask extends AsyncTask<String, Void, Bitmap> {
      private ImageView imageView;

      public RetrieveImageTask(ImageView imageView) {
         this.imageView = imageView;
      }

      @Override
      protected Bitmap doInBackground(String... args) {
         Bitmap bitmap = app.retrieveBitmap(args[0]);
         return bitmap;
      }

      @Override
      protected void onPostExecute(Bitmap bitmap) {
         if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            app.getImageCache().put((Long) imageView.getTag(), bitmap);
            imageView.setTag(null);
         }
      }
   }
}