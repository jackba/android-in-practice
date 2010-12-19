package com.manning.aip.dealdroid.test;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.manning.aip.dealdroid.DealDetails;
import com.manning.aip.dealdroid.DealList;

public class DealListRobotiumTest extends
         ActivityInstrumentationTestCase2<DealList> {

   private Solo solo;

   public DealListRobotiumTest() {
      super("com.manning.aip.dealdroid", DealList.class);
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      solo = new Solo(getInstrumentation(), getActivity());
   }

   public void testDealListToDetailsWithListChangeUserFlow() throws Exception {
      DealList dealList = getActivity();
      dealList.getParseFeedTask().waitAndUpdate();

      getInstrumentation().waitForIdleSync();

      solo.clickInList(0);
      solo.assertCurrentActivity("expected DealDetails", DealDetails.class);
      solo.goBack();
      solo.assertCurrentActivity("expected DealList", DealList.class);
      solo.pressSpinnerItem(0, 2);
      solo.scrollDown();
      solo.clickInList(dealList.getItems().size() - 1);
      solo.assertCurrentActivity("expected DealDetails", DealDetails.class);
   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
   }
}
