package com.manning.aip.maven;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;


public class HelloMaven extends Activity {

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      // StringUtils is an external library dependency
      String toast = StringUtils.repeat("Hello Maven! ", 3);
      Toast.makeText(this, toast, Toast.LENGTH_LONG).show();
   }
}