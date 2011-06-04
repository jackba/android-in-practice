package com.manning.aip.canvasdemo;

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

      Button randColorButton = (Button) findViewById(R.id.randcolor_button);
      randColorButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(Main.this, Canvas2DRandomColorActivity.class));
         }
      });

      Button randColorFullScreenButton = (Button) findViewById(R.id.randcolor_fullscreen_button);
      randColorFullScreenButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(Main.this, Canvas2DRandomColorFullScreenActivity.class));
         }
      });

      Button randomShapesButton = (Button) findViewById(R.id.randomshapes_button);
      randomShapesButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(Main.this, Canvas2DRandomShapesActivity.class));
         }
      });

      Button randomShapesAlphaButton = (Button) findViewById(R.id.randomshapes_alpha_button);
      randomShapesAlphaButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(Main.this, Canvas2DRandomShapesWithAlphaActivity.class));
         }
      });
      
      Button randomShapesRedrawButton = (Button) findViewById(R.id.randomshapes_redraw_button);
      randomShapesRedrawButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(Main.this, Canvas2DRandomShapesRedrawActivity.class));
         }
      });
   }

}