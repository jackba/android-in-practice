package com.manning.aip.lifecycle;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

// logcat filterspec
// adb -e logcat "*:s LifecycleExplorer:v"

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
 * http://developer.android.com/intl/de/reference/android/app/Activity.html
 * 
 * @author ccollins
 *
 */
public class LifecycleActivity extends Activity {

   private static final String LOG_TAG = "LifecycleExplorer";

   // http://developer.android.com/intl/de/guide/topics/manifest/activity-element.html
   // manifest entries that can affect lifecycle/task (TODO cover with sep activities)
   // allowTaskReparenting
   // alwaysRetainTaskState
   // clearTaskOnLaunch
   // configChanges
   // finishOnTaskLaunch
   // launchMode
   // multiprocess
   // process
   // screenOrientation
   // taskAffinity

   // also include adb shell dumpsys activity

   @Override
   public void onCreate(Bundle savedInstanceState) {
      Log.d(LOG_TAG, " *** onCreate");
      Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();      
      super.onCreate(savedInstanceState);      
   }

   @Override
   protected void onStart() {
      Log.d(LOG_TAG, " *** onStart");
      Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();      
      super.onStart();      
   }

   @Override
   protected void onResume() {
      Log.d(LOG_TAG, " *** onResume");
      Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
      super.onResume();      
   }
      
   @Override
   protected void onPause() {
      Log.d(LOG_TAG, " *** onPause");
      Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
      super.onPause();      
   }

   @Override
   protected void onStop() {
      Log.d(LOG_TAG, " *** onStop");
      Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
      super.onStop();      
   }

   @Override
   protected void onDestroy() {
      Log.d(LOG_TAG, " *** onDestroy");
      Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
      super.onDestroy();      
   }

   //
   // other life cycle/task related methods
   //
   @Override
   public boolean isFinishing() {
      Log.d(LOG_TAG, " *** isFinishing");
      Toast.makeText(this, "isFinishing", Toast.LENGTH_SHORT).show();
      return super.isFinishing();
   }

   @Override
   public void finish() {
      Log.d(LOG_TAG, " *** finish");
      Toast.makeText(this, "finish", Toast.LENGTH_SHORT).show();
      super.finish();
   }

   @Override
   public void onLowMemory() {
      Log.d(LOG_TAG, " *** onLowMemory");
      Toast.makeText(this, "onLowMemory", Toast.LENGTH_SHORT).show();
      super.onLowMemory();
   }

   //
   // configuration related
   //
   @Override
   public void onConfigurationChanged(Configuration newConfig) {
      Log.d(LOG_TAG, " *** onConfigurationChanged");
      Toast.makeText(this, "onConfigurationChanged", Toast.LENGTH_SHORT).show();
      super.onConfigurationChanged(newConfig);      
   }

   @Override
   public Object onRetainNonConfigurationInstance() {
      Log.d(LOG_TAG, " *** onRetainNonConfigurationInstance");
      Toast.makeText(this, "onRetainNonConfigurationInstance", Toast.LENGTH_SHORT).show();
      return super.onRetainNonConfigurationInstance();
   }

   @Override
   public Object getLastNonConfigurationInstance() {
      Log.d(LOG_TAG, " *** getLastNonConfigurationInstance");
      Toast.makeText(this, "getLastNonConfigurationInstance", Toast.LENGTH_SHORT).show();
      return super.getLastNonConfigurationInstance();
   }

   //
   // state related
   //
   @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState) {
      Log.d(LOG_TAG, " *** onRestoreInstanceState");
      Toast.makeText(this, "onRestoreInstanceState", Toast.LENGTH_SHORT).show();
      super.onRestoreInstanceState(savedInstanceState);
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      Log.d(LOG_TAG, " *** onSaveInstanceState");
      Toast.makeText(this, "onSaveInstanceState", Toast.LENGTH_SHORT).show();
      super.onSaveInstanceState(outState);
   }  
}