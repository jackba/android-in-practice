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

import java.util.List;

public class DealList extends ListActivity {

   private DealDroidApp app;
   private DealsAdapter dealsAdapter;
   private ArrayAdapter<Section> spinnerAdapter;
   ///private List<Section> sectionList;
   private int currentSelectedSection;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.deallist);

      // Use Application object for app wide state
      app = (DealDroidApp) getApplication();

      // Adapter for deals
      dealsAdapter = new DealsAdapter();

      // ListView (start with first section at index 0);
      setListAdapter(dealsAdapter);

      // Spinner
      Spinner sectionSpinner = (Spinner) findViewById(R.id.section_spinner);
      spinnerAdapter = new ArrayAdapter<Section>(DealList.this, android.R.layout.simple_spinner_item, app.sectionList);
      spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      sectionSpinner.setAdapter(spinnerAdapter);
      sectionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            if (currentSelectedSection != position) {
               currentSelectedSection = position;
               app.currentSection = app.sectionList.get(position);
               dealsAdapter.setSection(app.sectionList.get(position));
               dealsAdapter.notifyDataSetChanged();
            }
         }

         @Override
         public void onNothingSelected(AdapterView<?> parentView) {
            Log.d(Constants.LOG_TAG, "Nothing to see here");
         }
      });

      // Oversimplified AsyncTask 
      // (better to handle instance states and dismiss Progress onPause, etc., here just simple)
      if (app.sectionList.isEmpty()) {
         new ParseFeedTask().execute();
      } else {
         if (!app.sectionList.isEmpty()) {
            // start off the sections selection with first one, Daily Deals
            dealsAdapter.setSection(app.sectionList.get(0));
            dealsAdapter.notifyDataSetChanged();
            spinnerAdapter.notifyDataSetChanged();
         } else {
            Toast.makeText(this, getString(R.string.deal_list_missing_data), Toast.LENGTH_LONG).show();
         }
      }

      sheduleAlarmReceiver();
   }

   @Override
   protected void onListItemClick(final ListView listView, final View view, final int position, final long id) {
      app.currentItem = app.sectionList.get(currentSelectedSection).items.get(position);
      Intent dealDetails = new Intent(DealList.this, DealDetails.class);
      startActivity(dealDetails);
   }

   // Schedule AlarmManager to invoke DealAlarmReceiver and cancel any existing current PendingIntent
   // we do this because we *also* invoke the receiver from a BOOT_COMPLETED receiver
   // so that we make sure the service runs either when app is installed/started, or when device boots
   private void sheduleAlarmReceiver() {
      AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
      PendingIntent pendingIntent =
               PendingIntent.getBroadcast(this, 0, new Intent(this, DealAlarmReceiver.class),
                        PendingIntent.FLAG_CANCEL_CURRENT);

      // Use inexact repeating which is easier on battery (system can phase events and not wake at exact times)
      alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, Constants.ALARM_TRIGGER_AT_TIME,
               Constants.ALARM_INTERVAL, pendingIntent);
   }

   // Use an AsyncTask<Params, Progress, Result> to easily perform tasks off of the UI Thread
   private class ParseFeedTask extends AsyncTask<Void, Void, List<Section>> {
      private final ProgressDialog dialog = new ProgressDialog(DealList.this);

      @Override
      protected void onPreExecute() {
         dialog.setMessage("Getting deal data . . .");
         dialog.show();
      }

      @Override
      protected List<Section> doInBackground(final Void... args) {
         return app.parser.parse();
      }

      @Override
      protected void onPostExecute(final List<Section> taskSectionList) {
         if (dialog.isShowing()) {
            // TODO cleanup progress
            // NOTE this is very easy to leak and cause errors, if Activity restarts before task is complete
            // generally need to hold an instance of task and use onPause, or use publishProgress/onProgressUpdate
            // and make ProgressDialog not a member of AsyncTask but of Activity
            // here we are skipping these steps to keep things simpler
            // (to demonstrate this error, rotate orientation while progress message is displayed)
            dialog.dismiss();
         }

         if (!taskSectionList.isEmpty()) {
            app.sectionList.clear();
            app.sectionList.addAll(taskSectionList);

            // start off the sections selection with first one, Daily Deals
            dealsAdapter.setSection(app.sectionList.get(0));
            dealsAdapter.notifyDataSetChanged();
            spinnerAdapter.notifyDataSetChanged();
         } else {
            Toast.makeText(DealList.this, getString(R.string.deal_list_missing_data), Toast.LENGTH_LONG).show();
         }
      }
   }

   private class RetrieveImageTask extends AsyncTask<String, Void, Bitmap> {
      private ImageView imageView;

      public RetrieveImageTask(final ImageView imageView) {
         this.imageView = imageView;
      }

      @Override
      protected Bitmap doInBackground(final String... args) {
         Bitmap bitmap = app.retrieveBitmap(args[0]);
         return bitmap;
      }

      @Override
      protected void onPostExecute(final Bitmap bitmap) {
         if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            app.imageCache.put((Long) imageView.getTag(), bitmap);
            imageView.setTag(null);
         }
      }
   }

   // Use ViewHolder and getTag/setTag to cut down on trips to findViewById in adapters/ListViews
   private class ViewHolder {
      private TextView text;
      private ImageView image;
   }

   // Use a custom Adapter to control the layout and views
   private class DealsAdapter extends BaseAdapter {
      private Section section;

      public DealsAdapter() {
         this.section = null;
      }

      @Override
      public int getCount() {
         if (section != null) {
            return section.items.size();
         }
         return 0;
      }

      @Override
      public Item getItem(int position) {
         if (section != null) {
            return section.items.get(position);
         }
         return null;
      }

      @Override
      public long getItemId(int position) {
         return (getItem(position)).itemId;
      }

      @Override
      public View getView(final int position, View convertView, ViewGroup parent) {

         if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.deal_title);
            holder.image = (ImageView) convertView.findViewById(R.id.deal_img);
            convertView.setTag(holder);
         }

         ViewHolder holder = (ViewHolder) convertView.getTag();
         final TextView text = holder.text;
         final ImageView image = holder.image;
         image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ddicon));

         final Item item = section.items.get(position);

         if (item != null) {
            text.setText(item.title);
            Bitmap bitmap = app.imageCache.get(item.itemId);
            if (bitmap != null) {
               image.setImageBitmap(bitmap);
            } else {
               // put item ID on image as TAG for use in task
               image.setTag(item.itemId);
               // separate thread/via task, for retrieving each image
               // (note that this is brittle as is, should stop all threads in onPause)               
               new RetrieveImageTask(image).execute(item.smallPicUrl);
            }
         }

         return convertView;
      }

      public synchronized void setSection(Section section) {
         if (((section != null) && (this.section == null))
                  || ((section != null) && (this.section != null) && !this.section.equals(section))) {
            this.section = section;
         }
      }
   }
}