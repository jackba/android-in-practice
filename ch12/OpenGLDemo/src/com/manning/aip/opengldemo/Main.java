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

   }
}