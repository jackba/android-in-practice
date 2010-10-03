package com.manning.aip.lifecycle;

import android.os.Bundle;

public class Activity2 extends LifecycleActivity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity2);    
   }
}