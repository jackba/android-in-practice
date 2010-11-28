package com.manning.aip.mymoviesdatabase;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.manning.aip.mymoviesdatabase.util.ImageCache;

public class DownloadListViewTask extends DownloadTask {
   
   private final int position;

   // pass in the cache so we can populate it as we go
   public DownloadListViewTask(ImageCache cache, int position, ImageView imageView) {
      super(cache, imageView);
      this.position = position;
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
