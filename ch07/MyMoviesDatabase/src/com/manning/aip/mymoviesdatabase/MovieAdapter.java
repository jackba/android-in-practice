package com.manning.aip.mymoviesdatabase;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.manning.aip.mymoviesdatabase.util.ImageCache;

import java.util.HashMap;

public class MovieAdapter extends ArrayAdapter<String> {

   private final ImageCache cache;   
   private final HashMap<Integer, Boolean> movieCollection;
   private final String[] movieIconUrls;
  
   public MovieAdapter(Context context, ImageCache cache) {
      super(context, R.layout.movie_item, android.R.id.text1, context.getResources().getStringArray(R.array.movies));
      this.cache = cache;
      this.movieCollection = new HashMap<Integer, Boolean>();
      this.movieIconUrls = context.getResources().getStringArray(R.array.movie_thumbs);
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
      View listItem = super.getView(position, convertView, parent);

      CheckedTextView checkMark = null;
      ImageView image = null;
      ViewHolder holder = (ViewHolder) listItem.getTag();
      if (holder != null) {
         checkMark = holder.checkMark;
         image = holder.image;
      } else {
         checkMark = (CheckedTextView) listItem.findViewById(android.R.id.text1);
         image = (ImageView) listItem.findViewById(R.id.movie_icon);
         holder = new ViewHolder(checkMark, image);
         listItem.setTag(holder);
      }      
      
      checkMark.setChecked(isInCollection(position));

      image.setImageDrawable(null);
      image.setTag(position);
      String imageUrl = this.movieIconUrls[position];
      if (cache.get(imageUrl) == null) {
         new DownloadTask(cache, position, image).execute(imageUrl);         
      } else {
         image.setImageBitmap(cache.get(imageUrl));
      }

      return listItem;
   }  
   
   private class ViewHolder {
      protected final CheckedTextView checkMark;
      protected final ImageView image;

      public ViewHolder(CheckedTextView checkMark, ImageView image) {
         this.checkMark = checkMark;
         this.image = image;
      }
   }
}
