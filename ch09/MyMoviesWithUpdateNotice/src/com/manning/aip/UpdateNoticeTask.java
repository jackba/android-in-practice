package com.manning.aip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UpdateNoticeTask extends AsyncTask<Void, Void, String> {

   private static final String UPDATE_URL =
            "http://android-in-practice.googlecode.com/files/update_notice.txt";

   private HttpURLConnection connection;

   private Handler handler;

   public UpdateNoticeTask(Handler handler) {
      this.handler = handler;
   }

   @Override
   protected String doInBackground(Void... params) {
      try {
         URL url = new URL(UPDATE_URL);
         connection = (HttpURLConnection) url.openConnection();
         connection.setRequestMethod("GET");
         connection.setRequestProperty("Accept", "text/plain");
         connection.setReadTimeout(10);
         connection.setConnectTimeout(10);
         connection.connect();
         int statusCode = connection.getResponseCode();
         if (statusCode != HttpURLConnection.HTTP_OK) {
            return "Error: Failed getting update notes";
         }
         return readTextFromServer();
      } catch (Exception e) {
         return "Error: " + e.getMessage();
      } finally {
         if (connection != null) {
            connection.disconnect();
         }
      }
   }

   private String readTextFromServer() throws IOException {
      InputStreamReader isr =
               new InputStreamReader(connection.getInputStream());
      BufferedReader br = new BufferedReader(isr);

      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      while (line != null) {
         sb.append(line + "\n");
         line = br.readLine();
      }
      return sb.toString();
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
