package com.manning.aip.lifecycle;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class Main extends LifecycleActivity {

   private static final String STATE_TEXT_KEY = "stateTextKey";

   private Button finish;
   private Button activity2;
   private Chronometer chrono;
   private EditText stateText;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      finish = (Button) findViewById(R.id.finishButton);
      finish.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            finish();
         }
      });
      activity2 = (Button) findViewById(R.id.activity2Button);
      activity2.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            startActivity(new Intent(Main.this, Activity2.class));
         }
      });
      chrono = (Chronometer) findViewById(R.id.chronometer);
      stateText = (EditText) findViewById(R.id.stateText);
   }

   @Override
   protected void onResume() {
      super.onResume();
      chrono.setBase(SystemClock.elapsedRealtime());
      chrono.start();

      // if the last non configuration object is present, show it
      Date date = (Date) this.getLastNonConfigurationInstance();
      if (date != null) {
         Toast.makeText(this, "\"LastNonConfiguration\" object present: " + date, Toast.LENGTH_LONG).show();
      }
   }

   @Override
   protected void onPause() {
      chrono.stop();
      super.onPause();
   }

   @Override
   protected void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      if (savedInstanceState != null && savedInstanceState.containsKey(STATE_TEXT_KEY)) {
         stateText.setText(savedInstanceState.getString(STATE_TEXT_KEY));
      }
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      outState.putString(STATE_TEXT_KEY, stateText.getText().toString());
      super.onSaveInstanceState(outState);
   }

   @Override
   public Object onRetainNonConfigurationInstance() {
      // don't call super here intentionally, we want to return date
      return new Date();
   }
}