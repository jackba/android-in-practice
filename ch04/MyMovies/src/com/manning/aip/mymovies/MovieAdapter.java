package com.manning.aip.mymovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.HashMap;

public class MovieAdapter extends ArrayAdapter<String> {

   private HashMap<Integer, Boolean> movieCollection =
            new HashMap<Integer, Boolean>();

   public MovieAdapter(Context context) {
      super(context, R.layout.movie_item, android.R.id.text1, context
               .getResources().getStringArray(R.array.movies));
   }

   public void toggleMovie(int position) {
      if (!isInCollection(position)) {
         movieCollection.put(position, true);
      } else {
         movieCollection.put(position, false);
      }
   }

   public boolean isInCollection(int position) {
      return movieCollection.get(position) == Boolean.TRUE;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {

      // View listItem = inflater.inflate(R.layout.movie_item, null);
      View listItem = super.getView(position, convertView, parent);

      CheckedTextView checkMark =
               (CheckedTextView) listItem.findViewById(android.R.id.text1);
      checkMark.setChecked(isInCollection(position));

      // TextView movieRank = (TextView)
      // listItem.findViewById(R.id.movie_rank);
      // movieRank.setText(Integer.toString(position + 1));

      return listItem;
   }
}
