package com.manning.aip.mymoviesdatabase;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MovieDetail extends Activity {

   TextView output;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      // TODO form and use parser
      super.onCreate(savedInstanceState);
      setContentView(R.layout.movie_detail);

     output = (TextView) findViewById(R.id.movie_detail_output);
   }
   

}
