package com.manning.aip.dealdroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import java.util.ArrayList;

public class DealService extends Service {

   private DealDroidApp app;

   @Override
   public IBinder onBind(Intent intent) {
      return null;
   }

   @Override
   public void onCreate() {

   }

   // See http://developer.android.com/intl/de/reference/android/app/Service.html#onStartCommand
   // AND http://android-developers.blogspot.com/2010/02/service-api-changes-starting-with.html

   // This is the old onStart method that will be called on the pre-2.0
   // platform.  On 2.0 or later we override onStartCommand() so this
   // method will not be called.
   @Override
   public void onStart(Intent intent, int startId) {
      handleCommand(intent);
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      handleCommand(intent);
      // We want this service to continue running until it is explicitly
      // stopped, so return sticky.
      return Service.START_NOT_STICKY;
   }

   private void handleCommand(Intent intent) {
      Log.i(Constants.LOG_TAG, "DealService invoked, checking for new deals (will notify if present)");
      this.app = (DealDroidApp) getApplication();
      int newDeals = this.checkForNewDeals();
      if (newDeals > 0) {
         this.sendNotification(this, newDeals);
      }
      this.stopSelf();
   }

   private int checkForNewDeals() {
      int newDeals = 0;
      try {
         ArrayList<Section> sections = app.parser.parse();
         ArrayList<Item> items = sections.get(0).items;
         int currentSize = app.deals.size();
         for (Item item : items) {
            if (!app.deals.containsKey(item.itemId) && (currentSize > 0)) {
               newDeals++;
            }
         }
         app.deals.clear();
         for (Item item : items) {
            app.deals.put(item.itemId, item);
         }
      } catch (Exception e) {
         Log.e("DealService", "Exception from Deals feed", e);
      }
      return newDeals;
   }

   private void sendNotification(final Context context, final int newDeals) {

      NotificationManager notificationMgr =
               (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      Notification notification =
               new Notification(android.R.drawable.ic_dialog_alert, getString(R.string.deal_service_ticker), System
                        .currentTimeMillis());

      PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, DealList.class), 0);

      String detailMsg = String.format(context.getString(R.string.deal_service_new_deal), newDeals);
      notification.setLatestEventInfo(context, getString(R.string.deal_service_title), detailMsg, pendingIntent);
      notificationMgr.notify(0, notification);
   }
}
