package com.manning.aip.canvasdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Random;

public class Canvas2DRandomColorFullScreenActivity extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);      

      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      
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