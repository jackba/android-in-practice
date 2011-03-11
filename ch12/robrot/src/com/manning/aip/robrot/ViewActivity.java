package com.manning.aip.robrot;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.totsp.robrot.R;

public class ViewActivity extends Activity {

   private MandelbrotView brot = null;
   private Button reset = null;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      this.brot = (MandelbrotView) this.findViewById(R.id.brot);
      this.reset = (Button) this.findViewById(R.id.reset);
      this.reset.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(View v) {
            brot.reset();
         }

      });
   }

   @Override
   protected void onPause() {
      if (brot != null) {
         brot.cancel();
      }
      super.onPause();
   }

   @Override
   protected void onResume() {
      if (brot != null) {
         brot.start();
      }
      super.onResume();
   }
}