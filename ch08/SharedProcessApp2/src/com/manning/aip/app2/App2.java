package com.manning.aip.app2;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class App2 extends Activity {

   private Context app1;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      try {
         app1 =
                  createPackageContext("com.manning.aip.app1",
                           CONTEXT_INCLUDE_CODE);
         Class<?> app1ActivityCls =
                  app1.getClassLoader().loadClass("com.manning.aip.app1.App1");
         Object app1Activity = app1ActivityCls.newInstance();
         Toast.makeText(this, app1Activity.toString(), Toast.LENGTH_LONG)
                  .show();
      } catch (Exception e) {
         e.printStackTrace();
         return;
      }

      SharedPreferences prefs =
               app1.getSharedPreferences("app1prefs", MODE_PRIVATE);
      TextView view = (TextView) findViewById(R.id.hello);
      String shared = prefs.getString("shared_value", null);
      if (shared == null) {
         view.setText("Failed to share!");
      } else {
         view.setText(shared);
      }
   }
}