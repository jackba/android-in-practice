package com.manning.aip.mymoviesdatabase;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.manning.aip.mymoviesdatabase.util.ImageCache;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTask extends AsyncTask<String, Void, Bitmap> {

   private final ImageCache cache;   
   private final int position;
   private final ImageView imageView;
   private final Drawable placeholder;

   // pass in the cache so we can populate it as we go
   public DownloadTask(ImageCache cache, int position, ImageView imageView) {
      this.cache = cache;
      this.position = position;
      this.imageView = imageView;
      Resources resources = imageView.getContext().getResources();
      this.placeholder = resources.getDrawable(android.R.drawable.gallery_thumb);
   }

   @Override
   protected void onPreExecute() {
      imageView.setImageDrawable(placeholder);
   }

   @Override
   protected Bitmap doInBackground(String... inputUrls) {
      Log.d(Constants.LOG_TAG, "making HTTP trip for image:" + inputUrls[0]);
      Bitmap bitmap = null;
      try {
         URL url = new URL(inputUrls[0]);
         bitmap = BitmapFactory.decodeStream(url.openStream());
         if (bitmap != null) {
            cache.put(inputUrls[0], bitmap);
         }
      } catch (MalformedURLException e) {
         Log.e(Constants.LOG_TAG, "Exception loading image, malformed URL", e);
      } catch (IOException e) {
         Log.e(Constants.LOG_TAG, "Exception loading image, IO error", e);
      }
      return bitmap;
   }

   @Override
   protected void onPostExecute(Bitmap result) {
      int forPosition = (Integer) imageView.getTag();
      if (forPosition == this.position) {
         if (result != null) {
            this.imageView.setImageBitmap(result);
         }
      }
   }
}
