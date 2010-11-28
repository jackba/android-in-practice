package com.manning.aip.mymoviesdatabase;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.manning.aip.mymoviesdatabase.model.Movie;

import java.util.List;

public class MyMovies extends ListActivity {

   private static final int EDIT = 0;
   private static final int DELETE = 1;
   
   public static final int PREFS = 0;
   public static final int ENTRY_FORM = 1;
   public static final int SEARCH_FORM = 2;
   public static final int CAT_MANAGER= 3;
   
   private MyMoviesApp app;
   
   private MovieAdapterDatabase adapter;
   private List<Movie> movies;

   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      app = (MyMoviesApp) getApplication();

      ListView listView = getListView();

      Button backToTop = (Button) getLayoutInflater().inflate(R.layout.list_footer, null);
      backToTop.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(android.R.drawable.ic_menu_upload),
               null, null, null);
      listView.addFooterView(backToTop, null, true);

      movies = app.getDataManager().getMovieDao().getAll();
      adapter = new MovieAdapterDatabase(this, app.getImageCache(), movies);
      listView.setAdapter(this.adapter);
      listView.setItemsCanFocus(false);
      registerForContextMenu(listView);
   }

   public void backToTop(View view) {
      getListView().setSelection(0);
   }

   protected void onListItemClick(ListView l, View v, int position, long id) {
      Intent intent = new Intent(this, MovieDetail.class);
      intent.putExtra(MovieDetail.MOVIE_ID_KEY, movies.get(position).getId());
      startActivity(intent);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, PREFS, 0, "Preferences").setIcon(android.R.drawable.ic_menu_preferences);
      menu.add(0, SEARCH_FORM, 0, "Search").setIcon(android.R.drawable.ic_menu_search);
      menu.add(0, ENTRY_FORM, 0, "Form").setIcon(android.R.drawable.ic_menu_edit);
      menu.add(0, CAT_MANAGER, 0, "Category Manager").setIcon(android.R.drawable.ic_menu_manage);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case PREFS:
            startActivity(new Intent(this, Preferences.class));
            break;
         case ENTRY_FORM:
            startActivity(new Intent(this, MovieForm.class));
            break;
         case SEARCH_FORM:
            startActivity(new Intent(this, MovieSearch.class));
            break;
         case CAT_MANAGER:
            startActivity(new Intent(this, CategoryManager.class));
            break;
      }
      return false;
   }
   
   @Override
   public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      menu.add(0, EDIT, 0, "Edit Movie");
      menu.add(0, DELETE, 1, "Delete Movie");
      menu.setHeaderTitle("Action");
   }

   @Override
   public boolean onContextItemSelected(final MenuItem item) {
      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
      final Movie movie = movies.get(info.position);
      switch (item.getItemId()) {
         case EDIT:
            Toast.makeText(MyMovies.this, "TODO EDIT " + movie.getName(), Toast.LENGTH_SHORT);
            return true;
         case DELETE:
            new AlertDialog.Builder(MyMovies.this).setTitle("Delete Movie?").setMessage(movie.getName())
                     .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface d, final int i) {
                           app.getDataManager().getMovieDao().delete(movie);
                           adapter.remove(movie);
                        }
                     }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface d, final int i) {
                        }
                     }).show();
            return true;
         default:
            return super.onContextItemSelected(item);
      }
   }
}