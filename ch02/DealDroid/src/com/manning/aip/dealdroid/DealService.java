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
import com.manning.aip.dealdroid.xml.DailyDealsFeedParser;
import com.manning.aip.dealdroid.xml.DailyDealsXmlPullFeedParser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DealService extends Service {

   private NotificationManager notificationMgr;

   private Timer timer;
   private LinkedHashMap<Long, Item> deals;
   private DailyDealsFeedParser feed;

   @Override
   public IBinder onBind(Intent intent) {
      return null;
   }

   @Override
   public void onCreate() {
      this.notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

      this.timer = new Timer();
      this.deals = new LinkedHashMap<Long, Item>(4);
      this.feed = new DailyDealsXmlPullFeedParser("http://deals.ebay.com/feeds/xml");

      timer.scheduleAtFixedRate(new TimerTask() {
         @Override
         public void run() {
            int newDeals = 0;
            try {
               ArrayList<Section> sections = feed.parse();
               ArrayList<Item> items = sections.get(0).items;

               int currentSize = deals.size();
               for (Item item : items) {
                  if (!deals.containsKey(item.itemId) && currentSize > 0) {
                     newDeals++;
                  }
               }
               deals.clear();
               for (Item item : items) {
                  deals.put(item.itemId, item);
               }
            } catch (Exception e) {
               Log.e("DealService", "Exception from Deals feed", e);
            }
            if (newDeals > 0) {
               sendNotification(newDeals);
            }
            Log.i(Constants.LOG_TAG, "DealService ran and found " + newDeals + " new deals");
         }
      }, 1000L, 5000L);
      // NOTE - this is for example purposes, DON'T DO THIS OUTSIDE OF A DEMO
      // services should use AlarmManager, and wake up when needed, not run constantly
      // and certainly not poll as often as 5 seconds
   }

   private void sendNotification(int newDeals) {
      Context appCtx = getApplicationContext();
      Notification notification =
               new Notification(android.R.drawable.ic_dialog_alert, "deals_service_ticker", System.currentTimeMillis());

      Intent intent = new Intent(appCtx, DealList.class);
      PendingIntent pending = PendingIntent.getActivity(this, 0, intent, 0);

      String detailMsg = String.format(getString(R.string.deal_service_new_deal), newDeals);
      notification.setLatestEventInfo(appCtx, "deals service title", detailMsg, pending);
      notificationMgr.notify(0, notification);
   }
}
