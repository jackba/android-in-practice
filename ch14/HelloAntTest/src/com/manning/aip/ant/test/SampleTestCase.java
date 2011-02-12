package com.manning.aip.ant.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.manning.aip.ant.HelloAnt;
import com.manning.aip.ant.R;

public class SampleTestCase extends ActivityUnitTestCase<HelloAnt> {

   public SampleTestCase() {
      super(HelloAnt.class);
   }

   public void testHelloViewExists() {
      startActivity(new Intent(), null, null);
      assertNull(getActivity().findViewById(R.id.hello));
   }

}
