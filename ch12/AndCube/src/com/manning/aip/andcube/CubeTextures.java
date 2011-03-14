package com.manning.aip.andcube;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

public class CubeTextures extends CubeColors {

   public CubeTextures() {
      super();
      float[] textureVertices =
               { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f,
                        0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, };

      ByteBuffer byteBuf = ByteBuffer.allocateDirect(textureVertices.length * 4);
      byteBuf.order(ByteOrder.nativeOrder());
      textureBuffer = byteBuf.asFloatBuffer();
      textureBuffer.put(textureVertices);
      textureBuffer.position(0);
   }

   @Override
   public void loadGLTexture(GL10 gl, Context context) {
      if (textures == null) {
         return;
      }
      textures = new int[1];
      InputStream is = context.getResources().openRawResource(R.drawable.smile);
      Bitmap bitmap = null;
      try {
         bitmap = BitmapFactory.decodeStream(is);

      } finally {
         try {
            is.close();
            is = null;
         } catch (IOException e) {
         }
      }

      gl.glGenTextures(1, textures, 0);
      gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

      gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
      gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

      gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
      gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

      GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

      bitmap.recycle();
   }
}
