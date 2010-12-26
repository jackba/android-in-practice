package com.manning.aip.mymoviesdatabase.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;

public class ImageUtil {

   private static final int COLOR = 0xff424242;
   
   // source: http://stackoverflow.com/questions/2459916/how-to-make-an-imageview-to-have-rounded-corners
   public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPx) {
      Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
      Canvas canvas = new Canvas(output);
      
      Paint paint = new Paint();
      Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
      RectF rectF = new RectF(rect);

      paint.setAntiAlias(true);
      canvas.drawARGB(0, 0, 0, 0);
      paint.setColor(COLOR);
      canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

      paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
      canvas.drawBitmap(bitmap, rect, rect, paint);

      return output;
   }
}
