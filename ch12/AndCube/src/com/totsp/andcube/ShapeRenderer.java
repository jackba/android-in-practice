package com.totsp.andcube;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ShapeRenderer implements GLSurfaceView.Renderer {

   private boolean translucent;
   private Shape shape;
   private float angle;
   private Context context;

   public ShapeRenderer(boolean translucent, Shape shape, Context context) {
      this.translucent = translucent;
      this.shape = shape;
      this.context = context;
   }

   @Override
   public void onDrawFrame(GL10 gl) {
      gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
      if (this.translucent) {
         gl.glClearColor(0, 0, 0, 0);
      } else {
         gl.glClearColor(1, 1, 1, 1);
      }
      gl.glMatrixMode(GL10.GL_MODELVIEW);
      gl.glLoadIdentity();
      gl.glTranslatef(0, 0, -3.0f);
      gl.glRotatef(angle, 0, 1, 0);
      gl.glRotatef(angle * 0.25f, 1, 0, 0);

      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

      shape.draw(gl);
      angle += 1.2f;
   }

   @Override
   public void onSurfaceChanged(GL10 gl, int width, int height) {
      gl.glViewport(0, 0, width, height);
      float ratio = (float) width / height;
      gl.glMatrixMode(GL10.GL_PROJECTION);
      gl.glLoadIdentity();
      gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
   }

   @Override
   public void onSurfaceCreated(GL10 gl, EGLConfig config) {
      gl.glDisable(GL10.GL_DITHER);
      gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
      if (this.translucent) {
         gl.glClearColor(0, 0, 0, 0);
      } else {
         gl.glClearColor(1, 1, 1, 1);
      }
      gl.glEnable(GL10.GL_TEXTURE_2D);
      gl.glShadeModel(GL10.GL_SMOOTH);
      gl.glEnable(GL10.GL_DEPTH_TEST);
      this.shape.loadGLTexture(gl, context);
   }
}
