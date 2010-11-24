package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.manning.aip.mymoviesdatabase.model.Movie;

public class MovieForm extends Activity {

   private EditText name;
   private EditText year;
   private RatingBar rating;
   private Button submit;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.movie_form);

      this.name = (EditText) this.findViewById(R.id.name);
      this.year = (EditText) this.findViewById(R.id.year);
      this.rating = (RatingBar) this.findViewById(R.id.rating);

      this.submit = (Button) this.findViewById(R.id.submit);
      this.submit.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            if (!isTextViewEmpty(name)) {
               Movie movie = new Movie();
               movie.setName(name.getText().toString());
               if (year.getText() != null && !year.getText().toString().equals("")) {
                  movie.setYear(Integer.valueOf(year.getText().toString()));
               }
               movie.setRating(rating.getNumStars());

               Toast.makeText(MovieForm.this, "Movie: " + movie, Toast.LENGTH_LONG).show();
            } else {
               Toast.makeText(MovieForm.this, "Name (minimum) required", Toast.LENGTH_LONG).show();
            }
         }
      });
   }

   private boolean isTextViewEmpty(final TextView textView) {
      return !(textView != null && textView.getText() != null && textView.getText().toString() != null
               && !textView.getText().toString().equals(""));
   }
}
