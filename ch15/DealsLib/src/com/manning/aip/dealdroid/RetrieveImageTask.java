package com.manning.aip.dealdroid;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class RetrieveImageTask extends AsyncTask<String, Void, Bitmap>{
    private final ImageView imageView;
    private final DealsApp app;

    public RetrieveImageTask(final ImageView imageView, final DealsApp app) {
       this.imageView = imageView;
       this.app = app;
    }

    @Override
    protected Bitmap doInBackground(final String... args) {
       Bitmap bitmap = Util.retrieveBitmap(args[0]);
       return bitmap;
    }

    @Override
    protected void onPostExecute(final Bitmap bitmap) {
       if (bitmap != null) {
          imageView.setImageBitmap(bitmap);
          app.imageCache.put((Long) imageView.getTag(), bitmap);
          imageView.setTag(null);
       }
    }
}
