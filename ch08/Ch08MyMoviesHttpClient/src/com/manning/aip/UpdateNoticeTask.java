package com.manning.aip;

import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UpdateNoticeTask extends AsyncTask<String, Void, String> {

   private Handler handler;

   private HttpClient httpClient = new DefaultHttpClient();

   public UpdateNoticeTask(Handler handler) {
      this.handler = handler;
   }

   @Override
   protected String doInBackground(String... params) {
      try {
         HttpGet request = new HttpGet(params[0]);
         request.setHeader("Accept", "text/plain");
         HttpResponse response = httpClient.execute(request);
         int statusCode = response.getStatusLine().getStatusCode();
         if (statusCode != HttpURLConnection.HTTP_OK) {
            return "Error: Failed getting update notes";
         }
         return EntityUtils.toString(response.getEntity());
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
