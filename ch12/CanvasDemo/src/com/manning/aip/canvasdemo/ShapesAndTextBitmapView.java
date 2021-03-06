/**
 * 
 */
package com.manning.aip.canvasdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * View to display a Bitmap.
 * 
 * @author tamas
 * 
 */
public class ShapesAndTextBitmapView extends View {

   private Paint paint;
   private Bitmap bitmap;

   public ShapesAndTextBitmapView(Context context) {
      super(context);
      // loading the bitmap
      bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.copter);
   }

   @Override
   protected void onDraw(Canvas canvas) {
      canvas.drawRGB(0, 0, 0);
      drawShapes(canvas);
      drawBitmap(canvas);
   }

   private void drawShapes(Canvas canvas) {
      // draw a red square, a green circle and blue triangle in the bottom part of the screen
      int side = canvas.getWidth() / 5;
      paint = new Paint();
      paint.setARGB(255, 255, 0, 0);
      canvas.drawRect(side, canvas.getHeight() - 60 - side, side + side, canvas.getHeight() - 60, paint);
      paint.setARGB(255, 0, 255, 0);
      canvas.drawCircle(side * 2 + side / 2, canvas.getHeight() - 60 - side / 2, side / 2, paint);
      paint.setARGB(255, 0, 0, 255);
      paint.setStyle(Paint.Style.FILL);
      Path triangle = new Path();
      triangle.moveTo(side * 3 + 30, canvas.getHeight() - 60 - side);
      triangle.lineTo(side * 3 + 60, canvas.getHeight() - 60);
      triangle.lineTo(side * 3, canvas.getHeight() - 60);
      triangle.lineTo(side * 3 + 30, canvas.getHeight() - 60 - side);
      canvas.drawPath(triangle, paint);
   }

   private void drawBitmap(Canvas canvas) {
      paint = new Paint();
      canvas.drawBitmap(bitmap, 0, 0, paint);
   }
}
