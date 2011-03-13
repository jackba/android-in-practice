package com.manning.aip.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class CustomButton extends View {

   private Paint borderPaint;
   private PathEffect borderRadius;

   private Paint textPaint;
   private Paint countPaint;
   private Paint squarePaint;

   private int height;
   private int width;

   private int count;
   private String text;

   public CustomButton(Context context) {
      this(context, null);
   }

   public CustomButton(Context context, AttributeSet attrs) {
      this(context, attrs, 0);
   }

   public CustomButton(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);

      this.text = "Button";
      this.count = 0;

      if (attrs != null) {
         TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton, 0, 0);
         String attText = a.getString(R.styleable.CustomButton_text);
         int attCount = a.getInt(R.styleable.CustomButton_count, 0);
         if (attText != null) {
            text = attText;
         }
         if (attCount > 0) {
            count = attCount;
         }
         a.recycle();
      }

      borderPaint = new Paint();
      borderPaint.setStyle(Style.STROKE);
      borderPaint.setColor(Color.rgb(75, 75, 75));
      borderPaint.setPathEffect(borderRadius);
      borderPaint.setStrokeWidth(2F);
      borderPaint.setAntiAlias(true);

      borderRadius = new CornerPathEffect(5);

      textPaint = new Paint();
      textPaint.setShadowLayer(1.0F, 0F, 2F, Color.WHITE);
      textPaint.setTextAlign(Align.CENTER);
      textPaint.setColor(Color.BLACK);
      textPaint.setStyle(Style.FILL);
      textPaint.setAntiAlias(true);
      textPaint.setTypeface(Typeface.SANS_SERIF);

      countPaint = new Paint();
      countPaint.setShadowLayer(1.0F, 0F, 2F, Color.WHITE);
      countPaint.setTextAlign(Align.RIGHT);
      countPaint.setColor(Color.BLUE);
      countPaint.setStyle(Style.FILL);
      countPaint.setTypeface(Typeface.MONOSPACE);
      countPaint.setAntiAlias(true);

      squarePaint = new Paint();
      squarePaint.setStyle(Style.FILL);
      squarePaint.setColor(Color.rgb(245, 245, 245));
      squarePaint.setPathEffect(borderRadius);
      squarePaint.setAntiAlias(true);
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getCount() {
      return count;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getText() {
      return text;
   }

   @Override
   public void onDraw(Canvas canvas) {
      squarePaint.setShader(new LinearGradient(0F, 0F, 0F, height, Color.rgb(254, 254, 254), Color.rgb(221, 221, 221),
               Shader.TileMode.REPEAT));

      textPaint.setTextSize(width * 0.09F);

      countPaint.setTextSize(height * 0.3F);

      Rect rect = new Rect(0, 0, width, height);
      canvas.drawRect(rect, squarePaint);
      canvas.drawText(text, (width / 2) - (width / 10) + 10, (height / 2) + (height / 3), textPaint);
      canvas.drawText("" + count, (int) (width * 0.92), height / 3, countPaint);
   }

   @Override
   protected void onMeasure(int widthSpecId, int heightSpecId) {
      this.height = View.MeasureSpec.getSize(heightSpecId);
      this.width = View.MeasureSpec.getSize(widthSpecId);
      setMeasuredDimension(this.width, this.height);
   }
}
