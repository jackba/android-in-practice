package com.manning.aip.button;

import android.content.Context;
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
   
   private Paint border = new Paint();
   private Paint textPaint = new Paint();
   private Paint count = new Paint();
   private Paint square = new Paint();
   private PathEffect borderRadius = new CornerPathEffect(5);
   private String text = "Some Text";
   private int countValue = 100;
   private int height;
   private int width;

   {
      textPaint.setShadowLayer(1.0F, 0F, 2F, Color.WHITE);
      textPaint.setTextAlign(Align.CENTER);
      textPaint.setColor(Color.BLACK);
      textPaint.setStyle(Style.FILL);
      textPaint.setAntiAlias(true);
      textPaint.setTypeface(Typeface.SANS_SERIF);

      count.setShadowLayer(1.0F, 0F, 2F, Color.WHITE);
      count.setTextAlign(Align.RIGHT);
      count.setColor(Color.BLACK);
      count.setStyle(Style.FILL);
      count.setTypeface(Typeface.MONOSPACE);
      count.setAntiAlias(true);

      square.setStyle(Style.FILL);
      square.setColor(Color.rgb(245, 245, 245));
      square.setPathEffect(borderRadius);
      square.setAntiAlias(true);

      border.setStyle(Style.STROKE);
      border.setColor(Color.rgb(75, 75, 75));
      border.setPathEffect(borderRadius);
      border.setStrokeWidth(2F);
      border.setAntiAlias(true);
   }

   public CustomButton(Context context) {
      super(context);
   }

   public CustomButton(Context context, AttributeSet attrs) {
      super(context, attrs);
   }

   public CustomButton(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
   }

   public void setCount(int count) {
      this.countValue = count;
   }

   public int getCount() {
      return countValue;
   }

   public void setText(String text) {
      this.text = text;
   }

   public String getText() {
      return text;
   }

   @Override
   public void draw(Canvas canvas) {
      square.setShader(new LinearGradient(0F, 0F, 0F, height, Color.rgb(245, 245, 245), Color.rgb(221, 221, 221),
               Shader.TileMode.REPEAT));

      this.textPaint.setTextSize(height * 0.8F);
      this.count.setTextSize(height * 0.28F);

      Rect rect = new Rect(0, 0, width, height);
      canvas.drawRect(rect, square);
      canvas.drawText(text, (width / 2) - (width / 10) + 10, (height / 2) + (height / 3), this.textPaint);
      canvas.drawText("" + countValue, (int) (width * 0.92), height / 3, this.count);
   }

   @Override
   protected void onMeasure(int widthSpecId, int heightSpecId) {
      this.height = View.MeasureSpec.getSize(heightSpecId);
      this.width = View.MeasureSpec.getSize(widthSpecId);
      setMeasuredDimension(this.width, this.height);
   }
}
