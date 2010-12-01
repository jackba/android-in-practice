package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.manning.aip.mymoviesdatabase.model.Category;
import com.manning.aip.mymoviesdatabase.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDetail extends Activity {

   public static final String MOVIE_ID_KEY = "midkey";

   private static final int OPTIONS_MENU_HOMEPAGE = 0;
   private static final int OPTIONS_MENU_TRAILER = 1;

   private MyMoviesApp app;

   private Movie movie;

   private TextView name;
   private TextView year;
   private ImageView image;
   private TextView tagline;
   private TextView rating;

   private ListView categoriesListView;
   private ArrayAdapter<Category> adapter;
   private List<Category> categories;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.movie_detail);

      app = (MyMoviesApp) getApplication();

      name = (TextView) findViewById(R.id.movie_detail_name);
      year = (TextView) findViewById(R.id.movie_detail_year);
      image = (ImageView) findViewById(R.id.movie_detail_image);
      tagline = (TextView) findViewById(R.id.movie_detail_tagline);
      rating = (TextView) findViewById(R.id.movie_detail_rating);

      categoriesListView = (ListView) findViewById(R.id.movie_detail_category_list);
      categoriesListView.setEmptyView(findViewById(R.id.movie_detail_category_list_empty));
      categories = new ArrayList<Category>();
      adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categories);
      categoriesListView.setAdapter(adapter);

      Intent intent = this.getIntent();
      long movieId = intent.getLongExtra(MOVIE_ID_KEY, 0);
      movie = app.getDataManager().getMovie(movieId);
      Log.d(Constants.LOG_TAG, "MOVIE: " + movie);
      if (movie != null) {
         this.populateViews();
      } else {
         Toast.makeText(this, "No movie found, nothing to see here", Toast.LENGTH_LONG).show();
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, OPTIONS_MENU_HOMEPAGE, 0, "Homepage").setIcon(android.R.drawable.ic_menu_info_details);
      menu.add(0, OPTIONS_MENU_TRAILER, 0, "Trailer").setIcon(android.R.drawable.ic_menu_view);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case OPTIONS_MENU_HOMEPAGE:
            if (movie.getHomepage() != null && !movie.getHomepage().equals("")) {
               startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getHomepage())));
            } else {
               Toast.makeText(this, "Homepage not available", Toast.LENGTH_SHORT).show();
            }
            break;
         case OPTIONS_MENU_TRAILER:
            if (movie.getTrailer() != null && !movie.getTrailer().equals("")) {
               startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movie.getTrailer())));
            } else {
               Toast.makeText(this, "Trailer not available", Toast.LENGTH_SHORT).show();
            }
            break;
      }
      return false;
   }

   private void populateViews() {
      name.setText(movie.getName());
      year.setText(String.valueOf(movie.getYear()));

      String thumbUrl = movie.getThumbUrl();
      if (thumbUrl != null && !thumbUrl.equals("")) {
         if (app.getImageCache().get(thumbUrl) == null) {
            new DownloadTask(app.getImageCache(), image).execute(thumbUrl);
         } else {
            image.setImageBitmap(app.getImageCache().get(thumbUrl));
         }
      }

      tagline.setText(movie.getTagline());

      rating.setText("Rating: " + String.valueOf(movie.getRating()));

      categories.addAll(movie.getCategories());
      adapter.notifyDataSetChanged();
   }
}
