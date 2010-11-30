package com.manning.aip.dealdroid.test;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;

import com.manning.aip.dealdroid.DealDetails;
import com.manning.aip.dealdroid.DealDroidApp;
import com.manning.aip.dealdroid.R;
import com.manning.aip.dealdroid.model.Item;

public class DealDetailsTest extends ActivityUnitTestCase<DealDetails> {

   private Item testItem;

   public DealDetailsTest() {
      super(DealDetails.class);
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();

      testItem = new Item();
      testItem.setItemId(1);
      testItem.setTitle("Test Item");
      testItem.setConvertedCurrentPrice("1");
      testItem.setLocation("USA");
      testItem.setDealUrl("http://example.com");

      DealDroidApp application = new DealDroidApp();
      application.setCurrentItem(testItem);
      setApplication(application);
   }

   public void testPreConditions() {
      startActivity(new Intent(getInstrumentation().getTargetContext(),
               DealDetails.class), null, null);

      Activity activity = getActivity();
      assertNotNull(activity.findViewById(R.id.details_price));
      assertNotNull(activity.findViewById(R.id.details_title));
      assertNotNull(activity.findViewById(R.id.details_location));
   }

   public void testThatAllFieldsAreSetCorrectly() {
      startActivity(new Intent(getInstrumentation().getTargetContext(),
               DealDetails.class), null, null);

      assertEquals("$" + testItem.getConvertedCurrentPrice(),
               getViewText(R.id.details_price));
      assertEquals(testItem.getTitle(), getViewText(R.id.details_title));
      assertEquals(testItem.getLocation(), getViewText(R.id.details_location));
   }

   public void testThatItemCanBeDisplayedInBrowser() {
      startActivity(new Intent(getInstrumentation().getTargetContext(),
               DealDetails.class), null, null);

      getInstrumentation().invokeMenuActionSync(getActivity(),
               DealDetails.MENU_BROWSE, 0);

      Intent browserIntent = getStartedActivityIntent();
      assertEquals(Intent.ACTION_VIEW, browserIntent.getAction());
      assertEquals(testItem.getDealUrl(), browserIntent.getDataString());

      //      IntentFilter browserStarted = new IntentFilter(Intent.ACTION_VIEW);
      //      //browserStarted.addDataScheme("http");
      //      //browserStarted.addDataAuthority("example.com", "80");
      //      ActivityMonitor monitor =
      //               getInstrumentation().addMonitor(browserStarted, null, true);
      //      getInstrumentation().waitForIdleSync();
      //
      //      assertTrue(getInstrumentation().checkMonitorHit(monitor, 1));
   }

   private String getViewText(int textViewId) {
      return ((TextView) getActivity().findViewById(textViewId)).getText()
               .toString();
   }
}
