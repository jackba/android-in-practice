package com.manning.aip.mymoviesfile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {

   // NOT currently used here, one way to check net state though, register a receiver
   // then startActivity here in onRecieve and do something
   
   @Override
   public void onReceive(Context context, Intent intent) {  
      Log.d(Constants.LOG_TAG, "network connectivity change action: " + intent.getAction());
   }
}