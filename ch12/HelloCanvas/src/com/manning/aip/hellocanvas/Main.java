package com.manning.aip.hellocanvas;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.widget.ImageView;

public class Main extends Activity {

   @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ImageView image = (ImageView) this.findViewById(R.id.imageView);        
        
        Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565); 
        bitmap.eraseColor(Color.WHITE);
        Canvas canvas = new Canvas(bitmap); 
        Paint textPaint = new Paint(); 
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(24);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Align.LEFT);
        canvas.drawText("Hello, Canvas!", 35F, 35F, textPaint);        
        image.setImageBitmap(bitmap);        
    }
}