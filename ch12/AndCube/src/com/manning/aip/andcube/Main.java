package com.manning.aip.andcube;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity implements OnClickListener {

   private GLSurfaceView surfaceColors;
   private GLSurfaceView surfaceTextures;

   private CubeColors cubeColors;
   private CubeTextures cubeTextures;
   
   private Button singleColorsButton;
   private Button singleTexturesButton;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      surfaceColors = (GLSurfaceView) findViewById(R.id.surfaceColors);
      surfaceTextures = (GLSurfaceView) findViewById(R.id.surfaceTextures);

      singleColorsButton = (Button) findViewById(R.id.singleColorsButton);
      singleTexturesButton = (Button) findViewById(R.id.singleTexturesButton); 
      singleColorsButton.setOnClickListener(this);
      singleTexturesButton.setOnClickListener(this);
      
      cubeColors = new CubeColors();
      cubeTextures = new CubeTextures();

      surfaceColors.setRenderer(new ShapeRenderer(true, cubeColors, this));
      surfaceTextures.setRenderer(new ShapeRenderer(true, cubeTextures, this));      
   }

   @Override
   public void onClick(View v) {
      Intent i = new Intent(this, DisplaySingle.class);
      if (v.equals(singleColorsButton)) {
         i.putExtra("KEY", 0);
      } else if (v.equals(singleTexturesButton)) {
         i.putExtra("KEY", 1);
      }
      startActivity(i);      
   }   
}