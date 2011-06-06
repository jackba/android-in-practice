package com.manning.aip.opengldemo;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class OpenGLTriangleActivity extends Activity {

   private GLSurfaceView glView;
   private Triangle triangle;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      requestWindowFeature(Window.FEATURE_NO_TITLE);
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
      glView = new GLSurfaceView(this);
      glView.setRenderer(new MyOpenGLRenderer());
      setContentView(glView);
   }

   class MyOpenGLRenderer implements Renderer {
      @Override
      public void onSurfaceChanged(GL10 gl, int width, int height) {
         Log.d("MyOpenGLRenderer", "Surface changed. Width=" + width + " Height=" + height);
         gl.glViewport(0, 0, width, height);
         gl.glMatrixMode(GL10.GL_PROJECTION);
         gl.glLoadIdentity();
         gl.glOrthof(0, 320, 0, 480, 1, -1);
      }

      @Override
      public void onSurfaceCreated(GL10 gl, EGLConfig config) {
         Log.d("MyOpenGLRenderer", "Surface created");
         triangle = new Triangle();
      }

      @Override
      public void onDrawFrame(GL10 gl) {
         gl.glClearColor(0.0f, 0.5f, 0.0f, 1f);
         gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
         triangle.draw(gl);
      }
   }
}
