package com.manning.aip;

import android.app.Activity;
import android.os.Bundle;

public class WorkerActivity extends Activity {

   private Worker worker;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      worker = (Worker) getLastNonConfigurationInstance();
      if (worker == null) {
         worker = new Worker();
         worker.execute();
      }
      worker.connectContext(this);
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
      worker.disconnectContext();
   }

   @Override
   public Object onRetainNonConfigurationInstance() {
      return worker;
   }
}