package com.manning.aip.andcube;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class Main extends Activity {

   private GLSurfaceView surfaceColors;
   private GLSurfaceView surfaceTextures;

   private CubeColors cubeColors;
   private CubeTextures cubeTextures;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      surfaceColors = (GLSurfaceView) findViewById(R.id.surfaceColors);
      surfaceTextures = (GLSurfaceView) findViewById(R.id.surfaceTextures);

      cubeColors = new CubeColors();
      cubeTextures = new CubeTextures();

      surfaceColors.setRenderer(new ShapeRenderer(true, cubeColors, this));
      surfaceTextures.setRenderer(new ShapeRenderer(true, cubeTextures, this));
   }
}