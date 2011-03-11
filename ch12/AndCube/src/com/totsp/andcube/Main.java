package com.totsp.andcube;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class Main extends Activity {
    
	private GLSurfaceView surfaceView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceView = new GLSurfaceView(this);
        setContentView(surfaceView);
        Cube cube = new Cube();
        surfaceView.setRenderer(new ShapeRenderer(true, cube, this));
       
    }
}