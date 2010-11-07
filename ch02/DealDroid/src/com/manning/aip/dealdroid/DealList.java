package com.manning.aip.dealdroid;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

   private static final int MENU_REPARSE = 0;

   private DealDroidApp app;
   private DealsAdapter dealsAdapter;
   private ArrayAdapter<Section> spinnerAdapter;
   private int currentSelectedSection;
   private ProgressDialog progressDialog;

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
      spinnerAdapter =
               new ArrayAdapter<Section>(DealList.this, android.R.layout.simple_spinner_item, app.getSectionList());
      spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      sectionSpinner.setAdapter(spinnerAdapter);
      sectionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            if (currentSelectedSection != position) {
               currentSelectedSection = position;
               app.setCurrentSection(app.getSectionList().get(position));
               dealsAdapter.setSection(app.getSectionList().get(position));
               dealsAdapter.notifyDataSetChanged();
            }
         }

         @Override
         public void onNothingSelected(AdapterView<?> parentView) {
            Log.d(Constants.LOG_TAG, "Nothing to see here");
         }
      });

      this.progressDialog = new ProgressDialog(this);
      this.progressDialog.setMax(2);
      this.progressDialog.setCancelable(false);
      this.progressDialog.setMessage(getString(R.string.deal_list_retrieving_data));

      if (app.getSectionList().isEmpty()) {
         if (app.connectionPresent()) {
            new ParseFeedTask().execute();
         } else {
            Toast.makeText(this, getString(R.string.deal_list_network_unavailable), Toast.LENGTH_LONG).show();
         }
      } else {
         // start off the sections selection with first one, Daily Deals
         dealsAdapter.setSection(app.getSectionList().get(0));
         dealsAdapter.notifyDataSetChanged();
         spinnerAdapter.notifyDataSetChanged();
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

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, DealList.MENU_REPARSE, 0, R.string.deal_list_reparse_menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case MENU_REPARSE:
            if (app.connectionPresent()) {
               new ParseFeedTask().execute();
            } else {
               Toast.makeText(this, getString(R.string.deal_list_network_unavailable), Toast.LENGTH_LONG).show();
            }
            break;
      }
      return false;
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
      protected List<Section> doInBackground(final Void... args) {
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
      protected void onPostExecute(final List<Section> taskSectionList) {
         if (!taskSectionList.isEmpty()) {
            app.getSectionList().clear();
            app.getSectionList().addAll(taskSectionList);

            // also make sure to update the "previous" deal ids with the current set 
            // so that when service checking for new deals runs it has correct data to compare to
            List<Long> currentDealIds = app.parseItemsIntoDealIds(app.getSectionList().get(0).getItems());
            app.setPreviousDealIds(currentDealIds);

            // start off the sections selection with first one, Daily Deals
            dealsAdapter.setSection(app.getSectionList().get(0));
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
            app.getImageCache().put((Long) imageView.getTag(), bitmap);
            imageView.setTag(null);
         }
      }
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
            return section.getItems().size();
         }
         return 0;
      }

      @Override
      public Item getItem(int position) {
         if (section != null) {
            return section.getItems().get(position);
         }
         return null;
      }

      @Override
      public long getItemId(int position) {
         return (getItem(position)).getItemId();
      }

      @Override
      public View getView(final int position, View convertView, ViewGroup parent) {

         if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
         }

         final TextView text = (TextView) convertView.findViewById(R.id.deal_title);
         final ImageView image = (ImageView) convertView.findViewById(R.id.deal_img);
         image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ddicon));

         final Item item = getItem(position);

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

      public synchronized void setSection(Section section) {
         if (((section != null) && (this.section == null))
                  || ((section != null) && (this.section != null) && !this.section.equals(section))) {
            this.section = section;
         }
      }
   }
}