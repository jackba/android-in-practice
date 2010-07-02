package com.manning.aip;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImageHandler extends Handler {

   private int position;

   private ImageView imageView;

   public ImageHandler(int position, ImageView imageView) {
      this.position = position;
      this.imageView = imageView;
   }

   @Override
   public void handleMessage(Message msg) {
      int forPosition = (Integer) imageView.getTag();
      if (forPosition != this.position) {
         return;
      }
      Bitmap image = msg.getData().getParcelable("image");
      imageView.setImageBitmap(image);
   }
}
