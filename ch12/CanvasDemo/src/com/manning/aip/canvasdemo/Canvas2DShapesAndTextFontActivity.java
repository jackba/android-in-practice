package com.manning.aip.canvasdemo;

import android.app.Activity;
import android.os.Bundle;

public class Canvas2DShapesAndTextFontActivity extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      ShapesAndTextFontView view = new ShapesAndTextFontView(this);
      view.setText("256 byte Style");
      setContentView(view);
   }
}
