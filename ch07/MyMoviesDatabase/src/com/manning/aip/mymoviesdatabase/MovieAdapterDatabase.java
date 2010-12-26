package com.manning.aip.mymoviesdatabase;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.manning.aip.mymoviesdatabase.model.Movie;
import com.manning.aip.mymoviesdatabase.util.ImageCache;

import java.util.List;

public class MovieAdapterDatabase extends ArrayAdapter<Movie> {

   private final ImageCache cache;

   public MovieAdapterDatabase(Context context, ImageCache cache, List<Movie> movies) {
      super(context, R.layout.movie_item, android.R.id.text1, movies);
      this.cache = cache;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      View listItem = super.getView(position, convertView, parent);

      TextView text = null;
      ImageView image = null;
      ViewHolder holder = (ViewHolder) listItem.getTag();
      if (holder != null) {
         text = holder.text;
         image = holder.image;
      } else {
         text = (TextView) listItem.findViewById(android.R.id.text1);
         image = (ImageView) listItem.findViewById(R.id.movie_icon);
         holder = new ViewHolder(text, image);
         listItem.setTag(holder);
      }

      Movie movie = this.getItem(position);

      text.setText(movie.getName());

      image.setImageDrawable(null);
      image.setTag(position);
      String thumbUrl = movie.getThumbUrl();
      if ((thumbUrl != null) && !thumbUrl.equals("")) {
         if (cache.get(thumbUrl) == null) {
            new DownloadListViewTask(cache, position, image).execute(thumbUrl);
         } else {
            image.setImageBitmap(cache.get(thumbUrl));
         }
      }

      return listItem;
   }

   private static class ViewHolder {
      protected final TextView text;
      protected final ImageView image;

      public ViewHolder(TextView text, ImageView image) {
         this.text = text;
         this.image = image;
      }
   }
}
