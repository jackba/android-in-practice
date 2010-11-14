package com.manning.aip;

import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

public class ImageDownloadWithMessagePassing extends Activity implements
         Handler.Callback {

   private Handler handler = new Handler(this);

   private Runnable imageDownloader = new Runnable() {

      private void sendMessage(String what) {
         Bundle bundle = new Bundle();
         bundle.putString("status", what);
         Message message = new Message();
         message.setData(bundle);
         handler.sendMessage(message);
      }

      public void run() {
         sendMessage("Download started");
         try {
            URL imageUrl = new URL("http://www.android.com/images/froyo.png");
            Bitmap image = BitmapFactory.decodeStream(imageUrl.openStream());
            if (image != null) {
               sendMessage("Successfully retrieved file!");
            } else {
               sendMessage("Failed decoding file from stream");
            }
         } catch (Exception e) {
            sendMessage("Failed downloading file!");
            e.printStackTrace();
         }
      }
   };

   public void startDownload(View source) {
      new Thread(imageDownloader, "Download thread").start();
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
   }

   public boolean handleMessage(Message msg) {
      String text = msg.getData().getString("status");
      TextView statusText = (TextView) findViewById(R.id.status);
      statusText.setText(text);
      return true;
   }
}