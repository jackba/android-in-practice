package com.manning.aip.andcube;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Shape {
   
   public static final int ONE = 0x10000;

   protected FloatBuffer vertices;
   protected FloatBuffer colors;
   protected ByteBuffer indices;
   
   protected FloatBuffer textureBuffer;
   
   // A raw pointer to our textures.
   protected int[] textures;

   /**
    * Populates the shape with the vertexes, colors and indices.
    * 
    * @param vertexArray
    * @param colors
    * @param indices
    */
   protected Shape(float[] vertexArray, float[] colorArray, byte[] indexArray) {
      ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
      vbb.order(ByteOrder.nativeOrder());
      vertices = vbb.asFloatBuffer();
      vertices.put(vertexArray);
      vertices.position(0);

      ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length * 4);
      cbb.order(ByteOrder.nativeOrder());
      colors = cbb.asFloatBuffer();
      colors.put(colorArray);
      colors.position(0);

      indices = ByteBuffer.allocateDirect(indexArray.length);
      indices.put(indexArray);
      indices.position(0);
   }

   public void draw(GL10 gl) {
      if (textures != null) {
         gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
      }
      
      // can't enable or map before drawElements, causes 1282 unsupported op error
      // http://www.opengl.org/sdk/docs/man/xhtml/glDrawElements.xml
      //gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      //gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
      
      gl.glFrontFace(GL10.GL_CW);
      gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.vertices);
      if (textures != null) {
         gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
      } else {
         gl.glColorPointer(4, GL10.GL_FIXED, 0, this.colors);
      }

      gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, this.indices);
      
      // gl.glGetError
      //gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
      //gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
   }

   public void loadGLTexture(GL10 gl, Context context) {
   }
}
