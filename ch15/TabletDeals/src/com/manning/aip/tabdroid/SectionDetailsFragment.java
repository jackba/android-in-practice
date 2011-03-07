package com.manning.aip.tabdroid;

import com.manning.aip.dealdroid.DealsApp;
import com.manning.aip.dealdroid.RetrieveImageTask;
import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class SectionDetailsFragment extends ListFragment {

	Section section;
	int currentPosition = 0;
	DealsApp app;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		app = (DealsApp) this.getActivity().getApplication();  	
	}
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	section = app.currentSection;
    	
    	if (savedInstanceState != null){
    		currentPosition = savedInstanceState.getInt("currentPosition");
    		int savedSectionPos = savedInstanceState.getInt("currentSection", -1);
    		if (savedSectionPos >= 0){
    			section = app.sectionList.get(savedSectionPos);
    			app.currentSection = section;
    		}
    	} else if (app.currentItem != null){
    		for (int i=0;i<section.items.size();i++){
    			if (app.currentItem.equals(section.items.get(i))){
    				currentPosition = i;
    				break;
    			}
    		}
    	}
    	buildUi();
    }
    
    private void buildUi(){
    	ListView listView = this.getListView();
    	listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
	    	String[] dealTitles = new String[section.items.size()];
	    	int i = 0;
	    	for (Item item : section.items){
	    		dealTitles[i++] = item.title;
	    	}
	    	setListAdapter(new ArrayAdapter<String>(getActivity(), 
	    			R.layout.deal_title_list_entry, dealTitles));
    	} else { // portrait
    		listView.setHorizontalScrollBarEnabled(true);
    		setListAdapter(new DealsImageAdapter());
    	}
    	listView.setSelection(currentPosition);
    	showDeal(currentPosition); 
    }
    
    public void setSection(int position){
    	currentPosition = 0;
    	section = app.sectionList.get(position);
    	app.currentSection = section;
    	buildUi();
    }
    
    private void showDeal(int position){
    	app.currentItem = app.currentSection.items.get(position);
    	DealFragment fragment = (DealFragment) getFragmentManager().findFragmentById(R.id.deal_fragment);
    	fragment.showCurrentItem();
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        currentPosition = position;
        showDeal(position);
    }
    
    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i=0;i<app.sectionList.size();i++){
        	if (app.sectionList.get(i).equals(section)){
        		outState.putInt("currentSection", i);
        		break;
        	}
        }
        outState.putInt("currentPosition", currentPosition);
    }
    
    class DealsImageAdapter extends BaseAdapter{
    	
    	private final Context ctx = getActivity();
    	
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
				imgView = new ImageView(ctx);
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
			return imgView;
		}
    	
    }    
}
