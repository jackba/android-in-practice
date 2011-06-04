package com.manning.aip.canvasdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class Canvas2DRandomColorActivity extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(new CanvasView(this));
   }

   class CanvasView extends View {
      Random random = new Random();

      public CanvasView(Context context) {
         super(context);
      }

      @Override
      protected void onDraw(Canvas canvas) {
         canvas.drawRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
      }
   }
}