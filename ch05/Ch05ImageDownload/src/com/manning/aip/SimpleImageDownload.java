package com.manning.aip;

import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SimpleImageDownload extends Activity {

   private Runnable imageDownloader = new Runnable() {

      public void run() {
         try {
            URL imageUrl = new URL("http", "android.com", "/images/froyo.png");
            Bitmap image = BitmapFactory.decodeStream(imageUrl.openStream());
            if (image != null) {
               Log.i("DL", "Successfully retrieved file!");
            } else {
               Log.i("DL", "Failed decoding file from stream");
            }
         } catch (Exception e) {
            Log.i("DL", "Failed downloading file!");
            e.printStackTrace();
         }
      }
   };

   public void startDownload(View source) {
      new Thread(imageDownloader, "Download thread").start();
      TextView statusText = (TextView) findViewById(R.id.status);
      statusText.setText("Download started...");
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
   }
}