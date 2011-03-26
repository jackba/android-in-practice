package com.manning.aip.mymoviesdatabase;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.manning.aip.mymoviesdatabase.util.ImageCache;

public class MovieCursorAdapter extends CursorAdapter {

   private final ImageCache cache;
   private final LayoutInflater vi;

   public MovieCursorAdapter(Context context, ImageCache cache, Cursor c) {
      super(context, c, true);
      this.cache = cache;
      this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }

   @Override
   public void bindView(final View v, final Context context, final Cursor c) {
      populateView(v, c);
   }

   @Override
   public View newView(final Context context, final Cursor c, final ViewGroup parent) {
      View listItem = vi.inflate(R.layout.movie_item, parent, false);
      TextView text = (TextView) listItem.findViewById(android.R.id.text1);
      ImageView image = (ImageView) listItem.findViewById(R.id.movie_icon);
      ViewHolder holder = new ViewHolder(text, image);
      listItem.setTag(holder);
      populateView(listItem, c);
      return listItem;
   }

   private void populateView(final View listItem, final Cursor c) {
      ViewHolder holder = (ViewHolder) listItem.getTag();
      if ((c != null) && !c.isClosed()) {
         long id = c.getLong(0);
         String name = c.getString(1);
         String thumbUrl = c.getString(2);

         holder.text.setText(name);

         holder.image.setImageDrawable(null);
         holder.image.setTag(id);         
         if ((thumbUrl != null) && !thumbUrl.equals("")) {
            if (cache.get(thumbUrl) == null) {
               new DownloadListViewTask(cache, id, holder.image).execute(thumbUrl);
            } else {
               holder.image.setImageBitmap(cache.get(thumbUrl));
            }
         }
      }
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
