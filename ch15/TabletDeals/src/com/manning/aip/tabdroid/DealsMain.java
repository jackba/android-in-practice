package com.manning.aip.tabdroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.manning.aip.dealdroid.DealsApp;
import com.manning.aip.dealdroid.RetrieveImageTask;
import com.manning.aip.dealdroid.Util;
import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;
import com.manning.aip.dealdroid.xml.DailyDealsFeedParser;
import com.manning.aip.dealdroid.xml.DailyDealsXmlPullFeedParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.StackView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class DealsMain extends Activity {
	
	private ProgressDialog progressDialog;
	private DealsApp app;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);        
        app = (DealsApp) getApplication();
        
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMax(2);
        this.progressDialog.setCancelable(false);
        this.progressDialog.setMessage(getString(R.string.deal_list_retrieving_data));
        
        new ParseFeedTask().execute();
    }
    
    private void buildUi(){
        TableLayout table = (TableLayout) findViewById(R.id.stackTable);
        int stackNum = 0;
        for (int i=0;i<table.getChildCount();i++){
        	TableRow row = (TableRow) table.getChildAt(i);
        	for (int j=0;j<row.getChildCount();j++){
        		StackView stack = (StackView) row.getChildAt(j);
        		stack.setAdapter(
        				new DealsStackAdapter(app.sectionList.get(stackNum++)));
        	}
        }
    }
    
    class DealsStackAdapter extends BaseAdapter{
    	
    	private final Section section;
    	private final Context ctx = DealsMain.this;
    	
    	DealsStackAdapter(Section section){
    		this.section =section;
    	}
    	
		@Override
		public int getCount() {
			return section.items.size();
		}

		@Override
		public Object getItem(int index) {
			return section.items.get(index);
		}

		@Override
		public long getItemId(int index) {
			return section.items.get(index).itemId;
		}

		@Override
		public View getView(int index, View recycleView, ViewGroup parent) {
			ImageView imgView = (ImageView) recycleView;
			if (imgView == null){
				imgView = new ImageView(DealsMain.this);
			}
			final Item item = (Item) getItem(index);
			Bitmap bmp = app.imageCache.get(item.itemId);
			if (bmp == null){
				imgView.setBackgroundResource(R.drawable.placeholder);
				imgView.setTag(item.itemId);
				new RetrieveImageTask(imgView, app).execute(item.picUrl);
			} else {
				imgView.setImageBitmap(bmp);
			}
			imgView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					app.currentSection = section;
					app.currentItem = item;
					Intent intent = new Intent(ctx, DetailsActivity.class);
					startActivity(intent);
				}
				
			});
			return imgView;
		}
    	
    }
    
    class ParseFeedTask extends AsyncTask<Void, Integer, List<Section>>{
    	final Context ctx = DealsMain.this;
        @Override
        protected void onPreExecute() {
           if (progressDialog.isShowing()) {
              progressDialog.dismiss();
           }
        }
        
		@Override
		protected List<Section> doInBackground(Void... args) {
	         publishProgress(1);
	         List<Section> sections = app.parser.parse();
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
	            app.sectionList.clear();
	            app.sectionList.addAll(taskSectionList);

	            // also make sure to update the "previous" deal ids with the current set 
	            // so that when service checking for new deals runs it has correct data to compare to
	            List<Long> currentDealIds = app.parseItemsIntoDealIds(app.sectionList.get(0).items);
	            app.setPreviousDealIdsToPrefs(currentDealIds);

	            buildUi();
	         } else {
	            Toast.makeText(ctx, getString(R.string.deal_list_missing_data), Toast.LENGTH_LONG).show();
	         }
	      }		
    }
}