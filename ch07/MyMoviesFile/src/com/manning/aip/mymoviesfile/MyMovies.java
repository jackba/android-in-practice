package com.manning.aip.mymoviesfile;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MyMovies extends ListActivity {

   private MyMoviesApp app;
   private MovieAdapter adapter;

   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      
      app = (MyMoviesApp) getApplication();

      ListView listView = getListView();

      Button backToTop =
               (Button) getLayoutInflater().inflate(R.layout.list_footer, null);
      backToTop.setCompoundDrawablesWithIntrinsicBounds(getResources()
               .getDrawable(android.R.drawable.ic_menu_upload), null, null,
               null);
      listView.addFooterView(backToTop, null, true);

      this.adapter = new MovieAdapter(this, app.getImageCache());
      listView.setAdapter(this.adapter);
      listView.setItemsCanFocus(false);
   }

   public void backToTop(View view) {
      getListView().setSelection(0);
   }

   protected void onListItemClick(ListView l, View v, int position, long id) {
      this.adapter.toggleMovie(position);
      this.adapter.notifyDataSetInvalidated();
   }
   
   public static final int ENTRY_FORM = 0;
   
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, ENTRY_FORM, 0, "entry form");
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case ENTRY_FORM:
            startActivity(new Intent(this, MovieSearch.class));
            break;
      }
      return false;
   }   
}