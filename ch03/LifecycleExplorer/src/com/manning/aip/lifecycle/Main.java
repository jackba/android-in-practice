package com.manning.aip.lifecycle;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;

// logcat filterspec
// adb -e logcat "*:s LifecycleExplorer:v"

/**
 * The lifecycle and task affinity of an Activity should be well understood when
 * writing Android apps. This sample can help expose what the platform is doing 
 * with regard to lifecycle/tasks. 
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
 * 
 * 
 * http://developer.android.com/intl/de/reference/android/app/Activity.html
 * 
 * @author ccollins
 *
 */
public class Main extends Activity {

   private static final String LOG_TAG = "LifecycleExplorer";

   private TextView phase;
   private TextView task;
   private Chronometer chrono;

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
   
   
   //
   // the life cycle "onX" methods
   //
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      phase = (TextView) findViewById(R.id.currentPhase);
      task = (TextView) findViewById(R.id.currentTask);
      chrono = (Chronometer) findViewById(R.id.chronometer);
      
      chrono.start();
   }
 
   @Override
   protected void onStart() {
      Log.d(LOG_TAG, " *** onStart");
      super.onStart();
      phase.setText("onStart");
   }

   @Override
   protected void onRestart() {
      Log.d(LOG_TAG, " *** onRestart");
      super.onRestart();
      phase.setText("onReStart");
   }
   
   @Override
   protected void onResume() {
      Log.d(LOG_TAG, " *** onResume");
      super.onResume();
      phase.setText("onResume");
   }

   @Override
   protected void onPause() {
      Log.d(LOG_TAG, " *** onPause");
      super.onPause();
      chrono.stop();
      phase.setText("onPause");
   }
   
   @Override
   protected void onStop() {
      Log.d(LOG_TAG, " *** onStop");
      super.onStop();
      phase.setText("onStop");
   }

   @Override
   protected void onDestroy() {
      Log.d(LOG_TAG, " *** onDestroy");
      super.onDestroy();
   }

   // also onPostCreate and onPostResume, but usually don't need to muck with those
   

   //
   // other life cycle/task related methods
   //
   @Override
   public boolean isFinishing() {
      Log.d(LOG_TAG, " *** ");
      return super.isFinishing();
   }

   @Override
   public void finish() {
      Log.d(LOG_TAG, " *** finish");
      super.finish();
   }

   //
   // task
   //
   @Override
   public boolean isTaskRoot() {
      Log.d(LOG_TAG, " *** ");
      return super.isTaskRoot();
   }

   @Override
   public boolean moveTaskToBack(boolean nonRoot) {
      Log.d(LOG_TAG, " *** ");
      return super.moveTaskToBack(nonRoot);
   }

   //
   // configuration related
   //
   @Override
   public void onConfigurationChanged(Configuration newConfig) {
      Log.d(LOG_TAG, " *** onConfigurationChanged");
      super.onConfigurationChanged(newConfig);
   }

   @Override
   public Object onRetainNonConfigurationInstance() {
      Log.d(LOG_TAG, " *** onRetainNonConfigurationInstance");
      return super.onRetainNonConfigurationInstance();
   }

   @Override
   public Object getLastNonConfigurationInstance() {
      Log.d(LOG_TAG, " *** getLastNonConfigurationInstance");
      return super.getLastNonConfigurationInstance();
   }

   //
   // state related
   //
   @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState) {
      Log.d(LOG_TAG, " *** onRestoreInstanceState");
      super.onRestoreInstanceState(savedInstanceState);
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      Log.d(LOG_TAG, " *** onSaveInstanceState");
      super.onSaveInstanceState(outState);
   }

   //
   // other interesting lifecycle/task related methods
   //
   @Override
   public void onLowMemory() {
      Log.d(LOG_TAG, " *** onLowMemory");
      super.onLowMemory();
   }
}