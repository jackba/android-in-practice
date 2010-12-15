package com.manning.aip.dealdroid.test;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import android.test.mock.MockContext;

import com.manning.aip.dealdroid.export.TextFileExporter;
import com.manning.aip.dealdroid.model.Item;

public class TextFileExporterTest extends TestCase {

   private List<Item> deals = new ArrayList<Item>();
   private int itemsWritten = 0;

   private class MockOutput extends FileOutputStream {

      public MockOutput() throws FileNotFoundException {
         super(FileDescriptor.out);
      }

      @Override
      public void write(byte[] buffer) throws IOException {
         Item currentItem = deals.get(itemsWritten++);
         assertEquals(currentItem.toString(), new String(buffer));
      }
   }

   private class MyMockContext extends MockContext {
      @Override
      public FileOutputStream openFileOutput(String name, int mode)
         throws FileNotFoundException {
         return new MockOutput();
      }
   }

   @Override
   protected void setUp() throws Exception {
      super.setUp();

      Item item1 = new Item();
      item1.setTitle("test item 1");
      deals.add(item1);

      Item item2 = new Item();
      item2.setTitle("test item 2");
      deals.add(item2);
   }

   public void testShouldExportItems() throws IOException {

      new TextFileExporter(new MyMockContext(), deals).export();

      assertEquals(2, itemsWritten);
   }
}
