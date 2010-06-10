package com.totsp.robrot;

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


public class MandelbrotView extends View {
    private Bitmap renderBitmap;
    private Canvas renderCanvas;
    private Handler handler = new Handler();
    private OnTouchListener onTouchListener = null;
    private Paint simplePaint = new Paint();
    private Thread renderThread = null;
    private int[] colorSpace = null;
    private float scale = 2;
    private float xc = -0.5f;
    private float yc = 0f;
    private int height;
    private int width;

    {
        this.simplePaint.setColor(Color.WHITE);
        this.simplePaint.setStyle(Paint.Style.STROKE);
        super.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                	if(onTouchListener != null && onTouchListener.onTouch(v, event)){
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

    public MandelbrotView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MandelbrotView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // TODO Auto-generated constructor stub
    }

    public MandelbrotView(Context context) {
        super(context);
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public OnTouchListener getOnTouchListener() {
        return onTouchListener;
    }

    public float getScale() {
        return scale;
    }

    public void cancel() {
        this.stopRender();
    }

    public void reset() {
        this.cancel();
        xc = -0.5f;
        yc = 0f;
        scale = 2;
        this.start();
    }

    public void setxScale(float scale) {
        this.scale = scale;
    }

    public void start() {
        if (this.renderThread == null) {
            this.renderThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            renderMandelbrot();
                        }
                    });
            this.renderThread.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.renderBitmap, 0, 0, this.simplePaint);
    }

    @Override
    protected void onMeasure(int widthSpecId, int heightSpecId) {
        this.stopRender();
        this.height = View.MeasureSpec.getSize(heightSpecId);
        this.width = View.MeasureSpec.getSize(widthSpecId);
        System.out.println("=======" + width + "x" + height);
        setMeasuredDimension(this.width, this.height);
        start();
    }

    private static int escapeIteration(ComplexNumber startValue,
        int maxIterations) {
        ComplexNumber z = new ComplexNumber(startValue);

        for (int i = 0; i < maxIterations; i++) {
            if (z.abs() > 2.0) {
                return i;
            }

            z.multiply(z).add(startValue);
        }

        return maxIterations;
    }

    private void readColors() throws IOException {
        InputStream stream = this.getContext().getResources()
                                 .openRawResource(R.raw.colors);
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        String line = r.readLine();
        this.colorSpace = new int[256];

        for (int i = 0; (line != null) && (i < 256); i++) {
            String[] vals = line.split(" ");
            this.colorSpace[i] = Color.rgb(Integer.parseInt(vals[0]),
                    Integer.parseInt(vals[1]), Integer.parseInt(vals[2]));
            line = r.readLine();
        }

        r.close();
    }

    private void renderMandelbrot() {
        if (this.colorSpace == null) {
            try {
                this.readColors();
            } catch (IOException e) {
                throw new Error(e);
            }
        }

        this.renderBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        this.renderCanvas = new Canvas(this.renderBitmap);

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

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                float xVal = xc - (xScale / 2) + ((xScale * x) / this.width);
                float yVal = yc - (yScale / 2) + ((yScale * y) / this.height);
                ComplexNumber z = new ComplexNumber(xVal, yVal);
                int escape = escapeIteration(z, maxIterations);
                p.setColor(this.colorSpace[escape]);
                this.renderCanvas.drawPoint(x, y, p);
            }

            if (Thread.currentThread().isInterrupted()) {
                return;
            }

            this.threadShiftInvalidate();
        }
    }

    private void stopRender() {
        if (this.renderThread != null) {
            this.renderThread.interrupt();
            this.renderThread = null;
        }
    }

    private void threadShiftInvalidate() {
        this.handler.post(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                }
            });
    }
}
