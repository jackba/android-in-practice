package com.manning.aip.canvasdemo;

import android.app.Activity;
import android.os.Bundle;

public class Canvas2DActivity extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(new ShapesAndTextView(this));
   }
}
