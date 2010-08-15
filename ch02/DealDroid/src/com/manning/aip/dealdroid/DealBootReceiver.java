package com.manning.aip.dealdroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class DealBootReceiver extends BroadcastReceiver {

   // In real life, use AlarmManager.INTERVALs with longer periods of time 
   // this *short* interval is ONLY FOR DEMO/EXAMPLE purposes (don't do this, battery killer, and annoying)
   private static final long INTERVAL = 10000;

   private static final long TRIGGER_AT_TIME = SystemClock.elapsedRealtime() + 15000;

   @Override
   public void onReceive(Context context, Intent intent) {
      Log.i(Constants.LOG_TAG, "DealBootReceiver invoked, configuring AlarmManager");
      AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
      PendingIntent pendingIntent =
               PendingIntent.getBroadcast(context, 0, new Intent(context, DealAlarmReceiver.class), 0);
      // use inexact repeating which is easier on battery (system can phase events and not wake at exact times)
      alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, TRIGGER_AT_TIME, INTERVAL, pendingIntent);
   }
}
