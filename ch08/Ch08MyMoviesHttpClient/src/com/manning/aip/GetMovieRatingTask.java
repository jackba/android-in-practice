package com.manning.aip;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.manning.aip.xml.SAXMovieParser;

public class GetMovieRatingTask extends AsyncTask<String, Void, Movie> {

   private static final String API_KEY = "624645327f33f7866355b7b728f9cd98";

   private static final String API_ENDPOINT = "http://api.themoviedb.org/2.1";

   private Activity activity;

   public GetMovieRatingTask(Activity activity) {
      this.activity = activity;
   }

   @Override
   protected Movie doInBackground(String... params) {
      try {
         String imdbId = params[0];
         HttpClient httpClient = MyMovies.getHttpClient();
         String path = "/Movie.imdbLookup/en/xml/" + API_KEY + "/" + imdbId;
         HttpGet request = new HttpGet(API_ENDPOINT + path);

         HttpResponse response = httpClient.execute(request);

         return SAXMovieParser.parseMovie(response.getEntity().getContent());
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   protected void onPostExecute(Movie movie) {
      if (movie == null) {
         Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show();
      }
      Dialog dialog = new Dialog(activity);
      dialog.setContentView(R.layout.movie_dialog);

      dialog.setTitle("IMDb rating for \"" + movie.getTitle() + "\"");

      TextView rating =
               (TextView) dialog.findViewById(R.id.movie_dialog_rating);
      rating.setText(movie.getRating());

      dialog.show();
   }
}
