package com.manning.aip.tabdroid;

import com.manning.aip.dealdroid.DealsApp;
import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class DetailsActivity extends Activity {
	boolean active = false;
	private DealsApp app;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.details);
    	app = (DealsApp) getApplication();
    	ActionBar bar = this.getActionBar();
    	final FragmentManager fm = getFragmentManager();
    	final Configuration config = getResources().getConfiguration();
    	TabListener listener = new TabListener(){

    		@Override
    		public void onTabReselected(Tab t, FragmentTransaction txn) {}

    		@Override
    		public void onTabSelected(Tab t, FragmentTransaction txn) {
    			if (active){
    				if (config.orientation == ORIENTATION_LANDSCAPE){
    					SectionDetailsFragment fragment = 
    						(SectionDetailsFragment) fm.findFragmentById(
    								R.id.section_list_fragment);
    					fragment.setSection(t.getPosition());
    				} else {
    					FilmstripFragment fragment = 
    						(FilmstripFragment) fm.findFragmentById(
    								R.id.section_filmstrip_fragment);
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
    		tab.setText(chomp(section.title));
    		tab.setTabListener(listener);
    		if (app.currentSection != null && 
    				app.currentSection.equals(section)){
    			bar.addTab(tab, true);
    		} else {
    			bar.addTab(tab);
    		}
    	}
    	bar.setDisplayShowTitleEnabled(false);
    	bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    	active = true;
    }
    
    private static String chomp(String string){
    	if (string == null) return null;
    	String str = string.trim();
    	int i = str.lastIndexOf(' ');
    	if (i < 0){
    		return str;
    	}
    	return str.substring(i+1);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);
        return true;
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
            case R.id.share_action:
            	shareDealUsingChooser("text/*");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void shareDealUsingChooser(final String type) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType(type);
        i.putExtra(Intent.EXTRA_SUBJECT, "Subject:");
        i.putExtra(Intent.EXTRA_TEXT, createDealMessage());
        try {
           startActivity(Intent.createChooser(i, "Share deal ..."));
        } catch (android.content.ActivityNotFoundException ex) {
           Toast.makeText(this, "There are no chooser options installed for the " + type + " + type.",
                    Toast.LENGTH_SHORT).show();
        }
     }    
    
    private String createDealMessage() {
        Item item = app.currentItem;
        StringBuffer sb = new StringBuffer();
        sb.append("Check out this deal:\n");
        sb.append("\nTitle:" + item.title);
        sb.append("\nPrice:" + item.convertedCurrentPrice);
        sb.append("\nLocation:" + item.location);
        sb.append("\nQuantity:" + item.quantity);
        sb.append("\nURL:" + item.dealUrl);
        return sb.toString();
     }    
}
