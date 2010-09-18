package com.manning.aip;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MyMovies extends ListActivity {

   private MovieAdapter adapter;

   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.main);

      ListView listView = getListView();

      Button backToTop =
               (Button) getLayoutInflater().inflate(R.layout.list_footer, null);
      backToTop.setCompoundDrawablesWithIntrinsicBounds(getResources()
               .getDrawable(android.R.drawable.ic_menu_upload), null, null,
               null);
      listView.addFooterView(backToTop, null, true);

      this.adapter = new MovieAdapter(this);
      listView.setAdapter(this.adapter);
      listView.setItemsCanFocus(false);
   }

   public void backToTop(View view) {
      getListView().setSelection(0);
   }

   protected void onListItemClick(ListView l, View v, int position, long id) {
      this.adapter.toggleMovie(position);
      this.adapter.notifyDataSetChanged();
   }
}