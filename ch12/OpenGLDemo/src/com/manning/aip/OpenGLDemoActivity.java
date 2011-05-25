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
	
	private GLSurfaceView glView;
	
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
    	}
    	
    	@Override
    	public void onSurfaceCreated(GL10 gl, EGLConfig config) {		// #9
    		Log.d("MyOpenGLRenderer", "Surface created");
    	}

    	@Override
		public void onDrawFrame(GL10 gl) {								// #10
			gl.glClearColor(0.0f, 0.5f, 0.0f, 1f);						// #11
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);						// #12
		}
    }
}