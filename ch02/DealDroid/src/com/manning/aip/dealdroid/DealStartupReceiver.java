package com.manning.aip.dealdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DealStartupReceiver extends BroadcastReceiver {
   @Override
   public void onReceive(Context context, Intent intent) {
      Log.i(Constants.LOG_TAG, "DealStartupReceiver invoked, starting DealService in background");
      Intent dealsService = new Intent(context, DealService.class);
      context.startService(dealsService);
   }
}
