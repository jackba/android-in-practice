package com.manning.aip.tabdroid;

import com.manning.aip.dealdroid.DealsApp;
import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SectionDetailsFragment extends ListFragment {

	Section section;
	int currentPosition = 0;
	DealsApp app;
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	app = (DealsApp) this.getActivity().getApplication();
    	
    	section = app.currentSection;
    	
    	if (savedInstanceState != null){
    		currentPosition = savedInstanceState.getInt("currentPosition");
    	} else if (app.currentItem != null){
    		for (int i=0;i<section.items.size();i++){
    			if (app.currentItem.equals(section.items.get(i))){
    				currentPosition = i;
    				break;
    			}
    		}
    	}
    	
    	ListView listView = this.getListView();
    	listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	String[] dealTitles = new String[section.items.size()];
    	int i = 0;
    	for (Item item : section.items){
    		dealTitles[i++] = item.title;
    	}
    	setListAdapter(new ArrayAdapter<String>(getActivity(), 
    			R.layout.deal_title_list_entry, dealTitles));
    	listView.setSelection(currentPosition);
    	showDeal(currentPosition); 	
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
        outState.putInt("currentPosition", currentPosition);
    }
}
