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
public class Pyramid {

   private FloatBuffer vertexBuffer;
   private float vertices[] = { 0.0f, 1.0f, 0.0f, // V1
            -1.0f, 0.0f, 0.0f, // V2
            0.0f, 0.0f, -1.0f, // V3

            0.0f, 1.0f, 0.0f, // V1	
            0.0f, 0.0f, -1.0f, // V3	
            1.0f, 0.0f, 0.0f, // V4	

            0.0f, 1.0f, 0.0f, // V1	
            1.0f, 0.0f, 0.0f, // V4	
            -1.0f, 0.0f, 0.0f, // V2	
   };

   private float rotation = 0.1f;

   public Pyramid() {
      ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
      byteBuffer.order(ByteOrder.nativeOrder());
      vertexBuffer = byteBuffer.asFloatBuffer();
      vertexBuffer.put(vertices);
      vertexBuffer.flip();
   }

   public void draw(GL10 gl) {
      rotation += 0.3f;
      gl.glRotatef(rotation, 0f, 1f, 0f);
      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      gl.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);
      gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
      gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);
      gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
   }
}
