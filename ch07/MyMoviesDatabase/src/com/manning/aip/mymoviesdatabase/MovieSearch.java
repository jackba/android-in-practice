package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.manning.aip.mymoviesdatabase.model.Movie;
import com.manning.aip.mymoviesdatabase.model.MovieSearchResult;
import com.manning.aip.mymoviesdatabase.xml.MovieFeed;
import com.manning.aip.mymoviesdatabase.xml.TheMovieDBXmlPullFeedParser;

import java.util.ArrayList;
import java.util.List;

public class MovieSearch extends Activity {

   private MyMoviesApp app;

   private MovieFeed parser;
   private List<MovieSearchResult> movies;
   private ArrayAdapter<MovieSearchResult> adapter;

   private EditText input;
   private Button search;
   private ListView listView;

   private ProgressDialog progressDialog;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.movie_search);

      app = (MyMoviesApp) getApplication();

      parser = new TheMovieDBXmlPullFeedParser();

      progressDialog = new ProgressDialog(this);
      progressDialog.setIndeterminate(true);
      progressDialog.setCancelable(false);
      progressDialog.setMessage("Retrieving data...");

      input = (EditText) findViewById(R.id.search_input);
      search = (Button) findViewById(R.id.search_submit);
      search.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            if (!isTextViewEmpty(input)) {
               // do network trip/parsing in separate thread
               new ParseMovieSearchTask().execute(input.getText().toString());
            } else {
               Toast.makeText(MovieSearch.this, "Search term required", Toast.LENGTH_SHORT).show();
            }
         }
      });

      movies = new ArrayList<MovieSearchResult>();
      listView = (ListView) findViewById(R.id.search_results_list);
      listView.setEmptyView(findViewById(R.id.search_results_list_empty));
      adapter = new ArrayAdapter<MovieSearchResult>(this, android.R.layout.simple_list_item_1, movies);
      listView.setAdapter(adapter);
      listView.setOnItemClickListener(new OnItemClickListener() {
         public void onItemClick(final AdapterView<?> parent, final View v, final int index, final long id) {
            final MovieSearchResult movieSearchResult = movies.get(index);
            // do network trip/parsing in separate thread
            new ParseMovieTask().execute(movieSearchResult.getProviderId());
         }
      });
   }

   @Override
   protected void onPause() {
      if (progressDialog.isShowing()) {
         progressDialog.dismiss();
      }
      super.onPause();
   }

   private boolean isTextViewEmpty(final TextView textView) {
      return !((textView != null) && (textView.getText() != null) && (textView.getText().toString() != null) && !textView
               .getText().toString().equals(""));
   }

   private class ParseMovieSearchTask extends AsyncTask<String, Integer, List<MovieSearchResult>> {

      @Override
      protected void onPreExecute() {
         super.onPreExecute();
         if (!progressDialog.isShowing()) {
            progressDialog.show();
         }
      }

      @Override
      protected List<MovieSearchResult> doInBackground(String... args) {
         List<MovieSearchResult> moviesFromTask = parser.search(args[0]);
         return moviesFromTask;
      }

      @Override
      protected void onPostExecute(List<MovieSearchResult> moviesFromTask) {
         
         if (progressDialog.isShowing()) {
            progressDialog.hide();
         }
         
         movies.clear();
         movies.addAll(moviesFromTask);
         Log.d(Constants.LOG_TAG, " movies size after parse: " + movies.size());
         adapter.notifyDataSetChanged();
      }
   }

   private class ParseMovieTask extends AsyncTask<String, Integer, Movie> {

      @Override
      protected void onPreExecute() {
         super.onPreExecute();
         if (!progressDialog.isShowing()) {
            progressDialog.show();
         }
      }

      @Override
      protected Movie doInBackground(String... args) {
         Movie movie = parser.get(args[0]);
         return movie;
      }

      @Override
      protected void onPostExecute(final Movie movie) {

         if (progressDialog.isShowing()) {
            progressDialog.hide();
         }

         if (movie != null) {
            new AlertDialog.Builder(MovieSearch.this).setTitle("Add Movie?").setMessage(movie.getName())
                     .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface d, final int i) {
                           // let the activity check if movie exists, not manager
                           // (activity has contextual info to know the check is necessary)
                           Movie exists = app.getDataManager().findMovie(movie.getName());
                           if (exists == null) {
                              app.getDataManager().saveMovie(movie);
                              startActivity(new Intent(MovieSearch.this, MyMovies.class));
                           } else {
                              Toast.makeText(MovieSearch.this, "Movie already exists", Toast.LENGTH_SHORT).show();
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
   }
}
