package com.manning.aip.maven.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.manning.aip.maven.MavenPoweredActivity;
import com.manning.aip.maven.R;

public class LonelyTestCase extends
         ActivityInstrumentationTestCase2<MavenPoweredActivity> {

   private Solo solo;

   public LonelyTestCase() {
      super(MavenPoweredActivity.class);
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      solo = new Solo(getInstrumentation(), getActivity());
   }

   public void testHelloWorld() {
      TextView helloView =
               solo.getText(getActivity().getString(R.string.hello));
      assertNotNull(helloView);
   }
}
