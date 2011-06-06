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
public class Triangle {

   private FloatBuffer vertexBuffer;
   private float vertices[] = {
            //	         0.0f,   0.0f,   0.0f,        // V1 - first vertex (x,y,z)
            //          319.0f, 0.0f,   0.0f,        // V2 - second vertex
            //          160.0f, 479.0f, 0.0f         // V3 - third vertex
            100.0f, 150.0f, 0.0f, // V1 - first vertex (x,y,z)
            219.0f, 150.0f, 0.0f, // V2 - second vertex
            160.0f, 279.0f, 0.0f // V3 - third vertex
            };

   public Triangle() {
      ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3 * 3 * 4);
      byteBuffer.order(ByteOrder.nativeOrder());
      vertexBuffer = byteBuffer.asFloatBuffer();
      vertexBuffer.put(vertices);
      vertexBuffer.flip();
   }

   public void draw(GL10 gl) {
      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      // set the colour for the triangle
      gl.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);
      // Point to our vertex buffer
      gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
      // Draw the vertices as triangle strip
      gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);
      // Disable the client state before leaving
      gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
   }
}
