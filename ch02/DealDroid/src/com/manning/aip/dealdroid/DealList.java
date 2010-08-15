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
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DealList extends ListActivity {

   private DealDroidApp app;
   private ArrayAdapter<Section> spinnerAdapter;
   private DealsAdapter dealsAdapter;   
   private List<Section> sectionList = null;
   private int currentSelectedSection = 0;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.showdeals);
      
      // Use Application object for app wide state
      app = (DealDroidApp) getApplication();      

      // Adapter for deals, with empty list of data to start
      sectionList =  new ArrayList<Section>();
      dealsAdapter = new DealsAdapter();          

      // ListView (start with first section at index 0);
      setListAdapter(dealsAdapter);      

      // Spinner
      spinnerAdapter =
               new ArrayAdapter<Section>(DealList.this, android.R.layout.simple_spinner_item, sectionList);
      spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

      Spinner sectionSpinner = (Spinner) findViewById(R.id.section_spinner);
      sectionSpinner.setAdapter(spinnerAdapter);
      sectionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            if (currentSelectedSection != position) {
               Section section = sectionList.get(position);              
               app.currentSection = section;
               dealsAdapter.setSection(section);
               dealsAdapter.notifyDataSetChanged();
               currentSelectedSection = position;
            }
         }

         @Override
         public void onNothingSelected(AdapterView<?> parentView) {
            Log.d(Constants.LOG_TAG, "Nothing to see here");
         }
      });

      // Oversimplified AsyncTask (better to handle instance states and dismiss Progress onPause, etc.)      
      new ParseFeedTask().execute();      
   }
   
   // Use ViewHolder and getTag/setTag to cut down on trips to findViewById in adapters/ListViews
   private class ViewHolder {
      private TextView text;
      private ImageView image;
   }
   
   // Use a custom Adapter to control the layout and views
   private class DealsAdapter extends BaseAdapter {
      private Section section ;

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
         return ((Item) getItem(position)).itemId;
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
         
         Item item = null;
         if (section != null && section.items.size() >= position) {
            item = section.items.get(position);
         }
         
         TextView text = holder.text;
         ImageView image = holder.image;
         
         if (item != null) {
            text.setText(section.items.get(position).title);
            Bitmap bitmap = app.iconCache.get(item.itemId);                    
            if (bitmap == null){
               try {
                  // this is the quick and dirty way to do this, HttpClient and a sep Thread/Task would be better
                  URL imageUrl = new URL(item.smallPicUrl);           
                  InputStream stream = imageUrl.openConnection().getInputStream();
                  bitmap = BitmapFactory.decodeStream(stream);
                  app.iconCache.put(item.itemId, bitmap);
               } catch (Exception e) {
                  Log.e(Constants.LOG_TAG, "Exception loading image", e);
               }
            }            
            image.setImageBitmap(bitmap);
         }          

         convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               app.currentItem = getItem(position);
               Intent dealDetails = new Intent(DealList.this, DealDetails.class);
               startActivity(dealDetails);
            }
         });

         return convertView;
      }

      public synchronized void setSection(Section section) {
         if ((section != null && this.section == null)
                  || (section != null && this.section != null && !this.section.equals(section))) {
            this.section = section;
         }
      }
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
            dialog.dismiss();
         }
         sectionList.clear();
         sectionList.addAll(taskSectionList);   
         if (sectionList != null && !sectionList.isEmpty()) {
            // start off the sections selection with first one, Daily Deals
            dealsAdapter.setSection(sectionList.get(0));
            dealsAdapter.notifyDataSetChanged();  
            spinnerAdapter.notifyDataSetChanged();
         }
      }
   }
}