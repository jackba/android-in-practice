package com.manning.aip.tabdroid;

import com.manning.aip.dealdroid.DealsApp;
import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

public class DetailsActivity extends Activity {
	boolean active = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.details);
    	DealsApp app = (DealsApp) getApplication();
    	ActionBar bar = this.getActionBar();
    	TabListener listener = new TabListener(){

			@Override
			public void onTabReselected(Tab t, FragmentTransaction txn) {}

			@Override
			public void onTabSelected(Tab t, FragmentTransaction txn) {
				if (active){
					if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					SectionDetailsFragment fragment = 
			    		(SectionDetailsFragment) getFragmentManager().findFragmentById(
			    				R.id.section_list_fragment);
					fragment.setSection(t.getPosition());
					} else {
						FilmstripFragment fragment = (FilmstripFragment) getFragmentManager().findFragmentById(R.id.section_filmstrip_fragment);
						fragment.setSection(t.getPosition());
					}
				}
			}

			@Override
			public void onTabUnselected(Tab t, FragmentTransaction txn) {}
			
		}; 
		for (int i=0;i<Math.min(6, app.sectionList.size());i++){
    		final Section section = app.sectionList.get(i);
    		Tab tab = bar.newTab();
    		tab.setText(section.title);
    		tab.setTabListener(listener);
    		if (app.currentSection != null && app.currentSection.equals(section)){
    			bar.addTab(tab, true);
    		} else {
    			bar.addTab(tab);
    		}
    	}
    	bar.setDisplayShowTitleEnabled(false);
        //bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
       bar.setDisplayShowHomeEnabled(true);
       active = true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in Action Bar clicked; go home
                Intent intent = new Intent(this, DealsMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }    
}
