package com.manning.aip.dealdroid.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.widget.TextView;

import com.manning.aip.dealdroid.DealDetails;
import com.manning.aip.dealdroid.DealDroidApp;
import com.manning.aip.dealdroid.R;
import com.manning.aip.dealdroid.model.Item;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowActivity;
import com.xtremelabs.robolectric.view.TestMenuItem;

@RunWith(RobolectricTestRunner.class)
public class DealDetailsRobolectricTest {

   private DealDetails activity;

   private Item testItem;

   @Before
   public void setUp() {
      testItem = new Item();
      testItem.setItemId(1);
      testItem.setTitle("Test Item");
      testItem.setConvertedCurrentPrice("1");
      testItem.setLocation("USA");
      testItem.setDealUrl("http://example.com");

      activity = new DealDetails();
      DealDroidApp application = (DealDroidApp) activity.getApplication();
      application.setCurrentItem(testItem);

      activity.onCreate(null);
   }

   @Test
   public void testPreConditions() {
      assertNotNull(activity.findViewById(R.id.details_price));
      assertNotNull(activity.findViewById(R.id.details_title));
      assertNotNull(activity.findViewById(R.id.details_location));
   }

   @Test
   public void testThatAllFieldsAreSetCorrectly() {
      assertEquals("$" + testItem.getConvertedCurrentPrice(),
               getViewText(R.id.details_price));
      assertEquals(testItem.getTitle(), getViewText(R.id.details_title));
      assertEquals(testItem.getLocation(), getViewText(R.id.details_location));
   }

   @Test
   public void testThatItemCanBeDisplayedInBrowser() {
      activity.onOptionsItemSelected(new TestMenuItem() {
         public int getItemId() {
            return DealDetails.MENU_BROWSE;
         }
      });

      ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
      Intent startedIntent = shadowActivity.getNextStartedActivity();

      assertEquals(Intent.ACTION_VIEW, startedIntent.getAction());
      assertEquals(testItem.getDealUrl(), startedIntent.getData().toString());
   }

   private String getViewText(int textViewId) {
      return ((TextView) activity.findViewById(textViewId)).getText()
               .toString();
   }
}
