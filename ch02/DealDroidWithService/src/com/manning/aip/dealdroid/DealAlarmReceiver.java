package com.manning.aip.dealdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DealAlarmReceiver extends BroadcastReceiver {

   // onReceive must be very quick and not block, so it just fires up a Service
   @Override
   public void onReceive(Context context, Intent intent) {
      Log.i(Constants.LOG_TAG, "DealAlarmReceiver invoked, starting DealService in background");
      context.startService(new Intent(context, DealService.class));
   }
}
