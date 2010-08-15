package com.manning.aip.dealdroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.manning.aip.dealdroid.model.Item;

import java.util.LinkedHashMap;
import java.util.Timer;

public class DealsService extends Service {
   private final Timer timer = new Timer();
   private final LinkedHashMap<Long, Item> deals = new LinkedHashMap<Long, Item>(4);

   @Override
   public IBinder onBind(Intent intent) {
      return null;
   }

   @Override
   public void onCreate() {
      
      /*
      timer.scheduleAtFixedRate(new TimerTask() {
         @Override
         public void run() {
            int newDeals = 0;
            try {
               DailyDeals feed = DealsFeedParser.parseFeed();
               int currentSize = deals.size();
               for (Item item : feed) {
                  if (!deals.containsKey(item.getId()) && currentSize > 0) {
                     newDeals++;
                  }
               }
               deals.clear();
               for (Item item : feed) {
                  deals.put(item.getId(), item);
               }
            } catch (Exception e) {
               Log.e("DealsService", "Exception from Deals feed", e);
            }
            if (newDeals > 0) {
               sendNotification(newDeals);
            }
         }
      }, 1000L, 3000L);
      */
   }
   
   /*
   private void sendNotification(int newDeals) {
      Context appCtx = getApplicationContext();
      Notification notification =                                          
         new Notification(R.drawable.dd_icon, 
               getString(R.string.deals_service_ticker), 
               System.currentTimeMillis());
      NotificationManager mgr =                                            
         (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      Intent intent = new Intent(appCtx, DealList.class);                 
      PendingIntent pending = 
         PendingIntent.getActivity(this, 0, intent, 0);
      int resourceId = R.string.deals_service_one;
      switch(newDeals){                                                    
      case 2: 
         resourceId = R.string.deals_service_two;
         break;
      case 3:
         resourceId = R.string.deals_service_three;
         break;
      case 4:
         resourceId = R.string.deals_service_four;
         break;
      default:
         break;
      }
      String detailMsg = getString(resourceId);
      notification.setLatestEventInfo(appCtx,                              
            getString(R.string.deals_service_title), 
            detailMsg, pending);
      mgr.notify(0, notification);                                         
   }
   */
}
