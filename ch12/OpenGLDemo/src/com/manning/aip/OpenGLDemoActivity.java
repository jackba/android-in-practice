package com.manning.aip;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class OpenGLDemoActivity extends Activity {
	
	private GLSurfaceView 	glView;
	private Triangle 		triangle;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {					// #1
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);					// #2
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,// #3
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		glView = new GLSurfaceView(this);								// #4
		glView.setRenderer(new MyOpenGLRenderer());						// #5
        setContentView(glView);											// #6
    }

    class MyOpenGLRenderer implements Renderer {						// #7

    	@Override
    	public void onSurfaceChanged(GL10 gl, int width, int height) {	// #8
    		Log.d("MyOpenGLRenderer", "Surface changed. Width=" + width
    				+ " Height=" + height);
    		gl.glViewport(0, 0, width, height);
    		gl.glMatrixMode(GL10.GL_PROJECTION);
    		gl.glLoadIdentity();
    		gl.glOrthof(0, 320, 0, 480, 1, -1);
    	}
    	
    	@Override
    	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		// #9
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