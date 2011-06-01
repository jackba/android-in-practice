package com.manning.aip;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class OpenGLDemoActivity extends Activity {
	
	private GLSurfaceView 	glView;
	private Pyramid		pyramid;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {					
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);					
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		glView = new GLSurfaceView(this);								
		glView.setRenderer(new MyOpenGLRenderer());						
        setContentView(glView);											
    }

    class MyOpenGLRenderer implements Renderer {						

    	@Override
    	public void onSurfaceChanged(GL10 gl, int width, int height) { 
    		Log.d("MyOpenGLRenderer", "Surface changed. Width=" + width
    				+ " Height=" + height);
    		gl.glViewport(0, 0, width, height);
    		gl.glMatrixMode(GL10.GL_PROJECTION);
    		gl.glLoadIdentity();
            //Calculate The Aspect Ratio Of The Window
			GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 
					0.1f, 100.0f);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
//    		gl.glOrthof(0, 320, 0, 480, 1, -1);
    	}
    	
    	@Override
    	public void onSurfaceCreated(GL10 gl, EGLConfig config) {	
    		Log.d("MyOpenGLRenderer", "Surface created");
    		pyramid = new Pyramid();
    	}

    	@Override
		public void onDrawFrame(GL10 gl) {								
			gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);						
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl.glLoadIdentity();
//			triangle.draw(gl);
			gl.glTranslatef(0.0f, 0.0f, -10.0f);     // move 5 units INTO the screen
	                                                // is the same as moving the camera 5 units away
			pyramid.draw(gl);
		}
    }
}