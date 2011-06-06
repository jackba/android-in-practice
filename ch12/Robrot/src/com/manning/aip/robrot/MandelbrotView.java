package com.manning.aip.robrot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// TODO profile this, improve algorithm, improve speed

public class MandelbrotView extends View {

   private static final ComplexNumber temp = new ComplexNumber(0, 0);

   private float scale = 2;
   private float xc = -0.5f;
   private float yc = 0f;

   private Bitmap renderBitmap;
   private Canvas renderCanvas;
   private Handler handler;
   private OnTouchListener onTouchListener;

   private Paint simplePaint;

   private final Runnable invalidator;
   private final Runnable renderer;

   private Thread renderThread;

   private int[] colorSpace;

   private int height;
   private int width;

   public MandelbrotView(Context context) {
      this(context, null);
   }

   public MandelbrotView(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public MandelbrotView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      handler = new Handler();

      simplePaint = new Paint();
      simplePaint.setColor(Color.WHITE);
      simplePaint.setStyle(Paint.Style.STROKE);

      invalidator = new Runnable() {
         @Override
         public void run() {
            invalidate();
         }
      };

      renderer = new Runnable() {
         @Override
         public void run() {
            renderMandelbrot();
         }
      };

      this.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View v, MotionEvent event) {
            if ((onTouchListener != null) && onTouchListener.onTouch(v, event)) {
               return true;
            }

            if (event.getAction() != MotionEvent.ACTION_DOWN) {
               return false;
            }

            cancel();

            float x = event.getX();
            float y = event.getY();
            float xScale = scale;
            float yScale = scale;

            if (height > width) {
               yScale = yScale * ((float) height / (float) width);
            } else {
               xScale = xScale * ((float) width / (float) height);
            }

            xc = xc - (xScale / 2) + ((xScale * x) / width);
            yc = yc - (yScale / 2) + ((yScale * y) / height);

            scale = scale * .5f;

            start();

            return true;
         }
      });
   }

   @Override
   protected void onMeasure(int widthSpecId, int heightSpecId) {
      stopRender();
      height = View.MeasureSpec.getSize(heightSpecId);
      width = View.MeasureSpec.getSize(widthSpecId);
      setMeasuredDimension(width, height);
      start();
   }
   
   @Override
   protected void onDraw(Canvas canvas) {
      canvas.drawBitmap(renderBitmap, 0, 0, simplePaint);
   }   
   
   public void setScale(float scale) {
      this.scale = scale;
   }

   public void cancel() {
      stopRender();
   }

   public void reset() {
      cancel();
      xc = -0.5f;
      yc = 0f;
      scale = 2;
      start();
   }

   public void start() {
      if (renderThread == null) {
         renderThread = new Thread(renderer);
         renderThread.start();
      }
   }

   
   //
   // private
   // 
   
   private void readColors() throws IOException {
      InputStream stream = getContext().getResources().openRawResource(R.raw.colors);
      BufferedReader r = new BufferedReader(new InputStreamReader(stream));
      String line = r.readLine();
      colorSpace = new int[256];

      for (int i = 0; (line != null) && (i < 256); i++) {
         String[] vals = line.split(" ");
         colorSpace[i] = Color.rgb(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]), Integer.parseInt(vals[2]));
         line = r.readLine();
      }

      r.close();
   }
   
   private static int escapeIteration(ComplexNumber startValue, int maxIterations) {
      MandelbrotView.temp.setValue(startValue);

      for (int i = 0; i < maxIterations; i++) {
         if (MandelbrotView.temp.abs() > 2.0) {
            return i;
         }

         MandelbrotView.temp.multiply(MandelbrotView.temp).add(startValue);
      }

      return maxIterations;
   }   

   private void renderMandelbrot() {
      if (colorSpace == null) {
         try {
            readColors();
         } catch (IOException e) {
            throw new Error(e);
         }
      }

      renderBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
      renderCanvas = new Canvas(renderBitmap);

      int maxIterations = 255;
      float xScale = scale;
      float yScale = scale;

      if (height > width) {
         yScale = yScale * ((float) height / (float) width);
      } else {
         xScale = xScale * ((float) width / (float) height);
      }

      Paint p = new Paint();
      p.setStyle(Paint.Style.FILL_AND_STROKE);

      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            float xVal = xc - (xScale / 2) + ((xScale * x) / width);
            float yVal = yc - (yScale / 2) + ((yScale * y) / height);
            ComplexNumber z = new ComplexNumber(xVal, yVal);
            int escape = MandelbrotView.escapeIteration(z, maxIterations);
            p.setColor(colorSpace[escape]);
            renderCanvas.drawPoint(x, y, p);
         }

         if (Thread.currentThread().isInterrupted()) {
            return;
         }

         threadShiftInvalidate();
      }
   }

   private void stopRender() {
      if (renderThread != null) {
         renderThread.interrupt();
         renderThread = null;
      }
   }

   private void threadShiftInvalidate() {
      handler.post(invalidator);
   }
}
