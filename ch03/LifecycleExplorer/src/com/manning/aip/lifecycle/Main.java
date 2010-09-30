package com.manning.aip.lifecycle;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;

public class Main extends LifecycleActivity {

   private Button finish;
   private Chronometer chrono;
  
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      chrono = (Chronometer) findViewById(R.id.chronometer);     
      
      finish = (Button) findViewById(R.id.finishButton);
      finish.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            finish();
         }
      });
   }

   @Override
   protected void onStart() {     
      super.onStart();
      chrono.start();
   }  
}