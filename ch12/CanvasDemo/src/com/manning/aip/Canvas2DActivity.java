package com.manning.aip;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Canvas2DActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(new CanvasView(this));
	}

	// creating the actual View
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
						rnd.nextInt(canvas.getHeight()), rnd.nextInt(canvas
								.getWidth()), rnd.nextInt(canvas.getHeight()),
						paint);
				canvas.drawCircle(rnd.nextInt(canvas.getWidth() - 30), rnd // #6
						.nextInt(canvas.getHeight() - 30), rnd.nextInt(30),
						paint);
				canvas.drawRect(rnd.nextInt(canvas.getWidth()), rnd // #7
						.nextInt(canvas.getHeight()), rnd.nextInt(canvas
						.getWidth()), rnd.nextInt(canvas.getHeight()), paint);
			}
			invalidate();
		}
	}
}