package com.manning.aip.dealdroid;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.manning.aip.dealdroid.model.Item;

import java.util.ArrayList;
import java.util.List;

// Use IntentService which will queue each call to startService(Intent) through onHandleIntent and then shutdown
//
// NOTE that this implementation intentionally doesn't use PowerManager/WakeLock or deal with power issues
// (if the device is asleep, AlarmManager wakes up for BroadcastReceiver onReceive, but then might sleep again)
// (can use PowerManager and obtain WakeLock here, but STILL might not work, there is a gap)
// (this can be mitigated but for this example this complication is not needed)
// (it's not critical if user doesn't see new deals until phone is awake and notification is sent, both)
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
      if (app.sectionList != null && !app.sectionList.isEmpty()) {
         List<Item> previousDeals = app.sectionList.get(0).items;
         app.sectionList = app.parser.parse();
         List<Item> newDealsList = this.checkForNewDeals(previousDeals, app.sectionList.get(0).items);
         if (!newDealsList.isEmpty()) {
            this.sendNotification(this, newDealsList);
         }
      } else {
         Log.w(Constants.LOG_TAG, "DealDroidApp setionList null or empty, parsing may have failed");
      }

      // uncomment to force notification, new deals or not
      /*
      count++;
      if (count == 1) {
         SystemClock.sleep(5000);
         this.sendNotification(this, 1);
      }
      */
   }

   private List<Item> checkForNewDeals(final List<Item> previousDeals, final List<Item> currentDeals) {
      List<Item> newDealsList = new ArrayList<Item>();
      for (Item item : currentDeals) {
         if (!previousDeals.contains(item)) {
            Log.d(Constants.LOG_TAG, "New deal found: " + item.title);
            newDealsList.add(item);
         }
      }
      return newDealsList;
   }

   private void sendNotification(final Context context, final List<Item> newDealsList) {
      Intent notificationIntent = new Intent(context, DealList.class);
      PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

      NotificationManager notificationMgr =
               (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      Notification notification =
               new Notification(android.R.drawable.star_on, getString(R.string.deal_service_ticker) + " "
                        + getNewDealsString(newDealsList), System.currentTimeMillis());
      notification.flags |= Notification.FLAG_AUTO_CANCEL;
      notification.setLatestEventInfo(context, getResources().getString(R.string.deal_service_title), getResources()
               .getQuantityString(R.plurals.deal_service_new_deal, newDealsList.size(), newDealsList.size()),
               contentIntent);
      notificationMgr.notify(0, notification);
   }

   private String getNewDealsString(final List<Item> newDealsList) {
      StringBuilder sb = new StringBuilder();
      for (int i = 1; i <= newDealsList.size(); i++) {
         Item item = newDealsList.get(i - 1);
         sb.append(i + "." + item.title);
         if (i != newDealsList.size()) {
            sb.append(" ");
         }
      }
      return sb.toString();
   }
}
