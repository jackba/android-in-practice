package com.manning.aip;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UpdateNoticeTask extends AsyncTask<Void, Void, String> {

   private String updateUrl =
            "http://android-in-practice.googlecode.com/files/update_notice.txt";

   private Handler handler;

   private HttpClient httpClient;

   public UpdateNoticeTask(Handler handler) {
      this.handler = handler;
      setupHttpClient();
   }

   private void setupHttpClient() {
      SchemeRegistry schemeRegistry = new SchemeRegistry();
      schemeRegistry.register(new Scheme("http", PlainSocketFactory
               .getSocketFactory(), 80));

      HttpParams connManagerParams = new BasicHttpParams();
      ConnManagerParams.setMaxTotalConnections(connManagerParams, 5);
      ConnManagerParams.setMaxConnectionsPerRoute(connManagerParams,
               new ConnPerRouteBean(5));
      ConnManagerParams.setTimeout(connManagerParams, 15 * 1000);

      ThreadSafeClientConnManager cm =
               new ThreadSafeClientConnManager(connManagerParams,
                        schemeRegistry);

      HttpParams clientParams = new BasicHttpParams();
      HttpProtocolParams.setUserAgent(clientParams, "MyMovies/1.0");
      HttpConnectionParams.setConnectionTimeout(clientParams, 15 * 1000);
      HttpConnectionParams.setSoTimeout(clientParams, 15 * 1000);
      httpClient = new DefaultHttpClient(cm, clientParams);
   }

   @Override
   protected String doInBackground(Void... params) {
      try {
         HttpGet request = new HttpGet(updateUrl);
         request.setHeader("Accept", "text/plain");
         HttpResponse response = httpClient.execute(request);
         int statusCode = response.getStatusLine().getStatusCode();
         if (statusCode != HttpStatus.SC_OK) {
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
