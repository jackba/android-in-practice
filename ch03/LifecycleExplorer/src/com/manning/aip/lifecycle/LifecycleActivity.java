package com.manning.aip.lifecycle;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

// logcat filterspec
// adb -e logcat "*:s LifecycleExplorer:v"

// also include adb shell dumpsys activity

/**
 * The lifecycle and task affinity of an Activity should be well understood when
 * writing Android apps. This class can help expose what the platform is doing 
 * with regard to lifecycle/tasks (it uses Toasts and logging to emphasize each method call). 
 * 
 * Four Activity states:
 * ACTIVE/RUNNING (in the foreground)
 * PAUSED (lost focus, but still visible -- another non-full-sized Activity on top of this one)
 * STOPPED (completely obscured by another Activity)
 * KILLED (paused or stopped may be finished or killed)
 * 
 * Three key loops to keep in mind:
 * ENTIRE LIFETIME - onCreate to onDestroy
 * VISIBLE LIFETIME - onStart to onStop
 * FOREGROUND LIFETIME - onResume to onPause
 * 
 * Three key methods (though there are also many more):
 * onCreate
 * onResume
 * onPause
 * 
 * http://developer.android.com/intl/de/reference/android/app/Activity.html
 * 
 * Note: Use logcat filterspec to see just these debug statements:
 * adb -e logcat "*:s LifecycleExplorer:v"
 * 
 * Note: Construct with false to disable Notifications (which can be slow when there are many).
 * 
 * @author ccollins
 *
 */
public abstract class LifecycleActivity extends Activity {

   private static final String LOG_TAG = "LifecycleExplorer";

   private NotificationManager notifyMgr;
   private boolean enableNotifications;

   public LifecycleActivity() {
      super();
   }

   public LifecycleActivity(final boolean enableNotifications) {
      this();
      this.enableNotifications = enableNotifications;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      notifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      debugEvent("onCreate");
   }

   @Override
   protected void onStart() {
      debugEvent("onStart");
      super.onStart();
   }

   @Override
   protected void onResume() {
      debugEvent("onResume");
      super.onResume();
   }

   @Override
   protected void onPause() {
      debugEvent("onPause");
      super.onPause();
   }

   @Override
   protected void onStop() {
      debugEvent("onStop");
      super.onStop();
   }

   @Override
   protected void onDestroy() {
      debugEvent("onDestroy");
      super.onDestroy();
   }

   //
   // state related
   //
   @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState) {
      debugEvent("onRestoreInstanceState");
      super.onRestoreInstanceState(savedInstanceState);
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      debugEvent("onSaveInstanceState");
      super.onSaveInstanceState(outState);
   }

   //
   // configuration related 
   //
   @Override
   public void onConfigurationChanged(Configuration newConfig) {
      debugEvent("onConfigurationChanged");
      super.onConfigurationChanged(newConfig);
   }

   @Override
   public Object onRetainNonConfigurationInstance() {
      debugEvent("onRetainNonConfigurationInstance");
      return super.onRetainNonConfigurationInstance();
   }

   //
   // other handy Activity methods
   //
   @Override
   public boolean isFinishing() {
      debugEvent("isFinishing");
      return super.isFinishing();
   }

   @Override
   public void finish() {
      super.finish();
   }

   @Override
   public void onLowMemory() {
      Toast.makeText(this, "onLowMemory", Toast.LENGTH_SHORT).show();
      super.onLowMemory();
   }

   //
   // notify helper
   //
   private void debugEvent(final String method) {
      long ts = System.currentTimeMillis();
      Log.d(LOG_TAG, " *** " + method + " " + getClass().getName() + " " + ts);
      if (enableNotifications) {
         Notification notification = new Notification(android.R.drawable.star_big_on, "Lifeycle Event: " + method, 0L);
         RemoteViews notificationContentView = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
         notification.contentView = notificationContentView;
         notification.contentIntent = PendingIntent.getActivity(this, 0, null, 0);
         notification.flags |= Notification.FLAG_AUTO_CANCEL;
         notificationContentView.setImageViewResource(R.id.image, android.R.drawable.btn_star);
         notificationContentView.setTextViewText(R.id.lifecycle_class, getClass().getName());
         notificationContentView.setTextViewText(R.id.lifecycle_method, method);
         notificationContentView.setTextColor(R.id.lifecycle_method, R.color.black);
         notificationContentView.setTextViewText(R.id.lifecycle_timestamp, Long.toString(ts));
         notifyMgr.notify((int) System.currentTimeMillis(), notification);
      }
   }
}