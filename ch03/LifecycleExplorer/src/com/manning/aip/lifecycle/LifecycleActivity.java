package com.manning.aip.lifecycle;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
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
 * @author ccollins
 *
 */
public abstract class LifecycleActivity extends Activity {

   private static final String LOG_TAG = "LifecycleExplorer";

   private NotificationManager notifyMgr;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      Log.d(LOG_TAG, " *** onCreate");
      super.onCreate(savedInstanceState);
      notifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      notify("onCreate", R.color.yellow, android.R.drawable.btn_star);
   }

   @Override
   protected void onStart() {
      Log.d(LOG_TAG, " *** onStart");
      notify("onStart", R.color.green, android.R.drawable.btn_star);
      super.onStart();
   }

   @Override
   protected void onResume() {
      Log.d(LOG_TAG, " *** onResume");
      notify("onResume", R.color.dark_green, android.R.drawable.btn_star);
      super.onResume();
   }

   @Override
   protected void onPause() {
      Log.d(LOG_TAG, " *** onPause");
      notify("onPause", R.color.yellow, android.R.drawable.btn_star);
      super.onPause();
   }

   @Override
   protected void onStop() {
      Log.d(LOG_TAG, " *** onStop");
      notify("onStop", R.color.red, android.R.drawable.btn_star);
      super.onStop();
   }

   @Override
   protected void onDestroy() {
      Log.d(LOG_TAG, " *** onDestroy");
      notify("onDestroy", R.color.grey, android.R.drawable.btn_star);
      super.onDestroy();
   }
  
   //
   // state related
   //
   @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState) {
      Log.d(LOG_TAG, " *** onRestoreInstanceState");
      notify("onRestoreInstanceState", R.color.black, android.R.drawable.btn_star);
      super.onRestoreInstanceState(savedInstanceState);
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      Log.d(LOG_TAG, " *** onSaveInstanceState");
      notify("onSaveInstanceState", R.color.black, android.R.drawable.btn_star);
      super.onSaveInstanceState(outState);
   }
   
   //
   // configuration related 
   //
   @Override
   public void onConfigurationChanged(Configuration newConfig) {
      Log.d(LOG_TAG, " *** onConfigurationChanged");
      notify("onConfigurationChanged", R.color.black, android.R.drawable.btn_star);
      super.onConfigurationChanged(newConfig);
   }
   
   @Override
   public Object onRetainNonConfigurationInstance() {
      Log.d(LOG_TAG, " *** onRetainNonConfigurationInstance");
      notify("onRetainNonConfigurationInstance", R.color.black, android.R.drawable.btn_star);
      return super.onRetainNonConfigurationInstance();
   }
   
   //
   // other handy Activity methods
   //
   @Override
   public boolean isFinishing() {
      Log.d(LOG_TAG, " *** isFinishing");
      return super.isFinishing();
   }

   @Override
   public void finish() {
      Log.d(LOG_TAG, " *** finish");      
      super.finish();
   }

   @Override
   public void onLowMemory() {
      Log.d(LOG_TAG, " *** onLowMemory");
      Toast.makeText(this, "onLowMemory", Toast.LENGTH_SHORT).show();
      super.onLowMemory();
   }

   //
   // notify helper
   //
   private void notify(final String method, final int methodColor, final int drawable) {
      Notification notification = new Notification(android.R.drawable.star_big_on, "Lifeycle Event: " + method, 0L);
      RemoteViews notificationContentView = new RemoteViews(getPackageName(), R.layout.custom_notification_layout);
      notification.contentView = notificationContentView;
      notification.contentIntent = PendingIntent.getActivity(this, 0, null, 0);
      notification.flags |= Notification.FLAG_AUTO_CANCEL;

      notificationContentView.setImageViewResource(R.id.image, drawable);
      notificationContentView.setTextViewText(R.id.lifecycle_class, getClass().getName());
      notificationContentView.setTextViewText(R.id.lifecycle_method, method);
      notificationContentView.setTextColor(R.id.lifecycle_method, methodColor);
      notificationContentView.setTextViewText(R.id.lifecycle_timestamp, Long.toString(System.currentTimeMillis()));
      this.notifyMgr.notify((int)SystemClock.elapsedRealtime(), notification);
   }
}