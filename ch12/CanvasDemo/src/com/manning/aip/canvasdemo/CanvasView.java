package com.manning.aip.canvasdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.Random;

class CanvasView extends View {

   Random rnd = new Random();
   Paint paint;

   public CanvasView(Context context) {
      super(context);
   }

   @Override
   protected void onDraw(Canvas canvas) {
      canvas.drawRGB(0, 0, 0); // #1
      for (int i = 0; i < 10; i++) { // #2
         paint = new Paint(); // #3
         paint.setARGB(rnd.nextInt(256), rnd.nextInt(256), rnd // #4
                  .nextInt(256), rnd.nextInt(256));
         canvas.drawLine(
                  rnd.nextInt(canvas.getWidth()), // #5
                  rnd.nextInt(canvas.getHeight()), rnd.nextInt(canvas.getWidth()), rnd.nextInt(canvas.getHeight()),
                  paint);
         canvas.drawCircle(rnd.nextInt(canvas.getWidth() - 30), rnd // #6
                  .nextInt(canvas.getHeight() - 30), rnd.nextInt(30), paint);
         canvas.drawRect(rnd.nextInt(canvas.getWidth()), rnd // #7
                  .nextInt(canvas.getHeight()), rnd.nextInt(canvas.getWidth()), rnd.nextInt(canvas.getHeight()), paint);
      }
      invalidate();
   }
}
