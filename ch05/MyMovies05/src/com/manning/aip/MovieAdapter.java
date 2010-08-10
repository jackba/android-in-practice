package com.manning.aip;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

public class MovieAdapter extends ArrayAdapter<String> {

   private HashMap<Integer, Boolean> movieCollection =
            new HashMap<Integer, Boolean>();

   private String[] movieIconUrls;

   private ThreadPoolExecutor executor;

   public MovieAdapter(Context context) {
      super(context, R.layout.movie_item, android.R.id.text1, context
               .getResources().getStringArray(R.array.movies));

      movieIconUrls =
               context.getResources().getStringArray(R.array.movie_thumbs);
      executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
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

   private void downloadImageForView(int position, ImageView imageView) {
      final Handler handler = new ImageHandler(position, imageView);
      final String imageUrl = movieIconUrls[position];
      executor.execute(new Runnable() {
         public void run() {
            try {
               URL url = new URL(imageUrl);
               Bitmap image = BitmapFactory.decodeStream(url.openStream());
               Bundle data = new Bundle();
               data.putParcelable("image", image);
               Message message = new Message();
               message.setData(data);
               handler.sendMessage(message);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      View listItem = super.getView(position, convertView, parent);

      CheckedTextView checkMark =
               (CheckedTextView) listItem.findViewById(android.R.id.text1);
      checkMark.setChecked(isInCollection(position));

      ImageView imageView = (ImageView) listItem.findViewById(R.id.movie_icon);
      imageView.setImageDrawable(null);
      imageView.setTag(position);
      downloadImageForView(position, imageView);

      return listItem;
   }
}
