package com.manning.aip.tabdroid;

import com.manning.aip.dealdroid.DealsApp;
import com.manning.aip.dealdroid.RetrieveImageTask;
import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class FilmstripFragment extends Fragment {
	Section section;
	int currentPosition = 0;
	DealsApp app;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		app = (DealsApp) this.getActivity().getApplication();  	
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
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		HorizontalScrollView strip = (HorizontalScrollView) inflater.inflate(R.layout.filmstrip, container, false);
		fillWithPics(strip);
		return strip;
	}

	private void fillWithPics(HorizontalScrollView strip) {
		ViewGroup pics = (ViewGroup) strip.findViewById(R.id.pics);
		if (pics.getChildCount() > 0){
			pics.removeAllViews();
		}
		int i =0;
		for (Item item : section.items){
			ImageView imgView = new ImageView(getActivity());
			Bitmap bmp = app.imageCache.get(item.itemId);
			if (bmp == null){
				imgView.setBackgroundResource(R.drawable.placeholder);
				imgView.setTag(item.itemId);
				new RetrieveImageTask(imgView, app).execute(item.picUrl);
			} else {
				imgView.setImageBitmap(bmp);
			}
			final int pos = i;
			imgView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View img) {
					currentPosition = pos;
					showDeal(pos);
				}
				
			});
			pics.addView(imgView);
			i++;
		}
		showDeal(currentPosition);
	}
	
    private void showDeal(int position){
    	app.currentItem = app.currentSection.items.get(position);
    	DealFragment fragment = (DealFragment) getFragmentManager().findFragmentById(R.id.deal_fragment);
    	fragment.showCurrentItem();
    }
    
    public void setSection(int position){
    	currentPosition = 0;
    	section = app.sectionList.get(position);
    	app.currentSection = section;
    	HorizontalScrollView strip = (HorizontalScrollView) this.getView();
    	fillWithPics(strip);
    }
}
