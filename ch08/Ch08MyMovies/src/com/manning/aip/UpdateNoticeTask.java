package com.manning.aip;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UpdateNoticeTask extends AsyncTask<String, Void, String> {

   private HttpURLConnection connection;

   private Handler handler;

   public UpdateNoticeTask(Handler handler) {
      this.handler = handler;
   }

   @Override
   protected String doInBackground(String... params) {
      try {
         URL url = new URL(params[0]);
         connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("GET");
         InputStreamReader isr =
                  new InputStreamReader(connection.getInputStream());
         BufferedReader br = new BufferedReader(isr);

         StringBuilder sb = new StringBuilder();
         String line = br.readLine();
         while (line != null) {
            sb.append(line + "\n");
            line = br.readLine();
         }
         connection.disconnect();
         return sb.toString();
      } catch (Exception e) {
         return "Error: " + e.getMessage();
      }
   }

   @Override
   protected void onPostExecute(String updateNotice) {
      Message message = new Message();
      Bundle data = new Bundle();
      data.putString("text", updateNotice);
      message.setData(data);
      handler.sendMessage(message);
   }
}
