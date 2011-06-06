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
public class ColouredPyramid {

   private FloatBuffer vertexBuffer;
   private static final int VERTEX_SIZE = (3 + 4) * 4;
   private float vertices[] = {
            0.0f, 1.0f, 0.0f, 1, 0, 0, 1, // V1 - red
            -1.0f, 0.0f, 0.0f, 1, 0, 0, 1, // V2 - red
            0.0f, 0.0f, -1.0f, 1, 0, 0, 1, // V3 - red

            0.0f, 1.0f, 0.0f, 0, 1, 0, 1, // V1 - green
            0.0f, 0.0f, -1.0f, 0, 1, 0, 1, // V3 - green	
            1.0f, 0.0f, 0.0f, 0, 1, 0, 1, // V4 - green

            0.0f, 1.0f, 0.0f, 0, 0, 1, 1, // V1 - blue
            1.0f, 0.0f, 0.0f, 0, 0, 1, 1, // V4 - blue
            -1.0f, 0.0f, 0.0f, 0, 0, 1, 1, // V2 - blue
   };

   private float rotation = 0.1f;

   public ColouredPyramid() {
      ByteBuffer byteBuffer = ByteBuffer.allocateDirect(ColouredPyramid.VERTEX_SIZE * 3 * 4);
      byteBuffer.order(ByteOrder.nativeOrder());
      vertexBuffer = byteBuffer.asFloatBuffer();
      vertexBuffer.put(vertices);
      vertexBuffer.flip();
   }

   public void draw(GL10 gl) {
      rotation += 1.0f;
      gl.glRotatef(rotation, 1f, 1f, 1f);
      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

      vertexBuffer.position(0);
      gl.glVertexPointer(3, GL10.GL_FLOAT, ColouredPyramid.VERTEX_SIZE, vertexBuffer);
      vertexBuffer.position(3);
      gl.glColorPointer(4, GL10.GL_FLOAT, ColouredPyramid.VERTEX_SIZE, vertexBuffer);

      gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3 * 3);
      gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
      gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
   }
}
