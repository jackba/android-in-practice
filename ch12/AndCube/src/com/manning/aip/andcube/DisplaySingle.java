package com.manning.aip.andcube;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class DisplaySingle extends Activity {

   private GLSurfaceView surface;
   private Shape shape;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.single);

      surface = (GLSurfaceView) findViewById(R.id.surface);
      
      int shapeKey = this.getIntent().getIntExtra("KEY", 0);
      switch (shapeKey) {
         case 0:
            shape = new CubeColors();
            break;
         case 1:
            shape = new CubeTextures();
            break;
      }

      surface.setRenderer(new ShapeRenderer(true, shape, this));
   }
}