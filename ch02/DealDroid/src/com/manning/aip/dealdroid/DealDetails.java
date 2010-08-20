package com.manning.aip.dealdroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manning.aip.dealdroid.model.Item;

public class DealDetails extends Activity {

   private static final int NONE = 0;
   private static final int MAIL = 1;
   private static final int BROWSE = 2;
   private static final int SHARE = 3;

   private DealDroidApp app;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.dealdetails);

      app = (DealDroidApp) getApplication();

      Item item = app.currentItem;

      if (item != null) {
         //Bitmap bitmap = app.imageCache.get(item.itemId);
         Bitmap bitmap = app.retrieveBitmap(item.pic175Url);
         ImageView icon = (ImageView) findViewById(R.id.details_icon);
         icon.setImageBitmap(bitmap);

         TextView title = (TextView) findViewById(R.id.details_title);
         title.setText(item.title);         

         CharSequence pricePrefix = getText(R.string.deal_details_price_prefix);
         TextView price = (TextView) findViewById(R.id.details_price);
         price.setText(pricePrefix + item.convertedCurrentPrice);

         TextView msrp = (TextView) findViewById(R.id.details_msrp);
         msrp.setText(item.msrp);

         TextView quantity = (TextView) findViewById(R.id.details_quantity);
         quantity.setText(Integer.toString(item.quantity));
         
         TextView quantitySold = (TextView) findViewById(R.id.details_quantity_sold);
         quantitySold.setText(Integer.toString(item.quantitySold));

         TextView location = (TextView) findViewById(R.id.details_location);
         location.setText(item.location);

      } else {
         Toast.makeText(this, "Error, no current item selected, nothing to see here", Toast.LENGTH_LONG).show();
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(DealDetails.NONE, DealDetails.MAIL, DealDetails.NONE, R.string.deal_details_mail_menu);
      menu.add(DealDetails.NONE, DealDetails.BROWSE, DealDetails.NONE, R.string.deal_details_browser_menu);
      menu.add(DealDetails.NONE, DealDetails.SHARE, DealDetails.NONE, R.string.deal_details_share_menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case MAIL:
            mailDeal();
            break;
         case BROWSE:
            openDealInBrowser();
            break;
         case SHARE:
            shareDeal();
            break;
      }
      return false;
   }

   private void mailDeal() {
      Intent i = new Intent(Intent.ACTION_SEND);
      i.setType("text/html");
      i.putExtra(Intent.EXTRA_SUBJECT, "Subject:");
      i.putExtra(Intent.EXTRA_TEXT, createMailMessage());
      try {
         startActivity(Intent.createChooser(i, "Send mail..."));
      } catch (android.content.ActivityNotFoundException ex) {
         Toast.makeText(DealDetails.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
      }
   }

   private void openDealInBrowser() {
      Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(app.currentItem.dealUrl));
      startActivity(i);
   }

   private void shareDeal() {
      Intent i = new Intent(Intent.ACTION_SEND);
      i.setType("text/*");
      i.putExtra(Intent.EXTRA_SUBJECT, "Subject:");
      i.putExtra(Intent.EXTRA_TEXT, createMailMessage());
      startActivity(Intent.createChooser(i, "Share options"));
   }

   // TODO not i18n'd
   private String createMailMessage() {
      Item item = app.currentItem;
      StringBuffer sb = new StringBuffer();
      sb.append("Check out this deal:\n");
      sb.append("\nTitle:" + item.title);
      sb.append("\nPrice:" + item.convertedCurrentPrice);
      sb.append("\nLocation:" + item.location);
      sb.append("\nQuantity:" + item.quantity);
      sb.append("\nURL:" + item.dealUrl);
      return sb.toString();
   }
}
