package com.manning.aip.dealdroid;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.manning.aip.dealdroid.model.Item;
import com.manning.aip.dealdroid.model.Section;

import java.util.ArrayList;

// Use IntentService which will queue each call to startService(Intent) through onHandleIntent and then shutdown
//
// NOTE that this implementation intentionally doesn't use PowerManager/WakeLock or deal with power issues
// (if the device is asleep, AlarmManager wakes up for BroadcastReceiver onReceive, but then might sleep again)
// (can use PowerManager and obtain WakeLock here, but STILL might not work, there is a gap)
// (this can be mitigated but it is very complicated and beyond the scope of this example)
public class DealService extends IntentService {

   private DealDroidApp app;

   public DealService() {
      super("Deal Service");
   }

   @Override
   public void onStart(Intent intent, int startId) {
      super.onStart(intent, startId);
   }

   @Override
   public void onHandleIntent(Intent intent) {
      Log.i(Constants.LOG_TAG, "DealService invoked, checking for new deals (will notify if present)");
      this.app = (DealDroidApp) getApplication();
      int newDeals = this.checkForNewDeals();
      if (newDeals > 0) {
         this.sendNotification(this, newDeals);
      }
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
