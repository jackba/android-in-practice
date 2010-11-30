package com.manning.aip.dealdroid.test;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.ApplicationTestCase;
import android.test.MoreAsserts;

import com.manning.aip.dealdroid.DealDroidApp;
import com.manning.aip.dealdroid.R;

public class DealDroidAppTest extends ApplicationTestCase<DealDroidApp> {

   private DealDroidApp dealdroid;

   public DealDroidAppTest() {
      super(DealDroidApp.class);
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      createApplication();
      dealdroid = getApplication();
   }

   public void testShouldInitializeInstances() {
      assertNotNull(dealdroid.getSectionList());
      assertNotNull(dealdroid.getImageCache());
      assertNotNull(dealdroid.getParser());
   }

   public void testShouldStartWithEmptySections() {
      assertTrue(dealdroid.getSectionList().isEmpty());
      assertNull(dealdroid.getCurrentItem());
   }

   public void testCorrectProjectProperties() throws NameNotFoundException {
      PackageInfo info =
               dealdroid.getPackageManager().getPackageInfo(
                        dealdroid.getPackageName(), 0);

      assertEquals(R.drawable.ddicon, info.applicationInfo.icon);

      MoreAsserts.assertMatchesRegex("\\d\\.\\d", info.versionName);
   }
}
