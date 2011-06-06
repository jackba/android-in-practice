package com.manning.aip.opengldemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      Button greenScreenButton = (Button) findViewById(R.id.greenscreen_button);
      greenScreenButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(Main.this, OpenGLGreenScreenActivity.class));
         }
      });

      Button triangleButton = (Button) findViewById(R.id.triangle_button);
      triangleButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(Main.this, OpenGLTriangleActivity.class));
         }
      });
      
      Button pyramidButton = (Button) findViewById(R.id.pyramid_button);
      pyramidButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(Main.this, OpenGLPyramidActivity.class));
         }
      });
      
      Button colouredPyramidButton = (Button) findViewById(R.id.colouredpyramid_button);
      colouredPyramidButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(Main.this, OpenGLColouredPyramidActivity.class));
         }
      });
      
      Button texturedPyramidButton = (Button) findViewById(R.id.texturedpyramid_button);
      texturedPyramidButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(Main.this, OpenGLTexturedPyramidActivity.class));
         }
      });

   }
}