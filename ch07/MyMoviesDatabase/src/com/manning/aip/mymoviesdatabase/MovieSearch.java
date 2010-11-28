package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.manning.aip.mymoviesdatabase.model.Movie;
import com.manning.aip.mymoviesdatabase.model.MovieSearchResult;
import com.manning.aip.mymoviesdatabase.xml.MovieFeed;
import com.manning.aip.mymoviesdatabase.xml.TheMovieDBXmlPullFeedParser;

import java.util.ArrayList;
import java.util.List;

public class MovieSearch extends Activity {

   private MovieFeed parser;
   private List<MovieSearchResult> movies;
   private ArrayAdapter<MovieSearchResult> adapter;

   private EditText input;
   private Button search;
   private ListView listView;

   private MyMoviesApp app;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      // TODO form and use parser
      super.onCreate(savedInstanceState);
      setContentView(R.layout.movie_search);

      app = (MyMoviesApp) getApplication();

      parser = new TheMovieDBXmlPullFeedParser();

      input = (EditText) findViewById(R.id.input);
      search = (Button) findViewById(R.id.search);
      search.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            if (input.getText() != null && input.getText().toString() != null) {
               // TODO parse from AsyncTask (and check net avail)
               movies.clear();
               movies.addAll(parser.search(input.getText().toString()));
               Log.d(Constants.LOG_TAG, " movies size after parse: " + movies.size());
               adapter.notifyDataSetChanged();
            } else {
               Toast.makeText(MovieSearch.this, "Search term required", Toast.LENGTH_SHORT).show();
            }
         }
      });

      movies = new ArrayList<MovieSearchResult>();
      listView = (ListView) findViewById(R.id.searchresults);
      listView.setEmptyView(findViewById(R.id.empty));
      adapter = new ArrayAdapter<MovieSearchResult>(this, android.R.layout.simple_list_item_1, movies);
      listView.setAdapter(adapter);
      listView.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(final AdapterView<?> parent, final View v, final int index, final long id) {
            final MovieSearchResult movieSearchResult = movies.get(index);
            // TODO parse from AsyncTask (and check net avail)
            final Movie movie = parser.get(movieSearchResult.getProviderId());
            if (movie != null) {
               new AlertDialog.Builder(MovieSearch.this).setTitle("Add Movie?").setMessage(movie.toString())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           public void onClick(final DialogInterface d, final int i) {
                              // TODO check if movie already exists
                              Movie exists = app.getDataManager().getMovieDao().find(movie.getName());
                              if (exists == null) {
                                 app.getDataManager().getMovieDao().save(movie);
                                 startActivity(new Intent(MovieSearch.this, MyMovies.class));
                              } else {
                                 Toast.makeText(MovieSearch.this, "Movie already saved", Toast.LENGTH_SHORT).show();
                              }
                           }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                           public void onClick(final DialogInterface d, final int i) {
                           }
                        }).show();
            } else {
               Toast.makeText(MovieSearch.this, "Problem parsing movie, no result, please try again later",
                        Toast.LENGTH_SHORT).show();
            }
         }
      });
   }
}
