package com.manning.aip;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

public class Worker extends AsyncTask<Void, Void, String> {

   private Activity context;

   public void connectContext(Activity context) {
      this.context = context;
   }

   public void disconnectContext() {
      this.context = null;
   }

   @Override
   protected String doInBackground(Void... params) {
      try {
         Thread.sleep(3000);
      } catch (InterruptedException e) {
      }
      return "Work done!";
   }

   @Override
   protected void onPostExecute(String result) {
      if (context != null) {
         Toast.makeText(context, result, Toast.LENGTH_LONG).show();
      }
   }
}
