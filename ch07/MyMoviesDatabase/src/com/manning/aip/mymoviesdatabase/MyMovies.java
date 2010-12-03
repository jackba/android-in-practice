package com.manning.aip.mymoviesdatabase;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.manning.aip.mymoviesdatabase.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MyMovies extends ListActivity {

   private static final int CONTEXT_MENU_DELETE = 0;

   private static final int OPTIONS_MENU_SEARCH = 0;
   private static final int OPTIONS_MENU_CATMGR = 1;
   private static final int OPTIONS_MENU_PREFS = 2;
   private static final int OPTIONS_MENU_ABOUT = 3;

   private static final String ABOUT =
            "Demo application for the Manning Publications book \"Android in Practice\".\n\nPowered by:\n http://themoviedb.org";

   private SpannableString aboutString;

   private MyMoviesApp app;

   private MovieAdapterDatabase adapter;
   private List<Movie> movies;

   private Button backToTop;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      app = (MyMoviesApp) getApplication();

      ListView listView = getListView();

      backToTop = (Button) getLayoutInflater().inflate(R.layout.list_footer, null);
      backToTop.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(android.R.drawable.ic_menu_upload),
               null, null, null);
      // must add to ListView BEFORE setting adapter
      listView.addFooterView(backToTop, null, true);

      movies = new ArrayList<Movie>();
      adapter = new MovieAdapterDatabase(this, app.getImageCache(), movies);
      listView.setAdapter(this.adapter);
      listView.setItemsCanFocus(false);
      listView.setEmptyView(findViewById(R.id.main_list_empty));
      registerForContextMenu(listView);
      aboutString = new SpannableString(MyMovies.ABOUT);
      Linkify.addLinks(aboutString, Linkify.ALL);
   }

   @Override
   public void onResume() {
      super.onResume();
      movies.clear();
      movies.addAll(app.getDataManager().getMovieHeaders());
      adapter.notifyDataSetChanged();
      if (movies.size() < 8) {
         backToTop.setVisibility(View.INVISIBLE);
      } else {
         backToTop.setVisibility(View.VISIBLE);
      }
   }

   public void backToTop(View view) {
      getListView().setSelection(0);
   }

   @Override
   protected void onListItemClick(ListView l, View v, int position, long id) {
      Intent intent = new Intent(this, MovieDetail.class);
      intent.putExtra(MovieDetail.MOVIE_ID_KEY, movies.get(position).getId());
      startActivity(intent);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, MyMovies.OPTIONS_MENU_SEARCH, 0, "Search").setIcon(android.R.drawable.ic_menu_search);
      menu.add(0, MyMovies.OPTIONS_MENU_CATMGR, 0, "Category Manager").setIcon(android.R.drawable.ic_menu_manage);
      menu.add(0, MyMovies.OPTIONS_MENU_PREFS, 0, "Preferences").setIcon(android.R.drawable.ic_menu_preferences);
      menu.add(0, MyMovies.OPTIONS_MENU_ABOUT, 0, "About").setIcon(android.R.drawable.ic_menu_info_details);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case OPTIONS_MENU_SEARCH:
            startActivity(new Intent(this, MovieSearch.class));
            break;
         case OPTIONS_MENU_CATMGR:
            startActivity(new Intent(this, CategoryManager.class));
            break;
         case OPTIONS_MENU_PREFS:
            startActivity(new Intent(this, Preferences.class));
            break;
         case OPTIONS_MENU_ABOUT:
            AlertDialog dialog =
                     new AlertDialog.Builder(MyMovies.this).setTitle("About MyMovies").setMessage(aboutString)
                              .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                 public void onClick(final DialogInterface d, final int i) {
                                 }
                              }).create();
            dialog.show();
            // make the Linkify'ed aboutString clickable
            ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            break;
      }
      return false;
   }

   @Override
   public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      menu.add(0, MyMovies.CONTEXT_MENU_DELETE, 0, "Delete Movie");
      menu.setHeaderTitle("Action");
   }

   @Override
   public boolean onContextItemSelected(final MenuItem item) {
      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
      final Movie movie = movies.get(info.position);
      switch (item.getItemId()) {
         case CONTEXT_MENU_DELETE:
            new AlertDialog.Builder(MyMovies.this).setTitle("Delete Movie?").setMessage(movie.getName())
                     .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface d, final int i) {
                           if (app.getDataManager().deleteMovie(movie.getId())) {
                              adapter.remove(movie);
                           } else {
                              // should never get this, but just in case
                              Toast.makeText(MyMovies.this, "Unable to delete movie, please check logs",
                                       Toast.LENGTH_SHORT).show();
                           }
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