package com.manning.aip.app1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class App1 extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      SharedPreferences prefs = getSharedPreferences("app1prefs", MODE_PRIVATE);
      String value = "Hello from App1 preference file!";
      prefs.edit().putString("shared_value", value).commit();
   }

   @Override
   public String toString() {
      return "Hello from App1 toString()!";
   }
}