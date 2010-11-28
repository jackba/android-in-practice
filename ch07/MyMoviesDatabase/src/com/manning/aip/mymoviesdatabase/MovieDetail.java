package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.manning.aip.mymoviesdatabase.model.Category;
import com.manning.aip.mymoviesdatabase.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieDetail extends Activity {

   public static final String MOVIE_ID_KEY = "midkey";

   private MyMoviesApp app;
   
   private Movie movie;

   private TextView name;
   private TextView year;
   private ImageView thumb;
   private TextView tagline;
   private RatingBar rating;
   
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

      thumb = (ImageView) findViewById(R.id.movie_detail_thumb);

      tagline = (TextView) findViewById(R.id.movie_detail_tagline);

      rating = (RatingBar) findViewById(R.id.movie_detail_rating);

      categoriesListView = (ListView) findViewById(R.id.movie_detail_category_list);
      categoriesListView.setEmptyView(findViewById(R.id.movie_detail_category_list_empty));
      categories = new ArrayList<Category>();
      adapter = new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_1, categories);
      categoriesListView.setAdapter(adapter);

      Intent intent = this.getIntent();
      long movieId = intent.getLongExtra(MOVIE_ID_KEY, 0);
      movie = app.getDataManager().getMovieDao().get(movieId);
      if (movie != null) {
         this.populateViews();
      } else {
         Toast.makeText(this, "No movie found, nothing to see here", Toast.LENGTH_LONG).show();
      }
   }
   
   private void populateViews() {
      name.setText(movie.getName());
      year.setText(String.valueOf(movie.getYear()));
      
      String thumbUrl = movie.getThumbUrl();
      if (thumbUrl != null && !thumbUrl.equals("")) {         
         if (app.getImageCache().get(thumbUrl) == null) {
            new DownloadTask(app.getImageCache(), thumb).execute(thumbUrl);         
         } else {
            thumb.setImageBitmap(app.getImageCache().get(thumbUrl));
         }
      }  
      
      tagline.setText(movie.getTagline());
      
      // TODO rating.setNumStars(movie.getRating());
      
      categories.addAll(movie.getCategories());
      adapter.notifyDataSetChanged();      
   }

}
