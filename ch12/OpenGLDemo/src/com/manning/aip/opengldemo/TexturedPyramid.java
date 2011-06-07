/**
 * 
 */
package com.manning.aip.opengldemo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * @author tamas
 *
 */
public class TexturedPyramid {

   private int textureId;
   private FloatBuffer vertexBuffer;
   private static final int VERTEX_SIZE = (3 + 2) * 4;
   private float vertices[] = {

            0.0f, 1.0f, 0.0f, 0.5f, 0.0f, // V1 + mapping
            -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, // V2
            0.0f, 0.0f, -1.0f, 1.0f, 1.0f, // V3

            0.0f, 1.0f, 0.0f, 0.5f, 0.0f, // V1
            0.0f, 0.0f, -1.0f, 0.0f, 1.0f, // V3	
            1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // V4

            0.0f, 1.0f, 0.0f, 0.5f, 0.0f, // V1
            1.0f, 0.0f, 0.0f, 0.0f, 1.0f, // V4
            -1.0f, 0.0f, 0.0f, 1.0f, 1.0f, // V2

   };

   private float rotation = 0.1f;

   public TexturedPyramid(int textureId) {
      ByteBuffer byteBuffer = ByteBuffer.allocateDirect(TexturedPyramid.VERTEX_SIZE * 3 * 3);
      byteBuffer.order(ByteOrder.nativeOrder());
      vertexBuffer = byteBuffer.asFloatBuffer();
      vertexBuffer.put(vertices);
      vertexBuffer.flip();
      this.textureId = textureId;
   }

   public void draw(GL10 gl) {
      rotation += 1.0f;
      gl.glRotatef(rotation, 1f, 1f, 1f);

      gl.glEnable(GL10.GL_TEXTURE_2D);
      gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

      vertexBuffer.position(0);
      gl.glVertexPointer(3, GL10.GL_FLOAT, TexturedPyramid.VERTEX_SIZE, vertexBuffer);
      vertexBuffer.position(3);
      gl.glTexCoordPointer(2, GL10.GL_FLOAT, TexturedPyramid.VERTEX_SIZE, vertexBuffer);

      gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3 * 3);
      gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
      gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
   }
}
